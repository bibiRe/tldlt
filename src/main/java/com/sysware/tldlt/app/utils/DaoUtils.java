package com.sysware.tldlt.app.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.common.dao.Dao;
import org.g4studio.core.metatype.Dto;

/**
 * Type：DaoUtils
 * Descript：Dao工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月9日 上午11:03:25
 * Version：@version
 */
public class DaoUtils {
    /**
     * Log
     */
    private static final Log log = LogFactory.getLog(DaoUtils.class);
    /**
     * 得到全局变量值.
     * @param key 键值
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static String getGlobalParamsValue(Dao dao, String key) {
        String result = null;
        try {
            List<Dto> paramList = dao.queryForList("Resource.getParamList");
            for (Dto paramDto: paramList) {
                if (key.equals(paramDto.getAsString("paramkey"))) {
                    result = paramDto.getAsString("paramvalue");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("全局参数表加载失败!");
        }
        return result;
    }
    
}
