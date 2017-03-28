package com.hgsoft.obd.task;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.carowner.entity.ObdTestSendPacket;
import com.hgsoft.carowner.service.ObdTestSendPacketService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;

/**
 * obd测试下发报文任务
 * @author sujunguang
 * 2016年5月24日
 * 上午11:24:33
 */
@WebListener
public class ObdTestSendPacketTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	private static Integer testSendCount = Integer.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("testSendCount", "3"));
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final ObdTestSendPacketService obdTestSendPacketService = (ObdTestSendPacketService) context.getBean("obdTestSendPacketService");
		final SendUtil sendUtil = (SendUtil) context.getBean("sendUtil");
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					obdOnLineTaskLogger.info("【测试下发报文】开始");
					long begin = System.currentTimeMillis();
					try {
						Set<String> obdSnSet = GlobalData.OBD_CHANNEL.keySet();
						for (String obdSn : obdSnSet) {
							if(GlobalData.getChannelByObdSn(obdSn) == null){//不在线
								continue;
							}
							List<ObdTestSendPacket> ObdTestSendPackets = obdTestSendPacketService.queryByObdSn(obdSn);
							for (ObdTestSendPacket obdTestSendPacket : ObdTestSendPackets) {
								Integer sendCount = obdTestSendPacket.getSendCount();
								if(sendCount >= testSendCount){
									continue;
								}
								String typeName = obdTestSendPacket.getTypeStr();
								String[] types = typeName.split(":");
								Object resultObj = null;
								if(types.length >= 2 && !StringUtils.isEmpty(types[1])){
									String key = obdSn + ObdConstants.keySpilt + types[1];
									resultObj = sendUtil.msgSendGetResult(obdSn, SerialNumberUtil.getSerialnumber(obdSn), obdTestSendPacket.getMsgBody(), key, 20.0);
									if(typeName.endsWith("extensionDataQuery:DomainBlack") || typeName.endsWith("extensionDataQuery:DomainWhite")){
										//黑白名单特殊获取
										resultObj = GlobalData.getQueryDataMap(obdSn);
									}
								}else{
									resultObj = sendUtil.msgSendGetResult(obdSn, SerialNumberUtil.getSerialnumber(obdSn), obdTestSendPacket.getMsgBody(),20.0);	
								}
								String result = (resultObj == null ? "": resultObj.toString());
								obdOnLineTaskLogger.info("【测试下发报文】设备:"+obdSn+"，结果->"+result);
								//更新
								obdTestSendPacket.setSendCount(sendCount+1);
								if(!StringUtils.isEmpty(result)){
									obdTestSendPacket.setSended(1);
									obdTestSendPacket.setResult(result);
									obdTestSendPacket.setUpdateTime(new Date());
								}
								obdTestSendPacketService.sendPacketUpdate(obdTestSendPacket);
							}
						}
					} catch (Exception e) {
						obdOnLineTaskLogger.error("【测试下发报文】异常！",e);
					} finally{
						obdOnLineTaskLogger.info("【测试下发报文】结束");
						obdOnLineTaskLogger.info("【测试下发报文】耗时->"+(System.currentTimeMillis()-begin));
					}
					
				}
			};
	
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(runnable, 10, 45, TimeUnit.SECONDS);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
