package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.CarDispatch;
import com.hgsoft.common.dao.BaseDao;

@Component
public class CarDispatchDao extends BaseDao<CarDispatch>{
	
	public CarDispatch queryById(String id){
		String hql="from CarDispatch where id=?";
		List list= queryByHQL(hql, id);
		if(list.size()>0){
			CarDispatch carDispatch=(CarDispatch) list.get(0);
			return carDispatch;
		}
		return null;	
	}
}
