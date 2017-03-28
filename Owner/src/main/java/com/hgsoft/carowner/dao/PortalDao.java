package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.common.dao.BaseDao;
/**
 * wifi设置dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class PortalDao extends BaseDao<Portal> {

	//取最新的gps设置信息
	@SuppressWarnings("unchecked")
	public List<Portal> queryListByObdSn(String obdSn) {
		final String hql = "FROM Portal WHERE obdSn =:obdSn and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return (List<Portal>)query.list();
	}
	
	//取最新的gps设置信息
	public Portal queryByObdSn(String obdSn,String type) {
		final String hql = "FROM Portal WHERE obdSn =:obdSn and valid='1' and type=:type order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (Portal) query.uniqueResult();
	}
	
	
	//取最新的wifi开关设置信息
	public int setByParam(String obdSn,String type,String valid) {
		try {
			final String hql = "update Portal set valid=:valid,updateTime=:updateTime WHERE obdSn =:obdSn and type=:type and valid='1' ";
			Query query = getSession().createQuery(hql);
			query.setString("obdSn", obdSn);
			query.setString("valid", valid);
			query.setString("type", type);
			query.setTimestamp("updateTime", new Date());
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public boolean portalSaveOrUpdate(Portal portal) {
		try {
			saveOrUpdate(portal);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
