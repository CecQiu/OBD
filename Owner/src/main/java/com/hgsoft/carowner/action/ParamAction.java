package com.hgsoft.carowner.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.service.DictionaryService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;

/**
 * 系统参数配置
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ParamAction extends BaseAction {
	private final Log logger = LogFactory.getLog(ParamAction.class);
	@Resource
	private DictionaryService dictionaryService;
	private Dictionary dictionary;
	private List<Dictionary> dictionarys = new ArrayList<Dictionary>();
	private String starTime;
	private String endTime;

	// 列表展示
	public String list() {
		//清除缓存
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		// 分页获取对象
		dictionarys = dictionaryService.findByPager(pager,
				Order.desc("createTime"));
		return "list";
	}

	// 跳转到新增页面
	public String add() {
		return "add";
	}

	// 保存参数到数据库
	public String save() {
		Dictionary dic = new Dictionary();
		dic.setId(IDUtil.createID());
		dic.setCode(dictionary.getCode().trim());
		dic.setShowValue(dictionary.getShowValue().trim());
		dic.setTrueValue(dictionary.getTrueValue().trim());
		dic.setType(dictionary.getType().trim());
		dic.setRemark(dictionary.getRemark().trim());
		dic.setCreateTime(new Date());
		dic.setState("T");
		dictionaryService.save(dic);
		result = Result.SUCCESS;
		return list();
	}

	// 从数据库中查询数据
	public String query() {
		//清除缓存
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");

		String showValue = dictionary.getShowValue();
		String trueValue = dictionary.getTrueValue();
		String code = dictionary.getCode();
		String type = dictionary.getType();
		String remark = dictionary.getRemark();

		List<Property> list = new ArrayList<Property>();
		if (showValue != null && !"".equals(showValue)) {
			list.add(Property.like("showValue", "%"+showValue.trim()+"%"));
		}
		if (trueValue != null && !"".equals(trueValue)) {
			list.add(Property.like("trueValue", "%"+trueValue.trim()+"%"));
		}
		if (code != null && !"".equals(code)) {
			list.add(Property.like("code", "%"+code.trim()+"%"));
		}
		if (type != null && !"".equals(type)) {
			list.add(Property.like("type", "%"+type.trim()+"%"));
		}
		if (remark != null && !"".equals(remark)) {
			list.add(Property.like("remark", "%"+remark.trim()+"%"));
		}
		if (starTime != null && !"".equals(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (endTime != null && !"".equals(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		dictionarys = dictionaryService.findByPager(pager,Order.desc("createTime"), propertyArr);
		return "list";
	}
	
	//编辑
	public String edit() {
		//设置当前页
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		session.setAttribute("currentPage", this.pager.getCurrentPage());
		session.setAttribute("pageSize", this.pager.getPageSize());
		session.setAttribute("rowIndex", this.pager.getRowIndex());

		dictionary = dictionaryService.find(dictionary.getId());
		return "edit";
	}

	// 修改一条记录
	public String update() {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		this.pager.setCurrentPage(session.getAttribute("currentPage").toString().trim());
		this.pager.setPageSize(session.getAttribute("pageSize").toString().trim());
		this.pager.setRowIndex(session.getAttribute("rowIndex").toString().trim());

		//根据id查询对象
		Dictionary dic = dictionaryService.find(dictionary.getId());
		dic.setCode(dictionary.getCode().trim());
		dic.setType(dictionary.getType().trim());
		dic.setShowValue(dictionary.getShowValue());
		dic.setTrueValue(dictionary.getTrueValue());
		dic.setRemark(dictionary.getRemark().trim());
		dictionaryService.update(dic);
		message="updateSuccess";
		
		// 分页获取对象
		dictionarys = dictionaryService.findByPager(pager,
				Order.desc("createTime"), Order.asc("code"));
		return "list";
	}
	
	
	// 根据参数id删除一条参数记录
	public String delete() {
		Dictionary dic = dictionaryService.find(dictionary.getId());
		if(null != dic) {
			dictionaryService.delete(dic);
			message="deleteSuccess";
		}
		// 分页获取对象
		dictionarys = dictionaryService.findByPager(pager,
				Order.desc("createTime"), Order.asc("code"));
		return "list";
	}

	
	public List<Dictionary> getDictionarys() {
		return dictionarys;
	}

	public void setDictionarys(List<Dictionary> dictionarys) {
		this.dictionarys = dictionarys;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
