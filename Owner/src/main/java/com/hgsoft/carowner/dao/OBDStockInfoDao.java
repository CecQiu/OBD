package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.StrUtil;
/**
 * OBD设备DAO
 * @author sujunguang
 * 2015-8-5
 */
@Repository
public class OBDStockInfoDao extends BaseDao<OBDStockInfo> {
	
//	public OBDStockInfo queryBySNAndMSN(String obdSn){
//		final String hql = "FROM OBDStockInfo WHERE valid='1' and obdSn = ? or obdMSn =?";
//		List<OBDStockInfo> list = queryByHQL(hql, obdSn,obdSn);
//		return list.size() > 0 ? list.get(0) : null;
//	}
	
//	public OBDStockInfo queryByLikeSNAndMSN(String obdSn){
//		final String hql = "FROM OBDStockInfo WHERE valid='1' and obdSn like :obdSn or obdMSn like :obdSn";
//		
//		Query query = getSession().createQuery(hql);
//		query.setString("obdSn", "%"+obdSn+"%");
//		query.setMaxResults(1);
//		query.setFirstResult(0);
//		return (OBDStockInfo) query.uniqueResult();
//	}
	
	public OBDStockInfo queryLastByParam(Map<String,Object> map){
		if(map==null || map.size()==0){
			return null;
		}
		String hql = "FROM OBDStockInfo WHERE 1=1 ";
		if(map.containsKey("stockId")){
			hql +="AND stockId =:stockId ";
		}
		if(map.containsKey("obdId")){
			hql +="AND obdId =:obdId ";
		}
		if(map.containsKey("obdSn")){
			hql +="AND obdSn =:obdSn ";
		}
		if(map.containsKey("obdMSn")){
			hql +="AND obdMSn =:obdMSn ";
		}
		
		if(map.containsKey("fourGsimNo")){
			hql +="AND fourGsimNo =:fourGsimNo ";
		}
		
		if(map.containsKey("stockType")){
			hql +="AND stockType =:stockType ";
		}
		
		if(map.containsKey("stockState")){
			hql +="AND stockState =:stockState ";
		}
		
		if(map.containsKey("regUserId")){
			hql +="AND regUserId =:regUserId ";
		}
		
		if(map.containsKey("startDate")){
			hql +="AND startDate >=:startDate ";
		}
		
		if(map.containsKey("endDate")){
			hql +="AND startDate <=:endDate ";
		}
		
		if(map.containsKey("gpsState")){
			hql +="AND gpsState =:gpsState ";
		}
		
		if(map.containsKey("wifiState")){
			hql +="AND wifiState =:wifiState ";
		}
		
		if(map.containsKey("groupNum")){
			hql +="AND groupNum =:groupNum ";
		}
		
		if(map.containsKey("valid")){
			hql +="AND valid =:valid ";
		}
		hql+= "  order by startDate desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("stockId")){
			query.setString("stockId", (String)map.get("stockId"));
		}
		if(map.containsKey("obdId")){
			query.setString("obdId", (String)map.get("obdId"));
		}
		if(map.containsKey("obdSn")){
			query.setString("obdSn", (String)map.get("obdSn"));
		}
		if(map.containsKey("obdMSn")){
			query.setString("obdMSn", (String)map.get("obdMSn"));
		}
		
		if(map.containsKey("fourGsimNo")){
			query.setString("fourGsimNo", (String)map.get("fourGsimNo"));
		}
		
		if(map.containsKey("stockType")){
			query.setString("stockType", (String)map.get("stockType"));
		}
		
		if(map.containsKey("stockState")){
			query.setString("stockState", (String)map.get("stockState"));
		}
		
		if(map.containsKey("regUserId")){
			query.setString("regUserId", (String)map.get("regUserId"));
		}
		
		if(map.containsKey("startDate")){
			query.setString("startDate", (String)map.get("startDate"));
		}
		
		if(map.containsKey("endDate")){
			query.setString("endDate", (String)map.get("endDate"));
		}
		
