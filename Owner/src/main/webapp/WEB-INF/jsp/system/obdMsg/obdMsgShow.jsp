<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>obd报文解析内容</title>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
function reback(){	
	window.parent.hideIframe();//隐藏
}
</script>
</head>
<body>
	<form id="mebUserForm">
		<div class="widget-bottom">
			<input type="text" name="obdPacket.msg" value="${obdPacket.msg}" />  
			<center>
				<input class="btn btn-primary pull-center" type="button" value="返回" onclick="reback()"/>&nbsp;
			</center>
		</div>
	</form>
</body>
</html>