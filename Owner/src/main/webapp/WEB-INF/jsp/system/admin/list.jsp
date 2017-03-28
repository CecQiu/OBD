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
	
	function del(id) {
		if (confirm("系统提示：确认要停用该用户？")) {
			window.location.href = "admin_delete.do?admin.id=" + id;
		}
	}
	function enable(id) {
		if (confirm("系统提示：确认要启用该用户？")) {
			window.location.href = "admin_enable.do?admin.id=" + id;
		}
	}
	function add() {
		window.location.href = "admin_add.do";
	}
	function reset1(){
		get("admin.username").value = "";
		get("admin.name").value = "";
		get("admin.valid").value = "";
	}
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="admin_list.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>登录名:</th>
							<td class="pn-fcontent"><input name="admin.username" value="${admin.username}"
								maxlength="20" type="text" /></td>
							<th>真实姓名:</th>
							<td class="pn-fcontent"><input name="admin.name" value="${admin.name}"
								maxlength="20" type="text" /></td>
						</tr>
						<tr>
							<th>状态:</th>
							<td class="pn-fcontent" colspan="3"><select name="admin.valid" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${admin.valid=='1'}">selected</c:if>>启用</option>
									<option value="0" <c:if test="${admin.valid=='0'}">selected</c:if>>停用</option>
							</select></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" type="button" value="添加" auth="auth-add" onclick="add()"/>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="reset1()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="separator line"></div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>用户列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>登录名</th>
							<th>真实姓名</th>
							<th>性别</th>
							<th>启用</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td><a href='admin_info.do?admin.id=${item.id}'>${item.username}</a></td>
								<td>${item.name}</td>
								<td>${item.sex}</td>
								<td><c:if test="${item.valid==1}">是</c:if> <c:if
										test="${item.valid!=1}">否</c:if></td>
								<td><c:if test="${item.valid==1}">
										<a href="admin_edit.do?admin.id=${item.id}" auth="auth-edit"
											class="btn btn-info"><i class="icon-pencil"></i>&nbsp;&nbsp;修改</a>
											<c:if test="${item.username!='admin'}">
												<a href="javascript:del(${item.id});"
												class="btn btn-important"><i class="icon-lock"></i>&nbsp;&nbsp;停用</a>
											</c:if>
									</c:if> <c:if test="${item.valid==0}">
										<a href="javascript:enable(${item.id});"
											class="btn btn-success"><i class="icon-unlock"></i>&nbsp;&nbsp;启用</a>
									</c:if></td>
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