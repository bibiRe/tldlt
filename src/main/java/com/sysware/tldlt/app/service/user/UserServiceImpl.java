package com.sysware.tldlt.app.service.user;

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

    @Override
    public Dto saveGPSInfo(Dto info) {
        Dto outDto = checkSaveGPSInfo(info);
        if (null != outDto) {
            return outDto;
        }
        appDao.insert("App.User.saveGPSInfo", info);
        appDao.insert("App.User.saveReleateGPSInfo", info);
        return DtoUtils.getSuccessRetDto("");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto reportDeviceStatus(Dto info) {
        Dto result = checkReportDeviceStatus(info);
        if (null != result) {
            return result;
        }
        appDao.insert("App.User.reportDeviceStatus", info);
        BaseRetDto outDto = (BaseRetDto) DtoUtils.getSuccessRetDto("");
        outDto.put("devicesuggestinfoid", info.get("devicesuggestinfoid"));
        return outDto;
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

}
