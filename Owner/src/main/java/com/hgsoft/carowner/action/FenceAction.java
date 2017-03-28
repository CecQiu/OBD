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
import com.hgsoft.carowner.entity.Fence;
import com.hgsoft.carowner.service.FenceService;
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
public class FenceAction extends BaseAction {
	private String starTime;
	private String endTime;
	
	private Fence fence;
	private List<Fence> fences = new ArrayList<Fence>();
	@Resource
	private FenceService fenceService;
	private String fenceId;
	private String fenceJson;

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

		String obdSn = fence.getObdSn();
		Integer valid = fence.getValid();
		Integer type = fence.getType();

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
		}
		if (!StringUtils.isEmpty(valid)) {
			list.add(Property.eq("valid", valid));
		}
		if (!StringUtils.isEmpty(type)) {
			list.add(Property.eq("type", type));
		}
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		fences = fenceService.findByPager(pager,Order.desc("createTime"), propertyArr);
		return "list";
	}

	public String toFenceMap() {
		this.fence=fenceService.find(fenceId);
		this.fenceJson = fence.getPoints();
		return "fenceMap";
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


	public Fence getFence() {
		return fence;
	}


	public void setFence(Fence fence) {
		this.fence = fence;
	}


	public List<Fence> getFences() {
		return fences;
	}


	public void setFences(List<Fence> fences) {
		this.fences = fences;
	}


	public String getFenceId() {
		return fenceId;
	}


	public void setFenceId(String fenceId) {
		this.fenceId = fenceId;
	}


	public String getFenceJson() {
		return fenceJson;
	}


	public void setFenceJson(String fenceJson) {
		this.fenceJson = fenceJson;
	}

}
