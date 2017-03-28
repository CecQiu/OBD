/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdUnRegDao;
import com.hgsoft.carowner.entity.ObdUnReg;
import com.hgsoft.common.service.BaseService;

/**
 * OBD设置未注册记录
 */
@Service
public class ObdUnRegService extends BaseService<ObdUnReg>{
	
	@Resource
	private ObdUnRegDao obdUnRegDao;
	
	@Resource
	public void setDao(ObdUnRegDao obdUnRegDao){
		super.setDao(obdUnRegDao);
	}
	
	public void obdUnRegSave(ObdUnReg obdUnReg){
		obdUnRegDao.save(obdUnReg);	
	}
	
	public ObdUnReg queryByObdSn(String obdSn){
		return obdUnRegDao.queryByObdSn(obdSn);
	}
}
