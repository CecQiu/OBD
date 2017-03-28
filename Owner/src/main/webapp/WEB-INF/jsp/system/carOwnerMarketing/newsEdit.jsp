<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>发布资讯</title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
function reback(){
	window.parent.hideIframe();
}
function forward(){
	var infoTitle=document.getElementById("title_text").value;
	var summary=document.getElementById("summary_text").value;
	var infoDetail=document.getElementById("context").value;
	var infoId=document.getElementById("infoId").value;
	var url="admin/carOwnerMarketing_updateNews.do?infoId="+infoId+"&infoTitle="+encodeURI(encodeURI(infoTitle))+"&infoDetail="+encodeURI(encodeURI(infoDetail))+"&summary="+encodeURI(encodeURI(summary));
	window.location.href=url;
}
function update(){
	var infoTitle=document.getElementById("title_text").value;
	var summary=document.getElementById("summary_text").value;
	var infoDetail=document.getElementById("context").value;
	if(infoTitle==null||infoTitle.replace(/(^s*)|(s*$)/g, "").length ==0){
		 $('#error')[0].innerHTML = "<font color=red size=2>标题不能为空！</font>";
	}else{
		if(summary==null||summary.replace(/(^s*)|(s*$)/g, "").length ==0){
			$('#error')[0].innerHTML = "<font color=red size=2>概况不能为空！</font>";
		}else{
			if(infoDetail==null||infoDetail.replace(/(^s*)|(s*$)/g, "").length ==0){
				$('#error')[0].innerHTML = "<font color=red size=2>内容不能为空！</font>";
			}else{
				alert("修改成功！");			
				forward();
				window.parent.hideIframe();
			}
		}
	}

}
</script>
</head>
<body >
	<form id="newsForm" method="post" action="">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;发布资讯</li>
		</ul>
		<table class="table table-hover" border="0" cellpadding="10">
			<tr>
			<td>标题:&nbsp;&nbsp;<input type="text" id="title_text" size="20px" value="${serInformationman.infoTitle}" ></input></td>
			<td>概况：&nbsp;&nbsp;<input type="text" id="summary_text" value="${serInformationman.summary}" ></input></td>
			<input type="hidden" id="infoId" value="${serInformationman.infoId}"></input>
			</tr>
			<tr>
			<td colspan="2"><label>内容：&nbsp;</label><textarea  cols="56" rows="5" id="context" >${serInformationman.infoDetail}</textarea></td>	
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="error"></span>
			</tr>		
		</table>
	</form>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<button type="button" class="btn btn-primary" onclick="update()">保存</button>
<button type="button" class="btn btn-primary" onclick="reback()">取消</button>
</body>
</html>