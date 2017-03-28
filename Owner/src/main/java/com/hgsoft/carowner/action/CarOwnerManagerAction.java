package com.hgsoft.carowner.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.carowner.dao.MebUserDao;
import com.hgsoft.carowner.entity.CarAddoil;
import com.hgsoft.carowner.entity.CarDispatch;
import com.hgsoft.carowner.entity.CarType;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.carowner.service.CarAddoilService;
import com.hgsoft.carowner.service.CarDispatchService;
import com.hgsoft.carowner.service.CarTypeService;
import com.hgsoft.carowner.service.MebUserService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.IDUtil;

/**
 * 车主管理
 * @author sujunguang
 * 2015-7-23
 */
@Controller
@Scope("prototype")
public class CarOwnerManagerAction extends BaseAction {
	
	private String mobileNumber;
	private String license;
	private String obdSN;
	private MebUser mebUser;
	private String regUserId;
	@Resource
	private MebUserDao mebUserDao;
	private List<MebUser> list=new ArrayList<MebUser>();
	@Resource
	private MebUserService mebUserService;
	
	private String makeName;
	private String modelName;
	private String typeName;
	private String dropName;
	private List<CarType> carType=new ArrayList<CarType>();
	private List<CarType> modelNameList=new ArrayList<CarType>();
	private List<CarType> typeNameList=new ArrayList<CarType>();
	@Resource
	private CarTypeService carTypeService;
	@Resource
	private CarAddoilService carAddoilService;
	private List carAddoilLsit=new ArrayList();
	private String gasStation;
	private String userName;
	private String starTime;
	private String endTime;
	private List licenseList=new ArrayList();
	private String addTime;
	private String fee;
	private String gasStationAdd;
	private String oilNum;
	private String oilType;
	private String total;
	@Resource
	private CarDispatchService carDispatchService;
	private String status;
	private String drivingOutBeginTime;
	private String drivingOutEndTime;
	private String offRunningBeginTime;
	private String offRunningEndTime;
	private List carDispatchList=new ArrayList();
	private String drivingOutAddress;
	private String destination;
	private String drivingOutTime;
	private String carDispatchId;
	private String offRunningAddress;
	private String offRunningTime;
	private String comment;
	private List acceptList=new ArrayList();
	/**
	 * 出车管理
	 */
	public String busManagement() {
		carDispatchList=carDispatchService.getCarDispatchInfo(license, userName, status, drivingOutBeginTime, drivingOutEndTime, offRunningBeginTime, offRunningEndTime, pager);
		return "busManagement";
	}
	/**
	 *跳转至新增出车信息页面
	 * @return
	 */
	public String addBusManagementJsp(){
		licenseList=mebUserService.getLicense();
		return "busManagementAdd";
	}
	/**
	 * 新增出车信息
	 * @return
	 */
	public String addBusManagement(){
		CarDispatch carDispatch=new CarDispatch();
		carDispatch.setId(IDUtil.createID());
		carDispatch.setDrivingOutAddress(drivingOutAddress);
		carDispatch.setDestination(destination);
		carDispatch.setDrivingOutTime(drivingOutTime);
		carDispatch.setMobileNumber(mobileNumber);
		carDispatch.setUserName(userName);
		carDispatch.setStatus("01");
		MebUser mebUser=mebUserService.queryBylicense(license);
//		carDispatch.setCarId(mebUser.getCarId());
		carDispatch.setObdSn(mebUser.getObdSN());
		carDispatchService.save(carDispatch);
		return busManagement();
	}
	/**
	 * 删除出车信息
	 * @return
	 */
	public String delBusManagement(){
		String idArr=request.getParameter("idArr");
		if(idArr!=null){
			String[] str=idArr.split(",");		
			for (int i = 0; i < str.length; i++) {
				CarDispatch temp=carDispatchService.queryById(str[i]);
				if(temp!=null){
					carDispatchService.delete(temp);
				}
				
			}		
		}
		return busManagement();
	}
	/**
	 *跳转至收车信息页面
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String acceptBusManagementJsp() throws UnsupportedEncodingException{
		Map map=new HashMap();
		license=URLDecoder.decode(license, "utf-8");
		map.put("license", license);
		map.put("carDispatchId", carDispatchId);
		acceptList.add(map);
		return "acceptBusManagement";
	}
	/**
	 * 收车
	 * @return
	 */
	public String acceptBusManagement(){
		if(carDispatchId!=null&&carDispatchId.trim().length()>0){
			CarDispatch tem=carDispatchService.queryById(carDispatchId);
			tem.setOffRunningAddress(offRunningAddress);
			tem.setOffRunningTime(offRunningTime);
			tem.setComment(comment);
			tem.setStatus("00");
			carDispatchService.update(tem);
		}		
		return busManagement();
	}
	/**
	 * 加油管理
	 */
	public String fuelManagement() {
		carAddoilLsit=carAddoilService.getAddoil(license, gasStation, userName, starTime, endTime, pager);
		return "fuelManagement";
	}
	/**
	 * 删除加油信息
	 * @return
	 */
	public String delAddoil(){
		String idArr=request.getParameter("idArr");
		if(idArr!=null){
			String[] str=idArr.split(",");		
			for (int i = 0; i < str.length; i++) {
				CarAddoil temp=carAddoilService.queryById(str[i]);
				carAddoilService.delete(temp);
			}		
		}
		return fuelManagement();
	}
	/**
	 *跳转至新增加油信息页面
	 * @return
	 */
	public String addoilJsp(){
		licenseList=mebUserService.getLicense();
		return "fuelAdd";
	}
	/**
	 * 新增加油信息
	 * @return
	 */
	public String fuelAdd(){
		
		CarAddoil carAddoil=new CarAddoil();
		carAddoil.setId(IDUtil.createID());
		carAddoil.setAddTime(addTime);
		carAddoil.setFee(fee);
		carAddoil.setGasStation(gasStation);
		carAddoil.setGasStationAdd(gasStationAdd);
		carAddoil.setOilNum(oilNum);
		carAddoil.setOilType(oilType);
		carAddoil.setTotal(total);
		carAddoil.setYname(userName);
		MebUser mebUser=mebUserService.queryBylicense(license);
		carAddoil.setObdSn(mebUser.getObdSN());
//		carAddoil.setCarId(mebUser.getCarId());
		carAddoilService.save(carAddoil);
		return fuelManagement();
	}
	/**
	 * 查询车主信息列表
	 * @return
	 */
	public String search(){

		list=mebUserService.queryAll(mobileNumber, license,obdSN, pager);	
		return LIST;
	}
	/**
	 * 查询车主信息
	 * @return
	 */
	public String queryById(){
		
		this.mebUser=mebUserService.queryById(regUserId);
		return "info";		
	}
	/**
	 * 修改车主信息
	 * @return
	 */
	public String update(){
		
		MebUser temp=mebUserService.queryById(mebUser.getRegUserId());
//		System.out.println(temp);
		temp.setMobileNumber(mebUser.getMobileNumber());
		temp.setObdSN(mebUser.getObdSN());
//		temp.setCarId(mebUser.getCarId());
		temp.setLicense(mebUser.getLicense());
		temp.setName(mebUser.getName());
		temp.setSex(mebUser.getSex());
		temp.setUserType(mebUser.getUserType());
		temp.setPassword(mebUser.getPassword());
		temp.setPayPassword(mebUser.getPayPassword());
		temp.setValid(mebUser.getValid());
		mebUser.setCity(mebUser.getLicense().substring(0, 1));
		mebUser.setLetter(mebUser.getLicense().substring(1, 2));
		mebUser.setNumber(mebUser.getLicense().substring(2, mebUser.getLicense().length()));
		mebUserService.update(temp);
		message="修改成功";
		
		return search();
	}
	/**
	 * 删除车主信息
	 * @return
	 */
	public String delete(){
		
		MebUser temp=mebUserService.queryById(regUserId);
		mebUserService.delete(temp);
		message="删除成功";
		return search();
	}
	/**
	 * 根据车辆类型信息查询车主信息
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public String searchByCarId() throws IOException{
		if(makeName!=null){
			makeName=URLDecoder.decode(request.getParameter("makeName"),"utf-8");
		}
		if(modelName!=null){
			modelName=URLDecoder.decode(request.getParameter("modelName"),"utf-8");
		}
		if(typeName!=null){
			typeName=URLDecoder.decode(request.getParameter("typeName"),"utf-8");
		}
		list=mebUserService.queryUserBycarInfo(makeName, modelName, typeName,pager);
		carType=carTypeService.getMakeName();//
		return "carList";
	}
	/**
	 * 获取2级下拉框数据
	 * @return
	 * @throws IOException 
	 */
	public void getSecondDrop() throws IOException{
		
		modelNameList=carTypeService.getModelName(URLDecoder.decode(request.getParameter("dropName"),"utf-8"));
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out=response.getWriter();
		if(makeName=="-1"){
			out.print("请选择车品牌"+" ");
		}else{
			if(modelNameList!=null){
				for (int i = 0; i < modelNameList.size(); i++) {
					// CarType carType=modelNameList.get(i);
					 out.print(modelNameList.get(i)+",");
					
				}
			}
			
		}
		out.flush();
		out.close();
//		return "carList";
	}
	/**
	 * 获取3级下拉框数据
	 * @return
	 * @throws IOException
	 */
	public void getThreeDrop() throws IOException{
		
		typeNameList=carTypeService.getTypeName(URLDecoder.decode(request.getParameter("dropName"),"utf-8"));	
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out=response.getWriter();
		if(modelName=="-1"){
			out.print("请选择车系列-"+" ");
		}else{
			if(typeNameList!=null){
				for (int i = 0; i < typeNameList.size(); i++) {
					// CarType carType=modelNameList.get(i);
					 out.print(typeNameList.get(i)+",");
					
				}
			}
			
		}
		out.flush();
		out.close();
//		return "carList";
	}
	
	
	public String getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
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


