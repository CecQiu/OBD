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
		if(obdSn=='' && type=='' && valid=='' && starTime=='' && endTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="portal_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="portal.obdSn" value="${portal.obdSn}" />
							</td>
							<th>有效</th>
							<td class="pn-fcontent">
								<select name="portal.valid" id="valid" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${portal.valid=='1'}">selected</c:if>>有效1</option>
									<option value="0" <c:if test="${portal.valid=='0'}">selected</c:if>>无效0</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<th>操作类型</th>
							<td class="pn-fcontent">
								<select name="portal.type" id="type" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${portal.type=='0'}">selected</c:if>>设置URL-0</option>
									<option value="1" <c:if test="${portal.type=='1'}">selected</c:if>>保留-1</option>
									<option value="2" <c:if test="${portal.type=='2'}">selected</c:if>>流量额度限制-2</option>
									<option value="3" <c:if test="${portal.type=='3'}">selected</c:if>>白名单设置-3</option>
									<option value="4" <c:if test="${portal.type=='4'}">selected</c:if>>全部删除白名单-4</option>
									<option value="5" <c:if test="${portal.type=='5'}">selected</c:if>>单条删除白名单-5</option>
									<option value="6" <c:if test="${portal.type=='6'}">selected</c:if>>portal开关-6</option>
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
				<h5>portal设置信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x:scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>操作类型</th>
							<th>设置url</th>
							<th>流量额度限制</th>
							<th>白名单限制</th>
							<th>手机mac地址</th>
							<th>PROTAL开关</th>
							<th>创建时间</th>
							<th>更新时间</th>
							<th>有效</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${portals}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.type=='0'}">设置URL</c:when>
										<c:when test="${item.type=='1'}">保留</c:when>
										<c:when test="${item.type=='2'}">流量额度限制</c:when>
										<c:when test="${item.type=='3'}">白名单设置</c:when>
										<c:when test="${item.type=='4'}">全部删除白名单</c:when>
										<c:when test="${item.type=='5'}">单条删除白名单</c:when>
										<c:when test="${item.type=='6'}">portal开关</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								<td>${item.url}</td>
								<td>${item.mb}</td>
								<td>${item.whitelists}</td>
								<td>${item.mac}</td>
								<td>
									<c:choose>
										<c:when test="${item.onOff=='0'}">关闭</c:when>
										<c:when test="${item.onOff=='1'}">打开</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
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