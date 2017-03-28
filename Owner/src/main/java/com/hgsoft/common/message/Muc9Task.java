package com.hgsoft.common.message;


import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.UpdateDataService;
import com.hgsoft.common.message.OBDMessage;
/**
 * 9.	Mcu9远程升级请求包
 * @author liujialin
 *
 */
@Service
public class Muc9Task {
	private final Log logger = LogFactory.getLog(Muc9Task.class);
	
	@Resource
	UpdateDataService  updateDataService;

	/**
	 * 
	 * 程序入库
	 * @param om 请求消息对象
	 * @return 服务器发送升级包数据
	 * @throws Exception
	 */
	public void entrance(OBDMessage om) {
		//消息体
		String msg = om.getMsgBody();
		//obd设备ID
		String obdSn=om.getId();
		//请求包序号
		int packageNum = Integer.valueOf(msg,16);
		boolean flag= updateDataService.obdUpdateData(obdSn,packageNum);
		logger.info(obdSn+"******远程升级包:"+packageNum+"****结果:"+flag);
	}
}
