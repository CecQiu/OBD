package com.hgsoft.carowner.dao;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.CarAddoil;
import com.hgsoft.common.dao.BaseDao;
@Component
public class CarAddoilDao extends BaseDao<CarAddoil>{
	
	public CarAddoil queryById(String id){
		String hql="from CarAddoil where id=?";
		CarAddoil carAddoil=(CarAddoil) queryByHQL(hql, id).get(0);
		return carAddoil;	
	}
}
