package com.sysware.tldlt.app.service.inspectionrecordmakeup;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface InspectionRecordMakeupService 
{
	public String getRecordMakeup(Dto pDto);
	
	public Dto addRecordMakeup(Dto inDto);
	
	public String getInspecPlan(Dto pDto);
	
	public Dto updateRecordMakeup(Dto inDto);
	
	public Dto deleteRecordMakeup(Dto inDto);
	
    public Dto approvalRecordMakeup(Dto inDto);
}
