package com.hgsoft.carowner.dao;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.ObdBarrier;
import com.hgsoft.common.dao.BaseDao;
/**
 * 车辆参数
 * @author liujialin
 *
 */
@Repository
public class CarParamDao extends BaseDao<CarParam> {

	//查询当前车辆参数记录是否存在
	public CarParam isExist(String obdSn) {
		CarParam carParam = null;
		if (obdSn != null && !"".equals(obdSn)) {
			carParam = (CarParam) uniqueResult("from CarParam o where o.obdSn='" + obdSn + "'");
		}
		return carParam;
	}
}
