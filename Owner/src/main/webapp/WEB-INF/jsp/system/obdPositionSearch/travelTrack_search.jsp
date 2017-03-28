<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title></title>
<jsp:include page="../../include/common.jsp" />

<script language="javascript">

function myFormReset(){
	$("#obdSn").val("");
	$("#obdMSn").val("");
	$("#startTime").val("");
	$("#endTime").val("");
	
	$("#quickenNum").val("");
	$("#quickSlowDown").val("");
	$("#voltage").val("");
	$("#driverTime").val("");
	
	$("#distance").val("");
	$("#type").val("");
	
	$("#voltageFlag").val("");
	$("#driverTimeFlag").val("");
	$("#distanceFlag").val("");
}

function exportExcel(){
	var obdSn=$("#obdSn").val().trim();
	var obdMSn=$("#obdMSn").val().trim();
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	
	var quickenNum=$("#quickenNum").val().trim();
	var quickSlowDown=$("#quickSlowDown").val().trim();
	var voltage=$("#voltage").val().trim();
	var driverTime=$("#driverTime").val().trim();
	
	var distance=$("#distance").val().trim();
	var type=$("#type").val();
	var voltageFlag=$("#voltageFlag").val();
	var driverTimeFlag=$("#driverTimeFlag").val();
	var distanceFlag=$("#distanceFlag").val();
	
	if(startTime=='' && endTime==''){
		alert('开始时间和结束时间不能为空.');
		return false;
	}
	
	/* if(obdSn!='' && obdMSn!=''){
		alert("设备号和表面码只能输入一个.");
		return false;
	} */
	
	//查询只能查询一周的记录
	var a = startTime.split(" ");     
	var b = a[0].split("-");     
	var c = a[1].split(":");     
	var oldTime = new Date(b[0], b[1]-1, b[2], c[0], c[1], c[2]);  

	var aa = endTime.split(" ");     
	var bb = aa[0].split("-");     
	var cc = aa[1].split(":");     
	var newTime = new Date(bb[0], bb[1]-1, bb[2], cc[0], cc[1], cc[2]);  
	var days = parseInt((newTime.getTime()-oldTime.getTime()) / (1000 * 60 * 60 * 24));
	if(newTime<=oldTime){
		alert("开始时间不能大于结束时间.");
		return false;
	}
	if(days > 31){
		alert("日期范围应在31天之内.");
		return false;
	}
	
	window.location.href="${basePath}/admin/obdTravelTrackSearch_exportExcel.do?obdSn="+obdSn+
			"&obdMSn="+obdMSn+"&startTime="+startTime+"&endTime="+endTime+
			"&quickenNum="+quickenNum+"&quickSlowDown="+quickSlowDown+
			"&voltage="+voltage+"&driverTime="+driverTime+"&distance="+distance+
			"&type="+type+"&voltageFlag="+voltageFlag+"&driverTimeFlag="+driverTimeFlag+
			"&distanceFlag="+distanceFlag;
}

function getRowIndex(obj){
	get("pager.rowIndex").value=obj.rowIndex;
}

