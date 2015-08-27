package com.sysware.tldlt.app.local.rpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;

import com.google.common.collect.Maps;
import com.sysware.tldlt.app.utils.AppTools;

/**
 * Type：RPCRetDto
 * Descript：RPC返回Dto对象类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:03:32
 * Version：@version
 */
public class RPCRetDto {
    /**
     * 数据列表.
     */
    @SuppressWarnings("rawtypes")
    private Collection data = new ArrayList();
    /**
     * 信息.
     */
    String message;
    /**
     * 成功标志.
     */
    String success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 将此Dto对象转换为Json格式字符串.
     * @return string 返回Json格式字符串
     */
    public String toJson() {
        return toJson(null);
    }

    /**
     * 将此Dto对象转换为Json格式字符串.
     * @param pFormat 时间格式
     * @return string 返回Json格式字符串
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String toJson(String pFormat) {
        String strJson = null;
        Map map = Maps.newHashMap();
        map.put("success", success);
        map.put("message", message);
        map.put("data", data);
        if (!AppTools.isEmptyString(pFormat)) {
            strJson = JsonHelper.encodeObject2Json(map, pFormat);
        } else {
            strJson = JsonHelper.encodeObject2Json(map);
        }
        return strJson;
    }

    /**
     * 增加数据.
     * @param dto 数据
     */
    @SuppressWarnings("unchecked")
    public void addData(Dto dto) {
        this.data.add(dto);
    }

    /**
     * 增加列表数据.
     * @param list 列表数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addAllData(Collection list) {
        this.data.addAll(list);
    }
}
