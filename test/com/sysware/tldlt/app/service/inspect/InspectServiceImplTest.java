package com.sysware.tldlt.app.service.inspect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.system.common.util.SystemConstants;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import utils.BaseAppServiceImplTest;
import utils.TestUtils;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;

/**
 * Type：InspectServiceImplTest
 * Descript：巡检服务实现测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月27日 下午2:39:24
 * Version：@version
 */
public class InspectServiceImplTest extends BaseAppServiceImplTest {
    /**
     * 巡检计划设备信息编号.
     */
    private static int INSPECT_PLAN_DEVICE_ID = 1;
    /**
     * 巡检计划信息编号.
     */
    private static int INSPECT_PLAN_ID = 2;
    /**
     * 巡检记录编号.
     */
    private static int INSPECT_RECORD_ID = 1;

    /**
     * 巡检记录信息编号.
     */
    private static int INSPECT_RECORD_INFO_ID = 1;
    /**
     * 一天秒数.
     */
    private static int DAY_SEC = 86400;
    /**
     * 巡检服务对象.
     */
    private InspectServiceImpl inspectServiceImpl;

    /**
     * 创建Dto对象.
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto createDto() {
        Dto dto = new BaseDto();
        dto.put("userid", "10004894");
        dto.put("deviceID", "0000000001");
        dto.put("isOK", SystemConstants.ENABLED_Y);
        dto.put("checktime", TestUtils.getCurrentUnixTime());
        dto.put("planID", INSPECT_PLAN_ID);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private Dto createDtoInspectPlanDeviceDto(Dto dto, Integer pid) {
        Dto planDeviceDto = new BaseDto();
        planDeviceDto.put("inpsectplandeviceid", INSPECT_RECORD_INFO_ID);
        planDeviceDto.put("deviceid", dto.getAsString("deviceID"));
        planDeviceDto.put("planid", null != pid ? pid.intValue() : null);
        return planDeviceDto;
    }

    @Override
    protected BaseAppServiceImpl createService() {
        inspectServiceImpl = new InspectServiceImpl();
        return inspectServiceImpl;
    }

    /**
     * mock 查询巡检计划设备信息.
     * @param dto dto对象
     * @return dto对象
     */
    private Dto mockQueryInspectPlanDeviceInfo(Dto dto) {
        Dto planDeviceDto = createDtoInspectPlanDeviceDto(dto, dto
                .getAsInteger("planID").intValue());

        Mockito.when(
                appDao.queryForObject(
                        "App.InspectPlan.queryPlanDeviceByPlanIdAndDeviceId",
                        dto)).thenReturn(planDeviceDto);
        return planDeviceDto;
    }

