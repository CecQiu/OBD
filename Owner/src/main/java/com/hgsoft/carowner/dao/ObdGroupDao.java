package com.hgsoft.carowner.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.common.dao.BaseDao;
/**
 * wifi设置dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ObdGroupDao extends BaseDao<ObdGroup> {

	//取最新的gps设置信息
	@SuppressWarnings("unchecked")
	public List<ObdGroup> queryList() {
		final String hql = "FROM ObdGroup where valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
//		query.setString("obdSn", obdSn);
		return (List<ObdGroup>)query.list();
	}
	
	//取最新的gps设置信息
	public ObdGroup queryByGroupNum(String groupNum) {
		final String hql = "FROM ObdGroup WHERE groupNum =:groupNum and valid='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("groupNum", groupNum);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdGroup) query.uniqueResult();
	}
	
	
	public boolean wifiSetSaveOrUpdate(ObdGroup obdGroup) {
		try {
			saveOrUpdate(obdGroup);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
