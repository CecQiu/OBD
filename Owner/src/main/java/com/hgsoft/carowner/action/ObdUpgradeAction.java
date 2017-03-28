package com.hgsoft.carowner.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.api.UpgradeApi;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.entity.ObdMsg;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.carowner.service.ObdMsgService;
import com.hgsoft.carowner.service.ObdUpgradeService;
import com.hgsoft.carowner.service.UpgradeSetService;
import com.hgsoft.carowner.service.VMemberCarService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;

/**
 * obd设备升级-升级文件上载和推送类
 * @author fdf
 */
@Controller
@Scope("prototype")
public class ObdUpgradeAction extends BaseAction {
	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");
	@Resource
	private ObdUpgradeService obdUpgradeService;
	@Resource
	private VMemberCarService vMemberCarService;
	@Resource
	private ObdMsgService obdMsgService;
	@Resource
	private UpgradeSetService upgradeSetService;
	@Resource
	private UpgradeApi upgradeApi;
	
	private String uversion;
	private String firmType;
	private ObdUpgrade obdUpgrade;
	private String upId;
	private int editState;
	private List<ObdMsg> obdMsgList=new ArrayList<ObdMsg>();
	
	@Resource
	private ObdGroupService obdGroupService;
	private List<ObdGroup> obdGroups = new ArrayList<ObdGroup>();
	private ObdGroup obdGroup;
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	private List<OBDStockInfo> obdStockInfos = new ArrayList<OBDStockInfo>();
	
	//上传的文件
	private File obdexcel;
	//保存原始的文件名
	private String obdexcelFileName;
	
	/**
     * obd设备升级文件
     */
    private File updwj;  //上传的文件对象
    private String updwjFileName;  //文件名称  
    private String updwjContentType;  //文件类型
    private OBDStockInfo obdStockInfo;
	
	/**
	 * 展示obd设备升级的文件列表
	 */
	public String listUpgrade() {
		if(pager == null) {
			pager = new Pager();
		}
		if(StringUtils.isEmpty(uversion) && StringUtils.isEmpty(firmType)){
			list = obdUpgradeService.findByPagerCustom(pager,null,null);
		}else{
			list = obdUpgradeService.findByPagerCustom(pager,uversion,firmType);
		}
		return "listUpgrade";
	}
	
	/**
	 * 条件obd设备升级的文件
	 */
	public String addUpgrade() {
		editState = 0;
		return "addUpgrade";
	}
	
	/**
	 * 固件版本号校验
	 */
	public void versionCheck(){
		String version=obdUpgrade.getVersion();
		if(StringUtils.isEmpty(version)){
			outJsonMessage("{\"status\":\"fail\",\"message\":\"版本号为空.\"}");
		}else{
			Integer total=obdUpgradeService.findByVersion(version);
			if(total!=null){
				switch (total) {
				case 0:
					outJsonMessage("{\"status\":\"success\",\"message\":\"版本号校验通过.\"}");
					break;
				default:
					outJsonMessage("{\"status\":\"fail\",\"message\":\"版本号已存在.\"}");
					break;
				}
			}else{
				outJsonMessage("{\"status\":\"fail\",\"message\":\"系统异常.\"}");
			}
		}
	}
	
	/**
	 * 更新
	 */
	public String updateUpgrade() {
		editState = 1;
		obdUpgrade = obdUpgradeService.findByIdCustom(upId);
		return "updateUpgrade";
	}
	
	/**
	 * 保存
	 */
	public String save() {
		if(updwj != null) {
			byte[] fileByteArry = FileUtil.toByteArray(updwj);
			obdUpgrade.setFile(fileByteArry);
			obdUpgrade.setSize((long)fileByteArry.length);
		}
		
		//新增
		if(obdUpgrade.getId() == null || obdUpgrade.getId().trim().equals("")) {
			obdUpgrade.setId(IDUtil.createID());
			obdUpgrade.setAuditSend("0");
			obdUpgrade.setCreateTime(new Date());
			obdUpgrade.setFirmVersion(obdUpgrade.getFirmVersion().toLowerCase());
			obdUpgrade.setValid("1");
			obdUpgradeService.save(obdUpgrade);
			this.message = "保存成功";
		//更新
		} else {
			if(obdUpgrade.getFile() == null) {
				ObdUpgrade temp = obdUpgradeService.find(obdUpgrade.getId());
				if(temp != null) {
					obdUpgrade.setFile(temp.getFile());
				}
			}
			obdUpgradeService.update(obdUpgrade);
			this.message = "修改成功";
		}
		return listUpgrade();
	}
	
