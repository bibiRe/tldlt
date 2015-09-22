package com.sysware.tldlt.app.service.inspect;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.system.common.util.SystemConstants;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.service.media.MediaPathService;
import com.sysware.tldlt.app.service.media.MediaUrlService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：InspectServiceImpl
 * Descript：巡检服务实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 下午2:39:07
 * Version：@version
 */
public class InspectServiceImpl extends BaseAppServiceImpl implements
        InspectService {
    /**
     * 日志对象.
     */
    private static final Log log = LogFactory.getLog(InspectServiceImpl.class);

    /**
     * 媒体服务对象.
     */
    private MediaPathService mediaPathService;

    /**
     * 媒体链接服务接口.
     */
    private MediaUrlService mediaUrlService;

    /**
     * 增加设备故障信息.
     * @param inDto 输入Dto
     * @return 返回信息
     */
    @SuppressWarnings({"unchecked"})
    private BaseRetDto addDeviceFaultInfo(Dto inDto) {
        BaseRetDto result = null;
        result = (BaseRetDto) DtoUtils
                .addInfoAndCheckIntIdFail(appDao, "App.DeviceFault.addInfo",
                        inDto, "devicefaultinfoid", "设备故障信息");
        if (null != result) {
            return result;
        }
        Dto relDto = new BaseDto();
        relDto.put("devicefaultinfoid", inDto.getAsInteger("devicefaultinfoid"));
        relDto.put("type", AppCommon.DEVICE_FAULTINFO_TYPE_INSPECT);
        relDto.put("releaterecordid", inDto.getAsInteger("inspectrecordinfoid"));
        result = (BaseRetDto) DtoUtils.addInfoAndCheckIntIdFail(appDao,
                "App.DeviceFault.addReleateDeviceFaultInfo", relDto,
                "releatedevicefaultinfoid", "设备故障关联信息");
        if (null != result) {
            return result;
        }
        inDto.put("releatedevicefaultinfoid",
                relDto.getAsInteger("releatedevicefaultinfoid"));
        return result;
    }

    @Override
    public Dto addInfo(Dto inDto) {
        Dto result = checkAddInfo(inDto);
        if (null != result) {
            return result;
        }
        BaseRetDto outDto = getAddInspectRecordRetInfo(inDto);
        return outDto;
    }

    /**
     * 增加巡检记录.
     * @param inDto 巡检记录
     * @return dto
     */
    @SuppressWarnings("unchecked")
    private BaseRetDto addInspectRecord(Dto inDto) {
        Integer inspectRecordDto = (Integer) appDao.queryForObject(
                "App.Inspect.queryInspectRecordIdByPlanId",
                inDto.getAsInteger("planID").intValue());
        int inspectRecordId = 0;
        if (null != inspectRecordDto) {
            inspectRecordId = inspectRecordDto.intValue();
        }
        if (inspectRecordId < 1) {
            BaseRetDto result = (BaseRetDto) DtoUtils.addInfoAndCheckIntIdFail(
                    appDao, "App.Inspect.addInspectRecord", inDto,
                    "inspectrecordid", "巡检记录");
            if (null != result) {
                return result;
            }
            inspectRecordId = inDto.getAsInteger("inspectrecordid").intValue();
        } else {
            inDto.put("inspectrecordid", inspectRecordId);
        }
        return null;
    }

    /**
     * 增加巡检记录信息.
     * @param inDto dto对象
     */
    private BaseRetDto addInspectRecordInfo(Dto inDto) {
        return (BaseRetDto) DtoUtils.addInfoAndCheckIntIdFail(appDao,
                "App.Inspect.addInspectRecordInfo", inDto,
                "inspectrecordinfoid", "巡检记录信息");
    }

    /**
     * 检查新增信息.
     * @param inDto 输入dto.
     * @return
     */
    private Dto checkAddInfo(Dto inDto) {
        if (null == inDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "数据为空");
        }
        Dto result = DtoUtils.checkDtoUserId(g4Dao, inDto);
        if (null != result) {
            return result;
        }

        result = DtoUtils.checkDtoDeviceId(appDao, inDto);
        if (null != result) {
            return result;
        }
        result = DtoUtils.checkDtoCheckTime(inDto, "checktime");
        if (null != result) {
            return result;
        }
        result = checkDtoIsOK(inDto);
        if (null != result) {
            return result;
        }
        result = checkPlanIdAndDeviceId(inDto);
        return result;
    }

    /**
     * 检查isOK标志.
     * @param inDto dto对象
     * @return 是否有效
     */
    @SuppressWarnings("unchecked")
    private Dto checkDtoIsOK(Dto inDto) {
        String isOK = inDto.getAsString("isOK");
        if (AppTools.isEmptyString(isOK)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "isOK标志为空");
        }
        if (!SystemConstants.ENABLED_Y.equals(isOK)) {
            if (AppTools.isEmptyString(inDto.getAsString("checkdesc"))) {
                return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                        "故障没有描述信息");
            }
            inDto.put("state", SystemConstants.ENABLED_N);
        } else {
            inDto.put("state", SystemConstants.ENABLED_Y);
        }
        return null;
    }

    /**
     * 检查巡检记录信息编号.
     * @param dto dto对象.
     * @return dto
     */
    private Dto checkInspectRecordInfoId(Dto dto) {
        Integer inspectRecordInfoIdObj = dto
                .getAsInteger("inspectrecordinfoid");
        if (null == inspectRecordInfoIdObj) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "没有巡检记录信息编号");
        }
        Dto inspectRecordInfoDto = (Dto) appDao.queryForObject(
                "App.Inspect.queryInspectRecordInfoById",
                inspectRecordInfoIdObj.intValue());
        if (null == inspectRecordInfoDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "没有对应的巡检记录信息");
        }
        return null;
    }

    private Dto checkInspectRecordItemId(Dto dto) {
        Integer inspectrecorditemidObj = dto
                .getAsInteger("inspectrecorditemid");
        if (null == inspectrecorditemidObj) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "没有巡检记录Item信息编号");
        }
        Dto inspectRecordItemDto = (Dto) appDao.queryForObject(
                "App.Inspect.queryInspectRecordItemById", dto);
        if (null == inspectRecordItemDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "没有对应的巡检记录Item信息");
        }
        return null;
    }

    private Dto checkInspectRecordItemMedia(Dto dto) {
        Dto result = checkInspectRecordItemId(dto);
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
     * 检查巡检记录媒体信息.
     * @param dto dto对象
     * @return dto
     */
    private Dto checkInspectRecordMedia(Dto dto) {
        Dto result = checkInspectRecordInfoId(dto);
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
     * 检查巡检计划编号和设备编号.
     * @param inDto dto对象
     * @return 是否有效.
     */
    private Dto checkPlanIdAndDeviceId(Dto inDto) {
        Dto result = null;
        Integer planID = inDto.getAsInteger("planID");
        result = (null != planID) ? checkPlanIdNotEmptyAndDeviceId(inDto,
                planID) : checkPlanIdNullAndDeviceId(inDto);
        if (null != result) {
            return result;
        }
        return result;
    }

    /**
     * 检查巡检计划编号不为空是否有效.
     * @param inDto dto对象
     * @param planID 巡检计划编号
     * @return 是否有效
     */
    @SuppressWarnings("unchecked")
    private Dto checkPlanIdNotEmptyAndDeviceId(Dto inDto, int planID) {
        Dto planDto = (Dto) appDao.queryForObject(
                "App.InspectPlan.queryPlanById", planID);
        if (null == planDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "巡检计划编号无效");
        }
        int checktime = inDto.getAsInteger("checktime").intValue();
        if ((checktime < planDto.getAsInteger("executestarttime").intValue())
                || (checktime > planDto.getAsInteger("executeendtime")
                        .intValue())) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_OVER_STEP,
                    "巡检时间超出计划规定时间");
        }
        Dto planDeviceDto = (Dto) appDao.queryForObject(
                "App.InspectPlan.queryPlanDeviceByPlanIdAndDeviceId", inDto);
        if (null == planDeviceDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "巡检计划没有此设备");
        }
        int inspectPlanDeviceId = planDeviceDto.getAsInteger(
                "inspectplandeviceid").intValue();
        Dto inspectRecordInfoDto = (Dto) appDao.queryForObject(
                "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                inspectPlanDeviceId);
        if (null != inspectRecordInfoDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_REPEAT_VALUE,
                    "巡检记录已有此设备记录");
        }
        inDto.put("inspectplandeviceid", inspectPlanDeviceId);
        return null;
    }

    /**
     * 检查巡检计划编号为空是否有效.
     * @param inDto dto对象
     * @return 是否有效
     */
    @SuppressWarnings("unchecked")
    private Dto checkPlanIdNullAndDeviceId(Dto inDto) {
        List<Dto> list = ((List<Dto>) appDao
                .queryForList(
                        "App.InspectPlan.queryPlanDeviceByDeviceIdAndStateCanInspectAndExecuteTimeFit",
                        inDto));
        if ((null == list) || (list.size() < 1)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "设备没有对应巡检计划");
        }
        Dto inspectRecordInfoDto = null;
        int inspectPlanDeviceId = 0;
        for (Dto planDeviceDto : list) {
            inspectPlanDeviceId = planDeviceDto.getAsInteger(
                    "inspectplandeviceid").intValue();
            inspectRecordInfoDto = (Dto) appDao.queryForObject(
                    "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                    inspectPlanDeviceId);
            if (null == inspectRecordInfoDto) {
                inDto.put("planID", planDeviceDto.getAsInteger("planid")
                        .intValue());
                break;
            }
        }
        if (null != inspectRecordInfoDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_REPEAT_VALUE,
                    "巡检记录已有此设备记录");
        }
        inDto.put("inspectplandeviceid", inspectPlanDeviceId);
        return null;
    }

    /**
     * 保存巡检记录得到返回对象.
     * @param inDto dto对象
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    private BaseRetDto getAddInspectRecordRetInfo(Dto inDto) {
        BaseRetDto result = addInspectRecord(inDto);
        if (null != result) {
            return result;
        }

        result = addInspectRecordInfo(inDto);
        if (null != result) {
            return result;
        }
        int counta = ((Integer) appDao.queryForObject(
                "App.Inspect.queryInspectRecordDeviceFinished", inDto
                        .getAsInteger("planID").intValue())).intValue();
        if (counta < 1) {
            appDao.update("App.Inspect.updateInspectRecordFinished", inDto);
            inDto.put("state", AppCommon.INSPECT_STATE_FINISHED);
        } else {
            inDto.put("state", AppCommon.INSPECT_STATE_RUNNING);
        }
        appDao.update("App.InspectPlan.updateInspectPlanFinished", inDto);
        if (!SystemConstants.ENABLED_Y.equals(inDto.getAsString("isOK"))) {
            result = addDeviceFaultInfo(inDto);
            if (null != result) {
                return result;
            }
        }
        BaseRetDto outDto = (BaseRetDto) DtoUtils.getSuccessRetDto("");
        outDto.put("inspectrecordid", inDto.getAsInteger("inspectrecordid")
                .intValue());
        outDto.put("inspectrecordinfoid",
                inDto.getAsInteger("inspectrecordinfoid").intValue());
        result = outDto;
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Dto> queryInspectRecordInfoImages(int inspectRecordInfoId) {
        List<Dto> result = null;
        Dto dto = new BaseDto();
        dto.put("inspectrecordinfoid", inspectRecordInfoId);
        result = appDao.queryForList("App.Inspect.queryImages", dto);
        for (Dto iDto : result) {
            iDto.put("mediaurl",
                    mediaUrlService.getUrl(iDto.getAsString("mediaurl")));
        }
        return result;
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
        fileDto.put("inspectrecordinfoid",
                dto.getAsInteger("inspectrecordinfoid"));
        Dto result = DtoUtils.createMediaInfo(appDao, dto, fileDto);
        if (null != result) {
            return result;
        }
        return DtoUtils.addInfoAndCheckIntIdFail(appDao,
                "App.Inspect.addReleateMediaInfo", fileDto, "releatemediaid",
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
        result = DtoUtils.checkMedias(dto, mediaPathService);
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

    @SuppressWarnings("unchecked")
    private Dto saveMediaItemFile(Dto dto, Dto fileDto) throws IOException,
            NoSuchAlgorithmException {
        fileDto.put("inspectrecorditemid",
                dto.getAsInteger("inspectrecorditemid"));
        Dto result = DtoUtils.createMediaItemInfo(appDao, dto, fileDto);
        if (null != result) {
            return result;
        }
        return DtoUtils.addItemInfoAndCheckIntIdFail(appDao,
                "App.Inspect.addReleateMediaItemInfo", fileDto,
                "releatemediaid", "媒体关联记录");
    }

    @SuppressWarnings("unchecked")
    private Dto saveMediaItemFiles(Dto dto) throws IOException,
            NoSuchAlgorithmException {
        Dto result = null;
        result = DtoUtils.checkMedias(dto, mediaPathService);
        if (null != result) {
            return result;
        }
        List<Dto> fileList = (List<Dto>) dto.getAsList("medias");
        for (Dto fileDto : fileList) {
            result = saveMediaItemFile(dto, fileDto);
            if (null != result) {
                return result;
            }

        }
        return null;
    }

    public Dto saveUploadInspectRecordItemMedia(Dto dto) {
        Dto result = checkInspectRecordItemMedia(dto);
        if (null != result) {
            return result;
        }
        try {
            result = saveMediaItemFiles(dto);
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

    @Override
    public Dto saveUploadInspectRecordMedia(Dto dto) {
        Dto result = checkInspectRecordMedia(dto);
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

    public void setMediaPathService(MediaPathService mediaPathService) {
        this.mediaPathService = mediaPathService;
    }

    public void setMediaUrlService(MediaUrlService mediaUrlService) {
        this.mediaUrlService = mediaUrlService;
    }

}
