package com.hgsoft.carowner.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.MebCarFault;
import com.hgsoft.common.dao.BaseDao;

@Component
public class MebCarFaultDao extends BaseDao<MebCarFault>{

	/**
	 * 通过OBDSN获得最新OBD状态记录
	 * @param obdSN
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MebCarFault lastNewMebCarFault(String obdSN) {
		final String hql = "FROM MebCarFault WHERE obdSn =:obdSn ORDER BY faultUpdateTime DESC";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("obdSn", obdSN);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<MebCarFault> list = query.list();
		return (list.size() == 1 ? list.get(0) : null);
	}
	
}
