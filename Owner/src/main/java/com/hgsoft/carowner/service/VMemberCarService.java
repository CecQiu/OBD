/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.VMemberCarDao;
import com.hgsoft.carowner.entity.MemberCar;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;

/**
 * 用户和车辆信息service层
 * @author fdf
 */
@Service
public class VMemberCarService extends BaseService<MemberCar> {

	@Resource
	private VMemberCarDao vMemberCarDao;
	
	@Resource
	public void setDao(VMemberCarDao dao) {
		super.setDao(dao);
	}
	
	/**
	 * 通过电话号码查询用户对象
	 * @param mobileNumber 电话号码
	 * @return 用户对象
	 */
	public MemberCar findByMobile(String mobileNumber) {
		List<MemberCar> list = vMemberCarDao.findByPager(new Pager(), 
				Order.asc("mobileNumber"), Property.eq("mobileNumber", mobileNumber));
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 通过obdsn码查询用户对象
	 * @param obdsn obdsn码
	 * @return 用户对象
	 */
	public MemberCar findByObdsn(String obdsn) {
		List<MemberCar> list = vMemberCarDao.findByPager(new Pager(), 
				Order.asc("obdSn"), Property.eq("obdSn", obdsn));
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 通过车牌号码查询用户对象
	 * @param license 车牌号码
	 * @return 用户对象
	 */
	public MemberCar findByLicense(String license) {
		List<MemberCar> list = vMemberCarDao.findByPager(new Pager(), 
				Order.asc("license"), Property.eq("license", license));
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
}
