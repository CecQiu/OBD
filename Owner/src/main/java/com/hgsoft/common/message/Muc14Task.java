package com.hgsoft.common.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.service.ServerResponses;
/**
 * AGPS时间更新，作废
 * @author Administrator
 *
 */
@Service
public class Muc14Task {
	private final Log logger = LogFactory.getLog(Muc14Task.class);
	
	/**
	 * 程序入库
	 * @param om 请求消息对象
	 * @throws Exception 
	 */
	public String entrance(OBDMessage om) throws Exception{
		//消息体
		String msg = om.getMsgBody();//成功标志
		ServerResponses sr=new ServerResponses();
		//本地AGPS数据包更新时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String AGPSDate = "20" + msg;
		Date AGPSTime=sdf.parse(AGPSDate);
		
		String str="";
		try {
			str = sr.recvACK(om.getId(), om.getWaterNo(), om.getCommand(), 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
		return str;
	}
}
