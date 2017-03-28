package com.hgsoft.common.message;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.CarParamQueryAnswerService;
import com.hgsoft.common.message.OBDMessage;
/**
 * 3.	Mcu3参数查询应答
 * @author liujialin
 * 
 */
@Service
public class Muc3Task {
	private final Log logger = LogFactory.getLog(Muc3Task.class);
	@Resource
	private CarParamQueryAnswerService carParamQueryAnswerService;
	/**
	 * Mcu3参数查询应答 逻辑处理方法
	 * @param om 请求消息对象
	 */
	public void entrance(OBDMessage om) {
		boolean flag=carParamQueryAnswerService.carParamQueryAnswer(om);
		logger.info(om.getId()+"******************Mcu3参数查询应答:"+flag);
		
	}
}
