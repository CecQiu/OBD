package com.hgsoft.obd.service;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.FOTA;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.IPUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.DomainSetType;
import com.hgsoft.obd.util.EFenceType;
import com.hgsoft.obd.util.ExtensionDataSetType;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
import com.hgsoft.obd.util.UpgradeType;
import com.hgsoft.system.utils.ByteUtil;
/**
 * 服务器设置OBD
 * @author sujunguang
 * 2016年1月12日
 * 上午11:35:07
 */
@Service
public class ServerSettingService {
	
	private static Logger serverSendObdLogger = LogManager.getLogger("serverSendObdLogger");
	@Resource
	private SendUtil sendUtil;
	/**************************设备功能设置**************************/
	/**
	 * WiFi 开关
	 * @param obdSn
	 * @param state 状态 0-关 1-开
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String wifi(String obdSn,String state) throws OBDException{
		serverSendObdLogger.info("--------------【WiFi设置】--------------");
		String msgBody = ObdConstants.Server_SettingWiFi_OBD_Cmd;
		if("0".equals(state)){
			//开
			msgBody += "0000";
			serverSendObdLogger.info("--------------【WiFi设置】开");
		}else if("1".equals(state)){
			//关
			msgBody += "0001";
			serverSendObdLogger.info("--------------【WiFi设置】关");
		}else{
			serverSendObdLogger.error("--------------【WiFi设置】不合法的状态");
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * GPS 开关
	 * @param obdSn
	 * @param state 状态 0-开 1-关
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String gps(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingGPS_OBD_Cmd;
		if("0".equals(state)){
			//开
			msgBody += "0000";
		}else if("1".equals(state)){
			//关
			msgBody += "0002";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * GPS 数据格式
	 * @param obdSn
	 * @param state 状态 0-只传定位数据 1-全部
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String gpsFormat(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingGPSFormat_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0004";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 离线心跳设置
	 * @param obdSn
	 * @param state 状态 0-无 1-设置
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String offHeart(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingOffHeart_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0008";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 车辆设防撤防设置
	 * @param obdSn
	 * @param state 状态 0-关闭 1-开启
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String carGuard(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingGuard_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0010";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 车辆震动报警开关
	 * @param obdSn
	 * @param state 状态 0-关闭 1-开启
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String carShock(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingShock_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0020";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 超速报警开关
	 * @param obdSn
	 * @param state 状态 0-关闭 1-开启
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String overSpeedWarn(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingOverSpeed_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0040";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 清除故障码
	 * @param obdSn
	 * @param state 状态 0-无 1-操作
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String clearFaultCode(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingClearFaultCode_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0080";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 恢复出厂设置
	 * @param obdSn
	 * @param state 状态 0-无 1-操作
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String reset(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingReset_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0100";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 设备重启
	 * @param obdSn
	 * @param state 状态 0-无 1-重启
	 * @return 01-执行成功 其它-未知
	 * @throws OBDException 
	 */
	public String restart(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingRestart_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0200";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 设备升级设置
	 * @param obdSn
	 * @param state
	 * @param upgradeType 升级固件类型
	 * @param version 版本号
	 * @return
	 * @throws OBDException
	 */
	public String deviceUpgradeSet(String obdSn,String state, UpgradeType upgradeType, String version) throws OBDException{
		serverSendObdLogger.info("---------------【设备升级设置】------------");
		serverSendObdLogger.info("---------------【设备升级设置】开1："+ state);
		String msgBody = ObdConstants.Server_SettingDeviceUpgrade_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0400";
		}else{
			throw new OBDException("不合法的状态");
		}
		if(upgradeType != null){
			msgBody += upgradeType.getValue();
		}
		if(!StringUtils.isEmpty(version)){
			msgBody += version;
		}
		serverSendObdLogger.info("---------------【设备升级设置】发送报文："+ msgBody);
		return send(obdSn,msgBody);
	}
	
