package com.sysware.tldlt.app.service.devicetype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Test;
import org.mockito.Mockito;

import utils.BaseAppServiceImplTest;

import com.google.common.collect.ImmutableList;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.service.devicetype.DeviceTypeServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;

/**
 * Type：DeviceTypeServiceImplTest
 * Descript：DeviceTypeServiceImpl测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月31日 上午11:02:33
 * Version：@version
 */
public class DeviceTypeServiceImplTest extends BaseAppServiceImplTest {
    /**
     * DeviceTypeServiceImpl对象.
     */
    private DeviceTypeServiceImpl deviceTypeServiceImpl;

    @SuppressWarnings("unchecked")
    private Dto createInDto(String id, String name, String parentId) {
        Dto result = new BaseDto();
        result.put("devicetypeid", id);
        result.put("devicetypename", name);
        result.put("parentid", parentId);
        return result;
    }

    @Override
    protected BaseAppServiceImpl createService() {
        deviceTypeServiceImpl = new DeviceTypeServiceImpl();
        return deviceTypeServiceImpl;
    }

    @Test
    public void testAddInfo_Fail_Id_3_IsRepeat() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(inDto);
        Mockito.doNothing().when(appDao)
                .insert("App.DeviceType.addInfo", inDto);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_REPEAT_VALUE));
    }

    @Test
    public void testAddInfo_Fail_Id_Empty() {
        String id = "";
        Dto inDto = createInDto(id, "2", null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    @Test
    public void testAddInfo_Fail_Name_Empty() {
        String id = "3";
        Dto inDto = createInDto(id, "", null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    @Test
    public void testAddInfo_Fail_ParentId_4_Invalid() {
        String id = "3";
        String parentId = "4";
        Dto inDto = createInDto(id, "ttt", parentId);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        parentId)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    @Test
    public void testAddInfo_Success() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(null);
        Mockito.doNothing().when(appDao)
                .insert("App.DeviceType.addInfo", inDto);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteInfo_Success() {
        String id = "3";
        Dto inDto = new BaseDto();
        inDto.put("ids", ImmutableList.of(id));
        Mockito.when(appDao.delete("App.DeviceType.deleteInfo", id))
                .thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .deleteInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Fail_Id_3_Repeat() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        String id2 = "4";
        inDto.put("oid", id2);
        Dto dto2 = createInDto(id2, "3", null);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id2)).thenReturn(dto2);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(inDto);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_REPEAT_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Fail_OId_3_NotExist() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        inDto.put("oid", id);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Fail_Oid_4_Id_3_Repeat() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        String id2 = "4";
        inDto.put("oid", id2);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id2)).thenReturn(createInDto(id2, "3", null));
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(inDto);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_REPEAT_VALUE));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Success_Id_3() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        inDto.put("oid", id);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(createInDto(id, "3", null));
        Mockito.when(appDao.update("App.DeviceType.updateInfo", inDto))
                .thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Success_Oid_4_Id_3() {
        String id = "3";
        Dto inDto = createInDto(id, "2", null);
        String id2 = "4";
        inDto.put("oid", id2);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id2)).thenReturn(createInDto(id2, "3", null));
        Mockito.when(
                appDao.queryForObject("App.DeviceType.queryDeviceTypeInfoById",
                        id)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }
}
