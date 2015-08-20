package com.sysware.tldlt.app.service.region;

import java.util.Collection;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.common.service.AddInfoService;
import com.sysware.tldlt.app.common.service.DeleteInfoService;
import com.sysware.tldlt.app.service.common.UpdateInfoService;

/**
 * Type：RegionService
 * Descript：区域服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月13日 下午1:29:03
 * Version：@version
 */
public interface RegionService extends BaseService, AddInfoService,
        UpdateInfoService, DeleteInfoService {

    /**
     * 查询区域列表.
     * @param dto 区域对象
     * @return 区域列表对象
     */
    @SuppressWarnings("rawtypes")
    Collection queryRegionItems(Dto dto);

}
