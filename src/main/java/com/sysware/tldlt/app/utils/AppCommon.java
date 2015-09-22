package com.sysware.tldlt.app.utils;

/**
 * Type：AppCommon
 * Descript：常量类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月22日 上午9:00:15
 * Version：@version
 */
/**
 * Type：AppCommon
 * Descript：
 * Create：SW-ITS-HHE
 * Create Time：2015年9月2日 下午2:15:51
 * Version：@version
 */
public class AppCommon {
    /**
     * 设备错误信息类型-巡检.
     */
    public static final int DEVICE_FAULTINFO_TYPE_INSPECT = 1;
    /**
     * 设备错误信息类型-人员.
     */
    public static final int DEVICE_FAULTINFO_TYPE_USER = 2;
    
    /**
     * GPS信息类型-用户.
     */
    public static final int GPS_INFO_TYPE_USER = 1;
    /**
     * GPS信息类型-设备.
     */
    public static final int GPS_INFO_TYPE_DEVICE = 2;
    /**
     * GPS信息类型-巡检计划设备.
     */
    public static final int GPS_INFO_TYPE_INSPECTPLAN_DEVICE = 3;
    /**
     * 分钟秒数
     */
    public static final int MIN_SEC = 60;
    /**
     * 小时秒数
     */
    public static final int HOUR_SEC = 60 * MIN_SEC;
    /**
     * 实时巡检数据类型-用户GPS.
     */
    public static final int INSPECT_REAL_VIEW_DATA_TYPE_GPS_USER = 1;
    /**
     * 实时巡检数据类型-设备GPS.
     */
    public static final int INSPECT_REAL_VIEW_DATA_TYPE_GPS_DEVICE = 2;

    /**
     * 实时巡检数据类型-巡检设备GPS.
     */
    public static final int INSPECT_REAL_VIEW_DATA_TYPE_GPS_INSPECTPLAN_DEVICE = 3;
    /**
     * 实时巡检数据类型-巡检设备.
     */
    public static final int INSPECT_REAL_VIEW_DATA_TYPE_INSPECT_DEVICE = 4;
    /**
     * 实时巡检数据类型-设备上报.
     */
    public static final int INSPECT_REAL_VIEW_DATA_TYPE_DEVICESUGGEST = 5;
    /**
     * 巡检状态-进行中.
     */
    public static final int INSPECT_STATE_RUNNING = 5;
    /**
     * 巡检状态-完成.
     */
    public static final int INSPECT_STATE_FINISHED = 6;

    /**
     * 长度-KB
     */
    public static final int KB_SIZE = 1024;

    /**
     * 长度-MB
     */
    public static final int MB_SIZE = 1024 * KB_SIZE;

    /**
     * 媒体类型-图片.
     */
    public static final int MEDIA_TYPE_IMAGE = 0;

    /**
     * 创建路径失败.
     */
    public static final int RET_CODE_CREATE_PATH_FAIL = -10;

    /**
     * 返回码-成功.
     */
    public static final int RET_CODE_SUCCESS = 0;

    /**
     * 返回码-未知错误.
     */
    public static final int RET_CODE_UNKNOWN = -1;

    /**
     * 返回码-删除失败.
     */
    public static final int RET_CODE_DELETE_ERROR = -2;
    /**
     * 返回码-空值.
     */
    public static final int RET_CODE_NULL_VALUE = -3;

    /**
     * 返回码-无效值.
     */
    public static final int RET_CODE_INVALID_VALUE = -4;

    /**
     * 返回码-重复值.
     */
    public static final int RET_CODE_REPEAT_VALUE = -5;
    /**
     * 返回码-越界.
     */
    public static final int RET_CODE_OVER_STEP = -6;

    /**
     * 新增保存失败.
     */
    public static final int RET_CODE_ADD_FAIL = -7;

    /**
     * 更新保存失败.
     */
    public static final int RET_CODE_UPDATE_FAIL = -8;

    /**
     * 创建文件失败.
     */
    public static final int RET_CODE_CREATE_FILE_FAIL = -11;
    /**
     * 时间间隔.
     */
    public static final int TIME_INTERVAL = 1000;
}
