package com.hgsoft.obd.service;

import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.Fence;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.FenceService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.WarningMessageService;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.obd.util.WarnType;

@Service
public class FenceHandleService extends BaseService<Fence> {
	private static Logger log = LogManager.getLogger("obdHandlerPositionLogger");
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private PushApi pushApi;
	@Resource
	private WarningMessageService warningMessageService;
	@Resource
	private CarGSPTrackService carGSPTrackService;
	@Resource
	private ObdSateService obdSateService;

	@Resource
	private FenceService fenceService;

	/**
	 * 电子围栏预警信息推送 支持多区域，定时电子围栏类型+进区域、出区域、进出区域报警 获取该设备fence表中所有valid为1的记录
	 * 先判断定时规则，然后判断是否该位置是否在围栏内
	 * 
	 * @param obdSn
	 *            设备号
	 * @param longitude
	 *            经度,单位度,如113.00000000 长度不限
	 * @param latitude
	 *            纬度,单位度,如23.00000000
	 */
	public void dzwlWarning(String obdSn, String longitude, String latitude,Date gpsTime) {
		log.info("----------【电子围栏预警】设备：" + obdSn + "---经度：" + longitude + "---纬度：" + latitude + "---gps时间:" + gpsTime);
		// 查询fence表中valid为1的记录
		List<Fence> fences = fenceService.queryListByObdSn(obdSn, null, 1);
		if (fences == null || fences.size() == 0) {
			return;
		}
			// 循环处理
			for (Fence fence : fences) {
				try {
					handle(obdSn, longitude, latitude, gpsTime, fence);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("----------【电子围栏预警】设备：" + obdSn + "---异常信息---" + e);
				}
			}
	}

	/**
	 * 先根据定时规则，判断时间是否符合 根据告警类型，判断点是否在围栏内 如果都符合推送告警
	 * 
	 * @param obdSn
	 * @param longitude
	 * @param latitude
	 * @param gpsTime
	 * @param fence
	 * @throws Exception
	 */
	private void handle(String obdSn, String longitude, String latitude,Date gpsTime, Fence fence) throws Exception {
		log.info("----------【电子围栏预警】设备：" + obdSn + "---经度：" + longitude
				+ "---纬度：" + latitude + "---gps时间:" + gpsTime + "---围栏:"+ fence);
		// 1根据定时规则，判断时间是否OK
		boolean timeFlag = timeJudge(gpsTime, fence);
		log.info("----------【电子围栏预警】设备：" + obdSn + "---时间判断结果:" + timeFlag);
		if (!timeFlag) {
			log.info("----------【电子围栏预警】设备：" + obdSn + "---gps时间不在围栏时间内.");
			return;
		}
		Double gpsLon = Double.parseDouble(longitude);// 经度
		Double gpsLat = Double.parseDouble(latitude);// 纬度
		// 判断
		boolean pointFlag = gpsJudge(gpsLon, gpsLat, fence);
		log.info("----------【电子围栏预警】设备：" + obdSn + "---gps坐标是否在围栏内:"+ pointFlag);
		boolean sendFlag = false;// 是否推送预警
		
		String msgType="";//告警类型
		String msgDesc="";//告警描述
		WarnType warnType = null;//告警类型
		Integer areaNum = fence.getAreaNum();//编号
		Integer alert = fence.getAlert();
		switch (alert) {
		case 1:
			// 进区域报警
			if (pointFlag) {
				sendFlag = true;
				msgType = "51";
				msgDesc = "进区域报警.";
				warnType = WarnType.Efence_In;
			}
			break;
		case 2:
			// 出区域报警
			if (!pointFlag) {
				sendFlag = true;
				msgType = "52";
				msgDesc = "出区域报警.";
				warnType = WarnType.Efence_Out;
			}
			break;
		case 3:
			// 进出区域报警
			Map<Integer,Boolean> map=alert3( obdSn, fence);
			if(map.containsKey(1)){
				msgType = "53";
				boolean alert3Type = map.get(1);
				if(pointFlag==alert3Type){
					break;
				}else{
//					msgType = "53";
					sendFlag = true;
				}
				if(pointFlag && !alert3Type){
					msgDesc = "进出区域报警-进区域.";
					warnType = WarnType.Efence_InOut_In;
				}
				if(!pointFlag && alert3Type){
					msgDesc = "进出区域报警-出区域.";
					warnType = WarnType.Efence_InOut_Out;
				}
			}
			break;
		default:
			log.info("----------【电子围栏预警】设备：" + obdSn + "---告警类型有误:"+ alert);
			throw new Exception("告警类型有误:"+alert);
		}
		log.info("----------【电子围栏预警】设备：" + obdSn + "---是否推送预警:"+ sendFlag);
		if(sendFlag){
			boolean mflag = false;
			ObdState obdState = obdSateService.queryByObdSn(obdSn);
			log.info("----------【电子围栏预警】设备：" + obdSn+"---状态记录---"+obdState);
			if (obdState == null) {
				mflag = true;
			} else {
				if (!"0".equals(obdState.getEfenceSwitch())) {
					log.info("----------【OBD设备电子围栏判断】设备：" + obdSn+ "---电子围栏开关打开或未设置.");
					mflag = true;
				}
			}
			if (mflag) {
				pushApi.pushWarnHandler(obdSn, warnType,areaNum,msgType, msgDesc, longitude, latitude, gpsTime);
			}
		}
	}
	/**
	 * 进出区域预警
	 * @param pointFlag
	 * @param obdSn
	 * @return
	 * @throws Exception 
	 */
	private Map<Integer,Boolean> alert3(String obdSn,Fence fence) throws Exception{
		Map<Integer,Boolean> map = new HashMap<Integer, Boolean>();
		CarGSPTrack ct = carGSPTrackService.findLastBySn(obdSn);
		if(ct==null){
			log.info("----------【电子围栏预警】设备：" + obdSn + "---进出区域预警该设备之前没有历史轨迹点.");
			map.put(0, false);
			return map;
		}
		String lonLast = CoordinateTransferUtil.lnglatTransferDouble(ct.getLongitude());
		String latLast = CoordinateTransferUtil.lnglatTransferDouble(ct.getLatitude());
		log.info("----------【电子围栏预警】设备：" + obdSn + "---最后一个轨迹点:"+lonLast+"---"+latLast);
		double lon = Double.parseDouble(lonLast);
		double lat = Double.parseDouble(latLast);
		boolean gpsLastFlag=gpsJudge(lon, lat, fence);
		log.info("----------【电子围栏预警】设备：" + obdSn + "---进出区域预警该设备最后一个轨迹点是否在围栏:"+gpsLastFlag);
		map.put(1, gpsLastFlag);
		return map;
	}
	
