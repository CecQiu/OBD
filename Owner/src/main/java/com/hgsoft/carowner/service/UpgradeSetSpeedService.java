package com.hgsoft.carowner.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.UpgradeSetSpeedDao;
import com.hgsoft.carowner.entity.UpgradeSetSpeed;
import com.hgsoft.common.service.BaseService;
/**
 * 未注册设备远程升级service
 * @author liujialin
 * 2015-12-25
 */
@Service
public class UpgradeSetSpeedService extends BaseService<UpgradeSetSpeed> {
	
	@Resource
	public void setDao(UpgradeSetSpeedDao upgradeSetSpeedDao){
		super.setDao(upgradeSetSpeedDao);
	}
	
	public UpgradeSetSpeedDao upgradeSetSpeedGetDao() {
		return (UpgradeSetSpeedDao)super.getDao();
	}
	
	public UpgradeSetSpeed queryByObdSn(String obdSn){
		return upgradeSetSpeedGetDao().queryByObdSn(obdSn);
	}

	public void upgradeSetSpeedUpdate(UpgradeSetSpeed upgradeSetSpeed) {
		upgradeSetSpeedGetDao().update(upgradeSetSpeed);
	}
}
