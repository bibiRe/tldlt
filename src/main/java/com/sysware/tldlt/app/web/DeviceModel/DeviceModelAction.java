package com.sysware.tldlt.app.web.DeviceModel;

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
import com.sysware.tldlt.app.service.devicetype.DeviceTypeService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.web.common.BaseAppAction;
import com.sysware.tldlt.app.web.DeviceModel.service.DeviceModelService;
import org.g4studio.system.common.util.idgenerator.IDHelper;

public class DeviceModelAction extends BaseAppAction
{
    private DeviceModelService deviceModelService = (DeviceModelService) super.getService("deviceModelService");


    public ActionForward init(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        String viewString = "deviceModelView";
        
        return mapping.findForward(viewString);
    }
    
    public ActionForward query(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = deviceModelService.getDeviceModel(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward querydevtype(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = deviceModelService.getDeviceType(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward querydevmanuf(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = deviceModelService.getDeviceManuf(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {

    	Dto dto = ((BaseActionForm) form).getParamAsDto(request);
    	
    	
    	String newDeviceModelID = IDHelper.NewDeviceModelID();
    	
    	dto.put("id", newDeviceModelID);
    	 
        Dto outDto = deviceModelService.addInfo(dto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    public ActionForward update(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	
    	Dto dto = ((BaseActionForm) form).getParamAsDto(request);
   	 
        Dto outDto = deviceModelService.updateInfo(dto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
 


    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
                    throws Exception
    {
        String delkeys = request.getParameter("delkeys");
        Dto inDto = new BaseDto();
        String[] arrChecked = delkeys.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = deviceModelService.deleteInfo(inDto);
        setRetDtoTipMsg(response, outDto);
        return mapping.findForward(null);
    }

}
