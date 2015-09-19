package com.sysware.tldlt.app.service.devicetypecheckitem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.system.common.util.SystemConstants;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import utils.BaseAppServiceImplTest;

import com.google.common.collect.ImmutableList;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;

/**
 * Type：DeviceTypeCheckItemServiceImplTest
 * Descript：设备类型检查项服务实现类测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月18日 上午9:57:12
 * Version：@version
 */
public class DeviceTypeCheckItemServiceImplTest extends BaseAppServiceImplTest {
    /**
     * 设备类型编号.
     */
    private static int ACTION_VALUE = 1;
    /**
     * 检查编号1;
     */
    private static int DEVICETYPE_CHECKCONTENT_ID = 1;

    /**
     * 检查编号1;
     */
    private static int DEVICETYPE_CHECKCONTENT_ID_143 = 143;
    /**
     * 设备类型编号.
     */
    private static String DEVICETYPE_ID = "trans";

    /**
     * DeviceTypeServiceImpl对象.
     */
    private DeviceTypeCheckItemServiceImpl deviceTypeCheckItemServiceImpl;

    /**
     * 创建输入dto.
     * @param id 编号
     * @return dto
     */
    @SuppressWarnings("unchecked")
    private Dto createInDto(int id) {
        Dto result = new BaseDto();
        if (id > 0) {
            result.put("devicecheckcontentid", id);
        }
        result.put("checkitemname", "test");
        result.put("state", SystemConstants.ENABLED_Y);
        result.put("devicetypeid", DEVICETYPE_ID);
        result.put("action", ACTION_VALUE);
        return result;
    }

    @Override
    protected BaseAppServiceImpl createService() {
        deviceTypeCheckItemServiceImpl = new DeviceTypeCheckItemServiceImpl();
        return deviceTypeCheckItemServiceImpl;
    }

    /**
     * mock新增信息成功.
     * @param inDto 输入dto
     */
    private void mockAddInfoSuccess(Dto inDto) {
        Mockito.doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Dto inDto = (Dto) invocation.getArguments()[1];
                inDto.put("devicecheckcontentid", DEVICETYPE_CHECKCONTENT_ID);
                return null;
            }
        }).when(appDao).insert("App.DeviceTypeCheckItem.addInfo", inDto);
    }

    /**
     * mock查询设备类型编号.
     * @param departmentId
     *            部门编号
     */
    @SuppressWarnings("unchecked")
    private void mockQueryDeviceTypeId(String devicetypeid) {
        Dto deviceTypeDto = new BaseDto();
        deviceTypeDto.put("id", devicetypeid);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        devicetypeid)).thenReturn(deviceTypeDto);
    }

    /**
     * 测试新增-失败-动作3无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_Action_3_Invalid() {
        Dto inDto = createInDto(0);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        inDto.put("action", 3);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增-失败-动作不存在.
     */
    @Test
    public void testAddInfo_Fail_Action_NotExist() {
        Dto inDto = createInDto(0);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        inDto.remove("action");
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试新增-失败-设备编号不存在.
     */
    @Test
    public void testAddInfo_Fail_DeviceTypeId_NotExist() {
        Dto inDto = createInDto(0);
        inDto.remove("devicetypeid");
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试新增-失败-设备编号为空.
     */
    @Test
    public void testAddInfo_Fail_DeviceTypeId_Trans_Invalid() {
        Dto inDto = createInDto(0);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        DEVICETYPE_ID)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增-失败-编号存在.
     */
    @Test
    public void testAddInfo_Fail_Id_HasValue() {
        Dto inDto = createInDto(0);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增-失败-名称为空.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_Name_Empty() {
        Dto inDto = createInDto(0);
        inDto.put("checkitemname", "");
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试新增-失败-名称不存在.
     */
    @Test
    public void testAddInfo_Fail_Name_NotExist() {
        Dto inDto = createInDto(0);
        inDto.remove("checkitemname");
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试新增-失败-动作无效.
     */
    @Test
    public void testAddInfo_Fail_State_NotExist() {
        Dto inDto = createInDto(0);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        inDto.remove("state");
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试新增-成功.
     */
    @Test
    public void testAddInfo_Success() {
        Dto inDto = createInDto(0);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        mockAddInfoSuccess(inDto);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试更新-失败-编号143无效.
     */
    @Test
    public void testUpdateInfo_Id_143_Invalid() {
        Dto inDto = createInDto(DEVICETYPE_CHECKCONTENT_ID_143);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        Mockito.when(appDao.update("App.DeviceTypeCheckItem.updateInfo", inDto))
                .thenReturn(1);
        Mockito.when(
                appDao.queryForObject(
                        "App.DeviceTypeCheckItem.queryCheckItemById",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试更新-失败-编号143无效.
     */
    @Test
    public void testUpdateInfo_Fail_Id_143() {
        Dto inDto = createInDto(DEVICETYPE_CHECKCONTENT_ID_143);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        Mockito.when(appDao.update("App.DeviceTypeCheckItem.updateInfo", inDto))
                .thenReturn(1);
        Mockito.when(
                appDao.queryForObject(
                        "App.DeviceTypeCheckItem.queryCheckItemById",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(inDto);
        Mockito.when(appDao.update("App.DeviceTypeCheckItem.updateInfo", inDto))
                .thenReturn(0);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_UPDATE_FAIL));
    }

    /**
     * 测试更新-成功.
     */
    @Test
    public void testUpdateInfo_Success() {
        Dto inDto = createInDto(DEVICETYPE_CHECKCONTENT_ID_143);
        mockQueryDeviceTypeId(DEVICETYPE_ID);
        Mockito.when(appDao.update("App.DeviceTypeCheckItem.updateInfo", inDto))
                .thenReturn(1);
        Mockito.when(
                appDao.queryForObject(
                        "App.DeviceTypeCheckItem.queryCheckItemById",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(inDto);
        Mockito.when(appDao.update("App.DeviceTypeCheckItem.updateInfo", inDto))
                .thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试删除-成功-编号143
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteInfo_Success_Id_143() {
        String id = String.valueOf(DEVICETYPE_CHECKCONTENT_ID_143);
        Dto inDto = new BaseDto();
        inDto.put("ids", ImmutableList.of(id));
        Mockito.when(
                appDao.queryForObject(
                        "App.DeviceTypeCheckItem.queryCountInspectPlanDeviceById",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(0);
        Mockito.when(
                appDao.delete("App.DeviceTypeCheckItem.deleteInfo",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .deleteInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试删除-成功-编号143_更新数据编号
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteInfo_Success_Id_143_Update_State_0() {
        String id = String.valueOf(DEVICETYPE_CHECKCONTENT_ID_143);
        Dto inDto = new BaseDto();
        inDto.put("ids", ImmutableList.of(id));
        Mockito.when(
                appDao.queryForObject(
                        "App.DeviceTypeCheckItem.queryCountInspectPlanDeviceById",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(1);
        Mockito.when(
                appDao.delete("App.DeviceTypeCheckItem.updateStateDisableInfo",
                        DEVICETYPE_CHECKCONTENT_ID_143)).thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) deviceTypeCheckItemServiceImpl
                .deleteInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }
}
