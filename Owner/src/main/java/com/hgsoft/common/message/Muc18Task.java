package com.hgsoft.common.message;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.service.ServerResponses;

@Service
public class Muc18Task {
	private final Log logger = LogFactory.getLog(Muc18Task.class);

	/**
	 * 程序入库
	 * @param om 请求消息对象
	 * @throws Exception 
	 */
	public String entrance(OBDMessage om) throws Exception{
//		boolean flag=carTestService.carTestSave(om);
//		System.out.println(flag);
		boolean flag =true;
		ServerResponses sr=new ServerResponses();//消息返回
		try {
			if(flag){
				return sr.recvACK(om.getId(), om.getWaterNo(), om.getCommand(), 0);
			}else{
				return sr.recvACK(om.getId(), om.getWaterNo(), om.getCommand(), 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
}
