/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdMsgDao;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdMsg;
import com.hgsoft.common.service.BaseService;

/**
 * 用户和车辆信息service层
 * @author fdf
 */
@Service
public class ObdMsgService extends BaseService<ObdMsg> {
	
	
	@Resource
	public void setDao(ObdMsgDao obdMsgDao) {
		super.setDao(obdMsgDao);
	}

	public ObdMsgDao getObdMsgDao() {
		return (ObdMsgDao) this.getDao();
	}	
	
	/**
	 * 查询所有的OdbMsg信息
	 * @param license 车牌号码
	 * @return 用户对象
	 */
	public List<ObdMsg> queryObdMsgList(OBDStockInfo obdStockInfo) {
		List<ObdMsg> list = getObdMsgDao().queryObdMsgList(obdStockInfo);
		return list;
	}

	
}
