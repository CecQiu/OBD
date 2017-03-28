package com.hgsoft.carowner.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.PositionWarnInfoDao;
import com.hgsoft.carowner.entity.PositionWarnInfo;
import com.hgsoft.common.service.BaseService;

@Service
public class PositionWarnInfoService extends BaseService< PositionWarnInfo> {
	
	@Resource
	public void setDao(PositionWarnInfoDao positionWarnInfoDao) {
		super.setDao(positionWarnInfoDao);
	}

	public PositionWarnInfoDao getPositionWarnInfoDao() {
		return (PositionWarnInfoDao) this.getDao();
	}
	

	public void add(PositionWarnInfo positionWarnInfo) {
		getPositionWarnInfoDao().add(positionWarnInfo);
	}

	public void batchSave(List<PositionWarnInfo> positionWarnInfoLists) throws Exception {
		getPositionWarnInfoDao().batchSave(positionWarnInfoLists);
	}
	
}
