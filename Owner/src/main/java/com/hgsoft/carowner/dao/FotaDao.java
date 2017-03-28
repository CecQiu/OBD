package com.hgsoft.carowner.dao;

import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.FOTA;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.StrUtil;
/**
 * 
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class FotaDao extends BaseDao<FOTA> {

	//取最新的gps设置信息
	public FOTA queryByObdSn(String obdSn) {
		final String hql = "FROM FOTA WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (FOTA) query.uniqueResult();
	}
	
	//取最新的gps设置信息
	public List<FOTA> queryListByObdSn(String obdSn) {
		final String hql = "FROM FOTA WHERE obdSn =:obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return (List<FOTA>)query.list();
	}
	/**
	 * String obdSn,String auditResult,String valid,String mifiFlag
	 * @return
	 */
	public FOTA queryByConditions(Map<String, Object> map) {
		String hql = "FROM FOTA WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql += "and obdSn =:obdSn ";
		}
		
		hql +=" order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("obdSn")){
			query.setString("obdSn", (String)map.get("obdSn"));
		}
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (FOTA) query.uniqueResult();
	}
	
	
	public boolean fotaSaveOrUpdate(FOTA fota) {
		try {
			saveOrUpdate(fota);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//判断升级记录是否存在重复
	public List<FOTA> getListByMap(Map<String, Integer> obdSns) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String hql = "FROM FOTA WHERE obdSn in "+obdSnList+" order by createTime desc";
		return queryByHQL(hql);
	}
	
}
