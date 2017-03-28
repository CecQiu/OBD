package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.ObdCarHis;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
/**
 * AGPS
 * @author liujialin
 *
 */
@Repository
public class ObdCarHisDao extends BaseDao<ObdCarHis> {
	
	/**
	 * 
	 * @param obdSn
	 * @return
	 */
	public boolean obdCarHisSave(ObdCarHis obdCarHis){
		try {
			this.saveOrUpdate(obdCarHis);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ObdCarHis> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdCarHis WHERE 1=1 ";
		if(map.containsKey("obdSn")){
			hql+=" and obdSn = ? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime>=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime<=? ";
		}
		
		if(map.containsKey("delStartTime")){
			hql+=" and delTime>=? ";
		}
		if(map.containsKey("delEndTime")){
			hql+=" and delTime<=? ";
		}
		
		hql+=" order by createTime desc "; 
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
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
				if(map.containsKey("delStartTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("delStartTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("delStartTime");
					continue;
				}
				if(map.containsKey("delEndTime")){
					objArray[i]=(Date) DateUtil.fromatDate((String)map.get("delEndTime"), "yyyy-MM-dd HH:mm:ss");
					map.remove("delEndTime");
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
