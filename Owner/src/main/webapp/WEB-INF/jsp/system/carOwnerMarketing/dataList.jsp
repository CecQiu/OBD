<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>营销管理 -业务统计</title>
<jsp:include page="../../include/common.jsp" />
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link id="css_main" rel="stylesheet" type="text/css" href="${basePath}/theme/${session.theme}/main.css" />
<style type="text/css">
h3 {
	margin-bottom: 20px;
	text-align: center;
}

.page-content-wrapper {
	width: 75%;
	margin: 0 auto 35px;
	height: 50px;
	background: #f2f2f2;
	border-radius: 10px;
	text-align: center;
	color: #626262;
	line-height: 50px;
	border: 1px solid #cacaca;
}

.midnav {
	width: 80%;
	text-align: center;
	margin: 0 auto 30px;
}

.midnav li {
	min-width: 22%;
	margin: 17px 10px 0px;
	position: relative;
	display: inline-block;
	text-align: center;
}

.midnav li a {
	padding: 10px 9px 5px;
	display: block;
	font-weight: bold;
	outline: none;
	white-space: nowrap;
	color: #626262;
	border-radius: 3px;
	text-shadow: 0px 1px #FFF;
	border: 1px solid #D5D5D5;
	background: -moz-linear-gradient(center top, #FFF 0%, #F4F4F4 100%)
		repeat scroll 0% 0% transparent;
	box-shadow: 0px 1px 2px #EEE;
	margin-bottom: 20px;
}

.midnav li a.active {
	border: 1px solid #999;
}

.midnav li img {
	padding-right: 10px;
}
</style>
<script src="http://api.map.baidu.com/api?v=2.0&ak=rTbzYWazzcbZDP2NUEbff6zo" />
<script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js" />
<script async src="http://c.cnzz.com/core.php"></script>
<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
<script language="javascript" src="${basePath}/js/curentTime.js" />
<script language="javascript">
function forwardUrl(uri) {
	window.location.href = uri;
}

String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) { 
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) { 
	return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith); 
	} else { 
	return this.replace(reallyDo, replaceWith); 
	} 
	} 
	
/* $(document).ready(function() {
	alert("第二种方法"); 
});
 */
</script>
<style>
.t {
	background-image: url("${basePath}/images/point_end.png");
	float: right;
	/* background-position: -102px -50px ;*/
	background-repeat: no-repeat;
}

.tt {
	background-image: url("${basePath}/images/point_start.png");
	float: left;
	/* background-position: -182px -52px ; */
	background-repeat: no-repeat;
}
</style>
</head>
<body>
	<form id="myForm" class="myForm"
		action="${basePath}/admin/carOwnerMarketing_trackData.do"
		method="post">
		<div style="display:none;">
			<input type="text" name="obdSn" value="<s:property value="obdSn"/>"
				readonly="readonly" /> <input type="text" value="${insesrtTime}"
				readonly="readonly" name="insesrtTime" />
		</div>
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：营销管理 &gt; 行程记录信息</li>
		</ul>
		<h3>
			<c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if>
		</h3>
		<div class="widget">
			<div class="widget-content" style="line-height: 20px"></div>
			<div class="widget-content">

				<s:iterator value="dataTrack" status="st">
					<div style="height: 90px; width: 100%;">
						设备号:&nbsp;<span><s:property value="userName"></s:property></span>&nbsp;&nbsp;&nbsp;
						时间:&nbsp;<span><fmt:formatDate value='${travelStart }' pattern='yyyy-MM-dd HH:mm:ss' /></span>&nbsp;至&nbsp;
						<span><fmt:formatDate value='${travelEnd }' pattern='yyyy-MM-dd HH:mm:ss' /></span>
						
						<div class="tt" style="width: 50px; height: 80px;">
							<span class="star" style="position: absolute; margin: 32px 0 0 40px;" id='<s:property value="id"/>_sp'>
								<s:property value="starPoint"></s:property>
							</span>
						</div>

						<div class="t" style="width: 50px; height: 82px;">
							<span class="end" style="position: absolute; right: 60px; margin-top: 33px;" id='<s:property value="id"/>_ep'>
								<s:property value="endPoint"></s:property>
							</span>
						</div>

						<div
							style="width: 100%; background-color: green; height: 14px; margin: 32px 0 2px 0; position: absolute;">
						</div>

						<div align="center"
							style="width: 100%; height: 14px; margin: 50px 0 2px 0; position: absolute; color: #2a6496;">
							行程
							 <fmt:formatNumber value="${distance/100}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>
							km, 时长
							<s:property value="totalTime"></s:property>
							, 油耗
							<s:property value="totalFuel"></s:property>
							L, 平均速度
							 <fmt:formatNumber value="${avgSpeed/100}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>
							km/h
						</div>
					</div>
					<div>
						<button type="button" style="float: right; margin-bottom: 15px;"
							onclick="window.location.href='${basePath}/admin/carOwnerMarketing_poit.do?starTime=<fmt:formatDate value='${travelStart }' pattern='yyyy-MM-dd HH:mm:ss'/>&endTime=<fmt:formatDate value='${travelEnd }' pattern='yyyy-MM-dd HH:mm:ss'/>&obdSn=<s:property value="obdSn"></s:property>&insesrtTime=<s:property value="insesrtTime"></s:property>'">
							查看轨迹</button>
					</div>
					<div
						style="border-top: 1px solid #808069; margin-bottom: 10px; clear: both;"></div>
				</s:iterator>
				<div class="widget-bottom">
					<jsp:include page="../../include/pager.jsp" />
				</div>
				<div class="widget-bottom">
					&nbsp;<span class="pull-right">版权所有：广州华工信息软件有限公司</span>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			/* function $(element){
				jQuery.noConflict();
			}  */
			
			window.onload=function(){ 
				var tracks=eval(${dataTrackJson}); //获取后台返回的json对象
				//alert(tracks.length);
				for(var i=0;i<tracks.length;i++){
					var id=tracks[i].id;
					var obdSN=tracks[i].obdSn;
					var startLongitude = tracks[i].startLongitude;
					var startLatitude = tracks[i].startLatitude;
					var endLongitude = tracks[i].endLongitude;
					var endLatitude = tracks[i].endLatitude;
					var starPoint = tracks[i].starPoint;
					var endPoint = tracks[i].endPoint;
					//alert(id+"***"+obdSN+"***"+startLongitude+"***"+startLatitude+"***"+endLongitude+"***"+endLatitude+"***"+starPoint+"***"+endPoint);
					if(starPoint===""){
						getLngAndLat(id+"_sp",startLongitude,startLatitude);
					}
					if(endPoint===""){
						getLngAndLat(id+"_ep",endLongitude,endLatitude);
					}
				}
			}; 
			//调用百度地图
			function getLngAndLat(id,longitude,latitude){
				//alert(id+"**"+longitude+"**"+latitude);
				var ggPoint = new BMap.Point(longitude,latitude);
				BMap.Convertor.translate(ggPoint,0,function(point){
					var id2 = id;
					getAddress(id2,point);
				});
			}
			
			//调用百度地图
			function getAddress(id,point){
				var id2 = id;
				//alert(point.lng + "," + point.lat);
		    	var gc = new BMap.Geocoder();
				gc.getLocation(point, function(rs){
				   var addComp = rs.addressComponents;
				   var address1=addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
				   $("#"+id2).html(address1);
				   //alert(jQuery("#"+id2)+"***"+id2+"***"+address1);
				   //alert(address1)
				});
			}
		</script>
	</form>
</body>
</html>