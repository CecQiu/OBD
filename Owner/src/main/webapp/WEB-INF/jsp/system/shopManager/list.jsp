<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title></title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">	
	$().ready(function () {
		$("#myForm").validate();
		
	});
	
	function del(id) {
		if (confirm("系统提示：确认删除该用户现有管理彩站的管理权限？")) {
			window.location.href = "adminForShop_delete.do?adm.id=" + id;
		}
	}
	
	function add(){
		window.location.href = "adminForShop_add.do";
	}
	
	function edit(id){
		window.location.href = "adminForShop_edit.do?admin.id=" + id;
	}
	
	function reset1(){
		get("admin.username").value = "";
		get("admin.name").value = "";
		get("admin.valid").value = "";
		get("admin.phone").value = "";
	}
</script>
</head>
<body>
	<!-- <div class="row">
		<div style="width:40%;float: left;" id="users"></div>
		<div style="width:60%;float: right;" id="shops"></div>
	</div>
	<script type="text/javascript">
		$("#users").load("adminForShop_getManager.do");
		$("#shops").load("adminForShop_getShop.do");
	</script> -->
	<form name="myForm" id="myForm" action="adminForShop_list.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-bordered">
					<tbody>
						<tr>
							<th>登录名:</th>
							<td class="pn-fcontent"><input name="admin.username" value="${admin.username}" style="width: 200px;"
								maxlength="20" type="text" /></td>
							<th>真实姓名:</th>
							<td class="pn-fcontent"><input name="admin.name" value="${admin.name}" style="width: 200px;"
								maxlength="20" type="text" /></td>
						</tr>
						<tr>
							<th>状态:</th>
							<td class="pn-fcontent">
								<select name="admin.valid" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${admin.valid=='1'}">selected</c:if>>启用</option>
									<option value="0" <c:if test="${admin.valid=='0'}">selected</c:if>>停用</option>
								</select>
							</td>
							<th>手机号:</th>
							<td class="pn-fcontent">
								<input name="admin.phone" value="${admin.phone}" style="width: 200px;" maxlength="20" type="text" />
							</td>	
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-info" type="button" value="添加管理彩站" onclick="add();"/>&nbsp;
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
				<h5>彩站管理员列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>登录名</th>
							<th>真实姓名</th>
							<th>管理彩站</th>
							<th width="150px">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${managers}" var="item" varStatus="status">
							<tr>
								<td>${item.key.username}</td>
								<td>${item.key.name}</td>
								<td>
									<c:forEach items="${item.value}" var="shop">
										${shop.shopName};
									</c:forEach>
								</td>
								<td>
									<a href="javascript:edit(${item.key.id});" class="btn btn-success">
									<i class="icon-unlock"></i>修改管理彩站</a>
									<a href="javascript:del(${item.key.id});" class="btn btn-warning">
										<i class="icon-trash"></i>&nbsp;&nbsp;删除管理彩站</a>
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