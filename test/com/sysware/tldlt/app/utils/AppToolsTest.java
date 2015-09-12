package com.sysware.tldlt.app.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class AppToolsTest {

    /**
     * 测试得到文件MD5校验值成功.
     * @throws Exception
     */
    @Test
    public void testGetFileMD5CheckSum_Success() throws Exception {
        String expected = "4189db05e5fc5b7ff53d0e9519f63456";
        assertThat(
                AppTools.getFileMD5CheckSum("e:/media/2015-09-08/20150908040814.JPEG"),
                is(expected));
    }

}
