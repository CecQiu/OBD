package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.DomainStateDao;
import com.hgsoft.carowner.entity.DomainState;
import com.hgsoft.common.service.BaseService;
/**
 * 
 * @author liujialin
 * 2015-8-5
 */
@Service
public class DomainStateService extends BaseService<DomainState> {
	
	@Resource
	public void setDao(DomainStateDao domainStateDao){
		super.setDao(domainStateDao);
	}
	
	@Override
	public DomainStateDao getDao() {
		return (DomainStateDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得GpsSet
	 * @param obdSn
	 * @return
	 */
	public DomainState queryByObdSn(String obdSn){
		return getDao().queryByObdSn(obdSn);
	}

	
	public boolean domainStateSave(DomainState domainState){
		try {
			getDao().domainStateSave(domainState);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
