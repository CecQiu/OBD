package com.hgsoft.obd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.DomainState;
import com.hgsoft.carowner.entity.GpsSet;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.carowner.entity.WifiSet;
import com.hgsoft.carowner.service.DomainStateService;
import com.hgsoft.carowner.service.GpsStateSetService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdHandShakeService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.carowner.service.ObdTimeParamsService;
import com.hgsoft.carowner.service.ObdTravelParamsService;
import com.hgsoft.carowner.service.WiFiService;
import com.hgsoft.carowner.service.WifiStateSetService;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.util.ExtensionDataSetType;
import com.hgsoft.obd.util.SettingType;
/**
 * 未完成的任务
 *		离线时的操作，或在线由于网络原因操作不成功的
 * @author sujunguang
 * 2016年1月22日
 * 下午3:55:31
 */
@Service
public class UnfinishedTaskService {
	private static Logger serverSendObdLogger = LogManager.getLogger("serverSendObdLogger");
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private GpsStateSetService gpsStateSetService;
	@Resource
	private WifiStateSetService wifiStateSetService;
	@Resource
	private ObdTimeParamsService obdTimeParamsService;
	@Resource
	private ObdSettingService obdSettingService;
	@Resource
	private ObdTravelParamsService obdTravelParamsService;
	@Resource
	private WiFiService wiFiService;
	@Resource
	private ObdSateService obdSateService;
	@Resource
	private ObdHandShakeService obdHandShakeService;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private DomainStateService domainStateService;
	@Resource
	private DomainSynchroService domainSynchroService;
	@Resource
	private ProtalSendService protalSendService;// protalService设置
	
	public void handle(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】---------"+obdSn);
		handleGPS(obdSn);
		handleWiFi(obdSn);
		handleDrive(obdSn);
		handleWiFiPwdSsid(obdSn);
		handleSwitch(obdSn);
		handlePortalSwitch(obdSn);
		handleWakeupSwitch(obdSn);
		handleWakeupTime(obdSn);
		handleDomainSyn(obdSn);
		handleDomain(obdSn);
	}
	
	private void handleGPS(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】【GPS】---------"+obdSn);
		/**
		 * 1、获得最新设置的GPS记录
		 * 2、判断最新记录设置是否成功
		 * 3、成功，则不操作
		 * 4、不成功，则设备在线进行再次设置
		 */
		GpsSet gpsSet = gpsStateSetService.queryLastGpsSet(obdSn);
		if(gpsSet != null){
			//设置不成功
			if("1".equals(gpsSet.getValid())){
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】gps设置下发前："+gpsSet+"----"+obdSn);
				//进行再次下发设置
				try {
					String gState = "0".equals(gpsSet.getGpsState())?"1":"0";
					String result = serverSettingService.gps(obdSn, gState);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】下发结果："+result+"---"+obdSn);
					if(GlobalData.isSendResultSuccess(result)){
						//下发设置成功
						gpsSet.setUpdateTime(new Date());
						gpsSet.setValid("0");
						gpsStateSetService.gpsSetUpdate(gpsSet);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】gps设置下发结果："+gpsSet+"---"+obdSn);
						OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】设备更新前："+obdStockInfo+"---"+obdSn);
						if(obdStockInfo != null){
							obdStockInfo.setGpsState(gpsSet.getGpsState());
							obdStockInfoService.obdStateUpdate(obdStockInfo);
							serverSendObdLogger.info("----------【未完成的下发操作进行下发】设备更新结果："+obdStockInfo+"---"+obdSn);
						}
					}
				} catch (OBDException e) {
					e.printStackTrace();
					serverSendObdLogger.error("----------【未完成的下发操作进行下发】异常："+e+"---"+obdSn);
				}
			}
		}
	}
	
