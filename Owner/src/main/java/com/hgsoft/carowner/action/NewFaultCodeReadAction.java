/**
 * 
 */
package com.hgsoft.carowner.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.service.BlindPointClearService;
import com.hgsoft.carowner.service.CarParamQueryService;
import com.hgsoft.carowner.service.CarParamSetService;
import com.hgsoft.carowner.service.FangDaoService;
import com.hgsoft.carowner.service.FaultCodeClearService;
import com.hgsoft.carowner.service.FaultCodeReadService;
import com.hgsoft.carowner.service.MonitorService;
import com.hgsoft.carowner.service.MsgBatchReqService;
import com.hgsoft.carowner.service.OBDPositionService;
import com.hgsoft.carowner.service.OBDRecoveryService;
import com.hgsoft.carowner.service.OBDRestartService;
import com.hgsoft.carowner.service.OBDStudyService;
import com.hgsoft.carowner.service.ParamSetService;
import com.hgsoft.carowner.service.ProtalService;
import com.hgsoft.carowner.service.RemoteUpgradeService;
import com.hgsoft.carowner.service.ServerSetService;
import com.hgsoft.carowner.service.UpdateDataService;
import com.hgsoft.carowner.service.WiFiSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.MsgThread;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.handler.MessageObdSendDownHandler;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.system.utils.ByteUtil;

@Controller
@Scope("prototype")
public class NewFaultCodeReadAction extends BaseAction {
	private final Log logger = LogFactory.getLog(NewFaultCodeReadAction.class);

	@Resource
	FaultCodeReadService faultCodeReadService;
	@Resource
	FangDaoService fangDaoService;
	@Resource
	ParamSetService paramSetService;
	@Resource
	OBDPositionService oBDPositionService;
	@Resource
	MonitorService monitorService;
	@Resource
	FaultCodeClearService faultCodeClearService;
	@Resource
	MsgBatchReqService msgBatchReqService;
	@Resource
	OBDRestartService oBDRestartService;
	@Resource
	OBDStudyService oBDStudyService;
	@Resource
	BlindPointClearService blindPointClearService;
	@Resource
	OBDRecoveryService oBDRecoveryService;
	@Resource
	WiFiSetService wiFiSetService;
	@Resource
	ServerSetService serverSetService;
	@Resource
	CarParamSetService carParamSetService;
	@Resource
	CarParamQueryService carParamQueryService;
	@Resource
	UpdateDataService updateDataService;
	@Resource
	RemoteUpgradeService remoteUpgradeService;
	@Resource
	ProtalService protalService;
	
	//主动设置
	private String activeSetCommand = "8001";
	//服务器请求数据
	private String serverRequestCommand = "8002";
	
	public String list2(){
		return "list2";
	}
	
