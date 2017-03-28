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
			$('#'+id).attr("href",hrefString)
			alert($('#'+id).attr("href"));
			return true;
		}
	</script>
  </head>
  
  <body>
	<br/>
    <br/>
    <form name="testDataServerParamsSet" id="testDataServerParamsSet" method="post" action="${basePath}/testOBD/testFOTASetFOTAAction.do">
    	 <h4>FOTA参数：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn}" />
	    	    </td>
	    	    <td>文件名:</td>
	    		<td>
	    			<input type="text" name="fota.fileName" id="fota.fileName" />
	    	    </td>
	    	    <td>FTP地址:</td>
	    		<td>
	    			<input type="text" name="fota.address" id="fota.address" />
	    	    </td>
	    	    <tr/>
	    	    <tr>
	    	    <td>FTP端口:</td>
	    		<td>
	    			<input type="text" name="fota.port" id="fota.port"  />
	    	    </td>
	    	    <td>用户名:</td>
	    		<td>
	    			<input type="text" name="fota.username" id="fota.username" />
	    	    </td>
	    	    <td>密码:</td>
	    		<td>
	    			<input type="text" name="fota.password" id="fota.password" />
	    	    </td>
	    	</tr>
	    	<tr>
	    		<td colspan="6">
					<input class="btn btn-s-md btn-success" type="submit" value=" 测 试 "/>
				</td>
	    	</tr>
		</table>
	</form>
	结果：${result }
  </body>
</html>
