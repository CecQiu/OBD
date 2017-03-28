package com.hgsoft.carowner.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 车主通平台与客户端接口方法编码枚举类
 * @author fdf
 */
public enum ApiCode {
	/**激活/绑定终端*/
	Bind, 
	/**设置/取消车辆防盗状态*/
	Guard, 
	/**查询车辆防盗状态*/
	QUERYGUARD, 
	/**当前位置查询*/
	QueryCurrentLocation, 
	/**故障码查询/车辆体检/故障检测*/
	MonitorFault,
	/**油耗查询*/
	QueryPetrol, 
	/**驾驶优化建议*/
	OptimizeDrive, 
	/**查询运行轨迹*/
	QueryRunningTrack, 
	/**设置信息发送方式*/
	SendType, 
	/**设置OBD设备GPS开关*/
	ControlGps,
	/**设置OBD设备WIFI密码*/
	ControlWifi, 
	/**查询OBD 用户信息*/
	QueryBindInfo, 
	/**查询OBD 预警信息*/
	QueryWarningInfo, 
	/**查询OBD 驾驶优化信息*/
	QueryPushOptimizeInfo, 
	/**查询跨月运行轨迹*/
	QueryRunningTrackHis,
	/**油耗跨月查询*/
	QueryPetrolHis, 
	/**查询设备状态*/
	QueryDeviceStatus, 
	/**查询流量卡流量信息*/
	QueryNetFlow,
	/**解绑*/
	UnBind,
	/**查询水温、电压信息*/
	QueryCurrentObdInfo,
	
	/**设置OBD电子栅栏参数*/
	SetOBDBarrier,
	
	GetCarInfo,
	/**mac地址和黑名单*/
	Portal,
	/**obdSn转换*/
	ObdSnChange,
	Dzwl,
	/*驾驶行为接口*/
	DriveBehaviour,
	/**wifi使用时间**/
	WifiUseTime,
	SetdriveBehaviour,//驾驶行为参数设置
	WifiPwdAndName,//wifi密码和名字ssid
	AlarmSwitch,//设置OBD设备报警开关
	AlarmSwitchState,//设置OBD设备报警开关状态查询
	Test;
	
	
	private static Map<String, ApiCode> map;
	
	//初始化map对象
	static {
		map = new HashMap<String, ApiCode>();
		map.put("bind", Bind);
		map.put("guard", Guard);
		map.put("queryGuard", QUERYGUARD);
		map.put("queryCurrentLocation", QueryCurrentLocation);
		map.put("monitorFault", MonitorFault);
		map.put("queryPetrol", QueryPetrol);
		map.put("optimizeDrive", OptimizeDrive);
		map.put("queryRunningTrack", QueryRunningTrack);
		map.put("sendType", SendType);
		map.put("controlGps", ControlGps);
		map.put("controlWifi", ControlWifi);
		map.put("queryBindInfo", QueryBindInfo);
		map.put("queryWarningInfo", QueryWarningInfo);
		map.put("queryPushOptimizeInfo", QueryPushOptimizeInfo);
		map.put("queryRunningTrackHis", QueryRunningTrackHis);
		map.put("queryPetrolHis", QueryPetrolHis);
		map.put("queryDeviceStatus", QueryDeviceStatus);
		map.put("queryNetFlow", QueryNetFlow);
		map.put("unBind", UnBind);
		map.put("queryCurrentObdInfo", QueryCurrentObdInfo);
		map.put("setOBDBarrier", SetOBDBarrier);
		map.put("test", Test);
		map.put("getCarInfo", GetCarInfo);
		map.put("portal", Portal);
		map.put("obdSnChange", ObdSnChange);
		map.put("dzwl", Dzwl);
		map.put("driveBehaviour", DriveBehaviour);
		map.put("wifiUseTime", WifiUseTime);
		map.put("setdriveBehaviour", SetdriveBehaviour);
		map.put("wifiPwdAndName", WifiPwdAndName);
		map.put("alarmSwitch", AlarmSwitch);
		map.put("alarmSwitchState", AlarmSwitchState);
	}
	
	public static ApiCode getApiCode(String code) {
		return map.get(code);
	}
}
