package com.sysware.tldlt.app.utils;

import java.util.Date;

import org.g4studio.core.util.G4Utils;

/**
 * Type：AppTools
 * Descript：工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:48:25
 * Version：@version
 */
public class AppTools {

    /**
     * 转换NULL字符串为空字符串.
     * @param value 值
     * @return 字符串
     */
    public static String convertNullStr(String value) {
        if (null == value) {
            return "";
        } else {
            return value;
        }
    }

    /**
     * 判断字符串为空.
     * @param value 字符串
     * @return 是否为空
     */
    public static boolean isEmptyString(String value) {
        return (null == value || value.equals(""));
    }

    /**
     * unix时间转换为时间字符串.
     * @param unixTime unix时间.
     * @return 时间字符串
     */
    public static String unixTime2DateStr(long unixTime) {
        Date dt = new Date();
        dt.setTime(unixTime * AppCommon.TIME_INTERVAL);
        return G4Utils.Date2String(dt, "yyyy-MM-dd HH:mm:ss");
    }
}
