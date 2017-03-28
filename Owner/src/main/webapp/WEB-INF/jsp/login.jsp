
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/html">
<head>
<title>OBD平台管理系统</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/login/base.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/login/logcss.css" />
<script src="${basePath}/js/jquery.min.js"></script>
<script type="text/javascript">
	window.onload = function() {
		if (window.parent.length > 0)
			window.parent.location = location;
	}
	var pwdInpu, nameInput,validationCodeInput;

	function check() {
		pwdInput = $("#password");
		nameInput = $("#username");
		validationCodeInput=$("#validationCode");
		if (nameInput.val() == "") {
			nameInput.focus();
			$(".warm-info").html("<font color='red'>请输入用户名！</font>");
		} else if (pwdInput.val() == "") {
			pwdInput.focus();
			$(".warm-info").html("<font color='red'>请输入密码！</font>");
		} else if(validationCodeInput.val() == ""){
			validationCode.focus();
			$(".warm-info").html("<font color='red'>请输入验证码！</font>");
		} else {
			$("#loginform").submit();
		}
	}

	$(function() {
		document.onkeydown = function(e) {
			var ev = document.all ? window.event : e;
			if (ev.keyCode == 13) {
				check();
			}
		};

	});
</script>
</head>
<body class="loginpage">
	<%-- <div class="loginbox">            
        <form id="loginform" class="form-vertical" name="loginForm" action="login.do" method="post">
            <div class="formtitle">
            	<img src="${basePath}/images/logo.png" /><font size="6">车主通系统平台管理系统</font>
            </div>
            <div class="control-group form-actions" style="padding-top:30px;">
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_lr"><i class="icon-user"></i></span><input type="text" placeholder="用户名" name="admin.username" value="" id="username" />
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_lg"><i class="icon-lock"></i></span><input type="password" placeholder="密码" name="admin.password" id="1" value=""/>
                    </div>
                </div>
                <div class="info-box" <c:if test="${not empty message}">style="display:block;"</c:if>>
                    <span class="warm-info">
                    	<c:choose>
							<c:when test="${not empty message}">
								<font color='red'>${message}</font>
							</c:when>
							<c:otherwise>
								<font color='red'>${SPRING_SECURITY_LAST_EXCEPTION.message}</font>
							</c:otherwise>
						</c:choose>
                    </span>
                </div>
            </div>
            <div class="form-bottom">
                <a href="javascript:void(0);" class="btn btn-primary" onclick="check()" />登&nbsp;&nbsp;&nbsp;&nbsp;录</a>
            </div>
        </form>
    </div>
    <div class="info-box">
        <div class="system-info">
        	车主通系统平台<br />
			版权所有：广州华工信息软件有限公司
        </div>
    </div> --%>
	<div id="container">

		<div class="c-box">
			<img style="margin-left:60px;" src="${basePath}/images/login/login_03.png" />
		</div>

		<div class="color">
			<div class="c-box">
				<div class="fleft">
					<img style="margin-top:36px;" src="${basePath}/images/login/login_07.png" />
				</div>
				 <form id="loginform" class="form-vertical" name="loginForm" action="login.do" method="post">
				<div class="log fleft">
					<h2>用户登录</h2>
					<ul class="lgt">
						<li><img src="${basePath}/images/login/login_10.png" /><input type="text"
							class="int" value="" name="admin.username" id="username"/></li>
						<li class="H19"><img src="${basePath}/images/login/login_14.png" /><input
							type="password" class="int" name="admin.password" /></li>
						<li class="H19">
							<!-- <label>验证码:</label> -->
							<img src="${basePath}/images/login/login_14.png" style="float:left;"/>
							<input type="text" name="validationCode" id="validationCode" class="int" style="width:40%;float:left;" placeholder="验证码"/>
							<img id="validationCode_img"  src="randomCode.do" onclick="document.getElementById('validationCode_img').src='randomCode.do?'+Math.random()" style="float:left;"/>
							<!-- <a href="javascript:;" onclick="document.getElementById('validationCode_img').src='randomCode.do?'+Math.random()">换一个</a> -->
						</li>
					</ul>
					<div class="ant">
						   <a href="javascript:void(0);"   onclick="check()" /><img src="${basePath}/images/login/login_17.png" /></a>
					</div>
					<!-- <div class="fg">
						<a href="">忘记密码?</a>
					</div> -->
					<div align="right" style="margin-top: 8px;margin-left:40px;" class="info-box" <c:if test="${not empty message}">style="display:block;"</c:if>>
                    <span class="warm-info">
                    	<c:choose>
							<c:when test="${not empty message}">
								<font color='red'>${message}</font>
							</c:when>
							<c:otherwise>
								<font color='red'>${SPRING_SECURITY_LAST_EXCEPTION.message}</font>
							</c:otherwise>
						</c:choose>
                    </span>
                </div>
				</div>
				</form>
			</div>

		</div>
		<div class="footer">版权所有：广州华工信息软件有限公司</div>
	</div>
</body>
</html>