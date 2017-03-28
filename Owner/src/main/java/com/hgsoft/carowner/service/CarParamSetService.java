/**
 * 
 */
package com.hgsoft.carowner.service;

import java.text.DecimalFormat;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 8002	参数设置
 */
@Service
public class CarParamSetService{
	private final Log logger = LogFactory.getLog(CarParamSetService.class);
	
//	/**命令字与命令任务的map对象*/
//	private static Map<String, String> map;
	
	@Resource
	private ObdBarrierService obdBarrierService;
	@Resource
	private CarParamService carParamService;
//	@Resource
//	MsgSendUtil msgSendUtil;
//	//初始化map对象
//	static {
//		map = new HashMap<String, String>();
//		map.put("11", "10000001");//圆形进区域报警
//		map.put("12", "10000010");//圆形出区域报警
//		map.put("13", "10000011");//圆形进出区域报警
//		map.put("14", "10000100");//取消圆形围栏
//		map.put("15", "10000101");//取消所有圆形围栏
//		map.put("01", "00000001");//矩形进区域报警
//		map.put("02", "00000010");//矩形出区域报警
//		map.put("03", "00000011");//矩形进出区域报警
//		map.put("04", "00000100");//取消矩形围栏
//		map.put("05", "00000101");//取消所有矩形围栏
//	}

	@Resource
	DictionaryService  dictionaryService;
	/**
	 * 参数设置
	 * @param carParam 参数对象
	 * @return 终端ACK
	 */
	public String  paramSet(CarParam carParam){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		logger.info(carParam+"***************参数对象");
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.paramSet");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************8002流水号");
		//确定唯一消息
		String obdSn=carParam.getObdSn();
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		//拼接请求报文
		//参数总数
		int sum = 0;
		StringBuffer paramStr=new StringBuffer();//请求报文
		
		//0x00（电子围栏）(write only)

		String areaNum = carParam.getAreaNum();//区域编号
		String railAndAlert = carParam.getRailAndAlert();//围栏类型+报警方式
		String maxLongitude = carParam.getMaxLongitude();//大经
		String maxLatitude = carParam.getMaxLatitude();//大纬
		String minLongitude = carParam.getMinLongitude();//小经
		String minLatitude = carParam.getMinLatitude();//小纬
		//如果这些参数其中一个不为空，说明是电子围栏
		if(areaNum!=null || railAndAlert != null || maxLongitude != null || maxLatitude != null || minLongitude!=null || minLatitude!=null){
			sum++;//参数总数加1
			//0x00（电子围栏）
			String paramId="00";
			//参数长度，电子围栏18个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(18),1,"0");
			
			String areaNumStr=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(areaNum)),1,"0");//区域编号
