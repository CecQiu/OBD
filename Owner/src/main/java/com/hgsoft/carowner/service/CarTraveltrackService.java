package com.hgsoft.carowner.service;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.application.vo.Track;
import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.dao.CarTraveltrackDao;
import com.hgsoft.carowner.entity.CarDriveInfo;
import com.hgsoft.carowner.entity.CarOilInfo;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.WarnType;

@Service
public class CarTraveltrackService extends BaseService<CarTraveltrack> {
	
	@Resource
	private CarTraveltrackDao carTraveltrackDao;
	@Resource
	private MebUserService userService;
	
	private static Logger obdHandlerTravelLogger = LogManager.getLogger("obdHandlerTravelLogger");
	@Resource
	private CarDriveInfoService carDriveInfoService;
	@Resource
	private CarOilInfoService carOilInfoService;
	@Resource
	private CarInfoService carInfoService;
	@Resource
	private DriveTimeUtil driveTimeUtil;
	@Resource
	private PushApi pushApi;
	@Resource
	private ObdSateService obdSateService;
	
	public CarTraveltrackDao getDao() {
		return (CarTraveltrackDao)super.getDao();
	}
	
	@Resource
	public void setDao(CarTraveltrackDao carTraveltrackDao){
		super.setDao(carTraveltrackDao);
	}

	public void add(CarTraveltrack ct) {
		getDao().save(ct);
	}

