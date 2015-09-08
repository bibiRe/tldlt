package com.sysware.tldlt.app.service.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：UserServiceImpl
 * Descript：用户服务类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 上午9:59:35
 * Version：@version
 */
public class UserServiceImpl extends BaseAppServiceImpl implements UserService {
    /**
     * 日志对象.
     */
    private static final Log log = LogFactory.getLog(UserServiceImpl.class);

    /**
     * 检查基本信息.
     * @param info dto对象.
     * @return
     */
    private Dto checkBasicInfo(Dto info) {
        if (null == info) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "数据为空");
        }
        Dto result = DtoUtils.checkDtoUserId(g4Dao, info);
        if (null != result) {
            return result;
        }
        result = DtoUtils.checkDtoCheckTime(info, "datetime");
        if (null != result) {
            return result;
        }
        return null;
    }

    /**
     * 检查巡检记录媒体信息.
     * @param dto dto对象
     * @return dto
     */
    private Dto checkDeviceStatusMedia(Dto dto) {
        Dto result = checkDeviceSuggestInfoId(dto);
        if (null != result) {
            return result;
        }
        result = DtoUtils.checkDtoCheckTime(dto, "datetime");
        if (null != result) {
            return result;
        }
        return null;
    }

    /**
     * 检查设备建议信息编号.
     * @param dto dto对象.
     * @return dto
     */
    private Dto checkDeviceSuggestInfoId(Dto dto) {
        Integer devicesuggestinfoidObj = dto
                .getAsInteger("devicesuggestinfoid");
        if (null == devicesuggestinfoidObj) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "没有设备建议信息编号");
        }
        Dto inspectRecordInfoDto = (Dto) appDao.queryForObject(
                "App.User.queryDeviceSuggestInfoById",
                devicesuggestinfoidObj.intValue());
        if (null == inspectRecordInfoDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "没有对应的设备建议记录信息");
        }
        return null;
    }

    private Dto checkReportDeviceStatus(Dto info) {
        Dto result = checkBasicInfo(info);
        if (null != result) {
            return result;
        }
        result = DtoUtils.checkDtoDeviceId(appDao, info);
        if (null != result) {
            return result;
        }
        return null;
    }

    /**
     * 检查保存GPS信息.
     * @param info 信息.
     * @return dto对象
     */
    private Dto checkSaveGPSInfo(Dto info) {
        Dto result = checkBasicInfo(info);
        if (null != result) {
            return result;
        }
        return DtoUtils.checkGPSInfo(info);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto reportDeviceStatus(Dto info) {
        Dto result = checkReportDeviceStatus(info);
        if (null != result) {
            return result;
        }
        appDao.insert("App.User.addDeviceSuggestInfo", info);
        BaseRetDto outDto = (BaseRetDto) DtoUtils.getSuccessRetDto("");
        outDto.put("devicesuggestinfoid", info.get("devicesuggestinfoid"));
        return outDto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto saveGPSInfo(Dto info) {
        Dto outDto = checkSaveGPSInfo(info);
        if (null != outDto) {
            return outDto;
        }
        info.put("type", AppCommon.GPS_INFO_TYPE_USER);
        info.put("releaterecordid", info.getAsString("userid"));
        Dto result = DtoUtils.addGPSInfo(appDao, info);
        if (null != result) {
            return result;
        }
        return DtoUtils.getSuccessRetDto("");
    }

    /**
     * 保存文件列表.
     * @param savePath 保存路径
     * @param addressPart 部分地址保存路径
     * @param file 文件
     * @return dto对象.
     * @throws IOException IO异常
     * @throws NoSuchAlgorithmException 没有
     */
    @SuppressWarnings("unchecked")
    private Dto saveMediaFile(Dto dto, Dto fileDto) throws IOException,
            NoSuchAlgorithmException {
        fileDto.put("devicesuggestinfoid",
                dto.getAsInteger("devicesuggestinfoid"));
        Dto result = DtoUtils.createMediaInfo(appDao, dto, fileDto);
        if (null != result) {
            return result;
        }
        return DtoUtils.addInfoAndCheckIntIdFail(appDao,
                "App.User.addReleateMediaInfo", fileDto, "releatemediaid",
                "媒体关联记录");
    }

    /**
     * 保存媒体文件列表.
     * @param dto dto对象
     * @return 返回对象.
     * @throws IOException IO异常.
     * @throws NoSuchAlgorithmException 没有此算法异常
     */
    @SuppressWarnings("unchecked")
    private Dto saveMediaFiles(Dto dto) throws IOException,
            NoSuchAlgorithmException {
        Dto result = null;
        result = DtoUtils.checkMedias(dto);
        if (null != result) {
            return result;
        }
        List<Dto> fileList = (List<Dto>) dto.getAsList("medias");
        for (Dto fileDto : fileList) {
            result = saveMediaFile(dto, fileDto);
            if (null != result) {
                return result;
            }

        }
        return null;
    }

    @Override
    public Dto saveUploadDeviceStatusMedia(Dto dto) {
        Dto result = checkDeviceStatusMedia(dto);
        if (null != result) {
            return result;
        }
        try {
            result = saveMediaFiles(dto);
        } catch (Exception e) {
            log.info(e);
            result = DtoUtils.getErrorRetDto(AppCommon.RET_CODE_ADD_FAIL,
                    new Formatter().format("保存媒体列表失败: %s", e.getMessage())
                            .toString());
        }
        if (null != result) {
            return result;
        }
        return DtoUtils.getSuccessRetDto("");
    }

}
