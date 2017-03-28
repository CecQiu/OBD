package com.hgsoft.obd.service;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.Efence;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.EfenceService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.WarningMessageService;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.obd.util.WarnType;
@Service
public class DzwlService extends BaseService<Efence> {
	private static Logger obdHandlerPositionLogger = LogManager.getLogger("obdHandlerPositionLogger");
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private EfenceService efenceService;
	@Resource
	private PushApi pushApi;
	@Resource
	private WarningMessageService warningMessageService;
//	private static ExecutorService executor = Executors.newFixedThreadPool(4); 
	@Resource
	private CarGSPTrackService carGSPTrackService;
	@Resource
	private ObdSateService obdSateService;
	/**
	 * 电子围栏预警信息推送
	 * 暂时只做单个电子围栏,进区域报警，出区域报警，
	 * @param obdSn 设备号
	 * @param longitude 经度,单位度,如113.00000000 长度不限
	 * @param latitude  纬度,单位度,如23.00000000
	 */
	public void dzwlWarning(String obdSn,String longitude,String latitude,Date gpsTime){
		try {
			//如果存在当前车辆,并且车辆的防盗状态为设防,发送指令到app报警
			OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);// 设备号
			if(obdStockInfo!=null){
//				List<Efence> efList=efenceService.queryListByObdSn(obdSn);
//				for (Efence efence : efList) {
//					dzwlWarningMsg(pi, efence);
//				}
				
				Efence efence=efenceService.queryLastByObdSn(obdSn);
				if(efence!=null){
					obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" 
								+ obdSn+"-----经度:"+longitude+"----纬度:"
								+latitude+"----gps时间:"+DateUtil.getTimeString(gpsTime, "yyyy-MM-dd HH:mm:ss"));
					dzwlWarningMsg( obdSn, longitude, latitude, gpsTime, efence);
				}else{
					obdHandlerPositionLogger.info("----------【OBD设备电子围栏】该设备不存在电子围栏：" + obdSn);
				}
			}else{
				obdHandlerPositionLogger.info(obdSn+"*******该设备没有注册,无法推送电子围栏报警消息.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerPositionLogger.error(e);
			obdHandlerPositionLogger.error(obdSn+"****电子围栏报警信息发送失败."); 
		}
	}
	
