package com.hgsoft.obd.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.service.ObdService;

/**
 * 定时清理无用数据
 *
 * @author sjg
 * @version  [版本号, 2016年12月13日]
 */
@WebListener
public class ObdCleanTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	static OBDStockInfoService obdStockInfoService;
	static ObdService obdService;
	//当前主机数量
	final Integer hostCounts = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("hostCounts", "1"));
	//当前主机编号
	final Integer hostNo = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("hostNo", "1"));

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		if(GlobalData.cleanOBDDataSwitch){//定时清除数据开关
			final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
			obdStockInfoService = (OBDStockInfoService) context.getBean("obdStockInfoService");
			final JedisServiceUtil jedisServiceUtil = (JedisServiceUtil) context.getBean("jedisServiceUtil");
			obdService = (ObdService) context.getBean("obdService");

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					obdOnLineTaskLogger.info("【清除数据定时任务】开始");
					long begin = System.currentTimeMillis();
					try {
						List<OBDStockInfo> obdStockInfos = obdStockInfoService.getObdInfo(null, null, null, "1", null);
						for (OBDStockInfo obdStockInfo : obdStockInfos) {
							String obdSn = obdStockInfo.getObdSn();
							if(!StringUtils.isEmpty(obdSn) && Math.abs(obdSn.hashCode() % hostCounts) == hostNo-1){//集群多机器，需要负载来处理
								String onlineTimeStr = jedisServiceUtil.getHSetByRedis(ObdRedisData.OBD_LastOnLine_KEY,obdSn);
								if(onlineTimeStr != null && !"".equals(onlineTimeStr)){
									long onlineTime = Long.valueOf(onlineTimeStr);
									if(new Date().getTime() - onlineTime > GlobalData.cleanOBDDataTime){
										cleanOBDData(obdSn);
									}
								}
							}
						}
					} catch (Exception e) {
						obdOnLineTaskLogger.error("【清除数据定时任务】异常！",e);
					} finally{
						obdOnLineTaskLogger.info("【清除数据定时任务】结束");
						obdOnLineTaskLogger.info("【清除数据定时任务】耗时->"+(System.currentTimeMillis()-begin));
					}
				}
			};
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(runnable, 5, 60, TimeUnit.MINUTES);
			
			//状态校正定时，3分钟
			ScheduledExecutorService checkService = Executors.newSingleThreadScheduledExecutor();
			Runnable checkRunnable = new Runnable() {
				@Override
				public void run() {
					obdOnLineTaskLogger.info("【校正在线状态漏更任务】开始");
					long begin = System.currentTimeMillis();
					try {
						checkOnLineState();
					} catch (Exception e) {
						obdOnLineTaskLogger.error("【校正在线状态漏更任务】异常！",e);
					} finally {
						obdOnLineTaskLogger.info("【校正在线状态漏更任务】结束");
						obdOnLineTaskLogger.info("【校正在线状态漏更任务】耗时->"+(System.currentTimeMillis()-begin));
					}
				}
			};
			checkService.scheduleAtFixedRate(checkRunnable, 1, 3, TimeUnit.MINUTES);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
	
	private void cleanOBDData(String obdSn){
		obdOnLineTaskLogger.info("【清除数据定时任务】设备："+obdSn);
		obdOnLineTaskLogger.info("【清除数据定时任务】清除前："+GlobalData.OBD_ACK_OR_QueryData.size());
		for (int serialNum = 0; serialNum < 255; serialNum++) {
			String key = obdSn + "_" + serialNum;
			if(GlobalData.OBD_ACK_OR_QueryData.containsKey(key)){
				GlobalData.OBD_ACK_OR_QueryData.remove(key);
			}
		}
		obdOnLineTaskLogger.info("【清除数据定时任务】清除后："+GlobalData.OBD_ACK_OR_QueryData.size());
	}
	
	/**
	 * 校正出现在线状态漏更的情况
	 */
	private void checkOnLineState(){
		List<OBDStockInfo> obdStockInfos = obdStockInfoService.getObdInfo(null, null, "01", "1", null);
		for (OBDStockInfo obdStockInfo : obdStockInfos) {
			String obdSn = obdStockInfo.getObdSn();
			if(!StringUtils.isEmpty(obdSn) && Math.abs(obdSn.hashCode() % hostCounts) == hostNo-1){//集群多机器，需要负载来处理
				boolean onLine = obdService.obdOnLine(obdSn);
				if(!onLine){//不在线，则更正状态
					obdOnLineTaskLogger.info("【校正在线状态漏更任务】设备号:"+obdSn+","+obdStockInfo);
					obdStockInfo.setStockState("00");
					obdStockInfoService.obdStateUpdate(obdStockInfo);
				}
			}
		}
		
	}
}
