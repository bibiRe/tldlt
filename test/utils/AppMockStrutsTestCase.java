package utils;

import java.io.File;

import org.junit.Before;

import utils.mockstruts.HttpServletResponseSimulator;
import utils.mockstruts.MockStrutsTestCase;

/**
 * Type：AppMockStrutsTestCase
 * Descript：应用 MockStrutsTestCase类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月24日 下午2:23:12
 * Version：@version
 */
public class AppMockStrutsTestCase extends MockStrutsTestCase {
    /**
     * 判断Ret返回信息.
     * @return 返回信息字符串.
     */
    protected String actionExecuteAndAssertRetInfo() {
        String actual = getActionExecuteResponseString();
        assertTrue(actual.contains("\"retCode\":0"));
        return actual;
    }

    /**
     * 判断RPC返回信息失败.
     * @return 返回信息字符串.
     */
    protected String actionExecuteAndAssertRPCRetInfoError() {
        String actual = getActionExecuteResponseString();
        TestUtils.assertRPCRetInfoError(actual);
        return actual;
    }

    /**
     * 判断RPC返回信息成功.
     * @return 返回信息字符串.
     */
    protected String actionExecuteAndAssertRPCRetInfoSuccess() {
        String actual = getActionExecuteResponseString();
        TestUtils.assertRPCRetInfoSuccess(actual);
        return actual;
    }

    /**
     * 得到Action执行Response返回字符串.
     * @return 字符串
     */
    private String getActionExecuteResponseString() {
        actionPerform();
        String actual = ((HttpServletResponseSimulator) this.getResponse())
                .getWriterBuffer().toString();
        return actual;
    }

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
}
