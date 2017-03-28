<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>车主管理 - 车主查询功能</title>
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link id="css_main" rel="stylesheet" type="text/css" href="${basePath}/theme/${session.theme}/main.css" />
<script type="text/javascript" src="${basePath}/js/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/highcharts.js"></script>
<style type="text/css">

font.font_1 {
	color: #437bd0;
	font-family: 微软雅黑 Regular;
	font-weight: bold;
	font-size: 16px;
}
font.font_2 {
	color: #FFFFFF;
	font-family: 微软雅黑 Regular;
	font-size: 16px;
	font-weight: bold;
}
font.font_3 {
	font-family: Arial Bold;
	color: #FFFFFF;
	font-size: 37px;
}
font.font_4 {
	font-family: Arial Regular;
	color: #437bd0;
	font-size: 36px;
}
div.div_sty1 {
	float: left;
	width: 215px;
	height: 167px;
	margin-right: 10px;
	margin-left: 10px;
	position: relative;
	z-index: 1;
}
div.div_sty1_1 {
	width: 201px;
	height: 153px;
	z-index: 20;
	position: absolute;
	bottom:0;
	left:0;
	text-align: center;
	padding-top: 110px;
}
div.div_sty1_2 {
	width: 47px;
	height: 46px;
	line-height: 46px;
	position: absolute;
	right:0;
	top:0;
	z-index: 30;
	text-align: center;
}
ul.ul_1 li {
	text-align: left;
}
img.li_div_img {
	margin-left: 15px;
	vertical-align: middle;
}
div.li_div {
	width: 300px;
	height: 38px;
	line-height: 38px;
	text-align: left;
}
div.li_div_div {
	float:right;
	margin-top: 4px;
	margin-right: 10px;
	width: 30px;
	height: 30px;
	line-height: 30px;
	text-align: center;
}
div.little_div {
	display: inline-block;
	width: 98px;
	height: 30px;
	line-height: 30px;
}
</style>
<script language="javascript">
$(function(){
	$('#container').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '每日激活OBD设备'
        },
        subtitle: {
            text: ''
        },
        xAxis: {
        	title: {
                text: '8月'
            },
            categories: ['01','02','03','04','05','06','07','08','09','10','11','12',
                         '13','14','15','16','17','18','19','20','21','22','23','24',
                         '25','26','27','28','29','30','31']
        },
        yAxis: {
            title: {
                text: '激活数量（台）'
            },
            allowDecimals: false,
            alternateGridColor: '#FDFFD5',
            lineColor: '#C0C0C0',
            lineWidth: 1,
            floor: 0
        },
        tooltip: {
            enabled: true,
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br>'+this.x +': '+ this.y +'°C';
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        credits: {
            enabled: false
       	},
       	legend: {
            enabled: false
        },
        series: [{
            name: 'Tokyo',
            data: [0,0,0,0,0,0,0,2,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }]
    });
	
	
	$('#bzt').highcharts({
        chart: {
        	height: 180,
            type: 'pie',
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: ''
        },
        tooltip: {
    	    pointFormat: '{point.y}台'
        },
        plotOptions: {
            pie: {
            	//size: 200,
            	innerSize: 50,
                allowPointSelect: true,
                cursor: 'pointer',
                colors: ["#437bd0","#f1a437"],
                dataLabels: {
                	distance: -20,
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.percentage:.1f}</b>:  %'
                },
        		showInLegend: true
            }
        },
        credits: {
            enabled: false
       	},
       	legend: {
            enabled: false
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['已激活',   3],
                ['未激活',   1]
            ]
        }]
    });
});
</script>
</head>
<body>
<div style="height: 550px;width: 1100px;">
	<div>
		<div style="margin-bottom: 10px;padding-left: 10px;">
			<img src="${basePath}/images/ind/icon_lin.png" />
			<font class="font_1">待处理事务提醒</font>
		</div>
		<div class="div_sty1">
			<div class="div_sty1_1" style="background: url('${basePath}/images/ind/pic_0.png') no-repeat;">
				<font class="font_2">故障车辆</font>
			</div>
			<div class="div_sty1_2" style="background: url('${basePath}/images/ind/red_point_big.png') no-repeat;">
				<font class="font_3">4</font>
			</div>
		</div>
		<div class="div_sty1">
			<div class="div_sty1_1" style="background: url('${basePath}/images/ind/pic_1.png') no-repeat;">
				<font class="font_2">到期保养车辆</font>
			</div>
			<div class="div_sty1_2" style="background: url('${basePath}/images/ind/red_point_big.png') no-repeat;">
				<font class="font_3">0</font>
			</div>
		</div>
		<div class="div_sty1">
			<div class="div_sty1_1" style="background: url('${basePath}/images/ind/pic_2.png') no-repeat;">
				<font class="font_2">保养预约</font>
			</div>
			<div class="div_sty1_2" style="background: url('${basePath}/images/ind/red_point_big.png') no-repeat;">
				<font class="font_3">1</font>
			</div>
		</div>
		<div align="center" style="width: 300px;height: 153px;float: left;margin-top: 20px;margin-left: 20px;">
			<ul class="ul_1">
				<li>
					<div class="li_div" style="background: url('${basePath}/images/ind/rec.png') no-repeat;">
						<img class="li_div_img" src="${basePath}/images/ind/icon_time.png" />
						<font class="font_2" style="margin-left: 15px;">车检到期</font>
						<div class="li_div_div" style="background: url('${basePath}/images/ind/red_point_little.png') no-repeat;">
							<font class="font_2" style="font-size: 24px;">0</font>
						</div>
					</div>
				</li>
				<li style="margin-top: 15px;margin-bottom: 15px;">
					<div class="li_div" style="background: url('${basePath}/images/ind/rec.png') no-repeat;">
						<img class="li_div_img" src="${basePath}/images/ind/icon_time.png" />
						<font class="font_2" style="margin-left: 15px;">车船税到期</font>
						<div class="li_div_div" style="background: url('${basePath}/images/ind/red_point_little.png') no-repeat;">
							<font class="font_2" style="font-size: 24px;">2</font>
						</div>
					</div>
				</li>
				<li>
					<div class="li_div" style="background: url('${basePath}/images/ind/rec.png') no-repeat;">
						<img class="li_div_img" src="${basePath}/images/ind/icon_time.png" />
						<font class="font_2" style="margin-left: 15px;">年票到期</font>
						<div class="li_div_div" style="background: url('${basePath}/images/ind/red_point_little.png') no-repeat;">
							<font class="font_2" style="font-size: 24px;">1</font>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div style="clear:both;padding-left: 10px;">
		<div style="margin-bottom: 10px;padding-top: 10px;">
			<img src="${basePath}/images/ind/icon_bb.png" />
			<font class="font_1">统计报表</font>
		</div>
		<div id="container" style="width: 750px;height: 300px;float: left;"></div>
		<div style="float: left;width: 300px;height: 300px;background-color: #FFF;">
			<ul>
				<li style="text-align: center;">
					<font class="font_2" style="color: #828282;font-size: 14px;">OBD设备总数:</font>
					<font class="font_4">4</font>
					<font class="font_2" style="color: #828282;font-size: 14px;">台</font>
				</li>
				<li style="text-align: center;padding-bottom: 10px;">
					<img src="${basePath}/images/ind/ss.png" />
					<font class="font_2" style="color: #828282;font-size: 14px;">激活OBD设备比例</font>
				</li>
				<li style="text-align: center;">
					<div class="little_div" style="background: url('${basePath}/images/ind/blue.png');">
						<font class="font_2" style="font-size: 14px;font-weight: normal;">已激活：4台</font>
					</div>
					<div class="little_div" style="background: url('${basePath}/images/ind/orange.png');">
						<font class="font_2" style="font-size: 14px;font-weight: normal;">未激活：0台</font>
					</div>
				</li>
				<li style="text-align: center;">
					<div id="bzt" ></div>
				</li>
			</ul>
		</div>
	</div>
</div>
<%-- <img src="${basePath}/images/index_16.png" alt="" /> --%>
</body>
</html>