package com.sysware.tldlt.app.local.rpc;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.mvc.xstruts.action.ActionForward;

import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：RPCUtils
 * Descript：RPC工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:14:57
 * Version：@version
 */
public class RPCUtils {

    /**
     * 给Reponse写RPC错误信息.
     * @param response Http Response对象
     * @param info 信息
     * @throws IOException 异常
     */
    public static void writeErrorRPCInfo(HttpServletResponse response,
            String info) throws IOException {
        RPCRetDto outDto = RPCUtils.createDto(false, info);
        DtoUtils.writeToResponse(outDto.toJson(), response);
    }

    /**
     * 给Reponse写RPC错误信息.
     * @param response Http Response对象
     * @param info 信息
     * @return 返回actionforward
     * @throws IOException 异常
     */
    public static ActionForward sendErrorRPCInfoActionForward(
            HttpServletResponse response, String info) throws IOException {
        RPCRetDto outDto = RPCUtils.createDto(false, info);
        DtoUtils.writeToResponse(outDto.toJson(), response);
        return null;
    }
    /**
     * 给Reponse写RPC Dto信息.
     * @param response Http Response对象
     * @param dto 信息
     * @return 返回actionforward
     * @throws IOException 异常
     */
    public static ActionForward sendRPCDtoActionForward(
            HttpServletResponse response, RPCRetDto dto) throws IOException {
        DtoUtils.writeToResponse(dto.toJson(), response);
        return null;
    }
    /**
     * 给Reponse写RPCList信息.
     * @param response Http Response对象
     * @param list 信息
     * @return 返回actionforward
     * @throws IOException 异常
     */
    @SuppressWarnings("rawtypes")
    public static ActionForward sendRPCListDtoActionForward(
            HttpServletResponse response, Collection list) throws IOException {
        RPCRetDto outDto = RPCUtils.createDto(true, null);
        outDto.addAllData(list);
        return RPCUtils.sendRPCDtoActionForward(response, outDto);
    }

    /**
     * 创建dto对象.
     * @param success 成功标志.
     * @param msg 信息
     * @return dto对象
     */
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
}
