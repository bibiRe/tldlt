package com.sysware.tldlt.app.local.rpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;

import com.google.common.collect.Maps;
import com.sysware.tldlt.app.utils.AppTools;

public class RPCRetDto {
	@SuppressWarnings("rawtypes")
	private Collection data = new ArrayList();
	private String message;
	private String success;

	public static RPCRetDto createDto(boolean success, String msg) {
		RPCRetDto result = new RPCRetDto();
		result.success = success ? "1" : "0";
		if (null != msg) {
			result.message = msg;
		} else {
			result.message = "";
		}

		return result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 将此Dto对象转换为Json格式字符串<br>
	 * 
	 * @return string 返回Json格式字符串
	 */
	public String toJson() {
		return toJson(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String toJson(String pFormat) {
		String strJson = null;
		Map map = Maps.newHashMap();
		map.put("success", success);
		map.put("message", message);
		map.put("data", data);
		if (!AppTools.isEmptyString(pFormat)) {
			strJson = JsonHelper.encodeObject2Json(map, pFormat);
		} else {
			strJson = JsonHelper.encodeObject2Json(map);
		}
		return strJson;
	}

	@SuppressWarnings("unchecked")
	public void addData(Dto dto) {
		this.data.add(dto);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addAllData(Collection list) {
		this.data.addAll(list);
	}
}
