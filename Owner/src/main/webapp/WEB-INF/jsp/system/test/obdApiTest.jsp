<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>电信接口测试！</title>
<jsp:include page="../../include/common.jsp" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="${basePath}/js/jquery.md5.js"
	charset="UTF-8"></script>
<script type="text/javascript">
		var username = 'chezhutong';//权限校验
		var time='1447134959847';//时间
		var pwd = 'czt123456';
		
		function wifiPwdSsid(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var wPwd = $("#wPwd").val().trim();
			var wName = $("#wName").val().trim();
			if(wPwd =='' && wName ==''){
				alert("wifi密码和名称不能同时为空.");
				return false;
			}
			if(wPwd!=''){
				if(wPwd.length<8 || wPwd.length>30){
					alert("wifi密码或名称长度不对,请检查.");
					return false;
				}
			}
			if(wName!=''){
				if( wName.length>30){
					alert("wifi密码或名称长度不对,请检查.");
					return false;
				}
			}
			
			var flag = wifiPwdNameCheck(wPwd);
			if(!flag){
				alert("wifi密码和名称只能英文和数字.");
				return false;
			}
			flag = wifiPwdNameCheck(wName);
			if(!flag){
				alert("wifi密码和名称只能英文和数字.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/wifiPwdAndName',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'wPwd':wPwd,
					'wName':wName
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("请求异常,请稍后重试.");
				}
			});
		}
		//obd报警开关
		function alarmSwitch(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var switchType = $("#switchType").val();
			var switchState= $("input[name='switchState']:checked").val();
			if(switchType =='' || switchState =='' || switchState == undefined){
				alert("参数不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/alarmSwitch',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'switchType':switchType,
					'switchState':switchState
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//obd报警开关
		function bind(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var userId = $("#userId").val();
			var hgDeviceSn = $("#hgDeviceSn").val();
			var userType = "1";//注册用户类别
			if(userId =='' || hgDeviceSn ==''){
				alert("参数不能为空.");
				return false;
			}
			//var flag = checkPhone(userId);
			
			if(!/^[0-9]+$/.test(userId)){
				alert("请输入正确的手机号码.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/bind',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'userId':userId,
					'hgDeviceSn':hgDeviceSn,
					'userType':userType
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//obd解绑
		function unBind(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/unBind',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//设置/取消车辆防盗状态
		function guard(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			var operationType= $("input[name='operationType']:checked").val();
			if(operationType =='' || operationType == undefined){
				alert("参数不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/guard',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'operationType':operationType
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//查询车辆防盗状态
		function queryGuard(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/queryGuard',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//当前位置查询
		function queryCurrentLocation(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var mobileType = "0";
			
			$.ajax({
				url:'${basePath}/api/obd/queryCurrentLocation',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'mobileType':mobileType
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//车辆体检
		function monitorFault(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/monitorFault',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//油耗查询
		function queryPetrol(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var beginTime = $("#beginTime").val();
			var endTime = $("#endTime").val();
			if(beginTime =='' || endTime == ''){
				alert("查询时间不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/queryPetrol',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'beginTime':beginTime,
					'endTime':endTime
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//驾驶优化建议
		function optimizeDrive(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var beginDate = $("#beginDate").val();
			var endDate = $("#endDate").val();
			if(beginDate =='' || endDate == ''){
				alert("查询时间不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/optimizeDrive',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'beginDate':beginDate,
					'endDate':endDate
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//查询运行轨迹
		function queryRunningTrack(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var startTime9 = $("#startTime9").val();
			var endTime9 = $("#endTime9").val();
			if(startTime9 =='' || endTime9 == ''){
				alert("查询时间不能为空.");
				return false;
			}
			var sD = startTime9.substr(0,10);
			var eD = endTime9.substr(0,10);
			if(sD !== eD){
				alert("请选择同一天日期.");
				return false;
			}
			var theDate = sD;
			var startTime = startTime9.substr(startTime9.length-8,startTime9.length);
			var endTime = endTime9.substr(endTime9.length-8,endTime9.length);
			
			$.ajax({
				url:'${basePath}/api/obd/queryRunningTrack',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'startTime':startTime,
					'endTime':endTime,
					'theDate':theDate
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//WIFI开关
		function controlGps(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			var state= $("input[name='state10']:checked").val();
			if(state =='' || state == undefined){
				alert("参数不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/controlGps',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'state':state
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//GPS开关
		function controlWifi(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			var state= $("input[name='state11']:checked").val();
			if(state =='' || state == undefined){
				alert("参数不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/controlWifi',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'state':state
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//查询流量卡流量信息
		function queryNetFlow(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/queryNetFlow',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//查询水温、电压信息
		function queryCurrentObdInfo(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/queryCurrentObdInfo',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//portal
		function portal(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var type = $("#type14").val();
			if(type==''){
				alert("请选择类型.");
				return false;
			}
			var url = $("#url").val().trim();
			var mb = $("#mb").val().trim();
			var mac = $("#mac").val().trim();
			var whitelists = $("#whitelists").val().trim();
			var onOff= $("input[name='onOff']:checked").val();
			
			if(type=='6'){
				if(onOff=='' || onOff == undefined){
					alert("请选择开关状态.");
					return false;
				}
			}
			
			$.ajax({
				url:'${basePath}/api/obd/portal',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'type':type,
					'url':url,
					'mb':mb,
					'mac':mac,
					'whitelists':whitelists,
					'onOff':onOff
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//portal
		function dzwl(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var type = $("#type15").val();
			var railAndAlert = $("#railAndAlert").val();
			if(type=='' || railAndAlert == ''){
				alert("请选择类型.");
				return false;
			}
			var obdMsnList = $("#obdMsnList").val().trim();
			var maxLongitude = $("#maxLongitude").val().trim();
			var maxLatitude = $("#maxLatitude").val().trim();
			var minLongitude = $("#minLongitude").val().trim();
			var minLatitude = $("#minLatitude").val().trim();
			var startDate = $("#startDate15").val();
			var endDate = $("#endDate15").val();
			var areaNum = $("#areaNum").val();
			
			
			$.ajax({
				url:'${basePath}/api/obd/dzwl',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'type':type,
					'obdMsnList':obdMsnList,
					'railAndAlert':railAndAlert,
					'maxLongitude':maxLongitude,
					'maxLatitude':maxLatitude,
					'minLongitude':minLongitude,
					'minLatitude':minLatitude,
					'startDate':startDate,
					'endDate':endDate,
					'areaNum':areaNum
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//驾驶行为参数设置
		function setdriveBehaviour(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var quickenSpeed = $("#quickenSpeed").val().trim();
			var quickSlowDownSpeed = $("#quickSlowDownSpeed").val().trim();
			var quickturnSpeed = $("#quickturnSpeed").val().trim();
			var quickturnAngle = $("#quickturnAngle").val().trim();
			var overspeed = $("#overspeed").val().trim();
			var overspeedTime = $("#overspeedTime").val().trim();
			var fatigueDrive = $("#fatigueDrive").val().trim();
			var fatigueSleep = $("#fatigueSleep").val().trim();
			if(quickenSpeed=='' && quickSlowDownSpeed =='' && quickturnSpeed =='' 
				 && quickturnAngle =='' && overspeed =='' && overspeedTime ==''
				 && fatigueDrive ==''  && fatigueSleep =='' ){
				alert("请求参数不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/setdriveBehaviour',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'quickenSpeed':quickenSpeed,
					'quickSlowDownSpeed':quickSlowDownSpeed,
					'quickturnSpeed':quickturnSpeed,
					'quickturnAngle':quickturnAngle,
					'overspeed':overspeed,
					'overspeedTime':overspeedTime,
					'fatigueDrive':fatigueDrive,
					'fatigueSleep':fatigueSleep
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//Wifi使用休眠时间
		function wifiUseTime(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var useTime = $("#useTime").val().trim();
			if(useTime=='' ){
				alert("请求参数不能为空.");
				return false;
			}
			
			
			$.ajax({
				url:'${basePath}/api/obd/wifiUseTime',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'useTime':useTime
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//设备号转换查询接口
		function obdSnChange(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var obdSn = $("#obdSn16").val().trim();
			if(obdSn=='' ){
				alert("请求参数不能为空.");
				return false;
			}
			
			
			$.ajax({
				url:'${basePath}/api/obd/obdSnChange',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'obdSn':obdSn
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//驾驶行为
		function driveBehaviour(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var beginDate = $("#beginDate21").val();
			var endDate = $("#endDate21").val();
			if(beginDate =='' || endDate == ''){
				alert("查询时间不能为空.");
				return false;
			}
			
			$.ajax({
				url:'${basePath}/api/obd/driveBehaviour',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'beginDate':beginDate,
					'endDate':endDate
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//obd报警开关
		function alarmSwitchState(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var switchType = $("#switchType22").val();
			if(switchType =='' || switchType == undefined){
				alert("参数不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/alarmSwitchState',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'switchType':switchType
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//
		function domainTest(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var type = $("#type23").val();
			if(type =='' || type == undefined){
				alert("参数类型不能为空.");
				return false;
			}
			
			var whiteSwitch= $("input[name='whiteSwitch']:checked").val();
			var blackSwitch= $("input[name='blackSwitch']:checked").val();
			var mac = $("#mac23").val().trim();
			var domainName = $("#domainName").val().trim();
			
			
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/domain',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'type':type,
					'whiteSwitch':whiteSwitch,
					'blackSwitch':blackSwitch,
					'mac':mac,
					'domainName':domainName
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//查询域黑白名单设置
		function domainQuery(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/domainQuery',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//portal
		function fenceSet(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			var type = $("#type25").val();//类型
			var obdMsnList = $("#obdMsnList25").val();//类型
			var areaNum = $("#areaNum25").val();
			var points = $("#points25").val();
			var alert25 = $("#alert25").val();//告警方式
			var timerType = $("#timerType25").val();//定时规则
			var dayWeek = "";
			$('input[name="dayWeek"]:checked').each(function(){
				dayWeek+=$(this).val()+",";
			}); 
			var startDate = $("#startDate25").val();
			var endDate = $("#endDate25").val();
			var startTime = $("#startTime25").val();
			var endTime = $("#endTime25").val();
			if(type=='' || type == undefined || type == null){
				alert("请选择类型.");
				return false;
			}
			
			if(obdMsnList==''){
				alert("表面码不能为空.");
				return false;
			}
			
			if(areaNum!=''){
				var reg = new RegExp("^[0-9]*$");
				if(!reg.test(areaNum)){
					alert("围栏编号必须为阿拉伯数字.");
					return false;
				}
			}
			
			if(dayWeek!=""){
				dayWeek = dayWeek.substring(0,dayWeek.length-1);
			}
			
			$.ajax({
				url:'${basePath}/api/obd/fenceSet',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'type':type,
					'obdMsnList':obdMsnList,
					'areaNum':areaNum,
					'points':points,
					'alert':alert25,
					'timerType':timerType,
					'dayWeek':dayWeek,
					'startDate':startDate,
					'endDate':endDate,
					'startTime':startTime,
					'endTime':endTime
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//查询域黑白名单设置
		function fenceQuery(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			$.ajax({
				url:'${basePath}/api/obd/fenceQuery',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//查询域黑白名单设置
		function carType(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			var operType = $("#operType27").val();
			var typeId = $("#typeId27").val();
			if(operType==0 || operType==1){
				if(typeId==''){
					alert("请输入车辆型号.");
					return false;
				}
			}
			
			
			$.ajax({
				url:'${basePath}/api/obd/carType',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'operType':operType,
					'typeId':typeId
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		//查询域黑白名单设置
		function queryMiles(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			
			$.ajax({
				url:'${basePath}/api/obd/queryMiles',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//设备自动唤醒开关
		function wakeup(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			var type = $("#type29").val();
			var wakeupSwitch= $("input[name='wakeupSwitch29']:checked").val();
			var wakeupTime= $("#wakeupTime29").val();
			if(type==0 && wakeup==''){
				alert("请求参数不足.");
				return false;
			}
			if(type==1 && wakeupTime==''){
				alert("请求参数不足.");
				return false;
			}
			if(type==1){
				var reg = new RegExp("^[0-9]*$");
				if(!reg.test(wakeupTime)){
					alert(wakeupTime);
					alert("请输入阿拉伯数字.");
					return false;
				}
			}
			
			
			$.ajax({
				url:'${basePath}/api/obd/wakeup',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'type':type,
					'wakeupSwitch':wakeupSwitch,
					'wakeupTime':wakeupTime
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		//设备自动唤醒开关
		function wakeupQuery(){
			var deviceId = $("#deviceId").val().trim();
			if(deviceId ==''){
				alert("表面号不能为空.");
				return false;
			}
			var sign = $.md5(deviceId+username+time+pwd);
			
			var type = $("#type30").val();
			
			
			$.ajax({
				url:'${basePath}/api/obd/wakeupQuery',
				data:{
					'deviceId':deviceId,
					'username':username,
					'time':time,
					'sign':sign,
					'type':type
				},
				type:'post',
				cache : false,
				async : false,
				dataType :'json',
				success:function(data){
					var obj = eval(data); 
					$("#showMsg").html(JSON.stringify(obj));
				},
				error : function(){
					alert("异常,请稍后重试.");
				}
			});
		}
		
		
		function isChinese(temp)
	 	{
		  var re=/[\u0391-\uFFE5]+/;
	 	 // var re=/[\u4e00-\u9fa5\uf900-\ufa2d]+/;
	 	  return re.test(temp);
	 	   
	 	}
		function wifiPwdNameCheck(temp){
			var reg=/^[0-9a-zA-Z]*$/g ;
			return reg.test(temp);
		}
		
		function checkPhone(phone){ 
			 var pattern = /^1[34578]\d{9}$/;  
		    if (pattern.test(phone)) {  
		        return true;  
		    }  
		    return false;  
		}
	</script>
</head>

<body>
	<h4>
		表面码: <input type="text" id="deviceId" value="" />
	</h4>
	<form name="f1" id="f1">
		<h4>激活/绑定终端</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>手机号码:</td>
				<td><input type="text" id="userId" value="" /></td>
				<td>二维码:</td>
				<td><input type="text" id="hgDeviceSn" value="" /></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="bind()" /></td>
			</tr>
		</table>
	</form>
	<form name="f2" id="f2">
		<h4>解绑</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="unBind()" /></td>
			</tr>
		</table>
	</form>
	<form name="f3" id="f3">
		<h4>设置/取消车辆防盗状态</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>状态:</td>
				<td>设防:<input type="radio" name="operationType" value="1">&nbsp;
					&nbsp; 撤防:<input type="radio" name="operationType" value="0">
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="guard()" /></td>
			</tr>
		</table>
	</form>

	<form name="f4" id="f4">
		<h4>查询车辆防盗状态</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryGuard()" /></td>
			</tr>
		</table>
	</form>

	<form name="f5" id="f5">
		<h4>当前位置查询</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryCurrentLocation()" /></td>
			</tr>
		</table>
	</form>

	<form name="f6" id="f6">
		<h4>车辆体检</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="monitorFault()" /></td>
			</tr>
		</table>
	</form>

	<form name="f7" id="f7">
		<h4>油耗查询</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<th>开始时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="beginTime"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
				<th>结束时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endTime"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryPetrol()" /></td>
			</tr>
		</table>
	</form>

	<form name="f8" id="f8">
		<h4>驾驶优化建议</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<th>开始时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="beginDate"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
				<th>结束时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endDate"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="optimizeDrive()" /></td>
			</tr>
		</table>
	</form>

	<form name="f9" id="f9">
		<h4>查询运行轨迹</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<th>开始时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="startTime9"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
				<th>结束时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endTime9"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryRunningTrack()" /></td>
			</tr>
		</table>
	</form>

	<form name="f10" id="f10">
		<h4>GPS开关</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>状态:</td>
				<td>开启:<input type="radio" name="state10" value="1">&nbsp;
					&nbsp; 关闭:<input type="radio" name="state10" value="0">
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="controlGps()" /></td>
			</tr>
		</table>
	</form>

	<form name="f11" id="f11">
		<h4>WIFI开关</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>状态:</td>
				<td>开启:<input type="radio" name="state11" value="1">&nbsp;
					&nbsp; 关闭:<input type="radio" name="state11" value="0">
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="controlWifi()" /></td>
			</tr>
		</table>
	</form>

	<form name="f12" id="f12">
		<h4>查询流量卡流量信息</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryNetFlow()" /></td>
			</tr>
		</table>
	</form>

	<form name="f13" id="f13">
		<h4>查询水温、电压信息</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryCurrentObdInfo()" /></td>
			</tr>
		</table>
	</form>

	<form name="f14" id="f14">
		<h4>Portal设置</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>类型:</td>
				<td><select id="type14" style="width: 200px;" onchange="">
						<option value=""></option>
						<option value="0">设置URL</option>
						<!-- <option value="1" >保留</option>  -->
						<option value="2">流量额度限制</option>
						<option value="3">白名单设置</option>
						<option value="4">全部删除白名单</option>
						<option value="5">单条删除白名单</option>
						<option value="6">portal开关</option>
				</select></td>
			</tr>
			<tr id="f14_0">
				<td>url</td>
				<td><input type="text" id="url" style="width: 100%;"
					value="http://obd.gd118114.cn/html/portal/auth.jsp?" /></td>
			</tr>
			<tr id="f14_2">
				<td>流量额度限制(MB):</td>
				<td><input type="text" id="mb" value="50" /></td>
			</tr>
			<tr id="f14_4">
				<td>手机MAC地址:</td>
				<td><input type="text" id="mac" value="22:22:22:22:22:22" /></td>
			</tr>
			<tr id="f14_3">
				<td>白名单设置(以|分割):</td>
				<td><input type="text" id="whitelists" style="width: 200px;"
					value="ff:ff:ff:ff:ff:ff|ee:ee:ee:ee:ee:ee" /></td>
			</tr>
			<tr id="f14_6">
				<td>状态:</td>
				<td>打开:<input type="radio" name="onOff" value="1"> 关闭:<input
					type="radio" name="onOff" value="0">
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="portal()" /></td>
			</tr>
		</table>
	</form>

	<form name="f15" id="f15">
		<h4>电子围栏</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>表面码列表(英文逗号分隔):</td>
				<td colspan="3"><input type="text" id="obdMsnList"
					style="width: 100%;" value='4411600020128,4411600020849' /></td>
			</tr>
			<tr>
				<td>类型:</td>
				<td><select id="type15" style="width: 200px;" onchange="">
						<option value=""></option>
						<option value="0">普通电子围栏</option>
						<option value="1">定时定点电子围栏</option>
				</select></td>
				<td>围栏类型+报警方式:</td>
				<td><select id="railAndAlert" style="width: 200px;" onchange="">
						<option value=""></option>
						<option value="1">进区域报警</option>
						<option value="2">出区域报警</option>
						<option value="3">进出都报警</option>
						<option value="4">取消围栏</option>
						<option value="5">取消所有围栏</option>
				</select></td>
			</tr>
			<tr id="f14_2">
				<td>大经:</td>
				<td><input type="text" id="maxLongitude" value="113.040000" />
				</td>
				<td>大纬:</td>
				<td><input type="text" id="maxLatitude" value="33.040000" /></td>
			</tr>
			<tr id="f14_3">
				<td>小经:</td>
				<td><input type="text" id="minLongitude" value="112.040000" />
				</td>
				<td>小纬:</td>
				<td><input type="text" id="minLatitude" value="32.040000" /></td>
			</tr>
			<tr>
				<th>定时开始时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="startDate15"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
				<th>定时结束时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endDate15"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
			</tr>
			<tr id="f14_0">
				<td>区域编号</td>
				<td><input type="text" id="areaNum" value="0" /></td>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="dzwl()" /></td>
			</tr>
		</table>
	</form>

	<form name="f16" id="f16">
		<h4>设备号转换查询接口</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>Obd设备号:</td>
				<td><input type="text" id="obdSn16" value="2f100077" /></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="obdSnChange()" /></td>
			</tr>
		</table>
	</form>

	<form name="f17" id="f17">
		<h4>Wifi使用休眠时间</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>时间(单位：分钟 1-20):</td>
				<td><input type="text" id="useTime" value="" /></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="wifiUseTime()" /></td>
			</tr>
		</table>
	</form>

	<form name="f18" id="f18">
		<h4>驾驶行为参数设置</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>急加速速度(km/h):</td>
				<td><input type="text" id="quickenSpeed" value="" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>急减速速度(km/h):</td>
				<td><input type="text" id="quickSlowDownSpeed" value="" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>急转弯速度(km/h):</td>
				<td><input type="text" id="quickturnSpeed" value="" /></td>
				<td>急转弯角度:</td>
				<td><input type="text" id="quickturnAngle" value="" /></td>
			</tr>
			<tr>
				<td>超速速度(km/h):</td>
				<td><input type="text" id="overspeed" value="" /></td>
				<td>超速时间-秒:</td>
				<td><input type="text" id="overspeedTime" value="" /></td>
			</tr>
			<tr>
				<td>连续疲劳驾驶驾驶时间-分:</td>
				<td><input type="text" id="fatigueDrive" value="" /></td>
				<td>疲劳驾驶休息时间-分:</td>
				<td><input type="text" id="fatigueSleep" value="" /></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="setdriveBehaviour()" /></td>
			</tr>
		</table>
	</form>

	<form name="f19" id="f19">
		<h4>wifi密码和名称</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>密码(不支持中文,长度{8,30}):</td>
				<td><input type="text" id="wPwd" value="" /></td>
				<td>名称(不支持中文,长度{1,30}):</td>
				<td><input type="text" id="wName" value="" /></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="wifiPwdSsid()" /></td>
			</tr>
		</table>
	</form>

	<form name="f20" id="f20">
		<h4>OBD设备报警开关</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>开关:</td>
				<td><select id="switchType" style="width: 200px;">
						<option value=""></option>
						<option value="0" >非法启动探测</option>
						<option value="1" >非法震动探测</option>
						<!--  <option value="2" >蓄电电压异常报警</option> -->
						<!--  <option value="3" >发动机水温高报警</option> -->
						<option value="4" >车辆故障报警</option>
						<option value="5">超速报警</option>
						<option value="6">电子围栏报警</option>
						<!--  <option value="7" >保留</option> -->
						<option value="8">疲劳驾驶</option>
						<option value="9">急变速</option>
						<option value="10">长怠速</option>
				</select></td>
				<td>状态:</td>
				<td>打开:<input type="radio" name="switchState" value="1">
					关闭:<input type="radio" name="switchState" value="0">
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="alarmSwitch()" /></td>
			</tr>
		</table>
	</form>


	<form name="f21" id="f21">
		<h4>驾驶行为</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<th>开始时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="beginDate21"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
				<th>结束时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endDate21"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
				</td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="driveBehaviour()" /></td>
			</tr>
		</table>
	</form>

	<form name="f22" id="f22">
		<h4>obd开关状态查询</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>开关:</td>
				<td><select id="switchType22" style="width: 200px;">
						<option value=""></option>
						<option value="0" >非法启动探测</option>
						<option value="1" >非法震动探测</option>
						<!--<option value="2" >蓄电电压异常报警</option> -->
						<!--<option value="3" >发动机水温高报警</option> -->
						<option value="4" >车辆故障报警</option>
						<option value="5">超速报警</option>
						<option value="6">电子围栏报警</option>
						<!-- <option value="7" >保留</option>  -->
						<option value="8">疲劳驾驶</option>
						<option value="9">急变速</option>
						<option value="10">长怠速</option>
						<option value="11">wifi开关</option>
						<option value="12">gps开关</option>
						<!-- <option value="13">设防撤防开关</option> -->
				</select></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="alarmSwitchState()" /></td>
			</tr>
		</table>
	</form>

	<form name="f23" id="f23">
		<h4>域黑白名单设置</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>开关:</td>
				<td><select id="type23" style="width: 200px;">
						<option value=""></option>
						<option value="0">域白名单功能开关</option>
						<option value="1">域黑名单功能开关</option>
						<option value="2">禁止MAC上网</option>
						<option value="3">增加多个域白名单</option>
						<option value="4">删除单个域白名单</option>
						<option value="5">删除所有域白名单</option>
						<option value="6">增加多个域黑名单</option>
						<option value="7">删除单个域黑名单</option>
						<option value="8">删除所有域黑名单</option>
				</select></td>
			</tr>
			<tr>
				<td>域白名单功能开关:</td>
				<td>打开:<input type="radio" name="whiteSwitch" value="1">&nbsp;
					&nbsp; 关闭:<input type="radio" name="whiteSwitch" value="0">
				</td>
			</tr>
			<tr>
				<td>域黑名单功能开关:</td>
				<td>打开:<input type="radio" name="blackSwitch" value="1">&nbsp;
					&nbsp; 关闭:<input type="radio" name="blackSwitch" value="0">
				</td>
			</tr>
			<tr>
				<td>手机MAC:</td>
				<td><input type="text" id="mac23" style="width: 100%;" /></td>
			</tr>
			<tr>
				<td>域名(多个时以英文;分隔):</td>
				<td><textarea rows="3" cols="1" name="domainName"
						id="domainName" style="width: 100%;"></textarea></td>
			</tr>
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="domainTest()" /></td>
			</tr>
		</table>
	</form>

	<form name="f24" id="f24">
		<h4>查询域黑白名单设置:</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="domainQuery()" /></td>
			</tr>
		</table>
	</form>
	
	<form name="f25" id="f25">
		<h4>多区域电子围栏</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>表面码列表(英文逗号分隔):</td>
				<td colspan="3"><input type="text" id="obdMsnList25"
					style="width: 100%;" value='4411600020128,4411600020849'/></td>
			</tr>
			<tr>
				<td>类型:</td>
				<td><select id="type25" style="width: 200px;">
						<option value=""></option>
						<option value="1">新增围栏</option>
						<option value="2">取消围栏</option>
						<option value="3">取消所有围栏</option>
						<option value="4">暂停使用围栏</option>
						<option value="5">恢复使用围栏</option>
				</select></td>
				<td>报警方式:</td>
				<td><select id="alert25" style="width: 200px;">
						<option value=""></option>
						<option value="1">进区域报警</option>
						<option value="2">出区域报警</option>
						<option value="3">进出都报警</option>
				</select></td>
			</tr>
			<tr>
				<td>围栏顶点</td>
				<td colspan="3"><input type="text" id="points25" value="[{'longitude':'11','latitude':'22'},{'longitude':'33','latitude':'44'},{'longitude':'55','latitude':'66'},{'longitude':'77','latitude':'88'}]" style="width: 100%;"/></td>
			</tr>
			<tr id="f14_2">
				<td>定时类型:</td>
				<td>
					<select id="timerType25" style="width: 200px;">
						<option value=""></option>
						<option value="1">星期几</option>
						<option value="2">每天</option>
						<option value="3">日期范围</option>
					</select>
				</td>
				<td>星期几:</td>
				<td>
					星期一:<input type="checkbox" name="dayWeek"  id="dayWeek1" value="2"/> &nbsp;
					星期二:<input type="checkbox" name="dayWeek"  id="dayWeek2" value="3"/> &nbsp;
					星期三:<input type="checkbox" name="dayWeek"  id="dayWeek3" value="4"/> &nbsp;
					星期四:<input type="checkbox" name="dayWeek"  id="dayWeek4" value="5"/> &nbsp; 
					星期五:<input type="checkbox" name="dayWeek"  id="dayWeek5" value="6"/> &nbsp;
					星期六:<input type="checkbox" name="dayWeek"  id="dayWeek6" value="7"/> &nbsp; 
					星期日:<input type="checkbox" name="dayWeek"  id="dayWeek7" value="1"/> &nbsp;
				</td>
			</tr>
			<tr>
				<th>定时开始时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="startTime25"
					onclick="WdatePicker({dateFmt: 'HH:mm:ss'})" />
				</td>
				<th>定时结束时间</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endTime25"
					onclick="WdatePicker({dateFmt: 'HH:mm:ss'})" />
				</td>
			</tr>
			<tr>
				<th>定时开始日期</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="startDate25"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" />
				</td>
				<th>定时结束日期</th>
				<td class="pn-fcontent"><input type="text" class="Wdate"
					readonly="readonly" id="endDate25"
					onclick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" />
				</td>
			</tr>
			<tr>
				<td>区域编号</td>
				<td><input type="text" id="areaNum25" value="1" /></td>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="fenceSet()" /></td>
			</tr>
		</table>
	</form>
	
	<form name="f26" id="f26">
		<h4>查询电子围栏:</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="fenceQuery()" /></td>
			</tr>
		</table>
	</form>
	
	<form name="f27" id="f27">
		<h4>车辆型号:</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作类型:</td>
				<td><select id="operType27" style="width: 200px;">
						<option value="0">新增</option>
						<option value="1">修改</option>
						<option value="2">删除</option>
						<option value="3">查询</option>
				</select></td>
				<td>车辆型号</td>
				<td><input type="text" id="typeId27" value=""/></td>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="carType()" /></td>
			</tr>
		</table>
	</form>
	
	<form name="f28" id="f28">
		<h4>车辆里程:</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="queryMiles()" /></td>
			</tr>
		</table>
	</form>
	
	<form name="f29" id="f29">
		<h4>设备自动唤醒开关:</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作类型:</td>
				<td><select id="type29" style="width: 200px;">
						<option value="0">开关</option>
						<option value="1">时间</option>
				</select></td>
				<td>状态</td>
				<td>关闭:<input type="radio" name="wakeupSwitch29" value="0">&nbsp;
					&nbsp; 打开:<input type="radio" name="wakeupSwitch29" value="1">
				</td>
				<td>唤醒时间(单位10分钟)</td>
				<td><input type="text" id="wakeupTime29" value=""/></td>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="wakeup()" /></td>
			</tr>
		</table>
	</form>
	
	<form name="f30" id="f30">
		<h4>设备自动唤醒开关和时间查询:</h4>
		<table border="1px" style="text-align: left; margin: auto;">
			<tr>
				<td>操作类型:</td>
				<td><select id="type30" style="width: 200px;">
						<option value="0">开关</option>
						<option value="1">时间</option>
				</select></td>
				<td>操作</td>
				<td><input class="btn btn-s-md btn-success" type="button"
					value="确定" onclick="wakeupQuery()" /></td>
			</tr>
		</table>
	</form>
	
	
	<br/>
	<br/>
	<br/>
	<h1>返回报文</h1>
	<div id="showMsg" style="color: red;height:auto !important;"></div>
	<br/>
	<br/>
</body>
</html>
