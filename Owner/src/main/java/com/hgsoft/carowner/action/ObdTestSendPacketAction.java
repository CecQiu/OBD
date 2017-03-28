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
import com.hgsoft.carowner.entity.ObdTestSendPacket;
import com.hgsoft.carowner.service.ObdTestSendPacketService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * obd分组
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdTestSendPacketAction extends BaseAction {
	private String starTime;
	private String endTime;
	
	@Resource
	private ObdTestSendPacketService obdTestSendPacketService;
	private List<ObdTestSendPacket> sendPackets = new ArrayList<ObdTestSendPacket>();
	private ObdTestSendPacket obdTestSendPacket;

	// 列表展示
	public String list() {
		//清除缓存
		// 分页获取对象
		sendPackets = query();
		return "list";
	}


	// 从数据库中查询数据
	public List<ObdTestSendPacket> query() {
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
		if(obdTestSendPacket==null){
			return null;
		}

		String obdSn = obdTestSendPacket.getObdSn();
		Integer sended = obdTestSendPacket.getSended();

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.eq("obdSn", obdSn));
		}
		if (!StringUtils.isEmpty(sended)) {
			list.add(Property.eq("sended", sended));
		}
		if (starTime != null && !"".equals(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (endTime != null && !"".equals(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		if(list.size()==0){
			sendPackets = obdTestSendPacketService.findByPager(pager,Order.desc("createTime"));
		}
		sendPackets = obdTestSendPacketService.findByPager(pager,Order.desc("createTime"), propertyArr);
		return sendPackets;
	}
	
	
	// 根据参数id删除一条参数记录
	public String delete() {
		String id = obdTestSendPacket.getId();
		ObdTestSendPacket osp = obdTestSendPacketService.find(id);
		if(null != osp) {
			obdTestSendPacketService.deleteById(id);
			message="deleteSuccess";
		}
		// 分页获取对象
		sendPackets = query();
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

	public List<ObdTestSendPacket> getSendPackets() {
		return sendPackets;
	}


	public void setSendPackets(List<ObdTestSendPacket> sendPackets) {
		this.sendPackets = sendPackets;
	}


	public ObdTestSendPacket getObdTestSendPacket() {
		return obdTestSendPacket;
	}


	public void setObdTestSendPacket(ObdTestSendPacket obdTestSendPacket) {
		this.obdTestSendPacket = obdTestSendPacket;
	}

}
