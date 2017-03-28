<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测试！下发设置操作</title>
    <jsp:include page="../../include/common.jsp" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		function urlSet(id){
			var obdSN=$("#obdSn").val().trim();
			//alert(obdSN);
			if(obdSN == "" || obdSN == undefined || obdSN == null){
				alert("设备号不能为空");
				return false;
			}
			if(obdSN.length!=2*4){
				alert("设备号输入有误,obd设备号为4个byte.");
				return false;
			}
			//alert(id);
			var hrefStr=$('#'+id).attr("href");
			var obdSN=$("#obdSn").val();
			var hrefString = hrefStr + "&obdSn="+obdSN;
			if(id == 'upgrade'){
				hrefString += '&msg='+$('#msg').val()+'&upgradeType='+$('input:radio[name=upgradeType]:checked').val();
 			}
			$('#'+id).attr("href",hrefString)
			alert($('#'+id).attr("href"));
			return true;
		}
		function testDriveSetCheck(){
			var flag = true;
			if($("#tds_obdSn").val()==''){
				alert("设备号不能为空.");
				flag = false;
			};
			var quickenSpeed = $("#tds_quickenSpeed").val();
			var quickSlowDownSpeed = $("#tds_quickSlowDownSpeed").val();
			var quickturnSpeed = $("#tds_quickturnSpeed").val();
			var quickturnAngle = $("#tds_quickturnAngle").val();
			if((quickturnSpeed=='' && quickturnAngle!='') || (quickturnSpeed!='' && quickturnAngle=='')){
				alert("同一驾驶行为的两个参数需同时设置.");
				flag = false;
			}
			var overspeed = $("#tds_overspeed").val();
			var overspeedTime = $("#tds_overspeedTime").val();
			if((overspeed=='' && overspeedTime!='') || (overspeed!='' && overspeedTime=='')){
				alert("同一驾驶行为的两个参数需同时设置.");
				flag = false;
			}
			var fatigueDrive = $("#tds_fatigueDrive").val();
			var fatigueSleep = $("#tds_fatigueSleep").val();
			if((fatigueDrive=='' && fatigueSleep!='') || (fatigueDrive!='' && fatigueSleep=='')){
				alert("同一驾驶行为的两个参数需同时设置.");
				flag = false;
			}
			if(quickenSpeed=='' && quickSlowDownSpeed==''
					&& quickturnSpeed=='' && quickturnAngle=='' 
					&& overspeed=='' && overspeedTime==''
					&& fatigueDrive=='' && fatigueSleep==''){
				alert("参数不能为空.");
				flag = false;
			}
			return flag;
		}
	</script>
  </head>
  
  <body>
    <table border="1px" style="text-align: center;margin: auto;">
    	<tr>
    		<td>obd设备号:</td>
    		<td>
    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }">
    	    </td>
    	</tr>
    	<tr>
    		<td>1、测试GPS设置</td>
    		<td>
    			<a id='gps_open' href="${pageContext.request.contextPath}/testOBD/testGPS?state=0&typeName=测试GPS设置" onClick='return urlSet("gps_open");'>测试(开)</a>
    			<a id='gps_close' href="${pageContext.request.contextPath}/testOBD/testGPS?state=1&typeName=测试GPS设置" onClick='return urlSet("gps_close");'>测试(关)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>2、测试WIFI设置</td>
    		<td>
    			<a id='wifi_open' href="${pageContext.request.contextPath}/testOBD/testWiFi?state=0&typeName=测试WIFI设置" onClick='return urlSet("wifi_open");'>测试(开)</a>
    			<a id='wifi_close' href="${pageContext.request.contextPath}/testOBD/testWiFi?state=1&typeName=测试WIFI设置" onClick='return urlSet("wifi_close");'>测试(关)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>3、测试撤防设防设置</td>
    		<td>
    			<a id='guard_open' href="${pageContext.request.contextPath}/testOBD/testGuard?state=0&typeName=测试撤防设防设置" onClick='return urlSet("guard_open");'>测试(设防)</a>
    			<a id='guard_close' href="${pageContext.request.contextPath}/testOBD/testGuard?state=1&typeName=测试撤防设防设置" onClick='return urlSet("guard_close");'>测试(撤防)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>4、测试震动报警</td>
    		<td>
				<a id='shock_open' href="${pageContext.request.contextPath}/testOBD/testShock?state=0&typeName=测试震动报警" onClick='return urlSet("shock_open");'>测试(开)</a> 
				<a id='shock_close' href="${pageContext.request.contextPath}/testOBD/testShock?state=1&typeName=测试震动报警" onClick='return urlSet("shock_close");'>测试(关)</a>    	    
			</td>
    	</tr>
    	<tr>
    		<td>5、测试清除故障码</td>
    		<td>
    			<a id='clearCode' href="${pageContext.request.contextPath}/testOBD/testClearFaultCode?state=1&typeName=测试清除故障码" onClick='return urlSet("clearCode");'>测试清除故障码</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>6、测试GPS数据格式</td>
    		<td>
    			<a id='gpsFormat_all' href="${pageContext.request.contextPath}/testOBD/testGPSFormat?state=1&typeName=测试GPS数据格式" onClick='return urlSet("gpsFormat_all");'>测试(全部)</a>
    			<a id='gpsFormat' href="${pageContext.request.contextPath}/testOBD/testGPSFormat?state=0&typeName=测试GPS数据格式" onClick='return urlSet("gpsFormat");'>测试(只传定位数据)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>7、测试离线心跳设置</td>
    		<td>
    			<a id='heart_close' href="${pageContext.request.contextPath}/testOBD/testOffHeart?state=0&typeName=测试离线心跳设置" onClick='return urlSet("heart_close");'>测试(无)</a>
    			<a id='heart_open' href="${pageContext.request.contextPath}/testOBD/testOffHeart?state=1&typeName=测试离线心跳设置" onClick='return urlSet("heart_open");'>测试(设置)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>8、测试超速报警</td>
    		<td>
    			<a id='overspeed_open' href="${pageContext.request.contextPath}/testOBD/testOverSpeedWarn?&state=0&typeName=测试超速报警" onClick='return urlSet("overspeed_open");'>测试开</a>
    			<a id='overspeed_close' href="${pageContext.request.contextPath}/testOBD/testOverSpeedWarn?state=1&typeName=测试超速报警" onClick='return urlSet("overspeed_close");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>9、测试设备重置</td>
    		<td>
    			<a id='recovery' href="${pageContext.request.contextPath}/testOBD/testReset?state=1&typeName=测试设备重置" onClick='return urlSet("recovery");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>10、测试设备重启</td>
    		<td>
    			<a id='restart' href="${pageContext.request.contextPath}/testOBD/testRestart?state=1&typeName=测试设备重启" onClick='return urlSet("restart");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>11、测试设备升级设置</td>
    		<td>
    			版本:<input type="text" size="4" maxlength="4" name="msg" id="msg">
    			APP:<input type="radio" name="upgradeType" checked="checked" value="app">
    			IAP:<input type="radio" name="upgradeType" value="iap">
    			<a id='upgrade' href="${pageContext.request.contextPath}/testOBD/testDeviceUpgradeSet?state=1&typeName=测试设备升级设置" onClick='return urlSet("upgrade");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>12、清除流量计数值</td>
    		<td>
    			<a id='cleanFlow' href="${pageContext.request.contextPath}/testOBD/testCleanFlowStat?state=1&typeName=清除流量计数值设置" onClick='return urlSet("cleanFlow");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>13、关闭设备</td>
    		<td>
    			<a id='closeDevice' href="${pageContext.request.contextPath}/testOBD/testCloseDevice?state=1&typeName=关闭设备" onClick='return urlSet("closeDevice");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>14、FOTA升级设置</td>
    		<td>
    			<a id='FOTA' href="${pageContext.request.contextPath}/testOBD/testFOTAUpgradeSet?state=1&typeName=FOTA升级设置" onClick='return urlSet("FOTA");'>测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>15、wifi流量查询开关设置</td>
    		<td>
    			<a id='wifiFlowSet_open' href="${pageContext.request.contextPath}/testOBD/testWiFiFlowSet?state=1&typeName=测试wifi流量查询开关设置" onClick='return urlSet("wifiFlowSet_open");'>测试(开)</a>
    			<a id='wifiFlowSet_close' href="${pageContext.request.contextPath}/testOBD/testWiFiFlowSet?state=0&typeName=测试wifi流量查询开关设置" onClick='return urlSet("wifiFlowSet_close");'>测试(关)</a>
    	    </td>
    	</tr>
    </table>
    
    <br/>
    <form name="testExtension2Set" id="testExtension2Set" method="post" action="${basePath}/admin/obdTest_testExtension2Set.do">
    	 <h4>扩展数据参数设置：</h4>
    	 <input type="hidden" name="typeName" value="休眠电压+加速度"> 
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    		<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    		<td>休眠电压:</td>
	    		<td>
	    			<input type="text" name="sleepVolt" id="sleepVolt"  value="0000" readonly="readonly"/>
	    	    </td>
	    	    <td>电压差值:</td>
	    		<td>
	    			<input type="text" name="sleepVoltValue" id="sleepVoltValue"  value="01"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>休眠加速度:</td>
	    		<td>
	    			<input type="text" name="sleepOverSpeed" id="sleepOverSpeed" value="0001" readonly="readonly"/>
	    	    </td>
	    	    <td>加速度差值:</td>
	    		<td>
	    			<input type="text" name="sleepOverSpeedValue" id="sleepOverSpeedValue" value="0001"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
    
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="域白名单开关"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    		<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>域白名单开关:</td>
	    		<td>
	    			<input type="text" value="0002" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="radio" name="map['0002']" checked="checked" value="1"/>开
	    			<input type="radio" name="map['0002']" value="0"/>关
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="域黑名单开关"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>域黑名单开关:</td>
	    		<td>
	    			<input type="text" value="0003" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="radio" name="map['0003']" checked="checked" value="1"/>开
	    			<input type="radio" name="map['0003']" value="0"/>关
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	 	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="禁止的MAC"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>禁止的MAC:</td>
	    		<td>
	    			<input type="text" value="0004" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['0004']" value="ff:ff:ff:ff:ff:ff"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	 	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
        <input type="hidden" name="typeName" value="增加域白名单"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>增加域白名单的域名:</td>
	    		<td>
	    			<input type="text" value="0005" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['0005']"  value="www.baidu.com"/>
	    			(‘;’分割，不超过5个)
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	 	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
        <input type="hidden" name="typeName" value="删除域白名单"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>删除域白名单的域名:</td>
	    		<td>
	    			<input type="text" value="0006" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['0006']"  value="www.baidu.com"/>
	    			(00-删除全部)
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	 	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="增加域黑名单"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>增加域黑名单的域名:</td>
	    		<td>
	    			<input type="text" value="0007" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['0007']" value="www.baidu.com"/>
	    			(‘;’分割，不超过5个)
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	 	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="删除域黑名单"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>删除域黑名单的域名:</td>
	    		<td>
	    			<input type="text" value="0008" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['0008']"  value="www.baidu.com"/>
	    			(00-删除全部)
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
    	</table>
    </form>
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="车型功能设置"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>车型功能设置:</td>
	    		<td>
	    			<input type="text" value="0009" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['0009']"  value="0006"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
    	</table>
    </form>
    
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="低电压休眠阀值设置"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>低电压休眠阀值设置:</td>
	    		<td>
	    			<input type="text" value="000a" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['000a']"  value="01"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
    	</table>
    </form>
    
    <form name="testExtensionSet" id="testExtensionSet" method="post" action="${basePath}/admin/obdTest_testExtensionSet.do">
    	<input type="hidden" name="typeName" value="修改EEPROM/FLASH值"> 
    	<table border="1px" style="text-align: center;">
	    	<tr style="margin-top: 5px;display: block; ">
	    	<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td>修改EEPROM/FLASH值设置:</td>
	    		<td>
	    			<input type="text" value="000b" readonly="readonly" size="5"/>
	    	    </td>
	    		<td>
	    			<input type="text" name="map['000b']"  value=""/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
    	</table>
    </form>
    
    <br/>
    <br/>
    <form name="testDeviceTime" id="testDeviceTime" method="post" action="${basePath}/admin/obdTest_testDeviceTime.do">
    	 <input type="hidden" name="typeName" value="设备时间参数设置"> 
    	 <h4>设备时间参数：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    		<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>进入休眠模式时间(秒):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.sleepTime" id="sleepTime"  value="51"/>
	    	    </td>
	    	    <td>熄火后WIFI使用时间设置(分):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.wifiUseTime" id="wifiUseTime" value="1"/>
	    	    </td>
	    	    <td>GPS数据采集时间间隔(秒):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.gpsCollectDataTime" id="gpsCollectDataTime" value="4"/>
	    	    </td>
	    	    <td>位置数据上传时间间隔(秒):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.positionDataTime" id="positionDataTime"  value="27"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>OBD在线心跳包时间间隔(秒):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.obdOnlineTime" id="obdOnlineTime" value="26"/>
	    	    </td>
	    	    <td>OBD离线心跳包时间间隔(10分):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.obdOfflineTime" id="obdOfflineTime" value="17"/>
	    	    </td>
	    	    <td>OBD离线数据保存时间间隔(秒):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.obdOffDataTime" id="obdOffDataTime" value="59"/>
	    	    </td>
	    	    <td>请求AGPS数据包延时时间(10秒):</td>
	    		<td>
	    			<input type="text" name="obdTimeParams.requestAGPSTime" id="requestAGPSTime" value="180"/>
	    	    </td>
	    	    <td>对应位:</td>
	    		<td>
	    			<input type="text" name="positionChar" id="positionChar" value="00000000"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testDataServerParamsSet" id="testDataServerParamsSet" method="post" action="${basePath}/admin/obdTest_testDataServerParamsSet.do">
    	<input type="hidden" name="typeName" value="数据服务器参数设置"> 
    	 <h4>数据服务器参数：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>IP:</td>
	    		<td>
	    			<input type="text" name="ip" id="ip" value="221.4.53.120"/>
	    	    </td>
	    	    <td>port:</td>
	    		<td>
	    			<input type="text" name="port" id="port" value="6767"/>
	    	    </td>
	    	    <td>APN:</td>
	    		<td>
	    			<input type="text" name="APN" id="APN" value="obd"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testUpgradeServerParamsSet" id="testUpgradeServerParamsSet" method="post" action="${basePath}/admin/obdTest_testUpgradeServerParamsSet.do">
    	 <input type="hidden" name="typeName" value="升级服务器参数设置"> 
    	 <h4>升级服务器参数设置：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>IP:</td>
	    		<td>
	    			<input type="text" name="ip" id="ip" value="221.4.53.120"/>
	    	    </td>
	    	    <td>port:</td>
	    		<td>
	    			<input type="text" name="port" id="port" value="6767"/>
	    	    </td>
	    	    <td>APN:</td>
	    		<td>
	    			<input type="text" name="APN" id="APN" value="obd"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testPortalServerParamsSet" id="testPortalServerParamsSet" method="post" action="${basePath}/admin/obdTest_testPortalServerParamsSet.do">
    	 <input type="hidden" name="typeName" value="portal服务器参数设置">
    	 <h4>portal服务器参数设置：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>IP:</td>
	    		<td>
	    			<input type="text" name="ip" id="ip" value="221.4.53.120"/>
	    	    </td>
	    	    <td>port:</td>
	    		<td>
	    			<input type="text" name="port" id="port" value="6767"/>
	    	    </td>
	    	    <td>APN:</td>
	    		<td>
	    			<input type="text" name="APN" id="APN" value="obd"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testWarnSet" id="testWarnSet" method="post" action="${basePath}/admin/obdTest_testWarnSet.do">
    	 <input type="hidden" name="typeName" value="报警设置"> 
    	 <h4>报警设置：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>报警:</td>
	    		<td>
	    			<input type="text" name="warnSet" id="warnSet" value="10010010"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	
	 <br/>
    <br/>
    <form name="testTravelParamsSet" id="testTravelParamsSet" method="post" action="${basePath}/admin/obdTest_testTravelParamsSet.do">
    	 <input type="hidden" name="typeName" value="行程参数设置"> 
    	 <h4>行程参数设置：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    		<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>蓄电池电压低阈值(0.1V):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.batteryLow" id="batteryLow"  value="114"/>
	    	    </td>
	    	    <td>蓄电池电压高阈值（0.1V）:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.batteryHigh" id="batteryHigh" value="122"/>
	    	    </td>
	    	    <td>超速阈值：时速阈值(Km/h):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.overSpeed" id="gpsCollectDataTime" value="18"/>
	    	    </td>
	    	    <td>超速阈值：限速延迟时间阈值（S）:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.limitSpeedLazy" id="positionDataTime"  value="19"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td> 急转弯阈值：速度阈值(Km/h):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenTurnSpeed" id="shuddenTurnSpeed" value="20"/>
	    	    </td>
	    	    <td>急转弯阈值：角度阈值(度):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenTurnAngle" id="shuddenTurnAngle" value="21"/>
	    	    </td>
	    	    <td>急加速阈值：速度变化阈值(Km/h):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenOverSpeed" id="shuddenOverSpeed" value="22"/>
	    	    </td>
	    	    <td>急加速阈值：时间阈值（S）:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenOverSpeedTime" id="shuddenOverSpeedTime" value="23"/>
	    	    </td>
	    	    <td>急减速阈值：速度变化阈值(Km/h):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenLowSpeed" id="shuddenLowSpeed" value="24"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>急减速阈值：时间阈值（S）:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenLowSpeedTime" id="shuddenLowSpeedTime" value="25"/>
	    	    </td>
	    	    <td>急变道阈值：角度阈值 km/h:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenChangeAngle" id="shuddenChangeAngle" value="26"/>
	    	    </td>
	    	    <td> 急变道阈值：时间阈值:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenChangeTime" id="shuddenChangeTime" value="27"/>
	    	    </td>
	    	    <td>发动机水温报警阈值：低 摄氏度:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.engineLowTemperature" id="engineLowTemperature" value="28"/>
	    	    </td>
	    	    <td>发动机水温报警阈值：高 摄氏度:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.engineHighTemperature" id="engineHighTemperature" value="29"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>发动机转数报警阈值 转/分钟:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.engineTurnsWarn" id="engineTurnsWarn" value="6943"/>
	    	    </td>
	    	    <td>车速转速不匹配阈值：速度30km/h时匹配转速(转/分钟)</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.speedNotMatch" id="speedNotMatch" value="8226"/>
	    	    </td>
	    	    <td> 车速转速不匹配阈值：转速步进值(转/分钟):</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.speedNotMatchStep" id="speedNotMatchStep" value="4659"/>
	    	    </td>
	    	    <td>长怠速阈值：怠速车速阈值(km/h)</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.longLowSpeed" id="longLowSpeed" value="4"/>
	    	    </td>
	    	    <td>长怠速阈值：时间阈值 1分钟</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.longLowSpeedTime" id="longLowSpeedTime" value="1"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>急刹车强度阈值：</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shuddenBrakeStrength" id="shuddenBrakeStrength" value="2"/>
	    	    </td>
	    	    <td>侧翻角度阈值</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.sideTurnAngle" id="sideTurnAngle" value="19"/>
	    	    </td>
	    	    <td>碰撞强度阈值:</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.crashStrength" id="crashStrength" value="3"/>
	    	    </td>
	    	    <td>震动报警强度阈值</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.shockStrength" id="shockStrength" value="4"/>
	    	    </td>
	    	    <td>电子围栏驶出报警坐标：经度</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.fenceInLongtitude" id="fenceInLongtitude" value="136°04.000'"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>电子围栏驶出报警坐标：纬度</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.fenceInLatitude" id="fenceInLatitude" value="33°32.1000'"/>
	    	    </td>
	    	    <td>电子围栏驶入报警坐标：经度</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.fenceOutLongtitude" id="fenceOutLongtitude" value="136°14.101'"/>
	    	    </td>
	    	    <td>电子围栏驶入报警坐标：纬度</td>
	    		<td>
	    			<input type="text" name="obdTravelParams.fenceOutLatitude" id="fenceOutLatitude" value="34°33.0101'"/>
	    	    </td>
	    	    <td>对应位:</td>
	    		<td>
	    			<input type="text" name="positionChar" id="positionChar" value="0000000000000000"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
    
    <br/>
    <br/>
    <form name="testPortalORWifiSet0" id="testPortalORWifiSet0" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet0.do">
    	 <input type="hidden" name="typeName" value="设置url"> 
    	 <h4>设置url</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>url:</td>
	    		<td>
	    			<input type="text" name="url" id="url" value="http://obd.gd118114.cn/html/portal/auth.jsp?"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testPortalORWifiSet1" id="testPortalORWifiSet1" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet1.do">
    	<input type="hidden" name="typeName" value="设置ID"> 
    	 <h4>设置ID</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>id:</td>
	    		<td>
	    			<input type="text" name="id" id="id" value="2f000011"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
    
    <br/>
    <br/>
    <form name="testPortalORWifiSet2" id="testPortalORWifiSet2" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet2.do">
    	 <input type="hidden" name="typeName" value="流量限制Mac"> 
    	 <h4>流量限制Mac</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>mac:</td>
	    		<td>
	    			<input type="text" name="mac" id="mac" value="ff:fa:fb:fc:fd:fe"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testPortalORWifiSet3" id="testPortalORWifiSet3" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet3.do">
    	 <input type="hidden" name="typeName" value="设置白名单"> 
    	 <h4>设置白名单</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>macs:</td>
	    		<td>
	    			<input type="text" name="macs" id="macs" value="ff:fa:fb:fc:fd:fe,11:22:33:44:55:66"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
    
    <br/>
    <br/>
    <form name="testPortalORWifiSet4" id="testPortalORWifiSet4" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet4.do">
    	 <input type="hidden" name="typeName" value="删除全部白名单"> 
    	 <h4>删除全部白名单</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
    
     <br/>
    <br/>
    <form name="testPortalORWifiSet4_1" id="testPortalORWifiSet4_1" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet4_1.do">
    	 <input type="hidden" name="typeName" value="删除白名单-单条"> 
    	 <h4>删除白名单-单条</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>mac:</td>
	    		<td>
	    			<input type="text" name="mac" id="mac" value="ff:fa:fb:fc:fd:fe"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testPortalORWifiSet5" id="testPortalORWifiSet5" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet5.do">
    	 <input type="hidden" name="typeName" value="打开portal"> 
    	 <h4>打开portal</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	
	<br/>
    <br/>
    <form name="testPortalORWifiSet5_1" id="testPortalORWifiSet5_1" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet5_1.do">
    	 <input type="hidden" name="typeName" value="关闭portal"> 
    	 <h4>关闭portal</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>pwd:</td>
	    		<td>
	    			<input type="text" name="pwd" id="pwd" value="12345678"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testPortalORWifiSet6" id="testPortalORWifiSet6" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet6.do">
    	 <input type="hidden" name="typeName" value="设置SSID"> 
    	 <h4>设置SSID</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>ssid:</td>
	    		<td>
	    			<input type="text" name="ssid" id="ssid" value="123321"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	<br/>
    <br/>
    <form name="testPortalORWifiSet7" id="testPortalORWifiSet7" method="post" action="${basePath}/admin/obdTest_testPortalORWifiSet7.do">
    	 <input type="hidden" name="typeName" value="设置wifi密码"> 
    	 <h4>设置wifi密码</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="88888888"/>
	    	    </td>
	    	    <td>pwd:</td>
	    		<td>
	    			<input type="text" name="pwd" id="pwd" value="123#qax"/>
	    	    </td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
	
	
    <br/>
    <br/>
    <form name="testDriveSet" id="testDriveSet" method="post" action="${basePath}/admin/obdTest_testDriveSet.do" 
          onsubmit="return testDriveSetCheck();">
          <input type="hidden" name="typeName" value="驾驶行为设置"> 
    	 <h4>驾驶行为设置(同一驾驶行为的两个参数需同时设置)</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="tds_obdSn" value="88888888"/>
	    	    </td>
	    	    <td>操作</td>
	    	    <td>
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
	    	<tr>
	    	    <td>急加速_速度(km/h):</td>
	    		<td>
	    			<input type="text" name="quickenSpeed" id="tds_quickenSpeed" value="20"/>
	    	    </td>
	    	    <td>急减速_速度(km/h):</td>
	    		<td>
	    			<input type="text" name="quickSlowDownSpeed" id="tds_quickSlowDownSpeed" value="20"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>急转弯_速度(km/h):</td>
	    		<td>
	    			<input type="text" name="quickturnSpeed" id="tds_quickturnSpeed" value="20"/>
	    	    </td>
	    	    <td>急转弯—角度(度):</td>
	    		<td>
	    			<input type="text" name="quickturnAngle" id="tds_quickturnAngle" value="20"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>超速_速度(km/h):</td>
	    		<td>
	    			<input type="text" name="overspeed" id="tds_overspeed" value="20"/>
	    	    </td>
	    	    <td>超速—时间(s):</td>
	    		<td>
	    			<input type="text" name="overspeedTime" id="tds_overspeedTime" value="20"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>疲劳驾驶—连续驾驶时间(分):</td>
	    		<td>
	    			<input type="text" name="fatigueDrive" id="tds_fatigueDrive" value="20"/>
	    	    </td>
	    	    <td>疲劳驾驶—休息时间(分):</td>
	    		<td>
	    			<input type="text" name="fatigueSleep" id="tds_fatigueSleep" value="20"/>
	    	    </td>
	    	</tr>
		</table>
	</form>
	
    <br/>
    <br/>
    
    ${param.typeName }，结果：${result }
    &nbsp;离线下发记录=> <a href="admin/obdTestSendPacket_list.do?mid=69">查看</a>
    <br>
    <h3>ACKORQueryData：</h3>
    ${mapACKORQueryData }
    <br>
    <h3>OBD连接信息：</h3>
    ${mapOBDChannel }
  </body>
</html>
