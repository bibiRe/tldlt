package com.sysware.tldlt.app.service.device;

import java.util.Date;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Utils;

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
                    "App.InspectPlan.queryPlanDeviceByDeviceId", dto);
            if (null == inspectDto) {
                return DtoUtils.getErrorRetDto(
                        AppCommon.RET_CODE_INVALID_VALUE, "巡检计划没有此设备");
            }
            dto.put("type", AppCommon.GPS_INFO_TYPE_INSPECTPLAN_DEVICE);
            dto.put("releaterecordid",
                    inspectDto.getAsInteger("inpsectplandeviceid").intValue());
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
        String deviceId = info.getAsString("deviceID");
        if (AppTools.isEmptyString(deviceId)) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "设备编号为空");
        }
        Dto deviceDto = (BaseDto) appDao.queryForObject(
                "App.Device.queryDeviceInfo", deviceId);
        if (null == deviceDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "设备编号无效");
        }
        Dto result = checkGPSInspectPlanInfo(info);
        if (null != result) {
            return result;
        }
        return DtoUtils.checkGPSInfo(info);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto saveGPSInfo(Dto info) {
        Dto outDto = checkSaveGPSInfo(info);
        if (null != outDto) {
            return outDto;
        }
        long datetime = info.getAsLong("datetime").longValue();
        Date dt = new Date();
        dt.setTime(datetime * AppCommon.TIME_INTERVAL);
        String time = G4Utils.Date2String(dt, "yyyy-MM-dd HH:mm:ss");
        info.put("time", time);
        appDao.insert("App.User.saveGPSInfo", info);
        appDao.insert("App.Device.saveReleateGPSInfo", info);
        return DtoUtils.getSuccessRetDto("");
    }

}