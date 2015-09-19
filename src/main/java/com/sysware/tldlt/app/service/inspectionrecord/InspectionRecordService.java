package com.sysware.tldlt.app.service.inspectionrecord;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;


public interface InspectionRecordService
{
	public String getRecord(Dto pDto);
	
	public String getRecordInfo(Dto pDto);
	
	public String getRecordInfoItem(Dto pDto);
	
	public String getRecordMediaInfo(Dto pDto);
	
}
