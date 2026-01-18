package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void loginTest() {
        // 初始化页面对象
        LoginPage loginPage = new LoginPage(driver);
        
        // 执行登录动作
        loginPage.doLogin("Admin", "admin123");
        
        // 验证结果 (断言)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard"));
    }
}