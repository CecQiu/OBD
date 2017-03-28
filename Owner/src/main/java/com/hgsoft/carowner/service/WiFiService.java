/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.WifiDao;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.common.service.BaseService;

/**
 * @author liujialin
 * 801d 设置OBD的WIFI状态
 * wifi的密码不能小于8位，且不能和之前的密码一样
 */
@Service
public class WiFiService extends BaseService<Wifi>{
	
	@Resource
	private WifiDao wifiDao;
	
	@Resource
	public void setDao(WifiDao wifiDao){
		super.setDao(wifiDao);
	}
	
	//查询当前车辆参数记录是否存在
	public Wifi isExist(String obdSn) {
		return wifiDao.isExist(obdSn);
	}
	
	//查询当前车辆参数记录是否存在
	public void wifiSave(Wifi wifi) {
		 wifiDao.saveOrUpdate(wifi);
	}
}
