package com.hgsoft.carowner.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.entity.ObdSetting;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.carowner.service.ObdSettingService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Property;
import com.hgsoft.obd.util.SettingType;

/**
 * obd分组
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class ObdGroupAction extends BaseAction {
	private final Log logger = LogFactory.getLog(ObdGroupAction.class);
	@Resource
	private ObdGroupService obdGroupService;
	private List<ObdGroup> obdGroups = new ArrayList<ObdGroup>();
	private ObdGroup obdGroup;
	private String starTime;
	private String endTime;
	
	//上传的文件
	private File obdexcel;
	//保存原始的文件名
	private String obdexcelFileName;
	
	@Resource
	private ObdSettingService obdSettingService;
	

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
		obdGroups = obdGroupService.findByPager(pager,Order.desc("createTime"),Property.eq("valid", "1"));
		return "list";
	}

	// 跳转到新增页面
	public String add() {
		return "add";
	}

	// 保存参数到数据库
	public String save() {
		ObdGroup ogexist=obdGroupService.queryByGroupNum(obdGroup.getGroupNum().trim());
		if(ogexist!=null){
			result = Result.FAIL;
			return "add";
		}
		ObdGroup og = new ObdGroup();
		og.setGroupNum(obdGroup.getGroupNum().trim());
		og.setGroupName(obdGroup.getGroupName().trim());
		og.setRemark(obdGroup.getRemark());
		og.setLongitude(obdGroup.getLongitude().trim());
		og.setLatitude(obdGroup.getLatitude().trim());
		og.setRadius(obdGroup.getRadius().trim());
		og.setCreateTime(new Date());
		og.setValid("1");
		obdGroupService.save(og);
	
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

		String groupNum = obdGroup.getGroupNum();
		String groupName = obdGroup.getGroupName();

		List<Property> list = new ArrayList<Property>();
		if (groupNum != null && !"".equals(groupNum)) {
			list.add(Property.eq("groupNum", groupNum));
		}
		if (groupName != null && !"".equals(groupName)) {
			list.add(Property.like("groupName", "%"+groupName.trim()+"%"));
		}
		if (starTime != null && !"".equals(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (endTime != null && !"".equals(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		list.add(Property.eq("valid", "1"));
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		obdGroups = obdGroupService.findByPager(pager,Order.desc("createTime"), propertyArr);
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

		obdGroup = obdGroupService.find(obdGroup.getId());
		return "edit";
	}

	// 修改一条记录
	public String update() {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		this.pager.setCurrentPage(session.getAttribute("currentPage").toString().trim());
		this.pager.setPageSize(session.getAttribute("pageSize").toString().trim());
		this.pager.setRowIndex(session.getAttribute("rowIndex").toString().trim());

		try {
			//根据id查询对象
			ObdGroup og = obdGroupService.find(obdGroup.getId());
//			og.setGroupNum(obdGroup.getGroupNum().trim());
			og.setGroupName(obdGroup.getGroupName().trim());
			og.setLongitude(obdGroup.getLongitude().trim());
			og.setLatitude(obdGroup.getLatitude().trim());
			og.setRadius(obdGroup.getRadius().trim());
			og.setRemark(obdGroup.getRemark().trim());
			obdGroupService.update(og);
			message="updateSuccess";
		} catch (Exception e) {
			e.printStackTrace();
			message="updateFail";
			return "edit";
		}
		
		// 分页获取对象
		obdGroups = obdGroupService.findByPager(pager,Order.desc("createTime"),Property.eq("valid", "1"));
		return "list";
	}
	
	
	// 根据参数id删除一条参数记录
	public String delete() {
		obdGroup = obdGroupService.find(obdGroup.getId());
		if(null != obdGroup) {
			obdGroup.setValid("0");
			obdGroup.setUpdateTime(new Date());
			obdGroupService.update(obdGroup);
			message="deleteSuccess";
		}
		// 分页获取对象
		obdGroups = obdGroupService.findByPager(pager,Order.desc("createTime"),Property.eq("valid", "1"));
		return "list";
	}
	
	// 跳转页面
	public String toObdUnRegExcel() {
		obdGroup = obdGroupService.find(obdGroup.getId());
		return "obdUnRegExcel";
	}
		
	/**
	 * 未激活设备设置默认分组——》excel导入
	 */
	public String obdUnRegExcel() {
		logger.info("----------------【未激活设备设置默认分组】---------------");
		obdGroup = obdGroupService.find(obdGroup.getId());
		
		String msg = "保存成功.";
		
		try {
			//1
			String ftype=obdexcelFileName.substring(obdexcelFileName.indexOf("."),obdexcelFileName.length());
			//2读取excel文档保存入库
			Workbook workbook = null;
			if(".xls".equals(ftype)){
				workbook = new HSSFWorkbook(new FileInputStream(obdexcel));
			}else if(".xlsx".equals(ftype)){
					workbook = new XSSFWorkbook(new FileInputStream(obdexcel));
			}else{
				this.message="请求失败,excel文件后缀名有误,请检查---"+ftype;
				return "obdUnRegExcel";
			}
			List<ObdSetting> obdSettingList = new ArrayList<ObdSetting>();
			//循环工作表Sheet
			Map<String, Integer> obdSns = new HashMap<String, Integer>();
			for (int numSheet = 0;numSheet<workbook.getNumberOfSheets();numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
			     for (Row row : sheet) {
//		        	//第一行是标题行
//		        	if(row.getRowNum()==0){
//		        		continue;
//		        	}
			    	if(row.getCell(0)==null){
			    		throw new Exception("excel格式有误,请联系管理员.");
			    	}
			    	
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					String obdSn = row.getCell(0).getStringCellValue();
					if(StringUtils.isEmpty(obdSn)){
						continue;
					}
					obdSn=obdSn.trim().toLowerCase();//去掉两边和都转成小写
					if(!obdSns.containsKey(obdSn)){
						obdSns.put(obdSn.trim(), 1);
					}else{
						int val=obdSns.get(obdSn)+1;
						obdSns.put(obdSn, val);
					}
					//保存在obdSetting表里
					JSONObject jb = new JSONObject();
					jb.put("groupNum", obdGroup.getGroupNum());
					ObdSetting obdSetting = new ObdSetting();
					obdSetting.setId(IDUtil.createID());
					obdSetting.setObdSn(obdSn.trim().toLowerCase());
					obdSetting.setType(SettingType.OBDDEFAULT_00.getValue());
					obdSetting.setSettingMsg(jb.toString());
					obdSetting.setCreateTime(new Date());
					obdSetting.setValid("1");
					obdSettingList.add(obdSetting);
				}
			}
			
			if(obdSettingList==null || obdSettingList.size()==0){
				msg= "请求失败_文件为空,请检查.";
				this.message=msg;
				return "obdUnRegExcel";
			}
			
			//excel重复
			//2.先判断excel里是否有重复记录
			for (String key : obdSns.keySet()) {
				Integer total = obdSns.get(key);
				if(total>1){
					msg ="请求失败,excel记录重复,"+key+":"+total+"---";
					this.message=msg;
					return "obdUnRegExcel";
				}
				if(key.length()!=8){
					msg ="请求失败,"+key+":设备号长度有误---";
					this.message=msg;
					return "obdUnRegExcel";
				}
				if(!key.matches("^[a-z0-9A-Z]+$")){
					msg ="请求失败,"+key+":设备号有误---";
					this.message=msg;
					return "obdUnRegExcel";
				}
			}
			
			//判断是否存在重复记录
			List<ObdSetting> list=obdSettingService.getOSListByMap(obdSns, "obdDefault_00");
			if(list.size()>0){
				StringBuffer sb = new StringBuffer("");
				for (ObdSetting obdSetting : list) {
					sb.append(obdSetting.getObdSn()+",");
				}
				
				this.message=sb.toString()+",这些设备存在默认分组记录,请检查,请求失败.";
				return "obdUnRegExcel";
			}
			
			for (ObdSetting obdSetting : obdSettingList) {
				obdSettingService.obdSettingSave(obdSetting);
			}
			this.message="保存成功.";
		} catch (Exception e) {
			e.printStackTrace();
			this.message="请求异常,异常信息------"+e;
			return "obdUnRegExcel";
		}
		return "obdUnRegExcel";
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

	public List<ObdGroup> getObdGroups() {
		return obdGroups;
	}

	public void setObdGroups(List<ObdGroup> obdGroups) {
		this.obdGroups = obdGroups;
	}

	public ObdGroup getObdGroup() {
		return obdGroup;
	}

	public void setObdGroup(ObdGroup obdGroup) {
		this.obdGroup = obdGroup;
	}

	public File getObdexcel() {
		return obdexcel;
	}

	public void setObdexcel(File obdexcel) {
		this.obdexcel = obdexcel;
	}

	public String getObdexcelFileName() {
		return obdexcelFileName;
	}

	public void setObdexcelFileName(String obdexcelFileName) {
		this.obdexcelFileName = obdexcelFileName;
	}

}
