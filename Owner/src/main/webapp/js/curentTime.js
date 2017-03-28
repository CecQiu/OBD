function CurentTime() //yyyy-mm-dd hh:mm:ss
{ 
    var now = new Date(); 
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日  
    var hh = now.getHours();            //时
    var mm = now.getMinutes();			//分  
    var ss=now.getSeconds(); 			//秒
    var clock = year + "-";   
    if(month < 10)
        clock += "0";   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
   
     if(hh < 10)
        clock += "0";
    clock += hh + ":";
    if (mm < 10) clock += '0'; 
    clock += mm+":";
    if(ss<10)
   	clock += "0";
    clock += ss;
    return(clock); 
} 
function DateStart() //yyyy-mm-dd
{ 
    var now = new Date(); 
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日  
    var clock = year + "-";   
    if(month < 10)
        clock += "0";   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
    clock+="00:00:00";
    return(clock); 
}
function Data() //yyyy-mm-dd
{ 
    var now = new Date(); 
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日  
    var clock = year + "-";   
    if(month < 10)
        clock += "0";   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
    return(clock); 
} 