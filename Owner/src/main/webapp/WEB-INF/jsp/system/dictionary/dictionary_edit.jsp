<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>修改用户资料</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript">	
	$().ready(function() {
		$("#myForm").validate({
		});
	});
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="dictionary_update.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>参数名</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="25" name="dictionary.showValue" value="${dictionary.showValue}" size="24" required/>
							</td>
							<th>参数值</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="dictionary.trueValue" value="${dictionary.trueValue}" size="24" required/>
							</td>
						</tr>
						<tr>
							<th>编码</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="dictionary.code" value="${dictionary.code}" size="24" required/>
							</td>
							<th>类别编码</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="dictionary.type" value="${dictionary.type}" size="24" />
							</td>
						</tr>
						<tr>
							<th>备&nbsp;&nbsp;注</th>
							<td class="pn-fcontent">
								<textarea maxlength="75" name="dictionary.remark" rows="5" cols="50">${dictionary.remark}</textarea>
							</td>
							<td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<input type="hidden" name="dictionary.id" value="${dictionary.id}" /> 
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