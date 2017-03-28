package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
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
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.entity.ObdState;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdSateService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.service.ServerRequestQueryService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.SettingType;

/**
 * 车主通平台与客户端接口:查询当前位置
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class QueryCurrentLocationAction extends BaseAction {

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
	private CarGSPTrackService carGSPTrackService;

	// private SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
	// private SimpleDateFormat Hms = new SimpleDateFormat("HH:mm:ss");
	// private SimpleDateFormat yMdHms = new SimpleDateFormat("yyyy-MM-dd
	// HH:mm:ss");
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
		String dev = null;
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
			oar.setMethod("queryCurrentLocation");

			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);

			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.queryCurrentLocation(recJson);
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
			obdApiLogger.error("----------【电信接口】查询当前位置异常信息：" + e);
		}
		// 返回json数据
		if (returnJson == null) {
			returnJson = new JSONObject();
		}
		obdApiLogger.info("~obdApiLogger~:【" + request.getRemoteHost() + "】-设备:" + dev + "-" + requestUser + "-"
				+ request.getRequestURI() + "->" + (System.currentTimeMillis() - begin));
		oar.setReturnMsg(returnJson.toString());

		boolean flag = obdApiRecordService.irSave(oar);
		obdApiLogger.info("----------【电信接口】接口操作记录保存结果---" + flag);
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
	 * 当前位置查询 当前位置信息直接取数据库最新一条记录就可以
	 */
	public JSONObject queryCurrentLocation(JSONObject jso) {
		String deviceId = jso.optString("deviceId");
		obdApiLogger.info("----------【当前位置查询】设备：" + deviceId);
		int code = 0;
		String desc = "";
		JSONObject retJso = new JSONObject();
		JSONObject resultJso = new JSONObject();

		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		if (obdStockInfo == null) {
			// 查看是否存在该设备的位置信息，如果存在，新增相关信息
			code = -1;
			desc = "当前设备不存在，请联系管理员.";
			retJso = createReturnJson(code, desc);
			return retJso;
		}

		CarGSPTrack ct = null;// 最后一条位置信息
		String obdSn = obdStockInfo.getObdSn();
		ct = carGSPTrackService.findLastBySn(obdSn);
		String stockState = obdStockInfo.getStockState();
		int state = "01".equals(stockState) ? 1 : 0;
		if (ct != null) {
			code = 0;
			desc = "成功获取当前位置";
			retJso = createReturnJson(code, desc);
			 retJso.put("locatCode", "1");//0：定位无效，1：定位有效
			resultJso.put("longitude", CoordinateTransferUtil.lnglatTransferDouble(ct.getLongitude()));// 经度
			resultJso.put("latitude", CoordinateTransferUtil.lnglatTransferDouble(ct.getLatitude()));// 纬度
			resultJso.put("orient", ct.getDirectionAngle());// 方位
			// retJso.put("precision", state);//精度
			// retJso.put("altitude", state);//海拔高度
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			resultJso.put("time", sdf.format(ct.getGspTrackTime()));// 时间长度10字节格式UTC时间yyMMDDHHmmss
			resultJso.put("speed", ct.getGpsSpeed());// 速度
			resultJso.put("state", state);// 车辆启动状态 0x00车辆未启动 0x01车辆已启动
			obdApiLogger.info("----------【当前位置查询】成功：" + ct.getLatitude() + "----" + ct.getLongitude());
		} else {
			// 百度地图拾取到的经纬度是百度地图的经纬度，需要转换成原始的gps坐标：天河区体育西路.
			code = 0;
			desc = "定位失败,获取默认位置信息.";
			retJso = createReturnJson(code, desc);
			 retJso.put("locatCode", "0");//0：定位无效，1：定位有效
			resultJso.put("longitude", Double.parseDouble("113.316608"));// 经度
			resultJso.put("latitude", Double.parseDouble("23.140437"));// 纬度
			resultJso.put("orient", Float.parseFloat("0"));// 方位
			// retJso.put("precision", state);//精度
			// retJso.put("altitude", state);//海拔高度
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			resultJso.put("time", "");// 时间长度10字节格式UTC时间yyMMDDHHmmss
			resultJso.put("speed", Float.parseFloat("0"));// 速度
			resultJso.put("state", state);// 车辆启动状态 0x00车辆未启动 0x01车辆已启动
			obdApiLogger.info("----------【当前位置查询】失败：默认位置经纬度:" + "113.316608" + "----" + "23.140437");
		}

		retJso.put("result", resultJso);
		obdApiLogger.info("----------【当前位置查询】设备：" + deviceId + "返回结果：" + retJso);
		return retJso;

	}

//	private JSONObject returnJson(Integer code, Integer state, String desc) {
//		JSONObject retJso = createReturnJson(code, desc);
//		JSONObject resultJso = new JSONObject();
//		// 当前状态：0成功 -1失败
//		resultJso.put("state", state);
//		retJso.put("result", resultJso);
//		return retJso;
//	}

	private String deviceId;
	private String username;
	private String time;
	private String sign;

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

}
