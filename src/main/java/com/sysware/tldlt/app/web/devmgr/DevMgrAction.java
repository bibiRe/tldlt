package com.sysware.tldlt.app.web.devmgr;



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
import com.sysware.tldlt.app.service.devmgr.DevMgrService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.web.common.BaseAppAction;
import org.g4studio.system.common.util.idgenerator.IDHelper;




public class DevMgrAction extends BaseAppAction
{
    private DevMgrService accessService = (DevMgrService) super.getService("DevMgrService");



    public ActionForward init(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
    	
    	
        String viewString = "DevMgrView";
		
		Dto inDto = new BaseDto();
		String deptid = super.getSessionContainer(request).getUserInfo().getDeptid();
		inDto.put("deptid", deptid);
		Dto outDto = accessService.queryDeptinfoByDeptid(inDto);
		request.setAttribute("rootDeptid", outDto.getAsString("deptid"));
		request.setAttribute("rootDeptname", outDto.getAsString("deptname"));
        
        return mapping.findForward(viewString);
    }
    
    

    
    public ActionForward departmentTreeInit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
		Dto dto = new BaseDto();
		String nodeid = request.getParameter("node");
		dto.put("parentid", nodeid);
		Dto outDto = accessService.queryDeptItems(dto);
		write(outDto.getAsString("jsonString"), response);
		return mapping.findForward(null);
    }
    
   
	public ActionForward queryDevForDept(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		BaseActionForm aForm = (BaseActionForm)form;
		Dto dto = aForm.getParamAsDto(request);
		String deptid = request.getParameter("deptid");
		
		if("current_user".equals(deptid))
		{
			dto.remove("deptid");
			dto.put("deptid", super.getSessionContainer(request).getUserInfo().getDeptid());
		}	
		
	
		String jsonString = accessService.getDeviceInfo(dto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	
    public ActionForward queryregion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getRegion(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward querydevmodel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getDeviceModel(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward queryparentdev(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getParentDevice(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward saveDeviceItem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
    	Dto dto = ((BaseActionForm) form).getParamAsDto(request);
    	
    	
    	String newDeviceID = IDHelper.NewDeviceID();
    	
    	dto.put("id", newDeviceID);
    	
    	
    	String parentdevid = dto.getAsString("parentdevid");
    	
    	if(parentdevid == null || "".equals(parentdevid))
    	{
    		//parentdevid = "";
    		//dto.put("parentdevid", parentdevid);
    		
    		dto.remove("parentdevid");
    	}
    	
    	
    	String longtitude = dto.getAsString("longtitude");
    	
    	if(longtitude == null || "".equals(longtitude))
    	{
    		//longtitude = "";
    		//dto.put("longtitude", longtitude);
    		
    		dto.remove("longtitude");
    	}
    	
    	
    	String latitude = dto.getAsString("latitude");
    	
    	if(latitude == null || "".equals(latitude))
    	{
    		//latitude = "";
    		//dto.put("latitude", latitude);
    		
    		dto.remove("latitude");
    	}
    	
    	
    	String height = dto.getAsString("height");
    	
    	if(height == null || "".equals(height))
    	{
    		//height = "";
    		//dto.put("height", height);
    		
    		dto.remove("height");
    	}
    	

    	
    	
    	 
        Dto outDto = accessService.addInfo(dto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
	
    
    public ActionForward updateDevItem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
    	Dto dto = ((BaseActionForm) form).getParamAsDto(request);
    	
    	
    	String devid = dto.getAsString("devid");
    	
    	if(devid == null)
    	{
    		devid = "";
    	}
    	
    	dto.remove("devid");
    	dto.put("id", devid);
    	
    	String parentdevid = dto.getAsString("parentdevid");
    	
    	if(parentdevid == null || "".equals(parentdevid))
    	{
    		
    		dto.remove("parentdevid");
    	}
    	
    	
    	String longtitude = dto.getAsString("longtitude");
    	
    	if(longtitude == null || "".equals(longtitude))
    	{
    		
    		dto.remove("longtitude");
    	}
    	
    	
    	String latitude = dto.getAsString("latitude");
    	
    	if(latitude == null || "".equals(latitude))
    	{
    		
    		dto.remove("latitude");
    	}
    	
    	
    	String height = dto.getAsString("height");
    	
    	if(height == null || "".equals(height))
    	{
    		
    		dto.remove("height");
    	}
    	 
        Dto outDto = accessService.updateInfo(dto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward deldeviceitem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
                    throws Exception
    {
        String delkeys = request.getParameter("delkeys");
        Dto inDto = new BaseDto();
        String[] arrChecked = delkeys.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = accessService.deleteInfo(inDto);
        setRetDtoTipMsg(response, outDto);
        return mapping.findForward(null);
    }
    
}




