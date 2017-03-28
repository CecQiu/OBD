package com.hgsoft.carowner.service;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.WarningMessageDao;
import com.hgsoft.carowner.entity.WarningMessage;
import com.hgsoft.common.service.BaseService;

@Service
public class WarningMessageService extends BaseService<WarningMessage> {
	
	@Resource
	private  WarningMessageDao warningMessageDao;
	
	@Resource
	public void setDao(WarningMessageDao warningMessageDao){
		super.setDao(warningMessageDao);
	}
	
	public boolean warningMsgSave(WarningMessage wm){
		try {
			this.getDao().save(wm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public  WarningMessage queryLastByObdSn(String obdSn,String type){
		return warningMessageDao.queryLastByObdSn(obdSn,type);
	}
	
}
