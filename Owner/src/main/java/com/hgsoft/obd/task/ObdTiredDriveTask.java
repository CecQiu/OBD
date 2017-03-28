package com.hgsoft.obd.task;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.pool.HandlerThreadPool;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.service.ObdService;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.WarnType;

/**
 *  obd设备疲劳驾驶定时任务
 * @author sujunguang
 * 2016年5月26日
 * 上午10:30:36
 */
@WebListener
public class ObdTiredDriveTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final DriveTimeUtil driveTimeUtil = (DriveTimeUtil) context.getBean("driveTimeUtil");
		final ObdService obdService = (ObdService) context.getBean("obdService");
		final PushApi pushApi = (PushApi) context.getBean("pushApi");
		final ObdSateService obdSateService = (ObdSateService) context.getBean("obdSateService");
		final OBDStockInfoService obdStockInfoService = (OBDStockInfoService) context.getBean("obdStockInfoService");
		final JedisServiceUtil jedisServiceUtil = (JedisServiceUtil) context.getBean("jedisServiceUtil");

		//当前主机数量
		final Integer hostCounts = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("hostCounts", "1"));
		//当前主机编号
		final Integer hostNo = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("hostNo", "1"));
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				obdOnLineTaskLogger.info("【驾驶疲劳定时任务】开始");
				long begin = System.currentTimeMillis();
				try {
					List<OBDStockInfo> obdStockInfos = obdStockInfoService.getObdInfo(null, null, null, "1", null);
					Set<String> obdSns = new HashSet<String>();
					for (OBDStockInfo obdStockInfo : obdStockInfos) {
						String obdSn = obdStockInfo.getObdSn();
						String onlineTimeStr = jedisServiceUtil.getHSetByRedis(ObdRedisData.OBD_LastOnLine_KEY,obdSn);
						boolean noOver = false;
						if(onlineTimeStr != null && !"".equals(onlineTimeStr)){
							long onlineTime = Long.valueOf(onlineTimeStr);
							noOver = new Date().getTime() - onlineTime <= GlobalData.tiredDriveTime;
						}
						if(!StringUtils.isEmpty(obdSn) && Math.abs(obdSn.hashCode() % hostCounts) == hostNo-1){//集群多机器，需要负载来处理
							if(noOver){
								obdSns.add(obdSn);
							}
						}
					}
//					Map<String,String> map = driveTimeUtil.hGetAllTiredDrive();
//					Set<String>  driveTimeFieldSet =map.keySet();//驾驶记录
//					for (String driveTimeField : driveTimeFieldSet) {
//						String obdSn = "";
//						if(driveTimeField.contains(ObdRedisData.OBD_DriveTime_DrivingField)){
//							obdSn = driveTimeField.split(ObdRedisData.OBD_DriveTime_DrivingField)[1];
//						}
//						if(driveTimeField.contains(ObdRedisData.OBD_DriveTime_RestingField)){
//							obdSn = driveTimeField.split(ObdRedisData.OBD_DriveTime_RestingField)[1];
//						}
//						if(StringUtils.isEmpty(obdSn)){
//							continue;
//						}
//						if(Math.abs(obdSn.hashCode() % hostCounts) == hostNo-1){//集群多机器，需要负载来处理
//							obdSns.add(obdSn);
//						}
//					}
					obdOnLineTaskLogger.info("【驾驶疲劳定时任务】设备数量："+obdSns.size());
					for(String _obdSn : obdSns){
						final String obdSn = _obdSn;
						
						Date now = new Date();
						final boolean onLine = obdService.obdOnLine(obdSn);//在线
						final boolean tired = driveTimeUtil.isTiredByRedis(obdSn, now);//疲劳（发生）
						final boolean rested = driveTimeUtil.isRestedByRedis(obdSn, now);//休息（消除疲劳）
						final boolean driving = driveTimeUtil.isDrivingByRedis(obdSn);//正在行使
						//全false不打印日志,不继续执行
						if(!onLine && !tired && !rested){
							continue;
						}
						
						HandlerThreadPool.ObdTiredDrivePool.execute(new Runnable(){
							@Override
							public void run() {
								
							try {
								obdOnLineTaskLogger.info("【驾驶疲劳定时任务】设备："+obdSn);
								obdOnLineTaskLogger.info("<"+obdSn+">【驾驶疲劳定时任务】是否已疲劳："+tired);
								obdOnLineTaskLogger.info("<"+obdSn+">【驾驶疲劳定时任务】是否休息完："+rested);
								obdOnLineTaskLogger.info("<"+obdSn+">【驾驶疲劳定时任务】是否在线："+onLine);
								obdOnLineTaskLogger.info("<"+obdSn+">【驾驶疲劳定时任务】是否正在行使："+driving);
								final boolean noSleepOver = driveTimeUtil.isNoSleepOver(obdSn);
								
								if(onLine && tired && driving){//在线，发生驾驶疲劳
									obdOnLineTaskLogger.info("【驾驶疲劳定时任务】到达发生驾驶疲劳条件："+obdSn);
									if(!driveTimeUtil.sendedTiredByRedis(obdSn)  && !noSleepOver){//已经发送了疲劳驾驶/休眠时间还没达到,不需要再发
										ObdState obdState = obdSateService.queryByObdSn(obdSn);
										if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
												&& "1".equals(obdState.getFatigueDriveSwitch())){
											obdOnLineTaskLogger.info("<"+obdSn+">【驾驶疲劳定时任务】发生驾驶疲劳：推送发生疲劳驾驶告警");
											pushApi.pushWarnHandler(obdSn,WarnType.FatigueDrive,"14", "发生疲劳驾驶告警");
										}
										driveTimeUtil.sendTiredByRedis(obdSn);//记录发生疲劳告警
									}
								}
								if(!onLine && rested && !driving){//离线，休息足够，需要保证上次是发生疲劳驾驶-》消除驾驶疲劳
									obdOnLineTaskLogger.info("【驾驶疲劳定时任务】到达消除驾驶疲劳条件："+obdSn);
									if(driveTimeUtil.sendedTiredByRedis(obdSn)){//已经发送了疲劳驾驶才有消除疲劳的告警
										ObdState obdState = obdSateService.queryByObdSn(obdSn);
										if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
												&& "1".equals(obdState.getFatigueDriveSwitch())){
											obdOnLineTaskLogger.info("<"+obdSn+">【驾驶疲劳定时任务】消除驾驶疲劳：推送消除疲劳驾驶告警");
											pushApi.pushWarnHandler(obdSn,WarnType.FatigueDriveCancel,"00", "消除疲劳驾驶告警");
										}
										driveTimeUtil.removeSendedTiredByRedis(obdSn);//移除发生疲劳告警
									}
									
									driveTimeUtil.cleanStartRestByRedis(obdSn);
									driveTimeUtil.cleanStartDriveByRedis(obdSn);
								}
								
								//更正状态
								if(!onLine){
									//离线-》休息
									driveTimeUtil.putRestingByRedis(obdSn, "1");
									driveTimeUtil.putDrivingByRedis(obdSn, "0");
									driveTimeUtil.startRestByRedis(obdSn, new Date());//离线则记录休息时间
								}else{
									//在线-》驾驶中
									driveTimeUtil.putDrivingByRedis(obdSn, "1");
									driveTimeUtil.putRestingByRedis(obdSn, "0");
									driveTimeUtil.cleanStartRestByRedis(obdSn);//在线则消除休息时间
								}
//							
							} catch (Exception e) {
								obdOnLineTaskLogger.error("【驾驶疲劳定时任务】异常！",e);
							}
							}
							
						});
					}
					// 关闭启动线程
//					HandlerThreadPool.ObdTiredDrivePool.shutdown();
					// 等待子线程结束，再继续执行下面的代码
//					HandlerThreadPool.ObdTiredDrivePool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				} catch (Exception e) {
					obdOnLineTaskLogger.error("【驾驶疲劳定时任务】异常！",e);
				} finally{
					obdOnLineTaskLogger.info("【驾驶疲劳定时任务】结束");
					obdOnLineTaskLogger.info("【驾驶疲劳定时任务】耗时->"+(System.currentTimeMillis()-begin));
				}
			}
			
		};

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 10, 30, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	public static void main(String[] args) {
		System.out.println(Math.abs("30000824".hashCode() % 1));
		System.out.println(Math.abs("300007d4".hashCode() % 1));
		System.out.println(Math.abs("300007d5".hashCode() % 1));
		System.out.println(Math.abs("300007e5".hashCode() % 1));
		System.out.println(Math.abs("300007f5".hashCode() % 1));
	}
}
