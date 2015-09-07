package utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.codehaus.plexus.util.FileUtils;
import org.g4studio.common.dao.Dao;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.mvc.xstruts.upload.FormFile;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.local.rpc.RPCUserManage;

/**
 * Type：TestUtils
 * Descript：测试工具类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月25日 下午5:27:20
 * Version：@version
 */
public class TestUtils {
    /**
     * 判断RPC返回类字符串失败.
     * @param actual 字符串
     */
    public static void assertRPCRetInfoError(String actual) {
        assertTrue(actual.contains("\"success\":\"0\""));
    }

    /**
     * 判断RPC返回类字符串成功.
     * @param actual 字符串
     */
    public static void assertRPCRetInfoSuccess(String actual) {
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

    /**
     * 得到当前时间的unix值字符串.
     * @return unix时间值字符串
     */
    public static String getCurrentUnixTimeStr() {
        return Long.toString(getCurrentUnixTime());
    }

    /**
     * 登录用户编号.
     * @return 用户编号
     */
    public static String loginUser() {
        String key = "key-10004893";
        RPCUserManage.loginUser("10004893", key);
        return key;
    }

    /**
     * mock查询设备信息.
     * @param deviceId 设备编号
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    public static Dto mockQueryDeviceInfo(Dao dao, String deviceId) {
        Dto deviceDto = new BaseDto();
        deviceDto.put("deviceID", deviceId);
        deviceDto.put("parentDeviceId", null);
        deviceDto.put("devicename", "测试设备");
        Mockito.when(dao.queryForObject("App.Device.queryDeviceInfo", deviceId))
                .thenReturn(deviceDto);
        return deviceDto;
    }

    /**
     * mock 查询用户.
     * @param dao dao对象
     * @param dto dto对象
     * @param userid 用户编号
     * @return dto
     */
    @SuppressWarnings("unchecked")
    public static Dto mockQueryUserByUserId(Dao dao, Dto dto, String userid) {
        Dto userDto = new BaseDto();
        userDto.put("userid", userid);
        userDto.put("username", "张三");
        userDto.put("account", "zs");
        Mockito.when(dao.queryForObject("User.getUserInfoByKey", dto))
                .thenReturn(userDto);
        return userDto;
    }

    /**
     * 设置Dto GPS信息.
     * @param dto dto信息.
     */
    @SuppressWarnings("unchecked")
    public static void setGPSDto(Dto dto) {
        dto.put("longtitude", 118.850);
        dto.put("latitude", 32.1031);
        dto.put("height", 1);
        dto.put("speed", 1);
        dto.put("datetime", getCurrentUnixTime());
    }

    /**
     * 创建测试FileDto列表.
     * @param dto 对象.
     * @return 文件列表
     * @throws FileNotFoundException 文件没找到异常.
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    public static List<Dto> createFileDtoList(Dto dto) throws FileNotFoundException,
            IOException {
        List<Dto> fileList = Lists.newArrayList();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        String uploadFilePath = "E:/";
        factory.setRepository(new File(uploadFilePath)); // 设置临时目录
        factory.setSizeThreshold(FileUtils.ONE_KB * 8); // 8k的缓冲区,文件大于8K则保存到临时目录
        fileList.add(createFileDto(factory, "img1", "1.jpg", "e:/1.jpg"));
        dto.put("medias", fileList);
        return fileList;
    }

    /**
     * 创建文件Dto.
     * @param factory 文件条目工厂.
     * @param fieldName 字段名
     * @param fileName 文件名
     * @param realFileName 实际文件名.
     * @return 文件dto对象
     * @throws FileNotFoundException 文件未找到异常.
     * @throws IOException IO异常.
     */
    @SuppressWarnings("unchecked")
    private static Dto createFileDto(DiskFileItemFactory factory,
            String fieldName, String fileName, String realFileName)
            throws FileNotFoundException, IOException {
        FileItem fileItem = createFileItem(factory, fieldName, fileName,
                realFileName);
        FormFile formFile = new TestFormFile(fileItem);
        Dto fileDto = new BaseDto();
        fileDto.put("file", formFile);
        return fileDto;
    }

    /**
     * 创建文件条目对象.
     * @param factory 文件条目工厂.
     * @param fieldName 字段名
     * @param fileName 文件名
     * @param realFileName 实际文件名.
     * @return 文件条目对象
     * @throws FileNotFoundException 文件未找到异常.
     * @throws IOException IO异常.
     */
    private static FileItem createFileItem(DiskFileItemFactory factory,
            String fieldName, String fileName, String realFileName)
            throws FileNotFoundException, IOException {
        FileItem fileItem = factory.createItem(fieldName, "UTF-8", false,
                fileName);
        FileInputStream inStream = new FileInputStream(realFileName);
        byte[] inOutb = new byte[inStream.available()];
        inStream.read(inOutb); // 读入流,保存在byte数组
        fileItem.getOutputStream().write(inOutb); // 写出流,保存在文件newFace.gif中
        inStream.close();
        fileItem.getOutputStream().close();
        return fileItem;
    }
}
