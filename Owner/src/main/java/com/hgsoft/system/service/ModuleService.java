package com.hgsoft.system.service;


import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;
import com.hgsoft.system.dao.ModuleDao;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.Module;
import com.hgsoft.system.entity.Role;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

@Service
public class ModuleService extends BaseService<Module> {

	Order[] orders = new Order[] { Order.asc("level"), Order.asc("priority") };

	public List<Module> findBySubSystem() {
		return getDao().findAll(orders, Property.like("subsystem", "%KF%"));
	}

	public List<Module> findAll(){
		return getDao().findAll(orders);
	}

	public List<Module> findChildren(Integer id) {
		return getDao().findAll(orders, Property.eq("parent.id", id));
	}

	public List<Module> findByLevel(Integer id) {
		return getDao().findAll(orders, Property.gt("level", id));
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public List<Module> getMenus(HashSet<Module> set) {
		List list = new ArrayList();
		if (set != null && !set.isEmpty()) {
			Iterator it = set.iterator();
			while (it.hasNext()) {
				Module module = (Module) it.next();
				list.add(module.getId());
			}
		}
		return getDao().findAll(orders, Property.eq("display", 1),
				Property.in("id", list));
	}

	public String getCurrentPosition(Integer id) {
		String position = "";
		Module module = getDao().find(id);
		if (module != null) {
			position = module.getName();
			while (module.getParent() != null) {
				module = module.getParent();
				if (module != null) {
					position = module.getName() + " > " + position;
				}
			}
		}
		return position;
	}

	@Resource
	public void setDao(ModuleDao dao) {
		super.setDao(dao);
	}

	public void deleteModule(Module module) {
		((ModuleDao)this.getDao()).deleteRole_Module(module.getId());
		this.getDao().delete(find(module.getId()));
	}

    /**
     * 通过访问的url获取到所属页面的资源
     * @param url
     * @return
     */
    public List<Module> getResource(String url){
       return ((ModuleDao)this.getDao()).getResource(url);
    }

    /**
     * 从登录的Admin中对象中找出所属url的资源
     * @param operator
     * @param url
     * @return
     */
    public List<Module> getResource(Admin operator,String url){
        List<Module> allModule = getAllModule(operator);
        Module parentModule = getModuleByUrl(allModule,url);
        if(parentModule==null){
            Module urlModule = getModuleByUrlFromFunc(allModule,url);
            if(urlModule!=null){
                parentModule=urlModule.getParent();
                if(parentModule==null){
                    return null;
                }
            }else{
                return null;
            }
        }
        List<Module> childResourceList= getChildResource(allModule,parentModule);
        return childResourceList;


    }

    /**
     * 从moduleList中找出url的Module
     * @param moduleList
     * @param url
     * @return
     */
    public Module getModuleByUrl(List<Module> moduleList,String url){
        Iterator<Module> moduleIterator = moduleList.iterator();
        while(moduleIterator.hasNext()){
            Module module = moduleIterator.next();
            if(module.getUrl().contains(url)){
                return module;

            }
        }
        return null;
    }

    /**
     * 从ModuleList中找出function的url
     * @param moduleList
     * @param url
     * @return
     */
    public Module getModuleByUrlFromFunc(List<Module> moduleList,String url){
        Iterator<Module> moduleIterator = moduleList.iterator();
        while(moduleIterator.hasNext()){
            Module module = moduleIterator.next();
            if(module.getFunctions().contains(url)){
                return module;

            }
        }
        return null;
    }

    /**
     * 从moduleList中找出parent是parentModule的资源
     * @param moduleList
     * @param parentModule
     * @return
     */
    public List<Module> getChildResource(List<Module> moduleList,Module parentModule){
        if(parentModule==null){
            return null;
        }
        List<Module> resourceModuleList  = new ArrayList<Module>();
        Iterator<Module> moduleIterator = moduleList.iterator();
        while(moduleIterator.hasNext()){
            Module module = moduleIterator.next();
            if(module.getParent()!=null&&module.getParent().getId().equals(parentModule.getId())&&module.getResource()==Module.Resource.RESOURCE){
                resourceModuleList.add(module);
            }
        }
        return resourceModuleList;
    }

    /**
     * 获取use的拥有的所有Module
     * @param operator
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Module> getAllModule(Admin operator){
        List<Module> allModule = new ArrayList<Module>();
        Role role = operator.getRole();
        if(role != null && role.getId() > 0) {
            Set<Module> moduleSet = role.getModules();
            Iterator<Module> moduleIterator = moduleSet.iterator();
            while (moduleIterator.hasNext()){
                Module module = moduleIterator.next();
                allModule.add(module);
            }
        }
        return allModule;
    }
}
