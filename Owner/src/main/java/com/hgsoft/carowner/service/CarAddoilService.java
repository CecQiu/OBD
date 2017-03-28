package com.hgsoft.carowner.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.dao.CarAddoilDao;
import com.hgsoft.carowner.entity.CarAddoil;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Component
public class CarAddoilService extends BaseService<CarAddoil>{
	
	@Resource
	public void setDao(CarAddoilDao carAddoilDao) {
		// TODO Auto-generated method stub
		super.setDao(carAddoilDao);
	}

	public CarAddoilDao getCarAddoilDao() {
		// TODO Auto-generated method stub
		return (CarAddoilDao) this.getDao();
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public CarAddoil queryById(String id){
		return this.getCarAddoilDao().queryById(id);
	}
	
	/**
	 * 加油信息
	 * @param license
	 * @param gasStation
	 * @param yName
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List getAddoil(String license,String gasStation,String yName,String starTime,String endTime,Pager pager){
		String sql="select m.license,c.addTime,c.gasStation,c.gasStationAdd,c.oilType,c.fee,c.oilNum,c.total,c.yName,c.id from car_addoil c,meb_user m where c.obdSn=m.obdSN ";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(gasStation!=null&&gasStation.trim().length()>0){
			sql+=" and c.gasStation like '%"+gasStation+"%'";
		}
		if(yName!=null&&yName.trim().length()>0){
			sql+=" and c.yName like '%"+yName+"%'";
		}
		if(starTime!=null&&starTime.trim().length()>0){
			if(endTime!=null&&endTime.trim().length()>0){
				sql+=" and c.addTime >='"+starTime+"' and c.addTime <='"+endTime+"'";
			
			}else{
				sql+=" and c.addTime >='"+starTime+"' and c.addTime <='"+df.format(new Date())+"'";
				
			}
		}
		if(endTime!=null&&endTime.trim().length()>0){
			if(starTime!=null&&starTime.trim().length()>0){
				
			}else{
				sql+=" and c.addTime <='"+endTime+"'";
				
			}
		}
		sql+=" ORDER BY c.addTime DESC ";
		List list=this.getDao().queryBySQL(sql, null);
		List addoilList=new ArrayList();
		int beginPager=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int endPager=pager.getPageSize()*pager.getCurrentPage();
        for (int i = beginPager; i < list.size()&&i<endPager; i++) {
			Object[] ob=(Object[]) list.get(i);
			Map map=new HashMap();
			map.put("id", ob[9]);
			map.put("license", ob[0]);
			map.put("addTime", ob[1]);
			map.put("gasStation", ob[2]);
			map.put("gasStationAdd", ob[3]);
			map.put("oilType", ob[4]);
			map.put("fee", ob[5]);
			map.put("oilNum", ob[6]);
			map.put("total", ob[7]);
			map.put("yName", ob[8]);
			addoilList.add(map);		
		}
        pager.setTotalSize(list.size());
        return addoilList;
	}
}
