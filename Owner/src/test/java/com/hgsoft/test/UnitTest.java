package com.hgsoft.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.NumUtil;
import com.hgsoft.common.utils.StrUtil;

import net.sf.json.JSONObject;

public class UnitTest {
	@Test
	public void listToStr() throws NumberFormatException, Exception{
		List<String> sl= new ArrayList<String>();
		sl.add("123");
		sl.add("245");
		sl.add("568");
		System.out.println(StrUtil.strListToStr(sl, ","));
	}
	@Test
	public void juli() throws NumberFormatException, Exception{
		float f=0.762f / 1000.0f;
		System.out.println(f);
		String ss= NumUtil.getNumFractionDigits((0.762f / 1000.0f) / 50 * 100.0f, 2);
		System.out.println(ss);
	}
	@Test
	public void fls() throws NumberFormatException, Exception{
		String s="12.0";
		Float f=(Float.parseFloat(s));
		Long totalTime = new Long(0);// 总的驾驶时长
		totalTime = (long)f.intValue();
		System.out.println(totalTime);
	}
	@Test
	public void getTimes(){
		System.out.println("888888888888888888888881".length());
	}
	
	@Test
	public void MD5() throws Exception{
		String ss="00000000000000687E9FCA57"+"chezhutong"+"1447134959847"+"czt123456";
		//b9cbdd79e25ab48c707d49def60b4f95
		System.out.println(MD5Coder.encodeMD5Hex(ss));
	}
	@Test
	public void ID() throws Exception{
		for (int i = 0; i < 10; i++) {
			System.out.println(IDUtil.createID().substring(8,18));
		}
	}
	@Test
	public void getDateString() throws Exception{
		System.out.println(DateUtil.getTimeString(new Date(), "yyyyMMddHHmmss"));
	}
	@Test
	public void hexToASC2() throws Exception{
		System.out.println(StrUtil.hexStrToASC2("50"));
	}
	@Test
	public void str() throws Exception{
		String ss="12345678912";
		System.out.println(ss.substring(7,ss.length()));
	}
	@Test
	public void subStrTimes() throws Exception{
		String ss="aa123aa4561278aa91aa2aa";
		String sub="aa";
		System.out.println(finder(ss,sub));
	}
	@Test
	public void jsonStr() throws Exception{
		String ss="{'deviceId':'00000000000000687E9FCA4D','username':'chezhutong','time':'1447134959847','sign':'ae5ebe70109ca7fcf90513ec4192110e','mobileType':0}";
		JSONObject jb=new JSONObject();
		jb=jb.fromObject(ss);
		System.out.println(jb);
	}
	
