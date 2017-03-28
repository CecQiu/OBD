package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * 车主通平台与客户端接口:电子围栏查询
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class BindAction extends BaseAction {

	private static Logger obdApiLogger = LogManager.getLogger("obdApiLogger");

	/** 接收的json数据 */
	private String jsonStr;
	/** 权限检验-成功编码 */
	private final String CHECK_SUCCESS = "01";
	/** 权限检验-失败编码 */
	private final String CHECK_ERROR = "02";

	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ObdApiRecordService obdApiRecordService;
	@Resource
	private ObdSettingService obdSettingService;
	@Resource
	private MebUserService userService;
	@Resource
	private CarInfoService carInfoService;

//	private SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
//	private SimpleDateFormat Hms = new SimpleDateFormat("HH:mm:ss");
//	private SimpleDateFormat yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 接口入口，权限校验
	 */
	public void entryInterface() {
		ObdApiRecord oar = new ObdApiRecord();
		oar.setId(IDUtil.createID());
		oar.setCreateTime(new Date());
		long begin = System.currentTimeMillis();
		JSONObject returnJson = null;
		HttpServletRequest request = null;
		// 请求用户
		String requestUser = "";
		String dev =null;
		try {
			request = ServletActionContext.getRequest();
			// POST时获得参数
			if (null == jsonStr || "".equals(jsonStr)) {
				// 获取POST参数值
				BufferedReader br = request.getReader();
				String buffer = null;
				StringBuffer buff = new StringBuffer();
				while ((buffer = br.readLine()) != null) {
					buff.append(buffer);
				}
				br.close();
				jsonStr = buff.toString();
			}
			if (null != deviceId && !"".equals(deviceId)) {
				JSONObject json = new JSONObject();
				json.put("deviceId", deviceId);
				json.put("username", username);
				json.put("time", time);
				json.put("sign", sign);
				jsonStr = json.toString();
			}

			String path = request.getRequestURI();
			JSONObject jo = JSONObject.fromObject(jsonStr);
			dev = jo.optString("deviceId");
			oar.setObdMsn(dev);
			oar.setUrl(path);
			oar.setParam(jsonStr);
			oar.setMethod("bind");

			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);

			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.bind(recJson);
			} else {
				String errorStr = "it is not pass the permissions check,换句话说：就是权限校验没过，自己看着办";
				returnJson = createReturnJson(-1, "请求失败.");
				returnJson.put("errorInfo", errorStr);
				obdApiLogger.info("----------【校验权限】失败----------");
			}
		} catch (Exception e) {
			returnJson = createReturnJson(-1, "请求失败.");
			returnJson.put("errorInfo", e.getMessage());
			e.printStackTrace();
			obdApiLogger.error("----------【电信接口】设备激活接口异常信息：" + e);
		}
		// 返回json数据
		if (returnJson == null) {
			returnJson = new JSONObject();
		}
		obdApiLogger.info("~obdApiLogger~:【" + request.getRemoteHost() + "】-设备:"+dev+"-"
				+ requestUser + "-" + request.getRequestURI() + "->"
				+ (System.currentTimeMillis() - begin));
		oar.setReturnMsg(returnJson.toString());
		
		boolean flag=	obdApiRecordService.irSave(oar);
		obdApiLogger.info("----------【电信接口】设备激活接口操作记录保存结果---"+flag);
		this.outMessage(returnJson.toString());
	}

	/**
	 * 校验请求的权限
	 * 
	 * @param jsonStr
	 *            请求的json格式字符串
	 * @return 请求参数转换的json数据
	 */
	private JSONObject checkPermissions(String jsonStr) throws Exception {
		JSONObject jso = new JSONObject();
		JSONObject recJson = new JSONObject();
		recJson = JSONObject.fromObject(jsonStr);
		obdApiLogger.info("----------【校验权限】请求参数：" + recJson);
		// 权限校验的结果编码：01-失败 02-成功
		String checkCode = CHECK_SUCCESS;
		// OBD设备唯一标识
		String deviceId = recJson.optString("deviceId");
		// 平台分配的用户
		String username = recJson.optString("username");
		// 时间戳
		String time = recJson.optString("time");
		// MD5 32位(userId+ carUser + time+ password)password为系统分配的密码
		String sign = recJson.optString("sign");
		obdApiLogger.info("参数：【设备号】->" + deviceId + ",【时间】->" + time + ",【sign】->" + sign);
		// 读取账号密码
		String password = PropertiesUtil.getInstance("accounts.properties").readProperty(username);
		String mySign = MD5Coder.encodeMD5Hex(deviceId + username + time + password);
		obdApiLogger.info("----------【校验权限】MD5加密：" + mySign);
		if (StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(username) || StringUtils.isEmpty(time)
				|| !mySign.equals(sign)) {
			checkCode = CHECK_ERROR;
		}
		jso.put("recJson", recJson);
		jso.put("checkCode", checkCode);
		return jso;
	}

	/**
	 * 创建返回的json对象，里面包含公共的返回参数
	 * 
	 * @param code
	 *            返回结果标识，0成功，其它失败
	 * @param desc
	 *            设备唯一标识
	 * @return json对象
	 */
	private JSONObject createReturnJson(int code, String desc) {
		JSONObject retJso = new JSONObject();
		retJso.put("code", code);
		retJso.put("desc", desc);
		return retJso;
	}

	/**
	 * 激活/绑定终端 调用田工的接口(将用户信息绑定obd，并提交到车主通后台) 这步现在是调用华工信元时候，他们调用我们的激活接口。
	 * 我们激活接口里面会先调用你们的激活接口，再写自己的数据库 信元已经做了校验设备是否合格，不需要我们再做校验，如果设备不存在的话，直接入库
	 */
	private synchronized JSONObject bind(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【激活/绑定终端】----------");
		if (!StringUtils.isEmpty(hgDeviceSn)) {
			jso.put("hgDeviceSn", hgDeviceSn);
		}
		if( !StringUtils.isEmpty(userId)){
			jso.put("userId", userId);
		}
		if (!StringUtils.isEmpty(userType)) {
			jso.put("userType", userType);
		}
		String hgDeviceSn = jso.optString("hgDeviceSn");// 待绑定设备用户管理系统的唯一标识。此处为华工二维码
		String deviceId = jso.optString("deviceId");// 表面号
		String obdSN = "";
		try {
			obdSN =StrUtil.obdSnChange(deviceId);// 设备号
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.error("----------【激活/绑定终端】设备号解析有误，请求失败：" + deviceId);
			return returnJson(-1, 4, "表面号解析异常，请输入正确的表面号.");
		}
		String userId = jso.optString("userId");// 用户唯一标识（手机号码）
		String userType = jso.optString("userType");// 用户类型 1华工 2车主通
		obdApiLogger.info("----------【激活/绑定终端】二维码:" + hgDeviceSn + "---obd设备号:" + deviceId + "---手机号:" + userId + "---用户类型:" + userType);
		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(deviceId)||StringUtils.isEmpty(hgDeviceSn) || StringUtils.isEmpty(userId)
				|| StringUtils.isEmpty(userType)) {
			obdApiLogger.error("----------【激活/绑定终端】信息不全，请求失败：" + deviceId);
			return returnJson(-1, 3, "信息不全，请求失败.");
		} 
		if(StringUtils.isEmpty(obdSN)){
			obdApiLogger.error("----------【激活/绑定终端】设备号解析有误，请求失败：" + deviceId);
			return returnJson(-1, 4, "表面号解析异常，请输入正确的表面号.");
		}
		//根据表面号查询
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 表面号
		if(obdStockInfo != null){
			return returnJson(-1, 2, "设备已注册,绑定失败.");
		}
		//根据设备号查询
		OBDStockInfo obd2 = obdStockInfoService.queryBySN(obdSN);// 设备号
		if(obd2 !=null){
			return returnJson(-1, 2, "设备已注册,绑定失败.");
		}
		//根据手机号码查询
		Map<String, Object> map = new HashMap<>();
		map.put("mobileNumber", userId);
		map.put("valid", "1");
		MebUser muser=userService.queryLastByParam(map);
		if(muser !=null ){
			return returnJson(-1, 1, "该手机号码已注册,绑定失败.");
		}
		
		// 保存入库
		// 1.新增用户
		MebUser newUser = new MebUser();
		String carId = IDUtil.createID().substring(8, 18);
		String regUserId = IDUtil.createID();
		newUser.setRegUserId(regUserId);
		newUser.setCarId(carId);
		newUser.setObdSN(obdSN);
		newUser.setMobileNumber(userId);
		newUser.setUserType(Integer.parseInt(userType));
		newUser.setName(userId);// 用户名暂时用手机号
		newUser.setLicense("粤AW" + userId.substring(7, userId.length()));// 车牌号暂时截取最后四位
		newUser.setCreateTime(new Date());
		newUser.setValid("1");
		
		// 2.新增设备
		obdStockInfo = new OBDStockInfo();
		obdStockInfo.setStockId(IDUtil.createID());
		obdStockInfo.setObdId(hgDeviceSn);
		obdStockInfo.setObdSn(obdSN);// 表面码
		obdStockInfo.setObdMSn(deviceId);// obd设备号
		obdStockInfo.setStockType("00");
		obdStockInfo.setStockState("00");// 设备在线状态
		obdStockInfo.setRegUserId(regUserId);
