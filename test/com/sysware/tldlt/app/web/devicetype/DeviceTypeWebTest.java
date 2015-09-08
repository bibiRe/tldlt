package com.sysware.tldlt.app.web.devicetype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sysware.tldlt.app.web.CommonWebTest;

/**
 * Type：DeviceTypeWebTest
 * Descript：设备类型
 * Create：SW-ITS-HHE
 * Create Time：2015年8月3日 下午3:16:09
 * Version：@version
 */
public class DeviceTypeWebTest extends CommonWebTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        loginDeveloperUser();
    }

    /**
     * 测试打开设备类型页面.
     * @throws InterruptedException
     */
    @Test
    public void testOpen() throws InterruptedException {
        menuClick("基础数据", "设备类型");
        Thread.sleep(3000);
        driver.switchTo().frame(
                driver.findElement(By
                        .xpath("//iframe[contains(@src, 'deviceType.do')]")));
        WebElement element = driver.findElement(By
                .xpath("//div/span/span[text()='设备类型数据列表']"));
        assertThat(element, notNullValue());
    }
}
