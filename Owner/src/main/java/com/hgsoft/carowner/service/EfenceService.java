/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.EfenceDao;
import com.hgsoft.carowner.entity.Efence;
import com.hgsoft.common.service.BaseService;

/**
 * @author liujialin
 * 801d 设置OBD的WIFI状态
 * wifi的密码不能小于8位，且不能和之前的密码一样
 */
@Service
public class EfenceService extends BaseService<Efence>{
	private final Log logger = LogFactory.getLog(EfenceService.class);

	@Resource
	public void setDao(EfenceDao efenceDao){
		super.setDao(efenceDao);
	}
	
	@Override
	public EfenceDao getDao() {
		return (EfenceDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得Efence集合
	 * @param obdSn
	 * @return
	 */
	public List<Efence> queryListByObdSn(String obdSn){
		return getDao().queryListByObdSn(obdSn);
	}
	/**
	 * 查询所以有效的定时定点电子围栏
	 * @param obdSn
	 * @return
	 */
	public List<Efence> queryListByObdSnAndType(String obdSn,String type){
		return getDao().queryListByObdSnAndType(obdSn,type);
	}
	
	
	/**
	 * 通过OBDSN和区域编号获得Efence
	 * @param obdSn
	 * @return
	 */
	public Efence queryByObdSn(String obdSn,String areaNum){
		return getDao().queryByObdSn(obdSn,areaNum);
	}
	
	/**
	 * 通过OBDSN和区域编号获得Efence
	 * @param obdSn
	 * @return
	 */
	public Efence queryLastByObdSn(String obdSn){
		return getDao().queryLastByObdSn(obdSn);
	}
	
	/**
	 * 将所有的电子围栏设置为无效
	 * @param obdSn
	 * @return
	 */
	public int efenceNoValid(String obdSn){
		return getDao().efenceNoValid(obdSn);
	}
	
	/**
	 * 将所有的定时电子围栏设置为无效
	 * @param obdSn
	 * @return
	 */
	public int efenceTimeNoValid(String obdSn){
		return getDao().efenceTimeNoValid(obdSn);
	}
	
	/**
	 * 将所有的电子围栏设置为无效
	 * @param obdSn
	 * @return
	 */
	public int efenceNoValidByAreaNum(String obdSn,String areaNum){
		return getDao().efenceNoValidByAreaNum(obdSn,areaNum);
	}
	
	public boolean efenceSave(Efence efence){
		try {
			getDao().saveOrUpdate(efence);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

}
