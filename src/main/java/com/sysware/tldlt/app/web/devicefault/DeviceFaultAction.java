package com.sysware.tldlt.app.web.devicefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.common.dao.vo.UserInfoVo;

import com.sysware.tldlt.app.service.devicefault.DeviceFaultService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：DeviceFaultAction
 * Descript：设备故障action.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月19日 下午4:30:29
 * Version：@version
 */
public class DeviceFaultAction extends BaseAppAction {

    public DeviceFaultAction() {
        this.service = (DeviceFaultService) this
                .getService("deviceFaultService");
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
        setRequestUserInfo(request);
        String viewString = "deviceFaultView";
        return mapping.findForward(viewString);
    }

    /**
     * 故障查看.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"rawtypes"})
    public ActionForward queryDeviceFaultForManage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = createRequestCurrentUserDepartmentDto(form, request);
        List outDtos = appReader.queryForPage(
                "App.DeviceFault.queryDeviceFaultsForManage", dto);
        String jsonString = encodeList2PageJson(
                outDtos,
                (Integer) appReader
                        .queryForObject(
                                "App.DeviceFault.queryDeviceFaultsForManageForPageCount",
                                dto), null);
        return DtoUtils.sendStrActionForward(response, jsonString);
    }

    /**
     * 故障更新状态.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward saveUpdateStateInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        if (null == userInfo) {
            return DtoUtils.sendErrorRetDtoActionForward(response,
                    AppCommon.RET_CODE_NULL_VALUE, "没有当前用户");
        }
        dto.put("handler", userInfo.getUserid());
        dto.put("handletime", AppTools.currentUnixTime());
        Dto outDto = ((DeviceFaultService) service).updateStateInfo(dto);
        return DtoUtils.sendRetDtoActionForward(response, outDto);
    }
}
