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
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;

/**
 * @author liujialin
 * 读故障码
 */
@Service
public class FaultCodeReadService extends BaseService<FaultUpload> {
	private static Log logger = LogFactory.getLog(FaultCodeReadService.class);
	
	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	/**
	 * 读故障码 逻辑处理
	 * @param obdSn obd设备号
	 * @return
	 */
	public String faultCodeRead(String obdSn){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.faultCodeRead");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		//确定唯一消息
		String msgId=obdSn+"_"+common;
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		//发送请求给obd以及线程获取obd返回结果.
		String faultCodeList=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, null, msgId);
		logger.info(obdSn+"*********读故障码返回结果:"+faultCodeList);
		respMap.remove(msgId);//清除记录
		return faultCodeList;
	}
}
