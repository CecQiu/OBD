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
	
	if("${message}"=="updateSuccess") {
		alert("更新成功！");
	}
	
	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
		}
	function edit(id,pageSize,currentPage){
		window.location.href ="obdGroupSet_edit.do?obdStockInfo.stockId="+id+"&pager.pageSize="+pageSize+"&pager.currentPage="+currentPage+"&pager.rowIndex="+get("pager.rowIndex").value;
	}
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#obdMSn").val("");
		$("#obdId").val("");
		$("#groupNum").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	function toOdbGroupSet(){
		window.location.href = "obdGroupSet_listObdSet.do";
	}	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="carPosition_query.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdStockInfo.obdSn" value="${obdStockInfo.obdSn}" />
							</td>
							<th>表面号:</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="obdMSn" name="obdStockInfo.obdMSn" value="${obdStockInfo.obdMSn}" />
							</td>
						</tr>
						<tr>
							<th>二维码:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdId" name="obdStockInfo.obdId" value="${obdStockInfo.obdId}" />
							</td>
							<th>分组编号：</th>
							<td class="pn-fcontent">
								<select name="obdStockInfo.groupNum" id="groupNum">
									<option value="">全部</option>
									<c:forEach items="${obdGroups}" var="item" varStatus="status">
										<option value="${item.groupNum}" <c:if test="${item.groupNum==obdStockInfo.groupNum}">selected</c:if>>'${item.groupName}'</option>
									</c:forEach>
								</select>
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
							<th>编&nbsp;&nbsp;号</th>
							<th>设备号</th>
							<th>表面号</th>
							<th>二维码</th>
							<th>分组编号</th>
							<th>创建时间</th>
							<th>操&nbsp;&nbsp;作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdStockInfos}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.stockId}</td>
								<td>${item.obdSn}</td>
								<td>${item.obdMSn}</td>
								<td>${item.obdId}</td>
								<td>${item.groupNum}</td>
								<td>${item.startDate}</td>
								<td>
									<a class="btn btn-info" href="${basePath}/admin/carPosition_list.do?obdSn=${item.obdSn}"> 定 位 </a>
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