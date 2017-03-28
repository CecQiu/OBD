package com.hgsoft.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author liujialin
 * @date 20150721
 * @desc 加载spring配置文件
 *
 */
public class SpringInitUtil {
	private static final String configFile="applicationContext.xml";
	private static ApplicationContext applicationContext;
	// ApplicationContext对象只要加载一次就可以了
	static{
		applicationContext = new ClassPathXmlApplicationContext(configFile);
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	public static Object getBean(String obj){
		return getApplicationContext().getBean(obj);
	}
}
