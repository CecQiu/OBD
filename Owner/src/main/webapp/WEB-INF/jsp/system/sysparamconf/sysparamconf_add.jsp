<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>add</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript" src="${basePath}/js/jsonrpc.js"></script>
<script type="text/javascript">	
	if("${message}"=="hasPname"){
		alert("参数名已存在！");
	}
	
	$().ready(function() {
		$("#myForm").validate({
		});
	});
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="sysparamconf_save.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;添加其他参数配置</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>参数名</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="25" name="sysparamconf.pname" value="${sysparamconf.pname}" size="24" required/>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<!-- 如果选择的是-1(系统参数)，则是添加组别参数，隐藏该组别选项-->
					   		<!-- 如果选择的不是-1(系统参数)，则需要选择组别参数--> 
							<th>参数值</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" name="sysparamconf.pvalue" value="${sysparamconf.pvalue}" size="24" required/>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>类&nbsp;&nbsp;型</th>
							<td class="pn-fcontent">
								<c:if test="${sysparamconf.ptype==null}">
									<select required name="sysparamconf.ptype" style="display;  width: 178px;height: 24px;">
						                <option value="">- 请选择 -</option>
						                <option value="0">业务参数</option>
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>			          
					                </select>
								</c:if>
								<c:if test="${sysparamconf.ptype==0}">
									<select required name="sysparamconf.ptype" style="display;  width: 178px;height: 24px;">			       
						                <option value="0">业务参数</option>			       
						                <option value="">- 请选择 -</option>	
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>		          
					                </select>
					         	</c:if>
								<c:if test="${sysparamconf.ptype==1}">
									<select required name="sysparamconf.ptype" style="display;  width: 178px;height: 24px;">			                			               
						                <option value="1">其&nbsp;&nbsp;&nbsp;他</option>
						                <option value="">- 请选择 -</option>	
						                 <option value="0">业务参数</option>		          
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