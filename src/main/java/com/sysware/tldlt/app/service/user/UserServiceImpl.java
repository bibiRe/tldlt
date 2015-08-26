package com.sysware.tldlt.app.service.user;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
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
        if (null == info) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "数据为空");
        }
        if (AppTools.isEmptyString(info.getAsString("userid"))) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_NULL_VALUE,
                    "用户编号为空");
        }
        Dto userDto = (BaseDto) g4Dao.queryForObject("User.getUserInfoByKey",
                info);
        if (null == userDto) {
            return DtoUtils.getErrorRetDto(AppCommon.RET_CODE_INVALID_VALUE,
                    "用户编号无效");
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
        info.put("time", AppTools.unixTime2DateStr(info.getAsLong("datetime")
                .longValue()));
        appDao.insert("App.User.saveGPSInfo", info);
        appDao.insert("App.User.saveReleateGPSInfo", info);
        return DtoUtils.getSuccessRetDto("");
    }

}
