package com.sysware.tldlt.app.service.device;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：DeviceServiceImpl
 * Descript：设备服务实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 下午2:36:09
 * Version：@version
 */
public class DeviceServiceImpl extends BaseAppServiceImpl implements
        DeviceService {

    /**
     * 检查GPS巡检计划信息.
     * @param dto dto对象
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    private Dto checkGPSInspectPlanInfo(Dto dto) {
        int planId = 0;
        Integer planObj = dto.getAsInteger("planID");
        if (null != planObj) {
            planId = planObj.intValue();
        }
        if (planId > 0) {
            Dto inspectDto = (Dto) appDao.queryForObject(
                    "App.InspectPlan.queryPlanDeviceByPlanIdAndDeviceId", dto);
            if (null == inspectDto) {
                return DtoUtils.getErrorRetDto(
                        AppCommon.RET_CODE_INVALID_VALUE, "巡检计划没有此设备");
            }
            dto.put("type", AppCommon.GPS_INFO_TYPE_INSPECTPLAN_DEVICE);
            dto.put("releaterecordid",
                    inspectDto.getAsInteger("inspectplandeviceid").intValue());
        } else {
            dto.put("planID", null);
            dto.put("type", AppCommon.GPS_INFO_TYPE_DEVICE);
            dto.put("releaterecordid", dto.getAsString("deviceID"));
        }
        return null;
    }

    /**
     * 检查保存GPS信息.
     * @param info 信息.
     * @return dto对象
     */
    private Dto checkSaveGPSInfo(Dto info) {
        if (null == info) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "数据为空");
        }
        Dto result = DtoUtils.checkDtoDeviceId(appDao, info);
        if (null != result) {
            return result;
        }

        result = checkGPSInspectPlanInfo(info);
        if (null != result) {
            return result;
        }
        result = DtoUtils.checkDtoCheckTime(info, "datetime");
        if (null != result) {
            return result;
        }
        return DtoUtils.checkGPSInfo(info);
    }

    @Override
    public Dto saveGPSInfo(Dto info) {
        Dto result = checkSaveGPSInfo(info);
        if (null != result) {
            return result;
        }
        result = DtoUtils.addGPSInfo(appDao, info);
        if (null != result) {
            return result;
        }
        appDao.update("App.Device.updateLongLatInfo", info);
        return DtoUtils.getSuccessRetDto("");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto getLongLatInfo(String deviceId) {
        if (AppTools.isEmptyString(deviceId)) {
            return null;
        }
        Dto dto = (Dto) appDao.queryForObject(
                "App.Device.queryLongLatInfoByDeviceId", deviceId);
        if (null == dto) {
            return null;
        }
        if (!AppTools.isEmptyString(dto.getAsString("longtitude"))) {
            return dto;
        }
        Dto pDto = getLongLatInfo(dto.getAsString("parentdeviceid"));
        if (null == pDto) {
            return null;
        }
        dto.put("longtitude", pDto.getAsString("longtitude"));
        dto.put("latitude", pDto.getAsString("latitude"));
        return dto;
    }

}
