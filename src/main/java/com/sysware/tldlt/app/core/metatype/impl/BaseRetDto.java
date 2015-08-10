package com.sysware.tldlt.app.core.metatype.impl;

import org.g4studio.core.metatype.impl.BaseDto;

import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;

/**
 * Type：BaseRetDto
 * Descript：基础返回类dto.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月22日 上午8:58:10
 * Version：@version
 */
public class BaseRetDto extends BaseDto{
    
    /**
     * 
     */
    private static final long serialVersionUID = 8265857852614658773L;

    /**
     * 得到描述.
     * @return 描述
     */
    public String getDesc() {
        return AppTools.convertNullStr(getAsString("desc"));
    }

    /**
     * 设置描述.
     * @param desc 描述
     */
    @SuppressWarnings("unchecked")
    public void setDesc(String desc) {
        put("desc", desc);
    }

    /**
     * 得到返回值.
     * @return 返回值.
     */
    public int getRetCode() {
        Integer retCode = getAsInteger("retCode");
        if (null == retCode) {
            return AppCommon.RET_CODE_UNKNOWN;
        }
        return retCode.intValue();
    }

    /**
     * 设置返回值.
     * @param retCode
     */
    @SuppressWarnings("unchecked")
    public void setRetCode(int retCode) {
        put("retCode", Integer.valueOf(retCode));
    }

    /**
     * 设置返回值成功.
     */
    public void setRetSuccess() {
        setRetCode(AppCommon.RET_CODE_SUCCESS);        
    }

    /**
     * 返回值是否成功.
     * @return 返回值是否成功.
     */
    public boolean isRetSuccess() {
        return AppCommon.RET_CODE_SUCCESS == getRetCode();
    }
}
