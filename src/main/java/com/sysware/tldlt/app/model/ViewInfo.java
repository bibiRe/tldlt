package com.sysware.tldlt.app.model;

import java.util.Date;

/**
 * Type：ViewInfo
 * Descript：显示信息.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月8日 下午5:19:59
 * Version：@version
 */
public class ViewInfo {
    
    /**
     * 信息. 
     */
    private String info;
    /**
     * 名称.
     */
    private String name;
    /**
     * 时间.
     */
    private Date time;
    /**
     * 类型.
     */
    private int type;
    public String getInfo() {
        return info;
    }
    public String getName() {
        return name;
    }
    public Date getTime() {
        return time;
    }
    public int getType() {
        return type;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public void setType(int type) {
        this.type = type;
    }
}
