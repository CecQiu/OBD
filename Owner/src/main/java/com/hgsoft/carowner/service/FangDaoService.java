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

/**
 * @author liujialin
 * 19.	布防撤防设置
 */
@Service
public class FangDaoService {
	private final Log logger = LogFactory.getLog(FangDaoService.class);

	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	/**
	 *  防盗设置
	 * @param obdSn 设备Sn
	 * @param state 00 撤防 01设防
	 * @return 00成功 01失败 其他保留
	 */
	public String fangdaoSet(String obdSn,String state){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.guardSet");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************8018流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		try {
			//向客户端发送请求消息
			msgSendUtil.msgSend(obdSn,common,serialNumber,state);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(e);
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
		logger.info(resultStr+"*************************8018客户端响应结果");
		if(resultStr!=null){
			respMap.remove(msgId);//获取响应消息后，清除对应的消息
		}
		return resultStr;
	}
}
