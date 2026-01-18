package listeners;

import base.DriverFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentManager;
import utils.LogUtils; // 导入日志工具
import utils.ScreenshotUtils; // 导入截图工具

public class TestListener implements ITestListener {
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        LogUtils.info("--------- 开始执行测试用例: " + methodName + " ---------");
        
        ExtentTest extentTest = extent.createTest(methodName);
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("测试用例 [" + result.getMethod().getMethodName() + "] 执行成功 ✅");
        test.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        LogUtils.error("测试用例 [" + methodName + "] 执行失败 ❌");
        LogUtils.error("失败原因: " + result.getThrowable());

        test.get().log(Status.FAIL, "Test Failed");
        test.get().fail(result.getThrowable());

        try {
            // 1. 调用你写好的 ScreenshotUtils 保存物理文件到本地 (用于存档)
            String screenshotPath = ScreenshotUtils.pullScreenshot(DriverFactory.getDriver(), methodName);
            
            // 2. 同时生成 Base64 字符串直接嵌入 Extent Report (用于即时查看报告)
            String base64Screenshot = ((TakesScreenshot) DriverFactory.getDriver())
                                      .getScreenshotAs(OutputType.BASE64);
            
            test.get().addScreenCaptureFromBase64String(base64Screenshot, "失败现场截图");
            LogUtils.info("截图已保存至: " + screenshotPath);
            
        } catch (Exception e) {
            LogUtils.error("捕获截图时发生异常: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.warn("测试用例 [" + result.getMethod().getMethodName() + "] 已跳过 ⚠️");
        test.get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtils.info("========= 所有测试执行完毕，正在生成报告... =========");
        if (extent != null) {
            extent.flush(); 
        }
    }
}