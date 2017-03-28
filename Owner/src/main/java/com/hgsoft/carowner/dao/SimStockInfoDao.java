package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.common.dao.BaseDao;

@Component
public class SimStockInfoDao extends BaseDao<SimStockInfo>{

	/**
	 * 通过SIM卡号获得流量卡
	 * @param simId
	 * @return
	 */
	public SimStockInfo queryBySimId(String simId){
		final String hql = "FROM SimStockInfo WHERE simId = :simId ";
		Query query = getSession().createQuery(hql);
		query.setString("simId", simId);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (SimStockInfo) query.uniqueResult();
	}
	
	/**
	 * 通过OBDSN获得流量卡
	 * @param obdSn
	 * @return
	 */
	public SimStockInfo queryBySn(String obdSn){
		final String hql = "FROM SimStockInfo WHERE obdSn = :obdSn ";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (SimStockInfo) query.uniqueResult();
	}

	public SimStockInfo queryLastByObdSn(String obdSn) {
		return queryFirst("from SimStockInfo where obdSn = ? order by createTime desc", obdSn);
	}
}
