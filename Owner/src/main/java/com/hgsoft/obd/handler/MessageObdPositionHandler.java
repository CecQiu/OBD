package com.hgsoft.obd.handler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.ObdButtonAlarm;
import com.hgsoft.carowner.entity.ObdMiles;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.entity.PositionDriveInfo;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.carowner.entity.PositionWarnInfo;
import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdButtonAlarmService;
import com.hgsoft.carowner.service.ObdMilesService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.PositionDriveInfoService;
import com.hgsoft.carowner.service.PositionInfoService;
import com.hgsoft.carowner.service.PositionWarnInfoService;
import com.hgsoft.carowner.service.SimStockInfoService;
import com.hgsoft.carowner.service.WarningMessageService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.common.utils.ThreadLocalDateUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.service.FenceHandleService;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.JsonDateValueProcessor;
import com.hgsoft.obd.util.TravelDataUtil;
import com.hgsoft.obd.util.WarnType;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 设备上传通讯——位置数据
 * @author sujunguang
 * 2015年12月12日
 * 下午4:26:20
 */
@Service
public class MessageObdPositionHandler implements IMessageObd{

	private static Logger obdHandlerPositionLogger = LogManager.getLogger("obdHandlerPositionLogger");
	@Resource
	private PositionInfoService positionInfoService;
	@Resource
	private PositionDriveInfoService positionDriveInfoService;
	@Resource
	private PositionWarnInfoService positionWarnInfoService;
	@Resource
	private CarGSPTrackService gspTrackService;
	@Resource
	private WarningMessageService warningMessageService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private CarInfoService carInfoService;
	@Resource
	private SimStockInfoService simStockInfoService;
	@Resource
	private PushApi pushApi;
	@Resource
	private DriveTimeUtil driveTimeUtil;
	@Resource
	private ObdSateService obdSateService;
	@Resource
	private TravelDataUtil travelDataUtil;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private FenceHandleService fenceHandleService;
	@Resource
	private ObdMilesService obdMilesService ;
	@Resource
	private JedisServiceUtil jedisServiceUtil;
	@Resource
	private ObdButtonAlarmService obdButtonAlarmService;
	
