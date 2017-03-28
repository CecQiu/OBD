<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<title>路书</title> 
	<style type="text/css">
		body, html{width: 100%;height: 1000px;;margin:0;font-family:"微软雅黑";}
		#map_canvas{width:90%;height:900px;}
		#result {width:10%}
	</style>
	<script src="http://api.map.baidu.com/api?v=2.0&ak=rTbzYWazzcbZDP2NUEbff6zo"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
	<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/changeMore.js"></script>
</head> 
<body>
	<div id="result"></div>
	<div class="row">
		<form action="#" style="width: 710px;float: left;" id="myform">
			<label>选择日期：</label><input type="text" id="date" value="" name="date" readonly="readonly" style="width: 100px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			<label>开始时间：</label><input type="text" id="start" value="00:00:00" name="start" style="width: 100px;" readonly="readonly" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
			<label>结束时间：</label><input type="text" id="end" value="23:59:59" name="end" style="width: 100px;" readonly="readonly" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
		</form>
		<button id="search" class="btn" onclick="search()">查看</button>
		<button id="run" class="btn" onclick="lu_start()">开始</button> 
		<button id="stop" class="btn" onclick="lu_stop()">停止</button> 
		<button id="pause" class="btn" onclick="lu_pause()">暂停</button> 
		<!-- <button id="hide" class="btn" onclick="lu_hide()">隐藏信息窗口</button> 
		<button id="show" class="btn" onclick="lu_show()">展示信息窗口</button>  -->
	</div> 
	<div id="map_canvas"></div> 
    <script> 
	var map = new BMap.Map('map_canvas');
	map.enableScrollWheelZoom();
	var point = new BMap.Point(116.411053, 39.950507); // 确定中心点
    map.centerAndZoom(point, 16); // 初始化地图map,设置中心点和地图级别。
    var points = new Array();
    var polyline = null;
	function $(element){
		return document.getElementById(element);
		jQuery.noConflict();
	}
	
	function lu_start() {
		if(lushu == null || lushu == undefined) {
			alert("还没有路线");
			return;
		} else {
			lushu.start();
		}
	}
	
	function lu_stop() {
		if(lushu == null || lushu == undefined) {
			alert("还没有路线");
			return;
		} else {
			lushu.stop();
		}
	}
	
	function lu_pause() {
		if(lushu == null || lushu == undefined) {
			alert("还没有路线");
			return;
		} else {
			lushu.pause();
		}
	}
	
	function lu_hide() {
		if(lushu == null || lushu == undefined) {
			alert("还没有路线");
			return;
		} else {
			lushu.hideInfoWindow();
		}
	}
	
	function lu_show() {
		if(lushu == null || lushu == undefined) {
			alert("还没有路线");
			return;
		} else {
			lushu.showInfoWindow();
		}
	}
	
    function setLine(list) {
    	gpsPoints.length = 0;
    	mapPoints.length = 0;
    	for(var i=0;i<list.length;i++) {
    		gpsPoints.push(new BMap.Point(list[i].longitude, list[i].latitude));
    	}
    	BMap.Convertor.transMore(gpsPoints,0,callback);
	}
    
    var lushu = null;
    
	function search() {
		var date = jQuery("#date").val();
		var start = jQuery("#start").val();
		var end = jQuery("#end").val();
		if(date == '' || date == undefined) {
			alert("请选择日期");
			return;
		} else if(start == '' || start == undefined) {
			alert("请选择开始时间");
			return;
		} else if(end == "" || end == undefined) {
			alert("请选择结束时间");
			return;
		}
		if(polyline != null) {
			polyline.remove();
		}
		if(lushu != null) {
			map.removeOverlay(lushu);
		}
		map.clearOverlays();
		jQuery.ajax({
			url:'carLine_getRoute.do',
			data:{'sn':'${sn}','date':date,'start':start,'end':end},
			type:"POST",
			success:function(data) {
				if(data.status == 'success') {
					setLine(data.message);
				} else {
					alert(data.message)
				}
			},error:function() {
				alert("请求错误");
			}
		});
	}
	
