package com.hgsoft.carowner.api;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.obd.handler.MessageHandlerThread;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.UnfinishedTaskService;

//@WebListener
public class ObdUnhandledTask implements ServletContextListener{
	private static Logger serverSendObdLogger = LogManager.getLogger("serverSendObdLogger");
	
		@Override
		public void contextInitialized(ServletContextEvent sce) {
			final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
			final UnfinishedTaskService unfinishedTaskService = (UnfinishedTaskService) context.getBean("unfinishedTaskService");
			
			serverSendObdLogger.info("--------------【未完成的下发操作进行下发】定时任务初始化-----");
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						serverSendObdLogger.info("handler count:"+MessageHandlerThread.count);
						serverSendObdLogger.info("--------------【未完成的下发操作进行下发】定时任务开始-----");
						 Set<String> obdSnSet = GlobalData.OBD_CHANNEL.keySet();
						 for (String obdSn : obdSnSet) {
							 unfinishedTaskService.handle(obdSn);
						}
					} catch (Exception e) {
						serverSendObdLogger.info("【未完成的下发操作进行下发】异常！"+e);
					}
				}
			};
			
	 		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	 		
			service.scheduleAtFixedRate(runnable, 10,40, TimeUnit.SECONDS);
			serverSendObdLogger.info("--------------【未完成的下发操作进行下发】定时任务初始化结束-----");
		}
		
		@Override
		public void contextDestroyed(ServletContextEvent sce) {
			
		}
		
}
