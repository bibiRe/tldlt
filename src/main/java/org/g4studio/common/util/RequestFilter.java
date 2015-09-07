package org.g4studio.common.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.common.dao.Dao;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.properties.PropertiesFactory;
import org.g4studio.core.properties.PropertiesFile;
import org.g4studio.core.properties.PropertiesHelper;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.util.G4Utils;
import org.g4studio.system.admin.service.MonitorService;
import org.g4studio.system.common.dao.vo.UserInfoVo;
import org.g4studio.system.common.util.SystemConstants;
import org.g4studio.system.common.util.idgenerator.IDHelper;

import com.sysware.tldlt.app.local.rpc.RPCUserManage;
import com.sysware.tldlt.app.local.rpc.RPCUtils;
import com.sysware.tldlt.app.utils.AppTools;
import com.sysware.tldlt.app.utils.DtoUtils;

/**
 * 请求拦截过滤器
 * 
 * @author XiongChun
 * @since 2010-04-13
 */
public class RequestFilter implements Filter {

	private Log log = LogFactory.getLog(RequestFilter.class);
	protected FilterConfig filterConfig;
	protected boolean enabled;

	/**
	 * 构造
	 */
	public RequestFilter() {
		filterConfig = null;
		enabled = true;
	}

	/**
	 * 初始化
	 */
	public void init(FilterConfig pFilterConfig) throws ServletException {
		this.filterConfig = pFilterConfig;
		String value = filterConfig.getInitParameter("enabled");
		if (G4Utils.isEmpty(value)) {
			this.enabled = true;
		} else if (value.equalsIgnoreCase("true")) {
			this.enabled = true;
		} else {
			this.enabled = false;
		}
	}

	private boolean filterRPCUrl(HttpServletRequest request, String uri) {
		if (!uri.startsWith("/rpc")) {
			return false;
		}
		return true;
	}

	/**
	 * 过滤处理
	 */
	public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain fc)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) pRequest;
		HttpServletResponse response = (HttpServletResponse) pResponse;
		if (filterRequest(request, response)) {
			return;
		}
		// if(){.... return;}
		PropertiesHelper pHelper = PropertiesFactory.getPropertiesHelper(PropertiesFile.G4);
		String eventMonitorEnabel = pHelper.getValue("requestMonitor", "1");
		BigDecimal costTime = null;
		long start = System.currentTimeMillis();
		fc.doFilter(request, response);
		if (eventMonitorEnabel.equalsIgnoreCase(SystemConstants.EVENTMONITOR_ENABLE_Y)) {
			costTime = new BigDecimal(System.currentTimeMillis() - start);
			saveEvent(request, costTime);
		}
	}

	private boolean filterRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestUri = request.getRequestURI();
		String ctxPath = request.getContextPath();
		String uri = requestUri.substring(ctxPath.length());
		if (filterRPCUrl(request, uri)) {
			if (filterRPCRequestKey(request, response, uri)) {
				return true;
			}
			return false;
		}
		String isAjax = request.getHeader("x-requested-with");
		UserInfoVo userInfo = WebUtils.getSessionContainer(request).getUserInfo();
		if (filterRequestUserInfo(request, response, ctxPath, uri, isAjax, userInfo)) {
			return true;
		}
		if (filterRequestAjaxUserInfo(request, response, uri, isAjax, userInfo)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
    private boolean filterRPCRequestKey(HttpServletRequest request, HttpServletResponse response, String uri)
			throws IOException {
		if (!uri.contains("loginAction.do") || !"login".equals(request.getParameter("reqCode"))) {	        
		    String key = request.getParameter("key");
			if (G4Utils.isEmpty(key)) {
			    String contentType = request.getContentType();           
	            if (contentType.startsWith("multipart/form-data")) {
	                return false;
	            }				
                RPCUtils.writeErrorRPCInfo(response, "没有Key值");
                return true;
			}
			if (!RPCUserManage.checkUserKey(key)) {
				RPCUtils.writeErrorRPCInfo(response, "Key值无效");
				return true;
			}
		}
		return false;
	}

	private boolean filterRequestAjaxUserInfo(HttpServletRequest request, HttpServletResponse response, String uri,
			String isAjax, UserInfoVo userInfo) throws IOException {
		if (G4Utils.isNotEmpty(isAjax)) {
			if (!uri.equals("/login.do")) {
				String loginuserid = request.getParameter("loginuserid");
				if (G4Utils.isEmpty(loginuserid)) {
					response.sendError(G4Constants.Ajax_Unknow);
					log.error("请求非法,[loginuserid]参数缺失");
					return true;
				}
				if (!loginuserid.equals(userInfo.getUserid())) {
					response.sendError(G4Constants.Ajax_Session_Unavaliable);
					log.error("当前会话和登录用户会话不一致,请求被重定向到了登录页面");
					return true;
				}
			}
		}
		return false;
	}

	private boolean filterRequestUserInfo(HttpServletRequest request, HttpServletResponse response, String ctxPath,
			String uri, String isAjax, UserInfoVo userInfo) throws IOException {
		if (G4Utils.isEmpty(userInfo) && !uri.equals("/login.do") && enabled) {
			if (G4Utils.isEmpty(isAjax)) {
			    DtoUtils.writeToResponse("<script type=\"text/javascript\">parent.location.href='" + ctxPath
                        + "/login.do?reqCode=init'</script>", response);
			} else {
				response.sendError(G4Constants.Ajax_Timeout);
			}
			log.warn("警告:非法的URL请求已被成功拦截,请求已被强制重定向到了登录页面.访问来源IP锁定:" + request.getRemoteAddr() + " 试图访问的URL:"
					+ request.getRequestURL().toString() + "?reqCode=" + request.getParameter("reqCode"));
			return true;
		}
		return false;
	}

	/**
	 * 写操作员事件表
	 * 
	 * @param request
	 */
	private void saveEvent(HttpServletRequest request, BigDecimal costTime) {
		UserInfoVo userInfo = WebUtils.getSessionContainer(request).getUserInfo();
		if (G4Utils.isEmpty(userInfo)) {
			return;
		}
		String menuid = request.getParameter("menuid4Log");
		Dto dto = new BaseDto();
		dto.put("account", userInfo.getAccount());
		dto.put("activetime", G4Utils.getCurrentTimeAsNumber());
		dto.put("userid", userInfo.getUserid());
		dto.put("username", userInfo.getUsername());
		dto.put("requestpath", request.getRequestURI());
		dto.put("methodname", request.getParameter("reqCode"));
		dto.put("eventid", IDHelper.getEventID());
		dto.put("costtime", costTime);
		if (G4Utils.isNotEmpty(menuid)) {
			Dao g4Dao = (Dao) SpringBeanLoader.getSpringBean("g4Dao");
			String menuname = ((BaseDto) g4Dao.queryForObject("Resource.queryEamenuByMenuID", menuid))
					.getAsString("menuname");
			String msg = userInfo.getUsername() + "[" + userInfo.getAccount() + "]打开了菜单[" + menuname + "]";
			dto.put("description", msg);
			log.info(msg);
		} else {
			String msg = userInfo.getUsername() + "[" + userInfo.getAccount() + "]调用了Action方法["
					+ request.getParameter("reqCode") + "]";
			dto.put("description", msg);
			log.info(msg + ";请求路径[" + request.getRequestURI() + "]");
		}
		MonitorService monitorService = (MonitorService) SpringBeanLoader.getSpringBean("monitorService");
		monitorService.saveEvent(dto);

	}

	/**
	 * 销毁
	 */
	public void destroy() {
		filterConfig = null;
	}

}
