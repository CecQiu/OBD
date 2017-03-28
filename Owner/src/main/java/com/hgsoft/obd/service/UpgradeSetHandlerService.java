package com.hgsoft.obd.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.UpgradeSetService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.util.UpgradeType;


/**
 * 待升级列表下发
 * @author sujunguang
 * 2016年9月1日
 * 下午4:47:57
 */
@Service
public class UpgradeSetHandlerService {
	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");
	private static Integer upgradeCount = Integer.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("upgradeCount", "3"));
	@Resource
	private UpgradeSetService upgradeSetService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ServerRequestQueryService requestQueryService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	public void upgradeSetHandler(String obdSn){
		UpgradeSet appUpgradeSet  = upgradeSetService.queryByObdSnLikeType(obdSn, 0);
		UpgradeSet iapUpgradeSet  = upgradeSetService.queryByObdSnLikeType(obdSn, 1);
		
		if(appUpgradeSet != null){
			Integer isSuccessAPP = appUpgradeSet.getSuccess();
			if(isSuccessAPP == null || isSuccessAPP.intValue() != 1){//设备真正升级成功不再下发
				handler(obdSn, appUpgradeSet, iapUpgradeSet, UpgradeType.APP);
			}
		}
		if(iapUpgradeSet != null){
			Integer isSuccessIAP = iapUpgradeSet.getSuccess();
			if(isSuccessIAP == null || isSuccessIAP.intValue() != 1){//设备真正升级成功不再下发
				handler(obdSn, appUpgradeSet, iapUpgradeSet, UpgradeType.IAP);
			}
		}
	}
	
	public void handler(String obdSn, UpgradeSet appUpgradeSet, UpgradeSet iapUpgradeSet, UpgradeType upgradeType){
		upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】"+upgradeType+"-------------------");
		upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】"+upgradeType+"设备appUpgradeSet："+appUpgradeSet+",设备iapUpgradeSet："+iapUpgradeSet);

		try {
			switch (upgradeType) {
				case APP:
					//已经审核+推送同步成功+没真正升级成功
					if(appUpgradeSet != null && "1".equals(appUpgradeSet.getAuditState()) && "1".equals(appUpgradeSet.getSendFlag())){
						int sendedCount = appUpgradeSet.getSendedCount().intValue();
						if(upgradeCount != null && sendedCount >= upgradeCount.intValue()){
							upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】不会再下发了！"+upgradeType+"下发次数："+sendedCount+",达到最高限制次数："+upgradeCount.intValue());
							return;
						}
						OBDDeviceVersion obdDeviceVersion  = requestQueryService.deviceVersion(obdSn);
						if(obdDeviceVersion != null){
							String version = obdDeviceVersion.getAppVersion();
							if(!StringUtils.isEmpty(version)){
								if(!appUpgradeSet.getFirmVersion().toLowerCase().equals(version.toLowerCase())){//版本不同，下发升级
									//顺序与限速
									if(orderCan(obdSn, appUpgradeSet, iapUpgradeSet, upgradeType) && speedCan(obdSn, appUpgradeSet)){
										appUpgradeSet.setValid("0");
										appUpgradeSet.setUpdateTime(new Date());
										appUpgradeSet.setSendedCount(sendedCount+1);
										upgradeSetService.upgradeSetSave(appUpgradeSet);
										String result = serverSettingService.deviceUpgradeSet(obdSn, "1", upgradeType, appUpgradeSet.getFirmVersion());
										upgradeDataLogger.info("------------"+obdSn+"【特定列表设备升级】"+upgradeType+"下发,结果:"+result);
									}
								}else{
									upgradeDataLogger.info("------------"+obdSn+"【特定列表设备升级】"+upgradeType+"版本相同，说明升级成功。");
									//版本相同，说明升级成功
									appUpgradeSet.setSuccess(1);
									upgradeSetService.upgradeSetSave(appUpgradeSet);
								}
							}
						}
					}else{
						return;
					}
					break;
				case IAP:
					//已经审核+推送同步成功+没真正升级成功
					if(iapUpgradeSet != null && "1".equals(iapUpgradeSet.getAuditState()) && "1".equals(iapUpgradeSet.getSendFlag())){
						int sendedCount = iapUpgradeSet.getSendedCount().intValue();
						if(upgradeCount != null && sendedCount >= upgradeCount.intValue()){
							upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】不会再下发了！"+upgradeType+"下发次数："+sendedCount+",达到最高限制次数："+upgradeCount.intValue());
							return;
						}
						OBDDeviceVersion obdDeviceVersion  = requestQueryService.deviceVersion(obdSn);
						if(obdDeviceVersion != null){
							String version = obdDeviceVersion.getIapVersion();
							if(!StringUtils.isEmpty(version)){
								if(!iapUpgradeSet.getFirmVersion().toLowerCase().equals(version.toLowerCase())){//版本不同，下发升级
									//顺序与限速
									if(orderCan(obdSn, appUpgradeSet, iapUpgradeSet, upgradeType) && speedCan(obdSn, iapUpgradeSet)){
										iapUpgradeSet.setValid("0");
										iapUpgradeSet.setUpdateTime(new Date());
										iapUpgradeSet.setSendedCount(sendedCount+1);
										upgradeSetService.upgradeSetSave(iapUpgradeSet);
										String result = serverSettingService.deviceUpgradeSet(obdSn, "1", upgradeType, iapUpgradeSet.getFirmVersion());
										upgradeDataLogger.info("------------"+obdSn+"【特定列表设备升级】"+upgradeType+"下发,结果:"+result);
									}
								}else{
									upgradeDataLogger.info("------------"+obdSn+"【特定列表设备升级】"+upgradeType+"版本相同，说明升级成功。");
									//版本相同，说明升级成功
									iapUpgradeSet.setSuccess(1);
									upgradeSetService.upgradeSetSave(iapUpgradeSet);
								}
							}
						}
					}else{
						return;
					}
					break;

				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upgradeDataLogger.error("------------"+obdSn+"【特定列表设备升级】"+upgradeType+"异常！", e);
		}
	}

	/**
	 * 限速条件能否下发升级
	 * @param obdSn
	 * @param upgradeType
	 * @return
	 */
	private boolean speedCan(String obdSn, UpgradeSet upgradeSet){
		boolean can = false;
		if(upgradeSet != null){
			Integer obdSpeed = upgradeSet.getObdSpeed();
			Integer gpsSpeed = upgradeSet.getGpsSpeed();
			if(gpsSpeed != null){
				Integer lastGPSSpeed = GlobalData.OBD_PositionGPSSpeed.get(obdSn);
				upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】限速条件，记录上一传gps速度："+lastGPSSpeed+"-------------------");
				if(lastGPSSpeed != null){
					upgradeSet.setSpeedCount(upgradeSet.getSpeedCount()+1);
					upgradeSet.setUpdateTime(new Date());
					upgradeSet.setLastSpeedType("gps");
					upgradeSetService.upgradeSetUpdate(upgradeSet);
					if(lastGPSSpeed.intValue() >= gpsSpeed.intValue()){
						can = true;
						return can;
					}
				}
			}
			if(obdSpeed != null){
				Integer lastObdSpeed = GlobalData.OBD_PositionCarSpeed.get(obdSn);
				upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】限速条件，记录上一传obd速度："+lastObdSpeed+"-------------------");
				if(lastObdSpeed != null){
					upgradeSet.setSpeedCount(upgradeSet.getSpeedCount()+1);
					upgradeSet.setUpdateTime(new Date());
					upgradeSet.setLastSpeedType("obd");
					upgradeSetService.upgradeSetUpdate(upgradeSet);
					if(lastObdSpeed.intValue() >= obdSpeed.intValue()){
						can = true;
						return can;
					}
				}
			}
			if(gpsSpeed == null && obdSpeed == null){//都没设置，则不用卡速度条件
				can = true;
			}
		}
		upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】限速条件，升级类型："+upgradeSet+",能否下发"+can+"-------------------");
		return can;
	}

	/**
	 * 升级顺序
	 * 创建时间小的说明先导入，先导入的优先升级
	 * @param appUpgradeSet
	 * @param iapUpgradeSet
	 * @param upgradeType
	 * @return
	 */
	private boolean orderCan(String obdSn, UpgradeSet appUpgradeSet, UpgradeSet iapUpgradeSet, UpgradeType upgradeType){
		boolean can = true;
		switch (upgradeType) {
		case APP://升级APP，则判断是否还有有IAP记录或者未升级成功的IAP，有则进行时间判断
			if(iapUpgradeSet != null){
				Integer isSuccessIAP = iapUpgradeSet.getSuccess();
				if(isSuccessIAP == null || isSuccessIAP.intValue() != 1){
					if(appUpgradeSet.getCreateTime().getTime() > iapUpgradeSet.getCreateTime().getTime()){
						can = false;
					}
				}
			}
			break;
		case IAP://升级IAP，则判断是否还有有APP记录或者未升级成功的APP，有则进行时间判断
			if(appUpgradeSet != null){
				Integer isSuccessAPP = appUpgradeSet.getSuccess();
				if(isSuccessAPP == null || isSuccessAPP.intValue() != 1){
					if(iapUpgradeSet.getCreateTime().getTime() > appUpgradeSet.getCreateTime().getTime()){
						can = false;
					}
				}
			}
			break;

		default:
			break;
		}
		upgradeDataLogger.info("--------------"+obdSn+"【特定列表设备升级】顺序，升级类型："+upgradeType+",能否优先下发"+can+"-------------------");
		return can;
	}
}
