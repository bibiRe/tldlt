package com.sysware.tldlt.test.web;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Type：CommonWebTest
 * Descript：通用Web测试类.
 * Create：SW-ITS-HHE
 * Create Time：2015年8月3日 下午3:07:21
 * Version：@version
 */
public class CommonWebTest {
    protected WebDriver driver = null;

    /**
     * 登录用户.
     * @param userName 用户名
     * @param password 密码
     * @throws InterruptedException 异常
     */
    protected void loginByUser(String userName, String password) throws InterruptedException {
        Thread.sleep(1000);
        WebElement userInput = driver.findElement(By.xpath("//input[@id='account']"));
        userInput.sendKeys(userName);
        WebElement passInput = driver.findElement(By.xpath("//input[@id='password']"));
        passInput.sendKeys(password);
        WebElement loginBtn = driver.findElement(By.xpath("//button[contains(text(), '登录')]"));
        loginBtn.click();
    }

    /**
     * 测试方法开始.
     * @throws Exception 异常
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.firefox.bin",
                "D:/Program Files/Mozilla Firefox/firefox.exe");
        driver = new FirefoxDriver();
        driver.get("http://192.168.128.88:169/tldlt");
        driver.manage().window().maximize();
    }
    
    /**
     * 测试方法结束.
     * @throws Exception 异常
     */
    @After
    public void tearDown() throws Exception {
        if (null != driver) {
            driver.close();
        }
        driver = null;
    }
}
