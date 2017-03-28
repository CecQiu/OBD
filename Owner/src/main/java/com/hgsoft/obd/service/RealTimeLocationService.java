package com.hgsoft.obd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
/**
 * 获取实时位置信息 弃用！
 * @author sujunguang
 * 2016年1月7日
 * 下午6:23:08
 */
@Service
@Deprecated
public class RealTimeLocationService {
	@Resource
	private SendUtil sendUtil;
	
	public String realTimeGet(String obdSn){
		System.out.println("realTimeLoc...");
		String msgBody = ObdConstants.Server_RequestRealTimeLoc_OBD_Cmd;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		try {
			GlobalData.OBD_ACK_OR_QueryData.put(obdSn+"_"+serialNum, null);
			String key = "";
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNum, msgBody,key,null);
			System.out.println("realTimeLoc...:"+obj);
			if(obj instanceof PositionInfo){
				PositionInfo positionInfo = (PositionInfo)obj;//位置信息 TODO
				System.out.println("位置信息:"+positionInfo);
				return positionInfo.toString();
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return null;
	}
}
