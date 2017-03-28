<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../../include/common.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="${basePath}/js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/jquery.md5.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${basePath}/js/coordinateConversion.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/coordinateConversion.js" charset="UTF-8"></script>
<style type="text/css">
body, html {
	width: 100%;
	height: 100%;
	margin: 0;
	font-family: "微软雅黑";
}

.lu_input {
	background-color: white;
	padding-left: 8px;
	padding-right: 8px;
	border-color: #9999FF;
}

#users {
	position: absolute;
	width: 15%;
	height: 100%;
	right: 0px;
	top: 0px;
}
</style>

<link rel="stylesheet" type="text/css"
	href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=rTbzYWazzcbZDP2NUEbff6zo"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet"
	href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<script type="text/javascript"
	src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet"
	href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />

<title>电子围栏信息</title>
</head>
<body style="height: 1000px;">
	<div id="map" style="height: 80%; width: 100%;"></div>
	<br/>
	<div style="height: 20%; width: 100%;">
		'${fence}'
		<br/>
		<input class="btn btn-primary pull-center" type="button" value="返 回" onclick="javascript:window.history.back('-1');" />&nbsp;
	</div>
	
		
	<script type="text/javascript">
		var map = new BMap.Map("map");
		map.addControl(new BMap.NavigationControl()); //  map.addControl(new BMap.ScaleControl());                   
		//map.addControl(new BMap.MapTypeControl()); //     map.setMapType(BMAP_SATELLITE_MAP);  
		map.enableScrollWheelZoom(); //允许鼠标缩放
		
		
		var obj =  '${fenceJson}';
		//console.info(obj);
		//var fence = JSONArray.parse(fence);
		var fence = eval(obj);
		//百度地图坐标集合
		var bdArray = new Array();
		for(var i in fence){
			//console.info(i);
			var point = fence[i];
			//console.info(point.longitude+"---"+point.latitude);
			//WGS-84——》GCJ坐标
			var gcjObj = GPS.gcj_encrypt(point.latitude,point.longitude);
			//GCJ坐标——》转百度坐标
			var obj=GPS.bd_encrypt(gcjObj.lat,gcjObj.lon);
			if(i==0){
				map.centerAndZoom(new BMap.Point(obj.lon,obj.lat), 19); // 				
			}
			bdArray.push(new BMap.Point(obj.lon,obj.lat));
		}
		

		var polygon = new BMap.Polygon(bdArray, {
			strokeColor : "#f50704",
			fillColor : "",
			strokeWeight : 3,
			strokeOpacity : 0,
			fillOpacity : 0,
		});
		map.addOverlay(polygon);
	</script>
</body>
</html>