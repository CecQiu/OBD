package com.hgsoft.obd.util;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

import com.hgsoft.carowner.entity.Fence;
import com.hgsoft.carowner.service.FenceService;
import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.ObdRedisData;
/**
 * 推送工具类
 * @author sujunguang
 * 2016年3月17日
 * 上午10:08:07
 */
@Component
public class PushUtil {
	
	/**
	 * 设备号_类型，次数，时间
	 */
	public static final Map<String, Map<Integer,Date>> PushApiTime = new ConcurrentHashMap<>();
	@Resource
	private JedisUtil jedisUtil;
	@Resource
	private FenceService fenceService;
	
	private Map<Integer,Set<Integer>> fenceNum(String obdSn){
		Map<Integer,Set<Integer>> map = new HashMap<>();
		List<Fence> fences = fenceService.queryListByObdSn(obdSn, null, null);
		Set<Integer> num1 = new HashSet<>();
		Set<Integer> num2 = new HashSet<>();
		Set<Integer> num3 = new HashSet<>();
		for (Fence fence : fences) {
			Integer number = fence.getAreaNum();
			Integer alert = fence.getAlert();//1进2出3进出
			if(StringUtils.isEmpty(alert)){
				continue;
			}
			switch (alert) {
			case 1:
				num1.add(number);
				break;
			case 2:
				num2.add(number);
				break;
			case 3:
				num3.add(number);
				break;
			default:
				break;
			}
		}
		map.put(1, num1);
		map.put(2, num2);
		map.put(3, num3);
		return map;	
	}
	
	public void cleanAllPushByRedis(String obdSn){
		WarnType[] wts = WarnType.values();
		Map<Integer,Set<Integer>> numbers = fenceNum(obdSn);
		Set<Integer> nums = null;
		for (WarnType warnType : wts) {
			if(warnType == WarnType.Efence_In){
				nums = numbers.get(1);
			}else if(warnType == WarnType.Efence_Out){
				nums = numbers.get(2);
			}else if(warnType == WarnType.Efence_InOut_In || warnType == WarnType.Efence_InOut_Out){
				nums = numbers.get(3);
			}
			if(nums != null && nums.size() > 0){
				for (Integer num : nums) {
					cleanPushByRedis(obdSn+"_"+warnType+num);
				}
			}else{
				cleanPushByRedis(obdSn+"_"+warnType);//清除所有预警类型记录
			}
		}
	}
	
