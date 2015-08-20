package com.sysware.tldlt.test.web.region;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import utils.HttpServletResponseSimulator;
import utils.MockStrutsTestCase;

public class RegionActionTest extends MockStrutsTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setContextDirectory(new File("webapp"));
        setConfigFile("app", "/WEB-INF/struts-config-app.xml");
        setRequestPathInfo("app", "/app/region");
    }

    @Test
    public void testInitSuccess() {
        addRequestParameter("reqCode", new String[] {"init"});
        actionPerform();
        verifyForward("regionView");
        verifyForwardPath("/app/region/regionview.jsp");
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
    public void testSaveUpdateInfoSuccess() {
        addRequestParameter("reqCode", new String[] {"saveUpdateInfo"});
        addRequestParameter("regionname", new String[] {"test1"});
        addRequestParameter("parentid", new String[] {"2"});
        addRequestParameter("departmentid", new String[] {"001"});
        addRequestParameter("regionid", new String[] {"7"});
        addRequestParameter("regiontype", new String[] {"1"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
    }
    
    @Test
    public void testDeleteInfoSuccess() {
        addRequestParameter("reqCode", new String[] {"delete"});
        addRequestParameter("ids", new String[] {"9"});
        addRequestParameter("regionid", new String[] {""});
        addRequestParameter("type", new String[] {"1"});
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
    }
}
