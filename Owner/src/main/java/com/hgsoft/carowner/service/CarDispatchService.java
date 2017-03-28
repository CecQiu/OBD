package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.hgsoft.carowner.dao.CarDispatchDao;
import com.hgsoft.carowner.entity.CarDispatch;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Component
public class CarDispatchService extends BaseService<CarDispatch>{
	@Resource
	public void setDao(CarDispatchDao carDispatchDao) {
		// TODO Auto-generated method stub
		super.setDao(carDispatchDao);
	}
	@Resource
	private CarDispatchDao carDispatchDao;
	
	public CarDispatch queryById(String id){
		return carDispatchDao.queryById(id);
	}
	/**
	 * 查询出车信息
	 * @param license
	 * @param userName
	 * @param status
	 * @param drivingOutBeginTime
	 * @param drivingOutEndTime
	 * @param offRunningBeginTime
	 * @param offRunningEndTime
	 * @param pager
	 * @return
	 */
	public List getCarDispatchInfo(String license,String userName,String status,String drivingOutBeginTime,
			String drivingOutEndTime,String offRunningBeginTime,String offRunningEndTime,Pager pager){
		String sql="SELECT m.license,c.userName,c.mobileNumber,c.drivingOutTime,c.drivingOutAddress,c.offRunningTime,c.offRunningAddress,"
				+ "c.status,c.id from meb_user m,car_dispatch c where c.obdSn=m.obdSN ";
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(userName!=null&&userName.trim().length()>0){
			sql+=" and c.userName like '%"+userName+"%'";
		}
		if(status!=null&&status.trim().length()>0){
			sql+=" and c.status='"+status+"'";
		}
		if(drivingOutBeginTime!=null&&drivingOutBeginTime.trim().length()>0){
			sql+=" and c.drivingOutTime >='"+drivingOutBeginTime+"'";
		}
		if(drivingOutEndTime!=null&&drivingOutEndTime.trim().length()>0){
			sql+=" and c.drivingOutTime <='"+drivingOutEndTime+"'";
		}
		if(offRunningBeginTime!=null&&offRunningBeginTime.trim().length()>0){
			sql+=" and c.offRunningTime >='"+offRunningBeginTime+"'";
		}
		if(offRunningEndTime!=null&&offRunningEndTime.trim().length()>0){
			sql+=" and c.offRunningTime <='"+offRunningEndTime+"'";
		}
		sql+=" ORDER BY c.drivingOutTime";
		List list=carDispatchDao.queryBySQL(sql, null);
		List carDispatchList=new ArrayList();
		int beginPager=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int endPager=pager.getPageSize()*pager.getCurrentPage();
        for (int i = beginPager; i < list.size()&&i<endPager; i++) {
        	Object[] ob=(Object[]) list.get(i);
			Map map=new HashMap();
			map.put("license", ob[0]);
			map.put("userName", ob[1]);
			map.put("mobileNumber", ob[2]);
			map.put("drivingOutTime", ob[3]);
			map.put("drivingOutAddress", ob[4]);
			map.put("offRunningTime", ob[5]);
			map.put("offRunningAddress", ob[6]);
			map.put("status", ob[7]);
			map.put("id", ob[8]);
			carDispatchList.add(map);
        }
        pager.setTotalSize(list.size());
        return carDispatchList;
	}
}
