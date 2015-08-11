package com.sysware.tldlt.test.web;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.sysware.tldlt.app.utils.AppTools;

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
     * 得到菜单.
     * @param topMenuName 顶层菜单.
     * @param menuName 菜单名
     * @return 菜单
     * @throws InterruptedException 异常
     */
    protected WebElement getMenu(String topMenuName, String menuName)
            throws InterruptedException {
        Thread.sleep(5000);
        if (!AppTools.isEmptyString(topMenuName)) {
            WebElement topMenu = driver.findElement(By
                    .xpath("//div/span[text()='" + topMenuName + "']"));
            if (null == topMenu) {
                return null;
            }
            topMenu.click();
            Thread.sleep(1000);
        }
        WebElement menu = driver.findElement(By.xpath("//a/span[text()='"
                + menuName + "']"));
        return menu;
    }

    /**
     * 菜单点击.
     * @param topMenuName 顶层菜单.
     * @param menuName 菜单名
     * @throws InterruptedException
     */
    protected void menuClick(String topMenuName, String menuName) throws InterruptedException {
        WebElement menu = getMenu(topMenuName, menuName);
        if (null != menu) {
            menu.click();
        }
    }

    /**
     * 创建Chrome Web Driver.
     */
    @SuppressWarnings("unused")
    private void createChromeWebDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:/Users/SW-ITS-HHE/AppData/Local/Google/Chrome/Application/chromedriver.exe");
        driver = new ChromeDriver();
    }

    /**
     * 创建Firefox Web Driver.
     */
    private void createFirefoxWebDriver() {
        System.setProperty("webdriver.firefox.bin",
                "D:/Program Files/Mozilla Firefox/firefox.exe");
        driver = new FirefoxDriver();
    }

    /**
     * 登录用户.
     * @param userName 用户名
     * @param password 密码
     * @throws InterruptedException 异常
     */
    protected void loginByUser(String userName, String password)
            throws InterruptedException {
        Thread.sleep(1000);
        WebElement userInput = driver.findElement(By
                .xpath("//input[@id='account']"));
        userInput.sendKeys(userName);
        WebElement passInput = driver.findElement(By
                .xpath("//input[@id='password']"));
        passInput.sendKeys(password);
        WebElement loginBtn = driver.findElement(By
                .xpath("//button[contains(text(), '登录')]"));
        loginBtn.click();
    }

    /**
     * 用开发者用户登录.
     * @throws InterruptedException 异常
     */
    protected void loginDeveloperUser() throws InterruptedException {
        loginByUser("developer", "111111");
    }

    /**
     * 测试方法开始.
     * @throws Exception 异常
     */
    @Before
    public void setUp() throws Exception {
        createFirefoxWebDriver();
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
