<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="../../include/common.jsp" />

<script language="javascript">
	function myFormReset(){
		$("#obdMsn").val("");
		$("#method").val("");
		$("#startTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdMsn=$("#obdMsn").val().trim();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(obdMsn=='' || startTime=='' || endTime ==''){
			alert("表面号和开始时间和结束时间不能为空.");
			return false;
		}
		
		//查询只能查询一周的记录
		var a = startTime.split(" ");     
		var b = a[0].split("-");     
		var c = a[1].split(":");     
		var oldTime = new Date(b[0], b[1]-1, b[2], c[0], c[1], c[2]);  

		var aa = endTime.split(" ");     
		var bb = aa[0].split("-");     
		var cc = aa[1].split(":");     
		var newTime = new Date(bb[0], bb[1]-1, bb[2], cc[0], cc[1], cc[2]);  
		var days = parseInt((newTime.getTime()-oldTime.getTime()) / (1000 * 60 * 60 * 24));
		if(newTime<=oldTime){
			alert("结束时间不能大于开始时间.");
			return false;
		}
		if(days > 31){
			alert("日期范围应在31天内.");
			return false;
		}
		
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdApiRecord_query.do" method="post" onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>表面号</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="obdMsn" name="obdMsn" value="${obdMsn}" />
							</td>
							<th>方法</th>
							<td class="pn-fcontent">
								<select name="method" id="method" style="width: 200px;">
									<option value="">全部</option>
									<option value="bind" <c:if test="${method=='bind'}">selected</c:if>>激活</option>
									<option value="guard" <c:if test="${method=='guard'}">selected</c:if>>设防撤防</option>
									<option value="queryGuard" <c:if test="${method=='queryGuard'}">selected</c:if>>设防状态查询</option>
									<option value="queryCurrentLocation" <c:if test="${method=='queryCurrentLocation'}">selected</c:if>>当前位置</option>
									
									<option value="monitorFault" <c:if test="${method=='monitorFault'}">selected</c:if>>车辆体检</option>
									<option value="queryPetrol" <c:if test="${method=='queryPetrol'}">selected</c:if>>油耗查询</option>
									<option value="optimizeDrive" <c:if test="${method=='optimizeDrive'}">selected</c:if>>驾驶优化</option>
									<option value="queryRunningTrack" <c:if test="${method=='queryRunningTrack'}">selected</c:if>>车辆轨迹</option>
									
									<option value="controlGps" <c:if test="${method=='controlGps'}">selected</c:if>>GPS开关</option>
									<option value="controlWifi" <c:if test="${method=='controlWifi'}">selected</c:if>>WiFi开关</option>
									<option value="queryNetFlow" <c:if test="${method=='queryNetFlow'}">selected</c:if>>查询流量信息</option>
									<option value="queryDeviceStatus" <c:if test="${method=='queryDeviceStatus'}">selected</c:if>>查询设备状态</option>
									
									<option value="queryCurrentObdInfo" <c:if test="${method=='queryCurrentObdInfo'}">selected</c:if>>查询水温电压</option>
									<option value="unBind" <c:if test="${method=='unBind'}">selected</c:if>>解绑</option>
									<option value="portal" <c:if test="${method=='portal'}">selected</c:if>>portal</option>
									<option value="fenceSet" <c:if test="${method=='fenceSet'}">selected</c:if>>电子围栏</option>
									
									<option value="obdSnChange" <c:if test="${method=='obdSnChange'}">selected</c:if>>设备号转换</option>
									<option value="driveBehaviour" <c:if test="${method=='driveBehaviour'}">selected</c:if>>驾驶行为</option>
									<option value="wifiUseTime" <c:if test="${method=='wifiUseTime'}">selected</c:if>>WiFi使用休眠时间</option>
									<option value="setdriveBehaviour" <c:if test="${method=='setdriveBehaviour'}">selected</c:if>>驾驶行为参数设置</option>
									
									<option value="wifiPwdAndName" <c:if test="${method=='wifiPwdAndName'}">selected</c:if>>WiFi名称密码</option>
									<option value="alarmSwitch" <c:if test="${method=='alarmSwitch'}">selected</c:if>>OBD告警开关设置</option>
									<option value="alarmSwitchState" <c:if test="${method=='alarmSwitchState'}">selected</c:if>>告警开关状态查询</option>
									<option value="fenceQuery" <c:if test="${method=='fenceQuery'}">selected</c:if>>电子围栏查询</option>
									
									<option value="domain" <c:if test="${method=='domain'}">selected</c:if>>域名单设置</option>
									<option value="domainQuery" <c:if test="${method=='domainQuery'}">selected</c:if>>域名单查询</option>
									<option value="carType" <c:if test="${method=='carType'}">selected</c:if>>车辆型号</option>
									<option value="queryMiles" <c:if test="${method=='queryMiles'}">selected</c:if>>车辆里程</option>
									
								</select>
							</td>
						</tr>
						
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${startTime}" class="Wdate" readonly="readonly" name="startTime" id="startTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
							</td>
							<th>结束时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="myFormReset();"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>电信接口操作记录</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x: scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>表面号</th>
							<th>URL</th>
							<th>方法名</th>
							<th>请求参数</th>
							<th>返回报文</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdApiRecordList}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdMsn}</td>
								<td>${item.url}</td>
								<td>${item.method}</td>
								<td>${item.param}</td>
								<td>${item.returnMsg}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="widget-bottom">
					<jsp:include page="../../include/pager.jsp" />
				</div>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
</body>
</html>