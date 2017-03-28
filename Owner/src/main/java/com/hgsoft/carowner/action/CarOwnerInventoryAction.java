package com.hgsoft.carowner.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdUnReg;
import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdUnRegService;
import com.hgsoft.carowner.service.SimStockInfoService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
import com.hgsoft.common.utils.SerialNumberUtil;
@Component
@Scope("prototype")
public class CarOwnerInventoryAction extends BaseAction{
	private final Log logger = LogFactory.getLog(CarOwnerInventoryAction.class);
	@Resource
	private OBDStockInfoService obdStockInfoService;
	private List<OBDStockInfo> obdList=new ArrayList<>();
//	private String stockType;
	private String obdSn;
//	private String mobileNumber;
//	private String simNo;
	private String obdMSn;
//	private String license;
	private String stockState;
	private String valid;
	
	@Resource
	private SimStockInfoService simStockInfoService;
	private List<SimStockInfo> simList=new ArrayList<SimStockInfo>();
	//上传的文件
	private File obdexcel;
	//保存原始的文件名
	private String obdexcelFileName;
	//sim卡流量
	private SimStockInfo simStockInfo;
	private String starTime;
	private String endTime;
    //odb未注册设备
	private String createEndTime;
	private String createTime;
	private List<ObdUnReg> obdUnRegs;
	private ObdUnReg obdUnReg;
	@Resource
	ObdUnRegService obdUnRegService;
	
	public String getObdInfo(){
		this.valid="1";
		obdList=obdStockInfoService.getObdInfo(null, null, null,valid,pager);
		return "obdList";
	}
	
	/**
	 * obd设备信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String query(){
		obdList=obdStockInfoService.getObdInfo(obdMSn, obdSn, stockState,valid,pager);
		return "obdList";
	}
	/**
	 * 流量卡管理
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String getSimInfo(){
		//清除缓存
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		List<Property> list = new ArrayList<Property>();
		if (starTime != null && !"".equals(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (endTime != null && !"".equals(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if(simStockInfo!=null){
			String obdSN =simStockInfo.getObdSn();
			String simId = simStockInfo.getSimId();
			if(!StringUtils.isEmpty(obdSN)){
				list.add(Property.eq("obdSn", obdSN.trim()));
			}
			if(!StringUtils.isEmpty(simId)){
				list.add(Property.eq("simId", simId.trim()));
			}
		}
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		simList=simStockInfoService.findByPager(pager, Order.desc("createTime"), propertyArr);
		
		return "simList";
	}
	
	/**
	 * OBD未注册
	 */
	public String getObdUnregInfo(){
		obdUnRegs = getObdUnregList(pager);
		return "obdUnreg";
	}
	
