package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * obd设置表
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdSettingAction extends BaseAction {
	private final Log logger = LogFactory.getLog(ObdSettingAction.class);
	private String starTime;
	private String endTime;
	
	private ObdSetting obdSetting;
	private List<ObdSetting> obdSettings = new ArrayList<ObdSetting>();
	@Resource
	private ObdSettingService obdSettingService;

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
//		obdPackets = obdPacketService.findByPager(pager, Order.desc("insertTime"));
		return "list";
	}


	// 从数据库中查询数据
	public String query() {
		//清除缓存
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");

		String obdSn = obdSetting.getObdSn();
		String type = obdSetting.getType();
		String valid = obdSetting.getValid();
		String settingMsg = obdSetting.getSettingMsg();
		
		
		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
		}
		
		if (!StringUtils.isEmpty(type)) {
			list.add(Property.eq("type", type));
		}
		
		if (!StringUtils.isEmpty(valid)) {
			list.add(Property.eq("valid", valid));
		}
		
		if (!StringUtils.isEmpty(settingMsg)) {
			list.add(Property.like("settingMsg", "%"+settingMsg.trim()+"%"));
		}
		
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		obdSettings = obdSettingService.findByPager(pager,Order.desc("createTime"), propertyArr);
		return "list";
	}
	
	
	// 根据参数id删除一条参数记录
	public String delete() {
		obdSetting = obdSettingService.find(obdSetting.getId());
		if(null != obdSetting) {
			obdSetting.setValid("0");
			obdSetting.setUpdateTime(new Date());
			obdSettingService.update(obdSetting);
			message="deleteSuccess";
		}
//		// 分页获取对象
//		obdGroups = obdGroupService.findByPager(pager,Order.desc("createTime"),Property.eq("valid", "1"));
		return "list";
	}

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public ObdSetting getObdSetting() {
		return obdSetting;
	}


	public void setObdSetting(ObdSetting obdSetting) {
		this.obdSetting = obdSetting;
	}


	public List<ObdSetting> getObdSettings() {
		return obdSettings;
	}


	public void setObdSettings(List<ObdSetting> obdSettings) {
		this.obdSettings = obdSettings;
	}


	

}
