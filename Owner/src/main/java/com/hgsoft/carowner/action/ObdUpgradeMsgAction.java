package com.hgsoft.carowner.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.carowner.service.ObdUpgradeService;
import com.hgsoft.carowner.service.ObdVersionService;
import com.hgsoft.carowner.service.UpgradeSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
import com.hgsoft.common.utils.StrUtil;

/**
 * obd远程升级详情
 * @author ljl
 */
@Controller
@Scope("prototype")
public class ObdUpgradeMsgAction extends BaseAction {
	
	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");
	@Resource
	private ObdVersionService obdVersionService;
	@Resource
	private UpgradeSetService upgradeSetService;
	@Resource
	private UpgradeApi upgradeApi;
	private List<ObdVersion> obdVersionList = new ArrayList<ObdVersion>();
	private ObdVersion obdVersion;	

	private String starTime;
	private String endTime;
	private List<UpgradeSet> upgradeSets;
	private UpgradeSet upgradeSet;
	private String upgradeSetIds="";
	private String uresultIds="";
	private String qtype;
	
	private Integer id;
	private String obdSn;
	private String obdMSn;
	private String version;
	private String firmType;
	private String firmVersion;
	private String validTrue;
	private String auditState;
	private String upgradeFlag;
	private String success;
	private String sendFlag;
	private String valid;
	private String sendedCount;
	private String speedCount;
	
	private String updateFlag;
	
	private String obdSpeedFlag;
	private String gpsSpeedFlag;
	private String auditState2;
	@Resource
	private ObdUpgradeService obdUpgradeService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 展示obd设备升级的文件列表
	 * @throws ParseException 
	 */
	public String list() throws ParseException {
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
		
		obdVersionList = obdVersionService.findByPager(pager,Order.desc("createTime"));
		return "list";
	}
	
	
	// 从数据库中查询数据
		public String query() throws ParseException {
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
			
			if("1".equals(qtype)){
				Date start = sdf.parse(starTime);
				Date end = sdf.parse(endTime);
				
				obdVersionList = obdVersionService.getLastList(start, end,version,firmType,firmVersion,pager);
				if(obdVersionList!=null && obdVersionList.size()>0){
					List<ObdVersion> ovList = obdVersionService.getLastList(start, end,version,firmType,firmVersion,null);
					pager.setTotalSize(ovList.size());
					for (ObdVersion obdVersion : ovList) {
						uresultIds+= obdVersion.getId()+";";
					}
				}
				return "list";
			}

			String obdSN = "";//表面号解析成设备号
			if (!StringUtils.isEmpty(obdMSn)) {
				//将表面号解析成设备号
				try {
					obdSN = StrUtil.obdSnChange(obdMSn);// 设备号
					if(StringUtils.isEmpty(obdSN)){
						return "list";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "list";
				}
			}
			if(!StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdSN)){
				if(!obdSn.equals(obdSN)){
					return "list";
				}else{
					obdSN = "";
				}
			}
			
			List<Property> list = new ArrayList<Property>();
			if (!StringUtils.isEmpty(obdSn)) {
				list.add(Property.eq("obdSn", obdSn.trim()));
			}
			if (!StringUtils.isEmpty(obdSN)) {
				list.add(Property.eq("obdSn", obdSN));
			}
			
			if (!StringUtils.isEmpty(version)) {
				list.add(Property.eq("version", version.trim()));
			}
			if (!StringUtils.isEmpty(updateFlag)) {
				list.add(Property.eq("updateFlag", updateFlag.trim()));
			}
			
			if (!StringUtils.isEmpty(firmType)) {
				list.add(Property.eq("firmType", Integer.parseInt(firmType)));
			}
			
			if (!StringUtils.isEmpty(firmVersion)) {
				list.add(Property.eq("firmVersion", firmVersion.trim()));
			}
			
			if (!StringUtils.isEmpty(starTime)) {
				list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
			}
			if (!StringUtils.isEmpty(endTime)) {
				list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
			}
			Property[] propertyArr = new Property[list.size()];
			list.toArray(propertyArr);
			obdVersionList = obdVersionService.findByPager(pager,Order.desc("createTime"),propertyArr);
			
			if(obdVersionList!=null && obdVersionList.size()>0){
				List<ObdVersion> ovList = obdVersionService.findByPager(null,null,propertyArr);
				for (ObdVersion obdVersion : ovList) {
					uresultIds+= obdVersion.getId()+";";
				}
			}
			return "list";
		}

