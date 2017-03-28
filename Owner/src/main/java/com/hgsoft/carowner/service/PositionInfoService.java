package com.hgsoft.carowner.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.PositionInfoDao;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Service
public class PositionInfoService extends BaseService< PositionInfo> {
	

	@Resource
	public void setDao(PositionInfoDao positionInfoDao) {
		super.setDao(positionInfoDao);
	}

	public PositionInfoDao getPositionInfoDao() {
		return (PositionInfoDao) this.getDao();
	}
	
	public PositionInfo queryBySnAndTime(String obdSn,Date time){
		return getPositionInfoDao().queryBySnAndTime(obdSn, time);
	}
	
	public void add(PositionInfo positionInfo){
		getPositionInfoDao().save(positionInfo);
	}

	public PositionInfo findLastBySN(String sn) {
		return getPositionInfoDao().findLastBySn(sn);
	}
	
	public PositionInfo findLast(String sn) {
		return getPositionInfoDao().findLast(sn);
	}
	
	public PositionInfo findLastTemNotNull(String sn) {
		return getPositionInfoDao().findLastTemNotNull(sn);
	}

	public List<PositionInfo> findInsertTime(String obdSn, String time,Pager pager) {
		return getPositionInfoDao().findInsertTime(obdSn,time,pager);
	}
	public List<PositionInfo> queryByParams(Pager pager,Map<String, Object> map) {
		return getPositionInfoDao().queryByParams(pager,map);
	}
	
	public void batchSave(List<PositionInfo> positionInfos) throws Exception{
		getPositionInfoDao().batchSave(positionInfos);
	}

	public PositionInfo findLastBySNAndGtTime(String obdSn, String time) {
		return getPositionInfoDao().findLastBySNAndGtTime(obdSn, time);
	}
}
