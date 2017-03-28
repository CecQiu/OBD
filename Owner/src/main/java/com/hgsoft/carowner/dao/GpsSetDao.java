package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.GpsSet;
import com.hgsoft.common.dao.BaseDao;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class GpsSetDao extends BaseDao<GpsSet> {

	//取最新的gps设置信息
	public GpsSet queryByObdSn(String obdSn) {
		final String hql = "FROM GpsSet WHERE obdSn =:obdSn and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (GpsSet) query.uniqueResult();
	}
	
	//取最新的gps设置信息
	public int gpsStateNoValid(String obdSn) {
		final String hql = "update GpsSet set valid='0' WHERE obdSn =:obdSn";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return query.executeUpdate();
	}
	
	
	public boolean gpsSetSave(GpsSet gpsSet) {
		saveOrUpdate(gpsSet);
		return true;
	}

	public GpsSet queryLastGpsSet(String obdSn) {
		final String hql = "FROM GpsSet WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (GpsSet) query.uniqueResult();
	}
}
