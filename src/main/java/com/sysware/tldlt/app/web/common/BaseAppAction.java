package com.sysware.tldlt.app.web.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.common.dao.Reader;
import org.g4studio.common.service.BaseService;
import org.g4studio.common.web.BaseAction;
import org.g4studio.common.web.BaseActionForm;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

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
    protected ActionForward getPageNullForward(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String querySql,
            String queryCountSql) throws Exception {
        Dto dto = ((BaseActionForm) form).getParamAsDto(request);
        String jsonString = encodeList2PageJson(
                appReader.queryForPage(querySql, dto),
                (Integer) appReader.queryForObject(queryCountSql, dto), null);
        write(jsonString, response);
        return getNullForward(mapping);
    }

    public void setAppReader(Reader appReader) {
        this.appReader = appReader;
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
            setOkTipMsg("删除成功", response);
        } else {
            StringBuilder strB = new StringBuilder();
            strB.append("删除失败，错误码:");
            strB.append(retDto.getRetCode());
            strB.append("，错误信息:");
            strB.append(outDto.getAsString("desc"));
            setErrTipMsg(strB.toString(), response);
        }
    }
    
    public void setService(BaseService service) {
        this.service = service;
    }    
}
