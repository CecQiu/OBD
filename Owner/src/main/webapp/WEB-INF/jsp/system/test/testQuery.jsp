<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测试！查询操作</title>
    <jsp:include page="../../include/common.jsp" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  <script type="text/javascript">
		function urlSet(id){
			var obdSn=$("#obdSn").val().trim();
			if(obdSn == "" || obdSn == undefined || obdSn == null){
				alert("设备号不能为空");
				return false;
			}
			if(obdSn.length!=2*4){
				alert("设备号输入有误,obd设备号为4个byte.");
				return false;
			}
			var hrefStr=$('#'+id).attr("href");
			var hrefString = hrefStr + "&obdSn="+obdSn;
			$('#'+id).attr("href",hrefString)
			return true;
		}
	</script>
  <body>
   <table border="1px" style="margin: auto;">
   		<tr>
    		<td colspan="2">OBD设备号</td>
    		<td colspan="2">
    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }">
    	    </td>
    	</tr>
    	<tr>
    		<td>1、查询数据服务器参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testDataServerParams?typeName=查询数据服务器参数" onClick='return urlSet("dataServerParams");' id="dataServerParams">查询</a>
    	    </td>
    		<td>21、查询域黑名单</td>
    		<td>
    			<a id='DomainBlack_open' href="${pageContext.request.contextPath}/testOBD/testDomainBlack?typeName=查询域黑名单" 
    			onClick='return urlSet("DomainBlack_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>2、查询升级服务器参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testUpgradeServerParams?typeName=查询升级服务器参数" onClick='return urlSet("upgradeServerParams");' id="upgradeServerParams">查询</a>
    	    </td>
    		<td>22、查询域白名单开关</td>
    		<td>
    			<a id='DomainWhiteSwitch_open' href="${pageContext.request.contextPath}/testOBD/testDomainWhiteSwitch?typeName=查询域白名单开关" 
    			onClick='return urlSet("DomainWhiteSwitch_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>3、查询portal服务器参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testPortalServerParams?typeName=查询portal服务器参数" onClick='return urlSet("portalServerParams");' id="portalServerParams">查询</a>
    	    </td>
    	    <td>23、查询域黑名单开关</td>
    		<td>
    			<a id='DomainBlackSwitch_open' href="${pageContext.request.contextPath}/testOBD/testDomainBlackSwitch?typeName=查询域黑名单开关" 
    			onClick='return urlSet("DomainBlackSwitch_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>4、查询设备版本</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testDeviceVersion?typeName=查询设备版本" onClick='return urlSet("deviceVersion");' id="deviceVersion">查询</a>
    	    </td>
    	    <td>24、车型功能设置查询</td>
    		<td>
    			<a id='CarTypeSetting' href="${pageContext.request.contextPath}/testOBD/testCarTypeSetting?typeName=车型功能设置查询" 
    			onClick='return urlSet("CarTypeSetting");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>5、查询实时位置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testRealTimeLoc?typeName=查询实时位置" onClick='return urlSet("realTimeLoc");' id="realTimeLoc">查询</a>
    	    </td>
    	    <td>25、低电压休眠阈值查询</td>
    		<td>
    			<a id='LowVoltSleepValueQuery' href="${pageContext.request.contextPath}/testOBD/testLowVoltSleepValueQuery?typeName=低电压休眠阈值查询" 
    			onClick='return urlSet("LowVoltSleepValueQuery");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>6、查询半条行程</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testTotalMiles?&typeName=查询半条行程" onClick='return urlSet("totalMiles");' id="totalMiles">查询</a>
    	    </td>
    	    <td>26、网络模式查询</td>
    		<td>
    			<a id='NetModelQuery' href="${pageContext.request.contextPath}/testOBD/testNetModelQuery?typeName=网络模式查询" 
    			onClick='return urlSet("NetModelQuery");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>7、查询设备时间参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testDeviceTimeParams?typeName=查询设备时间参数" onClick='return urlSet("deviceTimeParams");' id="deviceTimeParams">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>8、查询行程参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testTravelTimeParams?typeName=查询行程参数" onClick='return urlSet("travelTimeParams");' id="travelTimeParams">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>9、查询设备状态</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testDeviceState?typeName=查询设备状态" onClick='return urlSet("deviceState");' id="deviceState">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>10、查询车辆状态</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testCarState?typeName=查询车辆状态" onClick='return urlSet("carState");' id="carState">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>11、查询报警设置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/testOBD/testWarnSettings?typeName=查询报警设置" onClick='return urlSet("warnSettings");' id="warnSettings">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>12、查询WIFI流量开关</td>
    		<td>
    			<a id='queryWifiFlowSet_open' href="${pageContext.request.contextPath}/testOBD/testQueryWifiFlowSet?typeName=查询WIFI流量开关" 
    			onClick='return urlSet("queryWifiFlowSet_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>13、查询WIFI流量</td>
    		<td>
    			<a id='wifiFlow_open' href="${pageContext.request.contextPath}/testOBD/testWifiFlow?typeName=查询WIFI流量" 
    			onClick='return urlSet("wifiFlow_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>14、查询熄火后WIFI使用时间提醒</td>
    		<td>
    			<a id='overWifiUse_open' href="${pageContext.request.contextPath}/testOBD/testOverWifiUse?typeName=查询熄火后WIFI使用时间提醒" 
    			onClick='return urlSet("overWifiUse_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>15、清除使用流量</td>
    		<td>
    			<a id='cleanFlowUse_open' href="${pageContext.request.contextPath}/testOBD/testCleanFlowUse?typeName=清除使用流量" 
    			onClick='return urlSet("cleanFlowUse_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>16、查询使用流量</td>
    		<td>
    			<a id='queryFlowUse_open' href="${pageContext.request.contextPath}/testOBD/testQueryFlowUse?typeName=查询使用流量" 
    			onClick='return urlSet("queryFlowUse_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>17、读取故障码</td>
    		<td>
    			<a id='readFaultCode_open' href="${pageContext.request.contextPath}/testOBD/testReadFaultCode?typeName=读取故障码" 
    			onClick='return urlSet("readFaultCode_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>18、查询休眠电压差值</td>
    		<td>
    			<a id='SleepVolt_open' href="${pageContext.request.contextPath}/testOBD/testSleepVolt?typeName=查询休眠电压差值" 
    			onClick='return urlSet("SleepVolt_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>19、查询休眠加速度差值</td>
    		<td>
    			<a id='SleepOverSpeed_open' href="${pageContext.request.contextPath}/testOBD/testSleepOverSpeed?typeName=查询休眠加速度差值" 
    			onClick='return urlSet("SleepOverSpeed_open");'>查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>20、查询域白名单</td>
    		<td>
    			<a id='DomainWhite_open' href="${pageContext.request.contextPath}/testOBD/testDomainWhite?typeName=查询域白名单" 
    			onClick='return urlSet("DomainWhite_open");'>查询</a>
    	    </td>
    	</tr>
    </table>
    
    ${obdSn }->${param.typeName }，结果：${result }
    &nbsp;离线下发记录=> <a href="admin/obdTestSendPacket_list.do?mid=69">查看</a>
    <br>
    <h3>ACKORQueryData：</h3>
    ${mapACKORQueryData }
    <br>
    <h3>OBD连接信息：</h3>
    ${mapOBDChannel }
  </body>
</html>
