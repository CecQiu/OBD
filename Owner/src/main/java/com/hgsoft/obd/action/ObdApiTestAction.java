package com.hgsoft.obd.action;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hgsoft.common.action.BaseAction;

/**
 * 电信接口测试页面
 * 
 * @author 刘家林 2016年5月24日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class ObdApiTestAction extends BaseAction {
	
	public String test(){
		return "obdApiTest";
	}
	
}
