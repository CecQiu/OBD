/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.MsgThread;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * @author liujialin
 * 8003	参数查询
 */
@Service
public class CarParamQueryService{
	private final Log logger = LogFactory.getLog(CarParamQueryService.class);

	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	/**
	 * 
	 * @param obdSn
	 * @param ids 参数ID列表
	 * @return CarParam 车辆参数对象
	 */
	public CarParam carParamQuery(String obdSn,String[] ids)throws Exception{
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.paramQuery");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************8003流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		String idsLen=StrUtil.strAppendByLen(Integer.toHexString(ids.length),1,"0");
		StringBuffer sb = new StringBuffer("");
		for (String id : ids) {
			sb.append(id);
		}
		String msg = idsLen+sb.toString();
		try {
			//向客户端发送请求消息
			msgSendUtil.msgSend(obdSn,common,serialNumber,msg);
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
		CarParam carParam= (CarParam) thread.getResMsg();
		logger.info(carParam+"*************************8003客户端响应结果");
		if(carParam!=null){
			respMap.remove(msgId);//获取响应消息后，清除对应的消息
		}
		return carParam;
	}
}
