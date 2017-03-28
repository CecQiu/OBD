package com.hgsoft.carowner.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdMsg;
import com.hgsoft.common.dao.BaseDao;
@Repository
public class ObdMsgDao extends BaseDao<ObdMsg>{

	public List<ObdMsg> queryObdMsgList(OBDStockInfo obdStockInfo) {
		String sql = "SELECT s.obdId AS s_obdId,s.obdSn AS s_obdSn,s.obdMSn AS s_obdMSn,s.stockType AS s_stockType,s.stockState AS s_stockState,s.regUserId AS s_regUserId,s.startDate AS s_startDate,s.gpsState AS s_gpsState,s.wifiState AS s_wifiState,s.groupNum AS s_groupNum,s.update_time AS s_updateTime,m.mobileNumber AS m_mobileNumber,m.carId AS m_carId,m.license AS m_license,m.name AS m_name,m.sex AS m_sex,m.userType AS m_userType,ci.carState AS ci_carState,ci.cartype_id AS ci_cartypeId,oc.hardware AS oc_hardware,oc.iap AS oc_iap,oc.software AS oc_software FROM obd_stock_info s LEFT JOIN meb_user m ON s.regUserId=m.regUserId LEFT JOIN car_info ci on m.carId=ci.carId LEFT JOIN car_type ct ON ci.cartype_id = ct.cartype_id LEFT JOIN obd_curversion oc ON s.obdMSn =oc.obdSn WHERE m.valid='1' AND ci.valid='1'";
		if(obdStockInfo!=null){
			String obdSN = obdStockInfo.getObdSn();
			if(!StringUtils.isEmpty(obdSN)){
				sql+=" AND s.obdSn ='"+obdSN+"'";
			}
			String obdMSn = obdStockInfo.getObdMSn();
			if(!StringUtils.isEmpty(obdMSn)){
				sql+=" AND s.obdMSn ='"+obdMSn+"'";
			}
			String stockState = obdStockInfo.getStockState();
			if(!StringUtils.isEmpty(stockState)){
				sql+=" AND s.stockState ='"+stockState+"'";
			}
			String groupNum = obdStockInfo.getGroupNum();
			if(!StringUtils.isEmpty(groupNum)){
				sql+=" AND s.groupNum ='"+groupNum+"'";
			}
		}
		Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(ObdMsg.class));
		List<ObdMsg> list=query.list();
		return list;
	}
	
}
