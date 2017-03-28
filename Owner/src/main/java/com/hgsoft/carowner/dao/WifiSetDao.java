package com.hgsoft.carowner.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.WifiSet;
import com.hgsoft.common.dao.BaseDao;
/**
 * wifi设置dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class WifiSetDao extends BaseDao<WifiSet> {

	//取最新的gps设置信息
	public List<WifiSet> queryListByObdSn(String obdSn) {
		final String hql = "FROM WifiSet WHERE obdSn =:obdSn and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return (List<WifiSet>)query.list();
	}
	
	//取最新的gps设置信息
	public WifiSet queryByObdSn(String obdSn,String type) {
		final String hql = "FROM WifiSet WHERE obdSn =:obdSn and valid='1' and type=:type order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (WifiSet) query.uniqueResult();
	}
	
	
	//取最新的wifi开关设置信息
	public int wifiSetNoValid(String obdSn,String type) {
		final String hql = "update WifiSet set valid='0' WHERE obdSn =:obdSn and type=:type";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		return query.executeUpdate();
	}
	
	
	public boolean wifiSetSaveOrUpdate(WifiSet wifiSet) {
		try {
			saveOrUpdate(wifiSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public WifiSet queryLastWifiSet(String obdSn,String type) {
		final String hql = "FROM WifiSet WHERE obdSn =:obdSn and type =:type order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (WifiSet) query.uniqueResult();
	}
}
