package com.hgsoft.obd.util;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdRedisData;
/**
 * 记录OBD设备在哪个服务器IP上
 * @author sujunguang
 * 2016年6月23日
 * 上午11:06:04
 */
@Component
public class ObdInHostUtil {
	@Resource
	private JedisUtil jedisUtil;
	
	/**
	 * 
	 * @param obdSn
	 * @return
	 */
	public String getObdInHost(String obdSn){
		String host = jedisUtil.get(ObdRedisData.ObdInHost+obdSn, null);
		return host;
	}
	
	/**
	 * 设置设备对应的主机名（IP）
	 * @param obdSn
	 * @param host
	 * @throws OBDException
	 */
	public void putObdInHost(String obdSn, String host) throws OBDException{
		if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(host) ){
			jedisUtil.set(ObdRedisData.ObdInHost+obdSn, host, 90);
		}else{
			throw new OBDException("设备号和直接名不能为空！");
		}
	}
	
}