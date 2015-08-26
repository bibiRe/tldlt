package com.sysware.tldlt.app.service.devicemodel;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

/**
 * Type：DeviceModelService
 * Descript：设备型号服务接口.
 * Create：yc
 * Create Time：2015年8月19日 下午3:31:42
 * Version：@version
 */
public interface DeviceModelService
{

	public String getDeviceModel(Dto pDto);
	
	public Dto addInfo(Dto inDto);
	
    public Dto deleteInfo(Dto inDto);
    
    public Dto updateInfo(Dto inDto);
	
	
	public String getDeviceType(Dto pDto);
	
	public String getDeviceManuf(Dto pDto);
}
