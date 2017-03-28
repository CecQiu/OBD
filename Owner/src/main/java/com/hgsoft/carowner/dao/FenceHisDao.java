package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.FenceHis;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;

/**
 * 电子围栏历史dao
 * 
 * @author liujialin 2015-8-5
 */
@Repository
public class FenceHisDao extends BaseDao<FenceHis> {

	// 
	@SuppressWarnings("unchecked")
	public List<FenceHis> queryListByObdSn(String obdSn, Integer areaNum,Integer valid) {
		String hql = "FROM FenceHis WHERE 1=1 ";
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
		return (List<FenceHis>) query.list();
	}


	public boolean fsave(FenceHis fenceHis) {
		try {
			this.saveOrUpdate(fenceHis);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<FenceHis> queryByParams(Pager pager,Map<String,Object>map) {
		if(map.size()==1){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM FenceHis WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn =? ";
		}
		if(map.containsKey("type")){
			hql+=" and type =? ";
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
				
				if(map.containsKey("type")){
					objArray[i]=map.get("type");
					map.remove("type");
					continue;
				}
				
				if(map.containsKey("startTime")){
					objArray[i]= (Date) DateUtil.fromatDate((String)map.get("startTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("startTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i]= (Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
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
