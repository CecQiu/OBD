package com.hgsoft.system.utils;

public class ByteUtil {

	public static void main(String[] args) {

	}

	/**
	 * byte数组转成16进制字符串
	 * 
	 * @param src
	 *            16进制的byte数组
	 * @return 16进制的字符串
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 把16进制的字符串转成byte数组
	 * 
	 * @param hexString
	 *            16进制的字符串
	 * @return 16进制的byte数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 16进制字符串转2进制字符串
	 * 
	 * @param hexString
	 *            16进制字符串
	 * @return 2进制字符串
	 */
	public static String hexStrToBinaryStr(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 字符串转换成十六进制字符串
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	// 把byte 转化为两位十六进制数
	public static String binaryStrToHexStr(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	// ASC2码转十六进制
	public static String ASC2ToHexStr(String asc2Str) {
		byte[] b = asc2Str.getBytes();
		int[] in = new int[b.length];
		for (int i = 0; i < in.length; i++) {
			in[i] = b[i] & 0xff;
		}
		StringBuffer sb = new StringBuffer("");
		for (int j = 0; j < in.length; j++) {
			sb.append(Integer.toString(in[j], 0x10));
		}
		return sb.toString();
	}

	/**
	 * 二进制字符串转十六进制字符串
	 */
	public static String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}
	
	/**
	 * 十进制int值转换成十六进制字符串 弃用！
	 * @param dec
	 * @return
	 */
//	@Deprecated
//	public static String decToHex(int dec) {
//	    String hex = "";
//	    if(dec == 0) {
//	    	return "00";
//	    }
//	    while(dec != 0) {
//	        String h = Integer.toString(dec & 0xff, 16);
//	        if((h.length() & 0x01) == 1)
//	            h = '0' + h;
//	        hex = hex + h;
//	        dec = dec >> 8;
//	    }
//	    return hex;
//	}

}
