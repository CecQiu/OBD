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
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.carowner.service.WiFiService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.service.ProtalSendService;
import com.hgsoft.obd.service.ServerSettingService;
import com.hgsoft.obd.util.RequestForwardUtil;
import com.hgsoft.obd.util.SettingType;

/**
 * 车主通平台与客户端接口:portal接口
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class PortalHandleAction extends BaseAction {

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
	private ObdSettingService obdSettingService;	
	@Resource
	private ObdApiRecordService obdApiRecordService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private RequestForwardUtil requestForwardUtil;
	@Resource
	private ProtalSendService protalSendService;// protalService设置
	@Resource
	private WiFiService wiFiService;
	
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
			
			if(StringUtils.isEmpty(jsonStr)){
				this.outMessage(createReturnJson(-1, "请求失败.").toString());
				return;
			}
			JSONObject jo = JSONObject.fromObject(jsonStr);
			if(!jo.containsKey("deviceId")){
				this.outMessage(createReturnJson(-1, "请求失败.").toString());
				return;
			}
			dev = jo.optString("deviceId");
			if(StringUtils.isEmpty(dev) || "null".equals(dev)){
				this.outMessage(createReturnJson(-1, "请求失败.").toString());
				return;
			}
			oar.setObdMsn(dev);
			oar.setUrl(path);
			oar.setParam(jsonStr);
			oar.setMethod("portal");
			
			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);
				
			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				//做集群转发
				JSONObject portalJson = JSONObject.fromObject(jsonStr);
				setPortalParams(portalJson);//将portal参数进行设置
				String forwardWakeup = requestForwardUtil.forward(path, portalJson.toString());
				if(!"0".equals(forwardWakeup)){
					returnJson = JSONObject.fromObject(forwardWakeup);
				}else{
					JSONObject recJson = jso.getJSONObject("recJson");
					returnJson = this.portal(recJson);
				}
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
			obdApiLogger.error("----------【电信接口异常】portal接口:" + e);
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
		obdApiLogger.error("----------【电信接口操作记录保存结果】:portal接口:" + flag);
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
	 * 车辆型号接口 liujialin 
	 * b.根据type进行逻辑处理
	 * 
	 */
	private JSONObject portal(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【portal】---");
		if (!StringUtils.isEmpty(type)) {
			jso.put("type", type);
		}
	
		String type = jso.optString("type");// 操作类别
		String deviceId = jso.optString("deviceId");
		
		obdApiLogger.info("----------【portal】---"+deviceId +"---操作类型:" + type + "---请求参数:"+jso.toString());
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		
		JSONObject retJso = null;
		switch (type) {
		case "0":
			retJso =type0(jso);
			break;
		case "1":
			retJso= resultJson(-1, -1, "保留类型,不处理."); 
			break;
		case "2":
			retJso =type2(jso);
			break;
		case "3":
			retJso =type3(jso);
			break;
		case "4":
			retJso =type4(jso);
			break;
		case "5":
			retJso =type5(jso);
			break;
		case "6":
			retJso =type6(jso);
			break;
		default:
			obdApiLogger.info("----------【portal】---"+deviceId+"---请求类型参数有误---"+type);
			return resultJson(-1, -1, "请求类型参数有误."); 
		}
		
		return retJso;
	}
	
