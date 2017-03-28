package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdGroupAGPS;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * @author sjg
 */
@Repository
public class ObdGroupAGPSDao extends BaseDao<ObdGroupAGPS> {

	public ObdGroupAGPS queryByGroupNum(String groupNum) {
		final String hql = "FROM ObdGroupAGPS WHERE groupNum =:groupNum order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("groupNum", groupNum);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdGroupAGPS) query.uniqueResult();
	}

	
	@SuppressWarnings("unchecked")
	public List<ObdGroupAGPS> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdGroupAGPS WHERE 1=1 ";
		if(map.containsKey("groupNum")){
			hql+=" and groupNum = ? ";
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
				if(map.containsKey("groupNum")){
					objArray[i]=map.get("groupNum");
					map.remove("groupNum");
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
