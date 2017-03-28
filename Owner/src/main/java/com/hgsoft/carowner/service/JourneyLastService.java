/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;

/**
 * @author liujialin
 * 最近的一次行程记录查询
 */
public class JourneyLastService extends BaseService<CarTraveltrack> {
	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	public String faultCodeClear(String obdSn){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.lastJourney");
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		try {
			msgSendUtil.msgSend(obdSn, dic.getTrueValue(),serialNumber, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
