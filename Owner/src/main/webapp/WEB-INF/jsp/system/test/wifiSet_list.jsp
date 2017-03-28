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
		$("#valid").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var valid=$("#valid").val().trim();
		var type=$("#type").val().trim();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		if(obdSn=='' && valid=='' && type=='' && starTime=='' && endTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="wifiSet_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="wifiSet.obdSn" value="${wifiSet.obdSn}" />
							</td>
							<th>设置类型</th>
							<td class="pn-fcontent">
								<select name="wifiSet.type" id="type" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${wifiSet.type=='1'}">selected</c:if>>修改SSID-1</option>
									<option value="2" <c:if test="${wifiSet.type=='2'}">selected</c:if>>修改WIFI密码-2</option>
									<option value="3" <c:if test="${wifiSet.type=='3'}">selected</c:if>>WIFI出厂设置-3</option>
									<option value="4" <c:if test="${wifiSet.type=='4'}">selected</c:if>>wifi开关设置-4</option>
									<option value="5" <c:if test="${wifiSet.type=='5'}">selected</c:if>>wifi使用时间-5</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<th>有效</th>
							<td class="pn-fcontent">
								<select name="wifiSet.valid" id="valid" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${wifiSet.valid=='1'}">selected</c:if>>有效1</option>
									<option value="0" <c:if test="${wifiSet.valid=='0'}">selected</c:if>>无效0</option>
								</select>
							</td>
							<th></th>
							<td class="pn-fcontent"></td>
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
				<h5>WiFi设置信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>设置类型</th>
							<th>wifi名称ssid</th>
							<th>WIFI共享方式</th>
							<th>Wifi密码加密方式</th>
							<th>wifi密码</th>
							<th>wifi开关状态</th>
							<th>使用时间</th>
							<th>创建时间</th>
							<th>更新时间</th>
							<th>有效</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${wifiSets}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.type=='1'}">修改SSID</c:when>
										<c:when test="${item.type=='2'}">修改WIFI密码</c:when>
										<c:when test="${item.type=='3'}">WIFI出厂</c:when>
										<c:when test="${item.type=='4'}">wifi开关</c:when>
										<c:when test="${item.type=='5'}">wifi使用时间</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								<td>${item.ssid}</td>
								<td>${item.autu}</td>
								<td>${item.encrypt}</td>
								<td>${item.wifiPwd}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.wifiState=='0'}">关闭</c:when>
										<c:when test="${item.wifiState=='1'}">打开</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								<td>${item.useTime}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<c:choose>
										<c:when test="${item.valid=='0'}">成功或被覆盖</c:when>
										<c:when test="${item.valid=='1'}">未成功</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
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