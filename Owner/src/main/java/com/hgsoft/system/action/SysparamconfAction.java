package com.hgsoft.system.action;


import com.hgsoft.common.action.BaseAction;
import com.hgsoft.system.entity.Sysparamconf;
import com.hgsoft.system.service.SysparamconfService;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author 
 * 系统参数配置
 */
@Controller
@Scope("prototype")
public class SysparamconfAction extends BaseAction {

	@Resource
	private SysparamconfService sysparamconfService;
	private Sysparamconf sysparamconf;

	/*
	 * 跳转到其他参数配置list页面
	 */
	public String list() {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");

		list = sysparamconfService.findByPager(pager);
		return "sysparamconf_list";
	}

	// 从数据库中查询数据
	public String query() {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");

		if (!sysparamconf.getPname().equals("")
				|| !sysparamconf.getPvalue().equals("")
				|| sysparamconf.getPtype() != null
				|| !sysparamconf.getRemark().equals("")) {

			session.setAttribute("pname", sysparamconf.getPname());
			session.setAttribute("pvalue", sysparamconf.getPvalue());
			session.setAttribute("ptype",sysparamconf.getPtype());
			session.setAttribute("remark",sysparamconf.getRemark());

			list = sysparamconfService.queryForAll(pager, sysparamconf
					.getPname(), sysparamconf.getPvalue(), sysparamconf
					.getPtype(), sysparamconf.getRemark());
			return "sysparamconf_list";

		}
		else {
			return list();
		}
	}

	// 跳转至参数增加页面
	public String add() {
		return "sysparamconf_add";
	}

	// 保存参数到数据库
	public String save() {
		if(sysparamconfService.checkPnameByPname(sysparamconf.getPname())==null){

			Sysparamconf scTemp=new Sysparamconf();
			scTemp.setPname(sysparamconf.getPname().trim());
			scTemp.setPtype(sysparamconf.getPtype());
			scTemp.setPvalue(sysparamconf.getPvalue().trim());
			scTemp.setRemark(sysparamconf.getRemark().trim());

			sysparamconfService.save(scTemp);
			result = Result.SUCCESS;
			scTemp=null;
		}
		else{
			message ="hasPname";

			return "sysparamconf_add";
		}
		return list();
	}

	public String edit() {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.setAttribute("currentPage", this.pager.getCurrentPage());
		session.setAttribute("pageSize", this.pager.getPageSize());
		session.setAttribute("rowIndex", this.pager.getRowIndex());

		sysparamconf = sysparamconfService.find(sysparamconf.getId());
		return "sysparamconf_edit";
	}

	// 修改一条记录
	public String update() {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		this.pager.setCurrentPage(session.getAttribute("currentPage").toString().trim());
		this.pager.setPageSize(session.getAttribute("pageSize").toString().trim());
		this.pager.setRowIndex(session.getAttribute("rowIndex").toString().trim());

		Sysparamconf temp = sysparamconfService.find(sysparamconf.getId());
		temp.setPname(sysparamconf.getPname().trim());
		temp.setPvalue(sysparamconf.getPvalue().trim());
		temp.setPtype(sysparamconf.getPtype());
		temp.setRemark(sysparamconf.getRemark().trim());

		sysparamconfService.update(temp);
		message="updateSuccess";
		temp=null;
		if(session.getAttribute("pname")!=null
				||session.getAttribute("pvalue")!=null
				||session.getAttribute("ptype")!=null
				||session.getAttribute("remark")!=null){
			if(session.getAttribute("ptype")==null){
				list = sysparamconfService.queryForAll(pager, session.getAttribute("pname").toString(),
						session.getAttribute("pvalue").toString(),
						null,
						session.getAttribute("remark").toString());

				sysparamconf.setPname(session.getAttribute("pname").toString());
				sysparamconf.setPvalue(session.getAttribute("pvalue").toString());
				sysparamconf.setPtype(null);
				sysparamconf.setRemark(session.getAttribute("remark").toString());
			}else{
				list = sysparamconfService.queryForAll(pager, session.getAttribute("pname").toString(),
						session.getAttribute("pvalue").toString(),
						Integer.parseInt(session.getAttribute("ptype").toString().trim()),
						session.getAttribute("remark").toString());

				sysparamconf.setPname(session.getAttribute("pname").toString());
				sysparamconf.setPvalue(session.getAttribute("pvalue").toString());
				sysparamconf.setPtype(Integer.parseInt(session.getAttribute("ptype").toString().trim()));
				sysparamconf.setRemark(session.getAttribute("remark").toString());
			}
		}else{
			list = sysparamconfService.findByPager(pager);
		}
		return  "sysparamconf_list";
	}

	// 根据参数id删除一条参数记录
	public String delete() {
		Sysparamconf sysparamconfTemp = sysparamconfService.find(sysparamconf.getId());
		if(null != sysparamconfTemp) {
			sysparamconfService.delete(sysparamconfTemp);
			message="deleteSuccess";
		}
		return list();
	}

	//--------------------------------------------get、set方法-----------------------------------------------------------

	public Sysparamconf getSysparamconf() {
		return sysparamconf;
	}

	public void setSysparamconf(Sysparamconf sysparamconf) {
		this.sysparamconf = sysparamconf;
	}

	//-------------------------------------------------------------------------------------------


	//跳转到Excel导入浏览文件页面
    public String toImportExcel(){
    	return "toImportExcel";
    }

	//判断是否是数字
	public boolean isNumber(String s) {
		String REG_DIGIT="[0-9]*";
		if(s.matches(REG_DIGIT))return true;
		return false;
	}

	//判断是否是0或1
	public boolean isNumberOneorTwo(String s) {
		String REG_DIGIT="[0-1]";
		if(s.matches(REG_DIGIT))return true;
		return false;
	}

	//判断参数是否符合标识符规范
	public boolean validatePname(String input) {
		if(!String.valueOf(input.charAt(0)).equals("$")){
			int pos = 0;
			if (Character.isJavaIdentifierStart(input.charAt(pos))) {
				while (++pos < input.length()) {
					if (!Character.isJavaIdentifierPart(input.charAt(pos))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
