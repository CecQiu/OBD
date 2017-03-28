package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.CarInfoDao;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.MemberCar;
import com.hgsoft.common.service.BaseService;
/**
 * 车辆信息Service
 * @author sujunguang
 * 2015-8-5
 */
@Service
public class CarInfoService extends BaseService<CarInfo> {
	
	@Resource
	private CarInfoDao carInfoDao;
	
	@Resource
	public void setDao(CarInfoDao carInfoDao){
		super.setDao(carInfoDao);
	}
	
	/**
	 * 通过OBDSN获得车辆信息
	 * @param obdSn
	 * @return
	 */
	public CarInfo queryCarInfoBySN(String obdSn){
		return carInfoDao.queryCarInfoBySN(obdSn);
	}
	/**
	 * 通过OBDSN获得车辆信息
	 * @param obdSn
	 * @return
	 */
	public CarInfo queryCarInfoByObdSnAndUserid(String obdSn,String regUserId){
		return carInfoDao.queryCarInfoByObdSnAndUserid(obdSn, regUserId);
	}
	
	/**
	 * 通过注册用户id获得车辆信息
	 * @param obdSn
	 * @return
	 */
	public CarInfo queryCarInfoByUserid(String regUserId){
		return carInfoDao.queryCarInfoByUserid(regUserId);
	}
	
	/**
	 * 通过OBDSN修改车辆状态
	 * @param obdSn
	 * @param carState
	 * @return
	 */
	public boolean updateCarInfoStateBySN(String obdSn,String carState){
		return carInfoDao.updateCarInfoStateBySN(obdSn, carState);
	}
}
