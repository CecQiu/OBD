package com.hgsoft.common.interceptor;

import com.hgsoft.application.util.OutMessageUtil;
import com.hgsoft.application.vo.ApplicationVo;
import com.hgsoft.application.vo.Status;
import com.hgsoft.system.entity.Admin;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

@Component
public class ErrorInterceptor implements Interceptor {

    private static Logger errorLog = LogManager.getLogger("errorLogger");

	private static final long serialVersionUID = 1L;


    public void destroy() {
    }

    public void init() {}

    public String intercept(ActionInvocation invocation) throws Exception {

        String result = null; // Action的返回值
        try {
            // 运行被拦截的Action,期间如果发生异常会被catch住
            result = invocation.invoke();
            return result;
        } catch (Exception e) {
            /**
             * 记录exception信息输出到界面
             */
            ServletActionContext.getRequest().setAttribute("message",e.getMessage());
            /**
             * log4j记录日志
             */
            Admin operator =(Admin)ActionContext.getContext().getSession().get("operator");
            if(operator!=null){
                errorLog.error("{}({})用户操作报错",operator.getName(),operator.getUsername());
            }
            errorLog.error(e);
            String basePath = ServletActionContext.getRequest().getContextPath();
            String uri = ServletActionContext.getRequest().getRequestURI().replaceFirst(basePath, "");
            if(uri.contains("/application/")) {
            	OutMessageUtil.outJsonMessage(new ApplicationVo(Status.ERROR, "服务器内部错误").getJson());
            	return null;
            }
            StringBuffer errorStrck = new StringBuffer();
            for(StackTraceElement traceElement:e.getStackTrace()){
                errorStrck.append("\tat ");
                errorStrck.append(traceElement);
                errorStrck.append("\n");
            }
            errorLog.error(errorStrck.toString());
            return "error";
        }
	}

}
