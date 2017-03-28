package com.hgsoft.common.interceptor;

import com.hgsoft.application.util.OutMessageUtil;
import com.hgsoft.application.util.SessionUtil;
import com.hgsoft.application.vo.ApplicationVo;
import com.hgsoft.application.vo.Status;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.Module;
import com.hgsoft.system.service.AdminService;
import com.hgsoft.system.service.ModuleService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import org.apache.commons.collections4.CollectionUtils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("rawtypes")
public class PermissionInterceptor implements Interceptor {

    private static Logger urlLogger = LogManager.getLogger("UrlPermission");

	private static final long serialVersionUID = 1L;
	@Resource
    private AdminService adminService;
    @Resource
    private ModuleService moduleService;
	
	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext request = ActionContext.getContext();
		String basePath = ServletActionContext.getRequest().getContextPath();
		String uri = ServletActionContext.getRequest().getRequestURI().replaceFirst(basePath, "");
        String requestUri = ServletActionContext.getRequest().getRequestURI();
        String queryStr = ServletActionContext.getRequest().getQueryString();
		
		Map session = request.getSession();
		Admin operator = (Admin)session.get("operator");

		//if(!uri.equals("/login.do") && !uri.equals("/randomCode.do")){
		if((uri.contains("/admin/") || uri.contains("/testOBD/")) && !uri.contains("/login.do")){
			if(operator != null){
				operator = adminService.find(operator.getId());
				session.put("operator", operator);
			}
			if(operator == null){
				request.put("message", "您还未登录或者登录已超时，请重新登陆");
				request.put("result", "RELOAD");
				return "login";
			}

			String functions = ";" + adminService.getFunctions(operator) + ";";
			session.put("functions", functions);
			
			if(functions == null || !functions.contains(";" + uri + ";")){
				request.put("message", "您的操作权限不足，请联系管理员");
				return "error";
			}
            List<Module> resourceModuleList = moduleService.getResource(operator,uri);
            if(CollectionUtils.isNotEmpty(resourceModuleList)){
                StringBuffer authResourceBuf= new StringBuffer() ;
                Iterator<Module> iterator = resourceModuleList.iterator();
                while (iterator.hasNext()){
                    Module resourceModule = iterator.next();
                    authResourceBuf.append(resourceModule.getResourceType());
                    if(iterator.hasNext()){
                        authResourceBuf.append(";");
                    }
                }
                session.put("authResource",authResourceBuf.toString());
            }
            urlLogger.info("{}({})访问url：{},queryString:{}", operator.getName(),operator.getUsername(),requestUri,queryStr);
		} else if(uri.contains("/application/")){
			if(uri.contains("getToken.do")) {
				if(ServletActionContext.getRequest().getSession().getAttribute("lastTime") != null) {
					long last_time = (long) ServletActionContext.getRequest().getSession().getAttribute("lastTime");
					if(System.currentTimeMillis() - last_time < 100) {
						OutMessageUtil.outMessage("无效请求");
						return null;
					}
				}
			} else {
				String check = SessionUtil.checkLoginUser(ServletActionContext.getRequest(), uri);
				if(!StringUtils.isEmpty(check)) {
					OutMessageUtil.outJsonMessage(new ApplicationVo(Status.ERROR, check).getJson());
					return null;
				}
			}
		}
		ServletActionContext.getRequest().getSession().setAttribute("lastTime", System.currentTimeMillis());
		return invocation.invoke();
	}

	public void init() {
	}
	
	public void destroy() {
	}
}
