package com.hgsoft.obd.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * 基站定位工具
 * @author sujunguang
 * 2015年12月28日
 * 上午10:54:32
 */
@SuppressWarnings("deprecation")
public class LBSUtil {
	
	private static LBSUtil util;
	
	public static LBSUtil getInstance(){
		if(util == null){
			util = new LBSUtil();
		}
		return util;
	}
	
	private LBSUtil() {
		
	}
	/**
	 * CDMA基站定位
	 * @param mcc
	 * @param sid 对应飞豹协议的mnc
	 * @param nid 对应飞豹协议的lac
	 * @param bid 对应飞豹协议的cell
	 * @return {"code":"0","data":{"addr":"广东省 广州市 天河区 S15沈海高速广州支线 靠近广东省城市建设高级技工学校(长福校区)","lat":23.168301,"lng":113.345682,"precision":794},"msg":"success","sid":"10001"}
	 *         code {0:成功;10001:无基站信息;10002:Key已过期;10003:Key非法请求;10004:Key请求次数已超上限}
	 *         
	 * @throws Exception
	 */
	public JSONObject CDMA(String mcc,String sid,String nid,String bid){
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		List<BasicNameValuePair> qparams = new ArrayList<BasicNameValuePair>();
		//飞豹提供的服务id
		qparams.add(new BasicNameValuePair("sid", "10001"));
		int mccI=Integer.parseInt(mcc);
		int mncI=Integer.parseInt(sid);
		int nidI=Integer.parseInt(nid);
		int cellI=Integer.parseInt(bid);
		StringBuffer baseinfo = new StringBuffer();//拼接请求参数
		baseinfo.append("{mcc:").append(mccI);
		baseinfo.append(",mnc:").append(mncI);
		baseinfo.append(",lac:").append(nidI);
		baseinfo.append(",cell:").append(cellI);
		baseinfo.append("}");
		qparams.add(new BasicNameValuePair("baseinfo", baseinfo.toString()));
		qparams.add(new BasicNameValuePair("ky", "5ZGofDJ8MTQ0MjQ1NTIwMDAwMHwxNDUwMzE3NjAwMDAwfDUwMDAwfDEwMDBofDE0NDI0NTUyMDAw"));
		try {
			java.net.URI uri = URIUtils.createURI("http", "121.43.117.211", 8080, "/lbs/lbs",URLEncodedUtils.format(qparams, "UTF-8"), null);
			HttpGet httpGet = new HttpGet(uri);
			System.out.println(httpGet.getURI());
			
			CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpGet);  
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String resp= EntityUtils.toString(entity, "UTF-8");
				JSONObject jb = new JSONObject(resp);
			    httpGet.abort();//简单的终止请求 
			    return jb;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
