<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Insert title here</title>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<script src="http://api.map.baidu.com/api?v=2.0&ak=rTbzYWazzcbZDP2NUEbff6zo"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
<script type="text/javascript">
/* function $(element){
	jQuery.noConflict();
} */
var arr=new Array();
var dataTrackStr=JSON.parse('${dataTrackStr}');
var poit=JSON.parse('${poinListStr}');
for(var i=0;i<poit.length;i++){
	//console.log(poit[i].x+"|"+poit[i].y);
	arr.push(new BMap.Point(poit[i].x,poit[i].y));
}
function setLine(list) {
	gspPoints.length = 0;
	mapPoints.length = 0;
	/* for(var i=0;i<list.length;i++) {
		var point = new BMap.Point(list[i].longitude, list[i].latitude);
		gspPoints.push(point);
	} */
	for(var i=0;i<arr.length;i++){
		gspPoints.push(arr[i]);
	}
	changePosition2();
}

var PositionUrl = 'http://api.map.baidu.com/geoconv/v1/?';
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

            script.onload = script.onreadystatechange = null;
            if ( head && script.parentNode ) {
                head.removeChild( script );
            }
        }
    };
    head.insertBefore( script, head.firstChild );
}



</script>
</head>
<body style="width: 100%;height: 900px ">
	<div id="map_canvas" style="height: 100%;width: 100% "></div>
	
</body>
</html>
<script type="text/javascript">
var map = new BMap.Map('map_canvas');
map.enableScrollWheelZoom();
//map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
map.centerAndZoom("广州 ", 11);

// 添加带有定位的导航控件
var navigationControl = new BMap.NavigationControl({
  // 靠左上角位置
  anchor: BMAP_ANCHOR_TOP_LEFT,
  // LARGE类型
  type: BMAP_NAVIGATION_CONTROL_LARGE,
  // 启用显示定位
  enableGeolocation: true
});
map.addControl(navigationControl);
// 添加定位控件
var geolocationControl = new BMap.GeolocationControl();
geolocationControl.addEventListener("locationSuccess", function(e){
  // 定位成功事件
  var address = '';
  address += e.addressComponent.province;
  address += e.addressComponent.city;
  address += e.addressComponent.district;
  address += e.addressComponent.street;
  address += e.addressComponent.streetNumber;
});
geolocationControl.addEventListener("locationError",function(e){
  // 定位失败事件
  alert(e.message);
});
map.addControl(geolocationControl);
//添加返回控件
// 定义一个控件类,即function
	function ZoomControl(){
	  // 默认停靠位置和偏移量
	  this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
	  this.defaultOffset = new BMap.Size(70, 10);
	}

	// 通过JavaScript的prototype属性继承于BMap.Control
	ZoomControl.prototype = new BMap.Control();

	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
	ZoomControl.prototype.initialize = function(map){
	  // 创建一个DOM元素
	  var div = document.createElement("div");
	  // 添加文字说明
	  div.appendChild(document.createTextNode("返回 "));
	  // 设置样式
	  div.style.cursor = "pointer";
	 // div.style.border = "0px solid gray";
	 // div.style.backgroundColor = "white";
	  // 绑定事件,点击一次放大两级
	  div.onclick = function(e){
		 var obdSn=dataTrackStr[0].obdSn;
		 var insesrtTime=dataTrackStr[0].insesrtTime;
		 window.location.href='${basePath}/admin/carOwnerMarketing_trackData.do?obdSn='+obdSn+'&insesrtTime='+insesrtTime;
	  };
	  // 添加DOM元素到地图中
	  map.getContainer().appendChild(div);
	  // 将DOM元素返回
	  return div;
	};
	// 创建控件
	var myZoomCtrl = new ZoomControl();
	// 添加到地图当中
	map.addControl(myZoomCtrl);
function showLine() {
	if(mapPoints.length > 1) {
		polyline = new BMap.Polyline(mapPoints); //创建折线
		map.addOverlay(polyline); //绘制折线  
		var div = '<div id="controls">'
			+ '<input type="button" onclick="lu_start();" class="lu_input" value="开始"/>'
			+ '<input type="button" onclick="lu_pause();" class="lu_input" value="暂停"/>'
			+ '<input type="button" onclick="lu_stop();" class="lu_input" value="停止"/></div>';
		//jQuery("#controls").html(div);
		lushu = new BMapLib.LuShu(map,mapPoints,{
			defaultContent:"",//"从天安门到百度大厦"
			autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
			icon  : new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)}),
			speed: 100,
			enableRotation:true,//是否设置marker随着道路的走向进行旋转
			landmarkPois: []
		}).start();
	} else if(mapPoints.length == 1) {
		var marker = new BMap.Marker(mapPoints[0]);  // 创建标注
		map.addOverlay(marker);
	} else {
		alert("没有轨迹信息");
	}
	map.centerAndZoom(mapPoints[0], 15);
}
setLine(2);
</script>