package com.hgsoft.obd.handler;


import java.util.Date;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.service.UpgradeSetService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerSettingService;

/**
 * 设备上传通讯——设备初始化
 * 
 * @author sujunguang 2015年12月12日 下午4:26:20
 */
@Service
public class MessageObdInitHandler implements IMessageObd {

	private static Logger obdHandlerDeviceInitLogger = LogManager.getLogger("obdHandlerDeviceInitLogger");

	@Resource
	private MessageParseHandler messageParseHandler;
	@Resource
	private UpgradeSetService upgradeSetService;
	@Resource
	private ServerSettingService serverSettingService;
	
	@Override
	public String entry(OBDMessage message) throws Exception {
		String obdSn = message.getId();
		obdHandlerDeviceInitLogger.info("------------------【设备上传通讯——设备初始化】-------------------");
		obdHandlerDeviceInitLogger.info("-----------"+obdSn+"---报文："+(GlobalData.isPrint2Char?StrUtil.format2Char(message.getMessage()):message.getMessage())+"-------------------");
		obdHandlerDeviceInitLogger.info("------------设备："+obdSn+"------------");
		String retrunMsgBody = "success";
		String[] cutStrs;// 截取结果数组
		String msgBody = message.getMsgBody();
		ObdHandShake obdHandShake = new ObdHandShake();
		obdHandShake.setId(IDUtil.createID());
		obdHandShake.setObdSn(obdSn);
		obdHandShake.setMessage(message.getMessage());
		obdHandShake.setCreateTime(new Date());
		try {
			//命令字
//			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String command = message.getCommand();
//			msgBody = cutStrs[1];
			//固件版本号
			cutStrs = StrUtil.cutStrByByteNum(msgBody, 2);
			String firmwareVersion = cutStrs[0];
			msgBody = cutStrs[1];
			obdHandlerDeviceInitLogger.info(""+"<"+obdSn+">固件版本号:"+firmwareVersion);
			obdHandShake.setFirmwareVersion(firmwareVersion);
//			UpgradeSet upgradeSet = upgradeSetService.queryByObdSnLike(obdSn);
//			if(upgradeSet != null && "1".equals(upgradeSet.getAuditState()) && "1".equals(upgradeSet.getVflag())//审核+推送到升级服务端
//					&& "1".equals(upgradeSet.getSendFlag()) && !firmwareVersion.equals(upgradeSet.getFirmVersion())){//待升级列表中，与固件版本不同则继续下发升级
//				String result = serverSettingService.deviceUpgradeSet(obdSn, "1");
//				obdHandlerDeviceInitLogger.info(""+"<"+obdSn+">初始化固件版本号:"+firmwareVersion+
//						",与待升级列表固件版本号:"+upgradeSet.getFirmVersion()+",不同。继续下发升级，下发结果："+result);
//				upgradeSet.setValid("0");//表面已经下发
//				upgradeSet.setUpdateTime(new Date());
//				upgradeSetService.upgradeSetSave(upgradeSet);
//			}
			//设备状态 处理
			msgBody = messageParseHandler.deviceStateHandler(message,msgBody,obdHandShake);
			
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerDeviceInitLogger.error(obdSn, e);
			return "error";
		}
		return retrunMsgBody;
	}

	public static void main(String[] args) throws Exception {
		// 流水号加1后返回，流水号长度为4
		// DecimalFormat df = new DecimalFormat(STR_FORMAT);
		// char[] c =
		// df.format(Double.valueOf(Integer.toBinaryString(Integer.valueOf("6512",16)))).toCharArray();
		// System.out.println(c);
		// System.out.println("size:"+c.length);
		// for (char cc : c) {
		// System.out.println(cc);
		// }
		//
		// System.out.println(StrUtil.hexToBinary("6512"));

		String str = "1234567890";
		String[] cutStrs = StrUtil.cutStrByByteNum(str, 2);
		System.out.println(cutStrs[0]);
		System.out.println(cutStrs[1]);
		str = cutStrs[1];
		cutStrs = StrUtil.cutStrByByteNum(str, 3);
		System.out.println(cutStrs[0]);
		System.out.println("left:" + cutStrs[1]);

//		str = cutStrs[1];
//		cutStrs = StrUtil.cutStrByByteNum(str, 3);
//		System.out.println(cutStrs[0]);
//		System.out.println("left:" + cutStrs[1]);
char c = 'd';
		System.out.println( new String(""+c+c));
		System.out.println(Integer.valueOf("79", 16));
	}

}
