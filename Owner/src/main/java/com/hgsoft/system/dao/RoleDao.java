package com.hgsoft.system.dao;

import java.util.List;

import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.system.entity.Role;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao extends BaseDao<Role> {

	
	public Role findByRoleCode(String spma) {
		String hql = "from Role where roleCode = ?";
		Query query = getSession().createQuery(hql);
		query.setString(0, spma);
		return (Role) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> findByRoleCode(String... spmas) {
		StringBuffer hql = new StringBuffer("from Role where roleCode in (");
		if(spmas==null || spmas.length==0) {
			return null;
		}
		int j = 0;
		for(int i=0;i<spmas.length;i++) {
			if(spmas[i]!=null && !"".equals(spmas[i])) {
				j++;
				hql.append("?,");
			}
		}
		if(j==0){
			return null;
		}
		hql.replace(hql.length()-1, hql.length(), ")");
		System.out.println(hql.toString());
		Query query = getSession().createQuery(hql.toString());
		j=0;
		for(int i=0;i<spmas.length;i++) {
			if(spmas[i]!=null && !"".equals(spmas[i])) {
				query.setString(j, spmas[i]);
				j++;
			}
		}
		return query.list();
	}

}
