package com.hgsoft.obd.handler;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.CarDriveInfo;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.CarOilInfo;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.CarDriveInfoService;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.CarOilInfoService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.ObdHandShakeService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.common.utils.ThreadLocalDateUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.WarnType;

/**
 * 设备上传通讯——行程记录
 * 
 * @author sujunguang 2015年12月12日 下午4:26:20
 */
@Service
public class MessageObdTravelHandler implements IMessageObd {

	private static Logger obdHandlerTravelLogger = LogManager.getLogger("obdHandlerTravelLogger");
	@Resource
	private CarTraveltrackService carTraveltrackService;
	@Resource
	private CarDriveInfoService carDriveInfoService;
	@Resource
	private CarOilInfoService carOilInfoService;
	@Resource
	private CarInfoService carInfoService;
	@Resource
	private ObdHandShakeService obdHandShakeService;
	@Resource
	private DriveTimeUtil driveTimeUtil;
	@Resource
	private PushApi pushApi;
	@Resource
	private ObdSateService obdSateService;
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		String obdSn = message.getId();
		obdHandlerTravelLogger.info("-------------------【设备上传通讯——行程记录】---------------------");
		obdHandlerTravelLogger.info("--------------报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		obdHandlerTravelLogger.info("------------设备："+ obdSn +"------------");
		String retrunMsgBody = "success";
		String[] cutStrs;// 截取结果数组
		String msgBody = message.getMsgBody();
		//行程中的部分数据分离出来做为驾驶行为数据记录
		CarTraveltrack carTraveltrack = new CarTraveltrack(IDUtil.createID());//行程
		CarDriveInfo carDriveInfo = new CarDriveInfo();//驾驶行为
		CarOilInfo carOilInfo = new CarOilInfo();//油耗
		carTraveltrack.setObdsn(obdSn);
		carDriveInfo.setObdSn(obdSn);
		carOilInfo.setObdSN(obdSn);
		CarInfo carInfo = carInfoService.queryCarInfoBySN(obdSn);
		
		try {
			/************** 行程信息begin ****************/
			// 命令字
			String command = message.getCommand();
			if(StringUtils.isEmpty(command)){//没传命令字过来的，说明是从离线数据上传而来
				obdHandlerTravelLogger.info("<"+obdSn+">------------------离线数据------------------");
			}
			if("-1".equals(command)){//命令字为-1，则说明是半条行程（下发查询时，返回的设备数据-行程报文）
				obdHandlerTravelLogger.info("<"+obdSn+">------------------半条行程数据------------------");
			}
			// 行程序号:记录第行程信息序号，最多**条
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String travelNo = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _travelNo = Integer.valueOf(travelNo, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">行程序号:"+travelNo+"->"+_travelNo);
			carTraveltrack.setTravelNo(_travelNo);
			
			// 行程开始时间:0x12 0x08 0x28 0x12 0x24 0x36，表示2012年8月28日12点24分36秒
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 6);
			String travelBeginTime = cutStrs[0];
			msgBody = cutStrs[1];
			Date travelStart = ThreadLocalDateUtil.parse("yyyyMMddHHmmss", "20"+travelBeginTime);
			obdHandlerTravelLogger.info("<"+obdSn+">行程开始时间:20"+travelBeginTime);
			carTraveltrack.setTravelStart(travelStart);
			
			// 行程结束时间:0x12 0x08 0x28 0x12 0x24 0x36，表示2012年8月28日12点24分36秒
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 6);
			String travelEndTime = cutStrs[0];
			msgBody = cutStrs[1];
			Date travelEnd = ThreadLocalDateUtil.parse("yyyyMMddHHmmss", "20"+travelEndTime);
			obdHandlerTravelLogger.info("<"+obdSn+">行程结束时间:20"+travelEndTime);
			carTraveltrack.setTravelEnd(travelEnd);
			carDriveInfo.setDriveDate(travelEnd);
			carOilInfo.setOilInfoTime(travelEnd);
			
			// 行驶距离（10M）：0x12 0x34，表示0x1234 10M
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String travelDistance = cutStrs[0];
			msgBody = cutStrs[1];
			Long _travelDistance = Long.valueOf(travelDistance, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">行驶距离（10M）:"+travelDistance+"->"+Integer.valueOf(travelDistance, 16));
			carTraveltrack.setDistance(_travelDistance);
			carOilInfo.setMileageNum(_travelDistance+"");
			
