package utils;

import java.io.File;

import junit.framework.AssertionFailedError;

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
     * 校验路径.
     * @param forwardName forward名称
     * @param forwardPath forward路径
     * @throws AssertionFailedError assert异常.
     */
    protected void actionExecuteAndVerifyForward(String forwardName,
            String forwardPath) throws AssertionFailedError {
        actionPerform();
        verifyForward(forwardName);
        verifyForwardPath(forwardPath);
    }

    /**
     * 得到Action执行Response返回字符串.
     * @return 字符串
     */
    public String getActionExecuteResponseString() {
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
        request.setHeader("USER-AGENT", "IE9");
        setContextDirectory(new File("webapp"));
        setConfigFile("app", "/WEB-INF/struts-config-app.xml");
        setConfigFile("rpc", "/WEB-INF/struts-config-rpc.xml");
    }

}
