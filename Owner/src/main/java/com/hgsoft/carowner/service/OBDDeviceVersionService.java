package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdDeviceVersionDao;
import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.service.BaseService;
/**
 * OBD设备版本信息
 * @author sujunguang
 * 2016年2月2日
 * 上午11:04:43
 */
@Service
public class OBDDeviceVersionService extends BaseService<OBDDeviceVersion> {
	
	@Resource
	private ObdDeviceVersionDao obdDeviceVersionDao;
	
	@Resource
	public void setDao(ObdDeviceVersionDao obdDeviceVersionDao){
		super.setDao(obdDeviceVersionDao);
	}
	
	@Override
	public BaseDao<OBDDeviceVersion> getDao() {
		return (ObdDeviceVersionDao)super.getDao();
	}
	
	public void add(OBDDeviceVersion obdDeviceVersion){
		getDao().save(obdDeviceVersion);
	}
}
