package com.hgsoft.system.action;

import com.hgsoft.common.action.BaseAction;
import com.hgsoft.system.entity.Role;
import com.hgsoft.system.service.ModuleService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

import java.util.HashSet;


@Controller
@Scope("prototype")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SystemAction extends BaseAction {

	@Resource
    ModuleService moduleService;
	
	private String shopName;
	private Long time;

	public String index() {
		if(operator == null){
			return LOGIN;
		}
		HashSet set = new HashSet();
		Role role = operator.getRole();
		if (role != null && role.getId() > 0) {
			set.addAll(role.getModules());
			if (set != null && !set.isEmpty()) {
				list = moduleService.getMenus(set);
			}
		}
		return "index";
	}

	public String top() {
		return "top";
	}

	public String left() {
		HashSet set = new HashSet();
		Role role = operator.getRole();
		if (role != null && role.getId() > 0) {
			set.addAll(role.getModules());
			if (set != null && !set.isEmpty()) {
				list = moduleService.getMenus(set);
			}
		}
		return "left";
	}

	public String leftJson() {
		HashSet set = new HashSet();
		Role role = operator.getRole();
		if (role != null && role.getId() > 0) {
			set.addAll(role.getModules());
			if (set != null && !set.isEmpty()) {
				list = moduleService.getMenus(set);
			}
		}
		return "leftJson";
	}



	public String right() {
		//彩站用户，把管理的彩站名拿到显示在首页右端
		return "right";
	}

	public String middle() {
		return "middle";
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
