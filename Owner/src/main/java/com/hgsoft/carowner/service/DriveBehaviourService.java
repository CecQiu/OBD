package com.hgsoft.carowner.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.DriveBehaviourDao;
import com.hgsoft.carowner.entity.DriveBehaviour;
import com.hgsoft.common.service.BaseService;

@Service
public class DriveBehaviourService extends BaseService<DriveBehaviour> {
	

	@Resource
	public void setDao(DriveBehaviourDao driveBehaviourDao) {
		super.setDao(driveBehaviourDao);
	}

	public DriveBehaviourDao getDriveBehaviourDao() {
		return (DriveBehaviourDao) this.getDao();
	}

	public List<DriveBehaviour> getBDListByObdSnAndTime(String starTime,String endTime,String obdSn,Integer page,Integer pageSize) {
		return getDriveBehaviourDao().getBDListByObdSnAndTime( starTime, endTime, obdSn, page, pageSize);
	}
	
	public Integer getBDListTotalByObdSnAndTime(String starTime,String endTime,String obdSn){
		return getDriveBehaviourDao().getBDListTotalByObdSnAndTime( starTime, endTime, obdSn);
	}
	
}
