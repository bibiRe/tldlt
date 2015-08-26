package com.sysware.tldlt.app.service.user;

import org.g4studio.core.metatype.Dto;

/**
 * Type：UserService
 * Descript：用户服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 上午9:44:15
 * Version：@version
 */
public interface UserService {

    /**
     * 保存GPS信息.
     * @param info 信息
     * @return dto对象
     */
    Dto saveGPSInfo(Dto info);

}
