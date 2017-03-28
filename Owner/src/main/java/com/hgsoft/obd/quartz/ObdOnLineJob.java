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
 * OBD上线任务统计
 * @author sujunguang
 * 2016年10月10日
 * 上午9:25:19
 */
public class ObdOnLineJob {
	
	private static Logger obdOnLineTaskLogger = LogManager.getLogger("obdOnLineTaskLogger");

	@Resource
	private ObdReportsService obdReportsService;
	
	public void run() {
		obdOnLineTaskLogger.info("----【设备上线量报表统计任务】----");
		obdOnLineTaskLogger.info("----【设备上线量报表统计任务】开始");
		long begin = System.currentTimeMillis();
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterDay = calendar.getTime();
		String date = (String) DateUtil.fromatDate(yesterDay, "yyyy-MM-dd");//默认时间为前一天
		obdReportsService.generateReportsData(ReportsType.OnLine, date, date);
		obdOnLineTaskLogger.info("----【设备上线量报表统计任务】耗时："+( System.currentTimeMillis() - begin));
		obdOnLineTaskLogger.info("----【设备上线量报表统计任务】结束");

	}
	
	public static void main(String[] args) {
		Date now = new Date();
		System.out.println(DateUtil.get24HoursFull(now));
		String nowStr = (String) DateUtil.fromatDate(now, "yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		System.out.println(nowStr);
		System.out.println(calendar.getTime());
		List<Date> dates = DateUtil.get24HoursFull(now);
		Date tmp = null;
		int ii = 0;
		/*for (int i = 0; i < dates.size(); i=i/2,i++) {
			if(i == 0) tmp = dates.get(i);
			System.out.println(tmp);
			ii++;
			tmp = dates.get(ii);
			System.out.println(tmp);
			if(ii == dates.size()-1) break;
			System.out.println();
		}*/
		for (int i = 1; i < dates.size(); i++) {
			System.out.println(i);
			System.out.println(dates.get(i-1));
			System.out.println(dates.get(i));
		}
	}
	
}
