package com.hgsoft.system.dao;

import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.system.entity.Module;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModuleDao extends BaseDao<Module> {

	public void deleteRole_Module(Integer id) {
		this.getSession().createSQLQuery("delete from tb_role_module where module_id="+id).executeUpdate();
		
	}

    /**
     * 通过访问的url获取到所属页面的资源
     * @param url
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Module> getResource(String url){
        String sql = "select a.* from tb_module a left join tb_module b on a.parent = b.id where b.url = :url";
        SQLQuery query = this.getSession().createSQLQuery(sql);
        query.setString("url",url);
        List<Module> list = query.addEntity(Module.class).list();
        return list;
    }

}