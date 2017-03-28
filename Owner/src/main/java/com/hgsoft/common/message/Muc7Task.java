package com.hgsoft.common.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import com.hgsoft.carowner.entity.CarDriveInfo;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.CarOilInfo;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.CarDriveInfoService;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.CarOilInfoService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.PositionalInformationService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * Mcu7上传行程记录接口任务类
 * @author fdf
 */
@Component
public class Muc7Task {
	
	private final Log logger = LogFactory.getLog(Muc7Task.class);
	private static Logger obdTravelLogger = LogManager.getLogger("obdTravelLogger");

	@Resource
	private CarTraveltrackService cts;
	@Resource 
	private CarOilInfoService carOilInfoService;
	@Resource 
	private CarInfoService carInfoService;
	@Resource
	private CarTraveltrackService carTraveltrackService;
	@Resource
	private PositionalInformationService pis;
	@Resource
	private CarDriveInfoService cdis;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	/**
	 * 
	 */
	public boolean doTask(OBDMessage om) {
//		boolean flag = true;
//		String msg = om.getMsgBody();
//		CarTraveltrack ct = new CarTraveltrack(IDUtil.createID());
//		String obdSn=om.getId();
//		ct.setObdsn(obdSn);
//		logger.info(obdSn+"***********行程记录开始");
//		obdTravelLogger.info(obdSn+"***********行程记录开始");
//		ct.setInsesrtTime(new Date());
//		ct.setMessage(om.getMessage());
//		
//		int[] fileLens = {12, 2, 12, 4, 2, 4, 4, 4, 4, 4, 4, 2, 4, 4, 4, 2, 4, 4, 2};
//		String[] files = new String[fileLens.length];
//		int totalLen = 0;
//		for(int i=0; i<fileLens.length; i++) {
//			int len = fileLens[i];
//			files[i] = msg.substring(totalLen, totalLen + len);
//			totalLen += len;
//		}
//		String travelEnd = files[0];//行程结束
//		String travelNo = files[1];//行程序号
//		String travelStart = files[2];//行程开始
//		String distance = files[3];//距离
//		String speed = files[4];//最大速度
//		String overspeedTime = files[5];//超速次数
//		String brakesNum = files[6];//急刹车次数
//		String quickTurn = files[7];//急转弯次数
//		String quickenNum = files[8];//急加速次数
//		String quickSlowDown = files[9];//急减速次数
//		String quickLaneChange = files[10];//急变道次数
//		String temperature = files[11];//发动机最高水温
//		String rotationalSpeed = files[12];//发动机最高工作转速,实际值要除以4
//		String engineMaxSpeed = files[13];//发动机最高工作转速次数
//		String speedMismatch = files[14];//车速转速不匹配次数
//		String voltage = files[15];//电压值，取到值除以10
//		String totalFuel = files[16];//总油耗
//		//平均油耗修改为怠速次数
//		String idling=files[17]; //怠速次数
//		String driverTime = files[18];//疲劳驾驶时长
//		
//		logger.info("行程原始结束时间:"+travelEnd+"*******原始行程开始时间:"+travelStart);
//		obdTravelLogger.info("原始行程结束时间:"+travelEnd+"*******原始行程开始时间:"+travelStart);
//		//系统结束时间
//		try {
//			ct.setTravelEnd(new SimpleDateFormat("yyyyMMddHHmmss").parse("20"+travelEnd));
//			logger.info(obdSn+"***行程记录行程结束时间:"+(String)DateUtil.fromatDate(ct.getTravelEnd(), "yyyy-MM-dd HH:mm:ss"));
//			obdTravelLogger.info(obdSn+"***行程记录行程结束时间:"+(String)DateUtil.fromatDate(ct.getTravelEnd(), "yyyy-MM-dd HH:mm:ss"));
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//			logger.error(e1);
//			obdTravelLogger.error(e1);
//			logger.error(obdSn+"***行程记录结束时间不正确:"+travelEnd);
//			obdTravelLogger.error(obdSn+"***行程记录结束时间不正确:"+travelEnd);
//			ct.setTravelEnd(new Date());
//		}
//		
//		//系统开始时间
//		try {
//			ct.setTravelStart(new SimpleDateFormat("yyyyMMddHHmmss").parse("20"+travelStart));
//			logger.info(obdSn+"***行程记录行程开始时间:"+(String)DateUtil.fromatDate(ct.getTravelStart(), "yyyy-MM-dd HH:mm:ss"));
//			obdTravelLogger.info(obdSn+"***行程记录行程开始时间:"+(String)DateUtil.fromatDate(ct.getTravelStart(), "yyyy-MM-dd HH:mm:ss"));
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//			logger.error(obdSn+"***行程记录开始时间不正确:"+travelStart);
//			obdTravelLogger.error(obdSn+"***行程记录开始时间不正确:"+travelStart);
//			ct.setTravelStart(new Date());
//		}
//		
//		
//		ct.setTravelNo(Integer.parseInt(travelNo, 16));
//		logger.info(obdSn+"***行程序号:"+ct.getTravelNo());
//		obdTravelLogger.info(obdSn+"***行程序号:"+ct.getTravelNo());
//		
//		ct.setDistance(Long.parseLong(distance, 16));
//		logger.info(obdSn+"***距离:"+ct.getDistance());
//		obdTravelLogger.info(obdSn+"***距离:"+ct.getDistance());
//		
//		ct.setSpeed(Integer.parseInt(speed, 16));
//		logger.info(obdSn+"***最大速度:"+ct.getSpeed());
//		obdTravelLogger.info(obdSn+"***最大速度:"+ct.getSpeed());
//		
//		ct.setOverspeedTime(Integer.parseInt(overspeedTime, 16));
//		logger.info(obdSn+"***超速次数:"+ct.getOverspeedTime());
//		obdTravelLogger.info(obdSn+"***超速次数:"+ct.getOverspeedTime());
//		
//		ct.setBrakesNum(Long.parseLong(brakesNum, 16));
//		logger.info(obdSn+"***急刹车次数:"+ct.getBrakesNum());
//		obdTravelLogger.info(obdSn+"***急刹车次数:"+ct.getBrakesNum());
//		
//		ct.setQuickTurn(Integer.parseInt(quickTurn,16));
//		logger.info(obdSn+"***急转弯次数:"+ct.getQuickTurn());	
//		obdTravelLogger.info(obdSn+"***急转弯次数:"+ct.getQuickTurn());	
//			
//		ct.setQuickenNum(Long.parseLong(quickenNum, 16));
//		logger.info(obdSn+"***急加速次数:"+ct.getQuickenNum());	
//		obdTravelLogger.info(obdSn+"***急加速次数:"+ct.getQuickenNum());	
//		
//		ct.setQuickSlowDown(Integer.parseInt(quickSlowDown, 16));
//		logger.info(obdSn+"***急减速次数:"+ct.getQuickSlowDown());	
//		obdTravelLogger.info(obdSn+"***急减速次数:"+ct.getQuickSlowDown());	
//		
//		ct.setQuickLaneChange(Integer.parseInt(quickLaneChange, 16));
//		logger.info(obdSn+"***急变道次数:"+ct.getQuickLaneChange());	
//		obdTravelLogger.info(obdSn+"***急变道次数:"+ct.getQuickLaneChange());	
//
//		if(!"ff".equals(temperature)){
//			ct.setTemperature(Integer.parseInt(temperature, 16)-40);
//		}else{
//			ct.setTemperature(0);
//			logger.info(obdSn+"*****行程记录的发动机最高水温为ff");
//		}
////		ct.setTemperature(Integer.parseInt(temperature, 16)-40);
//		logger.info(obdSn+"***发动机最高水温:"+ct.getTemperature());	
//		obdTravelLogger.info(obdSn+"***发动机最高水温:"+ct.getTemperature());	
//	
//		ct.setRotationalSpeed(Long.parseLong(rotationalSpeed, 16));
//		logger.info(obdSn+"***发动机最高工作转速:"+ct.getRotationalSpeed());	
//		obdTravelLogger.info(obdSn+"***发动机最高工作转速:"+ct.getRotationalSpeed());	
//		
//		ct.setEngineMaxSpeed(Integer.parseInt(engineMaxSpeed, 16));
//		logger.info(obdSn+"***发动机最高工作转速次数:"+ct.getEngineMaxSpeed());	
//		obdTravelLogger.info(obdSn+"***发动机最高工作转速次数:"+ct.getEngineMaxSpeed());	
//		
//		ct.setSpeedMismatch(Integer.parseInt(speedMismatch, 16));
//		logger.info(obdSn+"***车速转速不匹配次数:"+ct.getSpeedMismatch());	
//		obdTravelLogger.info(obdSn+"***车速转速不匹配次数:"+ct.getSpeedMismatch());	
//		
//		ct.setVoltage(((double)Integer.parseInt(voltage, 16))/10);
//		logger.info(obdSn+"***电压值:"+ct.getVoltage());	
//		obdTravelLogger.info(obdSn+"***电压值:"+ct.getVoltage());	
//		//总油耗要乘以10
//		ct.setTotalFuel(Long.parseLong(totalFuel, 16)*10);
//		logger.info(obdSn+"***总油耗:"+ct.getTotalFuel());	
//		obdTravelLogger.info(obdSn+"***总油耗:"+ct.getTotalFuel());	
//		
//		ct.setIdling(Integer.parseInt(idling, 16));
//		logger.info(obdSn+"***怠速次数:"+ct.getIdling());
//		obdTravelLogger.info(obdSn+"***怠速次数:"+ct.getIdling());
//		
//		ct.setDriverTime(Integer.parseInt(driverTime, 16));
//		logger.info(obdSn+"***疲劳驾驶时长:"+ct.getDriverTime());
//		obdTravelLogger.info(obdSn+"***疲劳驾驶时长:"+ct.getDriverTime());
//		
//		try {
//			//根据行程开始时间和行程结束时间以及obdSn号查询是否存在当前记录
//			CarTraveltrack ctt= cts.getTravelByObdAndTime(obdSn,ct.getTravelStart(), ct.getTravelEnd());
//			if(ctt!=null){
//				logger.info(obdSn+"*****当前设备行程记录存在相同记录:"+(String)DateUtil.fromatDate(ct.getTravelEnd(), "yyyy-MM-dd HH:mm:ss")+"***开始时:"+(String)DateUtil.fromatDate(ct.getTravelStart(), "yyyy-MM-dd HH:mm:ss"));
//				obdTravelLogger.info(obdSn+"*****当前设备行程记录存在相同记录:"+(String)DateUtil.fromatDate(ct.getTravelEnd(), "yyyy-MM-dd HH:mm:ss")+"***开始时:"+(String)DateUtil.fromatDate(ct.getTravelStart(), "yyyy-MM-dd HH:mm:ss"));
//				flag = false;
//				logger.info(obdSn+"***********行程记录结束");
//				obdTravelLogger.info(obdSn+"***********行程记录结束");
//				return flag;
//			}
//			
//			cts.add(ct);//插入行程记录
//			
//			//如果时间不对，都是12个0,不保存油耗数据
//			String zeroStr=StrUtil.strSame(12, "0");
//			boolean mark= true;
//			if(travelEnd.equals(zeroStr)||travelStart.equals(zeroStr)){
//				mark = false;
//			}
//			String zeroSix=StrUtil.strSame(6, "0");
//			//如果时间的前面6个为0,不保存油耗数据
//			String endSub=travelEnd.substring(0, 6);
//			String startSub=travelStart.substring(0,6);
//			if(endSub.equals(zeroSix)||startSub.equals(zeroSix)){
//				mark = false;
//			}
//			
//			try {
//				if(mark){
//					//保存油耗信息
//					CarOilInfo carOilInfo = new CarOilInfo();
//					carOilInfo.setObdSN(obdSn);
//					OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//					CarInfo carInfo = carInfoService.queryCarInfoBySN(obdStockInfo.getObdSn());
//					if(carInfo != null){
//						carOilInfo.setCarId(carInfo.getCarId());
//					    carOilInfo.setMileageNum(ct.getDistance().toString());//里程
//					    carOilInfo.setOilInfoTime(ct.getTravelEnd());//油耗时间默认是行程结束时间
//					    carOilInfo.setPetrolConsumeNum(ct.getTotalFuel().toString());//油耗毫升ML 小数点3位
//						//如果时间不正确，默认时间为0
//						if((ct.getTravelEnd().getTime()-ct.getTravelStart().getTime())>0){
//							carOilInfo.setTimeSpanNum(new Long((ct.getTravelEnd().getTime()-ct.getTravelStart().getTime())/1000).toString());//驾驶时长
//						}else{
//							carOilInfo.setTimeSpanNum("0");//驾驶时长
//							logger.info(obdSn+"***行程记录的开始时间大于结束时间");
//							obdTravelLogger.info(obdSn+"***行程记录的开始时间大于结束时间");
//						}
//					    carOilInfoService.carOilSave(carOilInfo);
//					}
//				}
//				
//				//保存车辆行为表
//				CarDriveInfo cd=new CarDriveInfo();
//				cd.setDriveInfoId(IDUtil.createID());//主键id
//				cd.setObdSn(obdSn);
//				cd.setTmsSpeeding(ct.getOverspeedTime());//超速次数
//				cd.setTmsRapDec(ct.getQuickSlowDown());//急减速
//				cd.setTmsRapAcc(ct.getQuickenNum().intValue());//急加速
//				cd.setHighSpeed(ct.getEngineMaxSpeed());//高转速
//				cd.setTmsSharpTurn(ct.getQuickTurn());//急转弯
//				cd.setNotMatch(ct.getSpeedMismatch());//车速转速不匹配
//				//疲劳驾驶去掉
//				//长时间怠速去掉
//				cd.setTmsBrakes(ct.getBrakesNum().intValue());//急刹车
//				cd.setTmsSteep(ct.getQuickLaneChange());//急变道
//				cd.setDriveDate(ct.getTravelEnd());
//				cd.setAvSpeed(0.0);
//				cd.setLongLowSpeed(ct.getIdling());//怠速次数
//				cdis.carDriveInfoSave(cd);
//				logger.info(obdSn+"*****驾驶行为保存成功.");
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(obdSn+"****行程记录保存油耗数据或驾驶行为数据失败,当前设备没有车辆数据.");
//			}
//			
//			logger.info(obdSn+"***行程结束："+(String)DateUtil.fromatDate(ct.getTravelEnd(), "yyyy-MM-dd HH:mm:ss")+"**行程序号:"+ct.getTravelNo()+"**行程开始："+
//			(String)DateUtil.fromatDate(ct.getTravelStart(), "yyyy-MM-dd HH:mm:ss")+"**距离："+ct.getDistance()+"**最大速度："+ct.getSpeed()+"**超速次数："+
//			ct.getOverspeedTime()+"**急刹车次数："+ct.getBrakesNum()+"**急转弯次数："+ct.getQuickTurn()+
//			"**急加速次数："+ct.getQuickenNum()+"急减速次数："+ct.getQuickSlowDown()+"**急变道次数："+
//			ct.getQuickLaneChange()+"**发动机最高水温："+ct.getTemperature()+"**发动机最高工作转速："+
//			ct.getRotationalSpeed()+"***发动机最高工作转速次数:"+ct.getEngineMaxSpeed()
//			+"***车速转速不匹配次数:"+ct.getSpeedMismatch()
//			+"**电压值："+ct.getVoltage()+"**总油耗："+ct.getTotalFuel()+
//			"***平均油耗:"+ct.getAverageFuel()+"**疲劳驾驶时长："+ct.getDriverTime());
//			obdTravelLogger.info(obdSn+"***："+(String)DateUtil.fromatDate(ct.getTravelEnd(), "yyyy-MM-dd HH:mm:ss")+"**行程序号:"+ct.getTravelNo()+"**行程开始："+
//					(String)DateUtil.fromatDate(ct.getTravelStart(), "yyyy-MM-dd HH:mm:ss")+"**距离："+ct.getDistance()+"**最大速度："+ct.getSpeed()+"**超速次数："+
//					ct.getOverspeedTime()+"**急刹车次数："+ct.getBrakesNum()+"**急转弯次数："+ct.getQuickTurn()+
//					"**急加速次数："+ct.getQuickenNum()+"急减速次数："+ct.getQuickSlowDown()+"**急变道次数："+
//					ct.getQuickLaneChange()+"**发动机最高水温："+ct.getTemperature()+"**发动机最高工作转速："+
//					ct.getRotationalSpeed()+"***发动机最高工作转速次数:"+ct.getEngineMaxSpeed()
//					+"***车速转速不匹配次数:"+ct.getSpeedMismatch()
//					+"**电压值："+ct.getVoltage()+"**总油耗："+ct.getTotalFuel()+
//					"***怠速次数:"+ct.getIdling()+"**疲劳驾驶时长："+ct.getDriverTime());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//			obdTravelLogger.error(e);
//			logger.info(obdSn+"***********行程记录结束");
//			obdTravelLogger.info(obdSn+"***********行程记录结束");
//			flag = false;
//		}
//		
//		logger.info(obdSn+"***********行程记录结束");
//		obdTravelLogger.info(obdSn+"***********行程记录结束");
//		return flag;
		return true;
	}
	
}
