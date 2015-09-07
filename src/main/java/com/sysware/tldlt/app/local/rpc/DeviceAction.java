package com.sysware.tldlt.app.local.rpc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.system.common.util.SystemConstants;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.device.DeviceService;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：DeviceAction
 * Descript：设备Action.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 下午1:46:41
 * Version：@version
 */
public class DeviceAction extends BaseAppAction {
    private DeviceService deviceService;

    public DeviceAction() {
        deviceService = (DeviceService) this.getService("deviceService");
    }

    /**
     * 查询设备巡检记录列表.
     * @param mapping
     *            struts mapping对象.
     * @param form
     *            struts数据form对象.
     * @param request
     *            http request对象.
     * @param response
     *            http response对象.
     * @return struts跳转地址.
     * @throws Exception
     *             异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward queryInspectRecord(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String deviceID = dto.getAsString("deviceID");
        if (AppTools.isEmptyString(deviceID)) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "设备编号不存在");
        }
        RPCRetDto outDto = RPCUtils.createDto(true, null);
        List<Dto> list = appReader.queryForList(
                "App.Device.queryInspectRecord", deviceID);
        for (Dto dm : list) {
            Integer state = dm.getAsInteger("State");
            if ((null != state) && 1 == state.intValue()) {
                dm.put("isOk", SystemConstants.ENABLED_Y);
            } else {
                dm.put("isOk", SystemConstants.ENABLED_N);
            }
            String recordInfoId = dm.getAsString("inspectRecordInfoId");
            if (!AppTools.isEmptyString(recordInfoId)) {
                dto.put("inspectRecordInfoId", recordInfoId);
                List<Dto> imglist = appReader.queryForList(
                        "App.Inspect.queryImages", dto);
                dm.put("images", imglist);
                String mediaFileUrl = AppTools.getAppPropertyValue("mediaFileUrl", "");
                if ((mediaFileUrl.length() < 1) || (mediaFileUrl.charAt(mediaFileUrl.length()) != '/')) {
                    mediaFileUrl += "/";
                }
                for (Dto iDto : imglist) {
                    String url = iDto.getAsString("mediaurl");
                    url = mediaFileUrl + url;
                    iDto.put("mediaurl", url);
                }
            }
        }
        outDto.addAllData(list);
        return RPCUtils.sendRPCDtoActionForward(response, outDto);
    }

    /**
     * 查询设备巡检记录列表.
     * @param mapping
     *            struts mapping对象.
     * @param form
     *            struts数据form对象.
     * @param request
     *            http request对象.
     * @param response
     *            http response对象.
     * @return struts跳转地址.
     * @throws Exception
     *             异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward queryDeviceInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String deviceID = dto.getAsString("deviceID");
        if (AppTools.isEmptyString(deviceID)) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "设备编号不存在");
        }
        List<Dto> list = appReader.queryForList("App.Device.queryDeviceInfo",
                deviceID);
        return RPCUtils.sendRPCListDtoActionForward(response, list);
    }

    /**
     * 查询设备巡检记录列表.
     * @param mapping
     *            struts mapping对象.
     * @param form
     *            struts数据form对象.
     * @param request
     *            http request对象.
     * @param response
     *            http response对象.
     * @return struts跳转地址.
     * @throws Exception
     *             异常对象.
     */
    public ActionForward saveDeviceGPSInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        BaseRetDto outDto = (BaseRetDto) deviceService.saveGPSInfo(dto);
        return RPCUtils.sendBasicRetDtoRPCInfoActionForward(response, outDto);
    }
}
