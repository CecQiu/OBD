package com.hgsoft.carowner.service;


import java.util.List;

import javax.annotation.Resource;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.CarGSPTrackDao;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.util.JsonDateValueProcessor;

@Service
public class CarGSPTrackService extends BaseService<CarGSPTrack> {
	
	@Resource
	private CarGSPTrackDao carGSPTrackDao;
	@Resource
	private JedisServiceUtil jedisServiceUtil;
	
	@Resource
	public void setDao(CarGSPTrackDao carGSPTrackDao){
		super.setDao(carGSPTrackDao);
	}

	/**
	 * 查询运行轨迹
	 * @param obdSN
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<CarGSPTrack> queryTrack(String obdSN, String startTime,	String endTime) {
		return carGSPTrackDao.queryTrack(obdSN, startTime,endTime);
	}
	
	/**
	 * 保存GSP数据
	 * @param cgt
	 */
	public void carGSPSave(CarGSPTrack cgt){
		this.getDao().save(cgt);
	}
	public void batchSave(List<CarGSPTrack> carGSPTracks) throws Exception{
		carGSPTrackDao.batchSave(carGSPTracks);
	}
	/**
	 * 根据obdSn获取车辆最新的位置信息
	 * @param obdSn
	 * @return
	 */
	public CarGSPTrack findLastBySn(String obdSn){
		CarGSPTrack ct = null;
		boolean openLastPositionRedis = GlobalData.openLastPositionRedis;
		if(openLastPositionRedis){//开启缓存
			String lastPostion = jedisServiceUtil.getHSetByRedis(ObdRedisData.OBD_LastPosition_KEY, obdSn);
			JsonConfig jsonConfig = new JsonConfig();
	        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
			JSONObject jsonObject = JSONObject.fromObject(lastPostion);
			if(!jsonObject.isNullObject() && !StringUtils.isEmpty(lastPostion) && !"null".equals(lastPostion)){
				JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));
				ct = (CarGSPTrack) JSONObject.toBean(jsonObject, CarGSPTrack.class);
			}else{
				ct = carGSPTrackDao.findLastBySn(obdSn);
				if(ct != null){
					jsonObject = JSONObject.fromObject(ct, jsonConfig);
					if(!jsonObject.isEmpty()){
						String gspTrackJSON = jsonObject.toString();
						jedisServiceUtil.putHSetByRedis(ObdRedisData.OBD_LastPosition_KEY, obdSn, gspTrackJSON);
					}
				}
			}
		}else{
			ct = carGSPTrackDao.findLastBySn(obdSn);
		}
		return ct;
	}
}
