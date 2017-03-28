package com.hgsoft.carowner.service;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdCurVersionDao;
import com.hgsoft.carowner.entity.ObdCurVersion;
import com.hgsoft.common.service.BaseService;
/**
 * obd版本信息Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdCurVersionService extends BaseService<ObdCurVersion> {
	
	@Resource
	public void setDao(ObdCurVersionDao obdCurVersionDao){
		super.setDao(obdCurVersionDao);
	}
	
	@Override
	public ObdCurVersionDao getDao() {
		return (ObdCurVersionDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得obd版本号
	 * @param obdSn
	 * @return
	 */
	public ObdCurVersion queryByObdSn(String obdSn){
		return getDao().queryByObdSn(obdSn);
	}
	
	/**
	 * 通过OBDSN修改obd版本号
	 * @param obdSn
	 * @param carState
	 * @return
	 */
	public boolean obdCurVersionUpdate(ObdCurVersion obdCurVersion){
		 try {
			getDao().update(obdCurVersion);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	public boolean obdCurVersionSave(ObdCurVersion obdVersion){
		try {
			getDao().saveOrUpdate(obdVersion);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
