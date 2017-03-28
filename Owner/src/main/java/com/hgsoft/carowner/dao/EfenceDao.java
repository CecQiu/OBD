package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.Efence;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
/**
 * 电子围栏dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class EfenceDao extends BaseDao<Efence> {

	//取最新的gps设置信息
	public List<Efence> queryListByObdSn(String obdSn) {
		final String hql = "FROM Efence WHERE obdSn =:obdSn and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return (List<Efence>)query.list();
	}
	
	//查询所以有效的定时定点电子围栏
	public List<Efence> queryListByObdSnAndType(String obdSn,String type) {
		final String hql = "FROM Efence WHERE obdSn =:obdSn and type=:type and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		return (List<Efence>)query.list();
	}
	
	//根据设备号和区域编号查询
	public Efence queryByObdSn(String obdSn,String areaNum) {
		final String hql = "FROM Efence WHERE obdSn =:obdSn and areaNum =:areaNum and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("areaNum", areaNum);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (Efence) query.uniqueResult();
	}
	
	//根据设备号查询最新一条有效的电子围栏
	public Efence queryLastByObdSn(String obdSn) {
		final String hql = "FROM Efence WHERE obdSn =:obdSn and railAndAlert !='4' and railAndAlert!='5' and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (Efence) query.uniqueResult();
	}
	
	//将当前设备所有电子围栏设为无效
	public int efenceNoValid(String obdSn) {
		String updateTime=(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		final String hql = "update Efence set valid='0',updateTime=:updateTime WHERE obdSn =:obdSn and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("updateTime", updateTime);
		query.setString("obdSn", obdSn);
		return query.executeUpdate();
	}
	
	

	//将当前设备所有定时电子围栏设为无效
	public int efenceTimeNoValid(String obdSn) {
		String updateTime=(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String hql = "update Efence set valid='0',updateTime=:updateTime WHERE obdSn =:obdSn and endDate<:updateTime and type='1' and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("updateTime", updateTime);
		return query.executeUpdate();
	}
	
	//将当前设备对应编号的电子围栏设为无效
	public int efenceNoValidByAreaNum(String obdSn,String areaNum) {
		String updateTime=(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		final String hql = "update Efence set valid='0',updateTime=:updateTime WHERE obdSn =:obdSn and areaNum =:areaNum and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("updateTime", updateTime);
		query.setString("areaNum", areaNum);
		query.setString("obdSn", obdSn);
		return query.executeUpdate();
	}
	
	public boolean efenceSaveOrUpdate(Efence Efence) {
		try {
			saveOrUpdate(Efence);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
