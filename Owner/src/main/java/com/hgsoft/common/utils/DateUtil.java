package com.hgsoft.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

@SuppressWarnings("deprecation")
public class DateUtil {
	
	/**
	 * 获取当前时间 精确到秒 格式为 yyyy-MM-dd HH:mm:ss
	 * @return 字符串类型的当前时间
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		return sdf.format(d);
	}

	/**
	 * 判断是否润年
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLeapYear(Date date) {
		if (date == null)
			return false;

		final SimpleDateFormat sd = new SimpleDateFormat("yyyy");
		String year = sd.format(date);
		int __year = Integer.parseInt(year);
		if ((__year % 4 == 0) && (__year % 100 != 0) || (__year % 400 == 0)) {
			return true;
		} else {
			return false;

		}
	}

	/**
	 * 获取传入日期的上一个月份起始至截止范围 传入 2012-01-01 返回 2011-12-01 00:00:00 和 2011-12-31
	 * 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static List<String> obtainPreMonthRange(Date date) {
		if (date == null)
			return null;

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");

		List<String> ranges = new ArrayList<String>(2);
		// 上个月
		final Date preMonth = DateUtils.addMonths(date, -1);
		Calendar c = Calendar.getInstance();
		c.setTime(preMonth);
		int month = c.get(Calendar.MONTH) + 1;
		String __day = "";
		String startTime = "";
		String endTime = "";
		// 润年
		if (month == 2) {
			if (isLeapYear(preMonth)) {
				__day = "29";

			} else {
				__day = "28";

			}
		} else if (month == 1 || month == 3 || month == 5 || month == 7
				|| month == 8 || month == 10 || month == 12) {
			__day = "31";
		} else {
			__day = "30";
		}
		startTime = sd.format(preMonth) + "-01 00:00:00";
		endTime = sd.format(preMonth) + "-" + __day + " 23:59:59";
		ranges.add(startTime);
		ranges.add(endTime);

		return ranges;
	}

	public static Object fromatDate(Object date, String formatType) {
		SimpleDateFormat sd = new SimpleDateFormat(formatType);
		if (date instanceof Date) {
			return sd.format((Date) date);
		} else if (date instanceof String) {
			try {
				return sd.parse((String) date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}

	public static Date addYear(int year, Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		String str = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
				+ "-" + c.get(Calendar.DATE) + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public static Date addMonth(Date dt,int monthCount) { SimpleDateFormat
	 * sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss"); Calendar c =
	 * Calendar.getInstance(); c.setTime(dt); c.add(Calendar.MONTH, monthCount);
	 * String
	 * date=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar
	 * .DATE)+" "+dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds(); try {
	 * return sdf.parse(date); } catch (ParseException e) { e.printStackTrace();
	 * } return null; }
	 */

	public static String addMonth(Date dt, int monthCount) {
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.MONTH, monthCount);

