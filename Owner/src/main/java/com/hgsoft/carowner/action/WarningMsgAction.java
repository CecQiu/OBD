package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.hgsoft.carowner.entity.WarningMessage;
import com.hgsoft.carowner.service.WarningMessageService;
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
public class WarningMsgAction extends BaseAction {
	private final Log logger = LogFactory.getLog(WarningMsgAction.class);
	private String starTime;
	private String endTime;
	
	@Resource
	private WarningMessageService warningMessageService;
	private WarningMessage warningMessage;
	private List<WarningMessage> warningMsgs = new ArrayList<WarningMessage>();

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
		Calendar c = Calendar.getInstance();
    	Date now = c.getTime();
    	endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
    	c.add(Calendar.MONTH, -1);
    	Date s= c.getTime();
    	starTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
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

		String obdSn = warningMessage.getObdSn();
		String messageType = warningMessage.getMessageType();
		String remark = warningMessage.getRemark();
		String company = warningMessage.getCompany();
		

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
		}
		if (!StringUtils.isEmpty(messageType)) {
			list.add(Property.eq("messageType", messageType.trim()));
		}
		if (!StringUtils.isEmpty(remark)) {
			list.add(Property.eq("remark", remark));
		}
		if (!StringUtils.isEmpty(company)) {
			list.add(Property.eq("company", company));
		}
		
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("messageTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("messageTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		warningMsgs = warningMessageService.findByPager(pager,Order.desc("messageTime"), propertyArr);
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


	public WarningMessage getWarningMessage() {
		return warningMessage;
	}


	public void setWarningMessage(WarningMessage warningMessage) {
		this.warningMessage = warningMessage;
	}


	public List<WarningMessage> getWarningMsgs() {
		return warningMsgs;
	}


	public void setWarningMsgs(List<WarningMessage> warningMsgs) {
		this.warningMsgs = warningMsgs;
	}


	

}
