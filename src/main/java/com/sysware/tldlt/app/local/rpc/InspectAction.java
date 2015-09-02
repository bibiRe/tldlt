package com.sysware.tldlt.app.local.rpc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

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
     * @param request http request对象.
     * @param response http response对象.
     * @param dto dto对象
     * @return 返回是否有效.
     * @throws IOException IO异常
     * @throws FileUploadException 文件上传异常.
     */
    @SuppressWarnings({"deprecation"})
    private boolean checkUploadMedias(HttpServletRequest request,
            HttpServletResponse response, Dto dto) throws IOException,
            FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            RPCUtils.sendErrorRPCInfoActionForward(response, "没有文件");
            return false;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        String uploadFilePath = "E:/";
        factory.setRepository(new File(uploadFilePath)); // 设置临时目录
        factory.setSizeThreshold((int) (FileUtils.ONE_KB * 8)); // 8k的缓冲区,文件大于8K则保存到临时目录
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);// 声明解析request的对象
        servletFileUpload.setHeaderEncoding("UTF-8"); // 处理文件名中文
        // upload.setFileSizeMax(1024 * 1024 * 5);// 设置每个文件最大为5M
        servletFileUpload.setSizeMax(FileUtils.ONE_MB * 10);// 一共最多能上传10M
        return createFileDtoList(request, response, dto, servletFileUpload);        
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
    private boolean createFileDtoList(HttpServletRequest request,
            HttpServletResponse response, Dto dto, ServletFileUpload upload)
            throws FileUploadException, IOException {
        List<Dto> fileList = Lists.newArrayList();
        List<FileItem> fileItems = upload.parseRequest(request);
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                Dto fileDto = new BaseDto();
                fileDto.put("file", fileItem);
                fileList.add(fileDto);
            }
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
     * 保存巡检记录.
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
        // request.setCharacterEncoding("UTF-8");
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
        if (!checkUploadMedias(request, response, dto)) {
            return null;
        }

        BaseRetDto outDto = (BaseRetDto) inspectService
                .saveUploadInspectRecordMedia(dto);
        return RPCUtils.sendRPCDtoActionForward(response,
                RPCUtils.createDto(outDto.isRetSuccess(), outDto.getDesc()));
    }
}
