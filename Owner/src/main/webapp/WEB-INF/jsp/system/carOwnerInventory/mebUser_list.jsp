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
		$("#obdSN").val("");
		$("#starTime").val("");
		$("#endTime").val("");
		
		$("#valid").val("");
		$("#mobileNumber").val("");
	}
	
	
	function formCheck(){
		var obdSN=$("#obdSN").val().trim();
		var mobileNumber=$("#mobileNumber").val().trim();
		var valid=$("#valid").val().trim();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		if(obdSN=='' && starTime=='' && endTime =='' && mobileNumber=='' && valid==''){
			alert("请输入查询参数.");
			return false;
		}
		
		if(starTime!='' && endTime!=''){
			var a = starTime.split(" ");     
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
	<form name="myForm" id="myForm" action="mebUser_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSN" name="obdSN" value="${obdSN}" />
							</td>
							<th>手机号码</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="mobileNumber" name="mobileNumber" value="${mobileNumber}" />
							</td>
						</tr>
						<tr>
							<th>激活</th>
							<td class="pn-fcontent">
								<select name="valid" id="valid" style="width: 100px;">
									<option value="">请选择</option>
									<option value="1" <c:if test="${valid=='1'}">selected</c:if>>绑定1</option>
									<option value="0" <c:if test="${valid=='0'}">selected</c:if>>解绑0</option>	
							  	</select>&nbsp;
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
		<div class="widget widget-table" style="overflow-x: scroll;">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>用户信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>手机号</th>
							<th>车辆编号</th>
							<th>车牌号</th>
							<th>真实姓名</th>
							<th>用户类型</th>
							<th>激活状态</th>
							<th>创建时间</th>
							<th>解绑时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${mebUsers}" var="item" varStatus="status">
							<tr>
								<td>${item.regUserId}</td>
								<td>${item.obdSN}</td>
								<td>${item.mobileNumber}</td>
								<td>${item.carId}</td>
								<td>${item.license}</td>
								<td>${item.name}</td>
								<td>${item.userType}</td>
								<td>${item.valid}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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