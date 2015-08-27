package com.sysware.tldlt.app.service.common;

import org.g4studio.core.id.IDGenerator;
import org.g4studio.system.common.util.idgenerator.IdGenerator;

/**
 * Type：GenerateStringIdIdGeneratorImpl
 * Descript：产生字符串编号IdGenerator实现类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月27日 上午10:38:31
 * Version：@version
 */
public class GenerateStringIdIdGeneratorImpl implements GenerateStringId {
    private IdGenerator idGenerator = new IdGenerator();
    private IDGenerator iDGenerator;

    /**
     * 设置字段名称.
     * @param fieldName 字段名称
     */
    public void setFieldName(String fieldName) {
        idGenerator.setFieldname(fieldName);
        iDGenerator = idGenerator.getDefaultIDGenerator();
    }

    @Override
    public String get() {
        return iDGenerator.create();
    }

}
