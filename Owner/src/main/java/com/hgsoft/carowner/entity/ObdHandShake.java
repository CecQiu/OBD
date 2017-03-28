package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * Obd握手
 * @author sujunguang
 * 2016年4月22日
 * 上午10:23:39
 */
public class ObdHandShake implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4356006745904596131L;
	
	private String id;
	private String obdSn;//设备号
	private String firmwareVersion;//固件版本号
	private Integer wakeUp;//唤醒方式：1-定时唤醒 0-其他唤醒
	private Integer gpsModule;//GPS模块：0-正常 1-异常
	private Integer efprom;//	EFPROM：	0-正常 1-异常
	private Integer accelerator3D;//3D加速器传感器：0-正常 1-异常
	private Integer hasOffData;//离线数据：	0-无 1-有
	private Integer wifiSet	;//Wifi设置：0-开 1-关
	private Integer gpsSet	;//GPS设置：0-开 1-关
	private Integer gpsDataFormat;//GPS数据格式：	0-只传定位数据 1-全部
	private Integer offHeartSet;//离线心跳设置：	0-无 1-设置
	private String regNet	;//注册网络
	private Integer netSinal;//网络信号强度
	private String ecu;//ECU通讯协议
	private Integer carFaultCode;//车辆故障码 0-无 1-有
	private Integer voltStatus;//蓄电池电压情况 0-正常 1-异常
	private Integer engineWater;//发动机水温 	0-正常 1-异常
	private Integer startMode;//启动方式 0-点火 1-震动
	private Integer illegalStartSet;//非法启动探测设置：0-开启 1-关闭
	private Integer illegalShockSet;//非法震动探测设置：0-开启 1-关闭
	private Integer voltUnusualSet;//蓄电电压异常报警设置：0-开启 1-关闭
	private Integer engineWaterWarnSet;//发动机水温高报警设置：0-开启 1-关闭
	private Integer carWarnSet;//车辆故障报警设置：0-开启 1-关闭
	private Integer overSpeedWarnSet;//超速报警设置：0-开启 1-关闭
	private Integer efenceWarnSet;//电子围栏报警设置：0-开启 1-关闭
	private String vinCode;//VIN码
	private String lastSleep;//	前次休眠原因
	private double	volt;//			蓄电池电压 V
	private Integer voltType;//0-12V 1-24V
	private Long flowCounter;//流量统计值
	private Integer upElectricNo;//上电号
	private Integer lastUpElectricNo;//上次上电号
	private String message;//握手报文
	private Date createTime;//创建时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public Integer getWakeUp() {
		return wakeUp;
	}
	public void setWakeUp(Integer wakeUp) {
		this.wakeUp = wakeUp;
	}
	public Integer getGpsModule() {
		return gpsModule;
	}
	public void setGpsModule(Integer gpsModule) {
		this.gpsModule = gpsModule;
	}
	public Integer getEfprom() {
		return efprom;
	}
	public void setEfprom(Integer efprom) {
		this.efprom = efprom;
	}
	public Integer getAccelerator3D() {
		return accelerator3D;
	}
	public void setAccelerator3D(Integer accelerator3d) {
		accelerator3D = accelerator3d;
	}
	public Integer getHasOffData() {
		return hasOffData;
	}
	public void setHasOffData(Integer hasOffData) {
		this.hasOffData = hasOffData;
	}
	public Integer getWifiSet() {
		return wifiSet;
	}
	public void setWifiSet(Integer wifiSet) {
		this.wifiSet = wifiSet;
	}
	public Integer getGpsSet() {
		return gpsSet;
	}
	public void setGpsSet(Integer gpsSet) {
		this.gpsSet = gpsSet;
	}
	public Integer getGpsDataFormat() {
		return gpsDataFormat;
	}
	public void setGpsDataFormat(Integer gpsDataFormat) {
		this.gpsDataFormat = gpsDataFormat;
	}
	public Integer getOffHeartSet() {
		return offHeartSet;
	}
	public void setOffHeartSet(Integer offHeartSet) {
		this.offHeartSet = offHeartSet;
	}
	public String getRegNet() {
		return regNet;
	}
	public void setRegNet(String regNet) {
		this.regNet = regNet;
	}
	public Integer getNetSinal() {
		return netSinal;
	}
	public void setNetSinal(Integer netSinal) {
		this.netSinal = netSinal;
	}
	public String getEcu() {
		return ecu;
	}
	public void setEcu(String ecu) {
		this.ecu = ecu;
	}
	public Integer getCarFaultCode() {
		return carFaultCode;
	}
	public void setCarFaultCode(Integer carFaultCode) {
		this.carFaultCode = carFaultCode;
	}
	public Integer getVoltStatus() {
		return voltStatus;
	}
	public void setVoltStatus(Integer voltStatus) {
		this.voltStatus = voltStatus;
	}
	public Integer getEngineWater() {
		return engineWater;
	}
	public void setEngineWater(Integer engineWater) {
		this.engineWater = engineWater;
	}
	public Integer getStartMode() {
		return startMode;
	}
	public void setStartMode(Integer startMode) {
		this.startMode = startMode;
	}
	public Integer getIllegalStartSet() {
		return illegalStartSet;
	}
	public void setIllegalStartSet(Integer illegalStartSet) {
		this.illegalStartSet = illegalStartSet;
	}
	public Integer getIllegalShockSet() {
		return illegalShockSet;
	}
	public void setIllegalShockSet(Integer illegalShockSet) {
		this.illegalShockSet = illegalShockSet;
	}
	public Integer getVoltUnusualSet() {
		return voltUnusualSet;
	}
	public void setVoltUnusualSet(Integer voltUnusualSet) {
		this.voltUnusualSet = voltUnusualSet;
	}
	public Integer getEngineWaterWarnSet() {
		return engineWaterWarnSet;
	}
	public void setEngineWaterWarnSet(Integer engineWaterWarnSet) {
		this.engineWaterWarnSet = engineWaterWarnSet;
	}
	public Integer getCarWarnSet() {
		return carWarnSet;
	}
	public void setCarWarnSet(Integer carWarnSet) {
		this.carWarnSet = carWarnSet;
	}
	public Integer getOverSpeedWarnSet() {
		return overSpeedWarnSet;
	}
	public void setOverSpeedWarnSet(Integer overSpeedWarnSet) {
		this.overSpeedWarnSet = overSpeedWarnSet;
	}
	public Integer getEfenceWarnSet() {
		return efenceWarnSet;
	}
	public void setEfenceWarnSet(Integer efenceWarnSet) {
		this.efenceWarnSet = efenceWarnSet;
	}
	public String getVinCode() {
		return vinCode;
	}
	public void setVinCode(String vinCode) {
		this.vinCode = vinCode;
	}
	public String getLastSleep() {
		return lastSleep;
	}
	public void setLastSleep(String lastSleep) {
		this.lastSleep = lastSleep;
	}
	public double getVolt() {
		return volt;
	}
	public void setVolt(double volt) {
		this.volt = volt;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getFlowCounter() {
		return flowCounter;
	}
	public void setFlowCounter(Long flowCounter) {
		this.flowCounter = flowCounter;
	}
	public Integer getVoltType() {
		return voltType;
	}
	public void setVoltType(Integer voltType) {
		this.voltType = voltType;
	}
	public Integer getUpElectricNo() {
		return upElectricNo;
	}
	public void setUpElectricNo(Integer upElectricNo) {
		this.upElectricNo = upElectricNo;
	}
	public Integer getLastUpElectricNo() {
		return lastUpElectricNo;
	}
	public void setLastUpElectricNo(Integer lastUpElectricNo) {
		this.lastUpElectricNo = lastUpElectricNo;
	}
	@Override
	public String toString() {
		return "ObdHandShake [id=" + id + ", obdSn=" + obdSn
				+ ", firmwareVersion=" + firmwareVersion + ", wakeUp=" + wakeUp
				+ ", gpsModule=" + gpsModule + ", efprom=" + efprom
				+ ", accelerator3D=" + accelerator3D + ", hasOffData="
				+ hasOffData + ", wifiSet=" + wifiSet + ", gpsSet=" + gpsSet
				+ ", gpsDataFormat=" + gpsDataFormat + ", offHeartSet="
				+ offHeartSet + ", regNet=" + regNet + ", netSinal=" + netSinal
				+ ", ecu=" + ecu + ", carFaultCode=" + carFaultCode
				+ ", voltStatus=" + voltStatus + ", engineWater=" + engineWater
				+ ", startMode=" + startMode + ", illegalStartSet="
				+ illegalStartSet + ", illegalShockSet=" + illegalShockSet
				+ ", voltUnusualSet=" + voltUnusualSet
				+ ", engineWaterWarnSet=" + engineWaterWarnSet
				+ ", carWarnSet=" + carWarnSet + ", overSpeedWarnSet="
				+ overSpeedWarnSet + ", efenceWarnSet=" + efenceWarnSet
				+ ", vinCode=" + vinCode + ", lastSleep=" + lastSleep
				+ ", volt=" + volt + ", voltType=" + voltType
				+ ", flowCounter=" + flowCounter + ", upElectricNo="
				+ upElectricNo + ", lastUpElectricNo=" + lastUpElectricNo
				+ ", message=" + message + ", createTime=" + createTime + "]";
	}
	
}