			// 最高车速:最高车速(公里/小时):16转10进制
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String highestSpeed = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _highestSpeed = Integer.valueOf(highestSpeed, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">最高车速(公里/小时):"+highestSpeed+"->"+_highestSpeed);
			carTraveltrack.setSpeed(_highestSpeed);
			carDriveInfo.setAvSpeed(_highestSpeed.doubleValue());
			
			// 本次行程油耗:10ML
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String travelOil = cutStrs[0];
			msgBody = cutStrs[1];
			Long _travelOil = Long.valueOf(travelOil, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">本次行程油耗:10ML:"+travelOil+"->"+_travelOil);
			carTraveltrack.setTotalFuel(_travelOil * 10);
			carOilInfo.setPetrolConsumeNum((_travelOil * 10) + "");
			
			// 驾驶时长:分钟
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String travelTimeout = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _travelTimeout = Integer.valueOf(travelTimeout, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">驾驶时长(分钟):"+travelTimeout+"->"+_travelTimeout);
			carTraveltrack.setDriverTime(_travelTimeout*60);
			carOilInfo.setTimeSpanNum(_travelTimeout*60+"");//秒
			
			/************** 行程信息end ****************/

			/************** 驾驶行为begin ****************/
			// 超速次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String overSpeedNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _overSpeedNums = Integer.valueOf(overSpeedNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">超速次数:"+overSpeedNums+"->"+_overSpeedNums);
			carTraveltrack.setOverspeedTime(_overSpeedNums);
			carDriveInfo.setTmsSpeeding(_overSpeedNums);
			
			// 急转弯次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String suddenTurnNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _suddenTurnNums = Integer.valueOf(suddenTurnNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">急转弯次数:"+suddenTurnNums+"->"+_suddenTurnNums);
			carTraveltrack.setQuickTurn(_suddenTurnNums);
			carDriveInfo.setTmsSharpTurn(_suddenTurnNums);
			
			// 急加速次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String suddenOverSpeedNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _suddenOverSpeedNums = Integer.valueOf(suddenOverSpeedNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">急加速次数:"+suddenOverSpeedNums+"->"+_suddenOverSpeedNums);
			carTraveltrack.setQuickenNum(_suddenOverSpeedNums.longValue());
			carDriveInfo.setTmsRapAcc(_suddenOverSpeedNums);
			
			// 急减速次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String suddenLowSpeedNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _suddenLowSpeedNums = Integer.valueOf(suddenLowSpeedNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">急减速次数:"+suddenLowSpeedNums+"->"+_suddenLowSpeedNums);
			carTraveltrack.setQuickSlowDown(_suddenLowSpeedNums);
			carDriveInfo.setTmsRapDec(_suddenLowSpeedNums);
			
			// 急刹车次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String suddenBrakesNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _suddenBrakesNums = Integer.valueOf(suddenBrakesNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">急刹车次数:"+suddenBrakesNums+"->"+_suddenBrakesNums);
			carTraveltrack.setBrakesNum(_suddenBrakesNums.longValue());
			carDriveInfo.setTmsBrakes(_suddenBrakesNums);
			
			// 急变道次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String suddenChangeRoadNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _suddenChangeRoadNums = Integer.valueOf(suddenChangeRoadNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">急变道次数:"+suddenChangeRoadNums+"->"+_suddenChangeRoadNums);
			carTraveltrack.setQuickLaneChange(_suddenChangeRoadNums);
			carDriveInfo.setTmsSteep(_suddenChangeRoadNums);
			
			// 发动机最高水温（摄氏度）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String engineHighestWaterTemperature = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _engineHighestWaterTemperature = Integer.valueOf(engineHighestWaterTemperature, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">发动机最高水温（摄氏度）:"+engineHighestWaterTemperature+"->"+_engineHighestWaterTemperature);
			carTraveltrack.setTemperature(_engineHighestWaterTemperature-40);
			
