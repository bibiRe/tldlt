package com.sysware.tldlt.app.service.devicefault;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * Type：DeviceFaultServiceImpl
 * Descript：设备故障服务接口实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月22日 下午2:49:32
 * Version：@version
 */
public class DeviceFaultServiceImpl extends BaseAppServiceImpl implements
        DeviceFaultService {

    /**
     * 检查处理人.
     * @param inDto 输入Dto
     * @return 错误返回Dto
     */
    @SuppressWarnings("unchecked")
    private Dto checkHandler(Dto inDto) {
        Dto result;
        String handler = inDto.getAsString("handler");
        Dto userDto = new BaseDto();
        userDto.put("userid", handler);
        result = DtoUtils.checkDtoUserId(g4Dao, userDto);
        return result;
    }

    /**
     * 检查状态值.
     * @param inDto 输入dto
     * @return 错误返回Dto.
     */
    private Dto checkState(Dto inDto) {
        Integer state = inDto.getAsInteger("state");
        if (null == state) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "处理状态值不存在");
        }
        if (0 == state.intValue()) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "处理状态值无效");
        }
        return null;
    }

    /**
     * 检查更新状态值.
     * @param inDto dto对象
     * @return 错误返回Dto.
     */
    private Dto checkUpdateStateInfo(Dto inDto) {
        Dto result = DtoUtils.checkDtoIntIdExist(appDao,
                "App.DeviceFault.queryDeviceFaultById", inDto,
                "devicefaultinfoid");
        if (null != result) {
            return result;
        }
        result = checkState(inDto);
        if (null != result) {
            return result;
        }
        result = checkHandler(inDto);
        if (null != result) {
            return result;
        }
        result = DtoUtils.checkDtoCheckTime(inDto, "handletime");
        if (null != result) {
            return result;
        }
        return result;
    }

    @Override
    public Dto updateStateInfo(Dto dto) {
        Dto result = checkUpdateStateInfo(dto);
        if (null != result) {
            return result;
        }
        if (appDao.update("App.DeviceFault.updateStateInfo", dto) < 1) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_UPDATE_FAIL,
                    "更新处理状态失败");
        } else {
            return DtoUtils.getSuccessRetDto("更新状态成功");
        }
    }

}
