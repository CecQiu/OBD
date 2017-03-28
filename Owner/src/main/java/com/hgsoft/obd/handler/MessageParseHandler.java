package com.hgsoft.obd.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hgsoft.carowner.entity.ObdSimCard;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.carowner.service.CarDriveInfoService;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.CarOilInfoService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.ObdHandShakeService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.ObdSimCardService;
import com.hgsoft.carowner.service.SimStockInfoService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.common.utils.ThreadLocalDateUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.PushUtil;
import com.hgsoft.obd.util.TravelDataUtil;
import com.hgsoft.obd.util.WarnType;

/**
 * 报文消息解析处理类
 * @author sujunguang
 * 2015年12月14日
 * 下午3:12:45
 */
@Service
public class MessageParseHandler {
	
	private static Logger obdHandlerDeviceInitLogger = LogManager.getLogger("obdHandlerDeviceInitLogger");
	
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ObdHandShakeService obdHandShakeService;
	@Resource
	private SimStockInfoService simStockInfoService;
	@Resource
	private PushUtil pushUtil;
	@Resource
	private DriveTimeUtil driveTimeUtil;
	@Resource
	private PushApi pushApi;
	@Resource
	private CarInfoService carInfoService;
	@Resource
	private ObdSateService obdSateService;
	@Resource
	private TravelDataUtil travelDataUtil;
	@Resource
	private CarTraveltrackService carTraveltrackService;
	@Resource
	private CarOilInfoService carOilInfoService;
	@Resource
	private CarDriveInfoService carDriveInfoService;
	@Resource
	private ObdSimCardService obdSimCardService;
	@Resource
	private JedisServiceUtil jedisServiceUtil;
	
