if("${result}"=="SUCCESS")
    alert("操作成功");
else if("${result}"=="FAIL")
    alert("操作失败");



$(document).ready(function () {
//    $("#myForm").validate({
//        //debug:true,
//        onkeyup: false,
//        success: function (label) {
//            label.html("&nbsp;").addClass("checked");
//        }
//    });


    //为所有的必填项自动添加红星星
    $("input[class*='required']").each(
        function (index, element) {
            $(element).after(
                '<span style="color: red">&nbsp;*</span>');
        });
    $("select[class*='required']").each(
        function (index, element) {
            $(element).after(
                '<span style="color: red">&nbsp;*</span>');
        });
	$("textarea[class*='required']").each(
			function (index, element) {
				$(element).after('<span style="color: red">&nbsp;*</span>');
            }
	);

    auth();

});

function auth(){

    var authStr = $("auth").attr("value");
    var authArr = authStr.split(";");
    if(authArr==null||authArr.length==0){
        return;
    }
    //找出所有带auth属性的元素，方便后续的便利
    var authObjectArr = $("[auth]");
    if(authObjectArr==null||authObjectArr.length==0){
        return;
    }
    for(var i = 0;i<authObjectArr.length;i++){
        var authObject = authObjectArr[i];
        var authObjectvalue = $(authObject).attr("auth");
        var index = jQuery.inArray(authObjectvalue,authArr);
        if(index==-1){
        	$(authObject).remove();
        }
    }
}