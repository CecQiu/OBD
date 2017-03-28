package com.hgsoft.obd.util;

import org.springframework.stereotype.Component;

import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
/**
 * 服务器请求OBD查询数据
 * @author sujunguang
 * 2016年1月12日
 * 上午11:35:07
 */
@Component
public class ServerRequestQueryUtil {
	/**
	 * 读取故障码
	 * @param obdSn
	 * @return 【读取故障码】消息体
	 */
	public String readFaultCode(String obdSn){
		String msgBody = ObdConstants.Server_RequestFaultCode_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * 请求离线数据  目前APP不会主动获取
	 * packetNo 包序号 00 00
	 * @return 【请求离线数据】消息体
	 */
	public String offData(String obdSn,Integer packetNo, boolean hasResult){
		if(packetNo == null){
			packetNo = 0;
		}
		//加包序号
		String pNo = StrUtil.strAppend(Integer.toHexString(packetNo),4,0, "0");
		String msgBody = ObdConstants.Server_RequestOffData_OBD_Cmd + pNo;
		return msgBody;
	}
	
	/**
	 * 离线行程单  目前APP不会主动获取
	 * @param obdSn
	 * @param packetNo
	 * @return 【离线行程单】消息体
	 */
	public String offTravel(String obdSn,Integer packetNo, boolean hasResult){
		if(packetNo == null){
			packetNo = 0;
		}
		String pNo = StrUtil.strAppend(Integer.toHexString(packetNo),4,0, "0");
		//加包序号
		String msgBody = ObdConstants.Server_RequestOffTravel_OBD_Cmd + pNo;
		return msgBody;
	}
	
	/**
	 * 设备状态
	 * @param obdSn
	 * @return 【设备状态】消息体
	 */
	public String deviceState(String obdSn){
		String msgBody = ObdConstants.Server_RequestDeviceState_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * 车辆状态
	 * @param obdSn
	 * @return 【车辆状态】消息体
	 */
	public String carState(String obdSn){
		String msgBody = ObdConstants.Server_RequestCarState_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * 报警设置
	 * @param obdSn
	 * @return 【报警设置】消息体
	 */
	public String warnSettings(String obdSn){
		String msgBody = ObdConstants.Server_RequestWarnSetting_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * 设备时间参数
	 * @param obdSn
	 * @param bits 数据帧
	 * @return 【设备时间参数】消息体
	 * @throws Exception 
	 */
	public String deviceTimeParams(String obdSn,char[] bits) throws Exception{
		String dataFrame = StrUtil.binary2Hex(bits);
		String msgBody = ObdConstants.Server_RequestDeviceTimeParams_OBD_Cmd + dataFrame;
		return msgBody;
	}
	
	/**
	 * 行程参数
	 * @param obdSn
	 * @param bits 数据帧 16bit
	 * @return 【行程参数】消息体
	 * @throws Exception 
	 */
	public String travelTimeParams(String obdSn,char[] bits) throws Exception{
		String dataFrame = StrUtil.binary2Hex(bits);
		String msgBody = ObdConstants.Server_RequestTravelParams_OBD_Cmd + dataFrame;
		return msgBody;
	}
	
	/**
	 * 查询半条行程
	 * @param obdSn
	 * @return 【查询半条行程】消息体
	 */
	public String halfTravel(String obdSn){
		String msgBody = ObdConstants.Server_RequestTotalMiles_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * 查询服务器参数
	 * @param obdSn
	 * @param type 数据，升级，portal
	 * @return 【查询服务器参数】消息体
	 * @throws OBDException 
	 */
	public String serverParams(String obdSn,ServerParamsType type) throws OBDException{
		String msgBody = "";
		switch (type) {
			case DATA:
				msgBody = ObdConstants.Server_RequestDataServerParams_OBD_Cmd;
				break;
			case UPGRADE:
				msgBody = ObdConstants.Server_RequestUpgradeServerParams_OBD_Cmd;
				break;
			case PORTAL:
				msgBody = ObdConstants.Server_RequestPortalServerParams_OBD_Cmd;
				break;
			default:
				throw new OBDException("没有对应的服务器参数："+type);
		}
		return msgBody;
	}
	
	/**
	 * 设备版本信息
	 * @param obdSn
	 * @return 【设备版本信息】消息体
	 */
	public String deviceVersion(String obdSn){
		String msgBody = ObdConstants.Server_RequestDeviceVersion_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * 实时位置查询
	 * @param obdSn
	 * @return 【实时位置查询】消息体
	 */
	public String realTimeLoc(String obdSn){
		String msgBody = ObdConstants.Server_RequestRealTimeLoc_OBD_Cmd;
		return msgBody;
	}
	
	/**
	 * WiFi流量查询 
	 * @param obdSn
	 * @return 【WiFi流量查询 】消息体
	 */
	public String wifiFlow(String obdSn){
		String msgBody = ObdConstants.Server_RequestExtension_OBD_Cmd;//临时用
		return msgBody;
	}

	/**
	 * 
	 * 扩展帧查询
	 * @param obdSn
	 * @param extensionDataEnum 扩展帧类型
	 * @return 【扩展帧查询】消息体
	 * @throws OBDException 
	 */
	public String extensionData(String obdSn,ExtensionDataEnum extensionDataEnum) throws OBDException{
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
		return msgBody;
	}
	
	/**
	 * 扩展帧2查询
	 * @param obdSn
	 * @param extensionDataQueryType 扩展帧查询类型
	 * @param offset 查询黑白名单起始号，其他则null
	 * @param count 查询黑白名单个数，其他则null
	 * @return 【扩展帧2查询】消息体
	 * @throws OBDException 
	 */
	public String extension2Data(String obdSn,ExtensionDataQueryType extensionDataQueryType,Integer offset,Integer count) throws OBDException{
		int length = 1;
		String lengthStr = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
		String msgBody = ObdConstants.Server_RequestExtension2_OBD_Cmd + lengthStr + extensionDataQueryType.getValue();
		if(extensionDataQueryType == ExtensionDataQueryType.DomainBlack || extensionDataQueryType == ExtensionDataQueryType.DomainWhite){
			if(offset == null || count == null || count < 0){
				throw new OBDException("参数不正确！请检查~offset:"+offset+","+count+":"+count);
			}
			msgBody += StrUtil.strAppend(Integer.toHexString(offset), 2, 0, "0");
			msgBody += StrUtil.strAppend(Integer.toHexString(count), 2, 0, "0");
		}
		return msgBody;
	}
}
