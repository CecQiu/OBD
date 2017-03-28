/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.CarParamDao;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 0003	Mcu3参数查询应答
 */
@Service
public class CarParamQueryAnswerService extends BaseService<CarParam> {
	private static Log logger = LogFactory.getLog(CarParamQueryAnswerService.class);
	@Resource
	private FaultCodeService faultCodeService;
	@Resource
	private DictionaryService  dictionaryService;
	
	@Resource
	private CarParamService carParamService;

	@Resource
	public void setDao(CarParamDao carParamDao){
		super.setDao(carParamDao);
	}
	
	public CarParamDao getCarParamDao() {
		return (CarParamDao) this.getDao();
	}
	public boolean carParamQueryAnswer(OBDMessage om){
		try {
			logger.info(om+"*************************参数设置");
			String msgBody=om.getMsgBody();//消息体
			int msgLen=om.getMsgLen();//消息体长度
			String waterNo  =om.getWaterNo();//流水号
			String command=om.getCommand();//命令字
			String obdSn=om.getId();//消息ID，即是OBD或车辆 唯一标识符
			logger.info(obdSn+"***命令字:"+command+"***流水号:"+waterNo+"***消息长度:"+msgLen+"***消息体:"+msgBody);
			int paramSum=Integer.valueOf(Integer.parseInt(msgBody.substring(0,2), 16));//应答参数总数
			logger.info(paramSum+"**************应答参数总数");
			int sum=0;//根据参数判断
			CarParam carParam = new CarParam();
			//如果故障码个数为0，则不插入数据
			if(paramSum>0){
				int byteNum=1;//参数列表开始下标,前面的byte总数
				do{
					sum++;//参数总数加一
					String id = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), 1);//参数ID
					byteNum = byteNum + 1;//前面的byte总数
					String paramLen = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), 1);//参数长度
					int len = Integer.valueOf(paramLen, 16);//十六进制转十进制
					byteNum = byteNum + 1;//前面的byte总数
					String paramMsg = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), len);//参数长度
					byteNum = byteNum + len;//前面的byte总数
					Map<String,String> map = new HashMap<String, String>();
					map.put(id, paramMsg);
					carParam=carParamSet(map,carParam);//解析报文
				}while(sum<paramSum);
				
				carParam.setObdSn(obdSn);
				carParam.setCreateTime(new Date());//插入时间
				carParam.setValid('T');
				logger.info("区域编号:"+carParam.getAreaNum()+"***围栏类型:"+carParam.getRailAndAlert()+"***大经:"+carParam.getMaxLongitude()+
						"***大纬:"+carParam.getMaxLatitude()+"***小经:"+carParam.getMinLongitude()+"***小纬:"+carParam.getMinLatitude()+
						"***超速报警阈值:"+carParam.getOverspeed()+"***进入休眠模式的时间:"+carParam.getSleepTime()+"***ACC:"+carParam.getAccTime()+
						"***心跳时间:"+carParam.getHeartTime()+"***GPS:"+carParam.getGps()+"***位置信息策略:"+carParam.getPosition()+
						"***设防撤防:"+carParam.getSafety()+"***时区设置:"+carParam.getTimeZone()+"***电压值:"+carParam.getVoltage()+
						"***GSM:"+carParam.getGsm()+"****欠压报警:"+carParam.getUndervoltage()+"***高压报警:"+carParam.getHighVoltage()+
						"***疲劳驾驶时间:"+carParam.getTiredDrive()+"***解除疲劳驾驶时间:"+carParam.getTiredDriveTime()+"***水温:"+carParam.getWaterTemp()+
						"***用户id"+carParam.getUserId()+"****急刹车:"+carParam.getEbrake()+"***急加速:"+carParam.getEspeedup()+"***碰撞:"+carParam.getCrash()+
						"***震动:"+carParam.getShake()+"***速度提醒开关"+carParam.getSpeedRemind()+"***IMEI:"+carParam.getImei()+"***obd数据汇报策略:"+carParam.getDataUploadType()+
						"***obd数据汇报间隔:"+carParam.getDataUploadTime()+"***附加位置信息:"+carParam.getPositionExtra()+"***GSM基站信息："+carParam.getGsmbts()+
						"***通信模块:"+carParam.getCommunication()+"***定位模块:"+carParam.getPositionType());
				//获取命令字
				Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.paramQuery");
				Map<String, Object> respMap=RunningData.getIdResponseMap();
				//如果该用户有这个请求，则将故障码放进Map去
				String mapId=obdSn+"_"+dic.getTrueValue();
				if(respMap.containsKey(mapId)){
					respMap.put(mapId, carParam);
				}
				//参数设置保存入库
				carParamService.carParamSaveOrUpdate(carParam);//保存参数设置	
			}
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public CarParam carParamSet(Map<String,String> map,CarParam param){
		for (String key : map.keySet()) {
			logger.info(key+"***"+map.get(key));
			String paramStr=map.get(key);//消息内容
			
			//0x00（电子围栏）(write only)
			if("00".equals(key)){
				int[] fileLens = {1*2, 1*2, 4*2, 4*2, 4*2, 4*2};
				String[] files = new String[fileLens.length];
				int totalLen = 0;
				for(int i=0; i<fileLens.length; i++) {
					int len = fileLens[i];
					files[i] = paramStr.substring(totalLen, totalLen + len);
					totalLen += len;
				}
				
				//区域编号
				String areaNum=Integer.valueOf(files[0], 16).toString();
				//围栏类型+报警方式
				String railAndAlert = ByteUtil.hexStrToBinaryStr(files[1]);
				//大经
				String maxLongitude = files[2];
				//大纬
				String maxLatitude = files[3];
				//小经
				String minLongitude = files[4];
				//小纬
				String minLatitude =files[5];
				
				//大经度
				String maxLongitudeStr = maxLongitude.substring(0, 3) + "°" + maxLongitude.substring(3, 5) + "." + 
						maxLongitude.substring(5, 8) + "'";
				//大纬度
				String maxLatitudeStr = maxLatitude.substring(0, 2) + "°" + maxLatitude.substring(2, 4) + "." + 
						maxLatitude.substring(4, 8) + "'";
				//小经度
				String minLongitudeStr = minLongitude.substring(0, 3) + "°" + minLongitude.substring(3, 5) + "." + 
						minLongitude.substring(5, 8) + "'";
				//小纬度
				String minLatitudeStr = minLatitude.substring(0, 2) + "°" + minLatitude.substring(2, 4) + "." + 
						minLatitude.substring(4, 8) + "'";
				param.setAreaNum(areaNum);
				param.setRailAndAlert(railAndAlert);
				param.setMaxLongitude(maxLongitudeStr);
				param.setMaxLatitude(maxLatitudeStr);
				param.setMinLongitude(minLongitudeStr);
				param.setMinLatitude(minLatitudeStr);
			}
			//0x01（超速报警阈值）
			if("01".equals(key)){
				String overspeed= Integer.valueOf(paramStr,16).toString();
				param.setOverspeed(overspeed);
			}
			//0x02（进入休眠模式的时间）
			if("02".equals(key)){
				String sleepTime = Integer.valueOf(paramStr,16).toString();
				param.setSleepTime(sleepTime);
			}
			//0x03（ACC开时候上传、存储位置信息的间隔，16进制转10进制）
			if("03".equals(key)){
				String accTime= Integer.valueOf(paramStr,16).toString();
				param.setAccTime(accTime);
			}
			//0x04   (心跳时间间隔)没有休眠时候间隔 
			if("04".equals(key)){
				String heartTime = Integer.valueOf(paramStr,16).toString();
				param.setHeartTime(heartTime);
			}
			//0x05(位置信息策略)
			if("05".equals(key)){
				param.setPosition(paramStr);
			}
			//0x06设防撤防
			if("06".equals(key)){
				param.setSafety(paramStr);
			}
			
			//0x07(时区设置)
			if("07".equals(key)){
				String hour = paramStr.substring(0, 2);
				hour =	Integer.valueOf(hour,16).toString();
				String minute = paramStr.substring(2, 4);
				minute = Integer.valueOf(minute,16).toString();
				String timeZone = hour+"_"+minute;
				param.setTimeZone(timeZone);
			}
			
			//0x08(电压值，只读)
			if("08".equals(key)){
				String voltage =Integer.valueOf(paramStr,16)/10+"";
				param.setVoltage(voltage);
			}
			
			//0x09(GSM 信号强度，只读)
			if("09".equals(key)){
				String GSM =Integer.valueOf(paramStr,16).toString();
				param.setGsm(GSM); 
			}
			
			//0x0A欠压报警阈值
			if("0a".equals(key)){
				String undervoltage =((double)(Integer.valueOf(paramStr,16)))/10+"";
				param.setUndervoltage(undervoltage);
			}
			
			//0x0B高压报警阈值
			if("0b".equals(key)){
				String highVoltage =((double)(Integer.valueOf(paramStr,16)))/10+"";
				param.setHighVoltage(highVoltage);
			}
			
			//0x0C疲劳驾驶时间阈值
			if("0c".equals(key)){
				String tiredDrive =Integer.valueOf(paramStr,16)*10+"";
				param.setTiredDrive(tiredDrive);
			}
			
			//0x0D解除疲劳驾驶时间阈值
			if("0d".equals(key)){
				String tiredDriveTime =Integer.valueOf(paramStr,16)*10+"";
				param.setTiredDriveTime(tiredDriveTime);
			}
			
			//0x0E水温高报警阈值
			if("0e".equals(key)){
				String waterTemp =Integer.valueOf(paramStr,16).toString();
				param.setWaterTemp(waterTemp);
			}
			
			//0x0F设置用户id
			if("0f".equals(key)){
				//1400000000013168094719
				int userIdLen = Integer.valueOf(paramStr.substring(0, 2), 16);//号码有效长度
				String userIdStr =paramStr.substring(2, paramStr.length());
				String userId = userIdStr.substring(userIdStr.length()-userIdLen,userIdStr.length());
				param.setUserId(userId);
			}
			
			//0x10(急刹车系数强度)
			if("10".equals(key)){
				String ebrake =Integer.valueOf(paramStr,16).toString();
				param.setEbrake(ebrake);
			}
			
			//0x11(急加速系数强度)
			if("11".equals(key)){
				String espeedup =Integer.valueOf(paramStr,16).toString();
				param.setEspeedup(espeedup);
			}
			
			//0x12(碰撞报警系数强度)
			if("12".equals(key)){
				String crash =Integer.valueOf(paramStr,16).toString();
				param.setCrash(crash);
			}
			
			//0x13(布防时震动报警系数强度)
			if("13".equals(key)){
				String shake =Integer.valueOf(paramStr,16).toString();
				param.setShake(shake);
			}
			
			//0x14(速度提醒开关配置)
			if("14".equals(key)){
				param.setSpeedRemind(paramStr);
			}
			
			//0x15(IMEI只读)
			if("15".equals(key)){
				param.setImei(paramStr);
			}
			
			//0x16(obd数据汇报策略)
			if("16".equals(key)){
				param.setDataUploadType(paramStr);
			}
			
			//0x19(obd数据汇报间隔)
			if("19".equals(key)){
				String dataUploadTime= Integer.parseInt(paramStr)+"";
				param.setDataUploadTime(dataUploadTime);
			}
			
			//0x17(上传附加位置信息1)
			if("17".equals(key)){
				param.setPositionExtra(paramStr);
			}
			
			//0x18(基站定位时GSM基站信息内容）
			if("18".equals(key)){
				param.setGsmbts(paramStr);
			}
			
			//0x1A(通信模块类型)
			if("1a".equals(key)){
				param.setCommunication(paramStr);
			}
			
			//0x1B(定位模块类型)
			if("1b".equals(key)){
				param.setPositionType(paramStr);
			}
			
			//0x1C(GPS模块开关)
			if("1c".equals(key)){
				param.setGps(paramStr);
			}
		}
		return param;
	}
	
}
