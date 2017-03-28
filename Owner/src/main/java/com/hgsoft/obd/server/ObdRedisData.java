package com.hgsoft.obd.server;

public class ObdRedisData {

	//OBD设备TTL时间
	public static String OBD_TTL_KEY = "OBD_TTL:";//obdSn_TTL_88888888
	
	//驾驶限制(参数)：休息，行车
	public final static String OBD_LimitDriveTime_KEY = "OBD_LimitDriveTime:";//OBD_LimitDriveTime_88888888
	public final static String OBD_LimitDriveTime_RestTimeField = "OBD_LimitDriveTime_RestTimeField_";
	public final static String OBD_LimitDriveTime_DriveTimeField = "OBD_LimitDriveTime_DriveTimeField_";
	
	//驾驶疲劳相关
	public final static String OBD_DriveTime_KEY = "OBD_DriveTime";//OBD_DriveTime
	public final static String OBD_DriveTime_StartTimeField = "OBD_DriveTime_StartTimeField_";
	public final static String OBD_DriveTime_NewTimeField = "OBD_DriveTime_NewTimeField_";
	
	public final static String OBD_DriveTime_DriveTimeField = "OBD_DriveTime_DriveTimeField_";//驾驶
	public final static String OBD_DriveTime_RestTimeField = "OBD_DriveTime_RestTimeField_";//休息
	public final static String OBD_DriveTime_DrivingField = "OBD_DriveTime_DrivingField_";//正在驾驶
	public final static String OBD_DriveTime_RestingField = "OBD_DriveTime_RestingField_";//正在休息
	public final static String OBD_DriveTime_SendTriedField = "OBD_DriveTime_SendTriedField_";//发送疲劳驾驶记录
	public final static String OBD_DriveTime_EnterSleepDateField = "OBD_DriveTime_EnterSleepDateField_";//设备进入休眠时间点（完整行程上传）
	
	//推送相关
	public static String OBD_Push_KEY = "OBD_Push:";//OBD_Push_88888888
	public static String OBD_Push_CountField = "OBD_Push_CountField_";
	public static String OBD_Push_TimeField = "OBD_Push_TimeField_";
	
	//上次上电号
	public final static String UpElectricNo = "UpElectricNo:";
	
	//行程相关：行程开始时间，行程结束时间，里程，油耗
	public final static String TravelData_ObdSn = "TravelData:";
	public final static String TravelStart = "TravelStart";
	public final static String TravelEnd = "TravelEnd";
	public final static String TravelMile = "TravelMile";
	public final static String TravelOil = "TravelOil";
	
	//记录OBD设备在哪个主机上：key->ObdInHost:88888888,value->127.0.0.1
	public final static String ObdInHost = "ObdInHost:";
	
	//OBD统计数据
	public final static String ObdStatics_ObdSn = "ObdStatics:";
	
	//服务器下发报文到设备是否成功统计次数，按天统计
	public final static String ServerSendToObdSuccess = "ServerSendToObdSuccess";
	public final static String ServerSendToObdFail = "ServerSendToObdFail";
	
	//最新位置Key
	public final static String OBD_LastPosition_KEY = "OBD_LastPosition";
	
	//存放设备最新初始化
	public final static String OBD_LastHandShake_KEY = "OBD_LastHandShake";

	//存放设备最后上线时间
	public final static String OBD_LastOnLine_KEY = "OBD_LastOnLine";

	//当前请求离线数据
	public final static String OffLineData_TTL = "OffLineData_TTL:";
	
	
	
}
