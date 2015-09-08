package com.sysware.tldlt.app.web.inspect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;

import com.sysware.tldlt.app.service.inspect.InspectService;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：InspectAction
 * Descript：巡检action.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月7日 下午2:02:33
 * Version：@version
 */
public class InspectViewAction extends BaseAppAction {
    /**
     * 组织机构模型模型业务接口.
     */
    private OrganizationService organizationService;

    public InspectViewAction() {
        this.service = (InspectService) super.getService("inspectService");
        organizationService = (OrganizationService) super
                .getService("organizationService");
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
        String viewString = "realView";
        return mapping.findForward(viewString);
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
    public ActionForward queryUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Dto dto = getRequestDto(form, request);
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        dto.put("deptid", userInfo.getDeptid());
        return null;
    }

}
