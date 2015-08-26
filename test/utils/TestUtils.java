package utils;

import static org.junit.Assert.*;

import java.util.Date;

import org.g4studio.core.metatype.Dto;

/**
 * Type：TestUtils
 * Descript：测试工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月25日 下午5:27:20
 * Version：@version
 */
public class TestUtils {
    /**
     * 判断RPC返回类字符串.
     * @param actual 字符串
     */
    public static void assertRPCRetInfo(String actual) {
        assertTrue(actual.contains("\"success\":\"1\""));
    }

    /**
     * 得到当前时间的unix值.
     * @return unix时间值
     */
    public static long getCurrentUnixTime() {
        long unixTime = new Date().getTime() / 1000;
        return unixTime;
    }

    public static void setGPSDto(Dto dto) {
        dto.put("longtitude", 118.850);
        dto.put("latitude", 32.1031);
        dto.put("height", 1);
        dto.put("speed", 1);
        dto.put("datetime", getCurrentUnixTime());
    }
}
