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
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.MsgThread;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * @author liujialin
 * 监听
 * 20150812
 */
@Service
public class MonitorService {
	private final Log logger = LogFactory.getLog(MonitorService.class);
	
	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	/**
	 * 
	 * @param obdSn
	 * @param phoneNum 13512345678 十一位电话号码前面要加个0
	 * @return
	 */
	public String monitor(String obdSn,String phoneNum){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.monitor");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info("点名消息id******************"+msgId);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		//0x80 0x06 0x06 0x01 0x35 0x12 0x34 0x56 0x78
		String phone="0"+phoneNum;//电话号码
		String len=StrUtil.strAppendByLen(Integer.parseInt(phone.length()/2+"", 16)+"", 1, "0");//消息长度
		String msg=len+phone;
		logger.info(msg+"********************监听的请求消息体");
		try {
			msgSendUtil.msgSend(obdSn, common,serialNumber, msg);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
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
		String resultStr= (String) thread.getResMsg();
		if(resultStr!=null){
			respMap.remove(msgId);//获取响应消息后，清除对应的消息
		}
		return resultStr;
	}
}
