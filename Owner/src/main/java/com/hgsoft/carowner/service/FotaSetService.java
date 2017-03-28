package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.FotaSetDao;
import com.hgsoft.carowner.entity.FotaSet;
import com.hgsoft.common.service.BaseService;
/**
 * WiFi设置Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class FotaSetService extends BaseService<FotaSet> {
	
	@Resource
	public void setDao(FotaSetDao fotaSetDao){
		super.setDao(fotaSetDao);
	}
	
	@Override
	public FotaSetDao getDao() {
		return (FotaSetDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得WifiSet集合
	 * @param obdSn
	 * @return
	 */
	public List<FotaSet> queryListByObdSn(String obdSn) {
		return getDao().queryListByObdSn(obdSn);
	}
	
	/**
	 * 
	 * @param obdSn
	 * @return
	 */
	public FotaSet queryByObdSn(String obdSn) {
		return getDao().queryByObdSn(obdSn);
	}
	
	
	public int setUnuseful(String obdSn) {
		return getDao().setUnuseful(obdSn);
	}
	
	public int setUnusefulByVersion(String version) {
		return getDao().setUnusefulByVersion(version);
	}
	
	
	public int setAuditResultByBatchVersion(String batchVersion,String auditOper,String auditResult,String auditTime) {
		return getDao().setAuditResultByBatchVersion(batchVersion,auditOper,auditResult,auditTime);
	}

	
	public boolean fsSaveOrUpdate(FotaSet fotaSet) {
//		return getDao().fsSaveOrUpdate(fotaSet);
		try {
			getDao().saveOrUpdate(fotaSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<FotaSet> getListByMap(Map<String, Integer> obdSns) {
		return getDao().getListByMap(obdSns);
	}
	
	public List<FotaSet> getListByIdMap(Set<String> ids) {
		return getDao().getListByIdMap(ids);
	}
	/**
	 * 根据MAP参数查询
	 * @param map
	 * @return
	 */
	public FotaSet queryByConditions(Map<String, Object> map) {
		return getDao().queryByConditions(map);
	}
}
