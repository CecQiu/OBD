package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.OBDPacketDao;
import com.hgsoft.carowner.entity.OBDPacket;
import com.hgsoft.common.service.BaseService;

@Service("obdPacketService")
public class OBDPacketService extends BaseService< OBDPacket> {
	

	@Resource
	public void setDao(OBDPacketDao obdPacketDao) {
		super.setDao(obdPacketDao);
	}

	public OBDPacketDao getObdPacketDao() {
		return (OBDPacketDao) this.getDao();
	}
	
	public void add(OBDPacket obdPacket){
		getObdPacketDao().save(obdPacket);
	}
}
