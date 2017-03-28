package com.hgsoft.obd.server;


/**
 * OBD常量值
 * @author sujunguang
 * 2016年1月7日
 * 上午9:37:06
 */
public class ObdConstants {
	/******************新协议常量值******************/
	/**
	 * 报文头
	 */
	public static final String OBD_Packet_Head = "aa";
	/**
	 * 报文尾
	 */
	public static final String OBD_Packet_Tail = "aa";

	/********************** 协议命令字 *********************/
	/**
	 * 设备初始化
	 */
	public static final String OBD_Init_Cmd = "0001";
	/**
	 * 位置数据上传
	 */
	public static final String OBD_Position_Cmd = "0002";
	/**
	 * 行程记录上传
	 */
	public static final String OBD_Travel_Cmd = "0003";
	/**
	 * 设备数据上传
	 */
	public static final String OBD_DeviceData_Cmd = "0004";
	/**
	 * 设备请求数据帧/ACK应答
	 */
	public static final String OBD_RequestOrACK_Cmd = "0005";
	/**
	 * 扩展数据上传
	 */
	public static final String OBD_ExtensionData_Cmd = "0006";
	/**
	 * 扩展2数据上传
	 */
	public static final String OBD_Extension2Data_Cmd = "0008";
	
	/********************** 协议命令字 —— 升级(旧协议)*********************/
	/**
	 * 请求升级包
	 */
	public static final String OBD_RequestUpgradeData_Cmd = "0009";
	/**
	 * 远程升级结果
	 */
	public static final String OBD_UpgradeResult_Cmd = "000a";
	
	/**
	 * 服务器响应升级数据包
	 */
	public static final String OBD_ServerResponseUpgradeData_Cmd = "800c";
	
	/********************** 服务器下发命令 *********************/
	/**
	 * 服务器下发OBD命令字——主动设置
	 */
	public static final String Server_SendSetting_OBD_Cmd = "8001";
	/**
	 * 服务器下发OBD命令字——服务器请求数据（查询）
	 */
	public static final String Server_SendQuery_OBD_Cmd = "8002";
	/**
	 * 服务器下发OBD命令字——服务器响应设备请求
	 */
	public static final String Server_SendResponse_OBD_Cmd = "8003";
	
	/**服务器应答ACK给OBD**/
	public static final String Server_ResponseACK_OBD_Cmd = Server_SendResponse_OBD_Cmd + "01";
	/**服务器应答AGPS给OBD**/
	public static final String Server_ResponseAGPS_OBD_Cmd = Server_SendResponse_OBD_Cmd + "02";
	/**服务器应答AGNSS给OBD**/
	public static final String Server_ResponseAGNSS_OBD_Cmd = Server_SendResponse_OBD_Cmd + "04";
	/**服务器应答固件升级包给OBD—没有用到，作废**/
	public static final String Server_ResponseUpgrade_OBD_Cmd = Server_SendResponse_OBD_Cmd + "04";
	/**服务器应答服务器时间给OBD**/
	public static final String Server_ResponseServerTime_OBD_Cmd = Server_SendResponse_OBD_Cmd + "08";
	