			// 发动机最高工作转数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String engineHighestRevolution = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _engineHighestRevolution = Integer.valueOf(engineHighestRevolution, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">发动机最高工作转数:"+engineHighestRevolution+"->"+_engineHighestRevolution);
			carTraveltrack.setEngineMaxSpeed(_engineHighestRevolution);
			
			// 车速转速不匹配次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String carSpeedNotMatchTurnNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _carSpeedNotMatchTurnNums = Integer.valueOf(carSpeedNotMatchTurnNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">车速转速不匹配次数:"+carSpeedNotMatchTurnNums+"->"+_carSpeedNotMatchTurnNums);
			carTraveltrack.setSpeedMismatch(_carSpeedNotMatchTurnNums);
			carDriveInfo.setNotMatch(_carSpeedNotMatchTurnNums);
			
			// 长怠速次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String longLowSpeedNums = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _longLowSpeedNums = Integer.valueOf(longLowSpeedNums, 16);
			obdHandlerTravelLogger.info("<"+obdSn+">长怠速次数:"+longLowSpeedNums+"->"+_longLowSpeedNums);
			carTraveltrack.setIdling(_longLowSpeedNums);
			carDriveInfo.setLongLowSpeed(_longLowSpeedNums);
			
			// 疲劳驾驶次数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String tiredDriveNums = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerTravelLogger.info("<"+obdSn+">疲劳驾驶次数:"+tiredDriveNums+"->"+Integer.valueOf(tiredDriveNums, 16));
			//TODO 
			Integer tiredCount = DriveTimeUtil.getDriveTimeCount(obdSn);
			obdHandlerTravelLogger.info("<"+obdSn+">疲劳驾驶次数(采集后台统计):"+tiredCount);
			carDriveInfo.setTmsFatigue(tiredCount);
			DriveTimeUtil.OBD_DriveTime_Count.remove(obdSn);//清空统计
			
			// 蓄电池电压：0.1V为一个单位，0x79表示12.1V 16转10进制
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String batteryVoltage = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _batteryVoltage = Integer.valueOf(batteryVoltage, 16);
			ObdHandShake obdHandShake = obdHandShakeService.findLastBySn(obdSn);
			if(obdHandShake != null){
				if(obdHandShake.getVoltType() == 1){
					//24V
					obdHandlerTravelLogger.info("<"+obdSn+">24V,蓄电池电压:"+batteryVoltage+"->"+_batteryVoltage*0.2);
					carTraveltrack.setVoltage(_batteryVoltage.doubleValue() * 0.2);
				}else{
					obdHandlerTravelLogger.info("<"+obdSn+">12V,蓄电池电压:"+batteryVoltage+"->"+_batteryVoltage*0.1);
					carTraveltrack.setVoltage(_batteryVoltage.doubleValue() * 0.1);	
				}
			}
			
