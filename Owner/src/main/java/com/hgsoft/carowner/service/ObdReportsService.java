/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdReportsDao;
import com.hgsoft.carowner.entity.ObdReports;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.util.ReportsType;

@Service
public class ObdReportsService extends BaseService<ObdReports>{
	
	@Resource
	private ObdReportsDao obdReportsDao;
	@Resource
	private ObdHandShakeService obdHandShakeService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	@Resource
	public void setDao(ObdReportsDao obdReportsDao){
		super.setDao(obdReportsDao);
	}
	
	public void obdReportsSave(ObdReports obdReports){
		obdReportsDao.obdReportsSave(obdReports);	
	}
	
	public ObdReports queryByDate(Date date){
		return obdReportsDao.queryByDate(date);
	}
	
	public List<ObdReports> queryByDate(Date begin, Date end, Integer type, ReportsType reportsType) throws OBDException{
		List<ObdReports> obdReportsList = obdReportsDao.queryByDate(begin, end, type);
		if(obdReportsList == null || obdReportsList.size() == 0){//查询不到数据，则去生成相应的记录。
			generateReports(begin, reportsType);
			obdReportsList = obdReportsDao.queryByDate(begin, end, type);
		}
		return obdReportsList;
	}
	
	/**
	 * 根据时间来生成报表
	 * @param date
	 * @throws OBDException 
	 */
	public void generateReports(Date date, ReportsType reportsType) throws OBDException{
		int type = -1;
		switch (reportsType) {
		case OnLine:
			type = 0;
			break;
		case Active:
			type = 2;
			break;
		default:
			throw new OBDException("类型不匹配："+reportsType);
		}
		/**
		 * 天统计
		 */
		String dayStr = (String)DateUtil.fromatDate(date, "yyyy-MM-dd");
		Date day = (Date)DateUtil.fromatDate(dayStr, "yyyy-MM-dd");
		ObdReports obdReportsDay = queryByDate(day);
		if(obdReportsDay == null || obdReportsDay.getType().intValue() != type+1){
			obdReportsDay = new ObdReports();
			obdReportsDay.setCreateTime(new Date());
			obdReportsDay.setDate(date);
			obdReportsDay.setType(type+1);
			Date dayS = (Date)DateUtil.fromatDate(dayStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date dayE = (Date)DateUtil.fromatDate(dayStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			List count = null;
			if(reportsType == ReportsType.OnLine){
				count = obdHandShakeService.find(dayS, dayE);
			}
			if(reportsType == ReportsType.Active){
				count =obdStockInfoService.find(dayS, dayE);
			}
			obdReportsDay.setCount(count.size());
			obdReportsSave(obdReportsDay);
		}
		/**
		 * 小时统计
		 */
		List<Date> dates = DateUtil.get24HoursFull(date);
		for (int i = 1; i < dates.size(); i++) {
			Date dateTmp = dates.get(i-1);
			ObdReports obdReports = queryByDate(dateTmp);
			if(obdReports == null || obdReportsDay.getType().intValue() != type){
				obdReports = new ObdReports();
				obdReports.setCreateTime(new Date());
				obdReports.setDate(dateTmp);
				obdReports.setType(type);
				List count = null;
				if(reportsType == ReportsType.OnLine){
					count = obdHandShakeService.find(dates.get(i-1), dates.get(i));
				}
				if(reportsType == ReportsType.Active){
					count =obdStockInfoService.find(dates.get(i-1), dates.get(i));
				}
				obdReports.setCount(count.size());
				obdReportsSave(obdReports);
			}
		}
	}

	/**
	 * 生成报表数据
	 * @param reportsType
	 * @param begin
	 * @param end
	 * @return
	 */
	public JSONArray generateReportsData(ReportsType reportsType,String begin, String end){
		int dataType = -1;
		switch (reportsType) {
		case OnLine:
			dataType = 0;
			break;
		case Active:
			dataType = 2;
			break;
		}
		JSONArray jarr= new JSONArray();
		JSONObject json1 = new JSONObject();
		try {
			if(!StringUtils.isEmpty(begin) && !StringUtils.isEmpty(end)){
				Date beginDate = (Date)DateUtil.fromatDate(begin, "yyyy-MM-dd");
				Date endDate = (Date)DateUtil.fromatDate(end, "yyyy-MM-dd");
				List<ObdReports> obdReports = null;
				List<Integer> counts = new ArrayList<>(); 
				List<String> dateSets = null;
				int type = 0;
				Calendar c = Calendar.getInstance();
				 long oneDay = 24*60*60*1000;
				if(DateUtil.gtIntervalMonth(beginDate, endDate, 12)){//大于一年
					//闭区间多一年
					c.setTime(endDate);
					c.add(Calendar.YEAR, 1);
					endDate = c.getTime();
					dateSets = new ArrayList<>(DateUtil.getTimesByBetwwenTime(beginDate, endDate, "yyyy"));
					type = 1;
				}else if(DateUtil.gtIntervalMonth(beginDate, endDate, 1)){//大于一个月
					//闭区间多一月
					c.setTime(endDate);
					c.add(Calendar.MONTH, 1);
					endDate = c.getTime();
					dateSets = new ArrayList<>(DateUtil.getTimesByBetwwenTime(beginDate, endDate, "yyyy-MM"));
					type = 2;
				}else if(endDate.getTime() - beginDate.getTime() >= oneDay){//超过1天
					//闭区间多一天
					endDate.setTime(endDate.getTime()+oneDay);
					dateSets = new ArrayList<>(DateUtil.getTimesByBetwwenTime(beginDate, endDate, "yyyy-MM-dd"));
					type = 3;
				}else{
					json1.put("name", "obd:"+begin);
					//
					obdReports = queryByDate((Date)DateUtil.fromatDate(begin+" 00:00:00", "yyyy-MM-dd HH:mm:ss"), 
							(Date)DateUtil.fromatDate(end+" 23:59:59", "yyyy-MM-dd HH:mm:ss"),dataType, reportsType);
					for (ObdReports obdReport : obdReports) {
						counts.add(obdReport.getCount());
					}
				}
				
				if(dateSets != null){//大于1天的(天，月，年)
					json1.put("dateSets", dateSets);
					String pattern = "";
					switch (type) {
					case 1:
						pattern = "yyyy";
						break;
					case 2:
						pattern = "yyyy-MM";
						break;
					case 3:
						pattern = "yyyy-MM-dd";
						break;
					default:
						break;
					}
					int dateSetsSize = dateSets.size();
					if(dateSetsSize >= 2){
						json1.put("name", "obd:"+dateSets.get(0) + " - " + dateSets.get(dateSetsSize-2));
					}
					for (int i = 1; i < dateSets.size(); i++) {
						String beginTmp = dateSets.get(i-1);
						String endTmp = dateSets.get(i);
						Date beginD = (Date)DateUtil.fromatDate(beginTmp, pattern);
						Date endD = (Date)DateUtil.fromatDate(endTmp, pattern);
						obdReports = queryByDate(beginD, endD, dataType+1, reportsType);
						Integer count = 0;
						for (ObdReports obdReport : obdReports) {
							count += obdReport.getCount();
						}
						counts.add(count);
					}
				}
				
				json1.put("data", JSONArray.fromObject(counts));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jarr.add(json1);
		return jarr;
	}
	
}
