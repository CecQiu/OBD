package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.AGPS;
import com.hgsoft.common.dao.BaseDao;
/**
 * AGPS
 * @author liujialin
 *
 */
@Repository
public class AGPSDao extends BaseDao<AGPS> {
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	public AGPS findLastBySn(String obdSn){
		final String hql = "FROM AGPS WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (AGPS) query.uniqueResult();
	}
}
