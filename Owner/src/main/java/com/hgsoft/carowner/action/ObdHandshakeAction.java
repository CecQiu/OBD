package com.hgsoft.carowner.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdHandShakeService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;

/**
 * obd报文查询
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdHandshakeAction extends BaseAction {
	private String startTime;
	private String endTime;
	
	private List<ObdHandShake> obdHandShakes = new ArrayList<ObdHandShake>();
	@Resource
	private ObdHandShakeService obdHandShakeService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	
	private String obdSn;
	private String obdMSn;
	
	private String gpsModule;
	private String accelerator3D;
	private String carFaultCode;
	private String wifiSet;
	
	// 列表展示
	public String list() {
		//清除缓存
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		// 分页获取对象
		if (StrUtil.arraySubNotNull(obdSn,obdMSn,gpsModule,accelerator3D,carFaultCode,wifiSet,startTime,endTime)) {
			obdHandShakes = query(pager);
		}else{
			Calendar c = Calendar.getInstance();
			Date now = c.getTime();
			endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
			c.add(Calendar.MONTH, -1);
			Date s= c.getTime();
			startTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
		}
		
		return "list";
	}


	// 从数据库中查询数据
	public List<ObdHandShake> query(Pager pager) {

		String obdSN = "";//表面号解析成设备号
		if (!StringUtils.isEmpty(obdMSn)) {
			//将表面号解析成设备号
			try {
				obdSN = StrUtil.obdSnChange(obdMSn);// 设备号
				if(StringUtils.isEmpty(obdSN)){
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdSN)){
			if(!obdSn.equals(obdSN)){
				return null;
			}else{
				obdSN = "";
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		Integer total=0;

		if (!StringUtils.isEmpty(obdSn)) {
			map.put("obdSn", obdSn);
			total++;
		}
		if(!StringUtils.isEmpty(obdSN)){
			map.put("obdSn", obdSN);
			total++;
		}
		
		if (!StringUtils.isEmpty(gpsModule)) {
			map.put("gpsModule", Integer.parseInt(gpsModule));
			total++;
		}
		
		
		if (!StringUtils.isEmpty(accelerator3D)) {
			map.put("accelerator3D", Integer.parseInt(accelerator3D));
			total++;
		}
		
		if (!StringUtils.isEmpty(carFaultCode)) {
			map.put("carFaultCode", Integer.parseInt(carFaultCode));
			total++;
		}
		
		if (!StringUtils.isEmpty(wifiSet)) {
			map.put("wifiSet", Integer.parseInt(wifiSet));
			total++;
		}
		
		if (!StringUtils.isEmpty(startTime)) {
			map.put("startTime", startTime);
			total++;
		}
		
		if (!StringUtils.isEmpty(endTime)) {
			map.put("endTime", endTime);
			total++;
		}
		
		map.put("paramsTotal", total);

		return obdHandShakeService.queryByParams(pager, map);
		 
	}
	
	/*
	 * 导出excel
	 */
	public String exportExcel(){
		
		String[] headers={"ID","设备号","固件版本号","唤醒方式","GPS模块","EFPROM","3D加速器传感器","离线数据","Wifi设置","GPS设置","GPS数据格式","离线心跳设置","注册网络","网络信号强度","ECU通讯协议","车辆故障码","蓄电池电压情况","发动机水温","启动方式","非法启动探测设置","非法震动探测设置","蓄电电压异常报警设置","系发动机水温高报警设置","车辆故障报警设置","超速报警设置","电子围栏报警设置","VIN码","前次休眠原因","蓄电池电压","电压类型","流量统计值","上电号","前次上电号","创建时间"};
		String[] cloumn={"id","obdSn","firmwareVersion","wakeUp","gpsModule","efprom","accelerator3D","hasOffData","wifiSet","gpsSet","gpsDataFormat","offHeartSet","regNet","netSinal","ecu","carFaultCode","voltStatus","engineWater","startMode","illegalStartSet","illegalShockSet","voltUnusualSet","engineWaterWarnSet","carWarnSet","overSpeedWarnSet","efenceWarnSet","vinCode","lastSleep","volt","voltType","flowCounter","upElectricNo","lastUpElectricNo","createTime"}; 
		String fileName="握手包.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		
		List<ObdHandShake> ohsList = query(null);

		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (ObdHandShake ohs   : ohsList) {
			HashMap<String,Object> map = new  HashMap<>();
			map.put("id",ohs.getId());
			map.put("obdSn",ohs.getObdSn());
			
			map.put("firmwareVersion",ohs.getFirmwareVersion());
			map.put("wakeUp",ohs.getWakeUp());
			
			map.put("gpsModule",ohs.getGpsModule());
			map.put("efprom",ohs.getEfprom());
			
			map.put("accelerator3D",ohs.getAccelerator3D());
			map.put("hasOffData",ohs.getHasOffData());
			
			map.put("wifiSet",ohs.getWifiSet());
			map.put("gpsSet",ohs.getGpsSet());
			
			map.put("gpsDataFormat",ohs.getGpsDataFormat());
			map.put("offHeartSet",ohs.getOffHeartSet());
			
			map.put("regNet",ohs.getRegNet());
			map.put("netSinal",ohs.getNetSinal());
			
			map.put("ecu",ohs.getEcu());
			map.put("carFaultCode",ohs.getCarFaultCode());
			
			map.put("voltStatus",ohs.getVoltStatus());
			map.put("engineWater",ohs.getEngineWater());
			
			map.put("startMode",ohs.getStartMode());
			map.put("illegalStartSet",ohs.getIllegalStartSet());
			
			map.put("illegalShockSet",ohs.getIllegalShockSet());
			map.put("voltUnusualSet",ohs.getVoltUnusualSet());
			
			map.put("engineWaterWarnSet",ohs.getEngineWaterWarnSet());
			map.put("carWarnSet",ohs.getCarWarnSet());
			
			map.put("overSpeedWarnSet",ohs.getOverSpeedWarnSet());
			map.put("efenceWarnSet",ohs.getEfenceWarnSet());
			
			map.put("vinCode",ohs.getVinCode());
			map.put("lastSleep",ohs.getLastSleep());
			
			map.put("volt",ohs.getVolt());
			map.put("voltType",ohs.getVoltType());
			
			map.put("flowCounter",ohs.getFlowCounter());
			map.put("upElectricNo",ohs.getUpElectricNo());
			
			map.put("lastUpElectricNo",ohs.getLastUpElectricNo());
			map.put("createTime",ohs.getCreateTime());
			lists.add(map);
		}
		
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("位置数据表", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);	
		return "excel";
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

	public List<ObdHandShake> getObdHandShakes() {
		return obdHandShakes;
	}


	public void setObdHandShakes(List<ObdHandShake> obdHandShakes) {
		this.obdHandShakes = obdHandShakes;
	}


	public String getObdMSn() {
		return obdMSn;
	}


	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}


	public String getObdSn() {
		return obdSn;
	}


	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}


	public String getGpsModule() {
		return gpsModule;
	}


	public void setGpsModule(String gpsModule) {
		this.gpsModule = gpsModule;
	}


	public String getAccelerator3D() {
		return accelerator3D;
	}


	public void setAccelerator3D(String accelerator3d) {
		accelerator3D = accelerator3d;
	}


	public String getCarFaultCode() {
		return carFaultCode;
	}

	public void setCarFaultCode(String carFaultCode) {
		this.carFaultCode = carFaultCode;
	}


	public String getWifiSet() {
		return wifiSet;
	}


	public void setWifiSet(String wifiSet) {
		this.wifiSet = wifiSet;
	}

}
