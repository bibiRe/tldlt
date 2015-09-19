package com.sysware.tldlt.app.web.inspectionrecord;

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
import com.sysware.tldlt.app.service.inspectionrecord.InspectionRecordService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.web.common.BaseAppAction;
import org.g4studio.system.common.util.idgenerator.IDHelper;


public class InspectionRecordAction  extends BaseAppAction
{

	 private InspectionRecordService accessService = (InspectionRecordService) super.getService("inspectRecordService");
	 
	 
	    public ActionForward init(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception 
	    {
	    	
	    	
	        String viewString = "InspectionRecordView";
			
			String deptid = super.getSessionContainer(request).getUserInfo().getDeptid();
			String deptname = super.getSessionContainer(request).getUserInfo().getDeptname();
			
			request.setAttribute("deptid", deptid);
			request.setAttribute("deptname", deptname);
	        
	        return mapping.findForward(viewString);
	    }
	    
	    public ActionForward queryrecord(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception 
	    {
	        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
	        
	        String jsonString = accessService.getRecord(dto);
	        
	        write(jsonString, response);
	        
	        return mapping.findForward(null);
	    }
	    
	    
	    public ActionForward queryrecordinfo(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception 
	    {
	        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
	        
	        String jsonString = accessService.getRecordInfo(dto);
	        
	        write(jsonString, response);
	        
	        return mapping.findForward(null);
	    }
	    
	    public ActionForward queryrecordinfoitem(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception 
	    {
	        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
	        
	        String jsonString = accessService.getRecordInfoItem(dto);
	        
	        write(jsonString, response);
	        
	        return mapping.findForward(null);
	    }
	    
	    
	    public ActionForward queryrecordmediainfo(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception 
	    {
	        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
	        
	        String jsonString = accessService.getRecordMediaInfo(dto);
	        
	        write(jsonString, response);
	        
	        return mapping.findForward(null);
	    }
}
