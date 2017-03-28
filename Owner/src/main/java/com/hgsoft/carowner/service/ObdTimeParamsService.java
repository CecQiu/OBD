package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdTimeParamsDao;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.service.BaseService;
/**
 * OBD设备版本信息
 * @author sujunguang
 * 2016年2月2日
 * 上午11:04:43
 */
@Service
public class ObdTimeParamsService extends BaseService<OBDTimeParams> {
	
	@Resource
	private ObdTimeParamsDao obdTimeParamsDao;
	
	@Resource
	public void setDao(ObdTimeParamsDao obdTimeParamsDao){
		super.setDao(obdTimeParamsDao);
	}
	
	@Override
	public BaseDao<OBDTimeParams> getDao() {
		return (ObdTimeParamsDao)super.getDao();
	}

	public boolean add(OBDTimeParams obdTimeParams){
		try {
			getDao().saveOrUpdate(obdTimeParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
 
	public OBDTimeParams getObdTimeParamsBySn(String obdSn) {
		return obdTimeParamsDao.getObdTimeParamsBySn(obdSn);
	}
}
