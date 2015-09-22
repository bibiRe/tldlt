package com.sysware.tldlt.app.web.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.common.dao.Reader;
import org.g4studio.common.service.BaseService;
import org.g4studio.common.web.BaseAction;
import org.g4studio.common.web.BaseActionForm;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;

/**
 * Type：BaseAppAction
 * Descript：基本App Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月21日 下午5:20:55
 * Version：@version
 */
public class BaseAppAction extends BaseAction {
    /**
     * app Reader对象.
     */
    protected Reader appReader = (Reader) getService("appReader");
    /**
     * 服务接口.
     */
    protected BaseService service;

    /**
     * 创建用户Dto.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @return Dto对象
     */
    @SuppressWarnings("unchecked")
    protected Dto createRequestCurrentUserDepartmentDto(ActionForm form,
            HttpServletRequest request) {
        Dto dto = getRequestDto(form, request);
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        if (null != userInfo) {
            dto.put("deptid", userInfo.getDeptid());
        }
        return dto;
    }

    /**
     * 得到Dto翻页空返回.
     * @param dto dto对象
     * @param querySql 查询条件Sql.
     * @param queryCountSql 查询条件总数Sql.
     * @param response http response对象.
     * @param mapping struts mapping对象.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    public ActionForward geDtoPageNullForward(Dto dto, String querySql,
            String queryCountSql, HttpServletResponse response,
            ActionMapping mapping) throws Exception {
        String jsonString = encodeList2PageJson(
                appReader.queryForPage(querySql, dto),
                (Integer) appReader.queryForObject(queryCountSql, dto), null);
        write(jsonString, response);
        return getNullForward(mapping);
    }

    /**
     * 返回空Forward.
     * @param mapping mapping对象.
     * @return 空Forward
     */
    protected ActionForward getNullForward(ActionMapping mapping) {
        return mapping.findForward(null);
    }

    /**
     * 得到翻页空返回.
     * @param mapping struts mapping对象.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @param response http response对象.
     * @param querySql 查询条件Sql.
     * @param queryCountSql 查询条件总数Sql.
     * @return struts跳转地址.
     * @throws Exception 异常对象.
     */
    protected ActionForward
            getPageNullForward(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    String querySql, String queryCountSql) throws Exception {
        Dto dto = getRequestDto(form, request);
        return geDtoPageNullForward(dto, querySql, queryCountSql, response,
                mapping);
    }

    /**
     * 得到请求的dto.
     * @param form struts数据form对象.
     * @param request http request对象.
     * @return dto
     */
    public Dto getRequestDto(ActionForm form, HttpServletRequest request) {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        return dto;
    }

    public void setAppReader(Reader appReader) {
        this.appReader = appReader;
    }

    /**
     * 设置Http request对应用户部门信息.
     * @param request http request对象.
     * @return 设置是否成功.
     */
    protected boolean setRequestUserInfo(HttpServletRequest request) {
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        if (null == userInfo) {
            return false;
        }
        request.setAttribute("login_account", userInfo.getAccount());
        request.setAttribute("login_userid", userInfo.getUserid());
        return true;
    }

    /**
     * 设置Http request对应用户部门信息.
     * @param request http request对象.
     * @return 设置是否成功.
     */
    @SuppressWarnings("unchecked")
    protected boolean
            setRequestUserDepartmentInfo(
                    OrganizationService organizationService,
                    HttpServletRequest request) {
        if (!setRequestUserInfo(request)) {
            return false;
        }
        super.removeSessionAttribute(request, "deptid");
        UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
        if (null == userInfo) {
            return false;
        }
        Dto dto = new BaseDto();
        String deptid = userInfo.getDeptid();
        dto.put("deptid", deptid);
        Dto outDto = organizationService.queryDeptinfoByDeptid(dto);
        if (null == outDto) {
            return false;
        }
        request.setAttribute("rootDeptid", outDto.getAsString("deptid"));
        request.setAttribute("rootDeptname", outDto.getAsString("deptname"));
        return true;
    }

    /**
     * 设置返回信息.
     * @param response response对象
     * @param outDto dto对象
     * @throws IOException IO异常
     */
    protected void setRetDtoTipMsg(HttpServletResponse response, Dto outDto)
            throws IOException {
        BaseRetDto retDto = (BaseRetDto) outDto;
        if (retDto.isRetSuccess()) {
            retDto.setSuccess(true);
            retDto.setMsg("操作成功");
        } else {
            StringBuilder strB = new StringBuilder();
            retDto.setSuccess(false);
            strB.append("操作失败，错误码:");
            strB.append(retDto.getRetCode());
            strB.append("，错误信息:");
            strB.append(outDto.getMsg());
            retDto.setMsg(strB.toString());
        }
        write(outDto.toJson(), response);
    }

    public void setService(BaseService service) {
        this.service = service;
    }
}
