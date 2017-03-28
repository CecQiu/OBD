package com.hgsoft.system.dao;

import java.util.List;

import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.system.entity.Admin;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends BaseDao<Admin> {

	//验证用户登录名是否存在
	public Admin getAdminByUsername(String adminName) {
		Admin admin = null;
		if (adminName != null && !"".equals(adminName)) {
			admin = (Admin) uniqueResult("from Admin admin where admin.username='" + adminName + "'");
		}
		//System.out.println("admin:=???"+(admin == null));
		return admin;
	}
	
	public Integer getMaxAdminID() {
		return  (Integer) this.getSession().createSQLQuery("SELECT max(id) FROM tb_admin").list().get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Admin> getAdminByRole(Integer roleId){
		String hql = "select adm.* from tb_Admin adm "
				+ "join tb_Admin_Role ar on adm.id = ar.admin and ar.role = ?";
		Query query = getSession().createSQLQuery(hql).addEntity(Admin.class);
		query.setInteger(0, roleId);
		return query.list();
	}
}