	/**
	 * 查询结果
	 * @param pager
	 * @return
	 */
	public List<UpgradeSet> listUpgradeSets(Pager pager){
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
		
		map.put("vflag", "1");
		total++;
		
		if (!StringUtils.isEmpty(obdSn)) {
			map.put("obdSn", obdSn);
			total++;
		}
		if(!StringUtils.isEmpty(obdSN)){
			map.put("obdSn", obdSN);
			total++;
		}
		
		if (!StringUtils.isEmpty(version)) {
			map.put("version", version);
			total++;
		}
		if (!StringUtils.isEmpty(valid)) {
			map.put("valid", valid);
			total++;
		}
		if (!StringUtils.isEmpty(sendFlag)) {
			map.put("sendFlag", sendFlag);
			total++;
		}
		
		if (!StringUtils.isEmpty(firmType)) {
			map.put("firmType", Integer.parseInt(firmType));
			total++;
		}
		
		if (!StringUtils.isEmpty(firmVersion)) {
			map.put("firmVersion", firmVersion);
			total++;
		}
		if (!StringUtils.isEmpty(validTrue)) {
			map.put("validTrue", validTrue);
			if(!"1".equals(validTrue)){
				total++;
			}
		}
		if (!StringUtils.isEmpty(auditState)) {
			map.put("auditState", auditState);
			total++;
		}
		if (!StringUtils.isEmpty(upgradeFlag)) {
			map.put("upgradeFlag", upgradeFlag);
			total++;
		}
		if (!StringUtils.isEmpty(success)) {
			map.put("success", Integer.parseInt(success));
			if(!"2".equals(success)){
				total++;
			}
		}
		
		if (!StringUtils.isEmpty(sendedCount)) {
			map.put("sendedCount", Integer.parseInt(sendedCount));
			total++;
		}
		
		if (!StringUtils.isEmpty(speedCount)) {
			map.put("speedCount", Integer.parseInt(speedCount));
			total++;
		}
		
		if (!StringUtils.isEmpty(obdSpeedFlag)) {
			map.put("obdSpeedFlag", obdSpeedFlag);
		}
		
		if (!StringUtils.isEmpty(gpsSpeedFlag)) {
			map.put("gpsSpeedFlag", gpsSpeedFlag);
		}
		
		if (!StringUtils.isEmpty(starTime)) {
			map.put("starTime", starTime);
			total++;
		}
		if (!StringUtils.isEmpty(endTime)) {
			map.put("endTime", endTime);
			total++;
		}
		
		map.put("paramsTotal", total);
		return upgradeSetService.queryByParams(pager, map);
	}
		
