package com.sysware.tldlt.app.web.devicetypecheckitem;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.mockstruts.HttpServletResponseSimulator;
/**
 * Type：DeviceTypeCheckItemActionTest
 * Descript：设备类型检查项Action测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月24日 下午3:57:16
 * Version：@version
 */
public class DeviceTypeCheckItemActionTest extends AppMockStrutsTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("app", "/app/deviceTypeCheckItem");
    }

    /**
     * 测试查询设备检查项管理数据-成功.
     */
    @Test
    public void testQueryCheckItemsForManage_Success() {
        addRequestParameter("reqCode", new String[] {"queryCheckItemsForManage"});
        addRequestParameter("devicetypeid", new String[] {"DC/AC Screen"});
        addRequestParameter("start", new String[] {"0"});
        addRequestParameter("limit", new String[] {"50"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("devicecheckcontentid"));
    }
    /**
     * 测试查询设备检查项管理数据-成功.
     */
    @Test
    public void testSaveAddInfo_Success() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("devicetypeid", new String[] {"DC/AC Screen"});
        addRequestParameter("action", new String[] {"1"});
        addRequestParameter("state", new String[] {"1"});
        addRequestParameter("checkitemname", new String[] {"test"});
        addRequestParameter("content", new String[] {"content"});
        addRequestParameter("remark", new String[] {"remark"});
        String actual = actionExecuteAndAssertRetInfo();
        assertTrue(actual.contains("devicecheckcontentid"));
    }

}