//				obdStockInfo.setGpsState("1");// 默认gps状态为1
//				obdStockInfo.setWifiState("1");// 默认wifi状态为1
		//查询是否存在默认分组设置
		ObdSetting obdSetting=obdSettingService.queryLastObdSetting(obdSN, "obdDefault_00");
		if(obdSetting!=null){
			String settingMsg = obdSetting.getSettingMsg();
			JSONObject json = JSONObject.fromObject(settingMsg);
			String groupNum = json.getString("groupNum");
			obdStockInfo.setGroupNum(groupNum);
		}else{
			obdStockInfo.setGroupNum("00");//默认未分组00
		}
		obdStockInfo.setStartDate((String) DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		obdStockInfo.setValid("1");
		

		// 3 车辆信息生成
		CarInfo carInfo = new CarInfo();
		carInfo.setCarId(carId);
		carInfo.setObdSn(obdSN);
		carInfo.setRegUserId(regUserId);
		carInfo.setCarState("00");// 默认：00 未设置防盗状态
		carInfo.setCreateTime(new Date());
		carInfo.setValid("1");
		
		obdStockInfoService.save(obdStockInfo);//1.先插入设备表
		userService.save(newUser);//插入用户表
		carInfoService.save(carInfo);

		desc = "激活/绑定终端成功.";
		obdApiLogger.info("----------【激活/绑定终端】成功");
			
		JSONObject retJso = createReturnJson(code, desc);
		// 0，绑定成功；1,用户不存在，绑定失败；2，要绑定的设备不存在，绑定失败；3,旧设备解绑定不成功, 6 设备已经绑定
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【激活/绑定终端】" + deviceId + ",结果：" + retJso);
		return retJso;
	}


	private JSONObject returnJson(Integer code, Integer state, String desc) {
		JSONObject retJso = createReturnJson(code, desc);
		JSONObject resultJso = new JSONObject();
		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		return retJso;
	}
	
	private String deviceId;
	private String username;
	private String time;
	private String sign;
	
	
	private String mobileType;
	private String userId;// 用户唯一标识（手机号码）
	private String hgDeviceSn;// 华工二维码
	private String userType;// 用户类型
	private String simId;// 流量卡号

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHgDeviceSn() {
		return hgDeviceSn;
	}

	public void setHgDeviceSn(String hgDeviceSn) {
		this.hgDeviceSn = hgDeviceSn;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSimId() {
		return simId;
	}

	public void setSimId(String simId) {
		this.simId = simId;
	}


}
