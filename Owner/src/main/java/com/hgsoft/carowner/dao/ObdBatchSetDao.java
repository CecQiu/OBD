package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.entity.ObdBatchSet;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;
/**
 * obd版本信息dao
 * @author liujialin
 * 1015-8-5
 */
@Repository
public class ObdBatchSetDao extends BaseDao<ObdBatchSet> {
	
	public boolean add(ObdBatchSet obdBatchSet) {
		try {
			this.saveOrUpdate(obdBatchSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Integer updByParams(Map<String, Object> map) {
		if(map==null || map.size()==0){
			return 0;
		}
		String hql = "UPDATE FROM ObdBatchSet set ";
		String setStr = "";
		if(map.containsKey("id1")){
			setStr+=",id =:id1 ";
		}
		if(map.containsKey("obdSn1")){
			setStr+=",obdSn =:obdSn1";
		}
		if(map.containsKey("type1")){
			setStr+=",type =:type1";
		}
		if(map.containsKey("version1")){
			setStr+=",version =:version1";
		}
		if(map.containsKey("auditOper1")){
			setStr+=",auditOper =:auditOper1";
		}
		if(map.containsKey("auditTime1")){
			setStr+=",auditTime =:auditTime1";
		}
		if(map.containsKey("auditState1")){
			setStr+=",auditState =:auditState1";
		}
		if(map.containsKey("sendedCount1")){
			setStr+=",sendedCount =:sendedCount1";
		}
		if(map.containsKey("success1")){
			setStr+=",success =:success1";
		}
		if(map.containsKey("valid1")){
			setStr+=",valid =:valid1";
		}
		if(map.containsKey("createTime1")){
			setStr+=",createTime =:createTime1";
		}
		if(map.containsKey("updateTime1")){
			setStr+=",updateTime =:updateTime1";
		}
		if(!StringUtils.isEmpty(setStr)){
			setStr=setStr.substring(1, setStr.length());
		}
		hql += setStr+" where 1=1 ";
		if(map.containsKey("id")){
			hql+=" and id =:id ";
		}
		if(map.containsKey("obdSn")){
			hql+=" and obdSn =:obdSn ";
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
		if(map.containsKey("sendedCount")){
			hql+=" and sendedCount >=:sendedCount ";
		}
		if(map.containsKey("success")){
			hql+=" and success =:success ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid =:valid ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime >=:startTime ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime <=:endTime ";
		}
		
		Query query = getSession().createQuery(hql);
		if(map.containsKey("id")){
			String id =(String) map.get("id");
			query.setString("id", id);
		}
		if(map.containsKey("obdSn")){
			String obdSn =(String) map.get("obdSn");
			query.setString("obdSn", obdSn);
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
		if(map.containsKey("sendedCount")){
			Integer sendedCount =(Integer) map.get("sendedCount");
			query.setInteger("sendedCount", sendedCount);
		}
		if(map.containsKey("success")){
			Integer success =(Integer) map.get("success");
			query.setInteger("success", success);
		}
		if(map.containsKey("valid")){
			Integer valid =(Integer) map.get("valid");
			query.setInteger("valid", valid);
		}
		if(map.containsKey("startTime")){
			Date startTime =(Date) map.get("startTime");
			query.setTimestamp("startTime", startTime);
		}
		if(map.containsKey("endTime")){
			Date endTime =(Date) map.get("endTime");
			query.setTimestamp("endTime", endTime);
		}
		
		if(map.containsKey("id1")){
			String id1 =(String) map.get("id1");
			query.setString("id1", id1);
		}
		if(map.containsKey("obdSn1")){
			String obdSn1 =(String) map.get("obdSn1");
			query.setString("obdSn1", obdSn1);
		}
		if(map.containsKey("type1")){
			String type1 =(String) map.get("type1");
			query.setString("type1", type1);
		}
		if(map.containsKey("version1")){
			String version1 =(String) map.get("version1");
			query.setString("version1", version1);
		}
		if(map.containsKey("auditOper1")){
			String auditOper1 =(String) map.get("auditOper1");
			query.setString("auditOper1", auditOper1);
		}
		if(map.containsKey("auditTime1")){
			Date auditTime1 =(Date) map.get("auditTime1");
			query.setTimestamp("auditTime1", auditTime1);
		}
		if(map.containsKey("auditState1")){
			Integer auditState1 =(Integer) map.get("auditState1");
			query.setInteger("auditState1", auditState1);
		}
		if(map.containsKey("auditState1")){
			Integer auditState1 =(Integer) map.get("auditState1");
			query.setInteger("auditState1", auditState1);
		}
		if(map.containsKey("sendedCount1")){
			Integer sendedCount1 =(Integer) map.get("sendedCount1");
			query.setInteger("sendedCount1", sendedCount1);
		}
		if(map.containsKey("success1")){
			Integer success1 =(Integer) map.get("success1");
			query.setInteger("success1", success1);
		}
		if(map.containsKey("valid1")){
			Integer valid1 =(Integer) map.get("valid1");
			query.setInteger("valid1", valid1);
		}
		if(map.containsKey("createTime1")){
			Date createTime1 =(Date) map.get("createTime1");
			query.setTimestamp("createTime1",createTime1);
		}
		if(map.containsKey("updateTime1")){
			Date updateTime1 =(Date) map.get("updateTime1");
			query.setTimestamp("updateTime1", updateTime1);
		}
		return query.executeUpdate();
	}
	
	
	public Long getTotalByParams(Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdBatchSet WHERE 1=1 ";
		if(map.containsKey("id")){
			hql+=" and id =? ";
		}
		if(map.containsKey("obdSn")){
			hql+=" and obdSn =? ";
		}
		if(map.containsKey("type")){
			hql+=" and type =? ";
		}
		if(map.containsKey("version")){
			hql+=" and version =? ";
		}
		if(map.containsKey("auditState")){
			hql+=" and auditState =? ";
		}
		if(map.containsKey("sendedCount")){
			hql+=" and sendedCount >=? ";
		}
		if(map.containsKey("success")){
			hql+=" and success =? ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid =? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime >=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime <=? ";
		}
		hql+= " order by createTime desc,obdSn desc";
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("id")){
					objArray[i]=map.get("id");
					map.remove("id");
					continue;
				}
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
					continue;
				}
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
				if(map.containsKey("sendedCount")){
					objArray[i]=map.get("sendedCount");
					map.remove("sendedCount");
					continue;
				}
				if(map.containsKey("success")){
					objArray[i]=map.get("success");
					map.remove("success");
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
			return executeCount(hql, objArray);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Integer delByParams(Map<String,Object> map)throws Exception{
		String hql = "DELETE FROM ObdBatchSet WHERE 1=1 ";
		if(map.containsKey("id")){
			hql+=" and id =:id ";
		}
		if(map.containsKey("obdSn")){
			hql+=" and obdSn =:obdSn ";
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
		if(map.containsKey("sendedCount")){
			hql+=" and sendedCount >=:sendedCount ";
		}
		if(map.containsKey("success")){
			hql+=" and success =:success ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid =:valid ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime >=:startTime ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime <=:endTime ";
		}
		hql+= " order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("id")){
			String id =(String) map.get("id");
			query.setString("id", id);
		}
		if(map.containsKey("obdSn")){
			String obdSn =(String) map.get("obdSn");
			query.setString("obdSn", obdSn);
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
		if(map.containsKey("sendedCount")){
			Integer sendedCount =(Integer) map.get("sendedCount");
			query.setInteger("sendedCount", sendedCount);
		}
		if(map.containsKey("success")){
			Integer success =(Integer) map.get("success");
			query.setInteger("success", success);
		}
		if(map.containsKey("valid")){
			Integer valid =(Integer) map.get("valid");
			query.setInteger("valid", valid);
		}
		if(map.containsKey("startTime")){
			Date startTime =(Date) map.get("startTime");
			query.setTimestamp("startTime", startTime);
		}
		if(map.containsKey("endTime")){
			Date endTime =(Date) map.get("endTime");
			query.setTimestamp("endTime", endTime);
		}
	
		return  query.executeUpdate();
	}
	
	
	public ObdBatchSet queryLastByParams(Map<String, Object> map) {
		String hql = "FROM ObdBatchSet WHERE 1=1 ";
		if(map.containsKey("id")){
			hql+=" and id =:id ";
		}
		if(map.containsKey("obdSn")){
			hql+=" and obdSn =:obdSn ";
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
		if(map.containsKey("sendedCount")){
			hql+=" and sendedCount >=:sendedCount ";
		}
		if(map.containsKey("success")){
			hql+=" and success =:success ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid =:valid ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime >=:startTime ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime <=:endTime ";
		}
		hql+= " order by createTime desc";
		Query query = getSession().createQuery(hql);
		if(map.containsKey("id")){
			String id =(String) map.get("id");
			query.setString("id", id);
		}
		if(map.containsKey("obdSn")){
			String obdSn =(String) map.get("obdSn");
			query.setString("obdSn", obdSn);
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
		if(map.containsKey("sendedCount")){
			Integer sendedCount =(Integer) map.get("sendedCount");
			query.setInteger("sendedCount", sendedCount);
		}
		if(map.containsKey("success")){
			Integer success =(Integer) map.get("success");
			query.setInteger("success", success);
		}
		if(map.containsKey("valid")){
			Integer valid =(Integer) map.get("valid");
			query.setInteger("valid", valid);
		}
		if(map.containsKey("startTime")){
			Date startTime =(Date) map.get("startTime");
			query.setTimestamp("startTime", startTime);
		}
		if(map.containsKey("endTime")){
			Date endTime =(Date) map.get("endTime");
			query.setTimestamp("endTime", endTime);
		}
	
		query.setMaxResults(1);
		query.setFirstResult(0);
		return (ObdBatchSet) query.uniqueResult();
	}
	
	
	//根据多个type查询
	@SuppressWarnings("unchecked")
	public List<ObdBatchSet> getListByTypes(Set<String> types) {
		String typeList = StrUtil.SetToString(types);
		String hql = "FROM ObdBatchSet WHERE type in "+typeList+" and valid='1' order by createTime desc";
		return (List<ObdBatchSet>)queryByHQL(hql);
	}
	
	public ObdBatchSet queryObdBatchSetValidNotSuccess(String obdSn){
		return queryFirst("FROM ObdBatchSet WHERE obdSn = ? AND auditState = 1 AND (success IS NULL OR success !=1)", obdSn);
	}
	
	@SuppressWarnings("unchecked")
	public List<ObdBatchSet> queryByParams(Pager pager,Map<String, Object> map) {
		if(map.size()==0){
			return null;
		}
		Integer paramTotal = (Integer) map.get("paramsTotal");
		String hql = "FROM ObdBatchSet WHERE 1=1 ";
		if(map.containsKey("id")){
			hql+=" and id =? ";
		}
		if(map.containsKey("obdSn")){
			hql+=" and obdSn =? ";
		}
		if(map.containsKey("type")){
			hql+=" and type =? ";
		}
		if(map.containsKey("version")){
			hql+=" and version =? ";
		}
		if(map.containsKey("auditState")){
			hql+=" and auditState =? ";
		}
		if(map.containsKey("sendedCount")){
			hql+=" and sendedCount >=? ";
		}
		if(map.containsKey("success")){
			hql+=" and success =? ";
		}
		if(map.containsKey("valid")){
			hql+=" and valid =? ";
		}
		if(map.containsKey("startTime")){
			hql+=" and createTime >=? ";
		}
		if(map.containsKey("endTime")){
			hql+=" and createTime <=? ";
		}
		hql+= " order by createTime desc,obdSn desc";
		Object[] objArray = new Object[paramTotal];
		try {
			for (int i = 0; i < paramTotal; i++) {
				if(map.containsKey("id")){
					objArray[i]=map.get("id");
					map.remove("id");
					continue;
				}
				if(map.containsKey("obdSn")){
					objArray[i]=map.get("obdSn");
					map.remove("obdSn");
					continue;
				}
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
				if(map.containsKey("sendedCount")){
					objArray[i]=map.get("sendedCount");
					map.remove("sendedCount");
					continue;
				}
				if(map.containsKey("success")){
					objArray[i]=map.get("success");
					map.remove("success");
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
	
	//判断升级记录是否存在重复
		
		@SuppressWarnings("unchecked")
		public List<ObdBatchSet> getListByMap(Map<String, Object> map) {
			String hql = "FROM ObdBatchSet WHERE 1=1 ";
			if(map.containsKey("obdSns")){
				hql+=" and obdSn in (:obdSns) ";
			}
			if(map.containsKey("type")){
				hql+=" and type =:type ";
			}
			if(map.containsKey("version")){
				hql+=" and version =:version ";
			}
			if(map.containsKey("valid")){
				hql+=" and valid =:valid ";
			}
			hql+= " order by createTime desc";
			Query query = getSession().createQuery(hql);
			if(map.containsKey("obdSns")){
				List<String> obdSns = (List<String>) map.get("obdSns");
				query.setParameterList("obdSns", obdSns);
			}
			if(map.containsKey("type")){
				String type =(String) map.get("type");
				query.setString("type", type);
			}
			if(map.containsKey("version")){
				String version =(String) map.get("version");
				query.setString("version", version);
			}
			if(map.containsKey("valid")){
				Integer valid =(Integer) map.get("valid");
				query.setInteger("valid", valid);
			}
			return (List<ObdBatchSet>) query.list();
		}
	
}
