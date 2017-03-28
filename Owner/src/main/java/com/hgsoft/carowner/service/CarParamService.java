/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.CarParamDao;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.common.service.BaseService;

/**
 * @author liujialin
 *
 */
@Service
public class CarParamService extends BaseService<CarParam> {
	private final Log logger = LogFactory.getLog(CarParamService.class);

	@Resource
	public void setDao(CarParamDao carParamDao){
		super.setDao(carParamDao);
	}
	
	public CarParamDao getCarParamDao() {
		return (CarParamDao) this.getDao();
	}
	
	public boolean carParamSaveOrUpdate(CarParam carParam){
		logger.info(carParam+"*********************车辆参数");
		//判断当前车辆参数是否存在
		CarParam param=this.getCarParamDao().isExist(carParam.getObdSn());
		if(param!=null){
			String areaNum = carParam.getAreaNum();
			if(areaNum!=null){
				param.setAreaNum(areaNum);
			}
			
			String railAndAlert = carParam.getRailAndAlert();
			if(railAndAlert!=null){
				param.setRailAndAlert(railAndAlert);
			}
			
			
			String maxLongitude = carParam.getMaxLongitude();
			if(maxLongitude!=null){
				param.setMaxLongitude(maxLongitude);
			}
			
			String maxLatitude = carParam.getMaxLatitude();
			if(maxLatitude!=null){
				param.setMaxLatitude(maxLatitude);
			}
			
			String minLongitude = carParam.getMinLongitude();
			if(minLongitude!=null){
				param.setMinLongitude(minLongitude);
			}
			
			String minLatitude = carParam.getMinLatitude();
			if(minLatitude!=null){
				param.setMinLatitude(minLatitude);
			}
			
			String overspeed = carParam.getOverspeed();
			if(overspeed!=null){
				param.setOverspeed(overspeed);
			}
			
			String sleepTime = carParam.getSleepTime();
			if(sleepTime!=null){
				param.setSleepTime(sleepTime);
			}
			
			String accTime = carParam.getAccTime();
			if(accTime!=null){
				param.setAccTime(accTime);
			}
			
			String heartTime = carParam.getHeartTime();
			if(heartTime!=null){
				param.setHeartTime(heartTime);
			}
			
			String GPS = carParam.getGps();
			if(GPS!=null){
				param.setGps(GPS);
			}
			
			String position = carParam.getPosition();
			if(position!=null){
				param.setPosition(position);
			}
			
			String safety = carParam.getSafety();
			if(safety!=null){
				param.setSafety(safety);
			}
			
			String timeZone = carParam.getTimeZone();
			if(timeZone!=null){
				param.setTimeZone(timeZone);
			}
			
			String voltage = carParam.getVoltage();
			if(voltage!=null){
				param.setVoltage(voltage);
			}
			
			String GSM = carParam.getGsm();
			if(GSM!=null){
				param.setGsm(GSM);
			}
			
			String undervoltage = carParam.getUndervoltage();
			if(undervoltage!=null){
				param.setUndervoltage(undervoltage);
			}
			
			String highVoltage = carParam.getHighVoltage();
			if(highVoltage!=null){
				param.setHighVoltage(highVoltage);
			}
			
			String tiredDrive = carParam.getTiredDrive();
			if(tiredDrive!=null){
				param.setTiredDrive(tiredDrive);
			}
			
			String tiredDriveTime = carParam.getTiredDriveTime();
			if(tiredDriveTime!=null){
				param.setTiredDriveTime(tiredDriveTime);
			}
			
			String waterTemp = carParam.getWaterTemp();
			if(waterTemp!=null){
				param.setWaterTemp(waterTemp);
			}
			
			String userId = carParam.getUserId();
			if(userId!=null){
				param.setUserId(userId);
			}
			
			String ebrake = carParam.getEbrake();
			if(ebrake!=null){
				param.setEbrake(ebrake);
			}
			
			String espeedup = carParam.getEspeedup();
			if(espeedup!=null){
				param.setEspeedup(espeedup);
			}
			
			String crash = carParam.getCrash();
			if(crash!=null){
				param.setCrash(crash);
			}
			
			String shake = carParam.getShake();
			if(shake!=null){
				param.setShake(shake);
			}
			
			String speedRemind = carParam.getSpeedRemind();
			if(speedRemind!=null){
				param.setSpeedRemind(speedRemind);
			}
			
			String IMEI = carParam.getImei();
			if(IMEI!=null){
				param.setImei(IMEI);
			}
			
			String dataUploadType = carParam.getDataUploadType();
			if(dataUploadType!=null){
				param.setDataUploadType(dataUploadType);
			}
			
			String dataUploadTime = carParam.getDataUploadTime();
			if(dataUploadTime!=null){
				param.setDataUploadTime(dataUploadTime);
			}
			
			String positionExtra = carParam.getPositionExtra();
			if(positionExtra!=null){
				param.setPositionExtra(positionExtra);
			}
			
			String GSMBTS = carParam.getGsmbts();
			if(GSMBTS!=null){
				param.setGsmbts(GSMBTS);
			}
			
			String communication = carParam.getCommunication();
			if(communication!=null){
				param.setCommunication(communication);
			}
			
			String positionType = carParam.getPositionType();
			if(positionType!=null){
				param.setPositionType(positionType);
			}
		
			this.getDao().saveOrUpdate(param);
		}else{
			this.getDao().save(carParam);
		}
		return true;
	}
}
