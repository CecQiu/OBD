package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.MemberCar;
import com.hgsoft.common.dao.BaseDao;
/**
 * 用户——车辆DAO
 * @author sujunguang
 * 2015-8-5
 */
@Repository
public class MemberCarDao extends BaseDao<MemberCar> {

	public MemberCar queryMemberCarBySN(String obdSn) {
		final String hql = "FROM MemberCar WHERE obdSn = ?";
		List<MemberCar> list = queryByHQL(hql, obdSn);
		return list.size() > 0 ? list.get(0) : null;
	}
	
}
