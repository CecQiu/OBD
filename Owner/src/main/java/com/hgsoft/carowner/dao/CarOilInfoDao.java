package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.CarOilInfo;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;

@Repository
public class CarOilInfoDao extends BaseDao<CarOilInfo> {

	
	@SuppressWarnings("unchecked")
	public List<CarOilInfo> queryBetwwenTime(String obdSN, String beginTime,
			String endTime) {
		final String hql = "FROM CarOilInfo WHERE obdSN = ? " +
				"AND oilInfoTime >= ? " +
				"AND oilInfoTime <= ? AND type!=2 GROUP BY oilInfoTime";
		return queryByHQL(hql, obdSN,beginTime,endTime);
	}

	/**
	 * 油耗查询
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object[] carOilCalc(String obdSn,String starTime,String endTime){
		String sql="SELECT SUM(c.petrolConsumeNum),SUM(c.mileageNum),SUM(c.timeSpanNum) FROM car_oil_info c WHERE c.obdSN = '"+obdSn+"' AND c.oilInfoTime BETWEEN '"+starTime+"' AND '"+endTime+"' AND c.type!=2";
		List list=queryBySQL(sql, null);
		Object[] carOil=null;
		if(list!=null&&list.size()>0){
			carOil=(Object[])list.get(0);
		}
		return carOil;
	}
	/**
	 * 油耗查询
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List carOilListCalc(String obdSn,String starTime,String endTime){
		String sql="SELECT DATE_FORMAT(c.oilInfoTime,'%Y-%m-%d'),SUM(c.petrolConsumeNum),SUM(c.mileageNum),SUM(c.timeSpanNum) FROM car_oil_info c WHERE c.obdSN = '"+obdSn+"' AND c.oilInfoTime BETWEEN '"+starTime+"' AND '"+endTime+"' AND c.type!=2 GROUP BY DATE_FORMAT(c.oilInfoTime,'%Y-%m-%d') ORDER BY c.oilInfoTime DESC";
		List list=queryBySQL(sql, null);
		return list;
	}

	public CarOilInfo getCarOilInfoByTime(String obdSn, Date oilInfoTime) {
		String hql = "FROM CarOilInfo WHERE oilInfoTime = :oilInfoTime AND type!=2";
		Query query = getSession().createQuery(hql);
		String _oilInfoTime=(String)DateUtil.fromatDate(oilInfoTime, "yyyy-MM-dd HH:mm:ss");
		query.setString("oilInfoTime", _oilInfoTime);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (CarOilInfo) query.uniqueResult();
	}

	public List<CarOilInfo> queryOilInfo(String obdSn) {
		final String hql = "from CarOilInfo where obdSn =:obdSn and type = 1 order by oilInfoTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		return query.list();
	}

	public CarOilInfo queryByIdAndObdSn(String id, String obdSn){
		return queryFirst("from CarOilInfo where obdSn = ? and oilInfoId = ?", obdSn, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CarOilInfo> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM CarOilInfo WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSN = ? ";
		}
		if(map.containsKey("type")){
			hql+=" and type = ? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and oilInfoTime>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and oilInfoTime<=? ";
		}
		
		hql+=" order by oilInfoTime desc "; 
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
