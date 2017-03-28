package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ObdVersionDao extends BaseDao<ObdVersion> {

	public ObdVersion queryObdVersionBySN(String obdSn) {
		final String hql = "FROM ObdVersion WHERE obdSn = ?";
		List<ObdVersion> list = queryByHQL(hql, obdSn);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public boolean updateObdVersion(ObdVersion obdVersion) {
		ObdVersion version = queryObdVersionBySN(obdVersion.getObdSn());
		if(version != null){
			String verison = obdVersion.getVersion();
			if(verison!=null){
				version.setVersion(verison);
			}
			String updateFlag = obdVersion.getUpdateFlag();
			if(updateFlag!=null){
				version.setUpdateFlag(updateFlag);
			}
			Date updateTime = obdVersion.getUpdateTime();
			if(updateTime!=null){
				version.setUpdateTime(updateTime);
			}
			update(version);
		}else{
			obdVersion.setCreateTime(new Date());
			save(obdVersion);
		}
		return true;
	}
	
	public boolean obdVersionSave(ObdVersion obdVersion) {
		try {
			saveOrUpdate(obdVersion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<ObdVersion> getLastList(Date start,Date end,String version,String firmType,String firmVersion,Pager pager) {
//		String hql = "SELECT a.* FROM ObdVersion a WHERE a.id in(SELECT	b.id FROM (SELECT c.* FROM ObdVersion c where c.createTime between :start and :end ORDER BY c.createTime DESC)b  GROUP BY b.obdSn)";
		String startStr = DateUtil.getTimeString(start, "yyyy-MM-dd HH:mm:ss");
		String endStr = DateUtil.getTimeString(end, "yyyy-MM-dd HH:mm:ss");
		//String sql ="SELECT a.* FROM obd_version a WHERE a.id in(SELECT	b.id FROM (SELECT c.* FROM obd_version c where c.create_time between '"+startStr+"' and '"+endStr+"' ORDER BY c.create_time DESC) b GROUP BY b.obdSn)";
		String sql ="SELECT * FROM obd_version o WHERE o.create_time BETWEEN '"+startStr+"' AND '"+endStr+"'";
		if(!StringUtils.isEmpty(version)){
			sql+=" AND version ='"+version.trim()+"'";
		}
		if(!StringUtils.isEmpty(firmType)){
			sql+=" AND firmType ='"+firmType.trim()+"'";
		}
		if(!StringUtils.isEmpty(firmType)){
			sql+=" AND firmVersion ='"+firmVersion.trim()+"'";
		}
		sql+=" AND NOT EXISTS (SELECT 1 FROM obd_version WHERE obdSn = o.obdSn AND create_time > o.create_time)";
		return this.findBySql(sql, pager, true);
	}
}
