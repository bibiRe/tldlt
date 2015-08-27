package com.sysware.tldlt.app.local.rpc;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.action.ActionForm;
import org.g4studio.core.mvc.xstruts.action.ActionForward;
import org.g4studio.core.mvc.xstruts.action.ActionMapping;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.web.common.BaseAppAction;

/**
 * Type：MenuAction
 * Descript：菜单Action类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 上午11:02:36
 * Version：@version
 */
public class MenuAction extends BaseAppAction {
	/**
	 * 删除信息.
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
	public ActionForward queryMenus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RPCRetDto outDto = RPCUtils.createDto(true, null);
		Dto data = new BaseDto();
		outDto.addData(data);
		data.put("group_meun_name", "巡检");
		Collection<Dto> menuList = Lists.newArrayList();
		data.put("menu", menuList);
		Dto menuDto = new BaseDto();
		menuList.add(menuDto);
		menuDto.put("menu_id", "001");
		menuDto.put("menu_name", "巡检计划");
		menuDto.put("class_name", "com.sdsw.firecontrolopenapp.app.fire.FireAlarmFragment");
		menuDto.put("menu_show_type", "1");
		menuDto.put("params", "{title_name:\'巡检计划\',event_type:1, sub_url:\'rpc/inspectPlanAction.do\'}");
		return RPCUtils.sendRPCDtoActionForward(response, outDto);
	}
	

}
