package com.hgsoft.obd.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.entity.PositionInfo;
import com.hgsoft.carowner.service.PositionInfoService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.ThreadLocalDateUtil;
import com.hgsoft.jedis.JedisUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.server.ObdServerInit;
import com.hgsoft.obd.util.ObdInHostUtil;
import com.hgsoft.obd.util.ObdRedisStaticsUtil;
import com.hgsoft.obd.util.PushUtil;
import com.hgsoft.obd.util.TravelDataUtil;
import com.hgsoft.obd.util.WarnType;

/**
 * 测试OBD——监控
 * 
 * @author sujunguang 2016年1月8日 上午11:27:38
 */
@Controller
@Scope("prototype")
public class TestControlAction extends BaseAction {
	@Resource
	private JedisUtil jedisUtil;
	@Resource
	private TravelDataUtil travelDataUtil;
	@Resource
	private ObdInHostUtil obdInHostUtil;
	@Resource
	private PushUtil pushUtil;
	
	public String obdSn;
	private Map mapOBDChannel = GlobalData.OBD_CHANNEL;
	private Integer obdCount = GlobalData.OBD_CHANNEL.size();
	private Map connectedMap;
	private String connectTime;
	@Resource
	private PositionInfoService positionInfoService;
	private List<PositionInfo> positionInfos;
	@Resource
	private ObdRedisStaticsUtil obdRedisStaticsUtil;
	private Map<String,Integer[]> sendStatsMap;
	private String endDate;
	private Integer count;
	private Boolean onLine;
	private Integer onLineCount;
	private String operType;
	private String result;
	private String bossState;
	private String workerState;
	private String pwd;
	private String switchState;
	
	public String list(){
		Set<String> obdOnLineSet = obdRedisStaticsUtil.queryOBDOnLine();
		onLineCount = obdOnLineSet.size();
		if(!StringUtils.isEmpty(obdSn)){
			Map obdChannelMap = new HashMap<>();
			obdSn = obdSn.toLowerCase();
			if(mapOBDChannel.containsKey(obdSn)){
				obdChannelMap.put(obdSn, GlobalData.OBD_CHANNEL.get(obdSn));
			}
			mapOBDChannel = obdChannelMap;
			onLine = obdOnLineSet.contains(ObdRedisData.OBD_TTL_KEY+obdSn);
		}
		return "list";
	}
	
	public String connected(){
		//查询20个设备号
		//20个设备的连接时间
		pager.setPageSize("20");
		positionInfos = positionInfoService.findInsertTime(obdSn,connectTime,pager);
		return "connected";
	}
	 
	public String sendStats(){
		sendStatsMap = obdRedisStaticsUtil.querySendToObdStatics(endDate, count == null ?  10:count);
		System.out.println(sendStatsMap);
		return "sendStats";
	}
	
