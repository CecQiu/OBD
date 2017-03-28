package com.hgsoft.common.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.service.ServerResponses;

@Service
public class Muc16Task {
	private final Log logger = LogFactory.getLog(Muc16Task.class);
	
	/**
	 * 程序入库
	 * @param om 请求消息对象
	 * @throws Exception 
	 */
	public String entrance(OBDMessage om) throws Exception{
		//消息体
		String flag = om.getMsgBody();//成功标志
		logger.info(flag+"********************更新AGPS数据结果:"+om.getId());
		ServerResponses sr=new ServerResponses();
		String str="";
		try {
			str = sr.recvACK(om.getId(), om.getWaterNo(), om.getCommand(), 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			str = sr.recvACK(om.getId(), om.getWaterNo(), om.getCommand(), 0);
			throw new Exception();
		}
		return str;
	}
}
