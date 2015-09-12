package com.sysware.tldlt.app.web.inspect;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;

import com.google.common.collect.Maps;
import com.sysware.tldlt.app.service.inspect.InspectViewService;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：InspectViewAction
 * Descript：巡检查看action.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月7日 下午2:02:33
 * Version：@version
 */
public class InspectViewAction extends BaseAppAction {
    /**
     * 偏差统计秒数.
     */
    private static final int DIFFCOUNT_SEC = AppCommon.HOUR_SEC * -2400000;
    /**
     * 组织机构模型模型业务接口.
     */
    private OrganizationService organizationService;

    /**
     * 构造函数.
     */
    public InspectViewAction() {
        this.service = (InspectViewService) super
                .getService("inspectViewService");
        organizationService = (OrganizationService) super
                .getService("organizationService");
    }

    /**
     * 转换Dto里面执行时间为字符串.
     * @param dtos dto列表
     */
    @SuppressWarnings("unchecked")
    private void convertDtoExecuteUnixTimeToStr(List<Dto> dtos) {
        for (Dto outDto : dtos) {
            Long v = outDto.getAsLong("executestarttime");
            if (null != v) {
                outDto.put("executestarttime",
                        AppTools.unixTime2DateStr(v.longValue()));
            }
            v = outDto.getAsLong("executeendtime");
            if (null != v) {
                outDto.put("executeendtime",
                        AppTools.unixTime2DateStr(v.longValue()));
            }
        }
    }

    /**
     * 得到用户巡检计划dto.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @return dto
     */
    @SuppressWarnings("unchecked")
    private Dto getUserInspectPlanDto(ActionForm form,
            HttpServletRequest request) {
        Dto dto = getRequestDto(form, request);
        if (AppTools.isEmptyString(dto.getAsString("userid"))) {
            dto.put("userid", dto.getAsString("userID"));
        }
        dto.put("datetime", "2015-08-21 14:47:41");
        return dto;
    }

    /**
     * 得到用户巡检计划列表.
     * @param statementName sql名字
     * @param dto dto对象
     * @return 列表
     */
    @SuppressWarnings("unchecked")
    private List<Dto> getUserInspectPlanList(String statementName, Dto dto) {
        List<Dto> outDtos = appReader.queryForList(statementName, dto);
        convertDtoExecuteUnixTimeToStr(outDtos);
        return outDtos;
    }

    /**
     * 初始化实时查看.
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
        setRequestUserDepartmentInfo(organizationService, request);
        String viewString = "inspectView";
        return mapping.findForward(viewString);
    }

    /**
     * 实时查看信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"unchecked"})
    public ActionForward queryInfoForManage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        if (null != userInfo) {
            dto.put("deptid", userInfo.getDeptid());
        }
        dto.put("datetime",
                AppTools.unixTime2DateStr(AppTools.diffUnixTime(
                        AppTools.currentUnixTime(), DIFFCOUNT_SEC)));
        List<Dto> outDtos = appReader.queryForPage(
                "App.InspectView.queryInfoForManage", dto);
        for (Dto dto1 : outDtos) {
            String info = dto1.getAsString("info");
            if (!AppTools.isEmptyString(info)) {
                Dto d = JsonHelper.parseSingleJson2Dto(info);
                dto1.put("info", d);
            }
        }
        String jsonString = encodeList2PageJson(outDtos,
                (Integer) appReader.queryForObject(
                        "App.InspectView.queryInfoForManageForPageCount", dto),
                null);
        return DtoUtils.sendStrActionForward(response, jsonString);
    }

    /**
     * 查询用户最后gps信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings("unchecked")
    public ActionForward queryUserCurrentInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getUserInspectPlanDto(form, request);
        Dto result = new BaseDto();
        List<Dto> outDtos = getUserInspectPlanList(
                "App.InspectPlan.querySoonExecutePlan", dto);
        result.put("soonplan", outDtos);
        List<Dto> outDtos1 = getUserInspectPlanList(
                "App.InspectPlan.queryExecutingPlan", dto);
        result.put("executeplan", outDtos1);
        return DtoUtils.sendSuccessDataRetDtoActionForward(response, result);

    }

    /**
     * 查询用户正在执行的巡检计划.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward queryUserExecutingInspectPlan(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return queryUserInspectPlanAndSendForward(
                "App.InspectPlan.queryExecutingPlan", form, request, response);
    }

    /**
     * 实时查看用户.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ActionForward queryUserForManage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        if (null != userInfo) {
            dto.put("deptid", userInfo.getDeptid());
        }
        dto.put("datetime",
                AppTools.unixTime2DateStr(AppTools.diffUnixTime(
                        AppTools.currentUnixTime(), DIFFCOUNT_SEC)));
        List outDtos = appReader.queryForPage(
                "App.InspectView.queryUserForManage", dto);
        String jsonString = encodeList2PageJson(outDtos,
                (Integer) appReader.queryForObject(
                        "App.InspectView.queryUserForManageForPageCount", dto),
                null);
        return DtoUtils.sendStrActionForward(response, jsonString);
    }

    /**
     * 查询用户正在执行的巡检计划.
     * @param statementName sql名字
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    private ActionForward queryUserInspectPlanAndSendForward(
            String statementName, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getUserInspectPlanDto(form, request);
        List<Dto> outDtos = getUserInspectPlanList(statementName, dto);
        return DtoUtils.sendSuccessDataRetDtoActionForward(response, outDtos);
    }

    /**
     * 查询用户最后gps信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    @SuppressWarnings({"unchecked"})
    public ActionForward queryUserLastGPSInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        if (AppTools.isEmptyString(dto.getAsString("userid"))) {
            dto.put("userid", dto.getAsString("userID"));
        }
        dto.put("datetime",
                AppTools.unixTime2DateStr(AppTools.diffUnixTime(
                        AppTools.currentUnixTime(), DIFFCOUNT_SEC)));
        List<Dto> outDtos = appReader
                .queryForList("App.User.queryGPSInfo", dto);

        Map<String, Dto> datas = Maps.newHashMap();
        for (Dto dto1 : outDtos) {
            String userid = dto1.getAsString("userid");
            if (AppTools.isEmptyString(userid)) {
                continue;
            }
            if (datas.containsKey(userid)) {
                continue;
            }
            datas.put(userid, dto1);
        }
        return DtoUtils.sendSuccessDataRetDtoActionForward(response,
                datas.values());
    }

    /**
     * 查询用户最后gps信息.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward queryUserSoonExecuteInspectPlan(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return queryUserInspectPlanAndSendForward(
                "App.InspectPlan.querySoonExecutePlan", form, request, response);
    }
}
