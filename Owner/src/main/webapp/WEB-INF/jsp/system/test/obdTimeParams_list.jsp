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
		$("#obdSn").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		if(obdSn=='' && starTime=='' && endTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdTimeParams_query.do" method="post" onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdTimeParams.obdSn" value="${obdTimeParams.obdSn}" />
							</td>
							<th></th>
							<td class="pn-fcontent">
							</td>
						</tr>
						
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
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
				<h5>OBD设备时间参数表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list"  style="overflow-x: scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>进入休眠模式时间-秒</th>
							<th>熄火后WIFI使用时间-分</th>
							<th>GPS数据采集时间间隔-秒</th>
							<th>位置数据上传时间间隔-秒</th>
							<th>OBD在线心跳包时间间隔-分</th>
							<th>OBD离线心跳包时间间隔-10分钟</th>
							<th>OBD离线数据保存时间间隔-秒</th>
							<th>请求AGPS数据包延时时间-秒</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdTimeParamsList}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>${item.sleepTime}</td>
								<td>${item.wifiUseTime}</td>
								<td>${item.gpsCollectDataTime}</td>
								<td>${item.positionDataTime}</td>
								<td>${item.obdOnlineTime}</td>
								<td>${item.obdOfflineTime}</td>
								<td>${item.obdOffDataTime}</td>
								<td>${item.requestAGPSTime}</td>
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