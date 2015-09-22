package com.sysware.tldlt.app.web.devicefault;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.TestUtils;

/**
 * Type：DeviceFaultActionTest
 * Descript：设备故障action.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月19日 下午4:36:32
 * Version：@version
 */
public class DeviceFaultActionTest extends AppMockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("app", "/app/deviceFault");
        TestUtils.loginWebUser(request);
    }

    @Test
    public void testInitSuccess() {
        addRequestParameter("reqCode", new String[] {"init"});
        actionExecuteAndVerifyForward("deviceFaultView",
                "/app/devicefault/devicefaultview.jsp");
    }

    /**
     * 测试查询错误信息成功.
     */
    @Test
    public void testQueryFaultInfoForManage_Success() {
        addRequestParameter("reqCode",
                new String[] {"queryDeviceFaultForManage"});
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
     * 测试更新状态信息成功.
     */
    @Test
    public void testSaveUpdateStateInfo_Success() {
        addRequestParameter("reqCode", new String[] {"saveUpdateStateInfo"});
        addRequestParameter("devicefaultinfoid", new String[] {"1"});
        addRequestParameter("state", new String[] {"2"});
        addRequestParameter("remark", new String[] {"r1"});
        actionExecuteAndAssertRetInfo();
    }

}
