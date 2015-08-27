package com.sysware.tldlt.app.local.rpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;
import org.g4studio.core.util.CodeUtil;
import org.g4studio.core.util.G4Constants;
import org.g4studio.system.admin.service.OrganizationService;
import org.g4studio.system.common.dao.vo.UserInfoVo;

import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：LoginAction
 * Descript：登录Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:01:56
 * Version：@version
 */
public class LoginAction extends BaseAppAction {
	/**
	 * 组织机构服务接口.
	 */
	private OrganizationService organizationService;

	public LoginAction() {
		organizationService = (OrganizationService) super.getService("organizationService");
	}

	/**
	 * 注册信息.
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
	public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Dto dto = getRequestDto(form, request);
		RPCRetDto outDto = null;
		if (AppTools.isEmptyString(dto.getAsString("account"))) {
			return RPCUtils.sendErrorRPCInfoActionForward(response, "没有用户名");
		}
		String password = request.getParameter("password");
		if (AppTools.isEmptyString(password)) {
			return RPCUtils.sendErrorRPCInfoActionForward(response, "没有密码");
		}
		password = CodeUtil.encryptBase64(password, G4Constants.BASE64_KEY);
		dto.put("password", password);
		UserInfoVo userInfo = (UserInfoVo) organizationService.getUserInfo(dto).get("userInfo");
		if (null == userInfo) {
			return RPCUtils.sendErrorRPCInfoActionForward(response, "用户名或密码无效");
		}
		outDto = RPCUtils.createDto(true, null);
		Dto data = new BaseDto();
		outDto.addData(data);
		data.put("user_name", userInfo.getUsername());
		data.put("userid", userInfo.getUserid());
		String key = "key-" + userInfo.getUserid();
		UserManage.loginUser(userInfo.getUserid(), key);
		data.put("key", key);
		data.put("deptid", userInfo.getDeptid());
		data.put("deptname", userInfo.getDeptname());
		data.put("app_control", "0");
		write(outDto.toJson(), response);
		return getNullForward(mapping);
	}

	/**
	 * 注册信息.
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
	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManage.logout(request.getParameter("key"));
		write(RPCUtils.createDto(true, null).toJson(), response);
		return getNullForward(mapping);
	}
}
