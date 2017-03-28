package com.hgsoft.obd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
/**
 * OBD设备操作 弃用！
 * @author sujunguang
 * 2016年1月7日
 * 下午6:23:08
 */
@Service
@Deprecated
public class OBDDeviceService {
	@Resource
	private SendUtil sendUtil;
	/**
	 * 恢复出厂设置
	 * @param obdSn
	 * @return
	 */
	public String reset(String obdSn){
		System.out.println("obd reset...");
		String msgBody = ObdConstants.Server_SettingReset_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_"+serialNum, null);
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody, null);
			System.out.println("obd reset...:"+obj);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 重新启动
	 * @param obdSn
	 * @return
	 */
	public String restart(String obdSn){
		System.out.println("obd restart...");
		String msgBody = ObdConstants.Server_SettingRestart_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_"+serialNum, null);
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody, null);
			System.out.println("obd restart...:"+obj);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 	请求离线数据
	 * @param obdSn
	 * @return
	 */
	public String requestOffData(String obdSn){
		System.out.println("obd OffData...");
		String msgBody = ObdConstants.Server_RequestOffData_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_"+serialNum, null);
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody, null);
			System.out.println("obd OffData...:"+obj);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 请求固件升级
	 * @param obdSn
	 * @return
	 */
	public String requestUpgrade(String obdSn){
		System.out.println("obd Upgrade...");
		String msgBody = ObdConstants.Server_RequestUpgradeServerParams_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_"+serialNum, null);
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody, null);
			System.out.println("obd Upgrade...:"+obj);
			return (String)obj;
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
}
