package com.sysware.tldlt.app.service.devmgr;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.service.devmgr.DevMgrService;
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


public class DevMgrServiceImpl  extends BaseAppServiceImpl implements DevMgrService
{

	public Dto queryDeptinfoByDeptid(Dto pDto) 
	{
		Dto outDto = new BaseDto();
		outDto.putAll((BaseDto) appDao.queryForObject("App.DevMgr.queryDeptinfoByDeptid", pDto));
		outDto.put("success", new Boolean(true));
		return outDto;
	}
	
	public Dto queryDeptItems(Dto pDto)
	{
		Dto outDto = new BaseDto();
		List deptList = appDao.queryForList("App.DevMgr.queryDeptItemsByDto", pDto);
		Dto deptDto = new BaseDto();
		for (int i = 0; i < deptList.size(); i++)
		{
			deptDto = (BaseDto) deptList.get(i);
			if (deptDto.getAsString("leaf").equals(SystemConstants.LEAF_Y))
				deptDto.put("leaf", new Boolean(true));
			else
				deptDto.put("leaf", new Boolean(false));
			if (deptDto.getAsString("id").length() == 6)
				deptDto.put("expanded", new Boolean(true));
		}
		
		outDto.put("jsonString", JsonHelper.encodeObject2Json(deptList));
		
		return outDto;
	}
	
	
	public String getDeviceInfo(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.DevMgr.getDeviceInfoCount", pDto);
			List   items = appDao.queryForPage("App.DevMgr.getDeviceInfo", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getDeviceModel(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.DevMgr.getDeviceModelCount", pDto);
			List    items = appDao.queryForList("App.DevMgr.getDeviceModel", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getParentDevice(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.DevMgr.getParentDeviceCount", pDto);
			List    items = appDao.queryForList("App.DevMgr.getParentDevice", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getRegion(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.DevMgr.getRegionCount", pDto);
			List    items = appDao.queryForList("App.DevMgr.getRegion", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	
    public Dto addInfo(Dto inDto) 
    {
       
    	{
    		String v = inDto.getAsString("deptid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("部门ID不能为空");
	        }
	    }
              
    	
    	
    	{
    		String v = inDto.getAsString("regionid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("区域ID不能为空");
	        }
	    }
    	
    	
    	
    	{
    		String v = inDto.getAsString("devmodelid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("设备型号ID不能为空");
	        }
	    }
    	
    	{
    		String v = inDto.getAsString("checkcycleid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("检查周期ID不能为空");
	        }
	    }
    	
    	
        try
        {

        	appDao.insert("App.DevMgr.AddDevice", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        return DtoUtils.getSuccessDto("设备型号数据新增成功");
    }
    
    
    
    public Dto updateInfo(Dto inDto)
    {

    	
    	{
    		String v = inDto.getAsString("id");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("设备ID不能为空");
	        }
	    }
    	
    	{
    		String v = inDto.getAsString("deptid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("部门ID不能为空");
	        }
	    }
              
    	
    	
    	{
    		String v = inDto.getAsString("regionid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("区域ID不能为空");
	        }
	    }
    	
    	
    	
    	{
    		String v = inDto.getAsString("devmodelid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("设备型号ID不能为空");
	        }
	    }
    	
    	{
    		String v = inDto.getAsString("checkcycleid");
    	
        
	        if(v != null)
	        {
	        	v = v.trim();
	        }
	        
	        if (AppTools.isEmptyString(v)) 
	        {
	            return DtoUtils.getErrorDto("检查周期ID不能为空");
	        }
	    }
        
        try
        {
	        int ret = appDao.update("App.DevMgr.updateDevice", inDto);
	        if(ret <= 0)
	        {
	        	return DtoUtils.getErrorDto("设备型号数据更新失败");
	        }
	        else
	        {
	        	return DtoUtils.getSuccessDto("设备型号数据更新成功");
	        }
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
    }
    
    
    
    public Dto deleteInfo(Dto inDto)
    {
        BaseRetDto outDto = new BaseRetDto();
        List<String> list = ((List<String>) inDto.getAsList("ids"));
        StringBuilder strB = new StringBuilder();
        strB.append("删除失败，编号：");
        outDto.setRetSuccess();
        
        
        try
        {
        	BaseDto idDto = new BaseDto();
        	        	
	        for(String id: list) 
	        {
	        	idDto.put("id",id);
	        	
	            if (appDao.delete("App.DevMgr.DeleteDevice", idDto) <= 0) 
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
        
        catch(DataAccessException e)
        {
        	
        	 String str = e.getRootCause().toString();
        	 
        	 if(str != null && str.indexOf("com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException") >= 0)
        	 { 
	        	 outDto.setRetCode(AppCommon.RET_CODE_DELETE_ERROR);
	        	 outDto.setDesc("该设备已经被占用!");
        	 }
        }
        
        catch(Exception e)
        {
	       	 outDto.setRetCode(AppCommon.RET_CODE_UNKNOWN);
	       	 outDto.setDesc("系统内部错误,请联系管理员");
        }
        
        return outDto;
    }
	
	
	
}