function formCheck(){
	var obdSn=$("#obdSn").val().trim();
	var obdMSn=$("#obdMSn").val().trim();
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	
	var quickenNum=$("#quickenNum").val().trim();
	var quickSlowDown=$("#quickSlowDown").val().trim();
	var voltage=$("#voltage").val().trim();
	var voltageFlag=$("#voltageFlag").val();
	var driverTime=$("#driverTime").val().trim();
	var driverTimeFlag=$("#driverTimeFlag").val();
	var distance=$("#distance").val().trim();
	var distanceFlag=$("#distanceFlag").val();
	var type=$("#type").val();
	
	if(startTime=='' && endTime==''){
		alert('开始时间和结束时间不能为空.');
		return false;
	}
	/* if(obdSn!='' && obdMSn!=''){
		alert("设备号和表面码只能输入一个.");
		return false;
	} */
	
	if(obdSn!=''){
		$("#obdSn").val(obdSn.trim());
	}
	if(obdMSn!=''){
		$("#obdMSn").val(obdMSn.trim());
	}
	if(quickenNum!=''){
		$("#quickenNum").val(quickenNum.trim());
	}
	if(quickSlowDown!=''){
		$("#quickSlowDown").val(quickSlowDown.trim());
	}
	if(voltage!=''){
		$("#voltage").val(voltage.trim());
	}
	if(driverTime!=''){
		$("#driverTime").val(driverTime.trim());
	}
	if(distance!=''){
		$("#distance").val(distance.trim());
	}
	//查询只能查询一周的记录
	var a = startTime.split(" ");     
	var b = a[0].split("-");     
	var c = a[1].split(":");     
	var oldTime = new Date(b[0], b[1]-1, b[2], c[0], c[1], c[2]);  

	var aa = endTime.split(" ");     
	var bb = aa[0].split("-");     
	var cc = aa[1].split(":");     
	var newTime = new Date(bb[0], bb[1]-1, bb[2], cc[0], cc[1], cc[2]);  
	var days = parseInt((newTime.getTime()-oldTime.getTime()) / (1000 * 60 * 60 * 24));
	if(newTime<=oldTime){
		alert("结束时间不能大于开始时间.");
		return false;
	}
	if(days > 31){
		alert("日期范围应在31天之内.");
		return false;
	}
	
	
	if((driverTime!='' && driverTimeFlag=='') || (driverTime=='' && driverTimeFlag!='')){
		alert("疲劳驾驶参数参数有误.");
		return false;
	}
	
	if((distance!='' && distanceFlag=='') || (distance=='' && distanceFlag!='')){
		alert("距离参数有误.");
		return false;
	}
	
	if((voltage!='' && voltageFlag=='') || (voltage=='' && voltageFlag!='')){
		alert("电压参数有误.");
		return false;
	}
	var reg = new RegExp("^[0-9]*$");  
	var dreg = new RegExp("^([+-]?)\\d*\\.\\d+$");
	if(quickenNum!='' && !reg.test(quickenNum)){  
	    alert("急加速请输入数字!");  
	    return false;
	} 
	if(quickSlowDown!='' && !reg.test(quickSlowDown)){  
	    alert("急减速请输入数字!");  
	    return false;
	} 
	if(driverTime!='' && !reg.test(driverTime)){  
	    alert("疲劳驾驶时长请输入数字!");  
	    return false;
	} 
	if(distance!='' && !reg.test(distance)){  
	    alert("距离请输入数字!");  
	    return false;
	} 
	if(voltage!='' && !dreg.test(voltage)){  
	    alert("电压请输入浮点数!");  
	    return false;
	} 
	
}

