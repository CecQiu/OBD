package com.hgsoft.carowner.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.CarDriveInfoDao;
import com.hgsoft.carowner.entity.CarDriveInfo;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;

@Service
public class CarDriveInfoService extends BaseService<CarDriveInfo> {
	
	@Resource
	private CarDriveInfoDao carDriveInfoDao;
	
	@Resource
	public void setDao(CarDriveInfoDao carDriveInfoDao){
		super.setDao(carDriveInfoDao);
	}
	
	@Override
	public CarDriveInfoDao getDao() {
		return (CarDriveInfoDao)super.getDao();
	}
	
	public void carDriveInfoUpdate(CarDriveInfo carDriveInfo){
		carDriveInfoDao.update(carDriveInfo);
	}
	/**
	 * 驾驶行为分析
	 * @param obdSn
	 * @param mobileNumber
	 * @param license
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List getDrivingBehavior(String obdSn,String mobileNumber,String license,String starTime,String endTime,Pager pager,int flag){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String sql="select c.obdSn,m.license,m.name,m.mobileNumber,c.driveDate,c.tmsSpeeding,c.tmsRapDec,c.tmsRapAcc,"
				+ "c.highSpeed,c.tmsSharpTurn,c.notMatch,c.longLowSpeed from car_drive_info c,meb_user m where m.carId=c.carId and c.type!=2 ";
		if(obdSn!=null&&obdSn.trim().length()>0){
			sql+=" and c.obdSn like '%"+obdSn+"%'";
		}
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(starTime!=null&&starTime.trim().length()>0){
			if(endTime!=null&&endTime.trim().length()>0){
				sql+=" and c.driveDate >='"+starTime+" 00:00:00"+"' and c.driveDate <='"+endTime+" 23:59:59"+"'";
			
			}else{
				sql+=" and c.driveDate >='"+starTime+" 00:00:00"+"' and c.driveDate <='"+df.format(new Date())+"'";
				
			}
		}
		if(endTime!=null&&endTime.trim().length()>0){
			if(starTime!=null&&starTime.trim().length()>0){
				
			}else{
				sql+=" and c.driveDate <='"+endTime+" 23:59:59"+"'";
				
			}
		}
		sql+=" ORDER BY c.driveDate DESC";
		List drivingList=new ArrayList();
		List list=this.getDao().queryBySQL(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
        if(flag==0){
        	for (int i = 0; i < list.size(); i++) {
    			Object[] ob=(Object[]) list.get(i);
    			
    			Map map=new HashMap();
    			map.put("obdSn", ob[0]);
    			map.put("license", ob[1]);
    			map.put("name", ob[2]);
    			map.put("mobileNumber", ob[3]);
    			map.put("driveDate", ob[4]);
    			map.put("tmsSpeeding", ob[5]);
    			map.put("tmsRapDec", ob[6]);
    			map.put("tmsRapAcc", ob[7]);
    			map.put("highSpeed", ob[8]);
    			map.put("tmsSharpTurn", ob[9]);
    			map.put("notMatch", ob[10]);
    			map.put("longLowSpeed", ob[11]);
    			drivingList.add(map);
    		
    		}
        }else{
        	for (int i = begin; i < list.size()&&i<end; i++) {
    			Object[] ob=(Object[]) list.get(i);
    			
    			Map map=new HashMap();
    			map.put("obdSn", ob[0]);
    			map.put("license", ob[1]);
    			map.put("name", ob[2]);
    			map.put("mobileNumber", ob[3]);
    			map.put("driveDate", ob[4]);
    			map.put("tmsSpeeding", ob[5]);
    			map.put("tmsRapDec", ob[6]);
    			map.put("tmsRapAcc", ob[7]);
    			map.put("highSpeed", ob[8]);
    			map.put("tmsSharpTurn", ob[9]);
    			map.put("notMatch", ob[10]);
    			map.put("longLowSpeed", ob[11]);
    			drivingList.add(map);
    		
    		}
        }			
		pager.setTotalSize(list.size());
		return drivingList;
	}
	
	/**
	 * 查询驾驶行为
	 * @param obdSN
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<CarDriveInfo> queryDriveInfo(String obdSN, String beginDate, String endDate) {
		return carDriveInfoDao.queryDriveInfo(obdSN, beginDate,endDate);
	}
	/**
	 * 保存驾驶行为
	 * @return
	 */
	public boolean carDriveInfoSave(CarDriveInfo carDriveInfo) {
		this.save(carDriveInfo);
		return true;
	}
	
	public CarDriveInfo getDriveInfoByDriveDate(String obdSn,
			Date driveDate) {
		return carDriveInfoDao.getDriveInfoByDriveDate(obdSn,driveDate);
	}
	
	/**
	 * 查询设备最新的驾驶行为信息
	 * @param obdSn
	 * @return
	 */
	public List<CarDriveInfo> queryDriveInfo(String obdSn) {
		return carDriveInfoDao.queryDriveInfo(obdSn);
	}

	public CarDriveInfo queryByIdAndObdSn(String id, String obdSn){
		return carDriveInfoDao.queryByIdAndObdSn(id, obdSn);
	}
	
	public List<CarDriveInfo> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
}
