package com.sysware.tldlt.app.local.rpc;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;

public class MenuActionTest extends AppMockStrutsTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		setRequestPathInfo("/rpc", "/rpc/menuAction");
	}

	@Test
	public void testQueryMenusSuccess() {
		addRequestParameter("reqCode", new String[] {"queryMenus"});
        String  actual = actionExecuteAndAssertRPCRetInfoSuccess();
        assertTrue(actual.contains("menu_id"));
	}

}
