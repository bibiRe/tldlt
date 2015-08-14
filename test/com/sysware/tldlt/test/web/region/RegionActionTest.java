package com.sysware.tldlt.test.web.region;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import utils.HttpServletResponseSimulator;
import utils.MockStrutsTestCase;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RegionActionTest  extends MockStrutsTestCase{

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
        String actual = ((HttpServletResponseSimulator)this.getResponse()).getWriterBuffer().toString();
        assertTrue(actual.contains("parentid")); 
    }    
}
