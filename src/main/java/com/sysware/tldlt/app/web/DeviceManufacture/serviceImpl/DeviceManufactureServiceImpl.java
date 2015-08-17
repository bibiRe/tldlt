package com.sysware.tldlt.app.web.DeviceManufacture.serviceImpl;

import java.util.List;

import org.g4studio.common.service.impl.BaseServiceImpl;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;
import org.g4studio.system.common.util.SystemConstants;
import org.g4studio.system.common.util.idgenerator.IdGenerator;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.DeviceManufacture.service.DeviceManufactureService;

public class DeviceManufactureServiceImpl extends BaseAppServiceImpl implements DeviceManufactureService 
{

	public String getDeviceManufacture(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.DeviceManufacture.getDeviceManufactureCount", pDto);
			List devmanufs = appDao.queryForPage("App.DeviceManufacture.getDeviceManufacture", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(devmanufs,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
    public Dto addInfo(Dto inDto) 
    {
       
        String name = inDto.getAsString("realname");//全名
        
        if(name != null)
        {
        	name = name.trim();
        }
        
        if (AppTools.isEmptyString(name)) 
        {
            return DtoUtils.getErrorDto("设备厂商名称不能为空");
        }
      
        
        try
        {

        	appDao.insert("App.DeviceManufacture.addInfo", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        return DtoUtils.getSuccessDto("设备厂商数据新增成功");
    }
    
    public Dto updateInfo(Dto inDto)
    {

        String id = inDto.getAsString("id");
        
        if(id != null)
        {
        	id = id.trim();
        }
        
        if (AppTools.isEmptyString(id)) 
        {
            return DtoUtils.getErrorDto("设备厂商ID不能为空");
        }
        
        String name = inDto.getAsString("realname");
        
        if(name != null)
        {
        	name = name.trim();
        }
        
        if (AppTools.isEmptyString(name)) 
        {
            return DtoUtils.getErrorDto("设备厂商名称不能为空");
        }
        
        try
        {
	        int ret = appDao.update("App.DeviceManufacture.updateInfo", inDto);
	        if(ret <= 0)
	        {
	        	return DtoUtils.getErrorDto("设备厂商数据更新失败");
	        }
	        else
	        {
	        	return DtoUtils.getSuccessDto("设备厂商数据更新成功");
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
	        for(String id: list) 
	        {
	            if (appDao.delete("App.DeviceManufacture.deleteInfo", id) <= 0) 
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
}
