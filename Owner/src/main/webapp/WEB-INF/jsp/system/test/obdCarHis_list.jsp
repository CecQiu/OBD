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
	function myFormReset(){
		$("#obdSn").val("");
		$("#obdMSn").val("");
		$("#starTime").val("");
		$("#endTime").val("");
		$("#delStartTime").val("");
		$("#delEndTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var obdMSn=$("#obdMSn").val().trim();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		var delStartTime=$("#delStartTime").val();
		var delEndTime=$("#delEndTime").val();
		if(obdSn=='' && obdMSn=='' && starTime=='' && endTime =='' && delStartTime=='' && delEndTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdCarHis_query.do" method="post" onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdSn" value="${obdSn}" />
							</td>
							<th>表面号</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="obdMSn" name="obdMSn" value="${obdMSn}" />
							</td>
						</tr>
						
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${startTime}" class="Wdate" readonly="readonly" name="startTime" id="starTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
							</td>
							<th>结束时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
						</tr>
						<tr>
							<th>删除开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${delStartTime}" class="Wdate" readonly="readonly" name="delStartTime" id="delStartTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
							</td>
							<th>删除结束时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${delEndTime}" class="Wdate" readonly="readonly" name="delEndTime" id="delEndTime"  onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
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
				<h5>OBD服务器参数表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x: scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>原序号</th>
							<th>设备号</th>
							<th>车辆型号</th>
							<th>操作类型</th>
							<th>创建时间</th>
							<th>删除时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdCarHisList}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.originId}</td>
								<td>${item.obdSn}</td>
								<td>${item.typeId}</td>
								<td>${item.operType}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.delTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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