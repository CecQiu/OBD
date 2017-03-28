/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.FaultCodeDao;
import com.hgsoft.carowner.entity.FaultCode;
import com.hgsoft.common.service.BaseService;

/**
 * @author Administrator
 *
 */
@Service
public class FaultCodeService extends BaseService<FaultCode> {

	@Resource
	public void setDao(FaultCodeDao FaultCodeDao){
		super.setDao(FaultCodeDao);
	}
	
	public FaultCodeDao getFaultCodeDao() {
		return (FaultCodeDao) this.getDao();
	}
	
	public FaultCode getFaultCodeByCode(String code){
		return this.getFaultCodeDao().getFaultCodeByCode(code);
	}
}
