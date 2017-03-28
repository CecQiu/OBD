package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdSimCard;
import com.hgsoft.common.dao.BaseDao;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ObdSimCardDao extends BaseDao<ObdSimCard> {

	//取最新的gps设置信息
	public ObdSimCard queryByObdSn(String obdSn) {
		final String hql = "FROM ObdSimCard WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdSimCard) query.uniqueResult();
	}
	
	
	public boolean oscSave(ObdSimCard obdSimCard) {
		try {
			this.saveOrUpdate(obdSimCard);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