	public void carTraveltrackUpdate(CarTraveltrack ct) {
		carTraveltrackDao.updateCarTraveltrack(ct);
	}
	/**
	 * 查询用户的行程记录
	 * @param obdSn 设备号
	 * @param mobileNumber 手机号
	 * @param license 车牌号
	 * @param starTime 开始时间
	 * @param endTime 结束时间
	 * @param pager 分页
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Track> getTravelTracks(String obdSn,String starTime,String endTime,Pager pager){
		String hql="select c.obdsn,date_format(c.travelEnd,'%Y-%m-%d'),sum(c.distance) from CarTraveltrack c where 1=1  and c.type!=2";
		if(obdSn!=null&&obdSn.trim().length()>0){
			hql+=" and c.obdsn like '%"+obdSn+"%'";
		}
		if(starTime!=null&&!starTime.trim().equals("")){
			hql+=" and c.travelEnd >= '"+starTime+" 00:00:00"+"'";
		}
		if(endTime!=null&&!endTime.trim().equals("")){
			hql+=" and c.travelEnd <= '"+endTime+" 23:59:59"+"'";
		}
		hql+="GROUP BY c.obdsn,DATE_FORMAT(c.travelEnd,'%Y-%m-%d') ORDER BY c.insesrtTime DESC";
//		List list = getDao().findByHql(hql, null);
		List<Track> trackList=new ArrayList<Track>();
		List list = this.getDao().findByHql2(hql, pager);
		for (int i=0;i<list.size();i++) {
			Object[] obj=(Object[]) list.get(i);
			Track track = new Track();
			track.setObdSn(obj[0].toString());
			track.setDate1((Date)DateUtil.fromatDate(obj[1].toString(), "yyyy-MM-dd"));
			track.setDistance(obj[2].toString());
			trackList.add(track);
		}
		return trackList;
	}
	
	
	/**
	 * 行程记录导出
	 * @param obdSn
	 * @param mobileNumber
	 * @param license
	 * @param starTime
	 * @param endTime
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List trackExport(String obdSn,String starTime,String endTime) throws ParseException{
		
//		String hql="select c.obdsn,c.mebUser.license,c.mebUser.name,c.mebUser.mobileNumber,date_format(c.insesrtTime,'%Y-%m-%d'),sum(c.distance) from CarTraveltrack c where 1=1";
		String hql="select c.obdsn,date_format(c.travelEnd,'%Y-%m-%d'),sum(c.distance) from CarTraveltrack c  where 1=1 and c.type!=2";
		if(obdSn!=null&&obdSn.trim().length()>0){
			hql+=" and c.obdsn = '"+obdSn+"'";
		}
		if(starTime!=null&&!starTime.trim().equals("")){
			hql+=" and c.travelEnd >= '"+starTime+" 00:00:00"+"'";
		}
		if(endTime!=null&&!endTime.trim().equals("")){
			hql+=" and c.travelEnd <= '"+endTime+" 23:59:59"+"'";
		}
		hql+="GROUP BY c.obdsn,DATE_FORMAT(c.travelEnd,'%Y-%m-%d') ORDER BY c.insesrtTime DESC";
		List list = getDao().findByHql(hql, null);
		List trackList= new ArrayList();
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[]) list.get(i);
			Map map = new HashMap();
			map.put("obdSn", obj[0]);
			map.put("insesrtTime", obj[1]);
			long dis=(long) obj[2];
			map.put("distance", ((double)dis/100d));
			trackList.add(map);
		}
		return trackList;
	}
  
	public CarTraveltrack findLastBySN(String sn) {
		return getDao().findLastBySn(sn);
	}
	
	public CarTraveltrack findLastByN(int n) {
		return getDao().findLastByN(n);
	}
	/**
	 * 获取某天的全部行程，经纬度
	 * @param obdSN
	 * @param insesrtTime
	 * @return
	 * @throws ParseException
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public  List<Track> trackData(String obdSN,String travelEnd,Pager pager) throws Exception{
		//1获取行程记录
		String hql="from CarTraveltrack c where 1=1 and c.type!=2";
		hql+=" and c.obdsn ='"+obdSN+"'";
		hql+=" and c.travelEnd like '%"+travelEnd+"%'";
		hql+=" order by c.travelEnd desc";
		List<CarTraveltrack> trackList = (List<CarTraveltrack>) this.getDao().findByHql2(hql, pager);
		//2获取用户信息
//		MebUser mebUser = userService.queryByObdSn(obdSN);//查询对应的用户
		List<Track> list=new ArrayList();
		if(trackList!=null&&trackList.size()>0){
			for (int i = 0; i < trackList.size(); i++) {
				CarTraveltrack ctt = trackList.get(i);
				Track track=new Track();
				track.setId(IDUtil.createID());
				//3开始结束时间
				track.setTravelEnd(ctt.getTravelEnd());//结束时间
				track.setTravelStart(ctt.getTravelStart());//开始时间
				
				//4车牌号码
//				track.setLicense(mebUser.getLicense());//车牌号码
				track.setDistance(String.valueOf(ctt.getDistance()));//行程				
				if(ctt.getDistance()==0){
					track.setDistance("1");//行程
				}
				//5总油耗
				Double totalFuel=Double.valueOf(ctt.getTotalFuel());
				track.setTotalFuel(String.valueOf(totalFuel/1000));//油耗
				//6驾驶时长
				long ss=ctt.getTravelEnd().getTime()-ctt.getTravelStart().getTime();
				track.setTotalTime(DateUtil.hms(ss));//总时长
//				BigDecimal sss = new BigDecimal(ss);
//				BigDecimal hour = new BigDecimal(60l * 60l * 1000l);
//				Calendar c = Calendar.getInstance();
//				c.setTimeInMillis(ss);//毫秒
//				track.setTotalTime(c.get(Calendar.HOUR_OF_DAY)-8+"h " + c.get(Calendar.MINUTE)+" min");
				//7总行程
//				Double distan=Double.valueOf(ctt.getDistance());
//				BigDecimal distance=new BigDecimal(ctt.getDistance());
				double distance=Double.valueOf(ctt.getDistance());
				double timeD=(Double.valueOf(ss))/(1000d*60d*60d);
				
				if(timeD>0d){
					double speedD=distance/timeD;
					track.setAvgSpeed(String.format("%.3f", speedD));
				}else{
					track.setAvgSpeed("0");
				}
				
				//8平均车速
// 				track.setAvgSpeed(distance.divide(sss.divide(hour, 3, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)+"");
				//9设备号
				track.setObdSn(obdSN);
				//10插入时间
				track.setInsesrtTime(travelEnd);
				//11用户姓名
				track.setUserName(obdSN);
				//轨迹查询
				String gpsSql="select longitude,latitude from car_gsptrack where obdSn='"+obdSN+"' and GSPTrackTime between '"+ctt.getTravelStart()+"' and '"+ctt.getTravelEnd()+"' order by gspTrackTime asc";
				List gpsList=this.getDao().queryBySQL(gpsSql, null);
				//解析起点和终点地址中文名称
				if(gpsList!=null && gpsList.size()>0){
					BigDecimal mp = new BigDecimal(60l);
					Pattern p = Pattern.compile("(\\d*)°(\\d*\\.\\d*)'");
					//单个点不解析
					//起点,如果经纬度不规范,返回错误
					Object[] startPoint=(Object[])gpsList.get(0);
					Matcher sm = p.matcher(String.valueOf(startPoint[0]));
					Matcher sn = p.matcher(String.valueOf(startPoint[1]));
					if(sm.matches() && sn.matches()) {
						String startLongitude=(String)startPoint[0];
						String startLatitude =(String)startPoint[1];
						track.setStartLongitude(CoordinateTransferUtil.lnglatTransferDouble(startLongitude));
						track.setStartLatitude(CoordinateTransferUtil.lnglatTransferDouble(startLatitude));
					}else{
						track.setStarPoint("经纬度解析异常.");
					}
					
					if(gpsList.size()>1){
						//终点
						Object[] endPoint=(Object[])gpsList.get(gpsList.size()-1);
						Matcher em = p.matcher(String.valueOf(endPoint[0]));
						Matcher en = p.matcher(String.valueOf(endPoint[1]));
						if(em.matches() && en.matches()) {
							String endLongitude=(String)endPoint[0];
							String endLatitude =(String)endPoint[1];
							track.setEndLongitude(CoordinateTransferUtil.lnglatTransferDouble(endLongitude));
							track.setEndLatitude(CoordinateTransferUtil.lnglatTransferDouble(endLatitude));
						}else{
							track.setEndPoint("经纬度解析异常.");
						}
					}else{
						track.setEndPoint("无轨迹信息.");
					}
						
//						for (int j = 0; j < gpsList.size(); j++) {
//							if(j==0 || j==gpsList.size()-1){
//								Object[] gps=(Object[])gpsList.get(j);
//								
//								Matcher m = p.matcher(String.valueOf(gps[0]));
//								Matcher n = p.matcher(String.valueOf(gps[1]));
//								if(m.matches()&&n.matches()) {
//		
//									BigDecimal gp = new BigDecimal(m.group(2));
//									BigDecimal np = new BigDecimal(n.group(2));
//									GpsAddressUtil address=new GpsAddressUtil();
//									//起点地址中文解析
//									if(j==0){
//										String x=new BigDecimal(m.group(1)).add(gp.divide(mp,6,RoundingMode.HALF_UP))+"";
//										String y=new BigDecimal(n.group(1)).add(np.divide(mp,6,RoundingMode.HALF_UP))+"";
//										System.out.println(address.getAddress(x, y)+"&&&&&&&&&&&&&&&&&&&起始点");					
//										track.setStarPoint(address.getAddress(x, y));
//									}
//									//终点地址中文
// 									if(j==gpsList.size()-1){
//										String x=new BigDecimal(m.group(1)).add(gp.divide(mp,6,RoundingMode.HALF_UP))+"";
//										String y=new BigDecimal(n.group(1)).add(np.divide(mp,6,RoundingMode.HALF_UP))+"";
//										System.out.println(address.getAddress(x, y)+"&&&&&&&&&&&&&&&&&&&终点");	
//										track.setEndPoint(address.getAddress(x, y));
//									}
//								}else {
//									System.out.println("not match");
//									track.setStarPoint("经纬度解析异常");
//									track.setEndPoint("经纬度解析异常");
//								}
//							}
//						}
				}else{
					track.setStarPoint("无轨迹信息");
					track.setEndPoint("无轨迹信息");
				}
				list.add(track);
			}
		}
		//pager.setTotalPage(list.size());
		
		return list;
	}
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1200000);
		System.out.println(c.getTime());
		System.out.println(c.get(Calendar.HOUR_OF_DAY));
		System.out.println(c.get(Calendar.MINUTE));
//		System.out.println(c.get);
	}
	
	/**
	 * 获取某段行程的所有点坐标
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public List poitList(String starTime,String endTime,String obdSn){
		String gpsSql="select longitude,latitude from car_gsptrack where GSPTrackTime >='"+starTime+"' and GSPTrackTime<='"+endTime+"'" + "and obdSn ='" +obdSn+ "' order by gspTrackTime asc";
		List gpsList=this.getDao().queryBySQL(gpsSql, null);
		BigDecimal mp = new BigDecimal(60l);
		Pattern p = Pattern.compile("(\\d*)°(\\d*\\.\\d*)'");
		List list=new ArrayList();
		if(gpsList!=null&&gpsList.size()>0){
			for (int j = 0; j < gpsList.size(); j++) {
				Object[] track=(Object[])gpsList.get(j);			
				Matcher m = p.matcher(String.valueOf(track[0]));
				Matcher n = p.matcher(String.valueOf(track[1]));
				if(m.matches()&&n.matches()) {
					Map map=new HashMap();
					BigDecimal gp = new BigDecimal(m.group(2));
					BigDecimal np = new BigDecimal(n.group(2));
					String poit=new BigDecimal(m.group(1)).add(gp.divide(mp,6,RoundingMode.HALF_UP))+","+new BigDecimal(n.group(1)).add(np.divide(mp,6,RoundingMode.HALF_UP));
					map.put("x", new BigDecimal(m.group(1)).add(gp.divide(mp,6,RoundingMode.HALF_UP)));
					map.put("y", new BigDecimal(n.group(1)).add(np.divide(mp,6,RoundingMode.HALF_UP)));
					list.add(map);
				} else {
					System.out.println(track[0]+"***"+track[1]);
					System.out.println("not match");
				}
				
			}
		}
		System.out.println(list.size());
		return list;
	}
	
	/**
	 * 驾驶优化
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public CarTraveltrack DrivingBetter(String starTime,String endTime,String obdSn){
		String sql="SELECT SUM(c.overspeedTime),SUM(c.brakesNum),SUM(c.quickTurn),SUM(c.quickenNum),SUM(c.quickSlowDown),SUM(c.quickLaneChange),SUM(c.engineMaxSpeed),SUM(c.speedMismatch),SUM(c.idling) "
				+ "FROM car_traveltrack c "
				+ "where c.type!=2 and c.obdsn='"+obdSn+"' and c.travelStart >='"+starTime+"' and c.travelEnd<='"+endTime+"'";
		List list=this.getDao().queryBySQL(sql, null);
		CarTraveltrack ct = new CarTraveltrack();
		if(list!=null&&list.size()>0){
			Object[] track=(Object[])list.get(0);
			ct.setOverspeedTime(track[0]!=null?Integer.parseInt(track[0].toString()):0);//超速次数
			ct.setBrakesNum(track[1]!=null?Long.parseLong(track[1].toString()):0L);//急刹车次数
			ct.setQuickTurn(track[2]!=null?Integer.parseInt(track[2].toString()):0);//急转弯次数
			ct.setQuickenNum(track[3]!=null?Long.parseLong(track[3].toString()):0L);//急加速次数
			ct.setQuickSlowDown(track[4]!=null?Integer.parseInt(track[4].toString()):0);//急减速次数
			ct.setQuickLaneChange(track[5]!=null?Integer.parseInt(track[5].toString()):0);//急变道次数
			ct.setEngineMaxSpeed(track[6]!=null?Integer.parseInt(track[6].toString()):0);//发动机最高转速次数
			ct.setSpeedMismatch(track[7]!=null?Integer.parseInt(track[7].toString()):0);//车速转速不匹配
			ct.setIdling(track[8]!=null?Integer.parseInt(track[8].toString()):0);//车速转速不匹配
		}
		return ct;
	}
	/**
	 * 驾驶优化
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<CarTraveltrack> DriveBetterDay(String starTime,String endTime,String obdSn){
		String sql="SELECT DATE_FORMAT(c.travelEnd, '%Y-%m-%d'),SUM(c.overspeedTime),SUM(c.brakesNum),SUM(c.quickTurn),SUM(c.quickenNum),SUM(c.quickSlowDown),SUM(c.quickLaneChange),SUM(c.engineMaxSpeed),SUM(c.speedMismatch),SUM(c.idling) "
				+ "FROM car_traveltrack c "
				+ "where c.type!=2 and c.obdsn='"+obdSn+"' and c.travelStart >='"+starTime+"' and c.travelEnd<='"+endTime+"' GROUP BY DATE_FORMAT(c.travelEnd,'%Y-%m-%d') ORDER BY c.travelEnd DESC";
		List list=this.getDao().queryBySQL(sql, null);
		List<CarTraveltrack> ctList = new ArrayList<CarTraveltrack>();
		if(list!=null){
			for (int i=0;i<list.size();i++) {
				Object[] track=(Object[])list.get(i);
				CarTraveltrack ct = new CarTraveltrack();
				ct.setTravelEnd((Date)DateUtil.fromatDate(track[0].toString(), "yyyy-MM-dd"));
				ct.setOverspeedTime(track[1]!=null?Integer.parseInt(track[1].toString()):0);//超速次数
				ct.setBrakesNum(track[2]!=null?Long.parseLong(track[2].toString()):0L);//急刹车次数
				ct.setQuickTurn(track[3]!=null?Integer.parseInt(track[3].toString()):0);//急转弯次数
				ct.setQuickenNum(track[4]!=null?Long.parseLong(track[4].toString()):0L);//急加速次数
				ct.setQuickSlowDown(track[5]!=null?Integer.parseInt(track[5].toString()):0);//急减速次数
				ct.setQuickLaneChange(track[6]!=null?Integer.parseInt(track[6].toString()):0);//急变道次数
				ct.setEngineMaxSpeed(track[7]!=null?Integer.parseInt(track[7].toString()):0);//发动机最高转速次数
				ct.setSpeedMismatch(track[8]!=null?Integer.parseInt(track[8].toString()):0);//车速转速不匹配
				ct.setIdling(track[9]!=null?Integer.parseInt(track[9].toString()):0);//怠速次数
				ctList.add(ct);
			}
		}
		
		return ctList;
	}
	/**
	 * 驾驶优化
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CarTraveltrack> getCarTravelsByTime(Date starTime,Date endTime,String obdSn){
		String hql="FROM CarTraveltrack c where c.type!=2 and c.obdsn=? and c.travelStart >=? and c.travelEnd<=? order by c.insesrtTime desc";
		List<CarTraveltrack> ctList=(List<CarTraveltrack>)this.getDao().queryByHQL(hql, obdSn,starTime,endTime);
		return ctList;
	}
	
	/**
	 * 根据obdSn号和开始时间，结束时间查询
	 * @param obdSn 设备号
	 * @param mobileNumber 手机号
	 * @param license 车牌号
	 * @param starTime 开始时间
	 * @param endTime 结束时间
	 * @param pager 分页
	 * @return
	 */
	public CarTraveltrack getTravelByObdAndTime(String obdSn, Date starTime,Date endTime ){
		CarTraveltrack ct=carTraveltrackDao.getTravelByObdAndTime(obdSn, starTime, endTime);
		return ct;
	}
	
