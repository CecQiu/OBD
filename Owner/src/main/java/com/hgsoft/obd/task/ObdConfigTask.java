package com.hgsoft.obd.task;

import java.util.HashMap;
import java.util.Map;
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

import com.hgsoft.carowner.entity.BatchSet;
import com.hgsoft.carowner.entity.ObdBatchSet;
import com.hgsoft.carowner.service.BatchSetService;
import com.hgsoft.carowner.service.ObdBatchSetService;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.ExtensionDataSetType;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;

import net.sf.json.JSONObject;

/**
 * OBD配置参数下发定时任务
 *
 * @author sjg
 * @version  [版本号, 2016年12月26日]
 */
@WebListener
public class ObdConfigTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final ObdBatchSetService obdBatchSetService = (ObdBatchSetService) context.getBean("obdBatchSetService");;
		final BatchSetService batchSetService = (BatchSetService) context.getBean("batchSetService");;
		final SendUtil sendUtil = (SendUtil) context.getBean("sendUtil");
		final ServerSettingService serverSettingService = (ServerSettingService) context.getBean("serverSettingService");

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				obdOnLineTaskLogger.info("【OBD配置参数下发定时任务】开始");
				long begin = System.currentTimeMillis();
				try {
					Set<String> obdSnSet = GlobalData.OBD_CHANNEL.keySet();
					for (String obdSn : obdSnSet) {
						if(GlobalData.getChannelByObdSn(obdSn) == null){//不在线
							continue;
						}
						ObdBatchSet obdBatchSet = obdBatchSetService.queryObdBatchSetValidNotSuccess(obdSn);
						if(obdBatchSet != null){
							obdOnLineTaskLogger.info("<"+obdSn+">【OBD配置参数下发定时任务】列表:"+obdBatchSet);
							String version = obdBatchSet.getVersion();
							Map<String,Object> map2 = new HashMap<String, Object>();
							map2.put("valid", 1);//有效（未删除）
							map2.put("auditState", 1);//审核通过
							map2.put("version", version);//版本号
							BatchSet batchSet = batchSetService.queryLastByParams(map2);
							if(batchSet != null){
								obdOnLineTaskLogger.info("<"+obdSn+">【OBD配置参数下发定时任务】配置信息:"+batchSet);
								if(obdBatchSet.getSendedCount() >= GlobalData.configSendCount){//超过下发次数，不下发
									obdOnLineTaskLogger.info("<"+obdSn+">【OBD配置参数下发定时任务】超过下发次数，不下发!");
									return;
								}
								String msgBody = batchSet.getBodyMsg();
								Map<ExtensionDataSetType, String> extensionDataSetTypes = new HashMap<ExtensionDataSetType, String>();
								extensionDataSetTypes.put(ExtensionDataSetType.EEPROMFLASH, msgBody);
								String result = serverSettingService.extensionDataSetting(obdSn, extensionDataSetTypes);
								obdOnLineTaskLogger.info("<"+obdSn+">【OBD配置参数下发定时任务】下发结果:"+result);
								obdBatchSet.setSendedCount(obdBatchSet.getSendedCount()+1);
								if(!StringUtils.isEmpty(result) && GlobalData.isSendResultSuccess(result.toString())){
									obdOnLineTaskLogger.info("<"+obdSn+">【OBD配置参数下发定时任务】下发成功!");
									obdBatchSet.setSuccess(1);
								}
								obdBatchSetService.obdBatchSetUpdate(obdBatchSet);
							}
						}
					}
				} catch (Exception e) {
					obdOnLineTaskLogger.error("【OBD配置参数下发定时任务】异常！",e);
				} finally{
					obdOnLineTaskLogger.info("【OBD配置参数下发定时任务】结束");
					obdOnLineTaskLogger.info("【OBD配置参数下发定时任务】耗时->"+(System.currentTimeMillis()-begin));
				}
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 40, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
