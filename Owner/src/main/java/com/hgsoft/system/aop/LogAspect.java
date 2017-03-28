package com.hgsoft.system.aop;


import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.service.SystemLogService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class LogAspect {
    private Logger logger = LogManager.getLogger("servicePermission");
	private Admin operator;
	@Resource
	private SystemLogService systemLogService;

	private static List<String> list = new ArrayList<String>();

	static {
		list.add("java.lang.Integer");
		list.add("java.lang.Object");
		list.add("java.lang.String");
		list.add("java.lang.Float");
		list.add("java.lang.Double");
		list.add("java.lang.Byte");
		list.add("java.lang.Char");
		list.add("java.lang.Long");
		list.add("java.lang.Boolean");
		list.add("java.lang.Short");
		list.add("java.util.Date");
		list.add("java.sql.Date");
	}

	public LogAspect() {

	}

	/**
	 * 添加时切入点
	 */
	@Pointcut("execution(* com.hgsoft.*.service.*.save*(..)) && !execution(* com.hgsoft.*.service.SystemLogService.save*(..))")
	public void addServiceCall() {
	};

	/**
	 * 更新时切入点
	 */
	@Pointcut("execution(* com.hgsoft.*.service.*.update*(..))")
	public void updateServiceCall() {
	};

	/**
	 * 删除时切入点
	 */
	@Pointcut("execution(* com.hgsoft.*.service.*.delete*(..))")
	public void deleteServiceCall() {
	};

	@AfterReturning(value = "addServiceCall()", returning = "rtv", argNames = "rtv")
	public void addServiceCallCalls(JoinPoint joinPoint, Object rtv) {
		this.operator = (Admin) ActionContext.getContext().getSession().get("operator");
		if (operator == null) {
			return;
		}
		if (joinPoint.getArgs() == null) {
			return;
		}
		writeLog(2, joinPoint);

	}

	@AfterReturning(value = "updateServiceCall()", returning = "rtv", argNames = "rtv")
	public void updateServiceCallCalls(JoinPoint joinPoint, Object rtv) {
		this.operator = (Admin) ActionContext.getContext().getSession().get("operator");
		if (operator == null) {
			return;
		}
		if (joinPoint.getArgs() == null) {
			return;
		}
		writeLog(3, joinPoint);

	}

	@AfterReturning(value = "deleteServiceCall()", returning = "rtv", argNames = "rtv")
	public void deleteServiceCalls(JoinPoint joinPoint, Object rtv) {
		this.operator = (Admin) ActionContext.getContext().getSession().get("operator");
		if (operator == null) {
			return;
		}
		if (joinPoint.getArgs() == null) {
			return;
		}
		writeLog(1, joinPoint);

	}

	private void writeLog(int logType, JoinPoint joinPoint) {
		String coverage = "";
		switch (logType) {
            case 1: {
                coverage = "删除";
                break;
            }
            case 2: {
                coverage = "添加";
                break;
            }
            case 3: {
                coverage = "更新";
                break;
            }
            default: {
                coverage = "未知";
            }
		}
        String msg = "{}({})执行{}，logData：{}";
        String targetClass = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		String opContent = optionContent(joinPoint.getArgs(), methodName,targetClass);
        logger.info(msg, operator.getName(), operator.getId(), coverage, opContent);
	}

	private String optionContent(Object[] args, String mName,String targetClass) {

		if (args == null) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		rs.append("[id:" + operator.getId() + ",name:" + operator.getName() + "]调用["+targetClass+"]");
		rs.append(mName);
		String className = null;
		for (Object info : args) {
			className = info.getClass().getName();
			// className = className.substring(className.lastIndexOf(".")+1);
			rs.append("[参数类型：" + className + ",值：");

			// 判断参数类型是够是基础类型，是直接调用toString，并且继续下一个
			if (list.contains(className)) {
				rs.append(info.toString() + "]");
				continue;
			}
			// 当参数类型不是基础类型，遍历其Method方法，获取包含id，name的get方法，并获取执行的值
			Method[] methods = info.getClass().getDeclaredMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.indexOf("get") == -1) {
					continue;
				} else if (!methodName.toLowerCase().contains("name") && !methodName.toLowerCase().contains("id")) {
					continue;
				}
				Object rsValue = null;
				try {
					rsValue = method.invoke(info);
					if (rsValue == null) {
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				rs.append("(" + methodName.replaceFirst("get","") + ":" + rsValue + ")");
			}
			rs.append("]");
		}

		return rs.toString();
	}

}
