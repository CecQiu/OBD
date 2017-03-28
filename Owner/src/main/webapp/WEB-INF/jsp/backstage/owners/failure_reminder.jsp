<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>车主服务 - 故障提醒</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	
});

function showMalfunctionDetail(obdsn) {
	window.location.href = "owners_showMalfunctionDetail.do?obdsn=" + obdsn;
}
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="owners_listMalfunction.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table id="table_conditions" class="pn-ftable table-bordered table-condensed" 
					border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>
								<select name="search_type">
									<option value="1" <c:if test="${search_type == 1 }">selected</c:if>>手机号</option>
									<option value="2" <c:if test="${search_type == 2 }">selected</c:if>>车牌号</option>
									<option value="3" <c:if test="${search_type == 3 }">selected</c:if>>智能盒激活码</option>
								</select>
							</th>
							<td class="pn-fcontent"><input name="search_value" value="${search_value }" style="width: 90%"
								maxlength="20" type="text" /></td>
							<th>故障类型：</th>
							<td class="pn-fcontent">
								<select name="problem_type">
									<option value="1" <c:if test="${problem_type == 1 }">selected</c:if>>全部</option>
									<option value="2" <c:if test="${problem_type == 2 }">selected</c:if>>有故障</option>
									<option value="3" <c:if test="${problem_type == 3 }">selected</c:if>>无故障</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="doReset()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="separator line"></div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>车主列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>性别</th>
							<th>手机号</th>
							<th>车牌号</th>
							<th>智能盒激活码（SN码）</th>
							<th>车型</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td>${item.name }</td>
								<td>
									<c:choose>
										<c:when test="${item.sex == '01' }">男</c:when>
										<c:when test="${item.sex == '02' }">女</c:when>
										<c:otherwise>未知</c:otherwise>
									</c:choose>
								</td>
								<td>${item.mobileNumber }</td>
								<td>${item.license }</td>
								<td>${item.obdSn }</td>
								<td>${item.typeName }</td>
								<td><a href="javascript:showMalfunctionDetail('${item.obdSn }');" class="btn btn-important"><i class="icon-lock"></i>查看详情</a>
								</td>
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