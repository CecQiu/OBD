/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.CarTestDao;
import com.hgsoft.carowner.entity.CarTest;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * 车辆体检数据
 * @author liujialin
 *
 */
@Service
public class CarTestService extends BaseService<CarTest> {
	private final Log logger = LogFactory.getLog(CarTestService.class);
	@Resource
	DictionaryService  dictionaryService;

	@Resource
	public void setDao(CarTestDao carTestDao){
		super.setDao(carTestDao);
	}
	
	public CarTestDao getCarTestDao() {
		return (CarTestDao) this.getDao();
	}
	
	public boolean carTestSave(OBDMessage om){
		String msgBody=om.getMsgBody();//消息体
		int msgLen=om.getMsgLen();//消息体长度
		String waterNo  =om.getWaterNo();//流水号
		String command=om.getCommand();//命令字
		String obdSn=om.getId();//消息ID，即是OBD或车辆 唯一标识符
		logger.info("消息ID:"+obdSn+"***命令字:"+command+"***流水号:"+waterNo+"***消息体长度:"+msgLen+"****消息体:"+msgBody);
		
		char[] statuschars = ByteUtil.hexStrToBinaryStr(msgBody).toCharArray();
		logger.info("*************************状态标识");
		System.out.println(statuschars);
		CarTest ct = new CarTest();
		ct.setEngine(statuschars[7]);//发动机系统
		ct.setPower(statuschars[6]);//电源电路
		ct.setThrottle(statuschars[5]);//节气门
		ct.setDischarge(statuschars[4]);//排放系统
		ct.setCooling(statuschars[3]);//冷却系统
		ct.setIdlingHigh(statuschars[2]);//怠速转速过高
		ct.setIdlingLow(statuschars[1]);//怠速转速过低
		ct.setBackup(statuschars[0]);//保留字段
		ct.setCreateTime(new Date());//创建时间
		ct.setObdSn(obdSn);//obd设备号
		ct.setId(IDUtil.createID());//主键ID
		ct.setValid('T');//是否有效
		this.getDao().save(ct);
		//如果用户有请求位置信息，返回位置信息对象
		//获取命令字8005
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.carTestQuery");
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		//如果该用户有这个请求，则将故障码放进Map去
		String mapId=om.getId()+"_"+dic.getTrueValue();
		logger.info(mapId+"**********************车辆体检数据id");
		if(respMap.containsKey(mapId)){
			respMap.put(mapId, ct);
		}
		logger.info("保留字段:"+ct.getBackup()+"***怠速转速过低:"+ct.getIdlingLow()+"***怠速转速过高:"+ct.getIdlingHigh()+"***冷却系统:"+ct.getCooling()+"***排放系统:"+ct.getDischarge()+"***节气门:"+ct.getThrottle()+"***电源电路:"+ct.getPower()+"***发动机系统:"+ct.getEngine());
		return true;
	}
	
}
