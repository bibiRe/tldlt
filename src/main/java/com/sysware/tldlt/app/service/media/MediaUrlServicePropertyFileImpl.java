package com.sysware.tldlt.app.service.media;

import com.sysware.tldlt.app.utils.AppTools;

/**
 * Type：MediaUrlServicePropertyFileImpl
 * Descript：媒体链接属性文件实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月8日 下午4:26:18
 * Version：@version
 */
public class MediaUrlServicePropertyFileImpl implements MediaUrlService {

    @Override
    public String getUrl(String partAddress) {
        return AppTools.addPathEndSeprator(AppTools
                .getAppPropertyValue("mediaFileUrl", "")) + partAddress;
    }

}
