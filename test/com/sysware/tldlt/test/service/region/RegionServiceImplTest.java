package com.sysware.tldlt.test.service.region;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;
import java.util.List;

import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.system.admin.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import utils.BaseAppServiceImplTest;

import com.google.common.collect.Lists;
import com.sysware.tldlt.app.core.metatype.impl.BaseRetDto;
import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;
import com.sysware.tldlt.app.service.region.RegionServiceImpl;
import com.sysware.tldlt.app.utils.AppCommon;

/**
 * Type：DeviceTypeServiceImplTest
 * Descript：RegionServiceImpl测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年7月31日 上午11:02:33
 * Version：@version
 */
public class RegionServiceImplTest extends BaseAppServiceImplTest {
    /**
     * 区域编号16.
     */
    private static final int REGION_ID_15 = 15;

    /**
     * 区域编号15.
     */
    private static final int REGION_ID_16 = 16;

    /**
     * 区域编号3.
     */
    private static final int REGION_ID_3 = 3;

    /**
     * 区域类型编号3.
     */
    private static final int REGION_TYPE_3 = 3;

    /**
     * 组织Service.
     */
    private OrganizationService organizationService;
    /**
     * RegionServiceImpl对象.
     */
    private RegionServiceImpl regionServiceImpl;

    /**
     * 创建dto.
     * @param id
     *            编号.
     * @param name
     *            名称.
     * @param parentId
     *            父区域编号.
     * @param departmentId
     *            部门编号.
     * @return dto
     */
    @SuppressWarnings("unchecked")
    private Dto createInDto(int id, String name, int parentId,
            String departmentId) {
        Dto result = new BaseDto();
        result.put("regionid", id);
        result.put("regionname", name);
        result.put("parentid", parentId);
        result.put("departmentid", departmentId);
        result.put("regiontype", 1);
        return result;
    }

    @Override
    protected BaseAppServiceImpl createService() {
        regionServiceImpl = new RegionServiceImpl();
        return regionServiceImpl;
    }

    /**
     * mock删除区域编号.
     * @param regionId
     *            区域编号
     */
    private void mockDeleteRegionId(int regionId) {
        Mockito.when(appDao.delete("App.Region.deleteInfo", regionId))
                .thenReturn(1);
    }

    /**
     * mock查询区域编号.
     * @param regionId
     *            父区域编号
     */
    private void mockQueryChildRegionId(int regionId) {
        Mockito.when(
                appDao.queryForList("App.Region.queryChildRegionIds", regionId))
                .thenReturn(Lists.newArrayList(regionId + 1));
    }

    /**
     * mock查询部门编号.
     * @param departmentId
     *            部门编号
     */
    @SuppressWarnings("unchecked")
    private void mockQueryDepartmentId(String departmentId) {
        Dto departmentDto = new BaseDto();
        departmentDto.put("deptid", departmentId);
        Mockito.when(
                organizationService.queryDeptinfoByDeptid(Mockito
                        .any(Dto.class))).thenReturn(departmentDto);
    }

    /**
     * mock查询区域编号.
     * @param regionId
     *            父区域编号
     */
    private void mockQueryRegionId(int regionId) {
        Mockito.when(
                appDao.queryForObject("App.Region.queryRegionInfoById",
                        regionId)).thenReturn(
                createInDto(regionId, "aaa", 0, "001"));
    }

    /**
     * 设置.
     */
    @Before
    public void setUp() {
        super.setUp();
        organizationService = Mockito.mock(OrganizationService.class);
        regionServiceImpl.setOrganizationService(organizationService);
    }

