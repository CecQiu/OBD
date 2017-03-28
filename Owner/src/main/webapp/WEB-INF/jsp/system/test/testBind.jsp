<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>激活设备</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script language="javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>

</head>
<script type="text/javascript" language="javascript">
$(function() {
	
	
});
function bind(){
	var obdSn = $('input[name=obdSn]').val();
	if(obdSn == '' || obdSn.length < 1){
		alert('设备号不能为空！');
		return;
	}
	var mobile = $('input[name=mobile]').val();
	if(mobile == '' || mobile.length < 1){
		alert('手机号不能为空！');
		return;
	}
	$.post('${pageContext.request.contextPath}/test/testSign',{
		deviceId:obdSn
	},function(sign){
		$.post('${pageContext.request.contextPath}/api/obd/bind',{
			deviceId:obdSn,
			username:'chezhutong',
			time:'123456',
			sign:sign,
			userId:mobile,
			hgDeviceSn:'123456',
			userType:2
		},function(data){
			$('#msg').text(data);
		});
	});
	
}

</script>
<body>
	<h3  align="center">测试-激活设备</h3>
  <table border="1px" style="text-align: center;margin: auto;">
    	<tr>
    		<td>设备号obdSn：</td>
    		<td><input type="text" name="obdSn"/></td>
    	</tr>
    	<tr>
    		<td>手机号：</td>
    		<td><input type="text" name="mobile"/></td>
    	</tr>
		<tr>
			<td colspan="2"><button onclick="bind();">激活设备</button></td>
		</tr>		
		<tr>
			<td colspan="2"><span id="msg"></span></td>
		</tr>		
	</table>
</body>
</html>
