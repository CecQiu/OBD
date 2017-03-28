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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.carowner.service.PositionInfoService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;
/**
 * 位置数据查询及导出
 * @author lsw
 *
 */
@Controller
@Scope("prototype")
public class PositionSearchAction extends BaseAction{

	private String obdSn;
	private String obdMSn;
	private String obdSpeed;//Obd速度
	private String engineTurns;//发动机转数
	private String engineTemperature;	//发动机水温
	private String obdSpeedFlag;//Obd速度
	private String engineTurnsFlag;//发动机转数
	private String engineTempFlag;	//发动机水温
	
	private List<PositionInfo> positionInfos;
	private String pids="";
	private String startTime;
	private String endTime;
    
	@Resource
	private PositionInfoService positionInfoService;
	
	/*
	 * 位置查询列表
	 * @return
	 */
	public String positionSearch(){
			
		  if(!StringUtils.isEmpty(startTime) ||!StringUtils.isEmpty(endTime) ||
				  !StringUtils.isEmpty(obdSn)|| !StringUtils.isEmpty(obdMSn)||
				  !StringUtils.isEmpty(obdSpeed) ||  !StringUtils.isEmpty(engineTurns) || 
				  !StringUtils.isEmpty(engineTemperature)){
		     positionInfos = getPositionInfoList(pager);
		}else{
			//设置默认时间
			Calendar c = Calendar.getInstance();
	    	Date now = c.getTime();
	    	endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
	    	c.add(Calendar.MONTH, -1);
	    	Date s= c.getTime();
	    	startTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
		}
		return "positionSearch";
	}
	
	
	/*
	 * 返回位置查询结果
	 */
	public List<PositionInfo> getPositionInfoList(Pager pager){		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		
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
		if(!StringUtils.isEmpty(obdSn)){
			map.put("obdSn", obdSn);
			total++;
		}
		if(!StringUtils.isEmpty(obdSN)){
			map.put("obdSn", obdSN);
			total++;
		}
		if(!StringUtils.isEmpty(startTime)){
			map.put("startTime", startTime);
			total++;
		}
		if(!StringUtils.isEmpty(endTime)){
			map.put("endTime", endTime);
			total++;
		}
		
		if(!StringUtils.isEmpty(obdSpeed)){
			map.put("obdSpeed", Integer.parseInt(obdSpeed));
			map.put("obdSpeedFlag", obdSpeedFlag);
			total++;
			
		}
		if(!StringUtils.isEmpty(engineTurns)){
			map.put("engineTurns", Integer.parseInt(engineTurns));
			map.put("engineTurnsFlag", engineTurnsFlag);
			total++;
		}
		if(!StringUtils.isEmpty(engineTemperature)){
			map.put("engineTemperature",Integer.parseInt(engineTemperature));
			map.put("engineTempFlag",engineTempFlag);
			total++;
		}
		map.put("paramsTotal", total);
		List<PositionInfo> pinfos=positionInfoService.queryByParams(pager,map);
		return pinfos;
		
	}
	
	
	/*
	 * 导出excel
	 */
	public String exportExcel(){
		
		String[] headers={"ID","设备号","导入时间","定位成功1-成功0-失败","定位类型","时间类型0-GPS时间1-系统时间","GPS/系统时间","经度类型","经度","纬度类型","纬度","方向角","GPS速度","OBD速度","发动机转数","发动机水温","卫星系统","卫星个数","卫星强度","网络类型","国家代码","移动网号码","系统识别码","网络识别码","基站","CDMA定位精度"};
		String[] cloumn={"id","obdSn","insertTime","statusGPS","type","timeType","time","longitudeType","longitude","latitudeType","latitude","directionAngle","gpsSpeed","obdSpeed","engineTurns","engineTemperature","satelliteSys","satellites","satelliteStrength","netType","mcc","mnc","sid","nid","bid","oprecision"}; 
		String fileName="位置数据表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		

		List<PositionInfo> positionInfos = getPositionInfoList(null);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (PositionInfo posInfo : positionInfos) {
			HashMap<String, Object> map = new  HashMap<>();
			map.put("id",posInfo.getId());
			map.put("obdSn",posInfo.getObdSn());
			map.put("insertTime",posInfo.getInsertTime());
			map.put("statusGPS",posInfo.getStatusGPS());
			map.put("type",posInfo.getType());
			map.put("timeType",posInfo.getTimeType());
			map.put("time",posInfo.getTime());
			map.put("longitudeType",posInfo.getLongitudeType());
			map.put("longitude",posInfo.getLongitude());
			map.put("latitudeType",posInfo.getLatitudeType());
			map.put("latitude",posInfo.getLatitude());
			map.put("directionAngle",posInfo.getDirectionAngle());
			map.put("gpsSpeed",posInfo.getGpsSpeed());
			map.put("obdSpeed",posInfo.getObdSpeed());
			map.put("engineTurns",posInfo.getEngineTurns());
			map.put("engineTemperature",posInfo.getEngineTemperature());
			map.put("satelliteSys",posInfo.getSatelliteSys());
			map.put("satellites",posInfo.getSatellites());
			map.put("satelliteStrength",posInfo.getSatelliteStrength());
			map.put("netType",posInfo.getNetType());
			map.put("mcc",posInfo.getMcc());
			map.put("mnc",posInfo.getMnc());
			map.put("sid",posInfo.getSid());
			map.put("nid",posInfo.getNid());
			map.put("bid",posInfo.getBid());
			map.put("oprecision",posInfo.getOprecision());
			lists.add(map);
		}
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("位置数据表", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);	
		return "excel";
	}
	
	////
	public List<PositionInfo> getPositionInfos() {
		return positionInfos;
	}

	public void setPositionInfos(List<PositionInfo> positionInfos) {
		this.positionInfos = positionInfos;
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


	public String getPids() {
		return pids;
	}


	public void setPids(String pids) {
		this.pids = pids;
	}


	public String getObdSpeed() {
		return obdSpeed;
	}


	public void setObdSpeed(String obdSpeed) {
		this.obdSpeed = obdSpeed;
	}


	public String getEngineTurns() {
		return engineTurns;
	}


	public void setEngineTurns(String engineTurns) {
		this.engineTurns = engineTurns;
	}


	public String getEngineTemperature() {
		return engineTemperature;
	}


	public void setEngineTemperature(String engineTemperature) {
		this.engineTemperature = engineTemperature;
	}


	public String getObdSpeedFlag() {
		return obdSpeedFlag;
	}


	public void setObdSpeedFlag(String obdSpeedFlag) {
		this.obdSpeedFlag = obdSpeedFlag;
	}


	public String getEngineTurnsFlag() {
		return engineTurnsFlag;
	}


	public void setEngineTurnsFlag(String engineTurnsFlag) {
		this.engineTurnsFlag = engineTurnsFlag;
	}


	public String getEngineTempFlag() {
		return engineTempFlag;
	}


	public void setEngineTempFlag(String engineTempFlag) {
		this.engineTempFlag = engineTempFlag;
	}

	
}
