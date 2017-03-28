package com.hgsoft.carowner.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hgsoft.carowner.entity.CarMaintain;
import com.hgsoft.carowner.entity.CarTraffic;
import com.hgsoft.carowner.entity.CarViolation;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.carowner.service.CarMaintainService;
import com.hgsoft.carowner.service.CarTrafficService;
import com.hgsoft.carowner.service.CarViolationService;
import com.hgsoft.carowner.service.FaultUploadService;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;

/**
 * 车主服务
 * @author sujunguang
 * 2015-7-23
 */
@Controller
@Scope("prototype")
public class CarOwnerServiceAction extends BaseAction {
	@Resource
	private MebUserService mebUserService;
	private List faultList=new ArrayList();
	private String mobileNumber;
	private String license;
	private String obdSN;
	private String starTime;
	private String endTime;
	@Resource
	private CarMaintainService carMaintainService;
	private String maintainStatus;
	private String orderStatus;
	private List carMaintainList=new ArrayList();
	@Resource
	private CarViolationService carViolationService;
	private List carViolationList=new ArrayList();
	@Resource
	private CarTrafficService carTrafficService;
	private List carTrafficList=new ArrayList();
	private String trafficStatus;
	private List obdSnList=new ArrayList();
	private String maintainTime;
	private String violationDesc;
	private String violationTime;
	private String penaltyPoints;
	private String trafficTime;
	@Resource
	private FaultUploadService faultUploadService;
	private String faultCode;
	private String createTime;
	private String id;
	private FaultUpload faultUpload;
	private CarMaintain carMaintain;
	private CarViolation carViolation;
	private CarTraffic carTraffic;
	//故障提醒
	public String faultSearch(){
		faultList=mebUserService.getFaultCode(mobileNumber, license, obdSN, starTime, endTime, pager);
		return "faultList";
	}
	/**
	 * 跳转至修改故障信息界面
	 * @return
	 */
	public String faultEditJsp(){
		faultUpload=faultUploadService.queryById(id);
		return "faultEditJsp";
	}
	/**
	 * 修改故障信息
	 * @return
	 * @throws ParseException 
	 */
	public String faultEdit() throws ParseException{
		 FaultUpload tem=faultUploadService.queryById(faultUpload.getFaultId());
		 if(tem!=null){
			 tem.setFaultCode(faultUpload.getFaultCode());
			 tem.setCreateTime(faultUpload.getCreateTime());
			 tem.setObdSn(faultUpload.getObdSn());
			 faultUploadService.update(tem);
		 }	
		return faultSearch();
	}
	/**
	 * 跳转至新增故障信息界面
	 * @return
	 */
	public String faultAddJsp(){
		obdSnList=mebUserService.getObdSN();
		return "faultAddJsp";
	}
	/**
	 * 新增故障信息
	 * @return
	 * @throws ParseException 
	 */
	public String faultAdd() throws ParseException{
		FaultUpload faultUpload=new FaultUpload();
		faultUpload.setFaultId(IDUtil.createID());
		faultUpload.setObdSn(obdSN);
		faultUpload.setFaultCode(faultCode);
		faultUpload.setState("1");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		faultUpload.setCreateTime(df.parse(createTime));
		faultUploadService.save(faultUpload);
		return faultSearch();
	}
	/**
	 * 删除故障信息
	 * @return
	 */
	public String faultDel(){
		String idArr=request.getParameter("idArr");
		if(idArr!=null){
			String[] str=idArr.split(",");		
			for (int i = 0; i < str.length; i++) {
				FaultUpload temp=faultUploadService.queryById(str[i]);
				if(temp!=null){
					faultUploadService.delete(temp);
				}			
			}		
		}
		return faultSearch();
	}
	
