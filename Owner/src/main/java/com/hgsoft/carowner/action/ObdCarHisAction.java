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
import com.hgsoft.carowner.entity.ObdCarHis;
import com.hgsoft.carowner.service.ObdCarHisService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.StrUtil;

/**
 * 
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdCarHisAction extends BaseAction {
	private String startTime;
	private String endTime;
	private String delStartTime;
	private String delEndTime;
	
	private String obdSn;
	private String obdMSn;
	private List<ObdCarHis> obdCarHisList = new ArrayList<ObdCarHis>();
	@Resource
	private ObdCarHisService obdCarHisService;
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

		String obdSN = "";//表面号解析成设备号
		if (!StringUtils.isEmpty(obdMSn)) {
			//将表面号解析成设备号
			try {
				obdSN = StrUtil.obdSnChange(obdMSn);// 设备号
				if(StringUtils.isEmpty(obdSN)){
					return "list";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "list";
			}
		}
		if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdSN)){
			if(!obdSn.equals(obdSN)){
				return "list";
			}else{
				obdSN = "";
			}
		}

		
		Map<String, Object> map = new HashMap<>();
		Integer total=0;

		if (!StringUtils.isEmpty(obdSn)) {
			map.put("obdSn", obdSn);
			total++;
		}
		if(!StringUtils.isEmpty(obdSN)){
			map.put("obdSn", obdSN);
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
		
		if (!StringUtils.isEmpty(delStartTime)) {
			map.put("delStartTime", delStartTime);
			total++;
		}
		
		if (!StringUtils.isEmpty(delEndTime)) {
			map.put("delEndTime", delEndTime);
			total++;
		}
		
		map.put("paramsTotal", total);
		
		obdCarHisList=obdCarHisService.queryByParams(pager, map);

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


	public String getObdSn() {
		return obdSn;
	}


	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}


	public List<ObdCarHis> getObdCarHisList() {
		return obdCarHisList;
	}


	public void setObdCarHisList(List<ObdCarHis> obdCarHisList) {
		this.obdCarHisList = obdCarHisList;
	}


	public String getDelStartTime() {
		return delStartTime;
	}


	public void setDelStartTime(String delStartTime) {
		this.delStartTime = delStartTime;
	}


	public String getDelEndTime() {
		return delEndTime;
	}


	public void setDelEndTime(String delEndTime) {
		this.delEndTime = delEndTime;
	}


	public String getObdMSn() {
		return obdMSn;
	}


	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}

}
