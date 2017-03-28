package com.hgsoft.carowner.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.CarTypeDao;
import com.hgsoft.carowner.entity.CarType;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
@Service
public class CarTypeService extends BaseService<CarType>{
	
	@Resource
	public void setDao(CarTypeDao dao) {
		super.setDao(dao);
	}

	public CarTypeDao getCarTypeDao() {
		return (CarTypeDao) this.getDao();
	}
	
	/**
	 * 查询车类类型
	 * @param make_name
	 * @param model_name
	 * @param type_name
	 * @param pager
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CarType> queryCarBy(String make_name,String model_name,String type_name,Pager pager){
		String hql=" from CarType c where 1=1 ";
		if(make_name!=null&&make_name.length()>0){
			hql+=" and c.make_name='"+make_name+"'";
		}
		if(model_name!=null&&model_name.length()>0){
			hql+=" and c.model_name='"+model_name+"'";
		}
		if(type_name!=null&&type_name.length()>0){
			hql+=" and c.type_name='"+type_name+"'";
		}
		
		List<CarType> carType=(List<CarType>) this.getDao().findByHql(hql, pager);
		return carType;
	}
	public List<CarType> getMakeName(){
		String sql="select distinct make_name from car_type";
		return (List<CarType>)this.getDao().queryBySQL(sql, null);
	}
	
	public List<CarType> getModelName(String makeName){
		String sql="select distinct model_name from car_type where make_name='"+makeName+"'";
		return (List<CarType>)this.getDao().queryBySQL(sql, null);
	}
	public List<CarType> getTypeName(String modelName){
		String sql="select distinct type_name from car_type where model_name='"+modelName+"'";
		return (List<CarType>)this.getDao().queryBySQL(sql, null);
	}
}
