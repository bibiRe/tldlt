package com.sysware.tldlt.app.service.media;

import org.g4studio.common.service.impl.BaseServiceImpl;

import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DaoUtils;

/**
 * Type：MediaUrlServiceGlobalParamImpl
 * Descript：媒体链接属性文件全局参数实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月9日 上午10:56:31
 * Version：@version
 */
public class MediaUrlServiceGlobalParamImpl extends BaseServiceImpl implements
        MediaUrlService {
    @Override
    public String getUrl(String partAddress) {
        return AppTools.addPathEndSeprator(AppTools.convertNullStr(DaoUtils
                .getGlobalParamsValue(g4Dao, "MEDIA_FILE_URL_HEADER")))
                + partAddress;
    }

}
