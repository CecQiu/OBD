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
	if("${result}"=="SUCCESS"){
		alert("添加分组成功！");
	}
	if("${message}"=="deleteSuccess") {
		alert("删除成功！");
	}
	if("${message}"=="updateSuccess") {
		alert("更新成功！");
	}
	
	function del(id){
		if(confirm("确认要删除吗？")){
			window.location.href = "obdGroup_delete.do?obdGroup.id="+id;
		}
	}
	
	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
		}
	function edit(id,pageSize,currentPage){
		window.location.href ="obdGroup_edit.do?obdGroup.id="+id+"&pager.pageSize="+pageSize+"&pager.currentPage="+currentPage+"&pager.rowIndex="+get("pager.rowIndex").value;
	}
	
	function myFormReset(){
		$("#groupNum").val("");
		$("#groupName").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	function add(){
		window.location.href = "obdGroup_add.do";
	}	
	
	function toObdUnRegExcel(id) {
		//跳转上传未激活设备设置默认分组页面
		window.location.href="${basePath}/admin/obdGroup_toObdUnRegExcel.do?obdGroup.id=" + id;
	}
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdGroup_query.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>编号</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="groupNum" name="obdGroup.groupNum" value="${obdGroup.groupNum}" />
							</td>
							<th>名称</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="groupName" name="obdGroup.groupName" value="${obdGroup.groupName}" />
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
						<input class="btn btn-primary pull-center" type="button" value="添加" onclick="add();"/>&nbsp;
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
				<h5>分组列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>编号</th>
							<th>名称</th>
							<th>经度</th>
							<th>纬度</th>
							<th>半径</th>
							<th>备注</th>
							<th>创建时间</th>
							<th>操&nbsp;&nbsp;作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdGroups}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td>${item.groupNum}</td>
								<td>${item.groupName}</td>
								<td>${item.longitude}</td>
								<td>${item.latitude}</td>
								<td>${item.radius}</td>
								<td>${item.remark}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<a href="javascript:edit('${item.id}',${pager.pageSize},${pager.currentPage});" class="btn btn-info"><i class="icon-pencil"></i>&nbsp;&nbsp;修改</a>
									<a href="javascript:del('${item.id}',${pager.pageSize},${pager.currentPage});" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;&nbsp;删除</a>
									<button type="button" id="excelUpgrade" class="btn" onclick="toObdUnRegExcel('${item.id}');" auth="auth-push">未激活设备分组</button>
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