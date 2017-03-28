package com.hgsoft.application.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class SessionUtil {

	private static Map<String, String> userSesion
		= new HashMap<String, String>();
	
	private static SessionUtil util = new SessionUtil();
	private SessionUtil(){}
	
	public static SessionUtil getIntence() {
		return util;
	}
	
	public static String getUsers(String username) {
		if(StringUtils.isEmpty(username)) {
			return null;
		} else {
			return userSesion.get(username);
		}
	}
	
	public static void reLogin(String username, String sessionId) {
		userSesion.put(username, sessionId);
	}
	
	public static Boolean hasLogin(String username, String sessionId) {
		if(userSesion.containsKey(username)) {
			return false;
		}
		userSesion.put(username, sessionId);
		return true;
	}
	
	public static void clearLogin(String username, String sessionId) {
		userSesion.remove(sessionId);
	}

	public static String checkLoginUser(HttpServletRequest request, String uri) {
		String username = request.getParameter("username");
		long last_time = 0L;
		if(request.getSession().getAttribute("lastTime")!= null) {
			last_time = (long) request.getSession().getAttribute("lastTime");
		}
		if(StringUtils.isEmpty(username)) {
			return "无效用户";
		} else if(!uri.contains("user_login.do") && !uri.contains("user_register.do")
				&& !uri.contains("user_checkUserName.do")) {
			String session = userSesion.get(username);
			if(StringUtils.isEmpty(session)) {
				if(request.getSession().getAttribute("username") != null) {
					return "你的账号在别的地方登陆";
				}
				return "你还没有登陆";
			} else if(System.currentTimeMillis() - last_time < 200) {//每秒限制五次
				return "无效请求";
			}
		} else if(System.currentTimeMillis() - last_time < 200) {//每秒限制五次
			return "无效请求";
		}
		return null;
	}
}