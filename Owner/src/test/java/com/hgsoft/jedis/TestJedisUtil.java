package com.hgsoft.jedis;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.ObdRedisStaticsUtil;
import com.hgsoft.obd.util.PushUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestJedisUtil {
	@Resource
	private JedisUtil jedisUtil;
	
	@Resource
	private DriveTimeUtil driveTimeUtil;
	
	@Resource
	private PushUtil pushUtil;
	
	@Resource
	private ObdRedisStaticsUtil obdRedisStaticsUtil;
	
	@Test 
	public void test(){
		System.out.println(jedisUtil.set("obd", "123456", 180));
	}
	@Test 
	public void testTTL(){
		System.out.println(jedisUtil.ttl("obd"));
	}
	@Test 
	public void testGet(){
		System.out.println(jedisUtil.get("obd","null"));
	}
	
	@Test 
	public void testDriveTimeUtil(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = sdf.parse("2016-03-18 19:28:53");
			System.out.println(driveTimeUtil.isTiredDriveByRedis("88888888",d1));
			Date d2 = sdf.parse("2016-03-18 23:28:26");
			System.out.println(driveTimeUtil.isTiredDriveByRedis("88888888",d2));
			Date d3 = sdf.parse("2016-03-18 23:28:56");
			System.out.println(driveTimeUtil.isTiredDriveByRedis("88888888",d3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testDriveTimeUtil2(){
		Date now = new Date();
		for (int i = 0; i < 50; i++) {
			now.setTime(now.getTime()+11*60*1000);
			Date d = new Date(now.getTime());
			System.out.println(driveTimeUtil.isTiredDriveByRedis("88888888",d));
		}
	}
	
	@Test 
	public void testPushTimeUtil(){
		Date now = new Date();
		for (int i = 0; i < 10; i++) {
			now.setTime(now.getTime()+1*60*1000);
			Date d = new Date(now.getTime());
			System.out.println(pushUtil.canPushByRedis("88888888",d));
		}
		pushUtil.cleanPushByRedis("88888888");
	}
	
	@Test 
	public void testPushTimeUtil2(){
		Date now = new Date();
		for (int i = 0; i < 10; i++) {
			now.setTime(now.getTime()+1*60*1000);
			Date d = new Date(now.getTime());
			System.out.println(pushUtil.canPush("88888888",d));
		}
	}

	
	@Test 
	public void testTriedAndRested(){
		Date now = new Date();
		for (int i = 0; i < 25; i++) {
			now.setTime(now.getTime()+11*60*1000);
			Date d = new Date(now.getTime());
			boolean b = driveTimeUtil.isTired2DriveByRedis("88888888",d);
			System.out.println();
			System.out.print( b +  " , ");
//			if(b)
//				System.out.println(driveTimeUtil.isRestedDriveByRedis("88888888",d));
		}
//		for (int i = 0; i < 10; i++) {
//			now.setTime(now.getTime()+21*60*1000);
//			Date d = new Date(now.getTime());
//			System.out.print(driveTimeUtil.isTired2DriveByRedis("88888888",d) +  " , ");
//			System.out.println(driveTimeUtil.isRestedDriveByRedis("88888888",d));
//		}
	}
	
	@Test 
	public void testServerSendToObdStatics(){
		System.out.println(obdRedisStaticsUtil.querySendToObdStatics("2016-08-01",0));
	}

	@Test 
	public void testKeys(){
		System.out.println(obdRedisStaticsUtil.queryOBDOnLine());
	}
	
}
