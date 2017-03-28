package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.ExtensionDataSetType;
import com.hgsoft.obd.util.RequestForwardUtil;
import com.hgsoft.obd.util.SettingType;

/**
 * 车主通平台与客户端接口:域黑白名单设置
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class DomainAction extends BaseAction {

	private static Logger obdApiLogger = LogManager.getLogger("obdApiLogger");

	final int domainTotal = new Integer(PropertiesUtil.getInstance("params.properties").readProperty("domainTotal"));// 域名总个数20
	final int domainUrlLength = new Integer(
			PropertiesUtil.getInstance("params.properties").readProperty("domainUrlLength"));// 每个域名的最大长度50
	final int domainEveryOneTotal = new Integer(
			PropertiesUtil.getInstance("params.properties").readProperty("domainEveryOneTotal"));// 每次设置域名总个数5
	final int domainDel = new Integer(PropertiesUtil.getInstance("params.properties").readProperty("domainDel"));// 每次只能删除1个
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
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private RequestForwardUtil requestForwardUtil;

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
			oar.setMethod("domain");

			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);

			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.domain(recJson);
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
			obdApiLogger.error("----------【电信接口】域名单异常信息：" + e);
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
	 * 要校验域名是否正确 如果是type为3、6,则根据domainName的个数N，拆分成N条obdSetting记录
	 */
	private JSONObject domain(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【域黑白名单设置】----------" + deviceId);
		if (!StringUtils.isEmpty(type)) {
			jso.put("type", type);
		}
		if (!StringUtils.isEmpty(whiteSwitch)) {
			jso.put("whiteSwitch", whiteSwitch);
		}
		if (!StringUtils.isEmpty(blackSwitch)) {
			jso.put("blackSwitch", blackSwitch);
		}
		if (!StringUtils.isEmpty(mac)) {
			jso.put("mac", mac);
		}
		if (!StringUtils.isEmpty(domainName)) {
			jso.put("domainName", domainName);
		}
		/*-----负载环境跳转请求------*/
		String path = request.getRequestURI();
		String forwardDomain = requestForwardUtil.forward(path, jso.toString());
		if (!"0".equals(forwardDomain)) {
			return JSONObject.fromObject(forwardDomain);
		}

		String deviceId = jso.optString("deviceId");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		String type = jso.optString("type");// 操作类别
		String whiteSwitch = jso.optString("whiteSwitch");//
		String blackSwitch = jso.optString("blackSwitch");//
		String mac = jso.optString("mac");//
		String domainName = jso.optString("domainName");
	
		obdApiLogger.info("----------【域黑白名单设置】---------设备号:" + deviceId + "---操作类型:" + type + "---whiteSwitch:"
				+ whiteSwitch + "---blackSwitch:" + blackSwitch + "---mac:" + mac + "---域名:" + domainName);

		String desc = "";
		// 1.查询是否存在该设备号
		OBDStockInfo obdStockInfo = obdStockInfoService.queryByObdMSN(deviceId);
		if (obdStockInfo == null) {
			obdApiLogger.info("----------【域黑白名单设置】----------" + deviceId + "---该设备未注册,请联系管理员.");
			desc = "----------【域黑白名单设置】----------" + deviceId + "---该设备未注册,请联系管理员.";
			return returnJson(-1, -1, desc);
		}
		
		if(StringUtils.isEmpty(type)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc);
		}
		
		JSONObject retJso = null;
		switch (type) {
		case "0":
			retJso = type0(obdStockInfo, whiteSwitch);
			break;
		case "1":
			retJso = type1(obdStockInfo, blackSwitch);		
			break;
		case "2":
			retJso = type2(obdStockInfo, mac);
			break;
		case "3":
			retJso = type3(obdStockInfo, domainName);
			break;
		case "4":
			retJso = type4(obdStockInfo, domainName);
			break;
		case "5":
			retJso = type5(obdStockInfo);
			break;
		case "6":
			retJso = type6(obdStockInfo, domainName);
			break;
		case "7":
			retJso = type7(obdStockInfo, domainName);
			break;
		case "8":
			retJso = type8(obdStockInfo);
			break;
		default:
			desc = "请求参数有误,type类型不存在.";
			retJso = returnJson(-1, -1, desc);
			break;
		}
			
		return retJso;
	}
	
	/**
	 * 白名单开关
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 设备离线,离线设置
	 * @param obdStockInfo
	 * @param whiteSwitch
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type0(OBDStockInfo obdStockInfo,String whiteSwitch) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(whiteSwitch)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		String settype = SettingType.DOMAIN_00.getValue();
		JSONObject jb = new JSONObject();// 保存内容
		jb.put("whiteSwitch", whiteSwitch);
		String msg = jb.toString();
		ObdSetting obdSetting = settingNew(obdSn, settype, msg);
		
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainWhiteSwitch, whiteSwitch);
			obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//将之前的记录置为无效
				int total = obdSettingService.obdSettingNoValid(obdSn, settype);
				obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---将离线设置的域白开关记录置为无效总数:" + total);
				//保存obdSetting记录
				obdSetting.setValid("0");//设置成功
				boolean otFlag=obdSettingService.obdSettingSave(obdSetting);
				obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---设置记录保存结果:" + otFlag);
				//更新domainState表
				DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
				domainState=dsNull(obdSn, domainState);
				
				boolean dsflag = domainStateSave(domainState, whiteSwitch, null, null, null, null);
				obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---域名单状态记录更新结果:" + dsflag);
				desc = "设置成功.";
			}else{
				desc = "开关在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---在线设置失败.");
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---设备离线,将进行离线设置." );
			//将之前的记录置为无效
			int total = obdSettingService.obdSettingNoValid(obdSn, settype);
			obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---将离线设置的域白开关记录置为无效总数:" + total);
			obdSetting.setValid("1");//设置成功
			boolean otFlag=obdSettingService.obdSettingSave(obdSetting);
			obdApiLogger.info("----------【域黑白名单设置】域白名单功能开关----------" + deviceId + "---设置记录保存结果:" + otFlag);
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state =1;
		}
		return returnJson(code, state, desc);
	}

	
	/**
	 * 黑名单开关
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 设备离线,离线设置
	 * 不需要查询obd
	 * @param obdStockInfo
	 * @param whiteSwitch
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type1(OBDStockInfo obdStockInfo,String blackSwitch) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(blackSwitch)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		String settype = SettingType.DOMAIN_01.getValue();
		JSONObject jb = new JSONObject();// 保存内容
		jb.put("blackSwitch", blackSwitch);
		ObdSetting obdSetting = settingNew(obdSn, settype, jb.toString());
		
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainBlackSwitch, blackSwitch);
			obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//将之前的记录置为无效
				int total = obdSettingService.obdSettingNoValid(obdSn, settype);
				obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---将离线设置的域白开关记录置为无效总数:" + total);
				//保存obdSetting记录
				obdSetting.setValid("0");//设置成功
				boolean otFlag=obdSettingService.obdSettingSave(obdSetting);
				obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---设置记录保存结果:" + otFlag);
				//更新domainState表
				DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
				domainState=dsNull(obdSn, domainState);
				boolean dsflag = domainStateSave(domainState, null, blackSwitch, null, null, null);
				obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---域名单状态记录更新结果:" + dsflag);
				desc = "设置成功.";
			}else{
				desc = "开关在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---在线设置失败.");
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---设备离线,将进行离线设置." );
			//将之前的记录置为无效
			int total = obdSettingService.obdSettingNoValid(obdSn, settype);
			obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---将离线设置的域白开关记录置为无效总数:" + total);
			obdSetting.setValid("1");
			boolean otFlag=obdSettingService.obdSettingSave(obdSetting);
			obdApiLogger.info("----------【域黑白名单设置】域黑名单功能开关----------" + deviceId + "---设置记录保存结果:" + otFlag);
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state =1;
		}
		return returnJson(code, state, desc);
	}

	
	/**
	 * 禁止MAC上网;
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 不需要查询obd
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type2(OBDStockInfo obdStockInfo,String mac) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】禁止MAC上网----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(mac)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		JSONObject jb = new JSONObject();// 保存内容
		jb.put("mac", mac);
		ObdSetting obdSetting = settingNew(obdSn, SettingType.DOMAIN_02.getValue(), jb.toString());
		
		
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainForbidMAC, mac);
			obdApiLogger.info("----------【域黑白名单设置】禁止MAC上网----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//保存obdSetting记录
				obdSetting.setValid("0");//设置成功
				boolean otFlag=obdSettingService.obdSettingSave(obdSetting);
				obdApiLogger.info("----------【域黑白名单设置】禁止MAC上网----------" + deviceId + "---设置记录保存结果:" + otFlag);
				desc = "设置成功.";
			}else{
				desc = "禁止MAC上网在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】禁止MAC上网----------" + deviceId + "---禁止MAC上网在线设置失败.");
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】禁止MAC上网----------" + deviceId + "---设备离线,将进行离线设置." );
			//直接返回
			desc ="设备离线,禁止MAC上网必须保存设备在线.";
			state =-1;
		}
		return returnJson(code, state, desc);
	}

	/**
	 * 3增加多个域白名单;
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 失败的话需要查询obd
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type3(OBDStockInfo obdStockInfo,String domainName) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(domainName)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		String[] domainNameArr = domainName.split(";");
		
		// 传过来的数组是否存在重复
		if (!StrUtil.checkRepeat(domainNameArr)) {
			desc = "请求参数有误,域名参数存在重复,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}

		if (domainNameArr.length > domainEveryOneTotal) {
			desc = "请求参数有误,域名参数每次最多只支持5个,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}

		for (String string : domainNameArr) {
			if (string.length() > domainUrlLength) {
				desc = "请求参数有误,域名参数长度不能超过50,请联系管理员.";
				return returnJson(-1, -1, desc); 
			}
		}
		
		Set<String> domainNameSet = new HashSet<String>(Arrays.asList(domainNameArr));//将域名设置为集合,然后判断是否存在重复
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
		List<ObdSetting> ufwsList = null;// 已存在未完成的白名单列表
		List<ObdSetting> ufbsList = null;// 已存在未完成的黑名单列表
		Set<String> whiteListY = new HashSet<>();//成功设置的白名单
		Set<String> blackListY = new HashSet<>();//成功设置的黑名单
		Set<String> whiteListN = new HashSet<>();//离线设置的白名单
		Set<String> blackListN = new HashSet<>();//离线设置的黑名单
		
		//已设置成功的域白名单是否存在重复
		if (domainState != null && !StringUtils.isEmpty(domainState.getWhiteList())) {
			JSONObject wlJson = JSONObject.fromObject(domainState.getWhiteList());
			Set<String> keys = (Set<String>) wlJson.keySet();
			if(keys.size()>0){
				whiteListY.addAll(keys);//赋值给主
				Set<String> whiteTem = new HashSet<>();
				whiteTem.addAll(keys);//赋值给临时
				whiteTem.retainAll(domainNameSet);
				if(whiteTem.size()>0){
					desc = "已设置的白名单中存在重复记录.";
					obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId+"---已设置的白名单中存在重复记录---"+keys.toString());
					return returnJson(0, -3, desc); 
				}
			}
		}
		//已设置成功的域黑名单是否存在重复
		if (domainState != null && !StringUtils.isEmpty(domainState.getBlackList())) {
			JSONObject blJson = JSONObject.fromObject(domainState.getBlackList());
			Set<String> keys = (Set<String>) blJson.keySet();
			if(keys.size()>0){
				blackListY.addAll(keys);
				Set<String> blackTem = new HashSet<>();
				blackTem.addAll(keys);
				blackTem.retainAll(domainNameSet);
				if(blackTem.size()>0){
					desc = "已设置的黑名单中存在重复记录.";
					obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId+"---已设置的黑名单中存在重复记录---"+keys.toString());
					return returnJson(0, -3, desc); 
				}
			}
		}

		//离线白名单是否存在重复
		ufwsList = obdSettingService.queryByObdSnAndType(obdSn, "domain_03");
		if (ufwsList != null && ufwsList.size() > 0) {
			Set<String> whiteTem = new HashSet<>();
			for (ObdSetting ot : ufwsList) {
				JSONObject json1 = JSONObject.fromObject(ot.getSettingMsg());
				whiteListN.add(json1.getString("domainName"));
				whiteTem.add(json1.getString("domainName"));
			}
			whiteTem.retainAll(domainNameSet);
			if(whiteTem.size()>0){
				desc = "离线设置的白名单中存在重复记录.";
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId+"---离线设置的白名单中存在重复记录---"+whiteTem.toString());
				return returnJson(0, -3, desc); 
			}
		}
		
		//离线黑名单是否存在重复
		ufbsList = obdSettingService.queryByObdSnAndType(obdSn, "domain_06");
		if (ufbsList != null && ufbsList.size() > 0) {
			Set<String> blackTem = new HashSet<>();
			for (ObdSetting ot : ufbsList) {
				JSONObject json1 = JSONObject.fromObject(ot.getSettingMsg());
				blackListN.add(json1.getString("domainName"));
				blackTem.add(json1.getString("domainName"));
			}
			blackTem.retainAll(domainNameSet);
			if(blackTem.size()>0){
				desc = "离线设置的黑名单中存在重复记录.";
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId+"---离线设置的黑名单中存在重复记录---"+blackTem.toString());
				return returnJson(0, -3, desc); 
			}
		}
		
		//判断总数是否超过20
		Integer total = whiteListY.size() + whiteListN.size() + domainNameSet.size();
		if(total > domainTotal){
			obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId+"---域白名单和离线白名单和待加上现在设置的总数超过20---"+total);
			desc = "已设置成功和离线设置和现在设置的白名单总数超过20.";
			return returnJson(0, -4, desc); 
		}
		
		
		String settype = SettingType.DOMAIN_03.getValue();//设置类别
		
		List<ObdSetting> setList = new ArrayList<>();// 拆分保存
		for (String string : domainNameArr) {
			JSONObject job = new JSONObject();// 保存内容
			job.put("domainName", string);
			ObdSetting os = settingNew(obdSn, settype, job.toString());
			
			setList.add(os);
		}
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainAddWhite, domainName);
			obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				for (ObdSetting os : setList) {
					os.setValid("0");
					boolean otFlag=obdSettingService.obdSettingSave(os);
					obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---设置记录保存结果:" + otFlag);
				}
				//更新domainSstate表
				domainState = dsNull(obdSn, domainState);
				JSONObject jobj = null;
				String whiteList = domainState.getWhiteList();
				if (!StringUtils.isEmpty(whiteList)) {
					jobj = JSONObject.fromObject(whiteList);
				} else {
					jobj = new JSONObject();
				}

				for (String dname : domainNameArr) {
					jobj.put(dname, dname);
				}
				String white = jobj.toString();
				boolean dsflag = domainStateSave(domainState, null, null, white, null, null);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---域名单设置修改结果:" + dsflag);
				
				desc = "设置成功.";
			}else{
				desc = "增加多个域白名单在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---在线设置失败.");
				//将之前的白名单同步离线记录置为无效
				int whiteSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_00.getValue());
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+whiteSyntotal);
				//在线设置失败,需要同步
				ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_00.getValue(), null);
				ost.setValid("1");
				boolean flag = obdSettingService.obdSettingSave(ost);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---在线设置失败,保存白名单同步的离线记录结果:"+flag);
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---设备离线,将进行离线设置." );
			for (ObdSetting os : setList) {
				os.setValid("1");
				boolean otFlag=obdSettingService.obdSettingSave(os);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域白名单----------" + deviceId + "---设置记录保存结果:" + otFlag);
			}
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state = 1;
		}
		return returnJson(code, state, desc);
	}

	/**
	 * 删除单个域白名单;
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 在线删除失败的话，需要同步状态
	 * 
	 * 先查看离线新增白名单是否存在该域名，如果存在，则删除该离线新增白名单记录
	 * 查看是否存在清空指令，如果存在，返回失败
	 * 查询离线删除白名单，是否存在该域名删除离线记录，如果存在返回失败
	 * 
	 * 如果新增白名单，然后清空白名单，是否可以将离线新增记录置为无效
	 * 
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type4(OBDStockInfo obdStockInfo,String domainName) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(domainName)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}

		if (domainName.length() > domainUrlLength) {
			desc = "请求参数有误,域名参数长度不能超过50,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
//		List<ObdSetting> ufwsList = null;// 已存在未完成的白名单列表
//		Set<String> whiteListY = new HashSet<>();//成功设置的白名单
//		Set<String> whiteListN = new HashSet<>();//离线设置的白名单
		
		//先查询离线白名单中是否存在，如果存在置为无效
		List<ObdSetting> ufwsList = obdSettingService.queryByObdSnAndType(obdSn, SettingType.DOMAIN_03.getValue());
		if(ufwsList!=null && ufwsList.size()>0){
			boolean flag = false;
			for (ObdSetting obdSetting : ufwsList) {
				String msg = obdSetting.getSettingMsg();
				String domainUrl = JSONObject.fromObject(msg).optString("domainName");
				if(domainName.equals(domainUrl)){
					obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---离线域白名单存在该域名,置为无效---"+obdSetting);
					flag = true;
					obdSetting.setValid("0");
					obdSetting.setUpdateTime(new Date());
					boolean ff=obdSettingService.obdSettingSave(obdSetting);
					obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---离线域白名单存在该域名,置为无效结果:"+ff);
					break;
				}
			}
			if(flag){
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---离线域白名单存在该域名.");
				desc = "离线域白名单存在该域名,已将离线记录置为无效.";
				return returnJson(0, 0, desc); 
			}
		}
		
		//先查询是否已经存在删除全部的命令，如果是不给删除
		ObdSetting whiteDelAll = obdSettingService.queryLastObdSetting(obdSn, SettingType.DOMAIN_05.getValue());
		if(whiteDelAll!=null){
			obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---存在清空白名单离线记录.");
			desc = "存在清空白名单指令,不允许再次删除.";
			return returnJson(0, -6, desc);
		}
		
		//查询是否存在相同的删除指令，如果是返回失败
		List<ObdSetting> ufwhiteDelList = obdSettingService.queryByObdSnAndType(obdSn,SettingType.DOMAIN_04.getValue());
		if(ufwhiteDelList!=null && ufwhiteDelList.size()>0){
			for (ObdSetting obdSetting : ufwhiteDelList) {
				String msg = obdSetting.getSettingMsg();
				String domainUrl = JSONObject.fromObject(msg).optString("domainName");
				if(domainName.equals(domainUrl)){
					obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---该域名已存在离线删除记录---"+obdSetting);
					desc = "已存在相同域名删除记录";
					return returnJson(0, -5, desc);
				}
			}
		}
		
		//已设置成功的域白名单是否存在重复
		if (domainState != null && !StringUtils.isEmpty(domainState.getWhiteList())) {
			JSONObject wlJson = JSONObject.fromObject(domainState.getWhiteList());
			//如果不存在直接返回成功
			if(!wlJson.containsKey(domainName)){
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---已设置的白名单中不存在该域名---"+wlJson.toString());
				desc = "已设置的白名单中不存在该域名.";
				return returnJson(0, -2, desc); 
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId+"---已设置的白名单中不存在该域名.域名单记录为空或白名单---");
			desc = "已设置的白名单中不存在该域名.";
			return returnJson(0, -2, desc);
		}
		
		String settype = SettingType.DOMAIN_04.getValue();//设置类别
		JSONObject jb = new JSONObject();// 保存内容
		jb.put("domainName", domainName);
		ObdSetting os = settingNew(obdSn, settype, jb.toString());
			
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, domainName);
			obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//1插入setting表
				os.setValid("0");
				boolean otFlag=obdSettingService.obdSettingSave(os);
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---设置记录保存结果:" + otFlag);
				
				//2更新domainSstate表
				JSONObject jobj = null;
				String wList = domainState.getWhiteList();
				jobj = JSONObject.fromObject(wList);
				jobj.remove(domainName);
				String white = jobj.toString();
				boolean dsflag = domainStateSave(domainState, null, null, white, null, null);
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---域名单设置修改结果:" + dsflag);
				
				desc = "设置成功.";
			}else{
				desc = "删除单个域白名单;在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---在线设置失败.");
				int whiteSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_00.getValue());
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---"+deviceId+"---将之前相关类型设置置为无效总数:"+whiteSyntotal);
				ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_00.getValue(), null);
				ost.setValid("1");
				boolean flag = obdSettingService.obdSettingSave(ost);
				obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---在线设置失败,保存白名单同步的离线记录结果:"+flag);
			
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---设备离线,将进行离线设置." );
			os.setValid("1");
			boolean otFlag=obdSettingService.obdSettingSave(os);
			obdApiLogger.info("----------【域黑白名单设置】删除单个域白名单---" + deviceId + "---设置记录保存结果:" + otFlag);
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state = 1;
		}
		return returnJson(code, state, desc);
	}

	
	/**
	 * 删除所有域白名单
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 失败的话需要查询obd
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type5(OBDStockInfo obdStockInfo) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单;----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
		
		String settype = SettingType.DOMAIN_05.getValue();
		ObdSetting os = settingNew(obdSn, settype, new JSONObject().toString());
			
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, "00");
			obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单;----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//将之前相关离线记录置为无效
				int total = settingUnuseful(obdSn, "domain_05","domain_04","domain_03");
				obdApiLogger.info("----------【域黑白名单设置】删除所有白名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+ total);
				//1插入setting表
				os.setValid("0");
				boolean otFlag=obdSettingService.obdSettingSave(os);
				obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
				domainState = dsNull(obdSn, domainState);
				//2更新domainSstate表
				String white = new JSONObject().toString();
				boolean dsflag = domainStateSave(domainState, null, null, white, null, null);
				obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单----------" + deviceId + "---域名单设置修改结果:" + dsflag);
				desc = "设置成功.";
			}else{
				desc = "删除所有域白名单;在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单;----------" + deviceId + "---删除所有域白名单;在线设置失败.");
				//调用线程查询
			}
		}else{
			//将之前相关离线记录置为无效
			int total = settingUnuseful(obdSn, "domain_05","domain_04","domain_03");
			obdApiLogger.info("----------【域黑白名单设置】删除所有白名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+total);
			obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单;----------" + deviceId + "---设备离线,将进行离线设置." );
			os.setValid("1");
			boolean otFlag=obdSettingService.obdSettingSave(os);
			obdApiLogger.info("----------【域黑白名单设置】删除所有域白名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state = 1;
		}
		return returnJson(code, state, desc);
	}
	
	/**
	 * 3增加多个域域名单;
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 失败的话需要查询obd
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type6(OBDStockInfo obdStockInfo,String domainName) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(domainName)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		String[] domainNameArr = domainName.split(";");
		
		// 传过来的数组是否存在重复
		if (!StrUtil.checkRepeat(domainNameArr)) {
			desc = "请求参数有误,域名参数存在重复,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}

		if (domainNameArr.length > domainEveryOneTotal) {
			desc = "请求参数有误,域名参数每次最多只支持5个,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}

		for (String string : domainNameArr) {
			if (string.length() > domainUrlLength) {
				desc = "请求参数有误,域名参数长度不能超过50,请联系管理员.";
				return returnJson(-1, -1, desc); 
			}
		}
		
		Set<String> domainNameSet = new HashSet<String>(Arrays.asList(domainNameArr));//将域名设置为集合,然后判断是否存在重复
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
		List<ObdSetting> ufwsList = null;// 已存在未完成的白名单列表
		List<ObdSetting> ufbsList = null;// 已存在未完成的黑名单列表
		Set<String> whiteListY = new HashSet<>();//成功设置的白名单
		Set<String> blackListY = new HashSet<>();//成功设置的黑名单
		Set<String> whiteListN = new HashSet<>();//离线设置的白名单
		Set<String> blackListN = new HashSet<>();//离线设置的黑名单
		
		
		//已设置成功的域黑名单是否存在重复
		if (domainState != null && !StringUtils.isEmpty(domainState.getBlackList())) {
			JSONObject blJson = JSONObject.fromObject(domainState.getBlackList());
			Set<String> keys = (Set<String>) blJson.keySet();
			if(keys.size()>0){
				blackListY.addAll(keys);
				Set<String> blackTem = new HashSet<>();
				blackTem.addAll(keys);
				blackTem.retainAll(domainNameSet);
				if(blackTem.size()>0){
					desc = "已设置的黑名单中存在重复记录.";
					obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId+"---已设置的黑名单中存在重复记录---"+keys.toString());
					return returnJson(0, -3, desc); 
				}
			}
		}
		
		
		//已设置成功的域白名单是否存在重复
		if (domainState != null && !StringUtils.isEmpty(domainState.getWhiteList())) {
			JSONObject wlJson = JSONObject.fromObject(domainState.getWhiteList());
			Set<String> keys = (Set<String>) wlJson.keySet();
			if(keys.size()>0){
				whiteListY.addAll(keys);
				Set<String> whiteTem = new HashSet<>();
				whiteTem.addAll(keys);
				whiteTem.retainAll(domainNameSet);
				if(whiteTem.size()>0){
					desc = "已设置的白名单中存在重复记录.";
					obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId+"---已设置的白名单中存在重复记录---"+keys.toString());
					return returnJson(0, -3, desc); 
				}
			}
		}
		
		//离线黑名单是否存在重复
		ufbsList = obdSettingService.queryByObdSnAndType(obdSn, "domain_06");
		if (ufbsList != null && ufbsList.size() > 0) {
			Set<String> blackTem = new HashSet<>();
			for (ObdSetting ot : ufbsList) {
				JSONObject json1 = JSONObject.fromObject(ot.getSettingMsg());
				blackListN.add(json1.getString("domainName"));
				blackTem.add(json1.getString("domainName"));
			}
			blackTem.retainAll(domainNameSet);
			if(blackTem.size()>0){
				desc = "离线设置的黑名单中存在重复记录.";
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId+"---离线设置的黑名单中存在重复记录---"+blackTem.toString());
				return returnJson(0, -3, desc); 
			}
		}
		
		//离线白名单是否存在重复
		ufwsList = obdSettingService.queryByObdSnAndType(obdSn, "domain_03");
		if (ufwsList != null && ufwsList.size() > 0) {
			Set<String> whiteTem = new HashSet<>();
			for (ObdSetting ot : ufwsList) {
				JSONObject json1 = JSONObject.fromObject(ot.getSettingMsg());
				whiteListN.add(json1.getString("domainName"));
				whiteTem.add(json1.getString("domainName"));
			}
			whiteTem.retainAll(domainNameSet);
			if(whiteTem.size()>0){
				desc = "离线设置的白名单中存在重复记录.";
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId+"---离线设置的白名单中存在重复记录---"+whiteTem.toString());
				return returnJson(0, -3, desc); 
			}
		}
		
		//判断总数是否超过20
		Integer total = blackListY.size() + blackListN.size() + domainNameSet.size();
		if(total > domainTotal){
			obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单----------" + deviceId+"---域黑名单和离线黑名单和待加上现在设置的总数超过20---"+total);
			desc = "已设置成功和离线设置和现在设置的黑名单总数超过20.";
			return returnJson(0, -4, desc); 
		}
		
		String settype = SettingType.DOMAIN_06.getValue();//设置类别
		
		List<ObdSetting> setList = new ArrayList<>();// 拆分保存
		for (String string : domainNameArr) {
			JSONObject jb = new JSONObject();// 保存内容
			jb.put("domainName", string);
			ObdSetting os = settingNew(obdSn, settype, jb.toString());
			setList.add(os);
		}
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainAddBlack, domainName);
			obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				for (ObdSetting os : setList) {
					os.setValid("0");
					boolean otFlag=obdSettingService.obdSettingSave(os);
					obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
				}
				//更新domainSstate表
				domainState = dsNull(obdSn, domainState);
				JSONObject jobj = null;
				String blackList = domainState.getBlackList();
				if (!StringUtils.isEmpty(blackList)) {
					jobj = JSONObject.fromObject(blackList);
				} else {
					jobj = new JSONObject();
				}

				for (String dname : domainNameArr) {
					jobj.put(dname, dname);
				}
				
				String black = jobj.toString();
				boolean dsflag = domainStateSave(domainState, null, null, null, black, null);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单----------" + deviceId + "---域名单设置修改结果:" + dsflag);
				
				desc = "设置成功.";
			}else{
				desc = "增加多个域黑名单;在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId + "---在线设置失败.");
				int blackSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_01.getValue());
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+blackSyntotal);
				ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_01.getValue(), null);
				ost.setValid("1");
				boolean flag = obdSettingService.obdSettingSave(ost);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单----------" + deviceId + "---在线设置失败,保存黑名单同步的离线记录结果:"+flag);
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId + "---设备离线,将进行离线设置." );
			for (ObdSetting os : setList) {
				os.setValid("1");
				boolean otFlag=obdSettingService.obdSettingSave(os);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
			}
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state = 1;
		}
		return returnJson(code, state, desc);
	}

	/**
	 * 删除单个域黑名单;
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 失败的话需要查询obd
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type7(OBDStockInfo obdStockInfo,String domainName) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单---" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		if(StringUtils.isEmpty(domainName)){
			desc = "请求参数有误,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}

		if (domainName.length() > domainUrlLength) {
			desc = "请求参数有误,域名参数长度不能超过50,请联系管理员.";
			return returnJson(-1, -1, desc); 
		}
		
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
		List<ObdSetting> ufbsList = null;// 已存在未完成的黑名单列表
//		Set<String> blackListY = new HashSet<>();//成功设置的黑名单
//		Set<String> blackListN = new HashSet<>();//离线设置的黑名单
		
		//先查询离线黑名单中是否存在，如果存在置为无效
		ufbsList = obdSettingService.queryByObdSnAndType(obdSn, SettingType.DOMAIN_06.getValue());
		if(ufbsList!=null && ufbsList.size()>0){
			boolean flag = false;
			for (ObdSetting obdSetting : ufbsList) {
				String msg = obdSetting.getSettingMsg();
				String domainUrl = JSONObject.fromObject(msg).optString("domainName");
				if(domainName.equals(domainUrl)){
					obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单---" + deviceId+"---离线域黑名单存在该域名,置为无效---"+obdSetting);
					flag = true;
					obdSetting.setValid("0");
					obdSetting.setUpdateTime(new Date());
					boolean ff=obdSettingService.obdSettingSave(obdSetting);
					obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单---" + deviceId+"---离线域黑名单存在该域名,置为无效结果:"+ff);
					break;
				}
			}
			if(flag){
				obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单---" + deviceId+"---离线域黑名单存在该域名.");
				desc = "离线域黑名单存在该域名,已将离线记录置为无效.";
				return returnJson(0, 0, desc); 
			}
		}
		
		//先查询是否已经存在删除全部的命令，如果是不给删除
		ObdSetting blackDelAll = obdSettingService.queryLastObdSetting(obdSn, SettingType.DOMAIN_08.getValue());
		if(blackDelAll!=null){
			obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单---" + deviceId+"---存在清空白名单离线记录.");
			desc = "存在清空黑名单指令,不允许再次删除.";
			return returnJson(0, -6, desc);
		}
		
		//查询是否存在相同的删除指令，如果是返回失败
		List<ObdSetting> ufblackDelList = obdSettingService.queryByObdSnAndType(obdSn,SettingType.DOMAIN_07.getValue());
		if(ufblackDelList!=null && ufblackDelList.size()>0){
			for (ObdSetting obdSetting : ufblackDelList) {
				String msg = obdSetting.getSettingMsg();
				String domainUrl = JSONObject.fromObject(msg).optString("domainName");
				if(domainName.equals(domainUrl)){
					obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单---" + deviceId+"---该域名已存在离线删除记录---"+obdSetting);
					desc = "已存在相同域名删除记录";
					return returnJson(0, -5, desc);
				}
			}
		}
		
		
		//已设置成功的域白名单是否存在重复
		if (domainState != null && !StringUtils.isEmpty(domainState.getBlackList())) {
			JSONObject blJson = JSONObject.fromObject(domainState.getBlackList());
			//如果不存在直接返回成功
			if(!blJson.containsKey(domainName)){
				obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId+"---已设置的白名单中不存在该域名---"+blJson.toString());
				desc = "已设置的黑名单中不存在该域名.";
				return returnJson(0, -2, desc); 
			}
		}else{
			obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId+"---已设置的黑名单中不存在该域名.域名单记录为空或白名单---");
			desc = "已设置的黑名单中不存在该域名.";
			return returnJson(0, -2, desc);
		}
		
		String settype = SettingType.DOMAIN_07.getValue();
		JSONObject jb = new JSONObject();// 保存内容
		jb.put("domainName", domainName);
		ObdSetting os = settingNew(obdSn, settype, jb.toString());
			
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, domainName);
			obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//1插入setting表
				os.setValid("0");
				boolean otFlag=obdSettingService.obdSettingSave(os);
				obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
				
				//2更新domainSstate表
				JSONObject jobj = null;
				String bList = domainState.getBlackList();
				jobj = JSONObject.fromObject(bList);
				jobj.remove(domainName);
				String black = jobj.toString();
				boolean dsflag = domainStateSave(domainState, null, null, null, black, null);
				obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单----------" + deviceId + "---域名单设置修改结果:" + dsflag+"---状态记录:"+domainState);
				
				desc = "设置成功.";
			}else{
				desc = "删除单个域黑名单;在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId + "---删除单个域黑名单;在线设置失败.");
				//调用线程查询
				int blackSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_01.getValue());
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+blackSyntotal);
				ObdSetting ost=settingNew(obdSn, SettingType.DOMAINSYN_01.getValue(), null);
				ost.setValid("1");
				boolean flag = obdSettingService.obdSettingSave(ost);
				obdApiLogger.info("----------【域黑白名单设置】增加多个域黑名单----------" + deviceId + "---在线设置失败,保存黑名单同步的离线记录结果:"+flag);
			}
		}else{
			//将之前的记录置为 
			obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId + "---设备离线,将进行离线设置." );
			os.setValid("1");
			boolean otFlag=obdSettingService.obdSettingSave(os);
			obdApiLogger.info("----------【域黑白名单设置】删除单个域黑名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state = 1;
		}
		return returnJson(code, state, desc);
	}

	/**
	 * 8删除所有域黑名单
	 * 如果设备在线,直接下发指令,成功则返回成功,失败则返回失败
	 * 失败的话需要查询obd
	 * @param obdStockInfo
	 * @param mac
	 * @return
	 * @throws OBDException 
	 */
	private JSONObject type8(OBDStockInfo obdStockInfo) throws OBDException{
		String deviceId = obdStockInfo.getObdMSn();
		obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单;----------" + deviceId);
		int code = 0;
		int state = 0;
		String desc = "";
		
		String obdSn = obdStockInfo.getObdSn();
		String obdState = obdStockInfo.getStockState();
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);// 查询设备最终状态
		
		String settype = SettingType.DOMAIN_08.getValue();
		ObdSetting os = settingNew(obdSn, settype, new JSONObject().toString());
		
		if("01".equals(obdState)){
			boolean obdSetRes = domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, "00");
			obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单;----------" + deviceId + "---下发报文设置结果:" + obdSetRes);
			if(obdSetRes){
				//将之前的白名单离线记录置为无效
				int total = settingUnuseful(obdSn, "domain_08","domain_07","domain_06");
				obdApiLogger.info("----------【域黑白名单设置】删除所有黑名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+total);
				//1插入setting表
				os.setValid("0");
				boolean otFlag=obdSettingService.obdSettingSave(os);
				obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
				
				//2更新domainSstate表
				domainState = dsNull(obdSn, domainState);
				String black = new JSONObject().toString();
				boolean dsflag = domainStateSave(domainState, null, null, null, black, null);
				obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单----------" + deviceId + "---域名单设置修改结果:" + dsflag);
				desc = "设置成功.";
			}else{
				desc = "删除所有域黑名单;在线设置失败,请稍后重试.";
				state = -1;
				obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单;----------" + deviceId + "---删除所有域黑名单;在线设置失败.");
				//调用线程查询
			}
		}else{
			//将之前相关离线记录置为无效
			int total = settingUnuseful(obdSn, "domain_08","domain_07","domain_06");
			obdApiLogger.info("----------【域黑白名单设置】删除所有黑名单----------"+deviceId+"---将之前相关类型设置置为无效总数:"+total);
			obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单;----------" + deviceId + "---设备离线,将进行离线设置." );
			os.setValid("1");
			boolean otFlag=obdSettingService.obdSettingSave(os);
			obdApiLogger.info("----------【域黑白名单设置】删除所有域黑名单;----------" + deviceId + "---设置记录保存结果:" + otFlag);
			//直接返回
			desc ="设备离线,将进行离线设置.";
			state = 1;
		}
		return returnJson(code, state, desc);
	}



	
	private JSONObject returnJson(Integer code, Integer state, String desc) {
		JSONObject retJso = createReturnJson(code, desc);
		JSONObject resultJso = new JSONObject();
		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		return retJso;
	}
	
	private DomainState dsNull(String obdSn,DomainState domainState){
		if(domainState!=null){
			return domainState;
		}else{
			JSONObject json=new JSONObject();
			domainState = new DomainState();
			domainState.setId(IDUtil.createID());
			domainState.setObdSn(obdSn);
			domainState.setWhiteList(json.toString());
			domainState.setBlackList(json.toString());
			domainState.setCreateTime(new Date());
			return domainState;
		}
	}
	
	
	private ObdSetting settingNew(String obdSn,String type,String msg){
		ObdSetting os = new ObdSetting();
		os.setId(IDUtil.createID());
		os.setObdSn(obdSn);
		os.setType(type);
		os.setCreateTime(new Date());
		os.setSettingMsg(msg);
		return os;
	}
	
	
	/**
	 * 通用的域名单设置
	 * @param obdSn
	 * @param type
	 * @param domainWhite
	 * @return
	 * @throws OBDException
	 */
	private boolean domainSet(String obdSn,ExtensionDataSetType type,String msg) throws OBDException{
		Map<ExtensionDataSetType, String> extensionDataSetTypes = new HashMap<ExtensionDataSetType, String>();
		extensionDataSetTypes.put(type, msg);
		String setResult = serverSettingService.extensionDataSetting(obdSn, extensionDataSetTypes);
		// 下发设置报文
		boolean obdSetRes = GlobalData.isSendResultSuccess(setResult);
		return obdSetRes;
	}
	
	
	private boolean domainStateSave(DomainState domainState,String whiteSwitch,String blackSwitch,String whiteList,String blackList,String createDate){
		if(domainState==null){
			return false;
		}
		if(!StringUtils.isEmpty(whiteSwitch)){
			domainState.setWhiteSwitch(whiteSwitch);
		}
		
		if(!StringUtils.isEmpty(blackSwitch)){
			domainState.setBlackSwitch(blackSwitch);
		}
		
		if(!StringUtils.isEmpty(whiteList)){
			domainState.setWhiteList(whiteList);
		}
		
		if(!StringUtils.isEmpty(blackList)){
			domainState.setBlackList(blackList);
		}
		
		if(!StringUtils.isEmpty(createDate)){
			domainState.setCreateTime(new Date());
		}
		
		domainState.setUpdateTime(new Date());
		boolean dsflag =domainStateService.domainStateSave(domainState);
		return dsflag;
	}
	
	private Integer settingUnuseful(String obdSn,String...type){
		Map<String, Integer> types = new HashMap<>();
		for (String string : type) {
			types.put(string, 1);
		}
		int total=obdSettingService.setNoValidByInType(obdSn, types);
		return total;
	}

	private String deviceId;
	private String username;
	private String time;
	private String sign;

	private String type;// 操作类别
	private String whiteSwitch;
	private String blackSwitch;

	private String mac;//
	private String domainName;//

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWhiteSwitch() {
		return whiteSwitch;
	}

	public void setWhiteSwitch(String whiteSwitch) {
		this.whiteSwitch = whiteSwitch;
	}

	public String getBlackSwitch() {
		return blackSwitch;
	}

	public void setBlackSwitch(String blackSwitch) {
		this.blackSwitch = blackSwitch;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}
