package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.common.dao.BaseDao;
/**
 * obd状态信息
 * @author liujialin
 * 2016-5-20
 */
@Repository
public class ObdStateDao extends BaseDao<ObdState> {

	//取最新的gps设置信息
	public ObdState queryByObdSn(String obdSn) {
		final String hql = "FROM ObdState WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdState) query.uniqueResult();
	}
	
}
