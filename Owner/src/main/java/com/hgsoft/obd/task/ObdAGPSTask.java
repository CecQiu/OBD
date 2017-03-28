package com.hgsoft.obd.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.entity.ObdGroupAGPS;
import com.hgsoft.carowner.service.ObdGroupAGPSService;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.AGPSHandlerService;

/**
 * AGPS定时更新内存数据
 *
 * @author sjg
 * @version  [版本号, 2016年12月26日]
 */
@WebListener
public class ObdAGPSTask implements ServletContextListener {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		final ObdGroupService obdGroupService = (ObdGroupService) context.getBean("obdGroupService");;
		final ObdGroupAGPSService obdGroupAGPSService = (ObdGroupAGPSService) context.getBean("obdGroupAGPSService");;
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				obdOnLineTaskLogger.info("【定时更新AGPS内存数据】开始");
				long begin = System.currentTimeMillis();
				try {
					List<ObdGroup> obdGroups = obdGroupService.queryList();
					for (ObdGroup obdGroup : obdGroups) {
						String groupNum = obdGroup.getGroupNum();
						ObdGroupAGPS obdGroupAGPS = obdGroupAGPSService.queryByGroupNum(groupNum);
						if(obdGroupAGPS != null){
							AGPSHandlerService.agpsGroupDatas.put(groupNum, obdGroupAGPS);
						}
					}
				} catch (Exception e) {
					obdOnLineTaskLogger.error("【定时更新AGPS内存数据】异常！",e);
				} finally{
					obdOnLineTaskLogger.info("【定时更新AGPS内存数据】结束");
					obdOnLineTaskLogger.info("【定时更新AGPS内存数据】耗时->"+(System.currentTimeMillis()-begin));
				}
			}
		};
		if(GlobalData.agpsInMemorySwitch){//开关
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.MINUTES);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
