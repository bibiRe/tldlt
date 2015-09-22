package com.sysware.tldlt.app.web.inspectionrecordmakeupfill;

import com.sysware.tldlt.app.web.common.BaseAppAction;
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
import com.sysware.tldlt.app.local.rpc.RPCUtils;
import com.sysware.tldlt.app.service.inspect.InspectService;
import com.sysware.tldlt.app.service.inspectionrecordmakeupfill.InspectionRecordMakeupFillService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.common.BaseAppAction;
import org.g4studio.system.common.util.idgenerator.IDHelper;


public class InspectionRecordMakeupFillAction   extends BaseAppAction
{
	private InspectionRecordMakeupFillService accessService = (InspectionRecordMakeupFillService) super.getService("InspectionRecordMakeupFillService");

	 private InspectService inspectService = (InspectService) super.getService("inspectService");


    public ActionForward init(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
    	
    	
        String viewString = "InspectionRecordMakeupFillView";
		
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
    
    
    public ActionForward queryinspecplandevice(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getInspecPlanDevice(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward addrecordinfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        Dto outDto = accessService.addInspecPlanRecordInfo(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }

    
    public ActionForward queryinspecrecordinfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getInspectRecordInfo(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward querydevcheckcontentinfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getDeviceCheckContentInfo(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    public ActionForward addrecorditem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        Dto outDto = accessService.addInspecPlanRecordItem(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
       
    public ActionForward queryinspecrecorditem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = accessService.getInspectRecordItem(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    public ActionForward addrecordinfomedia(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        
    	 Dto dto = getRequestDto(form, request);
    	 
//         if (!RPCUtils.checkKeyAndSendErrorRPCInfoActionForward(response, dto)) {
//             return null;
//         }
         
    	 //inspectrecordinfoid
    	 
    	  dto.put("userid", dto.getAsString("loginuserid"));
    	  
    	  String recordinfoid =  accessService.getInspectRecordInfo2(dto);
    	  dto.put("inspectrecordinfoid", recordinfoid);
    	  
    	  long datetime = AppTools.currentUnixTime();
    	  dto.put("datetime", datetime);
    	  
         if (!RPCUtils.checkUploadMediasAndSendErrorRPCInfoActionForward(form,
                 response, dto)) {
             return null;
         }

         BaseRetDto outDto = (BaseRetDto) inspectService.saveUploadInspectRecordMedia(dto);
         
         if (!outDto.isRetSuccess())
         {
             return RPCUtils.sendBasicRetDtoRPCInfoActionForward(response, outDto);
         }

         return RPCUtils.sendRPCListDtoActionForward(response,
                 DtoUtils.createUploadInspectRecordMediaSuccessRetList(dto));
        

    }
    
    
    
    public ActionForward addrecorditemmedia(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        
    	 Dto dto = getRequestDto(form, request);
    	 
    	 
    	  dto.put("userid", dto.getAsString("loginuserid"));
    	  
    	  String inspectrecorditemid =  accessService.getInspectRecordItem2(dto);
    	  dto.put("inspectrecorditemid", inspectrecorditemid);
    	  
    	  long datetime = AppTools.currentUnixTime();
    	  dto.put("datetime", datetime);
    	  
         if (!RPCUtils.checkUploadMediasAndSendErrorRPCInfoActionForward(form,
                 response, dto)) {
             return null;
         }

         BaseRetDto outDto = (BaseRetDto) inspectService.saveUploadInspectRecordItemMedia(dto);
         
         if (!outDto.isRetSuccess())
         {
             return RPCUtils.sendBasicRetDtoRPCInfoActionForward(response, outDto);
         }

         return RPCUtils.sendRPCListDtoActionForward(response,
                 DtoUtils.createUploadInspectRecordMediaSuccessRetList(dto));
        

    }
    
    
    
    public ActionForward finishinspecrecord(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
               
        Dto outDto = accessService.finishInspecRecord(dto);
        
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
}