//					String railAndAlertType=map.get(railAndAlert);//围栏类型+报警方式
			String railAndAlertType=railAndAlert;//围栏类型+报警方式
			String railAndAlertStr=ByteUtil.binaryStrToHexStr(railAndAlertType);//围栏类型+报警方式
			String maxLongitude1=maxLongitude.replaceAll("[°\\.']", "");//大经
			String maxLatitude1=maxLatitude.replaceAll("[°\\.']", "");//大纬
			String minLongitude1=minLongitude.replaceAll("[°\\.']", "");//小经
			String minLatitude1=minLatitude.replaceAll("[°\\.']", "");//小纬
			String param= paramId+paramLen+areaNumStr+railAndAlertStr+maxLongitude1+maxLatitude1+minLongitude1+minLatitude1;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0");
		
		//0x01（超速报警阈值）
		String overSpeed = carParam.getOverspeed();
		if(overSpeed!=null){
			sum++;
			//0x01（超速报警阈值）
			String paramId="01";
			//超速报警阈值1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String speed =StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(overSpeed)),1,"0");
			String param = paramId + paramLen + speed;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************1");
		
		//0x02（进入休眠模式的时间）
		String sleepTime = carParam.getSleepTime();
		if(sleepTime != null){
			sum++;
			//参数id:0x02
			String paramId="02";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			sleepTime = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(sleepTime)),1,"0");//转成16进制
			String param = paramId + paramLen + sleepTime;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************2");
		
		//0x03（ACC开时候上传、存储位置信息的间隔）
		String accTime = carParam.getAccTime();
		if(accTime != null){
			sum++;
			//参数id,0x03
			String paramId="03";
			//2个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(2),1,"0");
			//10进制转成16进制
			accTime = Integer.toHexString(Integer.parseInt(accTime));
			DecimalFormat d = new DecimalFormat("0000");//不够4位前面补0
			accTime = d.format(Long.parseLong(accTime));
			String param = paramId + paramLen + accTime;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************3");
		
		//0x04   (心跳时间间隔)没有休眠时候间隔 
		String heartTime = carParam.getHeartTime();
		if(heartTime != null){
			sum++;
			//参数id,0x04
			String paramId="04";
			//2个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(2),1,"0");
			heartTime = Integer.toHexString(Integer.parseInt(heartTime));
			DecimalFormat d = new DecimalFormat("0000");//不够4位前面补0
			heartTime = d.format(Long.parseLong(heartTime));
			String param = paramId + paramLen + heartTime;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************4");
		
		//0x05(位置信息策略)
		String position = carParam.getPosition();
		if(position != null){
			sum++;
			//参数id,0x05
			String paramId="05";
			//2个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + position;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************5");
		
		//0x06设防撤防
		String safety = carParam.getSafety();
		if(safety != null){
			sum++;
			//0x06设防撤防
			String paramId="06";
			//2个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + safety;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************6");
		
		//0x07(时区设置)
		String timeZone = carParam.getTimeZone();
		if(timeZone!=null){
			sum++;
			//0x07(时区设置)
			String paramId="07";
			//2个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(2),1,"0");
			String hour=timeZone.substring(0, 2);
			hour = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(hour)),1,"0");
			String minute= timeZone.substring(2,4);
			minute= StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(minute)),1,"0");
			String param = paramId + paramLen + hour + minute;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************7");
		
		//0x08(电压值，只读)
		
		//0x09(GSM 信号强度，只读)
		
		//0x0A欠压报警阈值:低于9.50v欠压报警,0.1v为单位，需传9.50
		String undervoltage = carParam.getUndervoltage();
		if(undervoltage!=null){
			sum++;
			//参数id,0x0a
			String paramId="0a";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			double uvd=Double.parseDouble(undervoltage);
			int uvi=(int)(uvd*10);
			undervoltage = StrUtil.strAppendByLen(Integer.toHexString(uvi),1,"0");
			String param = paramId + paramLen + undervoltage; 
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0a");
		
		//0x0B高压报警阈值
		String highVoltage = carParam.getHighVoltage();
		if(highVoltage!=null){
			sum++;
			//0x0B高压报警阈值
			String paramId="0b";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			double hvd=Double.parseDouble(highVoltage);
			int hvi=(int)(hvd*10);
			highVoltage = StrUtil.strAppendByLen(Integer.toHexString(hvi),1,"0");
			String param = paramId + paramLen + highVoltage;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0b");
		
		//0x0C疲劳驾驶时间阈值
		String tiredDrive = carParam.getTiredDrive();
		if(tiredDrive!=null){
			sum++;
			//0x0C疲劳驾驶时间阈值
			String paramId="0c";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			tiredDrive = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(tiredDrive)),1,"0");
			String param = paramId + paramLen + tiredDrive;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0c");
		
		//0x0D解除疲劳驾驶时间阈值:以10min 为一个单位
		String tiredDriveTime = carParam.getTiredDriveTime();
		if(tiredDriveTime!=null){
			sum++;
			//参数id,0x0D
			String paramId="0d";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			tiredDriveTime = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(tiredDriveTime)),1,"0");
			String param = paramId + paramLen + tiredDriveTime;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0d");
		
		//0x0E水温高报警阈值
		String waterTemp = carParam.getWaterTemp();
		if(waterTemp!=null){
			sum++;
			//参数id,0x0a
			String paramId="0e";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			waterTemp = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(waterTemp)),1,"0");
			String param = paramId + paramLen + waterTemp;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0e");
		
		//0x0F设置用户id
		String userId=carParam.getUserId();
		if(userId!=null){
			sum++;
			//参数id,0x0F
			String paramId="0f";
			//11个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(11),1,"0");
			String userIdLen=StrUtil.strAppendByLen(Integer.toHexString(userId.length()),1,"0");
			DecimalFormat d = new DecimalFormat("00000000000000000000");//不够20位前面补0
			userId = d.format(Long.parseLong(userId));
			String param = paramId + paramLen + userIdLen + userId;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************0f");
		
		//0x10(急刹车系数强度)
		String ebrake = carParam.getEbrake();
		if(ebrake!=null){
			sum++;
			//参数id,0x10
			String paramId="10";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			ebrake = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ebrake)),1,"0");
			String param = paramId + paramLen + ebrake;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************10");
		
		//0x11(急加速系数强度)
		String espeedup = carParam.getEspeedup();
		if(espeedup!=null){
			sum++;
			//参数id,0x11
			String paramId="11";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			espeedup = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(espeedup)),1,"0");
			String param = paramId + paramLen + espeedup;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************11");
		
		//0x12(碰撞报警系数强度)
		String crash = carParam.getCrash();
		if(crash!=null){
			sum++;
			//参数id,0x12
			String paramId="12";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			crash = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(crash)),1,"0");
			String param = paramId + paramLen + crash;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************12");
		
		//0x13(布防时震动报警系数强度)
		String shake = carParam.getShake();
		if(crash!=null){
			sum++;
			//参数id,0x13
			String paramId="13";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			shake = StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(shake)),1,"0");
			String param = paramId + paramLen + shake;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************13");

		//0x14(速度提醒开关配置)
		String speedRemind = carParam.getSpeedRemind();
		if(speedRemind!=null){
			sum++;
			//参数id,0x14
			String paramId="14";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + speedRemind;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************14");
		
		//0x15(IMEI只读)
		
		//0x16(obd数据汇报策略)
		String dataUploadType = carParam.getDataUploadType();
		if(dataUploadType!=null){
			sum++;
			//参数id,0x16
			String paramId="16";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + dataUploadType;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************16");
		
		//0x19(obd数据汇报间隔)
		String dataUploadTime = carParam.getDataUploadTime();
		if(dataUploadTime!=null){
			sum++;
			//参数id,0x19
			String paramId="19";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(2),1,"0");
			DecimalFormat d = new DecimalFormat("0000");//不够4位前面补0
			dataUploadTime = d.format(Long.parseLong(dataUploadTime));
			String param = paramId + paramLen + dataUploadTime;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************19");

		//0x17(上传附加位置信息1)
		String positionExtra = carParam.getPositionExtra();
		if(positionExtra!=null){
			sum++;
			//参数id,0x17
			String paramId="17";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + positionExtra;
			paramStr.append(param);
		}	
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************17");
		//0x18(基站定位时GSM基站信息内容）
		String GSMBTS = carParam.getGsmbts();
		if(GSMBTS!=null){
			sum++;
			//参数id,0x18
			String paramId="18";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + GSMBTS;
			paramStr.append(param);
		}	
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************18");

//		//0x1A(通信模块类型)
//		String communication = carParam.getCommunication();
//		if(communication!=null){
//			sum++;
//			//参数id,0x1A
//			String paramId="1a";
//			//1个byte
//			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
//			String param = paramId + paramLen + communication;
//			paramStr.append(param);
//		}	
//		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************1a");
//		
//		//0x1B(定位模块类型)
//		String positionType = carParam.getPositionType();
//		if(positionType!=null){
//			sum++;
//			//参数id,0x1B
//			String paramId="1b";
//			//1个byte
//			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
//			String param = paramId + paramLen + positionType;
//			paramStr.append(param);
//		}
//		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************1b");
		
		//0x1C(GPS模块开关)
		String GPS = carParam.getGps();
		if(GPS!=null){
			sum++;
			//参数id,0x1C
			String paramId="1c";
			//1个byte
			String paramLen=StrUtil.strAppendByLen(Integer.toHexString(1),1,"0");
			String param = paramId + paramLen + GPS;
			paramStr.append(param);
		}
		logger.info(paramStr.toString()+"***"+paramStr.toString().length()+"**********************1c");
		
		String msg =  StrUtil.strAppendByLen(Integer.toHexString(sum),1,"0") + paramStr.toString();
		//发送请求给obd以及线程获取obd返回结果.以防单例模式出现
		String resultStr=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, msg, msgId);
		logger.info(msgSendUtil+"*************是否单例模式");
		logger.info(resultStr+"*************************8002客户端响应结果");
		if(resultStr!=null && "00".equals(resultStr)){
			carParamService.carParamSaveOrUpdate(carParam);//保存参数设置
			respMap.remove(msgId);//获取响应消息后，清除对应的消息
		}
		return resultStr;
	}

}
