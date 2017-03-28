package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdTravelParamsDao;
import com.hgsoft.carowner.entity.OBDServerParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.service.BaseService;
/**
 * OBD设备版本信息
 * @author sujunguang
 * 2016年2月2日
 * 上午11:04:43
 */
@Service
public class ObdTravelParamsService extends BaseService<OBDTravelParams> {
	
	@Resource
	private ObdTravelParamsDao obdTravelParamsDao;
	
	@Resource
	public void setDao(ObdTravelParamsDao obdTravelParamsDao){
		super.setDao(obdTravelParamsDao);
	}
	
	@Override
	public BaseDao<OBDTravelParams> getDao() {
		return (ObdTravelParamsDao)super.getDao();
	}
	
	public boolean add(OBDTravelParams obdTravelParams){
		try {
			getDao().saveOrUpdate(obdTravelParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public OBDTravelParams queryByObdSn(String obdSn) {
		return obdTravelParamsDao.queryByObdSn(obdSn);
	}
}
