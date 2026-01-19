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
        
        // 1. 初始化配置读取器
        configReader = new ConfigReader();
        prop = configReader.init_prop();
        
        // 2. 获取配置
        String browserName = prop.getProperty("browser");
        // 建议在 config.properties 里加一个参数 remote=true/false
        String remoteMode = prop.getProperty("remote", "false"); 
        
        // 3. 核心修改：判断是跑本地还是 Docker
        if (remoteMode.equalsIgnoreCase("true")) {
            LogUtils.info("检测到开启远程模式，正在连接 Docker Selenium Grid...");
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            // 这里连接你刚才在浏览器看到的那个地址
            String hubUrl = "http://localhost:4444/wd/hub";
            driver = new RemoteWebDriver(new URL(hubUrl), options);
            
            // 为了保持你的 ThreadLocal 逻辑，把驱动塞回去
            DriverFactory.tlDriver.set(driver);
        } else {
            LogUtils.info("正在本地启动浏览器: " + browserName);
            driver = DriverFactory.initDriver(browserName);
        }
        
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
            DriverFactory.tlDriver.remove(); 
        }
        LogUtils.info("========= 测试环境清理完毕 =========");
    }
}