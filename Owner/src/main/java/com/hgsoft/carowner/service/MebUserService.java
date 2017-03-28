package com.hgsoft.carowner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.MebUserDao;
import com.hgsoft.carowner.entity.MebUser;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
@Service
public class MebUserService extends BaseService<MebUser>{

	@Resource
	private MebUserDao mebUserDao;
	@Resource
	public void setDao(MebUserDao dao) {
		super.setDao(dao);
	}

	public MebUserDao getMebUserDao() {
		return (MebUserDao) this.getDao();
	}
	public MebUser queryBylicense(String license){
		String hql="from MebUser m where m.license=? and m.valid='1'";
		return (MebUser) this.getMebUserDao().queryByHQL(hql, license).get(0);
	}
	/**
	 * 故障查询
	 * @param mobileNumber
	 * @param license
	 * @param obdSN
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List getFaultCode(String mobileNumber,String license,String obdSN,String starTime,String endTime,Pager pager){
		String sql="SELECT m.`name`,m.sex,m.mobileNumber,m.license, m.obdSN,f.fault_code,f.create_time,f.fault_id from meb_user m,fault_upload f WHERE m.obdSN=f.obdSn and f.state='T' and m.valid='1'";
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(obdSN!=null&&obdSN.trim().length()>0){
			sql+=" and m.obdSN like '%"+obdSN+"%'";
		}
		if(starTime!=null&&starTime.trim().length()>0){
			sql+=" and f.create_time >='"+starTime+"'";
		}
		if(endTime!=null&&endTime.trim().length()>0){
			sql+=" and f.create_time <='"+endTime+"'";
		}
		sql+=" ORDER BY f.create_time DESC";
		List list=this.getDao().queryBySQL(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
		List result=new ArrayList();
		for (int i = begin; i < list.size()&&i<end; i++) {
			Object[] ob=(Object[]) list.get(i);
			Map map=new HashMap();
			map.put("userName", ob[0]);
			map.put("sex", ob[1]);
			map.put("mobileNumber", ob[2]);
			map.put("license", ob[3]);
			map.put("obdSN", ob[4]);
			map.put("fault_code", ob[5]);
			map.put("create_time", ob[6]);
			map.put("id", ob[7]);
			String faultCode=String.valueOf(ob[5]);
			if(faultCode!=null&&faultCode.trim().length()>0){
				List codeList=this.getDao().queryBySQL("SELECT c.cname from fault_code c where c.code=?", faultCode);
				if(codeList!=null&&codeList.size()>0){
					map.put("fault_comment", codeList.get(0));
				}			
			}
			result.add(map);
			
		}
		pager.setTotalSize(list.size());
		return result;
	}
	/**
	 * 查询所有车牌号码
	 * @return
	 */
	public List getLicense(){
		String sql="select distinct license from meb_user m where m.license is not NULL and valid='1'";
		return this.getDao().queryBySQL(sql, null);
	}
	public List getObdSN(){
		String sql="select distinct ObdSN from meb_user m where m.ObdSN is not NULL and valid='1'";
		return this.getDao().queryBySQL(sql, null);
	}
	/**
	 * 根据表的字段-值查询车主信息
	 * @param mobileNumber
	 * @param license
	 * @param obdSN
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MebUser> queryAll(String mobileNumber,String license,String obdSN,Pager pager){
	
		String hql = "from MebUser where 1=1 and valid='1'";
		String count = "select count(regUserId) ";
		
		if(null != mobileNumber && mobileNumber.trim().length()>0)
		{
			hql += "  and  mobileNumber like '%"+mobileNumber.trim()+"%'";
		}
		if(null != license && license.trim().length()>0)
		{
			hql += "  and  license like '%"+license.trim()+"%'";
		}
		if(null != obdSN && obdSN.trim().length()>0)
		{
			hql += "  and  obdSN like '%"+obdSN.trim()+"%'";
		}
		count = count+hql;
		List<Long> counts = (List<Long>)this.getDao().findByHql(count, null);
		Long totalSize = (counts == null || counts.isEmpty()) ? 0l : counts.get(0);
		pager.setTotalSize(totalSize);
		
		hql += "order by regUserId desc";
		return (List<MebUser>) this.getDao().findByHql(hql, pager);
	}
	/**
	 * 根据ID查询车主信息
	 * @param regUserId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MebUser queryById(String regUserId){
		String hql="from MebUser where 1=1 and regUserId=? and valid='1'";	
		List<MebUser> list=this.getDao().queryByHQL(hql, regUserId);
		MebUser mebUser= list.size() > 0 ? list.get(0) : null;
		if(mebUser!=null && !StringUtils.isEmpty(mebUser.getLicense())){
			mebUser.setCity(mebUser.getLicense().substring(0, 1));
			mebUser.setLetter(mebUser.getLicense().substring(1, 2));
			mebUser.setNumber(mebUser.getLicense().substring(2, mebUser.getLicense().length()));
		}
		
		return mebUser;
	}
	/**
	 * 根据车辆类型查询车主信息
	 * @param makeName
	 * @param modelName
	 * @param typeName
	 * @param pager
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MebUser> queryUserBycarInfo(String makeName,String modelName,String typeName,Pager pager){
		String sql="select m.* from meb_user m,car_type t,car_info i where m.carId=i.carId and i.cartype_id=t.cartype_id and m.valid='1' and i.valid='1'";
		
		if(makeName!=null&&makeName.trim().length()>0&&!makeName.equals("1")){
			sql+=" and t.make_name='"+makeName+"'";
		}
		if(modelName!=null&&modelName.trim().length()>0&&!modelName.equals("1")){
			sql+=" and t.model_name='"+modelName+"'";
		}
		if(typeName!=null&&typeName.trim().length()>0&&!typeName.equals("1")){
			sql+=" and t.type_name='"+typeName+"'";
		}
		List list= this.getDao().findBySql(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
		List<MebUser> userList=new ArrayList<MebUser>();
		for (int i = begin; i < list.size()&&i<end; i++) {
			Object[] object=(Object[]) list.get(i);
			String regUserId=(String) object[0];
			String mobileNumber=(String) object[1];
			String obdSN=(String) object[2];
			String carId=(String) object[3];
			String license=(String) object[4];
			String name=(String) object[5];
			String sex=(String) object[6];
			String password=(String) object[8];
			String payPassword=(String) object[9];
			String valid=(String) object[10];
			
			MebUser mebUser=new MebUser();
			mebUser.setRegUserId(regUserId);
			mebUser.setMobileNumber(mobileNumber);
			mebUser.setObdSN(obdSN);
//			mebUser.setCarId(carId);
			mebUser.setLicense(license);
			mebUser.setName(name);
			mebUser.setSex(sex);
			if(object[7]==null||object[7]==""){
				mebUser.setUserType(null);
			}else{
				mebUser.setUserType((Integer)object[7]);
			}
			mebUser.setPassword(password);
			mebUser.setPayPassword(payPassword);
			mebUser.setValid(valid);
			userList.add(mebUser);
			System.out.println(userList);
		}
		pager.setTotalSize(list.size());
		return userList;
	}
	public static void main(String[] args) {
		Integer.parseInt(String.valueOf(""));
	}
	/**
	 * 查询车主信息
	 * @param moibleNumber手机号码
	 * @return
	 */
	public MebUser queryByMobileNumber(String mobileNumber){
		String hql="from MebUser where 1=1 and mobileNumber=? and valid='1'";
		List<MebUser> list=this.getDao().queryByHQL(hql, mobileNumber);
		MebUser mebUser= list.size() > 0 ? list.get(0) : null;
		return mebUser;
	}
	/**
	 * 查询车主信息
	 * @param 根据obdSn号码
	 * @return
	 */
	public MebUser queryByObdSn(String obdSn){
		String hql="from MebUser where 1=1 and obdSN=? and valid='1'";
		List<MebUser> list=this.getDao().queryByHQL(hql, obdSn);
		MebUser mebUser= list.size() > 0 ? list.get(0) : null;
		return mebUser;
	}
	/**
	 * 查询车主信息
	 * @param 根据obdSn号码
	 * @return
	 */
	public List<MebUser> getAllMebUser(){
		String hql="from MebUser where 1=1 and valid='1'";
		List<MebUser> list=this.getDao().queryByHQL(hql);
		return list;
	}

	public MebUser queryUserByMobile(String userName) {
		return getMebUserDao().queryUserByMobile(userName);
	}
	
	public boolean isHasOBDSN(String obdSn) {
		return ((MebUserDao)getDao()).isHasOBDSN(obdSn);
	}
	
	public MebUser queryLastByParam(Map<String,Object> map){
		return getMebUserDao().queryLastByParam(map);
	}
	
}
