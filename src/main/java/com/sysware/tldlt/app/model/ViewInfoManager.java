package com.sysware.tldlt.app.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Type：InfoManager
 * Descript：信息管理类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月8日 下午5:21:39
 * Version：@version
 */
public class ViewInfoManager {

    private Map<Integer, List<ViewInfo>> data = Maps.newConcurrentMap();

    public void addInfo(int type, String name, Date time, String info) {
        List<ViewInfo> list = data.get(type);
        if (null == list) {
            list = Lists.newArrayList();
            data.put(type, list);
        }
        ViewInfo viewInfo = new ViewInfo();
        viewInfo.setType(type);
        viewInfo.setName(name);
        viewInfo.setTime(time);
        viewInfo.setInfo(info);
        list.add(viewInfo);        
    }
}