    /**
     * 测试新增信息-失败-部门编号00001不存在.
     */
    @Test
    public void testAddInfo_Fail_DepartmentId_00001_NotExist() {
        Dto inDto = createInDto(0, "2", 1, "00001");
        Mockito.when(
                organizationService.queryDeptinfoByDeptid(Mockito
                        .any(Dto.class))).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增信息-失败-部门编号为空.
     */
    @Test
    public void testAddInfo_Fail_DepartmentId_Empty() {
        Dto inDto = createInDto(0, "2", 1, "");
        BaseDto outDto = (BaseDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }

    /**
     * 测试新增信息-失败-名称为空.
     */
    @Test
    public void testAddInfo_Fail_Name_Empty() {
        Dto inDto = createInDto(0, "", 1, "001");
        BaseDto outDto = (BaseDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getSuccessFlag(), is(false));
    }

    /**
     * 测试新增信息-失败-父编号1不存在.
     */
    @Test
    public void testAddInfo_Fail_ParentId_1_NotExist() {
        String departmentId = "00001";
        int parentId = 1;
        Dto inDto = createInDto(0, "2", parentId, departmentId);
        mockQueryDepartmentId(departmentId);
        Mockito.when(
                appDao.queryForObject("App.Region.queryRegionInfoById",
                        parentId)).thenReturn(null);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增信息-失败-部门编号00001不存在.
     */
    @Test
    public void testAddInfo_Fail_RegionId_3_Exist() {
        Dto inDto = createInDto(REGION_ID_3, "2", 1, "00001");
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增信息-失败-区域类型2无效.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddInfo_Fail_RegionType_3_Invalid() {
        String departmentId = "001";
        int parentId = 1;
        Dto inDto = createInDto(0, "2", 1, departmentId);
        inDto.put("regiontype", REGION_TYPE_3);
        mockQueryDepartmentId(departmentId);
        mockQueryRegionId(parentId);
        Mockito.doNothing().when(appDao).insert("App.Region.addInfo", inDto);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_INVALID_VALUE));
    }

    /**
     * 测试新增信息-失败-区域类型不存在.
     */
    @Test
    public void testAddInfo_Fail_RegionType_NotExist() {
        String departmentId = "001";
        int parentId = 1;
        Dto inDto = createInDto(0, "2", 1, departmentId);
        inDto.remove("regiontype");
        mockQueryDepartmentId(departmentId);
        mockQueryRegionId(parentId);
        Mockito.doNothing().when(appDao).insert("App.Region.addInfo", inDto);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试新增信息-成功-父编号为0.
     */
    @Test
    public void testAddInfo_Success_ParentId_0() {
        String departmentId = "001";
        Dto inDto = createInDto(0, "2", 0, departmentId);
        mockQueryDepartmentId(departmentId);
        Mockito.doNothing().when(appDao).insert("App.Region.addInfo", inDto);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试新增信息-成功-父编号为0.
     */
    @Test
    public void testAddInfo_Success_ParentId_1() {
        String departmentId = "001";
        int parentId = 1;
        Dto inDto = createInDto(0, "2", 1, departmentId);
        mockQueryDepartmentId(departmentId);
        mockQueryRegionId(parentId);
        Mockito.doNothing().when(appDao).insert("App.Region.addInfo", inDto);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.addInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试更新信息-失败-编号为0.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDelete_Success_RegionId_15_16() {
        Dto inDto = new BaseDto();
        inDto.put("ids", Lists.newArrayList("15", "16"));
        mockQueryChildRegionId(REGION_ID_15);
        mockQueryChildRegionId(REGION_ID_16);
        mockDeleteRegionId(REGION_ID_15);
        mockDeleteRegionId(REGION_ID_16);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.deleteInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试查询区域列表-成功-父编号为0.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testQueryRegionItems_Success_ParentId_0() {
        Dto dto = new BaseDto();
        dto.put("parentId", 0);
        List list = Lists.newArrayList(createInDto(1, "test", 0, "001"),
                createInDto(2, "test1", 0, "002"));
        Mockito.when(
                appDao.queryForList("App.Region.queryRegionItemsByDto", dto))
                .thenReturn(list);
        Collection retList = regionServiceImpl.queryRegionItems(dto);
        assertThat(retList.size(), is(2));
    }

    /**
     * 测试更新信息-失败-编号为0.
     */
    @Test
    public void testUpdateInfo_Fail_RegionId_0() {
        String departmentId = "001";
        int parentId = 1;
        Dto inDto = createInDto(0, "2", parentId, departmentId);
        mockQueryDepartmentId(departmentId);
        mockQueryRegionId(parentId);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_NULL_VALUE));
    }

    /**
     * 测试更新信息-失败-编号为0.
     */
    @Test
    public void testUpdateInfo_Success_ParentId_0() {
        String departmentId = "001";
        int parentId = 0;
        int regionId = 1;
        Dto inDto = createInDto(regionId, "2", parentId, departmentId);
        mockQueryDepartmentId(departmentId);
        mockQueryRegionId(regionId);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }

    /**
     * 测试更新信息-失败-编号为0.
     */
    @Test
    public void testUpdateInfo_Success_ParentId_1() {
        String departmentId = "001";
        int parentId = 1;
        int regionId = 2;
        Dto inDto = createInDto(regionId, "2", parentId, departmentId);
        mockQueryDepartmentId(departmentId);
        mockQueryRegionId(parentId);
        mockQueryRegionId(regionId);
        BaseRetDto outDto = (BaseRetDto) regionServiceImpl.updateInfo(inDto);
        assertThat(outDto.getRetCode(), is(AppCommon.RET_CODE_SUCCESS));
    }
}
