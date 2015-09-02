package com.sysware.tldlt.app.local.rpc;

import org.g4studio.system.common.util.SystemConstants;
import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.TestCommon;
import utils.TestUtils;

/**
 * Type：InspectActionTest
 * Descript：巡检Action测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 下午2:13:36
 * Version：@version
 */
public class InspectActionTest extends AppMockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("/rpc", "/rpc/inspectAction");
    }

    /**
     * 测试保存巡检记录失败-Key为空.
     */
    @Test
    public void testSaveInspectRecordInfo_Fail_KeyEmpty() {
        addRequestParameter("reqCode", new String[] {"saveInspectRecordInfo"});
        addRequestParameter("key", new String[] {""});
        actionExecuteAndAssertRPCRetInfoError();
    }

    /**
     * 测试保存巡检记录失败-Key不存在.
     */
    @Test
    public void testSaveInspectRecordInfo_KeyNotExist() {
        addRequestParameter("reqCode", new String[] {"saveInspectRecordInfo"});
        actionExecuteAndAssertRPCRetInfoError();
    }

    /**
     * 测试保存巡检记录失败-Key非法.
     */
    @Test
    public void testSaveInspectRecordInfo_Key_1111_Invalid() {
        TestUtils.loginUser();
        addRequestParameter("reqCode", new String[] {"saveInspectRecordInfo"});
        addRequestParameter("key", new String[] {"1111"});
        actionExecuteAndAssertRPCRetInfoError();
    }

    /**
     * 测试保存巡检记录成功.
     */
    @Test
    public void testSaveInspectRecordInfoSuccess() {
        String key = TestUtils.loginUser();
        addRequestParameter("reqCode", new String[] {"saveInspectRecordInfo"});
        addRequestParameter("key", new String[] {key});
        addRequestParameter("deviceID", new String[] {"0000000001"});
        addRequestParameter("isOK", new String[] {SystemConstants.ENABLED_Y});
        long checktime = TestUtils.getCurrentUnixTime() - 10
                * TestCommon.DAY_SEC;
        addRequestParameter("checktime",
                new String[] {Long.toString(checktime)});
        actionExecuteAndAssertRPCRetInfoSuccess();
    }

    /**
     * 测试保存巡检记录成功.
     */
    @Test
    public void testUploadInspectRecordMediaSuccess() {
        String key = TestUtils.loginUser();
        addRequestParameter("reqCode", new String[] {"uploadInspectRecordMedia"});
        addRequestParameter("key", new String[] {key});
        addRequestParameter("inspectrecordinfoid", new String[] {"1"});
        actionExecuteAndAssertRPCRetInfoSuccess();
    }
    
    
}
