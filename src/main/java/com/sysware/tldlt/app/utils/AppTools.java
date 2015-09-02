package com.sysware.tldlt.app.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.plexus.util.FileUtils;
import org.g4studio.core.properties.PropertiesFactory;
import org.g4studio.core.properties.PropertiesFile;
import org.g4studio.core.properties.PropertiesHelper;
import org.g4studio.core.util.G4Constants;
import org.g4studio.core.util.G4Utils;

/**
 * Type：AppTools
 * Descript：工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月14日 下午3:48:25
 * Version：@version
 */
public class AppTools {

    /**
     * 转换NULL字符串为空字符串.
     * @param value 值
     * @return 字符串
     */
    public static String convertNullStr(String value) {
        if (null == value) {
            return "";
        } else {
            return value;
        }
    }

    /**
     * 判断字符串为空.
     * @param value 字符串
     * @return 是否为空
     */
    public static boolean isEmptyString(String value) {
        return (null == value || value.equals(""));
    }

    /**
     * unix时间转换为时间字符串.
     * @param unixTime unix时间.
     * @return 时间字符串
     */
    public static String unixTime2DateStr(long unixTime) {
        Date dt = new Date();
        dt.setTime(unixTime * AppCommon.TIME_INTERVAL);
        return G4Utils.Date2String(dt, G4Constants.FORMAT_DateTime);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss 时间字符串改为unix时间值.
     * @param unixTime unix时间.
     * @return 时间字符串
     */
    public static long dateTimeStr2UnixTime(String dateTime) {
        long result = 0;
        Date dt;
        try {
            dt = (new SimpleDateFormat(G4Constants.FORMAT_DateTime))
                    .parse(dateTime);
            result = dt.getTime() / AppCommon.TIME_INTERVAL;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到App 属性值.
     * @param key 键值
     * @param defaultValue 缺省值.
     * @return 属性值
     */
    public static String getAppPropertyValue(String key, String defaultValue) {
        PropertiesHelper pHelper = PropertiesFactory
                .getPropertiesHelper(PropertiesFile.APP);
        return pHelper.getValue(key, defaultValue);
    }

    /**
     * 创建文件MD5校验值.
     * @param filename 文件名
     * @return 校验值数组.
     * @throws IOException IO异常
     * @throws NoSuchAlgorithmException 无此算法异常
     */
    private static byte[] createFileMD5Checksum(String filename)
            throws NoSuchAlgorithmException, IOException {
        InputStream fis = new FileInputStream(filename);
        return create5InStreamMD5CheckSum(fis);
    }

    /**
     * 创建流MD5校验值.
     * @param inStream 输入流.
     * @return MD5校验值
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws IOException IO异常
     */
    private static byte[] create5InStreamMD5CheckSum(InputStream inStream)
            throws NoSuchAlgorithmException, IOException {
        byte[] buffer = new byte[FileUtils.ONE_KB];
        MessageDigest complete = MessageDigest.getInstance("MD5"); // 如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
        int numRead;

        do {
            numRead = inStream.read(buffer); // 从文件读到buffer，最多装满buffer
            if (numRead > 0) {
                complete.update(buffer, 0, numRead); // 用读到的字节进行MD5的计算，第二个参数是偏移量
            }
        } while (numRead != -1);
        inStream.close();
        return complete.digest();
    }

    /**
     * 得到文件MD5校验值.
     * @param fileName 文件名
     * @return MD5校验值
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws IOException IO异常
     * @throws Exception 异常
     */
    public static String getFileMD5CheckSum(String fileName)
            throws NoSuchAlgorithmException, IOException {
        byte[] b = createFileMD5Checksum(fileName);
        return getCheckSumBytesStr(b);
    }

    /**
     * 得到文件MD5校验值.
     * @param fileName 文件名
     * @return MD5校验值
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws IOException IO异常
     * @throws Exception 异常
     */
    public static String getInStreamMD5CheckSum(InputStream inStream)
            throws NoSuchAlgorithmException, IOException {
        byte[] b = create5InStreamMD5CheckSum(inStream);
        return getCheckSumBytesStr(b);
    }

    /**
     * 得到校验值数组字符串.
     * @param data 数据
     * @return 字符串
     */
    private static String getCheckSumBytesStr(byte[] data) {
        StringBuilder strB = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            strB.append(Integer.toString((data[i] & 0xff) + 0x100, 16)
                    .substring(1));// 加0x100是因为有的b[i]的十六进制只有1位
        }
        return strB.toString();
    }
}
