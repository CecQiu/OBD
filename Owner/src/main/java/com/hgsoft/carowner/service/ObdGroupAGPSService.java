package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdGroupAGPSDao;
import com.hgsoft.carowner.entity.ObdGroupAGPS;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
/**
 * @author sjg
 * @version  [ 2016年12月13日]
 */
@Service
public class ObdGroupAGPSService extends BaseService<ObdGroupAGPS> {
	
	@Resource
	public void setDao(ObdGroupAGPSDao ObdGroupAGPSDao){
		super.setDao(ObdGroupAGPSDao);
	}
	
	public ObdGroupAGPSDao ObdGroupAGPSGetDao() {
		return (ObdGroupAGPSDao)super.getDao();
	}
	
	public ObdGroupAGPS queryByGroupNum(String groupNum){
		return ObdGroupAGPSGetDao().queryByGroupNum(groupNum);
	}

	public void ObdGroupAGPSUpdate(ObdGroupAGPS ObdGroupAGPS) {
		ObdGroupAGPSGetDao().update(ObdGroupAGPS);
	}
	public List<ObdGroupAGPS> queryByParams(Pager pager,Map<String, Object> map) {
		return ObdGroupAGPSGetDao().queryByParams(pager, map);
	}
}
