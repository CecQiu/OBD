package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.dao.CarTrafficDao;
import com.hgsoft.carowner.entity.CarTraffic;
import com.hgsoft.carowner.entity.CarViolation;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Component
public class CarTrafficService extends BaseService<CarTraffic>{
	
	@Resource
	private CarTrafficDao carTrafficDao;
	@Resource
	public void setDao(CarTrafficDao carTrafficDao) {
		super.setDao(carTrafficDao);
	}
	public CarTraffic queryById(String id){
		return carTrafficDao.queryById(id);
	}
	
	public List getCarTraffic(String mobileNumber,String license,String obdSN,String trafficStatus,Pager pager){
		String sql="SELECT m.`name`,m.sex,m.mobileNumber,m.license, m.obdSN,f.trafficStatus,f.trafficTime,f.id from meb_user m,car_traffic f WHERE m.obdSN=f.obdSn ";
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(obdSN!=null&&obdSN.trim().length()>0){
			sql+=" and m.obdSN like '%"+obdSN+"%'";
		}
		if(trafficStatus!=null&&trafficStatus.trim().length()>0){
			sql+=" and f.trafficStatus='"+trafficStatus+"'";
		}
		sql+=" ORDER BY f.trafficTime DESC";
		List list=carTrafficDao.queryBySQL(sql, null);
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
			map.put("trafficStatus", ob[5]);
			map.put("trafficTime", ob[6]);
			map.put("id", ob[7]);
			result.add(map);
			
		}
		pager.setTotalSize(list.size());
		return result;
	}
	
}
