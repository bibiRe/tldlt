package com.sysware.tldlt.app.web.DeviceModel.service;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface DeviceModelService
{

	public String getDeviceModel(Dto pDto);
	
	public Dto addInfo(Dto inDto);
	
    public Dto deleteInfo(Dto inDto);
    
    public Dto updateInfo(Dto inDto);
	
	
	public String getDeviceType(Dto pDto);
	
	public String getDeviceManuf(Dto pDto);
}
