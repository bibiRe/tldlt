package com.sysware.tldlt.test.service.devicetype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.g4studio.common.dao.Dao;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.devicetype.DeviceTypeServiceImpl;

/**
 * Type：DeviceTypeServiceImplTest
 * Descript：DeviceTypeServiceImpl测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月31日 上午11:02:33
 * Version：@version
 */
public class DeviceTypeServiceImplTest {
    /**
     * App Dao对象.
     */
    private Dao appDao;
    /**
     * DeviceTypeServiceImpl对象.
     */
    private DeviceTypeServiceImpl deviceTypeServiceImpl;

    @SuppressWarnings("unchecked")
    private Dto createInDto(String id, String name) {
        Dto result = new BaseDto();
        result.put("id", id);
        result.put("name", name);
        return result;
    }

    @Before
    public void setUp() {
        deviceTypeServiceImpl = new DeviceTypeServiceImpl();
        appDao = Mockito.mock(Dao.class);
        deviceTypeServiceImpl.setAppDao(appDao);
    }

    @Test
    public void testAddInfo_Fail_Id_3_IsRepeat() {
        String id = "3";
        Dto inDto = createInDto(id, "2");
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id))
                .thenReturn(inDto);
        Mockito.doNothing().when(appDao)
                .insert("App.DeviceType.addInfo", inDto);
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }

    @Test
    public void testAddInfo_Fail_Id_Empty() {
        String id = "";
        Dto inDto = createInDto(id, "2");
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }

    @Test
    public void testAddInfo_Fail_Name_Empty() {
        String id = "3";
        Dto inDto = createInDto(id, "");
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }

    @Test
    public void testAddInfo_Success() {
        String id = "3";
        Dto inDto = createInDto(id, "2");
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id))
                .thenReturn(null);
        Mockito.doNothing().when(appDao)
                .insert("App.DeviceType.addInfo", inDto);
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.addInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteInfo_Fail_Id_4_Exist_Id_5_NotExist() {
        String id = "4";
        Dto inDto = new BaseDto();
        inDto.put("ids", ImmutableList.of(id, "5"));
        Mockito.when(appDao.delete("App.DeviceType.deleteInfo", id))
                .thenReturn(1);
        Mockito.when(appDao.delete("App.DeviceType.deleteInfo", "5"))
                .thenReturn(0);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .deleteInfo(inDto);
        assertThat(outDto.isRetSuccess(), is(false));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteInfo_Fail_Id_4_NotExist() {
        String id = "4";
        Dto inDto = new BaseDto();
        inDto.put("ids", ImmutableList.of(id));
        Mockito.when(appDao.delete("App.DeviceType.deleteInfo", id))
                .thenReturn(0);
        BaseRetDto outDto = (BaseRetDto) deviceTypeServiceImpl
                .deleteInfo(inDto);
        assertThat(outDto.isRetSuccess(), is(false));
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
        assertThat(outDto.isRetSuccess(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Fail_Id_3_Repeat() {
        String id = "3";
        Dto inDto = createInDto(id, "2");
        String id2 = "4";
        inDto.put("oid", id2);
        Dto dto2 = createInDto(id2, "3");
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id2))
                .thenReturn(dto2);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id))
                .thenReturn(inDto);
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.updateInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Fail_OId_3_NotExist() {
        String id = "3";
        Dto inDto = createInDto(id, "2");
        inDto.put("oid", id);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id))
                .thenReturn(null);
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.updateInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Success_Id_3() {
        String id = "3";
        Dto inDto = createInDto(id, "2");
        inDto.put("oid", id);
        Dto dto = createInDto(id, "3");
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id))
                .thenReturn(dto);
        Mockito.when(appDao.update("App.DeviceType.updateInfo", inDto))
                .thenReturn(1);
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.updateInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInfo_Success_Oid_4_Id_3() {
        String id = "3";
        Dto inDto = createInDto(id, "2");
        String id2 = "4";
        inDto.put("oid", id2);
        Dto dto2 = createInDto(id2, "3");
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id2))
                .thenReturn(dto2);
        Mockito.when(
                appDao.queryForObject("App.DeviceType.getDeviceTypeById", id))
                .thenReturn(null);
        BaseDto outDto = (BaseDto) deviceTypeServiceImpl.updateInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(true));
    }
}
