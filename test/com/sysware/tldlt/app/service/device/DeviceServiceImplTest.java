package com.sysware.tldlt.app.service.device;

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
 * Type：DeviceServiceImplTest
 * Descript：设备服务测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月26日 下午4:34:40
 * Version：@version
 */
public class DeviceServiceImplTest extends BaseAppServiceImplTest {

    /**
     * 设备服务实现类对象.
     */
    private DeviceServiceImpl deviceServiceImpl;

    /**
     * 创建设备GPS Dto对象.
     * @return Dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto createDeviceGPSDto() {
        Dto dto = new BaseDto();
        int planId = 1;
        String deviceId = "0000000001";
        dto.put("planID", planId);
        dto.put("deviceID", deviceId);
        TestUtils.setGPSDto(dto);
        return dto;
    }

    @Override
    protected BaseAppServiceImpl createService() {
        deviceServiceImpl = new DeviceServiceImpl();
        return deviceServiceImpl;
    }

    /**
     * mock查询巡检计划信息.
     * @param dto 巡检计划
     */
    @SuppressWarnings("unchecked")
    private void mockQueryInspectPlanDevice(Dto dto) {
        Dto inspectPlanDto = new BaseDto();
        inspectPlanDto.put("planID", dto.getAsInteger("planID"));
        inspectPlanDto.put("inspectplandeviceid", 1);
        Mockito.when(
                appDao.queryForObject(
                        "App.InspectPlan.queryPlanDeviceByPlanIdAndDeviceId", dto))
                .thenReturn(inspectPlanDto);
    }

    /**
     * 测试保存设备GPS信息成功-巡检计划编号000000001.
     */
    @Test
    public void testSaveDeviceGPSInfo_Success_PlanID_0000000001() {
        Dto dto = createDeviceGPSDto();
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanDevice(dto);
        Mockito.doNothing().when(appDao).insert("App.User.saveGPSInfo", dto);
        Mockito.doNothing().when(appDao)
                .insert("App.Device.saveReleateGPSInfo", dto);
        BaseRetDto outDto = (BaseRetDto) deviceServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试保存设备GPS信息成功-巡检计划编号.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveDeviceGPSInfo_Success_PlanID_Null() {
        Dto dto = createDeviceGPSDto();
        dto.put("planID", null);
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanDevice(dto);
        Mockito.doNothing().when(appDao).insert("App.User.saveGPSInfo", dto);
        Mockito.doNothing().when(appDao)
                .insert("App.Device.saveReleateGPSInfo", dto);
        BaseRetDto outDto = (BaseRetDto) deviceServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试保存设备GPS信息失败-巡检计划编号1-设备编号-000001不存在.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveDeviceGPSInfo_Fail_PlanID_1_DeviceID_000001_NotExist() {
        Dto dto = createDeviceGPSDto();
        dto.put("planID", 1);
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        Mockito.when(
                appDao.queryForObject(
                        "App.InspectPlan.queryPlanDeviceByPlanIdAndDeviceId", dto))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }
}
