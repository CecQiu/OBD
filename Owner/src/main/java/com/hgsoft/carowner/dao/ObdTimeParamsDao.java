package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.common.dao.BaseDao;
@Repository
public class ObdTimeParamsDao extends BaseDao<OBDTimeParams> {
	//查询当前车辆参数记录是否存在
	public OBDTimeParams getObdTimeParamsBySn(String obdSn) {
		OBDTimeParams obdTimeParams = null;
		if (obdSn != null && !"".equals(obdSn)) {
			final String hql = "FROM OBDTimeParams WHERE obdSn =:obdSn order by createTime desc";
			Query query = getSession().createQuery(hql);
			query.setString("obdSn", obdSn);
			query.setMaxResults(1);
			query.setFirstResult(0);
			obdTimeParams = (OBDTimeParams) query.uniqueResult();
		}
		return obdTimeParams;
	}
}