		String month = (c.get(Calendar.MONTH) + 1) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}

		String date = c.get(Calendar.DATE) + "";
		if (date.length() == 1) {
			date = "0" + date;
		}

		String hour = dt.getHours() + "";
		if (hour.length() == 1) {
			hour = "0" + hour;
		}

		String min = dt.getMinutes() + "";
		if (min.length() == 1) {
			min = "0" + min;
		}

		String sec = dt.getSeconds() + "";
		if (sec.length() == 1) {
			sec = "0" + sec;
		}

		String fulldate = c.get(Calendar.YEAR) + "-" + month + "-" + date + " "
				+ hour + ":" + min + ":" + sec;
		return fulldate;
	}

	/**
	 * 判断一个日期是否在一个时间段里面（不包含）
	 * @param obj 日期Date or Calendar
	 * @param bHours 开始小时
	 * @param bMinutes 开始分钟
	 * @param bSeconds 开始秒钟
	 * @param eHours 结束小时
	 * @param eMinutes 结束分钟
	 * @param eSeconds 结束秒钟
	 * @return
	 */
	public static boolean exitsBetweenTime(Object obj,int bHours,int bMinutes,int bSeconds,
		int eHours,int eMinutes,int eSeconds){
		Calendar calendar = null;
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		if(obj instanceof Date){
			calendar = Calendar.getInstance();
			calendar.setTime((Date)obj);
			
		} else if(obj instanceof Calendar){
			calendar = (Calendar)obj;
		}
		
		calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		calendar1.set(Calendar.HOUR_OF_DAY, bHours);
		calendar1.set(Calendar.MINUTE, bMinutes);
		calendar1.set(Calendar.SECOND, bSeconds);
		
		calendar2.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		calendar2.set(Calendar.HOUR_OF_DAY, eHours);
		calendar2.set(Calendar.MINUTE, eMinutes);
		calendar2.set(Calendar.SECOND, eSeconds);
		
		if(calendar.after(calendar1)&& calendar.before(calendar2) ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断日期是属于星期几
	 * @param obj 日期Date or Calendar
	 * @return
	 */
	public static String dayOfWeek(Object obj){
		Calendar calendar = null;
		if(obj instanceof Date){
			calendar = Calendar.getInstance();
			calendar.setTime((Date)obj);
		} else if(obj instanceof Calendar){
			calendar = (Calendar)obj;
		} else{
			return null;
		}
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch (weekDay) {
		case Calendar.MONDAY:
			 str = "一";
			break;
		case Calendar.TUESDAY:
			 str = "二";
			break;
		case Calendar.WEDNESDAY:
			 str = "三";
			break;
		case Calendar.THURSDAY:
			 str = "四";
			break;
		case Calendar.FRIDAY:
			 str = "五";
			break;
		case Calendar.SATURDAY:
			 str = "六";
			break;
		case Calendar.SUNDAY:
			 str = "日";
			break;
		default:
			break;
		}
		return str;
	}
	
	/**
	 * 获取一个时间段之间的每天集合
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception 
	 */
	public static Set<String> getTimesByBetwwenTime(Date startTime,Date endTime,String pattern) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startTime);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endTime);
		if(c2.before(c1)){
			throw new Exception("endTime must not before startTime!");
		}
		Set<String> list = new LinkedHashSet<String>();
		while(c1.before(c2)){
//			System.out.println(c1.getTime());
//			System.out.println(c2.getTime());
			if(sdf.format(c1.getTime()).equals(c2.getTime())){
//				System.out.println(0);
				break;
			}else{
				Date tmp = c1.getTime();
				list.add(sdf.format(tmp));
				c1.add(Calendar.DAY_OF_YEAR, 1);
			}
			
		}
		list.add(sdf.format(c2.getTime()));
		return list;
	}
	
	/**
	 * 计算两个时间之间跨越的长度
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static String getTwoDateLong(Date beginDate,Date endDate){
		String str = "";
		if(beginDate.after(endDate)){
			try {
				throw new Exception("开始日期不能晚于结束日期！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			long millis = endDate.getTime()-beginDate.getTime();
			Calendar calendar = Calendar.getInstance();
			//原始日期：1970-01-01 08:00:00
			calendar.setTimeInMillis(millis);
			System.out.println(calendar.getTime());
			int year = calendar.get(Calendar.YEAR)-1970;
			int month = calendar.get(Calendar.MONTH);//0-11
			int day =  calendar.get(Calendar.DAY_OF_YEAR)-1;
			int hour = calendar.get(Calendar.HOUR_OF_DAY)-8;
			int minute =  calendar.get(Calendar.MINUTE);
			int second =  calendar.get(Calendar.SECOND);
			int millisecond =  calendar.get(Calendar.MILLISECOND);
			
			if(year != 0){
				str += year+" 年";
			}
			if(month != 0){
				str += month+" 月";
			}
			if(day != 0){
				str += day+" 日";
			}
			if(hour != 0){
				str += hour+" 时";
			}
			if(minute != 0){
				str += minute+" 分";
			}
			if(second != 0){
				str += second+" 秒";
			}
			if(millisecond != 0){
				str += millisecond+" 毫秒";
			}
		}
		
		return str;
	}
	
	/**
	 * 根据传进来的日期对象和，日期格式，获取日期的字符串
	 * @return 字符串类型的当前时间
	 */
	public static String getTimeString(Date date,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 
	 * @param now
	 * @param efence
	 * @return 0不在日期内，1在日期外,-1异常
	 */
	public static int dateBetween(Date now,Date beginDate, Date endDate){
		try {
			if(now.after(beginDate) && now.before(endDate)){
				return 1;
			}else{
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 将毫秒数转成 小时：分：秒格式
	 * @param time
	 * @return
	 */
	public static String hms(long time){
    	long hour = time/(60*60*1000);
    	long minute = (time - hour*60*60*1000)/(60*1000);
    	long second = (time - hour*60*60*1000 - minute*60*1000)/1000;
    	if(second >= 60 )
    	{
    	  second = second % 60;
    	  minute+=second/60;
    	}
    	if(minute >= 60)
    	{
    	  minute = minute %60;
    	  hour += minute/60;
    	}
    	String sh = "";
    	String sm ="";
    	String ss = "";
    	if(hour <10)
    	{
    	   sh = "0" + String.valueOf(hour);
    	}else
    	{
    	   sh = String.valueOf(hour);
    	}
    	if(minute <10)
    	{
    	   sm = "0" + String.valueOf(minute);
    	}else
    	{
    	   sm = String.valueOf(minute);
    	}
    	if(second <10)
    	{
    	   ss = "0" + String.valueOf(second);
    	}else
    	{
    	   ss = String.valueOf(second);
    	}
    	String hmsString = sh +"小时"+ sm +"分"+ ss+"秒";
//    	System.out.println(hmsString);
    	return hmsString;
    }

	/**
	 * 获取日期这个月的最后一天
	 * @param sDate1
	 * @return
	 */
	public static Date getLastDayOfMonth(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}
	/**
	 * 下几个月的今天
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public static Date nextIntervalMonthDay(Date date,int interval) throws Exception{
		if( interval <= 0){
			throw new Exception("非法参数值interval:"+interval);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, interval);
		return c.getTime();
	}
	
	/**
	 * 间隔大于多长(月)
	 * @param begin 开始
	 * @param end 结束
	 * @param interval 几个
	 * @return
	 * @throws Exception 
	 */
	public static boolean gtIntervalMonth(Date begin, Date end, int interval) throws Exception{
		Date nextMonth = nextIntervalMonthDay(begin,interval);
		if(end.getTime() > nextMonth.getTime()){
			return true;
		}
		return false;
	}
	/**
	 * 获得24小时整点时间
	 * @param date
	 * @return
	 */
	public static List<Date> get24HoursFull(Date date){
		List<Date> lists = new ArrayList<>();
		String dateStr = (String) DateUtil.fromatDate(date, "yyyy-MM-dd");
		for (int i = 0; i <= 24; i++) {
			String dateStrTmp = dateStr;
			if(i < 10){
				dateStrTmp += " 0"+i;
			}else{
				dateStrTmp += " "+i;
			}
			dateStrTmp += ":00:00";
			Date dateTmp = (Date)DateUtil.fromatDate(dateStrTmp, "yyyy-MM-dd HH:00:00");
			lists.add(dateTmp);
		}
		return lists;
	}
	
	public static void main(String[] args) throws ParseException {
//		String date = "2015-08-19 23:59:59";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Date now = new Date();
//		Date now = sdf.parse(date);
//		String enddate = sdf.format(now);
//		System.out.println(enddate);
//		String startdate = DateUtil.addMonth(now, -1);
//		System.out.println(startdate);
//		startdate = DateUtil.addMonth(now, -3);
//		System.out.println(startdate);
//		startdate = DateUtil.addMonth(now, -6);
//		System.out.println(startdate);
//		try {
//			System.out.println(getTimesByBetwwenTime(sdf.parse( "2015-08-19 11:59:59"),sdf.parse(date),"yyyy-MM-dd"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Calendar cc = Calendar.getInstance();
//		cc.setTime(new Date());
//		System.out.println(sdf.format(cc.getTime()));
//		
//		Date d1 = sdf.parse("2015-08-19 10:10:20");
//		Date d2 = sdf.parse("2020-09-22 20:10:10");
//		System.out.println("相距："+getTwoDateLong(d1,d2));
		Date date = new Date();
		String str=getTimeString(date,"yyMMddHHmmss");
		System.out.println(str);
	}

}