package com.sysware.tldlt.app.web.region;

import org.junit.Before;
import org.junit.Test;

import utils.AppMockStrutsTestCase;
import utils.mockstruts.HttpServletResponseSimulator;

/**
 * Type：RegionActionTest
 * Descript：RegionAction测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月24日 下午2:22:45
 * Version：@version
 */
public class RegionActionTest extends AppMockStrutsTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setRequestPathInfo("app", "/app/region");
    }

    /**
     * 测试删除区域信息-成功-编号9.
     */
    @Test
    public void testDeleteInfo_Success_id_9() {
        addRequestParameter("reqCode", new String[] {"delete"});
        addRequestParameter("ids", new String[] {"9"});
        addRequestParameter("regionid", new String[] {""});
        addRequestParameter("type", new String[] {"1"});
        actionExecuteAndAssertRetInfo();
    }

    @Test
    public void testInitSuccess() {
        addRequestParameter("reqCode", new String[] {"init"});
        actionPerform();
        verifyForward("regionView");
        verifyForwardPath("/app/region/regionview.jsp");
    }

    @Test
    public void testQueryRegionsForManageSuccess() {
        addRequestParameter("reqCode", new String[] {"queryRegionsForManage"});
        addRequestParameter("start", new String[] {"0"});
        addRequestParameter("limit", new String[] {"50"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("parentid"));
    }

    @Test
    public void testRegionTreeInitSuccess() {
        addRequestParameter("reqCode", new String[] {"regionTreeInit"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("parentid"));
    }
    
    @Test
    public void testSaveAddInfoSuccess() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("regionname", new String[] {"test"});
        addRequestParameter("parentid", new String[] {"3"});
        addRequestParameter("departmentid", new String[] {"001"});
        addRequestParameter("regiontype", new String[] {"2"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
    }
    
    @Test
    public void testSaveAddInfoSuccess_parentid_null() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("regionname", new String[] {"test"});
        addRequestParameter("departmentid", new String[] {"001"});
        addRequestParameter("regiontype", new String[] {"2"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
    }
    
    @Test
    public void testSaveAddInfoSuccess_parentid_empty() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("parentid", new String[] {""});
        addRequestParameter("regionname", new String[] {"test"});
        addRequestParameter("departmentid", new String[] {"001"});
        addRequestParameter("regiontype", new String[] {"2"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
    }
    
    @Test
    public void testSaveAddInfoSuccess_parentid_0() {
        addRequestParameter("reqCode", new String[] {"saveAddInfo"});
        addRequestParameter("parentid", new String[] {"0"});
        addRequestParameter("regionname", new String[] {"test"});
        addRequestParameter("departmentid", new String[] {"001"});
        addRequestParameter("regiontype", new String[] {"2"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
    }
    
    @Test
    public void testSaveUpdateInfoSuccess() {
        addRequestParameter("reqCode", new String[] {"saveUpdateInfo"});
        addRequestParameter("regionname", new String[] {"test1"});
        addRequestParameter("parentid", new String[] {"2"});
        addRequestParameter("departmentid", new String[] {"001"});
        addRequestParameter("regionid", new String[] {"7"});
        addRequestParameter("regiontype", new String[] {"1"});
        actionExecuteAndAssertRetInfo();
    }
}
