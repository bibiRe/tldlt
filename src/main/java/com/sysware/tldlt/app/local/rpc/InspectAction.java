package com.sysware.tldlt.app.local.rpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.inspect.InspectService;
import com.sysware.tldlt.app.service.media.MediaUrlService;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：InspectAction
 * Descript：设备巡检Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:27:32
 * Version：@version
 */
public class InspectAction extends BaseAppAction {
    /**
     * 巡检服务对象.
     */
    private InspectService inspectService;

    public InspectAction() {
        inspectService = (InspectService) this.getService("inspectService");
    }

    /**
     * 保存巡检记录.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward saveInspectRecordInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String key = dto.getAsString("key");
        if (AppTools.isEmptyString(key)) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值为空");
        }
        Dto userDto = RPCUserManage.findUserDtoByKey(request
                .getParameter("key"));
        if (null == userDto) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值无效");
        }
        dto.put("userid", userDto.get("userId"));
        BaseRetDto outDto = (BaseRetDto) inspectService.addInfo(dto);
        return RPCUtils.sendBasicRetDtoRPCInfoActionForward(response, outDto);
    }

    /**
     * 上传巡检记录媒体信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward uploadInspectRecordMedia(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        if (!RPCUtils.checkKeyAndSendErrorRPCInfoActionForward(response, dto)) {
            return null;
        }
        if (!RPCUtils.checkUploadMediasAndSendErrorRPCInfoActionForward(form,
                response, dto)) {
            return null;
        }

        BaseRetDto outDto = (BaseRetDto) inspectService
                .saveUploadInspectRecordMedia(dto);
        if (!outDto.isRetSuccess()) {
            return RPCUtils.sendBasicRetDtoRPCInfoActionForward(response, outDto);
        }

        return RPCUtils.sendRPCListDtoActionForward(response,
                DtoUtils.createUploadInspectRecordMediaSuccessRetList(dto));
    }

}