	//车辆运行状态 TODO
	public String carStatus() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0001000000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "0000001000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_CAR_RUNING_STATE, "00000010");
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//行程参数
	public String travelParameters() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "00100000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DISTANCE_PARAMETER_PATTERN, StrUtil.binary2Hex("0000000001000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DISTANCE_PARAMETER, 
				Integer.toHexString(115)
				+ Integer.toHexString(145)
				+ Integer.toHexString(80)
				+ Integer.toHexString(80)
				+ Integer.toHexString(40)
				+ Integer.toHexString(50)
				+ Integer.toHexString(105)
				+ Integer.toHexString(2)
				+ Integer.toHexString(14)
				+ Integer.toHexString(25)
				+ Integer.toHexString(50)
				+ Integer.toHexString(20)
				+ Integer.toHexString(80)
				+ Integer.toHexString(115)
				+ Integer.toHexString(4500)
//				+ Integer.toHexString(4000)
//				+ Integer.toHexString(500)
				+ Integer.toHexString(5)
				+ Integer.toHexString(3)
				+ Integer.toHexString(3)
				+ Integer.toHexString(90)
				+ Integer.toHexString(3)
				+ Integer.toHexString(3)
				+ "11"
				+ "60"
				+ "40"
				+ "00"
				+ "33"
				+ "32"
				+ "00"
				+ "00"
				+ "11"
				+ "60"
				+ "40"
				+ "00"
				+ "33"
				+ "32"
				+ "00"
				+ "00"
				);
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//设备时间
	public String deviceTime() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_TIME_PATTERN, StrUtil.binary2Hex("00000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_TIME_PARAMETER, 
				Integer.toHexString(60)
				+ Integer.toHexString(1)
				+ Integer.toHexString(10)
				+ Integer.toHexString(30)
				+ Integer.toHexString(30)
				+ Integer.toHexString(3)
				+ Integer.toHexString(6)
				+ Integer.toHexString(3)
				);
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//报警设置
	public String warnSetting() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_WARN_SETTING, StrUtil.binary2Hex("00000010".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//读取故障码
	public String readFaultCodes() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0010000000000000");
//		map.put(MessageObdSendDownHandler.CODE_KEY_COMMAND_REQUEST, StrUtil.binary2Hex("10000000".toCharArray()));
//		mySendMsg(obdid, map);
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "1000000000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//服务器参数设置
	public String serviceParamSetting() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
//		String obdSn = request.getParameter("obdId");
//		String setType = request.getParameter("setType");
//		String serverType = request.getParameter("serverType");
		String port = request.getParameter("port");
		String ip = request.getParameter("ip");
		String APN = request.getParameter("APN");
		
		String portStr = Integer.toHexString(Integer.parseInt(port));
		String[] ipArr= ip.split("\\.");
		String ip1=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[0])),1,"0");
		String ip2=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[1])),1,"0");
		String ip3=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[2])),1,"0");
		String ip4=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[3])),1,"0");
		String ipStr = ip1+ip2+ip3+ip4;
		String ipStrLen = StrUtil.strAppendByLen(Integer.toHexString(ipStr.length()/2),1,"0");
		String APNStr = ByteUtil.ASC2ToHexStr(APN);//ASC2码转16进制
		String APNStrLen = StrUtil.strAppend(Integer.toHexString(APNStr.length()/2), 2, 0, "0");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "00010000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DATA_SERVICE_PARAMETER, portStr+ipStrLen+ipStr+APNStrLen+APNStr);
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//升级服务器参数
	public String serviceParamUpdate() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
//		String obdSn = request.getParameter("obdId");
//		String setType = request.getParameter("setType");
//		String serverType = request.getParameter("serverType");
		String port = request.getParameter("port");
		String ip = request.getParameter("ip");
		String APN = request.getParameter("APN");
		
		String portStr = Integer.toHexString(Integer.parseInt(port));
		String[] ipArr= ip.split("\\.");
		String ip1=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[0])),1,"0");
		String ip2=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[1])),1,"0");
		String ip3=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[2])),1,"0");
		String ip4=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[3])),1,"0");
		String ipStr = ip1+ip2+ip3+ip4;
		String ipStrLen = StrUtil.strAppendByLen(Integer.toHexString(ipStr.length()/2),1,"0");
		String APNStr = ByteUtil.ASC2ToHexStr(APN);//ASC2码转16进制
		String APNStrLen = StrUtil.strAppend(Integer.toHexString(APNStr.length()/2), 2, 0, "0");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "00001000");
		map.put(MessageObdSendDownHandler.CODE_KEY_UPDATE_SERVICE_PARAMETER, portStr+ipStrLen+ipStr+APNStrLen+APNStr);
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//Protal服务器参数
	public String protalServiceParam() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
//		String obdSn = request.getParameter("obdId");
//		String setType = request.getParameter("setType");
//		String serverType = request.getParameter("serverType");
		String port = request.getParameter("port");
		String ip = request.getParameter("ip");
		String APN = request.getParameter("APN");
		
		String portStr = Integer.toHexString(Integer.parseInt(port));
		String[] ipArr= ip.split("\\.");
		String ip1=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[0])),1,"0");
		String ip2=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[1])),1,"0");
		String ip3=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[2])),1,"0");
		String ip4=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[3])),1,"0");
		String ipStr = ip1+ip2+ip3+ip4;
		String ipStrLen = StrUtil.strAppendByLen(Integer.toHexString(ipStr.length()/2),1,"0");
		String APNStr = ByteUtil.ASC2ToHexStr(APN);//ASC2码转16进制
		String APNStrLen = StrUtil.strAppend(Integer.toHexString(APNStr.length()/2), 2, 0, "0");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "01000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "00000100");
		map.put(MessageObdSendDownHandler.CODE_KEY_PROTAL_SERVICE_PARAMETER, portStr+ipStrLen+ipStr+APNStrLen+APNStr);
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//设备版本信息-查询
	public String deviceVersion() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000000000100");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//设备状态-查询
	public String deviceStatusSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0001000000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//车辆状态-查询
	public String carStatusSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000100000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//离线位置数据-查询
	public String offlinePositionSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0100000000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//离线行程单-查询
	public String offlineTravelSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0010000000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//车辆运行状态-查询
	public String carRunStatusSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000001000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//报警设置-查询
	public String warnSettingSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000010000000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//总里程-查询
	public String totalSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000001000000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//数据服务器参数-查询
	public String dataServiceParamSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000000100000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//升级服务器参数-查询
	public String updateServiceParamSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000000010000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//Protal服务器参数-查询
	public String protalServiceParamSearch() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000000001000");
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//设备时间帧格式-查询
	public String deviceTimeSerach() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000100000000");
