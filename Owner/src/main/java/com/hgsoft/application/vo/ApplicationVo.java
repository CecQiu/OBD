package com.hgsoft.application.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.hgsoft.application.util.EntityToJsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@SuppressWarnings("serial")
public class ApplicationVo implements Serializable {

	private Map<String, Object> datas = new HashMap<String, Object>();
	
	public ApplicationVo() {
		setStatus(Status.SUCCESS);
		setMessage("");
	}
	
	public ApplicationVo(Status status, String message) {
		super();
		setStatus(status);
		setMessage(message);
	}
	
	public void put(String key, Object value){
		datas.put(key, value);
	}

	public Status getStatus() {
		return (Status) datas.get("status");
	}

	public void setStatus(Status status) {
		put("status", status);
	}

	public String getMessage() {
		return (String)datas.get("message");
	}

	public void setMessage(String message) {
		put("message", message);
	}

	public String getJson() {
		StringBuffer buffer = new StringBuffer("{");
		for(String key: datas.keySet()) {
			buffer.append("\"");
			buffer.append(key);
			buffer.append("\":");
			Object object = datas.get(key);
			if(object instanceof String || object instanceof Enum) {
				buffer.append("\"");
				buffer.append(object);
				buffer.append("\"");
			} else if(object instanceof Collection) {
				JSONArray array = new JSONArray();
				JsonConfig config = new JsonConfig();
				config.setExcludes(new String[]{""});
				array.addAll((Collection)object);
				buffer.append(array.toString());
			} else if(object instanceof Number) {
				buffer.append(object);
			} else if(object.getClass().getName().startsWith("com.hgsoft")){
				buffer.append(EntityToJsonUtil.toJson(object));
			} else {
				buffer.append("\"");
				buffer.append(object);
				buffer.append("\"");
			}
			buffer.append(",");
		}
		buffer.replace(buffer.length()-1, buffer.length(), "}");
		return buffer.toString();
	}
	
}
