package com.hgsoft.obd.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.ExtensionDataEnum;
/**
 * 扩展数据上传处理
 * @author sujunguang
 * 2016年3月7日
 * 上午10:39:05
 */
@Service
public class MessageObdExtensionDataHandler implements IMessageObd {
	
	private static Logger extensionDataLogger = LogManager.getLogger("extensionDataLogger");
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		String obdSn = message.getId();
		extensionDataLogger.info("-------------"+obdSn+"---【设备上传通讯——扩展数据上传】---------------");
		extensionDataLogger.info("-------------"+obdSn+"-报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		extensionDataLogger.info("------------设备："+obdSn+"------------");
		String retrunMsgBody = "success";
		String[] cutStrs ;//截取结果数组
		String msgBody = message.getMsgBody();
		try {
			/**************常用数据begin****************/
			//命令字:0x00 ox04
//			String command = message.getCommand();
			
			//数据帧
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String dataFrame = cutStrs[0];
			msgBody = cutStrs[1];
			char bits[] = StrUtil.hexToBinary(dataFrame);
			
			if(bits[0] == '0'){//熄火后WIFI使用时间提醒（单位：分钟）
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String shutDownWiFiUseTimeStr = cutStrs[0];
				msgBody = cutStrs[1];
				Integer shutDownWiFiUseTime = Integer.valueOf(shutDownWiFiUseTimeStr, 16);
				String key = obdSn + ObdConstants.keySpilt + ExtensionDataEnum.OverWiFiUse;
				GlobalData.putExitsQueryDataToMap(key, shutDownWiFiUseTime);
				extensionDataLogger.info("<"+obdSn+">熄火后WIFI使用时间提醒（单位：分钟）:"+shutDownWiFiUseTimeStr+"->"+shutDownWiFiUseTime);
			}
			
			if(bits[1] == '0'){//实时流量开关
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
				String realTimeFlowStr = cutStrs[0];
				msgBody = cutStrs[1];
				Integer realTimeFlow = Integer.valueOf(realTimeFlowStr, 16);
				String key = obdSn + ObdConstants.keySpilt + ExtensionDataEnum.RealTimeFlow;
				GlobalData.putExitsQueryDataToMap(key, realTimeFlow);
				extensionDataLogger.info("<"+obdSn+">实时流量开关 1开 0关:"+realTimeFlowStr+"->"+realTimeFlow);
			}
			
			if(bits[2] == '0'){//WIFI流量查询
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 4);
				String wifiFlowStr = cutStrs[0];
				msgBody = cutStrs[1];
				Integer wifiFlow = Integer.valueOf(wifiFlowStr, 16);
				extensionDataLogger.info("<"+obdSn+">---WiFi 流量查询(B):"+wifiFlowStr+"->"+wifiFlow);
				String key = obdSn + ObdConstants.keySpilt + ExtensionDataEnum.WiFiFlow;
				GlobalData.putExitsQueryDataToMap(key, wifiFlow);
			}
			
		}catch(Exception e){
			extensionDataLogger.error(obdSn, e);
			return "error";
		}
		return retrunMsgBody;
	}

}
