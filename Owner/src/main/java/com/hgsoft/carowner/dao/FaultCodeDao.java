/**
 * 
 */
package com.hgsoft.carowner.dao;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.FaultCode;
import com.hgsoft.common.dao.BaseDao;

/**
 * @author liujialin
 * 故障码dao
 */
@Repository
public class FaultCodeDao extends BaseDao<FaultCode> {
		
		//获取每一台终端设备最新的ip和端口
		public FaultCode getFaultCodeByCode(String code) {
			FaultCode faultCode = new FaultCode();
			if (code != null && !"".equals(code)) {
				faultCode = (FaultCode) uniqueResult("from FaultCode o where o.code='" + code + "'");
			}
			return faultCode;
		}
}
