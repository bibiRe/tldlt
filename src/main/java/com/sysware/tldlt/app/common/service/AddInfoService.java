package com.sysware.tldlt.app.common.service;

import org.g4studio.core.metatype.Dto;

/**
 * Type：AddInfoService
 * Descript：增加信息服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月23日 下午4:33:21
 * Version：@version
 */
public interface AddInfoService {
    /**
     * 新增.
     * @param inDto 数据
     * @return 新增内容
     */
    Dto addInfo(Dto inDto);
}
