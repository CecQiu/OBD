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
    <form name="testTravelParamsSet" id="testTravelParamsSet" method="post" action="${basePath}/testOBD/testEFenceSetEFenceAction.do">
    	 <h4>电子围栏设置：</h4>
    	 <table border="1px" style="text-align: center;margin: auto;">
	    	<tr>
	    	    <td>定时定点</td>
	    		<td>
	    			<input type="radio" name="timingType" id="timingType"value="1"/>有定时
	    			<input type="radio" name="timingType" id="timingType" value="0" checked="checked" />无定时
	    	    </td>
	    	    <td>区域编号:</td>
	    		<td>
	    			<input type="text" placeholder="0~5" name="areaNo" id="areaNo" value="0"/>
	    	    </td>
	    	</tr>
	    	<tr rowspan="2">
	    	    <td> 围栏类型：</td>
	    		<td>
	    			<input type="radio" name="type" id="shuddenTurnSpeed" checked="checked" value="0"/>圆形
	    			<input type="radio" name="type" id="shuddenTurnSpeed" value="1"/>矩形
	    	    </td>
	    	    <td>报警方式：</td>
	    		<td colspan="2">
	    			<input type="radio" name="warnType" id="warnType" checked="checked" value="1"/>
	    			1进区域报警，
	    			<input type="radio" name="warnType" id="warnType" value="2"/>
	    			2出区域报警
	    			<input type="radio" name="warnType" id="warnType" value="4"/>
                 	4取消围栏
	    			<input type="radio" name="warnType" id="warnType" value="5"/>
	    			5取消所有围栏
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td> 大经度：</td>
	    		<td>
	    			<input type="text" placeholder="11604000" name="bigLongitude" id="bigLongitude" value="11604000"/>
	    	    </td>
	    	    <td>大纬度：</td>
	    		<td colspan="2">
	    			<input type="text" placeholder="33320000"  name="bigLatitude" id="bigLatitude" value="11604000"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td> 小经度：</td>
	    		<td>
	    			<input type="text" placeholder="11604000"  name="smallLongitude" id="smallLongitude" value="11604000"/>
	    	    </td>
	    	    <td>小纬度：</td>
	    		<td colspan="2">
	    			<input type="text" placeholder="33320000"  name="smallLatitude" id="smallLatitude" value="33320000"/>
	    	    </td>
	    	</tr>
	    	<tr>
	    	    <td> 定时开始时间：</td>
	    		<td>
	    			<input type="text" placeholder="yyyyMMddHHmmss" name="timingBegin" id="timingBegin" />
	    	    </td>
	    	    <td>定时结束时间：</td>
	    		<td colspan="2">
	    			<input type="text" placeholder="yyyyMMddHHmmss" name="timingEnd" id="timingEnd"/>
	    	    </td>
	    	</tr>
	    	 <tr>
	    		<td>设备号:</td>
	    		<td width="10px">
	    			<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
	    	    </td>
	    	    <td colspan="2">
					<input class="btn btn-s-md btn-success" type="submit" value=" 提 交 "/>
				</td>
	    	</tr>
		</table>
	</form>
    
    <br/>
    <br/>
    
   结果：${result }
  
  </body>
</html>
