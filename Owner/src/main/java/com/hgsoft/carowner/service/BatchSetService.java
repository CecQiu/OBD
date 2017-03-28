package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.BatchSetDao;
import com.hgsoft.carowner.entity.BatchSet;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;

import net.sf.json.JSONObject;
/**
 * obd设置表,包括离线设置
 * @author liujialin
 * 2015-8-5
 */
@Service
public class BatchSetService extends BaseService<BatchSet> {
	
	@Resource
	public void setDao(BatchSetDao batchSetDao){
		super.setDao(batchSetDao);
	}
	
	@Override
	public BatchSetDao getDao() {
		return (BatchSetDao)super.getDao();
	}
	
	public BatchSet queryLastByParams(Map<String, Object> map) {
		return getDao().queryLastByParams(map);
	}
	
	public List<BatchSet> getListByTypes(Set<String> types) {
		return getDao().getListByTypes(types);
	}
	
	public List<BatchSet> queryByParams(Pager pager,Map<String, Object> map) {
		return getDao().queryByParams(pager, map);
	}
	
	public boolean del(String id) {
		 try {
			this.deleteById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	public boolean add(BatchSet batchSet) {
		return getDao().add(batchSet);
	}
	
	public static String batchSetMsg(String msg)throws Exception{
		if(StringUtils.isEmpty(msg)){
			throw new Exception("批量设置报文不能为空.");
		}
		try {
			StringBuffer mess = new StringBuffer("");
			JSONObject json = JSONObject.fromObject(msg);
			if(json.isEmpty() || json.isNullObject()){
				throw new Exception("批量设置报文不能为空.");
			}
			if(!json.containsKey("params")){
				throw new Exception("批量设置报文没有包含params参数.");
			}
			String params =json.optString("params");
			String[] paramsArray= params.split(",");
			if(paramsArray==null || paramsArray.length==0){
				throw new Exception("批量设置报文解析有误1.");
			}
			String total = StrUtil.strAppend(Integer.toHexString(paramsArray.length), 2, 0, "0");
			mess.append(total);//修改处个数
			for (String string : paramsArray) {
				//偏移地址
				String address = json.optString(string+"-address");
				String value = json.optString(string+"-value");
				Integer length = json.optInt(string+"-length");
				if(StringUtils.isEmpty(address) || StringUtils.isEmpty(value) || length==0){
					throw new Exception(string+"---地址或值为空或长度不能为0.");
				}
				mess.append(address.toLowerCase());//地址偏移
				value = value.toLowerCase().replace(",", "");
				String leng = StrUtil.strAppend(Integer.toHexString(length), 2, 0, "0");
				mess.append(leng);//长度
				mess.append(value);//值
			}
			String message = mess.toString();
			if(message.endsWith(",")){
				message = message.substring(0,message.length()-1);
			}
			if(StringUtils.isEmpty(message)){
				throw new Exception(message+"---批量设置报文解析有误2.");
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("批量设置报文解析有误3.");
		}
	}
	
	public static void main(String[] args) {
		String msg ="{\"params\":\"EEPROM1,EEPROM2,FLASH3\",\"EEPROM1-address\":\"000A\",\"EEPROM2-address\":\"0001\",\"FLASH3-address\":\"0800\",\"EEPROM1-length\":5,\"FLASH3-length\":5,\"EEPROM2-length\":2,\"EEPROM1-value\":\"0c,1c,2c,3c,4c\",\"EEPROM2-value\":\"08,10\",\"FLASH3-value\":\"00,00,00,00,4C\"}";
		try {
			String ss=BatchSetService.batchSetMsg(msg);
			System.out.println(ss);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
