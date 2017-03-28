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
		alert("参数配置成功！");
	}
	if("${message}"=="deleteSuccess") {
		alert("删除成功！");
	}
	
	function del(id){
		if(confirm("确认要删除吗？")){
			window.location.href = "sysparamconf_delete.do?sysparamconf.id="+id;
		}
	}
	
	function dataToExcel(){
		window.location.href = "systemPara_dataToExcel.do";
	}
	
	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
		}
	function edit(id,pageSize,currentPage){
		window.location.href ="sysparamconf_edit.do?sysparamconf.id="+id+"&pager.pageSize="+pageSize+"&pager.currentPage="+currentPage+"&pager.rowIndex="+get("pager.rowIndex").value;
	}
	
	function reset1(){
		get("sysparamconf.pname").value = "";
		get("sysparamconf.ptype").value = "";
		get("sysparamconf.pvalue").value = "";
		get("sysparamconf.remark").value = "";
	}
	
	function add(){
		window.location.href = "sysparamconf_add.do";
	}	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="sysparamconf_query.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>参数名:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24"  name="sysparamconf.pname" value="${sysparamconf.pname}" />
							</td>
							<th>类&nbsp;&nbsp;型:</th>
							<td class="pn-fcontent">
								<c:if test="${sysparamconf.ptype==null}">
									<select  name="sysparamconf.ptype" style="width: 178px;height: 24px;;">
						                <option value="" selected="selected">- 请选择 -</option>
						                <option value="0">业务参数</option>
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>			          
					                </select>
				                </c:if>
								<c:if test="${sysparamconf.ptype==0}">
									<select  name="sysparamconf.ptype" style="width: 178px;height: 24px;;">			       
						                <option value="" >- 请选择 -</option>
						                <option value="0" selected="selected">业务参数</option>
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>			          
					                </select>
				                </c:if>
								<c:if test="${sysparamconf.ptype==1}">
									<select  name="sysparamconf.ptype" style="width: 178px;height: 24px;;">			                			               
						               <option value="" >- 请选择 -</option>
						                <option value="0" >业务参数</option>
						                <option value="1" selected="selected">其&nbsp;&nbsp;&nbsp;他</option>			          
					                </select>
				                </c:if>
							</td>
						</tr>
						<tr>
							<th>参数值:</th>
							<td class="pn-fcontent">
								<input type="text" size="24"   name="sysparamconf.pvalue" value="${sysparamconf.pvalue}" />
							</td>
							<th>备&nbsp;&nbsp;注:</th>
							<td class="pn-fcontent">
								<input type="text" size="24"   name="sysparamconf.remark" value="${sysparamconf.remark}" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" type="button" value="添加" onclick="add();"/>&nbsp;
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
							<th>编&nbsp;&nbsp;号</th>
							<th>参数名</th>
							<th>参数值</th>
							<th>类&nbsp;&nbsp;型</th>
							<th>操&nbsp;&nbsp;作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td>${item.pname}</td>
								<td>${item.pvalue}</td>
								<td>
									<c:if test="${item.ptype==0}">业务参数</c:if>
									<c:if test="${item.ptype==1}">其他</c:if>
								</td>
								<td>
									<a href="javascript:edit(${item.id},${pager.pageSize},${pager.currentPage})" class="btn btn-info"><i class="icon-pencil"></i>&nbsp;&nbsp;修改</a>
									<a href="javascript:del(${item.id},${pager.pageSize},${pager.currentPage});" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;&nbsp;删除</a>
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