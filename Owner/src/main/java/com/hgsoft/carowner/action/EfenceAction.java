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

import com.hgsoft.carowner.entity.Efence;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.EfenceService;
import com.hgsoft.carowner.service.ObdSateService;
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
public class EfenceAction extends BaseAction {
	private final Log logger = LogFactory.getLog(EfenceAction.class);
	private String starTime;
	private String endTime;
	
	private Efence efence;
	private List<Efence> efences = new ArrayList<Efence>();
	@Resource
	private EfenceService efenceService;

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

		String obdSn = efence.getObdSn();
		String valid = efence.getValid();

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
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
		efences = efenceService.findByPager(pager,Order.desc("createTime"), propertyArr);
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


	public Efence getEfence() {
		return efence;
	}


	public void setEfence(Efence efence) {
		this.efence = efence;
	}


	public List<Efence> getEfences() {
		return efences;
	}


	public void setEfences(List<Efence> efences) {
		this.efences = efences;
	}


}
