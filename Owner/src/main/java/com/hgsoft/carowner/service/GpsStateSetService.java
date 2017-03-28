package com.hgsoft.carowner.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.GpsSetDao;
import com.hgsoft.carowner.entity.GpsSet;
import com.hgsoft.common.service.BaseService;
/**
 * obd状态Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class GpsStateSetService extends BaseService<GpsSet> {
	
	@Resource
	public void setDao(GpsSetDao gpsSetDao){
		super.setDao(gpsSetDao);
	}
	
	@Override
	public GpsSetDao getDao() {
		return (GpsSetDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得GpsSet
	 * @param obdSn
	 * @return
	 */
	public GpsSet queryByObdSn(String obdSn){
		return getDao().queryByObdSn(obdSn);
	}
	
	/**
	 * 通过OBDSN修改obd版本号
	 * @param obdSn
	 * @param carState
	 * @return
	 */
	public boolean gpsSetUpdate(GpsSet gpsSet){
		 try {
			getDao().update(gpsSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	public boolean gpsSetSave(GpsSet gpsSet){
		try {
			getDao().saveOrUpdate(gpsSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	

	/**
	 * 通过OBDSN获得GpsSet
	 * @param obdSn
	 * @return
	 */
	public int gpsStateNoValid(String obdSn){
		return getDao().gpsStateNoValid(obdSn);
	}

	/**
	 * 获取最新一条GPS设置记录
	 * @param obdSn
	 * @return
	 */
	public GpsSet queryLastGpsSet(String obdSn) {
		return getDao().queryLastGpsSet(obdSn);
	}
	
}
