package com.sysware.tldlt.app.web.devicetypecheckitem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.service.devicetypecheckitem.DeviceTypeCheckItemService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * DeviceTypeCheckItemAction
 * Descript：设备检查项action.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月10日 上午10:50:00
 * Version：@version
 */
public class DeviceTypeCheckItemAction extends BaseAppAction {
    public DeviceTypeCheckItemAction() {
        this.service = (DeviceTypeCheckItemService) super
                .getService("deviceTypeCheckItemService");
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
        if (AppTools.isEmptyString(deleteIds)) {
            return DtoUtils.sendErrorRetDtoActionForward(response,
                    AppCommon.RET_CODE_NULL_VALUE, "没有删除编号列表");
        }
        Dto inDto = new BaseDto();
        String[] arrChecked = deleteIds.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = ((DeviceTypeCheckItemService) service).deleteInfo(inDto);
        setRetDtoTipMsg(response, outDto);
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
        Dto inDto = getRequestDto(form, request);
        Dto outDto = ((DeviceTypeCheckItemService) service).updateInfo(inDto);
        return DtoUtils.sendRetDtoActionForward(response, outDto);
    }

    /**
     * 查询检查项管理.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"unchecked"})
    public ActionForward queryCheckItemsForManage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        List<Dto> outDtos = null;
        Integer count = null;
        if (AppTools.isEmptyString(dto.getAsString("devicetypeid"))) {
            outDtos = Lists.newArrayList();
            count = Integer.valueOf(0);
        } else {
            outDtos = appReader.queryForPage(
                    "App.DeviceTypeCheckItem.queryCheckItemsForManage", dto);
            count = (Integer) appReader
                    .queryForObject(
                            "App.DeviceTypeCheckItem.queryCheckItemsForManageForPageCount",
                            dto);

        }
        return DtoUtils.sendStrActionForward(response,
                encodeList2PageJson(outDtos, count, null));
    }

    /**
     * 新增检查信息.
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
        Dto inDto = getRequestDto(form, request);
        Dto outDto = ((DeviceTypeCheckItemService) service).addInfo(inDto);
        return DtoUtils.sendRetDtoActionForward(response, outDto);
    }
}
