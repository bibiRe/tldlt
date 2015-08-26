package com.sysware.tldlt.app.service.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;

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
     * 测试保存用户GPS信息成功.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveGPSInfo_Fail_UserId_10003333_Invalid() {
        Dto dto = new BaseDto();
        String userid = "10003333";
        dto.put("userid", userid);
        dto.put("longtitude", 118.850);
        dto.put("latitude", 32.1031);
        dto.put("height", 1);
        dto.put("speed", 1);
        long unixTime = new Date().getTime() / 1000;
        dto.put("datetime", unixTime);
        Mockito.doNothing().when(appDao).insert("App.User.saveGPSInfo", dto);
        Mockito.when(g4Dao.queryForObject("User.getUserInfoByKey", dto))
                .thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试保存用户GPS信息成功.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveGPSInfoSuccess() {
        Dto dto = new BaseDto();
        String userid = "10004894";
        dto.put("userid", userid);
        dto.put("longtitude", 118.850);
        dto.put("latitude", 32.1031);
        dto.put("height", 1);
        dto.put("speed", 1);
        dto.put("datetime", TestUtils.getCurrentUnixTime());
        Dto userDto = new BaseDto();
        userDto.put("userid", userid);
        userDto.put("username", "张三");
        userDto.put("account", "zs");
        Mockito.when(g4Dao.queryForObject("User.getUserInfoByKey", dto))
                .thenReturn(userDto);
        Mockito.doNothing().when(appDao).insert("App.User.saveGPSInfo", dto);
        Mockito.doNothing().when(appDao)
                .insert("App.User.saveReleateGPSInfo", dto);
        BaseRetDto outDto = (BaseRetDto) userServiceImpl.saveGPSInfo(dto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

}
