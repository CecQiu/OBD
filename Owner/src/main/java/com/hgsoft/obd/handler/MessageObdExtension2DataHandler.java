package com.hgsoft.obd.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.util.ExtensionDataEnum;
import com.hgsoft.obd.util.ExtensionDataQueryType;
/**
 * 扩展2数据上传处理
 * @author sujunguang
 * 2016年7月21日
 * 下午5:41:33
 */
@Service
public class MessageObdExtension2DataHandler implements IMessageObd {
	
	private static Logger extensionDataLogger = LogManager.getLogger("extensionDataLogger");
	
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		final String obdSn = message.getId();
		extensionDataLogger.info("-------------"+obdSn+"---【设备上传通讯——扩展2数据上传】---------------");
		extensionDataLogger.info("-------------"+obdSn+"-报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		extensionDataLogger.info("------------设备："+obdSn+"------------");
		String retrunMsgBody = "success";
		String[] cutStrs ;//截取结果数组
		String msgBody = message.getMsgBody();
		try {
			/**************常用数据begin****************/
			//命令字:0x00 ox04
//			String command = message.getCommand();
			Map<ExtensionDataQueryType,String> extensionDataQueryTypeMap = new HashMap<>();
			//参数总数
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
			String totalParams = cutStrs[0];
			msgBody = cutStrs[1];
			Integer _totalParams = Integer.valueOf(totalParams, 16);
			extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】参数总数:"+totalParams+"->"+_totalParams);
			for (int i = 0; i < _totalParams; i++) {
				cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
				String paramKey = cutStrs[0];
				msgBody = cutStrs[1];
				ExtensionDataQueryType extensionDataQueryType = ExtensionDataQueryType.getDomainSetTypeByValue(paramKey);
				extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】参数类型:"+paramKey+"->"+extensionDataQueryType);
				switch (extensionDataQueryType) {
				case SleepVolt:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String sleepVoltValue = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, sleepVoltValue);
					extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】休眠电压差值:"+sleepVoltValue);
					break;
					
				case SleepOverSpeed:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
					String sleepOverSpeedValue = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, sleepOverSpeedValue);
					extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】休眠加速度差值:"+sleepOverSpeedValue);
					break;
					
				case DomainWhite:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String domainWhiteCountStr = cutStrs[0];
					msgBody = cutStrs[1];
					final Integer domainWhiteCount = Integer.valueOf(domainWhiteCountStr, 16);
					extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】白名单个数:"+domainWhiteCountStr+"->"+domainWhiteCount);
					if(domainWhiteCount >0 && !"ff".equals(domainWhiteCountStr)){
						cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
						String offsetStr = cutStrs[0];
						msgBody = cutStrs[1];
						final Integer offset = Integer.valueOf(offsetStr, 16);
						extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】白名单起始序号:"+offsetStr+"->"+offset);
						//计算是否还有剩余名单 0 1 2 3 4 5 6 7 8 9 10 11
						final Integer nextOffsetWhite =  offset+GlobalData.DomainCount;
						if(nextOffsetWhite < domainWhiteCount){
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】白名单再次获取下一起始号->"+nextOffsetWhite);
										int nextCount = domainWhiteCount-nextOffsetWhite;
										if(nextCount >= GlobalData.DomainCount){
											nextCount = 4;
										}
										serverRequestQueryService.extension2Data(obdSn, ExtensionDataQueryType.DomainWhite, nextOffsetWhite,nextCount);
									} catch (OBDException e) {
										extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】白名单再次获取下一起始号->"+nextOffsetWhite+",异常"+e);
									}
								}
							}).start();
						}
						String domainWhite = "";
						for (int j = 0; j < GlobalData.DomainCount; j++) {
							if(StringUtils.isEmpty(msgBody)){
								break;
							}
							cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
							String domainWhiteLengthStr = cutStrs[0];
							msgBody = cutStrs[1];
							Integer domainWhiteLength = Integer.valueOf(domainWhiteLengthStr, 16);
							cutStrs = StrUtil.cutStrByByteNum(msgBody, domainWhiteLength);
							String domainWhiteStr = cutStrs[0];
							msgBody = cutStrs[1];
							domainWhite += StrUtil.hex2ASCII(domainWhiteStr) + ";";
						}
						extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】白名单:"+domainWhite);
						GlobalData.putQueryDataMap(obdSn,offset/GlobalData.DomainCount+1,domainWhite);
						if(domainWhiteCount == nextOffsetWhite+GlobalData.DomainCount || domainWhiteCount-offset <= GlobalData.DomainCount){
							//全部取到
							GlobalData.putQueryDataMap(obdSn,0,"");
						}
				    }
					break;
					
				case DomainBlack:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String domainBlackCountStr = cutStrs[0];
					msgBody = cutStrs[1];
					final Integer domainBlackCount = Integer.valueOf(domainBlackCountStr, 16);
					extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】黑名单个数:"+domainBlackCountStr+"->"+domainBlackCount);
					if(domainBlackCount >0  && !"ff".equals(domainBlackCountStr)){
						cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
						String offsetBlackStr = cutStrs[0];
						msgBody = cutStrs[1];
						final Integer offsetBlack = Integer.valueOf(offsetBlackStr, 16);
						extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】黑名单起始序号:"+offsetBlackStr+"->"+offsetBlack);
						//计算是否还有剩余名单
						final Integer nextOffsetBlack = offsetBlack+GlobalData.DomainCount;
						if(nextOffsetBlack < domainBlackCount){
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】黑名单再次获取下一起始号->"+nextOffsetBlack);
										int nextCount = domainBlackCount-nextOffsetBlack;
										if(nextCount >= GlobalData.DomainCount){
											nextCount = 4;
										}
										serverRequestQueryService.extension2Data(obdSn, ExtensionDataQueryType.DomainBlack, nextOffsetBlack, nextCount);
									} catch (OBDException e) {
										extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】黑名单再次获取下一起始号->"+nextOffsetBlack+",异常"+e);
									}
								}
							}).start();
						}
						String domainBlack = "";
						for (int j = 0; j < GlobalData.DomainCount; j++) {
							if(StringUtils.isEmpty(msgBody)){
								break;
							}
							cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
							String domainBlackLengthStr = cutStrs[0];
							msgBody = cutStrs[1];
							Integer domainBlackLength = Integer.valueOf(domainBlackLengthStr, 16);
							cutStrs = StrUtil.cutStrByByteNum(msgBody, domainBlackLength);
							String domainBlackStr = cutStrs[0];
							msgBody = cutStrs[1];
							domainBlack += StrUtil.hex2ASCII(domainBlackStr) + ";";
						}
						extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】黑名单:"+domainBlack);
						GlobalData.putQueryDataMap(obdSn,offsetBlack/GlobalData.DomainCount+1,domainBlack);
						if(domainBlackCount == nextOffsetBlack+GlobalData.DomainCount || domainBlackCount-offsetBlack <= GlobalData.DomainCount){
							//全部取到
							GlobalData.putQueryDataMap(obdSn,0,"");
						}
					}
					break;
					
				case DomainWhiteSwitch:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String domainWhiteSwitch = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, domainWhiteSwitch);
					extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】白名单开关:"+domainWhiteSwitch);
					break;
					
				case DomainBlackSwitch:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String domainBlackSwitch = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, domainBlackSwitch);
					extensionDataLogger.info("<"+obdSn+">【设备上传通讯——扩展2数据上传】黑名单开关:"+domainBlackSwitch);
					break;

				case CarTypeSetting:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String settingLengthStr = cutStrs[0];
					msgBody = cutStrs[1];
					Integer settingLength = Integer.valueOf(settingLengthStr, 16);
					cutStrs = StrUtil.cutStrByByteNum(msgBody, settingLength);
					String carSetting = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, carSetting);
					break;
					
				case LowVoltSleepValue:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String lowVoltSleepValueStr = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, lowVoltSleepValueStr);
					break;

				case NetModel:
					cutStrs = StrUtil.cutStrByByteNum(msgBody, 1);
					String netModelLengthStr = cutStrs[0];
					msgBody = cutStrs[1];
					Integer netModelLength = Integer.valueOf(netModelLengthStr, 16);
					cutStrs = StrUtil.cutStrByByteNum(msgBody, netModelLength);
					String netModel = cutStrs[0];
					msgBody = cutStrs[1];
					extensionDataQueryTypeMap.put(extensionDataQueryType, netModel);
					break;
					
				default:
					break;
				}
			}
			String key = obdSn + ObdConstants.keySpilt + ObdConstants.ExtensionDataQuery;
			GlobalData.putExitsQueryDataToMap(key, extensionDataQueryTypeMap);
		}catch(Exception e){
			extensionDataLogger.error("<"+obdSn+">【设备上传通讯——扩展2数据上传】", e);
			return "error";
		}
		return retrunMsgBody;
	}

}
