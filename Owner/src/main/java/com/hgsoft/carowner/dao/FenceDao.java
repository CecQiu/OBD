package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.Fence;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * 电子围栏dao
 * 
 * @author liujialin 2015-8-5
 */
@Repository
public class FenceDao extends BaseDao<Fence> {

	// 取最新的gps设置信息
	@SuppressWarnings("unchecked")
	public List<Fence> queryListByObdSn(String obdSn, Integer areaNum,Integer valid) {
		String hql = "FROM Fence WHERE 1=1 ";
		if (!StringUtils.isEmpty(obdSn)) {
			hql += " and obdSn =:obdSn ";
		}
		if (areaNum != null) {
			hql += " and areaNum =:areaNum ";
		}
		if (valid != null) {
			hql += " and valid =:valid ";
		}

		hql += " order by createTime desc";
		Query query = getSession().createQuery(hql);
		if (!StringUtils.isEmpty(obdSn)) {
			query.setString("obdSn", obdSn);
		}
		if (areaNum != null) {
			query.setInteger("areaNum", areaNum);
		}

		if (valid != null) {
			query.setInteger("valid", valid);
		}
		return (List<Fence>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Fence> queryList(String obdSn, Integer areaNum,List<Integer> valid) {
		String hql = "FROM Fence WHERE 1=1 ";
		if (!StringUtils.isEmpty(obdSn)) {
			hql += " and obdSn =:obdSn ";
		}
		if (areaNum != null) {
			hql += " and areaNum =:areaNum ";
		}
		if (valid != null) {
			hql += " and valid in (:valid) ";
		}

		hql += " order by createTime desc";
		Query query = getSession().createQuery(hql);
		if (!StringUtils.isEmpty(obdSn)) {
			query.setString("obdSn", obdSn);
		}
		if (areaNum != null) {
			query.setInteger("areaNum", areaNum);
		}

		if (valid != null) {
			query.setParameterList("valid", valid);
		}
		return (List<Fence>) query.list();
	}

	// 将当前设备所有电子围栏设为无效
	public int setNoValid(String obdSn, Integer areaNum, Integer valid,Set<Integer> validSet) {
		String validStr = StrUtil.intSetToString(validSet);
		String updateTime = (String) DateUtil.fromatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
		String hql = "update Fence set valid=:valid,updateTime=:updateTime WHERE valid in"+validStr+" and obdSn =:obdSn";
		if (areaNum != null) {
			hql += " and areaNum=:areaNum ";
		}
		Query query = getSession().createQuery(hql);
		query.setInteger("valid", valid);
		query.setString("updateTime", updateTime);
		query.setString("obdSn", obdSn);
		if (areaNum != null) {
			query.setInteger("areaNum", areaNum);
		}
		return query.executeUpdate();
	}

	public boolean fsave(Fence fence) {
		try {
			this.saveOrUpdate(fence);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean fdel(Fence fence) {
		try {
			this.delete(fence);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public Integer fenceListDel(String obdSn, Integer areaNum,List<String> id) {
		try {
			String hql = "DELETE FROM Fence WHERE 1=1 ";
			if (!StringUtils.isEmpty(obdSn)) {
				hql += " and obdSn =:obdSn ";
			}
			if (areaNum != null) {
				hql += " and areaNum =:areaNum ";
			}
			if (id != null && id.size()!=0) {
				hql += " and id in (:id) ";
			}

			Query query = getSession().createQuery(hql);
			if (!StringUtils.isEmpty(obdSn)) {
				query.setString("obdSn", obdSn);
			}
			if (areaNum != null) {
				query.setInteger("areaNum", areaNum);
			}

			if (id != null && id.size()!=0) {
				query.setParameterList("id", id);
			}
			
			return query.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return 0;
	}

	// 判断是否存在重复
	@SuppressWarnings("unchecked")
	public List<Fence> getListByMap(Map<String, Integer> obdSns, Integer areaNum) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String hql = "FROM Fence WHERE obdSn in " + obdSnList + " and areaNum=" + areaNum + " and valid in (1,2)";
		return queryByHQL(hql);
	}
	
	public Fence queryLastByParams(String obdSn, Integer areaNum){
		String hql = "FROM Fence WHERE 1=1 and valid !=0 and valid !=-1 ";
		if (!StringUtils.isEmpty(obdSn)) {
			hql += " and obdSn =:obdSn ";
		}
		if (areaNum != null) {
			hql += " and areaNum =:areaNum ";
		}

		hql += " order by createTime desc";
		Query query = getSession().createQuery(hql);
		if (!StringUtils.isEmpty(obdSn)) {
			query.setString("obdSn", obdSn);
		}
		if (areaNum != null) {
			query.setInteger("areaNum", areaNum);
		}

		query.setMaxResults(1);
		query.setFirstResult(0);
		return (Fence) query.uniqueResult();
	}
}
