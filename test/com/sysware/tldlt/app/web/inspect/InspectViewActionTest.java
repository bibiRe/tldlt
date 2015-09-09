package com.sysware.tldlt.app.web.inspect;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;

/**
 * Type：InspectViewActionTest
 * Descript：
 * Create：SW-ITS-HHE
 * Create Time：2015年9月8日 上午11:33:22
 * Version：@version
 */
public class InspectViewActionTest extends AppMockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("app", "/app/inspectView");
    }

    /**
     * 测试初始化成功.
     */
    @Test
    public void testInit_Success() {
        addRequestParameter("reqCode", new String[] {"init"});
        actionPerform();
        verifyForward("inspectRealView");
        verifyForwardPath("/app/inspectview/inspectrealview.jsp");
    }

    /**
     * 测试查询用户成功.
     */
    @Test
    public void testQueryUserForManage_Success() {
        addRequestParameter("reqCode", new String[] {"queryUserForManage"});
        addRequestParameter("start", new String[] {"0"});
        addRequestParameter("limit", new String[] {"50"});
        addRequestParameter("deptid", new String[] {"001"});
        addRequestParameter("firstload", new String[] {"true"});
        addRequestParameter("loginuserid", new String[] {"10000001"});
        String actual = getActionExecuteResponseString();
        assertTrue(actual.contains("TOTALCOUNT:"));
        assertTrue(!actual.contains("TOTALCOUNT:0"));
    }
    
    /**
     * 测试查询用户成功.
     */
    @Test
    public void testQueryInfoForManage_Success() {
        addRequestParameter("reqCode", new String[] {"queryInfoForManage"});
        addRequestParameter("start", new String[] {"0"});
        addRequestParameter("limit", new String[] {"50"});
        addRequestParameter("deptid", new String[] {"001"});
        addRequestParameter("firstload", new String[] {"true"});
        addRequestParameter("loginuserid", new String[] {"10000001"});
        addRequestParameter("infoName", new String[] {"变压器"});
        String actual = getActionExecuteResponseString();
        assertTrue(actual.contains("TOTALCOUNT:"));
        assertTrue(!actual.contains("TOTALCOUNT:0"));
    }
}
