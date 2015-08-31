package com.sysware.tldlt.app.service.inspectionplan;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.service.inspectionplan.InspectionPlanService;
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


public class InspectionPlanServiceImpl   extends BaseAppServiceImpl implements InspectionPlanService
{

	public String getPlan(Dto pDto)
	{
		String str = "";
		
		try
		{
			
			Integer count = (Integer)appDao.queryForObject("App.InspectPlan.getPlanCount", pDto);
			List items = appDao.queryForPage("App.InspectPlan.getPlan", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getUser(Dto pDto)
	{
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.InspectPlan.getUserCount", pDto);
			List    items = appDao.queryForPage("App.InspectPlan.getUser", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public Dto addPlan(Dto inDto)
	{
		
		{
			String v = inDto.getAsString("loginuserid");
		
	    
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划创建人不能为空");
	        }
		}
	
    	{
    		String v = inDto.getAsString("planname");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划名称不能为空");
	        }
	    }
    	
    	{
    		String v = inDto.getAsString("state");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划状态不能为空");
	        }
	    }
    	
    	
    	{
    		String v = inDto.getAsString("executeuserid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划执行人不能为空");
	        }
	    }
    	
    	
    	{
    		String v = inDto.getAsString("executestartime");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划执行开始时间不能为空");
	        }
	    }
    	
    	
    	{
    		String v = inDto.getAsString("executeendtime");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划执行结束时间不能为空");
	        }
	    }
      
        
        try
        {

        	appDao.insert("App.InspectPlan.AddPlan", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        ////////////////add device
        
        
        
        return addDeviceForInspecPLan_internal(inDto);
	}
	
	
	
	public Dto addDeviceForInspecPLan_internal(Dto inDto)
    {
        String id = inDto.getAsString("id");
        
        if(id != null)
        {
        	id = id.trim();
        }
        
        if (AppTools.isEmptyString(id)) 
        {
        	  return DtoUtils.getErrorDto("巡检计划ID不能为空");
        }
        
        
        inDto.remove("id");
        inDto.put("planid", id);
        
        //////////////////////////
        
        Dto delItem = new BaseDto();
        
        delItem.put("planid", id);
        
        try
        {
        	appDao.delete("App.InspectPlan.deleteDeviceByInspecPlan", delItem);
        }
        
        catch(Exception e)
        {
        	
        }
        
        /////////////////////////////
        
        
        String devid = inDto.getAsString("devid");
        
        if(devid != null)
        {
        	devid = devid.trim();
        }
        
        if (AppTools.isEmptyString(devid)) 
        {
            return DtoUtils.getErrorDto("设备ID组不能为空");
        }
        
        
        inDto.remove("devid");
        
        String[] devids = devid.split(",");
        
        int i = 0;
        String tmpdevid = "";
        
        String msg = "";
        boolean flag = true;
        
        for(i = 0;i < devids.length;i++)
        {
        	tmpdevid = devids[i].trim();
        	
        	if (AppTools.isEmptyString(tmpdevid)) 
        	{
        		continue;
        	}
        	
        	inDto.put("devid", tmpdevid);
        	
            String newID = IDHelper.NewInspectPlanDeviceID();
        	
            inDto.put("id", newID);
      
	        try
	        {
	
	        	appDao.insert("App.InspectPlan.AddDeviceForInspectPlan", inDto);
	        }
	        
	        catch(Exception e)
	        {
	        	  //return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
	        	
	        	flag = true;
	        	
	        }
        }
        
        
        if(flag)
        {
        	return DtoUtils.getSuccessDto("巡检计划新增成功");
        }
        
        else
        {
        	return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        

    }
	
	
	public String getDevice(Dto pDto)
	{
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.InspectPlan.getDeviceCount", pDto);
			List    items = appDao.queryForPage("App.InspectPlan.getDevice", pDto);
			
			
			
	        String planid = pDto.getAsString("planid");
	        
	        if(planid != null)
	        {
	        	planid = planid.trim();
	        }
	        
	        List   plans = null;
	        
	        if (!AppTools.isEmptyString(planid)) 
	        {
	          
	        	
	        	 Dto groupuserdto = new BaseDto();
	        	 
	        	 groupuserdto.put("planid", planid);
	        	 
	        	 plans = appDao.queryForList("App.InspectPlan.getDeviceByInspecDevice", groupuserdto);
	        	 
	        }
	        	 
	        
 			Dto item = new BaseDto();
			for (int i = 0; i < items.size(); i++) 
			{
				item = (BaseDto) items.get(i);
			
				item.put("initialcheck", new Boolean(false));
				
				String devid = item.getAsString("devid");
				
				devid = devid.trim();
				
				
				if(plans != null)
				{
		 			Dto item2 = new BaseDto();
					for (int j = 0; j < plans.size(); j++) 
					{
						item2 = (BaseDto) plans.get(j);
						
						String devid2 = item2.getAsString("devid");
						
						devid2 = devid2.trim();
						
						if(planid.equals(devid2))
						{
							item.put("initialcheck", new Boolean(true));
							break;
						}
					}
				}
				
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
