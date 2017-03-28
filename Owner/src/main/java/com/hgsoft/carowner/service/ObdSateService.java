/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdStateDao;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.common.service.BaseService;

/**
 * @author liujialin
 * 801d	设置OBD的GSP状态
 */
@Service
public class ObdSateService extends BaseService<ObdState>{
	private final Log logger = LogFactory.getLog(ObdSateService.class);
	@Resource
	public void setDao(ObdStateDao obdStateDao){
		super.setDao(obdStateDao);
	}
	
	@Override
	public ObdStateDao getDao() {
		return (ObdStateDao)super.getDao();
	}
	/**
	 * 获取obd状态数据
	 * @param obdSn
	 * @return
	 */
	public ObdState queryByObdSn(String obdSn) {
		return getDao().queryByObdSn(obdSn);
	}
	
	public boolean add(ObdState obdState) {
		try {
			getDao().saveOrUpdate(obdState);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