/* 	function callback(xyResults){
		var xyResult = null;
		for(var index in xyResults){
			xyResult = xyResults[index];
			if(xyResult.error != 0){
				continue;//出错就直接返回;
			} else {
				mapPoints.push(new BMap.Point(xyResult.x, xyResult.y));
			}
			if(mapPoints.length == gpsPoints.length) {
				drawLine(mapPoints);
			}
		}
	}
	
	var gpsPoints = new Array();
	var mapPoints = new Array();
	
	function drawLine(points) {
		if(points.length > 1) {
    		polyline = new BMap.Polyline(points); //创建折线
    		map.addOverlay(polyline); //绘制折线  
    		map.centerAndZoom(points[0], 20);
    		lushu = new BMapLib.LuShu(map,points,{
    			defaultContent:"",//"从天安门到百度大厦"
    			autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
    			icon  : new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)}),
    			speed: 100,
    			enableRotation:true,//是否设置marker随着道路的走向进行旋转
    			landmarkPois: []
    		});
    	} else if(points.length == 1) {
    		var marker = new BMap.Marker(points[0]);  // 创建标注
			map.addOverlay(marker);
			map.centerAndZoom(points[0], 20);
    	} else {
    		alert("没有轨迹信息");
    	}
		gpsPoints.length = 0;
    	mapPoints.length = 0;
	} */
	

	function getLine() {
		hasload = true;
		clearArray(gspPoints.length);
		clearArray(mapPoints.length);
		var date = jQuery("#date").val();
		var start = jQuery("#start").val();
		var end = jQuery("#end").val();
		if(date == '' || date == undefined) {
			alert("请选择日期");
			return;
		} else if(start == '' || start == undefined) {
			alert("请选择开始时间");
			return;
		} else if(end == "" || end == undefined) {
			alert("请选择结束时间");
			return;
		}
		if(lushu != null) {
			map.clearOverlays();
		}
		jQuery.ajax({
			url:'carLine_getRoute.do',
			data:{'sn':jQuery("#sn").val(),'date':date,'start':start,'end':end},
			type:"POST",
			success:function(data) {
				if(data.status == 'success') {
					setLine(data.message);
				} else {
					alert(data.message)
				}
			},error:function() {
				alert("请求错误");
			}
		});
		jQuery("#close_modal").click();
	}
	
	function setLine(list) {
		gspPoints.length = 0;
		mapPoints.length = 0;
    	for(var i=0;i<list.length;i++) {
    		var point = new BMap.Point(list[i].longitude, list[i].latitude);
    		gspPoints.push(point);
    	}
    	changePosition2();
	}
	
	//单个坐标转换
	var PositionUrl = 'http://api.map.baidu.com/geoconv/v1/?';
	function changePosition(point){
		var str = point.lng + "," + point.lat;
		str  = "coords=" + str + "&from=1&to=5";
		var url = PositionUrl + str;
		var script = document.createElement('script');
		script.src = url + '&ak=rTbzYWazzcbZDP2NUEbff6zo&callback=dealResult';
		var head = document.getElementsByTagName('head')[0];
	    script.type = 'text/javascript';
	    //借鉴了jQuery的script跨域方法
	    script.onload = script.onreadystatechange = function(){
	        if((!this.readyState || this.readyState === "loaded" || this.readyState === "complete")){
	        	dealResult();
	            script.onload = script.onreadystatechange = null;
	            if ( head && script.parentNode ) {
	                head.removeChild( script );
	            }
	        }
	    };
	    head.insertBefore( script, head.firstChild );
	}
	
	//单个坐标转换
	function dealResult(msg){
		if(msg == undefined || msg.status == undefined) {//多个坐标转换时会调用这个还是，不知为什么，但是参数为空，判断空去重复
			return;
		}
		if(msg.status != 0){
			alert("无正确的返回结果。");
			return;
		} else {
			var point = new BMap.Point(msg.result[0].x,msg.result[0].y);
			translateCallback(point);
		}
	}
	
	var gspPoints = new Array();
	var mapPoints = new Array();
	
	//多GSP坐标转换回调函数		
	function callback(msg){
		if(msg == undefined || msg.status == undefined) {
			return;
		} else if(msg.status != 0){
			return;
		} else {
			for(var i=0;i<msg.result.length;i++) {
				var res = msg.result[i];
				var point = new BMap.Point(res.x,res.y);
				mapPoints.push(point);
			}
			//转换坐标校正，确保转换坐标与原始坐标一一对应
			if(msg.result.length < 60 && mapPoints.length < gspPoints.length) {
				for(var i=60;i>msg.result.length;i--) {
					gspPoints.remove(mapPoints.length);
				}
			}
		}
		//长度一致时显示轨迹
		if(mapPoints.length >= gspPoints.length) {
			setTimeout("showLine()","1000");
		} else {
			setTimeout("changePosition2()","100");
		}
	}
	
	//多个GSP坐标转换
	//IE最大URL长度 2083 坐标最大长度32，每次限定60个
	function changePosition2(){
		if(mapPoints.length >= gspPoints.length) {
			return;
		}
		var str = gspPoints[mapPoints.length].lng + "," + gspPoints[mapPoints.length].lat;
		//从未转换坐标开始转换60个
		for(var i=mapPoints.length;i<Math.min(mapPoints.length + 59,gspPoints.length);i++) {
			var point = gspPoints[i];
			str = str + ";" + point.lng + "," + point.lat;
		}
		str  = "coords=" + str + "&from=1&to=5";
		var url = PositionUrl + str;
		var script = document.createElement('script');
		script.src = url + '&ak=rTbzYWazzcbZDP2NUEbff6zo&callback=callback';
		var head = document.getElementsByTagName('head')[0];
	    script.type = 'text/javascript';
	    //借鉴了jQuery的script跨域方法
	    script.onload = script.onreadystatechange = function(){
	        if((!this.readyState || this.readyState === "loaded" || this.readyState === "complete")){
	        	dealResult();
	            script.onload = script.onreadystatechange = null;
	            if ( head && script.parentNode ) {
	                head.removeChild( script );
	            }
	        }
	    };
	    head.insertBefore( script, head.firstChild );
	}
	
	//显示轨迹
	function showLine(mapPoints) {
		if(mapPoints.length > 1) {
    		polyline = new BMap.Polyline(mapPoints); //创建折线
    		map.addOverlay(polyline); //绘制折线  
    		map.centerAndZoom(mapPoints[0], map.getZoom());
    		lushu = new BMapLib.LuShu(map,mapPoints,{
    			defaultContent:"",//"从天安门到百度大厦"
    			autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
    			icon  : new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)}),
    			speed: 100,
    			enableRotation:true,//是否设置marker随着道路的走向进行旋转
    			landmarkPois: []
    		});
    	} else if(mapPoints.length == 1) {
    		var marker = new BMap.Marker(mapPoints[0]);  // 创建标注
			map.addOverlay(marker);
			map.centerAndZoom(mapPoints[0], map.getZoom());
    	} else {
    		alert("没有轨迹信息");
    	}
		hasload = false;
	}
	
	function clearArray(arr) {
		for(var i=0;i<arr.length;i++) {
			arr.remove(i);
		}
	}
	
	var hasload = false;
</script> 
<script type="text/javascript" src="${basePath}/js/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/plugins/My97DatePicker/WdatePicker.js" charset="UTF-8"></script>
</body> 
</html> 
