package com.hgsoft.carowner.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.ObdBarrier;
import com.hgsoft.common.dao.BaseDao;

@Repository
public class ObdBarrierDao extends BaseDao<ObdBarrier> {

	//查询改用户是否已经设置了电子围栏
	public ObdBarrier barrierExist(String obdSn) {
		ObdBarrier obdBarrier = null;
		if (obdSn != null && !"".equals(obdSn)) {
			obdBarrier = (ObdBarrier) uniqueResult("from ObdBarrier o where o.obdSn='" + obdSn + "'");
		}
		return obdBarrier;
	}

	//不存在则保存，存在则更新
	public List<ObdBarrier> saveMore(List<String> sns, String minX, String minY, String maxX, String maxY,Integer areaNo,String type) {
		List<ObdBarrier> obdBarriers = new ArrayList<ObdBarrier>();
		for(String sn:sns) {
			ObdBarrier obd = barrierExist(sn);
			if(obd == null){
				obd = new ObdBarrier();
			}
			obd.setAreaNum(areaNo);
			obd.setMinLongitude(minX);
			obd.setMaxLongitude(maxX);
			obd.setMinLatitude(minY);
			obd.setMaxLatitude(maxY);
			obd.setValid(0);
			obd.setRailAndAlert(type);
			obd.setTime(new Date());
			obd.setObdSn(sn);
			obdBarriers.add(obd);
//			update(obd);在service执行保存操作
		}
		return obdBarriers;
	}

	public boolean isHasOBDSN(String sn) {
		String hql = "select count(1) from ObdBarrier where obdSn = :sn";
		Query query = getSession().createQuery(hql);
		query.setString("sn", sn);
		long count = (Long) query.uniqueResult();
		if(count > 0) {
			return true;
		}
		return false;
	}
}
