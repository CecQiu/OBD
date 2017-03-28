package com.hgsoft.obd.handler;

import java.util.HashMap;
import java.util.Map;

import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * 设备下传通讯——服务器数据下发(设置OBD设备参数等)
 * @author sujunguang
 * 2015年12月12日
 * 下午4:26:20
 */
public class MessageObdSendDownHandler {
	
	/**数据帧格式-存在*/
	public final byte CODE_EXIST = '1';
	/**数据帧格式-不存在*/
	public final byte CODE_UNEXIST = '0';
	
	/**数据代码-基础命令*/
	public static final String CODE_KEY_BASE = "basekey";
	/**数据代码-参数设置*/
	public static final String CODE_KEY_SETTING = "settingkey";
	/**数据代码-参数查询*/
	public static final String CODE_KEY_SEARCH = "searchkey";
	/**数据代码-设备功能设置格式帧*/
	public static final String CODE_KEY_DEVICE_SETTING_FORMAT = "devicesettingformat";
	/**数据代码-设备功能设置*/
	public static final String CODE_KEY_DEVICE_SETTING = "devicesetting";
	/**数据代码-命令/请求*/
	public static final String CODE_KEY_COMMAND_REQUEST = "commandrequest";
	/**数据代码-设备状态*/
	public static final String CODE_KEY_DEVICE_STATE = "devicestate";
	/**数据代码-车辆状态*/
	public static final String CODE_KEY_CAR_STATE = "carstate";
	/**数据代码-报警设置*/
	public static final String CODE_KEY_WARN_SETTING = "warnsetting";
	/**数据代码-车辆运行状态*/
	public static final String CODE_KEY_CAR_RUNING_STATE = "carruningstate";
	/**数据代码-设备时间帧格式*/
	public static final String CODE_KEY_DEVICE_TIME_PATTERN = "devicetimepattern";
	/**数据代码-设备时间参数*/
	public static final String CODE_KEY_DEVICE_TIME_PARAMETER = "devicetimeparameter";
	/**数据代码-行程参数帧格式*/
	public static final String CODE_KEY_DISTANCE_PARAMETER_PATTERN = "distanceparameterpattern";
	/**数据代码-行程参数*/
	public static final String CODE_KEY_DISTANCE_PARAMETER = "distanceparameter";
	/**数据代码-总里程*/
	public static final String CODE_KEY_TOTAL_MILEAGE = "totalmileage";
	/**数据代码-数据服务器参数*/
	public static final String CODE_KEY_DATA_SERVICE_PARAMETER = "dataserviceparameter";
	/**数据代码-升级服务器参数*/
	public static final String CODE_KEY_UPDATE_SERVICE_PARAMETER = "updateserviceparameter";
	/**数据代码-Protal服务器参数*/
	public static final String CODE_KEY_PROTAL_SERVICE_PARAMETER = "protalserviceparameter";
	/**数据代码-设备版本信息*/
	public static final String CODE_KEY_DEVICE_VERSION_INFO = "deviceversioninfo";
	/**数据代码-查询设备时间帧格式*/
	public static final String CODE_KEY_SEARCH_DEVICE_TIME = "searchdevicetime";
	/**数据代码-查询行程参数帧格式*/
	public static final String CODE_KEY_SEARCH_DISTANCE_PARAMETER = "searchdistanceparameter";
	/**数据代码-AGPS升级包*/
	public static final String CODE_KEY_AGPS_UPDATE_PACKAGE = "agpsupdatepackage";
	/**数据代码-固件升级包*/
	public static final String CODE_KEY_FIRMWARE_UPDATE_PACKAGE = "firmwareupdatepackage";
	/**数据代码-服务器时间*/
	public static final String CODE_KEY_SEARVICE_TIME = "servicetime";
	/**数据代码-WIFI白名单长度*/
	public static final String CODE_KEY_WIFI_WHITE_LENGHT = "wifiwhitelenght";
	/**数据代码-WIFI白名单内容*/
	public static final String CODE_KEY_WIFI_WHITE_CONTENT = "wifiwhiteconten";
	/**数据代码-WIFI黑名单长度*/
	public static final String CODE_KEY_WIFI_BLACK_LENGHT = "wifiblacklenght";
	/**数据代码-WIFI黑名单内容*/
	public static final String CODE_KEY_WIFI_BLACK_CONTENT = "wifiblackconten";
	/**服务器请求数据-故障码*/
	public static final String CODE_KEY_FAULT_CODE_SEARCH = "faultcodesearch";
	/**服务器请求数据-离线位置数据*/
	public static final String CODE_KEY_OFFLINE_POSISTION_SEARCH = "offlineposistionsearch";
	/**服务器请求数据-离线行程单*/
	public static final String CODE_KEY_OFFLINE_TRAVEL_SEARCH = "offlinetravelsearch";
	/**服务器请求数据-实时位置查询*/
	public static final String CODE_KEY_REALTIME_POSITION_SEARCH = "realtimepositionsearch";
	
