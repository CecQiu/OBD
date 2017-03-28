/**
 * 
 */
package com.hgsoft.carowner.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdBarrier;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdBarrierService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * @author Administrator
 *
 */
@Controller
@Scope("prototype")
public class CarLineAction extends BaseAction {
	@Resource
	private CarGSPTrackService gspTrackService;
	@Resource
	private CarTraveltrackService traveltrackService;
	@Resource
	private MebUserService userService;
	@Resource
	private ObdBarrierService obdBarrierService;

//	private MebUser user;
	private String sn;
	private String snos;
//	private String minX;
//	private String minY;
//	private String maxX;
//	private String maxY;
	
	private String date;
	private String start;
	private String end;
//	private String mobileNumber;
//	private String license;
//	private String obdSn;
//	private String valid;
//	private ObdBarrier obdBarrier;
	//存放用户与是否栅栏信息
	private Map<MebUser,ObdBarrier> map;

	@Resource
	private OBDStockInfoService obdStockInfoService;
	private OBDStockInfo obdStockInfo;
	private List<OBDStockInfo> obdStockInfos=new ArrayList<OBDStockInfo>();

//	//修改区域状态
//	public void update(){
//		if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(valid) && ("1".equals(valid) || "0".equals(valid))){
//			ObdBarrier obdBarrier = obdBarrierService.barrierExist(obdSn);
//			obdBarrier.setValid(Integer.valueOf(valid));
//			if("0".equals(valid)){//设置
//				obdBarrier.setRailAndAlert("02");
//			}else if("1".equals(valid)){//取消
//				obdBarrier.setRailAndAlert("04");
//			}
//			if(obdBarrierService.update(obdBarrier)){
//				outMessage("ok");
//			}else{
//				outMessage("fail");
//			}
//		}else{
//			outMessage("fail");
//		}
//	}
	public String list() {
		if(obdStockInfo==null){
			obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"),Property.eq("valid", "1") );
			return "list";
		}
		String obdSn = obdStockInfo.getObdSn();
		String obdMSn = obdStockInfo.getObdMSn();
		String groupNum = obdStockInfo.getGroupNum();

		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.eq("obdSn", obdSn.trim()));
		}
		if (!StringUtils.isEmpty(obdMSn)) {
			list.add(Property.eq("obdMSn", obdMSn.trim()));
		}
		if (!StringUtils.isEmpty(start)) {
			list.add(Property.ge("startDate", start));
		}
		if (!StringUtils.isEmpty(end)) {
			list.add(Property.le("startDate", end));
		}
		list.add(Property.eq("valid", "1"));
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"), propertyArr);
		return "list";
	}
	
	public String his() {
		if(sn == null) {
			message = "参数错误";
			System.out.println("Sn is null");
			return list();
		}
		return "his";
	}
	
	public String carTravelArea() {
		return "carTravelArea";
	}
 
//	//多个OBDSN
//	public void saveTravelArea() {
//		if(StringUtils.isEmpty(snos) || StringUtils.isEmpty(minX)
//				|| StringUtils.isEmpty(minY) || StringUtils.isEmpty(maxX)
//				|| StringUtils.isEmpty(maxY)){
//			outJsonMessage("{\"status\":\"fail\",\"message\":\"数据获取失败\"}");
//		} else {
//			String[] nos = snos.split("\\|");
//			List<String> sns = new ArrayList<String>();
//			for(String sn:nos) {
//				if(!StringUtils.isEmpty(sn)) {
//					if(userService.isHasOBDSN(sn) ){//&& !obdBarrierService.isHasOBDSN(sn)) {
//						sns.add(sn);
//					}
//				}
//			}
//			int i = 0;
//			if(sns.size() > 0) {
//				i = obdBarrierService.saveMore(sns, minX, minY, maxX, maxY, 1, "02"); 
//			}
//			outJsonMessage("{\"status\":\"fail\",\"message\":\"共" + sns.size() + "条,有"+i+"条保存成功！\"}");
//		}
//	}
	
