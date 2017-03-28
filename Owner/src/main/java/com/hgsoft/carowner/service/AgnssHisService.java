package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.AgnssHisDao;
import com.hgsoft.carowner.entity.AgnssHis;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * AGNSS
 * 
 * @author liujialin 2016年1月7日 下午6:23:08
 */
@Service
public class AgnssHisService extends BaseService<AgnssHis>{
	
	@Resource
	public void setDao(AgnssHisDao agnssHisDao){
		super.setDao(agnssHisDao);
	}
	
	@Override
	public AgnssHisDao getDao() {
		return (AgnssHisDao)super.getDao();
	}
	
	
	public boolean agnssHisSave(AgnssHis agnssHis){
		return getDao().agnssHisSave(agnssHis);
	}
	
	public List<AgnssHis> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}

}