	/**
	 * 删除
	 */
	public String deleteUpgrade() {
		upgradeDataLogger.info("----------------【固件删除】---------------");
		try {
			//调用接口删除固件
			obdUpgrade = obdUpgradeService.findByIdCustom(upId);
			String auditSend = obdUpgrade.getAuditSend();
			upgradeDataLogger.info("----------------【固件删除】版本号---"+obdUpgrade.getVersion()+"---是否已推送:"+auditSend);
			String[] res = new String[2];
			if("1".equals(auditSend)){
				res=upgradeApi.fileSent(obdUpgrade, 0);
				upgradeDataLogger.info("----------------【固件删除】结果:"+res);
				if("0".equals(res[0])){
					obdUpgrade.setUpdateTime(new Date());
					obdUpgrade.setValid("0");
					obdUpgradeService.update(obdUpgrade);//删除固件
				}
				this.message = res[1];
			}else{
				obdUpgrade.setUpdateTime(new Date());
				obdUpgrade.setValid("0");
				obdUpgradeService.update(obdUpgrade);//删除固件
//				obdUpgradeService.deleteById(upId);//删除固件
				this.message = "删除成功！";
				upgradeDataLogger.info("----------------【固件删除】结果:成功.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.message = "删除固件异常,原因---"+e;
			upgradeDataLogger.error("----------------【固件删除】异常---"+e);
		}
		return listUpgrade();
	}
	
	/**
	 * 展示要升级推送的obd列表
	 */
	public String listPushObd() {
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
		
		obdUpgrade = obdUpgradeService.findByIdCustom(upId);
		obdGroups = obdGroupService.queryList();
//		obdMsgList = obdMsgService.queryObdMsgList(obdStockInfo);
		if(obdStockInfo!=null){
			String obdSn = obdStockInfo.getObdSn();
			String obdMSn = obdStockInfo.getObdMSn();
			String groupNum = obdStockInfo.getGroupNum();
			String stockState =obdStockInfo.getStockState();

			List<Property> list = new ArrayList<Property>();
			if (!StringUtils.isEmpty(obdSn)) {
				list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
			}
			if (!StringUtils.isEmpty(obdMSn)) {
				list.add(Property.like("obdMSn", "%"+obdMSn.trim()+"%"));
			}
			if (!StringUtils.isEmpty(groupNum)) {
				list.add(Property.eq("groupNum", groupNum.trim()));
			}
			if (!StringUtils.isEmpty(stockState)) {
				list.add(Property.eq("stockState", stockState.trim()));
			}
			
			Property[] propertyArr = new Property[list.size()];
			list.toArray(propertyArr);
			obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"), propertyArr);
		}else{
			obdStockInfos = obdStockInfoService.findByPager(pager,Order.desc("startDate"));
		}
		
		return "listPushObd";
	}
	
	/**
	 * 跳转到固件审核页面
	 */
	public String toCheck() {
		//
		obdUpgrade = obdUpgradeService.findByIdCustom(upId);
		return "toCheck";
	}
	
	/**
	 * 固件审核
	 */
	public String check() {
		//更新固件审核信息
		try {
//			System.out.println(getAccountRole());
			//获取当前用户信息:getOperator()
//			System.out.println(getOperator().getRole());
//			System.out.println(getOperator().getName());
//			System.out.println(getOperator().getUsername());
			
			ObdUpgrade ou = obdUpgradeService.find(upId);
			ou.setAuditOper(getOperator().getUsername());
			ou.setAuditTime(new Date());
			ou.setAuditState(obdUpgrade.getAuditState());//审核结果;
			ou.setAuditMsg(obdUpgrade.getAuditMsg());
			obdUpgradeService.update(ou);
			obdUpgrade = ou;
			//如果审核通过，同时调用接口,推送升级包给升级服务器
			//查询固件信息
//			obdUpgrade = obdUpgradeService.findByIdCustom(upId);
			this.message = "审核完成.";
		} catch (Exception e) {
			e.printStackTrace();
			this.message = "审核失败,请联系管理员.";
		}
		return "toCheck";
	}
	
	/**
	 * 固件推送
	 */
	public String upgradeFileSend() {
		upgradeDataLogger.info("----------------【固件推送】---------------");
		//更新固件审核信息
		try {
			//如果审核通过，同时调用接口,推送升级包给升级服务器
			//查询固件信息
			obdUpgrade = obdUpgradeService.find(upId);
			String[] res = new String[2];
			res=upgradeApi.fileSent(obdUpgrade, 1);
			upgradeDataLogger.info("----------------【固件推送】推送版本---"+obdUpgrade.getVersion()+"----推送结果:"+res);
			//推送成功,更新推送结果
			if("0".equals(res[0])){
				obdUpgrade.setAuditSend("1");
				obdUpgradeService.update(obdUpgrade);
			}else{
				obdUpgrade.setAuditSend("2");//推送失败
				obdUpgradeService.update(obdUpgrade);
			}
			this.message = res[1];
		} catch (Exception e) {
			e.printStackTrace();
			this.message = "审核失败,信息---"+e;
			upgradeDataLogger.info("----------------【固件推送】异常---"+e);
		}
		return listUpgrade();
	}
	
	/**
	 * 升级列表推送
	 * 升级推送成功不能将vaild更为无效，需要在升级结果返回，并且升级结果为成功的情况下，才能将valid更新为无效
	 */
	public String upgradeObdSnsSend() {
		upgradeDataLogger.info("----------------【升级列表推送升级服务器】---------------");
		//更新固件审核信息
		try {
			//如果审核通过，同时调用接口,推送升级包给升级服务器
			//查询固件信息
			obdUpgrade = obdUpgradeService.find(upId);
			upgradeDataLogger.info("----------------【升级列表推送升级服务器】推送版本号---"+obdUpgrade.getVersion());
			//查找该版本号未推送的升级列表
			List<UpgradeSet> upgradeSetList=upgradeSetService.queryByVersionAndSflagAndAstate(obdUpgrade.getVersion(), "0","1");
			
			String[] res = new String[2];
			if(upgradeSetList!=null && upgradeSetList.size()>0){
				List<String> obdSns = new ArrayList<String>();
				for (UpgradeSet upgradeSet : upgradeSetList) {
					obdSns.add(upgradeSet.getObdSn());
				}
				upgradeDataLogger.info("----------------【升级列表推送升级服务器】推送列表---"+obdSns.toString());
				
				res=upgradeApi.importObdList(obdSns, obdUpgrade.getVersion(),obdUpgrade.getFirmVersion(), 1,obdUpgrade.getFirmType());
				upgradeDataLogger.info("----------------【升级列表推送升级服务器】推送结果---"+res);
				
				if("0".equals(res[0])){
					for (UpgradeSet upgradeSet : upgradeSetList) {
						upgradeSet.setSendFlag("1");//推送结果0未推送1推送成功
						upgradeSet.setUpdateTime(new Date());
						obdUpgradeService.update(upgradeSet);
					}
				}
				
				outJsonMessage("{\"status\":\"success\",\"message\":\""+res[1]+".\"}");
			
			}else{
				outJsonMessage("{\"status\":\"success\",\"message\":\"该固件无升级列表推送.\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			upgradeDataLogger.info("----------------【升级列表推送升级服务器】异常---"+e);
			outJsonMessage("{\"status\":\"fail\",\"message\":\"异常信息--"+e+".\"}");
		}
		return listUpgrade();
	}
	
	/**
	 * 推送obd设备更新文件
	 */
	public String pushUpgrade() {
		return "pushUpgrade";
	}
	
	/**
	 * 跳转到excel导入升级页面
	 */
	public String toExcelUpgrade() {
		obdUpgrade = obdUpgradeService.find(upId);
		return "excelUpgrade";
	}

	/**
	 * 读取obdExcel文件保存入库
	 */
	public String excelUpgrade() {
		upgradeDataLogger.info("----------------【升级列表上传】---------------");
		try {
			obdUpgrade = obdUpgradeService.findByIdCustom(upId);
			String flag=upgradeSetService.upgradeSetExcel(obdexcel,obdUpgrade.getFirmType(),obdexcelFileName,obdUpgrade.getVersion(),obdUpgrade.getFirmVersion());
			upgradeDataLogger.info("----------------【升级列表上传】版本号:"+obdUpgrade.getVersion()+"---结果:"+flag);
			if(flag.startsWith("true")){
				this.message="保存成功，请及时审核升级列表.";
			}else if(flag.startsWith("false")){
				this.message="保存失败,"+flag.substring(flag.indexOf("_")+1,flag.length());
			}else if(flag.startsWith("repeat")){
				this.message="保存失败,设备号有误:"+flag.substring(flag.indexOf("_")+1,flag.length());
			}else{
				this.message="保存异常,请联系管理员."+flag;
			}
		} catch (IOException e) {
			e.printStackTrace();
			upgradeDataLogger.error("----------------【升级列表上传】异常:"+e);
		}
		return "excelUpgrade";
	}
	
	/**
	 * 删除固件前提示是否还存在未推送成功的升级列表
	 * @return
	 */
	public String delectCheck(){
		obdUpgrade = obdUpgradeService.findByIdCustom(upId);
		List<UpgradeSet> usList=upgradeSetService.getListByVersionAndVflag(obdUpgrade.getVersion(), "1");//查询未推送的
		if(usList!=null && usList.size()>0){
			Integer total = usList.size();
			List<String> obdList = new ArrayList<String>();
			for (UpgradeSet us : usList) {
				obdList.add(us.getObdSn());
			}
			outJsonMessage("{\"status\":\"fail\",\"message\":\""+obdList.toString()+",这些设备还未升级,总数:"+total+"\"}");
		}else{
			outJsonMessage("{\"status\":\"success\",\"message\":\"该固件没有设备待升级记录.\"}");
		}
		return listUpgrade();
	}
	
	
	public ObdUpgrade getObdUpgrade() {
		return obdUpgrade;
	}

	public void setObdUpgrade(ObdUpgrade obdUpgrade) {
		this.obdUpgrade = obdUpgrade;
	}

	public File getUpdwj() {
		return updwj;
	}

	public void setUpdwj(File updwj) {
		this.updwj = updwj;
	}

	public String getUpdwjFileName() {
		return updwjFileName;
	}

	public void setUpdwjFileName(String updwjFileName) {
		this.updwjFileName = updwjFileName;
	}

	public String getUpdwjContentType() {
		return updwjContentType;
	}

	public void setUpdwjContentType(String updwjContentType) {
		this.updwjContentType = updwjContentType;
	}

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	public int getEditState() {
		return editState;
	}

	public void setEditState(int editState) {
		this.editState = editState;
	}

	public List<ObdMsg> getObdMsgList() {
		return obdMsgList;
	}

	public void setObdMsgList(List<ObdMsg> obdMsgList) {
		this.obdMsgList = obdMsgList;
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

	public OBDStockInfo getObdStockInfo() {
		return obdStockInfo;
	}

	public void setObdStockInfo(OBDStockInfo obdStockInfo) {
		this.obdStockInfo = obdStockInfo;
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

	public List<OBDStockInfo> getObdStockInfos() {
		return obdStockInfos;
	}

	public void setObdStockInfos(List<OBDStockInfo> obdStockInfos) {
		this.obdStockInfos = obdStockInfos;
	}

	public String getUversion() {
		return uversion;
	}

	public void setUversion(String uversion) {
		this.uversion = uversion;
	}

	public String getFirmType() {
		return firmType;
	}

	public void setFirmType(String firmType) {
		this.firmType = firmType;
	}

	
	
}
