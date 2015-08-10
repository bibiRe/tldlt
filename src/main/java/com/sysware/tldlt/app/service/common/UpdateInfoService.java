package com.sysware.tldlt.app.service.common;

import org.g4studio.core.metatype.Dto;

/**
 * Type：UpdateInfoService
 * Descript：更新数据接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月23日 下午4:35:20
 * Version：@version
 */
public interface UpdateInfoService {
    /**
     * 更新数据.
     * @param inDto 数据
     * @return 更新内容
     */
    Dto updateInfo(Dto inDto);
}
