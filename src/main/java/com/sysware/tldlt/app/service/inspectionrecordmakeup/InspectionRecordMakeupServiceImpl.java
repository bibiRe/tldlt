package com.sysware.tldlt.app.service.inspectionrecordmakeup;

import org.g4studio.core.metatype.Dto;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import java.util.List;

import org.g4studio.common.service.impl.BaseServiceImpl;
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


public class InspectionRecordMakeupServiceImpl     extends BaseAppServiceImpl implements InspectionRecordMakeupService
{

	public String getRecordMakeup(Dto pDto)
	{
		String str = "";
		
		try
		{
			
	
			Dto approitem = new BaseDto();
			
			approitem.put("userid", pDto.getAsString("loginuserid"));
			
			
			List  approvalist = appDao.queryForList("App.InspectPlan.getInspectPlanApprovalAuth", approitem);
			
			boolean canapproval = false;
			
			if(approvalist != null && approvalist.size() > 0)
			{
				canapproval = true;
			}
			
			if(canapproval)
			{
				pDto.put("canmgr", "OK");
			}
			else
			{
				
				pDto.put("uselogin", "OK");
			}
				
			
			Integer count = (Integer)appDao.queryForObject("App.InspectRecordMakeup.getRecordMakeupCount", pDto);
			List items = appDao.queryForPage("App.InspectRecordMakeup.getRecordMakeup", pDto);
			
			
			Dto item = new BaseDto();
			for (int i = 0; i < items.size(); i++) 
			{
				item = (BaseDto) items.get(i);
			
				item.put("canapproval", canapproval);
				

			}
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
	public Dto addRecordMakeup(Dto inDto)
	{
		
		{
			String v = inDto.getAsString("loginuserid");
		
	    
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("补录申请人不能为空");
	        }
		}
	
    	{
    		String v = inDto.getAsString("inspecplanid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划ID不能为空");
	        }
	    }
    	
    	{
    		String v = inDto.getAsString("recordmakeupstate");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("补录状态不能为空");
	        }
	    }
    	
    	
        
        try
        {

        	appDao.insert("App.InspectRecordMakeup.AddRecordMakeup", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        ////////////////add device
        
        
        
        return DtoUtils.getSuccessDto("新增补录申请成功");
	}
	
	
	
	public String getInspecPlan(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			List    items = appDao.queryForList("App.InspectRecordMakeup.getInspecPlan", pDto);
			int count = items.size();
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public Dto updateRecordMakeup(Dto inDto)
    {

		{
			String v = inDto.getAsString("loginuserid");
		
	    
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("补录申请人不能为空");
	        }
		}
		
		
		{
			String v = inDto.getAsString("recordmakeupid");
		
	    
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("补录ID不能为空");
	        }
		}
	
    	{
    		String v = inDto.getAsString("inspecplanid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划ID不能为空");
	        }
	    }
    	
    	{
    		String v = inDto.getAsString("recordmakeupstate");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("补录状态不能为空");
	        }
	    }
    	
    	
        
        try
        {

        	appDao.insert("App.InspectRecordMakeup.UpdateRecordMakeup", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        ////////////////add device
        
        
        
        return DtoUtils.getSuccessDto("修改补录信息成功");
    	
    }
    
	 public Dto deleteRecordMakeup(Dto inDto)
	    {
	        BaseRetDto outDto = new BaseRetDto();
	        List<String> list = ((List<String>) inDto.getAsList("ids"));
	        StringBuilder strB = new StringBuilder();
	        strB.append("删除失败，编号：");
	        outDto.setRetSuccess();
	        
	        
	        try
	        {
	        	Dto item = new BaseDto();
	        	
		        for(String id: list) 
		        {
		        	item.put("recordmakeupid", id);
		        	
		        	
		            if (appDao.delete("App.InspectRecordMakeup.deleteRecordMakeup", item) <= 0) 
		            {
		                outDto.setRetCode(AppCommon.RET_CODE_DELETE_ERROR);
		                strB.append(id);
		                strB.append(", ");
		            }                
		        }
		        
		        if (!outDto.isRetSuccess()) 
		        {
		            outDto.setDesc(strB.toString());
		        }        
	        }
	        
	        catch(Exception e)
	        {
	        	 outDto.setRetCode(AppCommon.RET_CODE_UNKNOWN);
	        	 outDto.setDesc("系统内部错误,请联系管理员");
	        }
	        
	        return outDto;
	    }
	 
	 
	 
	    public Dto approvalRecordMakeup(Dto inDto)
	    {

	    	

	    	{
				String v = inDto.getAsString("loginuserid");
			
		    
		        if(v != null)
		        {
		        	v = v.trim();
		        }
		        
		        if (AppTools.isEmptyString(v)) 
		        {
		            return DtoUtils.getErrorDto("审批用户id不能为空");
		        }
			}
	    	
	    	
	    	{
				String v = inDto.getAsString("recordmakeupid");
			
		    
		        if(v != null)
		        {
		        	v = v.trim();
		        }
		        
		        if (AppTools.isEmptyString(v)) 
		        {
		            return DtoUtils.getErrorDto("补录申请ID不能为空");
		        }
		        
		       
			}
		
	    	
	    	
	    	{
	    		String v = inDto.getAsString("recordmakeupstate2");
	    	
	        
		        if(v != null)
		        {
		        	v = v.trim();
		        }
		        
		        if (AppTools.isEmptyString(v)) 
		        {
		            return DtoUtils.getErrorDto("补录申请状态不能为空");
		        }
		       
		        inDto.put("recordmakeupstate", v);
		    }

	        try
	        {

	        	appDao.update("App.InspectRecordMakeup.ApprovalRecordMakeup", inDto);
	        }
	        
	        catch(Exception e)
	        {
	        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
	        }
	        
	        
	        return DtoUtils.getSuccessDto("补录申请审批成功");
	    	
	    }
	
}
