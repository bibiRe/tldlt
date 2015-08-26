package com.sysware.tldlt.app.service.usergroup;

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
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;


/**
 * Type：UserGroupServiceImpl
 * Descript：用户组服务实现类.
 * Create：yc
 * Create Time：2015年7月31日 上午11:03:01
 * Version：@version
 */

public class UserGroupServiceImpl extends BaseAppServiceImpl implements UserGroupService
{
	public String getGroup(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			List   items = appDao.queryForPage("App.UserGroup.getGroup", pDto);
			
			Dto item = new BaseDto();
			for (int i = 0; i < items.size(); i++) 
			{
				item = (BaseDto) items.get(i);
			
				item.put("leaf", new Boolean(true));
				item.put("expanded", new Boolean(true));
			}
			
			
			str = JsonHelper.encodeObject2Json(items);
			

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getUserByGroup(Dto pDto)
	{
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.UserGroup.getUserByGroupCount", pDto);
			List   usergroups = appDao.queryForList("App.UserGroup.getUserByGroup", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(usergroups,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public Dto addGroup(Dto inDto)
	{
        String name = inDto.getAsString("groupname");
        
        if(name != null)
        {
        	name = name.trim();
        }
        
        if (AppTools.isEmptyString(name)) 
        {
            return DtoUtils.getErrorDto("用户组名不能为空");
        }
      
        
        try
        {

        	appDao.insert("App.UserGroup.AddUserGroup", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        return DtoUtils.getSuccessDto("用户组数据新增成功");
	}
	
	
    public Dto updateGroup(Dto inDto)
    {

        String id = inDto.getAsString("id");
        
        if(id != null)
        {
        	id = id.trim();
        }
        
        if (AppTools.isEmptyString(id)) 
        {
            return DtoUtils.getErrorDto("用户组ID不能为空");
        }
        
        String name = inDto.getAsString("groupname");
        
        if(name != null)
        {
        	name = name.trim();
        }
        
        if (AppTools.isEmptyString(name)) 
        {
            return DtoUtils.getErrorDto("用户组名不能为空");
        }
        
        try
        {
	        int ret = appDao.update("App.UserGroup.UpdateUserGroup", inDto);
	        if(ret <= 0)
	        {
	        	return DtoUtils.getErrorDto("用户组数据更新失败");
	        }
	        else
	        {
	        	return DtoUtils.getSuccessDto("用户组数据更新成功");
	        }
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
    }
    
    
    public Dto deleteGroup(Dto inDto)
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
	        	
	            if (appDao.delete("App.UserGroup.DeleteUserGroup", idDto) <= 0) 
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
	        	 outDto.setDesc("该用户组已经被占用!");
        	 }
        }
        
        catch(Exception e)
        {
	       	 outDto.setRetCode(AppCommon.RET_CODE_UNKNOWN);
	       	 outDto.setDesc("系统内部错误,请联系管理员");
        }
        
        return outDto;
    }
    
    public Dto deleteUserByGroup( String[] key1, String[] key2)
    {
        BaseRetDto outDto = new BaseRetDto();
        StringBuilder strB = new StringBuilder();
        strB.append("删除失败，编号：");
        outDto.setRetSuccess();
        
        if((key1.length != key2.length) || key1.length == 0)
        {
	       	 outDto.setRetCode(AppCommon.RET_CODE_UNKNOWN);
	       	 outDto.setDesc("非法操作");
	       	 return outDto;
        }
        
        
        try
        {
        	
        	BaseDto idDto = new BaseDto();
        	
        	int i = 0;
        	        	
	        for(i = 0;i < key1.length;i++)
	        {
	        	
	        	idDto.put("id",key1[i]);
	        	idDto.put("userid",key2[i]);
	        	
	            if (appDao.delete("App.UserGroup.deleteUserByGroup", idDto) <= 0) 
	            {
	                outDto.setRetCode(AppCommon.RET_CODE_DELETE_ERROR);
	                strB.append(key1[i]);
	                strB.append("- ");
	                strB.append(key2[i]);
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
    
    public Dto addUserForGroup(Dto inDto)
    {
        String id = inDto.getAsString("id");
        
        if(id != null)
        {
        	id = id.trim();
        }
        
        if (AppTools.isEmptyString(id)) 
        {
            return DtoUtils.getErrorDto("用户组ID不能为空");
        }
        
        
        String userid = inDto.getAsString("userid");
        
        if(userid != null)
        {
        	userid = userid.trim();
        }
        
        if (AppTools.isEmptyString(userid)) 
        {
            return DtoUtils.getErrorDto("用户ID不能为空");
        }
      
        
        try
        {

        	appDao.insert("App.UserGroup.AddUserForGroup", inDto);
        }
        
        catch(Exception e)
        {
        	  return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
        }
        
        return DtoUtils.getSuccessDto("新增用户到组成功");
    }
    
    
	public String getGroup2(Dto pDto)
	{
		
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.UserGroup.getGroup2Count", pDto);
			List    items = appDao.queryForPage("App.UserGroup.getGroup2", pDto);
			
			
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
			Integer count = (Integer)appDao.queryForObject("App.UserGroup.getUserCount", pDto);
			List    items = appDao.queryForPage("App.UserGroup.getUser", pDto);
			
			
			str = JsonHelper.encodeList2PageJson(items,count,null);

		}
		catch(Exception e)
		{
			str = "系统内部错误,请联系管理员";
		}
		
		
		return str;
	}
	
	
	public String getUser2(Dto pDto)
	{
		String str = "";
		
		try
		{
			Integer count = (Integer)appDao.queryForObject("App.UserGroup.getUser2Count", pDto);
			List    items = appDao.queryForPage("App.UserGroup.getUser2", pDto);
			
			
			
	        String groupid = pDto.getAsString("groupid");
	        
	        if(groupid != null)
	        {
	        	groupid = groupid.trim();
	        }
	        
	        List   usergroups = null;
	        
	        if (!AppTools.isEmptyString(groupid)) 
	        {
	          
	        	
	        	 Dto groupuserdto = new BaseDto();
	        	 
	        	 groupuserdto.put("groupid", groupid);
	        	 
	        	usergroups = appDao.queryForList("App.UserGroup.getUserByGroup", groupuserdto);
	        	 
	        }
	        	 
	        
 			Dto item = new BaseDto();
			for (int i = 0; i < items.size(); i++) 
			{
				item = (BaseDto) items.get(i);
			
				item.put("initialcheck", new Boolean(false));
				
				String userid = item.getAsString("userid");
				
				userid = userid.trim();
				
				
				if(usergroups != null)
				{
		 			Dto item2 = new BaseDto();
					for (int j = 0; j < usergroups.size(); j++) 
					{
						item2 = (BaseDto) usergroups.get(j);
						
						String userid2 = item2.getAsString("userid");
						
						userid2 = userid2.trim();
						
						if(userid.equals(userid2))
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
	
	
    public Dto addUserForGroup2(Dto inDto)
    {
        String id = inDto.getAsString("id");
        
        if(id != null)
        {
        	id = id.trim();
        }
        
        if (AppTools.isEmptyString(id)) 
        {
            return DtoUtils.getErrorDto("用户组ID不能为空");
        }
        
        
        //////////////////////////
        
        Dto delItem = new BaseDto();
        
        delItem.put("id", id);
        
        try
        {
        	appDao.delete("App.UserGroup.deleteUserByGroup", delItem);
        }
        
        catch(Exception e)
        {
        	
        }
        
        /////////////////////////////
        
        
        String userid = inDto.getAsString("userid");
        
        if(userid != null)
        {
        	userid = userid.trim();
        }
        
        if (AppTools.isEmptyString(userid)) 
        {
            return DtoUtils.getErrorDto("用户ID组不能为空");
        }
        
        
        inDto.remove("userid");
        
        String[] userids = userid.split(",");
        
        int i = 0;
        String tmpuserid = "";
        
        String msg = "";
        boolean flag = true;
        
        for(i = 0;i < userids.length;i++)
        {
        	tmpuserid = userids[i].trim();
        	
        	if (AppTools.isEmptyString(tmpuserid)) 
        	{
        		continue;
        	}
        	
        	inDto.put("userid", tmpuserid);
      
	        try
	        {
	
	        	appDao.insert("App.UserGroup.AddUserForGroup", inDto);
	        }
	        
	        catch(Exception e)
	        {
	        	 // return DtoUtils.getErrorDto("系统内部错误,请联系管理员");
	        	flag = false;
	        }
        }
        
        if(flag)
        {

        }
        
        else
        {
        
        }
        
        return DtoUtils.getSuccessDto("用户组管理操作成功");
    }
				
}
