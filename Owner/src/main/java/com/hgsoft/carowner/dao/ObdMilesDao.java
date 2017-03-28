package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdMiles;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * ObdMiles
 * @author liujialin
 *
 */
@Repository
public class ObdMilesDao extends BaseDao<ObdMiles> {
	
	/**
	 * 获取最新的ObdMiles数据
	 * @param obdSn
	 * @return
	 */
	public ObdMiles getLatest(String obdSn)throws Exception{
			final String hql = "FROM ObdMiles where obdSn=:obdSn order by createTime desc";
			Query query = getSession().createQuery(hql);
			query.setString("obdSn", obdSn);
			query.setMaxResults(1);
			query.setFirstResult(0);
			return (ObdMiles)query.uniqueResult();
	}
	
	/**
	 * 获取最新的AGPS数据
	 * @param obdSn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ObdMiles> getListByParams()throws Exception{
		try {
			final String hql = "FROM Agnss  order by createTime desc";
			Query query = getSession().createQuery(hql);
			return (List<ObdMiles>) query.list();
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
	public boolean milesSave(ObdMiles obdMiles){
		try {
			this.saveOrUpdate(obdMiles);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean milesDel(ObdMiles obdMiles){
		try {
			this.delete(obdMiles);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObdMiles> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdMiles WHERE 1=1 ";
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
