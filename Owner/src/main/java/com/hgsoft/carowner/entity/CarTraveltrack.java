package com.hgsoft.carowner.entity;

// Generated 2015-7-23 14:29:35 by Hibernate Tools 3.4.0.CR1

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传行程记录类
 * @author fdf
 */
public class CarTraveltrack implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String obdsn;
	private Date insesrtTime;
	private Date travelEnd;
	private Integer travelNo;
	private Date travelStart;
	private Long distance;
	private Integer speed;
	private Long overspeed;
	private Long brakesNum;
	private Long urgentBrakesNum;
	private Long quickenNum;
	private Long urgentquickenNum;
	private Integer averageSpeed;
	private Integer temperature;
	private Long rotationalSpeed;
	private Double voltage;
	private Long totalFuel;
	private Long averageFuel;
	private Integer driverTime;
	private String message;
	private Integer overspeedTime;
	private Integer quickTurn;
	private Integer quickSlowDown;
	private Integer quickLaneChange;
	private Integer engineMaxSpeed;
	private Integer speedMismatch;
	private Integer idling;
	private Integer type;
//	private MebUser mebUser;
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "CarTraveltrack--->id=["+id+"],obdsn=["+obdsn+"],insesrtTime=["+sdf.format(insesrtTime)+
				"],travelEnd=["+sdf.format(travelEnd)+"],travelNo=["+travelNo+"],travelStart=["+
				sdf.format(travelStart)+"],distance=["+distance+"],speed=["+speed+"],overspeed=["+overspeed+
				"],brakesNum=["+brakesNum+"],urgentBrakesNum=["+urgentBrakesNum+"],quickenNum=["+
				quickenNum+"],urgentquickenNum=["+urgentquickenNum+"],averageSpeed=["+averageSpeed+
				"],temperature=["+temperature+"],rotationalSpeed=["+rotationalSpeed+"],voltage=["+
				voltage+"],totalFuel=["+totalFuel+"],averageFuel=["+averageFuel+"],driverTime=["+
				driverTime+",message=["+message+"],overspeedTime=["+overspeedTime+"]";
	}

	public CarTraveltrack() {
	}

	public CarTraveltrack(String id) {
		this.id = id;
	}

	public CarTraveltrack(String id, String obdsn, Date insesrtTime,
			Date travelEnd, Integer travelNo, Date travelStart, Long distance,
			Integer speed, Long overspeed, Long brakesNum,
			Long urgentBrakesNum, Long quickenNum, Long urgentquickenNum,
			Integer averageSpeed, Integer temperature, Long rotationalSpeed,
			Double voltage, Long totalFuel, Long averageFuel,
			Integer driverTime, String message,Integer overspeedTime,
			Integer quickTurn,Integer quickSlowDown,Integer quickLaneChange,
			Integer engineMaxSpeed,Integer speedMismatch,Integer idling
			//MebUser mebUser
			) {
		this.id = id;
		this.obdsn = obdsn;
		this.insesrtTime = insesrtTime;
		this.travelEnd = travelEnd;
		this.travelNo = travelNo;
		this.travelStart = travelStart;
		this.distance = distance;
		this.speed = speed;
		this.overspeed = overspeed;
		this.brakesNum = brakesNum;
		this.urgentBrakesNum = urgentBrakesNum;
		this.quickenNum = quickenNum;
		this.urgentquickenNum = urgentquickenNum;
		this.averageSpeed = averageSpeed;
		this.temperature = temperature;
		this.rotationalSpeed = rotationalSpeed;
		this.voltage = voltage;
		this.totalFuel = totalFuel;
		this.averageFuel = averageFuel;
		this.driverTime = driverTime;
		this.message = message;
		this.overspeedTime = overspeedTime;
		this.quickTurn = quickTurn;
		this.quickSlowDown = quickSlowDown;
		this.quickLaneChange = quickLaneChange;
		this.engineMaxSpeed = engineMaxSpeed;
		this.speedMismatch = speedMismatch;
		this.idling = idling;
//		this.mebUser = mebUser;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObdsn() {
		return this.obdsn;
	}

	public void setObdsn(String obdsn) {
		this.obdsn = obdsn;
	}

	public Date getInsesrtTime() {
		return this.insesrtTime;
	}

	public void setInsesrtTime(Date insesrtTime) {
		this.insesrtTime = insesrtTime;
	}

	public Date getTravelEnd() {
		return this.travelEnd;
	}

	public void setTravelEnd(Date travelEnd) {
		this.travelEnd = travelEnd;
	}

	public Integer getTravelNo() {
		return this.travelNo;
	}

	public void setTravelNo(Integer travelNo) {
		this.travelNo = travelNo;
	}

	public Date getTravelStart() {
		return this.travelStart;
	}

	public void setTravelStart(Date travelStart) {
		this.travelStart = travelStart;
	}

	public Long getDistance() {
		return this.distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Integer getSpeed() {
		return this.speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Long getOverspeed() {
		return this.overspeed;
	}

	public void setOverspeed(Long overspeed) {
		this.overspeed = overspeed;
	}

	public Long getBrakesNum() {
		return this.brakesNum;
	}

	public void setBrakesNum(Long brakesNum) {
		this.brakesNum = brakesNum;
	}

	public Long getUrgentBrakesNum() {
		return this.urgentBrakesNum;
	}

	public void setUrgentBrakesNum(Long urgentBrakesNum) {
		this.urgentBrakesNum = urgentBrakesNum;
	}

	public Long getQuickenNum() {
		return this.quickenNum;
	}

	public void setQuickenNum(Long quickenNum) {
		this.quickenNum = quickenNum;
	}

	public Long getUrgentquickenNum() {
		return this.urgentquickenNum;
	}

	public void setUrgentquickenNum(Long urgentquickenNum) {
		this.urgentquickenNum = urgentquickenNum;
	}

	public Integer getAverageSpeed() {
		return this.averageSpeed;
	}

	public void setAverageSpeed(Integer averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public Integer getTemperature() {
		return this.temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Long getRotationalSpeed() {
		return this.rotationalSpeed;
	}

	public void setRotationalSpeed(Long rotationalSpeed) {
		this.rotationalSpeed = rotationalSpeed;
	}


	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Long getTotalFuel() {
		return this.totalFuel;
	}

	public void setTotalFuel(Long totalFuel) {
		this.totalFuel = totalFuel;
	}

	public Long getAverageFuel() {
		return this.averageFuel;
	}

	public void setAverageFuel(Long averageFuel) {
		this.averageFuel = averageFuel;
	}

	public Integer getDriverTime() {
		return this.driverTime;
	}

	public void setDriverTime(Integer driverTime) {
		this.driverTime = driverTime;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getOverspeedTime() {
		return overspeedTime;
	}

	public void setOverspeedTime(Integer overspeedTime) {
		this.overspeedTime = overspeedTime;
	}

//	public MebUser getMebUser() {
//		return mebUser;
//	}
//
//	public void setMebUser(MebUser mebUser) {
//		this.mebUser = mebUser;
//	}

	public Integer getQuickTurn() {
		return quickTurn;
	}

	public void setQuickTurn(Integer quickTurn) {
		this.quickTurn = quickTurn;
	}

	public Integer getQuickSlowDown() {
		return quickSlowDown;
	}

	public void setQuickSlowDown(Integer quickSlowDown) {
		this.quickSlowDown = quickSlowDown;
	}

	public Integer getQuickLaneChange() {
		return quickLaneChange;
	}

	public void setQuickLaneChange(Integer quickLaneChange) {
		this.quickLaneChange = quickLaneChange;
	}

	public Integer getEngineMaxSpeed() {
		return engineMaxSpeed;
	}

	public void setEngineMaxSpeed(Integer engineMaxSpeed) {
		this.engineMaxSpeed = engineMaxSpeed;
	}

	public Integer getSpeedMismatch() {
		return speedMismatch;
	}

	public void setSpeedMismatch(Integer speedMismatch) {
		this.speedMismatch = speedMismatch;
	}

	public Integer getIdling() {
		return idling;
	}

	public void setIdling(Integer idling) {
		this.idling = idling;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
}
