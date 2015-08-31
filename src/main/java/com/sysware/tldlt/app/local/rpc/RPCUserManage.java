package com.sysware.tldlt.app.local.rpc;

import java.util.HashMap;
import java.util.Map;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

/**
 * Type：RPCUserManage
 * Descript：RPC用户管理类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月31日 上午10:23:13
 * Version：@version
 */
public class RPCUserManage {
	/**
	 * 登录Key键值.
	 */
	private static final String LOGIN_KEY = "loginKey";
	/**
	 * 用户列表.
	 */
	private static Map<String, Dto> users = new HashMap<String, Dto>();

	/**
	 * @param key
	 * @return
	 */
	public static boolean checkUserKey(String key) {
		return null != findUserDtoByKey(key);
	}

	/**
	 * 通过键值找到对应的用户信息.
	 * @param key 键值
	 * @return 用户信息
	 */
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

	/**
	 * 通过用户编号得到Dto.
	 * @param userId 用户编号
	 * @return Dto对象
	 */
	private static Dto getDto(String userId) {
		Dto dto = users.get(userId);
		if (null == dto) {
			dto = new BaseDto();
			users.put(userId, dto);
		}
		return dto;
	}

	/**
	 * 登录用户.
	 * @param userId 用户编号
	 * @param key 键值
	 */
	@SuppressWarnings("unchecked")
	public static void loginUser(String userId, String key) {
		Dto dto = getDto(userId);
		dto.put(LOGIN_KEY, key);
		dto.put("userId", userId);
	}

	/**
	 * 登出.
	 * @param key 键值
	 */
	@SuppressWarnings("unchecked")
	public static void logout(String key) {
		Dto dto = findUserDtoByKey(key);
		if (null != dto) {
			dto.put(LOGIN_KEY, "");
		}
		
	}
}
