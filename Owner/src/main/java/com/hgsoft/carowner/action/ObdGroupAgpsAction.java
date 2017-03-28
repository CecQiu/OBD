package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.ObdGroupAGPS;
import com.hgsoft.carowner.service.ObdGroupAGPSService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.Order;

/**
 * 
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdGroupAgpsAction extends BaseAction {
	private String startTime;
	private String endTime;
	
	private String groupNum;
	private List<ObdGroupAGPS> agpsGroupList = new ArrayList<ObdGroupAGPS>();
	@Resource
	private ObdGroupAGPSService obdGroupAGPSService;
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
		this.agpsGroupList=obdGroupAGPSService.findByPager(pager, Order.desc("createTime"));
//		Calendar c = Calendar.getInstance();
//    	Date now = c.getTime();
//    	endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
//    	c.add(Calendar.MONTH, -1);
//    	Date s= c.getTime();
//    	startTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
		return "list";
	}


	// 从数据库中查询数据
	public String query() {
		//清除缓存
		
		Map<String, Object> map = new HashMap<>();
		Integer total=0;

		if (!StringUtils.isEmpty(groupNum)) {
			map.put("groupNum", groupNum);
			total++;
		}
		
		if (!StringUtils.isEmpty(startTime)) {
			map.put("startTime", startTime);
			total++;
		}
		
		if (!StringUtils.isEmpty(endTime)) {
			map.put("endTime", endTime);
			total++;
		}
		
		map.put("paramsTotal", total);
		
		agpsGroupList=obdGroupAGPSService.queryByParams(pager, map);

		return "list";
		
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getGroupNum() {
		return groupNum;
	}


	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}


	public List<ObdGroupAGPS> getAgpsGroupList() {
		return agpsGroupList;
	}


	public void setAgpsGroupList(List<ObdGroupAGPS> agpsGroupList) {
		this.agpsGroupList = agpsGroupList;
	}


}
