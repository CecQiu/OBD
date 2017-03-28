package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.common.dao.BaseDao;
@Repository
public class ObdTravelParamsDao extends BaseDao<OBDTravelParams> {
	//根据设备号查询最新一条有效的电子围栏
	public OBDTravelParams queryByObdSn(String obdSn) {
		final String hql = "FROM OBDTravelParams WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (OBDTravelParams) query.uniqueResult();
	}
	
}
