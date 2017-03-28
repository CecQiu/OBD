<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>车主服务 - 故障提醒 - 故障列表</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	
});
</script>
<style>
.pn-ftable td {
	width: auto;
}
</style>
</head>
<body>
	<form name="myForm" id="myForm" action="owners_showMalfunctionDetail.do" method="post">
		<input type="hidden" name="obdsn" value="${obdsn }" />
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：故障提醒 - 故障列表</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table id="table_conditions" class="pn-ftable table-bordered table-condensed" 
					border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>姓名：</th>
							<td>${memberCar.name }</td>
							<th>性别：</th>
							<td>
								<c:choose>
									<c:when test="${memberCar.sex == '01' }">男</c:when>
									<c:when test="${memberCar.sex == '02' }">女</c:when>
									<c:otherwise>未知</c:otherwise>
								</c:choose>
							</td>
							<th>手机号：</th>
							<td>${memberCar.mobileNumber }</td>
						</tr>
						<tr>
							<th>车牌号：</th>
							<td>${memberCar.license }</td>
							<th>智能激活盒码：</th>
							<td>${memberCar.obdSn }</td>
							<th>车型：</th>
							<td>${memberCar.typeName }</td>
						</tr>
					</tbody>
				</table>
				<%-- --%>
				<div class="widget-bottom">
					<center>
					<input class="btn btn-danger pull-center" type="button" value="返回" onclick="doGoBack()"/>&nbsp;
					</center>
				</div> 
				
			</div>
			<!-- /widget-content -->
		</div>
		<div class="separator line"></div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>故障列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>故障码</th>
							<th>故障内容</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td>${item.faultCode }</td>
								<td>${item.faultCname }</td>
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