    /**
     * mock 查询巡检计划编号.
     * @param planId 计划编号
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto mockQueryInspectPlanInfo(int planId) {
        Dto planDto = new BaseDto();
        planDto.put("planID", planId);
        planDto.put("executestarttime", TestUtils.getCurrentUnixTime()
                - DAY_SEC * 10);
        planDto.put("executeendtime", TestUtils.getCurrentUnixTime() + DAY_SEC
                * 10);
        Mockito.when(
                appDao.queryForObject("App.InspectPlan.queryPlanById", planId))
                .thenReturn(planDto);
        return planDto;
    }

    /**
     * mock 查询巡检记录信息.
     * @param planDeviceId 巡检计划设备编号.
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto mockQueryInspectRecordInfoByPlanDeviceId(int planDeviceId) {
        Dto inspectRecordInfoDto = new BaseDto();
        inspectRecordInfoDto.put("inspectrecordinfoid", INSPECT_RECORD_INFO_ID);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                        planDeviceId)).thenReturn(inspectRecordInfoDto);
        return inspectRecordInfoDto;
    }

    /**
     * 测试增加信息失败-巡检时间为空.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_CheckTime_Null() {
        Dto dto = createDto();
        dto.put("checktime", null);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试增加信息失败-巡检时间为空.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_PlanID_1_CheckTime_OverStep() {
        Dto dto = createDto();
        dto.put("checktime", TestUtils.getCurrentUnixTime() - DAY_SEC * 100);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanInfo(dto.getAsInteger("planID").intValue());
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_OVER_STEP));
    }

    /**
     * 测试增加信息失败-用户编号0000001_巡检记录信息已存在.
     */
    @Test
    public void testAddInfo_Fail_DeviceID_00000001_InspectRecordInfo_Exist() {
        Dto dto = createDto();
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanInfo(dto.getAsInteger("planID").intValue());
        mockQueryInspectPlanDeviceInfo(dto);
        mockQueryInspectRecordInfoByPlanDeviceId(INSPECT_PLAN_DEVICE_ID);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_REPEAT_VALUE));
    }

    /**
     * 测试增加信息失败-设备编号999不存在.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_DeviceId_999_Invalid() {
        Dto dto = createDto();
        dto.put("deviceID", "999");
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试增加信息失败-设备编号不存在.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_DeviceId_Null() {
        Dto dto = createDto();
        dto.put("deviceID", null);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试增加信息失败-巡检计划编号1-设备编号999不存在.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_PlanID_1_Device_999_Invalid() {
        Dto dto = createDto();
        int planId = 3;
        dto.put("planID", planId);
        String deviceId = "999";
        dto.put("deviceID", deviceId);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, deviceId);
        mockQueryInspectPlanInfo(planId);
        Mockito.when(
                appDao.queryForObject(
                        "App.InspectPlan.queryPlanDeviceByPlanIdAndDeviceId",
                        dto)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试增加信息失败-巡检设备编号3无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_PlanId_3_Invalid() {
        Dto dto = createDto();
        int pId = 3;
        dto.put("planID", pId);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        Mockito.when(
                appDao.queryForObject("App.InspectPlan.queryPlanById", pId))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试增加信息失败-巡检编号为空_用户编号0000001_巡检记录信息已存在.
     */
    @SuppressWarnings({"unchecked"})
    @Test
    public void
            testAddInfo_Fail_PlanID_Null_DeviceID_0000001_CheckTime_OverStep() {
        Dto dto = createDto();
        dto.put("planID", null);
        dto.put("checktime", TestUtils.getCurrentUnixTime() - DAY_SEC * 100);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        Mockito.when(
                appDao.queryForList(
                        "App.InspectPlan.queryPlanDeviceByDeviceIdAndStateCanInspectAndExecuteTimeFit",
                        dto)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试增加信息失败-巡检编号为空_用户编号0000001_巡检记录信息已存在.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public
            void
            testAddInfo_Fail_PlanID_Null_DeviceID_0000001_InspectRecordInfo_Exist() {
        Dto dto = createDto();
        dto.put("planID", null);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        List list = Lists.newArrayList(createDtoInspectPlanDeviceDto(dto,
                INSPECT_PLAN_ID));
        Mockito.when(
                appDao.queryForList(
                        "App.InspectPlan.queryPlanDeviceByDeviceIdAndStateCanInspectAndExecuteTimeFit",
                        dto)).thenReturn(list);
        mockQueryInspectRecordInfoByPlanDeviceId(INSPECT_PLAN_DEVICE_ID);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_REPEAT_VALUE));
    }

    /**
     * 测试增加信息失败-巡检编号为空_用户编号0000001_没有找到巡检计划编号.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_PlanID_Null_DeviceID_0000001_NotFindPlanID() {
        Dto dto = createDto();
        dto.put("planID", null);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        Mockito.when(
                appDao.queryForList(
                        "App.InspectPlan.queryPlanDeviceByDeviceIdAndStateCanInspectAndExecuteTimeFit",
                        dto)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试增加信息失败-用户编号111无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_UserID_111_Invalid() {
        Dto dto = createDto();
        dto.put("userid", "111");
        Mockito.when(g4Dao.queryForObject("User.getUserInfoByKey", dto))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试增加信息成功-用户编号0000001.
     */
    @Test
    public void testAddInfo_Success_DeviceID_00000001_InspectRecord_NotExist() {
        Dto dto = createDto();
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanInfo(dto.getAsInteger("planID").intValue());
        mockQueryInspectPlanDeviceInfo(dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                        INSPECT_PLAN_DEVICE_ID)).thenReturn(null);
        Mockito.when(
                appDao.queryForObject("App.Inspect.queryInspectRecordByPlanId",
                        INSPECT_PLAN_ID)).thenReturn(null);
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordid", INSPECT_RECORD_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecord", dto);
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordinfoid", INSPECT_RECORD_INFO_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecordInfo", dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordDeviceFinished",
                        INSPECT_PLAN_ID)).thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试增加信息成功-用户编号0000001.
     */
    @Test
    public void testAddInfo_Success_DeviceID_00000001_InspectRecord_1_Exist() {
        Dto dto = createDto();
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanInfo(dto.getAsInteger("planID").intValue());
        mockQueryInspectPlanDeviceInfo(dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                        INSPECT_PLAN_DEVICE_ID)).thenReturn(null);
        mockInspectRecordByPlanId();
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordid", INSPECT_RECORD_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecord", dto);
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordinfoid", INSPECT_RECORD_INFO_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecordInfo", dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordDeviceFinished",
                        INSPECT_PLAN_ID)).thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    private void mockInspectRecordByPlanId() {
        Dto inspectRecordDto = new BaseDto();
        inspectRecordDto.put("inspectrecordid", INSPECT_RECORD_ID);
        Mockito.when(
                appDao.queryForObject("App.Inspect.queryInspectRecordByPlanId",
                        INSPECT_PLAN_ID)).thenReturn(inspectRecordDto);
    }

    /**
     * 测试增加信息成功-用户编号0000001.
     */
    @Test
    public void testAddInfo_Success_DeviceID_00000001_InspectRecordFinished() {
        Dto dto = createDto();
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        mockQueryInspectPlanInfo(dto.getAsInteger("planID").intValue());
        mockQueryInspectPlanDeviceInfo(dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                        INSPECT_PLAN_DEVICE_ID)).thenReturn(null);
        Mockito.when(
                appDao.queryForObject("App.Inspect.queryInspectRecordByPlanId",
                        INSPECT_PLAN_ID)).thenReturn(null);
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordid", INSPECT_RECORD_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecord", dto);
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordinfoid", INSPECT_RECORD_INFO_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecordInfo", dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordDeviceFinished",
                        INSPECT_PLAN_ID)).thenReturn(0);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试增加信息失败-巡检编号为空_用户编号0000001_巡检记录信息已存在.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public
            void
            testAddInfo_Success_PlanID_Null_DeviceID_0000001_InspectRecord_NotExist() {
        Dto dto = createDto();
        dto.put("planID", null);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        List list = Lists.newArrayList(createDtoInspectPlanDeviceDto(dto,
                INSPECT_PLAN_ID));
        Mockito.when(
                appDao.queryForList(
                        "App.InspectPlan.queryPlanDeviceByDeviceIdAndStateCanInspectAndExecuteTimeFit",
                        dto)).thenReturn(list);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                        INSPECT_PLAN_DEVICE_ID)).thenReturn(null);
        Mockito.when(
                appDao.queryForObject("App.Inspect.queryInspectRecordByPlanId",
                        INSPECT_PLAN_ID)).thenReturn(null);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordid", INSPECT_RECORD_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecord", dto);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordinfoid", INSPECT_RECORD_INFO_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecordInfo", dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordDeviceFinished",
                        INSPECT_PLAN_ID)).thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试增加信息失败-巡检编号为空_用户编号0000001_巡检记录信息已存在.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public
            void
            testAddInfo_Success_PlanID_Null_DeviceID_0000001_InspectRecord_Exist() {
        Dto dto = createDto();
        dto.put("planID", null);
        TestUtils.mockQueryUserByUserId(g4Dao, dto, dto.getAsString("userid"));
        TestUtils.mockQueryDeviceInfo(appDao, dto.getAsString("deviceID"));
        List list = Lists.newArrayList(createDtoInspectPlanDeviceDto(dto,
                INSPECT_PLAN_ID));
        Mockito.when(
                appDao.queryForList(
                        "App.InspectPlan.queryPlanDeviceByDeviceIdAndStateCanInspectAndExecuteTimeFit",
                        dto)).thenReturn(list);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordInfoByPlanDeviceId",
                        INSPECT_PLAN_DEVICE_ID)).thenReturn(null);
        mockInspectRecordByPlanId();
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordid", INSPECT_RECORD_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecord", dto);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto dto = invocation.getArgumentAt(1, BaseDto.class);
                dto.put("inspectrecordinfoid", INSPECT_RECORD_INFO_ID);
                return null;
            }
        }).when(appDao).insert("App.Inspect.addInspectRecordInfo", dto);
        Mockito.when(
                appDao.queryForObject(
                        "App.Inspect.queryInspectRecordDeviceFinished",
                        INSPECT_PLAN_ID)).thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) inspectServiceImpl.addInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }
}
