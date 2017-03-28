package com.hgsoft.obd.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.carowner.entity.OBDServerParams;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.service.FaultUploadService;
import com.hgsoft.carowner.service.OBDDeviceVersionService;
import com.hgsoft.carowner.service.ObdServerParamsService;
import com.hgsoft.carowner.service.ObdTimeParamsService;
import com.hgsoft.carowner.service.ObdTravelParamsService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerResponse;
import com.hgsoft.obd.util.ExtensionDataEnum;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
import com.hgsoft.obd.util.ServerParamsType;

/**
 * 设备上传通讯——设备数据上传
 * @author sujunguang
 * 2015年12月12日
 * 下午4:26:20
 */
@Service
public class MessageObdHandler implements IMessageObd{

	private static Logger obdHandlerDeviceDataLogger = LogManager.getLogger("obdHandlerDeviceDataLogger");
	
	@Resource
	private MessageObdTravelHandler messageObdTravelHandler;
	@Resource
	private MessageObdPositionHandler messageObdPositionHandler;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private FaultUploadService faultUploadService;
	@Resource
	private OBDDeviceVersionService obdDeviceVersionService;
	@Resource
	private ObdServerParamsService obdServerParamsService;
	@Resource
	private ObdTimeParamsService obdTimeParamsService;
	@Resource
	private ObdTravelParamsService obdTravelParamsService;
	@Resource
	private SendUtil sendUtil;
	@Resource
	private JedisServiceUtil jedisServiceUtil;
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		String obdSn = message.getId();
		obdHandlerDeviceDataLogger.info("----------------【设备上传通讯——设备数据上传】---------------");
		obdHandlerDeviceDataLogger.info("------------"+obdSn+"--报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		obdHandlerDeviceDataLogger.info("------------设备："+obdSn+"------------");
		String retrunMsgBody = "success";
		String[] cutStrs ;//截取结果数组
		String msgBody = message.getMsgBody();
		try {
			/**************常用数据begin****************/
			//命令字:0x00 ox04
//			String command = message.getCommand();
			
			//数据帧
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String dataFrame = cutStrs[0];
			msgBody = cutStrs[1];
			char bits[] = StrUtil.hexToBinary(dataFrame);
			
			//协议上的不足，处理无故障码特征：dataFrame=0000
			if("0000".equals(dataFrame)){//0、无故障码
				FaultUpload faultCode = new FaultUpload();
				faultCode.setFaultId(IDUtil.createID());
				faultCode.setObdSn(obdSn);
				faultCode.setState("0");
				String key = obdSn+ ObdConstants.keySpilt + ObdConstants.FaultCode;
				//入库
				faultCode.setCreateTime(new Date());
				faultUploadService.add(faultCode);
				GlobalData.putExitsQueryDataToMap(key, faultCode);
			}
			
			if(bits[0] == '1'){//故障码：存在
				/**故障码报文**/
				//故障码长度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				Integer faultCodeLength = Integer.valueOf(cutStrs[0],16);
				msgBody = cutStrs[1];
				//故障码:每个故障码三个字节，0x50 0x32 0x33表示故障码P3233
				if(faultCodeLength == 0){
					FaultUpload faultCode = new FaultUpload();
					faultCode.setFaultId(IDUtil.createID());
					faultCode.setObdSn(obdSn);
					faultCode.setState("0");
					//入库
					faultCode.setCreateTime(new Date());
					faultUploadService.add(faultCode);
					String key = obdSn+ ObdConstants.keySpilt + ObdConstants.FaultCode;
					GlobalData.putExitsQueryDataToMap(key, faultCode);
				}else if(faultCodeLength == 255){
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String type = cutStrs[0];//不能体检类型：01-速度不为0，02-ECU故障
					obdHandlerDeviceDataLogger.info("<"+obdSn+">不能体检类型->"+type);
					msgBody = cutStrs[1];
					FaultUpload faultCode = new FaultUpload();
					faultCode.setFaultId(IDUtil.createID());
					faultCode.setObdSn(obdSn);
					if("01".equals(type)){
						faultCode.setState("2");
					}else if("02".equals(type)){
						faultCode.setState("3");
					}
					//入库
					faultCode.setCreateTime(new Date());
					faultUploadService.add(faultCode);
					String key = obdSn+ ObdConstants.keySpilt + ObdConstants.FaultCode;
					GlobalData.putExitsQueryDataToMap(key, faultCode);
				}else{
					List<FaultUpload> faultUploads = new ArrayList<FaultUpload>();
					for (int i = 0; i < faultCodeLength/3; i++) {
						cutStrs = StrUtil.cutStrByByteNum(msgBody, 3);
						String faultType = cutStrs[0].substring(0,2);
						String faultCode = cutStrs[0].substring(2);
						msgBody = cutStrs[1];
						obdHandlerDeviceDataLogger.info("<"+obdSn+">故障码"+cutStrs[0]+"->第"+(i+1)+"个:"+StrUtil.hex2ASCII(faultType)+faultCode);
						
						FaultUpload faultUpload = new FaultUpload();
						faultUpload.setCreateTime(new Date());
						faultUpload.setFaultCode(StrUtil.hex2ASCII(faultType)+faultCode);
						faultUpload.setFaultId(IDUtil.createID());
						faultUpload.setObdSn(message.getId());
						faultUpload.setState("1");//有故障码
						faultUploads.add(faultUpload);
						//入库
						faultUploadService.add(faultUpload);
					}
					String key = obdSn+ ObdConstants.keySpilt + ObdConstants.FaultCode;
					GlobalData.putExitsQueryDataToMap(key, faultUploads);
				}
			}
			if(bits[1] == '1'){//离线位置数据：存在
				msgBody = offLineLocationData(msgBody,message.getId(), message.getWaterNo(), true);//离线位置数据，true
			}
			if(bits[2] == '1'){//离线行程单：存在
				msgBody = offLineLocationData(msgBody,message.getId(), message.getWaterNo(), false);//离线行程单，false
			}
			if(bits[3] == '1'){//设备状态：存在（服务器下发参数设置命令时不用）
				msgBody = deviceStateHandler(obdSn,msgBody);//设备状态数据处理
			}
			if(bits[4] == '1'){//车辆状态：存在（服务器下发参数设置命令时不用）
				msgBody = carStateHandler(obdSn,msgBody);//设备状态数据处理
			}
 			if(bits[5] == '1'){//报警设置：存在
				msgBody = warnSetHandler(obdSn,msgBody);//报警设置数据处理
			}
 			if(bits[6] == '1'){//车辆运行状态 TODO 暂不能用
				msgBody = carRunStateHandler(obdSn,msgBody);//车辆运行状态数据处理
			}
 			if(bits[7] == '1'){//设备时间参数
				msgBody = deviceTimeParamsHandler(obdSn,msgBody);//设备时间参数
			}
 			if(bits[8] == '1'){//行程参数
				msgBody = travelParamsHandler(obdSn,msgBody);//行程参数
			}
 			if(bits[9] == '1'){//总里程，弃用 -> 改为：半条行程的查询
				obdHandlerDeviceDataLogger.info("-------半条行程--------设备号："+"<"+obdSn+">"+msgBody);
				String key = obdSn + ObdConstants.keySpilt + ObdConstants.HalfTravel;
				GlobalData.putExitsQueryDataToMap(key, msgBody);
				OBDMessage messageHalf = new OBDMessage();
				messageHalf.setId(obdSn);
				messageHalf.setWaterNo(message.getWaterNo());
				messageHalf.setCommand("-1");
				messageHalf.setMsgBody(msgBody);
				messageHalf.setMessage(msgBody);
				// 半条行程
				messageObdTravelHandler.entry(messageHalf);
			}
 			if(bits[10] == '1'){//数据服务器参数
				msgBody = serverParamsHandler(obdSn,msgBody,ServerParamsType.DATA);
			}
 			if(bits[11] == '1'){//升级服务器参数
				msgBody = serverParamsHandler(obdSn,msgBody,ServerParamsType.UPGRADE);
			}
 			if(bits[12] == '1'){//Protal服务器参数
				msgBody = serverParamsHandler(obdSn,msgBody,ServerParamsType.PORTAL);
			}
 			if(bits[13] == '1'){//设备版本信息
				msgBody = deviceVersionHandler(obdSn, msgBody);
			}
 			if(bits[14] == '1'){//实时位置
				msgBody = realTimeLocHandler(obdSn,msgBody);
			}
 			if(bits[15] == '1'){//已弃用
				msgBody = wiFiFlowHandler(obdSn,msgBody);
			}
		}catch(Exception e){
			obdHandlerDeviceDataLogger.error(obdSn, e);
			return "error";
		}
		return retrunMsgBody;
	}

