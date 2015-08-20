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
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.service.devicetype.DeviceTypeService;
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
 * Type：RegionAction Descript： Create：SW-ITS-HHE Create Time：2015年8月17日
 * 下午3:56:04 Version：@version
 */
public class RegionAction extends BaseAppAction {
	/**
	 * 组织服务.
	 */
	private OrganizationService organizationService;

	public RegionAction() {
		this.service = (RegionService) super.getService("regionService");
		organizationService = (OrganizationService) super.getService("organizationService");
	}

	/**
	 * 初始化.
	 * 
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
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		setRequestUserDepartmentInfo(request);
		String viewString = "regionView";
		return mapping.findForward(viewString);
	}

	/**
	 * 查询区域管理.
	 * 
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward queryRegionsForManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Dto dto = getRequestDto(form, request);
		String regionid = request.getParameter("regionid");
		if (G4Utils.isNotEmpty(regionid)) {
			super.setSessionAttribute(request, "regionid", regionid);
		}
		if (!G4Utils.isEmpty(request.getParameter("firstload"))) {
			dto.put("regionid", null);
		} else {
			dto.put("regionid", super.getSessionAttribute(request, "regionid"));
		}
		List outDtos = appReader.queryForPage("App.Region.queryRegionsForManage", dto);
		setOutInfoLeaf(outDtos, "regionid");
		String jsonString = encodeList2PageJson(outDtos,

		(Integer) appReader.queryForObject("App.Region.queryRegionsForManageForPageCount", dto), null);
		write(jsonString, response);
		return getNullForward(mapping);
	}

	/**
	 * 区域树初始化.
	 * 
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
	@SuppressWarnings({ "unchecked" })
	public ActionForward regionTreeInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Dto dto = new BaseDto();
		String nodeid = request.getParameter("node");
		int pId = 0;
		if (!AppTools.isEmptyString(nodeid)) {
			pId = Integer.valueOf(nodeid);
		}
		dto.put("parentId", pId);
		Collection<Dto> outDtos = ((RegionService) this.service).queryRegionItems(dto);
		setOutInfoLeaf(outDtos, "id");
		write(JsonHelper.encodeObject2Json(outDtos), response);
		return mapping.findForward(null);
	}

	/**
	 * 新增信息.
	 * 
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
	public ActionForward saveAddInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseActionForm aForm = (BaseActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = ((RegionService) service).addInfo(inDto);
		setRetDtoTipMsg(response, outDto);
		return getNullForward(mapping);
	}

	/**
	 * 更新信息.
	 * 
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
	public ActionForward saveUpdateInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseActionForm aForm = (BaseActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = ((RegionService) service).updateInfo(inDto);
		setRetDtoTipMsg(response, outDto);
		return getNullForward(mapping);
	}

	/**
	 * 设置outInfo叶子节点值.
	 * 
	 * @param outDtos
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	private void setOutInfoLeaf(Collection<Dto> outDtos, String key) {
		Dto dto = new BaseDto();
		for (Dto outDto : outDtos) {
			dto.put("parentId", outDto.get(key));
			Collection<Dto> cOutDtos = ((RegionService) this.service).queryRegionItems(dto);
			outDto.put("leaf", cOutDtos.size() < 1);
		}
	}

	/**
	 * 设置Http request对应用户部门信息.
	 * 
	 * @param request
	 *            http request对象.
	 * @return 设置是否成功.
	 */
	@SuppressWarnings("unchecked")
	private boolean setRequestUserDepartmentInfo(HttpServletRequest request) {
		Dto dto = new BaseDto();
		UserInfoVo userInfo = super.getSessionContainer(request).getUserInfo();
		if (null == userInfo) {
			return false;
		}
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
        Dto inDto = new BaseDto();
        String[] arrChecked = deleteIds.split(",");
        inDto.put("ids", Lists.newArrayList(arrChecked));
        Dto outDto = ((RegionService)service).deleteInfo(inDto);
        setRetDtoTipMsg(response, outDto);
        return getNullForward(mapping);
    }
}
