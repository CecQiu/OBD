package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * obd报文查询
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class MebUserAction extends BaseAction {
	private String starTime;
	private String endTime;
	private String obdSN;
	private String mobileNumber;
	private String valid;
	
	private MebUser mebUser;
	private List<MebUser> mebUsers = new ArrayList<>();
	@Resource
	private MebUserService mebUserService;

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


		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSN)) {
			list.add(Property.eq("obdSN", obdSN.trim()));
		}
		if (!StringUtils.isEmpty(mobileNumber)) {
			list.add(Property.eq("mobileNumber", mobileNumber.trim()));
		}
		if (!StringUtils.isEmpty(valid)) {
			list.add(Property.eq("valid", valid));
		}
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		mebUsers = mebUserService.findByPager(pager,Order.desc("createTime"), propertyArr);
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


	public String getObdSN() {
		return obdSN;
	}


	public void setObdSN(String obdSN) {
		this.obdSN = obdSN;
	}


	public String getValid() {
		return valid;
	}


	public void setValid(String valid) {
		this.valid = valid;
	}


	public MebUser getMebUser() {
		return mebUser;
	}


	public void setMebUser(MebUser mebUser) {
		this.mebUser = mebUser;
	}


	public List<MebUser> getMebUsers() {
		return mebUsers;
	}


	public void setMebUsers(List<MebUser> mebUsers) {
		this.mebUsers = mebUsers;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