	public void cleanFencePushByRedis(String obdSn,WarnType warnType,Integer num){
			cleanPushByRedis(obdSn+"_"+warnType+num);
	}
	/**
	 * 清空设备推送的数据
	 * @param obdSn_type
	 */
	public void cleanPushByRedis(String obdSn_type){
		Jedis jedis =  null;
		String obdPushKey = ObdRedisData.OBD_Push_KEY+obdSn_type;
		String obdPushCountField = ObdRedisData.OBD_Push_CountField+obdSn_type;
		String obdPushTimeField = ObdRedisData.OBD_Push_TimeField+obdSn_type;
		try {
			jedis =	jedisUtil.getJedis();
			jedis.hdel(obdPushKey, obdPushCountField,obdPushTimeField);
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
	}
	
	/**
	 * 设备是否能继续推送
	 * @param obdSn_type 设备_类型
	 * @param pushDate 时间
	 * @return
	 */
	public boolean canPushByRedis(String obdSn_type,Date pushDate){
		Jedis jedis =  null;
		String obdPushKey = ObdRedisData.OBD_Push_KEY+obdSn_type;
		String obdPushCountField = ObdRedisData.OBD_Push_CountField+obdSn_type;
		String obdPushTimeField = ObdRedisData.OBD_Push_TimeField+obdSn_type;
		try {
			jedis =	jedisUtil.getJedis();
			if(jedis.hexists(obdPushKey, obdPushCountField)){
				Integer count = Integer.valueOf(jedis.hget(obdPushKey, obdPushCountField));
				if(count == 2){
					return false;
				}else if(count == 1){
					long pushTime = Long.valueOf(jedis.hget(obdPushKey, obdPushTimeField));
					if(pushDate.getTime() - pushTime >= 2*60*1000 ){
						jedis.hset(obdPushKey, obdPushCountField, "2");
						jedis.hset(obdPushKey, obdPushTimeField, String.valueOf(pushDate.getTime()));
						return true;
					}
				}else
					return true;
			}else{
				jedis.hset(obdPushKey, obdPushCountField, "1");
				jedis.hset(obdPushKey, obdPushTimeField, String.valueOf(pushDate.getTime()));
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * 获取设备推送的数据
	 * @param obdSn_type
	 * @return
	 */
	public Map<String,Long> getPushByRedis(String obdSn_type){
		Map<String,Long> map = null;
		Jedis jedis =  null;
		String obdPushKey = ObdRedisData.OBD_Push_KEY+obdSn_type;
		String obdPushCountField = ObdRedisData.OBD_Push_CountField+obdSn_type;
		String obdPushTimeField = ObdRedisData.OBD_Push_TimeField+obdSn_type;
		try {
			jedis =	jedisUtil.getJedis();
			if(jedis.hexists(obdPushKey, obdPushCountField)){
				map = new HashMap<String,Long>();
				map.put(jedis.hget(obdPushKey, obdPushCountField),Long.valueOf(jedis.hget(obdPushKey, obdPushTimeField)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
		return map;
	}
	
	/*
	 * 是否还能继续推送
	 * 1、是否包含设备号
	 * 2、不包含，插入，作为第一次
	 * 3、包含，是否有第一次
	 * 4、是：时间是否超过两分钟，是：作为第二次；
	 * 5、否：不能再插入，返回false
	 * 
	 */
	public static boolean canPush(String obdSn_type,Date pushDate){
		if(PushApiTime.containsKey(obdSn_type)){
			Map<Integer,Date> map = PushApiTime.get(obdSn_type);
			if(map.containsKey(2)){//已经两次了
				return false;
			}else if(map.containsKey(1)){
				Date d1 = map.get(1);
				if(pushDate.getTime() - d1.getTime() >= 2*60*1000 ){//大于两分钟
					map.put(2, pushDate);
					PushApiTime.put(obdSn_type, map);
					return true;
				}else{
					return false;
				}
			}else
				return true;
		}else{
			Map<Integer,Date> map = new HashMap<>();
			map.put(1, pushDate);
			PushApiTime.put(obdSn_type, map);
			return true;
		}
	}
	
	/**
	 * 清空设备号对应的推送记录信息
	 * @param obdSn
	 */
	public static void clearPush(String obdSn){
		WarnType[] wts = WarnType.values();
		for (WarnType warnType : wts) {
			PushApiTime.remove(obdSn+"_"+warnType);//清除所有预警类型记录
		}
//		PushApiTime.remove(obdSn+"_"+WarnType.OverSpeed);
//		PushApiTime.remove(obdSn+"_"+WarnType.IllegalStartUp);
//		PushApiTime.remove(obdSn+"_"+WarnType.IllegalShock);
//		PushApiTime.remove(obdSn+"_"+WarnType.Efence_In);
//		PushApiTime.remove(obdSn+"_"+WarnType.Efence_Out);
//		PushApiTime.remove(obdSn+"_"+WarnType.Efence_InOut_In);
//		PushApiTime.remove(obdSn+"_"+WarnType.Efence_InOut_Out);
	}
	
	/**
	 * 清空设备号+预警类型：对应的推送记录信息
	 * @param obdSn
	 */
	public static void clearPushByType(String obdSn,WarnType type){
			PushApiTime.remove(obdSn+"_"+type);//清除指定预警类型记录
	}
}
