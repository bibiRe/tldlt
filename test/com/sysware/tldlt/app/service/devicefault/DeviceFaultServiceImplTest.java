package com.sysware.tldlt.app.service.devicefault;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Test;
import org.mockito.Mockito;

import utils.BaseAppServiceImplTest;

import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;
import com.sysware.tldlt.app.utils.AppTools;

/**
 * Type：DeviceFaultServiceImplTest
 * Descript：设备故障服务接口实现测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年9月22日 下午2:50:16
 * Version：@version
 */
public class DeviceFaultServiceImplTest extends BaseAppServiceImplTest {

    private static int DEVICEFAULT_ID_143 = 143;
    /**
     * 设备故障服务实现对象.
     */
    private DeviceFaultServiceImpl deviceFaultServiceImpl;

    /**
     * 创建dto对象.
     * @param id 编号.
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    private Dto createDto(int id) {
        Dto inDto = new BaseDto();
        inDto.put("devicefaultinfoid", id);
        inDto.put("state", 1);
        inDto.put("handler", "00000001");
        inDto.put("handletime", AppTools.currentUnixTime());
        inDto.put("remark", "a");
        return inDto;
    }

    @Override
    protected BaseAppServiceImpl createService() {
        deviceFaultServiceImpl = new DeviceFaultServiceImpl();
        return deviceFaultServiceImpl;
    }

    /**
     * 测试更新状态信息-失败-编号为空.
     */
    @Test
    public void testUpdateStateInfo_Fail_Id_Null() {
        Dto inDto = createDto(1);
        inDto.remove("devicefaultinfoid");
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试更新-失败-编号143无效.
     */
    @Test
    public void testUpdateStateInfo_Fail_Id_143_NotExist() {
        Dto inDto = createDto(DEVICEFAULT_ID_143);
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById",
                        DEVICEFAULT_ID_143)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试更新-失败-状态0无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStateInfo_Fail_State_0_Invalid() {
        Dto inDto = createDto(1);
        inDto.put("state", 0);
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById", 1))
                .thenReturn(inDto);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试更新-失败-处理人编号为空.
     */
    @Test
    public void testUpdateStateInfo_Fail_Handler_Null() {
        Dto inDto = createDto(1);
        inDto.remove("handler");
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById", 1))
                .thenReturn(inDto);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试更新-失败-处理人编号为空.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStateInfo_Fail_Handler_0001_Invalid() {
        Dto inDto = createDto(1);
        inDto.put("handler", "0001");
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById", 1))
                .thenReturn(inDto);
        Mockito.when(
                g4Dao.queryForObject(Mockito.eq("User.getUserInfoByKey"),
                        Mockito.any(Dto.class))).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试更新-失败-处理人编号为空.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStateInfo_Fail_HandleTimer_Null() {
        Dto inDto = createDto(1);
        inDto.remove("handletime");
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById", 1))
                .thenReturn(inDto);
        Dto userDto = new BaseDto();
        userDto.put("userid", inDto.getAsString("handler"));
        Mockito.when(
                g4Dao.queryForObject(Mockito.eq("User.getUserInfoByKey"),
                        Mockito.any(Dto.class))).thenReturn(userDto);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试更新-失败-处理人编号为空.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStateInfo_Fail_Update() {
        Dto inDto = createDto(1);
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById", 1))
                .thenReturn(inDto);
        Dto userDto = new BaseDto();
        userDto.put("userid", inDto.getAsString("handler"));
        Mockito.when(
                g4Dao.queryForObject(Mockito.eq("User.getUserInfoByKey"),
                        Mockito.any(Dto.class))).thenReturn(userDto);
        Mockito.when(appDao.update("App.DeviceFault.updateStateInfo", inDto))
                .thenReturn(0);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_UPDATE_FAIL));
    }
    
    /**
     * 测试更新-成功.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateStateInfo_Success() {
        Dto inDto = createDto(1);
        Mockito.when(
                appDao.queryForObject("App.DeviceFault.queryDeviceFaultById", 1))
                .thenReturn(inDto);
        Dto userDto = new BaseDto();
        userDto.put("userid", inDto.getAsString("handler"));
        Mockito.when(
                g4Dao.queryForObject(Mockito.eq("User.getUserInfoByKey"),
                        Mockito.any(Dto.class))).thenReturn(userDto);
        Mockito.when(appDao.update("App.DeviceFault.updateStateInfo", inDto))
                .thenReturn(1);
        BaseRetDto outDto = (BaseRetDto) deviceFaultServiceImpl
                .updateStateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }
}
