package com.sysware.tldlt.app.service.inspectionrecordmakeupfill;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface InspectionRecordMakeupFillService 
{

	public String getRecordMakeup(Dto pDto);
	
	public String getInspecPlanDevice(Dto pDto);
	
	public Dto addInspecPlanRecordInfo(Dto pDto);
	
	public String getInspectRecordInfo(Dto pDto);
	
	public String getDeviceCheckContentInfo(Dto pDto);
	
	public Dto addInspecPlanRecordItem(Dto pDto);
	
	public String getInspectRecordItem(Dto pDto);
	
	public String getInspectRecordInfo2(Dto pDto);

	public String getInspectRecordItem2(Dto pDto);
	
	public Dto finishInspecRecord(Dto pDto);
}
