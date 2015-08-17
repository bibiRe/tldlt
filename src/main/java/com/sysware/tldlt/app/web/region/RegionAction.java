package com.sysware.tldlt.app.web.region;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.util.G4Utils;

import com.sysware.tldlt.app.service.region.RegionService;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：RegionAction
 * Descript：区域管理Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月11日 上午11:33:42
 * Version：@version
 */
/**
 * Type：RegionAction
 * Descript：
 * Create：SW-ITS-HHE
 * Create Time：2015年8月17日 下午3:56:04
 * Version：@version
 */
public class RegionAction extends BaseAppAction {
    public RegionAction() {
        this.service = (RegionService) super.getService("regionService");
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
        String viewString = "regionView";
        return mapping.findForward(viewString);
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
    public ActionForward regionTreeInit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Dto dto = new BaseDto();
        String nodeid = request.getParameter("node");
        int pId = 0;
        if (!AppTools.isEmptyString(nodeid)) {
            pId = Integer.valueOf(nodeid);
        }
        dto.put("parentId", pId);
        Collection<Dto> outDtos = ((RegionService) this.service)
                .queryRegionItems(dto);
        for (Dto outDto : outDtos) {
            dto.put("parentId", outDto.get("id"));
            Collection<Dto> cOutDtos = ((RegionService) this.service)
                    .queryRegionItems(dto);
            outDto.put("leaf", cOutDtos.size() < 1);
        }
        write(JsonHelper.encodeObject2Json(outDtos), response);
        return mapping.findForward(null);
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
    @SuppressWarnings("unchecked")
    public ActionForward queryRegionsForManage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Dto dto = getRequestDto(form, request);
        String regionid = request.getParameter("regionid");
        if (G4Utils.isNotEmpty(regionid)) {
            super.setSessionAttribute(request, "regionid", regionid);
        }
        if (!G4Utils.isEmpty(request.getParameter("firstload"))) {
            dto.put("regionid", "0");
        } else {
            dto.put("regionid", super.getSessionAttribute(request, "regionid"));
        }
        return geDtoPageNullForward(dto, "App.Region.queryRegionsForManage",
                "App.Region.queryRegionsForManageForPageCount", response,
                mapping);
    }
}
