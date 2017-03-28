package com.hgsoft.obd.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hgsoft.carowner.service.ObdReportsService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.util.ReportsType;

/**
 * OBD注册激活统计
 * @author sujunguang
 * 2016年10月12日
 * 下午3:20:40
 */
public class ObdActiveJob {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");

	@Resource
	private ObdReportsService obdReportsService;
	
	public void run() {
		obdOnLineTaskLogger.info("----【设备激活量报表统计任务】----");
		obdOnLineTaskLogger.info("----【设备激活量报表统计任务】开始");
		long begin = System.currentTimeMillis();
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterDay = calendar.getTime();
		String date = (String) DateUtil.fromatDate(yesterDay, "yyyy-MM-dd");//默认时间为前一天
		obdReportsService.generateReportsData(ReportsType.Active, date, date);
		obdOnLineTaskLogger.info("----【设备激活量报表统计任务】耗时："+( System.currentTimeMillis() - begin));
		obdOnLineTaskLogger.info("----【设备激活量报表统计任务】结束");
	}
	
}
