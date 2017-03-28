package com.hgsoft.common.message;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.Efence;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.PositionalInformation;
import com.hgsoft.carowner.entity.WarningMessage;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.DictionaryService;
import com.hgsoft.carowner.service.EfenceService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.PositionalInformationService;
import com.hgsoft.carowner.service.WarningMessageService;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * Mcu4位置信息接口任务类
 * @author fdf
 */
@Component
public class Muc4Task {
//	
//	@Resource
//	private PositionalInformationService pis;
//	@Resource
//	private CarGSPTrackService carGSPTrackService;
//	@Resource
//	private WarningMessageService warningMessageService;
//	@Resource
//	DictionaryService  dictionaryService;
//	@Resource
//	private CarInfoService carInfoService;
//	@Resource
//	private OBDStockInfoService obdStockInfoService;
//	@Resource
//	private PushApi pushApi;
//	@Resource
//	private EfenceService efenceService;
//	
//	private final Log logger = LogFactory.getLog(Muc4Task.class);
//	private static Logger obdPositionInfoLogger = LogManager.getLogger("obdPositionInfoLogger");
//	private static Logger obdTrackLogger = LogManager.getLogger("obdTrackLogger");
//
//	/**GPS定位时，使用GPS定位数据*/
//	private final int POSITION_1 = 1;
//	/**GPS不定位时,使用基站信息定位,且基站信息为7个基站Cellid*/
//	private final int POSITION_2 = 2;
//	/**GPS不定位时,使用基站信息定位，且基站信息为 celiid+lac*/
//	private final int POSITION_3 = 3;
//	/**GPS不定位时,使用基站信息定位，CDMA网络定位： sid+nid+bid+mcc+mnc*/
//	private final int POSITION_4 = 4;
//	/**
//	* 4.	Mcu4位置信息,逻辑处理方法
//	* @param om 请求消息对象
//	* @return 
//	*/
//	public boolean doTask(OBDMessage om) {
//		String obdSn=om.getId();
//		logger.info(obdSn+"***********位置信息开始");
//		obdPositionInfoLogger.info(obdSn+"***********位置信息开始");
//		
//		String gpsTime = "";//gps时间
//		String warn = "";//报警标志
//		String status = "";//状态标志
//		String satellites = "";//卫星数
//		String longitudes = "";//经度
//		String latitudes = "";//纬度
//		String directionAngle = "";//方向角
//		String gpsSpeed = "";//gps速度
//		String obdSpeed = "";//obd速度
//		String engineTemperature = "";//发动机水温
//		String baseStation = "";//基站信息
//		String mccStr = "";//移动用户所属国家代码
//		String mncStr = "";//移动网号码
//		String sid ="";//系统系别码
//		String nid ="";//网络识别码
//		String bid ="";//小区基站id
//		
//		//这是属性和长度的对应数组
//		int[] fileLens1 = {12, 4, 2, 2, 8, 8, 4, 2, 2, 2};
//		int[] fileLens2 = {12, 4, 2, 28, 2, 2};
//		int[] fileLens3 = {12, 4, 2, 10, 4, 2, 2, 2};
//		int[] fileLens4 = {12, 4, 2, 4, 4, 4, 4, 2, 2, 2};//CDMA网络定位
//		String[] files;
//		
//		//开始截取操作
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		DecimalFormat df = new DecimalFormat("00");
//		String msg = om.getMsgBody();
//		System.out.println("msg~~~:"+msg);
//		int type = Integer.parseInt(msg.substring(0, 2), 16);
//		
//		if(type < 1 || type > 4) {
//			throw new RuntimeException("error, this is no such positional information type here...");
//		}
//		PositionalInformation pi = new PositionalInformation(IDUtil.createID());
//		pi.setObdsn(obdSn);
//		pi.setInsesrtTime(new Date());
//		pi.setType(type);
//		int totalLen = 2;//累计的截取长度，2是忽略掉已截取的类型属性
//		switch(type) {
//			case POSITION_1:
//				files = new String[fileLens1.length];
//				for(int i=0; i<fileLens1.length; i++) {
//					int len = fileLens1[i];
//					files[i] = msg.substring(totalLen, totalLen + len);
//					totalLen += len;
//				}
//				gpsTime = files[0];
//				warn = files[1];
//				status = files[2];
//				satellites = files[3];
//				longitudes = files[4];
//				latitudes = files[5];
//				directionAngle = files[6];
//				gpsSpeed = files[7];
//				obdSpeed = files[8];
//				engineTemperature = files[9];
//				
//				//卫星数
//				pi.setSatellites(Integer.parseInt(satellites, 16));
//				
//				//经度
//				String longitudeStr = longitudes.substring(0, 3) + "°" + longitudes.substring(3, 5) + "." + 
//						longitudes.substring(5, 8) + "'";
//				pi.setLongitude(longitudeStr);
//				
//				//纬度
//				String latitudeStr = latitudes.substring(0, 2) + "°" + latitudes.substring(2, 4) + "." + 
//						latitudes.substring(4, 8) + "'";
//				pi.setLatitude(latitudeStr);
//				
//				//方向角
//				pi.setDirectionAngle(Integer.parseInt(directionAngle, 16));
//				//Gps速度
//				pi.setGpsSpeed(Integer.parseInt(gpsSpeed, 16));
//				logger.info(obdSn+"***GPS时间gpsTime:"+gpsTime+"***报警:"+warn+"***状态status:"+status+"***卫星数satellites:"+satellites+"***经度longitudes:"+longitudes+"***纬度:"+latitudes+"***方向角directionAngle:"+directionAngle+"***Gps速度gpsSpeed:"+gpsSpeed+"***OBD速度:"+obdSpeed+"***发动机水温:"+engineTemperature);
//				obdPositionInfoLogger.info(obdSn+"***GPS时间gpsTime:"+gpsTime+"***报警:"+warn+"***状态status:"+status+"***卫星数satellites:"+satellites+"***经度longitudes:"+longitudes+"***纬度:"+latitudes+"***方向角directionAngle:"+directionAngle+"***Gps速度gpsSpeed:"+gpsSpeed+"***OBD速度:"+obdSpeed+"***发动机水温:"+engineTemperature);
//				break;
//			case POSITION_2:
//				files = new String[fileLens2.length];
//				for(int i=0; i<fileLens2.length; i++) {
//					int len = fileLens2[i];
//					files[i] = msg.substring(totalLen, totalLen + len);
//					totalLen += len;
//				}
//				gpsTime = files[0];
//				warn = files[1];
//				status = files[2];
//				baseStation = files[3];
//				obdSpeed = files[4];
//				engineTemperature = files[5];
//				
//				//GSM基站信息
//				String mainSta = baseStation.substring(0, 4);
//				String childSta1 = baseStation.substring(4, 8);
//				String childSta2 = baseStation.substring(8, 12);
//				String childSta3 = baseStation.substring(12, 16);
//				String childSta4 = baseStation.substring(16, 20);
//				String childSta5 = baseStation.substring(20, 24);
//				String childSta6 = baseStation.substring(24, 28);
//				String gsmStationInformation = "S:" + mainSta + ",N1:" + childSta1 + ",N2:" + childSta2 + 
//						",N3:" + childSta3 + ",N4:" + childSta4 + ",N5:" + childSta5 + ",N6:" + childSta6;
//				pi.setGsmStationInformation(gsmStationInformation);
//				break;
//			case POSITION_3:
//				files = new String[fileLens3.length];
//				for(int i=0; i<fileLens3.length; i++) {
//					int len = fileLens3[i];
//					files[i] = msg.substring(totalLen, totalLen + len);
//					totalLen += len;
//				}
//				gpsTime = files[0];
//				warn = files[1];
//				status = files[2];
//				baseStation = files[3];
//				mccStr = files[4];
//				mncStr = files[5];
//				obdSpeed = files[6];
//				engineTemperature = files[7];
//				
//				//GSM基站信息
//				String cellid = baseStation.substring(0, 6);
//				String lac = baseStation.substring(6, 10);
//				String gsmStationInfo = "cellid:" + cellid + ",lac:" + lac;
//				pi.setGsmStationInformation(gsmStationInfo);
//				//移动用户所属国家代码
//				String mcc1 = mccStr.substring(0, 2);
//				String mcc2 = mccStr.substring(2, 4);
//				String mcc = Integer.parseInt(mcc1, 16) + df.format(Integer.parseInt(mcc2, 16));
//				pi.setMcc(mcc);
//				//移动网号码
//				pi.setMnc(df.format(Integer.parseInt(mncStr)));
//				break;
//			case POSITION_4:
//				files = new String[fileLens4.length];
//				for(int i=0; i<fileLens4.length; i++) {
//					int len = fileLens4[i];
//					files[i] = msg.substring(totalLen, totalLen + len);
//					totalLen += len;
//				}
//				gpsTime = files[0];
//				warn = files[1];
//				status = files[2];
//				sid = files[3];
//				nid = files[4];
//				bid = files[5];
//				mccStr = files[6];
//				mncStr = files[7];
//				obdSpeed = files[8];
//				engineTemperature = files[9];
//				
//				//sid,十六进制转成十进制
//				String sidStr = Integer.valueOf(sid,16).toString();
//				String nidStr = Integer.valueOf(nid,16).toString();
//				String bidStr = Integer.valueOf(bid,16).toString();
//				//移动用户所属国家代码
//				String mccNum = Integer.valueOf(mccStr,16).toString();
//				//httpclient访问飞豹系统，获取到经纬度
//				JSONObject jb =CDMA(mccNum, sidStr, nidStr, bidStr);
//				if(jb==null){
//					//定位失败
//					logger.info(obdSn+"*********************飞豹定位返回码,返回位置信息为空。");
//					obdPositionInfoLogger.info(obdSn+"*********************飞豹定位返回码,返回位置信息为空。");
//				}
//				String code = jb.getString("code");
//				if("0".equals(code)){
//					//定位成功
//					String lng = jb.getJSONObject("data").getString("lng");//经度
//					String lngStr=CoordinateTransferUtil.lnglatTransferString(lng);
//					pi.setLongitude(lngStr);
//					//纬度
//					String lat = jb.getJSONObject("data").getString("lat");//维度
//					String latStr=CoordinateTransferUtil.lnglatTransferString(lat);
//					pi.setLatitude(latStr);
//					//定位精度
//					Integer precision=(Integer) jb.getJSONObject("data").get("precision");
//					pi.setOprecision(precision.toString());
//					pi.setStatusGps(1);//定位成功.
//				}else{
//					//定位失败
//					pi.setStatusGps(0);//定位失败
//					logger.info(obdSn+"*****************飞豹定位返回码,定位失败:"+code);
//					obdPositionInfoLogger.info(obdSn+"*****************飞豹定位返回码,定位失败:"+code);
//				}
//				pi.setMcc(mccNum);
//				//移动网号码
//				pi.setMnc(Integer.valueOf(mncStr,16).toString());
//				pi.setSid(sidStr);
//				pi.setNid(nidStr);
//				pi.setBid(bidStr);
//				break;
//		}
//		
//		String gpstimeStr=StrUtil.strSame(12, "0");
//		if(gpstimeStr.equals(gpsTime)){
//			logger.error(obdSn+"****位置信息的gps时间不正确:"+gpsTime);
//		}
//		String gpstimeSix=StrUtil.strSame(6, "0");
//		if(gpstimeSix.equals(gpsTime)){
//			logger.error(obdSn+"****位置信息的gps时间不正确:"+gpsTime);
//		}
//		
//		Pattern pattern = Pattern.compile("[0-9]*");
//		if(!pattern.matcher(gpsTime).matches()){
//			logger.error(obdSn+"****位置信息的gps时间不正确:"+gpsTime);
//		}
//    	
//		//gps时间，如果gps时间没获取到，都是000000000000，不用管时间，
//		String gpsDate="20" + gpsTime;//gps时间
//		try {
//			pi.setGpsTime(sdf.parse(gpsDate));//gps时间
//			logger.info(pi.getGpsTime()+"*******gps原始时间:"+gpsTime);
//			logger.info(pi.getObdsn()+"******位置信息的gpstime:"+(String)DateUtil.fromatDate(pi.getGpsTime(), "yyyy-MM-dd HH:mm:ss"));
//			obdPositionInfoLogger.info(pi.getGpsTime()+"*******gps原始时间:"+gpsTime);
//			obdPositionInfoLogger.info(pi.getObdsn()+"******位置信息的gpstime:"+(String)DateUtil.fromatDate(pi.getGpsTime(), "yyyy-MM-dd HH:mm:ss"));
//		} catch (ParseException e) {
////			pi.setGpsTime(new Date());
//			e.printStackTrace();
//			logger.error(e);
//			obdPositionInfoLogger.error(e);
//			logger.error(obdSn+"******位置信息的gps时间异常:"+gpsTime);
//			return false;
//		}
//		
//		//警告标识
//		char[] warnchars = ByteUtil.hexStrToBinaryStr(warn).toCharArray();
//		logger.info(pi.getObdsn()+"*************************警告标识");
//		obdPositionInfoLogger.info(pi.getObdsn()+"*************************警告标识");
//		System.out.println(warnchars);
//		
//		//0冷却液过低  sos报警,没对应字段，WarnStation
//		pi.setWarnStation(Integer.parseInt(warnchars[0]+""));
//		//1,2保留字段
//		pi.setWarnHold1(Integer.parseInt(warnchars[1]+""));
//		pi.setWarnHold2(Integer.parseInt(warnchars[2]+""));
//		//3.非法启动(布防时才会产生)3
//		pi.setWarnShock(Integer.parseInt(warnchars[3]+""));
//		//4为1表示疲劳驾驶
//		pi.setWarnFailureSerious(Integer.parseInt(warnchars[4]+""));
//		//5用车超时报警5 没有对应的字段,WarnVin
//		pi.setWarnVin(Integer.parseInt(warnchars[5]+""));
//		//6水温高报警6
//		pi.setWarnWaterTemperature(Integer.parseInt(warnchars[6]+""));
//		//7 到保养期报警7
//		pi.setWarnMaintenance(Integer.parseInt(warnchars[7]+""));
//		//异常震动8
//		pi.setWarnCollision(Integer.parseInt(warnchars[8]+""));
//		//出矩形区域报警9
//		pi.setWarnOutRectangular(Integer.parseInt(warnchars[9]+""));
//		//进矩形区域报警10
//		pi.setWarnInRectangular(Integer.parseInt(warnchars[10]+""));
//		//电池过压11
//		pi.setWarnOvervoltage(Integer.parseInt(warnchars[11]+""));
//		//电池欠压12
//		pi.setWarnBrown(Integer.parseInt(warnchars[12]+""));
//		//13保留字段 
//		pi.setWarnHold3(Integer.parseInt(warnchars[13]+""));
//		//超速报警14
//		pi.setWarnOverspeed(Integer.parseInt(warnchars[14]+""));
//		//定时定点围栏报警15 没有对应字段 WarnSos
//		pi.setWarnSos(Integer.parseInt(warnchars[15]+""));
//		
//
//		//状态标识
//		char[] statuschars = ByteUtil.hexStrToBinaryStr(status).toCharArray();
//		logger.info(pi.getObdsn()+"*************************状态标识");
//		obdPositionInfoLogger.info(pi.getObdsn()+"*************************状态标识");
//		System.out.println(statuschars);
//		pi.setStatusAcc(Integer.parseInt(statuschars[7]+""));//ACC开启
//		pi.setStatusGps(Integer.parseInt(statuschars[6]+""));//GPS定位
//		logger.info(obdSn+"***gps定位状态:"+pi.getStatusGps());
//		obdPositionInfoLogger.info(obdSn+"***gps定位状态:"+pi.getStatusGps());
//		pi.setStatusLatitude(Integer.parseInt(statuschars[5]+""));//纬度-0南纬1北纬
//		pi.setStatusLongitude(Integer.parseInt(statuschars[4]+""));//经度-0西经1东经
//		pi.setStatusFatigue(Integer.parseInt(statuschars[3]+""));//疲劳驾驶-0否1是
//		pi.setStatusMalfunction(Integer.parseInt(statuschars[2]+""));//Obd故障警告-0否1是
//		pi.setStatusPosition(Integer.parseInt(statuschars[1]+""));//实时的位置消息-0否1是
//		pi.setStatusPlug(Integer.parseInt(statuschars[0]+""));//插设备-0否1是
//		logger.info(obdSn+"***插设备:"+pi.getStatusPlug());
//		obdPositionInfoLogger.info(obdSn+"***插设备:"+pi.getStatusPlug());
//
//		//Obd速度   暂时做转换
//		if(!"ff".equals(obdSpeed)){
//			pi.setObdSpeed(Integer.parseInt(obdSpeed, 16));
//		}else{
//			pi.setObdSpeed(0);
//			logger.info(obdSn+"*****位置信息的obd速度为ff");
//		}
//		//obd速度
////		pi.setObdSpeed(Integer.parseInt(obdSpeed, 16));
//		//发动机水温
//		if(!"ff".equals(engineTemperature)){
//			pi.setEngineTemperature(Integer.parseInt(engineTemperature, 16)-40);//发动机水温，要减去40
//		}else{
//			PositionalInformation p= pis.findLastBySN(obdSn);//最后一次位置信息的obd速度
//			if(p!=null){
//				pi.setEngineTemperature(p.getEngineTemperature());
//				logger.info(obdSn+"****位置信息里的发动机水温为ff,取上次位置信息的发动机水温:"+p.getEngineTemperature());
//			}else{
//				pi.setEngineTemperature(0);
//				logger.info(obdSn+"****位置信息里的发动机水温为ff,且之前没有位置信息.");
//			}
//			
//		}
//		
////		pi.setEngineTemperature(Integer.parseInt(engineTemperature, 16)-40);//发动机水温，要减去40
//		//如果没有定位到，也要保存入库，经纬度设置为null
//		//如果位置信息的obd设备号和gpstime时间跟数据库的一样的话，不保存入库
//		PositionalInformation p= pis.findByObdsnAndGpstime(pi.getObdsn(), pi.getGpsTime());
//		if(p!=null){
//			logger.info(pi.getObdsn()+"*****该设备,当前gpstime时间一存在记录:"+(String)DateUtil.fromatDate(pi.getGpsTime(), "yyyy-MM-dd HH:mm:ss"));
//			obdPositionInfoLogger.info(pi.getObdsn()+"*****该设备,当前gpstime时间一存在记录:"+(String)DateUtil.fromatDate(pi.getGpsTime(), "yyyy-MM-dd HH:mm:ss"));
//			return false;
//		}
//		pis.add(pi);//位置信息保存入库
////		
////		//如果用户有请求位置信息，返回位置信息对象
////		//获取命令字8005
////		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.obdPosition");
////		Map<String, Object> respMap=RunningData.getIdResponseMap();
////		//如果该用户有这个请求，则将位置信息放进Map去
////		String mapId=obdSn+"_"+dic.getTrueValue();
////		logger.info(mapId+"**********************位置消息id");
////		if(respMap.containsKey(mapId)){
////			respMap.put(mapId, pi);
////		}
//		
//		//同时插入:车辆行驶记录表（Car_GSPTrack）;如果没定位到，不插入gps表
//		if(pi.getLongitude()!=null && pi.getLatitude()!=null && pi.getStatusGps()==1){
//			if(type==POSITION_1||type==POSITION_4){
//				CarGSPTrack cgt=new CarGSPTrack();
//				cgt.setGspTrackId(IDUtil.createID());
//				cgt.setObdSn(pi.getObdsn());
//				cgt.setLongitude(pi.getLongitude());
//				cgt.setLatitude(pi.getLatitude());
//				cgt.setDirectionAngle(pi.getDirectionAngle());
//				cgt.setGpsSpeed(pi.getGpsSpeed());
//				cgt.setObdSpeed(pi.getObdSpeed());
//				//高位字段high即是海拔高度不知道在哪里取到
//				cgt.setGspTrackTime((String) DateUtil.fromatDate(pi.getGpsTime(),"yyyy-MM-dd HH:mm:ss"));
//				carGSPTrackService.carGSPSave(cgt);
//				logger.info(pi.getObdsn()+"****gps位置信息同时保存入库,经度:"+cgt.getLatitude()+"***纬度:"+cgt.getLongitude());
//				obdPositionInfoLogger.info(pi.getObdsn()+"****gps位置信息同时保存入库,经度:"+cgt.getLatitude()+"***纬度:"+cgt.getLongitude());
//				obdTrackLogger.info(pi.getObdsn()+"****gps位置信息同时保存入库,经度:"+cgt.getLatitude()+"***纬度:"+cgt.getLongitude());
//			}
//		}
//		//如果存在当前车辆,并且车辆的防盗状态为设防,发送指令到app报警
//		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//		if(obdStockInfo!=null){
//			CarInfo car=carInfoService.queryCarInfoBySN(obdStockInfo.getObdSn());
//			if(car!=null&&"01".equals(car.getCarState())){
//				boolean flag=msgSendApp(om, pi, "11", "非法启动(布防时才会产生)报警");
//				logger.info(obdSn+"***非法启动(布防时才会产生)报警结果:"+flag);
//				obdPositionInfoLogger.info(obdSn+"***非法启动(布防时才会产生)报警结果:"+flag);
//			}
//		}
//		
//		// 11：非法启动；12：异常震动现在只有11和12和27超速报警
//		//如果报警标志位：0000000000000000
//		logger.info("非法启动:"+pi.getWarnShock()+"****异常震动:"+pi.getWarnCollision());
//		obdPositionInfoLogger.info("非法启动:"+pi.getWarnShock()+"****异常震动:"+pi.getWarnCollision());
////		//0冷却液过低  sos报警,没对应字段，WarnStation
////		if("1".equals(pi.getWarnStation().toString())){
////			boolean flag=msgSendApp(om, pi, "0", "冷却液过低报警");
////			logger.info(obdSn+"***冷却液过低报警结果:"+flag);
////		}
////		//3.非法启动(布防时才会产生)3
////		if("1".equals(pi.getWarnShock().toString())){
//////			boolean flag=msgSendApp(om, pi, "3", "非法启动(布防时才会产生)报警");
////			boolean flag=msgSendApp(om, pi, "11", "非法启动(布防时才会产生)报警");
////			logger.info(obdSn+"***非法启动(布防时才会产生)报警结果:"+flag);
////			obdPositionInfoLogger.info(obdSn+"***非法启动(布防时才会产生)报警结果:"+flag);
////		}
//		
////		//4疲劳驾驶报警
////		if("1".equals(pi.getWarnFailureSerious().toString())){
////			boolean flag=msgSendApp(om, pi, "6", "疲劳驾驶报警");
////			logger.info(obdSn+"***疲劳驾驶报警结果:"+flag);
////		}
////		//5用车超时报警5 没有对应的字段,WarnVin
////		if("1".equals(pi.getWarnVin().toString())){
////			boolean flag=msgSendApp(om, pi, "6", "用车超时报警");
////			logger.info(obdSn+"***用车超时报警结果:"+flag);
////		}
////		//6水温高报警6
////		if("1".equals(pi.getWarnWaterTemperature().toString())){
////			boolean flag=msgSendApp(om, pi, "6", "水温高报警");
////			logger.info(obdSn+"***水温高报警结果:"+flag);
////		}
////		//7 到保养期报警7
////		if("1".equals(pi.getWarnMaintenance().toString())){
////			boolean flag=msgSendApp(om, pi, "7", "到保养期报警");
////			logger.info(obdSn+"***到保养期报警结果:"+flag);
////		}
//		//异常震动8
//		if("1".equals(pi.getWarnCollision().toString())){
////			boolean flag=msgSendApp(om, pi, "8", "异常震动报警");
//			boolean flag=msgSendApp(om, pi, "12", "异常震动报警");
//			logger.info(obdSn+"***异常震动报警结果:"+flag);
//			obdPositionInfoLogger.info(obdSn+"***异常震动报警结果:"+flag);
//		}
////		//出矩形区域报警9
////		if("1".equals(pi.getWarnOutRectangular().toString())){
////			boolean flag=msgSendApp(om, pi, "9", "出矩形区域报警");
////			logger.info(obdSn+"***进矩形区域报警结果:"+flag);
////		}
////		//进矩形区域报警10
////		if("1".equals(pi.getWarnInRectangular().toString())){
////			boolean flag=msgSendApp(om, pi, "10", "进矩形区域报警");
////			logger.info(obdSn+"***进矩形区域报警结果:"+flag);
////		}
////		//电池过压11
////		if("1".equals(pi.getWarnOvervoltage().toString())){
////			boolean flag=msgSendApp(om, pi, "11", "电池过压报警");
////			logger.info(obdSn+"***电池过压报警结果:"+flag);
////		}
////		//电池欠压12
////		if("1".equals(pi.getWarnBrown().toString())){
////			boolean flag=msgSendApp(om, pi, "12", "电池欠压报警");
////			logger.info(obdSn+"***电池欠压报警结果:"+flag);
////		}
//		//超速报警14
//		if("1".equals(pi.getWarnOverspeed().toString())){
////			boolean flag=msgSendApp(om, pi, "14", "超速报警");
//			boolean flag=msgSendApp(om, pi, "27", "超速报警");
//			logger.info(obdSn+"***超速报警报警结果:"+flag);
//			obdPositionInfoLogger.info(obdSn+"***超速报警报警结果:"+flag);
//		}
////		//定时定点围栏报警15 没有对应字段
////		if("1".equals(pi.getWarnSos().toString())){
////			boolean flag=msgSendApp(om, pi, "15", "定时定点围栏报警");
////			logger.info(obdSn+"***定时定点围栏报警结果:"+flag);
////		}
//		//电子围栏
//		if(pi.getStatusGps()==1){
//			dzwlWarning(pi);
//		}
//
//		//如果定位失败
//		if(pi.getLongitude()==null || pi.getLatitude()==null || pi.getStatusGps()==0){
//			logger.info(obdSn+"***********位置信息结束,定位失败.");
//			obdPositionInfoLogger.info(obdSn+"***********位置信息结束,定位失败.");
//			return false;
//		}
//		logger.info(obdSn+"***********位置信息结束");
//		obdPositionInfoLogger.info(obdSn+"***********位置信息结束");
//		return true;
//	}
//	/**
//	 * CDMA基站定位
//	 * @param mcc
//	 * @param sid 对应飞豹协议的mnc
//	 * @param nid 对应飞豹协议的lac
//	 * @param bid 对应飞豹协议的cell
//	 * @return {"code":"0","data":{"addr":"广东省 广州市 天河区 S15沈海高速广州支线 靠近广东省城市建设高级技工学校(长福校区)","lat":23.168301,"lng":113.345682,"precision":794},"msg":"success","sid":"10001"}
//	 *         code {0:成功;10001:无基站信息;10002:Key已过期;10003:Key非法请求;10004:Key请求次数已超上限}
//	 *         
//	 * @throws Exception
//	 */
//	@SuppressWarnings("deprecation")
//	public JSONObject CDMA(String mcc,String sid,String nid,String bid){
//		@SuppressWarnings("resource")
//		HttpClient httpclient = new DefaultHttpClient();
//		List<BasicNameValuePair> qparams = new ArrayList<BasicNameValuePair>();
//		//飞豹提供的服务id
//		qparams.add(new BasicNameValuePair("sid", "10001"));
//		int mccI=Integer.parseInt(mcc);
//		int mncI=Integer.parseInt(sid);
//		int nidI=Integer.parseInt(nid);
//		int cellI=Integer.parseInt(bid);
//		StringBuffer baseinfo = new StringBuffer();//拼接请求参数
//		baseinfo.append("{mcc:").append(mccI);
//		baseinfo.append(",mnc:").append(mncI);
//		baseinfo.append(",lac:").append(nidI);
//		baseinfo.append(",cell:").append(cellI);
//		baseinfo.append("}");
////		String baseinfo= "{mcc:"+mccI+",mnc:"+mncI+",lac:"+nidI+",cell:"+cellI+"}";
////		qparams.add(new BasicNameValuePair("baseinfo", "{mcc:460,mnc:03,lac:1,cell:3122}"));
//		qparams.add(new BasicNameValuePair("baseinfo", baseinfo.toString()));
//		qparams.add(new BasicNameValuePair("ky", "5ZGofDJ8MTQ0MjQ1NTIwMDAwMHwxNDUwMzE3NjAwMDAwfDUwMDAwfDEwMDBofDE0NDI0NTUyMDAw"));
//		try {
//			java.net.URI uri = URIUtils.createURI("http", "121.43.117.211", 8080, "/lbs/lbs",URLEncodedUtils.format(qparams, "UTF-8"), null);
//			HttpGet httpGet = new HttpGet(uri);
//			System.out.println(httpGet.getURI());
//			
//			CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpGet);  
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				String resp= EntityUtils.toString(entity, "UTF-8");
//				JSONObject jb = new JSONObject(resp);
//			    httpGet.abort();//简单的终止请求 
//			    return jb;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//			obdPositionInfoLogger.error(e);
//		}  
//		return null;
//	}
//	
//	
//	public boolean msgSendApp(OBDMessage om,PositionalInformation pi,String type,String desc){
//		String obdSn=om.getId();
//		//暂时转换
//		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//		if(obdStockInfo!=null){
//			obdSn=obdStockInfo.getObdSn();
//		}
//		String flag;
//		try {
////			flag = PushApi.pushWarningInfo(obdSn, type, (String) DateUtil.fromatDate(pi.getGpsTime(),"yyyy-MM-dd HH:mm:ss"), desc);
//			flag = pushApi.pushWarningInfo(obdSn, type, (String) DateUtil.fromatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), desc);
//			logger.info(obdSn+"***消息类别:"+type+"****"+flag+"**********"+desc+"预警消息推送0成功-1失败");
//			obdPositionInfoLogger.info(obdSn+"***消息类别:"+type+"****"+flag+"**********"+desc+"预警消息推送0成功-1失败");
//			
//			WarningMessage wm = new WarningMessage();
//			wm.setObdSn(obdSn);
//			wm.setMessageType(type);
//			wm.setMessageDesc(desc);
//			wm.setMessageTime(pi.getGpsTime());
//			wm.setRemark(flag);//是否成功
//			warningMessageService.warningMsgSave(wm);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//			obdPositionInfoLogger.error(e);
//			return false;
//		}
//		return true;
//	}
//	
//	/**
//	 * 电子围栏预警信息推送
//	 * @param pi
//	 */
//	public void dzwlWarning(PositionalInformation pi){
//		String obdSn = pi.getObdsn();
//		try {
//			//如果存在当前车辆,并且车辆的防盗状态为设防,发送指令到app报警
//			OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//			if(obdStockInfo!=null){
////				List<Efence> efList=efenceService.queryListByObdSn(obdSn);
////				for (Efence efence : efList) {
////					dzwlWarningMsg(pi, efence);
////				}
//				Efence efence=efenceService.queryLastByObdSn(obdSn);
//				if(efence!=null){
//					dzwlWarningMsg(pi, efence);
//				}
//			}else{
//				logger.info(obdSn+"*******该设备没有注册,无法推送电子围栏报警消息.");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//			logger.error(obdSn+"****电子围栏报警信息发送失败."); 
//		}
//	}
//	
//	//电子围栏报警信息推送
//	private void dzwlWarningMsg(PositionalInformation pi,Efence efence) throws Exception{
//		try {
//			String obdSn = pi.getObdsn();
//			OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//			//1判断位置信息是否在矩形区域内
//			Integer pin=dzwlJudge(pi,efence);
//			String type=efence.getType();//电子围栏类型
//			String railAndAlert=efence.getRailAndAlert();//报警方式
//			Date now = new Date();//预警发生时间
//			String nowStr = (String)DateUtil.fromatDate(now, "yyyy-MM-dd HH:mm:ss");
//			String msgType = null;
//			String msgDesc = null;
//			boolean flag = true;
//			//预警类型
//			if("1".equals(railAndAlert)){
//				//进区域报警
//				if(pin==1){
//					msgType="51";
//					msgDesc="进区域报警.";
//				}else{
//					flag = false;
//					return;
//				}
//			}else if("2".equals(railAndAlert)){
//				//出区域报警
//				if(pin==0){
//					msgType="52";
//					msgDesc="出区域报警.";
//				}else{
//					flag = false;
//					return;
//				}
//			}else if("3".equals(railAndAlert)){
//				//进出区域都报警,暂时不处理
//				return;
//			}else{
//				return;
//			}
//			//定时定点电子围栏
//			if("1".equals(type)){
//				int din=DateUtil.dateBetween(now, efence.getStartDate(), efence.getEndDate());
//				if(din==1){
//					msgDesc = "定时定点电子围栏,"+msgDesc;
//				}else{
//					flag =false;
//					return;
//				}
//			}else if("0".equals(type)){
//				msgDesc = "普通电子围栏,"+msgDesc;
//			}else{
//				return;
//			}
//			String longitude=CoordinateTransferUtil.lnglatTransferDouble(pi.getLongitude());//经度
//			String latitude=CoordinateTransferUtil.lnglatTransferDouble(pi.getLatitude());//纬度
//			//消息推送
//			if(flag && msgType!=null){
//				String result=pushApi.dzwlWarningInfo(obdStockInfo.getObdMSn(), msgType, nowStr, msgDesc, longitude, latitude);
//				//保存日记
//				WarningMessage wm = new WarningMessage();
//				wm.setObdSn(obdSn);
//				wm.setMessageType(msgType);
//				wm.setMessageDesc(msgDesc+"***经度:"+longitude+"***纬度:"+latitude);
//				wm.setMessageTime(now);
//				wm.setRemark(result);//是否成功
//				warningMessageService.warningMsgSave(wm);
//				logger.info(obdSn+"******电子围栏预警消息推送结果:"+result);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception("*********电子围栏发生异常.");
//		}
//		
//	}
//	/**
//	 * 判断位置是否在矩形区域内，且是否定时定点
//	 * @param pi
//	 * @param efence
//	 * @return 0在矩形区域外，1在矩形区域内，-1异常
//	 * @throws Exception 
//	 */
//	public int dzwlJudge(PositionalInformation pi,Efence efence) throws Exception{
//		try {
//			//经度 113°16.535'  23°07.1172'
//			String lngStr=CoordinateTransferUtil.lnglatTransferDouble(pi.getLongitude());
//			//纬度
//			String latStr=CoordinateTransferUtil.lnglatTransferDouble(pi.getLatitude());
//			String maxLongitude =efence.getMaxLongitude();
//			String minLongitude =efence.getMinLongitude();
//			String maxLatitude =efence.getMaxLatitude();
//			String minLatitude =efence.getMinLatitude();
//			double lon=Double.parseDouble(lngStr);
//			double lat=Double.parseDouble(latStr);
//			double maxlon=Double.parseDouble(maxLongitude);
//			double maxlat=Double.parseDouble(maxLatitude);
//			double minlon=Double.parseDouble(minLongitude);
//			double minlat=Double.parseDouble(minLatitude);
//			if(maxlon>=lon && minlon<=lon && maxlat>=lat && minlat<= lat){
//				return 1;
//			}else{
//				return 0;
//			}
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//			logger.info(pi.getObdsn()+"***电子围栏区域判断错误.");
//			throw new Exception("**********区域判断异常,请联系管理员.");
//		}
//	}
}
