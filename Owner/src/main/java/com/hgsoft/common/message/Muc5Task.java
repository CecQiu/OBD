package com.hgsoft.common.message;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.FaultUploadService;
import com.hgsoft.common.message.OBDMessage;
/**
 * 5.	Mcu5上传故障码
 * @author liujialin
 *
 */
@Service
public class Muc5Task {
	private final Log logger = LogFactory.getLog(Muc5Task.class);
	@Resource
	private FaultUploadService faultUploadService;
	/**
	 * Mcu5上传故障码 逻辑处理方法
	 * @param om 请求消息对象
	 */
	public void entrance(OBDMessage om) {
		boolean flag=faultUploadService.faultUploadSave(om);
		logger.info(om.getId()+"*******上传故障码结果:"+flag);
	}
}
