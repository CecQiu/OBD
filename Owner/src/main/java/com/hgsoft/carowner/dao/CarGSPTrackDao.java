package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;

@Repository
public class CarGSPTrackDao extends BaseDao<CarGSPTrack> {
	/**
	 * 查询运行轨迹
	 * @param obdSN
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CarGSPTrack> queryTrack(String obdSN, String startTime,String endTime) {
//		final String hql = "FROM CarGSPTrack WHERE obdSN = ? AND gspTrackTime >= ? AND gspTrackTime <= ? order by gspTrackTime asc"; 
		final String hql = "FROM CarGSPTrack WHERE obdSN = ? AND gspTrackTime >= ? AND gspTrackTime <= ? order by gspTrackTime asc "; 
		try {
			Date startD=(Date) DateUtil.fromatDate(startTime, "yyyy-MM-dd HH:mm:ss");
			Date endD=(Date) DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss");
//			return queryByHQL(hql, obdSN,ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", startTime),ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", endTime));
			return queryByHQL(hql, obdSN,startD,endD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询最新的gps位置信息
	 * @param sn
	 * @return
	 */
	public CarGSPTrack findLastBySn(String sn) {
		String hql = "from CarGSPTrack where obdsn = :sno order by gspTrackTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (CarGSPTrack) query.uniqueResult();
	}
}
