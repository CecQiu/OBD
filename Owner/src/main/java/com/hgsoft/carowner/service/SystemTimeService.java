/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.MsgThread;
import com.hgsoft.common.utils.SerialNumberUtil;

/**
 * @author liujialin
 * 系统时间服务类
 */
@Service
public class SystemTimeService {
	private final Log logger = LogFactory.getLog(SystemTimeService.class);
	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	public boolean systemTime(String obdSn) {
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		boolean flag = true;
		//获取命令字
		Dictionary dic = dictionaryService.getDicByCodeAndType("owner.command", "server.SystemTime");
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		String common = dic.getTrueValue();//命令字
//		String common = "8004";//命令字
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************系统时间KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		String date = (String) DateUtil.fromatDate(new Date(), "yyMMddHHmmss");
//		MsgSendUtil msgSendUtil = new MsgSendUtil();
		String resultStr=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, date, msgId);
		logger.info(obdSn+"****************8004系统时间响应结果:"+resultStr);
		respMap.remove(msgId);//获取响应消息后，清除对应的消息
		return flag;
	}
}	
