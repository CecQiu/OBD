package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.Date;
import java.util.List;
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
import com.hgsoft.carowner.entity.ObdCar;
import com.hgsoft.carowner.entity.ObdCarHis;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.carowner.service.ObdCarHisService;
import com.hgsoft.carowner.service.ObdCarService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;

/**
 * 车主通平台与客户端接口:车辆型号
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class CarTypeAction extends BaseAction {

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
	private ObdCarService obdCarService;
	@Resource
	private ObdCarHisService obdCarHisService;
	
	@Resource
	private ObdApiRecordService obdApiRecordService;
	
	
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
			oar.setMethod("carType");
			
			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);
				
			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.carType(recJson);
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
			obdApiLogger.error("----------【电信接口异常】车辆型号:" + e);
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
	private JSONObject carType(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【车辆型号设置】---"+deviceId);
		if (!StringUtils.isEmpty(operType)) {
			jso.put("operType", operType);
		}
		
		if (!StringUtils.isEmpty(typeId)) {
			jso.put("typeId", typeId);
		}
	
		String deviceId = jso.optString("deviceId");
		Integer operType = jso.optInt("operType");// 操作类别
		
		obdApiLogger.info("----------【车辆型号设置】设备号:"+deviceId +"---操作类型:" + operType + "---请求参数:"+jso.toString());
		
		//先判断设备是否存在
		OBDStockInfo obd = obdStockInfoService.queryByObdMSN(deviceId);
		if(obd==null){
			obdApiLogger.info("----------【车辆型号设置】---"+deviceId+"---设备不存在.");
			return resultJson(-1, -1, "设备不存在,请联系管理员.",null); 
		}
		String obdSN = obd.getObdSn();
		
		JSONObject retJso = null;
		switch (operType) {
		case 0:
			retJso =type0(obdSN, jso);
			break;
		case 1:
			retJso =type1(obdSN, jso);
			break;
		case 2:
			retJso =type2(obdSN, jso);
			break;
		case 3:
			retJso =type3(obdSN, jso);
			break;
		default:
			obdApiLogger.info("----------【车辆型号设置】---"+deviceId+"---请求类型参数有误---"+operType);
			return resultJson(-1, -1, "请求类型参数有误.",null); 
		}
		
		return retJso;
	}
	
/**
 * 新增
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type0(String obdSN,JSONObject jso ){
		obdApiLogger.info("----------【车辆型号设置】新增---"+deviceId+"---请求参数:"+jso.toString());
		int code = 0;
		int state = 0;
		String desc = "操作成功.";
		try {
			String deviceId = jso.optString("deviceId");
			Integer operType = jso.optInt("operType");// 操作类别
			String typeId = jso.optString("typeId");
			
			if(StringUtils.isEmpty(typeId)){
				obdApiLogger.info("----------【车辆型号设置】新增---"+deviceId+"---请求参数有误;");
				return resultJson(-1, -1, "请求参数有误.","");
			}
			Date now = new Date();
			//根据设备号查询obdCar，如果存在记录，移入历史表obdcarhis，然后插入新的记录
			obdCarDelUpd(obdSN, deviceId,operType,now);
			boolean flag3=ocsave(obdSN, typeId,now);
			obdApiLogger.info("----------【车辆型号设置】新增---"+deviceId+"---记录保存结果："+flag3);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【车辆型号设置】新增---"+deviceId+"---异常信息---"+e);
			code=-1;
			state=-1;
			desc="服务器异常,请联系管理员---"+e;
		}
		 return resultJson(code, state, desc,"");
	}

/**
 * 修改
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type1(String obdSN,JSONObject jso ){
		obdApiLogger.info("----------【车辆型号设置】修改---"+deviceId+"---请求参数:"+jso.toString());
		int code = 0;
		int state = 0;
		String desc = "操作成功.";
		try {
			String deviceId = jso.optString("deviceId");
			Integer operType = jso.optInt("operType");// 操作类别
			String typeId = jso.optString("typeId");
			
			if(StringUtils.isEmpty(typeId)){
				obdApiLogger.info("----------【车辆型号设置】修改---"+deviceId+"---请求参数有误;");
				return resultJson(-1, -1, "请求参数有误.",null);
			}
			Date now = new Date();
			//根据设备号查询obdCar，如果存在记录，移入历史表obdcarhis，然后插入新的记录
			List<ObdCar> obdCars= obdCarService.getLast(obdSN);
			if(obdCars==null || obdCars.size()==0){
				 return resultJson(0, -1, "该设备无相关记录.","");
			}
			obdCarDelUpd(obdSN, deviceId,operType,now);
			boolean flag3=ocsave(obdSN, typeId,now);
			obdApiLogger.info("----------【车辆型号设置】修改---"+deviceId+"---记录保存结果："+flag3);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【车辆型号设置】修改---"+deviceId+"---异常信息---"+e);
			code=-1;
			state=-1;
			desc="服务器异常,请联系管理员---"+e;
		}
		 return resultJson(code, state, desc,"");
	}

/**
 * 删除
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type2(String obdSN,JSONObject jso ){
		obdApiLogger.info("----------【车辆型号设置】删除---"+deviceId+"---请求参数:"+jso.toString());
		int code = 0;
		int state = 0;
		String desc = "操作成功.";
		try {
			String deviceId = jso.optString("deviceId");
			Integer operType = jso.optInt("operType");// 操作类别
			
			Date now = new Date();
			//根据设备号查询obdCar，如果存在记录，移入历史表obdcarhis，然后插入新的记录
			List<ObdCar> obdCars= obdCarService.getLast(obdSN);
			if(obdCars==null || obdCars.size()==0){
				 return resultJson(0, -1, "该设备无相关记录.","");
			}
			obdCarDelUpd(obdSN, deviceId,operType,now);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【车辆型号设置】删除---"+deviceId+"---异常信息---"+e);
			code=-1;
			state=-1;
			desc="服务器异常,请联系管理员---"+e;
		}
		 return resultJson(code, state, desc,"");
	}

//历史记录删除
private void obdCarDelUpd(String obdSN,String deviceId,Integer operType,Date now){
	List<ObdCar> obdCars= obdCarService.getLast(obdSN);
	if(obdCars!=null && obdCars.size()>0){
		for (ObdCar obdCar : obdCars) {
			ObdCarHis obdCarHis = new ObdCarHis();
			obdCarHis.setId(IDUtil.createID());
			obdCarHis.setOriginId(obdCar.getId());
			obdCarHis.setObdSn(obdCar.getObdSn());
			obdCarHis.setTypeId(obdCar.getTypeId());
			obdCarHis.setOperType(operType);
			obdCarHis.setCreateTime(obdCar.getCreateTime());
			obdCarHis.setDelTime(now);
			boolean flag1=obdCarHisService.obdCarHisSave(obdCarHis);
			obdApiLogger.info("----------【车辆型号设置】---"+deviceId+"---历史记录插入结果："+flag1);
			boolean flag2=obdCarService.obdCarDel(obdCar);
			obdApiLogger.info("----------【车辆型号设置】---"+deviceId+"---原记录删除结果："+flag2);
		}
	}
}

//记录保存结果
private boolean ocsave(String obdSN,String typeId,Date now){
	ObdCar obdCar = new ObdCar();
	obdCar.setId(IDUtil.createID());
	obdCar.setCreateTime(now);
	obdCar.setObdSn(obdSN);
	obdCar.setTypeId(typeId);
	boolean flag=obdCarService.obdCarSave(obdCar);
	return flag;
}

/**
 * 新增
 * @param deviceId
 * @param jso
 * @return
 */
