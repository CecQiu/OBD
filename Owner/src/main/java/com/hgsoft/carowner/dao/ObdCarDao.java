package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdCar;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * AGNSS
 * @author liujialin
 *
 */
@Repository
public class ObdCarDao extends BaseDao<ObdCar> {
	
	/**
	 * 获取最新的ObdCar数据
	 * @param obdSn
	 * @return
	 */
	public ObdCar getLatest(String obdSn){
		final String hql = "FROM ObdCar where obdSn=:obdSn  order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdCar)query.uniqueResult();
	}
	
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ObdCar> getLast(String obdSn){
		final String hql = "FROM ObdCar where obdSn=:obdSn  order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return (List<ObdCar>) query.list();
	}
	
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	public boolean obdCarSave(ObdCar obdCar){
		try {
			this.saveOrUpdate(obdCar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean obdCarDel(ObdCar obdCar){
		try {
			this.delete(obdCar);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObdCar> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdCar WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn = ? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime<=? ";
		}
		
		hql+=" order by createTime desc "; 
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
					continue;
				}
				if(map.containsKey("startTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("startTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("startTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("endTime");
					continue;
				}
			}
			if(pager!=null){
				return queryByHQL(pager, hql, objArray);
			}else{
				return queryByHQL(hql,objArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
