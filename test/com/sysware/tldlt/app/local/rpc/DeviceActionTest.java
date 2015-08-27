package com.sysware.tldlt.app.local.rpc;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.TestUtils;

/**
 * Type：DeviceActionTest
 * Descript：设备Action测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 下午2:10:36
 * Version：@version
 */
public class DeviceActionTest extends AppMockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("/rpc", "/rpc/deviceAction");
    }

    /**
     * 测试查询巡检记录成功.
     */
    @Test
    public void testQueryInspectRecordSuccess() {
        addRequestParameter("reqCode", new String[] {"queryInspectRecord"});
        addRequestParameter("deviceID", new String[] {"0000000001"});
        String actual = actionExecuteAndAssertRPCRetInfo();
        assertTrue(actual.contains("inspectplanid"));
    }

    /**
     * 测试查询设备信息成功.
     */
    @Test
    public void testQueryDeviceInfoSuccess() {
        addRequestParameter("reqCode", new String[] {"queryDeviceInfo"});
        addRequestParameter("deviceID", new String[] {"0000000001"});
        String actual = actionExecuteAndAssertRPCRetInfo();
        assertTrue(actual.contains("parentdeviceid"));
    }

    /**
     * 测试保存巡检计划GPS信息成功-巡检计划编号1.
     */
    @Test
    public void testSaveDeviceGPSInfo_Success_PlanId_1() {
        addRequestParameter("reqCode", new String[] {"saveDeviceGPSInfo"});
        addRequestParameter("planID", new String[] {"1"});
        addRequestParameter("deviceID", new String[] {"0000000001"});
        addRequestParameter("longtitude", new String[] {"118.850"});
        addRequestParameter("latitude", new String[] {"32.1031"});
        addRequestParameter("height", new String[] {"1"});
        addRequestParameter("speed", new String[] {"1"});
        addRequestParameter("datetime",
                new String[] {Long.toString(TestUtils.getCurrentUnixTime())});
        actionExecuteAndAssertRPCRetInfo();
    }

    /**
     * 测试保存巡检计划GPS信息成功-巡检计划编号1.
     */
    @Test
    public void testSaveDeviceGPSInfo_Success_PlanId_Null() {
        addRequestParameter("reqCode", new String[] {"saveDeviceGPSInfo"});
        addRequestParameter("deviceID", new String[] {"0000000001"});
        addRequestParameter("longtitude", new String[] {"118.850"});
        addRequestParameter("latitude", new String[] {"32.1031"});
        addRequestParameter("height", new String[] {"1"});
        addRequestParameter("speed", new String[] {"1"});
        addRequestParameter("datetime",
                new String[] {Long.toString(TestUtils.getCurrentUnixTime())});
        actionExecuteAndAssertRPCRetInfo();
    }

}
