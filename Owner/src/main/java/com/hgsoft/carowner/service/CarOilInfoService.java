package com.hgsoft.carowner.service;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.CarOilInfoDao;
import com.hgsoft.carowner.entity.CarOilInfo;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Service
public class CarOilInfoService extends BaseService<CarOilInfo> {
	
	@Resource
	private CarOilInfoDao carOilInfoDao;
	
	@Resource
	public void setDao(CarOilInfoDao carOilInfoDao){
		super.setDao(carOilInfoDao);
	}

	@Override
	public CarOilInfoDao getDao() {
		return (CarOilInfoDao)super.getDao();
	}
	
	public void carOilInfoUpdate(CarOilInfo carOilInfo){
		carOilInfoDao.update(carOilInfo);
	}
	
	/**
	 * 查询时间段之间的油耗记录
	 * @param obdSN
	 * @param beginTime
	 * @param endTime
	 */
	public List<CarOilInfo> queryBetwwenTime(String obdSN, String beginTime, String endTime) {
		return carOilInfoDao.queryBetwwenTime(obdSN, beginTime, endTime);
	}
	/**
	 * 油耗统计
	 * @param obdSn
	 * @param mobileNumber
	 * @param license
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List getoilConsumption(String obdSn,String mobileNumber,String license,String starTime,String endTime,Pager pager,int flag){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String sql="select o.obdSn,m.license,m.name,m.mobileNumber,o.petrolConsumeNum,o.mileageNum,o.oilInfoTime from car_oil_info o,meb_user m where m.carId=o.carId and o.type!=2 ";
		if(obdSn!=null&&obdSn.trim().length()>0){
			sql+=" and o.obdSn like '%"+obdSn+"%'";
		}
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(starTime!=null&&starTime.trim().length()>0){
			if(endTime!=null&&endTime.trim().length()>0){
				sql+=" and o.oilInfoTime >='"+starTime+" 00:00:00"+"' and o.oilInfoTime <='"+endTime+" 23:59:59"+"'";
			
			}else{
				sql+=" and o.oilInfoTime >='"+starTime+" 00:00:00"+"' and o.oilInfoTime <='"+df.format(new Date())+"'";
				
			}
		}
		if(endTime!=null&&endTime.trim().length()>0){
			if(starTime!=null&&starTime.trim().length()>0){
				
			}else{
				sql+=" and o.oilInfoTime <='"+endTime+" 23:59:59"+"'";
				
			}
		}
		sql+=" ORDER BY o.oilInfoTime DESC";
		List oilList=new ArrayList();
		List list=this.getDao().queryBySQL(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
        if(flag==0){
        	DecimalFormat fm = new DecimalFormat("0.00");
        	for (int i = 0; i < list.size(); i++) {
    			Object[] ob=(Object[]) list.get(i);
    			
    			Map map=new HashMap();
    			map.put("obdSn", ob[0]);
    			map.put("license", ob[1]);
    			map.put("name", ob[2]);
    			map.put("mobileNumber", ob[3]);
    			
    			map.put("mileageNum", ob[5]);
    			map.put("oilInfoTime", ob[6]);
    			Double first=Double.parseDouble(String.valueOf(ob[4]));
    			map.put("petrolConsumeNum", fm.format(first*1.00/1000));//油耗
    			Double second=Double.parseDouble(String.valueOf(ob[5]))/100;
    			if(second==0.0){
    				second=1.0;
    				map.put("mileageNum", second);
    			}   		
    		      if(first==0.0)
    		      {
    		       map.put("avgOil", 0);
    		      }else
    		      {
    		    	  map.put("avgOil", fm.format(((first*1.00/1000)/second)*100));//公里
    		      }
    			oilList.add(map);
    		
    		}
        }else{
            DecimalFormat fm = new DecimalFormat("0.00");
        	for (int i = begin; i < list.size()&&i<end; i++) {
    			Object[] ob=(Object[]) list.get(i);
    			
    			Map map=new HashMap();
    			map.put("obdSn", ob[0]);
    			map.put("license", ob[1]);
    			map.put("name", ob[2]);
    			map.put("mobileNumber", ob[3]);
    			
    			map.put("mileageNum", ob[5]);
    			map.put("oilInfoTime", ob[6]);
    			Double first=Double.parseDouble(String.valueOf(ob[4]));
    			map.put("petrolConsumeNum", fm.format(first*1.00/1000));
    			Double second=Double.parseDouble(String.valueOf(ob[5]))/100;//公里
    			if(second==0.0){
    				second=1.0;
    				map.put("mileageNum", second);
    			}   		
    		      if(first==0.0)
    		      {
    		       map.put("avgOil", 0);
    		      }else
    		      {
    		    	  map.put("avgOil", fm.format(((first*1.00/1000)/second)*100));
    		      }
    			oilList.add(map);
    		
    		}
        }
		
		pager.setTotalSize(list.size());
		return oilList;
	}
	
	public void carOilSave(CarOilInfo co){
		this.getDao().save(co);
	}

	/**
	 * 油耗查询
	 * @param obdSN
	 * @param beginTime
	 * @param endTime
	 */
	public Object[] carOilCalc(String obdSn, String starTime, String endTime) {
		return carOilInfoDao.carOilCalc(obdSn, starTime, endTime);
	}
	/**
	 * 油耗查询
	 * @param obdSN
	 * @param beginTime
	 * @param endTime
	 */
	public List carOilListCalc(String obdSn, String starTime, String endTime) {
		return carOilInfoDao.carOilListCalc(obdSn, starTime, endTime);
	}
	
	public CarOilInfo getCarOilInfoByTime(String obdSn,Date oilInfoTime){
		return carOilInfoDao.getCarOilInfoByTime(obdSn,oilInfoTime);
	}

	/**
	 * 根据设备号获取最新油耗信息
	 * @param obdSn
	 * @return
	 */
	public List<CarOilInfo> queryOilInfo(String obdSn) {
		return carOilInfoDao.queryOilInfo(obdSn);
	}

	public CarOilInfo queryByIdAndObdSn(String id, String obdSn){
		return carOilInfoDao.queryByIdAndObdSn(id, obdSn);
	}
	public List<CarOilInfo> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
}
