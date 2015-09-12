package com.sysware.tldlt.app.service.device;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

/**
 * Type：DeviceService
 * Descript：设备服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 下午2:27:36
 * Version：@version
 */
public interface DeviceService extends BaseService {

    /**
     * 保存设备巡检计划对应GPS信息.
     * @param info 信息
     * @return 
     */
    Dto saveGPSInfo(Dto info);
    /**
     * 得到设备的GPS信息.
     * @param deviceId 设备编号
     * @return dto信息.
     */
    Dto getLongLatInfo(String deviceId);
}
