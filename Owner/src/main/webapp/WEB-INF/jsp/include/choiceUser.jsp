<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script>
	$(document).ready(function() {
		$("input[type=submit]").button();
		$("#dialog-form").dialog({
			autoOpen : false,
			height : 300,
			width : 750,
			modal : true,
			position : [ 50, 100 ],
			buttons : {
				<%--
				"取消" : function() {
					var idType = $("select[name='idType']").val();
					var idNum = $("input[name='idNum']").val();
					var name = $("input[name='name']").val();
					alert(idType);
					alert(idNum);
					alert(name);
					$(this).dialog("close");
				}
				--%>
			},
			close : function() {
			}
		});
		$("#dialog-form").tabs();
		$("#search").button().click(function() {
			$("#userTable").html("");
			var idType = $("select[name='idType']").val();
			var idNum = $("input[name='idNum']").val();
			var name = $("input[name='name']").val();
			if(idType==""&&idNum==""&&name==""){
				alert("请选择查询条件");
				return false;
			}
			if(idType!=""&&idNum==""){
				alert("请输入证件号码");
				return false;
			}
			getUserInfo(idType, idNum, name);
		});
	});

	function getUserInfo(idType, idNum, name) {
		$.ajax({
					url : 'user_getUserInfo.do',
					type : 'POST',
					dataType : 'json',
					async : true,
					data:{
						"idType":idType,
						"idNum":idNum,
						"name":encodeURI(encodeURI(name))
					},
					success : function(json) {
						
						if (json != null) {
							//var resultStr = '<tr><th>姓名</th><th>性别</th><th>证件类型</th><th>证件号码</th><th>手机号码</th><th>选择</th></tr>';
                            var resultStr = '<tr><th>姓名</th><th>证件类型</th><th>证件号码</th><th>手机号码</th><th>选择</th></tr>';
							$("#userTable").append(resultStr);
							var i = 0;
							$.each(json,function(index, val) {
								var tdStr = "";
								tdStr +="<tr";
								if(i%2==0){
									tdStr +=" class='bg'";
								}
								tdStr +=">";
								tdStr +='<td>'+val.name+'</td>';
								i++;
								/*
                                var sexStr="";
								switch(val.sex)
								{
								case 0:sexStr = "女";break;
								case 1:sexStr = "男";break;
								}
								tdStr +='<td>'+sexStr+'</td>';
								*/
								var idTypeStr="";
								
								switch(val.idType)
								{
								case '0':idTypeStr = "身份证";break;
								case '1':idTypeStr = "军官证";break;
								case '2':idTypeStr = "护照";break;
								case '3':idTypeStr = "入境证（限港台居民）";break;
								case '4':idTypeStr = "临时身份证";break;
								case '5':idTypeStr = "营业执照";break;
								case '6':idTypeStr = "组织机构代码证";break;
								}
								tdStr +='<td>'+idTypeStr+'</td>';
								tdStr +='<td>'+val.idNum+'</td>';
								tdStr +='<td>'+val.tel+'</td>';
								tdStr +='<td><button class="choice" id="choice'+index+'" style="width: 60px; height: 30px">选择</button></td>';
								tdStr +="</tr>";
								$("#userTable").append(tdStr);
								$('#choice'+index).button().click(function(){
                                    setChoiceUser(val);
									$("#dialog-form").dialog("close");
								});
							});
							
						}
					}
				});
	}
</script>

<div id="dialog-form" title="请选择用户资料">
		<table width="100%" border="0" cellpadding="3" cellspacing="0">
			<tr>
				<td style="text-align:right;width:15%;">证件类型：</td>
				<td style="width:17%;text-align: left;">
					<select id="idType" name="idType" style="width: 100%;">
							<option value="">请选择</option>
							<option value="0">身份证</option>
							<option value="1">军官证</option>
							<option value="2">护照</option>
							<option value="3">入境证（限港台居民）</option>
							<option value="4">临时身份证</option>
							<option value="5">营业执照</option>
							<option value="6">组织机构代码证</option>
					</select>				
				</td>
				<td style="text-align:right;width:15%;">证件号码：</td>
				<td style="width:17%;text-align: left;">
					<input name="idNum" maxlength="18"/>			
				</td>
				<td style="text-align:right;width:12%;">姓名：</td>
				<td style="text-align: left;width: 15%;">
					<input name="name" maxlength="10"/>		
				</td>
				<td>
				<button id="search">搜索</button>
				</td>
			</tr>
		</table>
	<table style="text-align: center; width: 100%" id="userTable" class="ftable" border="0" cellspacing="0" cellpadding="0">


	</table>
</div>