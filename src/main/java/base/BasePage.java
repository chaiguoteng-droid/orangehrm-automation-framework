package base;

import java.time.Duration;
import utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils; 

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // 针对 Java 11 + Selenium 4 优化，使用 10 秒作为标准超时
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * 增强版点击：常规点击失败后自动切换 JS 点击，若全失败则抛出异常以触发截图
     */
    public void clickElement(By locator) {
        try {
            LogUtils.info("等待元素可点击并执行常规点击: " + locator);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            LogUtils.warn("常规点击失败，尝试 JavaScript 强制点击: " + locator);
            try {
                jsClick(locator);
            } catch (Exception jsEx) {
                LogUtils.error("所有点击方式均失败，元素可能被遮挡或不存在: " + locator);
                throw jsEx; // 必须抛出，否则测试会假装通过，Listener 无法捕获失败
            }
        }
    }

    /**
     * 增强版输入：自动清空、等待可见、原生输入并记录日志
     */
    public void sendText(By locator, String text) {
        try {
            LogUtils.info("输入文本 [" + text + "] 到元素: " + locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text); // 确保使用原生 sendKeys
        } catch (Exception e) {
            LogUtils.error("向元素输入文本失败: " + locator);
            throw e;
        }
    }

    /**
     * JavaScript 强制点击：解决元素被透明层遮挡的利器
     */
    public void jsClick(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            LogUtils.error("JS 执行点击失败: " + locator);
            throw e;
        }
    }

    /**
     * 判断元素是否存在且可见：用于逻辑分支判断，不抛出异常
     */
    public boolean isElementDisplayed(By locator) {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
            LogUtils.info("检查元素显示状态: " + locator + " -> " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取元素属性：用于验证 placeholder 或 CSS 样式
     */
    public String getAttributeValue(By locator, String attribute) {
        String value = wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attribute);
        LogUtils.info("获取元素属性 [" + attribute + "] 值为: " + value);
        return value;
    }

    /**
     * 获取页面标题：审计日志核心
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        LogUtils.info("当前页面标题: " + title);
        return title;
    }

    /**
     * 获取文本：用于断言验证
     */
    public String getElementText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }
}