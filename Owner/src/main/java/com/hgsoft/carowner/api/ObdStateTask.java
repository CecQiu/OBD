package com.hgsoft.carowner.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.message.RunningData;

//@WebListener
public class ObdStateTask implements ServletContextListener{
		private final Log logger = LogFactory.getLog(ObdStateTask.class);
		 
		@Override
		public void contextInitialized(ServletContextEvent sce) {
			logger.info("*******************更新obd状态.");
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
			final OBDStockInfoService obdStockInfoService = (OBDStockInfoService) context.getBean("obdStockInfoService");
			List<OBDStockInfo> osList=obdStockInfoService.findAll();
			for (OBDStockInfo obdStockInfo : osList) {
				RunningData.getObdStateMap().put(obdStockInfo.getObdMSn().toLowerCase(), new Date());//设备状态日期初始化
			}
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					System.out.println("################################");
					long now = System.currentTimeMillis();
					Map<String,Date> obdStateMap =RunningData.getObdStateMap();
					for(String key : obdStateMap.keySet()){
						Date date = obdStateMap.get(key);
//						OBDStockInfo obd=obdStockInfoService.queryBySN(key);
						OBDStockInfo obd = obdStockInfoService.queryByObdMSN(key);// 设备号
						if(obd==null){
							continue;
						}
						if(now-date.getTime()>120*1000){
							if("01".equals(obd.getStockState())||"02".equals(obd.getStockState())){
								obd.setStockState("00");
								obdStockInfoService.obdStateUpdate(obd);
							}
						}else{
							if(!"01".equals(obd.getStockState())){
								obd.setStockState("01");
								obdStockInfoService.obdStateUpdate(obd);
							}
							
						}
					}
				}
			};
			
	 		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	 		
			service.scheduleAtFixedRate(runnable, 120,120, TimeUnit.SECONDS);
			System.out.println("#########end");
		}
		
		@Override
		public void contextDestroyed(ServletContextEvent sce) {
			
		}
		
}
