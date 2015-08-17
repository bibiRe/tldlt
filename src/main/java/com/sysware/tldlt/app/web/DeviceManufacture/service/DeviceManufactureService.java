package com.sysware.tldlt.app.web.DeviceManufacture.service;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface DeviceManufactureService extends BaseService
{
	
	public String getDeviceManufacture(Dto pDto);
	
	public Dto addInfo(Dto inDto);
	
	public Dto updateInfo(Dto inDto);
	
	public Dto deleteInfo(Dto inDto);
}
