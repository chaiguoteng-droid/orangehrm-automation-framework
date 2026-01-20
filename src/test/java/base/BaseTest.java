package base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.ConfigReader;
import utils.LogUtils;

public class BaseTest {
    
    protected WebDriver driver;
    protected Properties prop;
    protected ConfigReader configReader;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        LogUtils.info("========= 正在初始化测试环境 =========");
        
        configReader = new ConfigReader();
        prop = configReader.init_prop();
        
        String browserName = prop.getProperty("browser");
        // 从 config.properties 读取，默认设为 false 方便本地调试
        String remoteMode = prop.getProperty("remote", "false"); 
        
        if (remoteMode.equalsIgnoreCase("true")) {
            LogUtils.info("检测到开启远程模式，正在连接 Docker Selenium Grid...");
            
            ChromeOptions options = new ChromeOptions();
            // 在 Docker 容器中运行必须添加的参数
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless"); // 建议 Jenkins 跑时开启无头模式，节省资源
            
            // 注意：Selenium 4 的标准连接地址是 http://localhost:4444
            String hubUrl = "http://localhost:4444"; 
            
            try {
                driver = new RemoteWebDriver(new URL(hubUrl), options);
                // 确保你之前的 DriverFactory 逻辑依然兼容
                DriverFactory.setDriver(driver); 
                LogUtils.info("远程 RemoteWebDriver 初始化成功。");
            } catch (Exception e) {
                LogUtils.error("无法连接到 Selenium Grid: " + e.getMessage());
                throw e; 
            }
            
        } else {
            LogUtils.info("正在本地启动浏览器: " + browserName);
            driver = DriverFactory.initDriver(browserName);
        }
        
        String url = prop.getProperty("url");
        LogUtils.info("正在打开网址: " + url);
        driver.get(url);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            LogUtils.info("正在关闭浏览器并清理会话...");
            driver.quit(); 
            // 确保 ThreadLocal 资源被正确移除，防止内存泄漏
            if(DriverFactory.tlDriver != null) {
                DriverFactory.tlDriver.remove(); 
            }
        }
        LogUtils.info("========= 测试环境清理完毕 =========");
    }
}