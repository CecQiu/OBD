package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdTestSendPacketDao;
import com.hgsoft.carowner.entity.ObdTestSendPacket;
import com.hgsoft.common.service.BaseService;
/**
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdTestSendPacketService extends BaseService<ObdTestSendPacket> {
	
	@Resource
	public void setDao(ObdTestSendPacketDao obdTestSendPacketDao){
		super.setDao(obdTestSendPacketDao);
	}
	
	@Override
	public ObdTestSendPacketDao getDao() {
		return (ObdTestSendPacketDao)super.getDao();
	}
	
	public List<ObdTestSendPacket> queryByObdSn(Map<String,Object> map) {
		return getDao().queryByObdSn(map);
	}
	
	public boolean sendPacketSave(ObdTestSendPacket obdTestSendPacket) {
		return getDao().sendPacketSave(obdTestSendPacket);
	}

	public void sendPacketUpdate(ObdTestSendPacket obdTestSendPacket) {
		getDao().sendPacketUpdate(obdTestSendPacket);
	}
	
	public ObdTestSendPacket queryByObdSnAndMsgBody(String obdSn,String msgBody) {
		return getDao().queryByObdSnAndMsgBody(obdSn,msgBody);
	}
	
	public List<ObdTestSendPacket> queryByObdSn(String obdSn){
		return getDao().queryByObdSn(obdSn);
	}
}
