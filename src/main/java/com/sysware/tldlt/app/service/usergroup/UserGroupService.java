package com.sysware.tldlt.app.service.usergroup;

import org.g4studio.common.service.BaseService;
import org.g4studio.core.metatype.Dto;

/**
 * Type：UserGroupService
 * Descript：用户组服务接口.
 * Create：yc
 * Create Time：2015年8月19日 下午3:31:42
 * Version：@version
 */
public interface UserGroupService 
{

	public String getGroup(Dto pDto);
	
	public String getUserByGroup(Dto pDto);
	
	public Dto addGroup(Dto inDto);
	
	public Dto updateGroup(Dto inDto);
	
	public Dto deleteGroup(Dto inDto);
	
	public Dto deleteUserByGroup( String[] key1, String[] key2);
	
	public Dto addUserForGroup(Dto inDto);
	
	public String getGroup2(Dto pDto);
	
	public String getUser(Dto pDto);
	
	public String getUser2(Dto pDto);
	
	public Dto addUserForGroup2(Dto inDto);
	
}
