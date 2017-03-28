package com.hgsoft.obd.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.carowner.entity.OBDServerParams;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.ExtensionDataEnum;
import com.hgsoft.obd.util.ExtensionDataQueryType;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
import com.hgsoft.obd.util.ServerParamsType;
/**
 * 服务器请求OBD查询数据
 * @author sujunguang
 * 2016年1月12日
 * 上午11:35:07
 */
@Service
public class ServerRequestQueryService {
	@Resource
	private SendUtil sendUtil;
	/**
	 * 读取故障码
	 * @param obdSn
	 * @return
	 */
	public List<FaultUpload> readFaultCode(String obdSn){
		//清空旧数据
//		GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_faultCode", null);
		String msgBody = ObdConstants.Server_RequestFaultCode_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			String key = obdSn+ ObdConstants.keySpilt + ObdConstants.FaultCode;
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum,	msgBody,key,null);
			List<FaultUpload> faultUploads = new ArrayList<>();
			//4、车辆不在线，不能进行体检
			//3、ECU故障，未能正常体检
			if(GlobalData.OBD_PositionECUWarn.containsKey(obdSn)){
				if(GlobalData.OBD_PositionECUWarn.get(obdSn)){
					FaultUpload faultCode = new FaultUpload();
					faultCode.setState("3");
					faultUploads.add(faultCode);
					return faultUploads;
				}
			}
			//2、车速不为0，不能进行体检
			if(GlobalData.OBD_PositionCarSpeed.containsKey(obdSn)){
				if(0 != GlobalData.OBD_PositionCarSpeed.get(obdSn)){
					FaultUpload faultCode = new FaultUpload();
					faultCode.setState("2");
					faultUploads.add(faultCode);
					return faultUploads;
				}
			}
			//5、超时
			if (obj == null) {
				FaultUpload faultCode = new FaultUpload();
				faultCode.setState("5");
				faultUploads.add(faultCode);
				return faultUploads;
			}
			//0、无故障码等
			if(obj instanceof FaultUpload){
				faultUploads.add((FaultUpload)obj);
				return faultUploads;
			}
			//1、有故障码
			if (obj instanceof List) {
			    faultUploads = (List<FaultUpload>) obj;
				return faultUploads;
			}
			
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 请求离线数据  目前APP不会主动获取
	 * packetNo 包序号 00 00
	 * @return
	 */
	public String offData(String obdSn,Integer packetNo, boolean hasResult){
		if(packetNo == null){
			packetNo = 0;
		}
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		//加包序号
		String pNo = StrUtil.strAppend(Integer.toHexString(packetNo),4,0, "0");
		String data = ObdConstants.Server_RequestOffData_OBD_Cmd + pNo;
		try {
			//存储已发送的包序号
			String key = obdSn+"_offData_packetNo";
			GlobalData.OBD_PacketNum.put(key, packetNo);
			if(hasResult){
				String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data,2.0);
				return result;
			}else{
				//无须等待结果，直接发送
				sendUtil.msgSend(obdSn, serialNum, data);
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 离线行程单  目前APP不会主动获取
	 * @param obdSn
	 * @param packetNo
	 * @return
	 */
	public String offTravel(String obdSn,Integer packetNo, boolean hasResult){
		if(packetNo == null){
			packetNo = 0;
		}
		String pNo = StrUtil.strAppend(Integer.toHexString(packetNo),4,0, "0");
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		//加包序号
		String data = ObdConstants.Server_RequestOffTravel_OBD_Cmd + pNo;
		try {
			if(hasResult){
				String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data,2.0);
				return result;
			}else{//无须等待结果，直接发送
				sendUtil.msgSend(obdSn, serialNum, data);
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 设备状态
	 * @param obdSn
	 * @return
	 */
	public ObdHandShake deviceState(String obdSn){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestDeviceState_OBD_Cmd;
		String key = obdSn+ ObdConstants.keySpilt + ObdConstants.DeviceState;
		try {
			ObdHandShake obdHandShake = (ObdHandShake)sendUtil.msgSendGetResult(obdSn, serialNum, data,key, null);
			return obdHandShake;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 车辆状态
	 * @param obdSn
	 * @return
	 */
	public ObdHandShake carState(String obdSn){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestCarState_OBD_Cmd;
		String key = obdSn+ ObdConstants.keySpilt + ObdConstants.CarState;
		try {
			ObdHandShake obdHandShake = (ObdHandShake) sendUtil.msgSendGetResult(obdSn, serialNum, data,key, null);
			return obdHandShake;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 报警设置
	 * @param obdSn
	 * @return
	 */
	public ObdHandShake warnSettings(String obdSn){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestWarnSetting_OBD_Cmd;
		String key = obdSn+ ObdConstants.keySpilt + ObdConstants.WarnSet;
		try {
			ObdHandShake obdHandShake = (ObdHandShake) sendUtil.msgSendGetResult(obdSn, serialNum, data,key,null);
			return obdHandShake;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 车辆运行状态——去除
	 * @param obdSn
	 * @return
	 */
	/*public String carRunState(String obdSn){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestCarRunState_OBD_Cmd;
		try {
			String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data,null);
			return result;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * 设备时间参数
	 * @param obdSn
	 * @param bits 数据帧
	 * @return
	 */
	public OBDTimeParams deviceTimeParams(String obdSn,char[] bits){
		try {
		String dataFrame = StrUtil.binary2Hex(bits);
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestDeviceTimeParams_OBD_Cmd + dataFrame;
		String key = obdSn + ObdConstants.keySpilt + ObdConstants.DeviceTimeParams;
		
			Object result = sendUtil.msgSendGetResult(obdSn, serialNum, data, key, null);
			if(result instanceof OBDTimeParams){
				return (OBDTimeParams) result;
			}
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 行程参数
	 * @param obdSn
	 * @param bits 数据帧 16bit
	 * @return
	 */
	public OBDTravelParams travelTimeParams(String obdSn,char[] bits){
		try {
			String dataFrame = StrUtil.binary2Hex(bits);
			Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
			String data = ObdConstants.Server_RequestTravelParams_OBD_Cmd + dataFrame;
			String key = obdSn + ObdConstants.keySpilt + ObdConstants.TravelParams;
			Object result = sendUtil.msgSendGetResult(obdSn, serialNum, data, key,null);
			if(result instanceof OBDTravelParams){
				return (OBDTravelParams) result;
			}
		} catch (OBDException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询半条行程
	 * @param obdSn
	 * @return
	 */
	public String halfTravel(String obdSn){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestTotalMiles_OBD_Cmd;
		String key = obdSn + ObdConstants.keySpilt + ObdConstants.HalfTravel;
		try {
			String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data,key, null);
			return result;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询服务器参数
	 * @param obdSn
	 * @param type 数据，升级，portal
	 * @return
	 * @throws OBDException 
	 */
	public OBDServerParams serverParams(String obdSn,ServerParamsType type) throws OBDException{
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = "";
		String key = obdSn+ObdConstants.keySpilt;
		switch (type) {
			case DATA:
				data = ObdConstants.Server_RequestDataServerParams_OBD_Cmd;
				key += ObdConstants.DataServerParams;
				break;
			case UPGRADE:
				data = ObdConstants.Server_RequestUpgradeServerParams_OBD_Cmd;
				key += ObdConstants.UpgradeServerParams;
				break;
			case PORTAL:
				data = ObdConstants.Server_RequestPortalServerParams_OBD_Cmd;
				key += ObdConstants.PortalServerParams;
				break;
			default:
				throw new OBDException("没有对应的服务器参数："+type);
		}
		try {
			Object result = sendUtil.msgSendGetResult(obdSn, serialNum, data,key, null);
			if(result instanceof OBDServerParams){
				return (OBDServerParams) result;
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 设备版本信息
	 * @param obdSn
	 * @return
	 */
	public OBDDeviceVersion deviceVersion(String obdSn){
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestDeviceVersion_OBD_Cmd;
		String key = obdSn + ObdConstants.keySpilt + ObdConstants.ObdDeviceVersion;
		try {
			Object result = sendUtil.msgSendGetResult(obdSn, serialNum, data, key, null);
			if(result instanceof OBDDeviceVersion){
				return (OBDDeviceVersion) result;
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 实时位置查询
	 * @param obdSn
	 * @return
	 */
	public String realTimeLoc(String obdSn){
		System.out.println("realTimeLoc...");
		String msgBody = ObdConstants.Server_RequestRealTimeLoc_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			String key = obdSn+ObdConstants.keySpilt+ObdConstants.RealTimeLoc;
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody,key,null);
			System.out.println("realTimeLoc...:"+obj);
			
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * WiFi流量查询 
	 * @param obdSn
	 * @return 
	 */
	public Long wifiFlow(String obdSn){
		String msgBody = ObdConstants.Server_RequestExtension_OBD_Cmd;//临时用
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			String key = obdSn+ObdConstants.keySpilt+ExtensionDataEnum.WiFiFlow;//临时用
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody,key,null);
			return Long.valueOf(obj.toString());
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 扩展帧查询
	 * @param obdSn
	 * @param extensionDataEnum 扩展帧类型
	 * @return
	 * @throws OBDException 
	 */
	public Object extensionData(String obdSn,ExtensionDataEnum extensionDataEnum) throws OBDException{
		String msgBody = ObdConstants.Server_RequestExtension_OBD_Cmd;
		switch (extensionDataEnum) {
		case OverWiFiUse:
			msgBody += "fffe";//1111 1110
			break;
		case RealTimeFlow://1111 1101
			msgBody += "fffd";
			break;
		case WiFiFlow://1111 1011
			msgBody += "fffb";
			break;
		default:
			throw new OBDException("扩展数据帧没有对应的数据！");
		}
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			String key = obdSn+ObdConstants.keySpilt+extensionDataEnum;
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody,key,null);
			return obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 扩展帧2查询
	 * @param obdSn
	 * @param extensionDataQueryType 扩展帧查询类型
	 * @param offset 查询黑白名单起始号，其他则null
	 * @param count 查询黑白名单个数，其他则null
	 * @return 开关：00-关 01-开
	 * @throws OBDException 
	 */
	public Object extension2Data(String obdSn,ExtensionDataQueryType extensionDataQueryType,Integer offset,Integer count) throws OBDException{
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		int length = 1;
		String lengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
		String msgBody = ObdConstants.Server_RequestExtension2_OBD_Cmd + lengthStr + extensionDataQueryType.getValue();
		Object obj = null;
		if(extensionDataQueryType == ExtensionDataQueryType.DomainBlack || extensionDataQueryType == ExtensionDataQueryType.DomainWhite){
			if(offset == null || count == null || count < 0){
				throw new OBDException("参数不正确！请检查~offset:"+offset+","+count+":"+count);
			}
			msgBody += StrUtil.strAppend(Integer.toHexString(offset), 2, 0, "0");
			msgBody += StrUtil.strAppend(Integer.toHexString(count), 2, 0, "0");
			//结果不从这里等待获取：需要组装数据
			obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody, null);
		}else{
			String key = obdSn+ObdConstants.keySpilt+ObdConstants.ExtensionDataQuery;
			obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody, key, null);
		}
		return obj;
	}
}
