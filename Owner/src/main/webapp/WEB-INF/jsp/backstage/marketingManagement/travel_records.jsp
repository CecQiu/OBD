<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>营销管理 - 行程记录统计</title>
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
							<th>车牌号：</th>
							<td class="pn-fcontent"><input name="search_value" value="${search_value }" style="width: 90%"
								maxlength="20" type="text" /></td>
							<th>时间段：</th>
							<td class="pn-fcontent">
								<input type="text" id="startTime" name="startTime" readonly="readonly" value="<fmt:formatDate value='${startTime }' pattern='yyyy-MM-dd' />" style="width: 48%;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'startTime\')}'})"/>
									 - <input type="text" id="endTime" name="endTime" readonly="readonly" value="<fmt:formatDate value='${endTime }' pattern='yyyy-MM-dd' />" style="width: 48%;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'endTime\')}'})"/>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="doReset()"/>&nbsp;
						<input class="btn btn-s-md btn-success" type="button" value="打印" />&nbsp;
						<input class="btn btn-s-md btn-success" type="button" value="导出" />&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="separator line"></div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>行程记录列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>姓名</th>
							<th>车牌号</th>
							<th>里程</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.count}</td>
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