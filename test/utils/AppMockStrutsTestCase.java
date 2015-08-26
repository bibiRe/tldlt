package utils;

import java.io.File;

import org.junit.Before;

/**
 * Type：AppMockStrutsTestCase
 * Descript：应用 MockStrutsTestCase类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月24日 下午2:23:12
 * Version：@version
 */
public class AppMockStrutsTestCase extends MockStrutsTestCase {
    /**
     * 配置.
     */
    @Before
    protected void setUp() throws Exception {
        super.setUp();
        setContextDirectory(new File("webapp"));
        setConfigFile("app", "/WEB-INF/struts-config-app.xml");
        setConfigFile("rpc", "/WEB-INF/struts-config-rpc.xml");
    }

    /**
     * 判断RPC返回信息.
     * @return 返回信息字符串.
     */
    protected String actionExecuteAndAssertRPCRetInfo() {
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        TestUtils.assertRPCRetInfo(actual);
        return actual;
    }

    /**
     * 判断Ret返回信息.
     * @return 返回信息字符串.
     */
    protected String actionExecuteAndAssertRetInfo() {
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        assertTrue(actual.contains("\"retCode\":0"));
        return actual;
    }
}
