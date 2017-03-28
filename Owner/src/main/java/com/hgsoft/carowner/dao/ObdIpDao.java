package com.hgsoft.carowner.dao;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.ObdIp;
import com.hgsoft.common.dao.BaseDao;
/**
 * 保存终端设备最新ip和端口号
 * @author liujialin
 *
 */
@Repository
public class ObdIpDao extends BaseDao<ObdIp> {
	
	//获取每一台终端设备最新的ip和端口
	public ObdIp getObdIpByObdId(String obdId) {
		ObdIp obdIp = null;
		if (obdId != null && !"".equals(obdId)) {
			obdIp = (ObdIp) uniqueResult("from ObdIp o where o.obdId='" + obdId + "' and o.createTime = (SELECT MAX(i.createTime) from ObdIp i WHERE i.obdId= '"+obdId+"')");
		}
		return obdIp;
	}

}
