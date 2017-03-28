package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.CarMaintain;
import com.hgsoft.carowner.entity.CarViolation;
import com.hgsoft.common.dao.BaseDao;
@Component
public class CarViolationDao extends BaseDao<CarViolation>{
	
	public CarViolation queryById(String id){
		String hql="from CarViolation where id=?";
		List list= queryByHQL(hql, id);
		if(list.size()>0){
			CarViolation carViolation=(CarViolation) list.get(0);
			return carViolation;
		}
		return null;	
	}
}
