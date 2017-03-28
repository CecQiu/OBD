package com.hgsoft.common.message;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.service.OBDVersionCheckService;
import com.hgsoft.common.message.OBDMessage;

/**
 * obd发送当前设备号
 * @author liujialin
 * 
 */
@Service
public class Muc13Task {
	private final Log logger = LogFactory.getLog(Muc13Task.class);
	@Resource
	private OBDVersionCheckService obdVersion;
	/**
	 * 后台获取到obd当前设备号，然后查询最新的设备号，如果和obd当前设备号不一致，则请求远程升级
	 * @param om 请求消息对象
	 * @return 0x00成功接收，0x01接收错误,其它保留
	 * @throws Exception
	 */
	public void entrance(OBDMessage om) {
		boolean flag=obdVersion.obdVersionCheck(om);
		logger.info(om.getId()+"****obd设备号校验是否最新:"+flag);
	}
}
