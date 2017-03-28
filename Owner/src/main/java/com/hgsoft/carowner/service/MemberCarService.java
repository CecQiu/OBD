package com.hgsoft.carowner.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.MemberCarDao;
import com.hgsoft.carowner.entity.MemberCar;
import com.hgsoft.common.service.BaseService;
/**
 * 用户——车辆信息Service
 * @author sujunguang
 * 2015-8-5
 */
@Service
public class MemberCarService extends BaseService<MemberCar> {
	
	@Resource
	private MemberCarDao memberCarDao;
	
	@Resource
	public void setDao(MemberCarDao memberCarDao){
		super.setDao(memberCarDao);
	}
	
	/**
	 * 通过OBDSN获得用户车辆信息
	 * @param obdSn
	 * @return
	 */
	public MemberCar queryMemberCarBySN(String obdSn){
		return memberCarDao.queryMemberCarBySN(obdSn);
	}
}
