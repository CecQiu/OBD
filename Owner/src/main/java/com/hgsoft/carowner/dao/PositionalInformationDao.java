package com.hgsoft.carowner.dao;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.PositionalInformation;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;

@Repository
public class PositionalInformationDao extends BaseDao<PositionalInformation> {
	/**
	 * 不能根据insertTime desc查询，因为位置信息会因为网络延迟而导致批量上传，而insertTime只是系统事件，所以要根据gpsTime查询
	 * @param sn
	 * @return
	 */
	public PositionalInformation findLastBySn(String sn) {
		String hql = "from PositionalInformation where obdsn = :sno and longitude is not null and latitude is not null and statusGps = 1 order by gpsTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionalInformation) query.uniqueResult();
	}
	/**
	 * 根据obdSn和上次行程记录单的最后行程结束时间，查找出之后的第一条位置信息
	 * @param sn
	 * @return
	 */
	public PositionalInformation findLastBySnAndGpstime(String sn,Date end) {
		String endStr=(String)DateUtil.fromatDate(end, "YYYY-MM-dd HH:mm:ss");
		String hql = "from PositionalInformation where obdsn = :sno  and gpsTime>:send and statusGps = 1 order by gpsTime asc";
//		String hql = "from PositionalInformation where obdsn ='"+sn+"' and gpsTime>'"+endStr+"' order by gpsTime asc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setString("send", endStr);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionalInformation) query.uniqueResult();
	}
	/**
	 * 根据obdSn和上次行程记录单的最后行程结束时间，查询小于等于改设备最后一次行程记录的行程结束时间的最后一条位置信息
	 * @param sn
	 * @return
	 */
	public PositionalInformation findFirstBySnAndGpstime(String sn,Date end) {
		String endStr=(String)DateUtil.fromatDate(end, "YYYY-MM-dd HH:mm:ss");
		String hql = "from PositionalInformation where obdsn = :sno  and gpsTime<=:send and statusGps = 1 order by gpsTime desc";
//		String hql = "from PositionalInformation where obdsn ='"+sn+"' and gpsTime>'"+endStr+"' order by gpsTime asc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setString("send", endStr);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionalInformation) query.uniqueResult();
	}
	/**
	 * 根据obdSn，查找出设备的第一条位置信息
	 * @param sn
	 * @return
	 */
	public PositionalInformation findFirstBySn(String sn) {
		String hql = "from PositionalInformation where obdsn = :sno and statusGps = 1 order by gpsTime asc";
//		String hql = "from PositionalInformation where obdsn ='"+sn+"' and gpsTime>'"+endStr+"' order by gpsTime asc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionalInformation) query.uniqueResult();
	}
	
	/**
	 * 根据obdSn和gpstime时间查询记录，如果存在该条记录，不保存入库
	 * @param sn
	 * @return
	 */
	public PositionalInformation findByObdsnAndGpstime(String obdSn,Date gpstime) {
		String gpstimeStr=(String)DateUtil.fromatDate(gpstime, "YYYY-MM-dd HH:mm:ss");
		String hql = "from PositionalInformation where obdsn = :sno and gpsTime =:sgt order by gpsTime asc";
//		String hql = "from PositionalInformation where obdsn ='"+sn+"' and gpsTime>'"+endStr+"' order by gpsTime asc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", obdSn);
		query.setString("sgt", gpstimeStr);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionalInformation) query.uniqueResult();
	}
	
}
