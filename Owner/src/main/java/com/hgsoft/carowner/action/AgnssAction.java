package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hgsoft.carowner.entity.Agnss;
import com.hgsoft.carowner.service.AgnssService;
import com.hgsoft.common.action.BaseAction;

/**
 * 
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class AgnssAction extends BaseAction {
	private List<Agnss> agnssList = new ArrayList<Agnss>();
	@Resource
	private AgnssService agnssService;


	// 从数据库中查询数据
	public String list() {
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

		agnssList=agnssService.getLast();
		return "list";
		
	}

	public List<Agnss> getAgnssList() {
		return agnssList;
	}

	public void setAgnssList(List<Agnss> agnssList) {
		this.agnssList = agnssList;
	}

}
