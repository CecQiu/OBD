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
	<form name="myForm" id="myForm" action="obdState_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="obdState.obdSn" value="${obdState.obdSn}" />
							</td>
							<th></th>
							<td class="pn-fcontent"></td>
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
		<div class="widget widget-table" style="overflow-x: scroll;">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd状态信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>非法启动开关</th>
							<th>车辆震动开关</th>
							<th>蓄电电压异常开关</th>
							<th>发动机水温高开关</th>
							<th>车辆故障开关</th>
							<th>超速开关</th>
							<th>电子围栏开关</th>
							<th>保留开关</th>
							<th>wifi开关</th>
							<th>gps开关</th>
							<th>设防撤防开关</th>
							<th>急变速开关</th>
							<th>长怠速开关</th>
							<th>疲劳驾驶开关</th>
							<th>自动唤醒开关</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdStates}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>
									<c:choose>
										<c:when test="${item.startupSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.startupSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.shakeSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.shakeSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.voltageSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.voltageSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.engineTempSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.engineTempSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.carfaultSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.carfaultSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.overspeedSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.overspeedSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.efenceSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.efenceSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.backupSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.backupSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.wifiState=='0'}">关闭0</c:when>
										<c:when test="${item.wifiState=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.gpsState=='0'}">关闭0</c:when>
										<c:when test="${item.gpsState=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.safetySwitch=='0'}">关闭0</c:when>
										<c:when test="${item.safetySwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.rapidSpeedChangeSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.rapidSpeedChangeSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.idlingSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.idlingSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.fatigueDriveSwitch=='0'}">关闭0</c:when>
										<c:when test="${item.fatigueDriveSwitch=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.wakeup=='0'}">关闭0</c:when>
										<c:when test="${item.wakeup=='1'}">开启1</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
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