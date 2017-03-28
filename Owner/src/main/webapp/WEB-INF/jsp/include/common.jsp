<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<link type="text/css" rel="stylesheet" href="${basePath}/css/jquery.css"/>
<script type="text/javascript" src="${basePath}/js/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/jquery-form.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/bootstrap.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/jquery.validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/jquery.validate.extends.js?v=7575" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/jquery.metadata.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/plugins/My97DatePicker/WdatePicker.js" charset="UTF-8"></script>

<script type="text/javascript" src="${basePath}/js/bootstrap-sortable.js" charset="UTF-8"></script>
<script type="text/javascript" src="${basePath}/js/util.js" charset="UTF-8"></script>

<link rel="stylesheet" type="text/css" href="${basePath}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${basePath}/css/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/css/bootstrap-sortable.css" />

<!--[if IE 7]>
<link rel="stylesheet" href="${basePath}/css/plugins/font-awesome/css/font-awesome-ie7.min.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/theme/${session.theme}/main.css" />
<auth value="${authResource}"/>
<script>
//条件过滤的统一重置操作
function doReset() {
	var input_text = $("#table_conditions").find("input[type=text]");
	var the_select = $("#table_conditions").find("select");
	
	//清空输入框的值
	for (var i = 0; i < input_text.length; i++) {
		input_text[i].value = "";
	}
	//把下拉框默认选中第一个
	for (var i = 0; i < the_select.length; i++) {
		the_select[i].options[0].selected = true;
	}
}

//普通的页面后退方法
function doGoBack() {
	history.go(-1);
}

function isChinese(param){	  
	return (param.length != param.replace(/[^\x00-\xff]/g,"**").length);   
}
$(function() {
	
	if ("${message}" != "" && isChinese(decodeURI('${message}'))) {
		alert(decodeURI("${message}"));
	}
});
</script>