package com.hgsoft.obd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.ObdTravelParamsService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.ObdRedisData;
/**
 * 疲劳驾驶判断工具类
 * @author sujunguang
 * 2016年2月26日
 * 下午5:20:14
 */
@Component
public class DriveTimeUtil {
	
	@Resource
	private ObdTravelParamsService obdTravelParamsService;
	@Resource
	private JedisUtil jedisUtil;
	@Resource
	private PushApi pushApi;
	@Resource
	private ObdSateService obdSateService;
	
	/**
	 * 驾驶疲劳时间参数
	 */
	public static final Map<String, Map<String,Integer>> OBD_LimitDriveTime = new ConcurrentHashMap<>();
	
	/**
	 * 疲劳驾驶
		连续开4小时，不休息20分钟的。
		时间：
		t1
		t2
		t3
		t4
		t5
		t6
		t7
		t8
		...
		1、时间间隔大于20分钟的，清空前面所有时间。
		2、时间间隔小于20分钟的，不清空，
			获得第一个时间，计算时长是否超过4小时。
			超过，报警，清空。
			不超过，继续等待。
		
		开始时间t1
		最新时间t2
		当前时间t3
	 * OBD驾驶时间-->>判断是否疲劳驾驶
	 * OBD设备号String，开始时间，最新时间
	 */
	public static final Map<String, Map<String,Date>> OBD_DriveTime = new ConcurrentHashMap<>();
	/**
	 * 统计设备疲劳驾驶次数
	 */
	public static final Map<String, Integer> OBD_DriveTime_Count = new ConcurrentHashMap<>();

	public static void putToDriveTimeCount(String obdSn,Integer count){
		if(OBD_DriveTime_Count.containsKey(obdSn)){
			Integer c = OBD_DriveTime_Count.get(obdSn);
			OBD_DriveTime_Count.put(obdSn, c+count);
		}else{
			OBD_DriveTime_Count.put(obdSn, count);
		}
	}
	
	public static Integer getDriveTimeCount(String obdSn){
		if(OBD_DriveTime_Count.containsKey(obdSn)){
			Integer c = OBD_DriveTime_Count.get(obdSn);
			return c;
		}else{
			return 0;
		}
	}

	/**
	 * 是否疲劳驾驶
	 * @param obdSn
	 * @param date
	 * @return
	 */
	public boolean isTiredDrive(String obdSn,Date date){
		int restTime = 20;//分钟
		int driveTime = 4*60;//分钟
		synchronized (obdSn) {
			//获取驾驶疲劳参数
			if(OBD_LimitDriveTime.containsKey(obdSn)){
				restTime = OBD_LimitDriveTime.get(obdSn).get("restTime");
				driveTime = OBD_LimitDriveTime.get(obdSn).get("driveTime");
			}else{
				OBDTravelParams obdTravelParams = obdTravelParamsService.queryByObdSn(obdSn);
				if(obdTravelParams != null && obdTravelParams.getFatigueSleep() != null
						&& obdTravelParams.getFatigueDrive() != null){
					restTime = obdTravelParams.getFatigueSleep();
					driveTime = obdTravelParams.getFatigueDrive();
				}
				Map<String,Integer> map = new HashMap<>();
				map.put("restTime", restTime);
				map.put("driveTime", driveTime);
				OBD_LimitDriveTime.put(obdSn, map);
			}
			
			//是否已经有OBD设备号key
			if(OBD_DriveTime.containsKey(obdSn)){
				//是
				Map<String,Date> timeMap = OBD_DriveTime.get(obdSn);
				//是否有开始时间，没有则把date作为开始时间，有则把date作为最新时间
				if(timeMap.containsKey("startTime")){
					if(timeMap.containsKey("newTime")){
						//有最新的，则date为当前时间，判断：当前时间-最新时间是否大于20分钟，是：清空。否：比较开始时间，大于4小时，则疲劳。
						Date newTime = timeMap.get("newTime");
						if(date.getTime()-newTime.getTime() > restTime * 60 * 1000){
							OBD_DriveTime.remove(obdSn);
							return false;
						}else{
							Date startTime = timeMap.get("startTime");
							if(date.getTime() - startTime.getTime() > driveTime * 60 * 1000){//疲劳驾驶
								OBD_DriveTime.remove(obdSn);
								putToDriveTimeCount(obdSn,1);
								return true;
							}
							timeMap.put("newTime", date);//不是疲劳驾驶，继续讲当前传进来的date作为最新时间
						}
					}else{
						timeMap.put("newTime", date);//没有最新时间，则将date作为第一个
					}
				}else{
					timeMap.put("startTime", date);//没有开始时间，则将date作为第一个
				}
				OBD_DriveTime.put(obdSn, timeMap);
			}else{
				//否
				Map<String,Date> timeMap = new ConcurrentHashMap<>();
				timeMap.put("startTime", date);
				OBD_DriveTime.put(obdSn, timeMap);
			}
		}
		return false;
	}
	
