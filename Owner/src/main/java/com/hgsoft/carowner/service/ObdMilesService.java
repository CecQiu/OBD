package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdMilesDao;
import com.hgsoft.carowner.entity.ObdMiles;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * AGNSS
 * 
 * @author liujialin 2016年1月7日 下午6:23:08
 */
@Service
public class ObdMilesService extends BaseService<ObdMiles>{
	
	@Resource
	public void setDao(ObdMilesDao obdMilesDao){
		super.setDao(obdMilesDao);
	}
	
	@Override
	public ObdMilesDao getDao() {
		return (ObdMilesDao)super.getDao();
	}
	
	public ObdMiles getLatest(String obdSn)throws Exception{
		return getDao().getLatest(obdSn);
	}
	
	public List<ObdMiles> getListByParams()throws Exception{
		return getDao().getListByParams();
	}
	
	public boolean milesSave(ObdMiles obdMiles){
		return getDao().milesSave(obdMiles);
	}

	public boolean milesDel(ObdMiles obdMiles){
		return getDao().milesDel(obdMiles);
	}
	
	public List<ObdMiles> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
}
