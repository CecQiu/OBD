/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.ObdUpgradeDao;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * @author fdf
 */
@Service
public class ObdUpgradeService extends BaseService<ObdUpgrade> {
	
	@Resource
	public void setDao(ObdUpgradeDao dao) {
		super.setDao(dao);
	}
	
	public List<ObdUpgrade> findByPagerCustom(Pager pager,String version,String firmType) {
		String hql = " select new ObdUpgrade(id,fileName,version,firmType,firmVersion,size,memo,createTime,updateTime,auditOper,auditMsg,auditTime,auditState,auditSend) from ObdUpgrade where valid='1' ";
		if(!StringUtils.isEmpty(version)){
			hql+=" and version = '"+version+"'";
		}
		if(!StringUtils.isEmpty(firmType)){
			Integer firmTypeI= Integer.parseInt(firmType);
			hql+=" and firmType = "+firmTypeI+"";
		}
		hql+=" order by createTime desc";
		return getDao().findByHql2(hql, pager);
	}

	@SuppressWarnings("rawtypes")
	public ObdUpgrade findByIdCustom(String upId) {
		String hql = " select new ObdUpgrade(id,fileName,version,firmType,firmVersion,size,memo,createTime,updateTime,auditOper,auditMsg,auditTime,auditState,auditSend) from ObdUpgrade where id = '" + upId + "' and valid='1'";
		List list = getDao().findByHql(hql, null);
		if(list != null && list.size() > 0) {
			return (ObdUpgrade) list.get(0);
		} else {
			return null;
		}
	}
	
	public Integer findByVersion(String version){
		String hql ="FROM ObdUpgrade WHERE VERSION='"+version+"' and valid='1'";
		return getDao().executeCountQuery(hql);
	}
	
	@SuppressWarnings("rawtypes")
	public ObdUpgrade findLastByVersion(String version) {
		String hql = " select new ObdUpgrade(id,fileName,version,firmType,firmVersion,size,memo,createTime,updateTime,auditOper,auditMsg,auditTime,auditState,auditSend) from ObdUpgrade where VERSION='"+version+"' and valid='1'";
		List list = getDao().findByHql(hql, null);
		if(list != null && list.size() > 0) {
			return (ObdUpgrade) list.get(0);
		} else {
			return null;
		}
	}
}