	/**
	 * 清除驾驶疲劳限制参数：驾驶时间，休息时间
	 * @param obdSn
	 * @return
	 */
	public Long cleanLimitDriveTimeByRedis(String obdSn){
		String obdLimitKey = ObdRedisData.OBD_LimitDriveTime_KEY+obdSn;
		String obdDriveTimeField = ObdRedisData.OBD_LimitDriveTime_DriveTimeField+obdSn;
		String obdRestTimeField = ObdRedisData.OBD_LimitDriveTime_RestTimeField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hdel(obdLimitKey, obdDriveTimeField,obdRestTimeField);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 获得设备疲劳驾驶数据
	 * @param obdSn
	 * @return
	 */
	public Map<String,String>  getTiredDriveByRedis(String obdSn){
		Map<String,String> map = new HashMap<>();
		String obdLimitKey = ObdRedisData.OBD_LimitDriveTime_KEY+obdSn;
		String obdDriveTimeField = ObdRedisData.OBD_LimitDriveTime_DriveTimeField+obdSn;
		String obdRestTimeField = ObdRedisData.OBD_LimitDriveTime_RestTimeField+obdSn;
		
		String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdStartTimeField = ObdRedisData.OBD_DriveTime_StartTimeField + obdSn;
		String obdNewTimeField = ObdRedisData.OBD_DriveTime_NewTimeField + obdSn;
		
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			if(jedis.hexists(obdLimitKey,obdDriveTimeField)){
				map.put("driveTime", jedis.hget(obdLimitKey,obdDriveTimeField));
			}
			if(jedis.hexists(obdLimitKey,obdRestTimeField)){
				map.put("restTime", jedis.hget(obdLimitKey,obdRestTimeField));
			}
			
			if(jedis.hexists(obdDriveKey,obdStartTimeField)){
				map.put("startTime", jedis.hget(obdDriveKey,obdStartTimeField));
			}
			if(jedis.hexists(obdDriveKey,obdNewTimeField)){
				map.put("newTime", jedis.hget(obdDriveKey,obdNewTimeField));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally{
			jedisUtil.returnResource(jedis);
		}
		return map;
	}
	
	/**
	 * 设备是否疲劳驾驶
	 * @param obdSn
	 * @param date
	 * @return
	 */
	public boolean isTiredDriveByRedis(String obdSn,Date date){
		int restTime = 20;//分钟
		int driveTime = 4*60;//分钟
		String obdLimitKey = ObdRedisData.OBD_LimitDriveTime_KEY+obdSn;
		String obdDriveTimeField = ObdRedisData.OBD_LimitDriveTime_DriveTimeField+obdSn;
		String obdRestTimeField = ObdRedisData.OBD_LimitDriveTime_RestTimeField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			synchronized (obdSn) {
				//获取驾驶疲劳参数
				if(jedis.hexists(obdLimitKey,obdDriveTimeField) && jedis.hexists(obdLimitKey,obdRestTimeField)){
					driveTime  = Integer.valueOf(jedis.hget(obdLimitKey,obdDriveTimeField));
					restTime = Integer.valueOf(jedis.hget(obdLimitKey,obdRestTimeField));
				}else{
					OBDTravelParams obdTravelParams = obdTravelParamsService.queryByObdSn(obdSn);
					if(obdTravelParams != null && obdTravelParams.getFatigueSleep() != null
							&& obdTravelParams.getFatigueDrive() != null){
						restTime = obdTravelParams.getFatigueSleep();
						driveTime = obdTravelParams.getFatigueDrive();
					}
					
					jedis.hset(obdLimitKey, obdDriveTimeField, String.valueOf(driveTime));
					jedis.hset(obdLimitKey, obdRestTimeField, String.valueOf(restTime));
				}
				
				String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
				String obdStartTimeField = ObdRedisData.OBD_DriveTime_StartTimeField + obdSn;
				String obdNewTimeField = ObdRedisData.OBD_DriveTime_NewTimeField + obdSn;
				long current = date.getTime();
				// 是否已经有OBD设备号key
				if (jedis.hexists(obdDriveKey, obdStartTimeField)) {
					
					long startTime = Long.valueOf(jedis.hget(obdDriveKey, obdStartTimeField));
					
					if (jedis.hexists(obdDriveKey, obdNewTimeField)) {
						// 有最新的，则date为当前时间，判断：当前时间-最新时间是否大于20分钟，是：清空。否：比较开始时间，大于4小时，则疲劳。
						long newTime = Long.valueOf(jedis.hget(obdDriveKey, obdNewTimeField));
						if (current - newTime > restTime * 60 * 1000) {
							jedis.hdel(obdDriveKey, obdStartTimeField,obdNewTimeField);
							return false;
						} else {
							if (current - startTime > driveTime * 60 * 1000) {// 疲劳驾驶
								jedis.hdel(obdDriveKey, obdStartTimeField,obdNewTimeField);
								return true;
							}
							jedis.hset(obdDriveKey, obdNewTimeField, String.valueOf(current)); // 不是疲劳驾驶，继续将当前传进来的date作为最新时间
						}
					} else {
						jedis.hset(obdDriveKey, obdNewTimeField, String.valueOf(current)); // 没有最新时间，则将date作为第一个
					}
				}else{
					//否
					jedis.hset(obdDriveKey, obdStartTimeField, String.valueOf(current));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally{
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * 是否发生驾驶疲劳
	 * @param obdSn
	 * @param date
	 * @return
	 */
	public boolean isTired2DriveByRedis(String obdSn,Date date){
		int driveTime = 4*60;//分钟
		String obdLimitKey = ObdRedisData.OBD_LimitDriveTime_KEY+obdSn;
		String obdDriveTimeField = ObdRedisData.OBD_LimitDriveTime_DriveTimeField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			synchronized (obdSn) {
				//获取驾驶疲劳参数
				if(jedis.hexists(obdLimitKey,obdDriveTimeField)){
					driveTime  = Integer.valueOf(jedis.hget(obdLimitKey,obdDriveTimeField));
				}else{
					OBDTravelParams obdTravelParams = obdTravelParamsService.queryByObdSn(obdSn);
					if(obdTravelParams != null  && obdTravelParams.getFatigueDrive() != null){
						driveTime = obdTravelParams.getFatigueDrive();
					}
					jedis.hset(obdLimitKey, obdDriveTimeField, String.valueOf(driveTime));
				}
				
				String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
				String obdStartTimeField = ObdRedisData.OBD_DriveTime_StartTimeField + obdSn;
				long current = date.getTime();
				// 是否已经有OBD设备号key
				if (jedis.hexists(obdDriveKey, obdStartTimeField)) {
					long startTime = Long.valueOf(jedis.hget(obdDriveKey, obdStartTimeField));
					if (current - startTime > driveTime * 60 * 1000) {// 疲劳驾驶
						return true;
					}
				}else{
					//否
					jedis.hset(obdDriveKey, obdStartTimeField, String.valueOf(current));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally{
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	
	
	
	
	/**
	 * 驾驶疲劳，记录开始休息
	 */
	public void startRestByRedis(String obdSn, Date date) {
		String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdRestTimeField = ObdRedisData.OBD_DriveTime_RestTimeField	+ obdSn;
		startByRedis(obdSn,date,obdDriveKey,obdRestTimeField);
	}
	
	/**
	 * 驾驶疲劳，记录开始驾驶
	 */
	public void startDriveByRedis(String obdSn, Date date) {
		String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdRestTimeField = ObdRedisData.OBD_DriveTime_DriveTimeField + obdSn;
		startByRedis(obdSn,date,obdDriveKey,obdRestTimeField);
	}

	/**
	 * 设备进入休眠时间点（完整行程上传）
	 */
	public void enterSleepDateByRedis(String obdSn, Date date) {
		String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdEnterSleepDateField = ObdRedisData.OBD_DriveTime_EnterSleepDateField + obdSn;
		startByRedis(obdSn,date,obdDriveKey,obdEnterSleepDateField);
	}
	
	private void startByRedis(String obdSn, Date date,String key,String field) {
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			if (!jedis.hexists(key, field)) {
				jedis.hset(key, field, DateUtil.getTimeString(date, "yyyy-MM-dd HH:mm:ss"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
	}
	

	/**
	 * 清空开始驾驶时间
	 * @return
	 */
	public Long cleanStartDriveByRedis(String obdSn){
		String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdDriveTimeField = ObdRedisData.OBD_DriveTime_DriveTimeField	+ obdSn;
		return cleanDriveByRedis(obdSn,obdDriveKey,obdDriveTimeField);
	}
	
	/**
	 * 清空开始休息时间
	 * @return
	 */
	public Long cleanStartRestByRedis(String obdSn){
		String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdRestTimeField = ObdRedisData.OBD_DriveTime_RestTimeField	+ obdSn;
		return cleanDriveByRedis(obdSn,obdDriveKey,obdRestTimeField);
	}
	
	private Long cleanDriveByRedis(String obdSn,String key,String field){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			if (jedis.hexists(key, field)) {
				return jedis.hdel(key, field);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
		return null;
	}

	/**
	 * 是否达到休息时间，消除驾驶疲劳
	 * @param obdSn
	 * @param date
	 * @return
	 */
	public boolean isRestedByRedis(String obdSn,Date date){
		int restTime = 20;//分钟
		String obdLimitKey = ObdRedisData.OBD_LimitDriveTime_KEY+obdSn;
		String obdRestTimeField = ObdRedisData.OBD_LimitDriveTime_RestTimeField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			synchronized (obdSn) {
				//获取驾驶疲劳参数
				if(jedis.hexists(obdLimitKey,obdRestTimeField)){
					restTime = Integer.valueOf(jedis.hget(obdLimitKey,obdRestTimeField));
				}else{
					OBDTravelParams obdTravelParams = obdTravelParamsService.queryByObdSn(obdSn);
					if(obdTravelParams != null && obdTravelParams.getFatigueSleep() != null ){
						restTime = obdTravelParams.getFatigueSleep();
					}
					jedis.hset(obdLimitKey, obdRestTimeField, String.valueOf(restTime));
				}
				
				String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
				String restTimeField = ObdRedisData.OBD_DriveTime_RestTimeField + obdSn;
				long current = date.getTime();
				if (jedis.hexists(obdDriveKey, restTimeField)) {
					String  restTimeStr = jedis.hget(obdDriveKey, restTimeField);
					long newTime = ((Date)DateUtil.fromatDate(restTimeStr, "yyyy-MM-dd HH:mm:ss")).getTime();
					if (current - newTime - (30 * 1000) > restTime * 60 * 1000) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally{
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * 是否达到疲劳驾驶，发生驾驶疲劳
	 * @param obdSn
	 * @param date
	 * @return
	 */
	public boolean isTiredByRedis(String obdSn,Date date){
		int driveTime = 4*60;//分钟
		String obdLimitKey = ObdRedisData.OBD_LimitDriveTime_KEY+obdSn;
		String obdLimitDriveTimeField = ObdRedisData.OBD_LimitDriveTime_DriveTimeField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			synchronized (obdSn) {
				//获取驾驶疲劳参数
				if(jedis.hexists(obdLimitKey,obdLimitDriveTimeField)){
					driveTime  = Integer.valueOf(jedis.hget(obdLimitKey,obdLimitDriveTimeField));
				}else{
					OBDTravelParams obdTravelParams = obdTravelParamsService.queryByObdSn(obdSn);
					if(obdTravelParams != null  && obdTravelParams.getFatigueDrive() != null){
						driveTime = obdTravelParams.getFatigueDrive();
					}
					jedis.hset(obdLimitKey, obdLimitDriveTimeField, String.valueOf(driveTime));
				}
				
				String obdDriveKey = ObdRedisData.OBD_DriveTime_KEY;
				String obdDriveTimeField = ObdRedisData.OBD_DriveTime_DriveTimeField + obdSn;
				long current = date.getTime();
				// 是否已经有OBD设备号key
				if (jedis.hexists(obdDriveKey, obdDriveTimeField)) {
					String  obdDriveTime = jedis.hget(obdDriveKey, obdDriveTimeField);
					long startTime = ((Date)DateUtil.fromatDate(obdDriveTime, "yyyy-MM-dd HH:mm:ss")).getTime();
					if (current - startTime - (30 * 1000) > driveTime * 60 * 1000) {// 疲劳驾驶
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally{
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * 完整行程上传之后，是否达到休眠超时
	 * @param obdSn
	 * @param date
	 * @return
	 */
	public boolean isNoSleepOver(String obdSn){
		String obdDriveTimeKey = ObdRedisData.OBD_DriveTime_KEY;
		String obdEnterSleepDateField = ObdRedisData.OBD_DriveTime_EnterSleepDateField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			if(jedis.hexists(obdDriveTimeKey,obdEnterSleepDateField)){
				Date enterSleepDate = (Date)DateUtil.fromatDate(jedis.hget(obdDriveTimeKey,obdEnterSleepDateField),"yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				if(now.getTime()-enterSleepDate.getTime() <= 3*60*1000){//3分钟
					return true;
				}
				//清除
				jedis.hdel(obdDriveTimeKey,obdEnterSleepDateField);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally{
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * 是否正在驾驶中。。。
	 * @param obdSn
	 * @return
	 */
	public boolean isDrivingByRedis(String obdSn){
		return isOneTureByRedis(obdSn,ObdRedisData.OBD_DriveTime_KEY,
				ObdRedisData.OBD_DriveTime_DrivingField+obdSn);
	}

	/**
	 * 是否正在休息中。。。
	 * @param obdSn
	 * @return
	 */
	public boolean isRestingByRedis(String obdSn){
		return isOneTureByRedis(obdSn,ObdRedisData.OBD_DriveTime_KEY,
				ObdRedisData.OBD_DriveTime_RestingField+obdSn);
	}
	
	public boolean isOneTureByRedis(String obdSn,String key,String field){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			if(jedis.hexists(key, field)){
				String state = jedis.hget(key, field);
				return "1".equals(state) ? true : false;
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
	 * 记录此设备驾驶状态
	 * @param obdSn
	 * @param state 1-正在行使中 0-无 
	 */
	public void putDrivingByRedis(String obdSn,String state){
		putByRedis(obdSn,ObdRedisData.OBD_DriveTime_KEY,
				ObdRedisData.OBD_DriveTime_DrivingField+obdSn,state);
	}
	
	/**
	 * 记录此设备休息状态
	 * @param obdSn
     * @param state 1-正在休息中 0-无 
	 */
	public void putRestingByRedis(String obdSn,String state){
		putByRedis(obdSn,ObdRedisData.OBD_DriveTime_KEY,
				ObdRedisData.OBD_DriveTime_RestingField+obdSn,state);
	}
	
	private void putByRedis(String obdSn, String key, String field, String value){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
	}

	/**
	 * 获得全部疲劳驾驶
	 * @return
	 * @throws Exception 
	 */
	public Map<String,String> hGetAllTiredDrive() throws Exception{
		String key = ObdRedisData.OBD_DriveTime_KEY;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
			throw new Exception(e);
		} finally {
			jedisUtil.returnResource(jedis);
		}
	}
	
	/**
	 * 记录发送过疲劳驾驶（产生）
	 * @param obdSn
	 */
	public void sendTiredByRedis(String obdSn){
		String key = ObdRedisData.OBD_DriveTime_KEY;
		String field = ObdRedisData.OBD_DriveTime_SendTriedField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			jedis.hset(key, field, "1");
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
	}
	
	/**
	 * 是否已经发送了疲劳驾驶（产生）
	 * @param obdSn
	 * @return
	 */
	public boolean sendedTiredByRedis(String obdSn){
		String key = ObdRedisData.OBD_DriveTime_KEY;
		String field = ObdRedisData.OBD_DriveTime_SendTriedField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
		return false;
	}
	
	/**
	 * 移除发生疲劳驾驶记录
	 * @param obdSn
	 */
	public void removeSendedTiredByRedis(String obdSn){
		String key = ObdRedisData.OBD_DriveTime_KEY;
		String field = ObdRedisData.OBD_DriveTime_SendTriedField+obdSn;
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
		    jedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		} finally {
			jedisUtil.returnResource(jedis);
		}
	}
	
	/**
	 * 重新设置参数，需要清除数据
	 * @param obdSn
	 */
	public void resetParamsClean(String obdSn){
		cleanLimitDriveTimeByRedis(obdSn);
//		removeSendedTiredByRedis(obdSn);
		cleanStartDriveByRedis(obdSn);
		cleanStartRestByRedis(obdSn);
		//重置时，消除疲劳告警
		cleanTiredPushWarn(obdSn);
	}
	
	/**
	 * 已经发生疲劳告警前提，则推送疲劳消除告警
	 * @param obdSn
	 */
	public void cleanTiredPushWarn(String obdSn){
		if(sendedTiredByRedis(obdSn)){//已经发送了疲劳驾驶才有消除疲劳的告警
			removeSendedTiredByRedis(obdSn);//移除发生疲劳告警
			ObdState obdState = obdSateService.queryByObdSn(obdSn);
			if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
					&& "1".equals(obdState.getFatigueDriveSwitch())){
				pushApi.pushWarnHandler(obdSn,WarnType.FatigueDriveCancel,"00", "消除疲劳驾驶告警");
			}
		}
	}
	
	public static void main(String[] args) throws ParseException {
//		Date now = new Date();
//		for (int i = 0; i < 50; i++) {
//			now.setTime(now.getTime()+11*60*1000);
//			Date d = new Date(now.getTime());
//			System.out.println(d);
			DriveTimeUtil driveTimeUtil = new DriveTimeUtil();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = sdf.parse("2016-03-18 19:28:53");
			System.out.println(driveTimeUtil.isTiredDrive("300007ed",d1));
			Date d2 = sdf.parse("2016-03-18 23:28:26");
			System.out.println(driveTimeUtil.isTiredDrive("300007ed",d2));
			Date d3 = sdf.parse("2016-03-18 23:28:56");
			System.out.println(driveTimeUtil.isTiredDrive("300007ed",d3));
			System.out.println(OBD_DriveTime);
//		}
		System.out.println("疲劳驾驶次数：" +getDriveTimeCount("300007ed"));
	}
	
}
