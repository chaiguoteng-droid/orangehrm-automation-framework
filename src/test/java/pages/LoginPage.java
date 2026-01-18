package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BasePage;

public class LoginPage extends BasePage {

    // 1. 定义页面元素 (By Locators) - 这样即使网页改版，你只需要改这里
    private By username = By.name("username");
    private By password = By.name("password");
    private By loginBtn = By.xpath("//button[@type='submit']");

    // 2. 构造函数
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // 3. 业务动作 (Page Actions)
    public void doLogin(String un, String pwd) {
        sendText(username, un); // 调用父类 BasePage 的封装方法
        sendText(password, pwd);
        clickElement(loginBtn);
    }
}
