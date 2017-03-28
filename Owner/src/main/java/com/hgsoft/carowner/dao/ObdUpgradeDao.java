package com.hgsoft.carowner.dao;

import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.common.dao.BaseDao;
//@Component
@Repository
public class ObdUpgradeDao extends BaseDao<ObdUpgrade>{
	
	//获取最新的版本
	public ObdUpgrade getLatestVersion(){
		String hql = "from ObdUpgrade ou where ou.createTime =(select max(o.createTime) from ObdUpgrade o)";
		ObdUpgrade ou=(ObdUpgrade) uniqueResult(hql);
		return ou;
	}
	
	//获取最新的版本号
	public String getLatestVersionNum(){
		String hql = "select ou.version from ObdUpgrade ou where ou.createTime =(select max(o.createTime) from ObdUpgrade o)";
		String version= (String) uniqueResult(hql);
		return version;
	}
}
