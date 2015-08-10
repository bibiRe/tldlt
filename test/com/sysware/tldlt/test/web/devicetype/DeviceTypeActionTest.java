package com.sysware.tldlt.test.web.devicetype;

import java.io.File;

import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.metatype.Dto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.MockStrutsTestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeviceTypeActionTest extends MockStrutsTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setContextDirectory(new File("webapp"));
        setConfigFile("app", "/WEB-INF/struts-config-app.xml");
        setRequestPathInfo("app", "/app/deviceType");
    }

    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testInitSuccess() {
        addRequestParameter("reqCode", new String[] {"init"});
        actionPerform();
        verifyForward("deviceTypeView");
        verifyForwardPath("/app/devicetype/devicetypeview.jsp");
    }

    @Test
    public void testQueryFull() {
        addRequestParameter("reqCode", new String[] {"query"});
        actionPerform();
        verifyForward(null);
        Dto dto = JsonHelper.parseSingleJson2Dto(getMockResponse()
                .getWriterBuffer().toString());
        assertThat(dto.getAsInteger("TOTALCOUNT"), notNullValue());
    }

    @Test
    public void testQuery_deviceTypeId_is_1() {
        addRequestParameter("reqCode", new String[] {"query"});
        addRequestParameter("deviceTypeId", new String[] {"1"});
        actionPerform();
        verifyForward(null);
        Dto dto = JsonHelper.parseSingleJson2Dto(getMockResponse()
                .getWriterBuffer().toString());
        assertThat(dto.getAsInteger("TOTALCOUNT"), notNullValue());
        assertThat(dto.getAsInteger("TOTALCOUNT").intValue(), is(1));
    }
}
