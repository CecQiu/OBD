package com.hgsoft.carowner.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.WifiSetDao;
import com.hgsoft.carowner.entity.GpsSet;
import com.hgsoft.carowner.entity.WifiSet;
import com.hgsoft.common.service.BaseService;
/**
 * WiFi设置Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class WifiStateSetService extends BaseService<WifiSet> {
	
	@Resource
	public void setDao(WifiSetDao wifiSetDao){
		super.setDao(wifiSetDao);
	}
	
	@Override
	public WifiSetDao getDao() {
		return (WifiSetDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得WifiSet集合
	 * @param obdSn
	 * @return
	 */
	public List<WifiSet> queryListByObdSn(String obdSn){
		return getDao().queryListByObdSn(obdSn);
	}
	
	/**
	 * 通过OBDSN和类别获得WifiSet
	 * @param obdSn
	 * @return
	 */
	public WifiSet queryWifStateByObdSn(String obdSn,String type){
		return getDao().queryByObdSn(obdSn,type);
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
	
	public boolean wifiSetSave(WifiSet wifiSet){
		try {
			getDao().saveOrUpdate(wifiSet);
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
	public int wifiSetNoValid(String obdSn,String type){
		return getDao().wifiSetNoValid(obdSn,type);
	}

	/**
	 * 查询最近一次WiFi设置记录
	 * @param obdSn
	 * @return
	 */
	public WifiSet queryLastWifiSet(String obdSn,String type) {
		return getDao().queryLastWifiSet(obdSn,type);
	}

	public void wifiSetUpdate(WifiSet wifiSet) {
		getDao().wifiSetSaveOrUpdate(wifiSet);
	}
	
}
