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
	<div id="controls" style="text-align: right;width: 85%">
		<input type="button" id="info_control" onclick="set_showInfo();" class="lu_input" value="打开提示"/>
		<span id="control"></span>
	</div>
	<div id="allmap" style="width: 85%;float: left;height: 100%;position: relative;"></div>
	<div style="float: left;width: 165px;height: 100%;overflow:scroll;">
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
	<div class="row">
		<div class="col-md-12">
			<a id="modal-925285" href="#modal-container-925285" role="button" class="btn" data-toggle="modal" style="display: none"></a>
			<div class="modal fade" id="modal-container-925285" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
							<h4 class="modal-title" id="myModalLabel">查看轨迹</h4>
						</div>
						<div class="modal-body">
							<form role="form" class="form-horizontal" id="myform">
								<div class="form-group">
									<label for="name" class="col-sm-3" style="text-align: right;">用户名：</label>
									<div class="col-sm-9" style="float: right">
										<input type="text" style="width: 80%" class="form-control" id="name" readonly="readonly" />
										<input type="hidden" id="sn"/>
									</div>
								</div>
								<div class="form-group">
									<label for="plate" class="col-sm-3" style="text-align: right;">车牌：</label>
									<div class="col-sm-9" style="float: right">
										<input type="text" style="width: 80%" class="form-control" id="plate" readonly="readonly"/>
									</div>
								</div>
								<div class="form-group">
									<label for="date" class="col-sm-3" style="text-align: right;">查看日期：</label>
									<div class="col-sm-9" style="float: right">
										<input type="text" style="width: 80%" class="form-control" id="date" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
									</div>
								</div>
								<div class="form-group">
									<label for="start" class="col-sm-3" style="text-align: right;">开始时间：</label>
									<div class="col-sm-9" style="float: right">
										<input type="text" style="width: 80%" class="form-control" value="00:00:00" id="start" readonly="readonly" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
									</div>
								</div>
								<div class="form-group">
									<label for="end" class="col-sm-3" style="text-align: right;">结束时间：</label>
									<div class="col-sm-9" style="float: right">
										<input type="text" style="width: 80%" class="form-control" value="23:59:59" id="end" readonly="readonly" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
									</div>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal" id="close_modal">关闭</button> 
							<button type="button" class="btn btn-default" onclick="getLine()">查看</button>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
		function $(element){
			jQuery.noConflict();
		}
		//=======================设置中心点===================================
		// 百度地图API功能
		var map = new BMap.Map("allmap");
			map.enableScrollWheelZoom(true);//允许鼠标缩放
		/* var poi = new BMap.Point(113.307800,23.057031);
	    	map.centerAndZoom(poi, 16); */
	    var myCity = new BMap.LocalCity();
		myCity.get(function(result){
			var cityName = result.name;
			map.setCenter(cityName);
		});
		//=======================设置中心点===================================
		//向地图添加控件
		var opts = {type: BMAP_NAVIGATION_CONTROL_LARGE}
		map.addControl(new BMap.NavigationControl(opts));    
		map.addControl(new BMap.ScaleControl());    
		map.addControl(new BMap.OverviewMapControl());    
		map.addControl(new BMap.MapTypeControl());  
		var stCtrl = new BMap.PanoramaControl();  
		stCtrl.setOffset(new BMap.Size(20, 60)); 
		map.addControl(stCtrl);
		//======================^添加控制^===================================
	 	// 根据坐标得到地址描述    
	 	
	    var myGeo = new BMap.Geocoder();      
		var labels = new Array();
		var mark_points = new Array();
		var info_wins = new Array();
		
		var now_po;
		var po_msg;//位置描述信息
		//=================================车辆位置信息标注===================================
		function remarkPosition(val){
			map.addOverlay(mark_points[0]);
			jQuery("#controls").find('input').each(function(index){
				if(index > 0) {
					jQuery(this).remove();
				}
			});
			po_msg = '定位';
			var point = new BMap.Point('${positionInfoM.longitude }','${positionInfoM.latitude }');
	    	map.centerAndZoom(point, 12);
			if(now_po != undefined && (now_po.equals(point))) {
				return;
			} else {
				mark_points.length = 1;
				info_wins.length = 0;
				map.removeOverlay(mark_points.pop());
				now_po = point;
			}
			changePosition(point);
		}
		
		//================================点击获取地图经纬度===================================
		function showInfo(e){
			//alert(e.point.lng + ", " + e.point.lat);
			var lng = e.point.lng;
			var lat = e.point.lat;
			jQuery.ajax({
					url:'carPosition_bd2gps.do',
					data:{'lng':lng,'lat':lat},
					type:"POST",
				    dataType:"text",
					success:function(data) {
						jQuery('#bd').val(lat + ", " + lng);
						jQuery('#gps').val(data);
					}
		  });
	  }
		map.disableDoubleClickZoom();//禁止双击变大
		map.addEventListener("dblclick", showInfo);
		
		//回调函数
		function translateCallback(point){
			if(point == null || point == undefined) {
				alert("没有位置信息");
				return;
			}
			var marker = new BMap.Marker(point);  // 创建标注
			var label = new BMap.Label(po_msg,{offset:new BMap.Size(20,-10)});
			marker.setLabel(label);
			map.addOverlay(marker);              // 将标注添加到地图中
			myGeo.getLocation(point, function(result){      
		        var str = labels.pop();
		        if (result){
		        	//alert(result.address);
		        	str = labels.pop() + result.address + str;
		        } else {
		        	str = labels.pop() + str;
		        }
		        var infoWindow = new BMap.InfoWindow(str);  // 创建信息窗口对象
				if(isShowInfo) {
					map.openInfoWindow(infoWindow, point); //开启信息窗口
				}
				info_wins.push(infoWindow);
				marker.addEventListener("click", function(){
					map.openInfoWindow(infoWindow,point); //开启信息窗口
				});
				t1 = true;
			});
			//map.centerAndZoom(point, map.getZoom());
			showCenter(point);
			mark_points.push(marker);
		}
		//
		function playTack(val,name,plate) {
			map.clearOverlays();
			for(var i=0;i<mark_points.length;i++) {
				map.addOverlay(mark_points[i]);
			}
			jQuery("#modal-925285").click();
			jQuery("#name").val(name);
			jQuery("#plate").val(plate);
			jQuery("#sn").val(val);
		}
		
		var lushu;
		
		
		function getLine() {
			t1 = false;
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
			map.clearOverlays();
			for(var i=0;i<mark_points.length;i++) {
				map.addOverlay(mark_points[i]);
			}
			jQuery.ajax({
				url:'carLine_getRoute.do',
				data:{'sn':jQuery("#sn").val(),'date':date,'start':start,'end':end},
				type:"POST",
				success:function(data) {
					if(data.status == 'success' && data.message.length > 0) {
						setLine(data.message);
					} else if(data.status == 'success') {
						alert("没有轨迹信息。");
						t1 = true;
					} else {
						alert(data.message)
					}
				},error:function() {
					alert("请求错误");
				}
			});
			jQuery("#close_modal").click();
		}
		
		//单个坐标转换
		var PositionUrl = "http://api.map.baidu.com/geoconv/v1/?";
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
		
		var timePoints = new Array();
		
		function lnglatTransferString(lnglat){
			//113.3482666666
			//var s = '113.3482666666';
			//alert(lnglat);
			var ll = lnglat.split(".");
			var lng = ll[0];
			var lat = ('0.'+ll[1])*60;
			//alert(ss[0]);
			//alert((('0.'+ss[1])*60).toFixed(4));
			if(lng.length == 2){
				if(lat < 10){
					lat = '0' + lat.toFixed(4);
				}else{
					lat = lat.toFixed(4);
				}
				lnglat = lng + '°' + lat + '\'';
			}
			if(lng.length == 3){
				if(lat < 10){
					lat = '0' + lat.toFixed(2);
				}else{
					lat = lat.toFixed(3);
				}
				lnglat = lng + '°' + lat + '\'';
			}
			
			return lnglat;
		}
		
		function setLine(list) {
			//先暂停定时查询位置,然后清除所有地图标注
			window.clearTimeout(ptimer)
			map.clearOverlays();
			
			gspPoints.length = 0;
			mapPoints.length = 0;
			timePoints = new Array();
	    	for(var i=0;i<list.length;i++) {
	    		var point = new BMap.Point(list[i].longitude, list[i].latitude);
	    		gspPoints.push(point);
	    		var str = JSON.stringify(list[i].gspTrackTime); 
	    		var obj = JSON.parse(str);
	    		
	    		var date1 = new Date(obj.time); 
	    		timePoints.push(date1.format("yyyy-MM-dd HH:mm:ss") + ' ' + lnglatTransferString(list[i].longitude) + ' ' + lnglatTransferString(list[i].latitude));
	    	}
	    	changePosition2();
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
				//alert(msg.result[0].x);alert(msg.result[0].y);
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
				//alert('msg.result.length:'+msg.result.length);
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
				setTimeout("changePosition2()","100");//延时执行，连续请求无响应
			}
		}
		
		//多个GSP坐标转换
		//IE最大URL长度 2083 坐标最大长度32，每次限定60个
		function changePosition2(){
			if(mapPoints.length >= gspPoints.length) {
				return;
			}
			var str = '';//gspPoints[mapPoints.length].lng + "," + gspPoints[mapPoints.length].lat;
			//alert('str:'+lnglatTransferString(gspPoints[mapPoints.length].lng+'') + "," + lnglatTransferString(gspPoints[mapPoints.length].lat+''));
			//从未转换坐标开始转换60个
			//alert('changePosition2~gspPoints.length:'+gspPoints.length);
			//alert('changePosition2~mapPoints.length:'+mapPoints.length);
			for(var i=mapPoints.length;i<Math.min(mapPoints.length + 60,gspPoints.length);i++) {
				var point = gspPoints[i];
				str = str + point.lng + "," + point.lat + ";";
				//console.info(i);
			}
			str = str.substring(0, str.length-1);
			str  = "coords=" + str + "&from=1&to=5";
			var url = PositionUrl + str;
			//alert(url);
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
		
		var oldMarker = new Array();
		function isLook(){
			var b = true;
			if(jQuery('#islook').attr("checked") == 'checked' && timePoints.length > 0){
				b = true;
			}else{
				b = false;
			}
			var c = jQuery('#count').val();
			if(isNaN(c)){
				c = 2;
			}
			c++;
			for(var i = 0; i < gspPoints.length; i+=c){
				var marker = new BMap.Marker(mapPoints[i]);  // 创建标注
				var label = new BMap.Label(timePoints[i],{offset:(i % 2 == 0 ? new BMap.Size(-70,-17):new BMap.Size(-70,-17))});
				marker.setLabel(label);
				if(b){
					map.addOverlay(marker);
					oldMarker.push(marker);
				}else{
					for(var j = 0;j < oldMarker.length; j++){
						oldMarker.pop().remove();
					}
				}
			}
		}
		
		//显示轨迹
		function showLine() {
			//alert('gspPoints.length:'+gspPoints.length);
			//alert('mapPoints.length:'+mapPoints.length);
			if(mapPoints.length > 1) {
	    		polyline = new BMap.Polyline(mapPoints); //创建折线
	    		map.addOverlay(polyline); //绘制折线  
	    		var div = '<input type="button" onclick="lu_start();" class="lu_input" value="开始"/>'
	    			+ '<input type="button" onclick="lu_pause();" class="lu_input" value="暂停"/>'
	    			+ '<input type="button" onclick="lu_stop();" class="lu_input" value="停止"/>';
	    		jQuery("#control").html(div);
	    		/* var attr = new Array();
	    		for(var  i = 0 ; i < mapPoints.length-1; i++){
	    			var distance = map.getDistance(mapPoints[i],mapPoints[i+1]);
	    			//if(distance > 100) alert(distance);
	    			if( 5 < distance && distance < 6 ){
	    				attr.push(mapPoints[i]);
	    			}
	    		} */
	    		lushu = new BMapLib.LuShu(map,mapPoints,{
	    			defaultContent:"",//"从天安门到百度大厦"
	    			autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
	    			icon  : new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)}),
	    			speed: 100,
	    			enableRotation:true,//是否设置marker随着道路的走向进行旋转
	    			landmarkPois: []
	    		});
	    		
	    		lushu.start();
	    	} else if(mapPoints.length == 1) {
	    		var marker = new BMap.Marker(mapPoints[0]);  // 创建标注
				map.addOverlay(marker);
	    	} else {
	    		alert("没有轨迹信息");
	    	}
			map.centerAndZoom(mapPoints[0], map.getZoom());
		}
		
		function clearArray(arr) {
			for(var i=0;i<arr.length;i++) {
				arr.remove(i);
			}
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
			t1 = true;
		}
		
		function lu_pause() {
			if(lushu == null || lushu == undefined) {
				alert("还没有路线");
				return;
			} else {
				lushu.pause();
			}
		}
		
		var t1 = true;
		var isShowInfo = false;
		var ptimer;//10秒查询一次位置信息
		//定时查询位置信息
		function flushPosition(){
			//console.info(1)
			var val = jQuery("#userList").find(":checked");
			if(val != undefined && t1) {
				remarkPosition(val);
			}
			window.clearTimeout(ptimer);
			ptimer=setTimeout(flushPosition,10000); 
		}
		
		//flushPosition();
		
		function set_showInfo(){
			if(isShowInfo) {
				jQuery("#info_control").val("打开提示");
				isShowInfo = false;
			} else {
				jQuery("#info_control").val("关闭提示");
				isShowInfo = true;
			}
		}
		
		function showCenter(point){
			var bs = map.getBounds();   //获取可视区域
			var bssw = bs.getSouthWest();   //可视区域左下角
			var bsne = bs.getNorthEast();   //可视区域右上角
			if(bssw.lng > point.lng || point.lng >  bsne.lng || bssw.lat > point.lat || point.lat >  bsne.lat){
				map.centerAndZoom(point, map.getZoom());
			}
		}
		
		Date.prototype.format = function(format) {
		       var date = {
		              "M+": this.getMonth() + 1,
		              "d+": this.getDate(),
		              "H+": this.getHours(),
		              "m+": this.getMinutes(),
		              "s+": this.getSeconds(),
		              "q+": Math.floor((this.getMonth() + 3) / 3),
		              "S+": this.getMilliseconds()
		       };
		       if (/(y+)/i.test(format)) {
		              format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
		       }
		       for (var k in date) {
		              if (new RegExp("(" + k + ")").test(format)) {
		                     format = format.replace(RegExp.$1, RegExp.$1.length == 1
		                            ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
		              }
		       }
		       return format;
		}
		
		remarkPosition();
	</script>
</body>
</html>