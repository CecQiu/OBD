package com.hgsoft.common.message;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.MsgUploadBatchService;
import com.hgsoft.common.message.OBDMessage;
/**
 * 6.	Mcu6上传批量信息
 * @author liujialin
 *
 */
@Service
public class Muc6Task {
	private final Log logger = LogFactory.getLog(Muc6Task.class);
	@Resource
	private MsgUploadBatchService msgUploadBatchService;
	/**
	 * 6.	Mcu6上传批量信息 逻辑处理方法
	 * @param om 请求消息对象
	 */
	public void entrance(OBDMessage om) {
		boolean flag=msgUploadBatchService.msgSave(om);
		logger.info(om.getId()+"*******上传批量信息结果:"+flag);
	}
}
