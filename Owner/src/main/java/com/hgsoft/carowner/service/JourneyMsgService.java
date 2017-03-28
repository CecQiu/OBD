/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * @author liujialin
 * 请求行程信息
 * 20150512
 */
@Service
public class JourneyMsgService extends BaseService<FaultUpload> {
	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	/**
	 * 
	 * @param obdId obd设备号
	 * @param travelNo 行程序号
	 * @return
	 */
	public String obdStudy(String obdSn,Integer travelNo){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.journeyMsg");
		String travelNum=StrUtil.strAppendByLen(travelNo.toString(), 1, "0");
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		try {
			msgSendUtil.msgSend(obdSn, dic.getTrueValue(),serialNumber, travelNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}	
