package com.sysware.tldlt.app.service.common;

import org.g4studio.common.dao.Dao;
import org.g4studio.common.service.impl.BaseServiceImpl;

/**
 * Type：BaseAppServiceImpl
 * Descript：基本App服务实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 上午11:38:07
 * Version：@version
 */
public class BaseAppServiceImpl extends BaseServiceImpl {
    
    /**
     * 基类中给子类暴露的一个DAO接口<br>
     * 连接平台数据库
     */
    protected Dao appDao;

    public void setAppDao(Dao appDao) {
        this.appDao = appDao;
    }
    
}
