<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript"></script>
</head>
<body>
	<form name="myForm" id="myForm" method="post" action="${basePath}/admin/faultCodeRead_readMsg.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="读取故障码"/>
	</form>
	<form name="ff" id="ff" method="post" action="${basePath}/admin/faultCodeRead_respMsg.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="布防撤防"/>
	</form>
	<form name="yy" id="kk" method="post" action="${basePath}/admin/faultCodeRead_paramSet.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="参数设置"/>
	</form>
	<form name="yy" id="aa" method="post" action="${basePath}/admin/faultCodeRead_obdPosition.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="点名"/>
	</form>
	<form name="ss" id="dd" method="post" action="${basePath}/admin/faultCodeRead_monitor.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="监听"/>
	</form>
	<form name="dd" id="ee" method="post" action="${basePath}/admin/faultCodeRead_faultCodeClear.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="清除故障码"/>
	</form>
	<form name="as" id="ad" method="post" action="${basePath}/admin/faultCodeRead_msgReq.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="请求批量信息"/>
	</form>
	<form name="ae" id="de" method="post" action="${basePath}/admin/faultCodeRead_restart.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="重启终端"/>
	</form>
	<form name="jj" id="kk" method="post" action="${basePath}/admin/faultCodeRead_OBDStudy.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="OBD安装位置学习"/>
	</form>
	<form name="sd" id="sf" method="post" action="${basePath}/admin/faultCodeRead_blindPointClear.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="清除盲点"/>
	</form>
	<form name="fd" id="fs" method="post" action="${basePath}/admin/faultCodeRead_OBDRecovery.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="参数恢复出厂设置"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/faultCodeRead_WIFI1.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="wifi打开"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/faultCodeRead_WIFI2.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="wifi关闭"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/faultCodeRead_WIFI3.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="wifi_ssid"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/faultCodeRead_WIFI4.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="wifi_密码"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/faultCodeRead_WIFI5.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="wifi_恢复出厂设置"/>
	</form>
	
	
	<form name="param" id="param" method="post" action="${basePath}/admin/faultCodeRead_param.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="参数设置"/>
	</form>
	<form name="param" id="param" method="post" action="${basePath}/admin/faultCodeRead_paramQuery.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="参数查询"/>
	</form>
	<form name="param" id="obdupdate" method="post" action="${basePath}/admin/faultCodeRead_obdUpdate.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="obd升级"/>
	</form>
	<form name="param" id="remoteUpgrade" method="post" action="${basePath}/admin/faultCodeRead_remoteUpgrade.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="远程升级申请"/>
	</form>
	<form name="param" id="portal" method="post" action="${basePath}/admin/faultCodeRead_portal0.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="portal设置url"/>
	</form>
	<form name="param" id="portal" method="post" action="${basePath}/admin/faultCodeRead_portal2.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="portal流量额度限制"/>
	</form>
	<form name="param" id="portal" method="post" action="${basePath}/admin/faultCodeRead_portal3.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="portal白名单设置"/>
	</form>
	<form name="param" id="portal" method="post" action="${basePath}/admin/faultCodeRead_portal4.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="portal全部删除白名单"/>
	</form>
	<form name="param" id="portal" method="post" action="${basePath}/admin/faultCodeRead_portal5.do">
		<input type="text" value="888888888888888888888888" name="obdId"/>
		<input type="submit" value="portal单跳删除白名单"/>
	</form>
	<form name="param" id="portal" method="post" action="${basePath}/admin/faultCodeRead_portal6.do">
		<input type="text" value="00000000000000002f10002a" name="obdId"/>
		<input type="text" value="1" name="onOff"/>
		<input type="submit" value="portal开关"/>
	</form>
	<br/>
	<br/>
	<form name="ser" id="serv" method="post" action="${basePath}/admin/faultCodeRead_server.do">
		<input type="text" value="111111111111111111111111" name="obdId"/>
		<input type="text" value="01" name="setType"/>
		<input type="text" value="01" name="serverType"/>
		<input type="text" value="7089" name="port"/>
		<input type="text" value="14.29.5.42" name="ip"/>
		<input type="text" value="CMNET" name="APN"/>
		<input type="submit" value="配置服务器信息"/>
	</form>
	<form name="ser" id="serv" method="post" action="${basePath}/admin/faultCodeRead_server1.do">
		<input type="text" value="111111111111111111111111" name="obdId"/>
		<input type="text" value="01" name="setType"/>
		<input type="text" value="01" name="serverType"/>
		<input type="text" value="7089" name="port"/>
		<input type="text" value="14.29.5.42" name="ip"/>
		<input type="text" value="CMNET" name="APN"/>
		<input type="submit" value="单台设备配置服务器信息"/>
	</form>
</body>
</html>