	/**服务器请求OBD故障码**/
	public static final String Server_RequestFaultCode_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0001";
	/**服务器请求OBD离线数据**/
	public static final String Server_RequestOffData_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0002";
	/**服务器请求OBD离线行程单**/
	public static final String Server_RequestOffTravel_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0004";
	/**服务器请求OBD设备状态**/
	public static final String Server_RequestDeviceState_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0008";
	/**服务器请求OBD车辆状态**/
	public static final String Server_RequestCarState_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0010";
	/**服务器请求OBD报警设置**/
	public static final String Server_RequestWarnSetting_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0020";
	/**服务器请求OBD车辆运行状态——去除
	public static final String Server_RequestCarRunState_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0040";
	**/
	/**服务器请求OBD 扩展帧2数据**/
	public static final String Server_RequestExtension2_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0040";
	/**服务器请求OBD设备时间参数**/
	public static final String Server_RequestDeviceTimeParams_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0080";
	/**服务器请求OBD行程参数**/
	public static final String Server_RequestTravelParams_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0100";
	/**服务器请求OBD总里程**/
	public static final String Server_RequestTotalMiles_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0200";
	/**服务器请求OBD数据服务器参数**/
	public static final String Server_RequestDataServerParams_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0400";
	/**服务器请求OBD升级服务器参数**/
	public static final String Server_RequestUpgradeServerParams_OBD_Cmd = Server_SendQuery_OBD_Cmd + "0800";
	/**服务器请求OBDPortal服务器参数**/
	public static final String Server_RequestPortalServerParams_OBD_Cmd = Server_SendQuery_OBD_Cmd + "1000";
	/**服务器请求OBD设备版本信息**/
	public static final String Server_RequestDeviceVersion_OBD_Cmd = Server_SendQuery_OBD_Cmd + "2000";
	/**服务器请求OBD 实时位置**/
	public static final String Server_RequestRealTimeLoc_OBD_Cmd = Server_SendQuery_OBD_Cmd + "4000";
	/**服务器请求OBD 扩展帧查询**/
	public static final String Server_RequestExtension_OBD_Cmd = Server_SendQuery_OBD_Cmd + "8000";
	
	/*****************************服务器设备功能设置*****************************/
	public static final String SerDevFunc = "01";
	/**服务器设置OBD WiFi**/
	public static final String Server_SettingWiFi_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0001";
	/**服务器设置OBD GPS**/
	public static final String Server_SettingGPS_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0002";
	/**服务器设置OBD GPS数据格式**/
	public static final String Server_SettingGPSFormat_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0004";
	/**服务器设置OBD 离线心跳**/
	public static final String Server_SettingOffHeart_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0008";
	/**服务器设置OBD 撤防设防**/
	public static final String Server_SettingGuard_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0010";
	/**服务器设置OBD 车辆震动（拖车）报警**/
	public static final String Server_SettingShock_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0020";
	/**服务器设置OBD 超速报警开关**/
	public static final String Server_SettingOverSpeed_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0040";
	/**服务器设置OBD 清除故障码**/
	public static final String Server_SettingClearFaultCode_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0080";
	/**服务器设置OBD 恢复出厂**/
	public static final String Server_SettingReset_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0100";
	/**服务器设置OBD 重新启动**/
	public static final String Server_SettingRestart_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0200";
	/**服务器设置OBD 设备升级设置**/
	public static final String Server_SettingDeviceUpgrade_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0400";
	/**服务器设置OBD 清除流量计数值**/
	public static final String Server_SettingCleanFlowStat_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "0800";
	/**服务器设置OBD 关闭设备**/
	public static final String Server_SettingCloseDevice_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "1000";
	/**服务器设置OBD FOTA升级设置**/
	public static final String Server_SettingFOTAUpgrade_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "2000";
	/**服务器设置OBD 流量开关设置**/
	public static final String Server_SettingWiFiFlow_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevFunc + "4000";
	
	/*****************************服务器参数设置*****************************/
	public static final String SerDevParams = "02";
	/**服务器设置OBD 报警设置**/
	public static final String Server_SettingWarn_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "01";
	/**服务器设置OBD 设备时间参数**/
	public static final String Server_SettingDeviceTimeParams_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "02";
	/**服务器设置OBD 行程参数**/
	public static final String Server_SettingTravelParams_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "04";
	/**服务器设置OBD 数据服务器参数**/
	public static final String Server_SettingDataServerParams_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "08";
	/**服务器设置OBD 升级服务器参数**/
	public static final String Server_SettingUpgradeServerParams_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "10";
	/**服务器设置OBD Portal服务器参数**/
	public static final String Server_SettingPortalServerParams_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "20";
	
	public static final String	Server_SendPortal_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerDevParams + "40";
	
