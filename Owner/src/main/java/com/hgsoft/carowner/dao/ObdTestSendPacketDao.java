package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.ObdTestSendPacket;
import com.hgsoft.common.dao.BaseDao;
/**
 * 
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ObdTestSendPacketDao extends BaseDao<ObdTestSendPacket> {

	//
	public List<ObdTestSendPacket> queryByObdSn(Map<String,Object> map) {
		String hql = "FROM ObdTestSendPacket WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+="and obdSn =:obdSn ";
		}
		if(map.containsKey("sended")){
			hql+="and sended =:sended ";
		}
		if(map.containsKey("startDate")){
			hql +="and createTime >=:startDate ";
		}
		if(map.containsKey("endDate")){
			hql +="and createTime <=:endDate ";
		}
		hql+=" order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("obdSn")){
			String obdSn = (String) map.get("obdSn");
			query.setString("obdSn", obdSn);
		}
		if(map.containsKey("startDate")){
			Date startDate = (Date) map.get("startDate");
			query.setTimestamp("startDate", startDate);
		}
		if(map.containsKey("endDate")){
			Date endDate = (Date) map.get("endDate");
			query.setTimestamp("endDate", endDate);
		}
		return  query.list();
	}
	
	public boolean sendPacketSave(ObdTestSendPacket obdTestSendPacket) {
		try {
			save(obdTestSendPacket);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean sendPacketUpdate(ObdTestSendPacket obdTestSendPacket) {
		try {
			update(obdTestSendPacket);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ObdTestSendPacket queryByObdSnAndMsgBody(String obdSn, String msgBody) {
		final String hql = "FROM ObdTestSendPacket WHERE obdSn =:obdSn AND msgBody =:msgBody AND sended=0  order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("msgBody", msgBody);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdTestSendPacket) query.uniqueResult();
	}

	public List<ObdTestSendPacket> queryByObdSn(String obdSn){
		final String hql = "FROM ObdTestSendPacket WHERE obdSn = ? AND sended=0";
		return queryByHQL(hql, obdSn);
	}
	
}
