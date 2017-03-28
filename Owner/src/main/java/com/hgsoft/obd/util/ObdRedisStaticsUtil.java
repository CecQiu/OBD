package com.hgsoft.obd.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

import com.hgsoft.common.utils.ThreadLocalDateUtil;
import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.ObdRedisData;
/**
 * Obd统计数据工具
 * @author sujunguang
 * 2016年6月24日
 * 上午9:20:51
 */
@Component
public class ObdRedisStaticsUtil {
	@Resource
	private JedisUtil jedisUtil;
	
	/**
	 * 统计发送成功次数+1
	 * @return
	 */
	public Long incrServerSendToObdStaticsSuccess(){
		return incrServerSendToObdStatics(ObdRedisData.ServerSendToObdSuccess);
	}
	
	/**
	 * 统计发送失败次数+1
	 * @return
	 */
	public Long incrServerSendToObdStaticsFail(){
		return incrServerSendToObdStatics(ObdRedisData.ServerSendToObdFail);
	}
	
	private Long incrServerSendToObdStatics(String sendType){
		String dateStr = ThreadLocalDateUtil.formatDate("yyyy-MM-dd", new Date());
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.hincrBy(sendType, dateStr, 1);
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
	
	public Map<String,Integer[]> querySendToObdStatics(String endDate, int count){
		if(count <= 0){
			count = 10;
		}
		Date date = new Date();
		if(StringUtils.isEmpty(endDate)){
			endDate = ThreadLocalDateUtil.formatDate("yyyy-MM-dd", date);
		}
		
		Jedis jedis = null;
		Map<String,Integer[]> map = new TreeMap<String, Integer[]>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		});
		Integer[] counts = null;
		try {
			jedis = jedisUtil.getJedis();
			Calendar calendar = Calendar.getInstance();
			date = ThreadLocalDateUtil.parse("yyyy-MM-dd", endDate);
			for (int i = 0; i < count; i++) {
				counts = new Integer[2];
				calendar.setTime(date);
				String dateStr = ThreadLocalDateUtil.formatDate("yyyy-MM-dd", date);
				String successCount = jedis.hget(ObdRedisData.ServerSendToObdSuccess, dateStr);
				if(StringUtils.isEmpty(successCount)){
					successCount = "0";
				}
				String failCount = jedis.hget(ObdRedisData.ServerSendToObdFail, dateStr);
				if(StringUtils.isEmpty(failCount)){
					failCount = "0";
				}
				counts[0] = Integer.valueOf(successCount);
				counts[1] = Integer.valueOf(failCount);
				map.put(dateStr, counts);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
				date = calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return map;
	}
	
	public Set<String> queryOBDOnLine(){
		Jedis jedis = null;
		try {
			jedis = jedisUtil.getJedis();
			return jedis.keys(ObdRedisData.OBD_TTL_KEY+"*");
		}catch(Exception e){
			e.printStackTrace();
			jedisUtil.returnBrokenResource(jedis);
		}finally{
			jedisUtil.returnResource(jedis);
		}
		return null;
	}
}
