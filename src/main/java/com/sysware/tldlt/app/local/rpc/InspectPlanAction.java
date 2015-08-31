package com.sysware.tldlt.app.local.rpc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：InspectPlanAction
 * Descript：巡检计划Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:01:42
 * Version：@version
 */
public class InspectPlanAction extends BaseAppAction {
	/**
	 * 删除巡检计划基本信息.
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
	@SuppressWarnings("unchecked")
	public ActionForward queryPlanBasicInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Dto dto = getRequestDto(form, request);
		List<Dto> list = appReader.queryForList("App.InspectPlan.queryExecuteUserPlanOutline", dto);
		return RPCUtils.sendRPCListDtoActionForward(response, list);
	}
	
	/**
	 * 查询巡检计划设备列表.
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
	@SuppressWarnings("unchecked")
	public ActionForward queryPlanDeviceInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String planID = request.getParameter("planID");
		if (AppTools.isEmptyString(planID)) {
			return RPCUtils.sendErrorRPCInfoActionForward(response, "巡检计划编号不存在");
		}
		List<Dto> list = appReader.queryForList("App.InspectPlan.queryPlanDevicesByPlanId", planID);
		return RPCUtils.sendRPCListDtoActionForward(response, list);
	}
}
