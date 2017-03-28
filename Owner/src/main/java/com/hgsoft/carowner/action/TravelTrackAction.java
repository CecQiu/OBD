package com.hgsoft.carowner.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;

/**
 * 行程数据查询及导出
 * 
 * @author lsw
 *
 */
@Controller
@Scope("prototype")
public class TravelTrackAction extends BaseAction {

	// private CarTraveltrack carTraveltrack;
	private String obdSn;
	private String obdMSn;
	private String startTime;
	private String endTime;
	
	private String quickenNum;
	private String quickSlowDown;
	private String voltageFlag;
	private String voltage;
	private String driverTimeFlag;
	private String driverTime;
	private String distanceFlag;
	private String distance;
	private String type;
	
	private List<CarTraveltrack> carTraveltracks;
	
	@Resource
	private CarTraveltrackService carTraveltrackService;

	/*
	 * 返回行程查询列表
	 */
	public List<CarTraveltrack> getCarTravelTrackList(Pager pager) {

		String obdSN = "";//表面号解析成设备号
		if (!StringUtils.isEmpty(obdMSn)) {
			//将表面号解析成设备号
			try {
				obdSN = StrUtil.obdSnChange(obdMSn);// 设备号
				if(StringUtils.isEmpty(obdSN)){
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdSN)){
			if(!obdSn.equals(obdSN)){
				return null;
			}else{
				obdSN = "";
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		Integer total=0;

		if (!StringUtils.isEmpty(obdSn)) {
			map.put("obdSn", obdSn);
			total++;
		}
		if(!StringUtils.isEmpty(obdSN)){
			map.put("obdSn", obdSN);
			total++;
		}

		if (!StringUtils.isEmpty(quickenNum)) {
			map.put("quickenNum", Long.parseLong(quickenNum));
			total++;
		}
		
		if (!StringUtils.isEmpty(quickSlowDown)) {
			map.put("quickSlowDown", Integer.parseInt(quickSlowDown));
			total++;
		}
		
		if (!StringUtils.isEmpty(voltage)) {
			map.put("voltage", Double.parseDouble(voltage));
			map.put("voltageFlag", voltageFlag);
			total++;
		}
		
		if (!StringUtils.isEmpty(driverTime)) {
			map.put("driverTime", Integer.parseInt(driverTime));
			map.put("driverTimeFlag", driverTimeFlag);
			total++;
		}
		
		if (!StringUtils.isEmpty(distance)) {
			map.put("distance", Long.parseLong(distance));
			map.put("distanceFlag", distanceFlag);
			total++;
		}
		if (!StringUtils.isEmpty(type)) {
			map.put("type", Integer.parseInt(type));
			total++;
		}
		
		if (!StringUtils.isEmpty(startTime)) {
			map.put("startTime", startTime);
			total++;
		}
		
		if (!StringUtils.isEmpty(endTime)) {
			map.put("endTime", endTime);
			total++;
		}
		
		map.put("paramsTotal", total);

		return carTraveltrackService.queryByParams(pager, map);

	}

	/*
	 * 返回行程查询结果
	 */
	public String travelTrackSearch() {

		if (StrUtil.arraySubNotNull(obdSn,obdMSn,quickenNum,quickSlowDown,voltage,driverTime,distance,type,startTime,endTime)) {
			carTraveltracks = getCarTravelTrackList(pager);
		}else{
			//设置默认时间
			Calendar c = Calendar.getInstance();
	    	Date now = c.getTime();
	    	endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
	    	c.add(Calendar.MONTH, -1);
	    	Date s= c.getTime();
	    	startTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
		}

		return "travelTrackSearch";
	}

	/*
	 * 导出excel
	 */
	public String exportExcel() {

		String[] headers = { "ID", "设备号", "导入时间", "行程结束", "行程序号", "行程开始", "距离",
				"最大速度", "超速次数", "急刹车次数", "急转弯次数", "急加速次数", "急减速次数", "急变道次数",
				"怠速次数", "发动机最高水温", "发动机最高工作转速", "发动机最高工作转速次数", "车速转速不匹配次数",
				"电压值", "总油耗", "平均油耗", "疲劳驾驶时长", "行程类型", "报文" };
		String[] cloumn = { "id", "obdsn", "insesrtTime", "travelEnd",
				"travelNo", "travelStart", "distance", "speed",
				"overspeedTime", "brakesNum", "quickTurn", "quickenNum",
				"quickSlowDown", "quickLaneChange", "idling", "temperature",
				"rotationalSpeed", "engineMaxSpeed", "speedMismatch",
				"voltage", "totalFuel", "averageFuel", "driverTime", "type",
				"message" };
		String fileName = "行程数据表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+ fileName;
		
		List<CarTraveltrack> carTraveltracks = getCarTravelTrackList(null);
		
		List<HashMap<String, Object>> lists = new ArrayList<>();
		for (CarTraveltrack carTrack : carTraveltracks) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", carTrack.getId());
			map.put("obdsn", carTrack.getObdsn());
			map.put("insesrtTime", carTrack.getInsesrtTime());
			map.put("travelEnd", carTrack.getTravelEnd());
			map.put("travelNo", carTrack.getTravelNo());
			map.put("travelStart", carTrack.getTravelStart());
			map.put("distance", carTrack.getDistance());
			map.put("speed", carTrack.getSpeed());
			map.put("overspeedTime", carTrack.getOverspeedTime());
			map.put("brakesNum", carTrack.getBrakesNum());
			map.put("quickTurn", carTrack.getQuickTurn());
			map.put("quickenNum", carTrack.getQuickenNum());
			map.put("quickSlowDown", carTrack.getQuickSlowDown());
			map.put("quickLaneChange", carTrack.getQuickLaneChange());
			map.put("idling", carTrack.getIdling());
			map.put("temperature", carTrack.getTemperature());
			map.put("rotationalSpeed", carTrack.getRotationalSpeed());
			map.put("engineMaxSpeed", carTrack.getEngineMaxSpeed());
			map.put("speedMismatch", carTrack.getSpeedMismatch());
			map.put("voltage", carTrack.getVoltage());
			map.put("totalFuel", carTrack.getTotalFuel());
			map.put("averageFuel", carTrack.getAverageFuel());
			map.put("driverTime", carTrack.getDriverTime());
			map.put("type", carTrack.getType());
			map.put("message", carTrack.getMessage());
			lists.add(map);
		}

		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("行程数据表", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<CarTraveltrack> getCarTraveltracks() {
		return carTraveltracks;
	}

	public void setCarTraveltracks(List<CarTraveltrack> carTraveltracks) {
		this.carTraveltracks = carTraveltracks;
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getObdMSn() {
		return obdMSn;
	}

	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}

	public String getQuickenNum() {
		return quickenNum;
	}

	public void setQuickenNum(String quickenNum) {
		this.quickenNum = quickenNum;
	}

	public String getQuickSlowDown() {
		return quickSlowDown;
	}

	public void setQuickSlowDown(String quickSlowDown) {
		this.quickSlowDown = quickSlowDown;
	}

	public String getVoltageFlag() {
		return voltageFlag;
	}

	public void setVoltageFlag(String voltageFlag) {
		this.voltageFlag = voltageFlag;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getDriverTimeFlag() {
		return driverTimeFlag;
	}

	public void setDriverTimeFlag(String driverTimeFlag) {
		this.driverTimeFlag = driverTimeFlag;
	}

	public String getDriverTime() {
		return driverTime;
	}

	public void setDriverTime(String driverTime) {
		this.driverTime = driverTime;
	}

	public String getDistanceFlag() {
		return distanceFlag;
	}

	public void setDistanceFlag(String distanceFlag) {
		this.distanceFlag = distanceFlag;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