	/**
	 * 清除流量计数值
	 * @param obdSn
	 * @param state 0-无 1-清除
	 * @return
	 * @throws OBDException
	 */
	public String cleanFlowStat(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingCleanFlowStat_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "0800";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 关闭设备
	 * @param obdSn
	 * @param state 0-无 1-关闭
	 * @return
	 * @throws OBDException
	 */
	public String closeDevice(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingCloseDevice_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "1000";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * FOTA升级设置
	 * @param obdSn
	 * @param state 0-无 1-更新MIFI
	 * @return
	 * @throws OBDException
	 */
	public String fotaUpgradeSet(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingFOTAUpgrade_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "2000";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**
	 * 流量开关设置
	 * @param obdSn
	 * @param state 0-关 1-开
	 * @return
	 * @throws OBDException
	 */
	public String wifiFlowSet(String obdSn,String state) throws OBDException{
		String msgBody = ObdConstants.Server_SettingWiFiFlow_OBD_Cmd;
		if("0".equals(state)){
			msgBody += "0000";
		}else if("1".equals(state)){
			msgBody += "4000";
		}else{
			throw new OBDException("不合法的状态");
		}
		return send(obdSn,msgBody);
	}
	
	/**************************设备参数设置**************************/
	/**
	 * 报警设置
	 * @param obdSn
	 * @param 
	    0	非法启动探测	0：开启；1：关闭
		1	非法震动探测	0：开启；1：关闭
		2	蓄电电压异常报警	0：开启；1：关闭
		3	发动机水温高报警	0：开启；1：关闭
		4	车辆故障报警	0：开启；1：关闭
		5	超速报警	0：开启；1：关闭
		6	电子围栏报警	0：开启；1：关闭
		7	保留	
	 * @return
	 * @throws Exception 
	 */
	public String warnSet(String obdSn,String warnSet) throws Exception{
		if(warnSet == null || warnSet.length() != 8){
			throw new OBDException("报警设置参数非法！");
		}
		String msgBody = ObdConstants.Server_SettingWarn_OBD_Cmd + StrUtil.binary2Hex(warnSet.toCharArray());
		return send(obdSn,msgBody);
	}
	
	/**
	 * 行程参数设置
	 * @param obdSn 设备号
	 * @param bits 数据帧格式 16bit
	 * @param obdTravelParams 行程参数实体
	 * @return
	 * @throws Exception
	 */
	public String travelParamsSet(String obdSn, char[]bits, OBDTravelParams obdTravelParams) throws Exception{
		validateBinary(bits);
		if(obdTravelParams == null){
			throw new OBDException("行程参数设置实体不能为空！");
		}
		if(bits == null || bits.length != 16){
			throw new OBDException("行程参数数据帧格式非法！");
		}
		char[] bitss = new char[bits.length];//高位在前
		bitss = Arrays.copyOfRange(bits, 0, bits.length);
		String msgBody = ObdConstants.Server_SettingTravelParams_OBD_Cmd + StrUtil.binary2Hex(bitss);
		
		//蓄电池电压阈值—0-存在
		if(bits[0] == '0'){
			String batteryLow = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getBatteryLow()),2, 0, "0");
			String batteryHigh = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getBatteryHigh()),2, 0, "0");
			msgBody += batteryLow	 + batteryHigh;
		}
		//超速阈值
		if(bits[1] == '0'){
			String overSpeed = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getOverSpeed()),2, 0, "0");
			String limitSpeedLazy = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getLimitSpeedLazy()),2, 0, "0");
			msgBody += overSpeed	 + limitSpeedLazy;
		}
		//急转弯阈值 
		if(bits[2] == '0'){
			String shuddenTurnSpeed = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenTurnSpeed()),2, 0, "0");
			String shuddenTurnAngle = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenTurnAngle()),2, 0, "0");
			msgBody += shuddenTurnSpeed	 + shuddenTurnAngle;
		}
		//急加速阈值
		if(bits[3] == '0'){
			String shuddenOverSpeed = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenOverSpeed()),2, 0, "0");
			String shuddenOverSpeedTime = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenOverSpeedTime()),2, 0, "0");
			msgBody += shuddenOverSpeed	 + shuddenOverSpeedTime;
		}
		//急减速阈值
		if(bits[4] == '0'){
			String shuddenLowSpeed = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenLowSpeed()),2, 0, "0");
			String shuddenLowSpeedTime = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenLowSpeedTime()),2, 0, "0");
			msgBody += shuddenLowSpeed	 + shuddenLowSpeedTime;
		}
		//急变道阈值
		if(bits[5] == '0'){
			String shuddenChangeAngle = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenChangeAngle()),2, 0, "0");
			String shuddenChangeTime = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenChangeTime()),2, 0, "0");
			msgBody += shuddenChangeAngle + shuddenChangeTime;
		}
		//发动机水温报警阈值
		if(bits[6] == '0'){
			String engineLowTemperature = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getEngineLowTemperature()),2, 0, "0");
			String engineHighTemperature = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getEngineHighTemperature()),2, 0, "0");
			msgBody += engineLowTemperature + engineHighTemperature;
		}
		//发动机转数报警阈值 
		if(bits[7] == '0'){
			String engineTurnsWarn = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getEngineTurnsWarn()),4, 0, "0");
			msgBody += engineTurnsWarn;
		}
		//车速转速不匹配阈值 
		if(bits[8] == '0'){
			String speedNotMatch = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getSpeedNotMatch()),4, 0, "0");
			String speedNotMatchStep = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getSpeedNotMatchStep()),4, 0, "0");
			msgBody += speedNotMatch + speedNotMatchStep;
		}
		//长怠速阈值 
		if(bits[9] == '0'){
			String longLowSpeed = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getLongLowSpeed()),2, 0, "0");
			String longLowSpeedTime = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getLongLowSpeedTime()),2, 0, "0");
			msgBody += longLowSpeed + longLowSpeedTime;
		}
		//急刹车强度阈值 
		if(bits[10] == '0'){
			String shuddenBrakeStrength = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShuddenBrakeStrength()),2, 0, "0");
			msgBody += shuddenBrakeStrength;
		}
		//侧翻角度阈值（度） 
		if(bits[11] == '0'){
			String sideTurnAngle = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getSideTurnAngle()),2, 0, "0");
			msgBody += sideTurnAngle;
		}
		//碰撞强度阈值 
		if(bits[12] == '0'){
			String crashStrength = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getCrashStrength()),2, 0, "0");
			msgBody += crashStrength;
		}
		//震动报警强度阈值 
		if(bits[13] == '0'){
			String shockStrength = StrUtil.strAppend(Integer.toHexString(obdTravelParams.getShockStrength()),2, 0, "0");
			msgBody += shockStrength;
		}
		
		//电子栅栏单独抽出来
		/*//电子围栏驶出报警坐标
		if(bits[14] == '0'){
			String fenceOutLongtitudeStr = obdTravelParams.getFenceOutLongtitude();
			fenceOutLongtitudeStr = fenceOutLongtitudeStr.replaceAll("'", "").replaceAll("\\.", "").replaceAll("°", "");
			String fenceOutLatitudeStr = obdTravelParams.getFenceOutLatitude();
			fenceOutLatitudeStr = fenceOutLatitudeStr.replaceAll("'", "").replaceAll("\\.", "").replaceAll("°", "");
			String fenceOutLongtitude = StrUtil.strAppend(fenceOutLongtitudeStr,8, 1, "0");
			String fenceOutLatitude = StrUtil.strAppend(fenceOutLatitudeStr,8, 1, "0");
			msgBody += fenceOutLongtitude	 + fenceOutLatitude;
		}
		//电子围栏进入报警坐标
		if(bits[15] == '0'){
			String fenceInLongtitudeStr = obdTravelParams.getFenceInLongtitude();
			fenceInLongtitudeStr = fenceInLongtitudeStr.replaceAll("'", "").replaceAll("\\.", "").replaceAll("°", "");
			String fenceInLatitudeStr = obdTravelParams.getFenceInLatitude();
			fenceInLatitudeStr = fenceInLatitudeStr.replaceAll("'", "").replaceAll("\\.", "").replaceAll("°", "");
			String fenceInLongtitude = StrUtil.strAppend(fenceInLongtitudeStr,8, 1, "0");
			String fenceInLatitude = StrUtil.strAppend(fenceInLatitudeStr,8, 1, "0");
			msgBody += fenceInLongtitude	 + fenceInLatitude;
		}*/

		return send(obdSn,msgBody);
	}
	
	/**
	 * 电子栅栏设置
	 * @param obdSn 设备号
	 * @param timingEFenceType 有无定时 有定时：data-开始时间 结束时间 yyyyMMddHHmmss
	 * @param efenceNo 区域编号
	 * @param type 围栏类型：圆/矩形
	 * @param warnEFenceType 报警方式
	 * @param data 数据
	 * @return
	 * @throws Exception 
	 */
	public String efenceSet(String obdSn,EFenceType timingEFenceType,int efenceNo,EFenceType type,
			EFenceType warnEFenceType,String... data) throws Exception{
		String dataFrame = "1111 1111 1111 1101".replaceAll(" ", "");
		String msgBody = ObdConstants.Server_SettingTravelParams_OBD_Cmd + StrUtil.binary2Hex(dataFrame.toCharArray());
		String msg = "";
		//定时定点+区域编号
		if(timingEFenceType == EFenceType.Timing){
			efenceNo += 128;
		}else if(timingEFenceType == EFenceType.NoTiming){
		}else{
			throw new OBDException("有无定时类型不匹配");
		}
		msg += StrUtil.strAppend(Integer.toHexString(efenceNo),2, 0, "0");
		//围栏类型+报警方式
		int warnType = EFenceType.getWarnType(warnEFenceType);
		if(warnType == -1){
			throw new OBDException("报警类型不符合！");
		}
		
		if(type == EFenceType.Circle){
			warnType += 128;//最高位为1
		}else if(type == EFenceType.Rectangle){
		}else{
			throw new OBDException("围栏类型不符合！");
		}
		msg += StrUtil.strAppend(Integer.toHexString(warnType),2, 0, "0");
		
		int i = 0;
		String bigLongtitude =  data[i++];
		String bigLatitude =  data[i++];
		String smallLongtitude =  data[i++];
		String smallLatitude =  data[i++];
		msg += CoordinateTransferUtil.format(bigLongtitude);
		msg += CoordinateTransferUtil.format(bigLatitude);
		msg += CoordinateTransferUtil.format(smallLongtitude);
		msg += CoordinateTransferUtil.format(smallLatitude);
		if(timingEFenceType == EFenceType.Timing){
			String timingBegin =  data[i++];//定时开始时间
			String timingEnd =  data[i++];//定时结束时间
			msg += timingBegin;
			msg += timingEnd;
		}
		String msgLen = StrUtil.strAppendByLen(Integer.toHexString(msg.length()/2),1,"0");
		msgBody += msgLen + msg;
		return send(obdSn,msgBody);
	}
	
	/**
	 * 	设备时间参数设置
	 * @param obdSn 设备号
	 * @param bits 数据帧格式 8bit
	 * @param obdTimeParams 	设备时间参数
	 * @return
	 * @throws Exception 
	 */
	public String deviceTimeSet(String obdSn,char[]bits,OBDTimeParams obdTimeParams) throws Exception{
		validateBinary(bits);
		if(obdTimeParams == null){
			throw new OBDException("时间参数设置实体不能为空！");
		}
		if(bits == null || bits.length != 8){
			throw new OBDException("行程参数数据帧格式非法！");
		}
		char[] bitss = new char[bits.length];//高位在前
		bitss = Arrays.copyOfRange(bits, 0, bits.length);
		String msgBody = ObdConstants.Server_SettingDeviceTimeParams_OBD_Cmd + StrUtil.binary2Hex(bitss);
		
		if(bits[0] == '0'){
			//进入休眠模式时间
			String sleepTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getSleepTime()),2, 0, "0");
			msgBody += sleepTime;
		}
		if(bits[1] == '0'){
			//熄火后WIFI使用时间
			String wifiUseTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getWifiUseTime()),2, 0, "0");
			msgBody += wifiUseTime;
		}
		if(bits[2] == '0'){
			//GPS数据采集时间间隔
			String gpsCollectDataTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getGpsCollectDataTime()),2, 0, "0");
			msgBody += gpsCollectDataTime;
		}
		if(bits[3] == '0'){
			//位置数据上传时间间隔
			String positionDataTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getPositionDataTime()),2, 0, "0");
			msgBody += positionDataTime;
		}
		if(bits[4] == '0'){
			//OBD在线心跳包时间间隔
			String obdOnlineTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getObdOnlineTime()),2, 0, "0");
			msgBody += obdOnlineTime;
		}
		if(bits[5] == '0'){
			//OBD离线心跳包时间间隔
			String obdOfflineTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getObdOfflineTime()),2, 0, "0");
			msgBody += obdOfflineTime;
		}
		if(bits[6] == '0'){
			//OBD离线数据保存时间间隔
			String obdOffDataTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getObdOffDataTime()),2, 0, "0");
			msgBody += obdOffDataTime;
		}
		if(bits[7] == '0'){
			//请求AGPS数据包延时时间（S）
			String requestAGPSTime = StrUtil.strAppend(Integer.toHexString(obdTimeParams.getRequestAGPSTime()),2, 0, "0");
			msgBody += requestAGPSTime;
		}
		return send(obdSn,msgBody);
	}
	
	
	/**
	 * 生成服务器参数报文
	 * @param ip IP地址
	 * @param port 端口
	 * @param APN APN
	 * @return
	 * @throws OBDException 
	 */
	private String serverParams(String ip,String port,String APN) throws OBDException{
		if(!IPUtil.isIPAdress(ip)){
			throw new OBDException("非法IP地址！");
		}
		String portStr = "";
		String ipStr = "";
		String ipStrLen = "";
		String APNStr = "";
		String APNStrLen = "";
		try {
			portStr = StrUtil.strAppend(Integer.toHexString(Integer.parseInt(port)),4,0,"0");
			String[] ipArr= ip.split("\\.");
			String ip1=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[0])),1,"0");
			String ip2=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[1])),1,"0");
			String ip3=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[2])),1,"0");
			String ip4=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[3])),1,"0");
			ipStr = ip1+ip2+ip3+ip4;
			ipStrLen = StrUtil.strAppendByLen(Integer.toHexString(ipStr.length()/2),1,"0");
			APNStr = ByteUtil.ASC2ToHexStr(APN);
			APNStrLen = StrUtil.strAppend(Integer.toHexString(APNStr.length()/2), 2, 0, "0");
		} catch (Exception e) {
			throw new OBDException("服务器参数异常："+e);
		}
		
		return portStr + ipStrLen + ipStr + APNStrLen + APNStr;
	}
	
	/**
	 * 数据服务器参数设置
	 * @param obdSn 设备号
	 * @param ip ip地址
	 * @param port 端口号
	 * @param APN APN
	 * @return
	 * @throws OBDException 
	 */
	public String dataServerParamsSet(String obdSn,String ip,String port,String APN) throws OBDException{
		String msgBody = ObdConstants.Server_SettingDataServerParams_OBD_Cmd + serverParams(ip, port, APN);
		return send(obdSn,msgBody);
	}
	
	/**
	 * 升级服务器参数设置
	 * @param obdSn 设备号
	 * @param ip ip地址
	 * @param port 端口号
	 * @param APN APN
	 * @return
	 * @throws OBDException 
	 */
	public String upgradeServerParamsSet(String obdSn,String ip,String port,String APN) throws OBDException{
		String msgBody = ObdConstants.Server_SettingUpgradeServerParams_OBD_Cmd + serverParams(ip, port, APN);
		return send(obdSn,msgBody);
	}
	
	/**
	 * portal服务器参数设置
	 * @param obdSn 设备号
	 * @param ip ip地址
	 * @param port 端口号
	 * @param APN APN
	 * @return
	 * @throws OBDException 
	 */
	public String portalServerParamsSet(String obdSn,String ip,String port,String APN) throws OBDException{
		String msgBody = ObdConstants.Server_SettingPortalServerParams_OBD_Cmd + serverParams(ip, port, APN);
		return send(obdSn,msgBody);
	}
	
	/**************************Portal或WiFi**************************/
	/**
	 * 设置portal或WiFi
	 * @param obdSn 设备号
	 * @param bits  数据帧格式 16bit
	 * @param data 
	 * 1000 0000 0000 0000：URL
	 * 0100 0000 0000 0000：ID
	 * 0010 0000 0000 0000：流量限制MAC+流量限制限额（M）
	 * 0001 0000 0000 0000：设置白名单-》 白名单长度+白名单
	 * 0000 1000 0000 0000：删除白名单-》 00：全部删除白名单（后面无数据）/ 01：删除单条白名单"+删除白名单MAC
	 * 0000 0101 0000 0000：portal开关 0:关闭portal+密码/1:打开portal
		 	(1)开portal,以下两个同时设:
				1.开portal
				2.清除密码
			(2)关portal,以下两个同时设:
				1.关portal
				2.设置密码
	
	 *  0000 0010 0000 0000：WIFI  ssid
	 *  0000 0001 0000 0000：密码
	 * @return
	 * @throws Exception
	 */
	public String portalOrWifiSet(String obdSn,char[]bits, String... data) throws Exception{
		validateBinary(bits);
		if(bits == null || bits.length != 16){
			throw new OBDException("设置portal或WiFi数据帧格式非法！");
		}
		char[] bitss = new char[bits.length];//高位在前
		bitss = Arrays.copyOfRange(bits, 0, bits.length);
		if(bitss[5] == '1'){
			bitss[7] = '1';
		}
		String msgBody = ObdConstants.Server_SettingPortalWiFi_OBD_Cmd + StrUtil.binary2Hex(bitss);
		int i = 0;
		if(bits[0] == '1'){
			//设置URL
			String url = data[i];
			//url ascii-> 16 进制值
			String urlStr = ByteUtil.ASC2ToHexStr(url);
			//url 长度
			String urlStrLen = StrUtil.strAppend(Integer.toHexString(urlStr.length()/2), 2, 0, "0");
			
			msgBody += urlStrLen + urlStr;
			i++;
		}
		if(bits[1] == '1'){
			//设置设备ID
			String id = data[i];
			//ID 16进制值
			String idStr = ByteUtil.ASC2ToHexStr(id);
			//ID 长度
			String idStrLen = StrUtil.strAppend(Integer.toHexString(idStr.length()/2), 2, 0, "0");

			msgBody += idStrLen + idStr;
			i++;
		}
		if(bits[2] == '1'){
			//流量额度限制-单条
			String flowLimtMac = data[i];
			String flowLimtMacStr = ByteUtil.ASC2ToHexStr(flowLimtMac);
			i++;
			//限制额
			String limtValue = data[i];
			//ascii->16进制值
			String limtValueStr = ByteUtil.ASC2ToHexStr(limtValue);
			//长度
			String limtValueStrLen = StrUtil.strAppend(Integer.toHexString(limtValueStr.length()/2), 2, 0, "0");
			msgBody += flowLimtMacStr + limtValueStrLen + limtValueStr;
			i++;
		}
		if(bits[3] == '1'){
			//设置白名单-多，以‘,’分隔
			String whiteList = data[i];
			int length = whiteList.split(",").length;
			String lengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
			String whiteListStr = ByteUtil.ASC2ToHexStr(whiteList.replaceAll(",", ""));
			msgBody += lengthStr + whiteListStr;
			i++;
		}
		if(bits[4] == '1'){
			//删除白名单,位00-全部删除 01删除单条
			String delBit = data[i];
			i++;
			msgBody += delBit;
			if(!"00".equals(delBit)){
				String delFlowList = data[i];
				msgBody += ByteUtil.ASC2ToHexStr(delFlowList);
				i++;
			}
			
		}
		if(bits[5] == '1'){
			//设置-portal 打开/关闭
			String portalBit = data[i];
			i++;
			msgBody += portalBit;
			if("01".equals(portalBit)){
				//打开portal,清除密码
				msgBody += "00";
			}else if("00".equals(portalBit)){
				//关闭portal,设置密码
				String password = data[i];
				//密码 16进制值
				String passwordStr = ByteUtil.ASC2ToHexStr(password);
				//密码 长度
				String passwordStrLen = StrUtil.strAppend(Integer.toHexString(passwordStr.length()/2), 2, 0, "0");
				msgBody += passwordStrLen + passwordStr;
				i++;
			}
			return send(obdSn,msgBody,20.0);
		}
		if(bits[6] == '1'){
			//设置SSID
			String ssid = data[i];
			//ssid 16进制值
			String ssidStr = ByteUtil.ASC2ToHexStr(ssid);
			//ssid 长度
			String ssidStrLen = StrUtil.strAppend(Integer.toHexString(ssidStr.length()/2), 2, 0, "0");

			msgBody += ssidStrLen + ssidStr;
			i++;
			return send(obdSn,msgBody,20.0);
		}
		if(bits[7] == '1'){
			//设置密码
			//data[0] i=1 
			if(i  == data.length){
				//清除密码
				msgBody += "00";
			}else{
				String password = data[i];
				//密码 16进制值
				String passwordStr = ByteUtil.ASC2ToHexStr(password);
				//密码 长度
				String passwordStrLen = StrUtil.strAppend(Integer.toHexString(passwordStr.length()/2), 2, 0, "0");
				msgBody += passwordStrLen + passwordStr;
			}
			
			i++;
			return send(obdSn,msgBody,20.0);
		}
		//8~15保留
		 
		return send(obdSn,msgBody,null);
	}
	
	
	/**************************FOTA**************************/
	/**
	 * 设置FOTA参数
	 * @param obdSn 设备号
	 * @param bits  数据帧格式 8bit
	 * @param data 
	 * @return
	 * @throws Exception
	 */
	public String fotaSet(String obdSn,FOTA fota) throws Exception{
		char[] bits = "00000000".toCharArray();
		if(fota == null){
			throw new OBDException("fota为空！设置FOTA非法！");
		}
		String fileName = fota.getFileName();
		String address = fota.getAddress();
		Integer port = fota.getPort();
		String username = fota.getUsername();
		String password = fota.getPassword();
		int i = 0;
		String msg = "";
		if(!StringUtils.isEmpty(fileName)){
			//文件名
			bits[i++] = '1';
			//fileName ascii-> 16 进制值
			String fileNameStr = ByteUtil.ASC2ToHexStr(fileName);
			//fileName 长度
			String fileNameStrLen = StrUtil.strAppend(Integer.toHexString(fileNameStr.length()/2), 2, 0, "0");
			msg += fileNameStrLen + fileNameStr;
		}
		if(!StringUtils.isEmpty(address)){
			//FTP地址
			bits[i++] = '1';
			//ftp 16进制值
			String ftpStr = ByteUtil.ASC2ToHexStr(address);
			//ftp 长度
			String ftpStrLen = StrUtil.strAppend(Integer.toHexString(ftpStr.length()/2), 2, 0, "0");
			msg += ftpStrLen + ftpStr;
		}
		if(port != null && port > 0 && port < 65535){
			//端口
			bits[i++] = '1';
			//ftp端口 16进制值
			String ftpPortStr = ByteUtil.ASC2ToHexStr(port+"");
			//ftp端口 长度
			String ftpPortLen = StrUtil.strAppend(Integer.toHexString(ftpPortStr.length()/2), 2, 0, "0");
			msg += ftpPortLen + ftpPortStr;
		}
		if(!StringUtils.isEmpty(username)){
			//用户名
			bits[i++] = '1';
			//username 16进制值
			String usernameStr = ByteUtil.ASC2ToHexStr(username);
			//username 长度
			String usernameLen = StrUtil.strAppend(Integer.toHexString(usernameStr.length()/2), 2, 0, "0");
			msg += usernameLen + usernameStr;
		}
		if(!StringUtils.isEmpty(password)){
			//密码
			bits[i++] = '1';
			//password 16进制值
			String passwordStr = ByteUtil.ASC2ToHexStr(password);
			//password 长度
			String passwordLen = StrUtil.strAppend(Integer.toHexString(passwordStr.length()/2), 2, 0, "0");
			msg += passwordLen + passwordStr;
		}
		String msgBody = ObdConstants.Server_SettingFOTA_OBD_Cmd + StrUtil.binary2Hex(bits) +  msg;
		String result = send(obdSn,msgBody,null);
		return result;
	}
	
	/**************************设备流量同步**************************/
	public String deviceFlowSync(String obdSn,Long flow) throws Exception{
		if(StringUtils.isEmpty(obdSn) || flow == null ){
			throw new OBDException("参数不合法！");
		}
		String msgBody = ObdConstants.DeviceFlowSync
				+ StrUtil.strAppend(Long.toHexString(flow), 8, 0, "0");
		
		return send(obdSn,msgBody,null);
	}
	
	/**************************域黑白名单设置**************************/
	/**
	 * 域黑白名单设置
	 * @param obdSn 设备号
	 * @param domainSetType 域设置类型
	 * @param data
	 * 域白名单开关：0-关 1-开
	 * 域黑名单开关：0-关 1-开
	 * 禁止MAC上网：ff:ff:ff:ff:ff:ff
	 * 增加域白名单：www.baidu.com;www.cctv.com
	 * 删除域白名单：全部删除为:00,部分为:www.baidu.com;www.cctv.com
	 * 增加域黑名单：www.baidu.com;www.cctv.com
	 *  删除域黑名单：全部删除为:00,部分为:www.baidu.com;www.cctv.com
	 * @return
	 * @throws Exception
	 */
	public String domainBlackWhiteListSet(String obdSn, DomainSetType domainSetType, String data) throws Exception{
		char[] bits = domainSetType.getValue().replaceAll(" ", "").toCharArray();
		return domainBlackWhiteListSet(obdSn,bits,data);
	}
	/**
	 * 域黑白名单设置：只能执行一种操作
	 * @param obdSn 设备号
	 * @param bits 
	 * @param data
	 * 1000 0000 0000 0000 域白名单开关：0-关 1-开
	 * 0100 0000 0000 0000 域黑名单开关：0-关 1-开
	 * 0010 0000 0000 0000 禁止MAC上网：ff:ff:ff:ff:ff:ff
	 * 0001 0000 0000 0000 增加域白名单：www.baidu.com;www.cctv.com
	 * 0000 1000 0000 0000 删除域白名单：全部删除为:00,部分为:www.baidu.com;www.cctv.com
	 * 0000 0100 0000 0000 增加域黑名单：www.baidu.com;www.cctv.com
	 * 0000 0010 0000 0000 删除域黑名单：全部删除为:00,部分为:www.baidu.com;www.cctv.com
	 * @return
	 * @throws Exception
	 */
	private String domainBlackWhiteListSet(String obdSn,char[]bits, String data) throws Exception{
		if(bits == null || bits.length != 16){
			throw new OBDException("设备的位bits无效！bits:"+String.valueOf(bits));
		}
		validateBinary(bits);
		boolean isOnlyOne = String.valueOf(bits).split("1").length-1 == 1 ? true : false;//是否只设置一项
		if(!isOnlyOne){
			throw new OBDException("设备的位bits无效！只能设置一项！bits:"+String.valueOf(bits));
		}
		if(StringUtils.isEmpty(data)){
			throw new OBDException("设置的数据不能空:"+data);
		}
		char[] bitss = new char[bits.length];//高位在前
		bitss = Arrays.copyOfRange(bits, 0, bits.length);
		String msgBody = ObdConstants.DomainBlackWhiteListSetting + StrUtil.binary2Hex(bitss);
		if(bits[0] == '1'){
			//域白名单功能开关
			if("0".equals(data)){
				//关
				msgBody += "00";
			}else if("1".equals(data)){
				//开
				msgBody += "01";
			}else{
				throw new OBDException("设置域白名单功能开关，对应开关不正确："+data);
			}
		}
		if(bits[1] == '1'){
			//域黑名单功能开关
			if("0".equals(data)){
				//关
				msgBody += "00";
			}else if("1".equals(data)){
				//开
				msgBody += "01";
			}else{
				throw new OBDException("设置域黑名单功能开关，对应开关不正确："+data);
			}
		}
		if(bits[2] == '1'){
			//禁止MAC上网
			if(data == null || data.length() != 17){
				throw new OBDException("设置禁止MAC上网，长度不正确，并且只能设置一个："+data);
			}
			msgBody += ByteUtil.ASC2ToHexStr(data);
		}
		if(bits[3] == '1' || bits[5] == '1'){
			//增加域白/黑名单,';'分隔
			String[] lists = data.split(";");
			int length = lists.length;
			String whiteLengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
			for (String list : lists) {
				if(list == null || list.length() != 17){
					if(bits[3] == '1'){
						throw new OBDException("设置增加域白名单，"+data+"中，长度不正确："+list);
					}
					if(bits[5] == '1'){
						throw new OBDException("设置增加域黑名单，"+data+"中，长度不正确："+list);
					}
				}
			}
			String whiteListStr = ByteUtil.ASC2ToHexStr(data.replaceAll(";", ""));
			msgBody += whiteLengthStr + whiteListStr;
		}
		if(bits[4] == '1' || bits[6] == '1'){
			//删除域白名单 //删除域黑名单
			int length = 0;
			String lengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
			if("00".equals(data)){
				//删除全部
				msgBody += lengthStr;
			}else{
				//删除部分
				String[] lists = data.split(";");
				length = lists.length;
				lengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
				for (String list : lists) {
					if(list == null || list.length() != 17){
						if(bits[4] == '1'){
							throw new OBDException("设置删除域白名单，"+data+"中，长度不正确："+list);
						}
						if(bits[6] == '1'){
							throw new OBDException("设置删除域黑名单，"+data+"中，长度不正确："+list);
						}
					}
				}
				String whiteListStr = ByteUtil.ASC2ToHexStr(data.replaceAll(";", ""));
				msgBody += lengthStr + whiteListStr;
			}
		
		}
		return send(obdSn, msgBody);
	}
	
	/**************************扩展数据设置**************************/
	/**
	 * 扩展数据设置下发
	 * @param obdSn
	 * @param extensionDataSetTypes
	 * 休眠电压： 电压差值-01
	 * 休眠加速度：加速度差值-0001
	 * 	域白名单开关：0-关 1-开
	 * 域黑名单开关：0-关 1-开
	 * 禁止MAC上网：ff:ff:ff:ff:ff:ff
	 * 增加域白名单：www.baidu.com;www.cctv.com
	 * 删除域白名单：全部删除为:00,部分为:www.baidu.com;www.cctv.com
	 * 增加域黑名单：www.baidu.com;www.cctv.com
	 *  删除域黑名单：全部删除为:00,部分为:www.baidu.com;www.cctv.com
	 * @return
	 * @throws OBDException
	 */
	public String extensionDataSetting(String obdSn, Map<ExtensionDataSetType,String> extensionDataSetTypes) throws OBDException{
		String msgBody = ObdConstants.ExtensionDataSetting;
		Integer paramSize = extensionDataSetTypes.size();
		msgBody += StrUtil.strAppendByLen(Integer.toHexString(paramSize),1,"0");//参数总数
		for (ExtensionDataSetType extensionDataSetType : extensionDataSetTypes.keySet()) {
			String param = extensionDataSetType.getValue();
			msgBody += param;
			String data = extensionDataSetTypes.get(extensionDataSetType);
			if(StringUtils.isEmpty(data)){
				throw new OBDException("参数值不能为空，请检查！data->"+data);
			}
			switch (extensionDataSetType) {
			case SleepVolt://休眠电压
				if(data.length() != 1*2){//1byte
					throw new OBDException("参数值不正确，请检查！data->"+data);
				}
				msgBody += data;
				break;
				
			case SleepOverSpeed://休眠加速度
				if(data.length() != 2*2){//2byte
					throw new OBDException("参数值不正确，请检查！data->"+data);
				}
				msgBody += data;
				break;
				
			case DomainWhiteSwitch://域白名单开关
				if("0".equals(data)){
					//关
					msgBody += "00";
				}else if("1".equals(data)){
					//开
					msgBody += "01";
				}else{
					throw new OBDException("设置域白名单功能开关，对应开关不正确："+data);
				}
				break;
			 
			case DomainBlackSwitch://域黑名单开关
				if("0".equals(data)){
					//关
					msgBody += "00";
				}else if("1".equals(data)){
					//开
					msgBody += "01";
				}else{
					throw new OBDException("设置域黑名单功能开关，对应开关不正确："+data);
				}
				break;
				
			case DomainForbidMAC://禁止MAC上网
				//禁止MAC上网
				if(data == null || data.length() != 17){
					throw new OBDException("设置禁止MAC上网，长度不正确，并且只能设置一个："+data);
				}
				msgBody += ByteUtil.ASC2ToHexStr(data);
				break;
				
			case DomainAddWhite://增加域白名单
				msgBody += generateDomainData(data, false);
				break;
				
			case DomainDelWhite://删除域白名单
				int length = 0;
				String lengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
				if("00".equals(data)){
					//删除全部
					msgBody += lengthStr;
				}else{
					msgBody += generateDomainData(data, true);
				}
				break;
				
			case DomainAddBlack://增加域黑名单
				msgBody += generateDomainData(data, false);
				break;
				
			case DomainDelBlack://删除域黑名单
				int lengthBlack = 0;
				String lengthBlackStr = StrUtil.strAppend(Integer.toHexString(lengthBlack), 2, 0, "0");
				if("00".equals(data)){
					//删除全部
					msgBody += lengthBlackStr;
				}else{
					msgBody += generateDomainData(data, true);
				}
				break;
				
			case CarTypeSetting://车型功能设置
				int settingLength = data.length()/2;
				String settingLengthStr = StrUtil.strAppend(Integer.toHexString(settingLength), 2, 0, "0");
				msgBody += settingLengthStr+data;
				break;
				
			case LowVoltSleepValue:
				msgBody += data;
				break;
				
			case EEPROMFLASH:
				msgBody += data;
				break;
				
			default:
				throw new OBDException("没有对应扩展数据类型，请检查！extensionDataSetType->"+extensionDataSetType);
			}
		}
		return send(obdSn,msgBody,null);
	}
	
	public String privateProtocolMsg(String obdSn,String msgBody){
		return send(obdSn,msgBody);
	}
	
	/**
	 * 生成Domain数据 16进制 长度+数据
	 * @param data
	 * @return
	 * @throws OBDException
	 */
	private String generateDomainData(String data,boolean isDel) throws OBDException{
			String[] lists = data.split(";");
			int count = lists.length;
			String countStr = "";
			String macStr = "";
			if(!isDel){
				countStr = StrUtil.strAppend(Integer.toHexString(count), 2, 0, "0");
			}else{
				//只能删除一个
				if(lists.length != 1){
					throw new OBDException("只能删除一个！");
				}
			}
			for (String list : lists) {
				macStr += StrUtil.strAppend(Integer.toHexString(list.length()), 2, 0, "0") + ByteUtil.ASC2ToHexStr(list);
			}
			
		return countStr + macStr;
	}
		
	/**
	 * 验证二进制数组是否合法
	 * @param bits
	 * @throws OBDException
	 */
	private void validateBinary(char[] bits) throws OBDException{
		for (char bit : bits) {
			if(bit != '0' && bit != '1'){
				throw new OBDException("非法二进制值!");
			}
		}
	}
	
	/**
	 * 发送报文到OBD设备
	 * @param obdSn
	 * @param msgBody
	 * @return
	 */
	private String send(String obdSn,String msgBody,Double waitSeconds){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			serverSendObdLogger.info("--------------【服务器下发设置】报文(消息体)："+msgBody);
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum,	msgBody,waitSeconds);
			serverSendObdLogger.info("--------------【服务器下发设置】返回结果："+obj);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String send(String obdSn,String msgBody){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			serverSendObdLogger.info("--------------【服务器下发设置】报文(消息体)："+msgBody);
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum,	msgBody,null);
			serverSendObdLogger.info("--------------【服务器下发设置】返回结果："+obj);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
}
