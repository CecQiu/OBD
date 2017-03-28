package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.FotaDao;
import com.hgsoft.carowner.entity.FOTA;
import com.hgsoft.common.service.BaseService;
/**
 * WiFi设置Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class FotaService extends BaseService<FOTA> {
	
	@Resource
	public void setDao(FotaDao fotaDao){
		super.setDao(fotaDao);
	}
	
	@Override
	public FotaDao getDao() {
		return (FotaDao)super.getDao();
	}
	
	//取最新的gps设置信息
		public FOTA queryByObdSn(String obdSn) {
			return this.getDao().queryByObdSn(obdSn);
		}
		
		//取最新的gps设置信息
		public List<FOTA> queryListByObdSn(String obdSn) {
			return this.getDao().queryListByObdSn(obdSn);
		}
		
		public FOTA queryByConditions(Map<String, Object> map) {
			return this.getDao().queryByConditions(map);
		}
		
		
		public boolean fotaSaveOrUpdate(FOTA fota) {
			return this.getDao().fotaSaveOrUpdate(fota);
		}
		
		//判断升级记录是否存在重复
		public List<FOTA> getListByMap(Map<String, Integer> obdSns) {
			return this.getDao().getListByMap(obdSns);
		}
}
