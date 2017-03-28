package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.common.dao.BaseDao;
@Repository
public class MebUserDao extends BaseDao<MebUser>{

	public MebUser queryUserByMobile(String userName) {
		String hql = "from MebUser where mobileNumber= :name and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("name", userName);
		return (MebUser) query.uniqueResult();
	}

	public boolean isHasOBDSN(String obdSn) {
		String hql = "select count(1) from MebUser where obdSN = :sn and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("sn", obdSn);
		long count = (Long) query.uniqueResult();
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	public MebUser queryLastByParam(Map<String,Object> map){
		if(map==null || map.size()==0){
			return null;
		}
		String hql = "FROM MebUser WHERE 1=1 ";
		if(map.containsKey("regUserId")){
			hql +="AND regUserId =:regUserId ";
		}
		if(map.containsKey("mobileNumber")){
			hql +="AND mobileNumber =:mobileNumber ";
		}
		if(map.containsKey("obdSN")){
			hql +="AND obdSN =:obdSN ";
		}
		if(map.containsKey("carId")){
			hql +="AND carId =:carId ";
		}
		
		if(map.containsKey("license")){
			hql +="AND license =:license ";
		}
		
		if(map.containsKey("name")){
			hql +="AND name =:name ";
		}
		
		if(map.containsKey("userType")){
			hql +="AND userType =:userType ";
		}
		
		if(map.containsKey("startDate")){
			hql +="AND createTime >=:startDate ";
		}
		
		if(map.containsKey("endDate")){
			hql +="AND createTime <=:endDate ";
		}
		
		if(map.containsKey("valid")){
			hql +="AND valid =:valid ";
		}
		
		hql+= "  order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("regUserId")){
			query.setString("regUserId", (String)map.get("regUserId"));
		}
		if(map.containsKey("mobileNumber")){
			query.setString("mobileNumber", (String)map.get("mobileNumber"));
		}
		if(map.containsKey("obdSN")){
			query.setString("obdSN", (String)map.get("obdSN"));
		}
		if(map.containsKey("carId")){
			query.setString("carId", (String)map.get("carId"));
		}
		
		if(map.containsKey("license")){
			query.setString("license", (String)map.get("license"));
		}
		
		if(map.containsKey("name")){
			query.setString("name", (String)map.get("name"));
		}
		
		if(map.containsKey("userType")){
			query.setString("userType", (String)map.get("userType"));
		}
		
		if(map.containsKey("startDate")){
			query.setTimestamp("createTime", (Date)map.get("startDate"));
		}
		
		if(map.containsKey("endDate")){
			query.setTimestamp("createTime", (Date)map.get("endDate"));
		}
		
		if(map.containsKey("valid")){
			query.setString("valid", (String)map.get("valid"));
		}
		
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (MebUser) query.uniqueResult();
	}
	
}
