package com.hgsoft.obd.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.common.action.BaseAction;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.EFenceType;

/**
 * 测试OBD
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class TestEFenceAction extends BaseAction {
	

	@Resource
	private ServerSettingService serverSettingService;
	
	private String result;
	private String obdSn;
	
	private String timingType;//有无定时
	private String areaNo;//区域编号
	private String type;//栅栏类型
	private String warnType;//报警方式
	
	private String bigLongitude;
	private String bigLatitude;
	private String smallLongitude;
	private String smallLatitude;
	private String timingBegin;
	private String timingEnd;

	
	public String test(){
		if(!StringUtils.isEmpty(obdSn)){
			if(GlobalData.getChannelByObdSn(obdSn.toLowerCase()) == null)
				result="<font color=red size=12>设备离线！不能执行操作。。。</font>";
		}
		return "testEFence";
	}
	
	public String testEFenceSet(){
		System.out.println("timingType:"+timingType);
		System.out.println("areaNo:"+areaNo);
		System.out.println("type:"+type);
		System.out.println("warnType:"+warnType);
		try {
			System.out.println("bigLongitude:"+new String(bigLongitude.getBytes(),"UTF-8"));
		
		System.out.println("bigLatitude:"+new String(bigLatitude.getBytes(),"ISO-8859-1"));
		System.out.println("smallLongitude:"+smallLongitude);
		System.out.println("smallLatitude:"+smallLatitude);
		System.out.println("timingBegin:"+timingBegin);
		System.out.println("timingEnd:"+timingEnd);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			EFenceType timingEFenceType = null;
			EFenceType warnEFenceType = null;
			EFenceType eFenceType = null;
			int efenceNo = Integer.valueOf(areaNo);
			if("0".equals(type)){
				eFenceType = EFenceType.Circle;
			}else if("1".equals(type)){
				eFenceType = EFenceType.Rectangle;
			}else{
				result="<font color=red size=12>参数不满足！</font>";
			}
			
			if("1".equals(warnType)){
				warnEFenceType = EFenceType.InArea;
			}else if("2".equals(warnType)){
				warnEFenceType = EFenceType.OutArea;
			}else if("4".equals(warnType)){
				warnEFenceType = EFenceType.CancelFence;
			}else if("5".equals(warnType)){
				warnEFenceType = EFenceType.CancelAllFences;
			}else{
				result="<font color=red size=12>参数不满足！</font>";
			}
			
			if("0".equals(timingType)){
				timingEFenceType = EFenceType.NoTiming;
				result = serverSettingService.efenceSet(obdSn, timingEFenceType , efenceNo , eFenceType, warnEFenceType , 
						bigLongitude,bigLatitude,smallLongitude,smallLatitude);
			}else if("1".equals(timingType)){
				timingEFenceType = EFenceType.Timing;
				result = serverSettingService.efenceSet(obdSn, timingEFenceType , efenceNo , eFenceType, warnEFenceType , 
						bigLongitude,bigLatitude,smallLongitude,smallLatitude,timingBegin,timingEnd);
			}else{
				result="<font color=red size=12>参数不满足！</font>";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result="<font color=red size=12>异常！参数不符合</font>";
		}
		
		return test();
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getTimingType() {
		return timingType;
	}

	public void setTimingType(String timingType) {
		this.timingType = timingType;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWarnType() {
		return warnType;
	}

	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}

	public String getBigLongitude() {
		return bigLongitude;
	}

	public void setBigLongitude(String bigLongitude) {
		this.bigLongitude = bigLongitude;
	}

	public String getBigLatitude() {
		return bigLatitude;
	}

	public void setBigLatitude(String bigLatitude) {
		this.bigLatitude = bigLatitude;
	}

	public String getSmallLongitude() {
		return smallLongitude;
	}

	public void setSmallLongitude(String smallLongitude) {
		this.smallLongitude = smallLongitude;
	}

	public String getSmallLatitude() {
		return smallLatitude;
	}

	public void setSmallLatitude(String smallLatitude) {
		this.smallLatitude = smallLatitude;
	}

	public String getTimingBegin() {
		return timingBegin;
	}

	public void setTimingBegin(String timingBegin) {
		this.timingBegin = timingBegin;
	}

	public String getTimingEnd() {
		return timingEnd;
	}

	public void setTimingEnd(String timingEnd) {
		this.timingEnd = timingEnd;
	}
	
}
