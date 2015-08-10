package com.sysware.tldlt.test.web.devicetype;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sysware.tldlt.test.web.CommonWebTest;

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
        loginByUser("developer", "111111");
    }

    /**
     * 测试打开设备类型页面.
     * @throws InterruptedException
     */
    @Test
    public void testOpen() throws InterruptedException {
        Thread.sleep(3000);
        WebElement topMenu = driver.findElement(By.xpath("//div/span[text()='基础数据']/../"));
        topMenu.click();
        WebElement menu = driver.findElement(By.xpath("//a/span[text()='设备类型']"));
        assertThat(menu, notNullValue());
    }

}
