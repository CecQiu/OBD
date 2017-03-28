package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.ObdSimCardDao;
import com.hgsoft.carowner.entity.ObdSimCard;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
/**
 * obd状态Service
 * @author liujialin
 * 2015-8-5
 */
@Service
public class ObdSimCardService extends BaseService<ObdSimCard> {
	
	@Resource
	public void setDao(ObdSimCardDao obdSimCardDao){
		super.setDao(obdSimCardDao);
	}
	
	@Override
	public ObdSimCardDao getDao() {
		return (ObdSimCardDao)super.getDao();
	}
	
	public ObdSimCard queryByObdSn(String obdSn) {
		return getDao().queryByObdSn(obdSn);
	}
	
	public boolean oscSave(ObdSimCard obdSimCard) {
		return getDao().oscSave(obdSimCard);
	}
	
	public List<ObdSimCard> getList(String obdSn,Integer type,String starTime,String endTime,Pager pager){
		List<Property> list = new ArrayList<Property>();

		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.eq("obdSn", obdSn.trim()));
		}
		
		if (!StringUtils.isEmpty(type)) {
			list.add(Property.eq("type", type));
		}
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		return this.findByPager(pager, Order.desc("createTime"), propertyArr);
	}
}
