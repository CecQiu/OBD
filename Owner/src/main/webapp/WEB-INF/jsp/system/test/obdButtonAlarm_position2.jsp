<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="${basePath}/js/jquery.min.js" charset="UTF-8"></script>

<link rel="stylesheet" type="text/css" href="${basePath}/css/bootstrap.min.css" />
<!--[if IE 7]>
<link rel="stylesheet" href="${basePath}/css/plugins/font-awesome/css/font-awesome-ie7.min.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<style type="text/css">
	body, html{width: 100%;height: 900px;margin:0;font-family:"微软雅黑";}
	#controls{
		text-align: right;
	    width: 85%;
	    padding-top: 10px;
	    position: absolute;
	    z-index: 100;
	    padding-right: 130px;
	}
	.lu_input{
		background-color: white;
	    padding-left: 8px;
	    padding-right: 8px;
	    border-color: #9999FF;
	}
</style>
<script src="http://api.map.baidu.com/api?v=2.0&ak=rTbzYWazzcbZDP2NUEbff6zo"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
<script type="text/javascript" src="${basePath}/js/plugins/My97DatePicker/WdatePicker.js" charset="UTF-8"></script>
<script src="${basePath}/js/bootstrap.min.js"></script>
<title>城市名定位</title>
</head>
<body>
<!-- 	<div id="controls" style="text-align: right;width: 85%">
		<input type="button" id="info_control" onclick="set_showInfo();" class="lu_input" value="打开提示"/>
		<span id="control"></span>
	</div> -->
	<div id="allmap" style="width: 85%;float: left;height: 100%;position: relative;"></div>
	<div style="float: left;width: 165px;height: 100%;overflow:scroll;font-size: 16px;">
		设备：${positionInfo.obdSn }<br/>
		定位时间：<br/>
		<fmt:formatDate value="${positionInfo.time }" pattern="yyyy-MM-dd HH:mm:ss"/>
		<br/>
		坐标：<br/>
		<c:if test="${!empty positionInfo }">
			<c:if test="${empty positionInfo.longitude }">
				当前点定位失败<br/>
				<a href="obdButtonAlarm_position.do?obdSn=${positionInfo.obdSn}&time=${positionInfo.time }">查看上一定位成功点</a>
			</c:if>
			<c:if test="${!empty positionInfo.longitude }">
				${positionInfo.longitude }<br/>${positionInfo.latitude }<br/>
				转化后：<br/>
				${positionInfoM.longitude }<br/>${positionInfoM.latitude }<br/>
			</c:if>
		</c:if>
		<c:if test="${empty positionInfo }">
			定位失败！
		</c:if>
		<input class="btn btn-primary pull-center" type="button" value="返 回" onclick="javascript:window.history.back('-1');"/>&nbsp;
	</div>
	<script type="text/javascript">
		function $(element){
			jQuery.noConflict();
		}
		// 百度地图API功能
		var map = new BMap.Map("allmap");
		map.centerAndZoom(new BMap.Point(116.331398,39.897445),11);
		map.enableScrollWheelZoom(true);
		
		// 用经纬度设置地图中心点
		function theLocation(){
			map.clearOverlays(); 
			var new_point = new BMap.Point(113.307800,23.057031);
			map.centerAndZoom(new_point, 12);
			if('${positionInfoM.longitude}' != ''){
				new_point = new BMap.Point('${positionInfoM.longitude }','${positionInfoM.latitude }');
				var marker = new BMap.Marker(new_point);  // 创建标注
				map.addOverlay(marker);              // 将标注添加到地图中
				map.centerAndZoom(new_point, 15);
			}
				
			map.panTo(new_point);      
		}
		theLocation();
	</script>
</body>
</html>