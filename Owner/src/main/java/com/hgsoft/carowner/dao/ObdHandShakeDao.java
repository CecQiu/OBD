package com.hgsoft.carowner.dao;

import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;

@Repository
public class ObdHandShakeDao extends BaseDao<ObdHandShake> {

	/**
	 * 根据设备号获取最新握手记录
	 * @param obdSn
	 * @return
	 */
	public ObdHandShake findLastBySn(String obdSn) {
		String hql = "from ObdHandShake where obdSn = :obdSn order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdHandShake) query.uniqueResult();
	}
	
	/**
	 * 根据设备号分页查询
	 * @param obdSn
	 * @param pager
	 * @return
	 */
	public List<ObdHandShake> find(String obdSn,Pager pager){
		return (List<ObdHandShake>) findByHql2("from ObdHandShake where obdSn = ? order by createTime desc", pager, obdSn);
	}
	/**
	 * 根据开始时间和结束时间查询
	 * @param begin
	 * @param end
	 * @return
	 */
	public List find(Date begin, Date end) {
		final String sql = "SELECT c  FROM (SELECT COUNT(obdSn) AS c FROM obd_handshake WHERE createTime >= ? AND createTime < ? GROUP BY obdSn) AS handShake";
		return queryBySQL(sql, begin, end);
	}
	
	@SuppressWarnings("unchecked")
	public List<ObdHandShake> queryByParams(Pager pager,Map<String,Object>map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdHandShake WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn = ? ";
		}
		
		if(map.containsKey("gpsModule")){
			hql+=" and gpsModule=? ";
		}
		
		if(map.containsKey("accelerator3D")){
			hql+=" and accelerator3D>=? ";
		}
		
		if(map.containsKey("carFaultCode")){
			hql+=" and carFaultCode=? ";
		}
		
		if(map.containsKey("wifiSet")){
			hql+=" and wifiSet=? ";
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
				if(map.containsKey("gpsModule")){
					objArray[i]=map.get("gpsModule");
					map.remove("gpsModule");
					continue;
				}
				if(map.containsKey("accelerator3D")){
					objArray[i]=map.get("accelerator3D");
					map.remove("accelerator3D");
					continue;
				}
				if(map.containsKey("carFaultCode")){
					objArray[i]=map.get("carFaultCode");
					map.remove("carFaultCode");
					continue;
				}
				
				if(map.containsKey("wifiSet")){
					objArray[i]=map.get("wifiSet");
					map.remove("wifiSet");
					continue;
				}
				
				if(map.containsKey("startTime")){
					objArray[i]= (Date) DateUtil.fromatDate((String)map.get("startTime"), "yyyy-MM-dd HH:mm:ss");
					//ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", (String)map.get("startTime"));
					map.remove("startTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i]= (Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
					//ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", (String)map.get("endTime"));
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
