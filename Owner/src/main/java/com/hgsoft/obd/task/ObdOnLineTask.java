package com.hgsoft.obd.task;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.pool.HandlerThreadPool;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.service.ObdService;
import com.hgsoft.obd.util.DriveTimeUtil;

/**
 * obd设备在线定时任务
 * 
 * @author sujunguang 2016年5月18日 上午9:50:14
 */
@WebListener
public class ObdOnLineTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final OBDStockInfoService obdStockInfoService = (OBDStockInfoService) context.getBean("obdStockInfoService");
		final ObdService obdService = (ObdService) context.getBean("obdService");
		final DriveTimeUtil driveTimeUtil = (DriveTimeUtil) context.getBean("driveTimeUtil");
		final JedisServiceUtil jedisServiceUtil = (JedisServiceUtil) context.getBean("jedisServiceUtil");
		// TODO 是否全部下线的开关 0-开 1-关
		String allOffLine = PropertiesUtil.getInstance("owner.properties").readProperty("allOffLine", "0");
		obdOnLineTaskLogger.info("是否全部下线的开关 0-开 1-关:"+allOffLine);
		if ("0".equals(allOffLine)) {
			obdStockInfoService.obdAllOffLine();
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				obdOnLineTaskLogger.info("【是否在线状态更新】开始");
				long begin = System.currentTimeMillis();
				try {
					/**
					 * 获取当前设备连接的channel，会存在设备离线channel清理完成或者清理不完成的情况
						所以增加记录上线时间的设备，如果设备有上线过，则判断是否超时离线，如果超时离线，则
						清除对应记录。
					 */
					Set<String> obdSnSet = new HashSet<>();
					Set<String> obdChannelSnSet = GlobalData.OBD_CHANNEL.keySet();
					Set<String> obdOnLineSnSet = GlobalData.OBD_OnLine_Time.keySet();
					obdSnSet.addAll(obdChannelSnSet);
					obdSnSet.addAll(obdOnLineSnSet);
					for (String obdSn : obdSnSet) {
						final String _obdSn = obdSn;
						HandlerThreadPool.ObdLinePool.execute(new Runnable() {
							@Override
							public void run() {
								boolean onLine = obdService.obdOnLine(_obdSn);
								obdOnLineTaskLogger.info(_obdSn+",是否在线:" + onLine);
								try {
									if(onLine){
										driveTimeUtil.cleanStartRestByRedis(_obdSn);//清除开始休息的时间
										obdStockInfoService.obdSetOnLine(_obdSn);
										//记录设备上线时间
										jedisServiceUtil.putHSetByRedis(ObdRedisData.OBD_LastOnLine_KEY, _obdSn, String.valueOf(new Date().getTime()));
									}else{
										obdStockInfoService.obdSetOffLine(_obdSn);
										if(GlobalData.OBD_OnLine_Time.containsKey(_obdSn)){
											GlobalData.OBD_OnLine_Time.remove(_obdSn);
										}
									}
								} catch (Exception e) {
									obdOnLineTaskLogger.error("【是否在线状态更新】异常！",e);
								}
							}
						});
					}
				} catch (Exception e) {
					obdOnLineTaskLogger.error("【是否在线状态更新】异常！",e);
				} finally{
					obdOnLineTaskLogger.info("【是否在线状态更新】结束");
					obdOnLineTaskLogger.info("【是否在线状态更新】耗时->"+(System.currentTimeMillis()-begin));
				}
			}
		};

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 10, 40, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
	
}
