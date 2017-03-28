/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;

/**
 * @author liujialin
 * 0x80 0x15 AGPS判断本地AGPS数据时间是否最新
 * 20150812
 */
@Service
public class AGPSIsNewService {
	private final Log logger = LogFactory.getLog(AGPSIsNewService.class);
	
	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	public boolean AGPSIsNew(String obdSn,String result){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.AGPSIsNew");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		try {
			msgSendUtil.msgSend(obdSn, common,serialNumber, result);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}
}
