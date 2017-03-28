package com.hgsoft.common.message;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.SystemTimeService;
import com.hgsoft.common.message.OBDMessage;
/**
 * Mcu11 请求系统时间
 * @author liujialin
 * 
 */
@Service
public class Muc11Task {
	private final Log logger = LogFactory.getLog(Muc11Task.class);
	@Resource
	private SystemTimeService systemTimeService;
	/**
	 * Mcu11 请求系统时间
	 * @param om 请求消息对象
	 */
	public void entrance(OBDMessage om) {
		String obdSn=om.getId();
		boolean flag=systemTimeService.systemTime(obdSn);
		logger.info(obdSn+"******************Mcu11 请求系统时间结果:"+flag);
	}
}
