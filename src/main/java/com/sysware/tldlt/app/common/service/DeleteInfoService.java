package com.sysware.tldlt.app.common.service;

import org.g4studio.core.metatype.Dto;

/**
 * Type：DeleteInfoService
 * Descript：删除数据服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月23日 下午4:37:52
 * Version：@version
 */
public interface DeleteInfoService {
    /**
     * 删除数据.
     * @param inDto 数据.
     * @return 删除成功与否.
     */
    Dto deleteInfo(Dto inDto);
}
