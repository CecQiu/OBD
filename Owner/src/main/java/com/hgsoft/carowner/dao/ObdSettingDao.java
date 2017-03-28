package com.hgsoft.carowner.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.StrUtil;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ObdSettingDao extends BaseDao<ObdSetting> {

	//取最新的obd设置信息
	public ObdSetting queryByObdSn(String obdSn) {
		final String hql = "FROM ObdSetting WHERE obdSn =:obdSn and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdSetting) query.uniqueResult();
	}
	
	//将记录置为无效
	public int obdSettingNoValid(String obdSn,String type) {
		String time = DateUtil.getCurrentTime();
		String hql = "update ObdSetting set valid='0',updateTime=:time WHERE valid='1' and obdSn =:obdSn and type =:type";
		Query query = getSession().createQuery(hql);
		query.setString("time", time);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		return query.executeUpdate();
	}
	
	//将记录置为无效
	public int setNoValidByLikeType(String obdSn,String type) {
		String time = DateUtil.getCurrentTime();
		String hql = "update ObdSetting set valid='0',updateTime=:time WHERE valid='1' and obdSn =:obdSn and type like :type";
		Query query = getSession().createQuery(hql);
		query.setString("time", time);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		return query.executeUpdate();
	}
	
	//将记录置为无效
	public int setNoValidByInType(String obdSn,Map<String, Integer> types) {
		String inType = StrUtil.MapToString(types);
		String time = DateUtil.getCurrentTime();
		String hql = "update ObdSetting set valid='0',updateTime=:time WHERE valid='1' and obdSn =:obdSn and type in " + inType;
		Query query = getSession().createQuery(hql);
		query.setString("time", time);
		query.setString("obdSn", obdSn);
		return query.executeUpdate();
	}
	

	public ObdSetting queryLastObdSetting(String obdSn,String type) {
		final String hql = "FROM ObdSetting WHERE obdSn =:obdSn  and type =:type and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("type", type);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdSetting) query.uniqueResult();
	}
	
	public List<ObdSetting> queryByObdSnAndType(String obdSn,String type){
		final String hql = "FROM ObdSetting WHERE obdSn = ? and type like ? and valid='1' order by createTime desc";
		return (List<ObdSetting>)queryByHQL(hql, obdSn,type);
	}
	
	//判断升级记录是否存在重复
	public List<ObdSetting> getOSListByMap(Map<String, Integer> obdSns,String type) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String hql = "FROM ObdSetting WHERE obdSn in "+obdSnList+" and type='"+type+"' and valid='1' order by createTime desc";
		return queryByHQL(hql);
	}
	
	//根据多个type查询
	public List<ObdSetting> getListByTypes(String obdSn,Set<String> types) {
		String typeList = StrUtil.SetToString(types);
		String hql = "FROM ObdSetting WHERE obdSn ='"+obdSn+"' and type in "+typeList+" and valid='1' order by createTime desc";
		return queryByHQL(hql);
	}
}
