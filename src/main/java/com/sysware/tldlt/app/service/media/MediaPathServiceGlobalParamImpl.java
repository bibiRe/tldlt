package com.sysware.tldlt.app.service.media;

import org.g4studio.common.service.impl.BaseServiceImpl;

import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DaoUtils;

/**
 * Type：MediaPathServiceGlobalParamImpl
 * Descript：媒体路径属性全局参数实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月9日 上午11:34:46
 * Version：@version
 */
public class MediaPathServiceGlobalParamImpl extends BaseServiceImpl implements
        MediaPathService {

    @Override
    public String getPath(String childPath) {
        String savePath = AppTools.convertNullStr(DaoUtils
                .getGlobalParamsValue(g4Dao, "MEDIA_FILE_SAVE_PATH"));
        if (!AppTools.isEmptyString(childPath)) {
            savePath = AppTools.addPathEndSeprator(savePath) + childPath;
        }
        return savePath;       
    }

}
