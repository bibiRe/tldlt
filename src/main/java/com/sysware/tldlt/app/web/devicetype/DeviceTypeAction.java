package com.sysware.tldlt.app.web.devicetype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.common.web.BaseActionForm;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.service.devicetype.DeviceTypeService;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：DeviceTypeAction
 * Descript：设备类型action.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月10日 上午10:50:00
 * Version：@version
 */
public class DeviceTypeAction extends BaseAppAction {
    
    public DeviceTypeAction() {
        this.service = (DeviceTypeService) super
                .getService("deviceTypeService");    
    }
    /**
     * 删除信息.
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
            throws Exception {
        String deleteIds = request.getParameter("ids");
        Dto inDto = new BaseDto();
        String[] arrChecked = deleteIds.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = ((DeviceTypeService)service).deleteInfo(inDto);
        setRetDtoTipMsg(response, outDto);
        return getNullForward(mapping);
    }

    /**
     * 初始化.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String viewString = "deviceTypeView";
        return mapping.findForward(viewString);
    }

    /**
     * 查询.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward query(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return getPageNullForward(mapping, form, request, response,
                "App.DeviceType.getDeviceTypes",
                "App.DeviceType.getDeviceTypesCount");
    }

    /**
     * 新增信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward saveAddInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        BaseActionForm aForm = (BaseActionForm) form;
        Dto inDto = aForm.getParamAsDto(request);
        Dto outDto = ((DeviceTypeService)service).addInfo(inDto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        return getNullForward(mapping);
    }

    /**
     * 更新信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward saveUpdateInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        BaseActionForm aForm = (BaseActionForm) form;
        Dto inDto = aForm.getParamAsDto(request);
        Dto outDto = ((DeviceTypeService)service).updateInfo(inDto);
        String jsonString = JsonHelper.encodeObject2Json(outDto);
        write(jsonString, response);
        return getNullForward(mapping);
    }
}
