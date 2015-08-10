package com.sysware.tldlt.app.utils;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

/**
 * Type：DtoUtils
 * Descript：Dto工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:38:30
 * Version：@version
 */
public class DtoUtils {

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
     * 得到成功Dto对象.
     * @param info 成功信息.
     * @return Dto对象
     */
    public static Dto getSuccessDto(String info) {
        Dto result = new BaseDto();
        setDtoInfo(result, info, true);
        return result;
    }
}
