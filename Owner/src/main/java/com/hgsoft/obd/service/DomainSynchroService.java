package com.hgsoft.obd.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.DomainState;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.service.DomainStateService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.util.ExtensionDataQueryType;
import com.hgsoft.obd.util.ExtensionDataSetType;
import com.hgsoft.obd.util.SettingType;

import net.sf.json.JSONObject;

/**
 * 域名单同步 传obdSn和domainState type:3,4,6,7 先查询obd对应类型的数据
 * 如果数据跟后台的不一致的话，下发指令对obd进行增删，保持跟服务器上的一致
 * 
 * @author 2016年1月22日 下午3:55:31
 */
@Service
public class DomainSynchroService {
	private static Logger serverSendObdLogger = LogManager.getLogger("serverSendObdLogger");

	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private ObdSettingService obdSettingService;
	@Resource
	private DomainStateService domainStateService;

	final int domainTotal = new Integer(PropertiesUtil.getInstance("params.properties").readProperty("domainTotal"));// 域名总个数20

	/**
	 * 3增加多个域白名单; 查询obd的白名单 判断总数是否跟服务器一致，如果不一致下发指令 判断名单是否一致，如果不一致下发指令
	 * 判断离线的总记录数和服务器已设置成功的白名单总记录数加起来是否超过20 判断离线的总记录数和obd已设置成功的白名单总记录数加起来是否超过20
	 * 
	 * 服务器有，obd没有
	 * obd有，服务器没有
	 * obd重复N个,先删除
	 * 下发N-1条删除指令
	 * 
	 * 先删除重复，下发N-1条删除指令
	 * 删除服务器没有，obd有的域名
	 * 服务器有，obd没有，同步域名
	 * 
	 * @param type
	 * @param domainState
	 * @return 域黑名单，结果：m.baidu.com;m.zhihu.com;m.zol.com.cn;3g.163.com
	 */
	public void type3(String obdSn) {
		serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
		if(!"01".equals(obdStockInfo.getStockState())){
			serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn+"---设备离线");
			return;
		}
		boolean synflag = true;//是否同步成功全局变量
		
		// 查询DomainState记录
		DomainState domainState = domainStateService.queryByObdSn(obdSn);
		String result = null;// obd白名单集合字符串
		List<String> obdWhite = new ArrayList<>();// obd的白名单,可能存在重复
		List<String> serverWhite = new ArrayList<>();// 服务器的白名单,不会重复

		Set<String> serverHasObdNot = new HashSet<>();// 服务器的白名单,obd没有——》下发新增
		Set<String> serverNotObdHas = new HashSet<>();//服务器没有的白名单,obd有 
		Map<String,Integer> obdRepeat = new HashMap<>();//obd域名重复次数
		
		try {
			// 1.先保证服务器已设置成功的白名单总数+离线设置的白名单总数不超过20,如果超过20的话，将这些离线设置都置为无效。
			if (domainState != null && !StringUtils.isEmpty(domainState.getWhiteList())) {
				String whiteListStr = domainState.getWhiteList();
				JSONObject json = JSONObject.fromObject(whiteListStr);
				Set<String> set= (Set<String>) json.keySet();
				serverWhite = new ArrayList<>(set);// 服务器白名单域名
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---已设置成功的白名单:" + serverWhite.toString());
			}

			// 2.再查询obd白名单
			result = (String) serverRequestQueryService.extension2Data(obdSn, ExtensionDataQueryType.DomainWhite, 0, 4);
			Thread.sleep(10000);// 等待10秒,如果中间发生异常,则直接捕获异常
			result = GlobalData.getQueryDataMap(obdSn);
			serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---obd域白名单:" + result);
			//--------------------
			if (!StringUtils.isEmpty(result)) {
				// 先判断总数是否一致,如果不一致下发指令
				obdWhite = Arrays.asList(result.split(";"));// obd的白名单域名
			}

			boolean delFlag = false;// 下发清空指令
			boolean sendFlag = false;// 下发服务器的状态给obd

			// 如果服务器为空，obd不为空
			if (serverWhite.size() == 0 && !StringUtils.isEmpty(result)) {
				delFlag = true;
			}

			//不知道基于什么原因导致取到的结果null
			if (serverWhite.size() >= 0 && StringUtils.isEmpty(result)) {
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---obd域白名单为空,因为不知道是基于什么原因为空,所以不需要同步.");
//				synflag = false;
			}

			if (!delFlag && serverWhite.size() >= 0 && !StringUtils.isEmpty(result)) {
				boolean whiteFlag = StrUtil.compare(obdWhite, serverWhite);//判断两个集合是否元素都相同
				if (!whiteFlag) {
					serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---obd域白名单和服务器的白名单不同:"+ serverWhite + "---" + result);
					sendFlag = true;
					obdRepeat = listToMap(obdWhite);//找出obd重复的记录
					serverHasObdNot = list1NotInList2(serverWhite, obdWhite);//服务器有，obd没有
					serverNotObdHas = list1NotInList2(obdWhite, serverWhite);//obd有,服务器没有
				}
			}

			if (delFlag) {
				// 下发清空指令
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---服务器域白名单为空,OBD不为空,则下发删除所有白名单指令给obd.");
				boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, "00");
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---删除所有域白名单下发报文设置结果:" + obdSetRes);
				if(!obdSetRes){
					serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---删除所有域白名单下发结果失败,需要下次再次同步." );
					synflag = false;
				}
			}

