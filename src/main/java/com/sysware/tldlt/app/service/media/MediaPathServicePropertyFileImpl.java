package com.sysware.tldlt.app.service.media;

import com.sysware.tldlt.app.utils.AppTools;

/**
 * Type：MediaPathServicePropertyFileImpl
 * Descript：媒体路径属性文件实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月8日 下午4:26:18
 * Version：@version
 */
public class MediaPathServicePropertyFileImpl implements MediaPathService {

    @Override
    public String getPath(String childPath) {
        String savePath = AppTools.getAppPropertyValue("mediaFilePath", "");
        if (!AppTools.isEmptyString(childPath)) {
            savePath = AppTools.addPathEndSeprator(savePath) + childPath;
        }
        return savePath;
    }

}
