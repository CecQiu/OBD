package com.hgsoft.carowner.service;


import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdVersionDao;
import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
/**
 * obd版本信息Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdVersionService extends BaseService<ObdVersion> {
	
//	@Resource
//	private ObdVersionDao obdVersionDao;
	
	@Resource
	public void setDao(ObdVersionDao obdVersionDao){
		super.setDao(obdVersionDao);
	}
	
	@Override
	public ObdVersionDao getDao() {
		return (ObdVersionDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得obd版本号
	 * @param obdSn
	 * @return
	 */
	public ObdVersion queryObdVersionBySN(String obdSn){
		return getDao().queryObdVersionBySN(obdSn);
	}
	
	/**
	 * 通过OBDSN修改obd版本号
	 * @param obdSn
	 * @param carState
	 * @return
	 */
	public boolean obdVersionUpdate(ObdVersion obdVersion){
		return getDao().updateObdVersion(obdVersion);
	}
	
	public boolean obdVersionSave(ObdVersion obdVersion){
		return getDao().obdVersionSave(obdVersion);
	}
	
//	public boolean getObdVersions(ObdVersion obdVersion){
//		return obdVersionDao.obdVersionSave(obdVersion);
//	}
	
	public List<ObdVersion> getLastList(Date start,Date end,String version,String firmType,String firmVersion,Pager pager) {
		return getDao().getLastList(start, end,version,firmType,firmVersion,pager);
	}
	
}
