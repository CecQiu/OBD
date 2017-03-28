package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.CarDriveInfo;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;

@Repository
public class CarDriveInfoDao extends BaseDao<CarDriveInfo> {

	@SuppressWarnings("unchecked")
	public List<CarDriveInfo> queryDriveInfo(String obdSN, String beginDate, String endDate) {
		final String hql = "FROM CarDriveInfo WHERE obdSn = ? AND driveDate >= ? AND driveDate <= ? AND type!=2";
		return queryByHQL(hql, obdSN, beginDate, endDate);
	}

	public CarDriveInfo getDriveInfoByDriveDate(String obdSn, Date driveDate) {
		String _driveDate = (String)DateUtil.fromatDate(driveDate, "yyyy-MM-dd HH:mm:ss");
		String hql = "from CarDriveInfo where obdSn=:obdSn and driveDate=:driveDate and type!=2";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("driveDate", _driveDate);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (CarDriveInfo) query.uniqueResult();
	}

	
	public List<CarDriveInfo> queryDriveInfo(String obdSn) {
		final String hql = "from CarDriveInfo where obdSn =:obdSn and type = 1 order by driveDate desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return query.list();
	}

	public CarDriveInfo queryByIdAndObdSn(String id, String obdSn){
		return queryFirst("from CarDriveInfo where obdSn = ? and driveInfoId = ?", obdSn, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CarDriveInfo> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM CarDriveInfo WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn = ? ";
		}
		if(map.containsKey("type")){
			hql+=" and type = ? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and driveDate>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and driveDate<=? ";
		}
		
		hql+=" order by driveDate desc "; 
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
