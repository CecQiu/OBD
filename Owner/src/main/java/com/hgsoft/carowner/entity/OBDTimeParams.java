package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * OBD设备时间参数
 * @author sujunguang
 * 2016年1月14日
 * 下午2:39:30
 */
public class OBDTimeParams implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1227241453770924149L;
	
	private String id;
	private String obdSn;
	private Integer sleepTime;//进入休眠模式时间	秒，0xff,为0则关闭休眠功能 60秒0x3C
	private Integer wifiUseTime;//熄火后WIFI使用时间 分钟，最多不超过30分钟 1分钟0x01
	private Integer gpsCollectDataTime;//GPS数据采集时间间隔 秒	10秒
	private Integer positionDataTime;//位置数据上传时间间隔	秒	30秒0x1E
	private Integer obdOnlineTime;//OBD在线心跳包时间间隔 分钟，最多不超过1小时，保持TCP连接 30分钟0x1E
	private Integer obdOfflineTime;//OBD离线心跳包时间间隔 10分钟，按设定时间间隔上传心跳包 3小时0x12
	private Integer obdOffDataTime;//OBD离线数据保存时间间隔 秒，离线时，保存位置信息的间隔，最多条	60秒0x3C
	private Integer requestAGPSTime;//请求AGPS数据包延时时间	秒，过延时时间GPS未定位，请求AGPS数据3分钟
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
	public Integer getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}
	public Integer getWifiUseTime() {
		return wifiUseTime;
	}
	public void setWifiUseTime(Integer wifiUseTime) {
		this.wifiUseTime = wifiUseTime;
	}
	public Integer getGpsCollectDataTime() {
		return gpsCollectDataTime;
	}
	public void setGpsCollectDataTime(Integer gpsCollectDataTime) {
		this.gpsCollectDataTime = gpsCollectDataTime;
	}
	public Integer getPositionDataTime() {
		return positionDataTime;
	}
	public void setPositionDataTime(Integer positionDataTime) {
		this.positionDataTime = positionDataTime;
	}
	public Integer getObdOnlineTime() {
		return obdOnlineTime;
	}
	public void setObdOnlineTime(Integer obdOnlineTime) {
		this.obdOnlineTime = obdOnlineTime;
	}
	public Integer getObdOfflineTime() {
		return obdOfflineTime;
	}
	public void setObdOfflineTime(Integer obdOfflineTime) {
		this.obdOfflineTime = obdOfflineTime;
	}
	public Integer getObdOffDataTime() {
		return obdOffDataTime;
	}
	public void setObdOffDataTime(Integer obdOffDataTime) {
		this.obdOffDataTime = obdOffDataTime;
	}
	public Integer getRequestAGPSTime() {
		return requestAGPSTime;
	}
	public void setRequestAGPSTime(Integer requestAGPSTime) {
		this.requestAGPSTime = requestAGPSTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public OBDTimeParams(String id, String obdSn, Integer sleepTime,
			Integer wifiUseTime, Integer gpsCollectDataTime,
			Integer positionDataTime, Integer obdOnlineTime,
			Integer obdOfflineTime, Integer obdOffDataTime,
			Integer requestAGPSTime, Date createTime) {
		super();
		this.id = id;
		this.obdSn = obdSn;
		this.sleepTime = sleepTime;
		this.wifiUseTime = wifiUseTime;
		this.gpsCollectDataTime = gpsCollectDataTime;
		this.positionDataTime = positionDataTime;
		this.obdOnlineTime = obdOnlineTime;
		this.obdOfflineTime = obdOfflineTime;
		this.obdOffDataTime = obdOffDataTime;
		this.requestAGPSTime = requestAGPSTime;
		this.createTime = createTime;
	}
	public OBDTimeParams(Integer sleepTime, Integer wifiUseTime,
			Integer gpsCollectDataTime, Integer positionDataTime,
			Integer obdOnlineTime, Integer obdOfflineTime,
			Integer obdOffDataTime, Integer requestAGPSTime) {
		super();
		this.sleepTime = sleepTime;
		this.wifiUseTime = wifiUseTime;
		this.gpsCollectDataTime = gpsCollectDataTime;
		this.positionDataTime = positionDataTime;
		this.obdOnlineTime = obdOnlineTime;
		this.obdOfflineTime = obdOfflineTime;
		this.obdOffDataTime = obdOffDataTime;
		this.requestAGPSTime = requestAGPSTime;
	}
	public OBDTimeParams() {
		super();
	}
	@Override
	public String toString() {
		return "OBDTimeParams [id=" + id + ", obdSn=" + obdSn + ", sleepTime="
				+ sleepTime + ", wifiUseTime=" + wifiUseTime
				+ ", gpsCollectDataTime=" + gpsCollectDataTime
				+ ", positionDataTime=" + positionDataTime + ", obdOnlineTime="
				+ obdOnlineTime + ", obdOfflineTime=" + obdOfflineTime
				+ ", obdOffDataTime=" + obdOffDataTime + ", requestAGPSTime="
				+ requestAGPSTime + ", createTime=" + createTime + "]";
	}
	 
}
