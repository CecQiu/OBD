package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdCarDao;
import com.hgsoft.carowner.entity.ObdCar;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * ObdCar
 * 
 * @author liujialin 2016年1月7日 下午6:23:08
 */
@Service
public class ObdCarService extends BaseService<ObdCar>{
	
	@Resource
	public void setDao(ObdCarDao obdCarDao){
		super.setDao(obdCarDao);
	}
	
	@Override
	public ObdCarDao getDao() {
		return (ObdCarDao)super.getDao();
	}
	
	public ObdCar getLatest(String obdSn){
		return getDao().getLatest(obdSn);
	}
	
	public List<ObdCar> getLast(String obdSn){
		return getDao().getLast(obdSn);
	}
	
	public boolean obdCarSave(ObdCar obdCar){
		return getDao().obdCarSave(obdCar);
	}

	public boolean obdCarDel(ObdCar obdCar){
		return getDao().obdCarDel(obdCar);
	}
	
	public List<ObdCar> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
}
