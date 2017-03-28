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
			window.location.href = "dictionary_delete.do?dictionary.id="+id;
		}
	}
	
	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
		}
	function edit(id,pageSize,currentPage){
		window.location.href ="dictionary_edit.do?dictionary.id="+id+"&pager.pageSize="+pageSize+"&pager.currentPage="+currentPage+"&pager.rowIndex="+get("pager.rowIndex").value;
	}
	
	function myFormReset(){
		$("#showValue").val("");
		$("#trueValue").val("");
		$("#code").val("");
		$("#type").val("");
		$("#remark").val("");
	}
	
	function add(){
		window.location.href = "dictionary_add.do";
	}	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="dictionary_query.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>参数名</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="showValue" name="dictionary.showValue" value="${dictionary.showValue}" />
							</td>
							<th>参数值</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="trueValue" name="dictionary.trueValue" value="${dictionary.trueValue}" />
							</td>
						</tr>
						<tr>
							<th>编码</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="code" name="dictionary.code" value="${dictionary.code}" />
							</td>
							<th>类别编码</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="type"  name="dictionary.type" value="${dictionary.type}" />
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
						<tr>
							<th>备&nbsp;&nbsp;注</th>
							<td class="pn-fcontent" colspan="1">
								<input type="text" size="24" id="remark"  name="dictionary.remark" value="${dictionary.remark}" />
							</td>
							<th></th>
							<td class="pn-fcontent">
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
				<h5>参数列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>编&nbsp;&nbsp;号</th>
							<th>参数名</th>
							<th>参数值</th>
							<th>编码</th>
							<th>类别编码</th>
							<th>备注</th>
							<th>创建时间</th>
							<th>操&nbsp;&nbsp;作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${dictionarys}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td>${item.showValue}</td>
								<td>${item.trueValue}</td>
								<td>${item.code}</td>
								<td>${item.type}</td>
								<td>${item.remark}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<a href="javascript:edit('${item.id}',${pager.pageSize},${pager.currentPage});" class="btn btn-info"><i class="icon-pencil"></i>&nbsp;&nbsp;修改</a>
									<a href="javascript:del('${item.id}',${pager.pageSize},${pager.currentPage});" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;&nbsp;删除</a>
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