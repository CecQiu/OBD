package com.hgsoft.carowner.action;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.MemberCar;
import com.hgsoft.carowner.service.DictionaryService;
import com.hgsoft.carowner.service.FaultUploadService;
import com.hgsoft.carowner.service.VMemberCarService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.entity.BaseStatus;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * 后台管理-车主服务模块action层
 * @author fdf
 */

@Controller
@Scope("prototype")
public class OwnersAction extends BaseAction {
	
	@Resource
	private VMemberCarService vMemberCarService;
	@Resource
	private FaultUploadService faultUploadService;
	@Resource
	private DictionaryService dictionaryService;
	
	/*-------------查询过滤条件参数-----------------*/
	/**查询类型-1：手机号码、2：车牌、3：激活条码*/
	private int search_type;
	/**查询类型对应的值*/
	private String search_value;
	/**故障类型-1：全部、2：有故障、3：无故障*/
	private int problem_type;
	private String obdsn;//obdsn码
	
	/*--------------------状态参数-----------------------*/
	/**查询类型-手机*/
	private final int search_type_mobileNumber = 1;
	/**查询类型-车牌*/
	private final int search_type_license = 2;
	/**查询类型-obdsn码*/
	private final int search_type_obdsn = 3;
	
	/**故障类型-全部*/
	private final int problem_type_all = 1;
	/**故障类型-有故障*/
	private final int problem_type_isfault = 2;
	/**故障类型-没故障*/
	private final int problem_type_nofault = 3;
	
	private MemberCar memberCar;
	
	/**
	 * 故障提醒
	 */
	public String listMalfunction() {
		//TODO 故障提醒
		List<Property> properties = new ArrayList<Property>();
		//故障类别过滤
		switch(problem_type) {
		case problem_type_all:
			break;
		case problem_type_isfault:
			properties.add(Property.eq("isFault", dictionaryService.
					getDicByCodeAndType(BaseStatus.code_fault_status, BaseStatus.type_fault_normal).getTrueValue()));
			break;
		case problem_type_nofault:
			properties.add(Property.eq("isFault", dictionaryService.
					getDicByCodeAndType(BaseStatus.code_fault_status, BaseStatus.type_fault_error).getTrueValue()));
			break;
		default :
			break;
		}
		
		//查询类型过滤
		if(!StringUtils.isEmpty(search_value)) {
			search_value = search_value.trim();
			switch(search_type) {
			case search_type_mobileNumber:
				properties.add(Property.eq("mobileNumber", search_value));
				break;
			case search_type_license:
				properties.add(Property.eq("license", search_value));
				break;
			case search_type_obdsn:
				properties.add(Property.eq("obdSn", search_value));
				break;
			default:
				break;
			}
		}
		
		list = vMemberCarService.findByPager(pager, null, 
				properties.toArray(new Property[] {}));
		
		return "listMalfunction";
	}
	
	/**
	 * 显示车主的故障列表
	 */
	public String showMalfunctionDetail() {
		//TODO 显示车主的故障列表
		if(!StringUtils.isEmpty(obdsn)) {
			obdsn = obdsn.trim();
			memberCar = vMemberCarService.findByObdsn(obdsn);
			list = faultUploadService.findByPager(pager, Order.asc("createTime"), Property.eq("obdId", obdsn));
		}
		return "showMalfunctionDetail";
	}
	
	/**
	 * 保养提醒
	 */
	public String listMaintenance() {
		//TODO 保养提醒
		return "listMaintenance";
	}
	
	/**
	 * 违章提醒
	 */
	public String listViolateRegulations() {
		//TODO 违章提醒
		return "listViolateRegulations";
	}
	
	/**
	 * 车务提醒
	 */
	public String listCarService() {
		//TODO 车务提醒
		return "listCarService";
	}

	public int getSearch_type() {
		return search_type;
	}

	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}

	public String getSearch_value() {
		return search_value;
	}

	public void setSearch_value(String search_value) {
		this.search_value = search_value;
	}

	public int getProblem_type() {
		return problem_type;
	}

	public void setProblem_type(int problem_type) {
		this.problem_type = problem_type;
	}

	public String getObdsn() {
		return obdsn;
	}

	public void setObdsn(String obdsn) {
		this.obdsn = obdsn;
	}

	public MemberCar getMemberCar() {
		return memberCar;
	}

	public void setMemberCar(MemberCar memberCar) {
		this.memberCar = memberCar;
	}
}