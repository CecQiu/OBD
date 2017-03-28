<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<jsp:include page="../../include/common.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="${basePath}/js/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/jquery.md5.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/coordinateConversion.js" charset="UTF-8"></script>
<style type="text/css">
	body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
	.lu_input{
		background-color: white;
	    padding-left: 8px;
	    padding-right: 8px;
	    border-color: #9999FF;
	}
	#users{
		position: absolute;
    	width: 15%;
    	height: 100%;
    	right: 0px;
    	top: 0px;
	}
</style>

<link rel="stylesheet" type="text/css" href="${basePath}/css/bootstrap.min.css" />
<!--[if IE 7]>
<link rel="stylesheet" href="${basePath}/css/plugins/font-awesome/css/font-awesome-ie7.min.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=rTbzYWazzcbZDP2NUEbff6zo"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
<title>电子围栏</title>
</head>
<body style="height:1100px;">
	<div id="map" style="height:70%;width: 80%;"></div>
	<div id="users" style="position: absolute;width:20%;height: 70%;float: right;overflow-x: scroll;overflow-y: scroll;">	
		<div>
			位置:<input type="text" id="address"/>
			<input class="btn btn-primary pull-center" type="button" value="搜索" onclick="searchPoint()"/>
		</div>
		<div>
			<input class="btn btn-primary pull-center" type="button" value="获取围栏顶点" onclick="getPoint()"/>
			<br/>
			<br/>
			<input class="btn btn-primary pull-center" type="button" value="清除所有覆盖物" onclick="clearAll()"/>
			<!-- <input type="button" value="获取绘制的覆盖物个数" onclick="alert(overlays.length)"/> -->
		</div>
		<br/>
		<h3>绘制围栏获取坐标</h3>
		百度坐标:
		<div id="ps1"></div>
		<br/>
		<br/>
		gps坐标坐标:
		<div id="ps2"></div>
		<br/>
		<br/>
		<div id="ps3">
			<h3>鼠标右键获取百度坐标</h3>
			百度坐标:<br/>
			<span  id="bdP1"></span><br/>
			转gps坐标:<br/>
			<span id="gpsP"></span><br/>
			转百度坐标:<br/>
			<span id="bdP2"></span>
		</div>
		<br/>
		<input class="btn btn-primary pull-center" type="button" value="返 回" onclick="javascript:window.history.back('-1');"/>&nbsp;
	</div>
	<div id="" style="height:20%;width: 100%;overflow-y: scroll;">
		<form name="f25" id="f25">
			<h4>多区域电子围栏</h4>
			<table border="1px" style="text-align: left; margin: auto;">
				<tr>
					<td>表面码列表(英文逗号分隔):</td>
					<td colspan="3"><input type="text" id="obdMsnList25"
						style="width: 100%;" value='${snos}' readonly="readonly"/></td>
				</tr>
				<tr>
					<td>类型:</td>
					<td><select id="type25" style="width: 200px;">
							<!-- <option value=""></option> -->
							<option value="1">新增围栏</option>
							<option value="2">取消围栏</option>
							<option value="3">取消所有围栏</option>
							<option value="4">暂停使用围栏</option>
							<option value="5">恢复使用围栏</option>
					</select></td>
					<td>报警方式:</td>
					<td><select id="alert25" style="width: 200px;">
							<!-- <option value=""></option> -->
							<option value="1">进区域报警</option>
							<option value="2">出区域报警</option>
							<option value="3">进出都报警</option>
					</select></td>
				</tr>
				<tr>
					<td>围栏顶点</td>
					<td colspan="3"><input type="text" id="points25" value="" style="width: 100%;" readonly="readonly"/></td>
				</tr>
				<tr id="f14_2">
					<td>定时类型:</td>
					<td><select id="timerType25" style="width: 200px;">
							<option value="1">星期几</option>
							<option value="2">每天</option>
							<option value="3">日期范围</option>
					</select></td>
					<td>星期几:</td>
					<td>
						星期一:<input type="checkbox" name="dayWeek" id="dayWeek1" value="2"/> &nbsp; 
						星期二:<input type="checkbox" name="dayWeek" id="dayWeek2" value="3"/> &nbsp; 
						星期三:<input type="checkbox" name="dayWeek" id="dayWeek3" value="4"/> &nbsp; 
						星期四:<input type="checkbox" name="dayWeek" id="dayWeek4" value="5"/> &nbsp; 
						星期五:<input type="checkbox" name="dayWeek" id="dayWeek5" value="6"/> &nbsp; 
						星期六:<input type="checkbox" name="dayWeek" id="dayWeek6" value="7"/> &nbsp; 
						星期日:<input type="checkbox" name="dayWeek" id="dayWeek7" value="1"/> &nbsp; 
					</td>
				</tr>
				<tr>
					<th>定时开始时间</th>
					<td class="pn-fcontent"><input type="text" class="Wdate"
						readonly="readonly" id="startTime25"
						onclick="WdatePicker({dateFmt: 'HH:mm:ss'})" /></td>
					<th>定时结束时间</th>
					<td class="pn-fcontent"><input type="text" class="Wdate"
						readonly="readonly" id="endTime25"
						onclick="WdatePicker({dateFmt: 'HH:mm:ss'})" /></td>
				</tr>
				<tr>
					<th>定时开始日期</th>
					<td class="pn-fcontent"><input type="text" class="Wdate"
						readonly="readonly" id="startDate25"
						onclick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" /></td>
					<th>定时结束日期</th>
					<td class="pn-fcontent"><input type="text" class="Wdate"
						readonly="readonly" id="endDate25"
						onclick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" /></td>
				</tr>
				<tr>
					<td>区域编号</td>
					<td><input type="text" id="areaNum25" value="1" /></td>
					<td>操作</td>
					<td><input class="btn btn-s-md btn-success" type="button"
						value="确定" onclick="fenceSet()" /></td>
				</tr>
			</table>
		</form>
	</div>
	<h1>返回报文</h1>
	<div id="showMsg" style="color: red;height: 90px;"></div>
	<script type="text/javascript">

	var polygon;
	// 百度地图API功能
    var map = new BMap.Map('map');
    var poi = new BMap.Point(113.352249,23.171118);
    map.centerAndZoom(poi, 19);//设置中心点
    map.enableScrollWheelZoom(); //允许鼠标缩放
   /*  设置中心点为该城市中心
   var myCity = new BMap.LocalCity();
    var points = new Array();
	myCity.get(function(result){
		var cityName = result.name;
		map.setCenter(cityName);
	}); 
	*/
	
	var overlays = [];//围栏个数
    
	var overlaycomplete = function(e){
        overlays.push(e.overlay); 
    };
    
    var styleOptions = {
        strokeColor:"red",    //边线颜色。
        fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 1,       //边线的宽度，以像素为单位。
        strokeOpacity: 1,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.2,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    }
    
 	//实例化鼠标绘制工具
    var drawingManager = new BMapLib.DrawingManager(map, {
        isOpen: false, //是否开启绘制模式
        enableDrawingTool: true, //是否显示工具栏
        drawingToolOptions: {
            anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
            offset: new BMap.Size(5, 5), //偏离值
            drawingModes:[BMAP_DRAWING_RECTANGLE,BMAP_DRAWING_POLYGON]
        },
        spolygonOptions: styleOptions, //多边形的样式
        rectangleOptions: styleOptions //矩形的样式
    });  
    
    //添加鼠标绘制工具监听事件，用于获取绘制结果
    drawingManager.addEventListener('overlaycomplete', overlaycomplete);
    
    //删除所有覆盖物
    function clearAll() {
    	if(overlays.length==0){
    		return false;
    	}
    	$("#ps1").html("");
		$("#ps2").html("");
		$("#points25").val("");//form表单
		for(var i = 0; i < overlays.length; i++){
            map.removeOverlay(overlays[i]);
        }
        overlays.length = 0   
    }
    
    //获取围栏顶点坐标
    function getPoint(){
		$("#ps1").html("");
		$("#ps2").html("");
		$("#points25").val("");//form表单
		var gpsPoints='';//gps坐标
		if(overlays.length!=1){
			alert("请绘制单个围栏.");
			return;
		}
		for(var i = 0; i < overlays.length; i++){
			var overlay=overlays[i].getPath();
			if(overlay.length<3){
				alert("请绘制3个顶点以上的围栏.");
				return false;
			}
			
			var p1 = overlay.length+":边形<br/>";//围栏顺序顶点坐标
			
			for(var j = 0; j < overlay.length; j++){
				var grid =overlay[j];
				//百度地图坐标
				p1+=(j+1)+"个点:('lng':"+grid.lng+",lat':"+grid.lat+");<br/>";
				//百度坐标转BD-09——》GCJ-02
				var gcjObj = GPS.bd_decrypt(grid.lat,grid.lng);
				//GCJ-02——》WGS-84
				var obj = GPS.gcj_decrypt(gcjObj.lat,gcjObj.lon);
				var p = "{'longitude':'"+obj.lon+"','latitude':'"+obj.lat+"'},";
				gpsPoints +=p;
			}
			$("#ps1").html(p1);
        }
		gpsPoints="["+gpsPoints.substring(0, gpsPoints.length - 1)+"]";
		$("#ps2").html(gpsPoints);
		$("#points25").val(gpsPoints);
	}
    
    
   //增加鼠标右击事件，查看地图坐标点
	map.addEventListener("rightclick", function(e) {
		$("#bdP1").html(e.point.lng + "," + e.point.lat);
		//百度坐标转BD-09——》GCJ-02——》转成gps坐标
		var gcjObj = GPS.bd_decrypt(e.point.lat,e.point.lng);
		//GCJ-02——》WGS-84
		var obj = GPS.gcj_decrypt(gcjObj.lat,gcjObj.lon);
		$("#gpsP").html(obj.lon+","+obj.lat);
		//console.info(obj);
		//gps坐标转GCJ-02坐标
		var gcj2Obj=GPS.gcj_encrypt(obj.lat,obj.lon);
		//GCJ-02坐标转百度坐标
		var bd2Obj=GPS.bd_encrypt(gcj2Obj.lat,gcj2Obj.lon);
		$("#bdP2").html(bd2Obj.lon+","+bd2Obj.lat);
	});
		//实例化鼠标绘制工具
		/**
		 *"isOpen" : {Boolean} 是否开启绘制模式 
		 "enableDrawingTool" : {Boolean} 是否添加绘制工具栏控件，默认不添加 
		 "drawingToolOptions" : {Json Object} 可选的输入参数，非必填项。可输入选项包括 
		 "anchor" : {ControlAnchor} 停靠位置、默认左上角 
		 "offset" : {Size} 偏移值。 
		 "scale" : {Number} 工具栏的缩放比例,默认为1 
		 "drawingModes" : {DrawingType} 工具栏上可以选择出现的绘制模式,将需要显示的DrawingType以数组型形式传入，如[BMAP_DRAWING_MARKER, BMAP_DRAWING_CIRCLE,BMAP_DRAWING_POLYGON] 将只显示画点和画圆的选项 
		 "enableCalculate" : {Boolean} 绘制是否进行测距(画线时候)、测面(画圆、多边形、矩形) 
		 "markerOptions" : {CircleOptions} 所画的点的可选参数，参考api中的对应类 
		 "circleOptions" : {CircleOptions} 所画的圆的可选参数，参考api中的对应类 
		 "polylineOptions" : {CircleOptions} 所画的线的可选参数，参考api中的对应类 
		 "polygonOptions" : {PolygonOptions} 所画的多边形的可选参数，参考api中的对应类 
		 "rectangleOptions" : {PolygonOptions} 
		 */
	</script>
