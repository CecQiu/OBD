package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.SerMessageinfo;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.Pager;

@Component
public class SerMessageinfoDao extends BaseDao<SerMessageinfo>{
	
	/**
	 * 查询推送消息
	 * @param pager
	 * @return
	 */
	public List<SerMessageinfo> queryMsg(Pager pager){
		String hql=" from SerMessageinfo where 1=1 ";
		pager.setTotalSize(findByHql(hql, pager).size());
		return (List<SerMessageinfo>) findByHql(hql, pager);
	}
}
