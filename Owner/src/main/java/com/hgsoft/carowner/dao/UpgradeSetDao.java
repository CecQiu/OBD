package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class UpgradeSetDao extends BaseDao<UpgradeSet> {

	public UpgradeSet queryByObdSn(String obdSn) {
		final String hql = "FROM UpgradeSet WHERE obdSn =:obdSn and vflag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSet) query.uniqueResult();
	}
	
	public UpgradeSet queryByObdSnLike(String obdSn) {
		final String hql = "FROM UpgradeSet WHERE obdSn like :obdSn and vflag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn",  "%"+obdSn);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSet) query.uniqueResult();
	}
	
	public UpgradeSet queryByObdSnLikeType(String obdSn, int type) {
		//未删除+未真正成功
		final String hql = "FROM UpgradeSet WHERE obdSn like :obdSn AND vflag='1' AND firmType=:firmType order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn",  "%"+obdSn);
		query.setInteger("firmType", type);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSet) query.uniqueResult();
	}
	
	public UpgradeSet queryByObdSn(String obdSn,String valid) {
		final String hql = "FROM UpgradeSet WHERE obdSn =:obdSn and valid=:valid and vflag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("valid", valid);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSet) query.uniqueResult();
	}
	
	public UpgradeSet queryByObdSnLike(String obdSn,String valid) {
		final String hql = "FROM UpgradeSet WHERE obdSn like :obdSn and valid=:valid and vflag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", "%"+obdSn);
		query.setString("valid", valid);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSet) query.uniqueResult();
	}
	
	//根据设备号和版本号
	public UpgradeSet queryByObdSnAndVersion(String obdSn,String version) {
		final String hql = "FROM UpgradeSet WHERE obdSn =:obdSn and version=:version and vflag='1' order by createTime desc";
		Query query = getSession().createQuery(hql);
		query.setString("obdSn", obdSn);
		query.setString("version", version);
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (UpgradeSet) query.uniqueResult();
	}
	
	/**
	 * 获取未推送过的记录
	 * @param version
	 * @param sendFlag
	 * @return
	 */
	public List<UpgradeSet> getListByVersionAndVflag(String version,String vflag) {
		final String hql = "FROM UpgradeSet WHERE version =? and vflag=? order by createTime desc";
		return queryByHQL(hql, version,vflag);
	}
	
	/**
	 * 获取审核通过，且为推送过的记录
	 * @param version
	 * @param sendFlag
	 * @return
	 */
	public List<UpgradeSet> queryByVersionAndSflagAndAstate(String version,String sendFlag,String auditState) {
		final String hql = "FROM UpgradeSet WHERE version =? and sendFlag =? and auditState =? and vflag='1' order by createTime desc";
		return queryByHQL(hql, version,sendFlag,auditState);
	}
	
	
	//根据版本号获取记录,只获取有效的，且未审核的
	public List<UpgradeSet> getListByVersion(String version) {
		final String hql = "FROM UpgradeSet WHERE version =? and auditState='0' and vflag='1'";
		return queryByHQL(hql, version);
	}
	
	//根据版本号获取记录,只获取有效的，且未审核的
		public Long getTotalByVersion(String version) {
			final String hql = "FROM UpgradeSet WHERE version =? and auditState='0' and vflag='1'";
			return executeCount(hql, version);
		}
		
		public int updByVersion(String auditState,String version,String auditOper) {
			final String hql = "update UpgradeSet set auditState =:auditState,auditOper=:auditOper,updateTime=:updateTime,auditTime=:auditTime  WHERE version =:version  and auditState='0' and vflag='1'";
			Query query = getSession().createQuery(hql);
			query.setString("auditState", auditState);
			query.setString("auditOper", auditOper);
			query.setTimestamp("updateTime", new Date());
			query.setTimestamp("auditTime", new Date());
			query.setString("version", version);
			return query.executeUpdate();
		}
		
	
	//判断升级记录是否存在重复
	@SuppressWarnings("unchecked")
	public List<UpgradeSet> getListByMap(Map<String, Integer> obdSns,Integer firmType) {
		String obdSnList = StrUtil.MapToString(obdSns);
		String hql = "FROM UpgradeSet WHERE obdSn in "+obdSnList+" and firmType="+firmType+" and vflag='1' order by createTime desc";
		return queryByHQL(hql);
	}
	
	@SuppressWarnings("unchecked")
	public List<UpgradeSet> queryByParams(Pager pager,Map<String,Object>map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM UpgradeSet WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn = ? ";
		}
		
		if(map.containsKey("version")){
			hql+=" and version=? ";
		}
		
		if(map.containsKey("sendFlag")){
			hql+=" and sendFlag=? ";
		}
		
		if(map.containsKey("valid")){
			hql+=" and valid=? ";
		}
		
		if(map.containsKey("vflag")){
			hql+=" and vflag=? ";
		}
		
		if(map.containsKey("firmType")){
			hql+=" and firmType=? ";
		}
		if(map.containsKey("firmVersion")){
			hql+=" and firmVersion=? ";
		}
		if(map.containsKey("auditState")){
			hql+=" and auditState=? ";
		}
		if(map.containsKey("upgradeFlag")){
			hql+=" and upgradeFlag=? ";
		}
		if(map.containsKey("sendedCount")){
			hql+=" and sendedCount>=? ";
		}
		if(map.containsKey("speedCount")){
			hql+=" and speedCount>=? ";
		}
		
		if(map.containsKey("starTime")){
			hql+=" and createTime>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime<=? ";
		}
		if(map.containsKey("success")){
			int success = (int) map.get("success");
			if(2== success){
				hql+=" and success is null ";
			}else{
				hql+=" and success=? ";
			}
		}
		if(map.containsKey("validTrue")){
			String validTrue=(String) map.get("validTrue");
			if("1".equals(validTrue)){
				hql+=" and validTrue is null ";
			}else{
				hql+=" and validTrue=? ";
			}
		}
		if(map.containsKey("obdSpeedFlag")){
			String obdSpeedFlag = (String) map.get("obdSpeedFlag");
			if("0".equals(obdSpeedFlag)){
				hql+="  and obdSpeed is null  ";
			}else if("1".equals(obdSpeedFlag)){
				hql+="  and obdSpeed is not null  ";
			}
		}
		if(map.containsKey("gpsSpeedFlag")){
			String gpsSpeedFlag = (String) map.get("gpsSpeedFlag");
			if("0".equals(gpsSpeedFlag)){
				hql+="  and gpsSpeed is null  ";
			}else if("1".equals(gpsSpeedFlag)){
				hql+="  and gpsSpeed is not null  ";
			}
		}
		hql+=" order by createTime desc,id desc,obdSn desc ";
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
					continue;
				}
				if(map.containsKey("version")){
					objArray[i]=map.get("version");
					map.remove("version");
					continue;
				}
				if(map.containsKey("sendFlag")){
					objArray[i]=map.get("sendFlag");
					map.remove("sendFlag");
					continue;
				}
				if(map.containsKey("valid")){
					objArray[i]=map.get("valid");
					map.remove("valid");
					continue;
				}
				if(map.containsKey("vflag")){
					objArray[i]=map.get("vflag");
					map.remove("vflag");
					continue;
				}
				if(map.containsKey("firmType")){
					objArray[i]=map.get("firmType");
					map.remove("firmType");
					continue;
				}
				if(map.containsKey("firmVersion")){
					objArray[i]=map.get("firmVersion");
					map.remove("firmVersion");
					continue;
				}
				if(map.containsKey("auditState")){
					objArray[i]=map.get("auditState");
					map.remove("auditState");
					continue;
				}
				if(map.containsKey("upgradeFlag")){
					objArray[i]=map.get("upgradeFlag");
					map.remove("upgradeFlag");
					continue;
				}
				if(map.containsKey("sendedCount")){
					objArray[i]=map.get("sendedCount");
					map.remove("sendedCount");
					continue;
				}
				if(map.containsKey("speedCount")){
					objArray[i]=map.get("speedCount");
					map.remove("speedCount");
					continue;
				}
				if(map.containsKey("starTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("starTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("starTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("endTime");
					continue;
				}
				if(map.containsKey("success")){
					objArray[i]=map.get("success");
					map.remove("success");
					continue;
				}
				if(map.containsKey("validTrue")){
					objArray[i]=map.get("validTrue");
					map.remove("validTrue");
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
