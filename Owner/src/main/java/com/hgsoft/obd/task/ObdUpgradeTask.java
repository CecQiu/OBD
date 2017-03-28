package com.hgsoft.obd.task;

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

import com.hgsoft.obd.pool.HandlerThreadPool;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.FotaHandleService;
import com.hgsoft.obd.service.UpgradeSetHandlerService;

/**
 * OBD设备下发升级
 * @author sujunguang
 * 2016年9月1日
 * 下午4:56:11
 */
@WebListener
public class ObdUpgradeTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final FotaHandleService fotaHandleService = (FotaHandleService) context.getBean("fotaHandleService");
		final UpgradeSetHandlerService upgradeSetHandlerService = (UpgradeSetHandlerService) context.getBean("upgradeSetHandlerService");

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				obdOnLineTaskLogger.info("【设备相关升级】开始");
				long begin = System.currentTimeMillis();
				try {
					Set<String> obdSnSet = GlobalData.OBD_CHANNEL.keySet();
					for (final String obdSn : obdSnSet) {
						//下发FOTA待升级
						HandlerThreadPool.UpgradeHandlerPool.execute(new Runnable() {
							@Override
							public void run() {
								fotaHandleService.fotaSetSend(obdSn);
//								obdOnLineTaskLogger.info("【设备相关升级】设备：<"+ obdSn + ">下发FOTA待升级");
								//下发固件待升级
								upgradeSetHandlerService.upgradeSetHandler(obdSn);
//								obdOnLineTaskLogger.info("【设备相关升级】设备：<"+ obdSn + ">下发固件待升级");
							}
						});
					}
				} catch (Exception e) {
					obdOnLineTaskLogger.error("【设备相关升级】异常！",e);
				} finally{
					obdOnLineTaskLogger.info("【设备相关升级】结束");
					obdOnLineTaskLogger.info("【设备相关升级】耗时->"+(System.currentTimeMillis()-begin));
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
