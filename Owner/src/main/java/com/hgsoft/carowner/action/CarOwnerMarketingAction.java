package com.hgsoft.carowner.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hgsoft.application.vo.Track;
import com.hgsoft.carowner.entity.CarTraveltrack;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.SerInformationman;
import com.hgsoft.carowner.entity.SerMessageinfo;
import com.hgsoft.carowner.service.CarDriveInfoService;
import com.hgsoft.carowner.service.CarOilInfoService;
import com.hgsoft.carowner.service.CarTraveltrackService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.SerInformationmanService;
import com.hgsoft.carowner.service.SerMessageinfoService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.IDUtil;
@Component
@Scope("prototype")
public class CarOwnerMarketingAction extends  BaseAction{
	
	private SimpleDateFormat dataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	private SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	private String infoType;
	private String infoTitle;
	private String starTime;
	private String endTime;
	private String infoDetail;
	private String summary;
	private int infoId;
	private String license;
	private String obdSn;
	private String obdMSn;
	private String mobileNumber;
	private SerInformationman serInformationman;
	private List<SerInformationman> list=new ArrayList<SerInformationman>();
	private List businessStatistList=new ArrayList();
	@Resource
	private SerInformationmanService serInformationmanService;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private CarDriveInfoService carDriveInfoService;
	private List drivingBehaviorList=new ArrayList();
	@Resource
	private CarOilInfoService carOilInfoService;
	private List oilList=new ArrayList();
	@Resource
	private CarTraveltrackService carTraveltrackService;
//	private List trackList=new  ArrayList();
	private List<CarTraveltrack> trackList;
	private String fileName;        //文件名  
    private String tempPath;
    @Resource
    private SerMessageinfoService serMessageinfoService;
    private List<SerMessageinfo> msglist=new ArrayList<SerMessageinfo>();
    private String messageRec;
    private String messageTime;
    private String messageText;
    private String insesrtTime;
    private List<Track> dataTrack=new ArrayList<Track>();
    private String dataTrackJson;
    private String poit;
    private List poinList=new ArrayList();
    private String poinListStr;
    private String dataTrackStr;
	/**
	 * 查询资讯信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String queryInfomation() throws UnsupportedEncodingException{
		if(infoTitle!=null){
			infoTitle=URLDecoder.decode(request.getParameter("infoTitle"),"utf-8");
		}
		list=serInformationmanService.query(infoType,infoTitle, starTime, endTime, pager);
		return "newsList";
	}
	/**
	 * 跳转到资讯新增页面
	 */
	public String add(){
		return "newsAdd";
	}
	/**
	 * 保存资讯信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String saveNews() throws UnsupportedEncodingException{
		if(infoTitle!=null){
			infoTitle=URLDecoder.decode(request.getParameter("infoTitle"),"utf-8");
		}
		if(summary!=null){
			summary=URLDecoder.decode(request.getParameter("summary"),"utf-8");
		}
		if(infoDetail!=null){
			infoDetail=URLDecoder.decode(request.getParameter("infoDetail"),"utf-8");
		}

		SerInformationman info=new SerInformationman();
		info.setInfoType("01");
		info.setInfoTitle(infoTitle);
		info.setSummary(summary);
		info.setInfoDetail(infoDetail);
		info.setInfoTime(dataTime.format(new Date()));
		info.setInfoShow("0");
		info.setInfoRedShow("1");
	//	info.setRegUserId(ActionContext.getContext().getSession().get("name").toString());
		serInformationmanService.save(info);
		message="发布资讯成功 ！";
		return queryInfomation();
	}
	/**
	 * 查询资讯信息，修改
	 * @return
	 */
	public String queryInfoById(){
		this.serInformationman=serInformationmanService.query(infoId);
		return "newsEdit";
	}
	/**
	 * 修改资讯信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String updateNews() throws UnsupportedEncodingException{
		if(infoTitle!=null){
			infoTitle=URLDecoder.decode(request.getParameter("infoTitle"),"utf-8");
		}
		if(summary!=null){
			summary=URLDecoder.decode(request.getParameter("summary"),"utf-8");
		}
		if(infoDetail!=null){
			infoDetail=URLDecoder.decode(request.getParameter("infoDetail"),"utf-8");
		}
		 SerInformationman temp=serInformationmanService.query(infoId);
		 temp.setInfoDetail(infoDetail);
		 temp.setInfoTitle(infoTitle);
		 temp.setSummary(summary);
		 serInformationmanService.update(temp);
		 message="修改成功！";
		return queryInfomation();
	}
	/**
	 * 批量删除资讯
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String del() throws UnsupportedEncodingException{
		if(request.getParameter("infoArr")!=null){
			String[] str=request.getParameter("infoArr").split(",");
			for (int i = 0; i < str.length; i++) {
				SerInformationman temp=serInformationmanService.query(Integer.valueOf(str[i]));
				serInformationmanService.delete(temp);
			}
		}
		
		
		return queryInfomation();
	}
	/**
	 *  查询推送消息
	 * @return
	 */
	public String messageInfo(){
		msglist=serMessageinfoService.getMsg(pager);
		return "messagesList";
	}
	/**
	 * 发布推送消息
	 * @return
	 */
	public String addMsg(){
		SerMessageinfo msg=new SerMessageinfo();
		msg.setMessageId(IDUtil.createID());
		msg.setMessageType("01");
		msg.setMessageRec(messageRec);
		msg.setMessageTime(messageTime);
		msg.setMessageText(messageText);
		serMessageinfoService.save(msg);
		return messageInfo();
	}
	/**
	 * 获取某天的全部行程
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String trackData() throws Exception{
		dataTrack=carTraveltrackService.trackData(obdSn, insesrtTime,pager);
		dataTrackJson = JSONArray.fromObject(dataTrack).toString();
		return "dataList";
	}
	/**
	 * 轨迹页面
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String poit(){
		poinList=carTraveltrackService.poitList(starTime, endTime,obdSn);
		poinListStr = JSONArray.fromObject(poinList).toString();
		List list=new ArrayList();
		Map map=new HashMap();
		map.put("obdSn", obdSn);
		map.put("insesrtTime", insesrtTime);
		list.add(map);
		dataTrackStr=JSONArray.fromObject(list).toString();
//		for (Object object : poinList) {
//			System.out.println(object);
//		}
//		System.out.println(dataTrackStr);
		return "track";
	}
	/**
	 * 业务统计
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String businessStatistics() throws ParseException, IOException{
		
		if(starTime==null||starTime.trim().length()==0){
			starTime=data.format(new Date());
		}
		if(endTime==null||endTime.trim().length()==0){
			endTime=data.format(new Date());
		}

		businessStatistList=obdStockInfoService.obdStatist(starTime, endTime,pager,1);
		return "businessList";
	}
	/**
	 * 驾驶行为分析
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String drivingBehavior(){
		
		drivingBehaviorList=carDriveInfoService.getDrivingBehavior(obdSn, mobileNumber, license,starTime, endTime, pager,1);
		return "drivingBehaviorList";
	}
	/**
	 * 油耗统计
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String oilConsumption() throws UnsupportedEncodingException{

		oilList=carOilInfoService.getoilConsumption(obdSn, mobileNumber, license, starTime, endTime, pager,1);
		return "oilConsumptionList";
	}
	/**
	 * 行程记录统计
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	public String travelTrack() throws ParseException{
		if(StringUtils.isEmpty(starTime) || StringUtils.isEmpty(endTime)){
			starTime=data.format(new Date());
			endTime=data.format(new Date());
			return "travelTrackList";
		}
		if(StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdMSn)){
			OBDStockInfo obd = obdStockInfoService.queryByObdMSN(obdMSn);
			if(obd!=null){
				obdSn=obd.getObdSn();
			}
		}
		dataTrack=carTraveltrackService.getTravelTracks(obdSn, starTime, endTime, pager);
		return "travelTrackList";
	}
	
	/**
	 * 导出业务统计表
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String obdExport() throws ParseException, IOException{
		String[] headers={"日期","OBD设备总数","新激活设备","活跃设备","已绑定设备"};
		String[] cloumn={"date","obdNumber","obdNew","obdActive","obdBind"}; 
		String fileName="业务统计表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
		List<HashMap<String, Object>>  lists=obdStockInfoService.obdStatist(starTime, endTime,pager,0);
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out = new FileOutputStream(filepath);
		ex.carOwnerExport("业务统计表", headers, lists, cloumn, out);
		out.close();
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	/**
	 * 导出驾驶行为统计表
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String drivingExport() throws ParseException, IOException{
		String[] headers={"设备号","车牌号码","姓名","手机号码","行为发生日期","超速(次)","急减速(次)","急加速(次)","离转速(次)","急转弯(次)","转速不匹配(次)","怠速(次)"};
		String[] cloumn={"obdSn","license","name","mobileNumber","driveDate","tmsSpeeding","tmsRapDec","tmsRapAcc","highSpeed","tmsSharpTurn","notMatch","longLowSpeed"}; 
		String fileName="驾驶行为统计表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;
		if(request.getParameter("license")!=null){
			license=URLDecoder.decode(request.getParameter("license"),"utf-8");
		}
		List<HashMap<String, Object>>  lists =lists=carDriveInfoService.getDrivingBehavior(obdSn, mobileNumber, license, starTime, endTime, pager,0);
		ExcelUtil ex = new ExcelUtil();
		OutputStream out = new FileOutputStream(filepath);
		ex.carOwnerExport("驾驶行为统计表", headers, lists, cloumn, out);
		out.close();
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	/**
	 * 导出车辆油耗统计表
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String oilExport() throws ParseException, IOException{
		String[] headers={"设备号","车牌号码","姓名","手机号码","总油耗(升)","总里程(公里)","平均油耗 （升/百公里）","日期"};
		String[] cloumn={"obdSn","license","name","mobileNumber","petrolConsumeNum","mileageNum","avgOil","oilInfoTime"}; 
		String fileName="车辆油耗统计表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;
		if(request.getParameter("license")!=null){
			license=URLDecoder.decode(request.getParameter("license"),"utf-8");
		}
		List<HashMap<String, Object>>  lists =lists=carOilInfoService.getoilConsumption(obdSn, mobileNumber, license, starTime, endTime, pager,0);
		ExcelUtil ex = new ExcelUtil();
		OutputStream out = new FileOutputStream(filepath);
		ex.carOwnerExport("车辆油耗统计表", headers, lists, cloumn, out);
		out.close();
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	/**
	 * 导出行程记录统计表
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public String trackExport() throws ParseException, IOException{
		String[] headers={"设备号","里程","日期"};
		String[] cloumn={"obdSn","distance","insesrtTime"}; 
		String fileName="行程记录统计表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;
//		if(request.getParameter("license")!=null){
//			license=URLDecoder.decode(request.getParameter("license"),"utf-8");
//		}
//		String obdMSn =request.getParameter("obdMSn");
		if(StringUtils.isEmpty(obdSn) && !StringUtils.isEmpty(obdMSn)){
			OBDStockInfo obd = obdStockInfoService.queryByObdMSN(obdMSn);
			if(obd!=null){
				obdSn=obd.getObdSn();
			}
		}
		List<HashMap<String, Object>>  lists=carTraveltrackService.trackExport(obdSn, starTime, endTime);
		ExcelUtil<Object> ex = new ExcelUtil<Object>();
		OutputStream out = new FileOutputStream(filepath);
		ex.carOwnerExport("行程记录统计表", headers, lists, cloumn, out);
		out.close();
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
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

	public List<SerInformationman> getList() {
		return list;
	}

	public void setList(List<SerInformationman> list) {
		this.list = list;
	}
	public String getInfoDetail() {
		return infoDetail;
	}
	public void setInfoDetail(String infoDetail) {
		this.infoDetail = infoDetail;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getInfoId() {
		return infoId;
	}
	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}
	public SerInformationman getSerInformationman() {
		return serInformationman;
	}
	public void setSerInformationman(SerInformationman serInformationman) {
		this.serInformationman = serInformationman;
	}
	public List getBusinessStatistList() {
		return businessStatistList;
	}
	public void setBusinessStatistList(List businessStatistList) {
		this.businessStatistList = businessStatistList;
	}
	public List getDrivingBehaviorList() {
		return drivingBehaviorList;
	}
	public void setDrivingBehaviorList(List drivingBehaviorList) {
		this.drivingBehaviorList = drivingBehaviorList;
	}
	public List getOilList() {
		return oilList;
	}
	public void setOilList(List oilList) {
		this.oilList = oilList;
	}
	public List getTrackList() {
		return trackList;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public List<SerMessageinfo> getMsglist() {
		return msglist;
	}
	public void setMsglist(List<SerMessageinfo> msglist) {
		this.msglist = msglist;
	}
	public String getMessageRec() {
		return messageRec;
	}
	public void setMessageRec(String messageRec) {
		this.messageRec = messageRec;
	}
	public String getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getInsesrtTime() {
		return insesrtTime;
	}
	public void setInsesrtTime(String insesrtTime) {
		this.insesrtTime = insesrtTime;
	}
	public List<Track> getDataTrack() {
		return dataTrack;
	}
	public void setDataTrack(List<Track> dataTrack) {
		this.dataTrack = dataTrack;
	}
	public String getPoit() {
		return poit;
	}
	public void setPoit(String poit) {
		this.poit = poit;
	}
	public List getPoinList() {
		return poinList;
	}
	public void setPoinList(List poinList) {
		this.poinList = poinList;
	}
	public String getPoinListStr() {
		return poinListStr;
	}
	public void setPoinListStr(String poinListStr) {
		this.poinListStr = poinListStr;
	}
	public String getDataTrackStr() {
		return dataTrackStr;
	}
	public void setDataTrackStr(String dataTrackStr) {
		this.dataTrackStr = dataTrackStr;
	}
	public void setTrackList(List<CarTraveltrack> trackList) {
		this.trackList = trackList;
	}
	public String getDataTrackJson() {
		return dataTrackJson;
	}
	public void setDataTrackJson(String dataTrackJson) {
		this.dataTrackJson = dataTrackJson;
	}
	public String getObdMSn() {
		return obdMSn;
	}
	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}
	
	
}
