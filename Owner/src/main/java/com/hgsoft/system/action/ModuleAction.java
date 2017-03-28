package com.hgsoft.system.action;

import com.hgsoft.common.action.BaseAction;
import com.hgsoft.system.entity.Module;
import com.hgsoft.system.service.ModuleService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author liujiefeng
 * @date May 19, 2010
 * @Description 系统功能模块管理
 */

@Controller
@Scope("prototype")
@SuppressWarnings( { "rawtypes", "unchecked"})
public class ModuleAction extends BaseAction {

	private Module module;

	@Resource
	private ModuleService moduleService;

	public String save() {
        module.setFunctions(module.getFunctions().replaceAll("\\s+",""));//替换掉所有的符号
        if (module.getParent() != null && (module.getParent().getId() < 1)) {
			module.setParent(null);
		} else {
			module.setParent(moduleService.find(module.getParent().getId()));
		}

		if (module.getParent() == null) {
			module.setLevel(1);
		} else {
			module.setLevel(module.getParent().getLevel() + 1);
		}

		moduleService.save(module);
		return edit();
	}

	public String update() {
        module.setFunctions(module.getFunctions().replaceAll("\\s+",""));//替换掉所有的符号
		if (module.getParent() != null && (module.getParent().getId() < 1)) {
			module.setParent(null);
		} else {
			if (module.getId().equals(module.getParent().getId()))
				return edit();
			module.setParent(moduleService.find(module.getParent().getId()));
		}

		Integer level = module.getLevel();
		if (module.getParent() == null) {
			module.setLevel(1);
		} else {
			module.setLevel(module.getParent().getLevel() + 1);
		}
		moduleService.update(module);
		if (!level.equals(module.getLevel())) {
			// 更新module子节点level
			list = moduleService.findByLevel(level);
			updateLevel(list, module.getId(), module.getLevel());
		}
		return edit();
	}

	private void updateLevel(List list, Integer parent, int level) {
		for (int i = 0; i < list.size(); i++) {
			Module module = (Module) list.get(i);
			if (module.getParent() != null
					&& module.getParent().getId().equals(parent)) {
				module.setLevel(level + 1);
				moduleService.update(module);
				updateLevel(list, module.getId(), level + 1);
			}
		}
	}

	public String delete() {
		moduleService.deleteModule(module);
		return edit();
	}

	public String edit() {
		list = moduleService.findAll();
		if (list != null) {
			// 排序 list，把每个结点对应的下级结点相邻，采用循环查找方法
			for (int i = 0; i < list.size(); i++) {
				Integer currentId = ((Module) list.get(i)).getId();
				int count = list.size() - i - 1;
				for (int j = i + 1; count > 0 && j < list.size(); j++) {
					Module m = (Module) list.get(j);
					// 若此结点不是currentId的下层结点，则放到放到list最后
					if (m.getParent() == null) {
						list.remove(j);
						list.add(m);
						j--;
					} else if (!m.getParent().getId().equals(currentId)) {
						list.remove(j);
						list.add(m);
						j--;
					}
					count--;
				}
			}
		}
		return EDIT;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
}
