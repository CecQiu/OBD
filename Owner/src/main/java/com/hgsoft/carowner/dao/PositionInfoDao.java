package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;

@Repository
public class PositionInfoDao extends BaseDao<PositionInfo> {

	/**
	 * 根据OBDSN和时间查询位置信息
	 * @param obdSn
	 * @param time
	 * @return
	 */
	public PositionInfo queryBySnAndTime(String obdSn,Date time){
		List<PositionInfo> positionInfos = queryByHQL("from PositionInfo where obdSn=? and time = ?", obdSn,time);
		return positionInfos.size() > 0 ? positionInfos.get(0) : null;
	}

	/**
	 * 
	 * 不能根据insertTime desc查询，因为位置信息会因为网络延迟而导致批量上传，而insertTime只是系统事件，所以要根据gpsTime查询
	 * @param sn
	 */
	public PositionInfo findLastBySn(String sn) {
		String hql = "from PositionInfo where obdSn = :sno and statusGPS = 1 order by time desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionInfo) query.uniqueResult();
	}
	
	/**
	 * 
	 * 不管是否定位到
	 * @param sn
	 */
	public PositionInfo findLast(String sn) {
		String hql = "from PositionInfo where obdSn = :sno order by time desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionInfo) query.uniqueResult();
	}
	/**
	 * 取最后一条水温不为空的
	 * @param sn
	 * @return
	 */
	public PositionInfo findLastTemNotNull(String sn) {
		String hql = "from PositionInfo where obdSn = :sno and engineTemperature is not null order by time desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", sn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionInfo) query.uniqueResult();
	}
	
	/**
	 * 根据设备号或插入时间来获取最新位置信息 （代码待优化）
	 * @param obdSn
	 * @param time
	 * @param pager
	 * @return
	 */
	public List<PositionInfo> findInsertTime(String obdSn,String time, Pager pager) {
		String hql = "select * from (select * from position_info ";
		boolean hasParam = false;
		if(!StringUtils.isEmpty(obdSn)){
			hasParam = true;
		}
		if(!StringUtils.isEmpty(time)){
			hasParam = true;
		}
		
		if(hasParam){
			hql += " where ";
		}
		
		if(!StringUtils.isEmpty(obdSn)){
			hql += " obdSn = '" + obdSn + "'";
		}
		if(!StringUtils.isEmpty(time)){
			if(!StringUtils.isEmpty(obdSn)){
				hql += " and ";
			}
			hql += " insertTime like '" + time +"%'";
		}
		
		hql += " order by insertTime desc) p group by p.obdSn";
		return findBySql(hql, pager,true);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PositionInfo> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM PositionInfo WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSN = ? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and time>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and time<=? ";
		}
		if(map.containsKey("obdSpeed")){
			String obdSpeedFlag = (String) map.get("obdSpeedFlag");
			switch (obdSpeedFlag) {
			case "1":
				hql+=" and obdSpeed>=? ";
				break;
			case "0":
				hql+=" and obdSpeed<=? ";
				break;	
			default:
				break;
			}
		}
		if(map.containsKey("engineTurns")){
			String engineTurnsFlag = (String) map.get("engineTurnsFlag");
			switch (engineTurnsFlag) {
			case "1":
				hql+=" and engineTurns>=? ";
				break;
			case "0":
				hql+=" and engineTurns<=? ";
				break;	
			default:
				break;
			}
			
		}
		if(map.containsKey("engineTemperature")){
			String engineTempFlag = (String) map.get("engineTempFlag");
			switch (engineTempFlag) {
			case "1":
				hql+=" and engineTemperature>=? ";
				break;
			case "0":
				hql+=" and engineTemperature<=? ";
				break;	
			default:
				break;
			}
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
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("startTime"), "yyyy-MM-dd HH:mm:ss");
//							ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", (String)map.get("startTime"));
					map.remove("startTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
//							ThreadLocalDateUtil.parse("yyyy-MM-dd HH:mm:ss", (String)map.get("endTime"));
					map.remove("endTime");
					continue;
				}
				if(map.containsKey("obdSpeed")){
					objArray[i]=map.get("obdSpeed");
					map.remove("obdSpeed");
					continue;
				}
				if(map.containsKey("engineTurns")){
					objArray[i]=map.get("engineTurns");
					map.remove("engineTurns");
					continue;
				}
				if(map.containsKey("engineTemperature")){
					objArray[i]=map.get("engineTemperature");
					map.remove("engineTemperature");
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

	public PositionInfo findLastBySNAndGtTime(String obdSn, String time) {
		String hql = "from PositionInfo where obdSn = :sno and statusGPS = 1 AND time <= :time order by time desc";
		Query query = getSession().createQuery(hql);
		query.setString("sno", obdSn);
		query.setString("time", time);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (PositionInfo) query.uniqueResult();
	}
}
