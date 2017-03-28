package com.hgsoft.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.common.utils.HTTPFetcher;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.util.JsonDateValueProcessor;
import com.hgsoft.system.service.AdminService;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:applicationContext.xml"})
public class Test {

	@Autowired
	private AdminService adminService;
	
	@org.junit.Test
	public void t() {
		System.out.println(adminService.find(1));
	}
	
	public static void main(String[] args) throws Exception {
		/*
		System.out.println("苏俊光ljl88sjg28".hashCode());
		System.out.println("刘家林ljl88sjg28".hashCode());
		System.out.println(new MessageParseHandler());
		System.out.println(new MessageParseHandler());
		System.out.println(StrUtil.obdSnChange("4411610217533"));//4411610217533
		*/
		ObdHandShake obdHandShake = new ObdHandShake();
		obdHandShake.setCreateTime(new Date());
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("time", new Date());
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());   
        JSONObject json = JSONObject.fromObject(obdHandShake, jsonConfig);
		System.out.println(json);
		String jsonStr = json.toString();
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));
		ObdHandShake shake = (ObdHandShake) JSONObject.toBean(json, ObdHandShake.class);
		System.out.println(shake);
		System.out.println(StrUtil.format2Char("aa300007da0b003e00028082161207083613ffffffff08000900113204482310061600ff21ffff7d7c012300000000000001010100000100000000000000000000000300ffff45aa"));
		
		System.out.println(JSONObject.fromObject("null") == null);
		System.out.println(JSONObject.fromObject("null").equals(null));
		System.out.println(JSONObject.fromObject("null").equals("null"));
		System.out.println(JSONObject.fromObject("null").isEmpty());
		System.out.println(JSONObject.fromObject("null").isNullObject());
		System.out.println(JSONObject.fromObject(null).isEmpty());
		System.out.println(JSONObject.fromObject(null).isNullObject());
//		System.out.println((JSONNull)JSONObject.fromObject("null") == JSONNull.getInstance());
//		
//		JSONObject jsonObject = null;
//		System.out.println(jsonObject == null);
		distance();
		
		double x0 = 1;
		double y0 = 1;
		Map<String,double[]> groupP = new ConcurrentHashMap<String, double[]>();
		groupP.put("44", new double[]{2,2});
		groupP.put("45", new double[]{2,1});
		groupP.put("46", new double[]{3,3});
		System.out.println(pointToGroupNum(x0, y0, groupP));
		
		System.out.println(Math.abs("30102f13".hashCode())%4 );
		
		System.out.println(StrUtil.strAppend(Integer.toHexString(Integer.parseInt("65534")),4,0,"0"));
	}
	
	public static void distance(){
		double x1 = 23.123;
		double y1 = 112.221;
		double x2 = 22.234;
		double y2 = 113.234;
		double y = y2-y1;
		double x = x2-x1;
		System.out.println(Math.sqrt(x*x + y*y));
		List list = new ArrayList<Double>();
		list.add(x1);
		list.add(y1);
		list.add(x2);
		list.add(y2);
		Collections.sort(list);;
		System.out.println(list);
	}
	
	public static String pointToGroupNum(double x0, double y0, Map<String,double[]> groupP){
		List<Double> list = new ArrayList<Double>();
		Map<Double,String> distanceGroupNum = new ConcurrentHashMap<Double, String>();
		for (String groupNum : groupP.keySet()) {
			double[] pXY = groupP.get(groupNum);
			double y = pXY[1]-y0;
			double x = pXY[0]-x0;
			double distance = Math.sqrt(x*x + y*y);
			distanceGroupNum.put(distance, groupNum);
			list.add(distance);
		}
		Collections.sort(list);
		return distanceGroupNum.get(list.get(0));
	}
	
	@org.junit.Test
	public void testBind() throws Exception{
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try {
					HTTPFetcher httpFetcher = new HTTPFetcher();
					JSONObject data = new JSONObject();
					String deviceId = "4411600020849";
					String username = "chezhutong";
					String time = "1447134959847";
					String pwd = "czt123456";
					String sign = MD5Coder.encodeMD5Hex(deviceId+username+time+pwd);
					data.put("deviceId", deviceId);
					data.put("username", username);
					data.put("time", time);
					data.put("sign", sign);
					data.put("userId", "13922444784");
					data.put("hgDeviceSn", 123456);
					data.put("userType", 1);
					String result = httpFetcher.post("http://localhost:6060/Owner/api/obd/bind", data.toString());
					System.out.println(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
		new Thread(r).start();
		Thread.sleep(2000);
	}
}
