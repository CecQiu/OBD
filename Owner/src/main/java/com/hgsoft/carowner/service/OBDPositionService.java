/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.entity.PositionalInformation;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.MsgThread;
import com.hgsoft.common.utils.SerialNumberUtil;

/**
 * @author liujialin
 * 点名
 * 去掉不用
 */
@Service
public class OBDPositionService extends BaseService<FaultUpload> {
	private final Log logger = LogFactory.getLog(OBDPositionService.class);

	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	/**
	 * 点名接口逻辑处理方法
	 * @param obdSn obd设备号
	 * @return 位置信息
	 */
	public PositionalInformation OBDPosition(String obdSn){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.obdPosition");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		//确定唯一消息
//		String msgId=obdSn+"_"+common+"_"+serialNumber;
		String msgId=obdSn+"_"+common;
		logger.info("点名消息id******************"+msgId);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		try {
			msgSendUtil.msgSend(obdSn, common,serialNumber, null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
		MsgThread thread =new MsgThread(msgId);
		thread.start();
		try {
			//线程同步锁，顺序执行
			thread.join(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//获取到客户端的响应消息
		PositionalInformation pi= (PositionalInformation) thread.getResMsg();
		if(pi!=null){
			respMap.remove(msgId);//获取响应消息后，清除对应的消息
		}
		
		return pi;
	}
}	
