package com.sysware.tldlt.app.web.region;

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
    @SuppressWarnings({"unchecked", "rawtypes"})
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
        for(Dto outDto: outDtos) {
            dto.put("parentId", outDto.get("id"));
            Collection<Dto> cOutDtos = ((RegionService) this.service)
                    .queryRegionItems(dto);
            outDto.put("leaf", cOutDtos.size() < 1);            
        }
        write(JsonHelper.encodeObject2Json(outDtos), response);
        return mapping.findForward(null);
    }
    
    /**
     * 查询部门列表信息
     * 
     * @param
     * @return
     */
    public ActionForward queryRegionsForManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        BaseActionForm aForm = (BaseActionForm)form;
        Dto dto = aForm.getParamAsDto(request);
        String regionId = request.getParameter("regionid");
        dto.put("regionid", regionId);
        List menuList = g4Reader.queryForPage("App.Region.queryRegionsForManage", dto);
        Integer pageCount = (Integer) g4Reader.queryForObject("App.Region.queryDeptsForManageForPageCount", dto);
        String jsonString = encodeList2PageJson(menuList, pageCount, null);
        write(jsonString, response);
        return mapping.findForward(null);
    }
}
