package com.sysware.tldlt.app.utils;

import java.util.List;

import org.g4studio.common.util.WebUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;


/**
 * Type：DtoUtils
 * Descript：Dto工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:38:30
 * Version：@version
 */
public class DtoUtils {

    /**
     * 得到错误Dto对象.
     * @param info 错误信息.
     * @return Dto对象
     */
    public static Dto getErrorDto(String info) {
        Dto result = new BaseDto();
        setDtoInfo(result, info, false);
        return result;
    }

    /**
     * 得到错误Dto对象.
     * @param retCode 错误码.
     * @param info 错误信息.
     * @return Dto对象
     */
    public static Dto getErrorRetDto(int retCode, String info) {
        BaseRetDto result = new BaseRetDto();
        setRetDtoInfo(result, retCode, info);
        return result;
    }

    /**
     * 得到成功Dto对象.
     * @param info 成功信息.
     * @return Dto对象
     */
    public static Dto getSuccessDto(String info) {
        Dto result = new BaseDto();
        setDtoInfo(result, info, true);
        return result;
    }

    /**
     * 得到成功Dto对象.
     * @param info 错误信息.
     * @return Dto对象
     */
    public static Dto getSuccessRetDto(String info) {
        BaseRetDto result = new BaseRetDto();
        setRetDtoInfo(result, AppCommon.RET_CODE_SUCCESS, info);
        return result;
    }

    /**
     * 设置dto信息.
     * @param dto dto对象
     * @param info 错误信息
     * @param flag 成功标志
     * @return
     */
    @SuppressWarnings("unchecked")
    private static void setDtoInfo(Dto dto, String info, boolean flag) {
        dto.put("msg", info);
        dto.put("success", new Boolean(flag));
    }

    /**
     * 设置返回dto信息.
     * @param dto dto对象
     * @param info 错误信息
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    private static void setRetDtoInfo(BaseRetDto dto, int retCode, String info) {
        dto.setRetCode(retCode);
        dto.setDesc(info);
        dto.put("retCode", retCode);
        dto.put("msg", info);
        dto.put("success", new Boolean(dto.isRetSuccess()));
    }
    
    public static boolean checkDictCodeExist(String fieldName, String code) {
    	boolean result = false;
    	List<Dto> codelist = WebUtils.getDictCodeList();
    	String codedesc = null;
		for (Dto codeDto: codelist) {
			if (fieldName.equalsIgnoreCase(codeDto.getAsString("field"))
					&& code.equalsIgnoreCase(codeDto.getAsString("code"))) {
				result = true;
				break;
			}
		}
		return result;
    }
}
