<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>修改分组信息</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript">	
	
	$().ready(function() {
		$("#myForm").validate({
		});
	});
</script>
</head>
<body>
	<form id="myForm" name="myForm" action="obdGroupSet_groupEdit.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;&gt;&nbsp;obd分组设置</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd修改分组</h5>
			</div>
			<div>
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<tr>
						<th>设备号：</th>
						<td class="pn-fcontent">
							<input type="text" size="24"  id="obdSn" name="obdStockInfo.obdSn" value="${obdStockInfo.obdSn}" readonly="readonly"/>
						</td>
						<th>分组：</th>
						<td class="pn-fcontent">
							<select name="obdGroup.groupNum" id="groupNum" style="width: 100px;">
								<c:forEach items="${obdGroups}" var="item" varStatus="status">
									<option value="${item.groupNum}" <c:if test="${obdStockInfo.groupNum==item.groupNum}">selected</c:if>>'${item.groupName}'</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
			</div>
			<div class="widget-bottom">
				<center>
					<input class="btn btn-s-md btn-success" type="submit"  onclick="return check()" value="确定" />&nbsp;
					<input class="btn btn-danger btn-s-md" type="button" onclick="doGoBack()" value="返回"/>
				</center>
			</div>
		</div>
	</form>
</body>
</html>