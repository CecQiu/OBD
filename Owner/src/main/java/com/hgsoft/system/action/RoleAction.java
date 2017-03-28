package com.hgsoft.system.action;
import com.hgsoft.common.action.BaseAction;

import com.hgsoft.system.entity.Module;
import com.hgsoft.system.entity.Role;
import com.hgsoft.system.service.ModuleService;
import com.hgsoft.system.service.RoleService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author liujiefeng
 * @date May 19, 2010
 * @Description 角色权限管理
 */

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction {

    private Role role;
    @Resource
    private RoleService roleService;
    @Resource
    private ModuleService moduleService;

    private String nameIsExists;

    public RoleAction() {
        role = new Role();
    }

    public String save() {
        if(roleService.nameIsExists(role)) {
            nameIsExists = "名称已存在！";
            return add();
        }
        roleService.save(role);
        message = "操作成功！";
        return list();
    }

    public String update() {
        if(roleService.nameIsExists(role)) {
            nameIsExists = "名称已存在！";
            return edit();
        }
        roleService.update(role);
        message = "操作成功！";
        return list();
    }

    public String delete() {
        roleService.deleteById(role.getId());
        return list();
    }

    public String add() {
        list = moduleService.findAll();
        return ADD;
    }

    public String edit() {
        role = roleService.find(role.getId());
        list = moduleService.findAll();
        return EDIT;
    }

    public String list() {
        list = roleService.findByPager(pager);
        return LIST;
    }

    public Role getRole() {
        return role;
    }

    public String getNameIsExists() {
        return nameIsExists;
    }

    public void setNameIsExists(String nameIsExists) {
        this.nameIsExists = nameIsExists;
    }

    @SuppressWarnings("unchecked")
    public void setModules(String modules) {
        if (modules != null) {
            modules = modules.replace("，", ",").replace(" ", "");
            String[] ids = modules.split(",");
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    Integer j = new Integer(ids[i]);
                    if (j > 0) {
                        Module module = new Module();
                        module.setId(j);
                        role.getModules().add(module);
                    }
                }
            }
        }
    }
}
