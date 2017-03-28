<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<jsp:include page="../../include/common.jsp"/>

<script language="javascript">

function myFormReset(){
	$("#obdSn").val("");
	$("#obdMSn").val("");
	$("#startTime").val("");
	$("#endTime").val("");
	$("#obdSpeed").val("");
	$("#engineTurns").val("");
	$("#engineTemperature").val("");
	$("#obdSpeedFlag").val("");
	$("#engineTurnsFlag").val("");
	$("#engineTempFlag").val("");
	
}

function exportExcel(){
	var obdSn=$("#obdSn").val().trim();
	var obdMSn=$("#obdMSn").val().trim();
	
	var obdSpeed=$("#obdSpeed").val().trim();
	var engineTurns=$("#engineTurns").val().trim();
	var engineTemperature=$("#engineTemperature").val().trim();
	var obdSpeedFlag=$("#obdSpeedFlag").val();
	var engineTurnsFlag=$("#engineTurnsFlag").val();
	var engineTempFlag=$("#engineTempFlag").val();
	
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	
	/* if(obdSn!='' && obdMSn!=''){
		alert("设备号和表面码只能输入一个.");
		return false;
	} */
	if(obdSn=='' && obdMSn==''){
		if(confirm("请尽量输入设备号和表面码,谢谢.")){
			
		}else{
			return false;
		}
	}
	if(obdSpeed=='' && engineTurns=='' && engineTemperature==''&& obdSn=='' && obdMSn==''){
		alert("如果不是查询obd速度、发动机转数、发动机水温的话,必须输入设备号和表面号.");
		return false;
	}
	
	if(startTime=='' || endTime ==''){
		alert("请输入开始时间和结束时间.")
		return false;
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
	if(days > 7){
		alert("日期范围应在7天之内");
		return false;
	}
	
	var reg = new RegExp("^[0-9]*$");  
	if(!reg.test(obdSpeed)){  
	    alert("obd速度请输入数字!");  
	} 
	if(!reg.test(obdSpeed)){  
	    alert("obd速度请输入数字!");  
	    return false;
	} 
	if(!reg.test(engineTurns)){  
	    alert("发动机转数请输入数字!");  
	    return false;
	} 
	if(!reg.test(engineTemperature)){  
	    alert("发动机转数请输入数字!");  
	    return false;
	} 
	
	if((obdSpeed!='' && obdSpeedFlag=='') || (obdSpeed=='' && obdSpeedFlag!='')){
		alert("obd速度参数有误.");
		return false;
	}
	
	if((engineTurns!='' && engineTurnsFlag=='') || (engineTurns=='' && engineTurnsFlag!='')){
		alert("发动机转数参数有误.");
		return false;
	}
	
	if((engineTemperature!='' && engineTempFlag=='') || (engineTemperature=='' && engineTempFlag!='')){
		alert("发动机水温参数有误.");
		return false;
	}
	
	/* if(parseInt(obdSpeed)<150){
		alert("obd速度必须大于150");
		return false;
	} 
	if(parseInt(engineTurns)<5000){
		alert("发动机转数必须大于5000.");
		return false;
	}
	if(parseInt(engineTemperature)<120){
		alert("发动机水温必须大于120.");
		return false;
	} */
	
	window.location.href="${basePath}/admin/obdPositionSearch_exportExcel.do?obdSn="+obdSn+
			"&obdMSn="+obdMSn+"&startTime="+startTime+"&endTime="+endTime+"&obdSpeed="+obdSpeed+
			"&engineTurns="+engineTurns+"&engineTemperature="+engineTemperature+
			"&obdSpeedFlag="+obdSpeedFlag+"&engineTurnsFlag="+engineTurnsFlag+"&engineTempFlag="+engineTempFlag;
	
}

function getRowIndex(obj){
	get("pager.rowIndex").value=obj.rowIndex;
}

function formCheck(){
	var obdSn=$("#obdSn").val().trim();
	var obdMSn=$("#obdMSn").val().trim();
	
	var obdSpeed=$("#obdSpeed").val().trim();
	var engineTurns=$("#engineTurns").val().trim();
	var engineTemperature=$("#engineTemperature").val().trim();
	
	var obdSpeedFlag=$("#obdSpeedFlag").val();
	var engineTurnsFlag=$("#engineTurnsFlag").val();
	var engineTempFlag=$("#engineTempFlag").val();
	
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	//console.info(startTime+"---"+endTime);
	
	if(obdSn=='' && obdMSn==''){
		alert("请输入设备号或表面码.");
		return false;
	}
	
	/* if(obdSn!='' && obdMSn!=''){
		alert("设备号和表面码只能输入一个.");
		return false;
	} */
	
	/* if(obdSpeed=='' && engineTurns=='' && engineTemperature==''&& obdSn=='' && obdMSn==''){
		alert("如果不是查询obd速度、发动机转数、发动机水温的话,必须输入设备号和表面号.");
		return false;
	} */
	if(obdSn!=''){
		$("#obdSn").val(obdSn.trim());
	}
	if(obdMSn!=''){
		$("#obdMSn").val(obdMSn.trim());
	}
	if(obdSpeed!=''){
		$("#obdSpeed").val(obdSpeed.trim());
	}
	if(engineTurns!=''){
		$("#engineTurns").val(engineTurns.trim());
	}
	if(engineTemperature!=''){
		$("#engineTemperature").val(engineTemperature.trim());
	}
	
	if(startTime=='' || endTime ==''){
		alert("请输入开始时间和结束时间.")
		return false;
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
		alert("日期范围应在31天内.");
		return false;
	}
	
	var reg = new RegExp("^[0-9]*$");  
	if(!reg.test(obdSpeed)){  
	    alert("obd速度请输入数字!");  
	} 
	if(!reg.test(obdSpeed)){  
	    alert("obd速度请输入数字!");  
	    return false;
	} 
	if(!reg.test(engineTurns)){  
	    alert("发动机转数请输入数字!");  
	    return false;
	} 
	if(!reg.test(engineTemperature)){  
	    alert("发动机转数请输入数字!");  
	    return false;
	} 
	if((obdSpeed!='' && obdSpeedFlag=='') || (obdSpeed=='' && obdSpeedFlag!='')){
		alert("obd速度参数有误.");
		return false;
	}
	
	if((engineTurns!='' && engineTurnsFlag=='') || (engineTurns=='' && engineTurnsFlag!='')){
		alert("发动机转数参数有误.");
		return false;
	}
	
	if((engineTemperature!='' && engineTempFlag=='') || (engineTemperature=='' && engineTempFlag!='')){
		alert("发动机水温参数有误.");
		return false;
	}
	
	/* if(parseInt(obdSpeed)<150){
		alert("obd速度必须大于150");
		return false;
	}
	if(parseInt(engineTurns)<5000){
		alert("发动机转数必须大于5000.");
		return false;
	}
	if(parseInt(engineTemperature)<120){
		alert("发动机水温必须大于120.");
		return false;
	} */
}

</script>
</head>
<body>
     <form name="myForm" id="myForm" action="obdPositionSearch_positionSearch.do" method="post" onsubmit="return formCheck();">
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
                         <th>OBD速度</th> 
                           <td class="pn-fcontent">
                           	  <select name="obdSpeedFlag" id="obdSpeedFlag" style="width: 80px;">
								<option value="">请选择</option>
								<option value="1" <c:if test="${obdSpeedFlag=='1'}">selected</c:if>>大于</option>
								<option value="0" <c:if test="${obdSpeedFlag=='0'}">selected</c:if>>小于</option>	
							  </select>&nbsp;
                              <input type="text" id="obdSpeed" size="24" name="obdSpeed" value="${obdSpeed}"/>
                           </td>
                           <th>发动机转数</th> 
                           <td class="pn-fcontent">
                           	  <select name="engineTurnsFlag" id="engineTurnsFlag" style="width: 80px;">
								<option value="">请选择</option>
								<option value="1" <c:if test="${engineTurnsFlag=='1'}">selected</c:if>>大于</option>
								<option value="0" <c:if test="${engineTurnsFlag=='0'}">selected</c:if>>小于</option>	
							  </select>&nbsp;
                              <input type="text" id="engineTurns" size="24" name="engineTurns" value="${engineTurns}"/>
                           </td>                                        
                       </tr>
                       <tr>
                         <th>发动机水温</th> 
                           <td class="pn-fcontent">
                           	  <select name="engineTempFlag" id="engineTempFlag" style="width: 80px;">
								<option value="">请选择</option>
								<option value="1" <c:if test="${engineTempFlag=='1'}">selected</c:if>>大于</option>
								<option value="0" <c:if test="${engineTempFlag=='0'}">selected</c:if>>小于</option>	
							  </select>&nbsp;
                              <input type="text" id="engineTemperature" size="24" name="engineTemperature" value="${engineTemperature}"/>
                           </td>
                           <th></th> 
                           <td class="pn-fcontent"></td>                                        
                       </tr>
                       <tr>
                       <th>开始时间</th> 
                          <td class="pn-fcontent">
                             <input type="text" value="${startTime}" class="Wdate" readonly="readonly" name="startTime" id="startTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>                                
                          </td>       
                       <th>结束时间</th>   
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
        <!-- 查询列表 -->
        <div class="widget widget-table">
           <div class="widget-header">
              <i class="icon-th-list"></i>
		      <h5>位置数据列表</h5>       
           </div>
           <!-- 列表内容 -->
           <div class="widget-content widget-list" style="overflow-x: scroll;">
              <table class="table table-striped table-bordered table-condensed table-hover sortable">
                 <thead>
                    <tr>
                      <th>序号</th>
                      <th>设备号</th>
                      <th>导入时间</th>
                      <th>定位成功</th>
                      <th>定位类型</th>
                      <th>时间类型</th>                                                                        
                      <th>时间</th>  
                      <th>经度类型</th>                                                                      
                      <th>经度</th> 
                      <th>纬度类型</th>                    
                      <th>纬度</th>
                      <th>方向角</th>
                      <th>GPS速度</th>
                      <th>OBD速度</th>
                      <th>发动机转数</th>
                      <th>发动机水温</th>     
                      <th>卫星系统</th> 
                      <th>卫星个数</th>     
                      <th>卫星强度</th>
                      <th>网络类型</th>
                      <th>国家代码</th>
                      <th>移动网号码</th>
                      <th>系统识别码</th>
                      <th>网络识别码</th>
                      <th>基站</th>
                      <th>CDMA定位精度</th>
                    </tr>                   
                 </thead>
                 <tbody>
                    <c:forEach items="${positionInfos}" var="item" varStatus="status">
                         <tr onclick="getRowIndex(this)">
                           <td>${item.id}</td>
                           <td>${item.obdSn}</td>
                           <td><fmt:formatDate value="${item.insertTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                           <td>
	                           <c:choose>
									<c:when test="${item.statusGPS  eq 1}">成功</c:when>
									<c:when test="${item.statusGPS  eq 0}">失败</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
						   </td>
						   <td>${item.type}</td>
                           <td>
	                           <c:choose>
									<c:when test="${item.timeType  eq 1}">系统时间</c:when>
									<c:when test="${item.timeType  eq 0}">GPS时间</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</td>
                           
                           <td><fmt:formatDate value="${item.time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                           <td>${item.longitudeType}</td>
                           <td>${item.longitude}</td>
                           <td>${item.latitudeType}</td>
                           <td>${item.latitude}</td>
                           <td>${item.directionAngle}</td>
                           <td>${item.gpsSpeed}</td>
                           <td>${item.obdSpeed}</td>
                           <td>${item.engineTurns}</td>
                           <td>${item.engineTemperature}</td>
                           <td>${item.satelliteSys}</td>  
                           <td>${item.satellites}</td>
                           <td>${item.satelliteStrength}</td>
                           <td>${item.netType}</td>
                           <td>${item.mcc}</td>  
                           <td>${item.mnc}</td>
                           <td>${item.sid}</td> 
                           <td>${item.nid}</td>
                           <td>${item.bid}</td>
                           <td>${item.oprecision}</td>                  
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