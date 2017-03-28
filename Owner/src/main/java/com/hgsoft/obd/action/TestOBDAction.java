package com.hgsoft.obd.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.carowner.entity.OBDServerParams;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.service.AGPSService;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.service.UpgradeService;
import com.hgsoft.obd.util.ExtensionDataQueryType;
import com.hgsoft.obd.util.ServerParamsType;

/**
 * 测试OBD
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class TestOBDAction extends BaseAction {
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ServerRequestQueryService requestQueryService;
	@Resource
	private AGPSService agpsService;
	@Resource
	private UpgradeService upgradeService;
	
	private String obdSn;
	private String state;
	private String result;
	
	private Map mapACKORQueryData = GlobalData.OBD_ACK_OR_QueryData;
	private Map mapOBDChannel = GlobalData.OBD_CHANNEL;
	private Map mapOBD_Packet = GlobalData.OBD_Packet;
	private Map mapOBD_PacketNum = GlobalData.OBD_PacketNum;
	
	private String deviceId;
	private String username;

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

	public String test(){
		return "test";
	}
	
	public String test1(){
		return "test1";
	}

	public String testQuery(){
		return "testQuery";
	}
	
	/*********************设置********************/
	public String testGPS(){
		try {
			result = serverSettingService.gps(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testWiFi(){
		try {
			result = serverSettingService.wifi(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testGuard(){
		try {
			result = serverSettingService.carGuard(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testShock(){
		try {
			result = serverSettingService.carShock(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testClearFaultCode(){
		try {
			result = serverSettingService.clearFaultCode(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testGPSFormat(){
		try {
			result = serverSettingService.gpsFormat(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}

	public String testOffHeart(){
		try {
			result = serverSettingService.offHeart(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}

	public String testOverSpeedWarn(){
		try {
			result = serverSettingService.overSpeedWarn(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}

	public String testReset(){
		try {
			result = serverSettingService.reset(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testRestart(){
		try {
			result = serverSettingService.restart(obdSn, state);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	public String testDeviceUpgradeSet(){
		try {
			result = serverSettingService.deviceUpgradeSet(obdSn, state, null, null);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	/**
	 * 报警设置
	 */
	public String testWarnSet(){
		String warnSet = "10010010";
		try {
			result = serverSettingService.warnSet(obdSn, warnSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	/**
	 * 设备时间设置
	 */
	public String testDeviceTime(){
		OBDTimeParams obdTimeParams = new OBDTimeParams(51, 1, 4, 27,26, 170, 59, 180);
		try {
			result = serverSettingService.deviceTimeSet(obdSn,"00000000".toCharArray(), obdTimeParams);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	/**
	 * 行程参数设置
	 * @return
	 */
	public String testTravelParamsSet(){
		OBDTravelParams obdTravelParams = new OBDTravelParams(
				114, 122, 18, 19,20, 21, 22,
				23, 24, 25, 26, 27, 28, 29, 6943,
				8226, 4659, 4, 1, 2, 19, 3, 4, 1,20,
				"136°04.000'", "33°32.1000'", "136°14.101'", "34°33.0101'");
		
		try {
			result = serverSettingService.travelParamsSet(obdSn, "0000000000000000".toCharArray(), obdTravelParams);
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}

	/**
	 * 数据服务器参数设置
	 */
	public String testDataServerParamsSet(){
		String ip = "161.162.163.164";
		String port = "8888";
		String APN = "a123b";
		try {
			result = serverSettingService.dataServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	/**
	 * 升级服务器参数设置
	 */
	public String testUpgradeServerParamsSet(){
		String ip = "17.18.19.20";
		String port = "9999";
		String APN = "a123c";
		try {
			result = serverSettingService.upgradeServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	/**
	 * portal服务器参数设置
	 */
	public String testPortalServerParamsSet(){
		String ip = "49.50.51.52";
		String port = "7777";
		String APN = "a123d";
		try {
			result = serverSettingService.portalServerParamsSet(obdSn, ip, port, APN);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	//设置url
	public String testPortalORWifiSet0(){
		String url = "http://www.h123.com/tt";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "1000 0000 0000 0000".replaceAll(" ", "").toCharArray(), url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}

	//设置ID
	public String testPortalORWifiSet1(){
		String id = "2f000011";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0100 0000 0000 0000".replaceAll(" ", "").toCharArray(), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}

	//流量限制Mac
	public String testPortalORWifiSet2(){
		String mac = "ff:fa:fb:fc:fd:fe";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0010 0000 0000 0000".replaceAll(" ", "").toCharArray(), mac,"50");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	//设置白名单
	public String testPortalORWifiSet3(){
		String macs = "ff:fa:fb:fc:fd:fe,11:22:33:44:55:66";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0001 0000 0000 0000".replaceAll(" ", "").toCharArray(), macs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	//删除全部白名单
	public String testPortalORWifiSet4(){
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 1000 0000 0000".replaceAll(" ", "").toCharArray(), "00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
	//删除白名单-单条
	public String testPortalORWifiSet4_1(){
		String mac = "ff:fa:fb:fc:fd:fe";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 1000 0000 0000".replaceAll(" ", "").toCharArray(), "01",mac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	//打开portal
	public String testPortalORWifiSet5(){
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0100 0000 0000".replaceAll(" ", "").toCharArray(), "01");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}

	//关闭portal
	public String testPortalORWifiSet5_1(){
		String pwd = "12345678";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0100 0000 0000".replaceAll(" ", "").toCharArray(), "00",pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
		
	//设置SSID
	public String testPortalORWifiSet6(){
		String ssid = "123321";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0010 0000 0000".replaceAll(" ", "").toCharArray(), ssid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
		
	//设置密码
	public String testPortalORWifiSet7(){
		String pwd = "123#qax,;";
		try {
			result = serverSettingService.portalOrWifiSet(obdSn, "0000 0001 0000 0000".replaceAll(" ", "").toCharArray(), pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test";
	}
		
	/*********************查询********************/
	public String testReadFaultCode(){
		List<FaultUpload> faultUploads = requestQueryService.readFaultCode(obdSn);
		result = (faultUploads != null ? faultUploads.toString() : null);
		return "testQuery";
	}
	public String testCarRunState(){
//		result = requestQueryService.carRunState(obdSn);
		return "testQuery";
	}
	public String testCarState(){
		ObdHandShake obdHandShake = requestQueryService.carState(obdSn);
		result = (obdHandShake != null ? obdHandShake.toString() : "");
		return testQuery();
	}
	public String testDataServerParams(){
		try {
			OBDServerParams params = requestQueryService.serverParams(obdSn,ServerParamsType.DATA);
			result = (params != null?params.toString():"");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "testQuery";
	}
	public String testDeviceState(){
		ObdHandShake obdHandShake = requestQueryService.deviceState(obdSn);
		result = (obdHandShake != null ? obdHandShake.toString() : "");
		return testQuery();
	}
	public String testDeviceTimeParams(){
		result = requestQueryService.deviceTimeParams(obdSn,"00000000".toCharArray()).toString();
		return "testQuery";
	}
	public String testDeviceVersion(){
		OBDDeviceVersion deviceVersion = requestQueryService.deviceVersion(obdSn);
		result = deviceVersion != null ?deviceVersion.toString():"";
		return "testQuery";
	}
	public String testOffData(){
		result = requestQueryService.offData(obdSn,null, true);
		return "testQuery";
	}
	public String testOffTravel(){
		result = requestQueryService.offTravel(obdSn,null, true);
		return "testQuery";
	}
	public String testRealTimeLoc(){
		result = requestQueryService.realTimeLoc(obdSn);
//		result = (positionInfo != null ? positionInfo.toString():null);
		return "testQuery";
	}
	public String testTotalMiles(){
		String total = requestQueryService.halfTravel(obdSn);
		result = (total != null ?total.toString():"");
		return "testQuery";
	}
	public String testUpgradeServerParams(){
		try {
			OBDServerParams params = requestQueryService.serverParams(obdSn,ServerParamsType.UPGRADE);
			result = (params != null?params.toString():"");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "testQuery";
	}
	public String testTravelTimeParams(){
		OBDTravelParams obdTravelParams = requestQueryService.travelTimeParams(obdSn,"0000000000000000".toCharArray());
		result = obdTravelParams != null ? obdTravelParams.toString() : "";
		return "testQuery";
	}
	public String testPortalServerParams(){
		try {
			OBDServerParams params = requestQueryService.serverParams(obdSn,ServerParamsType.PORTAL);
			result = (params != null?params.toString():"");
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return "testQuery";
	}
	public String testWarnSettings(){
		ObdHandShake obdHandShake = requestQueryService.warnSettings(obdSn);
		result = (obdHandShake != null ? obdHandShake.toString() : "");
		return testQuery();
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
		return "test";
	}
	
	/**
	 * 下发固件升级
	 * @throws OBDException
	 */
	public String testUpgrade() throws OBDException{
		result = upgradeService.sendOBDRequest(obdSn);
		return "test";
	}
	
}
