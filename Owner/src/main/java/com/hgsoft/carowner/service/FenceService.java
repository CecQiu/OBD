/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.FenceDao;
import com.hgsoft.carowner.entity.Fence;
import com.hgsoft.common.service.BaseService;

/**
 * @author liujialin
 */
@Service
public class FenceService extends BaseService<Fence>{

	@Resource
	public void setDao(FenceDao fenceDao){
		super.setDao(fenceDao);
	}
	
	@Override
	public FenceDao getDao() {
		return (FenceDao)super.getDao();
	}
	
	public List<Fence> queryListByObdSn(String obdSn,Integer areaNum,Integer valid) {
		return getDao().queryListByObdSn(obdSn, areaNum, valid);
	}

	public List<Fence> queryList(String obdSn, Integer areaNum,List<Integer> valid) {
		return getDao().queryList(obdSn, areaNum, valid);
	}
	
	public int setNoValid(String obdSn, Integer areaNum, Integer valid,Set<Integer> validSet) {
		return getDao().setNoValid(obdSn, areaNum, valid, validSet);
	}

	public boolean fsave(Fence fence) {
			return getDao().fsave(fence);
	}
	
	public boolean fdel(Fence fence) {
		return getDao().fdel(fence);
	}
	
	public Integer fenceListDel(String obdSn, Integer areaNum,List<String> id) {
		return getDao().fenceListDel(obdSn, areaNum, id);
	}
	
	public List<Fence> getListByMap(Map<String, Integer> obdSns, Integer areaNum) {
		return getDao().getListByMap(obdSns, areaNum);
	}
	
	public Fence queryLastByParams(String obdSn, Integer areaNum){
		return getDao().queryLastByParams(obdSn, areaNum);
	}
}
