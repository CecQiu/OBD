package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;

@Repository
public class CarTraveltrackDao extends BaseDao<CarTraveltrack> {
	
	public CarTraveltrack findLastByN(int n) {
		String hql = "from CarTraveltrack c where obdSn='00000000000000687e9fca64' order by travelEnd desc";
		Query query = getSession().createQuery(hql);
		query.setMaxResults(1);
		query.setFirstResult(n);
		return (CarTraveltrack) query.uniqueResult();
	}
	
	public CarTraveltrack findLastBySn(String sn) {
//		String hql = "from CarTraveltrack c where c.obdsn = :sno and c.mebUser.valid='1' order by insesrtTime desc";
		String hql = "from CarTraveltrack c where c.obdsn = :sno and c.type!=2 order by insesrtTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (CarTraveltrack) query.uniqueResult();
	}
	/**
	 * 
	 * @param sn
	 * @param start
	 * @param end
	 * @return
	 */
	public CarTraveltrack getTravelByObdAndTime(String sn,Date start,Date end) {
		String startStr=(String)DateUtil.fromatDate(start, "yyyy-MM-dd HH:mm:ss");
		String endStr=(String)DateUtil.fromatDate(end, "yyyy-MM-dd HH:mm:ss");
		String hql = "from CarTraveltrack c where c.obdsn=:osn and c.travelStart=:ostart and c.travelEnd=:oend and c.type!=2";
		Query query = getSession().createQuery(hql);
		query.setString("osn", sn);
		query.setString("ostart", startStr);
		query.setString("oend", endStr);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (CarTraveltrack) query.uniqueResult();
	}
	
	/**
	 * 根据设备号、开始时间、结束时间 获取行程记录
	 * @param obdSn
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CarTraveltrack> queryTravelByObdSnTime(String obdSn,String start,String end){
		Date startDate = (Date)DateUtil.fromatDate(start, "yyyy-MM-dd HH:mm:ss");
		Date endDate = (Date)DateUtil.fromatDate(end, "yyyy-MM-dd HH:mm:ss");
		final String hql = "from CarTraveltrack where obdSn=? and travelStart > ? and travelEnd < ? and type!=2";
		return queryByHQL(hql,obdSn, startDate,endDate);
	}

	public List<CarTraveltrack> queryTravel(String obdSn, Date travelStart) {
		String startStr=(String)DateUtil.fromatDate(travelStart, "yyyy-MM-dd HH:mm:ss");
		final String hql = "from CarTraveltrack where obdSn=:obdSn and travelStart=:travelStart and type!=2 order by travelStart desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("travelStart", startStr);
		return query.list();
	}

	public void updateCarTraveltrack(CarTraveltrack ct) {
			update(ct);
	}
	
	@SuppressWarnings("unchecked")
	public List<CarTraveltrack> queryByParams(Pager pager,Map<String,Object>map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM CarTraveltrack WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSN = ? ";
		}
		
		if(map.containsKey("quickenNum")){
			hql+=" and quickenNum>=? ";
		}
		
		if(map.containsKey("quickSlowDown")){
			hql+=" and quickSlowDown>=? ";
		}
		
		if(map.containsKey("voltage")){
			String voltageFlag = (String) map.get("voltageFlag");
			switch (voltageFlag) {
			case "1":
				hql+=" and voltage>=? ";
				break;
			case "0":
				hql+=" and voltage<=? ";
				break;	
			default:
				break;
			}
		}
		
		if(map.containsKey("driverTime")){
			String driverTimeFlag = (String) map.get("driverTimeFlag");
			switch (driverTimeFlag) {
			case "1":
				hql+=" and driverTime>=? ";
				break;
			case "0":
				hql+=" and driverTime<=? ";
				break;	
			default:
				break;
			}
		}
		
		if(map.containsKey("distance")){
			String distanceFlag = (String) map.get("distanceFlag");
			switch (distanceFlag) {
			case "1":
				hql+=" and distance>=? ";
				break;
			case "0":
				hql+=" and distance<=? ";
				break;	
			default:
				break;
			}
		}
		
		if(map.containsKey("type")){
			hql+=" and type=? ";
		}
		
		if(map.containsKey("startTime")){
			hql+=" and travelStart>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and travelEnd<=? ";
		}
		
		hql+=" order by insesrtTime desc "; 
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
					continue;
				}
				if(map.containsKey("quickenNum")){
					objArray[i]=map.get("quickenNum");
					map.remove("quickenNum");
					continue;
				}
				if(map.containsKey("quickSlowDown")){
					objArray[i]=map.get("quickSlowDown");
					map.remove("quickSlowDown");
					continue;
				}
				if(map.containsKey("voltage")){
					objArray[i]=map.get("voltage");
					map.remove("voltage");
					continue;
				}
				if(map.containsKey("driverTime")){
					objArray[i]=map.get("driverTime");
					map.remove("driverTime");
					continue;
				}
				if(map.containsKey("distance")){
					objArray[i]=map.get("distance");
					map.remove("distance");
					continue;
				}
				if(map.containsKey("type")){
					objArray[i]=map.get("type");
					map.remove("type");
					continue;
				}
				if(map.containsKey("startTime")){
					objArray[i]= (Date) DateUtil.fromatDate((String)map.get("startTime"), "yyyy-MM-dd HH:mm:ss");
//							ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", (String)map.get("startTime"));
					map.remove("startTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i] = (Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
//							ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", (String)map.get("endTime"));
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
