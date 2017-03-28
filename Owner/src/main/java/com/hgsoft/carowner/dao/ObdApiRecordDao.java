package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdApiRecord;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * obdApi接口操作记录
 * @author liujialin
 * 2016-6-19
 */
@Repository
public class ObdApiRecordDao extends BaseDao<ObdApiRecord> {

	public boolean irSave(ObdApiRecord obdApiRecord) {
		try {
			this.saveOrUpdate(obdApiRecord);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<ObdApiRecord> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdApiRecord WHERE 1=1 ";
		if(map.containsKey("obdMsn")){
			hql+=" and obdMsn = ? ";
		}
		if(map.containsKey("method")){
			hql+=" and method = ? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime<=? ";
		}
		
		hql+=" order by createTime desc "; 
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("obdMsn")){
					objArray[i]=map.get("obdMsn");
					map.remove("obdMsn");
					continue;
				}
				if(map.containsKey("method")){
					objArray[i]=map.get("method");
					map.remove("method");
					continue;
				}
				if(map.containsKey("startTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("startTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("startTime");
					continue;
				}
				if(map.containsKey("endTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("endTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("endTime");
					continue;
				}
			}
			if(pager!=null){
				return queryByHQL(pager, hql, objArray);
			}else{
				return queryByHQL(hql,objArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
