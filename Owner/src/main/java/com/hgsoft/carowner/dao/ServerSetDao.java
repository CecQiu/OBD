package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ServerSet;
import com.hgsoft.common.dao.BaseDao;
/**
 * ServerSetdao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ServerSetDao extends BaseDao<ServerSet> {
	
	//取最新的gps设置信息
	public ServerSet queryByObdSn(String obdSn) {
		final String hql = "FROM ServerSet WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ServerSet) query.uniqueResult();
	}

	//取最新的gps设置信息
	public ServerSet queryByObdSnAndValid(String obdSn,String valid) {
		final String hql = "FROM ServerSet WHERE obdSn =:obdSn and valid=:valid order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("valid", valid);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ServerSet) query.uniqueResult();
	}
	
	public boolean serverSetSaveOrUpd(ServerSet serverSet) {
		try {
			saveOrUpdate(serverSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
