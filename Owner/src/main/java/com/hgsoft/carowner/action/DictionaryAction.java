/**
 * 
 */
package com.hgsoft.carowner.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.carowner.service.PositionInfoService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.Gps;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.PositionUtil;
import com.hgsoft.common.utils.Property;

/**
 * @author Administrator
 */
@Controller
@Scope("prototype")
public class DictionaryAction extends BaseAction {
	private final Log logger = LogFactory.getLog(DictionaryAction.class);
//	@Resource
//	private PositionalInformationService positionService;
	@Resource
	private PositionInfoService positionService;
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private CarGSPTrackService gspTrackService;
	@Resource
	private MebUserService userService;
	@Resource
	private CarTraveltrackService carTraveltrackService;
	@Resource
	private MebUserService mebUserService;
	
	private String sn;
//	private PositionalInformation position;
	private PositionInfo position;
	private List<MebUser> list=new ArrayList<MebUser>();
	private String mobileNumber;
	private String license;
	private String lng;
	private String lat;
	private String obdSn;
	private List<OBDStockInfo> obdStockInfos=new ArrayList<OBDStockInfo>();
	@Resource
	private ObdGroupService obdGroupService;
	private List<ObdGroup> obdGroups = new ArrayList<ObdGroup>();
	private OBDStockInfo obdStockInfo;
	private String starTime;
	private String endTime;
	private MebUser mebUser;

	
	//obd设备列表
	public String obdList(){
		//清除缓存
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		obdGroups = obdGroupService.queryList();
		// 分页获取对象
		obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"),Property.eq("valid","1"));
		return "obdList";
	}
	
	// 从数据库中查询数据
		public String query() {
			//清除缓存
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.removeAttribute("currentPage");
			session.removeAttribute("pageSize");
			session.removeAttribute("rowIndex");
			session.removeAttribute("pname");
			session.removeAttribute("pvalue");
			session.removeAttribute("ptype");
			session.removeAttribute("remark");

			String obdSn = obdStockInfo.getObdSn();
			String obdMSn = obdStockInfo.getObdMSn();
			String obdId = obdStockInfo.getObdId();
			String groupNum = obdStockInfo.getGroupNum();

			List<Property> list = new ArrayList<Property>();
			if (obdSn != null && !"".equals(obdSn)) {
				list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
			}
			if (obdMSn != null && !"".equals(obdMSn)) {
				list.add(Property.like("obdMSn", "%"+obdMSn.trim()+"%"));
			}
			if (obdId != null && !"".equals(obdId)) {
				list.add(Property.like("obdId", "%"+obdId.trim()+"%"));
			}
			if (groupNum != null && !"".equals(groupNum)) {
				list.add(Property.eq("groupNum", groupNum.trim()));
			}
			if (starTime != null && !"".equals(starTime)) {
				list.add(Property.ge("startDate", starTime));
			}
			if (endTime != null && !"".equals(endTime)) {
				list.add(Property.le("startDate", endTime));
			}
			list.add(Property.eq("valid", "1"));
			Property[] propertyArr = new Property[list.size()];
			list.toArray(propertyArr);
			obdGroups = obdGroupService.queryList();
			obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"), propertyArr);
			return "obdList";
		}
		
	
	public String list(){
		System.out.println(obdSn+"*********");
		mebUser = userService.queryByObdSn(obdSn);
		return "list";
	}
	
	public String search(){
		list=mebUserService.queryAll(mobileNumber, license,obdSn, pager);	
		return "carInfoList";
	}

	/**
	 * 根据经纬度获取
	 */
	public void lnglatChange(){
		outJsonMessage(changePoint(lng)+","+changePoint(lat));
	}
	
	public String getCar(){
		
		return "carInfo";
	}
	
	public void bd2gps(){
		Gps gps = PositionUtil.bd09_To_Gps84(Double.parseDouble(lat), Double.parseDouble(lng));
		String lat = String .format("%.6f",gps.getWgLat());
		String lng = String .format("%.6f",gps.getWgLon());
		outMessage(lat+","+lng);
	}
	
	public void carInfo() {
		int code = -1;
		String desc = "获取信息失败";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(obdSn)) {
			desc = "信息不全，请求失败！";
		} else {
			// 获取最新行程作为车辆的信息：车速、转速、温度
			CarTraveltrack carTraveltrack = carTraveltrackService.findLastBySN(obdSn);
			if (carTraveltrack != null) {
				resultJso.put("rotationalSpeed",
						carTraveltrack.getRotationalSpeed());// 转速
				resultJso.put("temperature", carTraveltrack.getTemperature());// 温度
				resultJso.put("speed", carTraveltrack.getSpeed());// 速度
				code = 0;
				desc = "获取信息成功";
			}
		}
		JSONObject retJso =  new JSONObject();
		retJso.put("code", code);
		retJso.put("desc", desc);
		retJso.put("result", resultJso);
		outJsonMessage(retJso.toString());
	}
	
	public void getPosition() {
		if(sn != null && !sn.trim().equals("")) {
			position = positionService.findLastBySN(sn);
			if(position != null) {
				String lat = position.getLatitude();
				position.setLatitude(changePoint(lat));
				String lnt = position.getLongitude();
				position.setLongitude(changePoint(lnt));
				try {
					message = JSONObject.fromObject(position).toString();
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(e);
				}
			} else {
				message = "{}";
			}
			outJsonMessage("{\"status\":\"success\",\"message\":" + message + "}");
		}else{
			outJsonMessage("{\"status\":\"fail\",\"message\":\"没有要获取的数据\"}");
		}
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setPosition(PositionInfo position) {
		this.position = position;
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
	
	
	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public List<MebUser> getList() {
		return list;
	}

	public void setList(List<MebUser> list) {
		this.list = list;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public List<OBDStockInfo> getObdStockInfos() {
		return obdStockInfos;
	}

	public void setObdStockInfos(List<OBDStockInfo> obdStockInfos) {
		this.obdStockInfos = obdStockInfos;
	}

	public List<ObdGroup> getObdGroups() {
		return obdGroups;
	}

	public void setObdGroups(List<ObdGroup> obdGroups) {
		this.obdGroups = obdGroups;
	}

	public OBDStockInfo getObdStockInfo() {
		return obdStockInfo;
	}

	public void setObdStockInfo(OBDStockInfo obdStockInfo) {
		this.obdStockInfo = obdStockInfo;
	}

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public MebUser getMebUser() {
		return mebUser;
	}

	public void setMebUser(MebUser mebUser) {
		this.mebUser = mebUser;
	}
	
}
