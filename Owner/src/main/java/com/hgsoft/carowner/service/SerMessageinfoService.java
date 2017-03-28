package com.hgsoft.carowner.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.dao.SerMessageinfoDao;
import com.hgsoft.carowner.entity.SerMessageinfo;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
@Component
public class SerMessageinfoService extends BaseService<SerMessageinfo>{
	@Resource
	private SerMessageinfoDao serMessageinfoDao;
	@Resource
	public void setDao(SerMessageinfoDao dao) {
		super.setDao(dao);
	}

	public SerMessageinfoDao getSerMessageinfoDao() {
		return (SerMessageinfoDao) this.getDao();
	}
	/**
	 * 查询推送消息
	 * @param pager
	 * @return
	 */
	public List<SerMessageinfo> getMsg(Pager pager){
		return serMessageinfoDao.queryMsg(pager);
	}
}
