package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.CarMaintainDao;
import com.hgsoft.carowner.entity.CarMaintain;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Service
public class CarMaintainService extends BaseService<CarMaintain>{
	
	@Resource
	private CarMaintainDao carMaintainDao;
	@Resource
	public void setDao(CarMaintainDao carMaintainDao) {
		super.setDao(carMaintainDao);
	}
	
	public CarMaintain queryById(String id){
		return carMaintainDao.queryById(id);
	}
	
	/**
	 * 获取保养状态信息
	 * @param mobileNumber
	 * @param license
	 * @param obdSN
	 * @param maintainStatus
	 * @param orderStatus
	 * @param pager
	 * @return
	 */
	public List getCarMaintainInfo(String mobileNumber,String license,String obdSN,String maintainStatus,String orderStatus,Pager pager){
		String sql="SELECT m.`name`,m.sex,m.mobileNumber,m.license, m.obdSN,f.maintainStatus,f.maintainTime,f.id from meb_user m,car_maintain f WHERE m.obdSN=f.obdSn ";
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(obdSN!=null&&obdSN.trim().length()>0){
			sql+=" and m.obdSN like '%"+obdSN+"%'";
		}
		if(maintainStatus!=null&&maintainStatus.trim().length()>0){
			sql+=" and f.maintainStatus='"+maintainStatus+"'";
		}
		if(orderStatus!=null&&orderStatus.trim().length()>0){
			sql+=" and f.orderStatus='"+orderStatus+"'";
		}
		sql+=" ORDER BY f.maintainTime DESC";
		List list=carMaintainDao.queryBySQL(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
		List result=new ArrayList();
		for (int i = begin; i < list.size()&&i<end; i++) {
			Object[] ob=(Object[]) list.get(i);
			Map map=new HashMap();
			map.put("userName", ob[0]);
			map.put("sex", ob[1]);
			map.put("mobileNumber", ob[2]);
			map.put("license", ob[3]);
			map.put("obdSN", ob[4]);
			map.put("maintainStatus", ob[5]);
			map.put("maintainTime", ob[6]);
			map.put("id", ob[7]);
			result.add(map);
			
		}
		pager.setTotalSize(list.size());
		return result;
	}
}
