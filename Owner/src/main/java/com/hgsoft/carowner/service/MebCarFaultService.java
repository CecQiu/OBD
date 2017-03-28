package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.MebCarFaultDao;
import com.hgsoft.carowner.entity.MebCarFault;
import com.hgsoft.common.service.BaseService;

@Service
public class MebCarFaultService extends BaseService<MebCarFault>{
	
	@Resource
	MebCarFaultDao mebCarFaultDao;
	@Resource
	public void setDao(MebCarFaultDao dao) {
		super.setDao(dao);
	}

	public MebCarFaultDao getMebCarFaultDao() {
		return (MebCarFaultDao) this.getDao();
	}
	
	public void add(MebCarFault mcf) {
		getDao().save(mcf);
	} 
	
	public MebCarFault lastNewMebCarFault(String obdSN){
		return mebCarFaultDao.lastNewMebCarFault(obdSN);
	}
}