	//电子围栏报警信息推送
	private void dzwlWarningMsg(String obdSn,String longitude,String latitude,Date gpsTime,Efence efence) throws Exception{
		WarnType warnType = null;
		//预警类型
		try {
			OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);// 设备号
			//1判断位置信息是否在矩形区域内
			Integer pin=dzwlJudge(longitude, latitude, efence);
			obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---是否区域内:"+pin);
			String type=efence.getType();//电子围栏类型
			String railAndAlert=efence.getRailAndAlert();//报警方式
			obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---报警方式:"+railAndAlert+"---电子围栏类型:"+type);
			String msgType = null;
			String msgDesc = null;
			boolean flag = true;
			//预警类型
			if("1".equals(railAndAlert)){
				//进区域报警
				if(pin==1){
					msgType="51";
					msgDesc="进区域报警.";
					warnType = WarnType.Efence_In;
				}else{
					flag = false;
					return;
				}
			}else if("2".equals(railAndAlert)){
				//出区域报警
				if(pin==0){
					msgType="52";
					msgDesc="出区域报警.";
					warnType = WarnType.Efence_Out;
				}else{
					flag = false;
					return;
				}
			}else if("3".equals(railAndAlert)){
				//进出区域都报警
				//获取该设备上次的经纬度
				CarGSPTrack ct = carGSPTrackService.findLastBySn(obdSn);
				if(ct!=null){
					obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---进出区域报警");
					String longitudeLast=CoordinateTransferUtil.lnglatTransferDouble(ct.getLongitude());
					String latitudeLast=CoordinateTransferUtil.lnglatTransferDouble(ct.getLatitude());
					Integer pinLast=dzwlJudge(longitudeLast, latitudeLast, efence);
					obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---进出区域报警,最近一次位置是否在区域内:"+pinLast);
					if(pin==pinLast){
						return;
					}else{
						msgType="53";
						if(pin==1){
							msgDesc="进出区域报警-进区域.";
							warnType = WarnType.Efence_InOut_In;
						}else if(pin==0){
							msgDesc="进出区域报警-出区域.";
							warnType = WarnType.Efence_InOut_Out;
						}
					}
				}else{
					return;
				}
			}else{
				return;
			}
			//定时定点电子围栏
			if("1".equals(type)){
				int din=DateUtil.dateBetween(gpsTime, efence.getStartDate(), efence.getEndDate());
				if(din==-1){
					obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---定时电子围栏时间为空.");
					throw new Exception("******定时电子围栏时间为空");
				}
				obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---定时电子围栏,gps时间:"+DateUtil.getTimeString(gpsTime, "yyyy-MM-dd HH:mm:ss")
						+"---电子围栏开始时间:"+DateUtil.getTimeString(efence.getStartDate(), "yyyy-MM-dd HH:mm:ss")
						+"---电子围栏结束时间:"+DateUtil.getTimeString(efence.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
				if(din==1){
					msgDesc = "定时定点电子围栏,"+msgDesc;
				}else{
					flag =false;
					return;
				}
			}else if("0".equals(type)){
				msgDesc = "普通电子围栏,"+msgDesc;
			}else{
				return;
			}
			
			//消息推送
			if(flag && msgType!=null){
				boolean mflag= false;
				ObdState obdState = obdSateService.queryByObdSn(obdSn);
				if(obdState==null){
					obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---电子围栏开关未设置,推送预警 .");
					mflag=true;
				}else{
					if( !"0".equals(obdState.getEfenceSwitch())){
						obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---电子围栏开关打开或未设置.");
						mflag=true;
					}
				}
				if(mflag){
					pushApi.pushWarnHandler(obdStockInfo.getObdSn(),warnType,0,msgType, msgDesc, longitude, latitude,gpsTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("*********电子围栏发生异常.");
		}
		
	}
	/**
	 * 判断位置是否在矩形区域内，且是否定时定点
	 * @param pi
	 * @param efence
	 * @return 0在矩形区域外，1在矩形区域内，异常
	 * @throws Exception 
	 */
	public int dzwlJudge(String longitude,String latitude,Efence efence) throws Exception{
		try {
			String maxLongitude =efence.getMaxLongitude();
			String minLongitude =efence.getMinLongitude();
			String maxLatitude =efence.getMaxLatitude();
			String minLatitude =efence.getMinLatitude();
			if(StringUtils.isEmpty(maxLongitude)||StringUtils.isEmpty(minLongitude)||StringUtils.isEmpty(maxLatitude)||StringUtils.isEmpty(minLatitude)){
				obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + efence.getObdSn()+"---电子围栏经纬度为空.");
				throw new Exception("**********电子围栏,请联系管理员.");
			}
			double lon=Double.parseDouble(longitude);
			double lat=Double.parseDouble(latitude);
			double maxlon=Double.parseDouble(maxLongitude);
			double maxlat=Double.parseDouble(maxLatitude);
			double minlon=Double.parseDouble(minLongitude);
			double minlat=Double.parseDouble(minLatitude);
			obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + efence.getObdSn()
					+"---经度:"+longitude
					+"---纬度:"+latitude
					+"---大经:"+maxLongitude
					+"---大纬:"+maxLatitude
					+"---小经:"+minLongitude
					+"---小纬:"+minLatitude);
			if(maxlon>=lon && minlon<=lon && maxlat>=lat && minlat<= lat){
				return 1;
			}else{
				return 0;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new Exception("**********区域判断异常,请联系管理员.");
		}
	}
	
//	/**
//	 * 处理预警信息推送
//	 * @param obdSn
//	 */
//	private void pushWarnHandler(final String obdSn,WarnType pushType,final String msgType,final  String msgDesc,
//			final  String longitude,final String latitude,final Date hDate) {
//		boolean canPush = PushUtil.canPush(obdSn+"_"+pushType, new Date());
//		obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---是否进行消息推送:"+canPush);
//		if(canPush){
//			//推送
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						String result = pushWarn(obdSn, msgType, msgDesc, longitude, latitude , hDate);
//						if(!"0".equals(result)){
//							//推送不成功，再来一次
//							pushWarn(obdSn, msgType, msgDesc, longitude, latitude , hDate);
//						}
//						obdHandlerPositionLogger.info("----------【OBD设备电子围栏判断】设备：" + obdSn+"---预警消息推送结果:"+result);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//	}
//
//	/**
//	 * 推送预警信息
//	 * @param obdSn
//	 * @param type
//	 * @param desc
//	 * @return
//	 * @throws Exception 
//	 */
//	private String pushWarn(String obdSn,String msgType, String msgDesc, String longitude, String latitude,Date hDate) throws Exception{
//		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);
//		if(obdStockInfo == null){
//			throw new Exception("设备对象为空："+obdSn);
//		}
//		String obdMsn = obdStockInfo.getObdMSn();//获取表面号
//		String msgTime = DateUtil.getTimeString(hDate, "yyyy-MM-dd HH:mm:ss");
//		try {
//			String result = pushApi.dzwlWarningInfo(obdMsn, msgType, msgTime, msgDesc, longitude, latitude);
//			//入库操作
//			WarningMessage wm = new WarningMessage();
//			wm.setObdSn(obdSn);
//			wm.setMessageType(msgType);
//			wm.setMessageDesc(msgDesc+"***经度:"+longitude+"***纬度:"+latitude+"***gps时间:"+msgTime);
//			wm.setMessageTime(new Date());
//			wm.setRemark(result);//是否成功
//			warningMessageService.warningMsgSave(wm);
//			logger.info(obdSn+"******电子围栏预警消息推送结果:"+result);
//			
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public static void main(String[] args) {
		
	}
}
