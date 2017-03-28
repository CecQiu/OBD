<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="../../include/common.jsp" />
<script src="${basePath}/js/jquery.min.js"></script>
<script src="${basePath}/js/highcharts-4.2.7/highcharts.js"></script>
<script src="${basePath}/js/highcharts-4.2.7/no-data-to-display.js"></script>
<script language="javascript">
	
</script>
</head>
<body>
<form>
<div id="selectDate">
	日期：<input type="text" value="${begin}" class="Wdate" readonly="readonly" name="begin" id="begin" 
	onclick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'2016-04-01',maxDate:'#F{$dp.$D(\'end\')}'})"/>
	-
	<input type="text" value="${end}" class="Wdate" readonly="readonly" name="end" id="end" 
	onclick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'begin\')}',maxDate:'%y-%M-{%d-1} %H:%m:%s'})"/>
	<input class="btn btn-s-md btn-success" type="button" value="查 询" onclick="ajaxData()"/>
</div><br/>
</form>
<div id="container"></div>
<script language="JavaScript">
$(document).ready(function() {
	ajaxData();
});
function ajaxData(){

	   var chart = {
	   	  renderTo : "container",
	      type: 'column'
	   };
	   var title = {
	      text: '设备上线统计'   
	   };
	   var subtitle = {
	      text: 'hgsoft'  
	   };
	   var xAxis = {
	      categories: ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31'],
	      crosshair: true
	   };
	   var yAxis = {
	      min: 0,
          allowDecimals:false,
	      title: {
	         text: '上线数量 (个)'         
	      }      
	   };
	   var tooltip = {
	      headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	      pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	         '<td style="padding:0"><b>{point.y} 个</b></td></tr>',
	      footerFormat: '</table>',
	      shared: true,
	      useHTML: true
	   };
	   var plotOptions = {
	      column: {
	         pointPadding: 0.2,
	         borderWidth: 0
	      },
	      series: {
              borderWidth: 0,
              dataLabels: {
                  enabled: true,
                  format: '{point.y}'
              }
          }
	   };  
	   var credits = {
	      enabled: false
	   };
	   var lang = {
	       noData: "暂无数据." //真正显示的文本
	   };
	   var noData = {  
	       // Custom positioning/aligning options  
	       position: {   
	           align: 'center',  
	           //verticalAlign: 'bottom'  
	       },  
	       // Custom svg attributes  
	       attr: {  
	           //stroke-width : 1,  
	           //stroke: '#cccccc'  
	       },  
	       // Custom css  
	       style: {                      
	           fontWeight: 'bold',       
	           fontSize: '15px',  
	           color: '#202030'          
	       }  
	   };
	   
	   var series= [{
	       type: 'line',
	       name: 'Random data',
	       data: []
	   }];
	      
	   var json = {};   
	   json.chart = chart; 
	   json.title = title;   
	   json.subtitle = subtitle; 
	   json.tooltip = tooltip;
	   json.xAxis = xAxis;
	   json.yAxis = yAxis;  
	   json.lang = lang;
	   json.noData = noData;
	   json.series = series;
	   json.plotOptions = plotOptions;  
	   json.credits = credits;
		var charts;
		$.ajax({
			url:'reports_onlineData.do',
			data :{begin:$('#begin').val(),end:$('#end').val()},
			type:'post',
			cache : false,
			async : false,
			dataType :'json',
			success:function(data){
				if(data!=null){
					series = eval(data);
					json.series = series;
					var dateSets = series[0].dateSets;
					if(dateSets != undefined && dateSets != null){
						json.xAxis = {
							      categories: dateSets ,
							      crosshair: true
							   }; 
					}
				} 
				charts = new Highcharts.Chart(json);
			},
			error : function(){
				alert("请求异常,请稍后重试.");
				charts = new Highcharts.Chart(json);
			}
		});
	   
}
</script>
</body>
</html>
