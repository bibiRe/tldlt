package com.sysware.tldlt.app.web.inspectionrecordmakeup;

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
import com.sysware.tldlt.app.service.inspectionrecordmakeup.InspectionRecordMakeupService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.web.common.BaseAppAction;
import org.g4studio.system.common.util.idgenerator.IDHelper;



public class InspectionRecordMakeupAction   extends BaseAppAction
{

	private InspectionRecordMakeupService accessService = (InspectionRecordMakeupService) super.getService("InspectionRecordMakeupService");



    public ActionForward init(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
    	
    	
        String viewString = "InspectionRecordMakeupView";
		
		String deptid = super.getSessionContainer(request).getUserInfo().getDeptid();
		String deptname = super.getSessionContainer(request).getUserInfo().getDeptname();
		
		request.setAttribute("deptid", deptid);
		request.setAttribute("deptname", deptname);
        
        return mapping.findForward(viewString);
    }
    
    
    public ActionForward queryrecordmakeup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getRecordMakeup(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward addrecordmakeup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String newID = IDHelper.NewInspectRecordMakeupID();
    	
    	dto.put("id", newID);
        
        Dto outDto = accessService.addRecordMakeup(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward queryinspecplan(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getInspecPlan(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
       
    public ActionForward deleterecordmakeup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
                    throws Exception
    {
        String delkeys = request.getParameter("delkeys");
        Dto inDto = new BaseDto();
        String[] arrChecked = delkeys.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = accessService.deleteRecordMakeup(inDto);
        setRetDtoTipMsg(response, outDto);
        return mapping.findForward(null);
    }
    
    
    
    public ActionForward updaterecordmakeup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
    	
        Dto outDto = accessService.updateRecordMakeup(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    public ActionForward approvalrecordmakeup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
    	
        Dto outDto = accessService.approvalRecordMakeup(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
}
