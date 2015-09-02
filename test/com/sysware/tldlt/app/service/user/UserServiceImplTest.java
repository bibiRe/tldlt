package com.sysware.tldlt.app.service.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Test;
import org.mockito.Mockito;

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
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.reportDeviceStatus(dto);
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
        Mockito.when(appDao.queryForObject("App.Device.queryDeviceInfo", deviceId))
        .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.reportDeviceStatus(dto);
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
        Mockito.doNothing().when(appDao).insert("App.User.reportDeviceStatus", dto);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.reportDeviceStatus(dto);
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

}
