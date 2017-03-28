<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=UTF-8"%>
<!-- saved from url=(0014)about:internet -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<%-- <link id="css_main" rel="stylesheet" type="text/css" href="${basePath}/theme/${session.theme}/main.css" />
 --%>
 <link id="css_main" rel="stylesheet" type="text/css" href="${basePath}/theme/dandelion/main.css" />
 <title>OBD平台管理系统</title>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<script language="javascript" src="${basePath}/js/bootstrap.min.js"></script>
<script type="text/javascript" language="javascript">
	$(window).resize(function() {
		 var width = $(this).width() - 200;
		 if(width < 768) width = 768;
		 $(".page-content").css("width",(width-10)+"px");
	});
	
	function setRightIframeHeight(height) {
		$("#rightFrame").height(height);
	}

	function setIFrameHeight() {
		
		var mainheight = $("#rightFrame").contents().find("body").attr('scrollHeight');
		if (mainheight < 580) {
			mainheight = 580;
		}
		$("#rightFrame").height(mainheight);
	}
	
	function setTheme(theme){
		var href = "${basePath}/theme/" + theme + "/main.css";
		$("#css_main").attr("href", href);
		$("#rightFrame").contents().find("#css_main").attr("href", href);
		$.get("admin_saveTheme.do",{theme: theme},function(data){});
		$(".theme-menu").hide();
	}
	function saveTheme(){
		$.get("admin_saveTheme.do",{theme: $('.set-theme').val()},function(data){
			if (data != null) {
				alert("保存成功");
			}
		});
	}
	
</script>
<script type="text/javascript" src="${basePath}/js/toTop.js"></script>
</head>
<body class="menu-left full-container" theme="${session.theme}">
	<div class="navbar navbar-static-top" role="navigation" style="background-color: #437bd0;border-top: 3px solid #437bd0; box-shadow: 0px 1px 1px #437bd0;
  background-image: url('');">
	  <div class="container">
	  	<jsp:include page="top.jsp" />
	  </div>
	</div>
	<div class="container relative main">
		 <div class="sidebar">
			<jsp:include page="left.jsp" />
		</div>
		<div class="page-content">
			<div class="content container">
				<div class="row">
					<div class="col-lg-12">
						<iframe id="rightFrame" name="rightFrame" frameborder="0"  onLoad="setIFrameHeight()" noresize width="100%" scrolling="no" src="${basePath }/admin/sys_right.do" ></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>