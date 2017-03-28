<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>edit</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript" src="${basePath}/js/jsAddress.js"></script>
<script language="javascript">
	$().ready(function () {
		
		addressInit('province', 'city', 'area', '${shopInfo.province}', '${shopInfo.city}', '${shopInfo.area}');
		
		<c:forEach items="${shopInfos}" var="item">
			addShop('${item.shopId}','${item.shopName}');
		</c:forEach>
	
		$("#myForm2222").validate({
			rules: {
	        }
	    });
	});

	function addShop(id,name){
		
		if('${role.id}'=='3'){
			$("#shops").find("div").each(function(index,ele){
				removeShop(ele.id);
			});
		}
    	var inHtml = ' <div class="btn-group" id="'+id+'"><button type="button" '
    			+ 'class="btn btn-default" onclick="removeShop(\''+ id +'\',\''+name+'\')">'
       			+ name
        		+ '<span class="trash"></span></button></div>';
        $("#shops").append(inHtml);
        $("#shopIds").val($("#shopIds").val() + id + ";");
        var inp = document.getElementById("shopTr"+id);
    	if(inp != "undefined" && inp != null && inp != "null"){
    		inp.checked = true;
    	}
    }
    
    function removeShop(id,name){
    	$("#"+id).remove();
    	var shopids = $("#shopIds").val();
    	var shops = shopids.split(";");
    	shopids = "";
    	for(var i=0;i<shops.length;i++){
    		var shop = shops[i];
    		if(shop!=id && shop!=""){
    			shopids = shop + ";";
    		}
    	}
    	$("#shopIds").val(shopids);
    	var inp = document.getElementById("shopTr"+id);
    	if(inp != "undefined" && inp != null && inp != "null"){
    		inp.checked = false;
    	}
    }
    
    function changeDiv(ckb,id,name){
    	if(ckb.checked){
    		addShop(id, name);
    	} else {
    		removeShop(id,name);
    	}
    }
    
    function searchShop(currentPage){
    	$("#tbody").load("adminForShop_getShop.do", {'shopInfo.province': $("#province").val(),
    									'shopInfo.city':$("#city").val(),
    									'shopInfo.area':$("#area").val(),
    									'pager.currentPage':currentPage});
    	top.setRightIframeHeight(600);
    	//$("#pager.currentPage").val(currentPage);
    }
    
    function changeBiggest(){
    	var biggest = document.getElementById("topage").innerHTML;
    	var jumpInput = $("[name='pager.currentPage']").val();
        var jumpInputNum = parseInt(jumpInput,10);
    	if(isNaN(jumpInputNum)||jumpInputNum>biggest){
    		$("[name='pager.currentPage']").val(1);
    	};
    }
</script>

</head>
<body>
	<form id="myForm2222" name="myForm2222" method="post" action="adminForShop_update.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;添加</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-bordered">
					<tbody>
						<tr>
							<th>登录名:</th>
							<td class="pn-fcontent">
								<input name="admin.id" value="${admin.id}" style="width: 200px;"
									maxlength="20" type="hidden" />
								<input name="admin.username" value="${admin.username}" style="width: 200px;"
									maxlength="20" type="text" readonly="readonly"/>
							</td>
							<th>真实姓名:</th>
							<td class="pn-fcontent">
								<input name="admin.name" value="${admin.name}" style="width: 200px;"
									maxlength="20" type="text" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<th>管理彩点:</th>
							<td class="pn-fcontent" colspan="3">
								<input type="hidden" name="shopids" value="" id="shopIds"/>
								<div style="margin:5px;width: 100%;height:30px;border:thin;border-color: black;" id="shops"></div>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<a onclick="$(this).parents('form').submit();" href="javascript:;" class="btn btn-primary">保 存</a>
						<a href="javascript:window.location.href='adminForShop_list.do';" class="btn btn-danger">返 回</a>
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
	<div class="separator line"></div>
	
<form id="myForm" name="myForm" action="adminForShop_edit.do" method="post">
	<input name="admin.id" value="${admin.id}" type="hidden" />
	<div class="widget widget-table">
		<div class="widget-content">
			<table class="pn-ftable table-bordered" cellpadding="10px" style="height: 100%;width: 100%">
					<tr>
						<td style="padding:3px;background-color: #EEEEEE;padding-left: 15px">
							<b>选择省市区：</b>
							<select style="width: 150px" name="shopInfo.province" id="province"></select>&emsp;
							<select style="width: 150px" id="city" name="shopInfo.city"></select>&emsp;
							<select style="width: 150px" id="area" name="shopInfo.area"></select>
							<!-- <input type="button" class="btn btn-info" value="查找" onclick="searchShop(1);"/> -->
							<input type="submit" class="btn btn-info" value="查找"/>
						</td>
					</tr>
			</table>
		</div>
	</div>
	<div class="widget widget-table">
		<div class="widget-content widget-list">
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<thead>
					<tr>
						<th>选择关联彩站</th>
						<th>彩站账号</th>
						<th>彩站名称</th>
						<th>所属省</th>
						<th>所属市</th>
						<th>所属地区</th>
						<th>彩站地址</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td width="100px"><input style="width: 20px" type="checkbox" id="shopTr${item.shopId}" 
								onclick="changeDiv(this,'${item.shopId}','${item.shopName}')"/>
							<%-- ${status.count } --%>
							</td>
							<td>${item.accountId}</td>
							<td>${item.shopName}</td>
							<td>${item.province}</td>
							<td>${item.city}</td>
							<td>${item.area}</td>
							<td>${item.shopAddress}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="widget-bottom">
				<jsp:include page="../../include/pager.jsp" />
			</div>
		</div>
		<!-- /widget-content -->
	</div>
</form>

</body>
</html>