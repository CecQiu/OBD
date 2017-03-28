package com.hgsoft.system.service;


import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;
import com.hgsoft.system.dao.RoleDao;
import com.hgsoft.system.entity.Role;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

@Service
public class RoleService extends BaseService<Role> {
	
	@Resource
	public void setDao(RoleDao dao){
		super.setDao(dao);
	}
	
	public boolean nameIsExists(Role role) {
	    
	    if(role == null) {
		return false;
	    }
	    
	    String name = role.getName();
	    
	    if(name == null || name.isEmpty()) {
		return false;
	    }
	    
	    List<Role> roleList = this.getDao().findAll(Order.desc("id"), Property.eq("name", name));
	    
	    Integer id = role.getId();
	    
	    if(roleList != null && !roleList.isEmpty()) {
		
		if(id != null) {
		    for(Role temp : roleList) {
			if(id == temp.getId()) {
			    return false;
			}
		    }
		}
		return true;
	    }
	    return false;
	}
	
	@SuppressWarnings("rawtypes")
	public List queryByHQL(String hql){
		return getDao().queryByHQL(hql);
	}

	public List<Role> findByRoleCode(String... spmas) {
		return ((RoleDao)getDao()).findByRoleCode(spmas);
	}

	public Role findByRoleCode(String spma) {
		return ((RoleDao)getDao()).findByRoleCode(spma);
	}
	

	
	/**
	 * 检查用户是否拥有某个角色
	 * @param adminId
	 * @param roleCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkRole(Integer adminId,String roleCode){
		String sql = "select tr.* from tb_Role tr,tb_Admin_Role tar where tar.role=tr.id and tr.roleCode=? and tar.admin=?";
		Query q = this.getDao().getSession().createSQLQuery(sql).addEntity(Role.class);
		q.setString(0, roleCode);
		q.setInteger(1, adminId);
		List<Role> list = q.list();
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}
}
