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
 * Type：DeviceTypeAction
 * Descript：设备类型Action
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:01:19
 * Version：@version
 */
public class DeviceTypeAction extends BaseAppAction {
	/**
	 * 查询设备巡检记录列表.
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
	public ActionForward queryCheckItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deviceTypeId = request.getParameter("typeID");
		if (AppTools.isEmptyString(deviceTypeId)) {
			return RPCUtils.sendErrorRPCInfoActionForward(response, "设备类型为空");
		}
		List<Dto> list = appReader.queryForList("App.DeviceTypeCheckItem.queryCheckItems", deviceTypeId);
		return RPCUtils.sendRPCListDtoActionForward(response, list);
	}
}