	public MebUserDao getMebUserDao() {
		return mebUserDao;
	}


	public void setMebUserDao(MebUserDao mebUserDao) {
		this.mebUserDao = mebUserDao;
	}


	public MebUserService getMebUserService() {
		return mebUserService;
	}

	public void setMebUserService(MebUserService mebUserService) {
		this.mebUserService = mebUserService;
	}


	public List<MebUser> getList() {
		return list;
	}

	public void setList(List<MebUser> list) {
		this.list = list;
	}

	public MebUser getMebUser() {
		return mebUser;
	}
	public void setMebUser(MebUser mebUser) {
		this.mebUser = mebUser;
	}
	
	public String getMakeName() {
		return makeName;
	}
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<CarType> getCarType() {
		return carType;
	}
	public void setCarType(List<CarType> carType) {
		this.carType = carType;
	}
	public List<CarType> getModelNameList() {
		return modelNameList;
	}
	public void setModelNameList(List<CarType> modelNameList) {
		this.modelNameList = modelNameList;
	}
	public List<CarType> getTypeNameList() {
		return typeNameList;
	}
	public void setTypeNameList(List<CarType> typeNameList) {
		this.typeNameList = typeNameList;
	}
	public String getDropName() {
		return dropName;
	}
	public void setDropName(String dropName) {
		this.dropName = dropName;
	}

