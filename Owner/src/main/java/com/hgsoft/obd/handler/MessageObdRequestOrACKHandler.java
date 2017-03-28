package com.hgsoft.obd.handler;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.common.utils.ThreadLocalDateUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.service.AGPSHandlerService;
import com.hgsoft.obd.service.AGPSService;
import com.hgsoft.obd.service.AgnssHandleService;
import com.hgsoft.obd.service.UpgradeService;
import com.hgsoft.obd.util.SendUtil;

/**
 * 设备上传通讯——设备请求数据/ACK应答
 * @author sujunguang
 * 2015年12月12日
 * 下午4:26:20
 */
@Service
public class MessageObdRequestOrACKHandler implements IMessageObd{

	private static Logger obdHandlerACKLogger = LogManager.getLogger("obdHandlerACKLogger");
	@Resource
	private UpgradeService upgradeService;
	@Resource
	private AGPSService agpsService;
	@Resource
	private AgnssHandleService agnssHandleService;
	@Resource
	private SendUtil sendUtil;
	@Resource
	private AGPSHandlerService agpsHandlerService; 
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		String obdSn = message.getId();
		obdHandlerACKLogger.info("----------------设备上传通讯——设备请求数据/ACK应答---------------");
		obdHandlerACKLogger.info("-----------"+obdSn+"---报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		obdHandlerACKLogger.info("------------设备："+message.getId()+"------------");
		
		String retrunMsgBody = "success";
		String[] cutStrs ;//截取结果数组
		String msgBody = message.getMsgBody();
		try {
			//命令字
//			String command = message.getCommand();
			
			//数据帧格式
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String dataFrame = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerACKLogger.info("<"+obdSn+">设备请求数据/ACK应答帧："+dataFrame);
			
			char[] bits = StrUtil.hexToBinary(dataFrame);
			//固件升级包请求:0-存在 TODO 已经去除！使用旧协议
			if('0' == bits[0]){
				//包序号
//				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
//				String packetNo = cutStrs[0];
//				msgBody = cutStrs[1];
//				Integer _packetNo = Integer.valueOf(packetNo,16);
//				obdHandlerACKLogger.info("固件升级包请求——包序号："+packetNo+"->"+_packetNo);
//				/**
//				 * 给设备发送升级包数据
//				 */
//				String obdSn = message.getId();
//				sendUpgradeData(obdSn,_packetNo);
			}
			//AGPS升级包请求:0-存在
			if('0' == bits[1]){
				//包序号
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String packetNo = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _packetNo = Integer.valueOf(packetNo,16);
				obdHandlerACKLogger.info("<"+obdSn+">AGPS升级包请求——包序号："+packetNo+"->"+_packetNo);
				/**
				 * 给设备发送AGPS包数据
				 */
				Integer serialNum = Integer.valueOf(message.getWaterNo(),16);
				sendAGPSData(obdSn,serialNum,_packetNo);
			}
			//请求服务器时间：1-请求
			if('1' == bits[2]){
				//服务器时间
				Integer _waterNo = Integer.valueOf(message.getWaterNo(),16);
				sendServerDate(obdSn,_waterNo);
			}
			//AAGNSS升级包请求:0-存在
			if('0' == bits[6]){
				//包序号
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String packetNo = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _packetNo = Integer.valueOf(packetNo,16);
				obdHandlerACKLogger.info("<"+obdSn+">AGNSS包请求——包序号："+packetNo+"->"+_packetNo);
				/**
				 * 给设备发送AGNSS包数据
				 */
				Integer serialNum = Integer.valueOf(message.getWaterNo(),16);
				sendAGNSSData(obdSn,serialNum,_packetNo);
			}
			
			//设备ACK应答:0-存在
			if('0' == bits[7]){
				//接收消息的流水号
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String waterNo = cutStrs[0];
				msgBody = cutStrs[1];
				Integer _waterNo = Integer.valueOf(waterNo,16);
				//命令执行状态
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String cmdState = cutStrs[0];
				msgBody = cutStrs[1];
				obdHandlerACKLogger.info("<"+obdSn+">设备ACK应答——接收消息的流水号："+waterNo+"->"+_waterNo);
				GlobalData.OBD_ACK_OR_QueryData.put(obdSn +"_"+ _waterNo, cmdState);// TODO
//				GlobalData.putExitsQueryDataToMap(message.getId() +"_"+ _waterNo, cmdState);// TODO
			}
			
		} catch (Exception e){
			obdHandlerACKLogger.error(obdSn, e);
			e.printStackTrace();
			return "error";
		}
		return retrunMsgBody;
	}
	
	/**
	 * 发送服务器时间到设备
	 * @param obdSn
	 * @throws OBDException 
	 */
	private void sendServerDate(String obdSn,Integer serialNum) throws OBDException {
		obdHandlerACKLogger.info("<"+obdSn+">---给设备服务器时间数据---");
		String serverDate = ThreadLocalDateUtil.formatDate("yyyyMMddHHmmss", new Date()).substring(2);//服务器时间返回格式如:1512151512
		obdHandlerACKLogger.info("<"+obdSn+">---服务器时间："+serverDate);
		String msg = ObdConstants.Server_ResponseServerTime_OBD_Cmd + serverDate;
		sendUtil.msgSend(obdSn, serialNum,msg,null);
		obdHandlerACKLogger.info("<"+obdSn+">---给设备服务器时间数据："+msg);
	}

	/**
	 * 发送AGPS数据到设备
	 * @param obdSn
	 * @param _packetNo
	 * @throws Exception 
	 */
	private void sendAGPSData(String obdSn,Integer serialNum, Integer _packetNo) throws Exception {
		obdHandlerACKLogger.info("<"+obdSn+">---给设备AGPS升级包数据---");
//		String agpsData = agpsService.getAgpsData(obdSn, _packetNo);
		String agpsData = agpsHandlerService.handler(obdSn, _packetNo);
		String msg = ObdConstants.Server_ResponseAGPS_OBD_Cmd + agpsData;
		obdHandlerACKLogger.info("<"+obdSn+">---AGPS数据为：" + msg);
		if(!StringUtils.isEmpty(agpsData)){
			sendUtil.msgSend(obdSn,serialNum, msg,null);
			obdHandlerACKLogger.info("<"+obdSn+">---给设备已经发送AGPS升级包数据---");
		}else{
			obdHandlerACKLogger.error("<"+obdSn+">---给设备发送AGPS升级包数据失败，数据为：" + msg);
		}
	}
	
	/**
	 * 发送AGPS数据到设备
	 * @param obdSn
	 * @param _packetNo
	 * @throws Exception 
	 */
	private void sendAGNSSData(String obdSn,Integer serialNum, Integer _packetNo) throws Exception {
		obdHandlerACKLogger.info("<"+obdSn+">---给设备AGNSS包数据---");
		String agnssData = agnssHandleService.getAgnssData(obdSn, _packetNo);
		String msg = ObdConstants.Server_ResponseAGNSS_OBD_Cmd + agnssData;
		obdHandlerACKLogger.info("<"+obdSn+">---AGNSS数据为：" + msg);
		if(!StringUtils.isEmpty(agnssData)){
			new SendUtil().msgSend(obdSn,serialNum, msg,null);
			obdHandlerACKLogger.info("<"+obdSn+">---给设备已经发送AGNSS包数据---");
		}else{
			obdHandlerACKLogger.error("<"+obdSn+">---给设备发送AGNSS包数据失败，数据为：" + msg);
		}
	}

}