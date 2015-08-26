package com.sysware.tldlt.app.local.rpc;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.mvc.xstruts.action.ActionForward;

import com.sysware.tldlt.app.utils.DtoUtils;

public class RPCUtils {

    /**
     * 给Reponse写RPC错误信息.
     * @param response Http Response对象
     * @param info 信息
     * @throws IOException 异常
     */
    public static void writeErrorRPCInfo(HttpServletResponse response,
            String info) throws IOException {
        RPCRetDto outDto = RPCRetDto.createDto(false, info);
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
        RPCRetDto outDto = RPCRetDto.createDto(false, info);
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
        RPCRetDto outDto = RPCRetDto.createDto(true, null);
        outDto.addAllData(list);
        return RPCUtils.sendRPCDtoActionForward(response, outDto);
    }
}
