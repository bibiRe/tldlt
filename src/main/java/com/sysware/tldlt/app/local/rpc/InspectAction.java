package com.sysware.tldlt.app.local.rpc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.mvc.xstruts.upload.FormFile;
import org.g4studio.core.mvc.xstruts.upload.MultipartRequestHandler;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.inspect.InspectService;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：InspectAction
 * Descript：设备巡检Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:27:32
 * Version：@version
 */
public class InspectAction extends BaseAppAction {
    /**
     * 巡检服务对象.
     */
    private InspectService inspectService;

    public InspectAction() {
        inspectService = (InspectService) this.getService("inspectService");
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
    private boolean checkUploadMedias(ActionForm form,
            HttpServletResponse response, Dto dto) throws IOException,
            FileUploadException {
        MultipartRequestHandler multiHandle = form.getMultipartRequestHandler();
        if (null == multiHandle) {
            RPCUtils.sendErrorRPCInfoActionForward(response, "没有文件");
            return false;
        }
        return createFileDtoList(response, dto, multiHandle);
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
    private boolean createFileDtoList(HttpServletResponse response, Dto dto,
            MultipartRequestHandler multiHandle) throws FileUploadException,
            IOException {
        List<Dto> fileList = Lists.newArrayList();
        Collection<FormFile> files = multiHandle.getFileElements().values();
        for (FormFile fileItem : files) {
            Dto fileDto = new BaseDto();
            fileDto.put("file", fileItem);
            fileList.add(fileDto);
        }
        if (fileList.size() < 1) {
            RPCUtils.sendErrorRPCInfoActionForward(response, "没有上传文件");
            return false;
        }
        dto.put("medias", fileList);
        return true;
    }

    /**
     * 保存巡检记录.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward saveInspectRecordInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String key = dto.getAsString("key");
        if (AppTools.isEmptyString(key)) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值为空");
        }
        Dto userDto = RPCUserManage.findUserDtoByKey(request
                .getParameter("key"));
        if (null == userDto) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值无效");
        }
        dto.put("userid", userDto.get("userId"));
        BaseRetDto outDto = (BaseRetDto) inspectService.addInfo(dto);
        return RPCUtils.sendRPCDtoActionForward(response,
                RPCUtils.createDto(outDto.isRetSuccess(), outDto.getDesc()));
    }

    /**
     * 上传巡检记录媒体信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward uploadInspectRecordMedia(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String key = dto.getAsString("key");
        if (AppTools.isEmptyString(key)) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值为空");
        }
        Dto userDto = RPCUserManage.findUserDtoByKey(key);
        if (null == userDto) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值无效");
        }
        dto.put("userid", userDto.get("userId"));
        if (!checkUploadMedias(form, response, dto)) {
            return null;
        }

        BaseRetDto outDto = (BaseRetDto) inspectService
                .saveUploadInspectRecordMedia(dto);
        if (!outDto.isRetSuccess()) {
            return RPCUtils
                    .sendRPCDtoActionForward(
                            response,
                            RPCUtils.createDto(outDto.isRetSuccess(),
                                    outDto.getDesc()));
        }

        return RPCUtils.sendRPCListDtoActionForward(response,
                createUploadInspectRecordMediaSuccessRetList(dto));
    }

    /**
     * 创建上传巡检记录媒体信息成功返回列表.
     * @param dto dto对象
     * @return 返回列表.
     */
    @SuppressWarnings("unchecked")
    private Collection<Dto>
            createUploadInspectRecordMediaSuccessRetList(Dto dto) {
        Collection<Dto> retList = Lists.newArrayList();
        List<Dto> fileList = (List<Dto>) dto.getAsList("medias");
        for (Dto fileDto : fileList) {
            Dto retDto = new BaseDto();
            FormFile fileItem = (FormFile) fileDto.get("file");
            retDto.put("filename", fileItem.getFileName());
            retDto.put("address", fileDto.getAsString("address"));
            retDto.put("hash", fileDto.getAsString("hash"));
            retList.add(retDto);
        }
        return retList;
    }
}
