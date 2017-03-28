package com.hgsoft.carowner.service;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.PositionalInformationDao;
import com.hgsoft.carowner.entity.PositionalInformation;
import com.hgsoft.common.service.BaseService;

@Service
public class PositionalInformationService extends BaseService<PositionalInformation> {
	
	@Resource
	public void setDao(PositionalInformationDao positionalInformationDao){
		super.setDao(positionalInformationDao);
	}

	public void add(PositionalInformation pi) {
		getDao().save(pi);
	}
	
	@Override
	public PositionalInformationDao getDao() {
		return (PositionalInformationDao)super.getDao();
	}
	
/*	public PositionalInformation lastNewInfo(String obdSN){
		final String hql = "FROM PositionalInformation ORDER BY insesrtTime DESC";
		Session session = getDao().getSession();
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(1);
		return (PositionalInformation) query.list().get(0);
	}*/

	public PositionalInformation findLastBySN(String sn) {
		return getDao().findLastBySn(sn);
	}
	public PositionalInformation findLastBySnAndGpstime(String sn,Date end) {
		return getDao().findLastBySnAndGpstime(sn,end);
	}
	public PositionalInformation findFirstBySnAndGpstime(String sn,Date end) {
		return getDao().findFirstBySnAndGpstime(sn,end);
	}
	public PositionalInformation findFirstBySn(String sn) {
		return getDao().findFirstBySn(sn);
	}
	public PositionalInformation findByObdsnAndGpstime(String obdSn,Date gpstime) {
		return getDao().findByObdsnAndGpstime(obdSn, gpstime);
	}
}