	/**
	 * 判断点是否在围栏内
	 * 
	 * @param longitude
	 * @param latitude
	 * @param fence
	 * @return
	 */
	private boolean gpsJudge(double longitude, double latitude, Fence fence)throws Exception {
		Point2D.Double gpsPoint = new Point2D.Double(longitude, latitude);
		String points = fence.getPoints();
		List<Point2D.Double> polygon = new ArrayList<Point2D.Double>();
		JSONArray array = JSONArray.fromObject(points);
		for (Object object : array) {
			JSONObject json = (JSONObject) object;
			double lon = json.optDouble("longitude");
			double lat = json.optDouble("latitude");
			Point2D.Double point = new Point2D.Double(lon, lat);
			polygon.add(point);
		}
		return checkWithJdkGeneralPath(gpsPoint, polygon);
	}

	public boolean checkWithJdkGeneralPath(Point2D.Double point,
			List<Point2D.Double> polygon) {
		java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
		Point2D.Double first = polygon.get(0);
		p.moveTo(first.x, first.y);
		for (Point2D.Double d : polygon) {
			p.lineTo(d.x, d.y);
		}
		p.lineTo(first.x, first.y);
		p.closePath();
		return p.contains(point);
	}

	/**
	 * 时间判断
	 * 
	 * @param gpsTime
	 * @param fence
	 * @return
	 * @throws Exception
	 */
	private boolean timeJudge(Date gpsTime, Fence fence) throws Exception {
		Integer timerType = fence.getTimerType();
		String dayWeek = fence.getDayWeek();
		Date startDate = fence.getStartDate();
		Date endDate = fence.getEndDate();
		Date startTime = fence.getStartTime();
		Date endTime = fence.getEndTime();
		boolean flag = false;
		switch (timerType) {
		case 1:
			flag = dayJudge(gpsTime, dayWeek, startTime, endTime);
			break;
		case 2:
			flag = HmsJudge(gpsTime, startTime, endTime);
			break;
		case 3:
			flag = dateJudge(gpsTime, startDate, endDate, startTime, endTime);
			break;
		default:
			throw new Exception("电子围栏定时规则类型有误.");
		}
		return flag;
	}

	// 星期几
	private boolean dayJudge(Date gpsTime, String dayWeek, Date startTime,
			Date endTime) throws ParseException {
		// 1先判断是星期几
		Integer dInt = dayInt(gpsTime);
		String[] dayArray = dayWeek.split(",");
		Set<Integer> set = new HashSet<>();
		for (String string : dayArray) {
			Integer param = Integer.parseInt(string);
			set.add(param);
		}
		if (!set.contains(dInt)) {
			return false;
		}
		// 判断时间范围
		boolean flag = HmsJudge(gpsTime, startTime, endTime);

		return flag;

	}

	// 判断日期是星期几
	private int dayInt(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer dInt = c.get(Calendar.DAY_OF_WEEK);
		return dInt;
	}

	/**
	 * 日期范围
	 * 
	 * @param gpsTime
	 * @param startDate
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	private boolean dateJudge(Date gpsTime, Date startDate, Date endDate,
			Date startTime, Date endTime) throws ParseException {
		// 把gpsTime转成yyyy-MM-dd时间类型
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String gps = f.format(gpsTime);
		Date gpsDate = f.parse(gps);
		if (gpsDate.before(startDate) || gpsDate.after(endDate)) {
			return false;
		}
		// 判断时间
		boolean flag = HmsJudge(gpsTime, startTime, endTime);
		return flag;
	}

	/**
	 * 判断时间范围
	 * 
	 * @param gpsTime gps时间
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws ParseException
	 */
	private boolean HmsJudge(Date gpsTime, Date startTime, Date endTime)
			throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
		String gps = f.format(gpsTime);
		String start = f.format(startTime);
		String end = f.format(endTime);
		Date gpsDate = f.parse(gps);
		Date startDate = f.parse(start);
		Date endDate = f.parse(end);
		if (gpsDate.before(startDate) || gpsDate.after(endDate)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {

	}
}