	public List getCarAddoilLsit() {
		return carAddoilLsit;
	}

	public void setCarAddoilLsit(List carAddoilLsit) {
		this.carAddoilLsit = carAddoilLsit;
	}

	public String getGasStation() {
		return gasStation;
	}

	public void setGasStation(String gasStation) {
		this.gasStation = gasStation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public List getLicenseList() {
		return licenseList;
	}

	public void setLicenseList(List licenseList) {
		this.licenseList = licenseList;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getGasStationAdd() {
		return gasStationAdd;
	}

	public void setGasStationAdd(String gasStationAdd) {
		this.gasStationAdd = gasStationAdd;
	}

	public String getOilNum() {
		return oilNum;
	}

	public void setOilNum(String oilNum) {
		this.oilNum = oilNum;
	}

	public String getOilType() {
		return oilType;
	}

	public void setOilType(String oilType) {
		this.oilType = oilType;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDrivingOutBeginTime() {
		return drivingOutBeginTime;
	}

	public void setDrivingOutBeginTime(String drivingOutBeginTime) {
		this.drivingOutBeginTime = drivingOutBeginTime;
	}

	public String getDrivingOutEndTime() {
		return drivingOutEndTime;
	}

	public void setDrivingOutEndTime(String drivingOutEndTime) {
		this.drivingOutEndTime = drivingOutEndTime;
	}

	public String getOffRunningBeginTime() {
		return offRunningBeginTime;
	}

	public void setOffRunningBeginTime(String offRunningBeginTime) {
		this.offRunningBeginTime = offRunningBeginTime;
	}

	public String getOffRunningEndTime() {
		return offRunningEndTime;
	}

	public void setOffRunningEndTime(String offRunningEndTime) {
		this.offRunningEndTime = offRunningEndTime;
	}

	public List getCarDispatchList() {
		return carDispatchList;
	}

	public void setCarDispatchList(List carDispatchList) {
		this.carDispatchList = carDispatchList;
	}
	public String getDrivingOutAddress() {
		return drivingOutAddress;
	}
	public void setDrivingOutAddress(String drivingOutAddress) {
		this.drivingOutAddress = drivingOutAddress;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDrivingOutTime() {
		return drivingOutTime;
	}
	public void setDrivingOutTime(String drivingOutTime) {
		this.drivingOutTime = drivingOutTime;
	}
	public String getCarDispatchId() {
		return carDispatchId;
	}
	public void setCarDispatchId(String carDispatchId) {
		this.carDispatchId = carDispatchId;
	}
	public String getOffRunningAddress() {
		return offRunningAddress;
	}
	public void setOffRunningAddress(String offRunningAddress) {
		this.offRunningAddress = offRunningAddress;
	}
	public String getOffRunningTime() {
		return offRunningTime;
	}
	public void setOffRunningTime(String offRunningTime) {
		this.offRunningTime = offRunningTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List getAcceptList() {
		return acceptList;
	}
	public void setAcceptList(List acceptList) {
		this.acceptList = acceptList;
	}
	
	
}
