package com.hgsoft.obd.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.common.action.BaseAction;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.service.AGPSService;
import com.hgsoft.obd.service.FalutCodeService;
import com.hgsoft.obd.service.OBDDeviceService;
import com.hgsoft.obd.service.RealTimeLocationService;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;

/**
 * 测试OBD
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class TestOBDAction0 extends BaseAction {
	@Resource
	private RealTimeLocationService realTimeLocationService;
	@Resource
	private FalutCodeService falutCodeService;
	@Resource
	private OBDDeviceService obdDeviceService;
	@Resource
	private AGPSService agpsService;
	@Resource
	private SendUtil sendUtil;

	private String obdSn;

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	// 读取实时位置
	public void testRealTimeLoc() {
		System.out.println("---获取实时位置---");
		String result = realTimeLocationService.realTimeGet(obdSn);
		if (result == null) {
			result = "获取实时位置失败！";
		}
		outMessage(result);
	}

	// 读取故障码
	public void testReadFaultCode() {
		System.out.println("---读取故障码---");
		String result = falutCodeService.readFaultCode(obdSn);
		if (result == null) {
			result = "读取故障码失败！";
		}
		outMessage(result);
	}

	// 清除故障码
	public void testClearFaultCode() {
		System.out.println("---清除故障码---");
		String result = falutCodeService.clearFaultCode(obdSn);
		if (result == null) {
			result = "清除故障码失败！";
		}
		outMessage(result);
	}

	// 恢复出厂设置
	public void testReset() {
		System.out.println("--- 恢复出厂设置---");
		long begin = System.currentTimeMillis();
		String result = obdDeviceService.reset(obdSn);
		if (result == null) {
			result = " 恢复出厂设置失败！";
		}
		System.out.println(System.currentTimeMillis() - begin);
		outMessage(result);
	}

	// OBD重启
	public void testRestart() {
		System.out.println("---OBD重启---");
		String result = obdDeviceService.restart(obdSn);
		if (result == null) {
			result = " OBD重启失败！";
		}
		outMessage(result);
	}

	// AGPS升级包
	public void testAGPS() throws Exception {
		System.out.println("---获取AGPS升级包---");
		
		Integer packageNum = 0;
		String data = agpsService.getAgpsData(obdSn,packageNum);
		Integer serialNum = 0;
		String result = null;
		try {
			result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data, null);
		} catch (OBDException e) {
			e.printStackTrace();
		}
		if (result == null) {
			result = " 更新AGPS升级包失败！";
		}
		outMessage(result);
	}
	
	/**
	 * 下发固件升级
	 * @throws OBDException
	 */
	public void testUpgrade() throws OBDException{
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_SettingUpgradeServerParams_OBD_Cmd;
		String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data, null);
		outMessage(result);
	}
	
	/** TODO
	 * 下发AGPS数据包升级
	 * @throws OBDException
	 */
	public void testSendAGPS() throws OBDException{
//		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
//		String data = ObdConstants.Server_Setting;
//		String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data);
//		outMessage(result);
	}
	
	/**
	 * 下发离线数据请求
	 * @throws OBDException
	 */
	public void testOffData() throws OBDException{
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		String data = ObdConstants.Server_RequestOffData_OBD_Cmd;
		String result = (String) sendUtil.msgSendGetResult(obdSn, serialNum, data, null);
		outMessage(result);
	}
}