	private void handleWiFi(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】【WIFI】---------"+"---"+obdSn);
		//wifi开关
		try {
			WifiSet wifiSet = wifiStateSetService.queryLastWifiSet(obdSn,"4");
			if(wifiSet != null){
				//设置不成功
				if("1".equals(wifiSet.getValid())){
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】wifi设置前："+wifiSet+"---"+obdSn);
					//进行再次下发设置
					String wState= "0".equals(wifiSet.getWifiState())?"1":"0";
					String result = serverSettingService.wifi(obdSn, wState);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】下发结果："+result+"---"+obdSn);
					if(GlobalData.isSendResultSuccess(result)){
						//下发设置成功
						wifiSet.setUpdateTime(new Date());
						wifiSet.setValid("0");
						wifiStateSetService.wifiSetUpdate(wifiSet);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】wifi设置下发结果："+wifiSet+"---"+obdSn);
						OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】设备更新前："+obdStockInfo+"---"+obdSn);
						if(obdStockInfo != null){
							obdStockInfo.setWifiState(wifiSet.getWifiState());
							obdStockInfoService.obdStateUpdate(obdStockInfo);
							serverSendObdLogger.info("----------【未完成的下发操作进行下发】设备更新结果："+obdStockInfo+"---"+obdSn);
						}
					}
				}
			}
			//wifi使用时间
			WifiSet wifiUseTimeSet = wifiStateSetService.queryLastWifiSet(obdSn,"5");
			
			if(wifiUseTimeSet!=null){
				Integer useTime = wifiUseTimeSet.getUseTime();
				//设置不成功
				if("1".equals(wifiUseTimeSet.getValid())){
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】wifi使用时间设置前："+wifiUseTimeSet+"---"+obdSn);
					//再次下发
					String bits = "10111111";
					OBDTimeParams obdTimeParams = null;
					obdTimeParams=obdTimeParamsService.getObdTimeParamsBySn(obdSn);
					if(obdTimeParams==null){
						obdTimeParams = new OBDTimeParams(); 
						obdTimeParams.setId(IDUtil.createID());
						obdTimeParams.setObdSn(obdSn);
						obdTimeParams.setCreateTime(new Date());
					}
					obdTimeParams.setWifiUseTime(useTime);
					
					String rt=serverSettingService.deviceTimeSet(obdSn, bits.toCharArray(), obdTimeParams);
					boolean flag = GlobalData.isSendResultSuccess(rt);
					if(flag){
						//更新时间参数表
						obdTimeParamsService.add(obdTimeParams);
						//更新wifiSet表
						wifiUseTimeSet.setValid("0");
						wifiStateSetService.wifiSetUpdate(wifiUseTimeSet);
					}
				}
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.error("----------【未完成的下发操作进行下发】异常："+e+"---"+obdSn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 驾驶行为设置
	 * @param obdSn
	 */
	private void handleDrive(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】【驾驶行为设置】------------"+obdSn);
		try {
			List<ObdSetting> obdSettings=obdSettingService.queryByObdSnAndType(obdSn, "drive_%");
			if(obdSettings!=null && obdSettings.size()>0){
				String bitStr="1111111111111111";
				OBDTravelParams op =  obdTravelParamsService.queryByObdSn(obdSn);
				if(op==null){
					op = new OBDTravelParams();
					op.setId(IDUtil.createID());
					op.setObdSn(obdSn);
					op.setCreateTime(new Date());
				}
				for (ObdSetting obdSetting : obdSettings) {
					String settingMsg = obdSetting.getSettingMsg();
					JSONObject json = JSONObject.fromObject(settingMsg);
					SettingType settingType = SettingType.getSettingTypeByValue(obdSetting.getType());
					if(settingType==null){
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】驾驶行为设置：类型异常:"+obdSn);
						continue;
					}
					switch (settingType) {
					case DRIVE_00:
						String quickenSpeed = json.getString("quickenSpeed");
						op.setShuddenOverSpeed(Integer.parseInt(quickenSpeed));// 急加速阈值：速度变化阈值km/h
						op.setShuddenOverSpeedTime(2);
						bitStr = StrUtil.StringReplaceByIndex(bitStr, 3, 4, "0");
						break;
					case DRIVE_01:
						String quickSlowDownSpeed = json.getString("quickSlowDownSpeed");
						Integer qsd1 = Integer.parseInt(quickSlowDownSpeed);
						op.setShuddenLowSpeed(qsd1);//速度变化阈值km/h
						op.setShuddenLowSpeedTime(2);// 急减速阈值：	时间阈值
						bitStr = StrUtil.StringReplaceByIndex(bitStr, 4, 5, "0");
						break;
					case DRIVE_02:
						String quickturnSpeed = json.getString("quickturnSpeed");
						String quickturnAngle = json.getString("quickturnAngle");
						Integer qt1 = Integer.parseInt(quickturnSpeed);
						Integer qt2 = Integer.parseInt(quickturnAngle);
						op.setShuddenTurnSpeed(qt1);// 急转弯阈值：速度阈值 km/h
						op.setShuddenTurnAngle(qt2);
						bitStr = StrUtil.StringReplaceByIndex(bitStr, 2, 3, "0");
						break;
					case DRIVE_03:
						String overspeed = json.getString("overspeed");
						String overspeedTime = json.getString("overspeedTime");
						Integer os1 = Integer.parseInt(overspeed);
						Integer os2 = Integer.parseInt(overspeedTime);
						op.setOverSpeed(os1);// 超速阈值：时速阈值km/h
						op.setLimitSpeedLazy(os2);// 超速阈值：限速延迟时间阈值s
						bitStr = StrUtil.StringReplaceByIndex(bitStr, 1, 2, "0");
						break;
					case DRIVE_04:
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】驾驶行为设置：疲劳驾驶设置暂无---"+obdSn);
						break;	
					default:
						break;
					}
				}
				String rt=serverSettingService.travelParamsSet(obdSn, bitStr.toCharArray(), op);
				boolean flag = GlobalData.isSendResultSuccess(rt);
				serverSendObdLogger.info("----------【驾驶行为参数设置】设备：" + obdSn + "，操作结果：" + flag);
				if(flag){
					//如果设置成功,更新
					obdTravelParamsService.add(op);
					obdSettingService.setNoValidByLikeType(obdSn, "drive_%");
				}
			}
		}catch(Exception e){
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】驾驶行为设置：设置异常:"+e+"---"+obdSn);
		}
	}
	
	/**
	 * wifi密码和ssid
	 * @param obdSn
	 */
	private void handleWiFiPwdSsid(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】【wifi密码和ssid设置】------------"+obdSn);
		try {
			List<ObdSetting> obdSettings=obdSettingService.queryByObdSnAndType(obdSn, "wifi_1%");
			if(obdSettings==null || obdSettings.size()==0){
				return;
			}
			String wPwd = "";
			String wName = "";
			for (ObdSetting obdSetting : obdSettings) {
				String type = obdSetting.getType();
				String settingMsg = obdSetting.getSettingMsg();
				JSONObject json = JSONObject.fromObject(settingMsg);
				switch (type) {
				case "wifi_10":
					wPwd = json.getString("wPwd");
					break;
				case "wifi_11":
					wName = json.getString("wName");
					break;
				case "wifi_12":
					wPwd = json.getString("wPwd");
					wName = json.getString("wName");
					break;	
				default:
					break;
				}
			}
			String bits = "0000001100000000";
			String rt = null;
			if(!StringUtils.isEmpty(wPwd) && !StringUtils.isEmpty(wName)){
//				bits = "0000001100000000";
//				rt=serverSettingService.portalOrWifiSet(obdSn, bits.toCharArray(), wName,wPwd);
				bits = "0000001000000000";
				String rt1=serverSettingService.portalOrWifiSet(obdSn, bits.toCharArray(), wName);
				boolean flag1 = GlobalData.isSendResultSuccess(rt1);
				bits = "0000000100000000";
				String rt2=serverSettingService.portalOrWifiSet(obdSn, bits.toCharArray(), wPwd);
				boolean flag2 = GlobalData.isSendResultSuccess(rt2);
				if(flag1 && flag2){
					rt = "00";//如果两个都是成功,默认是成功
				}
			}else if(!StringUtils.isEmpty(wPwd)){
				bits = "0000000100000000";
				rt=serverSettingService.portalOrWifiSet(obdSn, bits.toCharArray(),wPwd);
			}else if(!StringUtils.isEmpty(wName)){
				bits = "0000001000000000";
				rt=serverSettingService.portalOrWifiSet(obdSn, bits.toCharArray(), wName);
			}
			
			boolean flag = GlobalData.isSendResultSuccess(rt);
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】wifi密码和ssid设置,结果:"+flag+"---"+obdSn);
			if(flag){
				for (ObdSetting obdSetting : obdSettings) {
					obdSetting.setUpdateTime(new Date());
					obdSetting.setValid("0");
					obdSettingService.obdSettingSave(obdSetting);
				}
				
				//参数保存在wifi表里
				 Wifi wifi = wiFiService.isExist(obdSn);
				 if(wifi==null){
					 wifi = new Wifi();
					 wifi.setId(IDUtil.createID());
					 wifi.setCreateTime(new Date());
				 }
				 wifi.setObdSn(obdSn);
				 wifi.setWifiPwd(wPwd);
				 wifi.setSsid(wName);
				 wiFiService.wifiSave(wifi);
				 serverSendObdLogger.info("----------【未完成的下发操作进行下发】wifi密码和ssid设置,更新wifi表 ---"+obdSn);
			}
				
		}catch(Exception e){
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】wifi密码和ssid设置：设置异常:"+e+"---"+obdSn);
		}
	}
	
	/**
	 * 开关设置
	 * 直接调用接口查询obd状态
	 * obdstate表
	 * obd_handshake表
	 * 如果都为空那就不给设置
	 * @param obdSn
	 */
	private void handleSwitch(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】【obd预警开关设置】------------"+obdSn);
		try {
			List<ObdSetting> obdSettings=obdSettingService.queryByObdSnAndType(obdSn, "switch_%");
			if(obdSettings==null || obdSettings.size()==0){
				return;
			}
			//开关状态
			String startupSwitch = null;
			String shakeSwitch = null;
			String voltageSwitch = null;
			String engineTempSwitch = null;
			String carfaultSwitch = null;
			String overspeedSwitch = null;
			String efenceSwitch = null;
			String warnSet="";
			boolean flag = false;
			
			//直接查询obd如果查询到就设置，否则离线设置.
			ObdHandShake obdHandShake=serverRequestQueryService.warnSettings(obdSn);
			if(obdHandShake!=null){
				startupSwitch = obdHandShake.getIllegalStartSet().toString();//非法启动
				shakeSwitch = obdHandShake.getIllegalShockSet().toString();//非法启动探测设置：0-开启 1-关闭
				voltageSwitch = obdHandShake.getVoltUnusualSet().toString();//蓄电电压异常报警设置
				engineTempSwitch = obdHandShake.getEngineWaterWarnSet().toString();//发动机水温高报警设置
				carfaultSwitch = obdHandShake.getCarWarnSet().toString();//车辆故障报警设置
				overspeedSwitch = obdHandShake.getOverSpeedWarnSet().toString();//超速报警设置
				efenceSwitch = obdHandShake.getEfenceWarnSet().toString();//电子围栏报警设置
				if(!StringUtils.isEmpty(startupSwitch) && !StringUtils.isEmpty(shakeSwitch)
						&& !StringUtils.isEmpty(voltageSwitch) && !StringUtils.isEmpty(engineTempSwitch) 
						&& !StringUtils.isEmpty(carfaultSwitch) && !StringUtils.isEmpty(overspeedSwitch)
						&& !StringUtils.isEmpty(efenceSwitch)){
					warnSet = startupSwitch + shakeSwitch + voltageSwitch + engineTempSwitch + carfaultSwitch + overspeedSwitch + efenceSwitch + "1"; 
					
					flag = true;
				}
			}
			
			if(flag){
				for (ObdSetting obdSetting : obdSettings) {
					String type = obdSetting.getType();
					String settingMsg = obdSetting.getSettingMsg();
					JSONObject json = JSONObject.fromObject(settingMsg);
					String switchType = json.getString("switchType");
					String switchState = json.getString("switchState");
					Integer index = Integer.parseInt(switchType);
					String switchS = StrUtil.strExchange(switchState, "0", "1");//0和1对换
					//保留
					warnSet = StrUtil.StringReplaceByIndex(warnSet, index, index+1, switchS);

				}
				//下发指令
//				warnSet=StrUtil.swapWords(warnSet);
				String rt=serverSettingService.warnSet(obdSn, warnSet);
				flag = GlobalData.isSendResultSuccess(rt);
			}
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】【obd预警开关设置】下发设置指令结果---------"+flag+"---"+obdSn);
			
			if(flag){
				//更新设置表
				for (ObdSetting obdSetting : obdSettings) {
					obdSetting.setUpdateTime(new Date());
					obdSetting.setValid("0");
					obdSettingService.obdSettingSave(obdSetting);
				}
			}
				
		}catch(Exception e){
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】【obd预警开关设置】设置异常---------"+e+"---"+obdSn);
		}
	
	}
	
	/**
	 * 自动唤醒开关
	 * 1、获得最新设置的GPS记录
	 * 2、判断最新记录设置是否成功
	 * 3、成功，则不操作
	 * 4、不成功，则设备在线进行再次设置
	 */
	private void handleWakeupSwitch(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒开关---------"+obdSn);
		String settype = SettingType.WAKEUPSWITCH.getValue();
		ObdSetting obdSetting=obdSettingService.queryLastObdSetting(obdSn, settype);
		if(obdSetting != null){
			//设置不成功
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒开关,设置下发前："+obdSn+"---"+obdSetting);
			//进行再次下发设置
			try {
				String settingMsg = obdSetting.getSettingMsg();
				JSONObject json = JSONObject.fromObject(settingMsg);
				String wakeupSwitch = json.getString("wakeupSwitch");
				
				String result = serverSettingService.offHeart(obdSn, wakeupSwitch);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒开关,下发结果："+result+"---"+obdSn);
				if(GlobalData.isSendResultSuccess(result)){
					//下发设置成功
					obdSetting.setUpdateTime(new Date());
					obdSetting.setValid("0");
					boolean flag=obdSettingService.obdSettingSave(obdSetting);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒开关,离线记录置为无效结果："+flag+"---"+obdSn);
					//更新obdState表
					if(flag){
						//设置成功，更新obdstate字段
						ObdState obdState=obdSateService.queryByObdSn(obdSn);
						if(obdState==null){
							obdState = new ObdState();
							obdState.setId(IDUtil.createID());
							obdState.setObdSn(obdSn);
							obdState.setCreateTime(new Date());
							obdState.setValid("1");
						}
						obdState.setWakeup(wakeupSwitch);
						boolean ff=obdSateService.add(obdState);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒开关，离线设置成功，将obdState表更新结果---" + ff+"---"+obdSn);
					}
				}
			} catch (OBDException e) {
				e.printStackTrace();
				serverSendObdLogger.error("----------【未完成的下发操作进行下发】自动唤醒开关,异常："+e+"---"+obdSn);
			}
		}
	}
	
	/**
	 * portal开关
	 * 1、获得最新设置的GPS记录
	 * 2、判断最新记录设置是否成功
	 * 3、成功，则不操作
	 * 4、不成功，则设备在线进行再次设置
	 */
	private void handlePortalSwitch(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】portal开关---------"+obdSn);
		String settype = SettingType.PORTAL_00.getValue();
		ObdSetting obdSetting=obdSettingService.queryLastObdSetting(obdSn, settype);
		if(obdSetting != null){
			//设置不成功
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】portal开关,设置下发前："+obdSn+"---"+obdSetting);
			//进行再次下发设置
			try {
				String settingMsg = obdSetting.getSettingMsg();
				JSONObject json = JSONObject.fromObject(settingMsg);
				String onOff = json.getString("portalSwitch");
				
				String result ="";
				String bitsStr="0000010100000000";
				char[] bits=bitsStr.toCharArray();
				
				if("0".equals(onOff)){
					//关闭portal
					//打开portal,默认密码是1234567890
					//查询改设备是否存在密码
					Wifi wifi=wiFiService.isExist(obdSn);
					String pwd="1234567890";
					if(wifi!=null && !StringUtils.isEmpty(wifi.getWifiPwd())){
						pwd =wifi.getWifiPwd();
					}
					result=serverSettingService.portalOrWifiSet(obdSn, bits, "00",pwd);
				}else if("1".equals(onOff)){
					result=serverSettingService.portalOrWifiSet(obdSn, bits, "01");
				}
				
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】portal开关,下发结果："+result+"---"+obdSn);
				if(GlobalData.isSendResultSuccess(result)){
					//下发设置成功
					obdSetting.setUpdateTime(new Date());
					obdSetting.setValid("0");
					boolean flag=obdSettingService.obdSettingSave(obdSetting);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】portal开关,离线记录置为无效结果："+flag+"---"+obdSn);
					//更新portal开关记录
					Integer total=protalSendService.setByParam(obdSn, "6", "0");
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】portal开关记录置为无效总数："+total+"---"+obdSn);
				}
			} catch (Exception e) {
				e.printStackTrace();
				serverSendObdLogger.error("----------【未完成的下发操作进行下发】portal开关,异常："+e+"---"+obdSn);
			}
		}
	}
	
	/**
	 * 自动唤醒时间
	 * 1、获得最新设置的GPS记录
	 * 2、判断最新记录设置是否成功
	 * 3、成功，则不操作
	 * 4、不成功，则设备在线进行再次设置
	 */
	private void handleWakeupTime(String obdSn) {
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒时间---------" + obdSn);
		String settype = SettingType.WAKEUPTIME.getValue();
		ObdSetting obdSetting = obdSettingService.queryLastObdSetting(obdSn, settype);
		if (obdSetting != null) {
			// 设置不成功
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒时间,设置下发前：" + obdSn + "---" + obdSetting);
			// 进行再次下发设置
			try {
				String settingMsg = obdSetting.getSettingMsg();
				JSONObject json = JSONObject.fromObject(settingMsg);
				Integer wakeupTime = json.optInt("wakeupTime");

				OBDTimeParams obdTimeParams = new OBDTimeParams();
				obdTimeParams.setObdOfflineTime(wakeupTime);
				String positionChar = "11111011";

				String result = serverSettingService.deviceTimeSet(obdSn, positionChar.toCharArray(), obdTimeParams);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒时间,下发结果：" + result + "---" + obdSn);
				if (GlobalData.isSendResultSuccess(result)) {
					// 下发设置成功
					obdSetting.setUpdateTime(new Date());
					obdSetting.setValid("0");
					boolean flag = obdSettingService.obdSettingSave(obdSetting);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒时间,离线记录置为无效结果：" + flag + "---" + obdSn);
					// 更新obdState表
					if (flag) {
						// 设置成功，更新OBDTimeParams字段
						OBDTimeParams otp = obdTimeParamsService.getObdTimeParamsBySn(obdSn);
						if (otp == null) {
							otp = new OBDTimeParams();
							otp.setId(IDUtil.createID());
							otp.setObdSn(obdSn);
							otp.setCreateTime(new Date());
						}
						otp.setObdOfflineTime(wakeupTime);

						boolean ff = obdTimeParamsService.add(otp);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】自动唤醒时间，离线设置成功，将obdState表更新结果---" + ff + "---" + obdSn);
					}
				}
			} catch (OBDException e) {
				e.printStackTrace();
				serverSendObdLogger.error("----------【未完成的下发操作进行下发】自动唤醒时间,异常：" + e + "---" + obdSn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				serverSendObdLogger.error("----------【未完成的下发操作进行下发】自动唤醒时间,异常：" + e + "---" + obdSn);
			}
		}
	}
	
	
	/**
	 * 域名单同步
	 * 必须先同步完成,才可以下发域名单离线设置
	 * @param obdSn
	 */
	private synchronized void handleDomainSyn(String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单同步----------"+obdSn);
		List<ObdSetting> obdSettings=obdSettingService.queryByObdSnAndType(obdSn, "dmsyn_%");
		if(obdSettings==null || obdSettings.size()==0){
			//没有同步记录
			return;
		}
		for (ObdSetting obdSetting : obdSettings) {
			SettingType type = SettingType.getSettingTypeByValue(obdSetting.getType());
			switch (type) {
			case DOMAINSYN_00:
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单同步----------"+obdSn+"---白名单同步");
				domainSynchroService.type3(obdSn);
				break;
			case DOMAINSYN_01:
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单同步----------"+obdSn+"---黑名单同步");
				domainSynchroService.type6(obdSn);
				break;
			default:
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单同步----------"+obdSn+"---同步类型有误.");
				break;
			}
			
		}
		
	}
	
	/**
	 * 域黑白名单设置
	 * 下发成功更新domainSet记录置为无效,并且更新domainState表
	 * 新增多条域白名单和域黑名单前，先判断离线的白名单总数加上已设置成功的白名单总记录数是否超过20，如果超过20，将这些离线设置都置为无效
	 * @param obdSn
	 */
	private void handleDomain(final String obdSn){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置----------"+obdSn);
		try {
			List<ObdSetting> obdSettings=obdSettingService.queryByObdSnAndType(obdSn, "domain_%");
			if(obdSettings==null || obdSettings.size()==0){
				return;
			}
			String whiteSwitch = null;//白开关
			String blackSwitch = null;//黑开关
			StringBuffer whiteSet = new StringBuffer("");//白名单列表
			StringBuffer blackSet = new StringBuffer("");//黑名单列表
			Set<String> whiteSingleDel = new HashSet<>();//删除单个白
			Set<String> blackSingleDel = new HashSet<>();//删除单个黑
			boolean whiteDelAll = false;//删除所有白
			boolean blackDelAll = false;//删除所有黑
			ObdSetting wsSetting = null;//白开关
			ObdSetting bsSetting = null;//黑开关
			List<ObdSetting> wls= new ArrayList<>();//白名单列表
			List<ObdSetting> bls= new ArrayList<>();//黑名单列表
			
			Map<String,ObdSetting> whiteMap = new HashMap<>();//白名单列表
			Map<String,ObdSetting> blackMap = new HashMap<>();//黑名单列表
			List<ObdSetting> wss= new ArrayList<>();//删除单条白名单列表
			List<ObdSetting> bss= new ArrayList<>();//删除单条黑名单列表
			ObdSetting wdaSetting = null;//删除所有白;
			ObdSetting bdaSetting = null;//删除所有黑;
			
			for (ObdSetting obdSetting : obdSettings) {
				String type = obdSetting.getType();
				String settingMsg = obdSetting.getSettingMsg();
				JSONObject json = JSONObject.fromObject(settingMsg);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置----------"+obdSn+"---类型:"+type+"---设置内容:"+settingMsg);
				switch (type) {
				case "domain_00":
					//0域白名单功能开关;
					whiteSwitch = json.optString("whiteSwitch");
					wsSetting = obdSetting;
					break;
				case "domain_01":
					//1域黑名单功能开关;
					blackSwitch = json.optString("blackSwitch");
					bsSetting = obdSetting;
					break;
				case "domain_02":
					//不需要管
					//拼接报文
					break;
				case "domain_03":
					//增加多个域白名单;
					String dn1 = json.optString("domainName");
					whiteSet.append(dn1+";");
					wls.add(obdSetting);
					whiteMap.put(dn1, obdSetting);
					break;
				case "domain_04":
					//4删除单个域白名单;
					whiteSingleDel.add(json.optString("domainName"));
					wss.add(obdSetting);
					//拼接报文
					break;
				case "domain_05":
					//5删除所有域白名单
					whiteDelAll = true;
					wdaSetting = obdSetting;
					//拼接报文
					break;
				case "domain_06":
					//6增加多个域黑名单;
					String jn2 = json.optString("domainName");
					blackSet.append(jn2+";");
					bls.add(obdSetting);
					blackMap.put(jn2, obdSetting);
					//拼接报文
					break;
				case "domain_07":
					//7删除单个域黑名单;
					blackSingleDel.add(json.optString("domainName"));
					bss.add(obdSetting);
					//拼接报文
					break;
				case "domain_08":
					//8删除所有域黑名单
					blackDelAll = true;
					bdaSetting = obdSetting;
					//拼接报文
					break;	
				default:
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置----------"+obdSn+"---类型参数异常:"+type);
					throw new Exception("----------【未完成的下发操作进行下发】域黑白名单设置----------"+obdSn+"---类型参数异常:"+type);
				}
				
			}
			
			//拼接指令
			if(!StringUtils.isEmpty(whiteSwitch)){
				type0(obdSn, whiteSwitch, wsSetting);
			}
			
			if(!StringUtils.isEmpty(blackSwitch)){
				type1(obdSn, blackSwitch, bsSetting);
			}
			
			//删除所有白名单----------------------------------------------------------------------------------------------
			if(whiteDelAll){
				type5(obdSn, wdaSetting);
			}
			
			//删除所有黑名单----------------------------------------------------------------------------------------------
			if(blackDelAll){
				type8(obdSn, bdaSetting);
			}
			
			//删除单条白名单-----------------------------------------------------------------------------------------------
			if(whiteSingleDel.size()>0){
				type4(obdSn, wss);
			}
			
			//删除单条黑名单--------------------------------------------------------------------------------------------
			if(blackSingleDel.size()>0){
				type7(obdSn, bss);
			}
			
			//增加多个白名单--------------------------------------------------------------------------------------
			//增加白名单每次最多只能下发5个域名
			if(!StringUtils.isEmpty(whiteSet.toString())){
				type3(obdSn, whiteSet, whiteMap);
			}
			
			//增加多个黑名单---------------------------------------------------------------------------------------------
			if(!StringUtils.isEmpty(blackSet.toString())){
				type6(obdSn, blackSet, blackMap);
			}
			
			
		}catch(Exception e){
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置设置异常---------"+e);
		}
	
	}
	
	
	private void type0(String obdSn,String whiteSwitch,ObdSetting wsSetting){
		try {
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:白开关----------"+obdSn);
			//白开关
			Map<ExtensionDataSetType,String> extensionDataSetTypes = new HashMap<ExtensionDataSetType, String>();//拼接报文
			extensionDataSetTypes.put(ExtensionDataSetType.DomainWhiteSwitch, whiteSwitch);
			String setResult=serverSettingService.extensionDataSetting(obdSn, extensionDataSetTypes);
			//下发设置报文
			boolean obdSetRes = GlobalData.isSendResultSuccess(setResult);
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:白开关----------"+obdSn+"---白名单开关设置结果:"+obdSetRes);
			//保存domainSet记录
			if(obdSetRes){
				boolean keepFlag=settingSave(wsSetting);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:白开关----------"+obdSn+"---白名单开关设置记录更新结果:"+keepFlag);
				DomainState domainState = domainStateService.queryByObdSn(obdSn);
				domainState=dsNull(obdSn, domainState);
				boolean dsflag = domainStateSave(domainState, whiteSwitch, null, null, null, null);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:白开关----------"+obdSn+"---域白名单开关设置修改结果:"+dsflag);
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:白开关----------"+obdSn+"---异常信息:"+e);
		}
	}
	
	private void type1(String obdSn,String blackSwitch,ObdSetting bsSetting){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:黑开关----------"+obdSn);
		try {
			Map<ExtensionDataSetType,String> extensionDataSetTypes = new HashMap<ExtensionDataSetType, String>();//拼接报文
			extensionDataSetTypes.put(ExtensionDataSetType.DomainBlackSwitch, blackSwitch);
			String setResult=serverSettingService.extensionDataSetting(obdSn, extensionDataSetTypes);
			//下发设置报文
			boolean obdSetRes = GlobalData.isSendResultSuccess(setResult);
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:黑开关----------"+obdSn+"---设置结果:"+obdSetRes);
			//保存domainSet记录
			if(obdSetRes){
				boolean keepFlag=settingSave(bsSetting);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:黑开关----------"+obdSn+"---设置记录更新结果:"+keepFlag);
				DomainState domainState = domainStateService.queryByObdSn(obdSn);
				domainState=dsNull(obdSn, domainState);
				boolean dsflag = domainStateSave(domainState, null, blackSwitch, null, null, null);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:黑开关----------"+obdSn+"---设置修改结果:"+dsflag);
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:黑开关----------"+obdSn+"---异常信息:"+e);
		}
	}
	
	private void type3(String obdSn,StringBuffer whiteSet,Map<String,ObdSetting> whiteMap){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---"+obdSn);
		try {
			String whiteSetStr = whiteSet.toString();
			whiteSetStr=StrUtil.stringCutLastSub(whiteSetStr, ";");
			List<String> domainList = stringToList(whiteSetStr);
			boolean flag = false;//是否需要同步,如果下发的过程中存在失败，就需要同步
			
			if(domainList!=null && domainList.size()>0){
				for (String string : domainList) {
					String[] domainArray = string.split(";");
					//下发最多5个域名
					boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainAddWhite, string);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个白名单----------"+obdSn+"---下发设置结果:"+obdSetRes);
					//如果成功，将这5个记录置为无效，否则标识同步
					if(obdSetRes){
						DomainState domainState = domainStateService.queryByObdSn(obdSn);
						domainState=dsNull(obdSn, domainState);
						String whiteList = domainState.getWhiteList();
						JSONObject jobj3 =getJsonObject(whiteList);
						for (String str : domainArray) {
							jobj3.put(str, str);
							ObdSetting os = whiteMap.get(str);
							if(os!=null){
								boolean keepFlag=settingSave(os);
								serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个白名单----------"+obdSn+"---离线记录置为无效结果:"+keepFlag);
							}
						}
						//更新domainState表
						String white=jobj3.toString();
						boolean dsflag = domainStateSave(domainState, null, null, white, null, null);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个白名单----------"+obdSn+"---设置修改结果:"+dsflag);
					}else{
						//在线设置失败,需要同步
						flag = true;
						break;
					}
					
				}
			}
			
			if(flag){
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个白名单---------"+obdSn+"---失败,需要同步obd.");
				int whiteSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_00.getValue());
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个白名单---------"+obdSn+"---将之前相关类型设置置为无效总数:"+whiteSyntotal);
				//在线设置失败,需要同步
				ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_00.getValue(), null);
				ost.setValid("1");
				boolean ff = obdSettingService.obdSettingSave(ost);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个白名单----------" + obdSn + "---在线设置失败,保存白名单同步的离线记录结果:"+ff);
			}
			
		
			
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加白个黑名单---"+obdSn+"---异常信息:"+e);
		}
	
	}
	
	private void type6(String obdSn,StringBuffer blackSet,Map<String,ObdSetting> blackMap){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---"+obdSn);
		try {
			String blackSetStr = blackSet.toString();
			blackSetStr = StrUtil.stringCutLastSub(blackSetStr, ";");
			List<String> domainList = stringToList(blackSetStr);
			boolean flag = false;//是否需要同步,如果下发的过程中存在失败，就需要同步
			if(domainList!=null && domainList.size()>0){
				for (String string : domainList) {
					String[] domainArray = string.split(";");
					//下发最多5个域名
					boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainAddBlack, string);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---"+obdSn+"---下发设置结果:"+obdSetRes);
					//如果成功，将这5个记录置为无效，否则标识同步
					if(obdSetRes){
						DomainState domainState = domainStateService.queryByObdSn(obdSn);
						domainState=dsNull(obdSn, domainState);
						String blackList = domainState.getBlackList();
						JSONObject jobj6 =getJsonObject(blackList);
						for (String str : domainArray) {
							jobj6.put(str, str);
							ObdSetting os = blackMap.get(str);
							if(os!=null){
								boolean keepFlag=settingSave(os);
								serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---"+obdSn+"---离线记录更新结果:"+keepFlag);
							}
						}
						//更新domainState表
						String black =jobj6.toString();
						boolean dsflag = domainStateSave(domainState, null, null, null, black, null);
						serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---"+obdSn+"---域名单修改结果:"+dsflag);
					}else{
						//一旦有失败的马上同步
						flag = true;
						break;
					}
					
				}
			}
			
			if(flag){
				//失败则同步
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---------"+obdSn+"---失败,需要同步obd.");
				int blackSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_01.getValue());
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---------"+obdSn+"---将之前相关类型设置置为无效总数:"+blackSyntotal);
				//在线设置失败,需要同步
				ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_01.getValue(), null);
				ost.setValid("1");
				boolean ff = obdSettingService.obdSettingSave(ost);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单----------" + obdSn + "---在线设置失败,保存黑名单同步的离线记录结果:"+ff);
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:增加多个黑名单---"+obdSn+"--异常:"+e);
		}
	
		
	}
	
	private void type4(String obdSn,List<ObdSetting> wss){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn);
		try {
			for (ObdSetting obdSetting : wss) {
				JSONObject json = JSONObject.fromObject(obdSetting.getSettingMsg());
				String dn =  json.getString("domainName");
				
				boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, dn);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn+"---下发结果:"+obdSetRes+"---域名:"+dn);
				
				if(obdSetRes){
					boolean keepFlag=settingSave(obdSetting);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn+"---离线记录更新结果:"+keepFlag);
					//
					DomainState domainState = domainStateService.queryByObdSn(obdSn);
					domainState=dsNull(obdSn, domainState);
					String wList = domainState.getWhiteList();
					JSONObject jobj4 =getJsonObject(wList);
					jobj4.remove(dn);
					
					String white=jobj4.toString();
					boolean dsflag = domainStateSave(domainState, null, null, white, null, null);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn+"---域名单修改结果:"+dsflag);
				}else{
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn+"---失败,需要同步obd.");
					int whiteSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_00.getValue());
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn+"---将之前相关类型设置置为无效总数:"+whiteSyntotal);
					//在线设置失败,需要同步
					ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_00.getValue(), null);
					ost.setValid("1");
					boolean ff = obdSettingService.obdSettingSave(ost);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---" + obdSn + "---在线设置失败,保存白名单同步的离线记录结果:"+ff);
				}
				
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条白名单---"+obdSn+"---异常："+e);
		}
		
	
	}
	
	private void type7(String obdSn,List<ObdSetting> bss){
		serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn);
		try {
			for (ObdSetting obdSetting : bss) {
				JSONObject json = JSONObject.fromObject(obdSetting.getSettingMsg());
				String dn =  json.getString("domainName");
				
				boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, dn);
				serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn+"---下发结果:"+obdSetRes+"---域名:"+dn);
				if(obdSetRes){
					boolean keepFlag=settingSave(obdSetting);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn+"---离线记录更新结果:"+keepFlag);
					
					DomainState domainState = domainStateService.queryByObdSn(obdSn);
					domainState=dsNull(obdSn, domainState);
					String bList = domainState.getBlackList();
					JSONObject jobj7=getJsonObject(bList);
					jobj7.remove(dn);
					
					String black =jobj7.toString();
					boolean dsflag = domainStateSave(domainState, null, null, null, black, null);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn+"---域名单表修改结果:"+dsflag);
				}else{
					//失败则同步
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn+"---失败,需要同步obd.");
					int blackSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_01.getValue());
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn+"---将之前相关类型设置置为无效总数:"+blackSyntotal);
					//在线设置失败,需要同步
					ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_01.getValue(), null);
					ost.setValid("1");
					boolean ff = obdSettingService.obdSettingSave(ost);
					serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---" + obdSn + "---在线设置失败,保存黑名单同步的离线记录结果:"+ff);
				}
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【未完成的下发操作进行下发】域黑白名单设置:删除单条黑名单---"+obdSn+"---异常信息："+e);
		}
		
	
	}
	
	private void type5(String obdSn,ObdSetting wdaSetting){
		serverSendObdLogger.info("----------【域黑白名单设置】删除所有白名单----------"+obdSn);
		try {
			boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, "00");
			serverSendObdLogger.info("----------【域黑白名单设置】删除所有白名单----------"+obdSn+"---删除所有白名单结果:"+obdSetRes);
			wdaSetting.setUpdateTime(new Date());
			if(obdSetRes){
				boolean keepFlag=settingSave(wdaSetting);
				serverSendObdLogger.info("----------【域黑白名单设置】删除所有白名单----------"+obdSn+"---删除所有白名单结果:"+keepFlag);
				DomainState domainState = domainStateService.queryByObdSn(obdSn);
				domainState=dsNull(obdSn, domainState);
				String white=new JSONObject().toString();
				boolean dsflag = domainStateSave(domainState, null, null, white, null, null);
				serverSendObdLogger.info("----------【域黑白名单设置】删除所有白名单----------"+obdSn+"---删除所有白名单修改结果:"+dsflag);
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【域黑白名单设置】删除所有白名单----------"+obdSn+"---异常:"+e);
		}
	}
	
	private void type8(String obdSn,ObdSetting bdaSetting){
		serverSendObdLogger.info("----------【域黑白名单设置】删除所有黑名单----------"+obdSn);
		try {
			//下发报文
			boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, "00");
			serverSendObdLogger.info("----------【域黑白名单设置】删除所有黑名单----------"+obdSn+"---下发结果:"+obdSetRes);
			bdaSetting.setUpdateTime(new Date());
			if(obdSetRes){
				boolean keepFlag=settingSave(bdaSetting);
				serverSendObdLogger.info("----------【域黑白名单设置】删除所有黑名单----------"+obdSn+"---离线记录更新结果:"+keepFlag);
				
				DomainState domainState = domainStateService.queryByObdSn(obdSn);
				domainState=dsNull(obdSn, domainState);
				String black =new JSONObject().toString();
				boolean dsflag = domainStateSave(domainState, null, null, null, black, null);
				serverSendObdLogger.info("----------【域黑白名单设置】删除所有黑名单----------"+obdSn+"---域名单更新结果:"+dsflag);
			}
		} catch (OBDException e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【域黑白名单设置】删除所有白名单----------"+obdSn+"---异常:"+e);
		}
	}
	
	private JSONObject getJsonObject(String jsonStr){
		try {
			JSONObject jobj = null;
			if(!StringUtils.isEmpty(jsonStr)){
				jobj = JSONObject.fromObject(jsonStr);
			}else{
				jobj = new JSONObject();
			}
			return jobj;
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
	
	/**
	 * String str ="1;2;3;4;5;6;7;8;9";
	 * @return 1;2;3;4;5和6;7;8;9的集合
	 */
	private List<String> stringToList(String str) {
		String[] strArr = str.split(";");
		List<String> strList = new ArrayList<>();
		if(strArr.length<5){
			strList.add(str);
			return strList;
		}
		int temp = 1;
		String strTemp = "";
		for (int i=0; i< strArr.length; i++) {
			strTemp+=strArr[i]+";";
			if(temp % 5 == 0){
				strTemp = StrUtil.stringCutLastSub(strTemp, ";");
				strList.add(strTemp);
				strTemp="";
			}
			if(temp == strArr.length){
				strTemp = StrUtil.stringCutLastSub(strTemp, ";");
				strList.add(strTemp);
				break;
			}
			temp ++;
		}
		return strList;
	}
	
	
	/**
	 * 通用的域名单设置
	 * @param obdSn
	 * @param type
	 * @param domainWhite
	 * @return
	 * @throws OBDException
	 */
	private boolean domainSet(String obdSn,ExtensionDataSetType type,String msg) throws OBDException{
		Map<ExtensionDataSetType, String> extensionDataSetTypes = new HashMap<ExtensionDataSetType, String>();
		extensionDataSetTypes.put(type, msg);
		String setResult = serverSettingService.extensionDataSetting(obdSn, extensionDataSetTypes);
		// 下发设置报文
		boolean obdSetRes = GlobalData.isSendResultSuccess(setResult);
		return obdSetRes;
	}
	
	private boolean settingSave(ObdSetting obdSetting){
		obdSetting.setValid("0");
		obdSetting.setUpdateTime(new Date());
		boolean flag=obdSettingService.obdSettingSave(obdSetting);
		return flag;
	}
	
	private boolean domainStateSave(DomainState domainState,String whiteSwitch,String blackSwitch,String whiteList,String blackList,String createDate){
		if(domainState==null){
			return false;
		}
		if(!StringUtils.isEmpty(whiteSwitch)){
			domainState.setWhiteSwitch(whiteSwitch);
		}
		
		if(!StringUtils.isEmpty(blackSwitch)){
			domainState.setBlackSwitch(blackSwitch);
		}
		
		if(!StringUtils.isEmpty(whiteList)){
			domainState.setWhiteList(whiteList);
		}
		
		if(!StringUtils.isEmpty(blackList)){
			domainState.setBlackList(blackList);
		}
		
		if(!StringUtils.isEmpty(createDate)){
			domainState.setCreateTime(new Date());
		}
		
		domainState.setUpdateTime(new Date());
		boolean dsflag =domainStateService.domainStateSave(domainState);
		return dsflag;
	}
	
	private DomainState dsNull(String obdSn,DomainState domainState){
		if(domainState!=null){
			return domainState;
		}else{
			JSONObject json=new JSONObject();
			domainState = new DomainState();
			domainState.setId(IDUtil.createID());
			domainState.setObdSn(obdSn);
			domainState.setWhiteList(json.toString());
			domainState.setBlackList(json.toString());
			domainState.setCreateTime(new Date());
			return domainState;
		}
	}
	
	private ObdSetting settingNew(String obdSn,String type,String msg){
		ObdSetting os = new ObdSetting();
		os.setId(IDUtil.createID());
		os.setObdSn(obdSn);
		os.setType(type);
		os.setCreateTime(new Date());
		os.setSettingMsg(msg);
		return os;
	}
	
	private Integer settingUnuseful(String obdSn,String...type){
		Map<String, Integer> types = new HashMap<>();
		for (String string : type) {
			types.put(string, 1);
		}
		int total=obdSettingService.setNoValidByInType(obdSn, types);
		return total;
	}
}
