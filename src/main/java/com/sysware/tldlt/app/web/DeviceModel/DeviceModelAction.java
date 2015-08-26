package com.sysware.tldlt.app.web.devicemodel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.common.web.BaseActionForm;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.system.common.util.idgenerator.IDHelper;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.service.devicemodel.DeviceModelService;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.common.BaseAppAction;



/**
 * Type：DeviceModelAction
 * Descript：设备型号action.
 * Create：yc
 * Create Time：2015年8月17日 上午10:50:00
 * Version：@version
 */


public class DeviceModelAction extends BaseAppAction
{
    private DeviceModelService deviceModelService = (DeviceModelService) super.getService("deviceModelService");


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
        String viewString = "deviceModelView";
        
        return mapping.findForward(viewString);
    }
    
    
    /**
     * 查询设备型号
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    
    public ActionForward query(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = deviceModelService.getDeviceModel(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 查询设备类型.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward querydevtype(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = deviceModelService.getDeviceType(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 查询设备厂家.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward querydevmanuf(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception 
    {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        
        String jsonString = deviceModelService.getDeviceManuf(dto);
        
        write(jsonString, response);
        
        return mapping.findForward(null);
    }
    
    
    /**
     * 增加一个设备型号.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
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
    
    
    /**
     * 更新设备型号.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
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
 

    /**
     * 删除设备型号.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
                    throws Exception
    {
        String delkeys = request.getParameter("delkeys");
        Dto inDto = new BaseDto();
        String[] arrChecked = delkeys.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = deviceModelService.deleteInfo(inDto);
        return DtoUtils.sendRetDtoActionForward(response, outDto);
    }

}
