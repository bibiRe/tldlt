package com.sysware.tldlt.app.service.devicetype;

import java.util.Collection;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.service.common.AddInfoService;
import com.sysware.tldlt.app.service.common.DeleteInfoService;
import com.sysware.tldlt.app.service.common.UpdateInfoService;

/**
 * Type：DeviceTypeService
 * Descript：设备类型服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:31:42
 * Version：@version
 */
public interface DeviceTypeService extends BaseService, AddInfoService,
        UpdateInfoService, DeleteInfoService {
    /**
     * 查询设备类型列表.
     * @param dto 区域对象
     * @return 区域列表对象
     */
    @SuppressWarnings("rawtypes")
    Collection queryDeviceTypeItems(Dto dto);
}
