package com.sysware.tldlt.app.local.rpc;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;

public class DeviceTypeActionTest extends AppMockStrutsTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		setRequestPathInfo("/rpc", "/rpc/deviceTypeAction");
	}

	@Test
	public void testQueryCheckItemsSuccess() {
		addRequestParameter("reqCode", new String[] {"queryCheckItems"});
		addRequestParameter("typeID", new String[] {"Transformer"});
        String  actual = actionExecuteAndAssertRPCRetInfo();
        assertTrue(actual.contains("checkid"));
	}

}
