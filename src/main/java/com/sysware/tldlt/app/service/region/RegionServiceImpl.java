package com.sysware.tldlt.app.service.region;

import java.util.Collection;
import java.util.List;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;

/**
 * Type：RegionServiceImpl
 * Descript：区域服务实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月13日 下午1:53:19
 * Version：@version
 */
public class RegionServiceImpl extends BaseAppServiceImpl implements
        RegionService {

    @SuppressWarnings("rawtypes")
    @Override
    public Collection queryRegionItems(Dto dto) {
        List result = appDao.queryForList("App.Region.queryRegionItemsByDto",
                dto);
        return result;
    }

}
