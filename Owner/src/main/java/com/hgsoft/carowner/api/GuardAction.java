package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.SettingType;

/**
 * 车主通平台与客户端接口:电子围栏查询
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class GuardAction extends BaseAction {

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
	private ObdSateService obdSateService;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ObdSettingService obdSettingService;

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
			oar.setMethod("guard");

			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);

			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.guard(recJson);
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
			obdApiLogger.error("----------【电信接口】设防撤防异常信息：" + e);
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
		obdApiLogger.info("----------【电信接口】接口操作记录保存结果---"+flag);
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
		if(StringUtils.isEmpty(operationType)){
			return returnJson(-1, -1, "请求参数有误,请联系管理员.");
		}
		
		// 查询车辆在线状态
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);// 设备号
		if(obdStockInfo==null){
			return returnJson(-1, -1, "该设备不存在,请联系管理员.");
		}
		
		String obdSN = obdStockInfo.getObdSn();
		obdApiLogger.info("----------【设置/取消车辆防盗】设备：" + deviceId + "->设备号:" + obdSN);
		boolean flag = false;
		String sType=SettingType.SWITCH_00.getValue();
		//开关状态
		String startupSwitch = null;
		String shakeSwitch = null;
		String voltageSwitch = null;
		String engineTempSwitch = null;
		String carfaultSwitch = null;
		String overspeedSwitch = null;
		String efenceSwitch = null;
//		String backupSwitch = "0";
		String warnSet="";
		
		//直接查询obd如果查询到就设置，否则离线设置.
		ObdHandShake obdHandShake=serverRequestQueryService.warnSettings(obdSN);
		obdApiLogger.info("----------【设置/取消车辆防盗】最后握手包：" + obdHandShake);
		
		if(obdHandShake!=null){
			boolean remarkFlag = false;
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
			//如果flag为false:调用接口查询obd预警开关最终状态
			//如果都有值,下发指令,否则离线设置。
			if(remarkFlag){
				String switchS = StrUtil.strExchange(operationType, "0", "1");//0和1对换
				warnSet = StrUtil.StringReplaceByIndex(warnSet, 0, 1, switchS);
				String rt=serverSettingService.warnSet(obdSN, warnSet);
				flag = GlobalData.isSendResultSuccess(rt);
			}
		}
		obdApiLogger.info("----------【设置/取消车辆防盗】设置结果：" + flag);
		desc = flag?"操作成功.":"本次设置失败,将进行离线设置.";
		//先将之前非法启动告警开关离线记录置为无效。
		Integer total=obdSettingService.obdSettingNoValid(obdSN, sType);
		obdApiLogger.info("----------【设置/取消车辆防盗】将设备:"+deviceId +"---置为无效总数:"+ total);
		if(!flag){
			JSONObject jb = new JSONObject();
			jb.put("switchType", "0");
			jb.put("switchState", operationType);
			
			ObdSetting obdSetting = new ObdSetting();
			obdSetting.setId(IDUtil.createID());
			obdSetting.setObdSn(obdSN);
			obdSetting.setCreateTime(new Date());
			obdSetting.setType(sType);//
			obdSetting.setSettingMsg(jb.toString());
			obdSetting.setValid("1");
			//调用接口下发设置
			boolean ff=obdSettingService.obdSettingSave(obdSetting);	
			obdApiLogger.info("----------【设置/取消车辆防盗】非法启动离线记录保存结果：" + ff);
		}
		
		//不管是否成功，都保存用户设置状态
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
		obdState.setStartupSwitch(operationType);
		obdSateService.add(obdState);
		desc = "操作成功." ;
		
		JSONObject retJso = returnJson(code, state, desc);
		obdApiLogger.info("----------【设置/取消车辆防盗】设备：" + deviceId + "->返回结果：" + retJso);
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
	private String operationType;

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

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

}
