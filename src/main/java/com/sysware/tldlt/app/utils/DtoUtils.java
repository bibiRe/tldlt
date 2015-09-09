package com.sysware.tldlt.app.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.common.dao.Dao;
import org.g4studio.common.util.SpringBeanLoader;
import org.g4studio.common.util.WebUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.upload.FormFile;
import org.g4studio.core.util.G4Utils;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.media.MediaPathService;
import com.sysware.tldlt.app.service.media.MediaUrlService;

/**
 * Type：DtoUtils Descript：Dto工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:38:30
 * Version：@version
 */
public class DtoUtils {

    /**
     * 日志对象.
     */
    private static final Log log = LogFactory.getLog(DtoUtils.class);

    /**
     * 增加gps信息.
     * @param appDao appDao
     * @param inDto gps信息.
     * @return dto
     */
    public static Dto addGPSInfo(Dao appDao, Dto inDto) {
        Dto result = addInfoAndCheckIntIdFail(appDao,
                "App.GPS.addInfo", inDto, "gpsinfoid", "GPS记录");
        if (null != result) {
            return result;
        }
        return addInfoAndCheckIntIdFail(appDao,
                "App.GPS.saveReleateGPSInfo", inDto, "releategpsinfoid",
                "GPS关联记录");
    }

    /**
     * 新增并检查返回Id是否存在.
     * @param appDao dao对象
     * @param statementName 新增语句名称.
     * @param inDto 输入dto
     * @param checkIdKey 检查Id键值
     * @param errorInfo 错误信息.
     * @return dto
     */
    public static Dto
            addInfoAndCheckIntIdFail(Dao appDao, String statementName,
                    Dto inDto, String checkIdKey, String errorInfo) {
        appDao.insert(statementName, inDto);
        if (null == inDto.getAsInteger(checkIdKey)) {
            return getErrorRetDto(AppCommon.RET_CODE_ADD_FAIL, "新增" + errorInfo
                    + "失败");
        }
        return null;
    }

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
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "检查时间为空");
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
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "设备编号为空");
        }
        Dto deviceDto = (BaseDto) dao.queryForObject(
                "App.Device.queryDeviceInfoByDeviceId", deviceId);
        if (null == deviceDto) {
            return getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE, "设备编号无效");
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
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "用户编号为空");
        }
        Dto userDto = (BaseDto) dao.queryForObject("User.getUserInfoByKey",
                info);
        if (null == userDto) {
            return getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE, "用户编号无效");
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
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "时间为空");
        }
        if (null == info.getAsBigDecimal("longtitude")) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "经度为空");
        }
        if (null == info.getAsBigDecimal("latitude")) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "纬度为空");
        }
        return null;
    }

    /**
     * 检查媒体信息.
     * @param dto dto对象
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    public static Dto checkMedias(Dto dto, MediaPathService mediaPathService) {
        List<Dto> fileList = (List<Dto>) dto.getAsList("medias");
        if (null == fileList) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "没有文件列表");
        }
        if (fileList.size() < 1) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "文件列表为空");
        }
        return createSavePathAndPutInDto(dto, mediaPathService);
    }

    /**
     * 创建媒体文件.
     * @param path 路径
     * @param fileDto 文件dto
     * @return dto对象.
     */
    @SuppressWarnings("unchecked")
    private static Dto createMediaFile(String path, Dto fileDto) {
        FormFile fileItem = (FormFile) fileDto.get("file");
        if (null == fileItem) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "文件为空");
        }
        String fileName = fileItem.getFileName();
        String realFileName = path + fileName;
        try {
            FileUtils.copyInputStreamToFile(fileItem.getInputStream(),
                    new File(realFileName));
        } catch (Exception e) {
            log.info(e);
            getErrorRetDto(AppCommon.RET_CODE_CREATE_FILE_FAIL, e.getMessage());
        }
        fileDto.put("realFileName", realFileName);
        return null;
    }

    /**
     * 创建媒体信息.
     * @param appDao dao对象
     * @param dto dto对象
     * @param fileDto 文件对象
     * @return dto
     * @throws NoSuchAlgorithmException 没有此算法
     * @throws IOException io异常
     */
    @SuppressWarnings("unchecked")
    public static Dto createMediaInfo(Dao appDao, Dto dto, Dto fileDto)
            throws NoSuchAlgorithmException, IOException {
        fileDto.put("time", dto.getAsString("time"));
        FormFile fileItem = (FormFile) fileDto.get("file");
        if (null == fileItem) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "文件为空");
        }
        Dto result = createMediaFile(
                AppTools.addPathEndSeprator(dto.getAsString("savePath")),
                fileDto);
        if (null != result) {
            return result;
        }
        fileDto.put("type", AppCommon.MEDIA_TYPE_IMAGE);
        fileDto.put("address",
                AppTools.addPathEndSeprator(dto.getAsString("savePartPath"))
                        + fileItem.getFileName());
        fileDto.put("hash", AppTools.getFileMD5CheckSum(fileDto
                .getAsString("realFileName")));
        return addInfoAndCheckIntIdFail(appDao, "App.Media.addInfo", fileDto,
                "mediainfoid", "媒体记录");
    }

    /**
     * 创建保存路径并且放入到dto.
     * @param dto dto对象
     * @return 创建示范成功.
     */
    @SuppressWarnings("unchecked")
    public static Dto createSavePathAndPutInDto(Dto dto, MediaPathService mediaPathService) {        
        String savePath = mediaPathService.getPath("");
        if (AppTools.isBlankString(savePath)) {
            return getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE, "保存路径没定义");
        }
        // 检查路径是否存在,如果不存在则创建之
        File file = new File(savePath);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return getErrorRetDto(AppCommon.RET_CODE_CREATE_PATH_FAIL,
                        "创建保存路径失败");
            }
        }
        dto.put("savePartPath", G4Utils.getCurDate());
        savePath = mediaPathService.getPath(dto.getAsString("savePartPath"));
        File file1 = new File(savePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return getErrorRetDto(AppCommon.RET_CODE_CREATE_PATH_FAIL,
                        "创建保存路径失败");
            }
        }
        dto.put("savePath", savePath);
        return null;
    }

    /**
     * 创建上传巡检记录媒体信息成功返回列表.
     * @param dto dto对象
     * @return 返回列表.
     */
    @SuppressWarnings("unchecked")
    public static Collection<Dto> createUploadInspectRecordMediaSuccessRetList(
            Dto dto) {
        MediaUrlService mediaUrlService = (MediaUrlService) SpringBeanLoader
                .getSpringBean("mediaUrlService");
        Collection<Dto> retList = Lists.newArrayList();
        List<Dto> fileList = (List<Dto>) dto.getAsList("medias");
        for (Dto fileDto : fileList) {
            Dto retDto = new BaseDto();
            FormFile fileItem = (FormFile) fileDto.get("file");
            retDto.put("filename", fileItem.getFileName());
            if (null != mediaUrlService) {
                retDto.put("mediaurl",
                        mediaUrlService.getUrl(fileDto.getAsString("address")));
            } else {
                retDto.put("mediaurl", fileDto.getAsString("address"));
            }
            retDto.put("hash", fileDto.getAsString("hash"));
            retList.add(retDto);
        }
        return retList;
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
     * 得到返回Dto Json字符串.
     * @param outDto dto对象
     */
    private static String getRetDtoJson(Dto outDto) {
        BaseRetDto retDto = (BaseRetDto) outDto;
        BaseRetDto retDto1 = new BaseRetDto();
        try {
            BeanUtils.copyProperties(retDto1, retDto);
        } catch (IllegalAccessException e) {
            log.info(e);
        } catch (InvocationTargetException e) {
            log.info(e);
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
        return retDto1.toJson();
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
        return sendStrActionForward(response, dto.toJson());
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
            HttpServletResponse response, int retCode, String info)
            throws IOException {
        return sendStrActionForward(response,
                getRetDtoJson(getErrorRetDto(retCode, info)));
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
        return sendStrActionForward(response, getRetDtoJson(outDto));
    }

    /**
     * 给Reponse写字符串信息.
     * @param response Http Response对象
     * @param dto dto对象.
     * @return ActionForward
     * @throws IOException 异常
     */
    public static ActionForward sendStrActionForward(
            HttpServletResponse response, String info) throws IOException {
        writeToResponse(info, response);
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
     * @param dto dto对象
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
