/**
 * 
 */
package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;

/**
 * @author liujialin
 *
 */
@Repository
public class FaultUploadDao extends BaseDao<FaultUpload>{

	@SuppressWarnings("unchecked")
	public List<FaultUpload> queryByobdSn(String obdSn) {
		final String hql = "FROM FaultUpload WHERE obdSn = ?";
		return queryByHQL(hql, obdSn);
	}
	
	public FaultUpload queryById(String id){
		String hql="from FaultUpload where id=?";
		List list= queryByHQL(hql, id);
		if(list.size()>0){
			FaultUpload faultUpload=(FaultUpload) list.get(0);
			return faultUpload;
		}
		return null;	
	}
	public boolean faultUpdate(String obdSn){
		String now = (String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String hql="update FaultUpload f set f.state='0',f.updateTime='"+now+"' where f.obdSn='"+obdSn+"' and f.state='1'";
		updateByHql(hql);
		return true;	
	}
	
	@SuppressWarnings("unchecked")
	public List<FaultUpload> queryByObdsnAndState(String obdSn,String state) {
		final String hql = "FROM FaultUpload WHERE obdSn = ? and state = ?";
		return queryByHQL(hql, obdSn, state);
	}
}
