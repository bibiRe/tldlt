package com.sysware.tldlt.app.service.devmgr;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface DevMgrService
{

	public Dto queryDeptinfoByDeptid(Dto pDto);
	
	public Dto queryDeptItems(Dto pDto);
	
	public String getDeviceInfo(Dto pDto);
	
	public String getDeviceModel(Dto pDto);
	
	public String getParentDevice(Dto pDto);
	
	public String getRegion(Dto pDto);
	
    public Dto addInfo(Dto inDto);
    
    public Dto updateInfo(Dto inDto);
    
    public Dto deleteInfo(Dto inDto);
	
}
