package com.hgsoft.common.utils;

import java.io.IOException;

/**
 * 
 * @author Administrator
 *
 */
public class GpsAddressUtil {
	/*pi: 圆周率。
	a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
	ee: 椭球的偏心率。
	x_pi: 圆周率转换量。
	transformLat(lat, lon): 转换方法，比较复杂，不必深究了。输入：横纵坐标，输出：转换后的横坐标。
	transformLon(lat, lon): 转换方法，同样复杂，自行脑补吧。输入：横纵坐标，输出：转换后的纵坐标。
	wgs2gcj(lat, lon): WGS坐标转换为GCJ坐标。
	gcj2bd(lat, lon): GCJ坐标转换为百度坐标。*/
	static double pi = 3.14159265358979324;
	static double a = 6378245.0;
	static double ee = 0.00669342162296594323;
	private final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	private static double[] wgs2bd(double lat, double lon) {
	       double[] wgs2gcj = wgs2gcj(lat, lon);
	       double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
	       return gcj2bd;
	}

	private static double[] gcj2bd(double lat, double lon) {
	       double x = lon, y = lat;
	       double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
	       double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
	       double bd_lon = z * Math.cos(theta) + 0.0065;
	       double bd_lat = z * Math.sin(theta) + 0.006;
	       return new double[] { bd_lat, bd_lon };
	}

	private static double[] bd2gcj(double lat, double lon) {
	       double x = lon - 0.0065, y = lat - 0.006;
	       double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	       double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	       double gg_lon = z * Math.cos(theta);
	       double gg_lat = z * Math.sin(theta);
	       return new double[] { gg_lat, gg_lon };
	}

	private static double[] wgs2gcj(double lat, double lon) {
	       double dLat = transformLat(lon - 105.0, lat - 35.0);
	       double dLon = transformLon(lon - 105.0, lat - 35.0);
	       double radLat = lat / 180.0 * pi;
	       double magic = Math.sin(radLat);
	       magic = 1 - ee * magic * magic;
	       double sqrtMagic = Math.sqrt(magic);
	       dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	       dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	       double mgLat = lat + dLat;
	       double mgLon = lon + dLon;
	       double[] loc = { mgLat, mgLon };
	       return loc;
	}

	private static double transformLat(double lat, double lon) {
	       double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
	       ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
	       ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
	       ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi  / 30.0)) * 2.0 / 3.0;
	       return ret;
	}

	private static double transformLon(double lat, double lon) {
	       double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
	       ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
	       ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
	       ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
	       return ret;
	}
	public String getAddress(String x,String y) throws IOException{
				//y=23.168887, x=113.333817
				double[] db=wgs2bd(Double.valueOf(y),Double.valueOf(x));//(y,x)
				/*System.out.println(db[0]);
				System.out.println(db[1]);*/
				String url="http://api.map.baidu.com/geocoder/v2/?ak=702632E1add3d4953d0f105f27c294b9&callback=renderReverse&"
						+ "location="+db[0]+","+db[1]+"&output=json&pois=0 ";
//				java.net.URL l_url = new java.net.URL(url); 
//		        java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
//		        l_connection.setDoInput(true);        //设置输入流采用字节流
//		        l_connection.setDoOutput(true);        //设置输出流采用字节流
//                l_connection.setRequestMethod("POST");
//                l_connection.setUseCaches(false);    //设置缓存
//                l_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                l_connection.setRequestProperty("Charset", "utf-8");

//		        l_connection.setRequestMethod("GET");
//		        l_connection.setRequestProperty("Content-Type","text/javascript;charset=utf-8");
//		        l_connection.setRequestProperty("Cache-Control","no-cache");
//		        l_connection.setRequestProperty("Charsert", "UTF-8");
//		        l_connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
//		        l_connection.connect();
//		        java.io.InputStream l_urlStream= l_connection.getInputStream();    
//		        java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));     
//		        String str=l_reader.readLine();
//		        System.out.println(str);
//		        String s="\""+","+"\""+"business"+"\""+":";
//		        String[] strs=str.split(s, 2);
//		        String s1="\""+"formatted_address"+"\""+":"+"\""; 
//		        String[] a=strs[0].split(s1, 2);
//		        String address=a[1];
//		        System.out.println(address);
//		        System.out.println(new String(address.getBytes("GBK"),"UTF-8"));
//		        
//		        System.out.println("hhhh:"+HttpUtil.httpGet(url).body().html().replaceAll("&quot;", "\"").replaceAll("&amp;", "&"));
		        
		        String  str = HttpUtil.httpGet(url).body().html().replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
		        System.out.println(str);
		        String ss="\""+","+"\""+"business"+"\""+":";
		        String[] strs1=str.split(ss, 2);
		        String s1s="\""+"formatted_address"+"\""+":"+"\""; 
		        String[] aa=strs1[0].split(s1s, 2);
		        String address=aa[1];
		       /* String jsonStr = str;
		        String[] address1 = jsonStr.split("\\(");
	            String[] addrJson = address1[1].split("\\)");
	            
	            JSONObject jsonObject = JSONObject.fromObject(addrJson[0]);
	            JSONObject addressComponent = jsonObject.getJSONObject("result").getJSONObject("addressComponent");
	            String city = addressComponent.getString("city");
	            String district = addressComponent.getString("district");
	            System.out.println("city:"+city);
	            //北京市
	            System.out.println("district:"+district);
	            //海淀区
*/		return address;
	}
	
	public static void main(String[] args) {
		double[] d = bd2gcj(23.158301,113.358381);
		System.out.println(d[0]+","+d[1]);
		double[] dd = gcj2bd(23.158301,113.358381);
		System.out.println(dd[0]+","+dd[1]);
	}
}
