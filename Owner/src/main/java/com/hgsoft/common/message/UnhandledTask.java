/**
 * 
 */
package com.hgsoft.common.message;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.GpsSet;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.carowner.entity.ServerSet;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.carowner.entity.WifiSet;
import com.hgsoft.carowner.service.CarParamSetService;
import com.hgsoft.carowner.service.GpsStateSetService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ProtalService;
import com.hgsoft.carowner.service.RemoteUpgradeService;
import com.hgsoft.carowner.service.ServerSetService;
import com.hgsoft.carowner.service.UpdateDataService;
import com.hgsoft.carowner.service.UpgradeSetService;
import com.hgsoft.carowner.service.WiFiSetService;
import com.hgsoft.carowner.service.WifiStateSetService;

/**
 * @author liujialin
 * @date 20150721
 * @desc Mcu1终端心跳
 */
@Service("unhandledTask")
public class UnhandledTask {
	private final Log logger = LogFactory.getLog(UnhandledTask.class);
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private GpsStateSetService gpsStateSetService;
	@Resource
	private WifiStateSetService wifiStateSetService;
	@Resource
	private WiFiSetService wiFiSetService;
	@Resource
	private CarParamSetService carParamSetService;// 车辆参数设置
	@Resource
	private ProtalService protalService;// protal设置
	@Resource
	private ServerSetService serverSetService;// 配置服务器
	@Resource
	private UpgradeSetService upgradeSetService;// 未注册的设备远程升级
	@Resource
	private UpdateDataService updateDataService;//
	@Resource
	private RemoteUpgradeService remoteUpgradeService;//远程升级请求
	
	/**
	 * 终端心跳逻辑处理方法
	 * @param om 请求报文解析后实体对象：消息ID,流水号,消息长度,消息体内容,异或校验码
	 * @return 0x00成功接收，0x01接收错误,其它保留
	 * @throws Exception
	 */
	public void entrance(String obdSn) {
//		String obdSn = om.getId();
//		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//		//判断是否存在未完成的gpsSet操作
//		GpsSet gpsSet=gpsStateSetService.queryByObdSn(obdSn);
//		if(gpsSet!=null){
//			gpsSet(gpsSet);//gps设置
//		}
//		//判断是否存在未完成的wifiSet操作
//		WifiSet wifiSet=wifiStateSetService.queryWifStateByObdSn(obdSn, "4");
//		if(wifiSet!=null){
//			wifSet(wifiSet);//wifi开关设置
//		}
//		//判断是否存在未完成的portal操作
//		List<Portal> plist= protalService.queryListByObdSn(obdSn);
//		if(plist!=null && plist.size()>0){
//			portalSet(plist);//portal设置
//		}
		//判断是否有未完成的配置服务器
		ServerSet serverSet=serverSetService.queryByObdSnAndValid(obdSn, "1");
		if(serverSet!=null){
			serverSet(serverSet);//gps设置
		}
//		UpgradeSet upgradeSet=upgradeSetService.queryByObdSn(obdSn);
//		if(upgradeSet!=null){
//			upgradeSet(upgradeSet);//gps设置
//		}
//		logger.info(obdSn+"*********gps开关设置和WiFi开关设置和portal设置和配置服务器设置未完成的设置");
	}
	
	public void gpsSet(GpsSet gpsSet){
		String obdSn = gpsSet.getObdSn();
		logger.info(obdSn+"*************gps设置");
		try {
			if(gpsSet!=null){
				String obdGpsState ="0"+gpsSet.getGpsState();//前面补0
				//如果设备在线,向obd发送指令
				CarParam carParam = new CarParam();
				carParam.setObdSn(obdSn.toLowerCase());// obd设备号均是小写
				carParam.setGps(obdGpsState);//设置obd开关状态 
				String mark = carParamSetService.paramSet(carParam);//发送指令给obd
				//如果设置成功.
				if(mark!=null && "00".equals(mark)){
					//设置成功,
					gpsSet.setUpdateTime(new Date());
					gpsSet.setValid("0");//是否有效
					gpsStateSetService.gpsSetUpdate(gpsSet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(obdSn+"**************gps设置异常");
		}
	}
	
	public void wifSet(WifiSet wifiSet){
		String obdSn = wifiSet.getObdSn();
		logger.info(obdSn+"*************wifi设置");
		try {
			if(wifiSet!=null){
				Wifi wifi = new Wifi();
				wifi.setObdSn(obdSn.toLowerCase());
				wifi.setWifiState(wifiSet.getWifiState());//wifi状态
				String resultStr = wiFiSetService.WiFiSet(wifiSet.getWifiState(), wifi);
				if(resultStr!=null && "00".equals(resultStr)){
					wifiSet.setValid("0");//设置成功
					wifiSet.setUpdateTime(new Date());
					wifiStateSetService.wifiSetSave(wifiSet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(obdSn+"*****wifi设置未完成操作异常.");
		}
	}
	
	public void portalSet(List<Portal> plist){
		logger.info("*************Portal设置");
		try {
//			List<Portal> plist= protalService.queryListByObdSn(obdSn);
			if(plist!=null && plist.size()>0){
				for (Portal portal : plist) {
					boolean flag=protalService.protalSet(portal);//下发设置参数
					//如果设置成功
					if(flag){
						portal.setValid("0");
						portal.setUpdateTime(new Date());
						protalService.portalSaveOrUpdate(portal);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*****wifi设置未完成操作报错.");
		}
	}
	
	public void serverSet(ServerSet serverSet){
		String obdSn = serverSet.getObdSn();
		logger.info(obdSn+"*************配置服务器设置");
		try {
			if(serverSet!=null){
				boolean flag=serverSetService.serverSet(serverSet);
				logger.info(serverSet.getObdSn()+"******未完成的配置服务器结果:"+flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(obdSn+"******未完成的配置服务器异常");
		}
	}
	public void upgradeSet(UpgradeSet upgradeSet){
		String obdSn = upgradeSet.getObdSn();
		logger.info("*************未注册设备进行远程升级");
		//1查询最新版本号
		//2.查询obd最新的版本号，如果设备号不匹配，则远程升级申请
		String versionNew=updateDataService.getLatestVersionNum();
		//如果不存在最新的版本号
		if(StringUtils.isEmpty(versionNew)){
			return;
		}
		try {
			if(upgradeSet!=null){
				boolean flag = remoteUpgradeService.remoteUpgrade(obdSn.toLowerCase(), versionNew);
				//不管是否下发成功.更新
//				if(flag){
					upgradeSet.setValid("0");
					upgradeSet.setUpdateTime(new Date());
					upgradeSetService.upgradeSetSave(upgradeSet);
//				}
				logger.info(obdSn+"******未注册设备进行远程升级:"+flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(obdSn+"******未注册设备进行远程升级异常");
		}
	}
}