/**
 * url
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type0(JSONObject jso )throws Exception{
		obdApiLogger.info("----------【portal】设置URL---"+deviceId+"---请求参数:"+jso.toString());
		String deviceId = jso.optString("deviceId");
		if (!StringUtils.isEmpty(url)) {
			jso.put("url", url);
		}
		String url = jso.optString("url");// 设置URL
		String type = jso.optString("type");// 操作类别
		if(StringUtils.isEmpty(url)){
			obdApiLogger.info("----------【portal】设置URL---"+deviceId+"---请求参数有误;");
			return resultJson(-1, -1, "请求参数有误.");
		}
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】设置URL---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		String obdSn = obd.getObdSn();
		
		//判断设备在线状态
		String stockState =obd.getStockState();
		//如果设备离线,不给设置
		Portal portal =getNewPortal(obdSn, type, url, null, null, null, null,null);
		if(ObdConstants.ObdOffline.equals(stockState)){
			obdApiLogger.info("----------【portal】设置URL---"+deviceId+"---设备离线.");
			portal.setValid("1");
			boolean ff=protalSendService.portalSaveOrUpdate(portal);
			return resultJson(-1, -1, "设备离线,请保持设备在线."); 
		}
		
		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		
		
		String result ="";
		String bitsStr="1000000000000000";
		char[] bits=bitsStr.toCharArray();
		
		try {
			result=serverSettingService.portalOrWifiSet(obdSn, bits, url);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【portal】设置URL---"+deviceId+"---在线设置失败---"+e);
			state = -1;
			code = -1;
			desc="操作失败:"+e;
		}
		boolean flag = GlobalData.isSendResultSuccess(result);
		if (flag) {
			portal.setValid("0");// 设置成功.
		} else {
			portal.setValid("1");// 设置成功.
			state = -1;
			code = -1;
			desc = "url设置失败,重新设置！";
		}

		boolean ff=protalSendService.portalSaveOrUpdate(portal);
		obdApiLogger.info("----------【Portal设置】设置URL,poral记录保存结果:"+ff);
		return resultJson(code, state, desc); 
	}

/**
 * 流量额度限制
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type2(JSONObject jso )throws Exception{
		obdApiLogger.info("----------【portal】流量额度限制---"+deviceId+"---请求参数:"+jso.toString());
		String deviceId = jso.optString("deviceId");
		if (!StringUtils.isEmpty(mb)) {
			jso.put("mb", mb);
		}
		if (!StringUtils.isEmpty(mac)) {
			jso.put("mac", mac);
		}
		String mb = jso.optString("mb");// 流量额度限制
		String mac = jso.optString("mac");// 当type为2,5时有值
		String type = jso.optString("type");// 操作类别
		if(StringUtils.isEmpty(mb) || StringUtils.isEmpty(mac) || StringUtils.isEmpty(deviceId)){
			obdApiLogger.info("----------【portal】流量额度限制---"+deviceId+"---请求参数有误;");
			return resultJson(-1, -1, "请求参数有误.");
		}
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】流量额度限制---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		String obdSn = obd.getObdSn();
		
		//判断设备在线状态
		String stockState =obd.getStockState();
		//如果设备离线,不给设置
		Portal portal =getNewPortal(obdSn, type, null, mb, null, mac, null,null);
		if(ObdConstants.ObdOffline.equals(stockState)){
			obdApiLogger.info("----------【portal】流量额度限制---"+deviceId+"---设备离线.");
			portal.setValid("1");
			boolean ff=protalSendService.portalSaveOrUpdate(portal);
			return resultJson(-1, -1, "设备离线,请保持设备在线."); 
		}
		
		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		
		String result ="";
		String bitsStr="0010000000000000";
		char[] bits=bitsStr.toCharArray();
		
		try {
			result=serverSettingService.portalOrWifiSet(obdSn, bits, mac,mb);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【portal】流量额度限制---"+deviceId+"---在线设置失败---"+e);
			state = -1;
			code = -1;
			desc="操作失败:"+e;
		}
		boolean flag = GlobalData.isSendResultSuccess(result);
		if (flag) {
			portal.setValid("0");// 设置成功.
		} else {
			portal.setValid("1");// 设置成功.
			state = -1;
			code = -1;
			desc = "流量额度限制设置失败,重新设置！";
		}

		boolean ff=protalSendService.portalSaveOrUpdate(portal);
		obdApiLogger.info("----------【Portal设置】流量额度限制,poral记录保存结果:"+ff);
		return resultJson(code, state, desc); 
	}


/**
 * 白名单设置
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type3(JSONObject jso )throws Exception{
		obdApiLogger.info("----------【portal】白名单设置---"+deviceId+"---请求参数:"+jso.toString());
		String deviceId = jso.optString("deviceId");
		if (!StringUtils.isEmpty(whitelists)) {
			jso.put("whitelists", whitelists);
		}
		String whitelists = jso.optString("whitelists");// 白名单设置
		String type = jso.optString("type");// 操作类别
		if(StringUtils.isEmpty(whitelists) || StringUtils.isEmpty(deviceId)){
			obdApiLogger.info("----------【portal】白名单设置---"+deviceId+"---请求参数有误;");
			return resultJson(-1, -1, "请求参数有误.");
		}
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】白名单设置---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		String obdSn = obd.getObdSn();
		
		//判断设备在线状态
		String stockState =obd.getStockState();
		//如果设备离线,不给设置
		Portal portal =getNewPortal(obdSn, type, null, null, whitelists, null, null,null);
		if(ObdConstants.ObdOffline.equals(stockState)){
			obdApiLogger.info("----------【portal】白名单设置---"+deviceId+"---设备离线.");
			portal.setValid("1");
			boolean ff=protalSendService.portalSaveOrUpdate(portal);
			return resultJson(-1, -1, "设备离线,请保持设备在线."); 
		}
		
		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		
		
		String result ="";
		String bitsStr="0001000000000000";
		char[] bits=bitsStr.toCharArray();
		
		try {
			result=serverSettingService.portalOrWifiSet(obdSn, bits, whitelists.replaceAll("\\|", ","));
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【portal】白名单设置---"+deviceId+"---在线设置失败---"+e);
			state = -1;
			code = -1;
			desc="操作失败:"+e;
		}
		boolean flag = GlobalData.isSendResultSuccess(result);
		if (flag) {
			portal.setValid("0");// 设置成功.
		} else {
			portal.setValid("1");// 设置成功.
			state = -1;
			code = -1;
			desc = "白名单设置设置失败,重新设置！";
		}

		boolean ff=protalSendService.portalSaveOrUpdate(portal);
		obdApiLogger.info("----------【Portal设置】白名单设置,poral记录保存结果:"+ff);
		return resultJson(code, state, desc); 
	}

/**
 * 全部删除白名单
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type4(JSONObject jso )throws Exception{
		obdApiLogger.info("----------【portal】全部删除白名单---"+deviceId+"---请求参数:"+jso.toString());
		String deviceId = jso.optString("deviceId");
		String type = jso.optString("type");// 操作类别
		if(StringUtils.isEmpty(deviceId)){
			obdApiLogger.info("----------【portal】全部删除白名单---"+deviceId+"---请求参数有误;");
			return resultJson(-1, -1, "请求参数有误.");
		}
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】全部删除白名单---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		String obdSn = obd.getObdSn();
		
		//判断设备在线状态
		String stockState =obd.getStockState();
		//如果设备离线,不给设置
		Portal portal =getNewPortal(obdSn, type, null, null, null, null, null,null);
		if(ObdConstants.ObdOffline.equals(stockState)){
			obdApiLogger.info("----------【portal】全部删除白名单---"+deviceId+"---设备离线.");
			portal.setValid("1");
			boolean ff=protalSendService.portalSaveOrUpdate(portal);
			return resultJson(-1, -1, "设备离线,请保持设备在线."); 
		}
		
		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		
		String result ="";
		String bitsStr="0000100000000000";
		char[] bits=bitsStr.toCharArray();
		
		try {
			result=serverSettingService.portalOrWifiSet(obdSn, bits, "00");
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【portal】全部删除白名单---"+deviceId+"---在线设置失败---"+e);
			state = -1;
			code = -1;
			desc="操作失败:"+e;
		}
		boolean flag = GlobalData.isSendResultSuccess(result);
		if (flag) {
			portal.setValid("0");// 设置成功.
		} else {
			portal.setValid("1");// 设置成功.
			state = -1;
			code = -1;
			desc = "全部删除白名单失败,重新设置！";
		}

		boolean ff=protalSendService.portalSaveOrUpdate(portal);
		obdApiLogger.info("----------【Portal设置】全部删除白名单,poral记录保存结果:"+ff);
		return resultJson(code, state, desc); 
	}

/**
 * 单条删除白名单
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type5(JSONObject jso )throws Exception{
		obdApiLogger.info("----------【portal】单条删除白名单---"+deviceId+"---请求参数:"+jso.toString());
		String deviceId = jso.optString("deviceId");
		if (!StringUtils.isEmpty(mac)) {
			jso.put("mac", mac);
		}
		String mac = jso.optString("mac");// 单条删除白名单
		String type = jso.optString("type");// 操作类别
		if(StringUtils.isEmpty(mac) || StringUtils.isEmpty(deviceId)){
			obdApiLogger.info("----------【portal】单条删除白名单---"+deviceId+"---请求参数有误;");
			return resultJson(-1, -1, "请求参数有误.");
		}
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】单条删除白名单---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		String obdSn = obd.getObdSn();
		
		//判断设备在线状态
		String stockState =obd.getStockState();
		//如果设备离线,不给设置
		Portal portal =getNewPortal(obdSn, type, null, null, null, mac, null,null);
		if(ObdConstants.ObdOffline.equals(stockState)){
			obdApiLogger.info("----------【portal】单条删除白名单---"+deviceId+"---设备离线.");
			portal.setValid("1");
			boolean ff=protalSendService.portalSaveOrUpdate(portal);
			return resultJson(-1, -1, "设备离线,请保持设备在线."); 
		}
		
		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		
		String result ="";
		String bitsStr="0000100000000000";
		char[] bits=bitsStr.toCharArray();
		
		try {
			result=serverSettingService.portalOrWifiSet(obdSn, bits, "01",mac);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【portal】单条删除白名单---"+deviceId+"---在线设置失败---"+e);
			state = -1;
			code = -1;
			desc="操作失败:"+e;
		}
		boolean flag = GlobalData.isSendResultSuccess(result);
		if (flag) {
			portal.setValid("0");// 设置成功.
		} else {
			portal.setValid("1");// 设置成功.
			state = -1;
			code = -1;
			desc = "单条删除白名单设置失败,重新设置！";
		}

		boolean ff=protalSendService.portalSaveOrUpdate(portal);
		obdApiLogger.info("----------【Portal设置】单条删除白名单,poral记录保存结果:"+ff);
		return resultJson(code, state, desc); 
	}

/**
 * 开关
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type6(JSONObject jso )throws Exception{
		obdApiLogger.info("----------【portal】开关---"+deviceId+"---请求参数:"+jso.toString());
		String deviceId = jso.optString("deviceId");
		if (!StringUtils.isEmpty(onOff)) {
			jso.put("onOff", onOff);
		}
		String onOff = jso.optString("onOff");// 开关
		String type = jso.optString("type");// 操作类别
		if(StringUtils.isEmpty(onOff) || StringUtils.isEmpty(deviceId)){
			obdApiLogger.info("----------【portal】开关---"+deviceId+"---请求参数有误;");
			return resultJson(-1, -1, "请求参数有误.");
		}
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【portal】开关---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员."); 
		}
		String obdSn = obd.getObdSn();
		
		//判断设备在线状态
		String stockState =obd.getStockState();
		//如果设备离线,不给设置
		Portal portal =getNewPortal(obdSn, type, null, null, null, null, onOff,null);
		if(ObdConstants.ObdOffline.equals(stockState)){
			//将之前记录置为无效
			Integer total=protalSendService.setByParam(obdSn, "6", "0");
			obdApiLogger.info("----------【Portal设置】开关,将以前poral开关记录置为无效总数:"+total);
			obdApiLogger.info("----------【portal】开关---"+deviceId+"---设备离线.");
			portal.setValid("1");
			boolean ff=protalSendService.portalSaveOrUpdate(portal);
			//保存离线设置记录
			pswitchOffline(obdSn, onOff);
			return resultJson(0, 1, "设备离线,将进行离线设置."); 
		}
		
		int state = 0;
		int code = 0;
		String desc = "操作成功.";
		
		String result ="";
		String bitsStr="0000010100000000";
		char[] bits=bitsStr.toCharArray();
		
		try {
			if("0".equals(onOff)){
				//关闭portal
				//打开portal,默认密码是1234567890
				//查询改设备是否存在密码
				Wifi wifi=wiFiService.isExist(obdSn);
				String pwd="1234567890";
				if(wifi!=null && !StringUtils.isEmpty(wifi.getWifiPwd())){
					pwd =wifi.getWifiPwd();
				}
				result=serverSettingService.portalOrWifiSet(obdSn, bits, "00",pwd);
			}else if("1".equals(onOff)){
				result=serverSettingService.portalOrWifiSet(obdSn, bits, "01");
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【portal】开关---"+deviceId+"---在线设置失败---"+e);
		}
		boolean flag = GlobalData.isSendResultSuccess(result);
		if (flag) {
			portal.setValid("0");// 设置成功.
		} else {
			portal.setValid("1");// 设置成功.
			//保存离线设置记录
			pswitchOffline(obdSn, onOff);
			
			state = 1;
			code = 0;
			desc = "在线设置失败,保存离线设置记录.！";
		}
		//将之前记录置为无效
		Integer total=protalSendService.setByParam(obdSn, "6", "0");
		obdApiLogger.info("----------【Portal设置】开关,将以前poral开关记录置为无效总数:"+total);
		boolean ff=protalSendService.portalSaveOrUpdate(portal);
		obdApiLogger.info("----------【Portal设置】开关,poral记录保存结果:"+ff);
		return resultJson(code, state, desc); 
	}

private boolean pswitchOffline(String obdSn,String onOff){
	//将之前自动唤醒开关置为无效
	String settype = SettingType.PORTAL_00.getValue();
	int total = obdSettingService.obdSettingNoValid(obdSn, settype);
	obdApiLogger.info("----------【portal】开关----------" + deviceId + "---将离线自动唤醒开关记录置为无效总数:" + total);
	//存离线记录
	JSONObject jb = new JSONObject();// 保存内容
	jb.put("portalSwitch", onOff);
	String msg = jb.toString();
	ObdSetting obdSetting = settingNew(obdSn,settype, msg);
	obdSetting.setValid("1");
	//保存最新一条离线记录
	boolean ff=obdSettingService.obdSettingSave(obdSetting);
	obdApiLogger.info("----------【portal】开关----------" + deviceId + "---离线记录保存结果:" + ff);
	return ff;
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


	
	private JSONObject resultJson(Integer code, Integer state, String desc) {
		JSONObject retJso = createReturnJson(code, desc);
		JSONObject resultJso = new JSONObject();
		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		retJso.put("result", resultJso);
		return retJso;
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
	
	private Portal getNewPortal(String obdSn,String type,String url,String mb,String whitelists,String mac,String onOff,String valid)throws Exception{
		if(StringUtils.isEmpty(obdSn) || StringUtils.isEmpty(type)){
			throw new Exception("portal接口异常,设备号和设置类型不能为空.");
		}
		Portal portal = new Portal();
		portal.setObdSn(obdSn);
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
		if (!StringUtils.isEmpty(valid)) {
			portal.setValid(valid);// 有效性
		}
		return portal;
	}

	private String deviceId;
	private String username;
	private String time;
	private String sign;
	
	private String type;// 操作类别
	private String url;// url
	private String mb;// mb
	private String whitelists;// whitelists
	private String mac;// mac
	private String onOff;// onOff

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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


}
