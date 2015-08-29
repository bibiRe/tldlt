package com.sysware.tldlt.app.service.inspect;

import java.util.List;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
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
     * 保存巡检记录得到返回对象.
     * @param inDto dto对象
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    private BaseRetDto getAddInspectRecordRetInfo(Dto inDto) {
        addInspectRecord(inDto);
        addInspectRecordInfo(inDto);
        int counta = ((Integer) appDao.queryForObject(
                "App.Inspect.queryInspectRecordDeviceFinished", inDto
                        .getAsInteger("planID").intValue())).intValue();
        if (counta < 1) {
            appDao.update("App.Inspect.updateInspectRecordInfoFinished", inDto);
            inDto.put("state", AppCommon.INSPECT_STATE_FINISHED);            
        } else {
            inDto.put("state", AppCommon.INSPECT_STATE_RUNNING);
        }
        appDao.update("App.InspectPlan.updateInspectPlanFinished", inDto);
        BaseRetDto outDto = (BaseRetDto) DtoUtils.getSuccessRetDto("");
        outDto.put("inspectrecordid", inDto.getAsInteger("inspectrecordid")
                .intValue());
        outDto.put("inspectrecordinfoid",
                inDto.getAsInteger("inspectrecordinfoid").intValue());
        return outDto;
    }

    /**
     * 增加巡检记录信息.
     * @param inDto dto对象
     */
    private void addInspectRecordInfo(Dto inDto) {
        appDao.insert("App.Inspect.addInspectRecordInfo", inDto);
    }

    /**
     * 增加巡检记录.
     * @param inDto 巡检记录
     */
    @SuppressWarnings("unchecked")
    private void addInspectRecord(Dto inDto) {
        Dto inspectRecordDto = (Dto) appDao.queryForObject(
                "App.Inspect.queryInspectRecordByPlanId",
                inDto.getAsInteger("planID").intValue());
        int inspectRecordId = 0;
        if (null != inspectRecordDto) {
            inspectRecordId = inspectRecordDto.getAsInteger("inspectrecordid")
                    .intValue();
        }
        if (inspectRecordId < 1) {
            appDao.insert("App.Inspect.addInspectRecord", inDto);
            inspectRecordId = inDto.getAsInteger("inspectrecordid").intValue();
        } else {
            inDto.put("inspectrecordid", inspectRecordId);
        }
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
        result = checkPlanIdAndDeviceId(inDto);
        return result;
    }

    /**
     * 检查巡检计划编号和设备编号.
     * @param inDto dto对象
     * @return 是否有效.
     */
    /**
     * @param inDto
     * @return
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
                "inpsectplandeviceid").intValue();
        Dto inspectRecordInfoDto = (Dto) appDao.queryForObject(
                "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                inspectPlanDeviceId);
        if (null != inspectRecordInfoDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_REPEAT_VALUE,
                    "巡检记录已有此设备记录");
        }
        inDto.put("inpsectplandeviceid", inspectPlanDeviceId);
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
                    "inpsectplandeviceid").intValue();
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
        inDto.put("inpsectplandeviceid", inspectPlanDeviceId);
        return null;
    }

}
