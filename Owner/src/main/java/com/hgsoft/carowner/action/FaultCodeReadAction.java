/**
 * 
 */
package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.PositionalInformation;
import com.hgsoft.carowner.entity.ServerSet;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.carowner.service.BlindPointClearService;
import com.hgsoft.carowner.service.CarParamQueryService;
import com.hgsoft.carowner.service.CarParamSetService;
import com.hgsoft.carowner.service.FangDaoService;
import com.hgsoft.carowner.service.FaultCodeClearService;
import com.hgsoft.carowner.service.FaultCodeReadService;
import com.hgsoft.carowner.service.MonitorService;
import com.hgsoft.carowner.service.MsgBatchReqService;
import com.hgsoft.carowner.service.OBDPositionService;
import com.hgsoft.carowner.service.OBDRecoveryService;
import com.hgsoft.carowner.service.OBDRestartService;
import com.hgsoft.carowner.service.OBDStudyService;
import com.hgsoft.carowner.service.ParamSetService;
import com.hgsoft.carowner.service.ProtalService;
import com.hgsoft.carowner.service.RemoteUpgradeService;
import com.hgsoft.carowner.service.ServerSetService;
import com.hgsoft.carowner.service.UpdateDataService;
import com.hgsoft.carowner.service.WiFiSetService;
import com.hgsoft.common.action.BaseAction;

/**
 * @author liujialin
 * 手机用户登录系统，通过ip和端口与对应的OBD设备连接，发送请求消息
 */
@Controller
@Scope("prototype")
public class FaultCodeReadAction extends BaseAction {
	private final Log logger = LogFactory.getLog(FaultCodeReadAction.class);

	@Resource
	FaultCodeReadService faultCodeReadService;
	@Resource
	FangDaoService fangDaoService;
	@Resource
	ParamSetService paramSetService;
	@Resource
	OBDPositionService oBDPositionService;
	@Resource
	MonitorService monitorService;
	@Resource
	FaultCodeClearService faultCodeClearService;
	@Resource
	MsgBatchReqService msgBatchReqService;
	@Resource
	OBDRestartService oBDRestartService;
	@Resource
	OBDStudyService oBDStudyService;
	@Resource
	BlindPointClearService blindPointClearService;
	@Resource
	OBDRecoveryService oBDRecoveryService;
	@Resource
	WiFiSetService wiFiSetService;
	@Resource
	ServerSetService serverSetService;
	@Resource
	CarParamSetService carParamSetService;
	@Resource
	CarParamQueryService carParamQueryService;
	@Resource
	UpdateDataService updateDataService;
	@Resource
	RemoteUpgradeService remoteUpgradeService;
	@Resource
	ProtalService protalService;
	public String list(){
		//1.获取当前登录用户id或手机号码或绑定的设备号码
		
		return "list";
	}
	
	public String list2(){
		//1.获取当前登录用户id或手机号码或绑定的设备号码
		
		return "list2";
	}
	
	
	
	public String portal0(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		System.out.println(obdId);
		Portal p = new Portal();
		p.setObdSn(obdId);
		p.setType("0");
		p.setUrl("http://obd.gd118114.cn/html/portal/auth.jsp?");
		boolean aa =protalService.protalSet(p);
		logger.info(obdId+"********************Protal设置URL返回消息啦:"+aa);
		return "list";
	}
	
	public String portal2(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		System.out.println(obdId);
		Portal p = new Portal();
		p.setObdSn(obdId);
		p.setType("2");
		p.setMac("ff:ff:ff:ff:ff:ff");
		p.setMb("50");
		boolean aa =protalService.protalSet(p);
		logger.info(obdId+"********************Protal流量额度限制返回消息啦:"+aa);
		return "list";
	}
	
	public String portal3(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		System.out.println(obdId);
		Portal p = new Portal();
		p.setObdSn(obdId);
		p.setType("3");
		p.setWhitelists("ff:ff:ff:ff:ff:ff|ee:ee:ee:ee:ee:ee|22:22:22:22:22:22");
//		p.setWhitelists("ff:ff:ff:ff:ff:ff");
		boolean aa =protalService.protalSet(p);
		logger.info(obdId+"********************Protal白名单设置返回消息啦:"+aa);
		return "list";
	}
	
	public String portal4(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		System.out.println(obdId);
		Portal p = new Portal();
		p.setObdSn(obdId);
		p.setType("4");
		boolean aa =protalService.protalSet(p);
		logger.info(obdId+"********************Protal全部删除白名单返回消息啦:"+aa);
		return "list";
	}
	
	public String portal5(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		System.out.println(obdId);
		Portal p = new Portal();
		p.setObdSn(obdId);
		p.setType("5");
		p.setMac("22:22:22:22:22:22");
		boolean aa =protalService.protalSet(p);
		logger.info(obdId+"********************Protal单跳删除白名单返回消息啦:"+aa);
		return "list";
	}
	

