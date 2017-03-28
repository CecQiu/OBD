package com.hgsoft.carowner.service;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.ObdCurVersion;
import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.StrUtil;

/**
 * @author liujialin
 * 10.	请求远程升级
 */
@Service
public class OBDVersionCheckService extends BaseService<ObdVersion> {
	private final Log logger = LogFactory.getLog(OBDVersionCheckService.class);

	@Resource
	private DictionaryService  dictionaryService;
	@Resource
	private ObdVersionService obdVersionService;
	@Resource
	private UpdateDataService updateDataService;
	@Resource
	private RemoteUpgradeService remoteUpgradeService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ObdCurVersionService obdCurVersionService;
	
	public boolean obdVersionCheck(OBDMessage om){
		boolean flag=true;
		//1.解析报文，获取obd当前设备号
		String msg = om.getMsgBody();
		String obdSn=om.getId();
		
		int[] fileLens = {8, 8, 8};
		String[] files = new String[fileLens.length];
		int totalLen = 0;
		for(int i=0; i<fileLens.length; i++) {
			int len = fileLens[i];
			files[i] = msg.substring(totalLen, totalLen + len);
			totalLen += len;
		}
		String hardware = files[0];//硬件版本
		String iap = files[1];//IAP版本
		String software = files[2];//应用软件版本
		String hardwareStr=StrUtil.hexStrToASC2(hardware);//硬件版本号
		String iapStr=StrUtil.hexStrToASC2(iap);//IAP版本号
		String osoftwareStr=StrUtil.hexStrToASC2(software);
		logger.info("硬件版本:"+hardwareStr+"***IAP版本:"+iapStr+"***应用软件版本:"+osoftwareStr+"***********obd设备号:"+obdSn);
		//2.查询obd最新的版本号，如果设备号不匹配，则远程升级申请
		String versionNew=updateDataService.getLatestVersionNum();
		//不让远程升级.
		if(!osoftwareStr.equals(versionNew)){
			try {
				logger.info(obdSn+"****当前版本号:"+osoftwareStr+"***最新版本号:"+versionNew);
//				flag = remoteUpgradeService.remoteUpgrade(obdSn, versionNew);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
			logger.info(obdSn+"***********************800d远程升级结果:"+flag);
		}
		//查询设备当前版本
		ObdCurVersion obdCurVersion=obdCurVersionService.queryByObdSn(obdSn);
		if(obdCurVersion!=null){
			String version=obdCurVersion.getOsoftware();
			//如果设备号不一致，保存入库
			if(!osoftwareStr.equals(version)){
				obdCurVersion.setObdSn(obdSn);
				obdCurVersion.setOsoftware(osoftwareStr);//设备号
				obdCurVersion.setIap(iapStr);//iap版本号
				obdCurVersion.setHardware(hardwareStr);//硬件版本号
				obdCurVersion.setUpdateTime(new Date());
				obdCurVersionService.obdCurVersionSave(obdCurVersion);
			}
		}else{
			obdCurVersion = new ObdCurVersion();
			obdCurVersion.setObdSn(obdSn);
			obdCurVersion.setOsoftware(osoftwareStr);//设备号
			obdCurVersion.setIap(iapStr);//iap版本号
			obdCurVersion.setHardware(hardwareStr);//硬件版本号
			obdCurVersion.setCreateTime(new Date());
			obdCurVersionService.obdCurVersionSave(obdCurVersion);
		}
		
		return flag;
	}
}
