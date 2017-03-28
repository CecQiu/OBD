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
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.ObdBarrier;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.MsgThread;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 8002	参数设置
 */
@Service
public class ParamSetService{
	private final Log logger = LogFactory.getLog(ParamSetService.class);

//	/**命令字与命令任务的map对象*/
//	private static Map<String, String> map;
	
	@Resource
	private ObdBarrierService obdBarrierService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	@Resource
	DictionaryService  dictionaryService;
	
	/**
	 * @param obdSn  obdId
	 * @param areaNum  区域编号
	 * @param railAndAlert 围栏类型+报警方式：{11:圆形进区域报警;12:圆形出区域报警;13:圆形进出区域报警;14:取消圆形围栏;15:取消所有圆形围栏;
	 * 																	   01:矩形进区域报警;02:矩形出区域报警;03:矩形进出区域报警;04:取消矩形围栏;05:取消所有矩形围栏}
	 * @param maxLongitude  大经：116°04.000' 长度必须为8位
	 * @param maxLatitude   大纬：33°32.0000' 长度必须为8位
	 * @param minLongitude 小经:108°01.000' 长度必须为8位
	 * @param minLatitude  小纬:12°45.0000' 长度必须为8位
	 * @return Mcu2终端ACK:00成功接收，01接收错误,其它保留
	 */
	public String  paramSet(String obdSn,int areaNum,String railAndAlert,String maxLongitude,String maxLatitude,String minLongitude,String minLatitude){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.paramSet");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************8002流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		//拼接请求报文
		//参数总数
		String paramSum="01";
		//参数id,电子围栏：0x04
		String paramId="04";
		//参数长度，电子围栏18个byte
		String paramLen=StrUtil.strAppendByLen(Integer.toHexString(18),1,"0");
		//参数数据
		String areaNumStr=StrUtil.strAppendByLen(Integer.toHexString(areaNum),1,"0");//区域编号
//		String railAndAlertType=map.get(railAndAlert);//围栏类型+报警方式
		String railAndAlertType=railAndAlert;//围栏类型+报警方式
		String railAndAlertStr=ByteUtil.binaryStrToHexStr(railAndAlertType);//围栏类型+报警方式
		String maxLongitude1=maxLongitude.replaceAll("[°\\.']", "");//大经
		String maxLatitude1=maxLatitude.replaceAll("[°\\.']", "");//大纬
		String minLongitude1=minLongitude.replaceAll("[°\\.']", "");//小经
		String minLatitude1=minLatitude.replaceAll("[°\\.']", "");//小纬
		String paramStr=paramSum+paramId+paramLen+areaNumStr+railAndAlertStr+maxLongitude1+maxLatitude1+minLongitude1+minLatitude1;
		
		//向客户端发送请求消息
		try {
			msgSendUtil.msgSend(obdSn,common,serialNumber,paramStr);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
		MsgThread thread =new MsgThread(msgId);
		thread.start();
		try {
			//线程同步锁，顺序执行
			thread.join(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//获取到客户端的响应消息
		String resultStr= (String) thread.getResMsg();
		logger.info(resultStr+"*************************8002客户端响应结果:"+obdSn);
		if(resultStr!=null && "00".equals(resultStr)){
			//跟新电子围栏数据表，如果结果为：00成功接收，保存入库
			ObdBarrier obd=obdBarrierService.barrierExist(obdSn);//判断是否存在
			if(obd==null){
				obd = new ObdBarrier();
			}
			obd.setObdSn(obdSn);
			obd.setAreaNum(areaNum);//区域编号
			obd.setRailAndAlert(railAndAlert);//围栏类型+报警方式
			obd.setMaxLongitude(maxLongitude);//大经
			obd.setMaxLatitude(maxLatitude);//大纬
			obd.setMinLongitude(minLongitude);//小经
			obd.setMinLatitude(minLatitude);//小纬
			obd.setTime(new Date());
			obd.setValid(0);
			obdBarrierService.getDao().saveOrUpdate(obd);
			respMap.remove(msgId);//获取响应消息后，清除对应的消息
		}
		return resultStr;
	}
}
