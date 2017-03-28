<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>车主服务 - 车务提醒</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	
});
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="owners_listCarService.do" method="post">
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
								<select>
									<option>手机号</option>
									<option>车牌号</option>
									<option>智能盒激活码</option>
								</select>
							</th>
							<td class="pn-fcontent"><input name="" value="" style="width: 90%"
								maxlength="20" type="text" /></td>
							<th>车务状态：</th>
							<td class="pn-fcontent">
								<select>
									<option>全部</option>
									<option>保险到期</option>
									<option>车船税到期</option>
									<option>年票到期</option>
									<option>年审到期</option>
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
				<h5>车务提醒列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>车牌号</th>
							<th>手机号</th>
							<th>智能盒激活码（SN码）</th>
							<th>姓名</th>
							<th>车型</th>
							<th>车务提醒类型</th>
							<th>上次车务办理时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
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