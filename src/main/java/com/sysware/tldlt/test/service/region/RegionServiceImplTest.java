package com.sysware.tldlt.test.service.region;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;
import java.util.List;

import org.g4studio.common.dao.Dao;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sysware.tldlt.app.service.region.RegionServiceImpl;

/**
 * Type：RegionServiceImplTest
 * Descript：RegionServiceImpl测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月13日 下午2:35:24
 * Version：@version
 */
public class RegionServiceImplTest {
    /**
     * App Dao对象.
     */
    private Dao appDao;
    /**
     * RegionServiceImpl对象.
     */
    private RegionServiceImpl regionServiceImpl;

    @Before
    public void setUp() throws Exception {
        regionServiceImpl = new RegionServiceImpl();
        appDao = Mockito.mock(Dao.class);
        regionServiceImpl.setAppDao(appDao);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testQueryRegionItems_ParentId_Root_Success() {
        Dto dto = new BaseDto();
        dto.put("parentId", 0);
        List list = ImmutableList.of(ImmutableMap.of("regionId", 1,
                "departmentId", "001", "parentId", ""));
        Mockito.when(
                appDao.queryForList("App.Region.queryRegionItemsByDto", dto))
                .thenReturn(list);
        Collection result = regionServiceImpl.queryRegionItems(dto);
        assertThat(result.size(), is(1));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testQueryRegionItems_ParentId_Null_Success() {
        Dto dto = new BaseDto();
        List list = ImmutableList.of(ImmutableMap.of("id", 1,
                "departmentId", "001", "parentId", ""), ImmutableMap.of(
                "id", 4, "departmentId", "", "parentId", 1));
        Mockito.when(
                appDao.queryForList("App.Region.queryRegionItemsByDto", dto))
                .thenReturn(list);
        Collection result = regionServiceImpl.queryRegionItems(dto);
        assertThat(result.size(), is(2));
    }
}
