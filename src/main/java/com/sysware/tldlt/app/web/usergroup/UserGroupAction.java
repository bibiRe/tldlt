package com.sysware.tldlt.app.web.usergroup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.common.util.SessionListener;
import org.g4studio.common.util.WebUtils;
import org.g4studio.common.web.BaseAction;
import org.g4studio.common.web.BaseActionForm;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.util.CodeUtil;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.admin.service.MonitorService;
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;
import org.g4studio.system.common.util.idgenerator.IDHelper;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.usergroup.UserGroupService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.web.common.BaseAppAction;
import org.g4studio.system.common.util.idgenerator.IDHelper;


/**
 * Type：UserGroupAction
 * Descript：用户组action.
 * Create：yc
 * Create Time：2015年8月17日 上午10:50:00
 * Version：@version
 */

public class UserGroupAction extends BaseAppAction
{
    private UserGroupService userGrpService = (UserGroupService) super.getService("UserGroupService");


    /**
     * 初始化.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    
    public ActionForward init(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        String viewString = "UserGroupView";
        
		request.setAttribute("root_groupid", "-1");
		request.setAttribute("root_groupname","用户组");
        
        return mapping.findForward(viewString);
    }
    
    
    /**
     * 建立组树信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    
    public ActionForward groupTreeInit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = userGrpService.getGroup(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 查询某个组的用户信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward querygroupuser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
//        String grpid = dto.getAsString("groupid");
//        
//        if(grpid != null && grpid.equals("-1"))
//        {
//        	dto.remove("groupid");
//        }
        
        String jsonString = userGrpService.getUserByGroup(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    /**
     * 增加组信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward addgroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
    	String newgroupID = IDHelper.NewUserGroupID();
    	
    	dto.put("id", newgroupID);
        
        Dto outDto = userGrpService.addGroup(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 更新组信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward updategroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	
    	Dto dto = ((BaseActionForm) form).getParamAsDto(request);
   	 
        Dto outDto = userGrpService.updateGroup(dto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
 

    /**
     * 删除组信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward deletegroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
                    throws Exception
    {
        String delkeys = request.getParameter("delkeys");
        Dto inDto = new BaseDto();
        String[] arrChecked = delkeys.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = userGrpService.deleteGroup(inDto);
        setRetDtoTipMsg(response, outDto);
        return mapping.findForward(null);
    }
    
    
    
    /**
     * 按组删除用户信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward deleteuserbygroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
                    throws Exception
    {
        String delkeys1 = request.getParameter("delkeys1");
        String delkeys2 = request.getParameter("delkeys2");
        
        String[] arrdelKey1 = delkeys1.split(",");
        String[] arrdelKey2 = delkeys2.split(",");
        
        Dto outDto = userGrpService.deleteUserByGroup(arrdelKey1,arrdelKey2);
        setRetDtoTipMsg(response, outDto);
        return mapping.findForward(null);
    }
    
    
    /**
     * 加入一个用户到组信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward adduserforgroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        Dto outDto = userGrpService.addUserForGroup(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 查询组信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward querygroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = userGrpService.getGroup2(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    /**
     * 查询用户信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward queryuser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = userGrpService.getUser(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 查询用户信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward queryuser2(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = userGrpService.getUser2(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 加入用户到组.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward adduserforgroup2(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
    	
    	//String userid = request.getParameter("userid");
    	
    	//String[] userids = userid.split(",");
    	  
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        Dto outDto = userGrpService.addUserForGroup2(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
}
