package com.hgsoft.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hgsoft.carowner.action.ObdUpgradeAction;
import com.hgsoft.carowner.action.ObdUpgradeMsgAction;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.FaultCode1;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.service.CarOilInfoService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.DictionaryService;
import com.hgsoft.carowner.service.FaultCode1Service;
import com.hgsoft.carowner.service.FaultCodeReadService;
import com.hgsoft.carowner.service.FaultUploadService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
import com.hgsoft.common.utils.SpringInitUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class TT {
	
	@Resource
	private FaultUploadService fs;
	
	@Resource
	private DictionaryService dictionaryService;
	
	@Resource
	private FaultCodeReadService faultCodeReadService;
	
	@Resource
	private CarTraveltrackService carTraveltrackService;
	
	@Resource
	private OBDStockInfoService oBDStockInfoService;
	@Resource
	private FaultUploadService faultUploadService;
	@Resource
	private FaultCode1Service faultCode1Service;
	@Resource
	private CarOilInfoService carOilInfoService;
	
	@Test
	public void action(){
		ObdUpgradeMsgAction oa=new ObdUpgradeMsgAction();
		try {
			oa.list();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void ctv(){
		String obdSn="00000000000000687E9FCA50";
		CarTraveltrack sd=carTraveltrackService.findLastBySN(obdSn);
		System.out.println(sd.getObdsn());
	}
	@Test
	public void carOilListCalc(){
		String obdSn="00000000000000687e9fca4a";
		String bdate="2015-10-27 00:00:00";
		String edate="2015-10-28 23:59:59";
		List list=new ArrayList();
		list=carOilInfoService.carOilListCalc(obdSn, bdate, edate);
		System.out.println(list.size());
		for(int i=0;i<list.size();i++){
			Object[] o=(Object[])list.get(i);
			System.out.println(o[0].toString());
		}
	}
	@Test
	public void carOilCalc(){
		String obdSn="00000000000000687e9fca4a";
		String bdate="2015-10-27 00:00:00";
		String edate="2015-10-28 23:59:59";
		Object[] o=carOilInfoService.carOilCalc(obdSn, bdate, edate);
		System.out.println(o[0]==null);
//		System.out.println(o[0].toString()+"***"+o[1].toString()+"***"+o[2]);
	}
	@Test
	public void fl(){
		String obdSn="762.0";
		System.out.println(Float.parseFloat(obdSn));
	}
	@Test
	public void fc(){
		String obdSn="123";
		List<FaultUpload> fu=faultUploadService.queryByObdsnAndState(obdSn, "1");
		System.out.println(fu.size());
	}
	@Test
	public void carT(){
		String obdSn="00000000000000687e9fca52";
		Date d1=(Date)DateUtil.fromatDate("2080-01-06 08:00:50", "yyyy-MM-dd HH:mm:ss");
		Date d2=(Date)DateUtil.fromatDate("2015-11-27 12:10:46", "yyyy-MM-dd HH:mm:ss");
		CarTraveltrack ct=carTraveltrackService.getTravelByObdAndTime(obdSn, d1, d2);
		System.out.println(ct.getObdsn());
	}
	@Test
	public void faultcode1(){
//		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
		FaultCode1 f=faultCode1Service.getFaultCodeByCode("P1234");
		System.out.println(f.getFaultDesc());
	}
	@Test
	public void fault(){
//		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
		String obdSn="105010000140";
		boolean flag=faultUploadService.faultUpdate(obdSn);
		System.out.println(flag);
	}
	@Test
	public void driverDay(){
//		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
		String obdSn="105010000140";
		String starTime="2015-11-01 00:00:00";
		String endTime="2015-11-23 23:59:59";
		List<CarTraveltrack> list =carTraveltrackService.DriveBetterDay(starTime, endTime, obdSn);
		System.out.println(list.size());
	}
	@Test
	public void driver(){
//		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
		String obdSn="105010000140";
		String starTime="2015-11-20 00:00:00";
		String endTime="2015-11-23 23:59:59";
		Date d1= (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss");
		Date d2= (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss");
		List<CarTraveltrack> list =carTraveltrackService.getCarTravelsByTime(d1, d2, obdSn);
		System.out.println(list.size());
	}
	@Test
	public void drivering(){
//		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
		String obdSn="105010000140";
		String starTime="2015-11-20 00:00:00";
		String endTime="2015-11-23 23:59:59";
		CarTraveltrack cc=carTraveltrackService.DrivingBetter(starTime, endTime, obdSn);
		System.out.println(cc.getOverspeedTime()+""+cc.getBrakesNum()+""+cc.getQuickTurn()+""+cc.getQuickenNum()+""+cc.getQuickSlowDown()+""
		+cc.getQuickLaneChange()+""+cc.getEngineMaxSpeed()+""+cc.getSpeedMismatch());
	}
//	@Test
//	public void getTravels(){
////		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
//		String obdSn="0000000000000066cb82a143";
//		String mobileNumber="18988889996";
//		String license="粤AW3948";
//		String starTime="2015-10-15";
//		String endTime="2015-10-22";
//		Pager pager = new Pager();
//		List<CarTraveltrack> list = carTraveltrackService.getTravelTracks(obdSn, mobileNumber, license, starTime, endTime, pager);
//		System.out.println(list.size());
//		System.out.println(list.get(0).getMebUser().getMobileNumber());
//	}
	@Test
	public void obdExcel(){
		File file = new File("C:\\Users\\Administrator\\Desktop\\obd.xlsx");
		try {
			oBDStockInfoService.obdExcel(file, "obd.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void faultU(){
//		carTraveltrackService = (CarTraveltrackService) SpringInitUtil.getBean("carTraveltrackService");
//		List<CarTraveltrack> list = carTraveltrackService.getTravelTrack();
//		System.out.println(list.get(0).getMebUser().getMobileNumber());
	}
	
//	@Test
//	public void faultU(){
//		FaultUpload fu=new FaultUpload();
//		fu.setCreateTime(new Date());
//		fu.setFaultCode("001001");
//		fu.setObdId("asd");
//		fu.setRemark("asd");
//		fu.setState('F');
//		fs.save(fu);
//	}
//	
//	@Test
//	public void getDictionary(){
//		Dictionary dictionary = new Dictionary();
//		dictionary.setDicId(IDUtil.createID());
//		dictionary.setCode("owner.faultUpload.faultCode");
//		dictionary.setTrueValue("P1234");
//		dictionary.setShowValue("紧急刹车");
//		dictionary.setCreateTime(new Date());
//		dictionary.setCreateUser("123");
//		dictionary.setState('T');
//		dictionary.setRemark("故障码");
//		dictionaryService.save(dictionary);
//	}
//	
//	@Test
//	public void getDicByCodeAndTrueValue(){
//		Dictionary dic = new Dictionary();
//		dic.setCode("owner.faultUpload.faultCode");
//		dic.setTrueValue("P1234");
//		List<Dictionary> list=dictionaryService.getDicByCodeAndTrueValue(dic.getCode(),dic.getTrueValue());
//		for (Dictionary dictionary : list) {
//			System.out.println(dictionary.getShowValue());
//		}
//	}
//	@Test
//	public void getDicByCode(){
//		Dictionary dic = new Dictionary();
//		dic.setCode("owner.faultUpload.faultCode");
//		List<Dictionary> list=dictionaryService.getDicByCode(dic.getCode());
//		for (Dictionary dictionary : list) {
//			System.out.println(dictionary.getShowValue());
//		}
//	}
//	@Test
//	public void getSingleDicByCode(){
//		Dictionary dic = new Dictionary();
//		dic.setCode("owner.faultUpload.faultState");
//		Dictionary d=dictionaryService.getOneDicByCode(dic.getCode());
//		System.out.println(d.getShowValue());
//	}
//	@Test
//	public void getDicByEntity(){
//		Dictionary dic = new Dictionary();
//		dic.setCode("owner.faultUpload.faultState");
//		dic.setShowValue("1");
//		dic.setTrueValue("00");
////		dic.setCreateTime(new Date());
//		Pager pager=new Pager();
//		List<Dictionary> list=dictionaryService.getDicByEntity(pager, dic, null);
//		for (Dictionary dictionary : list) {
//			System.out.println(dictionary.getTrueValue());
//		}
//	}
	@Test
	public void f(){
		
	}
}
