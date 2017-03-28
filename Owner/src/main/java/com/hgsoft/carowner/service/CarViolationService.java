package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.dao.CarViolationDao;
import com.hgsoft.carowner.entity.CarMaintain;
import com.hgsoft.carowner.entity.CarViolation;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Component
public class CarViolationService extends BaseService<CarViolation>{
	
	@Resource
	private CarViolationDao carViolationDao;
	@Resource
	public void setDao(CarViolationDao carViolationDao) {
		super.setDao(carViolationDao);
	}
	public CarViolation queryById(String id){
		return carViolationDao.queryById(id);
	}
	
	/**
	 * 获取违章信息
	 * @param mobileNumber
	 * @param license
	 * @param obdSN
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List getCarViolation(String mobileNumber,String license,String obdSN,Pager pager){
		String sql="SELECT m.`name`,m.sex,m.mobileNumber,m.license, m.obdSN,f.violationDesc,f.penaltyPoints,f.violationTime,f.id  from meb_user m,car_violation f WHERE m.obdSN=f.obdSn ";
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(obdSN!=null&&obdSN.trim().length()>0){
			sql+=" and m.obdSN like '%"+obdSN+"%'";
		}
		sql+=" ORDER BY f.violationTime DESC";
		List list=carViolationDao.queryBySQL(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
		List result=new ArrayList();
		for (int i = begin; i < list.size()&&i<end; i++) {
			Object[] ob=(Object[]) list.get(i);
			Map map=new HashMap();
			map.put("userName", ob[0]);
			map.put("sex", ob[1]);
			map.put("mobileNumber", ob[2]);
			map.put("license", ob[3]);
			map.put("obdSN", ob[4]);
			map.put("violationDesc", ob[5]);
			map.put("penaltyPoints", ob[6]);
			map.put("violationTime", ob[7]);
			map.put("id", ob[8]);
			result.add(map);
			
		}
		pager.setTotalSize(list.size());
		return result;
	}
}
