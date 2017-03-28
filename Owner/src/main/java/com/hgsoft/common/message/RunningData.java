/**
 * 
 */
package com.hgsoft.common.message;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liujilain
 * 保存会话消息
 */
public class RunningData {
	/**
	 * ID->用户会话
	 */
	private static final Map<String, ChannelHandlerContext> ID_SESSION_MAP = new ConcurrentHashMap<String, ChannelHandlerContext>();
	/**
	 * 会话请求和响应消息
	 */
	private static final Map<String, Object> ID_RESPONSE_MAP = new HashMap<String, Object>();
	/**
	 * 客户端会话请求和响应消息
	 */
	private static final Map<String, Object> ID_CLIENT_MAP = new HashMap<String, Object>();
	
	/**
	 * 客户端会话请求和响应消息
	 */
	private static final Map<String,Date> obdStateMap = new ConcurrentHashMap<String,Date>();
	
	/**
	 * 获取map数据
	 */
	public static Map<String, ChannelHandlerContext> getIdSessionMap(){
		return ID_SESSION_MAP;
	}
	
	/**
	 * 获取客户端响应回来的消息
	 */
	public static Map<String, Object> getIdResponseMap(){
		return ID_RESPONSE_MAP;
	}
	/**
	 * 获取第三方服务器端响应回来的消息
	 */
	public static Map<String, Object> getIdClientMap(){
		return ID_CLIENT_MAP;
	}
	
	/**
	 * 获取第三方服务器端响应回来的消息
	 */
	public static Map<String,Date> getObdStateMap(){
		return obdStateMap;
	}
	
	
}
