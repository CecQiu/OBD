/**
 * 
 */
package com.hgsoft.common.message;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.utils.DateUtil;

/**
 * 保存终端设备最新ip和端口
 * @author liujialin
 *
 */
@Component
public class ObdStateService{
	private final Log logger = LogFactory.getLog(ObdStateService.class);
	private static ObdStateService obdStateService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	
	@PostConstruct
	public void init() {
		obdStateService = this;
	}

	private ObdStateService() {
	}

	public static ObdStateService instance() {
		if (obdStateService == null) {
			obdStateService = new ObdStateService();
		}

		return obdStateService;
	}
	
	/**
	 * 更新obd在线状态.
	 * @param obdId
	 * @return
	 */
	public boolean obdStateChange(String obdSn){
		boolean flag=true;
		OBDStockInfo os=obdStockInfoService.queryBySN(obdSn);
		if(os!=null){
			os.setStockState("00");//离线
			flag=obdStockInfoService.obdStateUpdate(os);
		}else{
			flag=false;
		}
		logger.info(obdSn+"*********心跳包,车辆离线时间:"+(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return flag;
	}
}
