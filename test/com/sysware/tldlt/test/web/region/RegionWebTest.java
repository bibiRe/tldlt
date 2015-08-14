package com.sysware.tldlt.test.web.region;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sysware.tldlt.test.web.CommonWebTest;

/**
 * Type：RegionWebTest
 * Descript：区域Web测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月10日 下午2:43:59
 * Version：@version
 */
public class RegionWebTest extends CommonWebTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        loginDeveloperUser();
    }

    /**
     * 测试-打开.
     * @throws InterruptedException 
     */
    @Test
    public void testOpen() throws InterruptedException {
        menuClick("基础数据", "区域管理");
        Thread.sleep(3000);      
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@src, 'region.do')]")));
        WebElement element = driver.findElement(By
                .xpath("//div/span/span[text()='区域管理数据列表']"));
        assertThat(element, notNullValue());
    }

}
