package com.hgsoft.carowner.api;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.hgsoft.carowner.entity.Fence;
import com.hgsoft.carowner.entity.FenceHis;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.carowner.service.FenceHisService;
import com.hgsoft.carowner.service.FenceService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdApiRecordService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;

/**
 * 车主通平台与客户端接口:电子围栏设置
 * 
 * @author ljl
 */
@Controller
@Scope("prototype")
public class DzwlAction extends BaseAction {

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
	private FenceService fenceService;// 电子围栏
	@Resource
	private FenceHisService fenceHisService;// 电子围栏
	
	@Resource
	private ObdApiRecordService obdApiRecordService;
	
	final int fenceObdTotal = new Integer(PropertiesUtil.getInstance("params.properties").readProperty("fenceObdTotal","100"));// 每次只能删除1个
	
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
			
			if(StringUtils.isEmpty(jsonStr)){
				this.outMessage(createReturnJson(-1, "请求失败.").toString());
				return;
			}
			String path = request.getRequestURI();
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
			oar.setMethod("dzwl");
			
			obdApiLogger.info("----------【请求路径】：" + path + "----------【请求基本参数】：" + jsonStr);
				
			// 校验权限
			obdApiLogger.info("----------【校验权限】----------");
			JSONObject jso = checkPermissions(jsonStr);
			requestUser = jso.optString("username");
			if (jso.optString("checkCode").equals(CHECK_SUCCESS)) {
				obdApiLogger.info("----------【校验权限】通过----------");
				JSONObject recJson = jso.getJSONObject("recJson");
				returnJson = this.fenceSet(recJson);
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
			obdApiLogger.error("----------【电信接口】电子围栏异常信息:" + e);
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
		obdApiLogger.error("----------【电信接口操作记录保存结果】电子围栏:" + flag);
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
	 * 电子围栏接口 liujialin 
	 * b.根据type进行逻辑处理
	 * 
	 */
	private JSONObject fenceSet(JSONObject jso) throws Exception {
		obdApiLogger.info("----------【电子围栏设置】---"+deviceId);
		if (!StringUtils.isEmpty(type)) {
			jso.put("type", type);
		}
		if (!StringUtils.isEmpty(obdMsnList)) {
			jso.put("obdMsnList", obdMsnList);
		}
		
		if (!StringUtils.isEmpty(areaNum)) {
			jso.put("areaNum", areaNum);
		}
		if (!StringUtils.isEmpty(points)) {
			jso.put("points", points);
		}
		if (!StringUtils.isEmpty(alert)) {
			jso.put("alert", alert);
		}
		if (!StringUtils.isEmpty(timerType)) {
			jso.put("timerType", timerType);
		}
		if (!StringUtils.isEmpty(dayWeek)) {
			jso.put("dayWeek", dayWeek);
		}
		if (!StringUtils.isEmpty(startDate)) {
			jso.put("startDate", startDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			jso.put("endDate", endDate);
		}
		
		if (!StringUtils.isEmpty(startTime)) {
			jso.put("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime)) {
			jso.put("endTime", endTime);
		}
		
		String deviceId = jso.optString("deviceId");
		Integer type = jso.optInt("type");// 操作类别
		
		obdApiLogger.info("----------【电子围栏设置】设备号:"+deviceId +"---操作类型:" + type + "---请求参数:"+jso.toString());
		
		int state = 0;
		int code = 0;
		String desc = "";
		
		JSONObject retJso = null;
		switch (type) {
		case 1:
			retJso =type1(deviceId, jso);
			break;
		case 2:
			retJso =type2(deviceId, jso);
			break;
		case 3:
			retJso =type3(deviceId, jso);
			break;
		case 4:
			retJso =type4(deviceId, jso);
			break;
		case 5:
			retJso =type5(deviceId, jso);
			break;
		default:
			obdApiLogger.info("----------【电子围栏设置】---"+deviceId+"---请求类型参数有误---"+type);
			code = -1;
			state = -1;
			desc ="请求类型参数有误.";
			return resultJson(code, state, desc); 
		}
		
		return retJso;
	}
	//2取消围栏
	private JSONObject type2(String deviceId,JSONObject jso){
		obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---请求参数:"+jso.toString());
		Integer type = jso.optInt("type");// 操作类别
		String obdMsnList = jso.optString("obdMsnList");
		Integer areaNum = jso.optInt("areaNum");// 操作类别
		int code =0;
		int state =0;
		String desc = "操作成功.";

		if(StringUtils.isEmpty(obdMsnList)){
			code = -1; state = -1; desc ="请求参数有误.";
			 return resultJson(code, state, desc);
		}
		
		OBDStockInfo obd=obdStockInfoService.queryByObdMSN(obdMsnList);
		if(obd==null){
			 code = -1;
			 state =-1;
			 desc ="该设备未注册.";
			 return resultJson(code, state, desc);
		}
		String obdSn = obd.getObdSn();
		//查询围栏信息
		Fence fence=fenceService.queryLastByParams(obdSn, areaNum);
		obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---"+fence);
		if(fence==null){
			return resultJson(-1, -3, "操作失败,不存在该编号围栏.");
		}
		
		//删除围栏
		boolean fdFlag=fenceService.fdel(fence);
		obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---删除围栏结果:"+fdFlag);
		if(!fdFlag){
			obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---删除围栏失败,用户重新删除.");
			return resultJson(-1, -1, "操作失败,请稍后再试或联系管理员.");
		}
//		//2将围栏置为无效
//		fence.setUpdateTime(new Date());
//		fence.setValid(0);
		
		//保存到历史表里去
		try {
			FenceHis fenceHis = getNewFenceHis(fence);
			boolean fsFlag=fenceHisService.fsave(fenceHis);
			obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---记录保存历史表结果:"+fsFlag);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---记录保存历史表异常:"+e);
		}
		
		FenceHis fh =  new FenceHis();
		fh.setId(IDUtil.createID());
		fh.setObdSn(obdSn);
		fh.setType(type);
		fh.setAreaNum(areaNum);
		fh.setCreateTime(new Date());
		fh.setValid(0);
		boolean flag = fenceHisService.fsave(fh);
		obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---"+obdSn+"---取消操作记录保存历史表结果:"+flag);
		//不管记录是否保存历史表成功，以及取消操作记录是否保存成功，都会返回成功。
		 return resultJson(code, state, desc);
		
//		//将围栏置为无效
//		Set<Integer> validSet = new HashSet<Integer>();
//		validSet.add(1);
//		validSet.add(2);
//		Integer total =fenceService.setNoValid(obdSn, areaNum, 0,validSet);
//		obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---"+obdSn+"---取消总数:"+total);
//		if(total==0){
//			obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---"+obdSn+"---取消失败,不存在改围栏编号:"+areaNum);
//			code =-1;
//			state =-1;
//			desc ="该设备不存在该围栏编号:"+areaNum;
//		}
//		Fence fence =  new Fence();
//		fence.setId(IDUtil.createID());
//		fence.setObdSn(obdSn);
//		fence.setType(type);
//		fence.setAreaNum(areaNum);
//		fence.setCreateTime(new Date());
//		if(total==0){
//			obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---"+obdSn+"---取消失败,不存在改围栏编号:"+areaNum);
//			state =-1;
//			desc ="该设备不存在该围栏编号:"+areaNum;
//			fence.setValid(-1);
//		}else{
//			fence.setValid(0);
//		}
//		boolean flag = fenceService.fsave(fence);
//		obdApiLogger.info("----------【电子围栏设置】取消围栏---"+deviceId+"---"+obdSn+"---操作记录保存结果:"+flag);
		
	}
	//3取消所有围栏
	private JSONObject type3(String deviceId,JSONObject jso){
		obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---请求参数:"+jso.toString());
		int code =0;
		int state =0;
		String desc = "操作成功.";
		String obdMsnList = jso.optString("obdMsnList");
		Integer type = jso.optInt("type");// 操作类别
		if(StringUtils.isEmpty(obdMsnList)){
			code = -1; state = -1; desc ="请求参数有误.";
			 return resultJson(code, state, desc);
		}
		
			OBDStockInfo obd=obdStockInfoService.queryByObdMSN(obdMsnList);
			if(obd==null){
				 code = -1; state =-1; desc ="该设备未注册.";
				 return resultJson(code, state, desc);
			}
			String obdSn = obd.getObdSn();
			//查询所有围栏数据
			List<Integer> vList = new ArrayList<>();
			vList.add(1);
			vList.add(2);
			List<Fence> fences=fenceService.queryList(obdSn, null, vList);
			if(fences==null ||fences.size()==0){
				obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---该设备不存在围栏数.");
				return resultJson(code, state, desc);
			}
			//删除围栏
			List<String> ids = new ArrayList<>();
			for (Fence fence : fences) {
				ids.add(fence.getId());
			}
			Integer total=fenceService.fenceListDel(null, null, ids);
			obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---取消围栏总数:"+total);
			if(total==null || total==0){
				return resultJson(-1, -1, "操作失败,请稍后重试或联系管理员.");
			}
			//保存历史表
			try {
				List<FenceHis> fhs=getFenceHisList(fences);
				for (FenceHis fenceHis : fhs) {
					boolean f=fenceHisService.fsave(fenceHis);
					obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---分条保存如历史表结果:"+f+"---"+fenceHis.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
				obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---分条保存如历史表异常:"+e);
			}
			//保存操作记录
			FenceHis fh =  new FenceHis();
			fh.setId(IDUtil.createID());
			fh.setObdSn(obdSn);
			fh.setType(type);
			fh.setCreateTime(new Date());
			fh.setValid(0);
			boolean flag=fenceHisService.fsave(fh);
			obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---操作记录保存历史表结果:"+flag);
			return resultJson(code, state, desc);
			
//			Set<Integer> validSet = new HashSet<Integer>();
//			validSet.add(1);
//			validSet.add(2);
//			Integer total =fenceService.setNoValid(obdSn, null, 0,validSet);
//			obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---置为无效总数:"+total);
//			Fence fence =  new Fence();
//			fence.setId(IDUtil.createID());
//			fence.setObdSn(obdSn);
//			fence.setType(type);
//			fence.setCreateTime(new Date());
//			fence.setValid(0);
//			boolean flag=fenceService.fsave(fence);
//			obdApiLogger.info("----------【电子围栏设置】取消所有围栏---"+deviceId+"---操作记录保存结果:"+flag);
	}
	//4暂停使用围栏
	private JSONObject type4(String deviceId,JSONObject jso){
		obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---请求参数:"+jso.toString());
		int code =0;
		int state =0;
		String desc = "操作成功.";
		try {
			String obdMsnList = jso.optString("obdMsnList");
			Integer areaNum = jso.optInt("areaNum");// 区域编号
			Integer type = jso.optInt("type");// 操作类别
			if(StringUtils.isEmpty(obdMsnList)){
				code = -1; state = -1; desc ="请求参数有误.";
				 return resultJson(code, state, desc);
			}
			
			OBDStockInfo obd=obdStockInfoService.queryByObdMSN(obdMsnList);
			if(obd==null){
				 code = -1; state =-1; desc ="该设备未注册.";
				 return resultJson(code, state, desc);
			}
			String obdSn = obd.getObdSn();
			//查询电子围栏
			Fence fe=fenceService.queryLastByParams(obdSn, areaNum);
			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---"+fe);
			if(fe==null){
				code = -1; state = -3; desc ="操作失败,不存在该编号的围栏.";
				 return resultJson(code, state, desc);
			}
			Integer validF= fe.getValid();
			if(validF!=null && validF==2){
				code = -1; state = -4; desc ="操作失败,该编号围栏暂停使用.";
				 return resultJson(code, state, desc);
			}
			
			//更新状态
			fe.setValid(2);
			fe.setUpdateTime(new Date());
			boolean ff=fenceService.fsave(fe);
			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---将记录更新为停用状态结果："+ff);
			if(!ff){
				 return resultJson(-1, -1, "操作失败,请稍后重试或联系管理员.");
			}
			
			//保存操作记录导历史表
			//保存操作记录
			FenceHis fh =  new FenceHis();
			fh.setId(IDUtil.createID());
			fh.setObdSn(obdSn);
			fh.setType(type);
			fh.setAreaNum(areaNum);
			fh.setCreateTime(new Date());
			fh.setValid(0);
			boolean flag=fenceHisService.fsave(fh);
			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---操作记录保存历史表结果:"+flag);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---异常信息:"+e);
			code =-1;
			state =-1;
			desc ="服务器异常，请联系管理员---"+e;
		}
		
		return resultJson(code, state, desc);
//		Set<Integer> validSet = new HashSet<Integer>();
//		validSet.add(1);
//		try {
//			Integer total =fenceService.setNoValid(obdSn, areaNum, 2,validSet);
//			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---将记录更新为停用总数:"+total);
//			if(total==0){
//				obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---不存在该编号的围栏.");
//				code =-1;
//				state =-3;
//				desc ="不存在该编号的围栏.";
//			}
			
			
//			Fence fence =  new Fence();
//			fence.setId(IDUtil.createID());
//			fence.setObdSn(obdSn);
//			fence.setType(type);
//			fence.setCreateTime(new Date());
//			if(total==0){
//				obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---不存在该编号的围栏.");
//				fence.setValid(-1);
//				state =-3;
//				desc ="不存在该编号的围栏.";
//			}else{
//				fence.setValid(0);
//			}
//			boolean flag=fenceService.fsave(fence);
//			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---操作记录保存结果:"+flag);
//		} catch (Exception e) {
//			e.printStackTrace();
//			obdApiLogger.info("----------【电子围栏设置】暂停使用围栏---"+deviceId+"---异常信息:"+e);
//			code =-1;
//			state =-1;
//			desc ="服务器异常，请联系管理员---"+e;
//		}
	}
	//7恢复使用围栏
	private JSONObject type5(String deviceId, JSONObject jso) {
		obdApiLogger.info("----------【电子围栏设置】启用围栏---" + deviceId + "---请求参数:" + jso.toString());
		int code = 0;
		int state = 0;
		String desc = "操作成功.";
		try {
			String obdMsnList = jso.optString("obdMsnList");
			Integer areaNum = jso.optInt("areaNum");// 区域编号
			Integer type = jso.optInt("type");// 操作类别
			if (StringUtils.isEmpty(obdMsnList)) {
				code = -1;
				state = -1;
				desc = "请求参数有误.";
				return resultJson(code, state, desc);
			}

			OBDStockInfo obd = obdStockInfoService.queryByObdMSN(obdMsnList);
			if (obd == null) {
				code = -1;
				state = -1;
				desc = "该设备未注册.";
				return resultJson(code, state, desc);
			}
			String obdSn = obd.getObdSn();
			// 查询电子围栏
			Fence fe = fenceService.queryLastByParams(obdSn, areaNum);
			obdApiLogger.info("----------【电子围栏设置】启用围栏---" + deviceId + "---" + fe);
			if (fe == null) {
				code = -1;
				state = -3;
				desc = "操作失败,不存在该编号的围栏.";
				return resultJson(code, state, desc);
			}
			Integer validF = fe.getValid();
			if (validF != null && validF == 1) {
				code = -1;
				state = -5;
				desc = "操作失败,该编号围栏处于启动状态.";
				return resultJson(code, state, desc);
			}
			// 恢复状态
			fe.setValid(1);
			fe.setUpdateTime(new Date());
			boolean ff = fenceService.fsave(fe);
			obdApiLogger.info("----------【电子围栏设置】启用围栏----" + deviceId + "---将记录更新为启用状态结果：" + ff);
			if (!ff) {
				return resultJson(-1, -1, "操作失败,请稍后重试或联系管理员.");
			}
			FenceHis fh = new FenceHis();
			fh.setId(IDUtil.createID());
			fh.setObdSn(obdSn);
			fh.setType(type);
			fh.setAreaNum(areaNum);
			fh.setCreateTime(new Date());
			fh.setValid(0);
			boolean flag = fenceHisService.fsave(fh);
			obdApiLogger.info("----------【电子围栏设置】启用围栏----" + deviceId + "---操作记录保存历史表结果:" + flag);

			// Set<Integer> validSet = new HashSet<Integer>();
			// validSet.add(2);
			// try {
			// Integer total =fenceService.setNoValid(obdSn, areaNum,
			// 1,validSet);
			// obdApiLogger.info("----------【电子围栏设置】启用围栏---"+deviceId+"---启用围栏总数:"+total);
			// if(total==0){
			// obdApiLogger.info("----------【电子围栏设置】启用围栏---"+deviceId+"---不存在该编号的围栏.");
			// code=-1;
			// state =-3;
			// desc ="不存在该编号围栏.";
			// }

			// Fence fence = new Fence();
			// fence.setId(IDUtil.createID());
			// fence.setObdSn(obdSn);
			// fence.setType(type);
			// fence.setCreateTime(new Date());
			// if(total==0){
			// obdApiLogger.info("----------【电子围栏设置】启用围栏---"+deviceId+"---不存在该编号的围栏.");
			// fence.setValid(-1);
			// state =-3;
			// desc ="不存在该编号围栏.";
			// }else{
			// fence.setValid(0);
			// }
			// boolean flag=fenceService.fsave(fence);
			// obdApiLogger.info("----------【电子围栏设置】启用围栏---"+deviceId+"---操作记录保存结果:"+flag);
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【电子围栏设置】启用围栏---" + deviceId + "---异常信息:" + e);
			code = -1;
			state = -1;
			desc = "服务器异常，请联系管理员---" + e;
		}
		return resultJson(code, state, desc);
	}
	/**
	 * 8新增围栏
	 * 因为一个围栏支持多个定时，是否需要判断围栏编号重复
	 * 总数不能超过100个；
	 * 不能有重复的设备号
	 * 只能设置激活的设备
	 * 我们这边不限制个数，由电信号百那边控制
	 * @param obdStockInfo
	 * @param areaNum
	 * @param points
	 * @param timerRuleData
	 * @return
	 */
	private JSONObject type1(String deviceId,JSONObject jso ){
		obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---请求参数:"+jso.toString());
		int code = 0;
		int state = 0;
		String desc = "操作成功.";
		try {
			Integer type = jso.optInt("type");// 操作类别
			String obdMsnList = jso.optString("obdMsnList");
			Integer areaNum = jso.optInt("areaNum");// 操作类别
			String points = jso.optString("points");
			Integer alert = jso.optInt("alert");// 操作类别
			Integer timerType = jso.optInt("timerType");// 操作类别
			String dayWeek = jso.optString("dayWeek");
			String startDate = jso.optString("startDate");
			String endDate = jso.optString("endDate");
			String startTime = jso.optString("startTime");
			String endTime = jso.optString("endTime");
			
			if(StringUtils.isEmpty(obdMsnList) || StringUtils.isEmpty(areaNum) || StringUtils.isEmpty(points) 
					|| StringUtils.isEmpty(alert) || StringUtils.isEmpty(timerType)  || StringUtils.isEmpty(startTime) 
					|| StringUtils.isEmpty(endTime)){
				obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---请求参数有误;");
				code = -1;
				state =-1;
				desc ="请求参数有误。";
				return resultJson(code, state, desc);
			}
			
			if(timerType==3){
				if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
					code = -1;
					state =-1;
					desc ="请求参数有误。";
					return resultJson(code, state, desc);
				}else{
					Date startDateD = (Date)DateUtil.fromatDate(startDate, "yyyy-MM-dd");
					Date endDateD = (Date)DateUtil.fromatDate(endDate, "yyyy-MM-dd");
					if(startDateD.after(endDateD) || startDateD.equals(endDateD)){
						code = -1;
						state =-1;
						desc ="开始日期不能大于等于结束日期。";
						return resultJson(code, state, desc);
					}
				}
			}
			
			if(timerType==1){
				if(StringUtils.isEmpty(dayWeek)){
					return resultJson(-1, -1, "请求参数有误,请联系管理员.");
				}
			}
			
			//开始时间不能大于结束时间
			Date startTimeD = (Date)DateUtil.fromatDate(startTime, "HH:mm:ss");
			Date endTimeD = (Date)DateUtil.fromatDate(endTime, "HH:mm:ss");
			if(startTimeD.after(endTimeD) || startTimeD.equals(endTimeD)){
				code = -1;
				state =-1;
				desc ="开始时间不能大于等于结束时间.";
				return resultJson(code, state, desc);
			}
			
			//同时设置总数不能超过100个
			String[] obdMsnArray = obdMsnList.split(",");
			if(obdMsnArray.length>fenceObdTotal){
				code = -1;
				state =-1;
				desc ="最多只能同时设置"+fenceObdTotal+"个设备的电子围栏。";
				return resultJson(code, state, desc);
			}
			//不能有重复的设备号
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (String string : obdMsnArray) {
				if(!map.containsKey(string)){
					map.put(string, 1);
				}else{
					map.put(string, map.get(string)+1);
				}
			}
			for (String key : map.keySet()) {
			   if(map.get(key)>1){
				   obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---表面码重复:"+key);
				   code =-1;
				   state =-1;
				   desc = key+"---表面码重复.";
				   return resultJson(code, state, desc);
			   }
			}
			
			//只能设置已激活的设备
			List<OBDStockInfo> obdList = obdStockInfoService.getListByMap(map);
			if(obdList.size()<obdMsnArray.length){
				obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---存在未注册表面号,请检查.");
				code =-1; 
				state =-1;
				 desc ="存在未注册表面号,请检查.";
				 return resultJson(code, state, desc);
			}
			Map<String, Integer> obdSnMap = new HashMap<String, Integer>();
			for (OBDStockInfo obd : obdList) {
				String obdSn = obd.getObdSn();
				obdSnMap.put(obdSn, 1);
			}
			
			//根据设备号和围栏编号查询，看是否存在重复围栏编号
			List<Fence> fenceHas = fenceService.getListByMap(obdSnMap, areaNum);
			if(fenceHas!=null && fenceHas.size()>0){
				StringBuffer sb = new StringBuffer();
				for (Fence fence : fenceHas) {
					sb.append(fence.getObdSn()+";");
				}
				obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---部分设备存在重复围栏编号---"+sb.toString());
				code =-1;state =-2;desc ="部分设备存在重复围栏编号:"+sb.toString();
				 return resultJson(code, state, desc);
			}
			
			for (String obdSn : obdSnMap.keySet()) {
				Fence fence = new Fence();
				fence.setId(IDUtil.createID());
				fence.setObdSn(obdSn);
				fence.setType(type);
				fence.setAlert(alert);
				fence.setAreaNum(areaNum);
				fence.setPoints(points);
				fence.setTimerType(timerType);
				if(timerType==1){
					fence.setDayWeek(dayWeek);
				}
				if(timerType==3){
					fence.setStartDate((Date)DateUtil.fromatDate(startDate, "yyyy-MM-dd"));
					fence.setEndDate((Date)DateUtil.fromatDate(endDate, "yyyy-MM-dd"));
				}
				fence.setStartTime(startTimeD);
				fence.setEndTime(endTimeD);
				
				fence.setCreateTime(new Date());
				fence.setValid(1);
				boolean flag = fenceService.fsave(fence);
				obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---"+obdSn+"---围栏保存结果---"+flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdApiLogger.info("----------【电子围栏设置】新增围栏---"+deviceId+"---异常信息---"+e);
			code=-1;
			state=-1;
			desc="服务器异常,请联系管理员---"+e;
		}
		 return resultJson(code, state, desc);
	}

	private FenceHis getNewFenceHis(Fence fence) throws Exception{
		if(fence == null){
			throw new Exception("电子围栏不能为空.");
		}
		FenceHis fenceHis = new FenceHis();
		fenceHis.setId(IDUtil.createID());
		fenceHis.setOriginId(fence.getId());
		fenceHis.setObdSn(fence.getObdSn());
		fenceHis.setType(fence.getType());
		fenceHis.setAlert(fence.getAlert());
		fenceHis.setAreaNum(fence.getAreaNum());
		fenceHis.setPoints(fence.getPoints());
		fenceHis.setTimerType(fence.getTimerType());
		fenceHis.setDayWeek(fence.getDayWeek());
		fenceHis.setStartDate(fence.getStartDate());
		fenceHis.setEndDate(fence.getEndDate());
		fenceHis.setStartTime(fence.getStartTime());
		fenceHis.setEndTime(fence.getEndTime());
		fenceHis.setCreateTime(fence.getCreateTime());
		fenceHis.setUpdateTime(fence.getUpdateTime());
		fenceHis.setValid(fence.getValid());
		fenceHis.setDelTime(new Date());
		return fenceHis;
	}
	
	private List<FenceHis> getFenceHisList(List<Fence> fences ) throws Exception{
		if(fences==null || fences.size()==0){
			throw new Exception("围栏数组不能为空或者数量为0.");
		}
		List<FenceHis> fenceHisList = new ArrayList<>();
		for (Fence fence : fences) {
			FenceHis fh=getNewFenceHis(fence);
			fenceHisList.add(fh);
		}
		return fenceHisList;
	}
	
	private JSONObject resultJson(Integer code, Integer state, String desc) {
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
	
	private Integer type;// 操作类别
	private String obdMsnList;
	private Integer areaNum;// 操作类别
	private String points;
	private Integer alert;// 报警方式
	private Integer timerType;// 报警方式
	private String dayWeek;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getObdMsnList() {
		return obdMsnList;
	}

	public void setObdMsnList(String obdMsnList) {
		this.obdMsnList = obdMsnList;
	}

	public Integer getAreaNum() {
		return areaNum;
	}

	public void setAreaNum(Integer areaNum) {
		this.areaNum = areaNum;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Integer getAlert() {
		return alert;
	}

	public void setAlert(Integer alert) {
		this.alert = alert;
	}

	public Integer getTimerType() {
		return timerType;
	}

	public void setTimerType(Integer timerType) {
		this.timerType = timerType;
	}

	public String getDayWeek() {
		return dayWeek;
	}

	public void setDayWeek(String dayWeek) {
		this.dayWeek = dayWeek;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	
}
