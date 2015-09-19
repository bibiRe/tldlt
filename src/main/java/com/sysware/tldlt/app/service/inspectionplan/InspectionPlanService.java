package com.sysware.tldlt.app.service.inspectionplan;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface InspectionPlanService 
{

	public String getPlan(Dto pDto);
	
	public String getUser(Dto pDto);
	
	public Dto addPlan(Dto pDto);
	
	public String getDevice(Dto pDto);
	
	public Dto deletePlan(Dto inDto);
	
	public Dto updatePlan(Dto inDto);
	
	public Dto approvalPlan(Dto inDto);
}
