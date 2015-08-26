package com.sysware.tldlt.app.local.rpc;

import java.util.HashMap;
import java.util.Map;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

public class UserManage {
	private static final String LOGIN_KEY = "loginKey";
	private static Map<String, Dto> users = new HashMap<String, Dto>();

	public static boolean checkUserKey(String key) {
		return null != findUserDtoByKey(key);
	}

	public static Dto findUserDtoByKey(String key) {
		Dto result = null;
		for (Dto dto : users.values()) {
			if (key.equals(dto.getAsString(LOGIN_KEY))) {
				result = dto;
				break;
			}
		}
		return result;
	}

	private static Dto getDto(String userId) {
		Dto dto = users.get(userId);
		if (null == dto) {
			dto = new BaseDto();
			users.put(userId, dto);
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	public static void loginUser(String userId, String key) {
		Dto dto = getDto(userId);
		dto.put(LOGIN_KEY, key);
		dto.put("userId", userId);
	}

	@SuppressWarnings("unchecked")
	public static void logout(String key) {
		Dto dto = findUserDtoByKey(key);
		if (null != dto) {
			dto.put(LOGIN_KEY, "");
		}
		
	}
}
