jQuery.extend(jQuery.validator.messages, {
    required: "必选字段",
    remote: "该字段已存在",
    email: "请输入正确格式的电子邮箱",
    url: "请输入合法的网址",
    date: "请输入合法的日期",
    dateISO: "请输入合法的日期 (ISO).",
    number: "请输入合法的数字",
    digits: "只能输入整数",
    creditcard: "请输入合法的信用卡号",
    equalTo: "两次密码不一致",
    accept: "请输入拥有合法后缀名的字符串",
    maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
    minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
    rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
    range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
    max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
    min: jQuery.validator.format("请输入一个最小为 {0} 的值")
});

jQuery.validator.addMethod("decimal", function (value, element) {
    var decimal = /^\d+(\.\d+)?$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请正确输入金额!"));

jQuery.validator.addMethod("int", function (value, element) {
    var decimal = /^[0-9]*[1-9][0-9]*$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请输入整数!"));

jQuery.validator.addMethod("fint", function (value, element) {
    var decimal = /^[0-9a-zA-Z]{4}$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请输入4位标识编号!"));

jQuery.validator.addMethod("nint", function (value, element) {
    var decimal = /^[0-9]{1,10}$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请输入十位以内整数!"));

jQuery.validator.addMethod("fare", function (value, element) {
    var decimal = /^\d{1,2}(\.\d{0,2})?$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请输入正确的数字 !"));

jQuery.validator.addMethod("disNum", function (value, element) {
    var decimal = /^\d{1,2}(\.\d{0,2})?$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请输入正确的数字 !"));

jQuery.validator.addMethod("nuclearLoad", function (value, element) {
    var decimal = /^(0|[1-9]\d*)(\.\d{0,2})?$/;
    return this.optional(element) || (decimal.test(value));
}, $.validator.format("请输入正确的数字 (小数最多两位)!"));

jQuery.validator.addMethod("inta", function (value, element) {
    var decimal = /^[0-9]*[1-9][0-9]*$/;
    return this.optional(element) || (decimal.test(value) && value <= 60);
}, $.validator.format("请输入一个1-60的整数!"));

jQuery.validator.addMethod("percent", function (value, element) {
    var decimal = /^[0-9]*[0-9][0-9]*$/;
    return this.optional(element) || (decimal.test(value) && value <= 100);
}, $.validator.format("请输入一个0-100的整数!"));

jQuery.validator.addMethod("maxlengthTwenty", function (value, element) {
    return this.optional(element) || ((Len(value) <= 45));
}, $.validator.format("长度不能大于45（汉字算两个)"));

jQuery.validator.addMethod("maxlengthRemark", function (value, element) {
    return this.optional(element) || ((Len(value) <= 255));
}, $.validator.format("长度不能大于255（汉字算两个)"));

jQuery.validator.addMethod("max100000", function (value, element) {
    return this.optional(element) || (value <= 100000);
}, $.validator.format("不能大于100000"));

jQuery.validator.addMethod("jpgFile", function (value, element) {
    var temp = value.substring(value.length - 4, value.length);
    return this.optional(element) || (temp == ".jpg");
}, $.validator.format("*.jpg"));

jQuery.validator.addMethod("imgFile", function (value, element) {
	var re = /(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG)$/g;
	return this.optional(element) || (re.test(value));
	//var temp = value.substring(value.length - 4, value.length);
	//return this.optional(element) || (temp == ".jpg");
}, $.validator.format("图片文件格式不正确"));

jQuery.validator.addMethod("ftN", function (value, element) {
    var decimal = /^[0-9]*[1-9][0-9]*$/;
    return this.optional(element) || (decimal.test(value) && Len(value) == 14);
}, $.validator.format("输入14位卡号"));
	 
jQuery.validator.addMethod("UID", function (value, element) {
	return this.optional(element) || isIdCardNo(value);
}, "请正确输入您的身份证号码");

jQuery.validator.addMethod("maxlength_T", function (value, element, param) {
    return this.optional(element) || ((Len(value) <= param));
}, $.validator.format("长度不能大于 {0} （汉字算两个)"));

jQuery.validator.addMethod("mobile", function (value, element) {
    var length = value.length;
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[0-9]{1}))+\d{8})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

jQuery.validator.addMethod("alnum", function (value, element) {
    return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
}, "只能包括英文字母和数字");
jQuery.validator.addMethod("vehiclePlate", function (value, element) {
    var temp = /^(([\u4e00-\u9fa5]{1})|([A-Z]{1}))[A-Z]{1}[A-Z_0-9]{4}(([\u4e00-\u9fa5]{1})|([A-Z_0-9]{1}))$/;
    var temp1 = /^WJ[0-9]{2}-([\u4e00-\u9fa5]{1})|([0-9]{1})[0-9]{4}$/;
    return this.optional(element) || (temp.test(value)) || (temp1.test(value));
}, $.validator.format("请输入正确的车牌号码"));

jQuery.validator.addMethod("multiples", function (value, element, param) {
    return this.optional(element) || ((value % param) == 0);
}, $.validator.format("必须是{0}的倍数"));

/*
 校验卡表面号和车牌是否一致
 */
jQuery.validator.addMethod("faceCardNumCorrect", function (value, element) {
    var response;
    //alert(value+":"+element+":"+params);
    var dom = jQuery(element).parents("tr").find("td > input[name*=vehiclePlate]");
    var vehplate = dom.attr('value');
    /* $("input[name*=vehiclePlae]").each */
    var datas = {};
    datas.cardNum = value;
    datas.plate =  encodeURIComponent(vehplate);
    $.ajax({
        type: "POST",
        url: "upgrade_checkPlate.do",
        data: datas,
        async: false,
        success: function (data) {
            response = data;
        }
    });
    if (response == 1) {
        return true;
    }
    else if (response == 0) {
        return false;
    }
}, "卡表面号和车牌不一致");

/*
 车牌和校验卡表面号是否一致
 */
jQuery.validator.addMethod("vehPlateCorrect", function (value, element) {
    var response;
    var dom = jQuery(element).parents("tr").find("td > input[name*=faceCardNum]");
    var faceCardNum = dom.attr('value');

    var datas = {};
    datas.cardNum = faceCardNum;
    datas.plate =  encodeURIComponent(value);
    $.ajax({
        type: "POST",
        url: "upgrade_checkPlate.do",
        data: datas,
        async: false,
        success: function (data) {
            response = data;
        }
    });
    if (response == 1) {
        return true;
    }
    else if (response == 0) {
        return false;
    }
}, "车牌和卡表面号不一致");

/*
 卡类型输入是否正确
 */
jQuery.validator.addMethod("cardTypeInput", function (value, element) {
    var dom = jQuery(element).parents("tr").find("td > input[name*=cardType]");
    var cardType = dom.attr('value');

    if (cardType == "储值卡"||cardType == "记账卡") {
        return true;
    }
    else  {
        return false;
    }
}, "卡类型需填储值卡或者记账卡");


jQuery.validator.addMethod("areaExist", function (value, element) {
    var flag = 1;
    $.ajax({
        type: "POST",
        url: "area_checkNameExist.do",
        async: false,
        data: {'area.name': value},
        success: function (msg) {
            if (msg == "true") {
                flag = 0;
            }
        }
    });
    if (flag == 0) {
        return false;
    } else {
        return true;
    }
});
jQuery.validator.addMethod("myRemote", function (value, element, param) {
    //var flag=1;

    var datas = {};
    var validator = this;
    datas[$(element).attr("name")] = value;
    var previous = validator.previousValue(element);
    $.ajax({
        type: "GET",
        url: param,
        async: false,
        data: datas,
        success: function (msg) {
            var valid = false;
            if (msg == "true") {
                var submitted = validator.formSubmitted;
                validator.prepareElement(element);
                validator.formSubmitted = submitted;
                validator.successList.push(element);
                validator.showErrors();
                valid = true;
            } else {
                var errors = {};
                if (msg.length > 20) {
                    msg = "系统错误！";
                }
                if (msg == "false") {
                    var message = validator.defaultMessage(element, "myRemote");
                } else {
                    var message = msg;
                }

                //var message = response!=null;

                errors[element.name] = message;
                validator.showErrors(errors);
                valid = false;
                validator.formSubmitted = false;
            }
            previous.valid = valid;
            validator.stopRequest(element, valid);

        }
    });
    return "pending";

});

//IP地址验证
jQuery.validator.addMethod("ip", function(value, element) {
    var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    return this.optional(element) || (ip.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
}, "Ip地址格式错误");

//字节长度验证
jQuery.validator.addMethod("byteLength", function(value, element, param) {
    var length = value.length;
    for (var i = 0; i < value.length;i++) {
        if (value.charCodeAt(i) > 127) {
            length++;
        }
    }
    return this.optional(element) || length <= param;
}, $.validator.format("最多{0}个字符(一个汉字算2个)"));

//Time类型验证
jQuery.validator.addMethod("times", function(value, element) {
    var times = /^(([0-1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]$/;
    return this.optional(element) || times.test(value);
}, "时间格式错误");


//Time类型验证
jQuery.validator.addMethod("datetimes", function(value, element) {
    var datetimes = /^\d{4}[-]\d{2}[-]\d{2} (([0-1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]\.{0,1}\d{0,3}$/;
    return this.optional(element) || datetimes.test(value);
}, "日期 时间格式错误");

//增加身份证验证
function isIdCardNo(num) {
    var factorArr = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
    var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
    var varArray = new Array();
    var intValue;
    var lngProduct = 0;
    var intCheckDigit;
    var intStrLen = num.length;
    var idNumber = num;
    // initialize
    if (intStrLen != 18) {
        return false;
    }
    // check and set value
    for (i = 0; i < intStrLen; i++) {
        varArray[i] = idNumber.charAt(i);
        if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
            return false;
        } else if (i < 17) {
            varArray[i] = varArray[i] * factorArr[i];
        }
    }
    if (intStrLen == 18) {
        //check date
        var date8 = idNumber.substring(6, 14);
        if (isDate8(date8) == false) {
            return false;
        }
        // calculate the sum of the products
        for (i = 0; i < 17; i++) {
            lngProduct = lngProduct + varArray[i];
        }
        // calculate the check digit
        intCheckDigit = parityBit[lngProduct % 11];
        // check last digit
        if (varArray[17] != intCheckDigit) {
            return false;
        }
    }
    else {        //length is 15
        //check date
        var date6 = idNumber.substring(6, 12);
        if (isDate6(date6) == false) {
            return false;
        }
    }
    return true;
}

function isDate6(sDate) {
    if (!/^[0-9]{6}$/.test(sDate)) {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    if (year < 1700 || year > 2500) return false;
    if (month < 1 || month > 12) return false;
    return true;
}

function isDate8(sDate) {
    if (!/^[0-9]{8}$/.test(sDate)) {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    day = sDate.substring(6, 8);
    var iaMonthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    if (year < 1700 || year > 2500) return false;
    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
    if (month < 1 || month > 12) return false;
    if (day < 1 || day > iaMonthDays[month - 1]) return false;
    return true;
}

//验证固定电话  
jQuery.validator.addMethod( "checkTel",function(value,element){     
    var pattern =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;  
    if(value!=''){if(!pattern.exec(value)){return false;}};  
    return true;   
} ,  "请输入有效的固定电话！" );


jQuery.validator.addMethod("bigThan",function(value,element,param){     
    var small = $(param).val();
    if(small == '') {
    	return true;
    }
    if(value > small) {
    	return true;
    }
    return false;   
} ,  "结束时间要大于开始时间！" );

jQuery.validator.addMethod("leThan",function(value,element,param){     
    var small = $(param).val();
    if(small == '') {
    	return true;
    }
    if(value >= small) {
    	return true;
    }
    return false;   
} ,  "结束时间要大于开始时间！" );


jQuery.validator.addMethod("isInstall", function(value, element){
	var point = value.lastIndexOf("."); 
	var type = new String(value.substr(point)).toLowerCase();
	var res = false;
    if(type == ".deb"|| type== ".ipa" || type==".apk" ||type==".xap"){
    	res = true;
    }
    return res;
}, "你选择的类型不符合要求");

jQuery.validator.addMethod("obdUpgradeFile", function (value, element) {
	var re = /(.bin|.BIN)$/g;
	return this.optional(element) || (re.test(value));
}, $.validator.format("只能上传bin格式文件"));

//上传OBD设备EXCEL
jQuery.validator.addMethod("addOBD", function (value, element) {
	var re = /(.xlsx|.xls)$/g;
	return this.optional(element) || (re.test(value));
}, $.validator.format("只能上传EXCEL格式文件"));