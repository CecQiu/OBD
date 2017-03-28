package com.hgsoft.obd.util;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdRedisData;
/**
 * 行程参数数据
 * @author sujunguang
 * 2016年6月21日
 * 下午3:01:49
 */
@Component
public class TravelDataUtil {
	@Resource
	private JedisUtil jedisUtil;
	
	/**
	 * 获取设备对应的上次上电序号
	 * @param obdSn
	 * @return
	 */
	public Integer getUpElectricNo(String obdSn){
		String upElectricNo = jedisUtil.get(ObdRedisData.UpElectricNo+obdSn, null);
		if(upElectricNo == null){
			return null;
		}
		return Integer.valueOf(upElectricNo);
	}
	
	/**
	 * 保存设备上电序号
	 * @param obdSn
	 * @param upElectricNo
	 * @throws OBDException
	 */
	public void putUpElectricNo(String obdSn, Integer upElectricNo) throws OBDException{
		if(!StringUtils.isEmpty(obdSn) && upElectricNo != null){
			jedisUtil.set(ObdRedisData.UpElectricNo+obdSn, upElectricNo.toString());
		}else{
			throw new OBDException("设置设备上电号不能为空！obdSn<"+obdSn+">,upElectricNo:"+upElectricNo);
		}
	}
	
	/**
	 * 行程开始时间
	 * @param obdSn
	 * @return
	 */
	public String getTravelStart(String obdSn){
		return getTravelTime(obdSn, ObdRedisData.TravelStart);
	}
	/**
	 * 行程结束时间
	 * @param obdSn
	 * @return
	 */
	public String getTravelEnd(String obdSn){
		return getTravelTime(obdSn, ObdRedisData.TravelEnd);
	}
	/**
	 * 设置行程开始时间
	 * @param obdSn
	 * @param travelStart
	 * @return
	 */
	public Long putTravelStart(String obdSn, String travelStart){
		return putTravelTime(obdSn, ObdRedisData.TravelStart, travelStart);
	}
	/**
	 * 设置行程结束时间
	 * @param obdSn
	 * @param travelEnd
	 * @return
	 */
	public Long putTravelEnd(String obdSn, String travelEnd){
		return putTravelTime(obdSn, ObdRedisData.TravelEnd, travelEnd);
	}
	
	/**
	 * 获取行程时间
	 * @param obdSn
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getTravelTime(String obdSn, String travelTimeType){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hget(ObdRedisData.TravelData_ObdSn+obdSn, travelTimeType);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 设置行程时间
	 * @param obdSn
	 * @param  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public Long putTravelTime(String obdSn, String travelTimeType, String travelTimeValue){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hset(ObdRedisData.TravelData_ObdSn+obdSn, travelTimeType, travelTimeValue);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 获取行程距离
	 * @param obdSn
	 * @return
	 */
	public String getTravelMile(String obdSn){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hget(ObdRedisData.TravelData_ObdSn+obdSn, ObdRedisData.TravelMile);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 设置行程距离
	 * @param obdSn
	 * @param travelMile
	 * @return
	 */
	public Long putTravelMile(String obdSn,String travelMile){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hset(ObdRedisData.TravelData_ObdSn+obdSn, ObdRedisData.TravelMile, travelMile);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 获取行程油耗
	 * @param obdSn
	 * @return
	 */
	public String getTravelOil(String obdSn){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hget(ObdRedisData.TravelData_ObdSn+obdSn, ObdRedisData.TravelOil);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 设置行程油耗
	 * @param obdSn
	 * @param  travelOil
	 * @return
	 */
	public Long putTravelOil(String obdSn,String travelOil){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hset(ObdRedisData.TravelData_ObdSn+obdSn, ObdRedisData.TravelOil, travelOil);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	public Long clearTravelTime(String obdSn, String travelTimeType){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hdel(ObdRedisData.TravelData_ObdSn+obdSn, travelTimeType);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	public Long clearTravelStart(String obdSn){
		return clearTravelTime(obdSn, ObdRedisData.TravelStart);
	}
	public Long clearTravelEnd(String obdSn){
		return clearTravelTime(obdSn, ObdRedisData.TravelEnd);
	}
	public Long clearTravelMile(String obdSn){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hdel(ObdRedisData.TravelData_ObdSn+obdSn, ObdRedisData.TravelMile);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	public Long clearTravelOil(String obdSn){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hdel(ObdRedisData.TravelData_ObdSn+obdSn, ObdRedisData.TravelOil);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
}
