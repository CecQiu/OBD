package com.hgsoft.obd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.ObdRedisData;
/**
 * OBD状态：在线，离线
 * @author sujunguang
 * 2016年5月16日
 * 下午3:56:49
 */
@Component
public class ObdService {
	@Resource
	private JedisUtil jedisUtil;
	
	/**
	 * 
	 * 设置设备的ttl超时时间
	 * @param obdSn
	 * @param timeout 默认60秒
	 * @return
	 */
	public boolean obdTTL(String obdSn,Integer timeout){
		timeout = (timeout == null ? 60 : timeout);//默认60秒
		return jedisUtil.set(ObdRedisData.OBD_TTL_KEY+obdSn, obdSn, timeout);
	}
	
	/**
	 * 设备是否在线
	 * @param obdSn
	 * @return true在线 false离线
	 */
	public boolean obdOnLine(String obdSn){
		return jedisUtil.ttl(ObdRedisData.OBD_TTL_KEY+obdSn) > 0 ? true : false;
	}
	
	public void removeTTL(String obdSn){
		jedisUtil.del(ObdRedisData.OBD_TTL_KEY+obdSn);
	}
}