			if (sendFlag) {
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---obd域白名单和服务器的白名单不同:"+ serverWhite + "---" + result);
				//先删除重复的
				if(obdRepeat!=null && !obdRepeat.isEmpty()){
					for(String key : obdRepeat.keySet()){
						Integer repeatTotal = obdRepeat.get(key);
						for (int i = 0; i < repeatTotal-1; i++) {
							boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, key);
							serverSendObdLogger.info("----------【域黑白名单设置】域白名单---"+obdSn+"---删除单条域白名单结果:"+obdSetRes+"---域名:"+key);
							if(!obdSetRes){
								serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---删除重复单条域白名单失败,需要下次再次同步." );
								synflag = false;
							}
						}
					}
				}
				
				//删除服务器没有,obd有的
				if(serverNotObdHas!=null && !serverNotObdHas.isEmpty()){
					for (String string : serverNotObdHas) {
						boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelWhite, string);
						serverSendObdLogger.info("----------【域黑白名单设置】域白名单---"+obdSn+"---删除服务器没有obd有的结果:"+obdSetRes+"---域名:"+ string);
						if(!obdSetRes){
							serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---" + obdSn + "---删除服务器没有obd有的结果失败,需要下次再次同步." );
							synflag = false;
						}
					}
				}
				
				//下发：服务器有，obd没有的，且每次最多下发5条
				if(serverHasObdNot!=null && !serverHasObdNot.isEmpty()){
					String serverHasObdNotString=SetToString(serverHasObdNot);
					List<String> domainList = stringToList(serverHasObdNotString);
					for (String string : domainList) {
						//下发最多5个域名
						boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainAddWhite, string);
						serverSendObdLogger.info("----------【域黑白名单设置】域白名单----------"+obdSn+"---增加服务器有obd没有域白名单结果:"+obdSetRes);
						if(!obdSetRes){
							serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单----------" + obdSn + "---增加服务器有obd没有域白名单结果失败,需要下次再次同步." );
							synflag = false;
						}
					}
					
				}
			}
			
			if(synflag){
				int whiteSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_00.getValue());
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单----------"+obdSn+"---将同步记录置为无效总数:"+whiteSyntotal);
			}

		} catch (Exception e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单---------" + obdSn + "---异常:" + e);
		}
		// 查询成功，域名单可能存在重复

	}

	/**
	 * ——————》方法弃用
	 * 
	 * 删除单个白名单 查询obd白名单——》如果有,则不管,如果没有 查询服务器白名单,——》如果有,则删除
	 * 
	 * @param obdSn
	 * @param domainState
	 */
	public void type4(String obdSn, String settingId) {
		serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单删除---------" + obdSn);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
		if(!"01".equals(obdStockInfo.getStockState())){
			serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单删除---------" + obdSn+"---设备离线");
			return;
		}
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);
		ObdSetting obdSetting = obdSettingService.find(settingId);
		if(obdSetting==null || "0".equals(obdSetting.getValid())){
			return;
		}
		serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单删除---------" + obdSn + "---待删除记录" + obdSetting + "---域名单记录:" + domainState);
		String result = null;// obd白名单集合字符串
		String[] obdWhite = null;// obd的白名单
		String[] serverWhite = null;// 服务器的白名单
		// Set<String> serverWhiteSet = new HashSet<>();
		String domainUrl = "";
		try {
			domainUrl = JSONObject.fromObject(obdSetting.getSettingMsg()).getString("domainName");// 待删除的

			if (domainState != null && !StringUtils.isEmpty(domainState.getWhiteList())) {
				String whiteListStr = domainState.getWhiteList();
				JSONObject json = JSONObject.fromObject(whiteListStr);
				Set<String> whiteSet = (Set<String>) json.keySet();
				serverWhite = whiteSet.toArray(new String[whiteSet.size()]);// 服务器白名单域名
				serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单删除---------" + obdSn + "---已设置成功的白名单:" + whiteSet);
			}

			// 查询obd白名单
			result = (String) serverRequestQueryService.extension2Data(obdSn, ExtensionDataQueryType.DomainWhite, 0, 4);
			Thread.sleep(10000);// 等待10秒,如果中间发生异常,则直接捕获异常
			result = GlobalData.getQueryDataMap(obdSn);
			serverSendObdLogger.info("----------【域黑白名单状态同步】域白名单删除---------" + obdSn + "---obd域白名单:" + result);

			//如果obd中不存在,则删除
			boolean flag = true;
			if (!StringUtils.isEmpty(result)) {
				obdWhite = result.split(";");
				flag = StrUtil.strArrayExist(obdWhite, domainUrl);
			}else{
				flag = false;
			}
			//可能存在情况:查询返回为空，实际obd是存在这个域名,不管
			
			if (!flag) {
				if (serverWhite != null && serverWhite.length > 0) {
					// 2更新domainSstate表
					String wList = domainState.getWhiteList();
					JSONObject jobj = JSONObject.fromObject(wList);
					if (jobj.containsKey(domainUrl)) {
						jobj.remove(domainUrl);
						domainState.setWhiteList(jobj.toString());
						domainState.setUpdateTime(new Date());
						boolean dsflag = domainStateService.domainStateSave(domainState);
						serverSendObdLogger.info("----------【域黑白名单设置】域白名单删除----------" + obdSn + "---域名单设置修改结果:" + dsflag + "---" + domainState);
					}
				}
			}

			// 将离线设置置为无效
			if (!flag) {
				serverSendObdLogger.info("----------【域黑白名单设置】域白名单删除----------" + obdSn + "---obd不存在该白名单,服务器删除该离线设置." + obdSetting);
				obdSetting.setValid("0");
				obdSetting.setUpdateTime(new Date());
				boolean ff = obdSettingService.obdSettingSave(obdSetting);
				serverSendObdLogger
						.info("----------【域黑白名单设置】域白名单删除----------" + obdSn + "---obd不存在该白名单,服务器删除该离线设置结果:" + ff);
			}
		} catch (Exception e) {
			serverSendObdLogger.info("----------【域黑白名单设置】域白名单删除----------" + obdSn + "---异常信息:" + e);
		}

	}

	/**
	 * 3增加多个域黑名单; 查询obd的黑名单 判断总数是否跟服务器一致，如果不一致下发指令 判断名单是否一致，如果不一致下发指令
	 * 判断离线的总记录数和服务器已设置成功的黑名单总记录数加起来是否超过20 判断离线的总记录数和obd已设置成功的黑名单总记录数加起来是否超过20
	 * 
	 * 服务器有，obd没有
	 * obd有，服务器没有
	 * obd重复N个,先删除
	 * 下发N-1条删除指令
	 * 
	 * 先删除重复，下发N-1条删除指令
	 * 删除服务器没有，obd有的域名
	 * 服务器有，obd有，obd没有，同步域名
	 * 
	 * @param type
	 * @param domainState
	 * @return 域黑名单，结果：m.baidu.com;m.zhihu.com;m.zol.com.cn;3g.163.com
	 */
	public void type6(String obdSn) {
		serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
		if(!"01".equals(obdStockInfo.getStockState())){
			serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn+"---设备离线");
			return;
		}
		boolean synflag = true;//是否同步成功全局变量
		
		// 查询DomainState记录
		DomainState domainState = domainStateService.queryByObdSn(obdSn);
		String result = null;// obd黑名单集合字符串
		List<String> obdBlack = new ArrayList<>();// obd的黑名单,可能存在重复
		List<String> serverBlack = new ArrayList<>();// 服务器的黑名单,不会重复

		Set<String> serverHasObdNot = new HashSet<>();// 服务器的黑名单,obd没有——》下发新增
		Set<String> serverNotObdHas = new HashSet<>();//服务器没有的黑名单,obd有 
		Map<String,Integer> obdRepeat = new HashMap<>();//obd域名重复次数

		try {
			// 1.先保证服务器已设置成功的黑名单总数+离线设置的黑名单总数不超过20,如果超过20的话，将这些离线设置都置为无效。
			if (domainState != null && !StringUtils.isEmpty(domainState.getWhiteList())) {
				String blackListStr = domainState.getBlackList();
				JSONObject json = JSONObject.fromObject(blackListStr);
				Set<String> set= (Set<String>) json.keySet();
				serverBlack = new ArrayList<>(set);// 服务器黑名单域名
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---已设置成功的黑名单总数:" + serverBlack.toString());
			}

			// 2.再查询obd黑名单
			result = (String) serverRequestQueryService.extension2Data(obdSn, ExtensionDataQueryType.DomainBlack, 0, 4);
			Thread.sleep(10000);// 等待10秒,如果中间发生异常,则直接捕获异常
			result = GlobalData.getQueryDataMap(obdSn);
			serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---------" + obdSn + "---obd域黑名单:" + result);
			//-------------------------------------
			
			if (!StringUtils.isEmpty(result)) {
				// 先判断总数是否一致,如果不一致下发指令
				obdBlack = Arrays.asList(result.split(";"));// obd的黑名单域名
			}

			boolean delFlag = false;// 下发清空指令
			boolean sendFlag = false;// 下发服务器的状态给obd

			// 如果服务器为空，obd不为空
			if (serverBlack.size() == 0 && !StringUtils.isEmpty(result)) {
				delFlag = true;
			}

			//不知道基于什么原因导致取到的结果null
			if (serverBlack.size() >= 0 && StringUtils.isEmpty(result)) {
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---obd域黑名单为空,不知道是什么原因，所以不同步.");
			}

			if (!delFlag && serverBlack.size() >= 0 && !StringUtils.isEmpty(result)) {
				boolean blackFlag = StrUtil.compare(obdBlack, serverBlack);//判断两个集合是否元素都相同
				if (!blackFlag) {
					serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---obd域黑名单和服务器的白名单不同:"+ serverBlack + "---" + result);
					sendFlag = true;
					obdRepeat = listToMap(obdBlack);//找出obd重复的记录
					serverHasObdNot = list1NotInList2(serverBlack, obdBlack);//服务器有，obd没有
					serverNotObdHas = list1NotInList2(obdBlack, serverBlack);//obd有,服务器没有
				}
			}

			if (delFlag) {
				// 下发清空指令
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---服务器域黑名单为空,OBD不为空,则下发删除所有黑名单指令给obd.");
				boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, "00");
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---删除所有域黑名单下发报文设置结果:" + obdSetRes);
				if(!obdSetRes){
					serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---删除所有域黑名单下发结果失败,需要下次再次同步." );
					synflag = false;
				}
			
			}

			if (sendFlag) {
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---obd域黑名单和服务器的黑名单不同:"+ serverBlack + "---" + result);
				//先删除重复的
				if(obdRepeat!=null && !obdRepeat.isEmpty()){
					for(String key : obdRepeat.keySet()){
						Integer repeatTotal = obdRepeat.get(key);
						for (int i = 0; i < repeatTotal-1; i++) {
							boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, key);
							serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---"+obdSn+"---删除单条域黑名单结果:"+obdSetRes+"---域名:"+key);
							if(!obdSetRes){
								serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---删除重复域黑名单失败,需要下次再次同步." );
								synflag = false;
							}
						}
					}
				}
				
				//删除服务器没有,obd有的
				if(serverNotObdHas!=null && !serverNotObdHas.isEmpty()){
					for (String string : serverNotObdHas) {
						boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainDelBlack, string);
						serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---"+obdSn+"---删除服务器有obd没有域黑名单结果:"+obdSetRes+"---域名:"+ string);
						if(!obdSetRes){
							serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---删除服务器有obd没有域黑名单失败,需要下次再次同步." );
							synflag = false;
						}
					}
				}
				
				//下发：服务器有，obd没有的，且每次最多下发5条-->新增
				if(serverHasObdNot!=null && !serverHasObdNot.isEmpty()){
					String serverHasObdNotString=SetToString(serverHasObdNot);
					List<String> domainList = stringToList(serverHasObdNotString);
					for (String string : domainList) {
						//下发最多5个域名
						boolean obdSetRes =domainSet(obdSn, ExtensionDataSetType.DomainAddBlack, string);
						serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---"+obdSn+"---下发服务器有obd没有结果:"+obdSetRes);
						if(!obdSetRes){
							serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---" + obdSn + "---下发服务器有obd没有失败,需要下次再次同步." );
							synflag = false;
						}
					}
					
				}
			}
			
			if(synflag){
				int blackSyntotal = settingUnuseful(obdSn, SettingType.DOMAINSYN_01.getValue());
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单----------"+obdSn+"---将同步记录置为无效总数:"+blackSyntotal);
			}

		} catch (Exception e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单---------" + obdSn + "---异常:" + e);
		}

	}


	/**
	 * ——————》方法弃用
	 * 删除单个黑名单 查询obd黑名单——》如果有,则不管,如果没有 查询服务器黑名单,——》如果有,则删除
	 * 
	 * @param obdSn
	 * @param domainState
	 */
	public void type7(String obdSn, String settingId) {
		serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单删除---------" + obdSn);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
		if(!"01".equals(obdStockInfo.getStockState())){
			serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单删除---------" + obdSn+"---设备离线");
			return;
		}
		
		DomainState domainState = domainStateService.queryByObdSn(obdSn);
		ObdSetting obdSetting = obdSettingService.find(settingId);
		if(obdSetting==null || "0".equals(obdSetting.getValid())){
			return;
		}
		serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单删除---------" + obdSn + "---待删除记录" + obdSetting + "---域名单记录:" + domainState);
		String result = null;// obd黑名单集合字符串
		String[] obdBlack = null;// obd的黑名单
		String[] serverBlack = null;// 服务器的黑名单
		// Set<String> serverWhiteSet = new HashSet<>();
		String domainUrl = "";
		try {
			domainUrl = JSONObject.fromObject(obdSetting.getSettingMsg()).getString("domainName");// 待删除的

			if (domainState != null && !StringUtils.isEmpty(domainState.getBlackList())) {
				String blackListStr = domainState.getBlackList();
				JSONObject json = JSONObject.fromObject(blackListStr);
				Set<String> blackSet = (Set<String>) json.keySet();
				serverBlack = blackSet.toArray(new String[blackSet.size()]);// 服务器黑名单域名
				serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单删除---------" + obdSn + "---已设置成功的黑名单:" + blackSet);
			}

			// 查询obd黑名单
			result = (String) serverRequestQueryService.extension2Data(obdSn, ExtensionDataQueryType.DomainBlack, 0, 4);
			Thread.sleep(10000);// 等待10秒,如果中间发生异常,则直接捕获异常
			result = GlobalData.getQueryDataMap(obdSn);
			serverSendObdLogger.info("----------【域黑白名单状态同步】域黑名单删除---------" + obdSn + "---obd域黑名单:" + result);
			
			boolean flag = true;
			if (!StringUtils.isEmpty(result)) {
				obdBlack = result.split(";");
				flag = StrUtil.strArrayExist(obdBlack, domainUrl);
			} else {
				flag = false;
			}

			if (!flag) {
				if (serverBlack != null && serverBlack.length > 0) {
					// 2更新domainSstate表
					String bList = domainState.getBlackList();
					JSONObject jobj = JSONObject.fromObject(bList);
					if (jobj.containsKey(domainUrl)) {
						jobj.remove(domainUrl);
						domainState.setBlackList(jobj.toString());
						domainState.setUpdateTime(new Date());
						boolean dsflag = domainStateService.domainStateSave(domainState);
						serverSendObdLogger.info("----------【域黑白名单设置】域黑名单删除----------" + obdSn + "---域名单设置修改结果:" + dsflag + "---" + domainState);
					}
				}
			}

			// 将离线设置置为无效
			if (!flag) {
				serverSendObdLogger.info("----------【域黑白名单设置】域黑名单删除----------" + obdSn + "---obd不存在该黑名单,服务器删除该离线设置." + obdSetting);
				obdSetting.setValid("0");
				obdSetting.setUpdateTime(new Date());
				boolean ff = obdSettingService.obdSettingSave(obdSetting);
				serverSendObdLogger.info("----------【域黑白名单设置】域黑名单删除----------" + obdSn + "---obd不存在该黑名单,服务器删除该离线设置结果:" + ff);
			}
		} catch (Exception e) {
			serverSendObdLogger.info("----------【域黑白名单设置】域黑名单删除----------" + obdSn + "---异常信息:" + e);
		}

	}

	private String SetToString(Set<String> set) {
		StringBuffer sb = new StringBuffer();
		for (String string : set) {
			sb.append(string + ";");
		}
		return StrUtil.stringCutLastSub(sb.toString(), ";");
	}
	
	private String listToString(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string + ";");
		}
		return StrUtil.stringCutLastSub(sb.toString(), ";");
	}
	
	/**
	 * 找出List集合中的重复记录和重复次数
	 * @param list
	 * @return
	 */
	private Map<String,Integer> listToMap(List<String> list){
		Map<String, Integer> map = new HashMap<>();
		for (String string : list) {
			if(map.containsKey(string)){
				map.put(string, map.get(string)+1);
			}else{
				map.put(string, 1);
			}
		}
		Set<String> set = new HashSet<>();
		for (String string : map.keySet()) {
			Integer total = map.get(string);
			if(total==1){
				set.add(string);
			}
		}
		for (String string : set) {
			map.remove(string);
		}
		return map;
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
	
	/**
	 * String str ="1;2;3;4;5;6;7;8;9";
	 * @return 1;2;3;4;5和6;7;8;9的集合
	 */
	private List<String> stringToList(String str) {
		String[] strArr = str.split(";");
		List<String> strList = new ArrayList<>();
		if(strArr.length<5){
			strList.add(str);
			return strList;
		}
		int temp = 1;
		String strTemp = "";
		for (int i=0; i< strArr.length; i++) {
			strTemp+=strArr[i]+";";
			if(temp % 5 == 0){
				strTemp = StrUtil.stringCutLastSub(strTemp, ";");
				strList.add(strTemp);
				strTemp="";
			}
			if(temp == strArr.length){
				strTemp = StrUtil.stringCutLastSub(strTemp, ";");
				strList.add(strTemp);
				break;
			}
			temp ++;
		}
		return strList;
	}
	
	private JSONObject getJsonObject(String jsonStr){
		try {
			JSONObject jobj = null;
			if(!StringUtils.isEmpty(jsonStr)){
				jobj = JSONObject.fromObject(jsonStr);
			}else{
				jobj = new JSONObject();
			}
			return jobj;
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
	/**
	 * list1中有，list2没有
	 * @param list1
	 * @param list2
	 * @return
	 */
	private Set<String> list1NotInList2(List<String> list1,List<String> list2) {
		Set<String> set = new HashSet<>();
		for(String string : list1){
			boolean flag = true;
			for (String string2 : list2) {
				if(string.equals(string2)){
					flag = false;
					break;
				}
			}
			if(flag){
				set.add(string);
			}
		}
		return set;
	}
	
	private Integer settingUnuseful(String obdSn,String...type){
		Map<String, Integer> types = new HashMap<>();
		for (String string : type) {
			types.put(string, 1);
		}
		int total=obdSettingService.setNoValidByInType(obdSn, types);
		return total;
	}
	
}