//		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_TIME_PATTERN, StrUtil.binary2Hex("00000000".toCharArray()));
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//行程参数帧格式-查询
	public String travelParametersSerach() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
//		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0000100000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SEARCH, "0000000010000000");
//		map.put(MessageObdSendDownHandler.CODE_KEY_DISTANCE_PARAMETER_PATTERN, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		
		mySendMsg(obdid, serverRequestCommand, map);
		return "list2";
	}
	
	//设防
	public String fortification() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000100000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//撤防
	public String disarm() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000100000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000100000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//超速报警开关-开
	public String superWarnOn() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000001000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//超速报警开关-关
	public String superWarnOff() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000001000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000001000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//车辆震动(拖车)报警开关设置-开
	public String shockOn() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000010000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//车辆震动(拖车)报警开关设置-关
	public String shockOff() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000010000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000010000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//离线心跳设置-关
	public String offlineOff() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0001000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//离线心跳设置-开
	public String offlineOn() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0001000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0001000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//GPS数据格式-只传定位数据
	public String gpsDataFormatPosition() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0010000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	//GPS数据格式-全部
	public String gpsDataFormatAll() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0010000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0010000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	public String gpsOn() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0100000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	public String gpsOff() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0100000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0100000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	public String wifiOn() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("1000000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000000000000".toCharArray()));
		
		mySendMsg(obdid, activeSetCommand, map);
		
		return "list2";
	}
	
	//清除故障码
	public String clearFaultCode() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000000100000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000100000000".toCharArray()));
		
		mySendMsg(obdid, activeSetCommand, map);
		
		return "list2";
	}
	
	//恢复出厂设置
	public String resetObd() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000000010000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000010000000".toCharArray()));
		
		mySendMsg(obdid, activeSetCommand, map);
		
		return "list2";
	}
	
	//设备重新启动
	public String restartObd() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("0000000001000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("0000000001000000".toCharArray()));
		
		mySendMsg(obdid, activeSetCommand, map);
		
		return "list2";
	}
	
	public String wifiOff() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdid = request.getParameter("obdId");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "10000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING_FORMAT, StrUtil.binary2Hex("1000000000000000".toCharArray()));
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_SETTING, StrUtil.binary2Hex("1000000000000000".toCharArray()));
		mySendMsg(obdid, activeSetCommand, map);
		return "list2";
	}
	
	int i=0;
	
	private String getResultMsg(String msgId) {
		i++;
		//如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
		MsgThread thread =new MsgThread(msgId);
		thread.start();
		try {
			//线程同步锁，顺序执行
			thread.join(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//获取到客户端的响应消息
		String resultStr= (String) thread.getResMsg();
		if(i<=2 && resultStr==null){
			resultStr = getResultMsg(msgId);
		}
		i=0;
		
		return resultStr;
	}
	
	public String clearChannel() {
//		GlobalData.clearChannel();
		return "list2";
	}
	
	public void mySendMsg(String obdid, String command, Map<String, String> map) throws Exception {
		MessageObdSendDownHandler m = new MessageObdSendDownHandler();
		String msg = m.createMsg(obdid, command, map);
		
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<msg.length(); i=i+2) {
			sb.append(msg.substring(i, i+2));
			sb.append(" ");
		}
		System.out.println(msg);
		System.out.println(sb);
		
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		msgSendUtil.sendMsg(obdid, msg);
		/*获取反馈信息
		int serialNumber=SerialNumberUtil.getSerialnumber();
		String msgId = obdid+"_"+serialNumber;
		
		String str = getResultMsg(msgId);
		if(str!=null){
			System.out.println(str+"**********************wifi打开-返回消息啦啦啦啦啦啦啦啦啦");
		}
		*/
	}
}
