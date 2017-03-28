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
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  <body>
   <table border="1px" style="text-align: center;margin: auto;">
   		<tr>
    		<td>OBD设备号</td>
    		<td>
    			<input type="text" name="obdSn">
    	    </td>
    	</tr>
    	<tr>
    		<td>1、查询数据服务器参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testDataServerParams?obdSn=${param.obdSn }&typeName=数据服务器参数">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>2、查询升级服务器参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testUpgradeServerParams?obdSn=${param.obdSn }&typeName=升级服务器参数">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>3、查询portal服务器参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testPortalServerParams?obdSn=${param.obdSn }&typeName=portal服务器参数">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>4、查询设备版本</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testDeviceVersion?obdSn=${param.obdSn }&typeName=设备版本">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>5、实时位置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testRealTimeLoc?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>6、总里程</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testTotalMiles?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>7、设备时间参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testDeviceTimeParams?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>8、行程参数</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testTravelTimeParams?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>9、设备状态</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testDeviceState?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>10、车辆状态</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testCarState?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
    	    </td>
    	</tr>
    	<tr>
    		<td>11、报警设置</td>
    		<td>
    			<a href="${pageContext.request.contextPath}/test/testWarnSettings?obdSn=${param.obdSn }&typeName=实时位置">查询</a>
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
