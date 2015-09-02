package com.sysware.tldlt.test.web.devicetype;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.mockstruts.HttpServletResponseSimulator;

/**
 * Type：DeviceTypeActionTest
 * Descript：设备类型Action测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月24日 下午3:57:16
 * Version：@version
 */
public class DeviceTypeActionTest extends AppMockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("app", "/app/deviceType");
    }

    /**
     * 测试初始化成功.
     */
    @Test
    public void testInit_Success() {
        addRequestParameter("reqCode", new String[] {"init"});
        actionPerform();
        verifyForward("deviceTypeView");
        verifyForwardPath("/app/devicetype/devicetypeview.jsp");
    }
    
    /**
     * 测试删除设备类型信息-成功-编号9.
     */
    @Test
    public void testDeleteInfo_Success_id_9() {
        addRequestParameter("reqCode", new String[] {"delete"});
        addRequestParameter("ids", new String[] {"9"});
        addRequestParameter("devicetypeid", new String[] {""});
        addRequestParameter("type", new String[] {"1"});
        actionExecuteAndAssertRetInfo();
    }
    
    /**
     * 测试初始化Tree
     */
    @Test
    public void testDeviceTypeTreeInit_Success() {
        addRequestParameter("reqCode", new String[] {"deviceTypeTreeInit"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("parentid"));
    }

    /**
     * 测试初始化Tree
     */
    @Test
    public void testDeviceTypeTreeInit_Success_Node_0() {
        addRequestParameter("reqCode", new String[] {"deviceTypeTreeInit"});
        addRequestParameter("node", new String[] {"0"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("parentid"));
    }
    
    /**
     * 测试查询设备类型管理数据-成功.
     */
    @Test
    public void testQueryDeviceTypesForManage_Success() {
        addRequestParameter("reqCode", new String[] {"queryDeviceTypesForManage"});
        addRequestParameter("start", new String[] {"0"});
        addRequestParameter("limit", new String[] {"50"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("parentid"));
    }

    /**
     * 测试新增成功-编号111.
     */
    @Test
    public void testSaveAddInfo_Success() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("devicetypeid", new String[] {"111"});
        addRequestParameter("devicetypename", new String[] {"test"});
        addRequestParameter("parentid", new String[] {""});
        actionExecuteAndAssertRetInfo();
    }

    /**
     * 测试新增成功-编号112.
     */
    @Test
    public void testSaveAddInfo_Success_Id112_parentid_0() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("devicetypeid", new String[] {"112"});
        addRequestParameter("devicetypename", new String[] {"test"});
        addRequestParameter("parentid", new String[] {"0"});
        actionExecuteAndAssertRetInfo();
    }
    
    /**
     * 测试更新成功-编号111.
     */
    @Test
    public void testSaveUpdateInfo_Success_Id_10() {
        addRequestParameter("reqCode", new String[] {"saveUpdateInfo"});
        addRequestParameter("devicetypename", new String[] {"test1"});
        addRequestParameter("parentid", new String[] {"1"});
        addRequestParameter("devicetypeid", new String[] {"10"});
        actionExecuteAndAssertRetInfo();
    }
    
   
    /**
     * 测试删除成功-编号111.
     */
    @Test
    public void testDeleteInfo_Success_Id_111() {
        addRequestParameter("reqCode", new String[] {"delete"});
        addRequestParameter("ids", new String[] {"111"});
        actionExecuteAndAssertRetInfo();
    }
}
