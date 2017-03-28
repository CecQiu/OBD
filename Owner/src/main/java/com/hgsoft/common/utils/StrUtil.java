/**
 * 
 */
package com.hgsoft.common.utils;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 
 */
public class StrUtil {

	/**
	 * 根据多少字节数来截取字符串（2个字符代表一个字节）
	 * 
	 * @param str
	 *            字符串
	 * @param byteSum
	 *            截取多少个字节
	 * @return 截取的字符串，截取剩下的字符串
	 * @throws Exception
	 */
	public static String[] cutStrByByteNum(String str, int byteSum)
			throws Exception {
		if (str.length() < byteSum) {
			throw new Exception("错误！字符串长度不够截取");
		}
		String cut = str.substring(0, byteSum * 2);// 2个字符代表一个字节
		String cutLeft = str.substring(byteSum * 2);
		return new String[] { cut, cutLeft };
	}

	/**
	 * 根据起始位置和，所占byte数截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @param byteTotal
	 *            所占byte总数
	 * @return
	 */
	public static String subStrByByteNum(String str, int start, int byteSum) {
		int n = 2;// 一个十六进制占2个位置
		return str.substring(start, start + n * byteSum);
	}

	/**
	 * 根据起始位置，和总长度截取
	 * 
	 * @param str
	 * @param start
	 * @param length
	 * @return
	 */
	public static String subStrByLen(String str, int start, int length) {
		return str.substring(start, start + length);
	}

	/**
	 * 根据前面参数的byte总数返回当前参数在字符串的起始下标
	 * 
	 * @param byteSum
	 * @return
	 */
	public static int subStrIndexByByteNum(int byteSum) {
		return 2 * byteSum;
	}

	/**
	 * 十六进制字符串转成asc2码表
	 * 
	 * @param bytes
	 * @return
	 */
	public static String hexStrToASC2(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		String hexString = "0123456789ABCDEF";
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * 每2个十六进制的字符串截取出来,转成asc2码
	 * 
	 * @param str
	 *            源字符串
	 * @param num
	 *            截取长度
	 * @return
	 */
	public static List<String> StrToStrArrByNum(String str, int num) {
		List<String> strArr = new ArrayList<String>();
		for (int i = 0; i < str.length(); i = i + num) {
			strArr.add(str.substring(i, i + num));
		}
		return strArr;
	}

	/**
	 * 将十六进制的list集合转成asc2的字符串
	 * 
	 * @param list
	 *            将asc2码截取出来
	 * @return
	 */
	public static String hexListToASC2Str(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(hexStrToASC2(string));
		}
		return sb.toString();
	}

	/**
	 * 将十六进制的集合转成ASC2码集合
	 * 
	 * @param hexList
	 *            十六进制的集合
	 * @return
	 */
	public static List<String> hexListToASC2List(List<String> hexList) {
		List<String> list = new ArrayList<String>();
		for (String string : hexList) {
			list.add(hexStrToASC2(string));
		}
		return list;
	}

	public static List<String> listToCompleteStrList(List<String> list, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		return StrToStrArrByNum(sb.toString(), len);
	}

	/**
	 * 将list集合拼接成字符串
	 * 
	 * @param list
	 * @param rex
	 * @return
	 */
	public static String strListToStr(List<String> list, String rex) {
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string + rex);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	/**
	 * 如果位数不够，前面补字母
	 * 
	 * @param str
	 * @param len
	 * @param app
	 * @return
	 */
	public static String strAppendByLen(String str, int len, String app) {
		StringBuffer sb = new StringBuffer();
		if (str.length() <= len) {
			return sb.append(app).append(str).toString();
		} else {
			return sb.append(str).toString();
		}
	}