	private Map<String,Integer> map = new HashMap<String,Integer>();
	/**
	 * 设备状态数据处理
	 * @param message 
	 * @param msgBody
	 * @return
	 */
	public String deviceStateHandler(OBDMessage message, String msgBody,ObdHandShake obdHandShake) throws Exception{
	
		final String obdSn = message.getId();
		String waterNo = message.getWaterNo();
		String cutStrs[];
		cutStrs = StrUtil.cutStrByByteNum(msgBody, 3);
		String obdState = cutStrs[0];
		msgBody = cutStrs[1];

		char[] bits;
		obdHandlerDeviceInitLogger.info("<"+obdSn+">>>设备状态数据："+obdState);
		//保存服务器请求查询 设备状态
		Integer _waterNo = Integer.valueOf(message.getWaterNo(),16);
		GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_"+_waterNo, obdState);
		
		bits = StrUtil.hexToBinary(obdState);
		char bit0 = bits[0];
		switch (bit0) {
		case '0':
			// 正常连接握手帧
			// TODO
			obdHandShake.setWakeUp(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">正常连接握手帧...");
			obdHandlerDeviceInitLogger.info("<"+obdSn+">其他唤醒...");
			break;
		case '1':
			// 离线心跳包
			// TODO
			obdHandShake.setWakeUp(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">离线心跳包...");
			obdHandlerDeviceInitLogger.info("<"+obdSn+">定时唤醒...");
			break;
		default:
			break;
		}
		
		if(bits[1] == '0'){//GPS模块	0：正常；1：异常
			obdHandShake.setGpsModule(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">GPS模块	0正常 1异常:"+bits[1]);
		}else{
			obdHandShake.setGpsModule(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">GPS模块	0正常 1异常:"+bits[1]);
		}
		if(bits[2] == '0'){//EEPROM	0：正常；1：异常
			obdHandShake.setEfprom(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">EEPROM	0正常 1异常:"+bits[2]);
		}else{
			obdHandShake.setEfprom(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">EEPROM	0正常 1异常:"+bits[2]);
		}
		if(bits[3] == '0'){//3D加速度传感器	0：正常；1：异常
			obdHandShake.setAccelerator3D(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">3D加速度传感器	0正常 1异常:"+bits[3]);
		}else{
			obdHandShake.setAccelerator3D(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">3D加速度传感器0正常 1异常:"+bits[3]);
		}
		if(bits[4] == '1'){//有离线数据
			obdHandShake.setHasOffData(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">----有离线数据！----");
			new Thread() {
				@Override
				public void run() {
					//记录下当前请求离线数据
					boolean reqOffData = jedisServiceUtil.validTTL(ObdRedisData.OffLineData_TTL, obdSn);
					if(!reqOffData){
						obdHandlerDeviceInitLogger.info("<"+obdSn+">----有离线数据！开始请求离线(位置+行程)数据");
						serverRequestQueryService.offTravel(obdSn, 0, false);//先请求行程，数据量较少
						serverRequestQueryService.offData(obdSn, 0, false);
					}else{
						//不能请求离线数据
						obdHandlerDeviceInitLogger.info("<"+obdSn+">----有离线数据！--不能请求,ttl:"
						 + jedisServiceUtil.getTTL(ObdRedisData.OffLineData_TTL, obdSn));
					}
				}
			}.start();
			
		}else{
			obdHandShake.setHasOffData(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">----无离线数据！----");
		}
		if(bits[5] == '1'){//WIFI 设置 1关闭 0打开
			obdHandShake.setWifiSet(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">WIFI设置 1关闭 0打开:"+bits[5]);
		}else{
			obdHandShake.setWifiSet(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">WIFI设置 1关闭 0打开:"+bits[5]);
		}
		if(bits[6] == '1'){//GPS 设置 1关闭 0打开
			obdHandShake.setGpsSet(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">GPS设置 1关闭 0打开:"+bits[6]);
		}else{
			obdHandShake.setGpsSet(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">GPS设置 1关闭 0打开:"+bits[6]);
		}
		if(bits[7] == '1'){//GPS数据格式 0：只传定位数据 1：全部
			obdHandShake.setGpsDataFormat(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">GPS数据格式 0只传定位数据 1全部:"+bits[7]);
		}else{
			obdHandShake.setGpsDataFormat(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">GPS数据格式 0只传定位数据 1全部:"+bits[7]);
		}
		if(bits[8] == '1'){//离线心跳设置 0无 1设置
			obdHandShake.setOffHeartSet(1);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">离线心跳设置 0无 1设置:"+bits[8]);
		}else{
			obdHandShake.setOffHeartSet(0);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">离线心跳设置 0无 1设置:"+bits[8]);
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
		case "1111":
			regNet += "->其它";
			break;
		default:
			break;
		}
		obdHandShake.setRegNet(regNet);
		
		// 网络信号强度
		if ('0' == bits[13]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String netSinal = cutStrs[0];
			msgBody = cutStrs[1];
			int _netSinal = Integer.valueOf(netSinal, 16);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">网络信号强度："+_netSinal);
			obdHandShake.setNetSinal(_netSinal);
			// TODO
		}
		// 车辆状态:BIT0=1时，BIT14=1，不上传车辆状态
		
		if ('0' == bits[14]) {
			//车辆状态数据处理
			msgBody = carStateHandler(message,msgBody,obdHandShake);
		}
		
		// 报警设置:BIT0=1时，BIT15=1，不上传报警设置
		if ('0' == bits[15]) {
			//报警设置数据处理
			msgBody = warnSetHandler(message,msgBody,obdHandShake);
		}
		
		// VIN码
		if ('0' == bits[16]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 17);
			String vinCode = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceInitLogger.info("<"+obdSn+">VIN码："+vinCode);
			String carState = cutStrs[0];
			char[] carStateBits = StrUtil.hexToBinary(carState);
			if('0' == carStateBits[7]){//点火+VIN->确定车辆首次点火启动  已去掉！
				
			}
			obdHandShake.setVinCode(vinCode);
		}
		
		boolean hasLastSleep = false;//是否有前次休眠原因
		String rest = null;
		// 前次休眠原因
		if ('0' == bits[17]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			rest = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceInitLogger.info("<"+obdSn+">前次休眠原因："+rest);
			obdHandShake.setLastSleep(rest);
			//清空报警记录
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录-----");
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：OverSpeed"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.OverSpeed));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：IllegalStartUp"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.IllegalStartUp));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：IllegalShock"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.IllegalShock));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_In"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.Efence_In));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_Out"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.Efence_Out));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_InOut_In"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.Efence_InOut_In));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_InOut_Out"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.Efence_InOut_Out));
//			obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：FatigueDrive"+PushUtil.PushApiTime.get(obdSn+"_"+WarnType.FatigueDrive));
//			PushUtil.clearPush(obdSn);
//			
			hasLastSleep = true;
			GlobalData.ObdLastSleep.add(obdSn);//记录前次休眠原因
		}

		// 蓄电池电压
		if ('0' == bits[18]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String volt = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _volt = Integer.valueOf(volt, 16);
			if ('1' == bits[20]) {//24V设备
				obdHandlerDeviceInitLogger.info("<"+obdSn+">24V,蓄电池电压0.1V："+volt+"->"+_volt*2.0/10);
				obdHandShake.setVolt(_volt*2.0/10);
				obdHandShake.setVoltType(1);
			}else{
				obdHandlerDeviceInitLogger.info("<"+obdSn+">12V,蓄电池电压0.1V："+volt+"->"+_volt*1.0/10);
				obdHandShake.setVolt(_volt*1.0/10);
				obdHandShake.setVoltType(0);
			}
		}
		
		//流量统计值
		if ('1' == bits[19]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
			String flowCounter = cutStrs[0];
			msgBody = cutStrs[1];
			Long _flowCounter = Long.valueOf(flowCounter, 16);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">流量统计值 KB："+flowCounter+"->"+_flowCounter);
			obdHandShake.setFlowCounter(_flowCounter);

			SimStockInfo simStockInfo = simStockInfoService.queryLastByObdSn(obdSn);//流量
			
			if(simStockInfo != null /*&& bits[17] == '0' */&& !"ffffffff".equals(flowCounter)){//前次休眠原因+流量数据不能为ffffffff
				//同步
				Long flowUse = simStockInfo.getFlowUse();
				Long tempFlowUse = simStockInfo.getTempFlowUse();
				Long totalFlowUse = flowUse + tempFlowUse;
				Long flow = _flowCounter;
				if(totalFlowUse-flow == 0){
					obdHandlerDeviceInitLogger.info("<"+obdSn+">【同步】后台=设备：同步下发设备->flowUse:"+flowUse
							+",tempFlowUse:"+tempFlowUse+"="+totalFlowUse
							+"=flow:"+flow);
					if(simStockInfo.getValid() == 0){//清零
						Long cleanAfterFlow = totalFlowUse -simStockInfo.getCleanFlowUse();
						String result = serverSettingService.deviceFlowSync(obdSn, cleanAfterFlow);
						obdHandlerDeviceInitLogger.info("<"+obdSn+">流量清零操作，同步下发结果："+result);
						if(GlobalData.isSendResultSuccess(result)){
							SimStockInfo simInfo = new SimStockInfo(IDUtil.createID());
							simInfo.setObdSn(obdSn);
							simInfo.setValid(1);
							simInfo.setCreateTime(new Date());
							simInfo.setFlowUse(cleanAfterFlow);
							simInfo.setTempFlowUse(0L);
							simInfo.setCreateTime(new Date());
							simStockInfoService.add(simInfo);
							obdHandlerDeviceInitLogger.info("<"+obdSn+">流量新增记录："+simInfo);
						}
					}else{//没清零，更新记录
						simStockInfo.setFlowUse(totalFlowUse);
						simStockInfo.setTempFlowUse(0L);
						simStockInfoService.simStockInfoUpdate(simStockInfo);
					}
				}else if(totalFlowUse-flow > 0){//后台>设备：同步下发设备
					obdHandlerDeviceInitLogger.info("<"+obdSn+">【同步】后台>设备：同步下发设备->flowUse:"+flowUse
							+",tempFlowUse:"+tempFlowUse+"="+totalFlowUse
							+">flow:"+flow);
					if(simStockInfo.getValid() == 0){//清零，+同步
						Long cleanAfterFlow = totalFlowUse -simStockInfo.getCleanFlowUse();
						String result = serverSettingService.deviceFlowSync(obdSn, cleanAfterFlow);
						obdHandlerDeviceInitLogger.info("<"+obdSn+">流量清零操作，同步下发结果："+result);
						if(GlobalData.isSendResultSuccess(result)){
							SimStockInfo simInfo = new SimStockInfo(IDUtil.createID());
							simInfo.setObdSn(obdSn);
							simInfo.setValid(1);
							simInfo.setFlowUse(cleanAfterFlow);
							simInfo.setTempFlowUse(0L);
							simInfo.setCreateTime(new Date());
							simStockInfoService.add(simInfo);
							obdHandlerDeviceInitLogger.info("<"+obdSn+">流量新增记录："+simInfo);
						}
					}else{//不清零，执行同步
						String result = serverSettingService.deviceFlowSync(obdSn, totalFlowUse);
						obdHandlerDeviceInitLogger.info("<"+obdSn+">流量不清零操作，同步下发结果："+result);
						//没清零，更新记录
						if(GlobalData.isSendResultSuccess(result)){
							simStockInfo.setFlowUse(totalFlowUse);
							simStockInfo.setTempFlowUse(0L);
							simStockInfoService.simStockInfoUpdate(simStockInfo);
						}
					}
				}else{//后台<设备：后台更新为设备数据
					obdHandlerDeviceInitLogger.info("<"+obdSn+">【同步】后台<设备：后台更新为设备数据->flowUse:"+flowUse
							+",tempFlowUse:"+tempFlowUse+"="+(totalFlowUse)
							+"<flow:"+flow);
					if(simStockInfo.getValid() == 0){//清零
						Long cleanAfterFlow = flow-simStockInfo.getCleanFlowUse();
						String result = serverSettingService.deviceFlowSync(obdSn, cleanAfterFlow);
						obdHandlerDeviceInitLogger.info("<"+obdSn+">流量清零操作，同步下发结果："+result);
						if(GlobalData.isSendResultSuccess(result)){
							SimStockInfo simInfo = new SimStockInfo(IDUtil.createID());
							simInfo.setObdSn(obdSn);
							simInfo.setValid(1);
							simInfo.setFlowUse(cleanAfterFlow);
							simInfo.setTempFlowUse(0L);
							simInfo.setCreateTime(new Date());
							simStockInfoService.add(simInfo);
							obdHandlerDeviceInitLogger.info("<"+obdSn+">流量新增记录："+simInfo);
						}
					}else{//不清零，同步操作
						simStockInfo.setFlowUse(_flowCounter);
						simStockInfo.setTempFlowUse(0L);
						simStockInfoService.simStockInfoUpdate(simStockInfo);
						obdHandlerDeviceInitLogger.info("<"+obdSn+">更新后simStockInfo:"+simStockInfo);
					}
					
				}
				
			}
		}
		
		boolean isSameUpElectricNo = false;//是否同一上电号
		//有无上电序号	0：无；1：有
		if ('1' == bits[21]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String upElectricNo = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _upElectricNo = Integer.valueOf(upElectricNo, 16);
			Integer lastUpElectricNo = travelDataUtil.getUpElectricNo(obdSn);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">上次上电序号lastUpElectricNo："+lastUpElectricNo);
			if(lastUpElectricNo == null){//没有上次上电号，则把上次上电号设置为此次上电号
				travelDataUtil.putUpElectricNo(obdSn, _upElectricNo);
			}else{
				obdHandShake.setLastUpElectricNo(lastUpElectricNo);
				if(lastUpElectricNo.intValue() != _upElectricNo.intValue()){
					travelDataUtil.putUpElectricNo(obdSn, _upElectricNo);
				}else{
					//此次上电序号跟上次上电序号一致，说明是同一行程
					isSameUpElectricNo = true;
				}
			}
			obdHandShake.setUpElectricNo(_upElectricNo);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">上电序号："+ _upElectricNo);
		}
		
		//有无SIM ID	0：无；1：有
		if ('1' == bits[22]) {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String simIdTypeStr = cutStrs[0];
			msgBody = cutStrs[1];
			ObdSimCard obdSimCard = new ObdSimCard();
			obdSimCard.setId(IDUtil.createID());
			obdSimCard.setObdSn(obdSn);
			obdSimCard.setFirmVersion(obdHandShake.getFirmwareVersion());
			obdSimCard.setLastSleep(obdHandShake.getLastSleep());
			obdSimCard.setUpElectricNo(obdHandShake.getUpElectricNo());
			Integer simIdType = Integer.valueOf(simIdTypeStr, 16);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【SIM ID】卡类型："+simIdTypeStr+"->"+simIdType);
			obdSimCard.setType(simIdType);
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String simIdLengthStr = cutStrs[0];
			Integer simIdLength = Integer.valueOf(simIdLengthStr, 16);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【SIM ID】卡号长度："+simIdLengthStr+"->"+simIdLength);
			msgBody = cutStrs[1];
			cutStrs = StrUtil.cutStrByByteNum(msgBody, simIdLength);
			String simIdStr = cutStrs[0];
			String simId = StrUtil.hex2ASCII(simIdStr);
			msgBody = cutStrs[1];
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【SIM ID】卡号："+simIdStr+"->"+simId);
			obdSimCard.setNumber(simId);
			obdSimCard.setCreateTime(new Date());
			obdSimCardService.oscSave(obdSimCard);
		}
		
		if(hasLastSleep){//有前次原因则进行
			if(isSameUpElectricNo && ("00".equals(rest) || "04".equals(rest))){//同一上电号+前次休眠原因00/04+有行程开始时间-》断电
				obdHandlerDeviceInitLogger.info("<"+obdSn+">同一上电号+前次休眠原因00/04！进行行程统计...");
				travelDataHanlder(obdSn);
			}
			if(!isSameUpElectricNo){
				obdHandlerDeviceInitLogger.info("<"+obdSn+">上电序号不同！进行清除统计...");
				travelDataUtil.clearTravelStart(obdSn);
				travelDataUtil.clearTravelEnd(obdSn);
				travelDataUtil.clearTravelOil(obdSn);
				travelDataUtil.clearTravelMile(obdSn);
			}
		}
		//握手包，相同报文并且》10s才入库。
		ObdHandShake handShake = obdHandShakeService.findLastBySn(obdSn);
		
		if(handShake == null || //空，入库
				!handShake.getMessage().equals(obdHandShake.getMessage()) ||//不同，入库
				(handShake.getMessage().equals(obdHandShake.getMessage())
				&& 
				(new Date().getTime()-handShake.getCreateTime().getTime()) > 60 * 1000))//同，》60秒入库
		{
			//清除位置数据部分统计
			if(GlobalData.OBD_PositionCarSpeed.containsKey(obdSn)){
				GlobalData.OBD_PositionCarSpeed.remove(obdSn);
			}
			if(GlobalData.OBD_PositionGPSSpeed.containsKey(obdSn)){
				GlobalData.OBD_PositionGPSSpeed.remove(obdSn);
			}
			if(GlobalData.OBD_PositionECUWarn.containsKey(obdSn)){
				GlobalData.OBD_PositionECUWarn.remove(obdSn);
			}
			//前次休眠原因操作：清空报警统计的记录
			if(! isSameUpElectricNo && hasLastSleep && bit0 != '1'){//不是同一上电号+有前次休眠原因+不是定时唤醒
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录-----");
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：OverSpeed"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.OverSpeed));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：IllegalStartUp"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.IllegalStartUp));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：IllegalShock"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.IllegalShock));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_In"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.Efence_In));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_Out"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.Efence_Out));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_InOut_In"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.Efence_InOut_In));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：Efence_InOut_Out"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.Efence_InOut_Out));
				obdHandlerDeviceInitLogger.info("<"+obdSn+">-----清空报警记录：FatigueDrive"+ pushUtil.getPushByRedis(obdSn+"_"+WarnType.FatigueDrive));
				pushUtil.cleanAllPushByRedis(obdSn);
				if(obdHandShake.getCarFaultCode() == 1){//有故障码
					ObdState obdStateModel = obdSateService.queryByObdSn(obdSn);
					if(obdStateModel == null  || "1".equals(obdStateModel.getCarfaultSwitch())){//默认推送，有设置开则推
						obdHandlerDeviceInitLogger.info("<"+obdSn+">推送【车辆故障提醒！可能有故障，请体检。】");
						pushApi.pushWarnHandler(obdSn,WarnType.CarFault,"17", "车辆故障提醒！可能有故障，请体检。");
					}
				}
				obdHandlerDeviceInitLogger.info("<"+obdSn+">重新断电，流量统计变更");
	
				SimStockInfo simStockInfo = simStockInfoService.queryLastByObdSn(obdSn);//流量
				obdHandlerDeviceInitLogger.info("<"+obdSn+">更新前："+simStockInfo);
				if(simStockInfo != null){
					Long tempFlowUse = simStockInfo.getTempFlowUse();
					Long flowUse = simStockInfo.getFlowUse();
					tempFlowUse = tempFlowUse==null?0:tempFlowUse;
					flowUse = flowUse==null?0:flowUse;
					simStockInfo.setFlowUse(flowUse+tempFlowUse);
					simStockInfo.setTempFlowUse(0L);
					simStockInfoService.simStockInfoUpdate(simStockInfo);
					obdHandlerDeviceInitLogger.info("<"+obdSn+">更新后："+simStockInfo);
				}else{
					simStockInfo = new SimStockInfo(IDUtil.createID()); 
					simStockInfo.setValid(1);
					simStockInfo.setObdSn(obdSn);
					simStockInfo.setTempFlowUse(0L);
					simStockInfo.setFlowUse(0L);
					simStockInfo.setCreateTime(new Date());
					simStockInfoService.add(simStockInfo);
					obdHandlerDeviceInitLogger.info("<"+obdSn+">无流量卡信息记录，新增："+simStockInfo);
				}
				
				//告警推送
				if(map.containsKey("startupType_"+obdSn)){
//					CarInfo carInfo = carInfoService.queryCarInfoBySN(obdSn);
					ObdState obdStateModel = obdSateService.queryByObdSn(obdSn);
					int type = map.get("startupType_"+obdSn);
					switch (type) {
						case 0:
							if(obdStateModel != null  && "1".equals(obdStateModel.getStartupSwitch())){//开关
								pushApi.pushWarnHandler(obdSn,WarnType.IllegalStartUp,"11", "非法启动(布防时才会产生)报警");
							}
							break;
						case 1:
							if(obdStateModel != null  && "1".equals(obdStateModel.getShakeSwitch())){//开关
								pushApi.pushWarnHandler(obdSn,WarnType.IllegalShock,"12", "非法震动(布防时才会产生)报警");
							}
							break;
						default:
							break;
					}
					map.remove("startupType_"+obdSn);
				}
			}
			obdHandlerDeviceInitLogger.info("握手包入库："+obdHandShake);
			obdHandShakeService.add(obdHandShake);
		}else{
			obdHandlerDeviceInitLogger.info("握手包重复，不入库！");
		}
		
		return msgBody;
	}
	
	/**
	 * 处理断电之后行程数据的处理
	 * @param obdSn
	 * @throws Exception
	 */
	private synchronized void travelDataHanlder(String obdSn) throws Exception{
		String travelStartStr =  travelDataUtil.getTravelStart(obdSn);
		if(!StringUtils.isEmpty(travelStartStr)){
			String travelMileStr = travelDataUtil.getTravelMile(obdSn);
			String travelOilStr = travelDataUtil.getTravelOil(obdSn);
			String travelEndStr =  travelDataUtil.getTravelEnd(obdSn);
			//如果不全则不统计，进行清除统计操作
			if(StringUtils.isEmpty(travelMileStr) || StringUtils.isEmpty(travelOilStr) || StringUtils.isEmpty(travelEndStr) ){
				obdHandlerDeviceInitLogger.info("<"+obdSn+">数据不完整,清除行程统计。");
				obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】开始时间："+travelStartStr);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】结束时间："+travelEndStr);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】油耗："+travelOilStr);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】里程："+travelMileStr);
				travelDataUtil.clearTravelStart(obdSn);
				travelDataUtil.clearTravelEnd(obdSn);
				travelDataUtil.clearTravelOil(obdSn);
				travelDataUtil.clearTravelMile(obdSn);
				return;
			}
			Date travelStart = ThreadLocalDateUtil.parse("yyyyMMddHHmmss", travelStartStr);
			Date travelEnd = ThreadLocalDateUtil.parse("yyyyMMddHHmmss", travelEndStr);
			if(travelEnd.getTime() < travelStart.getTime()){
				obdHandlerDeviceInitLogger.info("<"+obdSn+">结束时间小于开始时间，结束："+travelEnd+",开始："+travelStart+",清除行程统计。");
				travelDataUtil.clearTravelStart(obdSn);
				travelDataUtil.clearTravelEnd(obdSn);
				travelDataUtil.clearTravelOil(obdSn);
				travelDataUtil.clearTravelMile(obdSn);
				return;
			}
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】开始时间："+travelStartStr);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】结束时间："+travelEndStr);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】油耗："+travelOilStr);
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】里程："+travelMileStr);
			Long distance = Long.valueOf(travelMileStr);
			Long totalFuel = Long.valueOf(travelOilStr);
			CarTraveltrack carTraveltrack = new CarTraveltrack(IDUtil.createID());
			carTraveltrack.setObdsn(obdSn);
			carTraveltrack.setTravelStart(travelStart);
			carTraveltrack.setTravelEnd(travelEnd);
			carTraveltrack.setDistance(distance);
			carTraveltrack.setTotalFuel(totalFuel * 10);
			Integer driveTime = Long.valueOf((travelEnd.getTime()-travelStart.getTime())/1000).intValue();
			carTraveltrack.setDriverTime(driveTime);
			
			CarOilInfo carOilInfo = new CarOilInfo();//油耗
			carOilInfo.setObdSN(obdSn);
			carOilInfo.setOilInfoTime(travelEnd);
			carOilInfo.setMileageNum(distance+"");
			carOilInfo.setPetrolConsumeNum((totalFuel * 10) + "");
			carOilInfo.setTimeSpanNum(driveTime+"");//秒
			CarInfo carInfo = carInfoService.queryCarInfoBySN(obdSn);
			if(carInfo != null)
				carOilInfo.setCarId(carInfo.getCarId());
			carOilInfo.setOilInfoId(carTraveltrack.getId());
			
			//去重
			if(carTraveltrackService.getTravelByObdAndTime(obdSn,carTraveltrack.getTravelStart(),carTraveltrack.getTravelEnd()) == null){
				carTraveltrack.setInsesrtTime(new Date());
				List<CarTraveltrack> carTraveltracks = carTraveltrackService.queryTravel(obdSn,carTraveltrack.getTravelStart());
				for (CarTraveltrack ct : carTraveltracks) {
					if(ct != null){
						//行程
						ct.setType(2);// //排除序号递增，重复数据去除；或者半条去除
						carTraveltrackService.carTraveltrackUpdate(ct);
						//油耗
						CarOilInfo oilInfo = carOilInfoService.find(ct.getId());
						if(oilInfo != null){
							oilInfo.setType(2);
							carOilInfoService.carOilInfoUpdate(oilInfo);
						}
					}
				}
				//将正常行程的驾驶行为作为统计行程的驾驶行为
				if(carTraveltracks != null && carTraveltracks.size() > 0){
					String lastTravelTracksId = carTraveltracks.get(0).getId();
					CarDriveInfo driveInfo = carDriveInfoService.find(lastTravelTracksId);
					if(driveInfo != null){
						driveInfo.setType(2);
						carDriveInfoService.carDriveInfoUpdate(driveInfo);
						CarDriveInfo driveInfoNew = driveInfo;
						driveInfoNew.setType(0);
						driveInfoNew.setDriveInfoId(carTraveltrack.getId());
						carDriveInfoService.carDriveInfoSave(driveInfoNew);
					}
					//旧行程数据迁移
					CarTraveltrack carTraveltrackOld =  carTraveltracks.get(0);
					carTraveltrack.setSpeed(carTraveltrackOld.getSpeed());
					carTraveltrack.setBrakesNum(carTraveltrackOld.getBrakesNum());
					carTraveltrack.setQuickenNum(carTraveltrackOld.getQuickenNum());
					carTraveltrack.setQuickTurn(carTraveltrackOld.getQuickTurn());
					carTraveltrack.setQuickSlowDown(carTraveltrackOld.getQuickSlowDown());
					carTraveltrack.setIdling(carTraveltrackOld.getIdling());
					carTraveltrack.setOverspeedTime(carTraveltrackOld.getOverspeedTime());
					carTraveltrack.setVoltage(carTraveltrackOld.getVoltage());
					carTraveltrack.setSpeedMismatch(carTraveltrackOld.getSpeedMismatch());
					carTraveltrack.setEngineMaxSpeed(carTraveltrackOld.getEngineMaxSpeed());
					carTraveltrack.setTemperature(carTraveltrackOld.getTemperature());
					carTraveltrack.setQuickLaneChange(carTraveltrackOld.getQuickLaneChange());
				}
				carTraveltrack.setType(0);
				carTraveltrackService.add(carTraveltrack);
				carOilInfo.setType(0);
				carOilInfoService.carOilSave(carOilInfo);
			}
			travelDataUtil.clearTravelStart(obdSn);
			travelDataUtil.clearTravelEnd(obdSn);
			travelDataUtil.clearTravelOil(obdSn);
			travelDataUtil.clearTravelMile(obdSn);
		}else{
			obdHandlerDeviceInitLogger.info("<"+obdSn+">【行程统计】开始时间为空！："+travelStartStr);
		}
	}
	
	/**
	 * 车辆状态数据处理
	 * @param message 
	 * @param msgBody
	 * @param obdHandShake 
	 * @return
	 */
	private String carStateHandler(OBDMessage message, String msgBody, ObdHandShake obdHandShake) {
		String obdSn = message.getId();
		obdHandlerDeviceInitLogger.info("-----------"+obdSn+"---车辆状态数据处理--------------");
		String[] cutStrs;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String carState = cutStrs[0];
			msgBody = cutStrs[1];
			
			obdHandlerDeviceInitLogger.info("<"+obdSn+">>>车辆状态数据:"+carState);
			//保存服务器请求查询 车辆状态
			Integer _waterNo = Integer.valueOf(message.getWaterNo(),16);
			GlobalData.OBD_ACK_OR_QueryData.put(message.getId()+"_"+_waterNo, carState);
			
			char[] bits = StrUtil.hexToBinary(carState);
			//0-3	ECU通讯协议	
			String ecuPrtocol = ""+bits[3]+bits[2]+bits[1]+bits[0];
			String ecuDes = "";
			switch (ecuPrtocol) {
			case "0000"://ECU通讯异常
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ECU通讯异常:"+ecuPrtocol);
				ecuDes = "ECU通讯异常";
				break;
			case "0001"://ISO15765_STD_500K
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ISO15765_STD_500K:"+ecuPrtocol);
				ecuDes = "ISO15765_STD_500K";
				break;
			case "0010"://ISO15765_STD_250K
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ISO15765_STD_250K:"+ecuPrtocol);
				ecuDes = "ISO15765_STD_250K";
				break;
			case "0011"://ISO15765_EXT_500K
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ISO15765_EXT_500K:"+ecuPrtocol);
				ecuDes = "ISO15765_EXT_500K";
				break;
			case "0100"://ISO15765_EXT_250K
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ISO15765_EXT_250K:"+ecuPrtocol);
				ecuDes = "ISO15765_EXT_250K";
				break;
			case "0101"://ISO9141
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ISO9141:"+ecuPrtocol);
				ecuDes = "ISO9141";
				break;
			case "0110"://ISO14230
				obdHandlerDeviceInitLogger.info("<"+obdSn+">ISO14230:"+ecuPrtocol);
				ecuDes = "ISO14230";
				break;
			//其他：保留
			default:
				obdHandlerDeviceInitLogger.info("<"+obdSn+">其他:"+ecuPrtocol);
				ecuDes = "其他";
				break;
			}
			obdHandShake.setEcu(ecuPrtocol+"->"+ecuDes);
			
			//车辆故障码	0：无；1：有
			if('0' == bits[4]){
				obdHandShake.setCarFaultCode(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">车辆故障码	0：无");
			}else{
				obdHandShake.setCarFaultCode(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">车辆故障码	1：有");
//				obdHandlerDeviceInitLogger.info(obdSn+"---发送读取车辆故障码指令----");
//				List<FaultUpload> faultUploads = serverRequestQueryService.readFaultCode(message.getId());
//				obdHandlerDeviceInitLogger.info(faultUploads);
//				List<String> codeLists = new ArrayList<String>();
//				if(faultUploads != null && faultUploads.size() > 0){
//					for (FaultUpload faultUpload : faultUploads) {
//						String faultCode = faultUpload.getFaultCode();
//						if(!StringUtils.isEmpty(faultCode)){
//							codeLists.add(faultCode);
//						}
//					}
//				}
//				if(this.sameUpElectricNo){
//					pushApi.pushWarnHandler(obdSn,WarnType.CarFault,"17", "车辆故障提醒！可能有故障，请体检。");
//				}
			}
			//蓄电池电压	0：正常；1：异常
			if('0' == bits[5]){
				obdHandShake.setVoltStatus(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">蓄电池电压	0：正常");
			}else{
				obdHandShake.setVoltStatus(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">蓄电池电压	1：异常");
			}
			//发动机水温	0：正常；1：异常
			if('0' == bits[6]){
				obdHandShake.setEngineWater(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">发动机水温	0：正常");
			}else{
				obdHandShake.setEngineWater(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">发动机水温	1：异常");
			}
			
			//启动方式	0：点火启动；1：震动启动
			if('0' == bits[7]){
				obdHandShake.setStartMode(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">启动方式	0：点火启动");
				map.put("startupType_"+obdSn, 0);
			}else{
				obdHandShake.setStartMode(1);
				map.put("startupType_"+obdSn, 1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">启动方式	1：震动启动");
			}
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceInitLogger.error(obdSn,e);
		}
		return msgBody;
	}

	/**
	 * 报警设置数据处理
	 * @param message 
	 * @param msgBody
	 * @return
	 */
	private String warnSetHandler(OBDMessage message, String msgBody, ObdHandShake obdHandShake) {
		String obdSn = message.getId();
		obdHandlerDeviceInitLogger.info("----------------"+obdSn+"---报警设置数据处理-------------------");
		String[] cutStrs;
		try {
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String warnSet = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceInitLogger.info("<"+obdSn+">>>报警设置数据："+warnSet);
			//保存服务器请求查询 报警设置数据
			Integer _waterNo = Integer.valueOf(message.getWaterNo(),16);
			GlobalData.OBD_ACK_OR_QueryData.put(message.getId()+"_"+_waterNo, warnSet);
			
			char[] bits  = StrUtil.hexToBinary(warnSet);
			//非法启动探测
			if('0' == bits[0]){
				//开启
				obdHandShake.setIllegalStartSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">非法启动探测 开启:"+0);
			}else{
				//关闭
				obdHandShake.setIllegalStartSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">非法启动探测 关闭:"+bits[0]);
			}
			//非法震动探测
			if('0' == bits[1]){
				//开启
				obdHandShake.setIllegalShockSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">非法震动探测 开启:"+0);
			}else{
				//关闭
				obdHandShake.setIllegalShockSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">非法震动探测 关闭:"+bits[1]);
			}
			//蓄电电压异常报警
			if('0' == bits[2]){
				//开启
				obdHandShake.setVoltUnusualSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">蓄电电压异常报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setVoltUnusualSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">蓄电电压异常报警 关闭:"+bits[2]);
			}
			//发动机水温高报警
			if('0' == bits[3]){
				//开启
				obdHandShake.setEngineWaterWarnSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">发动机水温高报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setEngineWaterWarnSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">发动机水温高报警 关闭:"+ bits[3]);
			}
			//车辆故障报警
			if('0' == bits[4]){
				//开启
				obdHandShake.setCarWarnSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">车辆故障报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setCarWarnSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">车辆故障报警 关闭:"+bits[4]);
			}
			//超速报警
			if('0' == bits[5]){
				//开启
				obdHandShake.setOverSpeedWarnSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">超速报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setOverSpeedWarnSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">超速报警 关闭:"+bits[5]);
			}
			//电子围栏报警
			if('0' == bits[6]){
				//开启
				obdHandShake.setEfenceWarnSet(0);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">电子围栏报警 开启:"+0);
			}else{
				//关闭
				obdHandShake.setEfenceWarnSet(1);
				obdHandlerDeviceInitLogger.info("<"+obdSn+">电子围栏报警 关闭:"+bits[6]);
			}
			//7保留
			
		}catch(Exception e){
			e.printStackTrace();
			obdHandlerDeviceInitLogger.error(obdSn+e);
		}
		return msgBody;
	}
	
}
