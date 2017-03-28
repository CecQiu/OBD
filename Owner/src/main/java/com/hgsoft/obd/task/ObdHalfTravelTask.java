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

import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerRequestQueryService;

/**
 * obd设备定时查询半条行程
 * @author sujunguang
 * 2016年5月24日
 * 上午11:24:33
 */
@WebListener
public class ObdHalfTravelTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final ServerRequestQueryService serverRequestQueryService = (ServerRequestQueryService) context.getBean("serverRequestQueryService");
		// 是否开启定时查询半条行程开关 0-开 1-关
		String isQueryHalfTravel = PropertiesUtil.getInstance("owner.properties").readProperty("isQueryHalfTravel", "1");
		//查询频率 ，默认10分钟
		String queryHalfTravelTime = PropertiesUtil.getInstance("owner.properties").readProperty("QueryHalfTravelTime", "10");
		obdOnLineTaskLogger.info("是否开启定时查询半条行程开关 0-开 1-关:"+isQueryHalfTravel);
		if ("0".equals(isQueryHalfTravel)) {
		
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					obdOnLineTaskLogger.info("【下发查询半条行程】开始");
					long begin = System.currentTimeMillis();
					try {
						Set<String> obdSnSet = GlobalData.OBD_CHANNEL.keySet();
						for (String obdSn : obdSnSet) {
							String result = serverRequestQueryService.halfTravel(obdSn);
							obdOnLineTaskLogger.info("下发查询半条行程，设备:"+obdSn+"->"+result);
						}
					} catch (Exception e) {
						obdOnLineTaskLogger.error("【下发查询半条行程】异常！",e);
					} finally{
						obdOnLineTaskLogger.info("【下发查询半条行程】结束");
						obdOnLineTaskLogger.info("【下发查询半条行程】耗时->"+(System.currentTimeMillis()-begin));
					}
					
				}
			};
	
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(runnable, 10, Integer.valueOf(queryHalfTravelTime)*60, TimeUnit.SECONDS);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
 
}