	/**下发操作时错误信息统一前缀标识*/
	public final String EXCEPTION_INFO_PRE = "下发时命令数据校验错误：";
	
	public String createMsg(String obdid, String command, Map<String, String> codeMap) throws Exception {
		String resultStr = "";
		MsgSendUtil msgSendUtil = new MsgSendUtil();
//		String common = "8001";//命令字
		int serialNumber = SerialNumberUtil.getSerialnumber();
		resultStr = msgSendUtil.msgAll(obdid, command, serialNumber, createMsg(command, codeMap));
		return resultStr;
	}
	
	/**
	 * 创建下发报文
	 * @param obdId
	 * @param codeMap
	 * @return 下发报文
	 * @throws Exception 下发过程中抛出的异常
	 */
	private String createMsg(String command, Map<String, String> codeMap) throws Exception {
		if(codeMap == null || codeMap.keySet().size() == 0) 
			throw new RuntimeException(EXCEPTION_INFO_PRE + "MAP不能为空！");
		
		StringBuffer sbMsg = new StringBuffer("");
		
		//主动设置
		if(command.equals("8001")) {
			//数据帧格式
			String baseCode = codeMap.get(CODE_KEY_BASE);
			if(baseCode == null || baseCode.trim().equals("")) 
				throw new RuntimeException(EXCEPTION_INFO_PRE + "基本的数据帧格式不能为空！");
			baseCode = baseCode.trim();
			
			sbMsg.append(StrUtil.binary2Hex(baseCode.toCharArray()));
			byte[] baseCodeArry = baseCode.getBytes();
			
			//设备功能设置
			if(baseCodeArry[0] == CODE_EXIST) {
				String deviceSettingCodeFormat = codeMap.get(CODE_KEY_DEVICE_SETTING_FORMAT);
				if(deviceSettingCodeFormat == null || deviceSettingCodeFormat.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "设备功能设置的格式帧编码不能为空！");
				deviceSettingCodeFormat = deviceSettingCodeFormat.trim();
				
				String deviceSettingCode = codeMap.get(CODE_KEY_DEVICE_SETTING);
				if(deviceSettingCode == null || deviceSettingCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "设备功能设置的编码不能为空！");
				deviceSettingCode = deviceSettingCode.trim();
				
				sbMsg.append(deviceSettingCodeFormat);
				sbMsg.append(deviceSettingCode);
			}
			
			//参数设置命令
			if(baseCodeArry[1] == CODE_EXIST) {
				String settingCode = codeMap.get(CODE_KEY_SETTING);
				if(settingCode == null || settingCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "参数设置的数据帧格式不能为空！");
				settingCode = settingCode.trim();
				sbMsg.append(StrUtil.binary2Hex(settingCode.toCharArray()));
				byte[] settingCodeArry = settingCode.getBytes();
				
				//报警设置
				if(settingCodeArry[0] == CODE_EXIST) {
					String warnSettingCode = codeMap.get(CODE_KEY_WARN_SETTING);
					if(warnSettingCode == null || warnSettingCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-报警设置的编码不能为空！");
					warnSettingCode = warnSettingCode.trim();
					sbMsg.append(warnSettingCode);
				}
				
				//设备时间
				if(settingCodeArry[1] == CODE_EXIST) {
					//设备时间帧格式
					String deviceTimePatternCode = codeMap.get(CODE_KEY_DEVICE_TIME_PATTERN);
					if(deviceTimePatternCode == null || deviceTimePatternCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-设备时间帧格式的编码不能为空！");
					deviceTimePatternCode = deviceTimePatternCode.trim();
					sbMsg.append(deviceTimePatternCode);
					
					//设备时间参数
					String deviceTimeParameterCode = codeMap.get(CODE_KEY_DEVICE_TIME_PARAMETER);
					if(deviceTimeParameterCode == null || deviceTimeParameterCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-设备时间参数的编码不能为空！");
					deviceTimeParameterCode = deviceTimeParameterCode.trim();
					sbMsg.append(deviceTimeParameterCode);
				}

				//行程参数
				if(settingCodeArry[2] == CODE_EXIST) {
					//行程参数帧格式
					String distancePatternCode = codeMap.get(CODE_KEY_DISTANCE_PARAMETER_PATTERN);
					if(distancePatternCode == null || distancePatternCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-行程参数帧格式的编码不能为空！");
					distancePatternCode = distancePatternCode.trim();
					sbMsg.append(distancePatternCode);
					
					//行程参数
					String distanceParameterCode = codeMap.get(CODE_KEY_DISTANCE_PARAMETER);
					if(distanceParameterCode == null || distanceParameterCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-行程参数的编码不能为空！");
					distanceParameterCode = distanceParameterCode.trim();
					sbMsg.append(distanceParameterCode);
				}

				//数据服务器参数
				if(settingCodeArry[3] == CODE_EXIST) {
					String dataServiceCode = codeMap.get(CODE_KEY_DATA_SERVICE_PARAMETER);
					if(dataServiceCode == null || dataServiceCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-数据服务器参数的编码不能为空！");
					dataServiceCode = dataServiceCode.trim();
					sbMsg.append(dataServiceCode);
				}
				
				//升级服务器参数
				if(settingCodeArry[4] == CODE_EXIST) {
					String updateServiceCode = codeMap.get(CODE_KEY_UPDATE_SERVICE_PARAMETER);
					if(updateServiceCode == null || updateServiceCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-升级服务器参数的编码不能为空！");
					updateServiceCode = updateServiceCode.trim();
					sbMsg.append(updateServiceCode);
				}
				
				//Protal服务器参数
				if(settingCodeArry[5] == CODE_EXIST) {
					String protalServiceCode = codeMap.get(CODE_KEY_PROTAL_SERVICE_PARAMETER);
					if(protalServiceCode == null || protalServiceCode.trim().equals("")) 
						throw new RuntimeException(EXCEPTION_INFO_PRE + "-Protal服务器参数的编码不能为空！");
					protalServiceCode = protalServiceCode.trim();
					sbMsg.append(protalServiceCode);
				}
			}
			
			//Protal透传指令
			if(baseCodeArry[2] == CODE_EXIST) {
				
			}
			
		//TODO 服务器请求数据
		} else if(command.equals("8002")) {
			String searchCode = codeMap.get(CODE_KEY_SEARCH);
			if(searchCode == null || searchCode.trim().equals("")) 
				throw new RuntimeException(EXCEPTION_INFO_PRE + "参数设置的数据帧格式不能为空！");
			searchCode = searchCode.trim();
			sbMsg.append(StrUtil.binary2Hex(searchCode.toCharArray()));
			
			/*
			byte[] searchCodeArry = searchCode.getBytes();
			
			//故障码
			if(searchCodeArry[0] == CODE_EXIST) {
				String deviceStateCode = codeMap.get(CODE_KEY_FAULT_CODE_SEARCH);
				if(deviceStateCode == null || deviceStateCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-故障码的编码不能为空！");
				deviceStateCode = deviceStateCode.trim();
				sbMsg.append(deviceStateCode);
			}
			
			//离线位置数据
			if(searchCodeArry[1] == CODE_EXIST) {
				String offlinepositionCode = codeMap.get(CODE_KEY_OFFLINE_POSISTION_SEARCH);
				if(offlinepositionCode == null || offlinepositionCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-离线位置数据的编码不能为空！");
				offlinepositionCode = offlinepositionCode.trim();
				sbMsg.append(offlinepositionCode);
			}
			
			//离线行程单
			if(searchCodeArry[2] == CODE_EXIST) {
				String offlinetravelCode = codeMap.get(CODE_KEY_OFFLINE_TRAVEL_SEARCH);
				if(offlinetravelCode == null || offlinetravelCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-离线行程单的编码不能为空！");
				offlinetravelCode = offlinetravelCode.trim();
				sbMsg.append(offlinetravelCode);
			}
			
			//设备状态
			if(searchCodeArry[3] == CODE_EXIST) {
				String deviceStateCode = codeMap.get(CODE_KEY_DEVICE_STATE);
				if(deviceStateCode == null || deviceStateCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-设备状态的编码不能为空！");
				deviceStateCode = deviceStateCode.trim();
				sbMsg.append(deviceStateCode);
			}
			
			//车辆状态
			if(searchCodeArry[4] == CODE_EXIST) {
				String carStateCode = codeMap.get(CODE_KEY_CAR_STATE);
				if(carStateCode == null || carStateCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-车辆状态的编码不能为空！");
				carStateCode = carStateCode.trim();
				sbMsg.append(carStateCode);
			}
			
			//报警设置
			if(searchCodeArry[5] == CODE_EXIST) {
				String warnSettingCode = codeMap.get(CODE_KEY_WARN_SETTING);
				if(warnSettingCode == null || warnSettingCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-报警设置的编码不能为空！");
				warnSettingCode = warnSettingCode.trim();
				sbMsg.append(warnSettingCode);
			}
			
			//车辆运行状态
			if(searchCodeArry[6] == CODE_EXIST) {
				String carRunStateCode = codeMap.get(CODE_KEY_CAR_RUNING_STATE);
				if(carRunStateCode == null || carRunStateCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-车辆运行状态的编码不能为空！");
				carRunStateCode = carRunStateCode.trim();
				sbMsg.append(carRunStateCode);
			}
			
			//设备时间参数
			if(searchCodeArry[7] == CODE_EXIST) {
				//设备时间帧格式
				String deviceTimePatternCode = codeMap.get(CODE_KEY_DEVICE_TIME_PATTERN);
				if(deviceTimePatternCode == null || deviceTimePatternCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-设备时间帧格式的编码不能为空！");
				deviceTimePatternCode = deviceTimePatternCode.trim();
				sbMsg.append(deviceTimePatternCode);
			}
			
			//行程参数
			if(searchCodeArry[8] == CODE_EXIST) {
				//行程参数帧格式
				String distancePatternCode = codeMap.get(CODE_KEY_DISTANCE_PARAMETER_PATTERN);
				if(distancePatternCode == null || distancePatternCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-行程参数帧格式的编码不能为空！");
				distancePatternCode = distancePatternCode.trim();
				sbMsg.append(distancePatternCode);
			}
			
			//总里程
			if(searchCodeArry[9] == CODE_EXIST) {
				String totalMileageCode = codeMap.get(CODE_KEY_TOTAL_MILEAGE);
				if(totalMileageCode == null || totalMileageCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-总里程的编码不能为空！");
				totalMileageCode = totalMileageCode.trim();
				sbMsg.append(totalMileageCode);
			}
			
			//数据服务器参数
			if(searchCodeArry[10] == CODE_EXIST) {
				String dataServiceCode = codeMap.get(CODE_KEY_DATA_SERVICE_PARAMETER);
				if(dataServiceCode == null || dataServiceCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-数据服务器参数的编码不能为空！");
				dataServiceCode = dataServiceCode.trim();
				sbMsg.append(dataServiceCode);
			}
			
			//升级服务器参数
			if(searchCodeArry[11] == CODE_EXIST) {
				String updateServiceCode = codeMap.get(CODE_KEY_UPDATE_SERVICE_PARAMETER);
				if(updateServiceCode == null || updateServiceCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-升级服务器参数的编码不能为空！");
				updateServiceCode = updateServiceCode.trim();
				sbMsg.append(updateServiceCode);
			}
			
			//Protal服务器参数
			if(searchCodeArry[12] == CODE_EXIST) {
				String protalServiceCode = codeMap.get(CODE_KEY_PROTAL_SERVICE_PARAMETER);
				if(protalServiceCode == null || protalServiceCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-Protal服务器参数的编码不能为空！");
				protalServiceCode = protalServiceCode.trim();
				sbMsg.append(protalServiceCode);
			}
			
			//设备版本信息
			if(searchCodeArry[13] == CODE_EXIST) {
				String deviceVesrionCode = codeMap.get(CODE_KEY_DEVICE_VERSION_INFO);
				if(deviceVesrionCode == null || deviceVesrionCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-设备版本信息的编码不能为空！");
				deviceVesrionCode = deviceVesrionCode.trim();
				sbMsg.append(deviceVesrionCode);
			}
			
			//实时位置查询
			if(searchCodeArry[14] == CODE_EXIST) {
				String realtimepositionCode = codeMap.get(CODE_KEY_REALTIME_POSITION_SEARCH);
				if(realtimepositionCode == null || realtimepositionCode.trim().equals("")) 
					throw new RuntimeException(EXCEPTION_INFO_PRE + "-实时位置查询的编码不能为空！");
				realtimepositionCode = realtimepositionCode.trim();
				sbMsg.append(realtimepositionCode);
			}
			*/
		}
		
		return sbMsg.toString();
	}
	
	public static void main(String[] args) throws Exception {
		MessageObdSendDownHandler m = new MessageObdSendDownHandler();
		Map<String, String> map = new HashMap<String, String>();
		map.put(MessageObdSendDownHandler.CODE_KEY_BASE, "0001000000000000");
		map.put(MessageObdSendDownHandler.CODE_KEY_SETTING, "0000000000000100");
		map.put(MessageObdSendDownHandler.CODE_KEY_DEVICE_VERSION_INFO, "01010102010301040105");
//		System.out.println("下发指令：" + m.createMsg("88888888", map));
//		System.out.println(ByteUtil.decToHex(0));
	}
	
}
