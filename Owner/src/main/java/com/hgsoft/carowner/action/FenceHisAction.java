package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.FenceHis;
import com.hgsoft.carowner.service.FenceHisService;
import com.hgsoft.common.action.BaseAction;

/**
 * 电子围栏历史表
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class FenceHisAction extends BaseAction {
	private String startTime;
	private String endTime;
	private String obdSn;
	private String type;
	
	private List<FenceHis> fenceHiss = new ArrayList<FenceHis>();
	@Resource
	private FenceHisService fenceHisService;

	// 列表展示
	public String list() {
		// 分页获取对象
//		obdPackets = obdPacketService.findByPager(pager, Order.desc("insertTime"));
		return "list";
	}


	// 从数据库中查询数据
	public String query() {
		Map<String, Object> map = new HashMap<>();
		Integer total=0;

		if (!StringUtils.isEmpty(obdSn)) {
			map.put("obdSn", obdSn);
			total++;
		}
		
		if (!StringUtils.isEmpty(type)) {
			map.put("type", Integer.parseInt(type));
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

		fenceHiss = fenceHisService.queryByParams(pager, map);
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

	public List<FenceHis> getFenceHiss() {
		return fenceHiss;
	}


	public void setFenceHiss(List<FenceHis> fenceHiss) {
		this.fenceHiss = fenceHiss;
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


}
