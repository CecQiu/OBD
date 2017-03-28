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
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
    <table border="1px" style="text-align: center;margin: auto;">
    	<tr>
    		<td>1、测试GPS</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testGPS?obdSn=${param.obdSn}&state=0&typeName=测试GPS">测试(开)</a>
    			<a href="${pageContext.request.contextPath}/test/testGPS?obdSn=${param.obdSn}&state=1&typeName=测试GPS">测试(关)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>2、测试WIFI</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testWiFi?obdSn=${param.obdSn}&state=0&typeName=测试WIFI">测试(开)</a>
    			<a href="${pageContext.request.contextPath}/test/testWiFi?obdSn=${param.obdSn}&state=1&typeName=测试WIFI">测试(关)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>3、测试撤防设防</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testGuard?obdSn=${param.obdSn}&state=0&typeName=测试撤防设防">测试(设防)</a>
    			<a href="${pageContext.request.contextPath}/test/testGuard?obdSn=${param.obdSn}&state=1&typeName=测试撤防设防">测试(撤防)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>4、测试震动报警</td>
    		<td>
				<a href="${pageContext.request.contextPath}/test/testShock?obdSn=${param.obdSn}&state=0&typeName=测试震动报警">测试(开)</a> 
				<a href="${pageContext.request.contextPath}/test/testShock?obdSn=${param.obdSn}&state=1&typeName=测试震动报警">测试(关)</a>    	    
			</td>
    	</tr>
    	<tr>
    		<td>5、测试清除故障码</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testClearFaultCode?obdSn=${param.obdSn}&state=1&typeName=测试清除故障码">测试清除故障码</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>6、测试GPS数据格式</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testGPSFormat?obdSn=${param.obdSn}&state=1&typeName=测试GPS数据格式">测试(全部)</a>
    			<a href="${pageContext.request.contextPath}/test/testGPSFormat?obdSn=${param.obdSn}&state=0&typeName=测试GPS数据格式">测试(只传定位数据)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>7、测试离线心跳设置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testOffHeart?obdSn=${param.obdSn}&state=0&typeName=测试离线心跳设置">测试(无)</a>
    			<a href="${pageContext.request.contextPath}/test/testOffHeart?obdSn=${param.obdSn}&state=1&typeName=测试离线心跳设置">测试(设置)</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>8、测试超速报警</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testOverSpeedWarn?obdSn=${param.obdSn}&state=0&typeName=测试超速报警">测试kai</a>
    			<a href="${pageContext.request.contextPath}/test/testOverSpeedWarn?obdSn=${param.obdSn}&state=1&typeName=测试超速报警">测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>9、测试设备重置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testReset?obdSn=${param.obdSn}&state=1&typeName=测试设备重置">测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>10、测试设备重启</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testRestart?obdSn=${param.obdSn}&state=1&typeName=测试设备重启">测试</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>11、测试设备升级设置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testDeviceUpgradeSet?obdSn=${param.obdSn}&state=1&typeName=测试设备升级">测试</a>
    	    </td>
    	</tr>
    </table>
    
    ${param.typeName }，结果：${result }
    
    <br>
    <h3>ACKORQueryData：</h3>
    ${mapACKORQueryData }
    <br>
    <h3>OBD连接信息：</h3>
    ${mapOBDChannel }
  </body>
</html>