	public String portal6(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		String onOff = request.getParameter("onOff");
		System.out.println(onOff);
		Portal p = new Portal();
		p.setObdSn(obdId);
		p.setType("6");
		p.setOnOff(onOff);
		boolean aa =protalService.protalSet(p);
		logger.info(obdId+"********************Protal开关返回消息啦:"+aa);
		return "list";
	}
	
	public String readMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdId = request.getParameter("obdId");
		System.out.println(obdId);
		String aa =faultCodeReadService.faultCodeRead(obdId);
		logger.info(aa == null);
		logger.info("读取故障码返回消息*******************");
		logger.info(aa+"********************读取故障码返回消息啦");
		return "list";
	}
	public String respMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		System.out.println(obdSn);
		String str=fangDaoService.fangdaoSet(obdSn, "01");
		System.out.println(str+"**********************返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String paramSet(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		System.out.println(obdSn);
		String str=paramSetService.paramSet(obdSn, 1, "02", "116°04.000'", "33°32.0000'", "108°01.000'", "12°45.0000'");
		System.out.println(str+"**********************参数设置电子围栏返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String obdPosition(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		System.out.println(obdSn);
		PositionalInformation pi=oBDPositionService.OBDPosition(obdSn);
		if(pi!=null){
			System.out.println(pi.getLongitude()+"**********************点名返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	public String monitor(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		System.out.println(obdSn);
		String phone = "13512345678";
		String str=monitorService.monitor(obdSn,phone);
		System.out.println(str+"**********************监听返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String faultCodeClear(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String str=faultCodeClearService.faultCodeClear(obdSn);
		System.out.println(str+"**********************清除故障码返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String msgReq() throws InterruptedException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
//		Date d1 = new Date();
//		Thread.sleep(2000);
//		Date d2 = new Date();
		String str=msgBatchReqService.msgBatchReq(obdSn);
		System.out.println(str+"**********************请求批量信息返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	
	public String restart() throws InterruptedException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String str=oBDRestartService.OBDRestart(obdSn);
		System.out.println(str+"**********************重启终端返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String OBDStudy(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String str=oBDStudyService.obdStudy(obdSn);
		System.out.println(str+"**********************obd安装位置学习返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String blindPointClear(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String str=blindPointClearService.blindPointClear(obdSn);
		System.out.println(str+"**********************清除盲点返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String OBDRecovery(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String str=oBDRecoveryService.OBDRecovery(obdSn);
		if(str!=null){
			System.out.println(str+"**********************参数恢复出厂设置返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	public String WIFI1() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		
		//打开
		Wifi wifi = new Wifi();
		wifi.setObdSn(obdSn);
		String str=wiFiSetService.WiFiSet("04",wifi);
		if(str!=null){
			System.out.println(str+"**********************wifi打开返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	
	public String WIFI2() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		//恢复出厂设置
		Wifi wifi = new Wifi();
		wifi.setObdSn(obdSn);
		String str=wiFiSetService.WiFiSet("05",wifi);
		if(str!=null){
			System.out.println(str+"**********************wifi关闭返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	
	public String WIFI3() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
//		String str=wiFiSetService.WiFiSet(obdSn, "01", "123456");//ssid
//		String str=wiFiSetService.WiFiSet(obdSn, "03", null);//ssid
//		String str=wiFiSetService.WiFiSet(obdSn, "04", null);
//		//wifi密码
//		Wifi wifi = new Wifi();
//		wifi.setObdSn(obdSn);
//		wifi.setAutu("5");
//		wifi.setEncrypt("3");
//		wifi.setWifiPwd("777777777");
//		String str=wiFiSetService.WiFiSet("02",wifi);
//		//ssid
		Wifi wifi = new Wifi();
		wifi.setObdSn(obdSn);
		wifi.setSsid("123456");
		String str=wiFiSetService.WiFiSet("01",wifi);
		
	
		if(str!=null){
			System.out.println(str+"**********************wifi的ssid返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	
	public String WIFI4() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		Wifi wifi = new Wifi();
		wifi.setObdSn(obdSn);
		wifi.setAutu("5");
		wifi.setEncrypt("3");
		wifi.setWifiPwd("777777777");
		String str=wiFiSetService.WiFiSet("02",wifi);
	
		if(str!=null){
			System.out.println(str+"**********************wifi的密码返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	
	public String WIFI5() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		Wifi wifi = new Wifi();
		wifi.setObdSn(obdSn);
		wifi.setSsid("123456");
		String str=wiFiSetService.WiFiSet("03",wifi);
	
		if(str!=null){
			System.out.println(str+"**********************wifi的恢复出厂设置返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	
	public String server(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String setType = request.getParameter("setType");
		String serverType = request.getParameter("serverType");
		String port = request.getParameter("port");
		String ip = request.getParameter("ip");
		String APN = request.getParameter("APN");
		ServerSet serverSet = new ServerSet();
		serverSet.setObdSn(obdSn);
		serverSet.setSetType(setType);
		serverSet.setServerType(serverType);
		serverSet.setPort(port);
		serverSet.setIp(ip);
		serverSet.setApn(APN);
		boolean flag=serverSetService.serverSetList(serverSet);
//		String str=serverSetService.serverSet(obdSn, "01", "03", "12345", "www.baidu.com", "CMNET");
//		String str=serverSetService.serverSet(obdSn, "01", "01", "12345", "192.168.1.1", null);
//		String str=serverSetService.serverSet(obdSn, "01", "01", "7089", "221.4.53.120", "CMNET");
		System.out.println(flag+"**********************服务器配置返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String server1(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		String setType = request.getParameter("setType");
		String serverType = request.getParameter("serverType");
		String port = request.getParameter("port");
		String ip = request.getParameter("ip");
		String APN = request.getParameter("APN");
		ServerSet serverSet = new ServerSet();
		serverSet.setObdSn(obdSn.toLowerCase());
		serverSet.setSetType(setType);
		serverSet.setServerType(serverType);
		serverSet.setPort(port);
		serverSet.setIp(ip);
		serverSet.setApn(APN);
		boolean flag=serverSetService.serverSetSingle(serverSet);
//		String str=serverSetService.serverSet(obdSn, "01", "03", "12345", "www.baidu.com", "CMNET");
//		String str=serverSetService.serverSet(obdSn, "01", "01", "12345", "192.168.1.1", null);
//		String str=serverSetService.serverSet(obdSn, "01", "01", "7089", "221.4.53.120", "CMNET");
		System.out.println(flag+"**********************服务器配置返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String param(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		CarParam carParam =  new CarParam();
		carParam.setObdSn(obdSn);
//		carParam.setAreaNum("01");//区域编号
//		carParam.setRailAndAlert("00000010");//围栏类型+报警方式
//		carParam.setMaxLongitude("116°04.000'");
//		carParam.setMaxLatitude("33°32.0000'");
//		carParam.setMinLongitude("108°01.000'");
//		carParam.setMinLatitude("12°45.0000'");
//		carParam.setOverspeed("120");//0x01（超速报警阈值）
//		carParam.setSleepTime("120");//0x02（进入休眠模式的时间）
//		carParam.setAccTime("0120");//0x03（ACC开时候上传、存储位置信息的间隔）
//		carParam.setHeartTime("0120");//0x04   (心跳时间间隔)没有休眠时候间隔 
//		carParam.setPosition("02");//0x05(位置信息策略)
//		carParam.setSafety("01");//0x06设防撤防 
//		carParam.setTimeZone("0818");//0x07(时区设置)
//		carParam.setUndervoltage("9.5");//0x0A欠压报警阈值
//		carParam.setHighVoltage("14");//0x0B高压报警阈值
//		carParam.setTiredDrive("18");//0x0C疲劳驾驶时间阈值
//		carParam.setTiredDriveTime("03");//0x0D解除疲劳驾驶时间阈值
//		carParam.setWaterTemp("90");//0x0E水温高报警阈值
//		carParam.setUserId("13168094719");//0x0F设置用户id
//		carParam.setEbrake("03");//0x10(急刹车系数强度)
//		carParam.setEspeedup("03");//0x11(急加速系数强度)
//		carParam.setCrash("03");//0x12(碰撞报警系数强度)
//		carParam.setShake("03");//0x13(布防时震动报警系数强度)
		carParam.setSpeedRemind("01");//0x14(速度提醒开关配置)
//		carParam.setDataUploadType("00");//0x16(obd数据汇报策略)
//		carParam.setDataUploadTime("0101");//0x19(obd数据汇报间隔)
//		carParam.setPositionExtra("00");//0x17(上传附加位置信息1)
//		carParam.setGsmbts("00");//0x18(基站定位时GSM基站信息内容）
//		carParam.setCommunication("00");//0x1A(通信模块类型)
//		carParam.setPositionType("01");//0x1B(定位模块类型)
//		carParam.setGps("01");//GPS
		
		String str=carParamSetService.paramSet(carParam);
		System.out.println(str+"**********************参数设置返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	
	public String paramQuery() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		//00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c
		String[] ids = {"00","01","02","03","04","05","06","07","08","09","0a","0b","0c","0d","0e","0f","10","11","12","13","14","15","16","17","18","19","1a","1b","1c"};
		CarParam carParam=carParamQueryService.carParamQuery(obdSn, ids);
		if(carParam!=null){
			System.out.println(carParam.getAreaNum()+"**********************参数查询返回消息啦啦啦啦啦啦啦啦啦");
		}
		return "list";
	}
	public String obdUpdate(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		
		boolean flag=true;
		try {
			flag = updateDataService.obdUpdateData(obdSn, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(flag+"**********************obd固件升级包返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
	public String remoteUpgrade(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String obdSn = request.getParameter("obdId");
		List<String> obdSnList = new ArrayList<String>();
		obdSnList.add(obdSn);
		String obdVersion = "1.1";
		boolean flag=true;
		try {
//			flag = remoteUpgradeService.remoteUpgradeAsk(obdSnList, obdVersion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(flag+"**********************远程升级申请返回消息啦啦啦啦啦啦啦啦啦");
		return "list";
	}
}
