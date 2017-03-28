package com.hgsoft.carowner.action;


import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.service.RemoteUpgradeService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.service.ServerSettingService;

/**
 * obd设备升级-升级文件上载和推送类
 * @author fdf
 */
@Controller
@Scope("prototype")
public class UpgradeAction extends BaseAction {
	private static Logger serverSendObdLogger = LogManager.getLogger("serverSendObdLogger");

	@Resource
	private RemoteUpgradeService remoteUpgradeService;
	
	@Resource
	private ServerSettingService serverSettingService;
	
	
	
	private String obdListStr;
	private String obdVersion;
	
	/**
	 * 展示obd设备升级的文件列表
	 */
	public void upgrade() {
		serverSendObdLogger.info("--------------【OBD设备升级下发】-----");
		if(StringUtils.isEmpty(obdListStr)){
			outJsonMessage("{\"status\":\"fail\",\"message\":\"远程升级推送失败\"}");
		}else{
			try {
				String[] obdSnArr = obdListStr.trim().split(",");
				for (String obdSn : obdSnArr) {
					String result = serverSettingService.deviceUpgradeSet(obdSn, "1", null, null);
					serverSendObdLogger.info("--------------【OBD设备升级下发】设备："+obdSn+"，结果："+result);
				}
				outJsonMessage("{\"status\":\"success\",\"message\":\"远程升级推送成功\"}");
				serverSendObdLogger.info("--------------【OBD设备升级下发】设备："+obdListStr);
			} catch (OBDException e) {
				e.printStackTrace();
				outJsonMessage("{\"status\":\"fail\",\"message\":\"远程升级推送失败\"}");
				serverSendObdLogger.error("--------------【OBD设备升级下发】失败："+e);
			}
//			boolean flag=remoteUpgradeService.remoteUpgradeAsk(obdSnArr, obdVersion.trim());
//			if(flag){
//				outJsonMessage("{\"status\":\"success\",\"message\":\"远程升级推送成功\"}");
//				serverSendObdLogger.info("--------------【OBD设备升级下发】设备："+obdListStr);
//			}else{
//				outJsonMessage("{\"status\":\"fail\",\"message\":\"远程升级推送失败\"}");
//				serverSendObdLogger.info("--------------【OBD设备升级下发】结果："+result);
//			}
		}
	}

	public String getObdListStr() {
		return obdListStr;
	}

	public void setObdListStr(String obdListStr) {
		this.obdListStr = obdListStr;
	}

	public String getObdVersion() {
		return obdVersion;
	}

	public void setObdVersion(String obdVersion) {
		this.obdVersion = obdVersion;
	}
	
}