	private static ThreadLocal<List<PositionInfo>> positionInfoLists = new ThreadLocal<List<PositionInfo>>() {
		@Override
		protected List<PositionInfo> initialValue() {
			return new ArrayList<>();
		}
	};
	private static ThreadLocal<List<PositionDriveInfo>> positionDriveInfoLists = new ThreadLocal<List<PositionDriveInfo>>() {
		@Override
		protected List<PositionDriveInfo> initialValue() {
			return new ArrayList<>();
		}
	};
	private static ThreadLocal<List<PositionWarnInfo>> positionWarnInfoLists = new ThreadLocal<List<PositionWarnInfo>>() {
		@Override
		protected List<PositionWarnInfo> initialValue() {
			return new ArrayList<>();
		}
	};
	private static ThreadLocal<List<CarGSPTrack>> carGSPTrackList = new ThreadLocal<List<CarGSPTrack>>() {
		@Override
		protected List<CarGSPTrack> initialValue() {
			return new ArrayList<>();
		}
	};
	private void addPositionInfo(PositionInfo positionInfo){
		List<PositionInfo> positionInfos = positionInfoLists.get();
		positionInfos.add(positionInfo);
		positionInfoLists.set(positionInfos);
	}
	private void addPositionDriveInfo(PositionDriveInfo positionDriveInfo){
		List<PositionDriveInfo> positionDriveInfos = positionDriveInfoLists.get();
		positionDriveInfos.add(positionDriveInfo);
		positionDriveInfoLists.set(positionDriveInfos);
	}
	private void addPositionWarnInfo(PositionWarnInfo positionWarnInfo){
		List<PositionWarnInfo> positionWarnInfos = positionWarnInfoLists.get();
		positionWarnInfos.add(positionWarnInfo);
		positionWarnInfoLists.set(positionWarnInfos);
	}
	private void addCarGSPTrack(CarGSPTrack carGSPTrack){
		List<CarGSPTrack> carGSPTracks = carGSPTrackList.get();
		carGSPTracks.add(carGSPTrack);
		carGSPTrackList.set(carGSPTracks);
	}
	
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		String obdSn = message.getId();
		obdHandlerPositionLogger.info("------------------【设备上传通讯——位置信息数据上传】---------------");
		obdHandlerPositionLogger.info("------------"+obdSn+"---报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		obdHandlerPositionLogger.info("------------设备："+obdSn+"------------");
		String retrunMsgBody = "success";
		String[] cutStrs ;//截取结果数组
		String msgBody = message.getMsgBody();
		String waterNo = message.getWaterNo();
		boolean sameWaterNo = false;
		if(!StringUtils.isEmpty(waterNo)){
			if(GlobalData.OBD_PositionWaterNo.containsKey(obdSn)){
				String upWaterNo = GlobalData.OBD_PositionWaterNo.get(obdSn);
				if(waterNo.equals(upWaterNo)){
					sameWaterNo = true;
				}
			}
			GlobalData.OBD_PositionWaterNo.put(obdSn, waterNo);
		}
		//位置信息
		PositionInfo positionInfo = new PositionInfo();
		//位置-驾驶行为
		PositionDriveInfo positionDriveInfo = new PositionDriveInfo(IDUtil.createID());
		//位置-报警信息
		PositionWarnInfo positionWarnInfo = new PositionWarnInfo(IDUtil.createID());
		String positionId = IDUtil.createID();
		positionInfo.setId(positionId);
		positionDriveInfo.setPositionInfoId(positionId);
		positionWarnInfo.setPositionInfoId(positionId);
		
		positionInfo.setObdSn(message.getId());
		
		try {
			/**************常用数据begin****************/
			//命令字
			String command = message.getCommand();
			if(StringUtils.isEmpty(command)){
				//这是离线数据
				obdHandlerPositionLogger.info("--------------"+obdSn+"---离线数据------------------");
			}
			//数据帧
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String dataFrame = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerPositionLogger.info("<"+obdSn+">数据帧："+dataFrame);
			char[] bits = StrUtil.hexToBinary(dataFrame);
			
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 6);
			String time = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerPositionLogger.info("<"+obdSn+">系统时间："+time);
			Date obdTime =ThreadLocalDateUtil.parse("yyyyMMddHHmmss","20"+time);
			positionInfo.setTime(obdTime);
			positionWarnInfo.setTime(positionInfo.getTime());
			obdHandlerPositionLogger.info("<"+obdSn+">系统时间Date："+obdTime);
			
			String lastTravelStartStr = travelDataUtil.getTravelStart(obdSn);
			if(!StringUtils.isEmpty(lastTravelStartStr)){
				String lastTravelEndStr = travelDataUtil.getTravelEnd(obdSn);
				obdHandlerPositionLogger.info("<"+obdSn+">【行程数据参数记录】上次行程结束时间："+lastTravelEndStr);
				if(StringUtils.isEmpty(lastTravelEndStr)){
					travelDataUtil.putTravelEnd(obdSn, ThreadLocalDateUtil.formatDate("yyyyMMddHHmmss", obdTime));
				}else{
					Date lastTravelEnd = ThreadLocalDateUtil.parse("yyyyMMddHHmmss", lastTravelEndStr);
					if(lastTravelEnd.before(obdTime)){
						travelDataUtil.putTravelEnd(obdSn, ThreadLocalDateUtil.formatDate("yyyyMMddHHmmss", obdTime));
					}
				}
			}
			
			/*if(!StringUtils.isEmpty(command)){//离线数据不参与
	//			obdHandlerPositionLogger.info("<"+obdSn+">是否驾驶疲劳数据(前)：" + DriveTimeUtil.OBD_DriveTime.get(obdSn));
				obdHandlerPositionLogger.info("<"+obdSn+">是否驾驶疲劳数据(前)：" + driveTimeUtil.getTiredDriveByRedis(obdSn));
	//			boolean  isTired = driveTimeUtil.isTiredDrive(obdSn, obdTime);
				boolean  isTired = driveTimeUtil.isTiredDriveByRedis(obdSn, obdTime);
				obdHandlerPositionLogger.info("<"+obdSn+">是否驾驶疲劳：" + isTired);
				if(isTired){
					pushApi.pushWarnHandler(obdSn,WarnType.FatigueDrive,"14", "疲劳驾驶");//处理预警信息推送
				}
	//			obdHandlerPositionLogger.info("<"+obdSn+">是否驾驶疲劳数据(后)：" + DriveTimeUtil.OBD_DriveTime.get(obdSn));
				obdHandlerPositionLogger.info("<"+obdSn+">是否驾驶疲劳数据(后)：" + driveTimeUtil.getTiredDriveByRedis(obdSn));
			}*/
			
			//发动机水温
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String engineTemperature = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _engineTemperature = Integer.valueOf(engineTemperature,16);
			obdHandlerPositionLogger.info("<"+obdSn+">发动机水温："+engineTemperature+"->"+_engineTemperature);
			if("ff".equals(engineTemperature)){
				obdHandlerPositionLogger.error("<"+obdSn+">发动机水温异常："+engineTemperature);
			}else{//数据异常不入库，保留空
				positionInfo.setEngineTemperature(_engineTemperature-40);//-40为实际温度
			}
			
			//车辆速度
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String carSpeed = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _carSpeed = Integer.valueOf(carSpeed,16);
			obdHandlerPositionLogger.info("<"+obdSn+">车辆速度："+carSpeed+"->"+_carSpeed);
			if("ff".equals(carSpeed)){
				obdHandlerPositionLogger.error("<"+obdSn+">车辆速度异常："+carSpeed);
			}else{//数据异常不入库，保留空
				positionInfo.setObdSpeed(_carSpeed);
				GlobalData.OBD_PositionCarSpeed.put(obdSn, _carSpeed);
			}
			//TODO 如果车速不为0说明定位不到，需要用其他技术手段获取经纬度。速度为0不处理。
			
			//发动机工作转数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String engineTurns = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _engineTurns = Integer.valueOf(engineTurns,16);
			obdHandlerPositionLogger.info("<"+obdSn+">发动机工作转数："+engineTurns+"->"+_engineTurns);
			if("ffff".equals(engineTurns)){
				obdHandlerPositionLogger.error("<"+obdSn+">发动机工作转数异常："+engineTurns);
			}else{//数据异常不入库，保留空
				//点火标记-》前次休眠原因 + RPM！= null

				//设备点火,开始行使
				if(GlobalData.ObdLastSleep.contains(obdSn)){
					Date nowDate = new Date();
					driveTimeUtil.putDrivingByRedis(obdSn,"1");
					driveTimeUtil.putRestingByRedis(obdSn, "0");
					driveTimeUtil.startDriveByRedis(obdSn, nowDate);
					driveTimeUtil.isTiredByRedis(obdSn,nowDate);//只做初始化值
					GlobalData.ObdLastSleep.remove(obdSn);
				}
				positionInfo.setEngineTurns(_engineTurns);
			}
		
			
			/**************常用数据end****************/
			//根据数据帧确定有什么数据
			
			if('1'  == bits[0]){//驾驶行为数据：1-存在
				//驾驶行为数据
				msgBody = driveBehaviorHandler(obdSn,msgBody,positionDriveInfo);
			}
			
			if('1'  == bits[1]){//报警数据：1-存在
				//报警数据
				msgBody = warnHandler(obdSn,msgBody,positionWarnInfo);
			}else{
				GlobalData.OBD_PositionECUWarn.put(obdSn, false);
			}
			obdHandlerPositionLogger.info("<"+obdSn+">ECU通信故障报警标识："+GlobalData.OBD_PositionECUWarn.get(obdSn));
			
			/*********************流量入库*********************/
			if(bits[8] == '1'){
				//有流量值
				Long wifiFlow = null;
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String wifiFlowStr = cutStrs[0];
				msgBody = cutStrs[1];
				wifiFlow = Long.valueOf(wifiFlowStr, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">---WiFi 流量(KB):"+wifiFlowStr+"->"+wifiFlow);
				if("ffffffff".equals(wifiFlowStr)){
					obdHandlerPositionLogger.error("<"+obdSn+">---WiFi 流量(KB)异常:"+wifiFlowStr);
				}else{//数据异常不入库，操作
					saveWifiFlow(obdSn,positionInfo,wifiFlow,sameWaterNo);//入库操作
				}
			}
			
			char location = bits[7];//定位成功1 失败0
			obdHandlerPositionLogger.info("<"+obdSn+">定位成功1 失败0:"+location);
			
			String netType = null;
			if('0'  == bits[2]){//定位数据：0-存在
				 if('0' == bits[3]){//GPS数据
					 positionInfo.setType(1);
					 char gpsFormat = bits[4];//GPS数据格式:0-无卫星数据 1-有卫星数据
					 obdHandlerPositionLogger.info("<"+obdSn+">GPS数据格式:0-无卫星数据 1-有卫星数据:"+gpsFormat);
					 
					 if('1' == location){
						 positionInfo.setStatusGPS(1);
						 char longtitude = bits[5];//经度：0-东经 1-西经
						 obdHandlerPositionLogger.info("<"+obdSn+">经度：0-东经 1-西经:"+longtitude);
						 positionInfo.setLongitudeType(longtitude == '0' ? 1 : 0);//东经-1
						 
						 char latitude = bits[6];//纬度：0-北纬 1-南纬
						 obdHandlerPositionLogger.info("<"+obdSn+">纬度：0-北纬 1-南纬:"+latitude);
						 positionInfo.setLatitudeType(longtitude == '0' ? 1 : 0);//北纬-1
					 }else{
						 positionInfo.setStatusGPS(0);
					 }
					 
				 }else{//基站 	1：注册网络
					 positionInfo.setType(2);
					 String _netType = new String(""+ bits[7]+ bits[6]+ bits[5]+ bits[4]);
					 switch (_netType) {
					case "0000"://电信4G
						netType = "DX4G";
						break;
					case "0001"://电信3G EVDO
						netType = "DX3G";
						break;
					case "0010"://电信2G CDMA2000/1X
						netType = "DX2G";
						break;
					case "0011"://移动4G
						netType = "YD4G";
						break;
					case "0100"://移动3G
						netType = "YD3G";
						break;
					case "0101"://移动2G（GSM）
						netType = "YD2G";
						break;
					case "0111"://联通4G
						netType = "LT4G";
						break;
					case "1000"://联通3G
						netType = "LT3G";
						break;
					case "1001"://联通2G（GSM）
						netType = "LT2G";
						break;

					default:
						netType = "其他";
						break;
					}
					 positionInfo.setNetType(_netType+"->"+netType);
				 }
			}else{
				positionInfo.setStatusGPS(0);
			}
			
			//bit2=1,在线心跳包使用
			//TODO
			String[] gpsDatas = null;
			//
			if (location == '1') {
				// GPS数据长度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String gpsDataLengthStr = cutStrs[0];
				msgBody = cutStrs[1];

				Integer gpsDataLength = Integer.valueOf(gpsDataLengthStr, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">GPS数据长度:" + gpsDataLength);
				gpsDatas = new String[gpsDataLength / 9 * 3];//临时存储多GPS数据
				for (int i = 0; i < gpsDataLength / 9; i++) {//30s间隔，30/长度 表示每条记录的间隔
					// GPS数据
					// GPS速度：km\h，OBD速度异常时，参考
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String gpsSpeed = cutStrs[0];
					Integer _gpsSpeed = Integer.valueOf(gpsSpeed, 16);
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">GPS速度：km/h:" + _gpsSpeed);
					gpsDatas[i*3] = _gpsSpeed.toString();
					if("ff".equals(gpsSpeed)){
						obdHandlerPositionLogger.error("<"+obdSn+">GPS速度异常："+gpsSpeed);
					}else{//数据异常不入库，保留空
						GlobalData.OBD_PositionGPSSpeed.put(obdSn, _gpsSpeed);
					}
					
					// 经度： 0x11 0x60 0x40 0x00 表示：116˚04.000΄
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
					String longtitude = cutStrs[0];
					msgBody = cutStrs[1];
					String _longtitude = longtitude.substring(0,3)+"°"+longtitude.substring(3,5)+"."+longtitude.substring(5)+"'";
					obdHandlerPositionLogger.info("<"+obdSn+">经度:" + longtitude+"->"+_longtitude);
					gpsDatas[i*3+1] = _longtitude;
					
					// 纬度：0x33 0x32 0x00 0x00 表示：33˚32.0000΄
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
					String latitude = cutStrs[0];
					msgBody = cutStrs[1];
					String _latitude = latitude.substring(0,2)+"°"+latitude.substring(2,4)+"."+latitude.substring(4)+"'";
					obdHandlerPositionLogger.info("<"+obdSn+">纬度:" + latitude+"->"+_latitude);
					gpsDatas[i*3+2] = _latitude;
					
				}
				// 方向角：正北为0，范围0-360
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String direction = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _direction = Integer.valueOf(direction, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">方向角:" + _direction);
				positionInfo.setDirectionAngle(_direction);

			}
			

