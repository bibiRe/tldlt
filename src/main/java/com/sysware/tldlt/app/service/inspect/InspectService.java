package com.sysware.tldlt.app.service.inspect;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.service.common.AddInfoService;

/**
 * Type：InspectService
 * Descript：巡检服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:36:17
 * Version：@version
 */
public interface InspectService extends BaseService, AddInfoService {

    /**
     * 上传巡检记录媒体.
     * @param dto
     * @return 
     */
    Dto saveUploadInspectRecordMedia(Dto dto);

}
