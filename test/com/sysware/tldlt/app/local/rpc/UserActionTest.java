package com.sysware.tldlt.app.local.rpc;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.TestUtils;

/**
 * Type：UserActionTest
 * Descript：
 * Create：SW-ITS-HHE
 * Create Time：2015年8月25日 下午5:25:08
 * Version：@version
 */
public class UserActionTest extends AppMockStrutsTestCase {

    /**
     * 登录用户编号.
     * @return 用户编号
     */
    private String loginUser() {
        String key = "key-10004893";
        UserManage.loginUser("10004893", key);
        return key;
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("/rpc", "/rpc/userAction");
    }

    /**
     * 测试查询关注设备成功.
     */
    @Test
    public void testQueryFocusDevicesSuccess() {
        String key = loginUser();
        addRequestParameter("reqCode", new String[] {"queryFocusDevices"});
        addRequestParameter("key", new String[] {key});
        String actual = actionExecuteAndAssertRPCRetInfo();
        assertTrue(actual.contains("deviceid"));
    }

    /**
     * 测试查询用户列表成功.
     */
    @Test
    public void testQueryUserListSuccess() {
        addRequestParameter("reqCode", new String[] {"queryUserList"});
        String actual = actionExecuteAndAssertRPCRetInfo();
        assertTrue(actual.contains("userid"));
    }

    /**
     * 测试查询用户列表成功.
     */
    @Test
    public void testSaveGPSInfoSuccess() {
        String key = loginUser();
        addRequestParameter("reqCode", new String[] {"saveGPSInfo"});
        addRequestParameter("key", new String[] {key});
        addRequestParameter("longtitude", new String[] {"118.850"});
        addRequestParameter("latitude", new String[] {"32.1031"});
        addRequestParameter("height", new String[] {"1"});
        addRequestParameter("speed", new String[] {"1"});
        addRequestParameter("datetime",
                new String[] {Long.toString(TestUtils.getCurrentUnixTime())});
        actionExecuteAndAssertRPCRetInfo();
    }
}
