package com.sysware.tldlt.app.service.inspectionplan;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface InspectionPlanService 
{

	public String getPlan(Dto pDto);
	
	public String getUser(Dto pDto);
	
	public Dto addPlan(Dto pDto);
	
	public String getDevice(Dto pDto);
}
