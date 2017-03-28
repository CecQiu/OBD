package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * OBD电子栅栏
 * @author sujunguang
 *
 */
public class ObdBarrier implements Serializable{

	/**
	 */
	private static final long serialVersionUID = -3966558179022345316L;

	private Integer id;
	private String obdSn;//OBD设备
	private Integer areaNum;//区域编号
	private String railAndAlert;//围栏类型+报警方式
	private String minLongitude;//最小经度
	private String minLatitude;//最小纬度
	private String maxLongitude;//最大经度
	private String maxLatitude;//最大纬度
	private Date time;//设置时间
	private int valid;//是否有效 0 有效	1 无效
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getMinLongitude() {
		return minLongitude;
	}
	public void setMinLongitude(String minLongitude) {
		this.minLongitude = minLongitude;
	}
	public String getMinLatitude() {
		return minLatitude;
	}
	public void setMinLatitude(String minLatitude) {
		this.minLatitude = minLatitude;
	}
	public String getMaxLongitude() {
		return maxLongitude;
	}
	public void setMaxLongitude(String maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
	public String getMaxLatitude() {
		return maxLatitude;
	}
	public void setMaxLatitude(String maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public Integer getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(Integer areaNum) {
		this.areaNum = areaNum;
	}
	public String getRailAndAlert() {
		return railAndAlert;
	}
	public void setRailAndAlert(String railAndAlert) {
		this.railAndAlert = railAndAlert;
	}
	public ObdBarrier(String obdSn, Integer areaNum,String railAndAlert,String minLongitude,
			String minLatitude, String maxLongitude, String maxLatitude,
			Date time, int valid) {
		super();
		this.obdSn = obdSn;
		this.areaNum = areaNum;
		this.railAndAlert = railAndAlert;
		this.minLongitude = minLongitude;
		this.minLatitude = minLatitude;
		this.maxLongitude = maxLongitude;
		this.maxLatitude = maxLatitude;
		this.time = time;
		this.valid = valid;
	}
	public ObdBarrier() {
		super();
	}
	
}