	public List<CarTraveltrack> queryTravelByObdSnTime(String obdSn, String starTime,String endTime){
		return carTraveltrackDao.queryTravelByObdSnTime(obdSn, starTime, endTime);
	}

	/**
	 * 查询行程记录
	 * @param obdSn 设备号
	 * @param travelStart 行程开始时间
	 * @return
	 */
	public List<CarTraveltrack> queryTravel(String obdSn, Date travelStart) {
		return carTraveltrackDao.queryTravel(obdSn,travelStart);
	}
	
	public List<CarTraveltrack> queryByParams(Pager pager,Map<String,Object>map) {
		return this.getDao().queryByParams(pager, map);
	}

	public void travelDataSave(String obdSn, String command,CarTraveltrack carTraveltrack, CarDriveInfo carDriveInfo,CarOilInfo carOilInfo) throws Exception {
		//ID关联
		carDriveInfo.setDriveInfoId(carTraveltrack.getId());
		carOilInfo.setOilInfoId(carTraveltrack.getId());
		//去重
		if(getTravelByObdAndTime(obdSn,carTraveltrack.getTravelStart(),carTraveltrack.getTravelEnd()) == null){
			carTraveltrack.setInsesrtTime(new Date());
			
			List<CarTraveltrack> carTraveltracks = queryTravel(obdSn,carTraveltrack.getTravelStart());
			obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库【(半条行程)/(序号递增，重复数据)作废】条数："+carTraveltracks.size());
			boolean noValid = false;//当前行程是否无效
			for (CarTraveltrack ct : carTraveltracks) {
				if(ct != null){
					if(ct.getTravelEnd().getTime() > carTraveltrack.getTravelEnd().getTime()){//日期小，不更新日期大的数据
						noValid = true;
						continue;
					}
					//行程
					String id = ct.getId();
					ct.setType(2);// //排除序号递增，重复数据去除；或者半条去除
					carTraveltrackUpdate(ct);
					//驾驶行为
					CarDriveInfo driveInfo = carDriveInfoService.queryByIdAndObdSn(id, obdSn);
					if(driveInfo != null){
						driveInfo.setType(2);
						carDriveInfoService.carDriveInfoUpdate(driveInfo);
					}
					//油耗
					CarOilInfo oilInfo = carOilInfoService.queryByIdAndObdSn(id, obdSn);
					if(oilInfo != null){
						oilInfo.setType(2);
						carOilInfoService.carOilInfoUpdate(oilInfo);
					}
				}
			}
			
			if("-1".equals(command)){//是半条行程
				carTraveltrack.setType(1);
				add(carTraveltrack);
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功【半条行程】-----");
				carDriveInfo.setType(1);
				carDriveInfoService.carDriveInfoSave(carDriveInfo);
				obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功【半条驾驶行为】-----");
				carOilInfo.setType(1);
				carOilInfoService.carOilSave(carOilInfo);
				obdHandlerTravelLogger.info("<"+obdSn+">-----油耗入库成功【半条油耗】-----");
			} else { //离线数据/正常
				carTraveltrack.setType(noValid ? 2:0);//无效行程则设置为2
				add(carTraveltrack);
				carDriveInfo.setType(noValid ? 2:0);
				carDriveInfoService.carDriveInfoSave(carDriveInfo);
				carOilInfo.setType(noValid ? 2:0);
				carOilInfoService.carOilSave(carOilInfo);
				if(StringUtils.isEmpty(command)){
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功【离线行程数据】-----");
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功【离线驾驶行为数据】-----");
					obdHandlerTravelLogger.info("<"+obdSn+">-----油耗入库成功【离线油耗数据】-----");
				}else{
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库成功-----");
					obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行为入库成功-----");
					obdHandlerTravelLogger.info("<"+obdSn+">-----油耗入库成功-----");
					
					//熄火，移除正在驾驶，记录开始休息
					if(driveTimeUtil.sendedTiredByRedis(obdSn)){//已经发送了疲劳驾驶才有消除疲劳的告警
						driveTimeUtil.enterSleepDateByRedis(obdSn, new Date());
						driveTimeUtil.putDrivingByRedis(obdSn,"0");
						driveTimeUtil.putRestingByRedis(obdSn, "1");
						driveTimeUtil.startRestByRedis(obdSn, new Date());
						driveTimeUtil.removeSendedTiredByRedis(obdSn);//移除发生疲劳告警
						ObdState obdState = obdSateService.queryByObdSn(obdSn);
						if(obdState != null/* && "1".equals(obdState.getValid())*///暂时去除，逻辑有问题
								&& "1".equals(obdState.getFatigueDriveSwitch())){
							pushApi.pushWarnHandler(obdSn,WarnType.FatigueDriveCancel,"00", "消除疲劳驾驶告警");
						}
					}
				}
			}
		}else{
			obdHandlerTravelLogger.info("<"+obdSn+">-----驾驶行程入库失败：库内已有此记录！-----");
		}
	}
}
