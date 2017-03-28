package com.hgsoft.carowner.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.PositionDriveInfoDao;
import com.hgsoft.carowner.entity.PositionDriveInfo;
import com.hgsoft.common.service.BaseService;

@Service
public class PositionDriveInfoService extends BaseService< PositionDriveInfo> {
	

	@Resource
	public void setDao(PositionDriveInfoDao positionDriveInfoDao) {
		super.setDao(positionDriveInfoDao);
	}

	public PositionDriveInfoDao getPositionDriveInfoDao() {
		return (PositionDriveInfoDao) this.getDao();
	}

	public void add(PositionDriveInfo positionDriveInfo) {
		getPositionDriveInfoDao().add(positionDriveInfo);
	}

	public void batchSave(List<PositionDriveInfo> positionDriveInfoLists) throws Exception {
		getPositionDriveInfoDao().batchSave(positionDriveInfoLists);
	}
	
	
}
