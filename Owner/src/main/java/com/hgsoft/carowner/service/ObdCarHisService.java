package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdCarHisDao;
import com.hgsoft.carowner.entity.ObdCarHis;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * AGNSS
 * 
 * @author liujialin 2016年1月7日 下午6:23:08
 */
@Service
public class ObdCarHisService extends BaseService<ObdCarHis>{
	
	@Resource
	public void setDao(ObdCarHisDao obdCarHisDao){
		super.setDao(obdCarHisDao);
	}
	
	@Override
	public ObdCarHisDao getDao() {
		return (ObdCarHisDao)super.getDao();
	}
	
	
	public boolean obdCarHisSave(ObdCarHis obdCarHis){
		return getDao().obdCarHisSave(obdCarHis);
	}
	public List<ObdCarHis> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}

}
