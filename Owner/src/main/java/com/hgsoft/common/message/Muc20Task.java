package com.hgsoft.common.message;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.CarTestService;
import com.hgsoft.common.message.OBDMessage;
@Service
public class Muc20Task {
	private final Log logger = LogFactory.getLog(Muc20Task.class);
	@Resource
	private CarTestService carTestService;
	/**
	 * 程序入库 800c 请求车辆体检
	 * @param om 请求消息对象
	 * @return 0x00成功接收，0x01接收错误,其它保留
	 * @throws Exception
	 */
	public void entrance(OBDMessage om) {
		boolean flag=carTestService.carTestSave(om);
		logger.info(om.getId()+"****请求体检结果:"+flag);
	}
}