	public  int finder(String source, String regexNew) {  
        String regex = "[a-zA-Z]+";  
        if (regexNew != null && !regexNew.equals("")) {  
            regex = regexNew;  
        }  
        Pattern expression = Pattern.compile(regex);  
        Matcher matcher = expression.matcher(source);  
        TreeMap<Object, Integer> myTreeMap = new TreeMap<Object, Integer>();  
        int n = 0;  
        Object word = null;  
        Object num = null;  
        while (matcher.find()) {  
            word = matcher.group();  
            n++;  
            if (myTreeMap.containsKey(word)) {  
                num = myTreeMap.get(word);  
                Integer count = (Integer) num;  
                myTreeMap.put(word, new Integer(count.intValue() + 1));  
            } else {  
                myTreeMap.put(word, new Integer(1));  
            }  
        }  
        return n;  
	}  
	@Test
	public void xor() throws Exception {
		String ss="88888888888888888888888800006700071511281434000015112814260000010100010001000100010001000101000100010001010001000101";
		ServerResponses sr=new ServerResponses();
		String r=sr.xor(ss);
		System.out.println(r);
	}
	@Test
	public void re() throws Exception {
		//反转义
		/**需要转义的字符数组*/
		String[][] ESCES = {
			{"5501", "aa"},{"5502", "55"}
		};
		
		String msgBody="8888888888888888888888880355010e01";
		msgBody = msgBody.replaceFirst("5501", "aa");
//		for(String[] strs : ESCES) {
//			msgBody = unEscape(msgBody, strs[0], strs[1]);
//		}
		System.out.println(msgBody);
	}
	@Test
	public void strLen()  {
		String msgBody="asdf";
		System.out.println(StrUtil.strAppend(msgBody, 4, 0, "0"));
	}
	@Test
	public void asc2()  {
		String ss="3031";
		String k=StrUtil.hexStrToASC2(ss);
//		String s=ByteUtil.ASC2ToHexStr(ss);
		System.out.println(k);
	}
	@Test
	public void ss()  {
		String ss="3031";
		String s1=ss.substring(0,2);
		String s2=ss.substring(2,4);
		System.out.println(s1+"***"+s2);
	}
	@Test
	public void checkCode() throws Exception  {
		String ss="8888888888888888888888880000570006a5005215120711000001151207100000000101000000000000000000000000000000000000000000000000001512071100000115120710000000010100000000000000000000000000000000000000000000000000";
		ServerResponses sr=new ServerResponses();
		String str=sr.xor(ss);
		System.out.println(str);
	}
	@Test
	public void strKongGe()  {
		String input = "aa00000000000000687E9FCA57000002000117aa";
        String regex = "(.{2})";
        input = input.replaceAll (regex, "$1 ");
        System.out.println (input);
	}
	@Test
	public void ff()  {
		String ff="FF12";
		System.out.println(ff.toLowerCase());
	}
	@Test
	public void sss()  {
		String s="aaaa125488aaaa256fda62aa";
		int k=StrUtil.subStringTimes(s, "aa");
		System.out.println(k%2);
		String[] ss=s.split("aa");
		int i=0;
		for (String string : ss) {
			if(string.equals("")){
				System.out.println(i++);
			}
			System.out.println(string+"***"+i++);
		}
	}
	@Test
	public void kk() throws Exception  {
		String ss="P1234,P2345,P3456";
		String[] s=ss.split(",");
		int i=0;
		for (String string : s) {
			System.out.println(string+"***"+i++);
		}
	}
	@Test
	public void time() throws Exception  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String ss="20000000110000";
		Date d=sdf.parse(ss);
		System.out.println((String)DateUtil.fromatDate(d, "yyyy-MM-dd hh:mm:ss"));
	}
	@Test
	public void st() throws Exception  {
		String ss="aa8888888888888888888888880000670007151128143400001511281426000001010001000100010001000100010100010001000101000100010172aa";
		System.out.println(ss.length());
	}
	@Test
	public void tt(){
		System.out.println(secToTime(343909));
	}
	
	@Test
	public void strsanme(){
		System.out.println(StrUtil.strSame(12, "0"));
	}
	
	@Test
	public void sub(){
		String ss=StrUtil.strSame(12, "0");
		System.out.println(ss.substring(0, 6));
	}
	//秒数转HH:MM:SS格式时间
	public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    
    @Test
    public void pingjun() {
        float f1 = 10.00f;
        float f2 = 20.0f;
        if(f1<f2){
        	System.out.println(2222);
        }
    }
    
    @Test
    public void zyh() {
    	String zyh="10";
    	System.out.println(Long.parseLong(zyh, 16)*10);
    }
    
    @Test
    public void strshuzi() {
    	String str="1243";
    	Pattern pattern = Pattern.compile("[0-9]*");  
    	boolean flag=pattern.matcher(str).matches();
        System.out.println(flag);
    }
    
    @Test
    public void riqi() {
    	
        System.out.println((String) DateUtil.fromatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void tttt() {
//    	System.out.println(Integer.valueOf("ff01f6e8", 16));
    	System.out.println(Long.valueOf("ff01f6e8", 16));
//    	System.out.println(4278318824);
    	System.out.println(Integer.MAX_VALUE);
    }
    
    @Test
    public void test01(){
    	String s = "aa501005e604018200040002000001750080161127150451ffffffff0900116366842337090600ff0080161127150545ffffffff0900116366842337090600ff0080161203140642ffffffff0900116287722315843100ff0080161203165032ffffffff090080170105201533ffffffff0900116366802337092600ff0080170105222307ffffffff0900116366782337090300ff0080170106031341ffffffff0900116366802337089800ff0080170106092523ffffffff0900116366802337091200ff0080170106125929ffffffff0900116366782337088600ff0080170109103138ffffffff0900116366792337092500ff0080170109104214ffffffff0900116366852337087900ff0080170109104546ffffffff0900116366852337087900ff0080170109144722ffffffff0900116366732337081600ff0080170109151452ffffffff0900116366822337088400ff0080170109151929ffffffff0900116366832337093400ff0080170109153635ffffffff0900116366832337083900ff00000175617caa";
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < s.length(); i++) {
    		sb.append(s.subSequence(i, i+2)+" ");
    		i++;
		}
    	System.out.println(sb);
    }
    
    @Test
    public void test02(){
    	System.out.println(Long.valueOf(222));
    }
    
}
