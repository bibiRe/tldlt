package com.sysware.tldlt.app.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.g4studio.common.dao.Dao;
import org.g4studio.common.util.WebUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForward;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;

/**
 * Type：DtoUtils Descript：Dto工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:38:30
 * Version：@version
 */
public class DtoUtils {

    /**
     * 检查字典代码是否存在.
     * @param fieldName 字段
     * @param code 编码
     * @return 是否存在
     */
    @SuppressWarnings("unchecked")
    public static boolean checkDictCodeExist(String fieldName, String code) {
        boolean result = false;
        List<Dto> codelist = WebUtils.getDictCodeList();
        for (Dto codeDto : codelist) {
            if (fieldName.equalsIgnoreCase(codeDto.getAsString("field"))
                    && code.equalsIgnoreCase(codeDto.getAsString("code"))) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 检查Dto 设备编号.
     * @param info dto对象
     * @param timeKey 时间key值
     * @return 是否有效
     */
    @SuppressWarnings("unchecked")
    public static Dto checkDtoCheckTime(Dto info, String timeKey) {
        Long time = info.getAsLong(timeKey);
        if (null == time) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "检查时间为空");
        }
        info.put("time", AppTools.unixTime2DateStr(time.longValue()));
        return null;
    }

    /**
     * 检查Dto 设备编号.
     * @param dao DAO对象
     * @param info dto对象
     * @return 是否有效
     */
    public static Dto checkDtoDeviceId(Dao dao, Dto info) {
        String deviceId = info.getAsString("deviceID");
        if (AppTools.isEmptyString(deviceId)) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备编号为空");
        }
        Dto deviceDto = (BaseDto) dao.queryForObject(
                "App.Device.queryDeviceInfo", deviceId);
        if (null == deviceDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "设备编号无效");
        }
        return null;
    }

    /**
     * 检查Dto 用户编号.
     * @param dao DAO 对象
     * @param info dto对象
     * @return 是否有效
     */
    public static Dto checkDtoUserId(Dao dao, Dto info) {
        if (AppTools.isEmptyString(info.getAsString("userid"))) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "用户编号为空");
        }
        Dto userDto = (BaseDto) dao.queryForObject("User.getUserInfoByKey",
                info);
        if (null == userDto) {
            return getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "用户编号无效");
        }
        return null;
    }

    /**
     * 检查GPS信息.
     * @param info 信息.
     * @return dto
     */
    public static Dto checkGPSInfo(Dto info) {
        if (null == info.getAsLong("datetime")) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "时间为空");
        }
        if (null == info.getAsBigDecimal("longtitude")) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "经度为空");
        }
        if (null == info.getAsBigDecimal("latitude")) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "纬度为空");
        }
        return null;
    }

    /**
     * 得到错误Dto对象.
     * @param info
     *            错误信息.
     * @return Dto对象
     */
    public static Dto getErrorDto(String info) {
        Dto result = new BaseDto();
        setDtoInfo(result, info, false);
        return result;
    }

    /**
     * 得到错误Dto对象.
     * @param retCode
     *            错误码.
     * @param info
     *            错误信息.
     * @return Dto对象
     */
    public static Dto getErrorRetDto(int retCode, String info) {
        BaseRetDto result = new BaseRetDto();
        setRetDtoInfo(result, retCode, info);
        return result;
    }

    /**
     * 得到成功Dto对象.
     * @param info
     *            成功信息.
     * @return Dto对象
     */
    public static Dto getSuccessDto(String info) {
        Dto result = new BaseDto();
        setDtoInfo(result, info, true);
        return result;
    }

    /**
     * 得到成功Dto对象.
     * @param info
     *            错误信息.
     * @return Dto对象
     */
    public static Dto getSuccessRetDto(String info) {
        BaseRetDto result = new BaseRetDto();
        setRetDtoInfo(result, AppCommon.RET_CODE_SUCCESS, info);
        return result;
    }

    /**
     * 给Reponse写Dto信息.
     * @param response Http Response对象
     * @param dto dto对象.
     * @return ActionForward
     * @throws IOException 异常
     */
    public static ActionForward sendDtoActionForward(
            HttpServletResponse response, Dto dto) throws IOException {
        writeToResponse(dto.toJson(), response);
        return null;
    }

    /**
     * 设置返回信息.
     * @param response response对象
     * @param outDto dto对象
     * @return Struts Actionforward 对象
     * @throws IOException IO异常
     */
    public static ActionForward sendRetDtoActionForward(
            HttpServletResponse response, Dto outDto) throws IOException {
        writeRetDtoResponse(response, outDto);
        return null;
    }
    
    /**
     * 返回错误信息.
     * @param response response对象
     * @param retCode 返回码.
     * @param info 信息.
     * @return Struts Actionforward 对象
     * @throws IOException IO异常
     */
    public static ActionForward sendErrorRetDtoActionForward(
            HttpServletResponse response, int retCode, String info) throws IOException {
        writeRetDtoResponse(response, getErrorRetDto(retCode, info));
        return null;
    }
    /**
     * 设置dto信息.
     * @param dto
     *            dto对象
     * @param info
     *            错误信息
     * @param flag
     *            成功标志
     * @return
     */
    @SuppressWarnings("unchecked")
    private static void setDtoInfo(Dto dto, String info, boolean flag) {
        dto.put("msg", info);
        dto.put("success", new Boolean(flag));
    }

    /**
     * 设置返回dto信息.
     * @param dto  dto对象
     * @param retCode 返回值
     * @param info 信息.
     */
    @SuppressWarnings("unchecked")
    private static void setRetDtoInfo(BaseRetDto dto, int retCode, String info) {
        dto.setRetCode(retCode);
        dto.setDesc(info);
        dto.put("retCode", retCode);
        dto.put("msg", info);
        dto.put("success", new Boolean(dto.isRetSuccess()));
    }

    /**
     * 设置返回信息.
     * @param response response对象
     * @param outDto dto对象
     * @throws IOException IO异常
     */
    public static void writeRetDtoResponse(HttpServletResponse response,
            Dto outDto) throws IOException {
        BaseRetDto retDto = (BaseRetDto) outDto;
        BaseRetDto retDto1 = new BaseRetDto();
        try {
            BeanUtils.copyProperties(retDto1, retDto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (retDto1.isRetSuccess()) {
            retDto1.setMsg("操作成功");
        } else {
            StringBuilder strB = new StringBuilder();
            strB.append("操作失败，错误码:");
            strB.append(retDto1.getRetCode());
            strB.append("，错误信息:");
            strB.append(retDto1.getDesc());
            retDto1.setMsg(strB.toString());
        }
        writeToResponse(retDto1.toJson(), response);
    }

    /**
     * 给Http Response写信息.
     * @param str 字符串
     * @param response http response
     * @throws IOException 异常
     */
    public static void
            writeToResponse(String str, HttpServletResponse response)
                    throws IOException {
        response.getWriter().write(str);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
