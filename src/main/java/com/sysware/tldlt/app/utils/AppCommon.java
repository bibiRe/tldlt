package com.sysware.tldlt.app.utils;

/**
 * Type：AppCommon
 * Descript：常量类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月22日 上午9:00:15
 * Version：@version
 */
public class AppCommon {

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
     * GPS信息类型-设备.
     */
    public static final int GPS_INFO_TYPE_DEVICE = 2;

    /**
     * GPS信息类型-巡检计划设备.
     */
    public static final int GPS_INFO_TYPE_INSPECTPLAN_DEVICE = 3;

    /**
     * 时间间隔.
     */
    public static final int TIME_INTERVAL = 1000;
    
    /**
     * 巡检状态-完成. 
     */
    public static final int INSPECT_STATE_FINISHED = 6;
    /**
     * 巡检状态-进行中.
     */
    public static final int INSPECT_STATE_RUNNING = 5;

}