			/************** 驾驶行为end ****************/
			if(carInfo != null){
				carOilInfo.setCarId(carInfo.getCarId());
				carDriveInfo.setCarId(carInfo.getCarId());
			}
			
//			saveTravelData(obdSn,command,carTraveltrack,carDriveInfo,carOilInfo);//保存行程数据：行程+油耗+驾驶行为
			synchronized (obdSn.intern()) {
				carTraveltrackService.travelDataSave(obdSn,command,carTraveltrack,carDriveInfo,carOilInfo);//保存行程数据：行程+油耗+驾驶行为
			}
			//行程入库操作
//			saveTravel(obdSn,command,carTraveltrack);
			//驾驶行为入库操作
//			saveDriveInfo(obdSn,command,carDriveInfo);
			//油耗入库操作
//			saveOilInfo(obdSn,command,carOilInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerTravelLogger.error(obdSn, e);
			return "error";
		}
		
		if(!StringUtils.isEmpty(msgBody)){
			//不为空说明有多条数据，再次递归
			OBDMessage obdMessage = new OBDMessage();
			obdMessage.setId(message.getId());
			obdMessage.setMessage(msgBody);
			obdMessage.setMsgBody(msgBody);
			entry(obdMessage);
		}
		
		return retrunMsgBody;
	}

	private void saveTravelData(String obdSn, String command,CarTraveltrack carTraveltrack, CarDriveInfo carDriveInfo,CarOilInfo carOilInfo) {
		synchronized (obdSn.intern()) {
			//ID关联
			carDriveInfo.setDriveInfoId(carTraveltrack.getId());
			carOilInfo.setOilInfoId(carTraveltrack.getId());
			//去重
			if(carTraveltrackService.getTravelByObdAndTime(obdSn,carTraveltrack.getTravelStart(),carTraveltrack.getTravelEnd()) == null){
				carTraveltrack.setInsesrtTime(new Date());
				
				List<CarTraveltrack> carTraveltracks = carTraveltrackService.queryTravel(obdSn,carTraveltrack.getTravelStart());
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库【(半条行程)/(序号递增，重复数据)作废】条数："+carTraveltracks.size());
				for (CarTraveltrack ct : carTraveltracks) {
					if(ct != null){
						//行程
						ct.setType(2);// //排除序号递增，重复数据去除；或者半条去除
						carTraveltrackService.carTraveltrackUpdate(ct);
						//驾驶行为
						CarDriveInfo driveInfo = carDriveInfoService.find(ct.getId());
						if(driveInfo != null){
							driveInfo.setType(2);
							carDriveInfoService.carDriveInfoUpdate(driveInfo);
						}
						//油耗
						CarOilInfo oilInfo = carOilInfoService.find(ct.getId());
						if(oilInfo != null){
							oilInfo.setType(2);
							carOilInfoService.carOilInfoUpdate(oilInfo);
						}
					}
				}
				
				if("-1".equals(command)){//是半条行程
					carTraveltrack.setType(1);
					carTraveltrackService.add(carTraveltrack);
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功【半条行程】-----");
					carDriveInfo.setType(1);
					carDriveInfoService.carDriveInfoSave(carDriveInfo);
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功【半条驾驶行为】-----");
					carOilInfo.setType(1);
					carOilInfoService.carOilSave(carOilInfo);
					obdHandlerTravelLogger.info("<"+obdSn+">-----油耗入库成功【半条油耗】-----");
				} else { //离线数据/正常
					carTraveltrack.setType(0);
					carTraveltrackService.add(carTraveltrack);
					carDriveInfo.setType(0);
					carDriveInfoService.carDriveInfoSave(carDriveInfo);
					carOilInfo.setType(0);
					carOilInfoService.carOilSave(carOilInfo);
					if(StringUtils.isEmpty(command)){
						obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功【离线行程数据】-----");
						obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功【离线驾驶行为数据】-----");
						obdHandlerTravelLogger.info("<"+obdSn+">-----油耗入库成功【离线油耗数据】-----");
					}else{
						obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功-----");
						obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功-----");
						obdHandlerTravelLogger.info("<"+obdSn+">-----油耗入库成功-----");
						
						//熄火，移除正在驾驶，记录开始休息
						if(driveTimeUtil.sendedTiredByRedis(obdSn)){//已经发送了疲劳驾驶才有消除疲劳的告警
							driveTimeUtil.enterSleepDateByRedis(obdSn, new Date());
							driveTimeUtil.putDrivingByRedis(obdSn,"0");
							driveTimeUtil.putRestingByRedis(obdSn, "1");
							driveTimeUtil.startRestByRedis(obdSn, new Date());
							driveTimeUtil.removeSendedTiredByRedis(obdSn);//移除发生疲劳告警
							ObdState obdState = obdSateService.queryByObdSn(obdSn);
							if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
									&& "1".equals(obdState.getFatigueDriveSwitch())){
								pushApi.pushWarnHandler(obdSn,WarnType.FatigueDriveCancel,"00", "消除疲劳驾驶告警");
							}
						}
					}
				}
			}else{
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库失败：库内已有此记录！-----");
			}
		}
	}

	/**
	 * 保存油耗
	 * @param obdSn
	 * @param command
	 * @param carOilInfo
	 */
	private void saveOilInfo(String obdSn, String command, CarOilInfo carOilInfo) {
		//半条行程油耗 作废！
		List<CarOilInfo> oilInfos = carOilInfoService.queryOilInfo(obdSn);
		obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库【半条行程油耗】条数："+oilInfos.size());
		for (CarOilInfo oilInfo2 : oilInfos) {
			oilInfo2.setType(2);//半条行程油耗作废
			carOilInfoService.carOilInfoUpdate(oilInfo2);
		}
		
		if("-1".equals(command)){//是半条行程油耗
			carOilInfo.setType(1);
			carOilInfoService.carOilSave(carOilInfo);
			obdHandlerTravelLogger.info("<"+obdSn+">-----油耗信息入库成功【半条行程油耗】-----");
		}else{//离线数据行程油耗/正常
			String str  = (StringUtils.isEmpty(command) == true )? "【离线数据行程油耗】": ""; 
			if(carOilInfoService.getCarOilInfoByTime(obdSn, carOilInfo.getOilInfoTime()) == null){
				carOilInfo.setType(0);
				carOilInfoService.carOilSave(carOilInfo);
				obdHandlerTravelLogger.info("<"+obdSn+">-----油耗信息入库成功"+str+"-----");
			}else{
				obdHandlerTravelLogger.info("<"+obdSn+">-----油耗信息入库失败"+str+"：库内已有此记录！-----");
			}
		}
	}

	/**
	 * 保存驾驶行为
	 * @param obdSn
	 * @param command
	 * @param carDriveInfo
	 */
	private void saveDriveInfo(String obdSn, String command, CarDriveInfo carDriveInfo) {
		//半条行程驾驶行为 作废！
		List<CarDriveInfo> driveInfos = carDriveInfoService.queryDriveInfo(obdSn);
		obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库【半条行程驾驶行为】条数："+driveInfos.size());
		for (CarDriveInfo driveInfo : driveInfos) {
			driveInfo.setType(2);//半条行程行为作废
			carDriveInfoService.carDriveInfoUpdate(driveInfo);
		}
		
		if("-1".equals(command)){//是半条行程驾驶行为
			carDriveInfo.setDriveInfoId(IDUtil.createID());
			carDriveInfo.setType(1);
			carDriveInfoService.carDriveInfoSave(carDriveInfo);
			obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功【半条行程驾驶行为】-----");
		} else{//离线数据行程驾驶行为/正常
			String str  = (StringUtils.isEmpty(command) == true )? "【离线数据行程驾驶行为】": ""; 
			if(carDriveInfoService.getDriveInfoByDriveDate(obdSn, carDriveInfo.getDriveDate()) == null){
				carDriveInfo.setDriveInfoId(IDUtil.createID());
				carDriveInfo.setType(0);
				carDriveInfoService.carDriveInfoSave(carDriveInfo);
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功"+str+"-----");
			}else{
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库失败"+str+"：库内已有此记录！-----");
			}
		}
	}

	/**
	 * 保存行程
	 * @param obdSn
	 * @param command
	 * @param carTraveltrack
	 */
	private void saveTravel(String obdSn, String command, CarTraveltrack carTraveltrack) {
		//去重
		if(carTraveltrackService.getTravelByObdAndTime(obdSn,carTraveltrack.getTravelStart(),carTraveltrack.getTravelEnd()) == null){
			carTraveltrack.setInsesrtTime(new Date());
			
			//半条行程作废操作
			List<CarTraveltrack> carTraveltracks = carTraveltrackService.queryTravel(obdSn,carTraveltrack.getTravelStart());
			obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库【半条行程作废】条数："+carTraveltracks.size());
			for (CarTraveltrack ct : carTraveltracks) {
				if(ct != null){//是半条行程
					ct.setType(2);//半条行程作废
					carTraveltrackService.carTraveltrackUpdate(ct);
				} 
			}
			
			if("-1".equals(command)){//是半条行程
				carTraveltrack.setType(1);
				carTraveltrackService.add(carTraveltrack);
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功【半条行程】-----");
			} else { //离线数据/正常
				carTraveltrack.setType(0);
				carTraveltrackService.add(carTraveltrack);
				if(StringUtils.isEmpty(command)){
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功【离线行程数据】-----");
				}else{
//					//清空报警记录
//					PushUtil.clearPush(obdSn);
//					obdHandlerTravelLogger.info("-----清空报警记录-----");
//					obdHandlerTravelLogger.info("-----清空报警记录："+PushUtil.PushApiTime.get(obdSn));
				
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功-----");
				}
			}
		}else{
			obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库失败：库内已有此记录！-----");
		}
	}

}
