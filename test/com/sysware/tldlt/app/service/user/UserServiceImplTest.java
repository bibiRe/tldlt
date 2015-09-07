package com.sysware.tldlt.app.service.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import utils.BaseAppServiceImplTest;
import utils.TestUtils;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;

/**
 * Type：UserServiceImplTest
 * Descript：用户服务测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 上午10:00:56
 * Version：@version
 */
public class UserServiceImplTest extends BaseAppServiceImplTest {

    /**
     * 设备建议信息编号.
     */
    private static final int DEVICE_SUGGEST_INFO_ID = 1;
    /**
     * 用户服务实现对象.
     */
    private UserServiceImpl userServiceImpl;

    @Override
    protected BaseAppServiceImpl createService() {
        userServiceImpl = new UserServiceImpl();
        return userServiceImpl;
    }

    /**
     * 创建用户GPS Dto对象.
     * @param userid 用户编号
     * @return Dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto createUserGPSDto(String userid) {
        Dto dto = new BaseDto();
        dto.put("userid", userid);
        TestUtils.setGPSDto(dto);
        return dto;
    }

    /**
     * 创建用户GPS Dto对象.
     * @param userid 用户编号
     * @return Dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto createDeviceStatusDto(String userid) {
        Dto dto = new BaseDto();
        dto.put("userid", userid);
        dto.put("deviceID", "0000000002");
        dto.put("devicedesc", "测试");
        dto.put("datetime", TestUtils.getCurrentUnixTime());
        return dto;
    }

    /**
     * 测试保存用户GPS信息成功.
     */
    @Test
    public void testSaveGPSInfo_Fail_UserId_10003333_Invalid() {
        String userid = "10003333";
        Dto dto = createUserGPSDto(userid);
        Mockito.doNothing().when(appDao).insert("App.User.saveGPSInfo", dto);
        Mockito.when(g4Dao.queryForObject("User.getUserInfoByKey", dto))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试上报设备状态失败-用户编号1000334无效.
     */
    @Test
    public void testReportDeviceStatus_Fail_UserId_10003334_Invalid() {
        String userid = "10003334";
        Dto dto = createDeviceStatusDto(userid);
        Mockito.when(g4Dao.queryForObject("User.getUserInfoByKey", dto))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl
                .reportDeviceStatus(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试上报设备状态失败-设备编号0001无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testReportDeviceStatus_Fail_DeviceId_0001_Invalid() {
        String userid = "10003334";
        Dto dto = createDeviceStatusDto(userid);
        String deviceId = "0001";
        dto.put("deviceID", deviceId);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, userid);
        Mockito.when(
                appDao.queryForObject("App.Device.queryDeviceInfo", deviceId))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl
                .reportDeviceStatus(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试上报设备状态失败-设备编号0001无效.
     */
    @Test
    public void testReportDeviceStatus_Success() {
        String userid = "10003334";
        Dto dto = createDeviceStatusDto(userid);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, userid);
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        Mockito.doNothing().when(appDao)
                .insert("App.User.addDeviceSuggestInfo", dto);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl
                .reportDeviceStatus(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试保存用户GPS信息成功.
     */
    @Test
    public void testSaveGPSInfoSuccess() {
        String userid = "10004894";
        Dto dto = createUserGPSDto(userid);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, userid);
        Mockito.doNothing().when(appDao).insert("App.User.saveGPSInfo", dto);
        Mockito.doNothing().when(appDao)
                .insert("App.User.saveReleateGPSInfo", dto);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试上传媒体失败-信息编号为2无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public
            void
            testSaveUploadDeviceStatusMedia_Fail_DeviceSuggestInfoId_2_Invalid() {
        Dto dto = new BaseDto();
        dto.put("devicesuggestinfoid", 2);
        Mockito.when(
                appDao.queryForObject("App.User.queryDeviceSuggestInfoById",
                        dto.getAsInteger("devicesuggestinfoid"))).thenReturn(
                null);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl
                .saveUploadDeviceStatusMedia(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试上传巡检记录媒体成功.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveUploadDeviceStatusMedia_ImageList_Null() {
        Dto dto = new BaseDto();
        dto.put("devicesuggestinfoid", DEVICE_SUGGEST_INFO_ID);
        dto.put("images", null);
        Dto inspectRecordInfoDto = new BaseDto();
        inspectRecordInfoDto.put("devicesuggestinfoid",
                dto.getAsInteger("devicesuggestinfoid").intValue());
        Mockito.when(
                appDao.queryForObject("App.User.queryDeviceSuggestInfoById",
                        dto.getAsInteger("devicesuggestinfoid"))).thenReturn(
                inspectRecordInfoDto);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl
                .saveUploadDeviceStatusMedia(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试上传巡检记录媒体成功.
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    @Test
    public void testSaveUploadDeviceStatusMedia_Success() throws Exception {
        Dto dto = new BaseDto();
        dto.put("devicesuggestinfoid", DEVICE_SUGGEST_INFO_ID);
        dto.put("datetime", TestUtils.getCurrentUnixTime());
        List<Dto> fileList = TestUtils.createFileDtoList(dto);
        Dto inspectRecordInfoDto = new BaseDto();
        inspectRecordInfoDto.put("devicesuggestinfoid",
                dto.getAsInteger("devicesuggestinfoid").intValue());
        Mockito.when(
                appDao.queryForObject("App.User.queryDeviceSuggestInfoById",
                        dto.getAsInteger("devicesuggestinfoid"))).thenReturn(
                inspectRecordInfoDto);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("mediainfoid", 1);
                return null;
            }
        }).when(appDao).insert("App.Media.addInfo", fileList.get(0));
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("releatemediaid", 1);
                return null;
            }
        }).when(appDao)
                .insert("App.User.addReleateMediaInfo", fileList.get(0));
        BaseRetDto outDto = (BaseRetDto) userServiceImpl
                .saveUploadDeviceStatusMedia(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }
}