	public String redisData(){
		if(!StringUtils.isEmpty(operType)){
			switch (operType) {
			case "0":
				result = "在线时长:"+String.valueOf(jedisUtil.ttl(ObdRedisData.OBD_TTL_KEY+obdSn));
				break;
			case "1":
				result = "驾驶时间:"+jedisUtil.getHSet(ObdRedisData.OBD_LimitDriveTime_KEY+obdSn, ObdRedisData.OBD_LimitDriveTime_DriveTimeField+obdSn);
				result += ",休息时间:"+jedisUtil.getHSet(ObdRedisData.OBD_LimitDriveTime_KEY+obdSn, ObdRedisData.OBD_LimitDriveTime_RestTimeField+obdSn);
				break;
			case "2":
				result = "驾驶开始时间:"+jedisUtil.getHSet(ObdRedisData.OBD_DriveTime_KEY, ObdRedisData.OBD_DriveTime_DriveTimeField+obdSn)
					   + ",休息开始时间:"+jedisUtil.getHSet(ObdRedisData.OBD_DriveTime_KEY, ObdRedisData.OBD_DriveTime_RestTimeField+obdSn)
					   + ",正在驾驶ing:"+jedisUtil.getHSet(ObdRedisData.OBD_DriveTime_KEY, ObdRedisData.OBD_DriveTime_DrivingField+obdSn)
					   + ",正在休息ing:"+jedisUtil.getHSet(ObdRedisData.OBD_DriveTime_KEY, ObdRedisData.OBD_DriveTime_RestingField+obdSn)
					   + ",是否发生过疲劳:"+jedisUtil.getHSet(ObdRedisData.OBD_DriveTime_KEY, ObdRedisData.OBD_DriveTime_SendTriedField+obdSn)
					   + ",是否进入休眠:"+jedisUtil.getHSet(ObdRedisData.OBD_DriveTime_KEY, ObdRedisData.OBD_DriveTime_EnterSleepDateField+obdSn)
					   ;
				break;
			case "3":
				result = "";
				int i = 0;
				for(WarnType warnType : WarnType.values()){
					result += warnType+"="+pushUtil.getPushByRedis(obdSn+"_"+warnType)+" , ";
					i++;
					if(i%5 == 0)
						result += "\n";
				}
				result = result.substring(0, result.length()-2);
				break;
			case "4":
				result = "上次上电号:"+travelDataUtil.getUpElectricNo(obdSn);
				break;
			case "5":
				result = "行程开始时间:"+travelDataUtil.getTravelStart(obdSn)
						 + ",行程结束时间:"+travelDataUtil.getTravelEnd(obdSn)
						 + ",行程里程:"+travelDataUtil.getTravelMile(obdSn)
						 + ",行程油耗:"+travelDataUtil.getTravelOil(obdSn);
				break;
			case "6":
				result = "设备当前连接主机:"+obdInHostUtil.getObdInHost(obdSn);
				break;
			case "7":
				result = "设备最新位置数据:"+jedisUtil.getHSet(ObdRedisData.OBD_LastPosition_KEY,obdSn);
				break;
			case "8":
				result = "设备最新初始化数据:"+jedisUtil.getHSet(ObdRedisData.OBD_LastHandShake_KEY,obdSn);
				break;
			case "9":
				Date date = new Date();
				date.setTime(Long.valueOf(jedisUtil.getHSet(ObdRedisData.OBD_LastOnLine_KEY,obdSn)));
				result = "设备最新上线大概时间:"+ThreadLocalDateUtil.formatDate("yyyy-MM-dd HH:mm:ss", date);

				break;
			case "10":
				result = "请求离线数据时长:"+String.valueOf(jedisUtil.ttl(ObdRedisData.OffLineData_TTL+obdSn));
				
				break;

			default:
				break;
			}
		}
		return "redis";
	}

	public String nettyData(){
		try {
			if(!StringUtils.isEmpty(switchState)){
				switch (switchState) {
				case "0"://关闭
					ObdServerInit.close();
					result = "关闭成功！";
					break;
				case "1"://开启
					ObdServerInit.start();
					result = "开启成功！";
					break;
				default:
					result = "异常！状态为："+switchState;
					break;
				}
			}
			bossState = ObdServerInit.bossState()? "关闭" : "开启";
			workerState = ObdServerInit.wokerState()? "关闭" : "开启";
		}catch (Exception e) {
			e.printStackTrace();
			result = "异常："+e;
		}
		return "netty";
	}

 	public String getObdSn() {
		return obdSn;
	}
	public Integer getObdCount() {
		return obdCount;
	}
	public void setObdCount(Integer obdCount) {
		this.obdCount = obdCount;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public Map getMapOBDChannel() {
		return mapOBDChannel;
	}
	public void setMapOBDChannel(Map mapOBDChannel) {
		this.mapOBDChannel = mapOBDChannel;
	}
	public Map getConnectedMap() {
		return connectedMap;
	}
	public void setConnectedMap(Map connectedMap) {
		this.connectedMap = connectedMap;
	}
	public String getConnectTime() {
		return connectTime;
	}
	public void setConnectTime(String connectTime) {
		this.connectTime = connectTime;
	}
	public List<PositionInfo> getPositionInfos() {
		return positionInfos;
	}
	public void setPositionInfos(List<PositionInfo> positionInfos) {
		this.positionInfos = positionInfos;
	}
	public Map<String, Integer[]> getSendStatsMap() {
		return sendStatsMap;
	}
	public void setSendStatsMap(Map<String, Integer[]> sendStatsMap) {
		this.sendStatsMap = sendStatsMap;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Boolean getOnLine() {
		return onLine;
	}
	public void setOnLine(Boolean onLine) {
		this.onLine = onLine;
	}
	public Integer getOnLineCount() {
		return onLineCount;
	}
	public void setOnLineCount(Integer onLineCount) {
		this.onLineCount = onLineCount;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getBossState() {
		return bossState;
	}
	public void setBossState(String bossState) {
		this.bossState = bossState;
	}
	public String getWorkerState() {
		return workerState;
	}
	public void setWorkerState(String workerState) {
		this.workerState = workerState;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getSwitchState() {
		return switchState;
	}
	public void setSwitchState(String switchState) {
		this.switchState = switchState;
	}
 
}
