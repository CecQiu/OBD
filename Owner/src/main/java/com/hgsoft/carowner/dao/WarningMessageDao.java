package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.WarningMessage;
import com.hgsoft.common.dao.BaseDao;

@Repository
public class WarningMessageDao extends BaseDao<WarningMessage> {

	public WarningMessage queryLastByObdSn(String obdSn,String type) {
		final String hql = " from WarningMessage where obdSn = ? and messageType =?";
		List<WarningMessage> warningMessages =  queryByHQL(hql, obdSn,type);
		return warningMessages.size()>0 ? warningMessages.get(0) : null;
	}


}
