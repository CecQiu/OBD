package com.hgsoft.carowner.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.Agnss;
import com.hgsoft.common.dao.BaseDao;
/**
 * AGNSS
 * @author liujialin
 *
 */
@Repository
public class AgnssDao extends BaseDao<Agnss> {
	
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	public Agnss getLatest(){
		try {
			final String hql = "FROM Agnss  order by createTime desc";
			Query query = getSession().createQuery(hql);
			query.setMaxResults(1);
			query.setFirstResult(0);
			return (Agnss)query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Agnss> getLast(){
		try {
			final String hql = "FROM Agnss  order by createTime desc";
			Query query = getSession().createQuery(hql);
			return (List<Agnss>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	public boolean agnssSave(Agnss agnss){
		try {
			this.saveOrUpdate(agnss);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean agnssDel(Agnss agnss){
		try {
			this.delete(agnss);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
