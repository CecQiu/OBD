package com.hgsoft.carowner.action;

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

import com.hgsoft.carowner.entity.ObdButtonAlarm;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.carowner.service.ObdButtonAlarmService;
import com.hgsoft.carowner.service.PositionInfoService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;


/**
 *
 * @author sjg
 */

@Controller
@Scope("prototype")
public class ObdButtonAlarmAction extends BaseAction {
	@Resource
	private ObdButtonAlarmService obdButtonAlarmService;
	@Resource
	private PositionInfoService positionInfoService;
	private String startTime;
	private String endTime;
	private String obdSn;
	private String obdMSn;
	private String positionInfoId;
	private PositionInfo positionInfo;
	private PositionInfo positionInfoM;
	private List<ObdButtonAlarm> obdButtonAlarms = new ArrayList<ObdButtonAlarm>();
	private String time;
	
	// 列表展示
	public String list() {
		//清除缓存
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		// 分页获取对象
		if (StrUtil.arraySubNotNull(obdSn,obdMSn,startTime,endTime)) {
			obdButtonAlarms = query(pager);
		}else{
			Calendar c = Calendar.getInstance();
			Date now = c.getTime();
			endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
			c.add(Calendar.MONTH, -1);
			Date s= c.getTime();
			startTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
		}
		
		return "list";
	}


	// 从数据库中查询数据
	public List<ObdButtonAlarm> query(Pager pager) {

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
		
		if (!StringUtils.isEmpty(startTime)) {
			map.put("startTime", startTime);
			total++;
		}
		
		if (!StringUtils.isEmpty(endTime)) {
			map.put("endTime", endTime);
			total++;
		}
		
		map.put("paramsTotal", total);

		return obdButtonAlarmService.queryByParams(pager, map);
		 
	}
	
	public String position(){
		//查看定位
		if(!StringUtils.isEmpty(positionInfoId) && !StringUtils.isEmpty(obdSn)){
			positionInfo = positionInfoService.find(positionInfoId);
		}
		if(StringUtils.isEmpty(positionInfoId) && !StringUtils.isEmpty(obdSn)){
			//查看上一定位成功点
			positionInfo = positionInfoService.findLastBySNAndGtTime(obdSn, time);
		}
		if(!StringUtils.isEmpty(positionInfo) && 
				!StringUtils.isEmpty(positionInfo.getLongitude()) &&
				!StringUtils.isEmpty(positionInfo.getLatitude())){
			positionInfoM = new PositionInfo();
			positionInfoM.setTime(positionInfo.getTime());
			positionInfoM.setLongitude(
					CoordinateTransferUtil.lnglatTransferDouble(positionInfo.getLongitude()));
			positionInfoM.setLatitude(
					CoordinateTransferUtil.lnglatTransferDouble(positionInfo.getLatitude()));
		}
		
		return "position";
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
	public List<ObdButtonAlarm> getObdButtonAlarms() {
		return obdButtonAlarms;
	}
	public void setObdButtonAlarms(List<ObdButtonAlarm> obdButtonAlarms) {
		this.obdButtonAlarms = obdButtonAlarms;
	}
	public String getPositionInfoId() {
		return positionInfoId;
	}
	public void setPositionInfoId(String positionInfoId) {
		this.positionInfoId = positionInfoId;
	}
	public PositionInfo getPositionInfo() {
		return positionInfo;
	}
	public void setPositionInfo(PositionInfo positionInfo) {
		this.positionInfo = positionInfo;
	}
	public PositionInfo getPositionInfoM() {
		return positionInfoM;
	}
	public void setPositionInfoM(PositionInfo positionInfoM) {
		this.positionInfoM = positionInfoM;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
