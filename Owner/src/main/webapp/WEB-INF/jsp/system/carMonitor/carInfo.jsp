<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>车辆信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
   <script type="text/javascript" src="${basePath }/js/hightcharts/jquery-1.8.3.min.js">	</script>
    <script type="text/javascript" src="${basePath }/js/hightcharts/highcharts.js">	</script>
    <script type="text/javascript" src="${basePath }/js/hightcharts/exporting.js">	</script>
   <script type="text/javascript" src="${basePath }/js/hightcharts/highcharts-more.js">	</script>
	<script>
    //左侧Javascript代码
	$(function () {
		var speed = 0;
		var rotationalSpeed = 0;
		var temperature = 40;
		jQuery.ajax({
			url:'${basePath}/admin/carPosition_carInfo.do',
			data:{'obdSn':'${obdSn}'},
			type:"POST",
			async: false,
			success:function(data) {
				if(data.code == 0){
					//alert(data.result.temperature);
					speed = data.result.speed;
					temperature = data.result.temperature;
					rotationalSpeed = data.result.rotationalSpeed;
					//alert(speed);
				}else{
					//alert(data.desc);
				}
			},error:function() {
				alert("请求错误");
			}
		});
	
	/*车速*/
    $('#speed').highcharts({
	    chart: {
	        type: 'gauge',
	        plotBackgroundColor: null,
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    credits:{
	    	enabled:false
	    },
	    exporting:{
	    	enabled:false
	    },
	    title: {
	        text: '最高车速',
	        verticalAlign: 'bottom'
	    },
	  
	    pane: {
	        startAngle: -150,
	        endAngle: 150,
	        background: [{
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
	                ]
	            },
	            borderWidth: 0,
	            outerRadius: '109%'
	        }, {
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
	                ]
	            },
	            borderWidth: 1,
	            outerRadius: '107%'
	        }, {
	            // default background
	        }, {
	            backgroundColor: '#DDD',
	            borderWidth: 0,
	            outerRadius: '105%',
	            innerRadius: '103%'
	        }]
	    },
	       
	    // the value axis
	    yAxis: {
	        min: 0,
	        max: 200,
	        
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        minorTickPosition: 'inside',
	        minorTickColor: '#666',
	
	        tickPixelInterval: 30,
	        tickWidth: 2,
	        tickPosition: 'inside',
	        tickLength: 10,
	        tickColor: '#666',
	        labels: {
	            step: 2,
	            rotation: 'auto'
	        },
	        title: {
	            text: 'km/h'
	        },
	        plotBands: [{
	            from: 0,
	            to: 120,
	            color: '#55BF3B' // green
	        }, {
	            from: 120,
	            to: 160,
	            color: '#DDDF0D' // yellow
	        }, {
	            from: 160,
	            to: 200,
	            color: '#DF5353' // red
	        }]        
	    },
	
	    series: [{
	        name: '车速',
	        data: [speed],
	        tooltip: {
	            valueSuffix: ' km/h'
	        }
	    }]
	
	}, 
	// Add some life
	function (chart) {
		if (!chart.renderer.forExport) {/*
		    setInterval(function () {
		        var point = chart.series[0].points[0],
		            newVal,
		            inc = Math.round((Math.random() - 0.5) * 20);
		        
		        newVal = point.y + inc;
		        if (newVal < 0 || newVal > 200) {
		            newVal = point.y - inc;
		        }
		        
		        point.update(newVal);
		        
		    }, 3000);*/
		}
	});
	

    /*温度*/
    $('#temperature').highcharts({
	
	    chart: {
	        type: 'gauge',
	        plotBackgroundColor: null,
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    credits:{
	    	enabled:false
	    },
	    exporting:{
	    	enabled:false
	    },
	    title: {
	        text: '最高温度',
	        verticalAlign: 'bottom'
	    },
	    
	    pane: {
	        startAngle: -60,
	        endAngle: 60,
	        background: [{
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
	                ]
	            },
	            borderWidth: 0,
	            outerRadius: '109%'
	        }, {
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
	                ]
	            },
	            borderWidth: 1,
	            outerRadius: '107%'
	        }, {
	            // default background
	        }, {
	            backgroundColor: '#DDD',
	            borderWidth: 0,
	            outerRadius: '105%',
	            innerRadius: '103%'
	        }]
	    },
	       
	    // the value axis
	    yAxis: {
	        min: 40,
	        max: 120,
	        
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        minorTickPosition: 'inside',
	        minorTickColor: '#666',
	
	        tickPixelInterval: 30,
	        tickWidth: 2,
	        tickPosition: 'inside',
	        tickLength: 10,
	        tickColor: '#666',
	        labels: {
	            step: 2,
	            rotation: 'auto'
	        },
	        title: {
	            text: '°C'
	        },
	        plotBands: [{
	            from: 0,
	            to: 40,
	            zIndex: 38,
	            color: '#55BF3B' // green
	        }, {
	            from: 40,
	            to: 100,
	            color: '#55BF3B' // green
	        }, {
	            from: 100,
	            to: 120,
	            color: '#DF5353' // yellow
	        }]        
	    },
	
	    series: [{
	        name: '温度',
	        data: [temperature],
	        tooltip: {
	            valueSuffix: ' °C'
	        }
	    }]
	
	}, 
	// Add some life
	function (chart) {
		if (!chart.renderer.forExport) {/*
		    setInterval(function () {
		        var point = chart.series[0].points[0],
		            newVal,
		            inc = Math.round((Math.random() - 0.5) * 20);
		        
		        newVal = point.y + inc;
		        if (newVal < 0 || newVal > 200) {
		            newVal = point.y - inc;
		        }
		        
		        point.update(newVal);
		        
		    }, 3000);*/
		}
	});
    
    /*转速*/
    $('#rotationalSpeed').highcharts({
    	
	    chart: {
	        type: 'gauge',
	        plotBackgroundColor: null,
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    
	    title: {
	        text: '最高转速',
	        verticalAlign: 'bottom'
	    },
	    credits:{
	    	enabled:false
	    },
	    exporting:{
	    	enabled:false
	    },
	    pane: {
	        startAngle: -150,
	        endAngle: 150,
	        background: [{
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
	                ]
	            },
	            borderWidth: 0,
	            outerRadius: '109%'
	        }, {
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
	                ]
	            },
	            borderWidth: 1,
	            outerRadius: '107%'
	        }, {
	            // default background
	        }, {
	            backgroundColor: '#DDD',
	            borderWidth: 0,
	            outerRadius: '105%',
	            innerRadius: '103%'
	        }]
	    },
	       
	    // the value axis
	    yAxis: {
	        min: 0,
	        max: 8,
	        
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        minorTickPosition: 'inside',
	        minorTickColor: '#666',
	
	        tickPixelInterval: 30,
	        tickWidth: 2,
	        tickPosition: 'inside',
	        tickLength: 10,
	        tickColor: '#666',
	        labels: {
	            step: 4,
	            rotation: 'auto'
	        },
	        title: {
	            text: 'kr/min'
	        },
	        plotBands: [/*{
	            from: 40,
	            to: 100,
	            color: '#55BF3B' // green
	        }, */{
	            from: 6.5,
	            to: 8,
	            color: '#DF5353' // yellow
	        }]        
	    },
	
	    series: [{
	        name: '转速',
	        data: [rotationalSpeed/1000],
	        tooltip: {
	            valueSuffix: ' kr/min'
	        }
	    }]
	
	}, 
	// Add some life
	function (chart) {
		if (!chart.renderer.forExport) {/*
		    setInterval(function () {
		        var point = chart.series[0].points[0],
		            newVal,
		            inc = Math.round((Math.random() - 0.5) * 20);
		        
		        newVal = point.y + inc;
		        if (newVal < 0 || newVal > 200) {
		            newVal = point.y - inc;
		        }
		        
		        point.update(newVal);
		        
		    }, 3000);*/
		}
	});
});		
    
  </script>
   <script type="text/javascript">
	$(function(){
		
	});
	
	</script>
	<style type="text/css">
	#icon_list table tr td {
		vertical-align: middle;
		padding-top: 10px;
		padding-bottom: 10px;
	}
	.icon {
		text-align: right;
		padding-right: 10px;
	}
	.text {
		text-align: left;
		padding-left: 10px;
	}
	</style>
  </head>
  
  <body>
  <div align="center" style="margin:0 auto;width: 100%;height: 550px;background-color: #FFF">
	<div id="speed" style="width:33%;height:280px;display:inline;float:left;left: 30px"></div>
	<div id="temperature" style="width:33%;height:280px;display:inline;float:left;left: 30px"></div>
	<div id="rotationalSpeed" style="width:33%;height:280px;display:inline;float:left;left: 30px"></div>
	<div id="icon_list" style="float:left;width: 100%;padding-top: 15px;" align="center">
		<table width="90%">
			<tr>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_10.png" /></td>
				<td class="text">左转向灯</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_12.png" /></td>
				<td class="text">右转向灯</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_14.png" /></td>
				<td class="text">雾灯</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_16.png" /></td>
				<td class="text">近光灯</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_18.png" /></td>
				<td class="text">远光灯</td>
			</tr>
			<tr>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_35.png" /></td>
				<td class="text">小灯</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_26.png" /></td>
				<td class="text">空调</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_28.png" /></td>
				<td class="text">雨刮</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_30.png" /></td>
				<td class="text">档位</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_32.png" /></td>
				<td class="text">车锁</td>
			</tr>
			<tr>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_42.png" /></td>
				<td class="text">驻车</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_45.png" /></td>
				<td class="text">刹车</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_49.png" /></td>
				<td class="text">危险灯</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_52.png" /></td>
				<td class="text">安全带</td>
				<td class="icon"><img src="${basePath }/images/carinfo/icon_55.png" /></td>
				<td class="text">油门</td>
			</tr>
		</table>
	</div>
  </div>
  </body>
</html>