		if(map.containsKey("gpsState")){
			query.setString("gpsState", (String)map.get("gpsState"));
		}
		
		if(map.containsKey("wifiState")){
			query.setString("wifiState", (String)map.get("wifiState"));
		}
		
		if(map.containsKey("groupNum")){
			query.setString("groupNum", (String)map.get("groupNum"));
		}
		
		if(map.containsKey("valid")){
			query.setString("valid", (String)map.get("valid"));
		}
		
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (OBDStockInfo) query.uniqueResult();
	}
	
	
	public OBDStockInfo queryByObdMSN(String obdMSn){
		final String hql = "FROM OBDStockInfo WHERE obdMSn =:obdMSn and valid='1' order by startDate desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdMSn", obdMSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (OBDStockInfo) query.uniqueResult();
	}
	
	public OBDStockInfo queryBySN(String obdSn){
		final String hql = "FROM OBDStockInfo WHERE obdSn =:obdSn and valid='1' order by startDate desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (OBDStockInfo) query.uniqueResult();
	}
	
	public OBDStockInfo queryByLikeObdMSN(String obdMSn){
		final String hql = "FROM OBDStockInfo WHERE obdMSn like :obdMSn and valid='1' order by startDate desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdMSn", "%"+obdMSn+"%");
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (OBDStockInfo) query.uniqueResult();
	}
	
	public OBDStockInfo queryByLikeSN(String obdSn){
		final String hql = "FROM OBDStockInfo WHERE obdSn like :obdSn and valid='1' order by startDate desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", "%"+obdSn+"%");
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (OBDStockInfo) query.uniqueResult();
	}

	public OBDStockInfo queryBySNAndUserId(String obdSn, String regUserId) {
		final String hql = "FROM OBDStockInfo WHERE obdSn = ? AND regUserId = ? and valid='1'";
		List<OBDStockInfo> list = queryByHQL(hql, obdSn,regUserId);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public OBDStockInfo queryByObdId(String obdId){
		String hql = "from OBDStockInfo where obdId = :oid and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("oid", obdId);
		return (OBDStockInfo) query.uniqueResult();
	}
	
	public List<OBDStockInfo> queryList(){
		final String hql = "FROM OBDStockInfo WHERE  groupNum ='00' and valid='1'";
		List<OBDStockInfo> list = queryByHQL(hql);
		return list;
	}
	
	public int obdGroupNumUpd(String obdSn,String groupNum){
		String hql = "update OBDStockInfo set groupNum=:groupNum WHERE obdSn =:obdSn and valid='1'";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("groupNum", groupNum);
		return query.executeUpdate();
	}

	public void obdAllOffLine() {
		final String hql = "update OBDStockInfo set stockState = '00'";
		updateByHql(hql);
	}
	
	//判断升级记录是否存在重复
	public Integer getTotalByMap(Map<String, Integer> obdSns) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String sql = "SELECT COUNT(DISTINCT(obdMSn)) FROM obd_stock_info WHERE obdMSn in "+obdSnList+" and valid='1'";
//		String hql = "FROM OBDStockInfo WHERE obdMSn in "+obdSnList+" and valid='1'";
		Object obj =this.findBySql(sql);
		return  Integer.parseInt(obj.toString());
	}
	
	//判断升级记录是否存在重复
	public List<OBDStockInfo> getListByMap(Map<String, Integer> obdSns) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String hql = "FROM OBDStockInfo WHERE obdMSn in "+obdSnList+" and valid='1'";
		return  queryByHQL(hql);
	}
	
	/**
	 * 根据开始时间和结束时间查询
	 * @param begin
	 * @param end
	 * @return
	 */
	public List find(Date begin, Date end) {
		final String sql = "SELECT c  FROM (SELECT COUNT(obdSn) AS c FROM obd_stock_info WHERE valid='1' AND startDate >= ? AND startDate < ? GROUP BY obdSn) AS stock_info";
		return queryBySQL(sql, begin, end);
	}
}
