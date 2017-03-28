/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.FenceHisDao;
import com.hgsoft.carowner.entity.FenceHis;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * @author liujialin
 */
@Service
public class FenceHisService extends BaseService<FenceHis>{

	@Resource
	public void setDao(FenceHisDao fenceHisDao){
		super.setDao(fenceHisDao);
	}
	
	@Override
	public FenceHisDao getDao() {
		return (FenceHisDao)super.getDao();
	}
	
	public boolean fsave(FenceHis fenceHis) {
		return getDao().fsave(fenceHis);
	}
	
	public List<FenceHis> queryByParams(Pager pager,Map<String,Object>map) {
		return getDao().queryByParams(pager, map);
	}
}
