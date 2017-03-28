package com.hgsoft.carowner.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.common.dao.BaseDao;
/**
 * 车辆信息DAO
 * @author sujunguang
 * 2015-8-5
 */
@Repository
public class CarInfoDao extends BaseDao<CarInfo> {
	
	public boolean updateCarInfoStateBySN(String obdSn,String carState) {
		CarInfo carInfo = queryCarInfoBySN(obdSn);
		if(carInfo != null){
			carInfo.setCarState(carState);
			update(carInfo);
			return true;
		}
		return false;
		
		
	}

	public CarInfo queryCarInfoBySN(String obdSn) {
		final String hql = "FROM CarInfo WHERE obdSn = ? and valid = 1";
		List<CarInfo> list = queryByHQL(hql, obdSn);
		return list.size() > 0 ? list.get(0) : null;
	}
	public CarInfo queryCarInfoByObdSnAndUserid(String obdSn,String regUserId) {
		final String hql = "FROM CarInfo WHERE obdSn = ? and regUserId = ? and valid = 1";
		List<CarInfo> list = queryByHQL(hql, obdSn,regUserId);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public CarInfo queryCarInfoByUserid(String regUserId) {
		final String hql = "FROM CarInfo WHERE regUserId = ?";
		List<CarInfo> list = queryByHQL(hql, regUserId);
		return list.size() > 0 ? list.get(0) : null;
	}
}
