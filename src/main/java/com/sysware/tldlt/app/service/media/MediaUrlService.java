package com.sysware.tldlt.app.service.media;

import org.g4studio.common.service.BaseService;

/**
 * Type：MediaUrlService
 * Descript：媒体链接服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月8日 下午4:22:42
 * Version：@version
 */
public interface MediaUrlService  extends BaseService{
    /**
     * 得到媒体链接.
     * @param partAddress 部分地址.
     * @return 链接
     */
    public String getUrl(String partAddress);
}
