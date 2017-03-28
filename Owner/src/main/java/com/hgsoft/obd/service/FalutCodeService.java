package com.hgsoft.obd.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;

/**
 * 故障码服务 弃用
 * 
 * @author sujunguang 2016年1月7日 下午6:23:08
 */
@Service
@Deprecated
public class FalutCodeService {

	@Resource
	private SendUtil sendUtil;
	/**
	 * 清除故障码
	 */
	public String clearFaultCode(String obdSn) {
		String msgBody = ObdConstants.Server_SettingClearFaultCode_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum,	msgBody, null);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读故障码
	 */
	public String readFaultCode(String obdSn) {
		// aa 88 88 88 88 00 00 05 80 01 00 04 01 81 aa
		String msgBody = ObdConstants.Server_RequestFaultCode_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum,	msgBody, null);
			if (obj instanceof List) {
				List<FaultUpload> faultUploads = (List<FaultUpload>) obj;
				return faultUploads.toString();
			}

		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
}