private JSONObject type3(String obdSN,JSONObject jso ){
		obdApiLogger.info("----------【车辆型号设置】查询---"+deviceId+"---请求参数:"+jso.toString());
		int code = 0;
		int state = 0;
		String desc = "操作成功.";
		String typeId = "";
		try {
			String deviceId = jso.optString("deviceId");
			ObdCar obdCar=obdCarService.getLatest(obdSN);
			obdApiLogger.info("----------【车辆型号设置】查询---"+deviceId+"---查询结果："+obdCar);
			if(obdCar!=null){
				typeId = obdCar.getTypeId();
			}else{
				desc="无相关记录.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【车辆型号设置】查询---"+deviceId+"---异常信息---"+e);
			code=-1;
			state=-1;
			desc="服务器异常,请联系管理员---"+e;
		}
		 return resultJson(code, state, desc,typeId);
	}


	
	private JSONObject resultJson(Integer code, Integer state, String desc,String typeId) {
		JSONObject retJso = createReturnJson(code, desc);
		JSONObject resultJso = new JSONObject();
		// 当前状态：0成功 -1失败
		resultJso.put("state", state);
		resultJso.put("typeId", typeId);
		retJso.put("result", resultJso);
		return retJso;
	}

	private String deviceId;
	private String username;
	private String time;
	private String sign;
	
	private Integer operType;// 操作类别
	private String typeId;


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

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
	
}
