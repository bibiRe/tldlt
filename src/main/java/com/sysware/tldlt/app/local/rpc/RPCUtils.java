package com.sysware.tldlt.app.local.rpc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.upload.FormFile;
import org.g4studio.core.mvc.xstruts.upload.MultipartRequestHandler;
import org.g4studio.system.common.util.SystemConstants;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.utils.AppTools;
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
     * 创建dto对象.
     * @param success 成功标志.
     * @param msg 信息
     * @return dto对象
     */
    public static RPCRetDto createDto(boolean success, String msg) {
        RPCRetDto result = new RPCRetDto();
        result.setSuccess(success ? SystemConstants.ENABLED_Y
                : SystemConstants.ENABLED_N);
        if (null != msg) {
            result.setMessage(msg);
        } else {
            result.setMessage("");
        }

        return result;
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
        RPCRetDto outDto = createDto(false, info);
        DtoUtils.writeToResponse(outDto.toJson(), response);
        return null;
    }

    /**
     * 给Reponse写RPC基本返回Dto信息.
     * @param response Http Response对象
     * @param info 信息
     * @return 返回actionforward
     * @throws IOException 异常
     */
    public static ActionForward sendBasicRetDtoRPCInfoActionForward(
            HttpServletResponse response, BaseRetDto info) throws IOException {
        RPCRetDto outDto = createDto(info.isRetSuccess(), AppTools.convertNullStr(info.getDesc()));
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
        RPCRetDto outDto = createDto(true, null);
        outDto.addAllData(list);
        return sendRPCDtoActionForward(response, outDto);
    }

    /**
     * 给Reponse写RPC错误信息.
     * @param response Http Response对象
     * @param info 信息
     * @throws IOException 异常
     */
    public static void writeErrorRPCInfo(HttpServletResponse response,
            String info) throws IOException {
        RPCRetDto outDto = createDto(false, info);
        DtoUtils.writeToResponse(outDto.toJson(), response);
    }

    /**
     * 创建文件Dto列表.
     * @param request http request对象.
     * @param response http response对象.
     * @param dto dto对象.
     * @param upload 上传对象.
     * @return 是否有效
     * @throws FileUploadException 文件上传异常.
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    private static boolean createFileDtoList(HttpServletResponse response,
            Dto dto, MultipartRequestHandler multiHandle)
            throws FileUploadException, IOException {
        List<Dto> fileList = Lists.newArrayList();
        Collection<FormFile> files = multiHandle.getFileElements().values();
        for (FormFile fileItem : files) {
            Dto fileDto = new BaseDto();
            fileDto.put("file", fileItem);
            fileList.add(fileDto);
        }
        if (fileList.size() < 1) {
            sendErrorRPCInfoActionForward(response, "没有上传文件");
            return false;
        }
        dto.put("medias", fileList);
        return true;
    }

    /**
     * 检查上传的媒体列表.
     * @param form struts数据form对象.
     * @param response http response对象.
     * @param dto dto对象
     * @return 返回是否有效.
     * @throws IOException IO异常
     * @throws FileUploadException 文件上传异常.
     */
    public static boolean checkUploadMediasAndSendErrorRPCInfoActionForward(
            ActionForm form, HttpServletResponse response, Dto dto)
            throws IOException, FileUploadException {
        MultipartRequestHandler multiHandle = form.getMultipartRequestHandler();
        if (null == multiHandle) {
            sendErrorRPCInfoActionForward(response, "没有文件");
            return false;
        }
        return createFileDtoList(response, dto, multiHandle);
    }

    /**
     * 检查上传的key值.
     * @param response http response对象.
     * @param dto dto对象
     * @return 返回是否有效.
     * @throws IOException IO异常
     * @throws FileUploadException 文件上传异常.
     */
    @SuppressWarnings("unchecked")
    public static boolean checkKeyAndSendErrorRPCInfoActionForward(
            HttpServletResponse response, Dto dto) throws IOException {
        String key = dto.getAsString("key");
        if (AppTools.isEmptyString(key)) {
            RPCUtils.sendErrorRPCInfoActionForward(response, "key值为空");
            return false;
        }
        Dto userDto = RPCUserManage.findUserDtoByKey(key);
        if (null == userDto) {
            RPCUtils.sendErrorRPCInfoActionForward(response, "key值无效");
            return false;
        }
        dto.put("userid", userDto.get("userId"));
        return true;
    }
}
