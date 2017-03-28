package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.DomainState;
import com.hgsoft.common.dao.BaseDao;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class DomainStateDao extends BaseDao<DomainState> {

	//取最新的gps设置信息
	public DomainState queryByObdSn(String obdSn) {
		final String hql = "FROM DomainState WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (DomainState) query.uniqueResult();
	}
	
	
	public boolean domainStateSave(DomainState domainState) {
		try {
			saveOrUpdate(domainState);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
