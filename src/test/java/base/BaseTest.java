package base;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.ConfigReader;
import utils.LogUtils;

public class BaseTest {
    
    protected WebDriver driver;
    protected Properties prop;
    protected ConfigReader configReader;

    @BeforeMethod
    public void setup() {
        LogUtils.info("========= 正在初始化测试环境 =========");
        
        // 1. 初始化配置读取器
        configReader = new ConfigReader();
        prop = configReader.init_prop();
        
        // 2. 从配置文件读取浏览器类型
        String browserName = prop.getProperty("browser");
        LogUtils.info("正在启动浏览器: " + browserName);
        
        // 3. 使用 DriverFactory 初始化 ThreadLocal 驱动
        driver = DriverFactory.initDriver(browserName);
        
        // 4. 访问测试 URL
        String url = prop.getProperty("url");
        LogUtils.info("正在打开网址: " + url);
        driver.get(url);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            LogUtils.info("正在关闭浏览器并清理会话...");
            driver.quit(); 
            
            /* * 关键更新：在 ThreadLocal 架构中，quit() 只是关闭浏览器，
             * 必须手动移除线程中的副本，防止内存泄漏。
             */
            DriverFactory.tlDriver.remove(); 
        }
        LogUtils.info("========= 测试环境清理完毕 =========");
    }
}