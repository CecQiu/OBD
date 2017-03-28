package com.hgsoft.obd.handler;


import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.UpgradeService;
/**
 * 请求远程升级处理
 * @author sujunguang
 * 2016年3月9日
 * 上午11:09:31
 */
@Service
public class MessageObdRequestUpgradeHandler {
	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");
//	@Resource
//	UpdateDataService  updateDataService;
	@Resource
	UpgradeService upgradeService;
	
	public void entrance(OBDMessage om) throws Exception {
		//obd设备ID
		String obdSn = om.getId();
		upgradeDataLogger.info("---------------"+obdSn+"---【请求远程升级】--------------");
		upgradeDataLogger.info("--------------报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(om.getMessage()):om.getMessage())+"-------------------");
		upgradeDataLogger.info("------------------【请求远程升级】设备："+obdSn);
		//消息体
		String msg = om.getMsgBody();
		//请求包序号
		int packageNum = Integer.valueOf(msg,16);
		upgradeDataLogger.info("---------------"+obdSn+"---【请求远程升级】包序号："+msg+"->"+packageNum);
		try {
			upgradeService.sendUpgradeData(obdSn,packageNum);
		} catch (Exception e) {
			e.printStackTrace();
			upgradeDataLogger.error(obdSn, e);
		}
//		logger.info(obdSn+"******远程升级包:"+packageNum+"****结果:"+flag);
	}
}
