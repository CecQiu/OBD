/**
 * 
 */
package com.hgsoft.carowner.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.FaultCode1;
import com.hgsoft.common.dao.BaseDao;

/**
 * @author liujialin
 * 故障码dao
 */
@Repository
public class FaultCodeDao1 extends BaseDao<FaultCode1> {
		
		//获取每一台终端设备最新的ip和端口
		public FaultCode1 getFaultCodeByCode(String code) {
			String hql = "from FaultCode1 o where o.code=:fcode";
			Query query = getSession().createQuery(hql);
			query.setString("fcode", code);
			query.setMaxResults(1);
			query.setFirstResult(0);
			return (FaultCode1) query.uniqueResult();
		}
}
