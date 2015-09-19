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
        actionExecuteAndVerifyForward("inspectView",
                "/app/inspectview/inspectview.jsp");
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

    /**
     * 测试查询错误信息成功.
     */
    @Test
    public void testQueryFaultInfoForManage_Success() {
        addRequestParameter("reqCode", new String[] {"queryFaultInfoForManage"});
        addRequestParameter("start", new String[] {"0"});
        addRequestParameter("limit", new String[] {"50"});
        addRequestParameter("deptid", new String[] {"001"});
        addRequestParameter("firstload", new String[] {"true"});
        addRequestParameter("loginuserid", new String[] {"10000001"});
        addRequestParameter("faultInfoName", new String[] {"张明"});
        String actual = getActionExecuteResponseString();
        assertTrue(actual.contains("TOTALCOUNT:"));
        assertTrue(!actual.contains("TOTALCOUNT:0"));
    }

    /**
     * 测试查询用户最后GPS成功.
     */
    @Test
    public void testQueryUserLastGPSInfo_Success() {
        addRequestParameter("reqCode", new String[] {"queryUserLastGPSInfo"});
        addRequestParameter("userid", new String[] {"10004893"});
        String actual = actionExecuteAndAssertRetInfo();
        assertTrue(actual.contains("\"gpsinfoid\":"));
    }

    /**
     * 测试查询用户成功正在执行计划.
     */
    @Test
    public void testQueryUserExecutingInspectPlan_Success() {
        addRequestParameter("reqCode",
                new String[] {"queryUserExecutingInspectPlan"});
        addRequestParameter("userid", new String[] {"10004893"});
        String actual = actionExecuteAndAssertRetInfo();
        assertTrue(actual.contains("\"inspectrecordid\":"));
    }

    /**
     * 测试查询用户成功正在执行计划.
     */
    @Test
    public void testQueryUserCurrentInfo_Success() {
        addRequestParameter("reqCode", new String[] {"queryUserCurrentInfo"});
        addRequestParameter("userid", new String[] {"10004893"});
        String actual = actionExecuteAndAssertRetInfo();
        assertTrue(actual.contains("\"inspectrecordid\":"));
    }

    /**
     * 测试查询用户即将执行计划.
     */
    @Test
    public void testQueryUserSoonExecuteInspectPlan_Success() {
        addRequestParameter("reqCode",
                new String[] {"queryUserSoonExecuteInspectPlan"});
        addRequestParameter("userid", new String[] {"10004893"});
        String actual = actionExecuteAndAssertRetInfo();
        assertTrue(actual.contains("\"planid\":"));
    }
}