<script type="text/javascript">
function fenceSet(){
	var username = 'chezhutong';//权限校验
	var time='1447134959847';//时间
	var pwd = 'czt123456';
	var deviceId = '88888888';
	
	var sign = $.md5(deviceId+username+time+pwd);
	var type = $("#type25").val();//类型
	var obdMsnList = $("#obdMsnList25").val();//类型
	var areaNum = $("#areaNum25").val();
	var points = $("#points25").val();
	var alert25 = $("#alert25").val();//告警方式
	var timerType = $("#timerType25").val();//定时规则
	var dayWeek = "";
	$('input[name="dayWeek"]:checked').each(function(){
		dayWeek+=$(this).val()+",";
	}); 
	var startDate = $("#startDate25").val();
	var endDate = $("#endDate25").val();
	var startTime = $("#startTime25").val();
	var endTime = $("#endTime25").val();
	if(type=='' || type == undefined || type == null){
		alert("请选择类型.");
		return false;
	}
	
	if(obdMsnList==''){
		alert("表面码不能为空.");
		return false;
	}
	
	if(areaNum!=''){
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test(areaNum)){
			alert("围栏编号必须为阿拉伯数字.");
			return false;
		}
	}
	
	if(dayWeek!=""){
		dayWeek = dayWeek.substring(0,dayWeek.length-1);
	}
	
	$.ajax({
		url:'${basePath}/api/obd/fenceSet',
		data:{
			'deviceId':deviceId,
			'username':username,
			'time':time,
			'sign':sign,
			'type':type,
			'obdMsnList':obdMsnList,
			'areaNum':areaNum,
			'points':points,
			'alert':alert25,
			'timerType':timerType,
			'dayWeek':dayWeek,
			'startDate':startDate,
			'endDate':endDate,
			'startTime':startTime,
			'endTime':endTime
		},
		type:'post',
		cache : false,
		async : false,
		dataType :'json',
		success:function(data){
			var obj = eval(data); 
			$("#showMsg").html(JSON.stringify(obj));
		},
		error : function(){
			alert("异常,请稍后重试.");
		}
	});
}

function searchPoint(){
	var add= $("#address").val();
	if(add.trim==''){
		alert("请输入查询位置.");
		return false;
	}
	map.centerAndZoom(add,19);
}
</script>
</body>
</html>
