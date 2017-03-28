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
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_clearChannel.do">
		<input type="submit" value="clearChannel"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_wifiOn.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="wifi打开"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_wifiOff.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="wifi关闭"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_fortification.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="设防"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_disarm.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="撤防"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_gpsOn.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="gsp打开"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_gpsOff.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="gps关闭"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_gpsDataFormatPosition.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="gsp数据格式-只传定位"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_gpsDataFormatAll.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="gps数据格式-全部"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_offlineOn.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="离线心跳打开"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_offlineOff.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="离线心跳关闭"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_shockOn.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="车辆震动(拖车)报警打开"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_shockOff.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="车辆震动(拖车)报警关闭"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_superWarnOn.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="超速报警打开"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_superWarnOff.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="超速报警关闭"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_clearFaultCode.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="清除故障码"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_resetObd.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="恢复出厂设置"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_restartObd.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="设备重新启动"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_readFaultCodes.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="故障码-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_offlinePositionSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="离线位置数据-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_offlineTravelSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="离线行程单-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_carRunStatusSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="车辆运行状态-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_deviceStatusSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="设备状态-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_carStatusSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="车辆状态-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_warnSetting.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="报警设置"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_warnSettingSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="报警设置-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_deviceTime.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="设备时间参数设置"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_deviceTimeSerach.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="设备时间参数设置-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_travelParameters.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="行程参数设置"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_travelParametersSerach.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="行程参数设置-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_totalSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="总里程-查询"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_deviceVersion.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="设备版本信息-查询"/>
	</form>
	<form name="ser" id="serv" method="post" action="${basePath}/admin/newFaultCodeRead_serviceParamSetting.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="text" value="01" name="setType"/>
		<input type="text" value="01" name="serverType"/>
		<input type="text" value="7089" name="port"/>
		<input type="text" value="14.29.5.42" name="ip"/>
		<input type="text" value="CMNET" name="APN"/>
		<input type="submit" value="配置服务器信息"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_dataServiceParamSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="配置服务器信息-查询"/>
	</form>
	<form name="ser" id="serv" method="post" action="${basePath}/admin/newFaultCodeRead_serviceParamUpdate.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="text" value="01" name="setType"/>
		<input type="text" value="01" name="serverType"/>
		<input type="text" value="7089" name="port"/>
		<input type="text" value="14.29.5.42" name="ip"/>
		<input type="text" value="CMNET" name="APN"/>
		<input type="submit" value="升级服务器参数"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_updateServiceParamSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="升级服务器参数-查询"/>
	</form>
	<form name="ser" id="serv" method="post" action="${basePath}/admin/newFaultCodeRead_protalServiceParam.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="text" value="01" name="setType"/>
		<input type="text" value="01" name="serverType"/>
		<input type="text" value="7089" name="port"/>
		<input type="text" value="14.29.5.42" name="ip"/>
		<input type="text" value="CMNET" name="APN"/>
		<input type="submit" value="Protal服务器参数"/>
	</form>
	<form name="WIFI" id="wifi" method="post" action="${basePath}/admin/newFaultCodeRead_protalServiceParamSearch.do">
		<input type="text" value="88888888" name="obdId"/>
		<input type="submit" value="Protal服务器参数-查询"/>
	</form>
</body>
</html>