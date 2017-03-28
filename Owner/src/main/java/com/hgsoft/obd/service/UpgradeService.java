package com.hgsoft.obd.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdUpgradeDao;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * 升级
 * @author sujunguang
 * 2016年1月5日
 * 下午4:45:17
 */
@Service
public class UpgradeService {
	
	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");
	
	@Resource
	private ObdUpgradeDao obdUpgradeDao;
	@Resource
	private SendUtil sendUtil;
	/*
	 * 给设备发送请求升级包来升级
	 */
	@Deprecated
	public String sendOBDRequest(String obdSn) throws OBDException{
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_ResponseUpgrade_OBD_Cmd;
		return (String) sendUtil.msgSendGetResult(obdSn, serialNum, data, null);
	}
	
	/**
	 * 发送固件升级包到设备
	 * @param obdSn 设备号
	 * @param _packetNo 包序号
	 * @return
	 * @throws Exception
	 */
	public void sendUpgradeData(String obdSn, Integer _packetNo) throws Exception {
		String updateData = obdUpdateDataStr(_packetNo);
		upgradeDataLogger.info("------------------【请求远程升级】升级数据："+updateData);
		if(StringUtils.isEmpty(updateData)){
			return;
		}
		String msg = ObdConstants.OBD_ServerResponseUpgradeData_Cmd +updateData;
		upgradeDataLogger.info("------------------【请求远程升级】命令字+升级数据："+msg);
		sendUtil.msgSend(obdSn, SerialNumberUtil.getSerialnumber(obdSn),msg,null);
		upgradeDataLogger.info("------------------【请求远程升级】报文已经发送！");
	}
	
	/**
	 	2	包序号	序号从0开始
		2	当前包长度	<=800BYTE
		N	包数据
		4	数据总长度	仅当包序号为0时有此项
		1	异或校验和	仅当包序号为0时有此项
	 * 根据请求包序号获得升级文件数据
	 * @param packageNum 包序号
	 * @return
	 * @throws Exception
	 */
	public String obdUpdateDataStr(int packageNum)throws Exception {
	
		// 1获取数据库里的最新的升级包文件
		ObdUpgrade ou = obdUpgradeDao.getLatestVersion();
		if (ou == null) {
			return null;
		}
		// 2将数据库里的文件转成file
		byte[] fileByte = ou.getFile();
		if (fileByte == null || fileByte.length == 0) {
			return null;
		}
		// 3读取文件内容，指定的byte[],包数据
		byte[] fileMsgByte = null;
		try {
			fileMsgByte = FileUtil.cutByte(fileByte, packageNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fileMsgByte == null) {
			return null;
		}
		// 将byte数组转成16进制的字符串,包数据
		String fileMsgHex = ByteUtil.bytesToHexString(fileMsgByte);
		// 包序号packageNum: 2个byte
		String packNumHex = StrUtil.strAppend(Integer.toHexString(packageNum),4, 0, "0");
		// 当前包长度 2byte
		String packLen = StrUtil.strAppend(Integer.toHexString(fileMsgByte.length), 4, 0, "0");// 直接算长度
		
		StringBuffer msg = new StringBuffer("");
		msg.append(packNumHex).append(packLen).append(fileMsgHex);
		
		if (packageNum == 0) {
			// 固件长度,仅当包序号为0时有此项 4 byte
			String packSumLenHex = StrUtil.strAppend(Integer.toHexString(ou.getSize().intValue()), 4 * 2, 0, "0");
			// 固件异或校验和,仅当包序号为0时有此项
			String fileHex = ByteUtil.bytesToHexString(fileByte);
			String checkCode = "";
			try {
				ServerResponses sr = new ServerResponses();// 消息返回
				checkCode = sr.xor(fileHex);
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg.append(packSumLenHex).append(checkCode);
		}
		
		return msg.toString();
	}
}