	/**
	 * 指定字符串长度，如果长度不够，前面或后面补flag
	 * 
	 * @param str
	 *            原字符串
	 * @param totalLen
	 *            限定长度
	 * @param beforeOrAfter
	 *            0前面补，1后面补
	 * @param flag
	 *            补充字符
	 * @return 指定长度的字符串
	 */
	public static String strAppend(String str, int totalLen, int beforeOrAfter,
			String flag) {
		StringBuffer sb = new StringBuffer("");
		int strLen = str.length();
		if (strLen < totalLen) {
			int mLen = totalLen - strLen;
			for (int i = 0; i < mLen; i++) {
				sb.append(flag);
			}
			// 0前面补
			if (0 == beforeOrAfter) {
				sb.append(str);
			} else if (1 == beforeOrAfter) {
				sb.insert(0, str);
			}
		} else if (strLen == totalLen) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 拼接相同的字符串ffff
	 * 
	 * @param totalLen
	 *            总长度
	 * @param flag
	 *            字符
	 * @return 字符串
	 */
	public static String strSame(int totalLen, String flag) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < totalLen; i++) {
			sb.append(flag);
		}
		return sb.toString();
	}

	/**
	 * 统计子字符串在源字符串中出现次数
	 * 
	 * @param source
	 * @param regexNew
	 * @return
	 */
	public static int subStringTimes(String source, String regexNew) {
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

	// 16进制转成2进制格式：65 -> 01100101
	private static final String HEX_Binary_FORMAT = "00000000";

	/**
	 * 16进制转成2进制，高位在前
	 * 
	 * @param hexStr
	 *            16进制数（2的倍数位）
	 * @return 二进制数组
	 * @throws Exception
	 */
	public static char[] hexToBinary(String hexStr) throws Exception {
		char[] charArr = hexToBinary2(hexStr);
		swap(charArr);// 高位在前
		return charArr;
	}

	/**
	 * 16进制转成2进制
	 * 
	 * @param hexStr
	 *            16进制数（2的倍数位）
	 * @return 二进制数组
	 * @throws Exception
	 */
	public static char[] hexToBinary2(String hexStr) throws Exception {
		int length = hexStr.length();
		if (length % 2 != 0) {
			throw new Exception("位数不是2的倍数");
		}
		DecimalFormat df = new DecimalFormat(HEX_Binary_FORMAT);
		char[] charArr = new char[length * 4];
		for (int i = 0; i < length / 2; i++) {
			char[] c = df.format(
					Double.valueOf(Integer.toBinaryString(Integer.valueOf(
							hexStr.substring(i * 2, i * 2 + 2), 16))))
					.toCharArray();
			System.arraycopy(c, 0, charArr, i * 8, c.length);
		}
		return charArr;
	}
	
	/**
	 * 二进制转为16进制，高位在前 如：'0','0','0','1','1','1','0','1' -> b8
	 * 
	 * @param binary
	 * @return
	 * @throws Exception
	 */
	public static String binary2Hex(char[] binary) throws Exception {
		swap(binary);// 高位在前
		if (binary.length % 8 != 0) {
			throw new Exception("不合格的二进制数组(16进制值)");
		}
		String hex = "";
		for (int i = 0; i < binary.length; i++) {
			String s1 = String.valueOf(binary[i]);
			String s2 = String.valueOf(binary[++i]);
			String s3 = String.valueOf(binary[++i]);
			String s4 = String.valueOf(binary[++i]);
			Integer ii = Integer.valueOf(s1) * (int) Math.pow(2, 3)
					+ Integer.valueOf(s2) * (int) Math.pow(2, 2)
					+ Integer.valueOf(s3) * (int) Math.pow(2, 1)
					+ Integer.valueOf(s4) * (int) Math.pow(2, 0);
			hex += Integer.toHexString(ii);
		}
		return hex;
	}

	/**
	 * 16进制数转换为二进制 支持单双个
	 * @param hex
	 * @return
	 */
	public static char[] hex2Bin(String hexStr){
		int length = hexStr.length();
		String HEX_Binary_FORMAT = "0000";
		char[] charArr = new char[length * 4];
		DecimalFormat df = new DecimalFormat(HEX_Binary_FORMAT);
		
		for (int i = 0; i < length; i++) {
			char[] c = df.format(
					Double.valueOf(Integer.toBinaryString(Integer.valueOf(
							hexStr.substring(i * 1, i * 1 + 1), 16))))
					.toCharArray();
			System.arraycopy(c, 0, charArr, i * HEX_Binary_FORMAT.length(), c.length);
		}
		return charArr;
	}
	
	// 数组反转
	public static void swap(char strs[]) {
		int len = strs.length;
		int as = len / 2;
		for (int i = 0; i < as; i++) {
			char tmp = strs[i];
			strs[i] = strs[len - 1 - i];
			strs[len - 1 - i] = tmp;
		}
	}

	public static String obdSnChange(String obdSn) throws Exception{
		try {
			if(obdSn.length()>13){
				throw new Exception("激活表面号长度有误,请联系管理员.");
			}
			//如果设备号是以44开头
				String otype= obdSn.substring(2,3);//设备类型
				String oyear= obdSn.substring(3,5);//年份
				String obatch= obdSn.substring(5,6);//批次
				String oSn = obdSn.substring(6,12);//设备号
				System.out.println(otype+"***"+oyear+"***"+obatch+"***"+oSn);
				Integer itype = Integer.parseInt(otype);
				Integer iyear = Integer.parseInt(oyear);
				Integer ibatch = Integer.parseInt(obatch);
				Integer iSn = Integer.parseInt(oSn);
				if("51".equals(oyear)){
					//如果年份解析是51,单独解析
			    	String oSnHex=Integer.toHexString(iSn);
			    	String obdSnAll=StrUtil.strAppend(oSnHex, 5, 0, "0");
			    	obdSnAll="2f1"+obdSnAll;
			    	return obdSnAll;
				}
				
				String itypes = Integer.toBinaryString(itype) +"";
				String iyears = Integer.toBinaryString(iyear) +"";
				String ibatchs = Integer.toBinaryString(ibatch) +"";
				String iSns = Integer.toBinaryString(Integer.parseInt(oSn)) +"";
				String otypeBS = StrUtil.strAppend(itypes, 3, 0, "0");
				String oyearBS = StrUtil.strAppend(iyears, 5, 0, "0");
				String obatchBS = StrUtil.strAppend(ibatchs, 4, 0, "0");
				String oSnBS = StrUtil.strAppend(iSns, 20, 0, "0");
				System.out.println(otypeBS+"***"+oyearBS+"***"+obatchBS+"***"+oSnBS);
				StringBuffer sb = new StringBuffer("");
				sb.append(otypeBS).append(oyearBS).append(obatchBS).append(oSnBS);
				System.out.println(sb.toString());
				String obdSnAll=ByteUtil.binaryString2hexString(sb.toString());
				
				System.out.println(obdSnAll);
				return obdSnAll;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new Exception(".......obd激活绑定接口,表面号解析成设备号出错.");
		}
    	
	}

	/**
	 * 格式化字符串，每两个字符1空格
	 * 
	 * @param str
	 * @return
	 */
//	public static String format2Char(String str) {
//		String str1 = "";
//		char[] ccc = str.toCharArray();
//		for (int i = 0; i < ccc.length - 1;) {
//			str1 += "" + ccc[i] + ccc[i + 1] + " ";
//			i += 2;
//		}
//		return str1;
//	}
//	public static String format2CharN(String str) throws Exception {
//		if(StringUtils.isEmpty(str)){
//			throw new Exception("输入参数不能为空！");
//		}
//		StringBuffer sb = new StringBuffer();
//		int size = str.length();
//		if(size % 2 != 0){
//			throw new Exception("输入参数不合法！");
//		}
//		for (int i = 0; i < size/2; i++) {
//			sb.append(str.substring(i*2, (i+1)*2));
//			sb.append(" ");
//		}
//		return sb.toString();
//	}
	public static String format2Char(String str) throws Exception {
		if(StringUtils.isEmpty(str)){
			throw new Exception("输入参数不能为空！");
		}
		StringBuffer sb = new StringBuffer();
		char[] ccc = str.toCharArray();
		int size = ccc.length;
		if(size % 2 != 0){
			throw new Exception("输入参数不合法！");
		}
		for (int i = 0; i < size - 1; i += 2) {
			sb.append(ccc[i]);
			sb.append(ccc[i+1]);
			sb.append(" ");
		}
		return sb.toString();
	}
	/**
	 * 16进制转acsii字符 如：4d->M
	 * @param s
	 * @return
	 */
	public static String hex2ASCII(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "ASCII");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(obdSnToObdMsn("2f100028"));
		System.out.println(hexToBinary2("2a"));
		DecimalFormat df = new DecimalFormat("0000");
		String str = df.format(Double.valueOf(Integer.toBinaryString(Integer.valueOf("a", 16))));
		System.out.println(str);
		System.out.println(hex2Bin("a34"));
		System.out.println(Integer.valueOf("1010", 2));
		System.out.println(""+'1'+'2'+'3');
		System.out.println(String.valueOf('2'));
		System.out.println(Integer.valueOf("a3",16));
		System.out.println(hexToBinary("223311"));
		System.out.println(StrUtil.strAppend(Integer.toHexString(132),2, 0, "0"));
		System.out.println(isDigit("124°44"));
	}
	
	/**
	 * obd设备号转表面号
	 * 现在设备都是44开头,如果开头不是44开头,要另外讨论
	 * 00000000000000002f100028
	 * 000000000000000030100d78
	 * @param obd
	 * @return
	 * @throws Exception
	 */
    public static String obdSnToObdMsn(String obdSn) throws Exception{
    	try {
			//截取后面8位
			obdSn = obdSn.substring(obdSn.length()-8,obdSn.length());
			System.out.println("8位码："+obdSn);
			//如果设备号以2f1开头
			if(obdSn.startsWith("2f1")){
				String oSnH=obdSn.substring(obdSn.length()-5,obdSn.length());
				String oSnInt=Integer.valueOf(oSnH,16).toString();
				oSnInt = StrUtil.strAppend(oSnInt, 6, 0, "0");
				String obdSnAll= "441510"+oSnInt;
				System.out.println(obdSnAll);
				return obdSnAll;
			}
			//16进制转成2进制
			String obdBinary= ByteUtil.hexStrToBinaryStr(obdSn);
			System.out.println("二进制："+obdBinary);
			String otypeB= obdBinary.substring(0,3);
			String oyearB=obdBinary.substring(3, 8);
			String obatchB=obdBinary.substring(8,12);
			String oSnB=obdBinary.substring(12, 32);
			System.out.println(otypeB+"***"+oyearB+"****"+obatchB+"***"+oSnB);
			String otypeI=Integer.valueOf(otypeB,2).toString();
			String oyearI=Integer.valueOf(oyearB,2).toString();
			String obatchI=Integer.valueOf(obatchB,2).toString();
			String oSnI=Integer.valueOf(oSnB,2).toString();
			System.out.println(otypeI+"***"+oyearI+"****"+obatchI+"***"+oSnI);
			if(otypeI.length()>1||oyearI.length()>2||obatchI.length()>1||oSnI.length()>6){
				throw new Exception("设备号解析有误,请联系管理员.");
			}
			String otype=otypeI;
			String oyear= StrUtil.strAppend(oyearI, 2, 0, "0");
			String obatch=obatchI;
			String oSn= StrUtil.strAppend(oSnI, 6, 0, "0");
			System.out.println(otype+"***"+oyear+"****"+obatch+"***"+oSn);
			String obdSnAll="44"+otypeI+oyear+obatchI+oSn;
			System.out.println(obdSnAll);
			String checkCode=add(obdSnAll)+"";
			checkCode =checkCode.substring(checkCode.length()-1,checkCode.length());
			obdSnAll = obdSnAll + checkCode;
			System.out.println(obdSnAll);
			return obdSnAll;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("obd设备号转表面号出错.");
		}
	}
	
	/**
	 * 任意自然数以字符串形式为参数输入，将奇数位和偶数位的个数分别组合再相加输出。
	 * @param str
	 * @return
	 */
	 public static int add(String str){
	    	int odd = 0;
	    	int even = 0;
	    	 
	    	for(int i=0;i<str.length();i++){
	    		if(i%2==0){
	    			odd+=Integer.parseInt(str.substring(i,i+1));
	    		} else
	    			even+=Integer.parseInt(str.substring(i,i+1));
	    	}
	    	System.out.println(odd+"**"+even);
	    	return odd + even*2;
	    }
	 
	// 判断一个字符串是否都为数字  
	 public static boolean isDigit(String strNum) {  
	     Pattern pattern = Pattern.compile("[0-9]{1,}");  
	     Matcher matcher = pattern.matcher((CharSequence) strNum);  
	     return matcher.matches();  
	 }
	 /**
	  * 替换指定位置的字符串
	  * @param string
	  * @param first
	  * @param end
	  * @param stringRep
	  * @return
	  * @throws Exception
	  */
	 public static String StringReplaceByIndex(String string,Integer first,Integer end,String stringRep) throws Exception{
		 try {
			StringBuilder sb = new StringBuilder(string);
			 sb.replace(first, end, stringRep);
			 return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("字符串替换有误:"+string+"---"+first+"---"+end+"---"+stringRep);
		}
	 }
	 /**
	  * 将List<String>集合转成数据库查询的in('','')的条件参数
	  * @param sl 
	  * @return
	  */
	 public static String MapToString(Map<String, Integer> sl){
		StringBuffer sb = new StringBuffer("(");
		for (String key : sl.keySet()) {
			sb.append("'"+key+"',");
		}
    	sb.replace(sb.lastIndexOf(","), sb.length(), ")");
    	return sb.toString();
	 }
	 
	 /**
	  * 将List<String>集合转成数据库查询的in('','')的条件参数
	  * @param sl 
	  * @return
	  */
	 public static String SetToString(Set<String> set){
		StringBuffer sb = new StringBuffer("(");
		for (String string : set) {
			sb.append("'"+string+"',");
		}
    	sb.replace(sb.lastIndexOf(","), sb.length(), ")");
    	return sb.toString();
	 }
	 
	 public static String intSetToString(Set<Integer> set){
			StringBuffer sb = new StringBuffer("(");
			for (Integer i : set) {
				sb.append(""+i+",");
			}
	    	sb.replace(sb.lastIndexOf(","), sb.length(), ")");
	    	return sb.toString();
		 }
	 
	 /**
	  * 正则表达式校验字符串是否只包含某些字符
	  * @param string
	  * @param rex
	  * @return
	  */
	 public static boolean stringRex(String string,String rex){
		Pattern p = Pattern.compile(rex);
    	Matcher m = p.matcher(string);
    	if(m.find()){
    		return true;
    	}
    	return false;
	 }
	 
	 /**
	  * 判断字符数组是否存在某个子字符
	  * @param stringArr 字符数组
	  * @param string 子字符
	  * @return
	 * @throws Exception 
	  */
	public static boolean strArrayExist(String[] stringArr, String string) throws Exception {
		if(stringArr==null){
			throw new Exception("源字符串数组不能为空.");
		}
		for (String str : stringArr) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 字符串对换
	 * @param str1 源字符串
	 * @param str2 值1
	 * @param str3 值2
	 * @return
	 * @throws Exception
	 */
	public static String strExchange(String str1,String str2,String str3) throws Exception{
		if(str2.equals(str1)){
			return str3;
		}else if(str3.equals(str1)){
			return str2;
		}else{
			throw new Exception("源字符串有误,请联系管理员.");
		}
	}
	
	/**
	 * 字符串翻转过来
	 * @param str
	 * @return
	 */
	public static String swapWords(String str) {
		StringBuffer sb = new StringBuffer(str).reverse();

        return sb.toString();
	}
	/**
	 * 判断多个字符串中是否包含空字符串
	 * @param stringArr
	 * @return
	 */
	public static boolean arraySubNotNull(String...stringArr){
    	for (String string : stringArr) {
			if(!StringUtils.isEmpty(string)){
				return true;
			}
		}
    	return false;
    }
	
	/**
	 * 判断多个字符串中是否包含字字符串
	 * @param stringArr
	 * @return
	 */
	public static boolean arrayContainSub(String sub,String...stringArr){
    	for (String string : stringArr) {
			if(sub.equals(string)){
				return true;
			}
		}
    	return false;
    }
	/**
	 * 判断两个集合中是否有相同元素
	 * 只支持基本数据类型
	 * 实体类的话可以在类中implement hashcode()这样的话实体之间可以用equal进行比较
	 * @param stringArr
	 * @return
	 */
	public static boolean SetContain(Set<Object> set1,Set<Object> set2)throws Exception{
		set1.retainAll(set2);
		if(set1.size()>0){
			return true;
		}
    	return false;
    }
	
	//判断数组中是否有重复值
	public static boolean checkRepeat(String[] array){
	    Set<String> set = new HashSet<String>();
	    for(String str : array){
	        set.add(str);
	    }
	    if(set.size() != array.length){
	        return false;//有重复
	    }else{
	        return true;//不重复
	    }
	 
	}
	
	public static String stringCutLastSub(String string,String sub){
		if(string.endsWith(sub)){
			string = string.substring(0, string.length()-1);
		}
		return string;
	}
	/**
	 * 注意两个参数数组里的元素类型必须是同样基本类型,否则异常
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static boolean arraySame(Object[] array1,Object[] array2){
		try {
			Arrays.sort(array1);
			Arrays.sort(array2);
			if (Arrays.equals(array1, array2)) {
				//元素相同
				return true;
			} else {
				//两个数组中的元素值不相同
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 判断两个list集合元素完全相同
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
	    if(a.size() != b.size())
	        return false;
	    Collections.sort(a);
	    Collections.sort(b);
	    for(int i=0;i<a.size();i++){
	        if(!a.get(i).equals(b.get(i)))
	            return false;
	    }
	    return true;
	}
	
}
