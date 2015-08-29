package com.sysware.tldlt.app.local.rpc;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;

public class InspectPlanActionTest extends AppMockStrutsTestCase {

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		setRequestPathInfo("/rpc", "/rpc/inspectPlanAction");
	}

	@Test
	public void testQueryPlanBasicInfoSuccess() {
		addRequestParameter("reqCode", new String[] {"queryPlanBasicInfo"});
		addRequestParameter("userID", new String[] {"10004894"});
        String  actual = actionExecuteAndAssertRPCRetInfoSuccess();
        assertTrue(actual.contains("planid"));
	}

	@Test
	public void testQueryPlanDeviceInfoSuccess() {
		addRequestParameter("reqCode", new String[] {"queryPlanDeviceInfo"});
		addRequestParameter("planID", new String[] {"1"});
		String  actual = actionExecuteAndAssertRPCRetInfoSuccess();
		assertTrue(actual.contains("deviceid"));
	}

}
