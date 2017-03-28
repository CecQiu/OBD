package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdSettingDao;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.common.service.BaseService;
/**
 * obd设置表,包括离线设置
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdSettingService extends BaseService<ObdSetting> {
	
	@Resource
	public void setDao(ObdSettingDao obdSettingDao){
		super.setDao(obdSettingDao);
	}
	
	@Override
	public ObdSettingDao getDao() {
		return (ObdSettingDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得GpsSet
	 * @param obdSn
	 * @return
	 */
	public ObdSetting queryByObdSn(String obdSn){
		return getDao().queryByObdSn(obdSn);
	}
	
	/**
	 * 通过OBDSN修改obd版本号
	 * @param obdSn
	 * @param carState
	 * @return
	 */
	public boolean obdSettingUpdate(ObdSetting obdSetting){
		 try {
			getDao().update(obdSetting);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	public boolean obdSettingSave(ObdSetting obdSetting){
		try {
			getDao().saveOrUpdate(obdSetting);
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
	public int obdSettingNoValid(String obdSn,String type){
		return getDao().obdSettingNoValid(obdSn,type);
	}
	
	public int setNoValidByLikeType(String obdSn,String type) {
		return getDao().setNoValidByLikeType(obdSn,type);
	}
	/**
	 * 根据in type类型置为无效
	 * @param obdSn
	 * @param types
	 * @return
	 */
	public int setNoValidByInType(String obdSn,Map<String, Integer> types) {
		return getDao().setNoValidByInType(obdSn,types);
	}
	/**
	 * 获取最新一条GPS设置记录
	 * @param obdSn
	 * @return
	 */
	public ObdSetting queryLastObdSetting(String obdSn,String type) {
		return getDao().queryLastObdSetting(obdSn,type);
	}
	
	public List<ObdSetting> queryByObdSnAndType(String obdSn,String type){
		return getDao().queryByObdSnAndType(obdSn,type);
	}
	
	//判断是否已存在记录
	public List<ObdSetting> getOSListByMap(Map<String, Integer> obdSns,String type) {
		return getDao().getOSListByMap(obdSns,type);
	}
	
	//根据多个type查询
	public List<ObdSetting> getListByTypes(String obdSn,Set<String> types) {
		return getDao().getListByTypes(obdSn,types);
	}
}
