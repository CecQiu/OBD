package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.CarMaintain;
import com.hgsoft.carowner.entity.CarTraffic;
import com.hgsoft.common.dao.BaseDao;
@Component
public class CarTrafficDao extends BaseDao<CarTraffic>{
	
	public CarTraffic queryById(String id){
		String hql="from CarTraffic where id=?";
		List list= queryByHQL(hql, id);
		if(list.size()>0){
			CarTraffic carTraffic=(CarTraffic) list.get(0);
			return carTraffic;
		}
		return null;	
	}
}
