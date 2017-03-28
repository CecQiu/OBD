/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdApiRecordDao;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

/**
 * @author liujialin
 * 
 */
@Service
public class ObdApiRecordService extends BaseService<ObdApiRecord> {
	@Resource
	public void setDao(ObdApiRecordDao obdApiRecordDao){
		super.setDao(obdApiRecordDao);
	}
	
	@Override
	public ObdApiRecordDao getDao() {
		return (ObdApiRecordDao)super.getDao();
	}
	
	public boolean irSave(ObdApiRecord obdApiRecord) {
		return getDao().irSave(obdApiRecord);
	}
	
	public List<ObdApiRecord> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
}
