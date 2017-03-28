package com.hgsoft.system.service;


import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
import com.hgsoft.system.dao.AdminDao;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.Module;
import com.hgsoft.system.entity.Role;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@SuppressWarnings({ "rawtypes","unchecked"})
public class AdminService extends BaseService<Admin> {
	
	@Resource
	public void setDao(AdminDao dao) {
		super.setDao(dao);
	}

	public AdminDao getAdminDao() {
		return (AdminDao) this.getDao();
	}
	
	public void updateAdmin4Disabled(Integer id)
	{
		Admin temp = this.find(id);
		if(temp != null)
			temp.setValid("0");
	}
	
	public void updateAdmin4Enable(Integer id)
	{
		Admin temp = this.find(id);
		if(temp != null)
			temp.setValid("1");
	}
	

	public Admin check(Admin admin) {
		String username = admin.getUsername().trim();
		String password = admin.getPassword().trim();
		List<Admin> list = getDao().findAll(Order.asc("id"), Property.eq("username", username), Property.eq("password", password));
		if (list.size() > 0)
			return (Admin) list.get(0);
		else
			return null;
	}

	public List<Admin> findAllMajor() {
		return getDao().findAll(Order.asc("name"), Property.isNotEmpty("majors"));
	}

	
	public String getFunctions(Admin admin) {
		String functions = "";
		Role role = admin.getRole();
		if (role != null && role.getId() > 0) {
			Set modules = role.getModules();
			if (!modules.isEmpty()) {
				Iterator mit = modules.iterator();
				while (mit.hasNext()) {
					Module module = (Module) mit.next();
					functions = functions + ";" + module.getUrl() + ";" + module.getFunctions();
				}
			}

		}
		return functions;
	}

	/**
	 * 总审核人数
	 * 
	 * @return
	 */
	public Integer checkCount() {
		List list = this.getDao().findAll(Order.asc("id"), new Property[] { Property.isNotEmpty("majors") });
		if (list.size() > 0)
			return list.size();
		else
			return 0;
	}

	/**
	 * 根据审核id获取审核人员
	 * 
	 * @param checkId
	 * @return
	 */
	public Admin findAdminByCheckId(Integer checkId) {
		List<Admin> list = this.getDao().findAll(Order.asc("id"), Property.eq("id", checkId));
		if (list.size() > 0) {
			return list.get(0);
		} else
			return null;
	}

	public List<Admin> query(Pager pager,Admin admin,Admin operator) {
		String valid = admin.getValid();
		String name = admin.getName();
		String username = admin.getUsername();
		
		String hql = "from Admin where 1=1";
		String count = "select count(id) ";
		if(null != valid && !"".equals(valid))
		{
			hql += " and valid="+valid.trim();
		}
		if(null != name && !"".equals(name))
		{
			hql += " and name like '%"+name.trim()+"%'";
		}
		if(null != username && !"".equals(username))
		{
			hql += " and username like '%"+username.trim()+"%'";
		}
		count = count+hql;
		List<Long> counts = (List<Long>)getDao().findByHql(count, null);
		Long totalSize = (counts == null || counts.isEmpty()) ? 0l : counts.get(0);
		pager.setTotalSize(totalSize);
		
		hql += "order by id desc";
		return (List<Admin>)this.getAdminDao().findByHql(hql, pager);
	}

	public boolean usernameIsExists(Admin admin) {

		if (admin == null) {
			return false;
		}

		String name = admin.getUsername();

		if (name == null || name.isEmpty()) {
			return false;
		}

		List<Admin> adminList = this.getDao().findAll(Order.desc("id"), Property.eq("username", name));

		Integer id = admin.getId();

		if (adminList != null && !adminList.isEmpty()) {

			if (id != null) {
				for (Admin temp : adminList) {
					if (id == temp.getId()) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean isExistsByName(Admin admin) {
		if (null == admin || admin.getName() == null) {
			return false;
		}
		List list = getDao().findAll(Order.asc("id"), Property.eq("name", admin.getName()));
		if (list != null && list.size() > 0) {
			Admin tempAdmin = (Admin) list.get(0);
			if (admin.getName().equals(tempAdmin.getName())) {
				return true;
			}
		}
		return false;
	}
	
	//验证用户登录名是否存在
	public Admin getAdminByUsername(String adminName) {
		return getAdminDao().getAdminByUsername(adminName);
	}
	public Integer getMaxAdminID() {
		return getAdminDao().getMaxAdminID();
	}

	public List<Admin> findByPager(Pager pager, List<Integer> adminId) {
		if(adminId==null || adminId.size()==0){
			return null;
		}
		StringBuffer hql = new StringBuffer("from Admin where id ");
		if(adminId.size()==1){
			hql.append("= " + adminId.get(0));
		} else {
			hql.append("in (");
			for(int i:adminId){
				hql.append(i+",");
			}
			hql.replace(hql.length()-1, hql.length(),")");
		}
		return getDao().queryByHQL(pager, hql.toString());
	}
}
