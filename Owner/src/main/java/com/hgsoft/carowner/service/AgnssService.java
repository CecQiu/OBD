package com.hgsoft.carowner.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.AgnssDao;
import com.hgsoft.carowner.entity.Agnss;
import com.hgsoft.common.service.BaseService;

/**
 * AGNSS
 * 
 * @author liujialin 2016年1月7日 下午6:23:08
 */
@Service
public class AgnssService extends BaseService<Agnss>{
	
	@Resource
	public void setDao(AgnssDao agnssDao){
		super.setDao(agnssDao);
	}
	
	@Override
	public AgnssDao getDao() {
		return (AgnssDao)super.getDao();
	}
	
	public Agnss getLatest(){
		return getDao().getLatest();
	}
	
	public List<Agnss> getLast(){
		return getDao().getLast();
	}
	
	public boolean agnssSave(Agnss agnss){
		return getDao().agnssSave(agnss);
	}

	public boolean agnssDel(Agnss agnss){
		return getDao().agnssDel(agnss);
	}
}
