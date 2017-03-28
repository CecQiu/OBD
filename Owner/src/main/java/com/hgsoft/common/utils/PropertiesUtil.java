package com.hgsoft.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * 读取配置文件值
 * @author Sjg
 * 2014-8-14
 * 上午11:52:38
 */
public class PropertiesUtil {

	private static PropertiesUtil propUtil = null;
	
	private static Properties prop = new Properties();
	
	private static Map<String,PropertiesUtil> map = new HashMap<String,PropertiesUtil>();
	
	private PropertiesUtil(String resourceName){
		try {
			prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(resourceName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static PropertiesUtil getInstance(String resourceName){
		if(propUtil == null || map.get(resourceName) == null){
			propUtil = new PropertiesUtil(resourceName);
			map.put(resourceName, propUtil);
		}else{
			propUtil = map.get(resourceName);
		}
		return propUtil;
	}
	
	public String readProperty(String key){
		return readProperty(key,null);
	}
	
	public String readProperty(String key,String defaultValue){
		String value = prop.getProperty(key);
		return StringUtils.isEmpty(value) ? defaultValue : value;
	}
}