</script>
</head>
<body>
    <form  id="myForm" name="myForm" action="obdTravelTrackSearch_travelTrackSearch.do" method="post" onsubmit="return formCheck();">
       <ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
        <div class="widget widget-table">
           <div class="widget-content">
              <table class="pn-ftable table-condensed" border="0" cellpadding="10">
                   <tbody>
                       <tr>
                           <th>设备号</th> 
                           <td class="pn-fcontent">
                              <input type="text" id="obdSn" size="24" name="obdSn" value="${obdSn}"/>
                           </td> 
                           <th>表面号</th> 
                           <td class="pn-fcontent">
                              <input type="text" id="obdMSn" size="24" name="obdMSn" value="${obdMSn}"/>
                           </td>                                           
                       </tr>
                       <tr>
                           <th>急加速次数大于</th> 
                           <td class="pn-fcontent">
                              <input type="text" id="quickenNum" size="24" name="quickenNum" value="${quickenNum}"/>
                           </td> 
                           <th>急减速次数大于</th> 
                           <td class="pn-fcontent">
                              <input type="text" id="quickSlowDown" size="24" name="quickSlowDown" value="${quickSlowDown}"/>
                           </td>                                           
                       </tr>
                             
                       <tr>
                         <th>电压</th> 
                           <td class="pn-fcontent">
                           	  <select name="voltageFlag" id="voltageFlag" style="width: 80px;">
								<option value="">请选择</option>
								<option value="1" <c:if test="${voltageFlag=='1'}">selected</c:if>>大于</option>
								<option value="0" <c:if test="${voltageFlag=='0'}">selected</c:if>>小于</option>	
							  </select>&nbsp;
                              <input type="text" id="voltage" size="24" name="voltage" value="${voltage}"/>
                           </td>
                           <th>疲劳驾驶时长</th> 
                           <td class="pn-fcontent">
                           	  <select name="driverTimeFlag" id="driverTimeFlag" style="width: 80px;">
								<option value="">请选择</option>
								<option value="1" <c:if test="${driverTimeFlag=='1'}">selected</c:if>>大于</option>
								<option value="0" <c:if test="${driverTimeFlag=='0'}">selected</c:if>>小于</option>	
							  </select>&nbsp;
                              <input type="text" id="driverTime" size="24" name="driverTime" value="${driverTime}"/>
                           </td>                                        
                       </tr>
                       <tr>
                         <th>距离</th> 
                           <td class="pn-fcontent">
                           	  <select name="distanceFlag" id="distanceFlag" style="width: 80px;">
								<option value="">请选择</option>
								<option value="1" <c:if test="${distanceFlag=='1'}">selected</c:if>>大于</option>
								<option value="0" <c:if test="${distanceFlag=='0'}">selected</c:if>>小于</option>	
							  </select>&nbsp;
                              <input type="text" id="distance" size="24" name="distance" value="${distance}"/>
                           </td>
                           <th>类型</th> 
                           <td class="pn-fcontent">
                           	  <select name="type" id="type" style="width: 120px;">
								<option value="">全部</option>
								<option value="0" <c:if test="${type=='0'}">selected</c:if>>正常行程0</option>
								<option value="1" <c:if test="${type=='1'}">selected</c:if>>半条行程1</option>	
								<option value="2" <c:if test="${type=='2'}">selected</c:if>>半条行程失效2</option>	
							  </select>&nbsp;
                           </td>                                        
                       </tr>
                       
                       <tr>
                          <th>行程开始时间</th> 
                          <td class="pn-fcontent">
                             <input type="text" value="${startTime}" class="Wdate" readonly="readonly" name="startTime" id="startTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>                                
                          </td>       
                      	  <th>行程结束时间</th>   
                          <td class="pn-fcontent">
                            <input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
                          </td>
                       </tr>
                                
                   </tbody>                           
              </table>
              <div class="widget-bottom">
                   <center>
                       <input class="btn btn-s-md btn-success" type="submit" value="查询"/>&nbsp;    
                       <input class="btn pull-center  btn-info" type="button" value="重置" onclick="myFormReset();"/>&nbsp;
                       <input class="btn btn-primary pull-center" type="button" value="导出" onclick="exportExcel();"/>&nbsp;                          
                   </center>
              </div>                        
           </div>      
        </div>
        <div class="widget widget-table">
           <div class="widget-header">
              <i class="icon-th-list"></i>
		      <h5>行程数据列表</h5>       
           </div>
           <!-- 列表内容 -->
           <div class="widget-content widget-list" style="overflow-x: scroll;">
              <table class="table table-striped table-bordered table-condensed table-hover sortable">
                 <thead>
                    <tr>
                      <th>序号</th>
                      <th>设备号</th>
                      <th>导入时间</th>
                      <th>行程结束时间</th>                                                                        
                      <th>行程序号</th>                                                                        
                      <th>行程开始时间</th>                     
                      <th>距离</th>
                      <th>最大速度</th>
                      <th>超速次数</th>
                      <th>急刹车次数</th>
                      <th>急转弯次数</th>
                      <th>急加速次数</th>
                      <th>急减速次数</th>
                      <th>急变道次数</th>
                      <th>怠速次数</th>
                      <th>发动机最高水温</th>
                      <th>发动机最高转速</th>
                      <th>发动机最高转速次数</th>
                      <th>发动机转速不匹配次数</th>
                      <th>电压值</th>
                      <th>总油耗</th>                    
                      <th>平均油耗</th>                    
                      <th>疲劳驾驶时长</th>  
                      <th>行程类型</th>
                      <th>报文</th>                  
                    </tr>                   
                 </thead>
                 <tbody>
                    <c:forEach items="${carTraveltracks}" var="item" varStatus="status">
                         <tr onclick="getRowIndex(this)">
                           <td>${item.id}</td>
                           <td>${item.obdsn}</td>
                           <td><fmt:formatDate value="${item.insesrtTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                           <td><fmt:formatDate value="${item.travelEnd}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                           <td>${item.travelNo}</td>
                           <td><fmt:formatDate value="${item.travelStart}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                           <td>${item.distance}</td>
                           <td>${item.speed}</td>
                           <td>${item.overspeedTime}</td>
                           <td>${item.brakesNum}</td>
                           <td>${item.quickTurn}</td>
                           <td>${item.quickenNum}</td>
                           <td>${item.quickSlowDown}</td>
                           <td>${item.quickLaneChange}</td>
                           <td>${item.idling}</td>
                           <td>${item.temperature}</td>
                           <td>${item.engineMaxSpeed}</td>
                           <td>${item.rotationalSpeed}</td>
                           <td>${item.speedMismatch}</td>
                           <td>${item.voltage}</td>
                           <td>${item.totalFuel}</td>                        
                           <td>${item.averageFuel}</td>                        
                           <td>${item.driverTime}</td>      
                           <td>${item.type}</td>
                           <td>${item.message}</td>                   
                         </tr>                     
                    </c:forEach>        
                 </tbody>          
              </table>
              <div class="widget-bottom"> 
                 <jsp:include page="../../include/pager.jsp"/>
              </div>
           </div>
        </div>
    </form>
</body>
</html>