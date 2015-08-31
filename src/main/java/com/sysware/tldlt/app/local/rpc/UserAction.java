package com.sysware.tldlt.app.local.rpc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.common.util.SpringBeanLoader;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.user.UserService;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：UserAction
 * Descript：用户Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月25日 下午5:25:12
 * Version：@version
 */
public class UserAction extends BaseAppAction {
    /**
     * 用户服务对象.
     */
    private UserService userService;

    public UserAction() {
        userService = (UserService) SpringBeanLoader
                .getSpringBean("appUserService");
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
    public ActionForward queryFocusDevices(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = RPCUserManage.findUserDtoByKey(request.getParameter("key"));
        if (null == dto) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值无效");
        }
        List<Dto> list = appReader.queryForList("App.User.queryFocusDevices",
                dto.get("userId"));
        return RPCUtils.sendRPCListDtoActionForward(response, list);
    }

    /**
     * 查询用户列表.
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
    public ActionForward queryUserList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Dto> list = appReader.queryForList("App.User.queryUserList");
        return RPCUtils.sendRPCListDtoActionForward(response, list);
    }

    /**
     * 保存GPS信息.
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
    public ActionForward saveGPSInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Dto dto = getRequestDto(form, request);
        Dto userDto = RPCUserManage.findUserDtoByKey(request.getParameter("key"));
        if (null == userDto) {
            return RPCUtils.sendErrorRPCInfoActionForward(response, "key值无效");
        }
        dto.put("userid", userDto.get("userId"));
        BaseRetDto outDto = (BaseRetDto) userService.saveGPSInfo(dto);
        return RPCUtils.sendRPCDtoActionForward(response,
                RPCUtils.createDto(outDto.isRetSuccess(), outDto.getDesc()));
    }
}
