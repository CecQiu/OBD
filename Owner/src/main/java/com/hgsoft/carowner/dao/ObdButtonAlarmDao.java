package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.ObdButtonAlarm;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * 
 *
 * @author sjg
 * @version  [版本号, 2016年12月1日]
 */
@Repository
public class ObdButtonAlarmDao extends BaseDao<ObdButtonAlarm> {
	
	/**
	 * 查询最新记录
	 */
	public ObdButtonAlarm queryLast(String obdSn)throws Exception{
		final String hql = "FROM ObdButtonAlarm where obdSn=:obdSn order by time desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdButtonAlarm)query.uniqueResult();
	}
	
	public boolean obdButtonAlarmSaveOrUpdate(ObdButtonAlarm obdButtonAlarm){
		try {
			this.saveOrUpdate(obdButtonAlarm);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<ObdButtonAlarm> queryByParams(Pager pager, Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdButtonAlarm WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn = ? ";
		}
		
		if(map.containsKey("startTime")){
			hql+=" and time>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and time<=? ";
		}
		
		hql+=" order by time desc ";
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
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