	//保养提醒
	public String maintainSearch(){
		carMaintainList=carMaintainService.getCarMaintainInfo(mobileNumber, license, obdSN, maintainStatus, orderStatus, pager);
		return "maintainList";
	}
	/**
	 * 跳转至修改保养信息界面
	 * @return
	 */
	public String maintainEditJsp(){
		carMaintain=carMaintainService.queryById(id);
		return "maintainEditJsp";
	}
	/**
	 * 修改保养信息
	 * @return
	 * @throws ParseException 
	 */
	public String maintainEdit() throws ParseException{
		CarMaintain tem=carMaintainService.queryById(carMaintain.getId());
		 if(tem!=null){
			 tem.setMaintainTime(carMaintain.getMaintainTime());
			 tem.setMaintainStatus(carMaintain.getMaintainStatus());
			 tem.setObdSn(carMaintain.getObdSn());
			 carMaintainService.update(tem);
		 }	
		return maintainSearch();
	}
	/**
	 * 跳转至新增保养信息界面
	 * @return
	 */
	public String maintainAddJsp(){
		obdSnList=mebUserService.getObdSN();
		return "maintainAddJsp";
	}
	/**
	 * 新增保养信息
	 * @return
	 */
	public String maintainAdd(){
		CarMaintain carMaintain=new CarMaintain();
		carMaintain.setId(IDUtil.createID());
		carMaintain.setObdSn(obdSN);
		carMaintain.setMaintainStatus(maintainStatus);
		carMaintain.setMaintainTime(maintainTime);
		carMaintain.setOrderStatus(orderStatus);
		carMaintainService.save(carMaintain);
		return maintainSearch();
	}
	/**
	 * 删除保养信息
	 * @return
	 */
	public String maintainDel(){
		String idArr=request.getParameter("idArr");
		if(idArr!=null){
			String[] str=idArr.split(",");		
			for (int i = 0; i < str.length; i++) {
				CarMaintain temp=carMaintainService.queryById(str[i]);
				if(temp!=null){
					carMaintainService.delete(temp);
				}			
			}		
		}
		return maintainSearch();
	}
	//违章提醒
	public String peccancySearch(){
		carViolationList=carViolationService.getCarViolation(mobileNumber, license, obdSN, pager);
		return "peccancyList";
	}
	/**
	 * 跳转至修改违章信息界面
	 * @return
	 */
	public String peccancyEditJsp(){
		carViolation=carViolationService.queryById(id);
		return "peccancyEditJsp";
	}
	/**
	 * 修改违章信息
	 * @return
	 * @throws ParseException 
	 */
	public String peccancyEdit() throws ParseException{
		CarViolation tem=carViolationService.queryById(carViolation.getId());
		 if(tem!=null){
			 tem.setViolationDesc(carViolation.getViolationDesc());
			 tem.setViolationTime(carViolation.getViolationTime());
			 tem.setPenaltyPoints(carViolation.getPenaltyPoints());
			 tem.setObdSn(carViolation.getObdSn());
			 carViolationService.update(tem);
		 }	
		return peccancySearch();
	}
	/**
	 * 跳转至违章新增页面
	 * @return
	 */
	public String peccancyAddJsp(){
		obdSnList=mebUserService.getObdSN();
		return "peccancyAddJsp";
	}
	/**
	 * 新增违章信息
	 * @return
	 */
	public String peccancyAdd(){
		CarViolation violation=new CarViolation();
		violation.setId(IDUtil.createID());
		violation.setObdSn(obdSN);
		violation.setViolationDesc(violationDesc);
		violation.setViolationTime(violationTime);
		violation.setPenaltyPoints(penaltyPoints);
		carViolationService.save(violation);		
		return peccancySearch();
	}
	/**
	 * 删除违章信息
	 * @return
	 */
	public String peccancyDel(){
		String idArr=request.getParameter("idArr");
		if(idArr!=null){
			String[] str=idArr.split(",");		
			for (int i = 0; i < str.length; i++) {
				CarViolation temp=carViolationService.queryById(str[i]);
				if(temp!=null){
					carViolationService.delete(temp);
				}			
			}		
		}
		return peccancySearch();
	}
	//车务提醒
	public String carServiceSearch(){
		carTrafficList=carTrafficService.getCarTraffic(mobileNumber, license, obdSN, trafficStatus, pager);
		return "carServiceList";
	}
	/**
	 * 跳转至修改车务信息界面
	 * @return
	 */
	public String carServiceEditJsp(){
		carTraffic=carTrafficService.queryById(id);
		return "carServiceEditJsp";
	}
	/**
	 * 修改车务信息
	 * @return
	 * @throws ParseException 
	 */
	public String carServiceEdit() throws ParseException{
		CarTraffic tem=carTrafficService.queryById(carTraffic.getId());
		 if(tem!=null){
			 tem.setTrafficStatus(carTraffic.getTrafficStatus());
			 tem.setTrafficTime(carTraffic.getTrafficTime());
			 tem.setObdSn(carTraffic.getObdSn());
			 carTrafficService.update(tem);
		 }	
		return carServiceSearch();
	}
	/**
	 * 跳转至车务新增页面
	 * @return
	 */
	public String carServiceAddJsp(){
		obdSnList=mebUserService.getObdSN();
		return "carServiceAddJsp";
	}
	/**
	 * 新增车务信息
	 * @return
	 */
	public String carServiceAdd(){
		CarTraffic traffic=new CarTraffic();
		traffic.setId(IDUtil.createID());
		traffic.setObdSn(obdSN);
		traffic.setTrafficStatus(trafficStatus);
		traffic.setTrafficTime(trafficTime);
		carTrafficService.save(traffic);		
		return carServiceSearch();
	}
	/**
	 * 删除车务信息
	 * @return
	 */
	public String carServiceDel(){
		String idArr=request.getParameter("idArr");
		if(idArr!=null){
			String[] str=idArr.split(",");		
			for (int i = 0; i < str.length; i++) {
				CarTraffic temp=carTrafficService.queryById(str[i]);
				if(temp!=null){
					carTrafficService.delete(temp);
				}			
			}		
		}
		return carServiceSearch();
	}
	
