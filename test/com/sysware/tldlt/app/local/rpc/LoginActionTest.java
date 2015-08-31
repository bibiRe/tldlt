package com.sysware.tldlt.app.local.rpc;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;

public class LoginActionTest  extends AppMockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("/rpc", "/rpc/loginAction");
    }

	@Test
	public void testLoginSuccess() {
		addRequestParameter("reqCode", new String[] {"login"});
		addRequestParameter("account", new String[] {"wqiang"});
		addRequestParameter("password", new String[] {"111111"});
        actionExecuteAndAssertRPCRetInfoSuccess();
	}

	@Test
	public void testLoginoutSuccess() {
		RPCUserManage.loginUser("111", "key-sfsd");
		setRequestParameter("reqCode", new String[] {"logout"});
		addRequestParameter("key", new String[] {"key-sdsf"});
		actionExecuteAndAssertRPCRetInfoSuccess();
		
	}

}
