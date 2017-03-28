package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdVersionService;
import com.hgsoft.carowner.service.UpgradeSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
/**
 * 升级结果推送
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class UpgradeHandle extends BaseAction {

	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");

	/** 接收的json数据 */
	private String jsonStr;
	/** 权限检验-成功编码 */
	private final String CHECK_SUCCESS = "01";
	/** 权限检验-失败编码 */
	private final String CHECK_ERROR = "02";
	@Resource
	private MebUserService userService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ObdVersionService obdVersionService;
	@Resource
	private UpgradeSetService upgradeSetService;
	
	/**
	 * 接口入口，权限校验
	 */
	public void entryInterface() {
		long begin = System.currentTimeMillis();
		JSONObject returnJson = null;
		HttpServletRequest request = null;
		// 请求用户
		String requestUser = "";
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
			String path = request.getRequestURI();
			upgradeDataLogger.info("----------【请求路径】：" + path);
			upgradeDataLogger.info("----------【请求基本参数】：" + jsonStr);
//			Request rr = (Request) JSONObject.toBean(JSONObject.fromObject(jsonStr), Request.class);
//			System.out.println(rr);
			Pattern p = Pattern.compile(".*/services/upgrade/(.*)");
			Matcher m = p.matcher(path);
			if (m.matches()) {
				String method = m.group(1);
				// 校验权限
				upgradeDataLogger.info("----------【校验权限】----------");
				JSONObject jso = checkPermissions(jsonStr);
				requestUser = jso.optString("username");
				if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
					upgradeDataLogger.info("----------【校验权限】通过----------");
					JSONObject recJson = jso.getJSONObject("recJson");
					// 相应的接口
					switch (method) {
					case "updateObdStatus":
						returnJson = this.updateObdStatus(recJson);
						break;
					default:
						upgradeDataLogger.error("----------【请求失败】-没有对应的接口：" + method);
						break;
					}
				} else {
					String errorStr = "it is not pass the permissions check,换句话说：就是权限校验没过，自己看着办";
					returnJson = createReturnJson(-1, "请求失败.");
					returnJson.put("errorInfo", errorStr);
					upgradeDataLogger.info("----------【校验权限】失败----------");
				}
			} else {
				upgradeDataLogger.error("----------【请求路径不正常】：" + path);
			}
		} catch (Exception e) {
			returnJson = createReturnJson(-1, "请求失败,请联系管理员.");
			returnJson.put("errorInfo", e.getMessage());
			e.printStackTrace();
			upgradeDataLogger.error("----------【请求异常】：" + e);
		}
		System.out.println("go on");
		// 返回json数据
		if (returnJson == null) {
			returnJson = new JSONObject();
		}
		upgradeDataLogger.info("~upgradeDataLogger~:【" + request.getRemoteHost() + "】-"
				+ requestUser + "-" + request.getRequestURI() + "->"
				+ (System.currentTimeMillis() - begin));
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
		upgradeDataLogger.info("----------【校验权限】请求参数：" + recJson);
		// 权限校验的结果编码：01-失败 02-成功
		String checkCode = CHECK_SUCCESS;
		// 平台分配的用户
		String username = recJson.optString("username");
		// 时间戳
		String time = recJson.optString("time");
		// MD5 32位(userId+ carUser + time+ password)password为系统分配的密码
		String sign = recJson.optString("sign");
		upgradeDataLogger.info("参数：【用户】->" + username + ",【时间】->" + time
				+ ",【sign】->" + sign);
		// 读取账号密码
		String password = PropertiesUtil.getInstance("accounts.properties").readProperty(username);
		String mySign = MD5Coder.encodeMD5Hex( username + time + MD5Coder.encodeMD5Hex(password) +"obdhgsoft");
		upgradeDataLogger.info("----------【校验权限】MD5加密：" + mySign);
		if ( StringUtils.isEmpty(password)|| StringUtils.isEmpty(username) || StringUtils.isEmpty(time) || !mySign.equals(sign)) {
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
	 * 设备状态更新
	 * 
	 */
	private JSONObject updateObdStatus(JSONObject jso) throws Exception {
		upgradeDataLogger.info("----------【升级服务器-设备状态更新】----------");
		JSONObject json = jso.getJSONObject("body");
		String obdSn = json.optString("obdSn");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		String version = json.optString("version");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		Integer type = json.optInt("type");
		String updateFlag = json.optString("updateFlag");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		String updateTime = json.optString("updateTime");// 待绑定设备厂商的唯一标识。原设备厂商的设备序列号
		if(StringUtils.isEmpty(obdSn) && StringUtils.isEmpty(version)){
			throw new Exception("请求参数不足,请联系管理员.");
		}
		if(type==0){
			if( StringUtils.isEmpty(updateFlag) || StringUtils.isEmpty(updateTime)){
				throw new Exception("请求参数不足,请联系管理员.");
			}
		}
		upgradeDataLogger.info("----------【升级服务器-设备状态更新】参数：---设备号:"+obdSn+"---请求类型:"+type+"---版本号:"+version+"---升级结果:"+updateFlag+"---升级时间:"+updateTime);
		int code = 0;
		String desc = "成功！";
		//查询待升级记录
		UpgradeSet upgradeSet=upgradeSetService.queryByObdSnAndVersion(obdSn, version);
		if(upgradeSet==null){
			code =-1;
			desc="无对应的待升级记录";
		}
		switch (type) {
		case 0:
			ObdVersion obdVersion = new ObdVersion();
			obdVersion.setObdSn(obdSn);
			obdVersion.setVersion(version);
			obdVersion.setUpdateFlag(updateFlag);
			long udate= Long.parseLong(updateTime);
			obdVersion.setCreateTime(new Date(udate));
			if(upgradeSet!=null){
				upgradeDataLogger.info("----------【升级服务器-设备状态更新】无对应升级记录.");
				obdVersion.setFirmVersion(upgradeSet.getFirmVersion());
			}
			
			//保存升级结果
			if(upgradeSet!=null){
				obdVersion.setFirmType(upgradeSet.getFirmType());
			}
			boolean ff=obdVersionService.obdVersionSave(obdVersion);
			upgradeDataLogger.info("----------【升级服务器-设备状态更新】升级结果详情记录保存结果："+ff);
			if(upgradeSet!=null){
//				UpgradeSet upgradeSet=upgradeSetService.queryByObdSnAndVersion(obdSn, version);
				upgradeSet.setUpgradeFlag(updateFlag);
				upgradeSet.setUpdateTime(new Date(udate));
				boolean flag=upgradeSetService.upgradeSetSave(upgradeSet);
				upgradeDataLogger.info("----------【升级服务器-设备状态更新】更新升级记录的升级状态结果："+flag);
			}else{
				code =-1;
				desc ="不存在相关记录.";
			}
			break;
		case 1:
			if(upgradeSet!=null){
				String valid=upgradeSet.getValid();
				if(!"0".equals(valid)){
					upgradeSet.setValid("0");
				}
				upgradeSet.setValidTrue("0");
				upgradeSet.setUpdateTime(new Date());
				boolean flag1=upgradeSetService.upgradeSetSave(upgradeSet);
				upgradeDataLogger.info("----------【升级服务器-设备状态更新】更新升级记录的是否真正下发升级报文状态结果："+flag1);
			}else{
				code =-1;
				desc ="不存在相关记录";
			}
			break;
		default:
			 code = -1;
			 desc = "请求参数类型有误！";
		}
		
		JSONObject retJso = createReturnJson(code, desc);
		// 0，绑定成功；1,用户不存在，绑定失败；2，要绑定的设备不存在，绑定失败；3,旧设备解绑定不成功, 6 设备已经绑定
		upgradeDataLogger.info("----------【升级服务器-设备状态更新】返回结果："+ retJso);
		return retJso;
	}


	public static void main(String[] args) throws Exception {
//		{"body":{"entry":[{"key":{"@type":"xs:string","$":"createTime"},"value":{"@type":"xs:long","$":"1462286321687"}},{"key":{"@type":"xs:string","$":"id"},"value":{"@type":"xs:int","$":"20"}},{"key":{"@type":"xs:string","$":"obdSn"},"value":{"@type":"xs:string","$":"30000804"}},{"key":{"@type":"xs:string","$":"remark"},"value":{"@type":"xs:string","$":""}},{"key":{"@type":"xs:string","$":"updateFlag"},"value":{"@type":"xs:string","$":"00"}},{"key":{"@type":"xs:string","$":"updateTime"},"value":{"@type":"xs:long","$":"1462286321687"}},{"key":{"@type":"xs:string","$":"version"},"value":{"@type":"xs:string","$":"3333"}}]},"password":"5e26b1348d4516a8d8f8e8067450b4a4","sign":"829778dd3d224d3da7d6965640b9c38d","time":"1462286321740","username":"obdUpgrade"}
	}

	// 测试
	private void test() {}
	//权限校验
	private String username;
	private String password;
	private String time;
	private String sign;
	//设备状态更新
	private String obdSn;//设备号
	private Integer type;//0-更新升级结果 1-更新真正下发成功标志
	private String version;//版本号
	private String updateFlag;//更新结果
	private long updateTime;//更新时间（时间戳）

	
	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
