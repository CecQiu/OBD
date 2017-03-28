package com.hgsoft.obd.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.common.action.BaseAction;

/**
 * 测试OBD
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class PrivateProtocolAction extends BaseAction {

	private String obdSn;
	
	public String test(){
		return "test";
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	
}
