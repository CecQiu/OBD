package com.hgsoft.application.util;

import java.util.Date;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class TokenListener implements HttpSessionListener, ServletContextListener,ServletContextAttributeListener {

	public TokenListener() {
		System.out.println("listener is creating");
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println(arg0.getSession().getId() + "session created");
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		String username = (String)session.getAttribute("username");
		String sessionId = session.getId();
		SessionUtil.clearLogin(username, sessionId);
		System.out.println(username + "  " + new Date() + "  destroyed");
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent arg0) {
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent arg0) {
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent arg0) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	}
	
}
