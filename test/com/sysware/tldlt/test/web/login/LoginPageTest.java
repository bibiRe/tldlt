package com.sysware.tldlt.test.web.login;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sysware.tldlt.test.web.CommonWebTest;

/**
 * Type：LoginPageTest
 * Descript：登录页面测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月3日 下午3:07:09
 * Version：@version
 */
public class LoginPageTest extends CommonWebTest {

    /**
     * 测试登录失败-用户名super-密码错误.
     * @throws Exception 异常.
     */
    @Test
    public void testLogin_Fail_Id_Super_Password_Fail() throws Exception {
        loginByUser("super", "1111");
        Thread.sleep(1000);
        WebElement span = driver.findElement(By
                .xpath("//span[text()='密码输入错误,请重新输入!']"));
        assertThat(span, notNullValue());
    }

    /**
     * 测试登录失败-用户名test-不存在.
     * @throws Exception 异常.
     */
    @Test
    public void testLogin_Fail_Id_Test_NotExist() throws Exception {
        loginByUser("test", "111111");
        Thread.sleep(1000);
        WebElement span = driver.findElement(By
                .xpath("//span[text()='帐号输入错误,请重新输入!']"));
        assertThat(span, notNullValue());
    }

    /**
     * 测试登录成功-用户名super.
     * @throws Exception 异常.
     */
    @Test
    public void testLogin_Success_Id_Super() throws Exception {
        loginByUser("super", "111111");
        Thread.sleep(1000);
        WebElement btn = driver.findElement(By.xpath("//button[text()='主题']"));
        assertThat(btn, notNullValue());
    }

    /**
     * 测试打开登录页面.
     * @throws Exception 异常.
     */
    @Test
    public void testOpen() throws Exception {
        WebElement txtbox = driver.findElement(By
                .xpath("//input[@id='account']"));
        assertThat(txtbox, notNullValue());
    }
}
