package com.hgsoft.carowner.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdCurVersion;
import com.hgsoft.common.dao.BaseDao;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class ObdCurVersionDao extends BaseDao<ObdCurVersion> {

	public ObdCurVersion queryByObdSn(String obdSn) {
		final String hql = "FROM ObdCurVersion WHERE obdSn = ?";
		List<ObdCurVersion> list = queryByHQL(hql, obdSn);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	
	public boolean obdCurVersionSave(ObdCurVersion obdCurVersion) {
		saveOrUpdate(obdCurVersion);
		return true;
	}
}
