package com.hgsoft.carowner.action;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.entity.MemberCar;
import com.hgsoft.carowner.service.DictionaryService;
import com.hgsoft.carowner.service.VMemberCarService;
import com.hgsoft.common.action.BaseAction;

/**
 * 后台管理-营销管理模块action层
 * @author fdf
 */
@Controller
@Scope("prototype")
public class MarketingManagementAction extends BaseAction {
	
	@Resource
	private VMemberCarService vMemberCarService;
	@Resource
	private DictionaryService dictionaryService;
	
	/*-------------查询过滤条件参数-----------------*/
	private Date startTime;
	private Date endTime;
	
	/*--------------------状态参数-----------------------*/
	
	private MemberCar memberCar;
	
	/**
	 * 行程记录统计
	 */
	public String travelRecords() {
		//TODO 行程记录统计
		list = vMemberCarService.findAll();
		return "travelRecords";
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public MemberCar getMemberCar() {
		return memberCar;
	}

	public void setMemberCar(MemberCar memberCar) {
		this.memberCar = memberCar;
	}
}