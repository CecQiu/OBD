package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.CarInfo;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.DriveBehaviour;
import com.hgsoft.carowner.entity.Efence;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.entity.GpsSet;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.OBDTimeParams;
import com.hgsoft.carowner.entity.OBDTravelParams;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.carowner.entity.WifiSet;
import com.hgsoft.carowner.service.CarDriveInfoService;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.CarInfoService;
import com.hgsoft.carowner.service.CarOilInfoService;
import com.hgsoft.carowner.service.CarParamSetService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.DriveBehaviourService;
import com.hgsoft.carowner.service.EfenceService;
import com.hgsoft.carowner.service.FangDaoService;
import com.hgsoft.carowner.service.FaultCode1Service;
import com.hgsoft.carowner.service.FaultCodeReadService;
import com.hgsoft.carowner.service.FaultCodeService;
import com.hgsoft.carowner.service.FaultUploadService;
import com.hgsoft.carowner.service.GPSSetService;
import com.hgsoft.carowner.service.GpsStateSetService;
import com.hgsoft.carowner.service.MebCarFaultService;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.carowner.service.MemberCarService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdBarrierService;
import com.hgsoft.carowner.service.ObdHandShakeService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.carowner.service.ObdTimeParamsService;
import com.hgsoft.carowner.service.ObdTravelParamsService;
import com.hgsoft.carowner.service.PositionInfoService;
import com.hgsoft.carowner.service.SimStockInfoService;
import com.hgsoft.carowner.service.WiFiService;
import com.hgsoft.carowner.service.WiFiSetService;
import com.hgsoft.carowner.service.WifiStateSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.NumUtil;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.service.ProtalSendService;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.DriveTimeUtil;
import com.hgsoft.obd.util.RequestForwardUtil;
import com.hgsoft.obd.util.SettingType;

/**
 * 车主通平台与客户端接口
 * 
 * @author fdf + sjg
 */
@Controller
@Scope("prototype")
public class ObdApi extends BaseAction {

	private static Logger obdApiLogger = LogManager.getLogger("obdApiLogger");

	/** 接收的json数据 */
	private String jsonStr;
	/** 权限检验-成功编码 */
	private final String CHECK_SUCCESS = "01";
	/** 权限检验-失败编码 */
	private final String CHECK_ERROR = "02";
	@Resource
	private MebUserService userService;
	// @Resource
	// private PositionalInformationService positionalInformationService;
	@Resource
	private PositionInfoService positionInfoService;

	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private MemberCarService memberCarService;
	@Resource
	private CarGSPTrackService carGSPTrackService;
	@Resource
	private CarOilInfoService carOilInfoService;
	@Resource
	private CarInfoService carInfoService;
	@Resource
	private CarDriveInfoService carDriveInfoService;
	@Resource
	private MebCarFaultService carFaultService;
	@Resource
	private FaultUploadService faultUploadService;
	@Resource
	private FaultCodeService faultCodeService;
	@Resource
	private SimStockInfoService simStockInfoService;
	@Resource
	private FangDaoService fangDaoService;
	@Resource
	private ObdBarrierService barrierService;
	@Resource
	private CarTraveltrackService carTraveltrackService;
	@Resource
	private GPSSetService gpsSetService;
	@Resource
	private WiFiSetService wiFiSetService;
	@Resource
	private WiFiService wiFiService;
	@Resource
	private CarParamSetService carParamSetService;// 车辆参数设置
	@Resource
	private FaultCodeReadService faultCodeReadService;// 读故障码
	@Resource
	private FaultCode1Service faultCode1Service;// 故障码
	@Resource
	private GpsStateSetService gpsStateSetService;// gps设置
	@Resource
	private WifiStateSetService wifiStateSetService;// wifi设置
	// @Resource
	// private ProtalService protalService;//protalService设置
	@Resource
	private ProtalSendService protalSendService;// protalService设置

	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	
	@Resource
	private EfenceService efenceService;// 电子围栏
	@Resource
	private DriveBehaviourService driveBehaviourService;
	@Resource
	private ObdTimeParamsService obdTimeParamsService;
	@Resource
	private ObdTravelParamsService obdTravelParamsService;
	
	@Resource
	private ObdSettingService obdSettingService;
	@Resource
	private ObdHandShakeService obdHandShakeService;
	@Resource
	private ObdSateService obdSateService;
	@Resource
	private ObdApiRecordService obdApiRecordService;
	
