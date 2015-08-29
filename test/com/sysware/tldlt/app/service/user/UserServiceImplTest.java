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
        dto.put("longtitude", 118.850);
        TestUtils.setGPSDto(dto);
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
     * 测试保存用户GPS信息成功.
     */
    @SuppressWarnings("unchecked")
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
