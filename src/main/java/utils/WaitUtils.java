package utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
    // 等待元素直到可点击
    public static void waitUntilClickable(WebDriver driver, By locator, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // 等待页面标题包含特定文本 (用于验证跳转)
    public static boolean waitForTitle(WebDriver driver, String title, int timeout) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout))
            .until(ExpectedConditions.titleContains(title));
    }
}
