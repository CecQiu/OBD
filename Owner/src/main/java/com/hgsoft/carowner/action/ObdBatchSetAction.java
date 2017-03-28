package com.hgsoft.carowner.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.ObdBatchSet;
import com.hgsoft.carowner.service.ObdBatchSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;

/**
 *obd批量设置信息
 * @author ljl
 */
@Controller
@Scope("prototype")
public class ObdBatchSetAction extends BaseAction {
	@Resource
	private ObdBatchSetService obdBatchSetService;
	private List<ObdBatchSet> obdBatchSets = new ArrayList<ObdBatchSet>();

	private String startTime;
	private String endTime;
	private String id;
	private String obdSn;
	private String obdMSn;
	private String type;
	private String version;
	private String auditState;
	private String success;
	private String sendedCount;
	private String valid;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 展示obd设备升级的文件列表
	 * @throws ParseException 
	 */
	public String list() {
		obdBatchSets = obdBatchSetService.findByPager(pager,Order.desc("createTime"));
		return "list";
	}
	
	
	// 从数据库中查询数据
		public String query() throws ParseException {
			this.obdBatchSets = getList(pager);
			return "list";
		}

		
		/*
		 * 返回位置查询结果
		 */
		public List<ObdBatchSet> getList(Pager pager){		
			
			String obdSN = "";//表面号解析成设备号
			if (!StringUtils.isEmpty(obdMSn)) {
				//将表面号解析成设备号
				try {
					obdSN = StrUtil.obdSnChange(obdMSn);// 设备号
					if(StringUtils.isEmpty(obdSN)){
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdSN)){
				if(!obdSn.equals(obdSN)){
					return null;
				}else{
					obdSN = "";
				}
			}
			
			
			Map<String, Object> map = new HashMap<>();
			Integer total=0;
			if(!StringUtils.isEmpty(obdSn)){
				map.put("obdSn", obdSn);
				total++;
			}
			if(!StringUtils.isEmpty(obdSN)){
				map.put("obdSn", obdSN);
				total++;
			}
			if(!StringUtils.isEmpty(startTime)){
				map.put("startTime", startTime);
				total++;
			}
			if(!StringUtils.isEmpty(endTime)){
				map.put("endTime", endTime);
				total++;
			}
			
			if(!StringUtils.isEmpty(type)){
				map.put("type", type);
				total++;
				
			}
			if(!StringUtils.isEmpty(version)){
				map.put("version", version);
				total++;
			}
			if(!StringUtils.isEmpty(auditState)){
				map.put("auditState", Integer.parseInt(auditState));
				total++;
			}
			if(!StringUtils.isEmpty(sendedCount)){
				map.put("sendedCount", Integer.parseInt(sendedCount));
				total++;
			}
			if(!StringUtils.isEmpty(success)){
				map.put("success", Integer.parseInt(success));
				total++;
			}
			if(!StringUtils.isEmpty(valid)){
				map.put("valid",Integer.parseInt(valid));
				total++;
			}
			
			map.put("paramsTotal", total);
			List<ObdBatchSet> obdBatchSets = obdBatchSetService.queryByParams(pager,map);
			return obdBatchSets;
		}
		
		
	/**
	 * 审核
	 */
	public void audit(){
		Map<String, Object> map = new HashMap<>();
		Integer total=0;
		if(!StringUtils.isEmpty(version)){
			map.put("version", version);
			total++;
		}
		map.put("valid", 1);
		total++;
		map.put("paramsTotal", total);
		Long sum=obdBatchSetService.getTotalByParams(map);
		if(sum==null || sum==0){
			outMessage("1");
		}else{
			Date date=new Date();
			Map<String, Object> map2 = new HashMap<>();
			map2.put("version", version);
			map2.put("valid", 1);
			map2.put("auditOper1",getOperator().getName());
			map2.put("auditState1", Integer.parseInt(auditState));
			map2.put("auditTime1",date);
			map2.put("updateTime1",date);
			int i=obdBatchSetService.updByParams(map2);
			outMessage("0");
		}
	}
	/**
	 * 删除全部（筛选）
	 * 先判断是否有不符合删除的,如果有,全部不给删除
	 * 如果升级服务器待升级删除异常，本地也不能删除.
	 * 删除待升级列表
	 * 1.下发升级请求，无升级结果,不给删
	 * 2.下发。。。，有升级结果,可以删
	 * 3.无下发,可以删
	 */
	public void delAll(){}
	
	/**
	 * 删除全部（筛选）
	 * 先判断是否有不符合删除的,如果有,全部不给删除
	 * 如果升级服务器待升级删除异常，本地也不能删除.
	 * 删除待升级列表
	 * 1.下发升级请求，无升级结果,不给删
	 * 2.下发。。。，有升级结果,可以删
	 * 3.无下发,可以删
	 */
	public void adminDelAll(){
		
		try {
			//查找是否有未升级成功且有效的记录
			Map<String, Object> map = new HashMap<>();
			Integer total=0;
			if(!StringUtils.isEmpty(version)){
				map.put("version", version);
				total++;
			}
			map.put("valid", 1);
			total++;
			map.put("paramsTotal", total);
			Long sum=obdBatchSetService.getTotalByParams(map);
			
			if(sum==null || sum==0){
				outMessage("没有可删除的记录,请核查.");
				return;
			}
			//根据版本号删除
			Map<String, Object> map2 = new HashMap<>();
			if(!StringUtils.isEmpty(valid)){
				map2.put("valid", Integer.parseInt(valid));
			}
			if(!StringUtils.isEmpty(version)){
				map2.put("version", version);
			}
			Integer total2=obdBatchSetService.delByParams(map2);
			outMessage("删除成功,总数："+total2);
		} catch (Exception e) {
			e.printStackTrace();
			outMessage("系统异常,请联系管理员---"+e);
		}
		
		
	}
	
	/**
	 * 删除单条
	 * 删除待升级列表
	 * 1.下发升级请求，无升级结果,不给删
	 * 2.下发。。。，有升级结果,可以删
	 * 3.无下发,可以删
	 */
	public void del(){}
	 
	
	/**
	 * 管理员删除单条
	 * 删除待升级列表
	 * 1.是否有推送到升级服务器，如果有则先删除升级服务器的记录
	 * 如果没有推送到升级服务器，则删除本地记录即可。
	 */
	public void adminDel(){
		boolean flag=obdBatchSetService.del(id);
		if(flag){
			outMessage("删除成功.");
		}else{
			outMessage("删除失败.");
		}
	}
	 
	
	public String exportExcel(){
		String[] headers={"ID","设备号","类型","版本号","审核人","审核时间","审核状态","下发次数","成功","创建时间","更新时间","有效"};
		String[] cloumn={"id","obdSn","type","version","auditOper","auditTime","auditState","sendedCount","success","createTime","updateTime","valid"}; 
		String fileName="设备批量设置信息.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		obdBatchSets =  getList(null);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (ObdBatchSet obs : obdBatchSets) {
			HashMap<String, Object> map = new  HashMap<>();
			map.put("id",obs.getId());
			map.put("obdSn",obs.getObdSn());
			map.put("type",obs.getType());
			map.put("version",obs.getVersion());
			map.put("auditOper",obs.getAuditOper());
			map.put("auditTime",obs.getAuditTime());
			map.put("auditState",obs.getAuditState());
			map.put("sendedCount",obs.getSendedCount());
			map.put("success",obs.getSuccess());
			map.put("createTime",obs.getCreateTime());
			map.put("updateTime",obs.getUpdateTime());
			map.put("valid",obs.getValid());
			lists.add(map);
		}
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("设备批量信息", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	
	public String getObdSn() {
		return obdSn;
	}


	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}


	public String getValid() {
		return valid;
	}


	public void setValid(String valid) {
		this.valid = valid;
	}


	public String getSendedCount() {
		return sendedCount;
	}


	public void setSendedCount(String sendedCount) {
		this.sendedCount = sendedCount;
	}

	public String getSuccess() {
		return success;
	}


	public void setSuccess(String success) {
		this.success = success;
	}



	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	public String getAuditState() {
		return auditState;
	}


	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public List<ObdBatchSet> getObdBatchSets() {
		return obdBatchSets;
	}


	public void setObdBatchSets(List<ObdBatchSet> obdBatchSets) {
		this.obdBatchSets = obdBatchSets;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getObdMSn() {
		return obdMSn;
	}


	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
}
