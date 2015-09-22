package com.sysware.tldlt.app.service.devicefault;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

/**
 * Type：DeviceFaultService
 * Descript：设备故障服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月22日 下午2:05:21
 * Version：@version
 */
public interface DeviceFaultService extends BaseService {

    /**
     * 更新状态信息.
     * @param dto dto对象
     * @return dto对象
     */
    Dto updateStateInfo(Dto dto);
    
}