	@Resource
	private DriveTimeUtil driveTimeUtil;
	@Resource
	private RequestForwardUtil requestForwardUtil;
	@Resource
	private JedisServiceUtil jedisServiceUtil;
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// private final static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

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
			obdApiLogger.info("----------【请求路径】：" + path);
			obdApiLogger.info("----------【请求基本参数】：" + jsonStr);
			Pattern p = Pattern.compile(".*/api/obd/(.*)");
			Matcher m = p.matcher(path);
			if (m.matches()) {
				String apiCode = m.group(1);
				oar.setMethod(apiCode);
				// 校验权限
				obdApiLogger.info("----------【校验权限】----------");
				JSONObject jso = checkPermissions(jsonStr);
				requestUser = jso.optString("username");
				if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
					obdApiLogger.info("----------【校验权限】通过----------");
					JSONObject recJson = jso.getJSONObject("recJson");
					// 相应的接口
					switch (ApiCode.getApiCode(apiCode)) {
					case Bind:
						returnJson = this.bind(recJson);
						break;
					case Guard:
						returnJson = this.guard(recJson);
						break;
					case QUERYGUARD:
						returnJson = this.queryGuard(recJson);
						break;
					case QueryCurrentLocation:
						returnJson = this.queryCurrentLocation(recJson);
						break;
					case MonitorFault:
						String forwardMonitorFault = requestForwardUtil.forward(path, jsonStr);
						if(!"0".equals(forwardMonitorFault)){
							returnJson = JSONObject.fromObject(forwardMonitorFault);
							break;
						}
						returnJson = this.monitorFault(recJson);
						break;
					case QueryPetrol:
						returnJson = this.queryPetrol(recJson);
						break;
					case OptimizeDrive:
						returnJson = this.optimizeDrive(recJson);
						break;
					case QueryRunningTrack:
						returnJson = this.queryRunningTrack(recJson);
						break;
					case SendType:
						returnJson = this.sendType(recJson);
						break;
					case ControlGps:
						JSONObject gpsJson = JSONObject.fromObject(jsonStr);
						setControlGpsParams(gpsJson);//将portal参数进行设置
						String forwardGps = requestForwardUtil.forward(path, gpsJson.toString());
						if(!"0".equals(forwardGps)){
							returnJson = JSONObject.fromObject(forwardGps);
							break;
						}
						returnJson = this.controlGps(recJson);
						break;
					case ControlWifi:
						returnJson = this.controlWifi(recJson);
						break;
					case QueryBindInfo:
						returnJson = this.queryBindInfo(recJson);
						break;
					case QueryWarningInfo:
						break;
					case QueryPushOptimizeInfo:
						break;
					case QueryRunningTrackHis:
						break;
					case QueryPetrolHis:
						break;
					case QueryDeviceStatus:
						returnJson = this.queryDeviceStatus(recJson);
						break;
					case QueryNetFlow:
						returnJson = this.queryNetFlow(recJson);
						break;
					case UnBind:
						returnJson = this.unBind(recJson);
						break;
					case QueryCurrentObdInfo:
						returnJson = this.queryCurrentObdInfo(recJson);
						break;
					case SetOBDBarrier:
						returnJson = this.setOBDBarrier(recJson);
						break;
					case Test:
						test();
						break;
					case GetCarInfo:
						returnJson = this.getCarInfo(recJson);
						break;
					case Portal:
						JSONObject json = JSONObject.fromObject(jsonStr);
						setPortalParams(json);//将portal参数进行设置
						String forwardPortal = requestForwardUtil.forward(path, json.toString());
						if(!"0".equals(forwardPortal)){
							returnJson = JSONObject.fromObject(forwardPortal);
							break;
						}
						returnJson = this.portal(recJson);
						break;
					case ObdSnChange:
						returnJson = this.obdSnChange(recJson);
						break;
					case Dzwl:
						returnJson = this.dzwl(recJson);
						break;
					case DriveBehaviour:
						returnJson = this.driveBehaviour(recJson);
						break;
					case WifiUseTime:
						returnJson = this.wifiUseTime(recJson);
						break;
					case SetdriveBehaviour:
						returnJson = this.setdriveBehaviour(recJson);
						break;
					case WifiPwdAndName:
						returnJson = this.wifiPwdAndName(recJson);
						break;	
					case AlarmSwitch:
						returnJson = this.alarmSwitch(recJson);
						break;	
					case AlarmSwitchState:
						returnJson = this.alarmSwitchState(recJson);
						break;
					default:
						obdApiLogger.error("----------【请求失败】-没有对应的接口："+ apiCode);
						break;
					}
				} else {
					String errorStr = "it is not pass the permissions check,换句话说：就是权限校验没过，自己看着办";
					returnJson = createReturnJson(-1, "请求失败.");
					returnJson.put("errorInfo", errorStr);
					obdApiLogger.info("----------【校验权限】失败----------");
				}
			} else {
				obdApiLogger.error("----------【请求路径不正常】：" + path);
			}
		} catch (Exception e) {
			returnJson = createReturnJson(-1, "请求失败.");
			returnJson.put("errorInfo", e.getMessage());
			e.printStackTrace();
			obdApiLogger.error("----------【电信接口】异常信息：" + e);
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
		obdApiLogger.error("----------【电信接口操作记录保存结果】：" + flag);
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
		obdApiLogger.info("参数：【设备号】->" + deviceId + ",【时间】->" + time
				+ ",【sign】->" + sign);
		// 读取账号密码
		String password = PropertiesUtil.getInstance("accounts.properties")
				.readProperty(username);
		String mySign = MD5Coder.encodeMD5Hex(deviceId + username + time
				+ password);
		obdApiLogger.info("----------【校验权限】MD5加密：" + mySign);
		if (StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(username)
				|| StringUtils.isEmpty(time) || !mySign.equals(sign)) {
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
	private JSONObject bind(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【激活/绑定终端】----------");
		if (!StringUtils.isEmpty(hgDeviceSn) && !StringUtils.isEmpty(userId)) {
			jso.put("hgDeviceSn", hgDeviceSn);
			jso.put("userId", userId);
		}
		if (!StringUtils.isEmpty(userType)) {
			jso.put("userType", userType);
		}
		String hgDeviceSn = jso.optString("hgDeviceSn");// 待绑定设备用户管理系统的唯一标识。此处为华工二维码
		String deviceId = jso.optString("deviceId");// 表面号
		String obdSN = StrUtil.obdSnChange(deviceId);// 设备号
		String userId = jso.optString("userId");// 用户唯一标识（手机号码）
		String userType = jso.optString("userType");// 用户类型 1华工 2车主通
		obdApiLogger.info("----------【激活/绑定终端】设备：" + deviceId);
		obdApiLogger.info("----------【激活/绑定终端】二维码:" + hgDeviceSn + "---obd设备号:" + deviceId + "---手机号:" + userId + "---用户类型:" + userType);
		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(obdSN)||StringUtils.isEmpty(hgDeviceSn) || StringUtils.isEmpty(userId)
				|| StringUtils.isEmpty(userType)) {
			code = -1;
			state = -1;
			desc = "信息不全，请求失败";
			obdApiLogger.error("----------【激活/绑定终端】失败：" + desc);
		} else {
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
			if (obdStockInfo == null) {
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
			} else {

				state = -1;
				code = 0;
				desc = "设备已激活,请联系管理员先解绑.";
				obdApiLogger.info("----------【激活/绑定终端】不成功：" + desc);
			}
		}

		JSONObject retJso = createReturnJson(code, desc);
		// 0，绑定成功；1,用户不存在，绑定失败；2，要绑定的设备不存在，绑定失败；3,旧设备解绑定不成功, 6 设备已经绑定
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【激活/绑定终端】" + deviceId + ",结果：" + retJso);
		return retJso;
	}

	/**
	 * 设置/取消车辆防盗状态 直接更新表obdstate表的车辆防盗状态字段.
	 * @throws Exception 
	 */
	public JSONObject guard(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【设置/取消车辆防盗】----------");
		// 设置/取消车辆防盗状态
		if (!StringUtils.isEmpty(operationType)) {
			jso.put("operationType", operationType);
		}
		String deviceId = jso.optString("deviceId");
		String operationType = jso.optString("operationType");// 操作类型：0撤防；1 设防
		obdApiLogger.info("----------【设置/取消车辆防盗】设备：" + deviceId + "->设置状态:" + operationType);

		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if(StringUtils.isEmpty(operationType)){
			throw new Exception("请求参数有误,请联系管理员.");
		}
		
		// 查询车辆在线状态
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
		if (obdStockInfo != null) {
			String obdSN = obdStockInfo.getObdSn();
			obdApiLogger.info("----------【设置/取消车辆防盗】设备：" + deviceId + "->设备号:" + obdSN);
			ObdState obdState = obdSateService.queryByObdSn(obdSN);
			if(obdState==null){
				obdState = new ObdState();
				obdState.setId(IDUtil.createID());
				obdState.setObdSn(obdSN);
				obdState.setCreateTime(new Date());
				obdState.setValid("1");
			}else{
				obdState.setUpdateTime(new Date());
			}
			obdState.setSafetySwitch(operationType);
			obdSateService.add(obdState);
			desc = "操作成功." ;
		} else {
			code = -1;
			state = -1;
			desc = "操作失败,该设备不存在:" + deviceId;
		}
		
		resultJso.put("state", state);// 当前状态：0成功 其它失败
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【设置/取消车辆防盗】设备：" + deviceId + "->返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 查询车辆防盗状态
	 */
	public JSONObject queryGuard(JSONObject jso) {
		obdApiLogger.info("----------【查询车辆防盗状态】----------");
		// 查询车辆防盗状态
		String deviceId = jso.optString("deviceId");
		obdApiLogger.info("----------【查询车辆防盗状态】设备：" + deviceId);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();

		if (obdStockInfo != null) {
			String obdSN = obdStockInfo.getObdSn();
			obdApiLogger.info("----------【设置/取消车辆防盗】设备：" + deviceId + "->设备号:" + obdSN);
			ObdState obdState = obdSateService.queryByObdSn(obdSN);
			if(obdState!=null && obdState.getSafetySwitch() !=null){
				state = Integer.parseInt(obdState.getSafetySwitch());
			}
			desc = 1==state?"当前处于：启动设防":"当前处于：取消设防";
			
		} else {
			desc = "当前设备不存在:" + deviceId;
			state = -1;
			code = -1;
		}

		JSONObject retJso = createReturnJson(code, desc);

		resultJso.put("state", state);// 当前状态： 1 表示启动设防；0 表示取消设防
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【查询车辆防盗状态】设备：" + deviceId + "->返回结果："
				+ retJso);
		return retJso;
	}

	/**
	 * 当前位置查询 当前位置信息直接取数据库最新一条记录就可以
	 */
	public JSONObject queryCurrentLocation(JSONObject jso) {
		obdApiLogger.info("----------【当前位置查询】----------");
		// 当前位置查询
		if (!StringUtils.isEmpty(mobileType)) {
			jso.put("mobileType", mobileType);
		}
		String mobileType = jso.optString("mobileType");// 系统类型 1 ios；0 android
		String deviceId = jso.optString("deviceId");
		obdApiLogger.info("----------【当前位置查询】设备：" + deviceId);
		int code = 0;
		String desc = "";
		JSONObject retJso = new JSONObject();
		JSONObject resultJso = new JSONObject();

		if (StringUtils.isEmpty(mobileType)) {
			code = -1;
			desc = "信息不全，请求失败";
			retJso = createReturnJson(code, desc);
		} else {
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
			CarGSPTrack ct = null;// 最后一条位置信息
			// PositionalInformation positionalInformation = null;
			if (obdStockInfo != null) {
				String obdSn = obdStockInfo.getObdSn();
				ct = carGSPTrackService.findLastBySn(obdSn);
				String stockState = obdStockInfo.getStockState();
				int state = "01".equals(stockState) ? 1 : 0;
				if (ct != null) {
					code = 0;
					desc = "成功获取当前位置";
					retJso = createReturnJson(code, desc);
					// retJso.put("locatCode", state);//0：定位无效，1：定位有效
					resultJso.put("longitude", CoordinateTransferUtil
							.lnglatTransferDouble(ct.getLongitude()));// 经度
					resultJso.put("latitude", CoordinateTransferUtil
							.lnglatTransferDouble(ct.getLatitude()));// 纬度
					resultJso.put("orient", ct.getDirectionAngle());// 方位
					// retJso.put("precision", state);//精度
					// retJso.put("altitude", state);//海拔高度
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					resultJso.put("time", sdf.format(ct.getGspTrackTime()));// 时间长度10字节格式UTC时间yyMMDDHHmmss
					resultJso.put("speed", ct.getGpsSpeed());// 速度
					resultJso.put("state", state);// 车辆启动状态 0x00车辆未启动 0x01车辆已启动
					obdApiLogger.info("----------【当前位置查询】成功："
							+ ct.getLatitude() + "----" + ct.getLongitude());
				} else {
					// 百度地图拾取到的经纬度是百度地图的经纬度，需要转换成原始的gps坐标：天河区体育西路.
					code = 0;
					desc = "定位失败,获取默认位置信息.";
					retJso = createReturnJson(code, desc);
					// retJso.put("locatCode", state);//0：定位无效，1：定位有效
					resultJso.put("longitude", Double.parseDouble("113.316608"));// 经度
					resultJso.put("latitude", Double.parseDouble("23.140437"));// 纬度
					resultJso.put("orient", Float.parseFloat("0"));// 方位
					// retJso.put("precision", state);//精度
					// retJso.put("altitude", state);//海拔高度
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					resultJso.put("time", sdf.format(new Date()));// 时间长度10字节格式UTC时间yyMMDDHHmmss
					resultJso.put("speed", Float.parseFloat("0"));// 速度
					resultJso.put("state", state);// 车辆启动状态 0x00车辆未启动 0x01车辆已启动
					obdApiLogger.info("----------【当前位置查询】失败：默认位置经纬度:"
							+ "113.316608" + "----" + "23.140437");
				}
			} else {
				// 查看是否存在该设备的位置信息，如果存在，新增相关信息
				code = -1;
				desc = "当前设备不存在，请联系管理员.";
				retJso = createReturnJson(code, desc);
			}
		}

		retJso.put("result", resultJso);
		obdApiLogger.info("----------【当前位置查询】设备：" + deviceId + "返回结果：" + retJso);
		return retJso;

	}

	/**
	 * 故障码查询/车辆体检/故障检测 故障码要读取才上传，所以要下发请求给obd 读取故障码前要先更新故障码的状态。
	 * 先查询obd_handshake表,查看是否有obd不适配车辆——》ECU异常,如果是则直接返回如果obd不适配车辆，直接返回：{"code":-1,"desc":"车型不支持","result":{}}
	 */
	public JSONObject monitorFault(JSONObject jso) {

		obdApiLogger.info("----------【故障码查询/车辆体检/故障检测】----------");
		// 故障码查询/车辆体检/故障检测
		String deviceId = jso.optString("deviceId");
		obdApiLogger.info("----------【故障码查询/车辆体检/故障检测】设备：" + deviceId);
		int code = 0;
		String desc = "操作成功";
		// 查询设备在线状态
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号

		List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
			String obdSN = obdStockInfo.getObdSn();// obd设备号
			//先查询最后一个握手包,如果发生ECU故障，不给体检
//			boolean flag = true;
//			ObdHandShake obdHandShake=obdHandShakeService.findLastBySn(obdSN);
//			if(obdHandShake!=null){
//				String ecu = obdHandShake.getEcu();
//				if(!StringUtils.isEmpty(ecu) && ecu.contains("0000")){
//					flag = false;
//					obdApiLogger.info("----------【故障码查询/车辆体检/故障检测】设备：" + deviceId+"----ecu异常:"+ecu);
//					code =-1;
//					desc ="未能正常体检.";
//				}
//			}
			
			// 00离线状态
			if ("00".equals(obdStockInfo.getStockState())) {
				desc ="设备离线，不能进行体检.";
				resultJso.put("state", "4");
			}else if ("01".equals(obdStockInfo.getStockState())) {
				List<FaultUpload> faultUploads = serverRequestQueryService.readFaultCode(obdSN);
				// 1.读故障码
				if (faultUploads != null && faultUploads.size() > 0) {
					FaultUpload faultCode = faultUploads.get(0);
					String state = faultCode.getState();
					switch (state) {
					case "0"://无故障码
						desc = "无故障码，车辆健康.";
						resultJso.put("state", state);
						break;
					case "2"://车速不能为0
						desc = "车速不为0，不能进行体检.";
						resultJso.put("state", state);
						break;
					case "3"://ECU故障
						desc = "ECU故障，未能正常体检，请稍后重试.";
						resultJso.put("state", state);
						break;
					case "5"://超时
						desc = "体检超时,请稍后重试.";
						resultJso.put("state", state);
						break;
					default:
						desc = "存在故障码.";
						resultJso.put("state", "1");//有故障码
						String now = (String) DateUtil.fromatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
						for (FaultUpload faultUpload : faultUploads) {
							Map<String, String> infoMap = new HashMap<String, String>();
							infoMap.put("faultCode", faultUpload.getFaultCode());// 故障码编号
							// 有效字段为4字节String行。例如：”0100”
							infoMap.put("faultDesc", "暂无描述");// 故障描述
							infoMap.put("faultOccurTime", now);// 故障码发生时间；例如：
							infoMap.put("faultEliminateTime", "");// 故障码消除时间；本字段为空值有效
							infoMap.put("faultState", "0");// 故障码状态值；值为”0”或空值代表有效
							infos.add(infoMap);
						}
						break;
					}
				}else{
					desc = "体检超时,请稍后重试.";
					resultJso.put("state", state);
				}
			}
			resultJso.put("infos", infos);// 最近一次故障码
			resultJso.put("battery", "0");// 蓄电池状态 0：蓄电池良好； 1：蓄电池欠压
			resultJso.put("coolant", "0");// 发动机冷却液温度状态 0：温度正常 1：温度异常
			resultJso.put("cond", 90);// 车况指数 -1：无车况指数 0-100：车况指数分
		
		} else {
			code = -1;
			desc = deviceId + ":当前设备不存在,请联系管理员.";
		}

		JSONObject retJso = createReturnJson(code, desc);

		retJso.put("result", resultJso);
		obdApiLogger.info("----------【故障码查询/车辆体检/故障检测】设备：" + deviceId + "返回结果：" + retJso);
		return retJso;

	
	}

	/**
	 * 油耗查询
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject queryPetrol(JSONObject jso) {
		obdApiLogger.info("----------【油耗查询】----------");
		// 油耗查询
		if (!StringUtils.isEmpty(beginTime)) {
			jso.put("beginTime", beginTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			jso.put("endTime", endTime);
		}
		String beginTime = jso.optString("beginTime");// 起始时间 格式：YYYY-MM-DD
														// hh:mm:ss(UTC 时间)
		String endTime = jso.optString("endTime");// 结束时间 格式：YYYY-MM-DD(UTC 时间)
		String bTime = beginTime.substring(0, 10) + " 00:00:00";
		String eTime = endTime.substring(0, 10) + " 23:59:59";
		String deviceId = jso.optString("deviceId");
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号

		obdApiLogger.info("----------【油耗查询】设备：" + deviceId);
		JSONObject retJso = new JSONObject();// createReturnJson(0,"gseggg4ww");
		JSONObject resultJso = new JSONObject();
		int code = 0;
		String desc = "";
		if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)|| obdStockInfo == null) {
			code = -1;
			desc = "信息不全，请求失败";
		} else {
			String obdSn = obdStockInfo.getObdSn();// 设备号
//			String result = serverRequestQueryService.halfTravel(obdSn);
//			obdApiLogger.info("----------【油耗查询】下发半条行程数据结果：" + result);
			
			// 1.统计总的月份的油耗数据
			Object[] obj = carOilInfoService.carOilCalc(obdSn, bTime, eTime);
			Float totalPetrolConsume = new Float(0);// 总的油耗
			totalPetrolConsume = Float.parseFloat(obj[0] != null ? obj[0].toString() : "0");// 总的油耗
			Float totalMiles = new Float(0);// 总的里程
			totalMiles = Float.parseFloat(obj[1] != null ? obj[1].toString(): "0")/100;//10M / 100 = 公里
			Long totalTime = new Long(0);// 总的驾驶时长
			Float totalTimeF = (Float.parseFloat(obj[2] != null ? obj[2].toString() : "0"));
			totalTime = (long) totalTimeF.intValue();
			try {
				resultJso.put("mileageNum",NumUtil.getNumFractionDigits(totalMiles, 2));// 累计里程（公里）如：199.25
				resultJso.put("petrolConsumeNum", NumUtil.getNumFractionDigits(totalPetrolConsume / 1000.0f, 3));// 累计油耗(升)
				// 平均油耗
				resultJso.put("avgConsume",totalMiles == 0 ? 0 : NumUtil.getNumFractionDigits((totalPetrolConsume / 1000.0f) / totalMiles* 100.0f, 2));// 平均油耗(升/百公里)
				resultJso.put("timeSpanNum", totalTime.toString());// 驾驶时长(秒)
				resultJso.put("avgSpeed",totalTime == 0 ? 0 : NumUtil.getNumFractionDigits(totalMiles / totalTime * 60 * 60, 2));// 平均速度(公里/小时)
			} catch (NumberFormatException e) {
				e.printStackTrace();
				code = -1;
				desc = "数据异常,请联系管理员.";
			} catch (Exception e) {
				e.printStackTrace();
				code = -1;
				desc = "数据异常,请联系管理员.";
			}
			// 2.统计每天的油耗数据
			List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
			List list = new ArrayList();
			list = carOilInfoService.carOilListCalc(obdSn, bTime, eTime);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] object = (Object[]) list.get(i);
					String time = object[0] != null ? object[0].toString(): "0";// 时间
					Float totalPetrolConsume1 = new Float(0);// 总的油耗
					totalPetrolConsume1 = Float.parseFloat(object[1] != null ? object[1].toString() : "0");// 总的油耗
					Float totalMiles1 = new Float(0);// 总的里程
					totalMiles1 = Float.parseFloat(object[2] != null ? object[2].toString() : "0")/100; //10M / 100 = 公里
					Long totalTime1 = new Long(0);// 总的驾驶时长
					Float totalTimeF1 = (Float.parseFloat(object[3] != null ? object[3].toString() : "0"));
					totalTime1 = totalTime = (long) totalTimeF1.intValue();

					Map<String, String> infos = new HashMap<String, String>();
					try {
						infos.put("useDate", time);// 日期2012-01-01
						infos.put("mileageNum",NumUtil.getNumFractionDigits(totalMiles1, 2));// 累计里程（公里）如：199.25
						infos.put("petrolConsumeNum", NumUtil.getNumFractionDigits(totalPetrolConsume1 / 1000.0f, 3));// 累计油耗(升)

						infos.put("avgConsume",totalMiles1 == 0 ? "0" : NumUtil.getNumFractionDigits((totalPetrolConsume1 / 1000.0f)/ totalMiles1 * 100.0f,2));// 平均油耗(升/百公里)
						infos.put("timeSpanNum", totalTime1.toString());// 驾驶时长(秒)
						infos.put("avgSpeed",totalTime1 == 0 ? "0" : NumUtil.getNumFractionDigits(totalMiles1/ totalTime1 * 60 * 60, 2));// 平均速度(公里/小时)
						listData.add(infos);
					} catch (Exception e) {
						e.printStackTrace();
						code = -1;
						desc = "数据异常,请联系管理员.";
					}
				}
				desc = "油耗查询成功.";
			}
			resultJso.put("listData", listData);
		}
		retJso.put("code", code);
		retJso.put("desc", desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【油耗查询】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 驾驶优化建议
	 */
	public JSONObject optimizeDrive(JSONObject jso) {
		obdApiLogger.info("----------【驾驶优化建议】----------");
		if (!StringUtils.isEmpty(beginDate)) {
			jso.put("beginDate", beginDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			jso.put("endDate", endDate);
		}
		String beginDate = jso.optString("beginDate");// 起始时间 格式
														// UTC时间：yyyy-MM-dd
														// HH:mm:ss
		String endDate = jso.optString("endDate");// 结束时间 格式 UTC时间：yyyy-MM-dd
													// HH:mm:ss
		String deviceId = jso.optString("deviceId");
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
		String obdSN = obdStockInfo != null ? obdStockInfo.getObdSn() : null;
		obdApiLogger.info("----------【驾驶优化建议】设备：" + obdSN);
		JSONObject retJso = new JSONObject();// createReturnJson(0,"gseggg4ww");
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(endDate) || StringUtils.isEmpty(beginDate)
				|| StringUtils.isEmpty(obdSN)) {
			code = -1;
			desc = "信息不全，请求失败";
		} else {
			try {
//				String result = serverRequestQueryService.halfTravel(obdSN);
//				obdApiLogger.info("----------【驾驶优化建议】下发半条行程数据结果：" + result);
				
				// 统计总的驾驶优化记录
				CarTraveltrack ct = carTraveltrackService.DrivingBetter(
						beginDate, endDate, obdSN);
				if (ct != null) {
					resultJso.put("tmsRapAcc", ct.getQuickenNum().toString());// 急加速次数
					resultJso.put("tmsRapDec", ct.getQuickSlowDown().toString());// 急减速次数
					resultJso.put("tmsSharpTurn", ct.getQuickTurn().toString());// 急转弯次数
					resultJso.put("highSpeed", "0");// 发动机高转速次数
					resultJso.put("notMatch", ct.getSpeedMismatch().toString());// 车速转速不匹配次数
					resultJso.put("tmsSpeeding", ct.getOverspeedTime().toString());// 超速次数
					resultJso.put("tmsFatigue", "0");// 疲劳驾驶次数
					resultJso.put("avSpeed", "0");// 单位：km/h
					resultJso.put("tmsBrakes", ct.getBrakesNum().toString());// 急刹车次数
					resultJso.put("tmsSteep", ct.getQuickLaneChange().toString());// 急变道次数
					resultJso.put("idling", ct.getIdling().toString());// 怠速次数

					// 查询记录
					List<CarTraveltrack> ctList = carTraveltrackService.DriveBetterDay(beginDate, endDate, obdSN);
					/* 分离统计 */
					List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
					for (CarTraveltrack carTraveltrack : ctList) {
						Map<String, String> infos = new HashMap<String, String>();
						infos.put("useDate", (String) DateUtil.fromatDate(carTraveltrack.getTravelEnd(), "yyyy-MM-dd"));// 日期2012-01-01
						infos.put("tmsRapAcc", carTraveltrack.getQuickenNum().toString());// 急加速次数
						infos.put("tmsRapDec", carTraveltrack.getQuickSlowDown().toString());// 急减速次数
						infos.put("tmsSharpTurn", carTraveltrack.getQuickTurn().toString());// 急转弯次数
						infos.put("highSpeed", "0");// 发动机高转速次数
						infos.put("notMatch", carTraveltrack.getSpeedMismatch().toString());// 车速转速不匹配次数
						infos.put("tmsSpeeding", carTraveltrack.getOverspeedTime().toString());// 超速次数
						infos.put("tmsFatigue", "0");// 疲劳驾驶次数
						infos.put("avSpeed", "0");// 单位：km/h
						infos.put("tmsBrakes", carTraveltrack.getBrakesNum().toString());// 急刹车次数
						infos.put("tmsSteep", carTraveltrack.getQuickLaneChange().toString());// 急变道次数
						infos.put("idling", carTraveltrack.getIdling().toString());// 怠速次数
						listData.add(infos);
					}

					resultJso.put("listData", listData);
					code = 0;
					desc = "操作成功";

				} else {
					desc = "结果集为空";
					code = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				obdApiLogger.error("----------【驾驶优化建议】异常：" + e);
				code = -1;
				desc = "操作失败,请联系管理员！";
			}
		}

		retJso.put("code", code);
		retJso.put("desc", desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【驾驶优化建议】设备：" + obdSN + "，返回结果：" + retJso);
		return retJso;

	}
	
	
	/**
	 * 驾驶行为
	 */
	public JSONObject driveBehaviour(JSONObject jso) {
		obdApiLogger.info("----------【驾驶优化建议】----------");
		if (!StringUtils.isEmpty(beginDate)) {
			jso.put("beginDate", beginDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			jso.put("endDate", endDate);
		}
		if (page!=null) {
			jso.put("page", page);
		}
		if (pageSize!=null) {
			jso.put("pageSize", pageSize);
		}
		String beginDate = jso.optString("beginDate");// 起始时间 格式 UTC时间：yyyy-MM-dd HH:mm:ss
		String endDate = jso.optString("endDate");// 结束时间 格式 UTC时间：yyyy-MM-dd HH:mm:ss
		String deviceId = jso.optString("deviceId");
		Integer page = jso.optInt("page");
		Integer pageSize =jso.optInt("pageSize");
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
		String obdSN = obdStockInfo != null ? obdStockInfo.getObdSn() : null;
		obdApiLogger.info("----------【驾驶优化建议】设备：" + obdSN);
		JSONObject retJso = new JSONObject();// createReturnJson(0,"gseggg4ww");
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(endDate) || StringUtils.isEmpty(beginDate)|| StringUtils.isEmpty(obdSN)) {
			code = -1;
			desc = "信息不全或设备号不存在,请求失败.";
		} else {
			try {
				//查询总数
				Integer total = driveBehaviourService.getBDListTotalByObdSnAndTime(beginDate, endDate, obdSN);
				resultJso.put("count", total);
				// 查询记录
				List<DriveBehaviour> bdList = driveBehaviourService.getBDListByObdSnAndTime(beginDate, endDate, obdSN,page,pageSize);
				/* 分离统计 */
				List<Map<String, String>> posData = new ArrayList<Map<String, String>>();
				for (DriveBehaviour bd : bdList) {
					Map<String, String> infos = new HashMap<String, String>();
					//判断type
					infos.put("type", bd.getTypeString());
					infos.put("longitude", CoordinateTransferUtil.lnglatTransferDouble(bd.getLongitude()));
					infos.put("latitude", CoordinateTransferUtil.lnglatTransferDouble(bd.getLatitude()));
					infos.put("date", DateUtil.getTimeString(bd.getTime(), "yyyy-MM-dd HH:mm:ss"));
					posData.add(infos);
				}
				resultJso.put("posData", posData);
				code = 0;
				desc = "操作成功";
			} catch (Exception e) {
				e.printStackTrace();
				obdApiLogger.error("----------【驾驶优化建议】异常：" + e);
				code = -1;
				desc = "操作失败,请联系管理员！";
			}
		}

		retJso.put("code", code);
		retJso.put("desc", desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【驾驶优化建议】设备：" + obdSN + "，返回结果：" + retJso);
		return retJso;

	}


	/**
	 * 查询运行轨迹
	 */
	public JSONObject queryRunningTrack(JSONObject jso) {
		obdApiLogger.info("----------【查询运行轨迹】----------");

		if (!StringUtils.isEmpty(startTime)) {
			jso.put("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			jso.put("endTime", endTime);
		}
		if (!StringUtils.isEmpty(theDate)) {
			jso.put("theDate", theDate);
		}
		String deviceId = jso.optString("deviceId");
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
		String obdSN = obdStockInfo != null ? obdStockInfo.getObdSn() : null;// 设备号
		String startTime = jso.optString("startTime");// 开始时间(HH:mm:ss)
		String endTime = jso.optString("endTime");// 结束时间(HH:mm:ss)
		String theDate = jso.optString("theDate");// 日期(yyyy-MM-dd)

		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();

		List<Map<String, Object>> posData = new ArrayList<Map<String, Object>>();

		if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)
				|| StringUtils.isEmpty(theDate) || StringUtils.isEmpty(obdSN)) {
			desc = "信息不全，请求失败";
			code = -1;
		} else {
			try {
				List<CarGSPTrack> carGSPTracks = carGSPTrackService.queryTrack(
						obdSN, theDate + " " + startTime, theDate + " "
								+ endTime);
				if (carGSPTracks.size() > 0) {
					for (CarGSPTrack track : carGSPTracks) {
						Map<String, Object> infos = new HashMap<String, Object>();
						infos.put("longitude", CoordinateTransferUtil
								.lnglatTransferDouble(track.getLongitude()));// 经度
						infos.put("latitude", CoordinateTransferUtil
								.lnglatTransferDouble(track.getLatitude()));// 纬度
						infos.put("direction", track.getDirectionAngle() + "");// 方向
						// 时间戳
						infos.put(
								"time",
								String.valueOf(track.getGspTrackTime().getTime()));
						infos.put("speed", track.getGpsSpeed().floatValue());// 暂时取gps速度
						// infos.put("speed",
						// track.getObdSpeed().floatValue());// 速度
						posData.add(infos);
						code = 0;
						desc = "操作成功";
					}
				} else {
					desc = "结果集为空";
					code = 0;
				}

			} catch (Exception e) {
				desc = "操作失败";
				e.printStackTrace();
			}
		}
		JSONObject retJso = createReturnJson(code, desc);
		resultJso.put("posData", posData);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【查询运行轨迹】设备：" + deviceId + "，返回结果："
				+ retJso);
		return retJso;
	}

	/**
	 * 设置信息发送方式
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public JSONObject sendType(JSONObject jso) {
		// TODO 设置信息发送方式
		System.out.println("设置信息发送方式");

		String sendType = jso.optString("sendType");// 状态： 1 表示APP通知；0 表示短信

		JSONObject retJso = createReturnJson(0, "fasdo92803");
		int state = 0;
		retJso.put("state", state);// 当前状态：0成功 其它失败
		return retJso;
	}

	/**
	 * 设置OBD设备GPS开关 1.先设置obdstockinfo表的gpsstate字段
	 * 2.如果obd在线，下发设置参数，如果成功，生成gpsSet记录，并且valid为0;如果不成功，生成gpsSet记录，并且valid为1.
	 * 3.如果obd不在线，直接生成gpsSet记录，且valid为1;
	 */
	@SuppressWarnings("unused")
	public JSONObject controlGps(JSONObject jso) {
		obdApiLogger.info("----------【设置GPS开关】----------");
		// 设置OBD设备GPS开关
		if (!StringUtils.isEmpty(state)) {
			jso.put("state", state);
		}
		String inState = jso.optString("state");// 打开状态 1； 关闭0
		// String obdGpsState ="0"+inState;//前面补0
		String deviceId = jso.optString("deviceId");// OBDSN
		obdApiLogger.info("----------【设置GPS开关】设备：-" + deviceId);
		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(inState)) {
			desc = "信息不全，请求失败";
			state = -1;
			code = -1;
		} else {
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
			String obdSN = obdStockInfo.getObdSn();
			if (obdStockInfo != null) {
				// 1更新obdstockinfo表的gpsstate字段
				// obdStockInfo.setGpsState(inState);// 成功
				// obdStockInfoService.update(obdStockInfo);
				// 1.1先将这个设备之前的GpsSet记录修改为无效
				// int i=gpsStateSetService.gpsStateNoValid(obdSN);
				// obdApiLogger.info("----------【设置GPS开关】将之前的设置修改为无效:"+i);
				// 2.判断设备在线状态.
				GpsSet gpsSet = new GpsSet();
				gpsSet.setObdSn(obdSN);
				gpsSet.setGpsState(inState);// GPS开关状态
				gpsSet.setCreateTime(new Date());// 创建时间
				String stockState = obdStockInfo.getStockState();// 设备在线状态
				state = Integer.parseInt(inState);// 返回状态
				// 如果设备不在线
				if ("00".equals(stockState)) {
					gpsSet.setValid("1");// 不成功
					desc = "gps开关设置成功,obd离线,等待设备上线并重新设置.";
				} else if ("01".equals(stockState)) {
					// 如果设备在线,向obd发送指令
					// CarParam carParam = new CarParam();
					// carParam.setObdSn(obdMSN.toLowerCase());// obd设备号均是小写
					// carParam.setGps(obdGpsState);//设置obd开关状态
					// String mark =
					// carParamSetService.paramSet(carParam);//发送指令给obd
					String mark = "";
					try {
						String gstate = inState.equals("0") ? "1" : "0";
						mark = serverSettingService.gps(obdSN, gstate);
					} catch (OBDException e) {
						e.printStackTrace();
					}
					if (GlobalData.isSendResultSuccess(mark)) {
						// 如果设备设置成功
						obdStockInfo.setGpsState(inState);// 成功
						obdStockInfoService.update(obdStockInfo);
						// 1.生成gpsSet记录
						gpsSet.setValid("0");// 成功
						desc = "gps设置操作成功!";
					} else {
						gpsSet.setValid("1");// 不成功
						desc = "gps开关设置成功,obd设置不成功,再次设备上线并重新设置.";
					}
				}
				// 保存GpsSet记录
				gpsStateSetService.gpsSetSave(gpsSet);
			} else {
				state = -1;
				code = 0;
				desc = "该设备不存在:" + deviceId;
			}
		}

		JSONObject retJso = createReturnJson(code, desc);
		resultJso.put("state", state);// 1 表示启动；0 表示取消
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【设置GPS开关】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 设置OBD设备WIFI开关 1.先将之前的wifi设置设为无效 1.先设置obdstockinfo表的wifistate字段
	 * 2.如果obd在线，下发设置参数，如果成功，生成wifiset记录，并且valid为0;如果不成功，生成wifiset记录，并且valid为1.
	 * 3.如果obd不在线，直接生成wifiset记录，且valid为1;
	 */
	public JSONObject controlWifi(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【设置WIFI开关】----------");
		// TODO 设置OBD设备WIFI密码
		if (!StringUtils.isEmpty(state)) {
			jso.put("state", state);
		}
		String deviceId = jso.optString("deviceId");// OBDSN
		obdApiLogger.info("----------【设置WIFI开关】设备：" + deviceId);
		String inState = jso.optString("state");// 打开状态 1； 关闭0
		// String wifiState = inState.equals("1")?"04":"05";//wifi状态
		int code = 0;
		int state = 0;
		String desc = "操作成功";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(inState)) {
			desc = "信息不全，请求失败";
			code = -1;
			state = -1;
		} else {
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
			if (obdStockInfo != null) {
				String stockSate = obdStockInfo.getStockState();// obd在线状态
				String obdSN = obdStockInfo.getObdSn();
				state = Integer.parseInt(inState);
				// 1.先将之前的wifi设置设为无效
				// int i=wifiStateSetService.wifiSetNoValid(obdSN,"4");
				// obdApiLogger.info("----------【设置GPS开关】先将之前的wifi设置设为无效:"+i);
				// 2更新obdstockinfo表的wifiState状态
				// obdStockInfo.setWifiState(inState);
				// obdStockInfoService.update(obdStockInfo);
				// 3.1生成一条新的wifi设置记录
				WifiSet wifiSet = new WifiSet();
				wifiSet.setObdSn(obdSN);
				wifiSet.setCreateTime(new Date());
				wifiSet.setType("4");// wifi开关
				wifiSet.setWifiState(inState);// wifi开关状态;
				// 3.2判断obd是否在线，如果在线，下发设置参数
				// Wifi wifi = new Wifi();
				// wifi.setObdSn(obdMSN.toLowerCase());
				// wifi.setWifiState(wifiState);//wifi状态
				if ("01".equals(stockSate)) {
					// String resultStr = wiFiSetService.WiFiSet(wifiState, wifi);
					String wState = inState.equals("0") ? "1" : "0";
					String resultStr = serverSettingService.wifi(obdSN, wState);
					// 如果wifi设置成功
					if (GlobalData.isSendResultSuccess(resultStr)) {
						wifiSet.setValid("0");// 设置成功
						obdStockInfo.setWifiState(inState);
						obdStockInfoService.update(obdStockInfo);
					} else {
						wifiSet.setValid("1");// 设置失败，下次再次设置
					}
				} else {
					wifiSet.setValid("1");// 设置失败，下次再次设置
					desc = "wifi开关设置成功,当前设置不在线.";
				}
				wifiStateSetService.save(wifiSet);// 保存wifiset记录
			} else {
				state = -1;
				desc = "设置失败,当前设备不存在:" + deviceId;
			}
		}

		JSONObject retJso = createReturnJson(code, desc);
		resultJso.put("state", state);// 1 表示启动；0 表示关闭
		retJso.put("result", resultJso);
		obdApiLogger
				.info("----------【设置WIFI开关】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 查询OBD 用户信息
	 */
	public JSONObject queryBindInfo(JSONObject jso) {
		obdApiLogger.info("----------【 查询OBD用户信息】----------");
		// 查询OBD 用户信息
		JSONObject retJso = null;
		String deviceId = jso.optString("deviceId");// OBDSN
		obdApiLogger.info("----------【 查询OBD用户信息】-设备：" + deviceId);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(deviceId);
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
			// String userId = obdStockInfo.getRegUserId();
			// MebUser user = userService.queryById(userId);
			MebUser user = userService.queryByObdSn(obdStockInfo.getObdSn());
			if (user != null) {
				CarInfo carInfo = carInfoService.queryCarInfoByUserid(user
						.getRegUserId());
				retJso = createReturnJson(0, "获取用户成功");
				resultJso.put("deviceId", obdStockInfo.getObdSn());// 设备序列号
				resultJso.put("productType", "04");// 厂商 04——华软
				resultJso.put("hgDeviceSn", obdStockInfo.getObdSn());// 用户管理系统二维码
				resultJso.put("deviceUID", obdStockInfo.getObdId());// 设备UID
				String status = "01".equals(obdStockInfo.getStockType()) ? "0"
						: "1";
				String guardStatus = carInfo != null ? carInfo.getCarState()
						: "";
				if ("00".equals(guardStatus)) {
					resultJso.put("guardStatus", "0");// 防盗标志0 撤防；1 设防
				} else if ("01".equals(guardStatus)) {
					resultJso.put("guardStatus", "1");// 防盗标志0 撤防；1 设防
				}
				resultJso.put("sendType", "-1");// 1 表示APP通知；0 表示短信
				resultJso.put("status", status);// 状态 0:初始 1：绑定 2：失效3：暂停
			} else {
				retJso = createReturnJson(-1, "获取用户失败");
			}
		} else {
			retJso = createReturnJson(-1, "不存在该设备,请联系管理员.");
		}
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【 查询OBD用户信息】设备：" + deviceId + "，返回结果："
				+ retJso);
		return retJso;
	}

	/**
	 * 查询设备状态
	 */
	public JSONObject queryDeviceStatus(JSONObject jso) {
		obdApiLogger.info("----------【查询设备状态】----------");
		// 查询设备状态
		String deviceId = jso.optString("deviceId");
		obdApiLogger.info("----------【查询设备状态】设备：" + deviceId);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		String desc = "";
		int code = 0;
		int state = 0;
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
			String obdState = obdStockInfo.getStockState();
			if ("00".equals(obdState)) {
				state = 0;
			} else if ("01".equals(obdState)) {
				state = 1;
			} else if ("02".equals(obdState)) {
				state = 2;
			}
			desc = "成功查询设备状态";
			code = 0;
		} else {
			desc = "查询失败，设备不存在";
			state = -1;
		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【查询设备状态】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 查询流量卡流量信息
	 * 通知田学林,流量查询必须设备在线.
	 */
	public JSONObject queryNetFlow(JSONObject jso) throws Exception {
		
		obdApiLogger.info("----------【查询流量卡流量信息】----------");
		obdApiLogger.info("----------【设置WIFI开关】----------");
		String deviceId = jso.optString("deviceId");// OBDSN
		int code = 0;
		String desc = "";
		JSONObject retJso = new JSONObject();
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(deviceId)) {
			desc = "信息不全，请求失败";
			code = -1;
		} else {
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
			if(obdStockInfo!=null){
				String obdSN = obdStockInfo.getObdSn();
				obdApiLogger.info("----------【查询流量卡流量信息】设备：" + obdSN);
				//流量主动查询暂时去除
				//流量查询暂时不使用下发查询，改为定时上传。每次上传记录值累计在最新一条记录里。
//				Long totalUsedValue=serverRequestQueryService.wifiFlow(obdSN);
				Long totalUsedValue  = null;
//				if(totalUsedValue == null){
					//从数据获取最新数据
					SimStockInfo simStockInfo = simStockInfoService.queryLastByObdSn(obdSN);
					Long flowUse = null;
					Long tempFlowUse = null;
					Long cleanFlowUse = null;
					if(simStockInfo != null){
						if(simStockInfo.getValid() == 1){
							flowUse = simStockInfo.getFlowUse();
							tempFlowUse = simStockInfo.getTempFlowUse();
						}else{
							//清除
							flowUse = simStockInfo.getFlowUse();
							tempFlowUse = simStockInfo.getTempFlowUse();
							cleanFlowUse = simStockInfo.getCleanFlowUse();
						}
						flowUse = (flowUse == null ? 0 : flowUse);
						tempFlowUse = (tempFlowUse == null ? 0 : tempFlowUse);
						cleanFlowUse = (cleanFlowUse == null ? 0 : cleanFlowUse);
						totalUsedValue = flowUse + tempFlowUse - cleanFlowUse;
					}
					
//				}else{
//					//查询到的流量值——入库
//					SimStockInfo simStockInfo = new SimStockInfo(IDUtil.createID());
//					simStockInfo.setValid(1);
//					simStockInfo.setObdSn(obdSN);
//					simStockInfo.setFlowUse(totalUsedValue);
//					simStockInfo.setCreateTime(getNow());
//					simStockInfoService.add(simStockInfo);
//					obdApiLogger.info("----------【查询流量卡流量信息】设备：" + deviceId + "->"+obdSN+"，流量值："+totalUsedValue+"，入库成功！");
//				}
				if(totalUsedValue != null){
					resultJso.put("totalUsedValue", totalUsedValue/1024);//单位B->KB
					desc = "查询成功.";
					obdApiLogger.info("----------【查询流量卡流量信息】设备：" + deviceId + "->"+obdSN+"，流量值："+totalUsedValue);
				}else{
					resultJso.put("totalUsedValue", -1);
					desc = "查询流量卡流量信息失败.";
				}
			}else{
				code = -1;
				resultJso.put("totalUsedValue", -1);
				desc = "当前设备不存在,请联系管理员";
			}
		}
		resultJso.put("totalInitValue", 0);
		resultJso.put("totalSpareValue", 0);
		retJso.put("code", code);
		retJso.put("desc", desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【查询流量卡流量信息】设备：" + deviceId + "，返回结果："
				+ retJso);
		return retJso;
	}

	/**
	 * 查询水温、电压信息
	 */
	public JSONObject queryCurrentObdInfo(JSONObject jso) {
		obdApiLogger.info("----------【查询水温、电压信息】----------");
		// TODO 数据接口待开发调试 查询水温、电压信息
		String deviceId = jso.optString("deviceId");
		obdApiLogger.info("----------【查询水温、电压信息】设备：" + deviceId);
		int code = 0;
		String desc = "";
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
			String obdSN = obdStockInfo.getObdSn();
			//先从初始化表里取电压
			ObdHandShake obdHandShake=obdHandShakeService.findLastBySn(obdSN);
			if(obdHandShake!=null){
				resultJso.put("voltage", obdHandShake.getVolt());
			}else{
				CarTraveltrack carTraveltrack = carTraveltrackService.findLastBySN(obdSN);// 最后一次行程记录的电压
				if(carTraveltrack != null){
					resultJso.put("voltage", carTraveltrack.getVoltage());
				}else{
					obdApiLogger.info("----------【查询水温、电压信息】设备：" + deviceId+"-----没有行程记录信息.");
				}
			}
			//查询水温不为空的最后一条记录
			PositionInfo p = positionInfoService.findLastTemNotNull(obdSN);
			if(p!=null){
				resultJso.put("water", p.getEngineTemperature());
			}else{
				obdApiLogger.info("----------【查询水温、电压信息】设备：" + deviceId+"-----没有位置信息.");
			}
		} else {
			code = -1;
			desc = "当前设备不存在,请联系管理员.";
		}

		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【查询水温、电压信息】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 设置OBD设备电子栅栏
	 */
	private JSONObject setOBDBarrier(JSONObject jso) {
		obdApiLogger.info("----------【设置OBD设备电子栅栏】----------");
		if (!StringUtils.isEmpty(minLongitude)) {
			jso.put("minLongitude", minLongitude);
		}
		if (!StringUtils.isEmpty(minLatitude)) {
			jso.put("minLatitude", minLatitude);
		}
		if (!StringUtils.isEmpty(maxLongitude)) {
			jso.put("maxLongitude", maxLongitude);
		}
		if (!StringUtils.isEmpty(maxLatitude)) {
			jso.put("maxLatitude", maxLatitude);
		}
		String minLongitude = jso.optString("minLongitude");
		String minLatitude = jso.optString("minLatitude");
		String maxLongitude = jso.optString("maxLongitude");
		String maxLatitude = jso.optString("maxLatitude");
		String obdSn = jso.optString("deviceId");
		obdApiLogger.info("----------【设置OBD设备电子栅栏】设备：" + obdSn);
		int code = 0;
		int state = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(minLongitude)
				|| StringUtils.isEmpty(minLatitude)
				|| StringUtils.isEmpty(maxLongitude)
				|| StringUtils.isEmpty(maxLatitude)) {
			desc = "信息不全，请求失败";
			code = -1;
			state = -1;
		} else {
			// TODO 调用OBD接口，进行设置
			// 返回结果记录到电子栅栏表
			//
			int resultState = -1;
			// ObdBarrier barrier = new
			// ObdBarrier(obdSn,minLongitude,minLatitude,maxLongitude,maxLatitude,new
			// Date(),resultState);
			// barrierService.save(barrier);
			state = resultState;

		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【设置OBD设备电子栅栏】设备：" + obdSn + "，返回结果："
				+ retJso);
		return retJso;
	}

	/**
	 * 获得车辆信息
	 * 
	 * @return
	 */
	private JSONObject getCarInfo(JSONObject jso) {
		obdApiLogger.info("----------【获得车辆信息】----------");
		String deviceId = jso.optString("deviceId");

		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号

		obdApiLogger.info("----------【获得车辆信息】设备：" + deviceId);
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		if (StringUtils.isEmpty(deviceId)) {
			desc = "信息不全，请求失败！";
			code = -1;
		} else {
			// 获取最新行程作为车辆的信息：车速、转速、温度
			if (obdStockInfo != null) {
				CarTraveltrack carTraveltrack = carTraveltrackService
						.findLastBySN(obdStockInfo.getObdSn());
				if (carTraveltrack != null) {
					resultJso.put("rotationalSpeed",
							carTraveltrack.getRotationalSpeed());// 转速
					resultJso.put("temperature",
							carTraveltrack.getTemperature());// 温度
					resultJso.put("speed", carTraveltrack.getSpeed());// 速度
					code = 0;
					desc = "获取信息成功";
				} else {
					desc = "当前设备不存在，请求失败！";
					code = -1;
				}
			} else {
				code = -1;
				desc = "该设备不存在,请联系管理员.";
			}
		}
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【获得车辆信息】设备：" + obdSn + "，返回结果：" + retJso);
		return retJso;
	}

	/**
	 * 解绑
	 * 
	 */
	private JSONObject unBind(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【OBD设备解绑】----------");
		String deviceId = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		obdApiLogger.info("----------【OBD设备解绑】设备：" + deviceId);
		int state = 0;
		int code = 0;
		String desc = "解绑成功！";
		JSONObject resultJso = new JSONObject();
		try {
			// 1.查询是否存在该设备号
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 二维码
			if (obdStockInfo != null) {

				// 1.更新用户表
				MebUser mebUser = userService.queryById(obdStockInfo.getRegUserId());//
				mebUser.setValid("0");// 无效
				mebUser.setUpdateTime(new Date());
				userService.update(mebUser);

				// 2.更新车辆信息表
				CarInfo carInfo = carInfoService.queryCarInfoByUserid(mebUser.getRegUserId());
				carInfo.setValid("0");
				carInfo.setUpdateTime(new Date());
				carInfoService.update(carInfo);

				// obdStockInfo.setRegUserId(null);
				obdStockInfo.setStockType("02");//已解绑
				obdStockInfo.setUpdateTime(new Date());
				obdStockInfo.setValid("0");//该记录也置为无效
				obdStockInfoService.update(obdStockInfo);

				desc = "解绑成功！";
				obdApiLogger.info("----------【OBD设备解绑】解绑成功：");
			} else {
				state = -1;
				desc = "该设备不存在或已经解绑,请联系管理员";
				obdApiLogger.error("----------【OBD设备解绑】解绑失败：" + desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			state = -1;
			desc = "该设备不存在或已解绑,请联系管理员";
		}

		JSONObject retJso = createReturnJson(code, desc);
		// 0，绑定成功；1,用户不存在，绑定失败；2，要绑定的设备不存在，绑定失败；3,旧设备解绑定不成功, 6 设备已经绑定
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【OBD设备解绑】设备：" + deviceId + "，返回结果："
				+ retJso);
		return retJso;
	}

	/**
	 * obdSn转换 设备号转换查询接口
	 */
	private JSONObject obdSnChange(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【OBD设备 obdSn转换】----------");
		if (!StringUtils.isEmpty(obdSn)) {
			jso.put("obdSn", obdSn);
		}
		// String obdSn = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		String obdSn = jso.optString("obdSn");// obd设备号
		if(StringUtils.isEmpty(obdSn)){
			throw new Exception("请求参数有误,设备号不能为空.");
		}
		
		int state = 0;
		int code = 0;
		String desc = "";
		String obdPn = "";
		JSONObject resultJso = new JSONObject();
		// 1.查询是否存在该设备号
		// 暂时转换
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);// 设备号
		if (obdStockInfo != null) {
			obdPn = obdStockInfo.getObdMSn();// 表面号
			desc = "操作成功.";
			obdApiLogger.info("----------【OBD设备 obdSn转换】成功："
					+ obdStockInfo.getObdSn() + "->" + obdPn);
		} else {
			state = -1;
			code = 0;
			desc = "查询失败,该设备不存在,请联系管理员";
			obdApiLogger.error("----------【OBD设备 obdSn转换】失败：" + desc);
		}

		JSONObject retJso = createReturnJson(code, desc);
		// 当前状态：0成功 -1失败
		resultJso.put("obdPn", obdPn);
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【OBD设备 obdSn转换】返回结果：" + retJso);
		return retJso;
	}

	/**
	 * portal接口 1.1生成portal记录,
	 * 1.判断obd设备是否在线,如果在线，下发设置参数，设置成功，portal——valid为0,如果设置不成功，等待obd在线，重新设置，直到成功。
	 * 
	 */
	private JSONObject portal(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【Portal设置】----------");
		setPortalParams(jso);

		String deviceId = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		obdApiLogger.info("----------【Portal设置】设备：" + deviceId);
		String type = jso.optString("type");// 操作类别
		String url = jso.optString("url");// 设置URL
		String mb = jso.optString("mb");// 流量额度限制
		String whitelists = jso.optString("whitelists");// 白名单设置
		String mac = jso.optString("mac");// 当type为2,5时有值
		String onOff = jso.optString("onOff");// 当type为6时有值
		obdApiLogger.info("----------【Portal设置】" + "操作类型:" + type + "---url:"
				+ url + "---mb:" + mb + "---whitelists:" + whitelists
				+ "---mac:" + mac + "---onOff:" + onOff);

		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		// 1.查询是否存在该设备号
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		if (obdStockInfo != null) {
			// String stockState = obdStockInfo.getStockState();//设备在线状态
			String obdSN = obdStockInfo.getObdSn();
			Portal portal = new Portal();
			portal.setObdSn(obdSN);
			portal.setCreateTime(new Date());
			portal.setType(type);// 操作类别
			if (!StringUtils.isEmpty(url)) {
				portal.setUrl(url);// url
			}
			if (!StringUtils.isEmpty(mb)) {
				portal.setMb(mb);// mb
			}
			if (!StringUtils.isEmpty(whitelists)) {
				portal.setWhitelists(whitelists);// 白名单设置
			}
			if (!StringUtils.isEmpty(mac)) {
				portal.setMac(mac);// mac地址
			}
			if (!StringUtils.isEmpty(onOff)) {
				portal.setOnOff(onOff);// portal开关
			}

			String result ="";
			if("0".equals(type)){
				String bitsStr="1000000000000000";
				char[] bits=bitsStr.toCharArray();
				if(StringUtils.isEmpty(url)){
					throw new Exception("请求参数不全,请联系管理员.");
				}
				result=serverSettingService.portalOrWifiSet(obdSN, bits, url);
			}else if("1".equals(type)){
				throw new Exception("暂无该设置选项.");
			}else if("2".equals(type)){
				String bitsStr="0010000000000000";
				char[] bits=bitsStr.toCharArray();
				if(StringUtils.isEmpty(mac)||StringUtils.isEmpty(mb)){
					throw new Exception("请求参数不全,请联系管理员.");
				}
				result=serverSettingService.portalOrWifiSet(obdSN, bits, mac,mb);
			}else if("3".equals(type)){
				String bitsStr="0001000000000000";
				char[] bits=bitsStr.toCharArray();
				if(StringUtils.isEmpty(whitelists)){
					throw new Exception("请求参数不全,请联系管理员.");
				}
				result=serverSettingService.portalOrWifiSet(obdSN, bits, whitelists.replaceAll("\\|", ","));
			}else if("4".equals(type)){
				String bitsStr="0000100000000000";
				char[] bits=bitsStr.toCharArray();
				result=serverSettingService.portalOrWifiSet(obdSN, bits, "00");
			}else if("5".equals(type)){
				String bitsStr="0000100000000000";
				char[] bits=bitsStr.toCharArray();
				if(StringUtils.isEmpty(mac)){
					throw new Exception("请求参数不全,请联系管理员.");
				}
				result=serverSettingService.portalOrWifiSet(obdSN, bits, "01",mac);
			}else if("6".equals(type)){
				String bitsStr="0000010100000000";
				char[] bits=bitsStr.toCharArray();
				if(StringUtils.isEmpty(onOff)){
					throw new Exception("请求参数不全,请联系管理员.");
				}
				if("0".equals(onOff)){
					//关闭portal
					//打开portal,默认密码是1234567890
					//查询改设备是否存在密码
					Wifi wifi=wiFiService.isExist(obdSN);
					String pwd="1234567890";
					if(wifi!=null && !StringUtils.isEmpty(wifi.getWifiPwd())){
						pwd =wifi.getWifiPwd();
					}
					result=serverSettingService.portalOrWifiSet(obdSN, bits, "00",pwd);
				}else if("1".equals(onOff)){
					result=serverSettingService.portalOrWifiSet(obdSN, bits, "01");
				}
			}else{
				throw new Exception("不存在该设置指令,请联系管理员.");
			}
			
			boolean flag = GlobalData.isSendResultSuccess(result);
			if (flag) {
				portal.setValid("0");// 设置成功.
				desc = "portal设置成功.";
			} else {
				portal.setValid("1");// 设置成功.
				state = -1;
				desc = "portal设置失败,请检查设备是否在线,重新设置！";
			}

			protalSendService.save(portal);

			obdApiLogger.info("----------【Portal设置】portal设置结束");
		} else {
			state = -1;
			code = -1;
			desc = "portal设置失败,该设备不存在,请联系管理员";
			obdApiLogger.error("----------【Portal设置】portal设置失败：" + desc);
		}

		JSONObject retJso = createReturnJson(code, desc);
		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【Portal设置】返回结果：" + retJso);
		return retJso;
	}
	
	/**
	 * 设置portal传递的参数
	 * @param jso
	 */
	private void setControlGpsParams(JSONObject jso){
		if (!StringUtils.isEmpty(state)) {
			jso.put("state", state);
		}
	}
	
	/**
	 * 设置portal传递的参数
	 * @param jso
	 */
	private void setPortalParams(JSONObject jso){
		if (!StringUtils.isEmpty(type)) {
			jso.put("type", type);
		}
		if (!StringUtils.isEmpty(url)) {
			jso.put("url", url);
		}
		if (!StringUtils.isEmpty(mb)) {
			jso.put("mb", mb);
		}
		if (!StringUtils.isEmpty(whitelists)) {
			jso.put("whitelists", whitelists);
		}
		if (!StringUtils.isEmpty(mac)) {
			jso.put("mac", mac);
		}
		if (!StringUtils.isEmpty(onOff)) {
			jso.put("onOff", onOff);
		}
	}
	
	/**
	 * 电子围栏接口 liujialin 20160112 a.新增电子围栏 type:0 1.railAndAlert为4取消电子围栏
	 * 2.railAndAlert为5取消所有电子围栏 type:1
	 * 
	 * b.修改电子围栏
	 * 
	 */
	private JSONObject dzwl(JSONObject jso) throws Exception {
		if (!StringUtils.isEmpty(type)) {
			jso.put("type", type);
		}
		if (!StringUtils.isEmpty(railAndAlert)) {
			jso.put("railAndAlert", railAndAlert);
		}
		if (!StringUtils.isEmpty(areaNum)) {
			jso.put("areaNum", areaNum);
		}
		if (!StringUtils.isEmpty(maxLongitude)) {
			jso.put("maxLongitude", maxLongitude);
		}
		if (!StringUtils.isEmpty(maxLatitude)) {
			jso.put("maxLatitude", maxLatitude);
		}
		if (!StringUtils.isEmpty(minLongitude)) {
			jso.put("minLongitude", minLongitude);
		}
		if (!StringUtils.isEmpty(minLatitude)) {
			jso.put("minLatitude", minLatitude);
		}
		if (!StringUtils.isEmpty(startDate)) {
			jso.put("startDate", startDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			jso.put("endDate", endDate);
		}
		if (!StringUtils.isEmpty(isChange)) {
			jso.put("isChange", isChange);
		}

		String deviceId = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		obdApiLogger.info(deviceId+":设备----------【电子围栏设置】----------");
		String type = jso.optString("type");// 操作类别
		String railAndAlert = jso.optString("railAndAlert");//
		String areaNum = jso.optString("areaNum");//
		String maxLongitude = jso.optString("maxLongitude");//
		String maxLatitude = jso.optString("maxLatitude");//
		String minLongitude = jso.optString("minLongitude");//
		String minLatitude = jso.optString("minLatitude");//
		String startDate = jso.optString("startDate");//
		String endDate = jso.optString("endDate");//
		String isChange = jso.optString("isChange");//
		obdApiLogger.info(deviceId + ":操作类型:" + type + "***railAndAlert:" + railAndAlert + "***areaNum:" + areaNum
				+ "***maxLongitude:" + maxLongitude + "***maxLatitude:" + maxLatitude + "***minLongitude:"
				+ minLongitude + "***minLatitude:" + minLatitude + "***startDate:" + startDate + "***endDate:" + endDate
				+ "***isChange:" + isChange);
		if(StringUtils.isEmpty(type)||StringUtils.isEmpty(railAndAlert)){
			throw new Exception(deviceId+"*************电子围栏请求参数不全,请联系管理员.");
		}
		if("0".equals(type)){
			if("1".equals(railAndAlert)||"2".equals(railAndAlert)||"3".equals(railAndAlert)){
				if(StringUtils.isEmpty(areaNum)||StringUtils.isEmpty(maxLongitude)||StringUtils.isEmpty(maxLatitude)||StringUtils.isEmpty(minLongitude)||StringUtils.isEmpty(minLatitude)){
					throw new Exception(deviceId+"*************电子围栏请求参数不全,请联系管理员.");
				}
			}
		}else if("1".equals(type)){
			if("1".equals(railAndAlert)||"2".equals(railAndAlert)||"3".equals(railAndAlert)){
				if(StringUtils.isEmpty(areaNum)||StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate)){
					throw new Exception(deviceId+"*************电子围栏请求参数不全,请联系管理员.");
				}
			}
		}else{
			throw new Exception(deviceId+"*************电子围栏请求参数错误,请联系管理员.");
		}
		
		if("4".equals(railAndAlert)){
			if(StringUtils.isEmpty(areaNum)){
				throw new Exception(deviceId+"*************电子围栏请求参数不全,请联系管理员.");
			}
		}
		
		int state = 0;
		int code = 0;
		String desc = "";
		JSONObject resultJso = new JSONObject();
		// 1.查询是否存在该设备号
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		try {
			if (obdStockInfo != null) {
				String obdSN = obdStockInfo.getObdSn();
				
				Efence efence = new Efence();
				efence.setObdSn(obdSN);
				efence.setType(type);
				efence.setRailAndAlert(railAndAlert);
				if (!StringUtils.isEmpty(areaNum)) {
					efence.setAreaNum(areaNum);
				}
				if (!StringUtils.isEmpty(maxLongitude)) {
					efence.setMaxLongitude(maxLongitude);
					efence.setMaxLatitude(maxLatitude);
					efence.setMinLongitude(minLongitude);
					efence.setMinLatitude(minLatitude);
				}
				if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
					efence.setStartDate((Date) DateUtil.fromatDate(startDate, "yyyy-MM-dd HH:mm:ss"));
					efence.setEndDate((Date) DateUtil.fromatDate(endDate, "yyyy-MM-dd HH:mm:ss"));
				}
				if (!StringUtils.isEmpty(isChange)) {
					efence.setIsChange(isChange);
				}
				efence.setCreateTime(new Date());
				if("4".equals(railAndAlert)||"5".equals(railAndAlert)){
					efence.setValid("0");
				}else{
					efence.setValid("1");
				}
				int i = efenceService.efenceNoValid(obdSN);// 将所有电子围栏置为无效
				obdApiLogger.info(deviceId + "****电子围栏设为无效的个数:" + i);
//				// 如果是修改电子围栏
//				if (!StringUtils.isEmpty(isChange) && "1".equals(isChange)) {
//					int i = efenceService.efenceNoValidByAreaNum(obdSN, areaNum);// 将所有电子围栏置为无效
//					logger.info(deviceId + "****电子围栏设为无效的个数:" + i);
//
//				} else if (StringUtils.isEmpty(isChange) || "0".equals(isChange)) {
//					// 新增电子围栏
//					if (!StringUtils.isEmpty(railAndAlert) && "4".equals(railAndAlert)) {
//						int i = efenceService.efenceNoValidByAreaNum(obdSN, areaNum);// 将所有电子围栏置为无效
//						logger.info(deviceId + "****电子围栏设为无效的个数:" + i);
//					} else if (!StringUtils.isEmpty(railAndAlert) && "5".equals(railAndAlert)) {
//						int i = efenceService.efenceNoValid(obdSN);// 将所有电子围栏置为无效
//						logger.info(deviceId + "****电子围栏设为无效的个数:" + i);
//					} 
//				}
				// 保存新的电子围栏
				efenceService.save(efence);
				desc = deviceId + ":操作成功.";
				obdApiLogger.info(deviceId + "*******************************电子围栏设置成功!");
			} else {
				state = -1;
				code = -1;
				desc = "设置失败,该设备不存在,请联系管理员:" + deviceId;
				obdApiLogger.info(deviceId + "*******************************电子围栏设置失败.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.error(e);
			state = -1;
			code = -1;
			desc = deviceId + ":电子围栏设置异常,请联系管理员.";
		}

		JSONObject retJso = createReturnJson(code, desc);
		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info(deviceId + "***********电子围栏设置结果:" + retJso);
		return retJso;
	}
	
	/**
	 * 驾驶行为参数设置
	 * @throws Exception 
	 */
	private JSONObject setdriveBehaviour(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【驾驶行为参数设置】----------");
		if (!StringUtils.isEmpty(quickenSpeed)) {
			jso.put("quickenSpeed", quickenSpeed);
		}
		if (!StringUtils.isEmpty(quickSlowDownSpeed)) {
			jso.put("quickSlowDownSpeed", quickSlowDownSpeed);
		}
		if (!StringUtils.isEmpty(quickturnSpeed)) {
			jso.put("quickturnSpeed", quickturnSpeed);
		}
		if (!StringUtils.isEmpty(quickturnAngle)) {
			jso.put("quickturnAngle", quickturnAngle);
		}
		if (!StringUtils.isEmpty(overspeed)) {
			jso.put("overspeed", overspeed);
		}
		if (!StringUtils.isEmpty(overspeedTime)) {
			jso.put("overspeedTime", overspeedTime);
		}
		if (!StringUtils.isEmpty(fatigueDrive)) {
			jso.put("fatigueDrive", fatigueDrive);
		}
		if (!StringUtils.isEmpty(fatigueSleep)) {
			jso.put("fatigueSleep", fatigueSleep);
		}
		// 查询设备状态
		String quickenSpeed = jso.optString("quickenSpeed");
		String quickSlowDownSpeed = jso.optString("quickSlowDownSpeed");
		String quickturnSpeed = jso.optString("quickturnSpeed");
		String quickturnAngle = jso.optString("quickturnAngle");
		String overspeed = jso.optString("overspeed");
		String overspeedTime = jso.optString("overspeedTime");
		String fatigueDrive = jso.optString("fatigueDrive");
		String fatigueSleep = jso.optString("fatigueSleep");
		
		String deviceId = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		obdApiLogger.info("----------【驾驶行为参数设置】设备:" + deviceId
				+"---急加速:"+quickenSpeed
				+"---急减速:"+quickSlowDownSpeed
				+"---急转弯:"+quickturnSpeed+","+quickturnAngle
				+"---超速:"+overspeed+","+overspeedTime
				+"---疲劳驾驶:"+fatigueDrive+","+fatigueSleep);
		
		
		if((StringUtils.isEmpty(quickturnSpeed) && !StringUtils.isEmpty(quickturnAngle)) || (!StringUtils.isEmpty(quickturnSpeed) && StringUtils.isEmpty(quickturnAngle))){
			throw new Exception("请求参数错误,请联系管理员.");
    	}
		if((StringUtils.isEmpty(overspeed) && !StringUtils.isEmpty(overspeedTime)) || (!StringUtils.isEmpty(overspeed) && StringUtils.isEmpty(overspeedTime))){
			throw new Exception("请求参数错误,请联系管理员.");
    	}
		if((StringUtils.isEmpty(fatigueDrive) && !StringUtils.isEmpty(fatigueSleep)) || (!StringUtils.isEmpty(fatigueDrive) && StringUtils.isEmpty(fatigueSleep))){
			throw new Exception("请求参数错误,请联系管理员.");
    	}
		if(StringUtils.isEmpty(deviceId)){
			throw new Exception("请求参数错误,请联系管理员.");
		}
		if(StringUtils.isEmpty(quickenSpeed) && StringUtils.isEmpty(quickSlowDownSpeed) && StringUtils.isEmpty(quickturnSpeed) && StringUtils.isEmpty(quickturnAngle)
				&& StringUtils.isEmpty(overspeed) && StringUtils.isEmpty(overspeedTime) && StringUtils.isEmpty(fatigueDrive) && StringUtils.isEmpty(fatigueSleep)){
			throw new Exception("请求参数错误,请联系管理员.");
    	}
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		String desc = "";
		int code = 0;
		int state = 0;
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
			String obdSN = obdStockInfo.getObdSn();
			//1.更新对应驾驶行为 参数表
			OBDTravelParams op =  obdTravelParamsService.queryByObdSn(obdSN);
			if(op==null){
				op = new OBDTravelParams();
				op.setId(IDUtil.createID());
				op.setObdSn(obdSN);
				op.setCreateTime(new Date());
			}
			//2.更新obd设置表
			String bitStr="1111111111111111";
			JSONArray jms = new JSONArray();  
			if(!StringUtils.isEmpty(quickenSpeed)){
				//急加速,速度阈值单位km/s
				//急加速, 时间阈值秒
				Integer qk1 = Integer.parseInt(quickenSpeed);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_00.getValue(), "quickenSpeed", quickenSpeed,null, null);
				op.setShuddenOverSpeed(qk1);// 急加速阈值：速度变化阈值km/h
				op.setShuddenOverSpeedTime(2);
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 3, 4, "0");
			}
			
			if(!StringUtils.isEmpty(quickSlowDownSpeed)){
				//急减速,速度阈值单位km/s
				//急减速, 时间阈值秒
				Integer qsd1 = Integer.parseInt(quickSlowDownSpeed);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_01.getValue(), "quickSlowDownSpeed", quickSlowDownSpeed, null, null);
				op.setShuddenLowSpeed(qsd1);//速度变化阈值km/h
				op.setShuddenLowSpeedTime(2);// 急减速阈值：	时间阈值
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 4, 5, "0");
			}
			if(!StringUtils.isEmpty(quickturnSpeed) && !StringUtils.isEmpty(quickturnAngle)){
				//急转弯,速度阈值(类型2)，单位km/s
				// 急转弯阈值：角度阈值 度
				Integer qt1 = Integer.parseInt(quickturnSpeed);
				Integer qt2 = Integer.parseInt(quickturnAngle);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_02.getValue(), "quickturnSpeed", quickturnSpeed, "quickturnAngle", quickturnAngle);
				op.setShuddenTurnSpeed(qt1);// 急转弯阈值：速度阈值 km/h
				op.setShuddenTurnAngle(qt2);
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 2, 3, "0");
			}
			
			if(!StringUtils.isEmpty(overspeed) && !StringUtils.isEmpty(overspeedTime)){
				//超速,速度阈值单位km/s
				Integer os1 = Integer.parseInt(overspeed);
				Integer os2 = Integer.parseInt(overspeedTime);
				jsonArrayAdd(jms, "type", SettingType.DRIVE_03.getValue(), "overspeed", overspeed, "overspeedTime", overspeedTime);
				op.setOverSpeed(os1);// 超速阈值：时速阈值km/h
				op.setLimitSpeedLazy(os2);// 超速阈值：限速延迟时间阈值s
				bitStr = StrUtil.StringReplaceByIndex(bitStr, 1, 2, "0");
			}
			if(!StringUtils.isEmpty(fatigueDrive) && !StringUtils.isEmpty(fatigueSleep)){
				//疲劳驾驶连续驾驶超过阈值（类型6）单位小时
				//疲劳驾驶休息时间阈值（类型6）单位分
				jsonArrayAdd(jms, "type", SettingType.DRIVE_04.getValue(), "fatigueDrive", fatigueDrive, "fatigueSleep", fatigueSleep);
				//疲劳驾驶无
				op.setFatigueDrive(Integer.parseInt(fatigueDrive));
				op.setFatigueSleep(Integer.parseInt(fatigueSleep));
				
				driveTimeUtil.resetParamsClean(obdSN);
				obdApiLogger.info("----------【驾驶行为参数设置】清空驾驶疲劳设置参数,设备："+ obdSN);
			}
			//下发指令给obd
			String rt=serverSettingService.travelParamsSet(obdSN, bitStr.toCharArray(), op);
			boolean flag = GlobalData.isSendResultSuccess(rt);
			obdApiLogger.info("----------【驾驶行为参数设置】设备：" + deviceId + "，操作结果：" + flag);
			desc = flag?"操作成功.":"设备离线,离线设置成功.";
			//保存到obd设置表里
			for (int i=0;i<jms.size();i++) {
				JSONObject jobj = jms.getJSONObject(i);
				ObdSetting obdSetting = new ObdSetting();
				obdSetting.setId(IDUtil.createID());
				obdSetting.setObdSn(obdSN);
				obdSetting.setCreateTime(new Date());
				obdSetting.setType(jobj.getString("type"));//20obd驾驶行为参数设置
				jobj.remove("type");//去掉type类型
				obdSetting.setSettingMsg(jobj.toString());
				if(flag){
					obdSetting.setValid("0");
				}else{
					obdSetting.setValid("1");
				}
				obdSettingService.obdSettingNoValid(obdSN, obdSetting.getType());
				//调用接口下发设置
				obdSettingService.obdSettingSave(obdSetting);	
			}
			
			//不管是否成功,都更新到
			if(flag){
				boolean f=obdTravelParamsService.add(op);
				obdApiLogger.info("----------【驾驶行为参数设置】设备：" + deviceId + "，下发指令成功，行程参数记录保存结果：" + f);
				if(!StringUtils.isEmpty(fatigueDrive) && !StringUtils.isEmpty(fatigueSleep)){
//					if(DriveTimeUtil.OBD_LimitDriveTime.containsKey(obdSN)){
//						DriveTimeUtil.OBD_LimitDriveTime.remove(obdSN);
//					}
				}
			}else{
				//疲劳驾驶入库
				if(!StringUtils.isEmpty(fatigueDrive) && !StringUtils.isEmpty(fatigueSleep)){
					OBDTravelParams op2 =  obdTravelParamsService.queryByObdSn(obdSN);
					if(op2==null){
						op2 = new OBDTravelParams();
						op2.setId(IDUtil.createID());
						op2.setObdSn(obdSN);
						op2.setCreateTime(new Date());
					}
					op2.setFatigueDrive(Integer.parseInt(fatigueDrive));
					op2.setFatigueSleep(Integer.parseInt(fatigueSleep));
					boolean f=obdTravelParamsService.add(op2);
					obdApiLogger.info("----------【驾驶行为参数设置】设备：" + deviceId + "，下发指令失败，行程参数记录保存结果：" + f);
					if(DriveTimeUtil.OBD_LimitDriveTime.containsKey(obdSN)){
						DriveTimeUtil.OBD_LimitDriveTime.remove(obdSN);
					}
				}
			}
		} else {
			desc = "操作失败，设备不存在.";
			state = -1;
			code = -1;
		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【驾驶行为参数设置】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}
	
	private JSONArray jsonArrayAdd(JSONArray jsonArray,String key,String keyValue,String msgKey1,String msg1,String msgKey2,String msg2) throws Exception{
		JSONObject jb = new JSONObject();
		if(StringUtils.isEmpty(key)){
			throw new Exception("驾驶行为参数不能为空.");
		}
		jb.put(key, keyValue);
		if(!StringUtils.isEmpty(msgKey1)){
			jb.put(msgKey1, msg1);
		}
		if(!StringUtils.isEmpty(msgKey2)){
			jb.put(msgKey2, msg2);
		}
		jsonArray.add(jb);
		return jsonArray;
	}
	
	/**
	 * wifi使用时间设置
	 * 1.先插入wifiSet表
	 * 2.再写入obdTimeParams表
	 * @throws Exception 
	 */
	public JSONObject wifiUseTime(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【wifi使用时间设置】----------");
		if (useTime!=null) {
			jso.put("useTime", useTime);
		}
		// 查询设备状态
		String deviceId = jso.optString("deviceId");
		Integer useTime =jso.optInt("useTime");
		obdApiLogger.info("----------【wifi使用时间设置】设备：" + deviceId+"------时间:"+useTime);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		String desc = "";
		int code = 0;
		int state = 0;
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
			if(useTime>=1 && useTime <=20){
				String obdSN = obdStockInfo.getObdSn();
				String bits = "10111111";
				OBDTimeParams obdTimeParams = null;
				obdTimeParams=obdTimeParamsService.getObdTimeParamsBySn(obdSN);
				if(obdTimeParams==null){
					obdTimeParams = new OBDTimeParams(); 
					obdTimeParams.setId(IDUtil.createID());
					obdTimeParams.setObdSn(obdSN);
					obdTimeParams.setCreateTime(new Date());
				}
				obdTimeParams.setWifiUseTime(useTime);
				
				String rt=serverSettingService.deviceTimeSet(obdSN, bits.toCharArray(), obdTimeParams);
				boolean flag = GlobalData.isSendResultSuccess(rt);
//				state = flag ? 0 : -1;
				desc = flag?"操作成功.":"本次设置失败,将进行离线设置.";
				obdApiLogger.info("----------【wifi使用时间设置】设置结果：" + flag);
				//生成wifiSet记录
				WifiSet wifiSet = new WifiSet();
				wifiSet.setObdSn(obdSN);
				wifiSet.setCreateTime(new Date());
				wifiSet.setType("5");// wifi开关
				wifiSet.setUseTime(useTime);// wifi使用时间;
				wifiSet.setValid(flag?"0":"1");
				wifiStateSetService.save(wifiSet);// 保存wifiset记录
				if(flag){
					//下发成功才保存.
					obdTimeParamsService.saveOrUpdate(obdTimeParams);
				}
			}else {
				state = -1;
				code =0;
				desc ="操作失败,请求参数有误.";
			}
			
		} else {
			desc = "操作失败，设备不存在.";
			state = -1;
			code = -1;
		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【wifi使用时间设置】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}
	
	
	/**
	 * wifi密码和名称ssid
	 * 1.先插入wifiSet表
	 * 2.再写入obdTimeParams表
	 * @throws Exception 
	 */
	public JSONObject wifiPwdAndName(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【wifi密码和名称ssid设置】----------");
		if (!StringUtils.isEmpty(wPwd)) {
			jso.put("wPwd", wPwd);
		}
		if (!StringUtils.isEmpty(wName)) {
			jso.put("wName", wName);
		}
		// 查询设备状态
		String deviceId = jso.optString("deviceId");
		String wPwd = jso.optString("wPwd");
		String wName = jso.optString("wName");
		obdApiLogger.info("----------【wifi密码和名称ssid设置】设备：" + deviceId+"------pwd:"+wPwd+"---ssid:"+wName);
		if(StringUtils.isEmpty(wPwd) && StringUtils.isEmpty(wName)){
			throw new Exception("请求参数有误,请联系管理员.");
		}
		if(!StringUtils.isEmpty(wPwd)){
			if(wPwd.length()<8 || wPwd.length()>30){
				throw new Exception("wifi密码长度>=8且<=30");
			}
			
		}
		if(!StringUtils.isEmpty(wName)){
			if(wName.length()>30){
				throw new Exception("wifi名称长度>=1且<=30");
			}
		}
		
//		if(StrUtil.stringRex(wPwd, "[\u0391-\uFFE5]+") || StrUtil.stringRex(wName, "[\u0391-\uFFE5]+")){
//			throw new Exception("wifi名称和名称不能包含中文.");
//		}
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		String desc = "";
		int code = 0;
		int state = 0;
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
				String obdSN = obdStockInfo.getObdSn();
				String type="";
				Map<String, Integer> map = new HashMap<String, Integer>();
				String rt= null;
				String bits = "0000001100000000";
				if(!StringUtils.isEmpty(wPwd) && !StringUtils.isEmpty(wName)){
//					bits = "0000001100000000";
					type = SettingType.WIFI_12.getValue();
					map.put(SettingType.WIFI_12.getValue(), 1);
					map.put(SettingType.WIFI_11.getValue(), 1);
					map.put(SettingType.WIFI_10.getValue(), 1);
//					rt=serverSettingService.portalOrWifiSet(obdSN, bits.toCharArray(), wName,wPwd);
					bits = "0000001000000000";
					String rt1=serverSettingService.portalOrWifiSet(obdSN, bits.toCharArray(), wName);
					boolean flag1 = GlobalData.isSendResultSuccess(rt1);
					bits = "0000000100000000";
					String rt2=serverSettingService.portalOrWifiSet(obdSN, bits.toCharArray(), wPwd);
					boolean flag2 = GlobalData.isSendResultSuccess(rt2);
					if(flag1 && flag2){
						rt = "00";//如果两个都是成功,默认是成功
					}
				}else if(!StringUtils.isEmpty(wPwd)){
					bits = "0000000100000000";
					type = SettingType.WIFI_10.getValue();
					map.put(SettingType.WIFI_12.getValue(), 1);
					map.put(SettingType.WIFI_10.getValue(), 1);
					rt=serverSettingService.portalOrWifiSet(obdSN, bits.toCharArray(), wPwd);
				}else if(!StringUtils.isEmpty(wName)){
					bits = "0000001000000000";
					type = SettingType.WIFI_11.getValue();
					map.put(SettingType.WIFI_12.getValue(), 1);
					map.put(SettingType.WIFI_11.getValue(), 1);
					rt=serverSettingService.portalOrWifiSet(obdSN, bits.toCharArray(), wName);
				}
				
				boolean flag = GlobalData.isSendResultSuccess(rt);
				desc = flag?"操作成功.":"本次设置失败,将进行离线设置.";
				obdApiLogger.info("----------【wifi密码和名称ssid设置】设置结果：" + flag);
				//保存在obdSetting表里
				JSONObject jb = new JSONObject();
				if(!StringUtils.isEmpty(wPwd)){
					jb.put("wPwd", wPwd);
				}
				if(!StringUtils.isEmpty(wName)){
					jb.put("wName", wName);
				}
				Integer total=obdSettingService.setNoValidByInType(obdSN, map);
				obdApiLogger.info("----------【wifi密码和名称ssid设置】将设备:"+deviceId +"---之前的wifi密码和ssid置为无效总数:"+ total);
				
				ObdSetting obdSetting = new ObdSetting();
				obdSetting.setId(IDUtil.createID());
				obdSetting.setObdSn(obdSN);
				obdSetting.setCreateTime(new Date());
				obdSetting.setType(type);//
				obdSetting.setSettingMsg(jb.toString());
				if(flag){
					obdSetting.setValid("0");
				}else{
					obdSetting.setValid("1");
				}
				//调用接口下发设置
				obdSettingService.save(obdSetting);	
				
				if(flag){
					//参数保存在wifi表里
					 Wifi wifi = wiFiService.isExist(obdSN);
					 if(wifi==null){
						 wifi = new Wifi();
						 wifi.setId(IDUtil.createID());
						 wifi.setCreateTime(new Date());
					 }
					 wifi.setObdSn(obdSN);
					 if(!StringUtils.isEmpty(wPwd)){
						 wifi.setWifiPwd(wPwd);
					 }
					 if(!StringUtils.isEmpty(wName)){
						 wifi.setSsid(wName);
					 }
					 wiFiService.wifiSave(wifi);
				}
			
		} else {
			desc = "操作失败，设备不存在.";
			state = -1;
			code = -1;
		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【wifi密码和名称ssid设置】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}
	

	/**
	 * 设置OBD设备报警开关
	 * 1.先请求obd获取设置状态
	 * 表obdState表查看是否存在表字段
	 * 握手表obd_handshake里
	 * 2.如果都没有该状态就不给设置
	 * 需下发obd
	 * 0	非法启动探测
		1	非法震动探测
		2	蓄电电压异常报警
		3	发动机水温高报警
		4	车辆故障报警
		5	超速报警
		6	电子围栏报警
		7	保留
		不需要下发给obd
		8   疲劳驾驶开关（包括疲劳驾驶接触）
		9   急变速开关
		10 怠速告警开关
	 * @throws Exception 
	 */
	public JSONObject alarmSwitch(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【OBD设备报警开关设置】----------");
		if (!StringUtils.isEmpty(switchType)) {
			jso.put("switchType", switchType);
		}
		if (!StringUtils.isEmpty(switchState)) {
			jso.put("switchState", switchState);
		}
		// 查询设备状态
		String deviceId = jso.optString("deviceId");
		String switchType = jso.optString("switchType");
		String switchState = jso.optString("switchState");
		obdApiLogger.info("----------【OBD设备报警开关设置】设备：" + deviceId+"------类别:"+switchType+"---开关状态:"+switchState);
		if(StringUtils.isEmpty(switchType) || StringUtils.isEmpty(switchState)){
			throw new Exception("请求参数有误,请联系管理员.");
		}
		
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		String desc = "操作成功.";
		int code = 0;
		int state = 0;
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
				String obdSN = obdStockInfo.getObdSn();
				
				//先查询obdState表
				ObdState obdState = obdSateService.queryByObdSn(obdSN);
				boolean flag = true;//用于判断是否保存obdState
				boolean sendObd = true;//true需同步状态给obd,false不需要下发给obd
				
				//保存
				String sType="";
				switch (switchType) {
				case "0":
					sType = SettingType.SWITCH_00.getValue();
					break;
				case "1":
					sType = SettingType.SWITCH_01.getValue();
					break;
				case "2":
					sType = SettingType.SWITCH_02.getValue();
					break;
				case "3":
					sType = SettingType.SWITCH_03.getValue();
					break;
				case "4":
					sType = SettingType.SWITCH_04.getValue();
					break;
				case "5":
					sType = SettingType.SWITCH_05.getValue();
					break;
				case "6":
					sType = SettingType.SWITCH_06.getValue();
					break;
				case "7":
					sType = SettingType.SWITCH_07.getValue();
					break;
				case "8":
					sendObd = false;
					break;
				case "9":
					sendObd = false;
					break;
				case "10":
					sendObd = false;
					break;
				default:
					throw new Exception("请求参数有误,请联系管理员.");
				}
				
				if(sendObd){
					boolean remarkFlag = false;
					
					//开关状态
					String startupSwitch = null;
					String shakeSwitch = null;
					String voltageSwitch = null;
					String engineTempSwitch = null;
					String carfaultSwitch = null;
					String overspeedSwitch = null;
					String efenceSwitch = null;
//					String backupSwitch = "0";
					String warnSet="";
					//直接查询obd如果查询到就设置，否则离线设置.
					ObdHandShake obdHandShake=serverRequestQueryService.warnSettings(obdSN);
					obdApiLogger.info("----------【OBD设备报警开关设置】最后握手包：" + obdHandShake);
					if(obdHandShake!=null){
						startupSwitch = obdHandShake.getIllegalStartSet().toString();//非法启动
						shakeSwitch = obdHandShake.getIllegalShockSet().toString();//非法启动探测设置：0-开启 1-关闭
						voltageSwitch = obdHandShake.getVoltUnusualSet().toString();//蓄电电压异常报警设置
						engineTempSwitch = obdHandShake.getEngineWaterWarnSet().toString();//发动机水温高报警设置
						carfaultSwitch = obdHandShake.getCarWarnSet().toString();//车辆故障报警设置
						overspeedSwitch = obdHandShake.getOverSpeedWarnSet().toString();//超速报警设置
						efenceSwitch = obdHandShake.getEfenceWarnSet().toString();//电子围栏报警设置
						if(!StringUtils.isEmpty(startupSwitch) && !StringUtils.isEmpty(shakeSwitch)
								&& !StringUtils.isEmpty(voltageSwitch) && !StringUtils.isEmpty(engineTempSwitch) 
								&& !StringUtils.isEmpty(carfaultSwitch) && !StringUtils.isEmpty(overspeedSwitch)
								&& !StringUtils.isEmpty(efenceSwitch)){
							warnSet = startupSwitch + shakeSwitch + voltageSwitch + engineTempSwitch + carfaultSwitch + overspeedSwitch + efenceSwitch + "1"; 
							
							remarkFlag = true;
						}
					}

					//如果flag为false:调用接口查询obd预警开关最终状态
					//如果都有值,下发指令,否则离线设置。
					if(remarkFlag){
						Integer index = Integer.parseInt(switchType);
						String switchS = StrUtil.strExchange(switchState, "0", "1");//0和1对换
						warnSet = StrUtil.StringReplaceByIndex(warnSet, index, index+1, switchS);
						String rt=serverSettingService.warnSet(obdSN, warnSet);
						flag = GlobalData.isSendResultSuccess(rt);
					}else{
						flag = false;
					}
					
					obdApiLogger.info("----------【OBD设备报警开关设置】设置结果：" + flag);
					desc = flag?"操作成功.":"本次设置失败,将进行离线设置.";
					
					//保存在obdSetting表里
					JSONObject jb = new JSONObject();
					jb.put("switchType", switchType);
					jb.put("switchState", switchState);
					Integer total=obdSettingService.obdSettingNoValid(obdSN, sType);
					obdApiLogger.info("----------【OBD设备报警开关设置】将设备:"+deviceId +"---置为无效总数:"+ total);
					
					ObdSetting obdSetting = new ObdSetting();
					obdSetting.setId(IDUtil.createID());
					obdSetting.setObdSn(obdSN);
					obdSetting.setCreateTime(new Date());
					obdSetting.setType(sType);//
					obdSetting.setSettingMsg(jb.toString());
					if(flag){
						obdSetting.setValid("0");
					}else{
						obdSetting.setValid("1");
					}
					//调用接口下发设置
					obdSettingService.save(obdSetting);	
				}
				
				//不管是否设置成功,都保存用户设置状态
				if(obdState==null){
					obdState = new ObdState();
					obdState.setId(IDUtil.createID());
					obdState.setObdSn(obdSN);
					obdState.setCreateTime(new Date());
				}else{
					obdState.setUpdateTime(new Date());
				}
				
				switch (switchType) {
					case "0":
						obdState.setStartupSwitch(switchState);
						break;
					case "1":
						obdState.setShakeSwitch(switchState);
						break;
					case "2":
						obdState.setVoltageSwitch(switchState);
						break;
					case "3":
						obdState.setEngineTempSwitch(switchState);
						break;
					case "4":
						obdState.setCarfaultSwitch(switchState);
						break;
					case "5":
						obdState.setOverspeedSwitch(switchState);
						break;
					case "6":
						obdState.setEfenceSwitch(switchState);
						break;
					case "7":
						obdState.setBackupSwitch(switchState);
						break;
					case "8":
						obdState.setFatigueDriveSwitch(switchState);
						break;
					case "9":
						obdState.setRapidSpeedChangeSwitch(switchState);
						break;
					case "10":
						obdState.setIdlingSwitch(switchState);
						break;
					default:
						break;
				}
				boolean f=obdSateService.add(obdState);
				obdApiLogger.info("----------【OBD设备报警开关设置】设备：" + deviceId + "，obd状态记录保存结果：" + f);
			
		} else {
			desc = "操作失败，设备不存在.";
			state = -1;
			code = -1;
		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【OBD设备报警开关设置】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	
	}
	
	
	/**
	 * 设置OBD设备报警开关
	 * 1.先请求obd获取设置状态
	 * 表obdState表查看是否存在表字段
	 * 握手表obd_handshake里
	 * 2.如果都没有该状态就不给设置
	 * 需下发obd
	 * 0	非法启动探测
		1	非法震动探测
		2	蓄电电压异常报警
		3	发动机水温高报警
		4	车辆故障报警
		5	超速报警
		6	电子围栏报警
		7	保留
		不需要下发给obd
		8   疲劳驾驶开关（包括疲劳驾驶接触）
		9   急变速开关
		10 怠速告警开关
	 * @throws Exception 
	 */
	public JSONObject alarmSwitchState(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【OBD设备开关状态查询】----------");
		if (!StringUtils.isEmpty(switchType)) {
			jso.put("switchType", switchType);
		}
		// 查询设备状态
		String deviceId = jso.optString("deviceId");
		String switchType = jso.optString("switchType");
		obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------类别:"+switchType);
		if(StringUtils.isEmpty(switchType)){
			throw new Exception("请求参数有误,请联系管理员.");
		}
		
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		String desc = "操作成功.";
		int code = 0;
		String state = "-1";
		JSONObject resultJso = new JSONObject();
		if (obdStockInfo != null) {
				String obdSN = obdStockInfo.getObdSn();
				
				boolean flag = true;//用于判断是否需要查询obd状态
				
				switch (switchType) {
				case "0":
					flag = false;
					break;
				case "1":
					flag = false;
					break;
				case "2":
					flag = false;
					break;
				case "3":
					flag = false;
					break;
				case "4":
					flag = false;
					break;
				case "5":
					flag = false;
					break;
				case "6":
					flag = false;
					break;
				case "7":
					flag = false;
					break;
				case "8":
					flag = false;
					break;
				case "9":
					flag = false;
					break;
				case "10":
					flag = false;
				case "11":
					//WiFi开关下发查询
					break;
				case "12":
					//gps开关下发查询
					break;
				case "13":
					flag = false;
					break;
				default:
					throw new Exception("请求参数有误,请联系管理员.");
				}
				//obdState表
				if(!flag){
					ObdState obdState = obdSateService.queryByObdSn(obdSN);
					//1.无需下发查询命令
					if(obdState!=null){
						boolean nsflag = false;
						switch (switchType) {
						case "0":
							String startupSwitch = obdState.getStartupSwitch();
							if(!StringUtils.isEmpty(startupSwitch)){
								state = startupSwitch;
								nsflag = true;
							}
							break;
						case "1":
							String shakeSwitch = obdState.getShakeSwitch();
							if(!StringUtils.isEmpty(shakeSwitch)){
								state = shakeSwitch;
								nsflag = true;
							}
							break;
						case "2":
							String voltageSwitch = obdState.getVoltageSwitch();
							if(!StringUtils.isEmpty(voltageSwitch)){
								state = voltageSwitch;
								nsflag = true;
							}
							break;
						case "3":
							String engineTempSwitch = obdState.getEngineTempSwitch();
							if(!StringUtils.isEmpty(engineTempSwitch)){
								state = engineTempSwitch;
								nsflag = true;
							}
							break;
						case "4":
							String carfaultSwitch = obdState.getCarfaultSwitch();
							if(!StringUtils.isEmpty(carfaultSwitch)){
								state = carfaultSwitch;
								nsflag = true;
							}
							break;
						case "5":
							String overspeedSwitch = obdState.getOverspeedSwitch();
							if(!StringUtils.isEmpty(overspeedSwitch)){
								state = overspeedSwitch;
								nsflag = true;
							}
							break;
						case "6":
							String efenceSwitch = obdState.getEfenceSwitch();
							if(!StringUtils.isEmpty(efenceSwitch)){
								state = efenceSwitch;
								nsflag = true;
							}
							break;
						case "7":
							break;
						case "8":
							String fatigueDriveSwitch = obdState.getFatigueDriveSwitch();
							if(!StringUtils.isEmpty(fatigueDriveSwitch)){
								state = fatigueDriveSwitch;
								nsflag = true;
							}
							break;
						case "9":
							String rapidSpeedChangeSwitch = obdState.getRapidSpeedChangeSwitch();
							if(!StringUtils.isEmpty(rapidSpeedChangeSwitch)){
								state = rapidSpeedChangeSwitch;
								nsflag = true;
							}
							break;
						case "10":
							String idlingSwitch = obdState.getIdlingSwitch();
							if(!StringUtils.isEmpty(idlingSwitch)){
								state = idlingSwitch;
								nsflag = true;
							}
							break;
						case "13":
							String safetySwitch = obdState.getSafetySwitch();
							if(!StringUtils.isEmpty(safetySwitch)){
								state = safetySwitch;
								nsflag = true;
							}
							break;
						default:
							break;
						}
						if(!nsflag){
							obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------查询失败.");
							state ="-1";
							desc ="该开关未设置,默认关闭.";
						}
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------开关状态:"+state);
					}else{
						state = "-1";
						desc ="设备未设置该开关.";
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------不存在状态记录");
					}
				}else{
					boolean sflag = false;
					//2需要下发查询:WiFi开关状态和gps开关状态
					//如果设备在线，下发查询命令
					if("01".equals(obdStockInfo.getStockState())){
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------设备在线查询");
						ObdHandShake obdHandShake = serverRequestQueryService.deviceState(obdSN);
						if(obdHandShake!=null){
							switch (switchType) {
							case "11":
								Integer wifiState = obdHandShake.getWifiSet();
								state = wifiState==0?"1":"0";
								break;
							case "12":
								Integer gpsState = obdHandShake.getGpsSet();
								state = gpsState==0?"1":"0";
								break;
							default:
								break;
							}
							sflag = true;
							obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------设备在线查询状态:"+state);
						}
					}
					//查询obdstockinfo表的gps开关和WiFi开关
//					ObdState obdState = obdSateService.queryByObdSn(obdSN);
					//如果设备不在线或查询不到结果,返回后台obd_state表的记录
					if(!sflag){
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------设备查询状态表.");
						//查询obdstate表
						switch (switchType) {
						case "11":
							String wifiState = obdStockInfo.getWifiState();
							if(!StringUtils.isEmpty(wifiState)){
								
								state = wifiState;
								sflag = true;
							}
							break;
						case "12":
							String gpsState = obdStockInfo.getGpsState();
							if(!StringUtils.isEmpty(gpsState)){
								state = gpsState;
								sflag = true;
							}
							break;
						default:
							break;
						}
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------设备查询状态表状态---"+state);
					}
					//如果obd_state表也没记录,查询最后一次握手包
					if(!sflag){
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------设备查询最后握手包.");
						//查询最后一个握手包
						ObdHandShake ohs=obdHandShakeService.findLastBySn(obdSN);
						if(ohs!=null){
							switch (switchType) {
							case "11":
								Integer wifiState = ohs.getWifiSet();
								state = wifiState==0?"1":"0";
								break;
							case "12":
								Integer gpsState = ohs.getGpsSet();
								state = gpsState==0?"1":"0";
								break;
							default:
								break;
							}
							sflag = true;
						}
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------设备查询最后握手包状态---"+state);
					}
					
					if(!sflag){
						state ="-1";
						desc = "未设置,请联系管理员.";
						obdApiLogger.info("----------【OBD设备开关状态查询】设备：" + deviceId+"------在线离线查询,状态表,握手包查询均失败.");
					}
				}
				
		} else {
			desc = "操作失败，设备不存在.";
			state = "-1";
			code = -1;
		}
		resultJso.put("state", state);
		JSONObject retJso = createReturnJson(code, desc);
		retJso.put("result", resultJso);
		obdApiLogger.info("----------【OBD设备报警开关设置】设备：" + deviceId + "，返回结果：" + retJso);
		return retJso;
	}
	public static void main(String[] args) throws Exception {
		Date startTime = new Date();
		Date endTime = null;
		try {
			endTime = new Date(dateFormat.parse("2015-08-13 12:23:33")
					.getTime());
			Set<String> list = DateUtil.getTimesByBetwwenTime(startTime,
					endTime, "yyyy-MM-dd");

			for (String string : list) {
				System.out.println(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("2015-11-08 12:23:33".substring(0, 10));

		Float f = new Float(123.0 / 345);
		System.out.println(f);
		System.out.println(NumUtil.getNumFractionDigits(f, 3));
		System.out.println(NumUtil.getNumFractionDigits(new Float(
				56.80 / 23.00 * 100), 4));
		JSONObject json = new JSONObject();
		json.put("k", "v");
		System.out.println("".equals(json.optString("kk")));

		System.out.println("| 115.63738 |  22.9922 | 1439361576000 | 2015-08-12 14:39:36 |".split("\\|").length);
		// NumUtil.getNumFractionDigits((_totalPetrolConsume/1000) / _totalMiles
		// * 100, 2)
		// NumUtil.getNumFractionDigits((_totalPetrolConsume/1000.0f) /
		// _totalMiles * 100.0f, 2)
		Float t = new Float(4417.0);
		Float tt = new Float(32.0);
		System.out.println("222:"
				+ NumUtil.getNumFractionDigits((t / 1000.0f) / tt * 100.0f, 2));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = sdf.parse("2015-08-19 22:17:05");
		Date d2 = sdf.parse("2015-08-19 21:30:32");
		System.out.println("123:" + (d1.getTime() - d2.getTime()) / 1000);

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = sdf.parse("2015-08-21 17:49:18");
		System.out.println(sdf1.format(d));
		System.out.println(sdf1.format(new Date(150821174918L)));

	}

	// 测试
	private void test() {
//		System.out.println(oap.getpObdSn());
	}

	private String deviceId;
	private String username;
	private String time;
	private String sign;
	private String mobileType;
	private String userId;// 用户唯一标识（手机号码）
	private String hgDeviceSn;// 华工二维码
	private String operationType;
	private String beginTime;
	private String endTime;
	private String beginDate;
	private String endDate;
	private String startTime;
	private String theDate;
	private String state;
	private String simId;// 流量卡号
	private String userType;// 用户类型
	private String minLongitude;// 最小经度
	private String minLatitude;// 最小纬度
	private String maxLongitude;// 最大经度
	private String maxLatitude;// 最大纬度
	private String obdSn;// obdSnChange接口参数
	private String type;// 操作类别
	private String url;// url
	private String mb;// mb
	private String whitelists;// whitelists
	private String mac;// mac
	private String onOff;// onOff
	
	// 电子围栏
	// type已定义
	// private String type;//操作类别
	private String railAndAlert;
	private String areaNum;
	// 大经大维小经小纬已定义
	// private String minLongitude;// 最小经度
	// private String minLatitude;// 最小纬度
	// private String maxLongitude;// 最大经度
	// private String maxLatitude;// 最大纬度
	// 定时开始时间(未定义) ,定时结束时间(已定义)
	private String startDate;
	// private String endDate;
	private String isChange;
	//驾驶行为
	private Integer page;
	private Integer pageSize;
	//wifi使用时间
	private Integer useTime;
	//驾驶行为参数设置
	private String quickenSpeed;//急加速,速度阈值单位km/s
	private String quickSlowDownSpeed;//急减速,速度阈值单位km/s
	private String quickturnSpeed;//急转弯,速度阈值(类型2)，单位km/s
	private String quickturnAngle;//急转弯，角度阈值
	private String overspeed;//5-超速,速度阈值单位km/s
	private String overspeedTime;//5-超速, 时间阈值秒
	private String fatigueDrive;//疲劳驾驶连续驾驶超过阈值（类型6）单位小时
	private String fatigueSleep;//疲劳驾驶休息时间阈值（类型6）单位分
	//wifi名称和密码
	private String wPwd;
	private String wName;
	// 设置OBD设备报警开关
	private String switchType;
	private String switchState;
	
	
	public String getSwitchType() {
		return switchType;
	}

	public void setSwitchType(String switchType) {
		this.switchType = switchType;
	}

	public String getSwitchState() {
		return switchState;
	}

	public void setSwitchState(String switchState) {
		this.switchState = switchState;
	}

	public String getWPwd() {
		return wPwd;
	}

	public void setWPwd(String wPwd) {
		this.wPwd = wPwd;
	}

	public String getWName() {
		return wName;
	}

	public void setWName(String wName) {
		this.wName = wName;
	}

	public String getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(String minLongitude) {
		this.minLongitude = minLongitude;
	}

	public String getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(String minLatitude) {
		this.minLatitude = minLatitude;
	}

	public String getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(String maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public String getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(String maxLatitude) {
		this.maxLatitude = maxLatitude;
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

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTheDate() {
		return theDate;
	}

	public void setTheDate(String theDate) {
		this.theDate = theDate;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getHgDeviceSn() {
		return hgDeviceSn;
	}

	public void setHgDeviceSn(String hgDeviceSn) {
		this.hgDeviceSn = hgDeviceSn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
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

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMb() {
		return mb;
	}

	public void setMb(String mb) {
		this.mb = mb;
	}

	public String getWhitelists() {
		return whitelists;
	}

	public void setWhitelists(String whitelists) {
		this.whitelists = whitelists;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOnOff() {
		return onOff;
	}

	public void setOnOff(String onOff) {
		this.onOff = onOff;
	}

	public String getRailAndAlert() {
		return railAndAlert;
	}

	public void setRailAndAlert(String railAndAlert) {
		this.railAndAlert = railAndAlert;
	}

	public String getAreaNum() {
		return areaNum;
	}

	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getIsChange() {
		return isChange;
	}

	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getUseTime() {
		return useTime;
	}

	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}

	public String getQuickenSpeed() {
		return quickenSpeed;
	}

	public void setQuickenSpeed(String quickenSpeed) {
		this.quickenSpeed = quickenSpeed;
	}

	public String getQuickturnSpeed() {
		return quickturnSpeed;
	}

	public void setQuickturnSpeed(String quickturnSpeed) {
		this.quickturnSpeed = quickturnSpeed;
	}

	public String getQuickturnAngle() {
		return quickturnAngle;
	}

	public void setQuickturnAngle(String quickturnAngle) {
		this.quickturnAngle = quickturnAngle;
	}

	public String getOverspeed() {
		return overspeed;
	}

	public void setOverspeed(String overspeed) {
		this.overspeed = overspeed;
	}

	public String getOverspeedTime() {
		return overspeedTime;
	}

	public void setOverspeedTime(String overspeedTime) {
		this.overspeedTime = overspeedTime;
	}

	public String getQuickSlowDownSpeed() {
		return quickSlowDownSpeed;
	}

	public void setQuickSlowDownSpeed(String quickSlowDownSpeed) {
		this.quickSlowDownSpeed = quickSlowDownSpeed;
	}

	public String getFatigueDrive() {
		return fatigueDrive;
	}

	public void setFatigueDrive(String fatigueDrive) {
		this.fatigueDrive = fatigueDrive;
	}

	public String getFatigueSleep() {
		return fatigueSleep;
	}

	public void setFatigueSleep(String fatigueSleep) {
		this.fatigueSleep = fatigueSleep;
	}

	
	
}
