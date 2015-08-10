package com.sysware.tldlt.app.model.vo.devicetype;

/**
 * Type：DeviceTypeVo
 * Descript：设备类型值对象.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月13日 上午9:55:25
 * Version：@version
 */
public class DeviceTypeVo {
    /**
     * 设备类型编号.
     */
    private String id;

    /**
     * 设备类型名称.
     */
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