	/**
	 * 返回obd未注册查询结果
	 */
	public List<ObdUnReg> getObdUnregList(Pager pager){
		List<Property> list = new ArrayList<Property>();
		
		if(obdUnReg != null){
			String obdSn = obdUnReg.getObdSn();
			
		if(obdSn != null && !"".equals(obdSn)){
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
		}
		if(createTime != null && !"".equals(createTime)){
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(createTime+" 00:00:00", "yyyy-MM-dd HH:mm:ss")));
		}
		if(createEndTime != null && !"".equals(createEndTime)){
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(createEndTime+" 23:59:59", "yyyy-MM-dd HH:mm:ss")));
		}		
	}
		
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		return obdUnRegService.findByPager(pager, null , propertyArr);
	}
	
	/**
	 * 跳转到OBD的excel导入页面
	 */
	public String addOBD() {
		return "addOBD";
	}
	
	/**
	 * 读取obdExcel文件保存入库
	 */
	public String obdExcel() {
		boolean flag=false;
		try {
			flag=obdStockInfoService.obdExcel(obdexcel,obdexcelFileName);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(e);
		}
		this.message=flag==true? "保存成功":"保存失败";
		return "addOBD";
	}
	
	/**
	 * 导出obd未注册文件
	 */
	public String obdUnregExcel(){
		String[] headers={"ID","设备号","导入时间"};
		String[] cloumn={"id","obdSn","createTime"}; 
		String fileName="OBD未注册数据表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
//		upgradeSets =  listUpgradeSets(null);
		
		List<ObdUnReg> obdUnregs = getObdUnregList(null);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (ObdUnReg unregs : obdUnregs) {
			HashMap<String, Object> map = new  HashMap<>();
			map.put("id",unregs.getId());
			map.put("obdSn",unregs.getObdSn());
			map.put("createTime",unregs.getCreateTime());
			lists.add(map);
		}
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("OBD未注册数据表", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);	
		
		return "excel";
	}
	
	/**
	 * 导出业务统计表
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String simMsgExport() throws ParseException, IOException{
		String[] headers={"设备号","流量卡号","使用流量","创建时间"};
		String[] cloumn={"obdSn","simId","flowUse","createTime"};
		String fileName="SIM.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		
		List<Property> list = new ArrayList<Property>();
		if (starTime != null && !"".equals(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (endTime != null && !"".equals(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if(simStockInfo!=null){
			String obdSN =simStockInfo.getObdSn();
			String simId = simStockInfo.getSimId();
			if(!StringUtils.isEmpty(obdSN)){
				list.add(Property.eq("obdSn", obdSN.trim()));
			}
			if(!StringUtils.isEmpty(simId)){
				list.add(Property.eq("simId", simId.trim()));
			}
		}
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		simList=simStockInfoService.findByPager(pager, Order.desc("createTime"), propertyArr);
		
		List<HashMap<String, Object>>  lists = new ArrayList<HashMap<String,Object>>();
		for (SimStockInfo ssi : simList) {
			HashMap<String, Object> so = new HashMap<String, Object>();
			so.put("obdSn", ssi.getObdSn());
			so.put("simId", ssi.getSimId());
			so.put("flowUse", ssi.getFlowUse());
			so.put("createTime", DateUtil.getTimeString(ssi.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			lists.add(so);
		}
		ExcelUtil ex = new ExcelUtil();
		OutputStream out = new FileOutputStream(filepath);
		ex.carOwnerExport("SIM卡流量信息", headers, lists, cloumn, out);
		out.close();
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	
	public void test(){
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		try {
			MsgSendUtil msgSendUtil = new MsgSendUtil();
			msgSendUtil.msgSend("88888888888888888888", "8018", serialNumber, "00");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List<OBDStockInfo> getObdList() {
		return obdList;
	}
	public void setObdList(List<OBDStockInfo> obdList) {
		this.obdList = obdList;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	
	public List<SimStockInfo> getSimList() {
		return simList;
	}
	public void setSimList(List<SimStockInfo> simList) {
		this.simList = simList;
	}
	
	public String getObdexcelFileName() {
		return obdexcelFileName;
	}
	public void setObdexcelFileName(String obdexcelFileName) {
		this.obdexcelFileName = obdexcelFileName;
	}
	public File getObdexcel() {
		return obdexcel;
	}
	public void setObdexcel(File obdexcel) {
		this.obdexcel = obdexcel;
	}
	public String getObdMSn() {
		return obdMSn;
	}
	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}
	public String getStockState() {
		return stockState;
	}
	public void setStockState(String stockState) {
		this.stockState = stockState;
	}
	public SimStockInfo getSimStockInfo() {
		return simStockInfo;
	}
	public void setSimStockInfo(SimStockInfo simStockInfo) {
		this.simStockInfo = simStockInfo;
	}
	public String getStarTime() {
		return starTime;
	}
	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<ObdUnReg> getObdUnRegs() {
		return obdUnRegs;
	}
	public void setObdUnRegs(List<ObdUnReg> obdUnRegs) {
		this.obdUnRegs = obdUnRegs;
	}
	public ObdUnReg getObdUnReg() {
		return obdUnReg;
	}
	public void setObdUnReg(ObdUnReg obdUnReg) {
		this.obdUnReg = obdUnReg;
	}
	public String getCreateEndTime() {
		return createEndTime;
	}
	public void setCreateEndTime(String createEndTime) {
		this.createEndTime = createEndTime;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	
	
}