	public List getFaultList() {
		return faultList;
	}

	public void setFaultList(List faultList) {
		this.faultList = faultList;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getObdSN() {
		return obdSN;
	}

	public void setObdSN(String obdSN) {
		this.obdSN = obdSN;
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

	public String getMaintainStatus() {
		return maintainStatus;
	}

	public void setMaintainStatus(String maintainStatus) {
		this.maintainStatus = maintainStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List getCarMaintainList() {
		return carMaintainList;
	}

	public void setCarMaintainList(List carMaintainList) {
		this.carMaintainList = carMaintainList;
	}

	public List getCarViolationList() {
		return carViolationList;
	}

	public void setCarViolationList(List carViolationList) {
		this.carViolationList = carViolationList;
	}

	public List getCarTrafficList() {
		return carTrafficList;
	}

	public void setCarTrafficList(List carTrafficList) {
		this.carTrafficList = carTrafficList;
	}

	public String getTrafficStatus() {
		return trafficStatus;
	}

	public void setTrafficStatus(String trafficStatus) {
		this.trafficStatus = trafficStatus;
	}

	public List getObdSnList() {
		return obdSnList;
	}

	public void setObdSnList(List obdSnList) {
		this.obdSnList = obdSnList;
	}

	public String getMaintainTime() {
		return maintainTime;
	}

	public void setMaintainTime(String maintainTime) {
		this.maintainTime = maintainTime;
	}

	public String getViolationDesc() {
		return violationDesc;
	}

	public void setViolationDesc(String violationDesc) {
		this.violationDesc = violationDesc;
	}

	public String getViolationTime() {
		return violationTime;
	}

	public void setViolationTime(String violationTime) {
		this.violationTime = violationTime;
	}

	public String getPenaltyPoints() {
		return penaltyPoints;
	}

	public void setPenaltyPoints(String penaltyPoints) {
		this.penaltyPoints = penaltyPoints;
	}

	public String getTrafficTime() {
		return trafficTime;
	}

	public void setTrafficTime(String trafficTime) {
		this.trafficTime = trafficTime;
	}
	public String getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FaultUpload getFaultUpload() {
		return faultUpload;
	}
	public void setFaultUpload(FaultUpload faultUpload) {
		this.faultUpload = faultUpload;
	}
	public CarMaintain getCarMaintain() {
		return carMaintain;
	}
	public void setCarMaintain(CarMaintain carMaintain) {
		this.carMaintain = carMaintain;
	}
	public CarViolation getCarViolation() {
		return carViolation;
	}
	public void setCarViolation(CarViolation carViolation) {
		this.carViolation = carViolation;
	}
	public CarTraffic getCarTraffic() {
		return carTraffic;
	}
	public void setCarTraffic(CarTraffic carTraffic) {
		this.carTraffic = carTraffic;
	}
	
	
}
