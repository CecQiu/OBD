package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.common.dao.BaseDao;
/**
 * 车辆参数
 * @author liujialin
 *
 */
@Repository
public class WifiDao extends BaseDao<Wifi> {

	//查询当前车辆参数记录是否存在
	public Wifi isExist(String obdSn) {
		Wifi wifi = null;
		if (obdSn != null && !"".equals(obdSn)) {
			final String hql = "FROM Wifi WHERE obdSn =:obdSn order by createTime desc";
			Query query = getSession().createQuery(hql);
			query.setString("obdSn", obdSn);
			query.setMaxResults(1);
			query.setFirstResult(0);
			wifi = (Wifi) query.uniqueResult();
		}
		return wifi;
	}
}