			// 卫星数据：数据帧BIT4=1，上传卫星数据 离线不保存卫星数据
			if (bits[4] == '1') {
				// 卫星定位系统：P=GPS,SBAS,QZSS,L=GLONASS,A=Galileo, B=BeiDou
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String GPS = cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerPositionLogger.info("<"+obdSn+">卫星定位系统:" + GPS);
				positionInfo.setSatelliteSys(GPS);
				
				if(location == '1'){//定到位 海拔高度数据
					//海拔高度方向
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String highDirection = cutStrs[0];
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">海拔高度方向：" + highDirection);
					
					//海拔高度
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String high = cutStrs[0];
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">海拔高度：" + high);
				}
				
				// 卫星个数
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String satelliteCounts = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _satelliteCounts = Integer.valueOf(satelliteCounts, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">卫星个数:"+_satelliteCounts);
				positionInfo.setSatellites(_satelliteCounts);
				
				for (int j = 0; j < _satelliteCounts; j++) {
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String satelliteNo = cutStrs[0];
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">卫星编号："+ satelliteNo);
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String satelliteSignalStrength = cutStrs[0];
					msgBody = cutStrs[1];
					Integer _satelliteSignalStrength = Integer.valueOf(satelliteSignalStrength, 16);
					obdHandlerPositionLogger.info("<"+obdSn+">卫星信号强度："+ satelliteSignalStrength+"->"+_satelliteSignalStrength );
				}

			}
			/***基站数据：数据帧BIT3=1，基站数据            BIT4-7位确定网络类型***/ 

			if(bits[2] == '0' && bits[3] == '1'){
				// TODO
				String MCC="", SID="", NID="", BID="";
				if("YD2G".equals(netType)){
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String mcc = cutStrs[0];//移动国家代码:中国-460
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">移动国家代码:"+mcc);
					positionInfo.setMcc(mcc);
					MCC = mcc;
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String mnc = cutStrs[0];//移动网号码：中国移动-00，中国联通-01，中国电信-03
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">移动网号码:"+mnc);
					positionInfo.setMnc(mnc);
					SID = mnc;
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 3);
					String lac = cutStrs[0];//位置区码
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">位置区码:"+lac);
					positionInfo.setNid(lac);
					NID = lac;
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String cell = cutStrs[0];//基站小区号
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">基站小区号:"+cell);
					positionInfo.setBid(cell);
					BID = cell;
					
				}else if("DX2G".equals(netType) || "DX3G".equals(netType)){
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String mcc = cutStrs[0];//移动国家代码:中国-460
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">移动国家代码:"+mcc);
					positionInfo.setMcc(mcc);
					MCC = mcc;
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String mnc = cutStrs[0];//移动网号码：中国移动-00，中国联通-01，中国电信-03
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">移动网号码:"+mnc);
					positionInfo.setMnc(mnc);
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String sid = cutStrs[0];//对应MNC——系统识别码，每个地级市只有一个sid
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">对应MNC——系统识别码:"+sid);
					positionInfo.setSid(sid);
					SID = sid;
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String nid = cutStrs[0];//对应LAC——网络识别码，每个地级市可能有1到3个nid
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">对应LAC——网络识别码:"+nid);
					positionInfo.setNid(nid);
					NID = nid;
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String bid = cutStrs[0];//对应CELL——是网络中的某一个小区，可以理解为基站
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">对应CELL——是网络中的某一个小区，可以理解为基站:"+bid);
					positionInfo.setBid(bid);
					BID = bid;
				} 
				/*
				obdHandlerPositionLogger.info("---------------飞豹基站定位...----------------");
				//飞豹定位
				JSONObject jsonObject =	LBSUtil.getInstance().CDMA(MCC, SID, NID, BID);
				if(jsonObject != null && "0".equals(jsonObject.getString("code"))){
					obdHandlerPositionLogger.info("---------------飞豹基站定位成功----------------:"+jsonObject);
					positionInfo.setStatusGPS(1);
					//经度
					String lng = jsonObject.getJSONObject("data").getString("lng");
					String lngStr=CoordinateTransferUtil.lnglatTransferString(lng);
					obdHandlerPositionLogger.info("经度："+lng+"--》"+lngStr);
					positionInfo.setLongitude(lngStr);
					//纬度
					String lat = jsonObject.getJSONObject("data").getString("lat");
					String latStr=CoordinateTransferUtil.lnglatTransferString(lat);
					obdHandlerPositionLogger.info("纬度："+lat+"--》"+latStr);
					positionInfo.setLatitude(latStr);
					//定位精度
					String precision=(String) jsonObject.getJSONObject("data").get("precision");
					obdHandlerPositionLogger.info("定位精度："+precision);
					positionInfo.setOprecision(precision);
				}else{
					//定位失败
					positionInfo.setStatusGPS(0);
					obdHandlerPositionLogger.error("---------------飞豹基站定位失败----------------:"+jsonObject);
				}*/
			}
			
			/*********************行程数据参数*********************/
			if(bits[9] == '1'){
				msgBody = travelDataParamHandler(obdSn, msgBody);
			}
			/*********************车端私有协议参数*********************/
			if(bits[10] == '1'){
				msgBody = carPrivateHandler(obdSn, msgBody);
			}
			
			/*********************入库*********************/
			if(bits[2] == '0' && bits[7] == '1'){//存在定位数据+定位成功
				if(bits[3] == '0'){//GPS数据
					
					int len = 1;
					if(gpsDatas != null && gpsDatas.length >= 3){//有GPS数据
						len = (gpsDatas.length/3);
					}
					int len_time = 30/len;//30秒 间隔 TODO
					Calendar firstCalendar = Calendar.getInstance();//第一条时间
					firstCalendar.setTime(positionInfo.getTime());
					
					//GPS多条数据
					for(int i=0;i<len;i++){
						PositionInfo pInfo = new PositionInfo();
						BeanUtils.copyProperties(positionInfo,pInfo);
						if(i>0)//ID从第二条记录重新生成
							pInfo.setId(IDUtil.createID());
						
						pInfo.setGpsSpeed(Integer.valueOf(gpsDatas[i*3]));
						pInfo.setLongitude(gpsDatas[i*3+1]);
						pInfo.setLatitude(gpsDatas[i*3+2]);
						//过滤一些不正常的数据 
						if(validateLngLat(gpsDatas[i*3+1]) || validateLngLat(gpsDatas[i*3+2])){
							obdHandlerPositionLogger.error("<"+obdSn+">经纬度可能不正确：经度："+gpsDatas[i*3+1]+",纬度："+gpsDatas[i*3+2]);
							pInfo.setStatusGPS(0);//作为定位失败看待
						}
						//电子栅栏报警判断
						String longitude = CoordinateTransferUtil.lnglatTransferDouble(gpsDatas[i*3+1]);
						String latitude = CoordinateTransferUtil.lnglatTransferDouble(gpsDatas[i*3+2]);
						
						//GPS时间间隔x秒插入
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(firstCalendar.getTime());
						calendar.add(Calendar.SECOND, -(len-i-1)*len_time);//时间倒序，往前推
						pInfo.setTime(calendar.getTime());
						
						//过滤一些不正常的数据：时间
						String timeTmp = ThreadLocalDateUtil.formatDate( "yyyyMMddHHmmss",pInfo.getTime());
						Calendar c = Calendar.getInstance();
						int y = c.get(Calendar.YEAR) - 2000;//2016-2000=16nian
						if(Integer.valueOf(timeTmp.substring(2, 4)).intValue() > y){
							obdHandlerPositionLogger.error("<"+obdSn+">时间可能不正确："+timeTmp);
							pInfo.setStatusGPS(0);//作为定位失败看待
						}
						
						//是否已插入
//						if(positionInfoService.queryBySnAndTime(pInfo.getObdSn(),pInfo.getTime()) == null){
						if(!sameWaterNo){//不是同一流水号确定不会重复
							//TODO 电子围栏判定 设备号+经度+纬度+GPS时间
							fenceHandleService.dzwlWarning(obdSn, longitude, latitude,pInfo.getTime());
							
							pInfo.setInsertTime(new Date());//插入时间
//							positionInfoService.add(pInfo);
							addPositionInfo(pInfo);
							if(i==0){//只保存一次
								
								//保存位置-驾驶行为数据
								positionDriveInfo.setTime(pInfo.getTime());
//								positionDriveInfoService.add(positionDriveInfo);
								addPositionDriveInfo(positionDriveInfo);
								//保存位置-报警信息数据
								positionWarnInfo.setTime(pInfo.getTime());
//								positionWarnInfoService.add(positionWarnInfo);
								addPositionWarnInfo(positionWarnInfo);
							}
							//从位置信息获取驾驶记录
							saveGSPTrack(pInfo, i==len-1);
							obdHandlerPositionLogger.info("<"+obdSn+">位置信息插入成功（GPS数据）！");
						}
					}
					
				}else if(bits[3] == '1'){//保存基站数据
//					if(positionInfoService.queryBySnAndTime(positionInfo.getObdSn(),positionInfo.getTime()) == null){
					if(!sameWaterNo){//不是同一流水号确定不会重复
						positionInfo.setInsertTime(new Date());//插入时间
//						positionInfoService.add(positionInfo);
						addPositionInfo(positionInfo);
						//保存位置-驾驶行为数据
						positionDriveInfo.setTime(positionInfo.getTime());
//						positionDriveInfoService.add(positionDriveInfo);
						addPositionDriveInfo(positionDriveInfo);
						//保存位置-报警信息数据
						positionWarnInfo.setTime(positionInfo.getTime());
//						positionWarnInfoService.add(positionWarnInfo);
						addPositionWarnInfo(positionWarnInfo);
						//从位置信息获取驾驶记录
						saveGSPTrack(positionInfo, false);
						obdHandlerPositionLogger.info("<"+obdSn+">位置信息插入成功（基站数据）！");
					}
				
				}
			}
			
			//定位失败，入库
			if(location == '0' && !sameWaterNo){
//				if(positionInfoService.queryBySnAndTime(positionInfo.getObdSn(),positionInfo.getTime()) == null){
//					
//				}
				positionInfo.setInsertTime(new Date());//插入时间
//				positionInfoService.add(positionInfo);
				addPositionInfo(positionInfo);
				//保存位置-驾驶行为数据
				positionDriveInfo.setTime(positionInfo.getTime());
//				positionDriveInfoService.add(positionDriveInfo);
				addPositionDriveInfo(positionDriveInfo);
				
				//保存位置-报警信息数据
				positionWarnInfo.setTime(positionInfo.getTime());
//				positionWarnInfoService.add(positionWarnInfo);
				addPositionWarnInfo(positionWarnInfo);
				
				obdHandlerPositionLogger.info("<"+obdSn+">定位失败，入库!");
			}
			
			//9~14保留

			if(bits[15] == '1'){//存在测试字段
				obdHandlerPositionLogger.info("<"+obdSn+">---存在测试字段---");
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String testLengthStr = cutStrs[0];
				msgBody = cutStrs[1];
				Integer testLength = Integer.valueOf(testLengthStr,16);
				obdHandlerPositionLogger.info("<"+obdSn+">---存在测试字段---长度："+testLengthStr+"->"+testLength);
				if(testLength > 0){
					cutStrs = StrUtil.cutStrByByteNum(msgBody, testLength);
					String testStr = cutStrs[0];
					msgBody = cutStrs[1];
					obdHandlerPositionLogger.info("<"+obdSn+">---存在测试字段---数据："+testStr);
				}
			}
			
			if(!StringUtils.isEmpty(msgBody)){
				//不为空说明有多条数据，再次递归
				OBDMessage obdMessage = new OBDMessage();
				obdMessage.setId(message.getId());
				obdMessage.setMessage(msgBody);
				obdMessage.setMsgBody(msgBody);
				entry(obdMessage);
			}else{
				//空，则说明解析完成。进行批量入库
				positionInfoService.batchSave(positionInfoLists.get());
				positionDriveInfoService.batchSave(positionDriveInfoLists.get());
				positionWarnInfoService.batchSave(positionWarnInfoLists.get());
				gspTrackService.batchSave(carGSPTrackList.get());
				positionInfoLists.remove();
				positionDriveInfoLists.remove();
				positionWarnInfoLists.remove();
				carGSPTrackList.remove();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerPositionLogger.error(obdSn, e);
			return "error";
		}
		
		return retrunMsgBody;
	}

	/**
	 * 车端私有协议参数
	 * @param obdSn
	 * @param msgBody
	 * @return
	 */
	private String carPrivateHandler(String obdSn, String msgBody) {
		try {
			String[] cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String totalMilesParam = cutStrs[0];
			msgBody = cutStrs[1];
			char[] totalMilesParamBits = StrUtil.hexToBinary(totalMilesParam);
			if (totalMilesParamBits[0] == '1') {//总里程
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 3);
				String totalMilesStr = cutStrs[0];
				msgBody = cutStrs[1];
				Long totalMiles = Long.valueOf(totalMilesStr, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">【车端私有协议参数】总里程:"+totalMilesStr+"->"+totalMiles);
				if("ffffff".equals(totalMilesStr)){
					obdHandlerPositionLogger.info("<"+obdSn+">【车端私有协议参数】总里程:异常！->"+totalMilesStr);
				}else{
					ObdMiles obdMiles = obdMilesService.getLatest(obdSn);
					if(obdMiles == null){
						obdMiles = new ObdMiles();
						obdMiles.setId(IDUtil.createID());
						obdMiles.setObdSn(obdSn);
						obdMiles.setMiles(totalMiles);
						obdMiles.setCreateTime(new Date());
					}else{
						obdHandlerPositionLogger.info("<"+obdSn+">【车端私有协议参数】值大则更新!前一总里程:"+obdMiles.getMiles());
						obdMiles.setUpdateTime(new Date());
						obdMiles.setMiles(totalMiles);
					}
					obdMilesService.milesSave(obdMiles);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgBody;
	}

	private String travelDataParamHandler(String obdSn,String msgBody) {
		//行程数据参数
		try {
			String[] cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String travelDataParam = cutStrs[0];
			msgBody = cutStrs[1];
			char[] travelDataParamBits = StrUtil.hexToBinary(travelDataParam);
			if (travelDataParamBits[0] =='1') {
				//行程开始时间
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 6);
				String travelStart = cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerPositionLogger.info("<"+obdSn+">【行程数据参数】行程开始时间:"+travelStart);
				Date date = ThreadLocalDateUtil.parse("yyyyMMddHHmmss", "20"+travelStart);
				String dateStr = ThreadLocalDateUtil.formatDate("yyyyMMddHHmmss", date);
				travelDataUtil.putTravelStart(obdSn, dateStr);
			}
			if (travelDataParamBits[1] =='1') {
				//行使距离
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String travelMile = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _travelMile = Integer.valueOf(travelMile, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">【行程数据参数】行使距离(10米):"+travelMile+">"+_travelMile);
				String lastTravelStartStr = travelDataUtil.getTravelStart(obdSn);
				if(!StringUtils.isEmpty(lastTravelStartStr) && !"ffff".equals(travelMile) && !"0000".equals(travelMile)){
					String lastTravelMile = travelDataUtil.getTravelMile(obdSn);
					if(StringUtils.isEmpty(lastTravelMile)){
						travelDataUtil.putTravelMile(obdSn, _travelMile.toString());
					}else{
						if(_travelMile > Integer.valueOf(lastTravelMile)){//当前行程大于上次行程才会更新此值
							travelDataUtil.putTravelMile(obdSn, _travelMile.toString());
						}
					}
				}
			}
			if (travelDataParamBits[2] =='1') {
				//油耗
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String travelOil = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _travelOil = Integer.valueOf(travelOil, 16);
				obdHandlerPositionLogger.info("<"+obdSn+">【行程数据参数】行使油耗(10mL):"+travelOil+">"+ _travelOil);
				String lastTravelStartStr = travelDataUtil.getTravelStart(obdSn);
				if(!StringUtils.isEmpty(lastTravelStartStr) && !"ffff".equals(travelOil) && !"0000".equals(travelOil)){
					String lastTravelOil = travelDataUtil.getTravelOil(obdSn);
					if(StringUtils.isEmpty(lastTravelOil)){
						travelDataUtil.putTravelOil(obdSn, _travelOil.toString());
					}else{
						if(_travelOil > Integer.valueOf(lastTravelOil)){//当前油耗大于上次油耗才会更新此值
							travelDataUtil.putTravelOil(obdSn, _travelOil.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgBody;
	}

	/**
	 * 入库操作
	 * @param obdSn
	 * @param positionInfo
	 * @param wifiFlow
	 */
	private void saveWifiFlow(String obdSn, PositionInfo positionInfo,
			Long wifiFlow,boolean sameWaterNo) {

//		if(positionInfoService.queryBySnAndTime(positionInfo.getObdSn(),positionInfo.getTime()) == null){
		if(!sameWaterNo){//不是同一流水号确定不会重复
			//用位置时间记录来判断是否曾经入库
			SimStockInfo simStockInfo = simStockInfoService.queryLastByObdSn(obdSn);
			if(simStockInfo != null){
				Long tempFlowUse = simStockInfo.getTempFlowUse();
				obdHandlerPositionLogger.info("<"+obdSn+">---WiFi 流量(KB):基值流量统计->"+simStockInfo.getFlowUse());
				obdHandlerPositionLogger.info("<"+obdSn+">---WiFi 流量(KB):上次临时流量统计->"+tempFlowUse);
				obdHandlerPositionLogger.info("<"+obdSn+">---WiFi 流量(KB):当前上传临时流量统计->"+wifiFlow);
				simStockInfo.setTempFlowUse(wifiFlow);
				simStockInfo.setUpdateTime(new Date());//更新下时间
				simStockInfoService.simStockInfoUpdate(simStockInfo);
			}else{
				simStockInfo = new SimStockInfo(IDUtil.createID()); 
				simStockInfo.setValid(1);
				simStockInfo.setObdSn(obdSn);
				simStockInfo.setTempFlowUse(wifiFlow);
				simStockInfo.setFlowUse(0L);
				obdHandlerPositionLogger.info("<"+obdSn+">---WiFi 流量(KB):首次入库->"+wifiFlow);
				simStockInfo.setCreateTime(new Date());
				simStockInfoService.add(simStockInfo);
			}
		}else{
			obdHandlerPositionLogger.info("<"+obdSn+">---WiFi 流量(KB),库里已有此记录。");
		}
	}

	/**
	 * 验证经纬度正确性与否
	 * @param lnglat
	 * @return false错 true对
	 */
	private boolean validateLngLat(String lnglat) {
		String str = CoordinateTransferUtil.format(lnglat);
		if(!StrUtil.isDigit(str) || str.contains("00000000")){
			//非数字
			return true;
		}
		return false;
	}

	/**
	 * 从位置信息中抽取数据保存——驾驶记录
	 * @param positionInfo
	 * @throws ParseException 
	 */
	private void saveGSPTrack(PositionInfo positionInfo, boolean last) throws ParseException {
		String obdSn = positionInfo.getObdSn();
		//保存驾驶记录，定位不到不保存
		if(1 == positionInfo.getStatusGPS() && !StringUtils.isEmpty(positionInfo.getLatitude()) &&
				!StringUtils.isEmpty(positionInfo.getLongitude())){
			CarGSPTrack gspTrack = new CarGSPTrack();
			gspTrack.setGspTrackId(IDUtil.createID());
			gspTrack.setObdSn(positionInfo.getObdSn());
			gspTrack.setDirectionAngle(positionInfo.getDirectionAngle());
			gspTrack.setGpsSpeed(positionInfo.getGpsSpeed());
			gspTrack.setObdSpeed(positionInfo.getObdSpeed());
			gspTrack.setHigh("");//高度暂无
			gspTrack.setLatitude(positionInfo.getLatitude());
			gspTrack.setLongitude(positionInfo.getLongitude());
			gspTrack.setGspTrackTime(positionInfo.getTime());
//			gspTrackService.carGSPSave(gspTrack);
			addCarGSPTrack(gspTrack);
			obdHandlerPositionLogger.info("<"+obdSn+">从位置信息中抽取数据保存【驾驶记录成功】：" + gspTrack);
			if(last && GlobalData.openLastPositionRedis){//最后一个位置 + 开缓存
				String lastPostion = jedisServiceUtil.getHSetByRedis(ObdRedisData.OBD_LastPosition_KEY, obdSn);
				JSONObject lastJsonObject = JSONObject.fromObject(lastPostion);
				boolean isUpdateCache = false;
				if(lastJsonObject.isEmpty() || StringUtils.isEmpty(lastPostion) || "null".equals(lastPostion)){
					isUpdateCache = true;
				}else{
					Date lastDate = ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", lastJsonObject.optString("gspTrackTime"));
					if(gspTrack.getGspTrackTime().getTime() > lastDate.getTime()){//最新位置时间大于上一位置时间才进行更新缓存
						isUpdateCache = true;
					}
				}
				
				if(isUpdateCache){
					JsonConfig jsonConfig = new JsonConfig();
			        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());   
					JSONObject jsonObject = JSONObject.fromObject(gspTrack,jsonConfig);
					String gspTrackJSON = jsonObject.toString();
					jedisServiceUtil.putHSetByRedis(ObdRedisData.OBD_LastPosition_KEY, obdSn, gspTrackJSON);
					obdHandlerPositionLogger.info("<"+obdSn+">最后一个位置信息JSONObject数据：" + gspTrackJSON);
				}
			}
		}
	}

	/**
	 * 报警数据处理
	 * @param msgBody
	 * @param positionWarnInfo 
	 * @return
	 */
	private String warnHandler(String obdSn,String msgBody, PositionWarnInfo positionWarnInfo) {
		String[] cutStrs = null;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String warnData = cutStrs[0];
			msgBody = cutStrs[1];
			char[] warnChar = StrUtil.hexToBinary(warnData);
			if('1' == warnChar[0]){
				obdHandlerPositionLogger.info("<"+obdSn+">非法启动报警了！");
				positionWarnInfo.setIllegalStartUp(1);
//				//如果carInfo表的carState字段为01，且该设备有报文上来，推送
//				CarInfo carInfo = carInfoService.queryCarInfoBySN(obdSn);
//				if(carInfo != null && "01".equals(carInfo.getCarState())){
//					pushApi.pushWarnHandler(obdSn,WarnType.IllegalStartUp,"11", "非法启动(布防时才会产生)报警");
//				}
			}
			if('1' == warnChar[1]){
				obdHandlerPositionLogger.info("<"+obdSn+">非法震动报警了！");
				positionWarnInfo.setIllegalShake(1);
//				pushApi.pushWarnHandler(obdSn,WarnType.IllegalShock, "12", "异常震动报警");
			}
			
			if('1' == warnChar[2]){
				obdHandlerPositionLogger.info("<"+obdSn+">蓄电电压低报警了！");
				positionWarnInfo.setBatteryLow(1);
			}
			
			if('1' == warnChar[3]){
				obdHandlerPositionLogger.info("<"+obdSn+">蓄电电压高报警了！");
				positionWarnInfo.setBatteryUp(1);
			}
			
			if('1' == warnChar[4]){
				obdHandlerPositionLogger.info("<"+obdSn+">发动机水温高报警了！");
				positionWarnInfo.setEngineTemperature(1);
			}
			
			if('1' == warnChar[5]){
				obdHandlerPositionLogger.info("<"+obdSn+">车辆故障报警了！");
				positionWarnInfo.setVehicleFailure(1);
			}
			
			if('1' == warnChar[6]){
				obdHandlerPositionLogger.info("<"+obdSn+">超速报警了！");
				positionWarnInfo.setOverSpeed(1);
//				pushApi.pushWarnHandler(obdSn,WarnType.OverSpeed,"27", "超速报警");//处理预警信息推送
			}
			
			boolean buttonAlarm = false;//是否按钮报警
			if('1' == warnChar[7]){//按键报警
				obdHandlerPositionLogger.info("<"+obdSn+">按键报警了！");
				positionWarnInfo.setElecFenceOut(1);
				buttonAlarm = true;
			}
			if(GlobalData.buttonAlarmSwitch){//按键报警开关
				handlerButtonAlarm(positionWarnInfo, obdSn, buttonAlarm);
			}
			
			if('1' == warnChar[8]){//保留
//				obdHandlerPositionLogger.info("<"+obdSn+">电子围栏进入报警了！");
//				positionWarnInfo.setElecFenceIn(1);
			}

			if('1' == warnChar[9]){
				obdHandlerPositionLogger.info("<"+obdSn+">车辆侧翻报警了！");
				positionWarnInfo.setVehicleSideDown(1);
			}
			if('1' == warnChar[10]){
				obdHandlerPositionLogger.info("<"+obdSn+">车辆碰撞报警了！");
				positionWarnInfo.setVehicleCrash(1);
			}

			if('1' == warnChar[11]){
				obdHandlerPositionLogger.info("<"+obdSn+">ECU通信故障报警了！");
				positionWarnInfo.setEcu(1);
				GlobalData.OBD_PositionECUWarn.put(obdSn, true);
			}else{
				GlobalData.OBD_PositionECUWarn.put(obdSn, false);
			}
			
			if('1' == warnChar[12]){
				obdHandlerPositionLogger.info("<"+obdSn+">GPS模块故障报警了！");
				positionWarnInfo.setGps(1);
			}
			if('1' == warnChar[13]){
				obdHandlerPositionLogger.info("<"+obdSn+">EEPROM故障报警了！");
				positionWarnInfo.setEeprom(1);
			}
			if('1' == warnChar[14]){
				obdHandlerPositionLogger.info("<"+obdSn+">加速度传感器故障了！");
				positionWarnInfo.setAccelerateSensor(1);
			}
			if('1' == warnChar[15]){
				obdHandlerPositionLogger.info("<"+obdSn+">离线数据提醒了！");
				positionWarnInfo.setOffData(1);
				final String _obdSn = obdSn;
				new Thread() {
					@Override
					public void run() {
						boolean canReq = !jedisServiceUtil.validTTL(ObdRedisData.OffLineData_TTL, _obdSn);
						Long ttl = jedisServiceUtil.getTTL(ObdRedisData.OffLineData_TTL, _obdSn);
						obdHandlerPositionLogger.info("<"+_obdSn+">离线数据能否请求："+canReq+",ttl:"+ttl);
						if(/*!GlobalData.OBD_Packet.containsKey(_obdSn+ObdConstants.OFFData) &&*/ canReq){
							serverRequestQueryService.offData(_obdSn, 0, false);
							serverRequestQueryService.offTravel(_obdSn, 0, false);
							obdHandlerPositionLogger.info("<"+_obdSn+">离线数据下发请求！");
							//记录下当前请求离线数据
							jedisServiceUtil.ttl(ObdRedisData.OffLineData_TTL, _obdSn, 60);
						}else{
//							obdHandlerPositionLogger.info("<"+_obdSn+">离线数据下发不请求位置数据！"+GlobalData.OBD_Packet.get(_obdSn+ObdConstants.OFFData));
							obdHandlerPositionLogger.info("<"+_obdSn+">----有离线数据！--不能请求,ttl:"
									 + jedisServiceUtil.getTTL(ObdRedisData.OffLineData_TTL, _obdSn));
						}
//						if(/*!GlobalData.OBD_Packet.containsKey(_obdSn+ObdConstants.OFFTravel) &&*/ canReq){
//							obdHandlerPositionLogger.info("<"+_obdSn+">离线行程数据下发请求！");
//						}else{
//							obdHandlerPositionLogger.info("<"+_obdSn+">离线数据下发不请求行程数据！"+GlobalData.OBD_Packet.get(_obdSn+ObdConstants.OFFTravel));
//						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerPositionLogger.error(obdSn+e);
		}
		return msgBody;
	}
	
	/**
	 * 处理按钮报警
	 * @throws Exception 
	 */
	private void handlerButtonAlarm(PositionWarnInfo positionWarnInfo, String obdSn, boolean buttonAlarm) throws Exception {
		obdHandlerPositionLogger.info("<"+obdSn+">按键报警状态："+buttonAlarm);
		ObdButtonAlarm obdButtonAlarm = obdButtonAlarmService.queryLast(obdSn);
		ObdButtonAlarm obdButtonAlarmNew = null;
		if(obdButtonAlarm == null){
			obdButtonAlarmNew = new ObdButtonAlarm();
			obdButtonAlarmNew.setState(buttonAlarm?1:0);
			obdHandlerPositionLogger.info("<"+obdSn+">按键报警记录首次保存！");
		}else{
			int lastState = obdButtonAlarm.getState().intValue();
			//前次无报警+当前报警 || 前次消除报警+当前报警 => 记录发生告警
			if((lastState == 0 && buttonAlarm) || (lastState == 2 && buttonAlarm)){
				obdButtonAlarmNew = new ObdButtonAlarm();
				obdButtonAlarmNew.setState(1);
				obdHandlerPositionLogger.info("<"+obdSn+">发生按键报警！");
			}
			//前次发生报警+当前无报警 => 记录报警消除
			if(lastState == 1 && !buttonAlarm){
				obdButtonAlarmNew = new ObdButtonAlarm();
				obdButtonAlarmNew.setState(2);
				obdHandlerPositionLogger.info("<"+obdSn+">消除按键报警！");
			}
		}
		if(obdButtonAlarmNew != null){//最新变更记录入库
			obdButtonAlarmNew.setId(IDUtil.createID());
			obdButtonAlarmNew.setObdSn(obdSn);
			obdButtonAlarmNew.setPositionInfoId(positionWarnInfo.getPositionInfoId());
			obdButtonAlarmNew.setTime(positionWarnInfo.getTime());
			obdButtonAlarmNew.setBit(buttonAlarm?1:0);
			obdButtonAlarmNew.setCreateTime(new Date());
			obdButtonAlarmService.obdButtonAlarmSaveOrUpdate(obdButtonAlarmNew);
		}
	}
	
//	/**
//	 * 处理预警信息推送
//	 * @param obdSn
//	 */
//	public void pushWarnHandler(final String obdSn,WarnType pushType,final String type,final String desc) {
//		pushApi.pushWarnHandler(obdSn, pushType, type, desc);
//		boolean canPush = PushUtil.canPush(obdSn+"_"+pushType, new Date());
//		obdHandlerPositionLogger.info(obdSn+"->"+pushType+",是否能推送："+canPush+"->"+PushUtil.PushApiTime.get(obdSn+"_"+pushType));
//		if(canPush){
//			//推送
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						String result = pushWarn(obdSn,type,desc);
//						if(!"0".equals(result)){
//							//推送不成功，再来一次
//							obdHandlerPositionLogger.info("<"+obdSn+">推送不成功！再来一次。");
//							pushWarn(obdSn,type,desc);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						obdHandlerPositionLogger.error("<"+obdSn+">推送失败："+e);
//					}
//				}
//			});
//		}
//	}

//	/**
//	 * 推送预警信息
//	 * @param obdSn
//	 * @param type
//	 * @param desc
//	 * @return
//	 * @throws Exception 
//	 */
//	private String pushWarn(String obdSn,String type,String desc) throws Exception{
//		Date date=  new Date();
//		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);
//		if(obdStockInfo == null){
//			throw new Exception("<"+obdSn+">设备对象为空："+obdSn);
//		}
//		String obdMsn = obdStockInfo.getObdMSn();//表面号
//		try {
//			String result = new PushApi().pushWarningInfo(obdMsn, type,
//					(String) DateUtil.fromatDate(date,"yyyy-MM-dd HH:mm:ss")	, desc);
//			obdHandlerPositionLogger.info(obdSn+"->推送结果："+result);
//			//入库操作
//			WarningMessage wm = new WarningMessage();
//			wm.setObdSn(obdSn);
//			wm.setMessageType(type);
//			wm.setMessageDesc(desc);
//			wm.setMessageTime(date);
//			wm.setRemark(result);//是否成功
//			warningMessageService.warningMsgSave(wm);
//
//			return "0";
//		} catch (Exception e) {
//			e.printStackTrace();
//			obdHandlerPositionLogger.error(obdSn+"->推送结果："+e);
//		}
//		return null;
//	}
	
	/**
	 * 驾驶行为数据处理
	 * @param msgBody
	 * @param positionDriveInfo 
	 * @return
	 */
	private String driveBehaviorHandler(String obdSn,String msgBody, PositionDriveInfo positionDriveInfo) {
		String[] cutStrs = null;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String driveBehaviorData = cutStrs[0];
			msgBody = cutStrs[1];
			char[] driveChar = StrUtil.hexToBinary(driveBehaviorData);
			ObdState obdState = obdSateService.queryByObdSn(obdSn);
			if('0' == driveChar[0]){
				obdHandlerPositionLogger.info("<"+obdSn+">超速了！");
				positionDriveInfo.setOverspeed(1);
				if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
						&& "1".equals(obdState.getOverspeedSwitch())){
					pushApi.pushWarnHandler(obdSn,WarnType.OverSpeed,"27", "超速报警");//处理预警信息推送
				}
			}
			if('0' == driveChar[1]){
				obdHandlerPositionLogger.info("<"+obdSn+">急转弯了！");
				positionDriveInfo.setSuddenTurn(1);
			}
			if('0' == driveChar[2]){
				obdHandlerPositionLogger.info("<"+obdSn+">急加速了！");
				positionDriveInfo.setSuddenUpSpeed(1);
				if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
						&& "1".equals(obdState.getRapidSpeedChangeSwitch())){
					pushApi.pushWarnHandler(obdSn,WarnType.RapidSpeedChangePlus,"21", "急变速-急加速报警");//处理预警信息推送
				}
			}
			if('0' == driveChar[3]){
				obdHandlerPositionLogger.info("<"+obdSn+">急减速了！");
				positionDriveInfo.setSuddenDownSpeed(1);
				if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
						&& "1".equals(obdState.getRapidSpeedChangeSwitch())){
					pushApi.pushWarnHandler(obdSn,WarnType.RapidSpeedChangeMinus,"21", "急变速-急减速报警");//处理预警信息推送
				}
			}
			if('0' == driveChar[4]){
				obdHandlerPositionLogger.info("<"+obdSn+">急刹车了！");
				positionDriveInfo.setSuddenBrake(1);
			}
			if('0' == driveChar[5]){
				obdHandlerPositionLogger.info("<"+obdSn+">急变道了！");
				positionDriveInfo.setSuddenChangeRoad(1);
			}
			if('0' == driveChar[6]){
				obdHandlerPositionLogger.info("<"+obdSn+">车速转速不匹配了！");
				positionDriveInfo.setSpeedNotMatch(1);
			}
			if('0' == driveChar[7]){
				obdHandlerPositionLogger.info("<"+obdSn+">长怠速了！");
				positionDriveInfo.setLongLowSpeed(1);
				if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
						&& "1".equals(obdState.getIdlingSwitch())){
					pushApi.pushWarnHandler(obdSn,WarnType.Idling,"30", "怠速报警");//处理预警信息推送
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerPositionLogger.error(obdSn+e);
		}
		return msgBody;
	}
	
}
