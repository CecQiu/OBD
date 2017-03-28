package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.UpgradeSetSpeed;
import com.hgsoft.common.dao.BaseDao;
/**
 *待升级设备速度条件
 * @author sjg
 */
@Repository
public class UpgradeSetSpeedDao extends BaseDao<UpgradeSetSpeed> {

	public UpgradeSetSpeed queryByObdSn(String obdSn) {
		final String hql = "FROM UpgradeSetSpeed WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSetSpeed) query.uniqueResult();
	}

}
