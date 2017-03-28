package com.hgsoft.common.message;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.ObdCurVersion;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdCurVersionService;
import com.hgsoft.carowner.service.ObdVersionService;
import com.hgsoft.carowner.service.UpdateDataService;
import com.hgsoft.common.message.OBDMessage;
/**
 * 10.	Mcu10远程升级结果
 * @author liujialin
 *
 */
@Service
public class Muc10Task {
	private final Log logger = LogFactory.getLog(Muc10Task.class);
	@Resource
	ObdVersionService obdVersionService;
	@Resource
	UpdateDataService updateDataService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	@Resource
	private ObdCurVersionService obdCurVersionService;
	
	/**
	 * 程序入库
	 * @param om 请求消息对象
	 * @return  服务器ACK
	 * @throws Exception
	 */
	public void entrance(OBDMessage om) {
		String obdSn = om.getId();
		try {
			//消息体
			String flag = om.getMsgBody();//成功标志
			ObdVersion obdVersion = new ObdVersion();
			//如果更新成功，保存最新版本信息,无论成功还是失败都保存当前最新版本号
			ObdUpgrade ou=updateDataService.getObdUpgradeLatest();
			if(ou==null){
				return;
			}
			obdVersion.setVersion(ou.getVersion());
			obdVersion.setObdSn(obdSn);
			obdVersion.setUpdateFlag(flag);//更新结果
			obdVersion.setCreateTime(new Date());
			obdVersion.setUpdateTime(new Date());
			boolean mark=obdVersionService.obdVersionSave(obdVersion);//保存obd版本信息
			logger.info(obdSn+"*******远程升级结果:"+mark);
			//如果更新成功,更新最新版本
			if("00".equals(flag)){
				//查询设备当前版本
				ObdCurVersion obdCurVersion=obdCurVersionService.queryByObdSn(obdSn);
				if(obdCurVersion!=null){
					//如果设备号不一致，保存入库
					obdCurVersion.setSoftware(ou.getVersion());//设备版本号
					obdCurVersion.setUpdateTime(new Date());
					obdCurVersionService.obdCurVersionSave(obdCurVersion);
				}else{
					obdCurVersion = new ObdCurVersion();
					obdCurVersion.setObdSn(obdSn);
					obdCurVersion.setSoftware(ou.getVersion());//设备号
					obdCurVersion.setCreateTime(new Date());
					obdCurVersionService.obdCurVersionSave(obdCurVersion);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(obdSn+"*******远程升级结果失败.");
		}
	}
}
