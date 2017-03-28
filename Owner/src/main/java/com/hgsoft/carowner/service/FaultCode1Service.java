/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.FaultCodeDao1;
import com.hgsoft.carowner.entity.FaultCode1;
import com.hgsoft.common.service.BaseService;

/**
 * @author Administrator
 *故障码管理服务类
 */
@Service
public class FaultCode1Service extends BaseService<FaultCode1> {
	@Resource
	private FaultCodeDao1 faultCodeDao1;
	
	public FaultCode1 getFaultCodeByCode(String code){
		return faultCodeDao1.getFaultCodeByCode(code);
	}
}
