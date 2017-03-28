package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.DomainState;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.service.DomainStateService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.util.SettingType;

/**
 * 车主通平台与客户端接口:域黑白名单查询
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class DomainQueryAction extends BaseAction {

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
	private DomainStateService domainStateService;
	@Resource
	private ObdSettingService obdSettingService;

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
			oar.setMethod("domainQuery");

			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);

			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.domainQuery(recJson);
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
		boolean flag =obdApiRecordService.irSave(oar);
		obdApiLogger.error("----------【电信接口操作记录保存结果】:" + flag);
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
	 * liujialin 插入两张表domainSet和domainState表 开关设置直接下发给obd
	 * 域白名单设置和黑名单设置,先查询是否在domainState表中已存在--不管是否存在都下发 如果存在则不再下发设置，不存在则下发设置
	 * 
	 * 禁止MAC地址上网，必须设备在线 域名限制： 黑名单：20个，每个最长50个字符（ASCII码）；
	 * 白名单：20个，每个最长50个字符（ASCII码）； 名单个数限制，是否需要调用接口查询obd 名单限制不能重复
	 * 
	 * 开关状态需要直接查询obd，如果obd不在线，则直接返回数据库记录，其他
	 * 如果没有则返回空
	 * 
	 */
	private JSONObject domainQuery(JSONObject jso) throws Exception {

		String deviceId = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		obdApiLogger.info("----------【域黑白名单查询】----------" + deviceId);

		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		// 输出参数
		JSONObject resultJso = new JSONObject();
		try {

			// 1.查询是否存在该设备号
			OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
			if (obdStockInfo == null) {
				obdApiLogger.info("----------【域黑白名单查询】----------" + deviceId + "---该设备未注册,请联系管理员.");
				desc = "设备不存在,请联系管理员.";
				return returnJson(-1, -1, desc);
					
			}
			
			String obdSn = obdStockInfo.getObdSn();
			DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询域名单状态记录
			if (domainState != null) {
				String whiteSwitch = domainState.getWhiteSwitch();
				resultJso.put("whiteSwitch", whiteSwitch);
				String blackSwitch = domainState.getBlackSwitch();
				resultJso.put("blackSwitch", blackSwitch);
				String whiteList = domainState.getWhiteList();
				if (!StringUtils.isEmpty(whiteList)) {
					JSONObject jobj = JSONObject.fromObject(whiteList);
					StringBuffer sb = new StringBuffer();
					Set<String> ks = (Set<String>)jobj.keySet();
					if(ks.size()>0){
						for (String string : ks) {
							sb.append(string+";");
						}
						String wl = StrUtil.stringCutLastSub(sb.toString(), ";");
						resultJso.put("whiteList", wl);
					}
				}
				
				String blackList = domainState.getBlackList();
				if (!StringUtils.isEmpty(blackList)) {
					JSONObject jobj = JSONObject.fromObject(blackList);
					StringBuffer sb = new StringBuffer();
					Set<String> ks = (Set<String>)jobj.keySet();
					if(ks.size()>0){
						for (String string : ks) {
							sb.append(string+";");
						}
						String bl = StrUtil.stringCutLastSub(sb.toString(), ";");
						resultJso.put("blackList", bl);
					}
				}
			}
			//查询离线设置
			List<ObdSetting> obdSettings=obdSettingService.queryByObdSnAndType(obdSn, "domain_%");
			if(obdSettings!=null && obdSettings.size()>0){
				String whiteSwitchOffline = "";
				String blackSwitchOffline = "";
				String whilteListOffline = "";
				String blackListOffline = "";
				
				String whiteDelOffline = "";
				String blackDelOffline = "";
				String whiteDelAllOffline = "";
				String blackDelAllOffline = "";
				for (ObdSetting obdSetting : obdSettings) {
					String settype =obdSetting.getType();
					SettingType emu = SettingType.getSettingTypeByValue(settype);
					String settingMsg = obdSetting.getSettingMsg();
					JSONObject json = JSONObject.fromObject(settingMsg);
					switch (emu) {
					case DOMAIN_00:
						whiteSwitchOffline =json.optString("whiteSwitch");
						break;
					case DOMAIN_01:
						blackSwitchOffline =json.optString("blackSwitch");
						break;
					case DOMAIN_02:
						obdApiLogger.info("----------【域黑白名单查询】----------" + deviceId+"---离线设置存在有效的禁止MAC上网---"+obdSetting.toString());
						break;
					case DOMAIN_03:
						String dn3 = json.optString("domainName");
						whilteListOffline+=dn3+";";
						break;
					case DOMAIN_04:
						String dn4 = json.optString("domainName");
						whiteDelOffline+=dn4+";";
						break;
					case DOMAIN_05:
						whiteDelAllOffline+="1;";
						break;
					case DOMAIN_06:
						String dn6 = json.optString("domainName");
						blackListOffline+=dn6+";";
						break;
					case DOMAIN_07:
						String dn7 = json.optString("domainName");
						blackDelOffline+=dn7+";";
						break;
					case DOMAIN_08:
						blackDelAllOffline+="1;";
						break;
					default:
						break;
					}
				}
				
				if(!StringUtils.isEmpty(whiteSwitchOffline)){
					resultJso.put("whiteSwitchOffline", whiteSwitchOffline);
				}
				if(!StringUtils.isEmpty(blackSwitchOffline)){
					resultJso.put("blackSwitchOffline", blackSwitchOffline);
				}
				if(!StringUtils.isEmpty(whilteListOffline)){
					whilteListOffline = StrUtil.stringCutLastSub(whilteListOffline, ";");
					resultJso.put("whilteListOffline", whilteListOffline);
				}
				if(!StringUtils.isEmpty(blackListOffline)){
					blackListOffline = StrUtil.stringCutLastSub(blackListOffline, ";");
					resultJso.put("blackListOffline", blackListOffline);
				}
				if(!StringUtils.isEmpty(whiteDelOffline)){
					whiteDelOffline = StrUtil.stringCutLastSub(whiteDelOffline, ";");
					resultJso.put("whiteDelOffline", whiteDelOffline);
				}
				if(!StringUtils.isEmpty(blackDelOffline)){
					blackDelOffline = StrUtil.stringCutLastSub(blackDelOffline, ";");
					resultJso.put("blackDelOffline", blackDelOffline);
				}
				if(!StringUtils.isEmpty(whiteDelAllOffline)){
					resultJso.put("whiteDelAllOffline", whiteDelAllOffline);
				}
				if(!StringUtils.isEmpty(blackDelAllOffline)){
					resultJso.put("blackDelAllOffline", blackDelAllOffline);
				}
			}
		} catch (Exception e) {
			code = -1;
			state =-1;
			desc ="请联系管理员,系统异常,信息:"+e;
		}

		JSONObject retJso = createReturnJson(code, desc);

		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		obdApiLogger.info(deviceId + "-----------域黑白名单查询结果:" + retJso);
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