	/*****************************Protal/WiFi设置*****************************/
	public static final String SerPortalWiFi = "04";
	/**服务器设置OBD Protal/WiFi **/
	public static final String Server_SettingPortalWiFi_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerPortalWiFi;

	/*****************************FOTA设置*****************************/
	public static final String SerFOTA = "08";
	/**服务器设置OBD Protal/WiFi **/
	public static final String Server_SettingFOTA_OBD_Cmd = Server_SendSetting_OBD_Cmd + SerFOTA;

	
	/*****************************设备流量同步*****************************/
	public static final String DeviceFlowSync = Server_SendSetting_OBD_Cmd+"10";
	
	/*****************************扩展数据设置*****************************/
	public static final String ExtensionDataSetting = Server_SendSetting_OBD_Cmd+"80";
	/**域黑白名单设置 **/
	public static final String DomainBlackWhiteListSetting = Server_SendSetting_OBD_Cmd+"20";

	/*
	*//**
	 * OBD升级固件包：命令字+数据帧格式
	 *//*
	public static final String OBD_AGPS_Cmd_Frame = Server_Send_OBD_Cmd + "0020";
	*//**
	 * OBD升级固件包：命令字+数据帧格式
	 *//*
	public static final String OBD_Upgrade_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "40";
	*//**
	 * OBD服务器时间：命令字+数据帧格式
	 *//*
	public static final String OBD_ServerDate_Cmd_Frame = Server_Send_OBD_Cmd + "0080";
	*//**
	 * 下发Portal指令：命令字+数据帧格式
	 *//*
	public static final String OBD_Portal_Cmd_Frame = Server_Send_OBD_Cmd + "0100";
	*//**
	 * 读取故障码指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_Read_Fault_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "01";
	*//**
	 *清除故障码指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_Clear_Fault_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "02";
	*//**
	 * 获取实时位置指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_RealTimeLocation_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "10";
	*//**
	 * OBD恢复出厂设置指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_Reset_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "04";
	*//**
	 * OBD设备重新启动指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_Restart_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "08";
	*//**
	 * 请求离线数据指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_RequestOffData_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "20";
	*//**
	 * 请求固件升级指令：命令字+数据帧格式+请求帧
	 *//*
	public static final String OBD_RequestUpgade_Cmd_Frame = Server_Send_OBD_Cmd + "0004" + "40";
*/	

	/**
	 * Key 分隔符
	 */
	public static final String keySpilt = "_";
	/*** 实时位置*/
	public static final String RealTimeLoc = "realTimeLoc";
	/*** 数据服务器参数*/
	public static final String DataServerParams = "dataServerParams";
	/*** 升级服务器参数*/
	public static final String UpgradeServerParams = "upgradeServerParams";
	/*** portal服务器参数*/
	public static final String PortalServerParams = "portalServerParams";
	/*** 行程参数*/
	public static final String TravelParams = "travelParams";
	/*** 设备时间参数*/
	public static final String DeviceTimeParams = "deviceTimeParams";
	/*** OBD设备版本*/
	public static final String ObdDeviceVersion = "obdDeviceVersion";
	/*** 总里程=>半条行程*/
	public static final String HalfTravel = "halfTravel";
	/*** 故障码*/
	public static final String FaultCode = "faultCode";
	/*** 设备状态*/
	public static final String DeviceState = "deviceState";
	/*** 车辆状态*/
	public static final String CarState = "carState";
	/*** 报警设置*/
	public static final String WarnSet = "warnSet";
	/*** 发送报文，接收结果成功状态*/
	public static final String SendResultSucessState01 = "01";
	public static final String SendResultSucessState00 = "00";
	
	public static final String ExtensionDataQuery = "extensionDataQuery";
//	public static final String OverWiFiUse = "overWiFiUse";
//	public static final String RealTimeFlow = "wiFiFlow";
//	public static final String WiFiFlow = "wiFiFlow";
	public static final String OFFData = "_offData";
	public static final String OFFTravel = "_offTravel";
	
	public static final String ObdOnline = "01";
	public static final String ObdOffline = "00";
	
}