	/**
	 * WiFi 流量查询 (字节)
	 * @param obdSn
	 * @param msgBody
	 * @return
	 */
	private String wiFiFlowHandler(String obdSn, String msgBody) {
		obdHandlerDeviceDataLogger.info("---------"+obdSn+"---WiFi 流量查询------------");
		String[] cutStrs;
		Integer wifiFlow = null;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
			String wifiFlowStr = cutStrs[0];
			msgBody = cutStrs[1];
			wifiFlow = Integer.valueOf(wifiFlowStr, 16)*1024;
			obdHandlerDeviceDataLogger.info("---------"+obdSn+"---WiFi 流量查询(B):"+wifiFlowStr+"*1024->"+wifiFlow);
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(e.getMessage());
		}
		String key = obdSn + ObdConstants.keySpilt + ExtensionDataEnum.WiFiFlow;//临时用
		GlobalData.putExitsQueryDataToMap(key, wifiFlow);
		return msgBody;
	}

	
	/**
	 * 实时位置信息处理
	 * @param obdSn
	 * @param msgBody
	 * @return
	 */
	private String realTimeLocHandler(String obdSn, String msgBody) {
		obdHandlerDeviceDataLogger.info("---------"+obdSn+"---实时位置处理------------");
		String[] cutStrs;
		String position = null;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
			String longtitude = cutStrs[0];
			msgBody = cutStrs[1];
			
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
			String latitude = cutStrs[0];
			msgBody = cutStrs[1];
			
			String _longtitude = longtitude.substring(0,3)+"°"+longtitude.substring(3,5)+"."+longtitude.substring(5)+"'";
			String _latitude = latitude.substring(0,2)+"°"+latitude.substring(2,4)+"."+latitude.substring(4)+"'";
			position = _longtitude + "," + _latitude;
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(obdSn, e);
		}
		String key = obdSn + ObdConstants.keySpilt + ObdConstants.RealTimeLoc;
		GlobalData.putExitsQueryDataToMap(key, position);
		return msgBody;
	}

	/**
	 * 设备版本信息数据处理
	 * @param msgBody
	 * @return
	 */
	private String deviceVersionHandler(String obdSn, String msgBody) {
		obdHandlerDeviceDataLogger.info("---------"+obdSn+"---设备版本信息数据处理------------");
		String[] cutStrs;
		OBDDeviceVersion obdDeviceVersion = new OBDDeviceVersion();
		obdDeviceVersion.setId(IDUtil.createID());
		obdDeviceVersion.setObdSn(obdSn);
		try {
			//应用程序固件版本号
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String appVersion = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">应用程序固件版本号:"+appVersion);
			obdDeviceVersion.setAppVersion(appVersion);
			
			//IAP程序固件版本号
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String iapVersion = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">IAP程序固件版本号:"+iapVersion);
			obdDeviceVersion.setIapVersion(iapVersion);
			
			//MIFI模块固件版本号长度
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String mifiVersionLength = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">MIFI模块固件版长度:"+mifiVersionLength);
			
			//MIFI模块固件版本号
			cutStrs = StrUtil.cutStrByByteNum(msgBody, Integer.valueOf(mifiVersionLength,16));
			String mifiVersion = cutStrs[0];
			msgBody = cutStrs[1];
			String _mifiVersion = StrUtil.hex2ASCII(mifiVersion);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">MIFI模块固件版本号:"+mifiVersion+"->"+_mifiVersion);
			obdDeviceVersion.setMifiVersion(_mifiVersion);
			
			//GPS硬件版本号
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String gpsVersion = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS硬件版本号:"+gpsVersion);
			obdDeviceVersion.setGpsVersion(gpsVersion);
			
			//MIFI模块硬件版本号
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String mifiHardwareVersion = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">MIFI模块硬件版本号:"+mifiHardwareVersion);
			obdDeviceVersion.setMifiHardwareVersion(mifiHardwareVersion);
			String key = obdSn + ObdConstants.keySpilt + ObdConstants.ObdDeviceVersion;
			GlobalData.putExitsQueryDataToMap(key, obdDeviceVersion);
			//入库
			obdDeviceVersion.setCreateTime(new Date());
			obdDeviceVersionService.add(obdDeviceVersion);
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(e);
		}
		return msgBody;
	}
	
	/**
	 * XX服务器参数数据处理
	 * @param msgBody
	 * @param type 0-数据服务器参数 1-升级服务器参数 2-Protal服务器参数
	 * @return
	 */
	private String serverParamsHandler(String obdSn, String msgBody,ServerParamsType type) {
		obdHandlerDeviceDataLogger.info("---------"+obdSn+"---服务器参数数据处理-------------"+type);
		String key = obdSn;
		switch (type) {
			case DATA:
				key += ObdConstants.keySpilt+ObdConstants.DataServerParams;
				break;
			case UPGRADE:
				key += ObdConstants.keySpilt+ObdConstants.UpgradeServerParams;
				break;
			case PORTAL:
				key += ObdConstants.keySpilt+ObdConstants.PortalServerParams;
				break;
			default:
				break;
		}
		String[] cutStrs;
		OBDServerParams obdServerParams = new OBDServerParams();
		obdServerParams.setId(IDUtil.createID());
		obdServerParams.setObdSn(obdSn);
		try {
			//服务器端口
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String serverPort = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _serverPort = Integer.valueOf(serverPort, 16);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">服务器端口:"+serverPort+"->"+ _serverPort);
			obdServerParams.setPort(_serverPort);
			
			//服务器IP长度
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String serverIpLength = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _serverIpLength = Integer.valueOf(serverIpLength, 16);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">服务器IP长度:"+serverIpLength+"->"+ _serverIpLength);
			
			//服务器IP
			if(_serverIpLength > 0){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, _serverIpLength);
				String serverIp = cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">服务器IP:"+serverIp);
				obdServerParams.setIpAddress(serverIp);
			}
			
			//服务器APN长度
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String serverApnLength = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _serverApnLength = Integer.valueOf(serverApnLength, 16);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">服务器APN长度:"+serverApnLength+"->"+ _serverApnLength);
			
			//服务器APN
			if(_serverApnLength > 0){//长度》0说明有APN数据
				cutStrs = StrUtil.cutStrByByteNum(msgBody, _serverApnLength);
				String serverApn = cutStrs[0];//CMNET
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">服务器APN:"+serverApn);
				obdServerParams.setAPN(serverApn);
			}
			
			GlobalData.putExitsQueryDataToMap(key, obdServerParams);
			//入库
			obdServerParams.setType(type.ordinal());
			obdServerParams.setCreateTime(new Date());
			obdServerParamsService.add(obdServerParams);
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(obdSn+e);
		}
		return msgBody;
	}

	/**
	 * 行程参数数据处理
	 * @param msgBody
	 * @return
	 */
	private String travelParamsHandler(String obdSn, String msgBody) {
		obdHandlerDeviceDataLogger.info("------------"+obdSn+"---行程参数数据处理--------------");
		String[] cutStrs;
		String key = obdSn + ObdConstants.keySpilt + ObdConstants.TravelParams;
		OBDTravelParams obdTravelParams = new OBDTravelParams();
		obdTravelParams.setId(IDUtil.createID());
		obdTravelParams.setObdSn(obdSn);
		try {
			//行程参数帧格式
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String deviceTimeFrame = cutStrs[0];
			msgBody = cutStrs[1];
			
			char[] bits = StrUtil.hexToBinary(deviceTimeFrame);
			//蓄电池电压阈值—0-存在
			if(bits[0] == '0'){
				//蓄电池电压低阈值（0.1V）:0x79表示12.1V,<阈值报警 11.5V,小汽车，客货车阈值不同
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String batteryLowThreshold= cutStrs[0];
				Integer batteryLow = Integer.valueOf(batteryLowThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">蓄电池电压阈值——蓄电池电压低阈值（0.1V）:" + batteryLowThreshold);
				obdTravelParams.setBatteryLow(batteryLow);
				
				//蓄电池电压高阈值（0.1V）:0x79表示12.1V,>阈值报警 14.5V,小汽车，客货车阈值不同
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String batteryHighThreshold = cutStrs[0];
				Integer batteryHigh = Integer.valueOf(batteryHighThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">蓄电池电压阈值——蓄电池电压高阈值（0.1V）:" + batteryHighThreshold);
				obdTravelParams.setBatteryHigh(batteryHigh);
				
			}
			//超速阈值——时速超过阈值，且持续时间超过阈值，报警
			if(bits[1] == '0'){
				//时速阈值(Km/h):80km/h
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String timeSpeedThreshold= cutStrs[0];
				Integer overSpeed = Integer.valueOf(timeSpeedThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">超速阈值——时速阈值(Km/h):" + timeSpeedThreshold);
				obdTravelParams.setOverSpeed(overSpeed);
				
				//限速延迟时间阈值（S）:80S
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String limitSpeedLazyTimeThreshold = cutStrs[0];
				Integer limitSpeedLazy = Integer.valueOf(limitSpeedLazyTimeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">超速阈值——限速延迟时间阈值（S）:" + limitSpeedLazyTimeThreshold);
				obdTravelParams.setLimitSpeedLazy(limitSpeedLazy);
			}
			//急转弯阈值——速度与角度同时超过阈值，判为急转弯
			if(bits[2] == '0'){
				//速度阈值(Km/h)：40km/h
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String speedThreshold= cutStrs[0];
				Integer shuddenTurnSpeed = Integer.valueOf(speedThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急转弯阈值——速度阈值(Km/h):" + speedThreshold);
				obdTravelParams.setShuddenTurnSpeed(shuddenTurnSpeed);
				
				//角度阈值(度)：50度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String angleThreshold = cutStrs[0];
				Integer shuddenTurnAngle = Integer.valueOf(angleThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急加速阈值——速度变化阈值(Km/h):" + angleThreshold);
				obdTravelParams.setShuddenTurnAngle(shuddenTurnAngle);
				
			}
			//急加速阈值——速度在时间阈值之间的增加值超过设定的速度变化阈值时，判定为急加速
			if(bits[3] == '0'){
				//速度变化阈值(Km/h)：10.5km/h
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String speedChangeThreshold= cutStrs[0];
				Integer shuddenOverSpeed = Integer.valueOf(speedChangeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急加速阈值——速度变化阈值(Km/h):" + speedChangeThreshold);
				obdTravelParams.setShuddenOverSpeed(shuddenOverSpeed);
				
				//时间阈值（S）：2S
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String timeThreshold = cutStrs[0];
				Integer shuddenOverSpeedTime = Integer.valueOf(timeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急加速阈值——时间阈值（S）:" + timeThreshold);
				obdTravelParams.setShuddenOverSpeedTime(shuddenOverSpeedTime);
				
			}
			//急减速阈值——速度在时间阈值之间的减少值超过设定的速度变化阈值时，判定为急减速
			if(bits[4] == '0'){
				//速度变化阈值(Km/h)：14km/h
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String speedChangeThreshold= cutStrs[0];
				Integer shuddenLowSpeed = Integer.valueOf(speedChangeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急减速阈值——速度变化阈值(Km/h):" + speedChangeThreshold);
				obdTravelParams.setShuddenLowSpeed(shuddenLowSpeed);
				
				//时间阈值（S）：2S
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String timeThreshold = cutStrs[0];
				Integer shuddenLowSpeedTime = Integer.valueOf(timeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急减速阈值——角度阈值(Km/h):"+timeThreshold);
				obdTravelParams.setShuddenLowSpeedTime(shuddenLowSpeedTime);
				
			}
			//急变道阈值——角度在时间阈值之间的变化到>角度阈值时，判定为急变道
			if(bits[5] == '0'){
				//角度阈值(Km/h):50Km/h
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String angleThreshold= cutStrs[0];
				Integer shuddenChangeAngle = Integer.valueOf(angleThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急变道阈值——角度阈值(Km/h):"+angleThreshold);
				obdTravelParams.setShuddenChangeAngle(shuddenChangeAngle);
				
				//时间阈值（0.1S）:2S
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String timeThreshold = cutStrs[0];
				Integer shuddenChangeTime = Integer.valueOf(timeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急变道阈值——时间阈值（0.1S）:"+timeThreshold);
				obdTravelParams.setShuddenChangeTime(shuddenChangeTime);
				
			}
			//发动机水温报警阈值
			if(bits[6] == '0'){
				//低水温报警阈值(摄氏度)：发动机水温<水温报警阈值 80度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String lowWaterWarnThreshold= cutStrs[0];
				Integer engineLowTemperature = Integer.valueOf(lowWaterWarnThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">低水温报警阈值(摄氏度)："+lowWaterWarnThreshold);
				obdTravelParams.setEngineLowTemperature(engineLowTemperature);
				
				//高水温报警阈值(摄氏度)：发动机水温>水温报警阈值  115度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String highWaterWarnThreshold = cutStrs[0];
				Integer engineHighTemperature = Integer.valueOf(highWaterWarnThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">高水温报警阈值(摄氏度)："+highWaterWarnThreshold);
				obdTravelParams.setEngineHighTemperature(engineHighTemperature);
				
			}
			//发动机转数报警阈值(转/分钟)——发动机转数>转数报警阈值 4500转/分钟
			if(bits[7] == '0'){
				//
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String engineTurnsThreshold= cutStrs[0];
				Integer engineTurnsWarn = Integer.valueOf(engineTurnsThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机转数报警阈值(转/分钟):"+engineTurnsThreshold);
				obdTravelParams.setEngineTurnsWarn(engineTurnsWarn);
				
			}
			//车速转速不匹配阈值——转速按照线性插值计算，大于设定转速，判断不匹配？
			if(bits[8] == '0'){
				//车速转速不匹配——转速按照线性插值计算，大于设定转速，判断不匹配？
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String speed30TurnSpeed = cutStrs[0];
				Integer speedNotMatch = Integer.valueOf(speed30TurnSpeed, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">速度30km/h时匹配转速(转/分钟):"+speed30TurnSpeed);
				obdTravelParams.setSpeedNotMatch(speedNotMatch);
				
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String turnSpeedStep = cutStrs[0];
				Integer speedNotMatchStep = Integer.valueOf(turnSpeedStep, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">转速步进值(转/分钟):"+turnSpeedStep);
				obdTravelParams.setSpeedNotMatchStep(speedNotMatchStep);

			}
			//长怠速阈值——车速怠速车速阈值，持续时间超过时间阈值，判定为怠速状态
			if(bits[9] == '0'){
				//怠速车速阈值（km/h）：5 km/h
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String longLowSpeedThreshold= cutStrs[0];
				Integer longLowSpeed = Integer.valueOf(longLowSpeedThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">怠速车速阈值（km/h）:"+longLowSpeedThreshold);
				obdTravelParams.setLongLowSpeed(longLowSpeed);
				
				//时间阈值（1分钟）：3分钟
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String longLowTimeThreshold = cutStrs[0];
				Integer longLowSpeedTime = Integer.valueOf(longLowTimeThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">时间阈值（1分钟）:"+longLowTimeThreshold);
				obdTravelParams.setLongLowSpeedTime(longLowSpeedTime);
				
			}
			//急刹车强度阈值—— >急刹车阈值，1-8； 3
			if(bits[10] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String brakesThreshold= cutStrs[0];
				Integer shuddenBrakeStrength = Integer.valueOf(brakesThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">急刹车强度阈值:"+brakesThreshold);
				obdTravelParams.setShuddenBrakeStrength(shuddenBrakeStrength);
			}
			//侧翻角度阈值（度）—— >侧翻角度阈值 90
			if(bits[11] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String rolloverThreshold= cutStrs[0];
				Integer sideTurnAngle = Integer.valueOf(rolloverThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">侧翻角度阈值（度）:"+rolloverThreshold);
				obdTravelParams.setSideTurnAngle(sideTurnAngle);
			}
			//碰撞强度阈值—— >车辆碰撞阈值，1-8
			if(bits[12] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String crashStrengthThreshold = cutStrs[0];
				Integer crashStrength = Integer.valueOf(crashStrengthThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">碰撞强度阈值:"+crashStrengthThreshold);
				obdTravelParams.setCrashStrength(crashStrength);
			}
			//震动报警强度阈值—— >震动报警强度阈值，1-8
			if(bits[13] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String shakeWarnStrengthThreshold= cutStrs[0];
				Integer shockStrength = Integer.valueOf(shakeWarnStrengthThreshold, 16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">震动报警强度阈值:"+shakeWarnStrengthThreshold);
				obdTravelParams.setShockStrength(shockStrength);
			}
			//电子围栏报警坐标
			if(bits[14] == '0'){
				msgBody = efenceHandler(obdSn, msgBody);
			}
			/*//电子围栏驶出报警坐标
			if(bits[14] == '0'){
				//经度:0x11 0x 60 0x40 0x00，116˚04.000΄
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String outLongtitude= cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("电子围栏驶出报警坐标,经度:"+outLongtitude);
				String fenceOutLongtitude = outLongtitude.substring(0,3)+"°"+outLongtitude.substring(3,5)+"."+outLongtitude.substring(5)+"'";
				obdTravelParams.setFenceOutLongtitude(fenceOutLongtitude);
				//纬度:0x33 0x32 0x00 0x00 表示：33˚32.0000΄
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String outLatitude = cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("电子围栏驶出报警坐标,纬度:"+outLatitude);
				String fenceOutLatitude = outLatitude.substring(0,2)+"°"+outLatitude.substring(2,4)+"."+outLatitude.substring(4)+"'";
				obdTravelParams.setFenceOutLatitude(fenceOutLatitude);
			}
			//电子围栏进入报警坐标
			if(bits[15] == '0'){
				//经度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String enterLongtitude= cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("电子围栏进入报警坐标,经度:"+enterLongtitude);
				String fenceInLongtitude = enterLongtitude.substring(0,3)+"°"+enterLongtitude.substring(3,5)+"."+enterLongtitude.substring(5)+"'";
				obdTravelParams.setFenceInLongtitude(fenceInLongtitude);
				//纬度
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String enterLatitude = cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("电子围栏进入报警坐标,纬度:"+enterLatitude);
				String fenceInLatitude = enterLatitude.substring(0,2)+"°"+enterLatitude.substring(2,4)+"."+enterLatitude.substring(4)+"'";
				obdTravelParams.setFenceInLatitude(fenceInLatitude);
			}*/
			
			obdTravelParams.setCreateTime(new Date());
			obdTravelParamsService.add(obdTravelParams);
			
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(e);
		}
		GlobalData.putExitsQueryDataToMap(key, obdTravelParams);
		return msgBody;
	}
	
	/**
	 * 电子栅信息处理
	 * @param obdSn
	 * @param msgBody
	 * @return
	 * @throws Exception
	 */
	private String efenceHandler(String obdSn, String msgBody) throws Exception{
		obdHandlerDeviceDataLogger.info("------------------【处理电子围栏信息】----------------");
		String[] cutStrs;
		cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
		String efenceLengthStr = cutStrs[0];
		msgBody = cutStrs[1];
		Integer efenceLength = Integer.valueOf(efenceLengthStr, 16);
		obdHandlerDeviceDataLogger.info("<"+obdSn+">电子围栏信息长度:"+efenceLengthStr+"->"+efenceLength);
		if(efenceLength > 0){
				
				for(int i=1; i <= 8; i++){//最多8轮数据
					obdHandlerDeviceDataLogger.info("<"+obdSn+">电子围栏信息轮数:"+i);
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String timingOrAreaNo = cutStrs[0];//定时定点/区域编号
					msgBody = cutStrs[1];
					char[] timingOrAreaNoBits = StrUtil.hexToBinary2(timingOrAreaNo);
					int areaNo = Integer.valueOf(timingOrAreaNo, 16);//区域编号
					if(timingOrAreaNoBits[0] == '1'){//有定时
						areaNo -= 128;
						obdHandlerDeviceDataLogger.info("<"+obdSn+">有定时时间");
					}else{//无定时
						obdHandlerDeviceDataLogger.info("<"+obdSn+">无定时时间");
					}
					obdHandlerDeviceDataLogger.info("<"+obdSn+">区域编号："+timingOrAreaNo+"->"+areaNo);
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String fenceTypeAndWarn = cutStrs[0];//围栏类型+报警方式
					msgBody = cutStrs[1];
					char[] fenceTypeAndWarnBits = StrUtil.hex2Bin(fenceTypeAndWarn);
					if(fenceTypeAndWarnBits[0] == '1'){
						//圆形
						obdHandlerDeviceDataLogger.info("<"+obdSn+">围栏类型：圆形 "+ fenceTypeAndWarnBits[0]);
					}else{
						//矩形
						obdHandlerDeviceDataLogger.info("<"+obdSn+">围栏类型：矩形 "+ fenceTypeAndWarnBits[0]);
					}
					String warnTypeStr = ""+fenceTypeAndWarnBits[4]+fenceTypeAndWarnBits[5]+
							fenceTypeAndWarnBits[6]+fenceTypeAndWarnBits[7];
					int warnType = Integer.valueOf(warnTypeStr, 2);
					if(warnType == 1){
						//进区域报警
						obdHandlerDeviceDataLogger.info("<"+obdSn+">报警方式：进区域报警 "+warnType);
					}else if(warnType == 2){
						//出区域报警
						obdHandlerDeviceDataLogger.info("<"+obdSn+">报警方式：出区域报警 "+warnType);
					}else if(warnType == 3){
						//（保留）
					}else if(warnType == 4){
						//取消围栏
						obdHandlerDeviceDataLogger.info("<"+obdSn+">报警方式：取消围栏 "+warnType);
					}else if(warnType == 5){
						//取消所有围栏
						obdHandlerDeviceDataLogger.info("<"+obdSn+">报警方式：取消所有围栏 "+warnType);
					}else{
						//未知
					}
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
					String bigLongitudeStr = cutStrs[0];//大经度
					msgBody = cutStrs[1];
					String bigLongitude = CoordinateTransferUtil.formatLongtitude(bigLongitudeStr);
					obdHandlerDeviceDataLogger.info("<"+obdSn+">大经度： "+bigLongitude);
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
					String bigLatitudeStr = cutStrs[0];//大纬度
					msgBody = cutStrs[1];
					String bigLatitude = CoordinateTransferUtil.formatLatitude(bigLatitudeStr);
					obdHandlerDeviceDataLogger.info("<"+obdSn+">大纬度： "+bigLatitude);
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
					String smallLongitudeStr = cutStrs[0];//小经度
					msgBody = cutStrs[1];
					String smallLongitude = CoordinateTransferUtil.formatLongtitude(smallLongitudeStr);
					obdHandlerDeviceDataLogger.info("<"+obdSn+">小经度： "+smallLongitude);
					
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
					String smallLatitudeStr = cutStrs[0];//小纬度
					msgBody = cutStrs[1];
					String smallLatitude = CoordinateTransferUtil.formatLatitude(smallLatitudeStr);
					obdHandlerDeviceDataLogger.info("<"+obdSn+">小纬度： "+smallLatitude);
					
					if(timingOrAreaNoBits[0] == '1'){
						//有定时时间
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						
						cutStrs = StrUtil.cutStrByByteNum(msgBody, 6);
						String timingBegin = cutStrs[0];//定时起始时间
						msgBody = cutStrs[1];
						Date timingBeginTime = sdf.parse("20"+timingBegin);
						obdHandlerDeviceDataLogger.info("<"+obdSn+">定时起始时间： "+timingBegin+"->"+timingBeginTime);
						
						cutStrs = StrUtil.cutStrByByteNum(msgBody, 6);
						String timingEnd = cutStrs[0];//定时结束时间
						msgBody = cutStrs[1];
						Date timingEndTime = sdf.parse("20"+timingEnd);
						obdHandlerDeviceDataLogger.info("<"+obdSn+">定时结束时间： "+timingEnd+"->"+timingEndTime);
						
						efenceLength =- 30;
					}else{
						//无定时时间
						efenceLength -= 18;
					}
					
					if(efenceLength == 0){
						break;
					}
				}
				
		}
		
		return msgBody;
	}
	
	/**
	 * 设备时间参数数据处理
	 * @param msgBody
	 * @return
	 */
	private String deviceTimeParamsHandler(String obdSn, String msgBody) {
		obdHandlerDeviceDataLogger.info("---------"+obdSn+"---设备时间参数数据处理--------------");
		String[] cutStrs;
		OBDTimeParams obdTimeParams= obdTimeParamsService.getObdTimeParamsBySn(obdSn);
		if(obdTimeParams == null){
			obdTimeParams = new OBDTimeParams();
			obdTimeParams.setId(IDUtil.createID());
			obdTimeParams.setObdSn(obdSn);
			obdTimeParams.setCreateTime(new Date());
		}
		
//		OBDTimeParams obdTimeParams = new OBDTimeParams();
//		obdTimeParams.setId(IDUtil.createID());
//		obdTimeParams.setObdSn(obdSn);
		try {
			//设备时间帧格式
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String deviceTimeFrame = cutStrs[0];
			msgBody = cutStrs[1];
			
			char[] bits = StrUtil.hexToBinary(deviceTimeFrame);
			//进入休眠模式时间(s):秒，0xff,为0则关闭休眠功能;60秒0x3C
			if(bits[0] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String entrySleepTime = cutStrs[0];
				Integer sleepTime = Integer.valueOf(entrySleepTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">进入休眠模式时间(s):"+sleepTime);
				obdTimeParams.setSleepTime(sleepTime);
			}
			//熄火后WIFI使用时间(miu):分钟，最多不超过30分钟;1分钟0x01
			if(bits[1] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String downWifiTime = cutStrs[0];
				Integer wifiUseTime = Integer.valueOf(downWifiTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">熄火后WIFI使用时间(分钟):"+wifiUseTime);
				obdTimeParams.setWifiUseTime(wifiUseTime);
			}
			//GPS数据采集时间间隔(s):秒；10秒
			if(bits[2] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String gpsDataCollect = cutStrs[0];
				Integer gpsCollectDataTime = Integer.valueOf(gpsDataCollect,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS数据采集时间间隔(s):"+gpsCollectDataTime);
				obdTimeParams.setGpsCollectDataTime(gpsCollectDataTime);
			}
			//位置数据上传时间间隔(s):秒；30秒0x1E
			if(bits[3] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String positionDataUploadTime = cutStrs[0];
				Integer positionDataTime = Integer.valueOf(positionDataUploadTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">位置数据上传时间间隔(s):"+positionDataTime);
				obdTimeParams.setPositionDataTime(positionDataTime);
			}
			//OBD在线心跳包时间间隔(miu):分钟，最多不超过1小时，保持TCP连接；30分钟0x1E
			if(bits[4] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String obdOnLineHeartbeatTime = cutStrs[0];
				Integer obdOnlineTime = Integer.valueOf(obdOnLineHeartbeatTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">OBD在线心跳包时间间隔(分钟):"+obdOnlineTime);
				obdTimeParams.setObdOnlineTime(obdOnlineTime);
			}
			//OBD离线心跳包时间间隔(10miu):10分钟，按设定时间间隔上传心跳包；3小时0x12
			if(bits[5] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String obdOffLineHeartbeatTime = cutStrs[0];
				Integer obdOfflineTime = Integer.valueOf(obdOffLineHeartbeatTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">OBD离线心跳包时间间隔(分钟):"+obdOfflineTime);
				obdTimeParams.setObdOfflineTime(obdOfflineTime);
			}
			//OBD离线数据保存时间间隔(s):秒，离线时，保存位置信息的间隔，最多条；60秒0x3C
			if(bits[6] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String obdOffLineDataSaveTime = cutStrs[0];
				Integer obdOffDataTime = Integer.valueOf(obdOffLineDataSaveTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">OBD离线数据保存时间间隔(s):"+obdOffDataTime);
				obdTimeParams.setObdOffDataTime(obdOffDataTime);
			}
			//请求AGPS数据包延时时间(10s):秒，过延时时间GPS未定位，请求AGPS数据3分钟
			if(bits[7] == '0'){
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String requestAGPSTime = cutStrs[0];
				Integer _requestAGPSTime = Integer.valueOf(requestAGPSTime,16);
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">请求AGPS数据包延时时间(10s):"+ _requestAGPSTime);
				obdTimeParams.setRequestAGPSTime(_requestAGPSTime);
			}
			//入库
			obdTimeParams.setCreateTime(new Date());
			obdTimeParamsService.add(obdTimeParams);
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(e);
		}
		String key = obdSn + ObdConstants.keySpilt + ObdConstants.DeviceTimeParams;
		GlobalData.putExitsQueryDataToMap(key, obdTimeParams);
		return msgBody;
	}
	
	/**
	 * 设备状态数据处理
	 * @param msgBody
	 * @return
	 * @throws Exception 
	 */
	private String deviceStateHandler(String obdSn,String msgBody) throws Exception {
		ObdHandShake obdHandShake = new ObdHandShake(); 
		obdHandlerDeviceDataLogger.info("<"+obdSn+">设备状态---------------");
		String cutStrs[];
		cutStrs = StrUtil.cutStrByByteNum(msgBody, 3);
		String obdState = cutStrs[0];
		msgBody = cutStrs[1];

		char[] bits;
		obdHandlerDeviceDataLogger.info("<"+obdSn+">>>设备状态数据："+obdState);
		
		bits = StrUtil.hexToBinary(obdState);
		char bit = bits[0];
		switch (bit) {
		case '0':
			// 正常连接握手帧
			obdHandShake.setWakeUp(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">正常连接握手帧...");
			break;
		case '1':
			// 离线心跳包
			obdHandShake.setWakeUp(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">离线心跳包...");
			break;
		default:
			break;
		}
		
		if(bits[1] == '0'){//GPS模块	0：正常；1：异常
			obdHandShake.setGpsModule(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS模块	0正常 1异常:"+bits[1]);
		}else{
			obdHandShake.setGpsModule(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS模块	0正常 1异常:"+bits[1]);
		}
		if(bits[2] == '0'){//EEPROM	0：正常；1：异常
			obdHandShake.setEfprom(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">EEPROM	0正常 1异常:"+bits[2]);
		}else{
			obdHandShake.setEfprom(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">EEPROM	0正常 1异常:"+bits[2]);
		}
		if(bits[3] == '0'){//3D加速度传感器	0：正常；1：异常
			obdHandShake.setAccelerator3D(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">3D加速度传感器	0正常 1异常:"+bits[3]);
		}else{
			obdHandShake.setAccelerator3D(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">3D加速度传感器0正常 1异常:"+bits[3]);
		}
		if(bits[4] == '1'){//有离线数据
			obdHandShake.setHasOffData(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">----有离线数据！----");
		}else{
			obdHandShake.setHasOffData(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">----无离线数据！----");
		}
		if(bits[5] == '1'){//WIFI 设置 1关闭 0打开
			obdHandShake.setWifiSet(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">WIFI设置 1关闭 0打开:"+bits[5]);
		}else{
			obdHandShake.setWifiSet(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">WIFI设置 1关闭 0打开:"+bits[5]);
		}
		if(bits[6] == '1'){//GPS 设置 1关闭 0打开
			obdHandShake.setGpsSet(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS设置 1关闭 0打开:"+bits[6]);
		}else{
			obdHandShake.setGpsSet(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS设置 1关闭 0打开:"+bits[6]);
		}
		if(bits[7] == '1'){//GPS数据格式 0：只传定位数据 1：全部
			obdHandShake.setGpsDataFormat(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS数据格式 0只传定位数据 1全部:"+bits[7]);
		}else{
			obdHandShake.setGpsDataFormat(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">GPS数据格式 0只传定位数据 1全部:"+bits[7]);
		}
		if(bits[8] == '1'){//离线心跳设置 0无 1设置
			obdHandShake.setOffHeartSet(1);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">离线心跳设置 0无 1设置:"+bits[8]);
		}else{
			obdHandShake.setOffHeartSet(0);
			obdHandlerDeviceDataLogger.info("<"+obdSn+">离线心跳设置 0无 1设置:"+bits[8]);
		}
		
		//注册网络
		String regNet = ""+bits[12]+bits[11]+bits[10]+bits[9]; 
		switch (regNet) {
		case "0000":
			regNet += "->电信4G";
			break;
		case "0001":
			regNet += "->电信3G EVDO";
			break;
		case "0010":
			regNet += "->电信2G CDMA2000/1X";
			break;
		case "0011":
			regNet += "->移动4G";
			break;
		case "0100":
			regNet += "->移动3G";
			break;
		case "0101":
			regNet += "->移动2G(GSM)";
			break;
		case "0111":
			regNet += "->联通4G";
			break;
		case "1000":
			regNet += "->联通3G";
			break;
		case "1001":
			regNet += "->联通2G(GSM)";
			break;
		case "1010":
			regNet += "->电信2G/3G";
			break;
		default:
			break;
		}
		obdHandShake.setRegNet(regNet);
		
		// 网络信号强度
		if ('0' == bits[13]) {
		}
		
		// VIN码
		if ('0' == bits[16]) {
		}
		
		// 前次休眠原因
		if ('0' == bits[17]) {
		}

		// 蓄电池电压
		if ('0' == bits[18]) {
		}
		
		//流量统计值
		if ('1' == bits[19]) {
		}
		String key = obdSn+ ObdConstants.keySpilt + ObdConstants.DeviceState;
		GlobalData.putExitsQueryDataToMap(key, obdHandShake);
		return msgBody;
	}
	
	/**
	 * 车辆运行状态数据处理
	 * @param msgBody
	 * @return
	 */
	private String carRunStateHandler(String obdSn,String msgBody) {
		obdHandlerDeviceDataLogger.info("-------------"+obdSn+"---车辆运行状态数据处理-----------------");
		String[] cutStrs;
		try {
			/*
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 42);
			String carState = cutStrs[0];
			msgBody = cutStrs[1];
			*/
			//进气歧管绝对压力	千帕，（8位无符号）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String manifoldPressure = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">进气歧管绝对压力	千帕:"+manifoldPressure);
			
			//发动机转速	转每分,（16位无符号）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String engineRotateSpeed = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机转速	转每分:"+engineRotateSpeed);

			//1号气缸点火时提前角	0.1度,（16位有符号整形）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String oneFireAngle = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">1号气缸点火时提前角	0.1度:"+oneFireAngle);
			
			//进气温度	摄氏度,（8位有符号）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String enterTemperature = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">进气温度	摄氏度,（8位有符号）:"+enterTemperature);
			
			//空气流量传感器的空气流量	0.01加仑/秒,（16位无符号整形）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String airFlow = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">空气流量传感器的空气流量	0.01加仑/秒,（16位无符号整形）:"+airFlow);
			
			//绝对节气门位置	%,（8位有符号）
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String absoulteLoc = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">绝对节气门位置	%,（8位有符号）:"+absoulteLoc);
			
			//二次空气状态指令
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String secondAirStateCmd = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">二次空气状态指令:"+secondAirStateCmd);
			
			//氧传感器的位置	
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String oxygenLoc = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">氧传感器的位置	:"+oxygenLoc);
			
			//自发动机起动的时间	秒
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String formEngineTime = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">自发动机起动的时间	秒	:"+formEngineTime);
			
			//在故障指示灯激活状态下行驶的里程	千米
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String runMileInFault = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">在故障指示灯激活状态下行驶的里程	千米:"+runMileInFault);
			
			//相对于歧管真空度的油轨压力	千帕
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String oilPressureForManifold = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">相对于歧管真空度的油轨压力	千帕:"+oilPressureForManifold);

			//相对于大气压力的油轨压力	千帕
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String oilPressureForAtmosphere = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">相对于大气压力的油轨压力	千帕:"+oilPressureForAtmosphere);
			
			//废气再循环系统指令开度	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String wasteGasRecycleCmd = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">废气再循环系统指令开度	%:"+wasteGasRecycleCmd);
			
			//EGR开度误差  	%(实际开度-指令开度)/指令开度*100 
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String egrOpenError = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">EGR开度误差  	%(实际开度-指令开度)/指令开度*100 :"+egrOpenError);
			
			//蒸发冲洗控制指令	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String flushControlCmd = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">蒸发冲洗控制指令	% :"+flushControlCmd);
			
			//燃油液位输入	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String fuelLevelInput = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">燃油液位输入	% :"+fuelLevelInput);
			
			//蒸发系统的蒸气压力	Pa
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String steamPressure = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">蒸发系统的蒸气压力	Pa:"+steamPressure);
			
			//大气压	Kpa
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String Kpa = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">大气压	Kpa :"+Kpa);

			//相对节气门位置	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String relativeThrottleLoc = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">相对节气门位置	%:"+relativeThrottleLoc);
			
			//环境空气温度	℃
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String envAirTemperature = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">环境空气温度	℃:"+envAirTemperature);
			
			//绝对节气门位置B 	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String absoulteLocB = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">绝对节气门位置B 	%:"+absoulteLocB);
			
			//绝对节气门位置C 	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String absoulteLocC = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">绝对节气门位置C 	%:"+absoulteLocC);
			
			//加速踏板位置D  	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String speedUpPedalLocD = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">加速踏板位置D  	%:"+speedUpPedalLocD);
			
			//加速踏板位置E  	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String speedUpPedalLocE = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">加速踏板位置E  	%:"+speedUpPedalLocE);
			
			//加速踏板位置F  	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String speedUpPedalLocF = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">加速踏板位置F  	%:"+speedUpPedalLocF);
			
			//节气门执行器控制指令  	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String executeControlCmd = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">节气门执行器控制指令  	%:"+executeControlCmd);
			
			//故障指示灯处于激活状态下的发动机运转时间  	分钟
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String timeInFalutLinght = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">故障指示灯处于激活状态下的发动机运转时间  	分钟:"+timeInFalutLinght);
			
			//油轨绝对压力 	Kpa
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String oilKpa = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">油轨绝对压力 	Kpa:"+oilKpa);
			
			//加速踏板相对位置  	%
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String speedUpPedalAbsoulteLoc = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">加速踏板相对位置  	%:"+speedUpPedalAbsoulteLoc);
			
			//发动机机油温度 	摄氏度
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String engineOilTemperature = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机机油温度 	摄氏度:"+engineOilTemperature);
			
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(e);
		}
		return msgBody;
	}
	
	/**
	 * 报警设置数据处理
	 * @param msgBody
	 * @return
	 */
	private String warnSetHandler(String obdSn,String msgBody) {
		obdHandlerDeviceDataLogger.info("-------------"+obdSn+"---报警设置数据处理---------------");
		ObdHandShake obdHandShake = new ObdHandShake();
		String[] cutStrs;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String warnSet = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">>>报警设置数据："+warnSet);

			char[] bits  = StrUtil.hexToBinary(warnSet);
			//非法启动探测
			if('0' == bits[0]){
				//开启
				obdHandShake.setIllegalStartSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">非法启动探测 开启:"+0);
			}else{
				//关闭
				obdHandShake.setIllegalStartSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">非法启动探测 关闭:"+bits[0]);
			}
			//非法震动探测
			if('0' == bits[1]){
				//开启
				obdHandShake.setIllegalShockSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">非法震动探测 开启:"+0);
			}else{
				//关闭
				obdHandShake.setIllegalShockSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">非法震动探测 关闭:"+bits[1]);
			}
			//蓄电电压异常报警
			if('0' == bits[2]){
				//开启
				obdHandShake.setVoltUnusualSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">蓄电电压异常报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setVoltUnusualSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">蓄电电压异常报警 关闭:"+bits[2]);
			}
			//发动机水温高报警
			if('0' == bits[3]){
				//开启
				obdHandShake.setEngineWaterWarnSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机水温高报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setEngineWaterWarnSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机水温高报警 关闭:"+ bits[3]);
			}
			//车辆故障报警
			if('0' == bits[4]){
				//开启
				obdHandShake.setCarWarnSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">车辆故障报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setCarWarnSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">车辆故障报警 关闭:"+bits[4]);
			}
			//超速报警
			if('0' == bits[5]){
				//开启
				obdHandShake.setOverSpeedWarnSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">超速报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setOverSpeedWarnSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">超速报警 关闭:"+bits[5]);
			}
			//电子围栏报警
			if('0' == bits[6]){
				//开启
				obdHandShake.setEfenceWarnSet(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">电子围栏报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setEfenceWarnSet(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">电子围栏报警 关闭:"+bits[6]);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(e);
		}
		String key = obdSn+ ObdConstants.keySpilt + ObdConstants.WarnSet;
		GlobalData.putExitsQueryDataToMap(key, obdHandShake);
		return msgBody;
	}
	
	/**
	 * 车辆状态数据处理
	 * @param msgBody
	 * @return
	 */
	private String carStateHandler(String obdSn,String msgBody) {
		ObdHandShake obdHandShake = new ObdHandShake(); 
		obdHandlerDeviceDataLogger.info("-----------"+obdSn+"---车辆状态数据处理--------------");
		String[] cutStrs;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String carState = cutStrs[0];
			msgBody = cutStrs[1];
			
			obdHandlerDeviceDataLogger.info("<"+obdSn+">>>车辆状态数据:"+carState);
			
			char[] bits = StrUtil.hexToBinary(carState);
			//0-3	ECU通讯协议	
			String ecuPrtocol = ""+bits[3]+bits[2]+bits[1]+bits[0];
			String ecuDes = "";
			switch (ecuPrtocol) {
			case "0000"://ECU通讯异常
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ECU通讯异常:"+ecuPrtocol);
				ecuDes = "ECU通讯异常";
				break;
			case "0001"://ISO15765_STD_500K
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ISO15765_STD_500K:"+ecuPrtocol);
				ecuDes = "ISO15765_STD_500K";
				break;
			case "0010"://ISO15765_STD_250K
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ISO15765_STD_250K:"+ecuPrtocol);
				ecuDes = "ISO15765_STD_250K";
				break;
			case "0011"://ISO15765_EXT_500K
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ISO15765_EXT_500K:"+ecuPrtocol);
				ecuDes = "ISO15765_EXT_500K";
				break;
			case "0100"://ISO15765_EXT_250K
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ISO15765_EXT_250K:"+ecuPrtocol);
				ecuDes = "ISO15765_EXT_250K";
				break;
			case "0101"://ISO9141
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ISO9141:"+ecuPrtocol);
				ecuDes = "ISO9141";
				break;
			case "0110"://ISO14230
				obdHandlerDeviceDataLogger.info("<"+obdSn+">ISO14230:"+ecuPrtocol);
				ecuDes = "ISO14230";
				break;
			//其他：保留
			default:
				obdHandlerDeviceDataLogger.info("<"+obdSn+">其他:"+ecuPrtocol);
				ecuDes = "其他";
				break;
			}
			obdHandShake.setEcu(ecuPrtocol+"->"+ecuDes);
			
			//车辆故障码	0：无；1：有
			if('0' == bits[4]){
				obdHandShake.setCarFaultCode(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">车辆故障码	0：无");
			}else{
				obdHandShake.setCarFaultCode(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">车辆故障码	1：有");
			}
			//蓄电池电压	0：正常；1：异常
			if('0' == bits[5]){
				obdHandShake.setVoltStatus(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">蓄电池电压	0：正常");
			}else{
				obdHandShake.setVoltStatus(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">蓄电池电压	1：异常");
			}
			//发动机水温	0：正常；1：异常
			if('0' == bits[6]){
				obdHandShake.setEngineWater(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机水温	0：正常");
			}else{
				obdHandShake.setEngineWater(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">发动机水温	1：异常");
			}
			//启动方式	0：点火启动；1：震动启动
			if('0' == bits[7]){
				obdHandShake.setStartMode(0);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">启动方式	0：点火启动");
			}else{
				obdHandShake.setStartMode(1);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">启动方式	1：震动启动");
			}
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(obdSn, e);
		}
		String key = obdSn+ ObdConstants.keySpilt + ObdConstants.CarState;
		GlobalData.putExitsQueryDataToMap(key, obdHandShake);
		return msgBody;
	}

	/**
	 * 离线位置数据
	 * @param msgBody
	 * @param isLocation true位置，false行程单
	 */
	private String offLineLocationData(String msgBody,String obdSn,String waterNo,boolean isLocation) {
		obdHandlerDeviceDataLogger.info("---------"+obdSn+"---"+(isLocation ? "离线位置数据":"离线行程单数据")+"处理----------------");
		String[] cutStrs;
		try {
			//包序号,序号从0开始
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String packetNoStr = cutStrs[0];
			Integer packetNo = Integer.valueOf(packetNoStr,16);
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">包序号:"+packetNo);
			
			//离线位置数据、离线行程单采用此格式上传。注意：数据包长度<=800
			//当前包长度
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String packetLengthStr = cutStrs[0];
			Integer packetLength = Integer.valueOf(packetLengthStr,16);//<=800 Byte
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">当前包长度："+packetLength);
			if(packetLength > 800){
				obdHandlerDeviceDataLogger.error("<"+obdSn+">离线数据包长度超过800Byte！");
				throw new Exception("<"+obdSn+">离线数据包长度超过800Byte！");
			}
			if(packetLength == 0){
				obdHandlerDeviceDataLogger.info("<"+obdSn+">当前包长度为0，不处理！");
				return null;
			}
			//包数据N
			cutStrs = StrUtil.cutStrByByteNum(msgBody, packetLength);
			String packetData = cutStrs[0];//<=800 Byte
			msgBody = cutStrs[1];
			obdHandlerDeviceDataLogger.info("<"+obdSn+">包数据N:"+packetData);
			
			Integer _dataTotalLength = null;
			if(packetNo == 0){
				//数据总长度:仅当包序号为0时有此项
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String dataTotalLength = cutStrs[0];//
				msgBody = cutStrs[1];
				_dataTotalLength = Integer.valueOf(dataTotalLength,16);
				obdHandlerDeviceDataLogger.info("<"+obdSn+">数据总长度："+dataTotalLength+"->"+ _dataTotalLength);

				//分包数量
				Integer num = _dataTotalLength/800;
				if(_dataTotalLength % 800 != 0){
					 num = num + 1 ;
				}
				
				//异或检验和:仅当包序号为0时有此项 
				// TODO 需要再次校验一下
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String xorSum = cutStrs[0];//<=800 Byte
				msgBody = cutStrs[1];
				obdHandlerDeviceDataLogger.info("<"+obdSn+">异或检验和："+xorSum);
				

				if(isLocation){
					jedisServiceUtil.ttl(ObdRedisData.OffLineData_TTL, obdSn, num*3);//根据包数量设置请求离线超时时间 num*3秒
					GlobalData.OBD_PacketNum.put(obdSn+"_offData_packetNum", num);//包数量
					GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_offData_xorSum", xorSum);//离线数据校验和 
				}else{
					GlobalData.OBD_PacketNum.put(obdSn+"_offTravel_packetNum", num);//包数量
					GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_offTravel_xorSum", xorSum);//离线数据校验和 
				}
				
//				ServerResponse sr = new ServerResponse();
//				String xor = sr.xor(packetNoStr+packetLengthStr+packetData+dataTotalLength);
//				obdHandlerDeviceDataLogger.info("本地检验和："+xor);
				
//				if(!xorSum.equals(xor)){
//					obdHandlerDeviceDataLogger.error("检验码错误：本地检验和："+xor+"，原校验和："+xorSum);
//					//TODO 再次请求
//					obdHandlerDeviceDataLogger.info("再次请求数据，包序号："+packetNo);
//					if(isLocation){
//						serverRequestQueryService.offData(obdSn, packetNo);
//					}else{
//						serverRequestQueryService.offTravel(obdSn, packetNo);
//					}
//				}
			}
			
			if(isLocation){
				packetOffDataHandler(obdSn, waterNo, packetNo,packetData);
				//离线位置信息数据包 
			}else{
				packetOffTravelHandler(obdSn,waterNo, packetNo,packetData);
				//离线行程包43个字节为单位
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerDeviceDataLogger.error(obdSn, e);
		}
		return msgBody;
	}
	
	public void packetOffDataHandler(String obdSn, String waterNo, Integer packetNo,String packetData) throws Exception{
		obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】---------");
		/**
		 * 1、分包数量num 3
		 * 2、已经发的包序号->第n个包 00 01 ->1
		 * 3、获取下一个包序号  n+1
		 * 4、是否最后一个
		 * 5、
		 */
		//包数量
		Integer packetNum = GlobalData.OBD_PacketNum.get(obdSn+"_offData_packetNum");
		obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】包数量:"+packetNum);
		if(packetNum == 1){
			obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】单包！进行解析！");
			//单包
			// 包请求完毕，发送清除指令
			String msgBody = ObdConstants.Server_RequestOffData_OBD_Cmd + "8000";
			String result = (String) sendUtil.msgSendGetResult(obdSn, SerialNumberUtil.getSerialnumber(obdSn), msgBody,null);
			obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】单包请求完毕，发送清除指令，结果："+result);
			GlobalData.OBD_PacketNum.remove(obdSn+"_offData_packetNum");
			
			//进行解析
			OBDMessage message = new OBDMessage();
			message.setId(obdSn);
			message.setWaterNo(waterNo);
			message.setMessage(packetData);
			message.setMsgBody(packetData);
			// 离线位置信息数据包
			messageObdPositionHandler.entry(message);
			
		}else if(packetNum > 1){
			obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】多包!将分包进行合并！");
			// 多包
			//将分包进行合并
			if(packetNo == 0){//首包，清空有可能异常的旧数据
				GlobalData.OBD_Packet.remove(obdSn+ObdConstants.OFFData);
			}
			GlobalData.putPacketToMap(obdSn+ObdConstants.OFFData, packetNo, packetData);
			Integer nextPacketNo = packetNo + 1;
			if (nextPacketNo == packetNum) {// 下一包序号等于包数量，说明请求完毕
				//完整包组装
				 Map<Integer,String> packetDataMap = GlobalData.OBD_Packet.get(obdSn+ObdConstants.OFFData);
				 String packetDataAll = "";
				 for (int i = 0; i < packetNum; i++) {
					 String pData = packetDataMap.get(i);
					 if(StringUtils.isEmpty(pData)){
						 obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线位置数据包】完整包组装失败："+packetDataMap);
						 throw new OBDException("完整包组装失败！");
					 }else{
						 packetDataAll += pData;
					 }
				}

				/*校验是否通过*/
				boolean isXorSumOk = true;
				String xorSum = null;
				String sum = null;
				try {
					xorSum = (String) GlobalData.OBD_ACK_OR_QueryData.get(obdSn+"_offData_xorSum");//离线数据校验和 
					ServerResponse sr = new ServerResponse();
					GlobalData.OBD_ACK_OR_QueryData.remove(obdSn+"_offData_xorSum");
					sum = sr.xor(packetDataAll);
					if(! sum.equals(xorSum)){
						isXorSumOk = false;
						obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线位置数据包】校验码不通过！原始校验码："+xorSum+",本地校验码："+ sum);
					}
				} catch (Exception e) {
					e.printStackTrace();
					isXorSumOk = false;
					obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线位置数据包】校验码校验，异常："+e);
				}
				if(!isXorSumOk){
					obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】校验码不通过！重新请求离线数据。。。");
					serverRequestQueryService.offData(obdSn, 0, false);
					return;
				}
				
				// 包请求完毕，发送清除指令
				String msgBody = ObdConstants.Server_RequestOffData_OBD_Cmd + "8000";
				String result = (String) sendUtil.msgSendGetResult(obdSn, SerialNumberUtil.getSerialnumber(obdSn), msgBody,null);
				obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线位置数据包】多包请求完毕，发送清除指令，结果："+result);
				GlobalData.OBD_PacketNum.remove(obdSn+"_offData_packetNum");
				GlobalData.OBD_Packet.remove(obdSn+ObdConstants.OFFData); //TODO
				
				//进行解析
				OBDMessage message = new OBDMessage();
				message.setId(obdSn);
				message.setWaterNo(waterNo);
				message.setMessage(packetDataAll);
				message.setMsgBody(packetDataAll);
				// 离线位置信息数据包
				messageObdPositionHandler.entry(message);
				
			} else {
				obdHandlerDeviceDataLogger.info(" ---------【处理离线位置数据包】再次请求下一包！");
				// 再次请求下一包
				//判断上一包是否有数据了，有则下一包，没有则继续上一包
				if(packetNo != 0){
					String preData = GlobalData.OBD_Packet.get(obdSn+ObdConstants.OFFData).get(packetNo);
					if(StringUtils.isEmpty(preData)){
						obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线位置数据包】没有上一包，继续获取上一数据包："+packetNo);
						nextPacketNo = packetNo;
					}
				}
				
				serverRequestQueryService.offData(obdSn, nextPacketNo, false);
			}
			
		}else{
			obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线位置数据包】包数量异常："+packetNum);
			throw new OBDException("包数量异常："+packetNum);
		}
	}
	
	public void packetOffTravelHandler(String obdSn,String waterNo,Integer packetNo,String packetData) throws Exception{
		obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】---------");
		/**
		 * 1、分包数量num 3
		 * 2、已经发的包序号->第n个包 00 01 ->1
		 * 3、获取下一个包序号  n+1
		 * 4、是否最后一个
		 * 5、
		 */
		//包数量
		Integer packetNum = GlobalData.OBD_PacketNum.get(obdSn+"_offTravel_packetNum");
		obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】包数量："+packetNum);
		if(packetNum == 1){
			//单包
			obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】单包！");
			// 包请求完毕，发送清除指令
			String msgBody = ObdConstants.Server_RequestOffTravel_OBD_Cmd + "8000";
			String result = (String) sendUtil.msgSendGetResult(obdSn, SerialNumberUtil.getSerialnumber(obdSn), msgBody,null);
			obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】包请求完毕，发送清除指令，结果："+result);
			GlobalData.OBD_PacketNum.remove(obdSn+"_offTravel_packetNum");
			
			//进行解析
			OBDMessage message = new OBDMessage();
			message.setId(obdSn);
			message.setWaterNo(waterNo);
			message.setMessage(packetData);
			message.setMsgBody(packetData);
			// 离线行程信息数据包
			messageObdTravelHandler.entry(message);
			
		}else if(packetNum > 1){
			obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】多包！将分包进行合并！！！");
			// 多包
			//将分包进行合并
			if(packetNo == 0){//首包，清空有可能异常的旧数据
				GlobalData.OBD_Packet.remove(obdSn+ObdConstants.OFFTravel);
			}
			GlobalData.putPacketToMap(obdSn+ObdConstants.OFFTravel, packetNo, packetData);
			Integer nextPacketNo = packetNo + 1;
			if (nextPacketNo == packetNum) {// 下一包序号等于包数量，说明请求完毕
				//完整包组装
				 Map<Integer,String> packetDataMap = GlobalData.OBD_Packet.get(obdSn+ObdConstants.OFFTravel);
				 String packetDataAll = "";
				 for (int i = 0; i < packetNum; i++) {
					 String pData = packetDataMap.get(i);
					 if(StringUtils.isEmpty(pData)){
						 obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线行程数据包】完整包组装失败："+packetDataMap);
						 throw new OBDException("完整包组装失败！");
					 }else{
						 packetDataAll += pData;
					 }
				}
				
				/*校验是否通过*/
				boolean isXorSumOk = true;
				String xorSum = null;
				String sum = null;
				try {
					xorSum = (String) GlobalData.OBD_ACK_OR_QueryData.get(obdSn+"_offTravel_xorSum");//离线数据校验和 
					ServerResponse sr = new ServerResponse();
					GlobalData.OBD_ACK_OR_QueryData.remove(obdSn+"_offTravel_xorSum");
					sum = sr.xor(packetDataAll);
					if(! sum.equals(xorSum)){
						isXorSumOk = false;
						obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线行程数据包】校验码不通过！原始校验码："+xorSum+",本地校验码："+ sum);
					}
				} catch (Exception e) {
					e.printStackTrace();
					isXorSumOk = false;
					obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线行程数据包】校验码校验，异常："+e);
				}
				if(!isXorSumOk){
					obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】校验码不通过！重新请求离线数据。。。");
					serverRequestQueryService.offTravel(obdSn, 0, false);
					return;
				}
				
				// 包请求完毕，发送清除指令
				String msgBody = ObdConstants.Server_RequestOffTravel_OBD_Cmd + "8000";
				String result = (String) sendUtil.msgSendGetResult(obdSn, SerialNumberUtil.getSerialnumber(obdSn), msgBody,null);
				obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】包请求完毕，发送清除指令，结果："+result);
				GlobalData.OBD_PacketNum.remove(obdSn+"_offTravel_packetNum");
				GlobalData.OBD_Packet.remove(obdSn+ObdConstants.OFFTravel); //TODO
				
				//进行解析
				OBDMessage message = new OBDMessage();
				message.setId(obdSn);
				message.setWaterNo(waterNo);
				message.setMessage(packetDataAll);
				message.setMsgBody(packetDataAll);
				// 离线行程信息数据包
				messageObdTravelHandler.entry(message);
				
			} else {
				// 再次请求下一包
				//判断上一包是否有数据了，有则下一包，没有则继续上一包
				String preData = GlobalData.OBD_Packet.get(obdSn+ObdConstants.OFFTravel).get(packetNo);
				if(StringUtils.isEmpty(preData)){
					obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线行程数据包】没有上一包，继续获取上一数据包："+packetNo);
					nextPacketNo = packetNo;
				}
				obdHandlerDeviceDataLogger.info("------"+obdSn+"---【处理离线行程数据包】再次请求下一包："+nextPacketNo);
				serverRequestQueryService.offTravel(obdSn, nextPacketNo, false);
			}
			
		}else{
			obdHandlerDeviceDataLogger.error("------"+obdSn+"---【处理离线行程数据包】包数量异常:"+packetNum);
			throw new OBDException("包数量异常："+packetNum);
		}
	}
}
