package com.sysware.tldlt.app.service.user;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

/**
 * Type：UserService
 * Descript：用户服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 上午9:44:15
 * Version：@version
 */
public interface UserService extends BaseService {

    /**
     * 保存GPS信息.
     * @param info 信息
     * @return dto返回信息
     */
    Dto saveGPSInfo(Dto info);

    /**
     * 上报设备状态信息.
     * @param info dto对象
     * @return dto返回信息
     */
    Dto reportDeviceStatus(Dto info);

}
