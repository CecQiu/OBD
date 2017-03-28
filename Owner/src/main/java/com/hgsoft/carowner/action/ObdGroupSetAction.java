package com.hgsoft.carowner.action;

import java.util.ArrayList;
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

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * obd分组
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdGroupSetAction extends BaseAction {
	private final Log logger = LogFactory.getLog(ObdGroupSetAction.class);
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	private List<OBDStockInfo> obdStockInfos = new ArrayList<OBDStockInfo>();
	private OBDStockInfo obdStockInfo;
	@Resource
	private ObdGroupService obdGroupService;
	private List<ObdGroup> obdGroups = new ArrayList<ObdGroup>();
	private ObdGroup obdGroup;
	private String che_group;
	
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
		
		obdGroups = obdGroupService.queryList();
		// 分页获取对象
		obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"),Property.eq("valid","1"));
		return "list";
	}
	
	/**
	 * 展示要升级推送的obd列表
	 */
	public String listObdSet() {
		obdGroups = obdGroupService.queryList();
		obdStockInfos = obdStockInfoService.queryList();
		
		return "listObdGroup";
	}

	/**
	 * 设置分组
	 */
	public String set() {
		String groupNum=obdGroup.getGroupNum();
		logger.info("列表:"+che_group+"**************分组:"+groupNum);
		String[] obdList=che_group.split(",");
		for (String obd : obdList) {
			int total=obdStockInfoService.obdGroupNumUpd(obd.trim(),groupNum);
		}
		return list();
//		if(StringUtils.isEmpty(obdListStr)){
//			outJsonMessage("{\"status\":\"fail\",\"message\":\"远程升级推送失败\"}");
//		}else{
//			String[] obdSnArr=obdListStr.trim().split(",");
//			boolean flag=remoteUpgradeService.remoteUpgradeAsk(obdSnArr, obdVersion.trim());
//			if(flag){
//				outJsonMessage("{\"status\":\"success\",\"message\":\"远程升级推送成功\"}");
//			}else{
//				outJsonMessage("{\"status\":\"fail\",\"message\":\"远程升级推送失败\"}");
//			}
//		}
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

		String obdSn = obdStockInfo.getObdSn();
		String obdMSn = obdStockInfo.getObdMSn();
		String obdId = obdStockInfo.getObdId();
		String groupNum = obdStockInfo.getGroupNum();

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
		}
		if (!StringUtils.isEmpty(obdMSn)) {
			list.add(Property.like("obdMSn", "%"+obdMSn.trim()+"%"));
		}
		if (!StringUtils.isEmpty(obdId)) {
			list.add(Property.like("obdId", "%"+obdId.trim()+"%"));
		}
		if (!StringUtils.isEmpty(groupNum)) {
			list.add(Property.eq("groupNum", groupNum.trim()));
		}
		if (starTime != null && !"".equals(starTime)) {
			list.add(Property.ge("startDate", starTime));
		}
		if (endTime != null && !"".equals(endTime)) {
			list.add(Property.le("startDate", endTime));
		}
		list.add(Property.eq("valid", "1"));
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		obdGroups = obdGroupService.queryList();
		obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"), propertyArr);
		return "list";
	}
	
	//跳转到修改分组编号页面
	public String edit() {
		//设置当前页
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.setAttribute("currentPage", this.pager.getCurrentPage());
		session.setAttribute("pageSize", this.pager.getPageSize());
		session.setAttribute("rowIndex", this.pager.getRowIndex());
		obdGroups = obdGroupService.queryList();
		obdStockInfo = obdStockInfoService.find(obdStockInfo.getStockId());
		return "edit";
	}
	
	//编辑
	public String groupEdit() {
		//设置当前页
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.setAttribute("currentPage", this.pager.getCurrentPage());
		session.setAttribute("pageSize", this.pager.getPageSize());
		session.setAttribute("rowIndex", this.pager.getRowIndex());
		String obdSn=obdStockInfo.getObdSn();
		String groupNum=obdGroup.getGroupNum();
		if(StringUtils.isEmpty(groupNum)){
			groupNum = null;
		}
		System.out.println(obdStockInfo.getObdSn()+"***"+obdGroup.getGroupNum());
		int total=obdStockInfoService.obdGroupNumUpd(obdSn,groupNum);
		return list();
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

	public List<ObdGroup> getObdGroups() {
		return obdGroups;
	}

	public void setObdGroups(List<ObdGroup> obdGroups) {
		this.obdGroups = obdGroups;
	}

	public ObdGroup getObdGroup() {
		return obdGroup;
	}

	public void setObdGroup(ObdGroup obdGroup) {
		this.obdGroup = obdGroup;
	}

	public List<OBDStockInfo> getObdStockInfos() {
		return obdStockInfos;
	}

	public void setObdStockInfos(List<OBDStockInfo> obdStockInfos) {
		this.obdStockInfos = obdStockInfos;
	}

	public OBDStockInfo getObdStockInfo() {
		return obdStockInfo;
	}

	public void setObdStockInfo(OBDStockInfo obdStockInfo) {
		this.obdStockInfo = obdStockInfo;
	}

	public String getChe_group() {
		return che_group;
	}

	public void setChe_group(String che_group) {
		this.che_group = che_group;
	}

}
