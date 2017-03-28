<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<jsp:include page="../../include/common.jsp"></jsp:include>

<script language="javascript">
function myFormReset(){
	$("#obdSn").val("");
	$("#createTime").val("");
	$("#createEndTime").val("");
}

function exportExcel(){
	var obdSn = $('#obdSn').val();
	var createTime = $('#createTime').val();
	var createEndTime = $('#createEndTime').val();
	/* if(!$.trim(obdSn)){
		alert('设备号不能为空！');
		return;
	} */
	if(!$.trim(createTime) || !$.trim(createEndTime)){
		alert('时间不能为空！');
		return;
	}
	window.location.href="${basePath}/admin/carOwnerInventory_obdUnregExcel.do?obdUnReg.obdSn="+obdSn+"&createTime="+createTime+"&createEndTime="+createEndTime;
}

function getRowIndex(obj){
	get("pager.rowIndex").value=obj.rowIndex;
}

</script>
</head>
<body>
     <form id="myForm" name="myForm" action="carOwnerInventory_getObdUnregInfo.do" method="post">
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
                              <input type="text" id="obdSn" size="24" name="obdUnReg.obdSn" value="${obdUnReg.obdSn}"/>
                           </td>                                          
                       </tr>      
                       <tr>
                       <th>开始时间</th> 
                          <td class="pn-fcontent">
                             <input type="text" value="${createTime}" class="Wdate" readonly="readonly" name="createTime" id="createTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd',maxDate:'%y-%M-%d %H:%m:%s'})"/>                                
                          </td>                             
                       <th>结束时间</th> 
                          <td class="pn-fcontent">
                             <input type="text" value="${createEndTime}" class="Wdate" readonly="readonly" name="createEndTime" id="createEndTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd',maxDate:'%y-%M-%d %H:%m:%s'})"/>                                
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
		      <h5>OBD未注册列表</h5>       
           </div>
           <!-- 列表内容 -->
           <div class="widget-content widget-list">
              <table class="table table-striped table-bordered table-condensed table-hover sortable">
                 <thead>
                    <tr>
                      <th>序号</th>
                      <th>设备号</th>
                      <th>时间</th>
                    </tr>                   
                 </thead>
                 <tbody>
                    <c:forEach items="${obdUnRegs}" var="item" varStatus="status">
                         <tr onclick="getRowIndex(this)">
                           <td>${item.id}</td>
                           <td>${item.obdSn}</td>
                           <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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