package com.hgsoft.carowner.action;

import java.io.UnsupportedEncodingException;
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

import com.hgsoft.carowner.entity.OBDPacket;
import com.hgsoft.carowner.service.OBDPacketService;
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
public class ObdMsgAction extends BaseAction {
	private final Log logger = LogFactory.getLog(ObdMsgAction.class);
	@Resource
	private OBDPacketService obdPacketService;
	private List<OBDPacket> obdPackets = new ArrayList<OBDPacket>();
	private OBDPacket obdPacket;
	private String starTime;
	private String endTime;

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

		String obdSn = obdPacket.getObdSn();
		String comman = obdPacket.getComman();
		Integer packetType = obdPacket.getPacketType();
		

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.or(Property.like("obdSn", "%"+obdSn.trim()+"%"), Property.like("packetData", "%"+obdSn.trim()+"%")));
		}
		if (!StringUtils.isEmpty(comman)) {
			list.add(Property.eq("comman", comman.trim()));
		}
		if (!StringUtils.isEmpty(packetType)) {
			list.add(Property.eq("packetType", packetType));
		}
		
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("insertTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("insertTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		obdPackets = obdPacketService.findByPager(pager,Order.desc("insertTime"), propertyArr);
		return "list";
	}
	
	//编辑
	public String showMsg() throws UnsupportedEncodingException {
		//设置当前页
		String msg= new String(obdPacket.getMsg().getBytes("iso-8859-1"),"utf-8");
		//String msg = java.net.URLDecoder.decode(obdPacket.getMsg(), "UTF-8"); 
		obdPacket.setMsg(msg);
		System.out.println(obdPacket.getMsg());
		return "msgShow";
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


	public OBDPacketService getObdPacketService() {
		return obdPacketService;
	}


	public void setObdPacketService(OBDPacketService obdPacketService) {
		this.obdPacketService = obdPacketService;
	}


	public List<OBDPacket> getObdPackets() {
		return obdPackets;
	}


	public void setObdPackets(List<OBDPacket> obdPackets) {
		this.obdPackets = obdPackets;
	}


	public OBDPacket getObdPacket() {
		return obdPacket;
	}


	public void setObdPacket(OBDPacket obdPacket) {
		this.obdPacket = obdPacket;
	}

}
