package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.entity.ObdReports;
import com.hgsoft.carowner.service.ObdReportsService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.obd.util.ReportsType;

/**
 * 报表类
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ReportsAction extends BaseAction {
	private String begin;
	private String end;
	private static long oneDay = 24*60*60*1000;
	private static long oneMonth = 30*24*60*60*1000;
	private static long oneYear = 365*30*24*60*60*1000;
	
	@Resource
	private ObdReportsService obdReportsService;
	
	// 列表展示
	public String toObdOnline() {
		initDate();
		return "obdOnline";
	}

	public String 	toObdActive(){
		initDate();
		return "obdActive";
	}
	
	public void initDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterDay = calendar.getTime();
		begin = end = (String) DateUtil.fromatDate(yesterDay, "yyyy-MM-dd");//默认时间为前一天
	}
	
	public void onlineData(){
		JSONArray  jsonArray = obdReportsService.generateReportsData(ReportsType.OnLine, begin, end);
		if(jsonArray != null) outJsonMessage(jsonArray.toString());
	}
	
	public void onActiveData(){
		JSONArray  jsonArray = obdReportsService.generateReportsData(ReportsType.Active, begin, end);
		if(jsonArray != null) outJsonMessage(jsonArray.toString());
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public static void main(String[] args) throws Exception {
		Date beginDate = (Date)DateUtil.fromatDate("2016-09-10" + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date endDate = (Date)DateUtil.fromatDate("2016-10-10" + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

		System.out.println(DateUtil.getTimesByBetwwenTime(
				beginDate , endDate
				, "yyyy"));
//		long oneDay = 24*60*60*1000;
//		endDate.setTime(endDate.getTime()+oneDay);
//		List<String> dateSets = new ArrayList<>(DateUtil.getTimesByBetwwenTime(beginDate, endDate, "yyy-MM-dd"));
//
//		for (int i = 1; i < dateSets.size(); i++) {
//			String begin = dateSets.get(i-1);
//			String end = dateSets.get(i);
//			System.out.println(begin + " - " + end);
//		}
//		System.out.println(dateSets.size());
//		Date beginDate = (Date)DateUtil.fromatDate("2016-02-29" + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//		Calendar c = Calendar.getInstance();
//		c.setTime(beginDate);
//		c.add(Calendar.MONTH, 1);
//		System.out.println(c.getTime());
		Date d = (Date)DateUtil.fromatDate("2016-09", "yyyy-MM");
		System.out.println(DateUtil.getLastDayOfMonth(d));
	}
}
