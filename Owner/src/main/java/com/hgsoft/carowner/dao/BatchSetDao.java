package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.hgsoft.carowner.entity.BatchSet;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;
/**
 * obd版本信息dao
 * @author liujialin
 * 2015-8-5
 */
@Repository
public class BatchSetDao extends BaseDao<BatchSet> {
	
	public boolean add(BatchSet batchSet) {
		try {
			this.saveOrUpdate(batchSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public BatchSet queryLastByParams(Map<String, Object> map) {
		String hql = "FROM BatchSet WHERE 1=1 ";
		if(map.containsKey("id")){
			hql+=" and id =:id ";
		}
		if(map.containsKey("type")){
			hql+=" and type =:type ";
		}
		if(map.containsKey("version")){
			hql+=" and version =:version ";
		}
		if(map.containsKey("auditState")){
			hql+=" and auditState =:auditState ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid =:valid ";
		}
		hql+= " order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("id")){
			String id =(String) map.get("id");
			query.setString("id", id);
		}
		if(map.containsKey("type")){
			String type =(String) map.get("type");
			query.setString("type", type);
		}
		if(map.containsKey("version")){
			String version =(String) map.get("version");
			query.setString("version", version);
		}
		if(map.containsKey("auditState")){
			Integer auditState =(Integer) map.get("auditState");
			query.setInteger("auditState", auditState);
		}
		if(map.containsKey("valid")){
			Integer valid =(Integer) map.get("valid");
			query.setInteger("valid", valid);
		}
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (BatchSet) query.uniqueResult();
	}
	
	
	//根据多个type查询
	@SuppressWarnings("unchecked")
	public List<BatchSet> getListByTypes(Set<String> types) {
		String typeList = StrUtil.SetToString(types);
		String hql = "FROM ObdSetting WHERE type in "+typeList+" and valid='1' order by createTime desc";
		return (List<BatchSet>)queryByHQL(hql);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BatchSet> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM BatchSet WHERE 1=1 ";
		if(map.containsKey("type")){
			hql+=" and type = ? ";
		}
		if(map.containsKey("version")){
			hql+=" and version = ? ";
		}
		if(map.containsKey("auditState")){
			hql+=" and auditState = ? ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid = ? ";
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
				if(map.containsKey("type")){
					objArray[i]=map.get("type");
					map.remove("type");
					continue;
				}
				if(map.containsKey("version")){
					objArray[i]=map.get("version");
					map.remove("version");
					continue;
				}
				if(map.containsKey("auditState")){
					objArray[i]=map.get("auditState");
					map.remove("auditState");
					continue;
				}
				if(map.containsKey("valid")){
					objArray[i]=map.get("valid");
					map.remove("valid");
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
