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
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdSimCard;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdSimCardService;
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
public class ObdSimCardAction extends BaseAction {
	private String startTime;
	private String endTime;
	
	private String obdSn;
	private String obdMSn;
	private Integer type;

	private List<ObdSimCard> obdSimCards = new ArrayList<>();
	@Resource
	private ObdSimCardService obdSimCardService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	
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
		obdSimCards = obdSimCardService.findByPager(pager,Order.desc("createTime"));
		return "list";
	}


	/**
	 * 从数据库中查询数据
	 * 设备号： null  表面号：null
	 * 设备号：not null  表面号：null
	 * 设备号：null  表面号：not null
	 * 设备号：not null  表面号：not null
	 * 当表面号不为空的时候，查询设备——》获取设备号B，
	 * 如果设备不为空，如果设备号A参数也不为空，
	 * 如果A和B不一致则马上返回空，
	 * 如果A和B一致则将单独的设备号C置空
	 * @return
	 */
	public String query() {
		String obdSn2="";//通过表面号获取设备号
		if(!StringUtils.isEmpty(obdMSn)){
			OBDStockInfo obd=obdStockInfoService.queryByObdMSN(obdMSn);
			if(obd!=null){
				obdSn2 = obd.getObdSn();
			}else{
				return "list";
			}
		}
		//如果两个设备号不一致，则返回空
		if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdSn2)){
			if(!obdSn.equals(obdSn2)){
				return "list";
			}else{
				obdSn2 ="";
			}
		}
		
		
		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.eq("obdSn", obdSn.trim()));
		}
		if (!StringUtils.isEmpty(obdSn2)) {
			list.add(Property.eq("obdSn", obdSn2.trim()));
		}
		if (type!=-1) {
			list.add(Property.eq("type", type));
		}
		if (!StringUtils.isEmpty(startTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(startTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		if(propertyArr.length>0){
			obdSimCards = obdSimCardService.findByPager(pager,Order.desc("createTime"), propertyArr);
		}else{
			return list();
		}
		
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

	public String getObdMSn() {
		return obdMSn;
	}


	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}


	public String getObdSn() {
		return obdSn;
	}


	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}


	public List<ObdSimCard> getObdSimCards() {
		return obdSimCards;
	}


	public void setObdSimCards(List<ObdSimCard> obdSimCards) {
		this.obdSimCards = obdSimCards;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


}
