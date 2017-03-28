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
	<form id="myForm" name="myForm" method="post" action="sysparamconf_update.do">
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
								<input type="text" readonly="readonly" required  name="sysparamconf.pname" value="${sysparamconf.pname}" size="24" />
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>参数值</th>
							<td class="pn-fcontent">
								<input type="text" required maxlength="100" name="sysparamconf.pvalue" value="${sysparamconf.pvalue}" size="24"/>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>类&nbsp;&nbsp;型</th>
							<td class="pn-fcontent">
								<c:if test="${sysparamconf.ptype==null}">
									<select required name="sysparamconf.ptype" style="display;  width: 178px;height: 24px;">			       
									    <option value="" selected="selected">- 请选择 -</option>		
						                <option value="0">业务参数</option>			       			               
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>		          
					                </select>
				                </c:if>
							    <c:if test="${sysparamconf.ptype==0}">
								    <select required name="sysparamconf.ptype" style="display;  width: 178px;height: 24px;">		
									    <option value="" >- 请选择 -</option>			       
						                <option value="0" selected="selected">业务参数</option>			       			               
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>		          
					                </select>
				                </c:if>
								<c:if test="${sysparamconf.ptype==1}">
									<select required name="sysparamconf.ptype" style="display;  width: 178px;height: 24px;">		
									    <option value="" >- 请选择 -</option>			       
						                <option value="0" >业务参数</option>			       			               
						                <option value="1" selected="selected">其&nbsp;&nbsp;&nbsp;他</option>		          
					                </select>
				                </c:if>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>备&nbsp;&nbsp;注</th>
							<td class="pn-fcontent">
								<textarea maxlength="75" name="sysparamconf.remark" rows="5" cols="50">${sysparamconf.remark}</textarea>
							</td>
							<td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<input type="hidden" name="sysparamconf.id" value="${sysparamconf.id}" /> 
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