	/**
	 * 待升级列表
	 * @return
	 */
	public String upgradeSetList(){

		upgradeSets = listUpgradeSets(pager);
		
		return "upgradeSetList";
	}
	/**
	 * 审核
	 */
	public void auditUpgarde(){
		Long total=upgradeSetService.getTotalByVersion(version);
		if(total==null || total==0){
			outMessage("1");
		}else{
//			for (UpgradeSet it : upgradeset) {
//				it.setAuditOper(getOperator().getName());
//				it.setAuditTime(getNow());
//				it.setAuditState(auditState2);
//				it.setUpdateTime(new Date());
////				if("2".equals(auditState)){
////					it.setVflag("0");//记录失效
////				}
//				upgradeSetService.update(it);
//			}
			int i=upgradeSetService.updByVersion(auditState2, version,getOperator().getName());
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
	public void delAll(){
		upgradeDataLogger.info("----------------【待升级记录删除全部】---------------");
		upgradeDataLogger.info("----------------【待升级记录删除全部】版本号:---"+version);
		
		//查找是否有未升级成功且有效的记录
		List<UpgradeSet> usList=upgradeSetService.getListByVersionAndVflag(version, "1");
		if(usList!=null && usList.size()>0){
			boolean flag = true;
			String msg = "";
			for (UpgradeSet us : usList) {
				String obdSn =us.getObdSn();
				String sendFlag = us.getSendFlag();//是否推送到升级服务器
				String valid = us.getValid();//是否下发升级请求
				String upgradeFlag = us.getUpgradeFlag();//升级结果
				switch (sendFlag) {
				case "1":
					//推送成功
					if("0".equals(valid) && "0".equals(upgradeFlag)){
						//已下发升级请求且无升级结果
						flag = false;
						msg +=obdSn+",";
					}
					break;
				default:
					//未推送成功,不用管
					break;
				}
			}
			//如果存在不满足删除条件的记录,不给删除
			if(!flag){
				outMessage("存在不满足删除条件的记录:"+msg);
				return;
			}
			//先删除升级服务器记录,如果删除成功,才可以删除本地记录,否则不给删除本地记录
			List<UpgradeSet> notSend = new ArrayList<UpgradeSet>(); //未推送的
			List<UpgradeSet> send = new ArrayList<UpgradeSet>(); //推送成功,但未升级成功的
			for (UpgradeSet us : usList) {
				String obdSn = us.getObdSn();
				String sendFlag = us.getSendFlag();
				switch (sendFlag) {
				case "0":
					notSend.add(us);
					break;
				case "1":
					send.add(us);
					break;
				default:
					outMessage("删除失败,记录的推送类型有误,请联系管理员---"+obdSn);
					break;
				}
			}
			//1.删除升级服务器记录
			if(send.size()>0){
				List<String> obdSns = new ArrayList<String>();
				for (UpgradeSet us : send) {
					obdSns.add(us.getObdSn());
				}
				String[] res = new String[2];
				try {
					//先查询版本号
					ObdUpgrade obdUpgrade=obdUpgradeService.findLastByVersion(version.trim());
					if(obdUpgrade==null){
						outMessage("系统异常，请稍后重试---"+version);
						return;
					}
					res=upgradeApi.importObdList(obdSns, version,obdUpgrade.getFirmVersion(), 0,obdUpgrade.getFirmType());
					if("0".equals(res[0])){
						//删除成功
						for (UpgradeSet us : send) {
							us.setUpdateTime(new Date());
							us.setVflag("0");//置为无效
							upgradeSetService.update(us);
						}
					}else{
						outMessage("升级服务器的待升级记录删除失败:"+obdSns.toString()+"---失败总数:"+obdSns.size()+"---失败原因:"+res[1]);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					upgradeDataLogger.info("----------------【待升级记录删除全部】发生异常,信息------"+e);
					outMessage("删除异常,请联系管理员---"+e);
					return;
				}
			}
			
			if(notSend.size()>0){
				for (UpgradeSet us : notSend) {
					us.setUpdateTime(new Date());
					us.setVflag("0");//置为无效
					upgradeSetService.update(us);
				}
				upgradeDataLogger.info("----------------【待升级记录删除全部】未推送的待升级记录删除成功----------");
			}
			outMessage("删除记录成功："+usList.size()+"条，成功！");
		}else{
			outMessage("没有可删除的记录,请核查.");
			return;
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
	public void adminDelAll(){
		upgradeDataLogger.info("----------------【待升级记录删除全部】---------------");
		upgradeDataLogger.info("----------------【待升级记录删除全部】版本号:---"+version);
		
		//查找是否有未升级成功且有效的记录
		List<UpgradeSet> usList=upgradeSetService.getListByVersionAndVflag(version, "1");
		if(usList!=null && usList.size()>0){
			//先删除升级服务器记录,如果删除成功,才可以删除本地记录,否则不给删除本地记录
			List<UpgradeSet> notSend = new ArrayList<UpgradeSet>(); //未推送的
			List<UpgradeSet> send = new ArrayList<UpgradeSet>(); //推送成功,但未升级成功的
			for (UpgradeSet us : usList) {
				String obdSn = us.getObdSn();
				String sendFlag = us.getSendFlag();
				switch (sendFlag) {
				case "0":
					notSend.add(us);
					break;
				case "1":
					send.add(us);
					break;
				default:
					outMessage("删除失败,记录的推送类型有误,请联系管理员---"+obdSn);
					break;
				}
			}
			//1.删除升级服务器记录
			if(send.size()>0){
				List<String> obdSns = new ArrayList<String>();
				for (UpgradeSet us : send) {
					obdSns.add(us.getObdSn());
				}
				String[] res = new String[2];
				try {
					//先查询版本号
					ObdUpgrade obdUpgrade=obdUpgradeService.findLastByVersion(version.trim());
					if(obdUpgrade==null){
						outMessage("系统异常，请稍后重试---"+version);
						return;
					}
					res=upgradeApi.importObdList(obdSns, version,obdUpgrade.getFirmVersion(), 0,obdUpgrade.getFirmType());
					if("0".equals(res[0])){
						//删除成功
						for (UpgradeSet us : send) {
							us.setUpdateTime(new Date());
							us.setVflag("0");//置为无效
							upgradeSetService.update(us);
						}
					}else{
						outMessage("升级服务器的待升级记录删除失败:"+obdSns.toString()+"---失败总数:"+obdSns.size()+"---失败原因:"+res[1]);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					upgradeDataLogger.info("----------------【待升级记录删除全部】发生异常,信息------"+e);
					outMessage("删除异常,请联系管理员---"+e);
					return;
				}
			}
			
			if(notSend.size()>0){
				for (UpgradeSet us : notSend) {
					us.setUpdateTime(new Date());
					us.setVflag("0");//置为无效
					upgradeSetService.update(us);
				}
				upgradeDataLogger.info("----------------【待升级记录删除全部】未推送的待升级记录删除成功----------");
			}
			outMessage("删除记录成功："+usList.size()+"条，成功！");
			return;
		}else{
			outMessage("没有可删除的记录,请核查.");
			return;
		}
		
	}
	
	/**
	 * 删除单条
	 * 删除待升级列表
	 * 1.下发升级请求，无升级结果,不给删
	 * 2.下发。。。，有升级结果,可以删
	 * 3.无下发,可以删
	 */
	public void del(){
		upgradeDataLogger.info("----------------【待升级记录删除单条】---------------");
		UpgradeSet upgradeSet0 = upgradeSetService.find(id);
		if(upgradeSet0 != null){
			List<String> obdSns = new ArrayList<String>();
			obdSns.add(upgradeSet0.getObdSn());
			String[] res = new String[2];
			//如果未推送成功
			if("1".equals(upgradeSet0.getSendFlag())){
				String valid = upgradeSet0.getValid();//是否下发升级请求
				String upgradeFlag = upgradeSet0.getUpgradeFlag();
				
				try {
					switch (valid) {
					case "1":
						//未下发升级请求
//						res=upgradeApi.importObdList(obdSns, upgradeSet0.getVersion(),null, 0,null);
						res=upgradeApi.importObdList(obdSns, upgradeSet0.getVersion(),upgradeSet0.getFirmVersion(), 0,upgradeSet0.getFirmType());
						break;
					case "0":
						//下发升级请求
						//1.如果没有升级结果不给删
						switch (upgradeFlag) {
						case "0":
							outMessage("已下发升级请求,但未升级,不可以删除.");
							break;
						default:
							//存在升级结果,可以删
//							res=upgradeApi.importObdList(obdSns, upgradeSet0.getVersion(),null, 0,null);
							res=upgradeApi.importObdList(obdSns, upgradeSet0.getVersion(),upgradeSet0.getFirmVersion(), 0,upgradeSet0.getFirmType());
							break;
						}
						break;
					default:
						upgradeDataLogger.info("----------------【待升级记录删除单条】记录下发升级请求参数类别有误:"+valid);
						outMessage("记录下发升级请求参数类别有误,请联系管理员.");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					upgradeDataLogger.info("----------------【待升级记录删除单条】异常:"+e);
					outMessage("删除异常,信息---"+e);
					return;
				}
					
				upgradeDataLogger.info("----------------【待升级记录删除单条】设备号:---"+obdSns.toString()+"---结果:"+Arrays.toString(res));
				if(!StringUtils.isEmpty(res[0])){
					if("0".equals(res[0])){
						upgradeSet0.setUpdateTime(new Date());
						upgradeSet0.setVflag("0");//置为无效
						upgradeSetService.update(upgradeSet0);
					}
					outMessage(res[1]);
					return;
				}else{
					outMessage("待升级记录删除单条---返回删除结果类型为空.");
					return;
				}
				
			}else{
				upgradeSet0.setUpdateTime(new Date());
				upgradeSet0.setVflag("0");//置为无效
				upgradeSetService.update(upgradeSet0);
				outMessage("删除成功！");
			}
		}
	}
	 
	
	/**
	 * 管理员删除单条
	 * 删除待升级列表
	 * 1.是否有推送到升级服务器，如果有则先删除升级服务器的记录
	 * 如果没有推送到升级服务器，则删除本地记录即可。
	 */
	public void adminDel(){
		upgradeDataLogger.info("----------------【待升级记录删除单条】---------------");
		UpgradeSet upgradeSet0 = upgradeSetService.find(getId());
		if(upgradeSet0 != null){
			List<String> obdSns = new ArrayList<String>();
			obdSns.add(upgradeSet0.getObdSn());
			String[] res = new String[2];
			//如果未推送成功
			if("1".equals(upgradeSet0.getSendFlag())){
				try {
					res=upgradeApi.importObdList(obdSns, upgradeSet0.getVersion(),upgradeSet0.getFirmVersion(), 0,upgradeSet0.getFirmType());
				} catch (Exception e) {
					e.printStackTrace();
					upgradeDataLogger.info("----------------【待升级记录删除单条】异常:"+e);
					outMessage("删除异常,信息---"+e);
					return;
				}
				upgradeDataLogger.info("----------------【待升级记录删除单条】设备号:---"+obdSns.toString()+"---结果:"+Arrays.toString(res));
				if(!StringUtils.isEmpty(res[0])){
					if("0".equals(res[0])){
						upgradeSet0.setUpdateTime(new Date());
						upgradeSet0.setVflag("0");//置为无效
						upgradeSetService.update(upgradeSet0);
					}
					outMessage(res[1]);
					return;
				}else{
					outMessage("待升级记录删除单条---返回删除结果类型为空.");
					return;
				}
				
			}else{
				upgradeSet0.setUpdateTime(new Date());
				upgradeSet0.setVflag("0");//置为无效
				upgradeSetService.update(upgradeSet0);
				outMessage("删除成功！");
			}
		}
	}
	 
	
	public String exportExcel(){
		String[] headers={"ID","设备号","版本号","固件号","固件类型","推送","导入时间","更新时间","下发升级","真下发","审核人","审核时间","审核结果","升级状态","是否有效0无效1有效","下发次数","成功","obd速度","gps速度","比较次数","最新比较类型"};
		String[] cloumn={"id","obdSn","version","firmVersion","firmType","sendFlag","createTime","updateTime","valid","validTrue","auditOper","auditTime","auditState","upgradeFlag","vflag","sendedCount","success","obdSpeed","gpsSpeed","speedCount","lastSpeedType"}; 
		String fileName="待升级设备情况表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		upgradeSets =  listUpgradeSets(null);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (UpgradeSet upgradeSet : upgradeSets) {
			HashMap<String, Object> map = new  HashMap<>();
			map.put("id",upgradeSet.getId());
			map.put("obdSn",upgradeSet.getObdSn());
			map.put("version",upgradeSet.getVersion());
			map.put("firmVersion",upgradeSet.getFirmVersion());
			map.put("firmType",upgradeSet.getFirmType());
			map.put("sendFlag",upgradeSet.getSendFlag());
			map.put("createTime",upgradeSet.getCreateTime());
			map.put("updateTime",upgradeSet.getUpdateTime());
			map.put("valid",upgradeSet.getValid());
			map.put("validTrue",upgradeSet.getValidTrue());
			map.put("auditOper",upgradeSet.getAuditOper());
			map.put("auditTime",upgradeSet.getAuditTime());
			map.put("auditState",upgradeSet.getAuditState());
			map.put("upgradeFlag",upgradeSet.getUpgradeFlag());
			map.put("vflag",upgradeSet.getVflag());
			map.put("sendedCount",upgradeSet.getSendedCount());
			map.put("success",upgradeSet.getSuccess());
			map.put("obdSpeed",upgradeSet.getObdSpeed());
			map.put("gpsSpeed",upgradeSet.getGpsSpeed());
			map.put("speedCount",upgradeSet.getSpeedCount());
			map.put("lastSpeedType",upgradeSet.getLastSpeedType());
			lists.add(map);
		}
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("待升级设备情况表", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	
	/*
	 * 升级结果详情导出excel
	 */
	public String uresultExcel(){
		
		String[] headers={"ID","设备号","版本号","固件版本号","升级结果","创建时间"};
		String[] cloumn={"id","obdSn","version","firmVersion","updateFlag","createTime"}; 
		String fileName="升级结果详情.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		
		if(StringUtils.isEmpty(uresultIds)){
			return null;
		}
		
		if(uresultIds.endsWith(";")){
			uresultIds = uresultIds.substring(0, uresultIds.length()-1);
		}
		
		String[] ids = uresultIds.split(";");
		List<Integer> idList = new ArrayList<Integer>();
		for (String string : ids) {
			Integer id = Integer.parseInt(string);
			idList.add(id);
		}
		
		List<Property> list = new ArrayList<Property>();
			
		list.add(Property.in("id", idList));
			
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		
		List<ObdVersion> ovList = obdVersionService.findByPager(null, null , propertyArr);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (ObdVersion ov : ovList) {
			HashMap<String, Object> map = new  HashMap<>();
			map.put("id",ov.getId());
			map.put("obdSn",ov.getObdSn());
			map.put("version",ov.getVersion());
			map.put("firmVersion",ov.getFirmVersion());
			map.put("updateFlag",ov.getUpdateFlag());
			map.put("createTime",ov.getCreateTime());
			lists.add(map);
		}
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("obd升级结果", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);	
		return "excel";
	}
	
	/*
	 * 升级结果详情导出excel
	 * 只导出最新的记录
	 * 查询时间不可以为空
	 */
	public String ulastResultExcel(){
		
		String[] headers={"ID","设备号","版本号","固件版本号","升级结果","创建时间"};
		String[] cloumn={"id","obdSn","version","firmVersion","updateFlag","createTime"}; 
		String fileName="升级结果详情.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		
		
		
		List<Property> list = new ArrayList<Property>();
		if (!StringUtils.isEmpty(starTime)) {
			list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
		}
		if (!StringUtils.isEmpty(endTime)) {
			list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
			
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		
		List<ObdVersion> ovList = obdVersionService.findByPager(null, null , propertyArr);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (ObdVersion ov : ovList) {
			HashMap<String, Object> map = new  HashMap<>();
			map.put("id",ov.getId());
			map.put("obdSn",ov.getObdSn());
			map.put("version",ov.getVersion());
			map.put("firmVersion",ov.getFirmVersion());
			map.put("updateFlag",ov.getUpdateFlag());
			map.put("createTime",ov.getCreateTime());
			lists.add(map);
		}
		
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("obd升级结果", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);	
		return "excel";
	}

	
	public String getUpdateFlag() {
		return updateFlag;
	}


	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getObdSn() {
		return obdSn;
	}


	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}


	public String getSendFlag() {
		return sendFlag;
	}


	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
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


	public String getUpgradeFlag() {
		return upgradeFlag;
	}


	public void setUpgradeFlag(String upgradeFlag) {
		this.upgradeFlag = upgradeFlag;
	}


	public String getSuccess() {
		return success;
	}


	public void setSuccess(String success) {
		this.success = success;
	}


	public String getValidTrue() {
		return validTrue;
	}


	public void setValidTrue(String validTrue) {
		this.validTrue = validTrue;
	}


	public String getFirmVersion() {
		return firmVersion;
	}


	public void setFirmVersion(String firmVersion) {
		this.firmVersion = firmVersion;
	}


	public String getFirmType() {
		return firmType;
	}


	public void setFirmType(String firmType) {
		this.firmType = firmType;
	}


	public ObdVersion getObdVersion() {
		return obdVersion;
	}

	public void setObdVersion(ObdVersion obdVersion) {
		this.obdVersion = obdVersion;
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

	public List<ObdVersion> getObdVersionList() {
		return obdVersionList;
	}

	public void setObdVersionList(List<ObdVersion> obdVersionList) {
		this.obdVersionList = obdVersionList;
	}

	public List<UpgradeSet> getUpgradeSets() {
		return upgradeSets;
	}

	public void setUpgradeSets(List<UpgradeSet> upgradeSets) {
		this.upgradeSets = upgradeSets;
	}

	public UpgradeSet getUpgradeSet() {
		return upgradeSet;
	}

	public void setUpgradeSet(UpgradeSet upgradeSet) {
		this.upgradeSet = upgradeSet;
	}

	public String getUpgradeSetIds() {
		return upgradeSetIds;
	}

	public void setUpgradeSetIds(String upgradeSetIds) {
		this.upgradeSetIds = upgradeSetIds;
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


	public String getUresultIds() {
		return uresultIds;
	}


	public void setUresultIds(String uresultIds) {
		this.uresultIds = uresultIds;
	}


	public String getQtype() {
		return qtype;
	}


	public void setQtype(String qtype) {
		this.qtype = qtype;
	}


	public String getSpeedCount() {
		return speedCount;
	}


	public void setSpeedCount(String speedCount) {
		this.speedCount = speedCount;
	}


	public String getObdSpeedFlag() {
		return obdSpeedFlag;
	}


	public void setObdSpeedFlag(String obdSpeedFlag) {
		this.obdSpeedFlag = obdSpeedFlag;
	}


	public String getGpsSpeedFlag() {
		return gpsSpeedFlag;
	}


	public void setGpsSpeedFlag(String gpsSpeedFlag) {
		this.gpsSpeedFlag = gpsSpeedFlag;
	}


	public String getAuditState2() {
		return auditState2;
	}


	public void setAuditState2(String auditState2) {
		this.auditState2 = auditState2;
	}


	public String getObdMSn() {
		return obdMSn;
	}


	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}
	
	
}
