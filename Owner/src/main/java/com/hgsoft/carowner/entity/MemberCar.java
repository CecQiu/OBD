package com.hgsoft.carowner.entity;

/**
 * 用户和车辆信息类
 * @author fdf
 */
public class MemberCar implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String regUserId;
	private String mobileNumber;
	private String obdSn;
	private String carId;
	private String name;
	private String sex;
	private Integer userType;
	private String password;
	private String payPassword;
	private String valid;
	private String license;
	private String carState;
	private String cartypeId;
	private String firstLetter;
	private String makeName;
	private String modelSeries;
	private String modelName;
	private String typeSeries;
	private String typeName;
	private String country;
	private String technology;
	private String vehicleClass;
	private String engineCapacity;
	private String transmission;
	private Integer isFault;
	
	@Override
	public String toString() {
		return "MemberCar--->regUserId=["+regUserId+"],mobileNumber=["+mobileNumber+
				"],obdSn=["+obdSn+"],carId=["+carId+"],name=["+name+"],license=["+
				license+"],cartypeId=["+cartypeId+"]";
	}

	public MemberCar() {}
	
	//车牌号、 手机号、 智能盒激活码（SN码）、姓名、性别、车型
	public MemberCar(String license, String mobileNumber, String obdSn, 
			String name, String sex, String typeName) {
		this.license = license;
		this.mobileNumber = mobileNumber;
		this.obdSn = obdSn;
		this.name = name;
		this.sex = sex;
		this.typeName = typeName;
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

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getCarState() {
		return carState;
	}

	public void setCarState(String carState) {
		this.carState = carState;
	}

	public String getCartypeId() {
		return cartypeId;
	}

	public void setCartypeId(String cartypeId) {
		this.cartypeId = cartypeId;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

	public String getModelSeries() {
		return modelSeries;
	}

	public void setModelSeries(String modelSeries) {
		this.modelSeries = modelSeries;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getTypeSeries() {
		return typeSeries;
	}

	public void setTypeSeries(String typeSeries) {
		this.typeSeries = typeSeries;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public String getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(String engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public Integer getIsFault() {
		return isFault;
	}

	public void setIsFault(Integer isFault) {
		this.isFault = isFault;
	}
}
