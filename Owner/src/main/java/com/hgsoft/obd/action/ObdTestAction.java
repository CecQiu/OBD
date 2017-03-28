package com.hgsoft.obd.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.carowner.entity.OBDServerParams;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.entity.ObdTestSendPacket;
import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.carowner.service.GpsStateSetService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.carowner.service.ObdTestSendPacketService;
import com.hgsoft.carowner.service.ObdTravelParamsService;
import com.hgsoft.carowner.service.SimStockInfoService;
import com.hgsoft.carowner.service.WifiStateSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.service.AGPSService;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.service.UpgradeService;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.ExtensionDataEnum;
import com.hgsoft.obd.util.ExtensionDataQueryType;
import com.hgsoft.obd.util.ExtensionDataSetType;
import com.hgsoft.obd.util.ServerParamsType;
import com.hgsoft.obd.util.ServerRequestQueryUtil;
import com.hgsoft.obd.util.ServerSettingUtil;
import com.hgsoft.obd.util.SettingType;
import com.hgsoft.obd.util.UpgradeType;

/**
 * 测试OBD
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class ObdTestAction extends BaseAction {
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ServerRequestQueryService requestQueryService;
	@Resource
	private AGPSService agpsService;
	@Resource
	private UpgradeService upgradeService;
	@Resource
	private GpsStateSetService gpsStateSetService;
	@Resource
	private WifiStateSetService wifiStateSetService;// wifi设置
	@Resource
	private ObdTravelParamsService obdTravelParamsService;
	@Resource
	private ObdSettingService obdSettingService;
	@Resource
	private SimStockInfoService simStockInfoService;
	@Resource
	private DriveTimeUtil driveTimeUtil;
	@Resource
	private ObdTestSendPacketService obdTestSendPacketService;
	@Resource
	private ServerSettingUtil serverSettingUtil;
	@Resource
	private ServerRequestQueryUtil serverRequestQueryUtil;
	
	private String obdSn;
	private String state;
	private String result;
	private String typeName;
	private OBDTimeParams obdTimeParams;//obd时间参数
	
	private OBDTravelParams obdTravelParams;//行程参数设置
	private String url;//设置url
	private String id;//设置ID
	private String mac;//流量限制Mac
	private String macs;//白名单设置
	private String pwd;//设置portal密码
	private String ssid;//设置ssid
	private String positionChar;//对应为
	
	//数据服务器
	private String ip;
	private String port;
	private String APN;
	private String warnSet;//报警设置
	private Map mapACKORQueryData = GlobalData.OBD_ACK_OR_QueryData;
	private Map mapOBDChannel = GlobalData.OBD_CHANNEL;
	private Map mapOBD_Packet = GlobalData.OBD_Packet;
	private Map mapOBD_PacketNum = GlobalData.OBD_PacketNum;
	
	private String deviceId;
	private String username;
	
	//驾驶行为参数设置
	private String quickenSpeed;//急加速,速度阈值单位km/s
	private String quickSlowDownSpeed;//急减速,速度阈值单位km/s
	private String quickturnSpeed;//急转弯,速度阈值(类型2)，单位km/s
	private String quickturnAngle;//急转弯，角度阈值
	private String overspeed;//5-超速,速度阈值单位km/s
	private String overspeedTime;//5-超速, 时间阈值秒
	private String fatigueDrive;//疲劳驾驶连续驾驶超过阈值（类型6）单位小时
	private String fatigueSleep;//疲劳驾驶休息时间阈值（类型6）单位分

	//扩展2数据参数
	private String sleepVolt;
	private String sleepVoltValue;
	private String sleepOverSpeed;
	private String sleepOverSpeedValue;
	
	private Map<String,String> map; 
	
	
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String getSleepVolt() {
		return sleepVolt;
	}
	public void setSleepVolt(String sleepVolt) {
		this.sleepVolt = sleepVolt;
	}
	public String getSleepVoltValue() {
		return sleepVoltValue;
	}
	public void setSleepVoltValue(String sleepVoltValue) {
		this.sleepVoltValue = sleepVoltValue;
	}
	public String getSleepOverSpeed() {
		return sleepOverSpeed;
	}
	public void setSleepOverSpeed(String sleepOverSpeed) {
		this.sleepOverSpeed = sleepOverSpeed;
	}
	public String getSleepOverSpeedValue() {
		return sleepOverSpeedValue;
	}
	public void setSleepOverSpeedValue(String sleepOverSpeedValue) {
		this.sleepOverSpeedValue = sleepOverSpeedValue;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Map getMapOBD_Packet() {
		return mapOBD_Packet;
	}
	public void setMapOBD_Packet(Map mapOBD_Packet) {
		this.mapOBD_Packet = mapOBD_Packet;
	}
	public Map getMapOBD_PacketNum() {
		return mapOBD_PacketNum;
	}
	
	public String getQuickenSpeed() {
		return quickenSpeed;
	}
	public void setQuickenSpeed(String quickenSpeed) {
		this.quickenSpeed = quickenSpeed;
	}
	public String getQuickSlowDownSpeed() {
		return quickSlowDownSpeed;
	}
	public void setQuickSlowDownSpeed(String quickSlowDownSpeed) {
		this.quickSlowDownSpeed = quickSlowDownSpeed;
	}
	public String getQuickturnSpeed() {
		return quickturnSpeed;
	}
	public void setQuickturnSpeed(String quickturnSpeed) {
		this.quickturnSpeed = quickturnSpeed;
	}
	public String getQuickturnAngle() {
		return quickturnAngle;
	}
	public void setQuickturnAngle(String quickturnAngle) {
		this.quickturnAngle = quickturnAngle;
	}
	public String getOverspeed() {
		return overspeed;
	}
	public void setOverspeed(String overspeed) {
		this.overspeed = overspeed;
	}
	public String getOverspeedTime() {
		return overspeedTime;
	}
	public void setOverspeedTime(String overspeedTime) {
		this.overspeedTime = overspeedTime;
	}
	public String getFatigueDrive() {
		return fatigueDrive;
	}
	public void setFatigueDrive(String fatigueDrive) {
		this.fatigueDrive = fatigueDrive;
	}
	public String getFatigueSleep() {
		return fatigueSleep;
	}
	public void setFatigueSleep(String fatigueSleep) {
		this.fatigueSleep = fatigueSleep;
	}
	public void setMapOBD_PacketNum(Map mapOBD_PacketNum) {
		this.mapOBD_PacketNum = mapOBD_PacketNum;
	}
	public Map getMapACKORQueryData() {
		return mapACKORQueryData;
	}
	public void setMapACKORQueryData(Map mapACKORQueryData) {
		this.mapACKORQueryData = mapACKORQueryData;
	}
	public Map getMapOBDChannel() {
		return mapOBDChannel;
	}
	public void setMapOBDChannel(Map mapOBDChannel) {
		this.mapOBDChannel = mapOBDChannel;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String test(String msgBody,String sendResult){
		if(!StringUtils.isEmpty(obdSn)){
			boolean isOnLine = true;
			if(GlobalData.getChannelByObdSn(obdSn) == null){
				isOnLine = false;
				result="<font color=red size=12>设备离线！不能执行操作。。。</font>";
			}
			try {
				updateObdTestSendPacket( obdSn, msgBody, sendResult, isOnLine);
			} catch (OBDException e) {
				result="<font color=red size=12>操作错误！"+e.getMessage()+"</font>";
			}
		}
		return "test";
	}
	public String test(){
		return "test";
	}
	
	private void updateObdTestSendPacket(String obdSn, String msgBody, String result,boolean isOnLine) throws OBDException{
		if(!StringUtils.isEmpty(msgBody) && msgBody.length() % 2 != 0){
			throw new OBDException("消息体字符数不能为奇数！");
		}
		if(operator == null){
			return;
		}
		ObdTestSendPacket obdTestSendPacket = new ObdTestSendPacket();
		ObdTestSendPacket oldObdTestSendPacket = obdTestSendPacketService.queryByObdSnAndMsgBody(obdSn, msgBody);
		if(oldObdTestSendPacket != null){
			oldObdTestSendPacket.setSended(-1);
			oldObdTestSendPacket.setUpdateTime(new Date());
			obdTestSendPacketService.sendPacketUpdate(oldObdTestSendPacket);
		}
		if(!StringUtils.isEmpty(msgBody)){
			msgBody = msgBody.replaceAll(" ", "");
			obdTestSendPacket.setObdSn(obdSn);
			obdTestSendPacket.setMsgBody(msgBody);
			obdTestSendPacket.setCreateTime(new Date());
			obdTestSendPacket.setId(IDUtil.createID());
			obdTestSendPacket.setTypeStr(typeName);
			obdTestSendPacket.setResult(result);
		}
		obdTestSendPacket.setSended(0);
		if(!StringUtils.isEmpty(result)){
			obdTestSendPacket.setSended(1);
		}
		obdTestSendPacket.setSendCount(0);
		if(isOnLine){
			obdTestSendPacket.setSendCount(1);
		}
		if(operator != null){
			obdTestSendPacket.setOperator(operator.getUsername());
		}
		obdTestSendPacketService.sendPacketSave(obdTestSendPacket);
	}

	public String test1(){
		return "test1";
	}

	public String testQuery(String msgBody, String sendResult){
		if(!StringUtils.isEmpty(obdSn)){
			boolean isOnLine = true;
			if(GlobalData.getChannelByObdSn(obdSn) == null){
				isOnLine = false;
				result="<font color=red size=12>设备离线！不能执行操作。。。</font>";
			}
			try {
				updateObdTestSendPacket( obdSn, msgBody, sendResult, isOnLine);
			} catch (OBDException e) {
				result="<font color=red size=12>操作错误！"+e.getMessage()+"</font>";
			}
		}
		return "testQuery";
	}
	
	public String testQuery(){
		return "testQuery";
	}
	
	public OBDTimeParams getObdTimeParams() {
		return obdTimeParams;
	}
	public void setObdTimeParams(OBDTimeParams obdTimeParams) {
		this.obdTimeParams = obdTimeParams;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAPN() {
		return APN;
	}
	public void setAPN(String aPN) {
		APN = aPN;
	}
	
	public String getWarnSet() {
		return warnSet;
	}
	public void setWarnSet(String warnSet) {
		this.warnSet = warnSet;
	}
	public OBDTravelParams getObdTravelParams() {
		return obdTravelParams;
	}
	public void setObdTravelParams(OBDTravelParams obdTravelParams) {
		this.obdTravelParams = obdTravelParams;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getMacs() {
		return macs;
	}
	public void setMacs(String macs) {
		this.macs = macs;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getPositionChar() {
		return positionChar;
	}
	public void setPositionChar(String positionChar) {
		this.positionChar = positionChar;
	}
	/*********************设置********************/
	public String testGPS(){
		try {
			result = serverSettingService.gps(obdSn, state);
//			boolean flag = GlobalData.isSendResultSuccess(result);
//			//保存设置记录
//			GpsSet gpsSet = new GpsSet();
//			gpsSet.setObdSn(obdSn);
//			gpsSet.setGpsState(state);// GPS开关状态
//			gpsSet.setCreateTime(new Date());// 创建时间
//			if(flag){
//				gpsSet.setValid("0");
//			}else{
//				gpsSet.setValid("1");
//			}
//			gpsStateSetService.save(gpsSet);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.gps(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testWiFi(){
		try {
			result = serverSettingService.wifi(obdSn, state);
//			WifiSet wifiSet = new WifiSet();
//			wifiSet.setObdSn(obdSn);
//			wifiSet.setCreateTime(new Date());
//			wifiSet.setType("4");// wifi开关
//			wifiSet.setWifiState(state);// wifi开关状态;
//			boolean flag = GlobalData.isSendResultSuccess(result);
//			if(flag){
//				wifiSet.setValid("0");
//			}else{
//				wifiSet.setValid("1");
//			}
//			wifiStateSetService.save(wifiSet);// 保存wifiset记录
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.wifi(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testGuard(){
		try {
			result = serverSettingService.carGuard(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.carGuard(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testShock(){
		try {
			result = serverSettingService.carShock(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.carShock(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testClearFaultCode(){
		try {
			result = serverSettingService.clearFaultCode(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.clearFaultCode(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testGPSFormat(){
		try {
			result = serverSettingService.gpsFormat(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.gpsFormat(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	public String testOffHeart(){
		try {
			result = serverSettingService.offHeart(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.offHeart(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	public String testOverSpeedWarn(){
		try {
			result = serverSettingService.overSpeedWarn(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.overSpeedWarn(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	public String testReset(){
		try {
			result = serverSettingService.reset(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.reset(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testRestart(){
		try {
			result = serverSettingService.restart(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.restart(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testDeviceUpgradeSet(){
		try {
			if(!StringUtils.isEmpty(upgradeType)){
				if("app".equals(upgradeType)){
					result = serverSettingService.deviceUpgradeSet(obdSn, state, UpgradeType.APP, msg);
				}
				if("iap".equals(upgradeType)){
					result = serverSettingService.deviceUpgradeSet(obdSn, state, UpgradeType.IAP, msg);
				}
			}
			
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			if(!StringUtils.isEmpty(upgradeType)){
				if("app".equals(upgradeType)){
					msgBody = serverSettingUtil.deviceUpgradeSet(obdSn, state, UpgradeType.APP, msg);
				}
				if("iap".equals(upgradeType)){
					msgBody = serverSettingUtil.deviceUpgradeSet(obdSn, state, UpgradeType.IAP, msg);
				}
			}				
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 报警设置
	 */
	public String testWarnSet(){
//		String warnSet = "10010010";
		try {
			result = serverSettingService.warnSet(obdSn, warnSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.warnSet(obdSn, warnSet);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 设备时间设置
	 */
	public String testDeviceTime(){
		System.out.println(obdTimeParams.getObdSn());
//		OBDTimeParams obdTimeParams = new OBDTimeParams(51, 1, 4, 27,26, 170, 59, 180);
		try {
			result = serverSettingService.deviceTimeSet(obdSn,positionChar.toCharArray(), obdTimeParams);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.deviceTimeSet(obdSn,positionChar.toCharArray(), obdTimeParams);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 行程参数设置
	 * @return
	 */
	public String testTravelParamsSet(){
//		OBDTravelParams obdTravelParams = new OBDTravelParams(
//				114, 122, 18, 19,20, 21, 22,
//				23, 24, 25, 26, 27, 28, 29, 6943,
//				8226, 4659, 4, 1, 2, 19, 3, 4, 
//				"136°04.000'", "33°32.1000'", "136°14.101'", "34°33.0101'");
		
		try {
			result = serverSettingService.travelParamsSet(obdSn, positionChar.toCharArray(), obdTravelParams);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.travelParamsSet(obdSn, positionChar.toCharArray(), obdTravelParams);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	/**
	 * 数据服务器参数设置
	 */
	public String testDataServerParamsSet(){
//		String ip = "161.162.163.164";
//		String port = "8888";
//		String APN = "a123b";
		try {
			result = serverSettingService.dataServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.dataServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 升级服务器参数设置
	 */
	public String testUpgradeServerParamsSet(){
//		String ip = "17.18.19.20";
//		String port = "9999";
//		String APN = "a123c";
		try {
			result = serverSettingService.upgradeServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.upgradeServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * portal服务器参数设置
	 */
	public String testPortalServerParamsSet(){
//		String ip = "49.50.51.52";
//		String port = "7777";
//		String APN = "a123d";
		try {
			result = serverSettingService.portalServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	//设置url
	public String testPortalORWifiSet0(){
//		String url = "http://www.baidu.com/";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "1000 0000 0000 0000".replaceAll(" ", "").toCharArray(), url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "1000 0000 0000 0000".replaceAll(" ", "").toCharArray(), url);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	//设置ID
	public String testPortalORWifiSet1(){
//		String id = "2f000011";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0100 0000 0000 0000".replaceAll(" ", "").toCharArray(), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0100 0000 0000 0000".replaceAll(" ", "").toCharArray(), id);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	//流量限制Mac
	public String testPortalORWifiSet2(){
//		String mac = "ff:fa:fb:fc:fd:fe";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0010 0000 0000 0000".replaceAll(" ", "").toCharArray(), mac,"50");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0010 0000 0000 0000".replaceAll(" ", "").toCharArray(), mac,"50");
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	//设置白名单
	public String testPortalORWifiSet3(){
//		String macs = "ff:fa:fb:fc:fd:fe,11:22:33:44:55:66";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0001 0000 0000 0000".replaceAll(" ", "").toCharArray(), macs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0001 0000 0000 0000".replaceAll(" ", "").toCharArray(), macs);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	//删除全部白名单
	public String testPortalORWifiSet4(){
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 1000 0000 0000".replaceAll(" ", "").toCharArray(), "00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0000 1000 0000 0000".replaceAll(" ", "").toCharArray(), "00");
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	//删除白名单-单条
	public String testPortalORWifiSet4_1(){
//		String mac = "ff:fa:fb:fc:fd:fe";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 1000 0000 0000".replaceAll(" ", "").toCharArray(), "01",mac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0000 1000 0000 0000".replaceAll(" ", "").toCharArray(), "01",mac);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	//打开portal
	public String testPortalORWifiSet5(){
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0100 0000 0000".replaceAll(" ", "").toCharArray(), "01");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0000 0100 0000 0000".replaceAll(" ", "").toCharArray(), "01");
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}

	//关闭portal
	public String testPortalORWifiSet5_1(){
//		String pwd = "12345678";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0100 0000 0000".replaceAll(" ", "").toCharArray(), "00",pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0000 0100 0000 0000".replaceAll(" ", "").toCharArray(), "00",pwd);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
		
	//设置SSID
	public String testPortalORWifiSet6(){
//		String ssid = "123321";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0010 0000 0000".replaceAll(" ", "").toCharArray(), ssid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0000 0010 0000 0000".replaceAll(" ", "").toCharArray(), ssid);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
		
	//设置密码
	public String testPortalORWifiSet7(){
//		String pwd = "123#qax,;";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0001 0000 0000".replaceAll(" ", "").toCharArray(), pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.portalOrWifiSet(obdSn, "0000 0001 0000 0000".replaceAll(" ", "").toCharArray(), pwd);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 清除流量统计数值
	 * @return
	 */
	public String testCleanFlowStat(){
		try {
			result = serverSettingService.cleanFlowStat(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.cleanFlowStat(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 关闭设备
	 * @return
	 */
	public String testCloseDevice(){
		try {
			result = serverSettingService.closeDevice(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.closeDevice(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * FOTA升级设置
	 * @return
	 */
	public String testFOTAUpgradeSet(){
		try {
			result = serverSettingService.fotaUpgradeSet(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.fotaUpgradeSet(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * wifi流量开关设置
	 * @return
	 */
	public String testWiFiFlowSet(){
		try {
			result = serverSettingService.wifiFlowSet(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.wifiFlowSet(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	/**
	 * 驾驶行为设置
	 * @return
	 * @throws Exception 
	 */
	public String testDriveSet() throws Exception{
		//1.更新对应驾驶行为 参数表
		OBDTravelParams op = new OBDTravelParams();
		//2.更新obd设置表
		String bitStr="1111111111111111";
		try {
			if(StringUtils.isEmpty(obdSn)){
				return test(null,null);
			}
			
			op.setId(IDUtil.createID());
			op.setObdSn(obdSn);
			op.setCreateTime(new Date());
			
			JSONArray jms = new JSONArray();  
			if(!StringUtils.isEmpty(quickenSpeed)){
				//急加速,速度阈值单位km/s
				//急加速, 时间阈值秒
				Integer qk1 = Integer.parseInt(quickenSpeed);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_00.getValue(), "quickenSpeed", quickenSpeed,null, null);
				op.setShuddenOverSpeed(qk1);// 急加速阈值：速度变化阈值km/h
				op.setShuddenOverSpeedTime(2);
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 3, 4, "0");
			}
			
			if(!StringUtils.isEmpty(quickSlowDownSpeed)){
				//急减速,速度阈值单位km/s
				//急减速, 时间阈值秒
				Integer qsd1 = Integer.parseInt(quickSlowDownSpeed);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_01.getValue(), "quickSlowDownSpeed", quickSlowDownSpeed, null, null);
				op.setShuddenLowSpeed(qsd1);//速度变化阈值km/h
				op.setShuddenLowSpeedTime(2);// 急减速阈值：	时间阈值
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 4, 5, "0");
			}
			if(!StringUtils.isEmpty(quickturnSpeed) && !StringUtils.isEmpty(quickturnAngle)){
				//急转弯,速度阈值(类型2)，单位km/s
				// 急转弯阈值：角度阈值 度
				Integer qt1 = Integer.parseInt(quickturnSpeed);
				Integer qt2 = Integer.parseInt(quickturnAngle);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_02.getValue(), "quickturnSpeed", quickturnSpeed, "quickturnAngle", quickturnAngle);
				op.setShuddenTurnSpeed(qt1);// 急转弯阈值：速度阈值 km/h
				op.setShuddenTurnAngle(qt2);
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 2, 3, "0");
			}
			
			if(!StringUtils.isEmpty(overspeed) && !StringUtils.isEmpty(overspeedTime)){
				//超速,速度阈值单位km/s
				Integer os1 = Integer.parseInt(overspeed);
				Integer os2 = Integer.parseInt(overspeedTime);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_03.getValue(), "overspeed", overspeed, "overspeedTime", overspeedTime);
				op.setOverSpeed(os1);// 超速阈值：时速阈值km/h
				op.setLimitSpeedLazy(os2);// 超速阈值：限速延迟时间阈值s
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 1, 2, "0");
			}
			if(!StringUtils.isEmpty(fatigueDrive) && !StringUtils.isEmpty(fatigueSleep)){
				//疲劳驾驶连续驾驶超过阈值（类型6）单位小时
				//疲劳驾驶休息时间阈值（类型6）单位分
				jsonArrayAdd(jms, "type", SettingType.DRIVE_04.getValue(), "fatigueDrive", fatigueDrive, "fatigueSleep", fatigueSleep);
				//疲劳驾驶无
				op.setFatigueDrive(Integer.parseInt(fatigueDrive));
				op.setFatigueSleep(Integer.parseInt(fatigueSleep));
				driveTimeUtil.resetParamsClean(obdSn);
			}
			//更新驾驶行为记录表
			//下发指令给obd
			String rt=serverSettingService.travelParamsSet(obdSn, bitStr.toCharArray(), op);
			result = rt;
			boolean flag = GlobalData.isSendResultSuccess(rt);
			//保存到obd设置表里
			for (int i=0;i<jms.size();i++) {
				JSONObject jobj = jms.getJSONObject(i);
				ObdSetting obdSetting = new ObdSetting();
				obdSetting.setId(IDUtil.createID());
				obdSetting.setObdSn(obdSn);
				obdSetting.setCreateTime(new Date());
				obdSetting.setType(jobj.getString("type"));//20obd驾驶行为参数设置
				jobj.remove("type");//去掉type类型
				obdSetting.setSettingMsg(jobj.toString());
				if(flag){
					obdSetting.setValid("0");
				}else{
					obdSetting.setValid("1");
				}
				obdSettingService.obdSettingNoValid(obdSn, obdSetting.getType());
				//调用接口下发设置
//				obdSettingService.save(obdSetting);	 //暂不操作
			}
			//疲劳驾驶入库
			if(!StringUtils.isEmpty(fatigueDrive) && !StringUtils.isEmpty(fatigueSleep)){
				OBDTravelParams op2 =  obdTravelParamsService.queryByObdSn(obdSn);
				if(op2==null){
					op2 = new OBDTravelParams();
					op2.setId(IDUtil.createID());
					op2.setObdSn(obdSn);
					op2.setCreateTime(new Date());
				}
				op2.setFatigueDrive(Integer.parseInt(fatigueDrive));
				op2.setFatigueSleep(Integer.parseInt(fatigueSleep));
//				obdTravelParamsService.saveOrUpdate(op2); //暂不操作
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.travelParamsSet(obdSn, bitStr.toCharArray(), op);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testExtension2Set(){
		TreeMap params = new TreeMap<ExtensionDataSetType,String>();
		params.put(ExtensionDataSetType.getDomainSetTypeByValue(sleepVolt), sleepVoltValue);
		params.put(ExtensionDataSetType.getDomainSetTypeByValue(sleepOverSpeed), sleepOverSpeedValue);
		try {
			result = serverSettingService.extensionDataSetting(obdSn, params);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.extensionDataSetting(obdSn, params);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	public String testExtensionSet(){
		TreeMap params = new TreeMap<ExtensionDataSetType,String>();
		for ( String key : map.keySet()) {
			params.put(ExtensionDataSetType.getDomainSetTypeByValue(key), map.get(key));
		}
		try {
			result = serverSettingService.extensionDataSetting(obdSn, params);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverSettingUtil.extensionDataSetting(obdSn, params);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test(msgBody,result);
	}
	
	
	/*********************查询********************/
	public String testReadFaultCode(){
		List<FaultUpload> faultUploads = requestQueryService.readFaultCode(obdSn);
		result = (faultUploads != null ? faultUploads.toString() : null);
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.readFaultCode(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.FaultCode;
		return testQuery(msgBody, result);
	}
	public String testCarRunState(){
//		result = requestQueryService.carRunState(obdSn);
		return testQuery(null,null);
	}
	public String testCarState(){
		ObdHandShake obdHandShake = requestQueryService.carState(obdSn);
		result = (obdHandShake != null ? obdHandShake.toString() : "");
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.carState(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.CarState;
		return testQuery(msgBody, result);
	}
	public String testDataServerParams(){
		try {
			OBDServerParams params = requestQueryService.serverParams(obdSn,ServerParamsType.DATA);
			result = (params != null?params.toString():"");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.serverParams(obdSn,ServerParamsType.DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.DataServerParams;
		return testQuery(msgBody, result);
	}
	public String testDeviceState(){
		ObdHandShake obdHandShake = requestQueryService.deviceState(obdSn);
		result = (obdHandShake != null ? obdHandShake.toString() : "");
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.deviceState(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.DeviceState;
		return testQuery(msgBody, result);
	}
	public String testDeviceTimeParams(){
		OBDTimeParams obdTimeParams = requestQueryService.deviceTimeParams(obdSn,"00000000".toCharArray());
		result = obdTimeParams != null ? obdTimeParams.toString():"";
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.deviceTimeParams(obdSn,"00000000".toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.DeviceTimeParams;
		return testQuery(msgBody, result);
	}
	public String testDeviceVersion(){
		OBDDeviceVersion deviceVersion = requestQueryService.deviceVersion(obdSn);
		result = deviceVersion != null ?deviceVersion.toString():"";
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.deviceVersion(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ObdDeviceVersion;
		return testQuery(msgBody, result);
	}
	public String testOffData(){
		result = requestQueryService.offData(obdSn,null, true);
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.offData(obdSn,null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testQuery(msgBody, result);
	}
	public String testOffTravel(){
		result = requestQueryService.offTravel(obdSn,null, true);
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.offTravel(obdSn,null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testQuery(msgBody, result);
	}
	public String testRealTimeLoc(){
		result = requestQueryService.realTimeLoc(obdSn);
//		result = (positionInfo != null ? positionInfo.toString():null);
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.realTimeLoc(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.RealTimeLoc;
		return testQuery(msgBody, result);	
	}
	public String testTotalMiles(){
		String total = requestQueryService.halfTravel(obdSn);
		result = (total != null ?total.toString():"");
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.halfTravel(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.HalfTravel;
		return testQuery(msgBody, result);
	}
	public String testUpgradeServerParams(){
		try {
			OBDServerParams params = requestQueryService.serverParams(obdSn,ServerParamsType.UPGRADE);
			result = (params != null?params.toString():"");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.serverParams(obdSn,ServerParamsType.UPGRADE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.UpgradeServerParams;
		return testQuery(msgBody, result);
	}
	public String testTravelTimeParams(){
		OBDTravelParams obdTravelParams = requestQueryService.travelTimeParams(obdSn,"0000000000000000".toCharArray());
		result = obdTravelParams != null ? obdTravelParams.toString() : "";
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.travelTimeParams(obdSn,"0000000000000000".toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.TravelParams;
		return testQuery(msgBody, result);
	}
	public String testPortalServerParams(){
		try {
			OBDServerParams params = requestQueryService.serverParams(obdSn,ServerParamsType.PORTAL);
			result = (params != null?params.toString():"");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.serverParams(obdSn,ServerParamsType.PORTAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testQuery(msgBody, result);
	}
	public String testWarnSettings(){
		ObdHandShake obdHandShake = requestQueryService.warnSettings(obdSn);
		result = (obdHandShake != null ? obdHandShake.toString() : "");
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.warnSettings(obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.WarnSet;
		return testQuery(msgBody, result);
	}
	
	/**
	 * 查询WIFI流量
	 * @return
	 */
	public String testWifiFlow(){
		Integer flow;
		try {
			flow = (Integer) requestQueryService.extensionData(obdSn,ExtensionDataEnum.WiFiFlow);
//			flow = (Integer) requestQueryService.wifiFlow(obdSn);
			result = flow != null ? flow.toString() : "";
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extensionData(obdSn,ExtensionDataEnum.WiFiFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ExtensionDataEnum.WiFiFlow;
		return testQuery(msgBody, result);
	}
	/**
	 * 查询熄火后WIFI使用时间提醒
	 * @return
	 */
	public String testOverWifiUse(){
		try {
			Integer overWifiUse = (Integer) requestQueryService.extensionData(obdSn,ExtensionDataEnum.OverWiFiUse);
			result = overWifiUse != null ? overWifiUse.toString() : "";
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extensionData(obdSn,ExtensionDataEnum.OverWiFiUse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ExtensionDataEnum.OverWiFiUse;
		return testQuery(msgBody, result);
	}
	/**
	 * 查询WIFI流量开关
	 * @return
	 */
	public String testQueryWifiFlowSet(){
		try {
			Integer wifiFlowSet = (Integer) requestQueryService.extensionData(obdSn,ExtensionDataEnum.RealTimeFlow);
			result = wifiFlowSet != null ? wifiFlowSet.toString() : "";
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extensionData(obdSn,ExtensionDataEnum.RealTimeFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ExtensionDataEnum.RealTimeFlow;
		return testQuery(msgBody, result);
	}

	/**
	 * 清除WIFI使用流量
	 * @return
	 */
	public String testCleanFlowUse(){
		SimStockInfo simStockInfo = simStockInfoService.queryLastByObdSn(obdSn);
		if(simStockInfo != null){
			Long flowUse = simStockInfo.getFlowUse();
			Long tempFlowUse = simStockInfo.getTempFlowUse();
			simStockInfo.setCleanFlowUse(tempFlowUse+flowUse);
			simStockInfo.setValid(0);
			simStockInfoService.simStockInfoUpdate(simStockInfo);
			result = "清除流量成功";
		}else{
			result="流量记录为空！！！";
		}
		return "testQuery";
	}

	/**
	 * 查询WIFI使用流量
	 * @return
	 */
	public String testQueryFlowUse(){
		SimStockInfo simStockInfo = simStockInfoService.queryLastByObdSn(obdSn);
		Long flowUse = null;
		if(simStockInfo != null){
			if(simStockInfo.getValid() == 1){
				flowUse = simStockInfo.getFlowUse()+simStockInfo.getTempFlowUse();
			}else{
				//清除
				flowUse = simStockInfo.getFlowUse()+simStockInfo.getTempFlowUse()-simStockInfo.getCleanFlowUse();
			}
			result = "查询流量："+flowUse+" KB";
		}else{
			result="流量记录为空！！！";
		}
		return "testQuery";
	}
	
	/*查询休眠电压*/
	public String testSleepVolt(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.SleepVolt,null,null);
			result = (obj != null ? obj.toString() : ""); 
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.SleepVolt,null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" +  ExtensionDataQueryType.SleepVolt;
		return testQuery(msgBody, result);
	}

	/*查询休眠加速度*/
	public String testSleepOverSpeed(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.SleepOverSpeed,null,null);
			result = (obj != null ? obj.toString() : ""); 
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.SleepOverSpeed,null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.SleepOverSpeed;
		return testQuery(msgBody, result);
	}
	/*查询域白名单*/
	public String testDomainWhite(){
		try {
			result = (String)requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.DomainWhite,0,4);
			Thread.sleep(10000);
			result = GlobalData.getQueryDataMap(obdSn);
		} catch (OBDException | InterruptedException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.DomainWhite,0,4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.DomainWhite;
		return testQuery(msgBody, result);
	}
	/*查询域黑名单*/
	public String testDomainBlack(){
		try {
			result = (String)requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.DomainBlack,0,4);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			result = GlobalData.getQueryDataMap(obdSn);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.DomainBlack,0,4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.DomainBlack;
		return testQuery(msgBody, result);
	}
	/*查询域白名单开关*/
	public String testDomainWhiteSwitch(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.DomainWhiteSwitch,null,null);
			result = (obj != null ? obj.toString() : ""); 
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.DomainWhiteSwitch,null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.DomainWhiteSwitch;
		return testQuery(msgBody, result);
	}
	/*查询域黑名单开关*/
	public String testDomainBlackSwitch(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.DomainBlackSwitch, null, null);
			result = (obj != null ? obj.toString() : "");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.DomainBlackSwitch, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.DomainBlackSwitch;
		return testQuery(msgBody, result);
	}
	/*车型功能设置查询*/
	public String testCarTypeSetting(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.CarTypeSetting, null, null);
			result = (obj != null ? obj.toString() : "");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.CarTypeSetting, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.CarTypeSetting;
		return testQuery(msgBody, result);
	}
	/*低电压休眠阈值查询*/
	public String testLowVoltSleepValueQuery(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.LowVoltSleepValue, null, null);
			result = (obj != null ? obj.toString() : "");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.LowVoltSleepValue, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.LowVoltSleepValue;
		return testQuery(msgBody, result);
	}
	/*网络模式查询*/
	public String testNetModelQuery(){
		try {
			Object obj = requestQueryService.extension2Data(obdSn,ExtensionDataQueryType.NetModel, null, null);
			result = (obj != null ? obj.toString() : "");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		String msgBody = "";
		try {
			msgBody = serverRequestQueryUtil.extension2Data(obdSn,ExtensionDataQueryType.NetModel, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		typeName += ":" + ObdConstants.ExtensionDataQuery + ":" + ExtensionDataQueryType.NetModel;
		return testQuery(msgBody, result);
	}
	
	/**
	 * 测试-生成加密签字
	 */
	public void testSign(){
		try {
			String mySign = MD5Coder.encodeMD5Hex(deviceId + "chezhutong" + "123456" + "czt123456");
			outMessage(mySign);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// AGPS升级包
	public String testAGPS() throws Exception {
		System.out.println("---获取AGPS升级包---");
		Integer packageNum = 0;
		result = agpsService.sendAGPS(obdSn,packageNum);
		if (result == null) {
			result = " 更新AGPS升级包失败！";
		}
		return test(null,null);
	}
	
	/**
	 * 下发固件升级
	 * @throws OBDException
	 */
	public String testUpgrade() throws OBDException{
		result = upgradeService.sendOBDRequest(obdSn);
		return test(null,null);
	}
	
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void testPrivateProtocol(){
		result = serverSettingService.privateProtocolMsg(obdSn, msg);
		test(msg,result);
		outMessage("ok");
	}
	
	private JSONArray jsonArrayAdd(JSONArray jsonArray,String key,String keyValue,String msgKey1,String msg1,String msgKey2,String msg2) throws Exception{
		JSONObject jb = new JSONObject();
		if(StringUtils.isEmpty(key)){
			throw new Exception("驾驶行为参数不能为空.");
		}
		jb.put(key, keyValue);
		if(!StringUtils.isEmpty(msgKey1)){
			jb.put(msgKey1, msg1);
		}
		if(!StringUtils.isEmpty(msgKey2)){
			jb.put(msgKey2, msg2);
		}
		jsonArray.add(jb);
		return jsonArray;
	}
	private String upgradeType;
	public String getUpgradeType() {
		return upgradeType;
	}
	public void setUpgradeType(String upgradeType) {
		this.upgradeType = upgradeType;
	}
	
}
