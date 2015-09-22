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
				pDto.remove("deptid");
			}
			
			Integer count = (Integer)appDao.queryForObject("App.InspectPlan.getPlanCount", pDto);
			List items = appDao.queryForPage("App.InspectPlan.getPlan", pDto);
			
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
        	//appDao.delete("App.InspectPlan.deleteDeviceByInspecPlan", delItem);
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
           // return DtoUtils.getErrorDto("设备ID组不能为空");
        	return DtoUtils.getSuccessDto("巡检计划新增成功");
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
	
	
	public Dto updateDeviceForInspecPLan_internal(Dto inDto)
    {
        String id = inDto.getAsString("planid");
        
        if(id != null)
        {
        	id = id.trim();
        }
        
        if (AppTools.isEmptyString(id)) 
        {
        	  return DtoUtils.getErrorDto("巡检计划ID不能为空");
        }
        
        
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
          
        	return DtoUtils.getSuccessDto("巡检计划更新成功");
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
        	return DtoUtils.getSuccessDto("巡检计划更新成功");
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
	          
	        	
	        	 Dto filerDto = new BaseDto();
	        	 
	        	 filerDto.put("planid", planid);
	        	 
	        	 plans = appDao.queryForList("App.InspectPlan.getDeviceByInspecDevice", filerDto);
	        	 
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
						
						if(devid.equals(devid2))
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
	
	
    public Dto deletePlan(Dto inDto)
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
	        	item.put("planid", id);
	        	
	        	appDao.delete("App.InspectPlan.deleteDeviceByInspecPlan", item);
	        	
	            if (appDao.delete("App.InspectPlan.deleteInspecPlan", item) <= 0) 
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
    
    
    
    public Dto updatePlan(Dto inDto)
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
			String v = inDto.getAsString("planid");
		
	    
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

        	appDao.update("App.InspectPlan.UpdatePlan", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        
        
        ////////////////delete device

        
        

        return updateDeviceForInspecPLan_internal(inDto);
    	
    }
    
    
    public Dto approvalPlan(Dto inDto)
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
			String v = inDto.getAsString("planid");
		
	    
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
    		String v = inDto.getAsString("approstate");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("巡检计划状态不能为空");
	        }
	        
	        inDto.put("state", v);
	    }
    		
    	
//    	{
//    		String v = inDto.getAsString("approvetime");
//    	
//        
//	        if(v != null)
//	        {
//	        	v = v.trim();
//	        }
//	        
//	        if (AppTools.isEmptyString(v)) 
//	        {
//	            return DtoUtils.getErrorDto("审批时间不能为空");
//	        }
//	    }
    	
    	
 
    	
        try
        {

        	appDao.update("App.InspectPlan.ApprovalPlan", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        
        
        ////////////////delete device

        
        

        return DtoUtils.getSuccessDto("巡检计划审批成功");
    	
    }
    
}
