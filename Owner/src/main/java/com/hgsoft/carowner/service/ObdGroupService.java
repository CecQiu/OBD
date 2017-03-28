package com.hgsoft.carowner.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdGroupDao;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.common.service.BaseService;
/**
 * obdGroup设置Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdGroupService extends BaseService<ObdGroup> {
	
	@Resource
	public void setDao(ObdGroupDao obdGroupDao){
		super.setDao(obdGroupDao);
	}
	
	@Override
	public ObdGroupDao getDao() {
		return (ObdGroupDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得WifiSet集合
	 * @param obdSn
	 * @return
	 */
	public List<ObdGroup> queryList(){
		return getDao().queryList();
	}
	
	/**
	 * 
	 * @param obdSn
	 * @return
	 */
	public ObdGroup queryByGroupNum(String groupNum){
		return getDao().queryByGroupNum(groupNum);
	}
	
	public boolean obdGroupSave(ObdGroup obdGroup){
		try {
			getDao().saveOrUpdate(obdGroup);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
