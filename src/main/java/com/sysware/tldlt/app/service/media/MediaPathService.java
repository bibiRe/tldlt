package com.sysware.tldlt.app.service.media;

/**
 * Type：MediaPathService
 * Descript：媒体路径服务接口.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月9日 上午10:39:16
 * Version：@version
 */
public interface MediaPathService {
    /**
     * 得到媒体保存路径.
     * @param partAddress 部分地址.
     * @return 路径
     */
    public String getPath(String childPath);
}
