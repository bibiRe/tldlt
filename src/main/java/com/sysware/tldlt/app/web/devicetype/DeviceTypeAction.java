package com.sysware.tldlt.app.web.devicetype;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.common.web.BaseActionForm;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.util.G4Utils;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.service.devicetype.DeviceTypeService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
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
        if (AppTools.isEmptyString(deleteIds)) {
            return DtoUtils.sendErrorRetDtoActionForward(response,
                    AppCommon.RET_CODE_NULL_VALUE, "没有删除编号列表");
        }
        Dto inDto = new BaseDto();
        String[] arrChecked = deleteIds.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = ((DeviceTypeService) service).deleteInfo(inDto);
        outDto.put("devicetypeid", request.getParameter("devicetypeid"));
        setRetDtoTipMsg(response, outDto);
        return getNullForward(mapping);
    }

    /**
     * 区域树初始化.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"unchecked"})
    public ActionForward deviceTypeTreeInit(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = new BaseDto();
        dto.put("parentId", request.getParameter("node"));
        Collection<Dto> outDtos = ((DeviceTypeService) this.service)
                .queryDeviceTypeItems(dto);
        setOutInfoLeaf(outDtos, "id");
        return DtoUtils.sendStrActionForward(response,
                JsonHelper.encodeObject2Json(outDtos));
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
     * 查询区域管理.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ActionForward queryDeviceTypesForManage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String devicetypeid = request.getParameter("devicetypeid");
        if (G4Utils.isNotEmpty(devicetypeid)) {
            super.setSessionAttribute(request, "devicetypeid", devicetypeid);
        }
        if (!G4Utils.isEmpty(request.getParameter("firstload"))) {
            dto.put("devicetypeid", "0");
        } else {
            dto.put("devicetypeid",
                    super.getSessionAttribute(request, "devicetypeid"));
        }
        List outDtos = appReader.queryForPage(
                "App.DeviceType.queryDeviceTypesForManage", dto);
        setOutInfoLeaf(outDtos, "devicetypeid");
        String jsonString = encodeList2PageJson(outDtos,
                (Integer) appReader.queryForObject(
                        "App.DeviceType.queryDeviceTypesForManageForPageCount",
                        dto), null);
        return DtoUtils.sendStrActionForward(response, jsonString);
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
        Dto outDto = ((DeviceTypeService) service).addInfo(inDto);
        return DtoUtils.sendRetDtoActionForward(response, outDto);
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
        Dto outDto = ((DeviceTypeService) service).updateInfo(inDto);
        return DtoUtils.sendRetDtoActionForward(response, outDto);
    }

    /**
     * 设置outInfo叶子节点值.
     * @param outDtos
     * @param key
     */
    @SuppressWarnings("unchecked")
    private void setOutInfoLeaf(Collection<Dto> outDtos, String key) {
        Dto dto = new BaseDto();
        for (Dto outDto : outDtos) {
            dto.put("parentId", outDto.get(key));
            Collection<Dto> cOutDtos = ((DeviceTypeService) this.service)
                    .queryDeviceTypeItems(dto);
            outDto.put("leaf", cOutDtos.size() < 1);
        }
    }

}
