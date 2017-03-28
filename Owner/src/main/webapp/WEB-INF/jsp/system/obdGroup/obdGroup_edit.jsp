<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>修改分组信息</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript">	
	if("${message}"=="updateFail") {
		alert("更新失败,当前分组编号已存在！");
	}

	$().ready(function() {
		$("#myForm").validate({
		});
	});
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="obdGroup_update.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>编号</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="25" name="obdGroup.groupNum" value="${obdGroup.groupNum}" size="24" readonly="readonly"/>
							</td>
							<th>名称</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="obdGroup.groupName" value="${obdGroup.groupName}" size="24" required/>
							</td>
						</tr>
						<tr>
							<th>经度</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="obdGroup.longitude" value="${obdGroup.longitude}" size="24" required/>
							</td>
							<th>纬度</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="obdGroup.latitude" value="${obdGroup.latitude}" size="24" />
							</td>
						</tr>
						<tr>
							<th>半径</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="obdGroup.radius" value="${obdGroup.radius}" size="24" />
							</td>
							<th></th>
							<td class="pn-fcontent"></td>
						</tr>
						<tr>
							<th>备注</th>
							<td class="pn-fcontent">
								<textarea maxlength="75" name="obdGroup.remark" rows="5" cols="50">${obdGroup.remark}</textarea>
							</td>
							<th></th>
							<td class="pn-fcontent"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<input type="hidden" name="obdGroup.id" value="${obdGroup.id}" /> 
					<center>
						<input class="btn btn-primary pull-center" type="submit" value="保 存" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="返 回" onclick="javascript:history.go(-1);"/>
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>