//	//单个OBDSN
//	public void setAreaToOne() {
//		if(StringUtils.isEmpty(sn) || StringUtils.isEmpty(minX)
//				|| StringUtils.isEmpty(minY) || StringUtils.isEmpty(maxX)
//				|| StringUtils.isEmpty(maxY)){
//			outJsonMessage("{\"status\":\"fail\",\"message\":\"数据获取失败\"}");
//		} else {
//			ObdBarrier oBarrier = new ObdBarrier();
//			oBarrier.setAreaNum(1);
//			oBarrier.setMaxLatitude(maxY);
//			oBarrier.setMaxLongitude(maxX);
//			oBarrier.setMinLatitude(minY);
//			oBarrier.setMinLongitude(minX);
//			oBarrier.setObdSn(sn);
//			oBarrier.setRailAndAlert("02");
//			oBarrier.setTime(new Date());
//			oBarrier.setValid(0);
//			obdBarrierService.save(oBarrier); 
//			outJsonMessage("{\"status\":\"fail\",\"message\":\"保存成功\"}");
//		}
//	}
	
	public void getRoute() {
		String st = null;
		String en = null;
		Pattern p = Pattern.compile("20[0-9]{2}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{1,2}:[0-9]{1,2}");
		if(start == null || start.trim().equals("")) {
			start = date + " " + "00:00:00";
		} else {
			start = date + " " + start;
		}
		if(end == null || end.trim().equals("")) {
			end = date + " " + "23:59:59";
		} else {
			end = date + " " + end;
		}
		if(date != null && !date.trim().equals("")) {
			Matcher m1 = p.matcher(start);
			Matcher m2 = p.matcher(end);
			if(m1.matches()) {
				st = start;
			}
			if(m2.matches()) {
				en = end;
			}
			if(st == null || en == null) {
				outJsonMessage("{\"status\":\"fail\",\"message\":\"日期格式不正确\"}");
			} else {
				list = gspTrackService.queryTrack(sn, st, en);
//				System.out.println("first post:"+list.get(0));
//				System.out.println("end point:"+list.get(list.size()-1));
				list = filterRoute(list);
				JSONArray array = new JSONArray();
				JsonConfig config = new JsonConfig();
				config.setExcludes(new String[]{"", ""});
				array.addAll(list);
				outJsonMessage("{\"status\":\"success\",\"message\":" + array.toString() + "}");
			}
		} else {
			outJsonMessage("{\"status\":\"fail\",\"message\":\"请选择日期\"}");
		}
	}

//	public MebUser getUser() {
//		return user;
//	}
//
//	public void setUser(MebUser user) {
//		this.user = user;
//	}

//	public String getSn() {
//		return sn;
//	}
//
//	public void setSn(String sn) {
//		this.sn = sn;
//	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public Map<MebUser, ObdBarrier> getMap() {
		return map;
	}
	public void setMap(Map<MebUser, ObdBarrier> map) {
		this.map = map;
	}

//	public String getObdSn() {
//		return obdSn;
//	}
//
//	public void setObdSn(String obdSn) {
//		this.obdSn = obdSn;
//	}

	@SuppressWarnings("rawtypes")
	private List filterRoute(List list) {
		if(list != null) {
			for(int i=0;i<list.size();i++) {
				Object object = list.get(i);
				if(object instanceof CarGSPTrack) {
					CarGSPTrack track = (CarGSPTrack) object;
					if(track != null ){
						String lat = track.getLatitude();
						track.setLatitude(changePoint(lat));
						String lnt = track.getLongitude();
						track.setLongitude(changePoint(lnt));
					}
				}
			}
		}
		return list;
	}
	
	private String changePoint(String gps) {
		String ret = null;
		if(!StringUtils.isEmpty(gps)){
			try {
				int ind_d = gps.indexOf("°");
				int ind_f = gps.indexOf("'");
				String du = gps.substring(0, ind_d);
				String fen = gps.substring(ind_d + 1, ind_f);
				double d = Double.parseDouble(du);
				BigDecimal decimal = new BigDecimal(fen);
				BigDecimal devile = new BigDecimal("60");
				decimal = decimal.divide(devile,11, BigDecimal.ROUND_HALF_EVEN);
				d = d + decimal.doubleValue();
				ret = d + "";
				if(ret.length() >= ret.indexOf(".") + 11) {
					ret = ret.substring(0, ret.indexOf(".") + 11);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return ret;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public OBDStockInfo getObdStockInfo() {
		return obdStockInfo;
	}

	public void setObdStockInfo(OBDStockInfo obdStockInfo) {
		this.obdStockInfo = obdStockInfo;
	}

	public List<OBDStockInfo> getObdStockInfos() {
		return obdStockInfos;
	}

	public void setObdStockInfos(List<OBDStockInfo> obdStockInfos) {
		this.obdStockInfos = obdStockInfos;
	}

	public String getSnos() {
		return snos;
	}

	public void setSnos(String snos) {
		this.snos = snos;
	}

	
}