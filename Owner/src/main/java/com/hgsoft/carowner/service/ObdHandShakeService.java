package com.hgsoft.carowner.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.dao.ObdHandShakeDao;
import com.hgsoft.carowner.entity.ObdHandShake;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.jedis.JedisServiceUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdRedisData;
import com.hgsoft.obd.util.JsonDateValueProcessor;

@Service
public class ObdHandShakeService extends BaseService<ObdHandShake> {
	
	@Resource
	private JedisServiceUtil jedisServiceUtil;
	
	@Resource
	public void setDao(ObdHandShakeDao obdHandShakeDao) {
		super.setDao(obdHandShakeDao);
	}

	public ObdHandShakeDao getObdHandShakeDao() {
		return (ObdHandShakeDao) this.getDao();
	}
	
	public ObdHandShake findLastBySn(String obdSn) {
		//查缓存
		ObdHandShake obdHandShake = null;
		boolean openLastHandShakeRedis = GlobalData.openLastHandShakeRedis;
		if(openLastHandShakeRedis){
			String handShakeJson = jedisServiceUtil.getHSetByRedis(ObdRedisData.OBD_LastHandShake_KEY, obdSn);
			if(!StringUtils.isEmpty(handShakeJson) && !"null".equals(handShakeJson)){
				JSONObject jsonObject = JSONObject.fromObject(handShakeJson);
				JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));
				obdHandShake = (ObdHandShake) JSONObject.toBean(jsonObject, ObdHandShake.class);
			}else{
				//查数据库
				obdHandShake = getObdHandShakeDao().findLastBySn(obdSn);
				if(obdHandShake != null){
					JsonConfig jsonConfig = new JsonConfig();
			        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());   
			        JSONObject jsonObject = JSONObject.fromObject(obdHandShake, jsonConfig);
			        if(!jsonObject.isEmpty()){
			        	jedisServiceUtil.putHSetByRedis(ObdRedisData.OBD_LastHandShake_KEY, obdSn, jsonObject.toString());
			        }
				}
			}
		}else{
			obdHandShake = getObdHandShakeDao().findLastBySn(obdSn);
		}
		return obdHandShake;
	}
	
	public List<ObdHandShake> find(String obdSn,Pager pager){
		return getObdHandShakeDao().find(obdSn,pager);
	}

	public List find(Date begin, Date end){
		return getObdHandShakeDao().find(begin, end);
	}
	
	public void add(ObdHandShake obdHandShake){
		boolean openLastHandShakeRedis = GlobalData.openLastHandShakeRedis;
		if(obdHandShake != null && openLastHandShakeRedis){
			JsonConfig jsonConfig = new JsonConfig();
	        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());   
	        JSONObject jsonObject = JSONObject.fromObject(obdHandShake, jsonConfig);
	        if(!jsonObject.isEmpty()){
	        	jedisServiceUtil.putHSetByRedis(ObdRedisData.OBD_LastHandShake_KEY, obdHandShake.getObdSn(), jsonObject.toString());
	        }
		}
		save(obdHandShake);
	}
	public List<ObdHandShake> queryByParams(Pager pager,Map<String,Object>map) {
		return this.getObdHandShakeDao().queryByParams(pager, map);
	}
}
