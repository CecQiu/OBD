package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdButtonAlarmDao;
import com.hgsoft.carowner.entity.ObdButtonAlarm;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Service
public class ObdButtonAlarmService extends BaseService<ObdButtonAlarm>{
	
	@Resource
	public void setDao(ObdButtonAlarmDao obdButtonAlarmDao){
		super.setDao(obdButtonAlarmDao);
	}
	
	@Override
	public ObdButtonAlarmDao getDao() {
		return (ObdButtonAlarmDao)super.getDao();
	}
	
	public ObdButtonAlarm queryLast(String obdSn)throws Exception{
		return getDao().queryLast(obdSn);
	}
	
	public boolean obdButtonAlarmSaveOrUpdate(ObdButtonAlarm obdButtonAlarm){
		return getDao().obdButtonAlarmSaveOrUpdate(obdButtonAlarm);
	}
	
	public List<ObdButtonAlarm> queryByParams(Pager pager,Map<String,Object>map) {
		return this.getDao().queryByParams(pager, map);
	}
}
