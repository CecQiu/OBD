package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.CarDispatch;
import com.hgsoft.carowner.entity.CarMaintain;
import com.hgsoft.common.dao.BaseDao;
@Component
public class CarMaintainDao extends BaseDao<CarMaintain>{
	
	public CarMaintain queryById(String id){
		String hql="from CarMaintain where id=?";
		List list= queryByHQL(hql, id);
		if(list.size()>0){
			CarMaintain carMaintain=(CarMaintain) list.get(0);
			return carMaintain;
		}
		return null;	
	}
}
