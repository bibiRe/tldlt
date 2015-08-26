package com.sysware.tldlt.app.service.devicemanufacture;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

/**
 * Type：DeviceManufactureService
 * Descript：设备厂家服务接口.
 * Create：yc
 * Create Time：2015年8月19日 下午3:31:42
 * Version：@version
 */

public interface DeviceManufactureService extends BaseService
{
	
	public String getDeviceManufacture(Dto pDto);
	
	public Dto addInfo(Dto inDto);
	
	public Dto updateInfo(Dto inDto);
	
	public Dto deleteInfo(Dto inDto);
}
