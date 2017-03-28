package com.hgsoft.common.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.common.utils.Pager;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.service.ModuleService;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author liujiefeng
 * @date May 19, 2010
 * @Description action基类
 */

@Controller
@Scope("prototype")
public class BaseAction{
	protected Result result;//返回JSP结果
	protected String message;//返回JSP详细信息
	protected String siteTitle = "车主通";

    protected String redirectUrl;
    protected String redirectAction;
	@SuppressWarnings("rawtypes")
	protected List list;
	protected Admin operator;
	@Resource
    protected Pager pager;
	@Resource
    private ModuleService moduleService;
	/** 通过在外部注入所需对象 **/
	@Resource
	protected DataSource dataSource;
	
	protected static final String SUCCESS = "success";
	protected static final String ERROR = "error";
	protected static final String INPUT = "input";
	protected static final String LOGIN = "login";
	protected static final String ADD = "add";
	protected static final String EDIT = "edit";
	protected static final String LIST = "list";
    protected static final String INFO = "info";
    protected static final String REDIRECT = "redirect";
    protected static final String REDIRECTACTION = "redirectAction";

    protected HttpServletRequest request = ServletActionContext.getRequest();
	@PostConstruct
	public void init() {
		operator = (Admin) ActionContext.getContext().getSession().get("operator");
	}
	public String add(){
		return ADD;
	}
	public String edit(){
		return EDIT;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
	@SuppressWarnings({ "rawtypes" })
	public List getList() {
		return list;
	}
	public String getMessage() {
		return message;
	}
	
	public String getRedirectAction() {
		return redirectAction;
	}
	public void setRedirectAction(String redirectAction) {
		this.redirectAction = redirectAction;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBasePath(){
		return ServletActionContext.getRequest().getContextPath();
	}
	public void setMid(Integer id) {
		ActionContext.getContext().getSession().put("currentPosition", moduleService.getCurrentPosition(id));
	}
	public String getResult(){
		if(result == null){
			return "";
		}
		return result.toString();
	}
	
	public static enum Result{
		SUCCESS,FAIL,RCSUCCESS,FAIL1,FAIL2,FAIL3,FAIL4,FAIL5,FAIL6,FAIL7,
	}
	
	public Date getNow(){
		return new Date();
	}
	
	protected void outJsonMessage(String json) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	protected void outMessage(String message) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(message.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

    protected String getCurrentClientIp(){
        String clientHost = request.getRemoteAddr();

        return clientHost;
    }
	/**
	 * 提供getter，方便页面取值
	 * @return
	 */
	public Admin getOperator() {
		return operator;
	}
	public void setOperator(Admin operator) {
		this.operator = operator;
	}

    public String getSiteTitle() {
		return siteTitle;
	}
	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}
	public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
	
	/**
	 * 取得HttpRequest的简化方法.
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 取得HttpResponse的简化方法.
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 取得HttpSession的简化方法.
	 */
	public static ServletContext getApplication() {
		return ServletActionContext.getServletContext();
	}
	
	/**
	 * 取得HttpSession的简化方法.
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}
	
	/**
	 * 获取账号的角色
	 */
	public String getAccountRole() {
		operator = (Admin) ActionContext.getContext().getSession().get("operator");
		return operator.getRole().getName();
	}
	
	/**
	 * 获取账号的角色
	 */
	public static String CurrentAccountRole() {
		Admin admin = (Admin) ActionContext.getContext().getSession().get("operator");
		return admin.getRole().getName();
	}
	
	public String getCurrentTimeStr(){
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		outMessage(sf.format(date));
		return null;
	}
}