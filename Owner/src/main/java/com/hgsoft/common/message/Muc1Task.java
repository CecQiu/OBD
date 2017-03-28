/**
 * 
 */
package com.hgsoft.common.message;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.DateUtil;

/**
 * @author liujialin
 * @date 20150721
 * @desc Mcu1终端心跳
 */
@Service
public class Muc1Task {
	private final Log logger = LogFactory.getLog(MessageHandel.class);
	@Resource
	private OBDStockInfoService obdStockInfoService;
	/**
	 * 终端心跳逻辑处理方法
	 * @param om 请求报文解析后实体对象：消息ID,流水号,消息长度,消息体内容,异或校验码
	 * @return 0x00成功接收，0x01接收错误,其它保留
	 * @throws Exception
	 */
	public void entrance(OBDMessage om) {
		String obdSn = om.getId();
		//
		OBDStockInfo os=obdStockInfoService.queryBySN(obdSn);
		if(os!=null){
			if(os.getStockState().equals("00")){
				os.setStockState("01");//在线
				boolean flag=obdStockInfoService.obdStateUpdate(os);
				logger.info(obdSn+"**********心脏包上线结果:"+flag);
			}
		}
		logger.info(obdSn+"*********心跳包,车辆在线时间:"+(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
}
