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
		$("#type").val("");
		$("#startTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var type=$("#type").val().trim();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(obdSn=='' && startTime=='' && endTime =='' && type==''){
			alert("请输入查询参数.");
			return false;
		}
		
		if(startTime!='' && endTime!=''){
			var a = startTime.split(" ");     
			var b = a[0].split("-");     
			var c = a[1].split(":");     
			var oldTime = new Date(b[0], b[1]-1, b[2], c[0], c[1], c[2]);  

			var aa = endTime.split(" ");     
			var bb = aa[0].split("-");     
			var cc = aa[1].split(":");     
			var newTime = new Date(bb[0], bb[1]-1, bb[2], cc[0], cc[1], cc[2]);  
			if(newTime<=oldTime){
				alert("开始时间不能大于结束时间.");
				return false;
			}
		}
		
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="fenceHis_query.do" method="post" onsubmit="return formCheck();">
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
							<th>操作类型</th>
							<td class="pn-fcontent">
								<select name="type" id="type" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${type==1}">selected</c:if>>新增围栏1</option>
									<option value="2" <c:if test="${type==2}">selected</c:if>>取消围栏2</option>
									<option value="3" <c:if test="${type==3}">selected</c:if>>取消所有围栏3</option>
									<option value="4" <c:if test="${type==4}">selected</c:if>>暂停使用围栏4</option>
									<option value="5" <c:if test="${type==5}">selected</c:if>>恢复使用围栏5</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${startTime}" class="Wdate" readonly="readonly" name="startTime" id="startTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
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
				<h5>电子围栏历史信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x: scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>源序号</th>
							<th>设备号</th>
							<th>操作类型</th>
							<th>报警方式</th>
							<th>区域编号</th>
							<th>顶点</th>
							<th>定时规则</th>
							<th>星期几</th>
							<th>定时开始时间</th>
							<th>定时结束时间</th>
							<th>定时开始日期</th>
							<th>定时结束日期</th>
							<th>创建时间</th>
							<th>更新时间</th>
							<th>有效</th>
							<th>删除时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${fenceHiss}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.originId}</td>
								<td>${item.obdSn}</td>
								<td>${item.type}</td>
								<td>${item.alert}</td>
								<td>${item.areaNum}</td>
								<td>${item.points}</td>
								<td>${item.timerType}</td>
								<td>${item.dayWeek}</td>
								<td><fmt:formatDate value="${item.startTime}" pattern="HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.endTime}" pattern="HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.startDate}" pattern="yyyy-MM-dd"/></td>
								<td><fmt:formatDate value="${item.endDate}" pattern="yyyy-MM-dd"/></td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${item.valid}</td>
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