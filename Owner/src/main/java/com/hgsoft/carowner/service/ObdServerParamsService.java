package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdServerParamsDao;
import com.hgsoft.carowner.entity.OBDServerParams;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.service.BaseService;
/**
 * OBD设备版本信息
 * @author sujunguang
 * 2016年2月2日
 * 上午11:04:43
 */
@Service
public class ObdServerParamsService extends BaseService<OBDServerParams> {
	
	@Resource
	private ObdServerParamsDao obdServerParamsDao;
	
	@Resource
	public void setDao(ObdServerParamsDao obdServerParamsDao){
		super.setDao(obdServerParamsDao);
	}
	
	@Override
	public BaseDao<OBDServerParams> getDao() {
		return (ObdServerParamsDao)super.getDao();
	}
	
	public void add(OBDServerParams obdServerParams){
		getDao().save(obdServerParams);
	}
	
}
