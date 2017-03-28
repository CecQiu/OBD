package com.hgsoft.jedis;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
/**
 * Jedis操作工具类
 * @author sujunguang
 * 2016年9月28日
 * 上午10:13:05
 */
@Component
public class JedisServiceUtil {
	
	@Resource
	private JedisUtil jedisUtil;
	
	
	/**
	 * 设置ttl超时时间
	 * @param key
	 * @param obdSn
	 * @param timeout 默认60秒
	 * @return
	 */
	public boolean ttl(String key, String obdSn, Integer timeout){
		timeout = (timeout == null || timeout.intValue() < 0 ? 60 : timeout);//默认60秒
		return jedisUtil.set(key+obdSn, obdSn, timeout);
	}

	/**
	 * 获取ttl时间
	 * @param key
 	 * @param obdSn
	 * @return
	 */
	public Long getTTL(String key, String obdSn){
		return jedisUtil.ttl(key);
	}
	
	/**
	 * ttl是否有效
	 * @param key
	 * @param obdSn
	 * @return 
	 */
	public boolean validTTL(String key, String obdSn){
		return jedisUtil.ttl(key+obdSn) > 0 ? true : false;
	}
	/**
	 * 设置HSET
	 * @param key
	 * @param field
	 * @param value
	 */
	public void putHSetByRedis(String key, String field, String value){
		//如果设置的值跟已有值一致则不再设置
		if(!StringUtils.isEmpty(value) && value.equals(getHSetByRedis(key,field))){
			return;
		}
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(jedis);
		}
	}
	
	/**
	 * 获取HSET值
	 * @param key
	 * @param field
	 * @return
	 */
	public String getHSetByRedis(String key, String field){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			if(jedis.hexists(key, field)){
				return jedis.hget(key, field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(jedis);
		}
		return null;
	}
	
	/**
	 * 关闭Jedis
	 * @param jedis
	 */
	private void closeJedis(Jedis jedis){
		if(jedis != null)
			jedis.close();
	}
}