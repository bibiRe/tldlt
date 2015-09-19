package com.sysware.tldlt.app.service.inspectionrecord;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.service.inspectionrecord.InspectionRecordService;
import com.sysware.tldlt.app.service.media.MediaUrlService;

import java.util.List;

import org.g4studio.common.service.impl.BaseServiceImpl;
import org.g4studio.common.util.SpringBeanLoader;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.common.dao.vo.UserInfoVo;
import org.g4studio.system.common.util.SystemConstants;
import org.g4studio.system.common.util.idgenerator.IdGenerator;
import org.springframework.dao.DataAccessException;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

import org.g4studio.system.common.util.idgenerator.IDHelper;


public class InspectionRecordServiceImpl    extends BaseAppServiceImpl implements InspectionRecordService
{
	private MediaUrlService mediaUrlService = null;//(MediaUrlService) getService("mediaUrlService");
	 
	 
	public void setMediaUrlService(MediaUrlService mediaUrlService) {
		this.mediaUrlService = mediaUrlService;
	}

//	protected  Object getService(String pBeanId) 
//	{
//			Object springBean = SpringBeanLoader.getSpringBean(pBeanId);
//			return springBean;
//	}
		
	public String getRecord(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecord.getRecordCount", pDto);
			List items = appDao.queryForPage("App.InspectRecord.getRecord", pDto);
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getRecordInfo(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecord.getRecordInfoCount", pDto);
			List items = appDao.queryForPage("App.InspectRecord.getRecordInfo", pDto);
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	public String getRecordInfoItem(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecord.getRecordInfoItemCount", pDto);
			List items = appDao.queryForPage("App.InspectRecord.getRecordInfoItem", pDto);
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	public String getRecordMediaInfo(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecord.getRecordMediaInfoCount", pDto);
			List items = appDao.queryForPage("App.InspectRecord.getRecordMediaInfo", pDto);
			
			
			String preUrl = "http://192.168.128.250:8080/media/";
			
			Dto item = new BaseDto();
			for (int i = 0; i < items.size(); i++) 
			{
				item = (BaseDto) items.get(i);
				
				 String affixUrl = item.getAsString("mediaaddress");
				 
				// String Url = preUrl + affixUrl;
				 
				 String Url =  mediaUrlService.getUrl(affixUrl);

			
				item.put("mediaaddress", Url);
				
			}
			
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
}
