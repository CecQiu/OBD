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
		function testPrivateProtocol(){
			var obdSn = $('#obdSn').val();
			var msg = $('#msg').val();
			$.ajax({  
				type:'post',      
				url:'${basePath}/admin/obdTest_testPrivateProtocol.do',  
				data:{obdSn:obdSn,msg:msg,typeName:'私有协议下发'},  
				cache:false,  
				dataType:'text',  
				success:function(data){
					if(data == 'ok'){
						alert('下发成功！');
					}else{
						alert('下发失败！未知原因~');
					}
				}  
			});  
		}
		
	</script>
  </head>
  
  <body>
   
    <form name="priPro" id="priPro" method="post" action="${basePath}/admin/obdTest_testPrivateProtocol.do">
    	 <h4>私有协议下发</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
    	 	<tr>
	    	    <td>设备号:</td>
	    		<td>
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn}"/>
	    	    </td>
	    	</tr>
    	 	<tr>
	    	    <td>报文内容:</td>
	    		<td>
	    			<textarea name="msg" id="msg" cols="3" rows="3" style="width:100%;"></textarea>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td>操作:</td>
	    		<td>
					<input class="btn btn-s-md btn-success" type="button" value="提 交 " onclick="testPrivateProtocol();"/>
				</td>
	    	</tr>
		</table>
	</form>
	下发记录=> <a href="admin/obdTestSendPacket_list.do?mid=69">查看</a>
  </body>
</html>
