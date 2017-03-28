package com.hgsoft.common.message;


import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.AGPSDataService;
import com.hgsoft.common.message.OBDMessage;
/**
 * Mcu15 更新AGPS请求包
 * @author liujialin
 * 20150908
 */
@Service
public class Muc15Task {
	private final Log logger = LogFactory.getLog(Muc15Task.class);
	
	@Resource
	AGPSDataService  aGPSDataService;

	/**
	 * 程序入库
	 * @param om 请求消息对象
	 * @throws Exception 
	 */
	public void entrance(OBDMessage om) {
		boolean flag=aGPSDataService.AGPSData(om);
		logger.info(om.getId()+"******更新AGPS请求包:"+flag);
	}
}
