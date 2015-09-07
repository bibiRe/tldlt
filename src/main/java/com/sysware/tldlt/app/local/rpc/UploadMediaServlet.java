package com.sysware.tldlt.app.local.rpc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FileUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sysware.tldlt.app.utils.DtoUtils;

public class UploadMediaServlet extends HttpServlet {
    /**
     * 处理方式.
     */
    private Map<String, HandleUploadMediaRequest> handles = Maps.newHashMap();
    /**
     * 
     */
    private static final long serialVersionUID = -283157732591240007L;

    @SuppressWarnings("unchecked")
    public void
            doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        RequestContext requestContext = new ServletRequestContext(request);
        // 判断表单是否是Multipart类型的。这里可以直接对request进行判断，不过已经以前的用法了
        if (!FileUpload.isMultipartContent(requestContext)) {
            RPCUtils.writeErrorRPCInfo(response, "没有文件");
            return;
        }
        Dto dto = new BaseDto();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        String uploadFilePath = "E:/";
        factory.setRepository(new File(uploadFilePath)); // 设置临时目录
        factory.setSizeThreshold((int) (FileUtils.ONE_KB * 8)); // 8k的缓冲区,文件大于8K则保存到临时目录
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);// 声明解析request的对象
        servletFileUpload.setHeaderEncoding("UTF-8"); // 处理文件名中文
        // upload.setFileSizeMax(1024 * 1024 * 5);// 设置每个文件最大为5M
        servletFileUpload.setSizeMax(FileUtils.ONE_MB * 10); // 一共最多能上传10M
        List<Dto> fileList = Lists.newArrayList();
        List<FileItem> fileItems;
        try {
            fileItems = servletFileUpload.parseRequest(request);
        } catch (FileUploadException e) {
            RPCUtils.writeErrorRPCInfo(response, e.getMessage());
            return;
        }
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                Dto fileDto = new BaseDto();
                fileDto.put("file", fileItem);
                fileList.add(fileDto);
            } else {
                dto.put(fileItem.getName(), fileItem.getString());
            }
        }
        if (fileList.size() < 1) {
            RPCUtils.writeErrorRPCInfo(response, "没有上传文件");
        }
        dto.put("medias", fileList);
        DtoUtils.writeToResponse(RPCUtils.createDto(true, dto.toJson())
                .toJson(), response);
    }
}
