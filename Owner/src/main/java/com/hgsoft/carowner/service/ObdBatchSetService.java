package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdBatchSetDao;
import com.hgsoft.carowner.entity.ObdBatchSet;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
/**
 * obd设置表,包括离线设置
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdBatchSetService extends BaseService<ObdBatchSet> {
	
	@Resource
	public void setDao(ObdBatchSetDao obdBatchSetDao){
		super.setDao(obdBatchSetDao);
	}
	
	@Override
	public ObdBatchSetDao getDao() {
		return (ObdBatchSetDao)super.getDao();
	}
	
	public ObdBatchSet queryObdBatchSetValidNotSuccess(String obdSn){
		return getDao().queryObdBatchSetValidNotSuccess(obdSn);
	}
	
	public ObdBatchSet queryLastByParams(Map<String, Object> map) {
		return getDao().queryLastByParams(map);
	}
	
	public List<ObdBatchSet> getListByTypes(Set<String> types) {
		return getDao().getListByTypes(types);
	}
	
	public List<ObdBatchSet> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
	
	public boolean del(String id) {
		 try {
			this.deleteById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	public boolean add(ObdBatchSet obdBatchSet) {
		return getDao().add(obdBatchSet);
	}
	public List<ObdBatchSet> getListByMap(Map<String, Object> map) {
		return getDao().getListByMap(map);
	}
	
	public Integer delByParams(Map<String,Object> map)throws Exception{
		return getDao().delByParams(map);
	}
	
	public Long getTotalByParams(Map<String, Object> map) {
		return getDao().getTotalByParams(map);
	}
	
	public Integer updByParams(Map<String, Object> map) {
		return getDao().updByParams(map);
	}
	
	public void obdBatchSetUpdate(ObdBatchSet obdBatchSet){
		update(obdBatchSet);
	}
}
