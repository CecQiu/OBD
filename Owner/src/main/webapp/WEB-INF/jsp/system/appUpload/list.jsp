<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<jsp:include page="../../include/common.jsp" />
<title></title>
<script language="javascript">	
	$().ready(function () {
		$("#myForm").validate();
	});
	
	function add(){
		window.location.href = "appUpload_add.do";
	}
	
	function del(id) {
		if (confirm("确认要删除吗？")) {
			jQuery.post('appUpload_delete.do',{
				'info.id':id
			},function(data, textStatus, jqXHR){
				if(data != 'success') {
					alert("删除成功");
					$("#myForm").submit();
				}
			},'text');
		}
	}
	
	function reset1(){
		$(":text").val('');
		$('select').each(function(index,element){
			element.selectedIndex = 0;
		});
		document.getElementById("child.currentPage").value = '${pager.currentPage}';
	}
</script>
</head>
<body>
	<form id="myForm" name="myForm" action="appUpload_list.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-bordered">
					<tbody>
						<tr>
							<th>系统名称:</th>
							<td class="pn-fcontent">
								<input type="text" style="width: 200px" id="systemName"  name="info.systemName" maxlength="20" value="${info.systemName}"/></td>
							<td class="info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" type="button" value="添加" auth="auth-add" onclick="add()"/>
						<input class="btn btn-s-md btn-success" type="submit" value="查询" />&nbsp;
						<input class="btn btn-danger btn-s-md" type="button" onclick="reset1()" value="重置"/>&nbsp;
					</center>
				</div>
			</div>
		</div>
		<div class="separator line"></div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>彩站信息列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>系统名称</th>
							<th>系统最低版本</th>
							<th>APP版本号</th>
							<th>更新时间</th>
							<th>更新内容</th>
							<th width="80px">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr id="tr${status.count}">
								<td>${item.systemName}</td>
								<td>${item.minSysVersion}</td>
								<td>${item.appVersion}</td>
								<td>${item.updateTime}</td>
								<td>${item.updateContent}</td>
								<td>
									<a href="javascript:del(${item.id});" class="btn btn-warning" auth="auth-del">
										<i class="icon-trash"></i>&nbsp;&nbsp;删除</a>
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