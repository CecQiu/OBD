package com.hgsoft.carowner.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.FotaSet;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.StrUtil;
/**
 * 
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class FotaSetDao extends BaseDao<FotaSet> {

	//取最新的gps设置信息
	public List<FotaSet> queryListByObdSn(String obdSn) {
		final String hql = "FROM FotaSet WHERE obdSn =:obdSn and useFlag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return (List<FotaSet>)query.list();
	}
	
	//取最新的gps设置信息
	public FotaSet queryByObdSn(String obdSn) {
		final String hql = "FROM FotaSet WHERE obdSn =:obdSn and useFlag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (FotaSet) query.uniqueResult();
	}
	
	/**
	 * String obdSn,String auditResult,String valid,String mifiFlag
	 * @return
	 */
	public FotaSet queryByConditions(Map<String, Object> map) {
		String hql = "FROM FotaSet WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql += "and obdSn =:obdSn ";
		}
		if(map.containsKey("auditResult")){
			hql += "and auditResult =:auditResult ";
		}
		if(map.containsKey("valid")){
			hql += "and valid !=:valid ";
		}
		if(map.containsKey("mifiFlag")){
			hql += "and mifiFlag !=:mifiFlag ";
		}
		if(map.containsKey("useFlag")){
			hql += "and useFlag =:useFlag ";
		}
		hql +=" order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("obdSn")){
			query.setString("obdSn", (String)map.get("obdSn"));
		}
		if(map.containsKey("auditResult")){
			query.setString("auditResult", (String)map.get("auditResult"));
		}
		if(map.containsKey("valid")){
			query.setString("valid", (String)map.get("valid"));
		}
		if(map.containsKey("mifiFlag")){
			query.setString("mifiFlag", (String)map.get("mifiFlag"));
		}
		if(map.containsKey("useFlag")){
			query.setString("useFlag", (String)map.get("useFlag"));
		}
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (FotaSet) query.uniqueResult();
	}
	
	
	//取最新的wifi开关设置信息
	public int setUnuseful(String obdSn) {
		final String hql = "update FotaSet set useFlag='0' WHERE obdSn =:obdSn and useFlag ='1'";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return query.executeUpdate();
	}
	
	//取最新的wifi开关设置信息
	public int setUnusefulByVersion(String version) {
		final String hql = "update FotaSet set useFlag='0' WHERE version =:version and useFlag ='1'";
		Query query = getSession().createQuery(hql);
		query.setString("version", version);
		return query.executeUpdate();
	}
	
	//取最新的wifi开关设置信息
	public int setAuditResultByBatchVersion(String batchVersion,String auditOper,String auditResult,String auditTime) {
		final String hql = "update FotaSet set auditOper =:auditOper,auditResult =:auditResult,auditTime =:auditTime WHERE batchVersion =:batchVersion  and useFlag ='1'";
		Query query = getSession().createQuery(hql);
		query.setString("auditOper", auditOper);
		query.setString("auditResult", auditResult);
		query.setString("auditTime", auditTime);
		query.setString("batchVersion", batchVersion);
		return query.executeUpdate();
	}
	
	public boolean fsSaveOrUpdate(FotaSet fotaSet) {
		try {
			saveOrUpdate(fotaSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//判断升级记录是否存在重复
	public List<FotaSet> getListByMap(Map<String, Integer> obdSns) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String hql = "FROM FotaSet WHERE obdSn in "+obdSnList+" and useFlag='1' order by createTime desc";
		return queryByHQL(hql);
	}
	
	//判断升级记录是否存在重复
	public List<FotaSet> getListByIdMap(Set<String> ids) {
		String idArray = StrUtil.SetToString(ids);
		String hql = "FROM FotaSet WHERE id in "+idArray+" and useFlag='1' order by createTime desc";
		return queryByHQL(hql);
	}

}
