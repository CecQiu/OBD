package com.hgsoft.carowner.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.DriveBehaviour;
import com.hgsoft.common.dao.BaseDao;

@Repository
public class DriveBehaviourDao extends BaseDao<DriveBehaviour> {
	
	/**
	 * 驾驶行为总数统计
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	public Integer getBDListTotalByObdSnAndTime(String starTime,String endTime,String obdSn){
		String sql="SELECT COUNT(1) FROM (SELECT * FROM (SELECT pi.obdSn,pi.time,pi.longitude,pi.latitude,pd.overspeed,pd.suddenTurn,pd.suddenUpSpeed,pd.suddenDownSpeed,pd.suddenBrake,pd.suddenChangeRoad,pd.speedNotMatch,pd.longLowSpeed FROM position_info pi JOIN position_drive_info pd ON pi.id = pd.positionInfoId "
				+ "where pi.obdSn ='"+obdSn+"' AND pi.time BETWEEN '"+starTime+"' AND '"+endTime+"') p where p.overspeed =1 OR p.suddenTurn =1 OR p.suddenUpSpeed=1 OR p.suddenDownSpeed=1 OR p.suddenBrake =1 OR p.suddenChangeRoad =1 OR p.speedNotMatch =1 OR p.longLowSpeed=1)PC  ";
		BigInteger total=(BigInteger)findBySql(sql);
		return total.intValue();
	}

	/**
	 * 驾驶优化
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	public List<DriveBehaviour> getBDListByObdSnAndTime(String starTime,String endTime,String obdSn,Integer page,Integer pageSize){
		if(page==0){
			page = 1;
		}
		if(pageSize ==0){
			pageSize = 20;
		}
		String sql="SELECT * FROM (SELECT pi.obdSn,pi.time,pi.longitude,pi.latitude,pd.overspeed,pd.suddenTurn,pd.suddenUpSpeed,pd.suddenDownSpeed,pd.suddenBrake,pd.suddenChangeRoad,pd.speedNotMatch,pd.longLowSpeed FROM position_info pi JOIN position_drive_info pd ON pi.id = pd.positionInfoId "
				+ "where pi.obdSn ='"+obdSn+"' AND pi.time BETWEEN '"+starTime+"' AND '"+endTime+"') p where p.overspeed =1 OR p.suddenTurn =1 OR p.suddenUpSpeed=1 OR p.suddenDownSpeed=1 OR p.suddenBrake =1 OR p.suddenChangeRoad =1 OR p.speedNotMatch =1 OR p.longLowSpeed=1 ORDER BY p.time DESC limit "
				+(page-1)*pageSize+","+pageSize;
		Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(DriveBehaviour.class));
		List<DriveBehaviour> list=query.list();
		return list;
	}
 
}
