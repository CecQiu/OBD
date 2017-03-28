package com.hgsoft.obd.action;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.entity.FOTA;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerSettingService;

/**
 * 测试OBD
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class TestFOTAAction extends BaseAction {

	@Resource
	private ServerSettingService serverSettingService;
	
	private String result;
	private String obdSn;
	private FOTA fota;
	
	public String test(){
		if(!StringUtils.isEmpty(obdSn)){
			if(GlobalData.getChannelByObdSn(obdSn.toLowerCase()) == null)
				result="<font color=red size=12>设备离线！不能执行操作。。。</font>";
		}
		return "testFOTA";
	}

	public String testFOTASet(){
		try {
			result = serverSettingService.fotaSet(obdSn, fota);
		} catch (Exception e) {
			e.printStackTrace();
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
	public FOTA getFota() {
		return fota;
	}
	public void setFota(FOTA fota) {
		this.fota = fota;
	}
	
}
