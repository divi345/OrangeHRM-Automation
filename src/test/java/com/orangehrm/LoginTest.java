package com.orangehrm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;


import com.util.ScreenshotUtil;
import com.util.Xls_Reader;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    Xls_Reader reader;
    ExtentReports extent;
    ExtentTest test;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        reader = new Xls_Reader("C:\\Users\\91967\\eclipse-workspace\\OrangeHRM-Automation\\testdata\\Logindata.xlsx");

        // Ensure reports/screenshots folder exists
        new File(System.getProperty("user.dir") + "/reports/screenshots/").mkdirs();

        // ExtentReports setup
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/OrangeHRM_Report.html");
        spark.config().setReportName("OrangeHRM Automation Test Report");
        spark.config().setDocumentTitle("Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project", "OrangeHRM");
        extent.setSystemInfo("Tester", "Divyasree");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        int rowCount = reader.getRowCount("RegTestData");
        Object[][] data = new Object[rowCount - 1][2];

        for (int i = 2; i <= rowCount; i++) {
            data[i - 2][0] = reader.getCellData("RegTestData", "username", i);
            data[i - 2][1] = reader.getCellData("RegTestData", "password", i);
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginDataDrivenTest(String username, String password) {
        test = extent.createTest("Login Test for user: " + username);

        try {
            // Open login page
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            test.info("Opened login page")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, username + "_loginPage"));

            // Enter credentials
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(username);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys(password);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();

            DashboardPage dashboard = new DashboardPage(driver);

            // Wait for either dashboard or invalid login message
            wait.until(driver -> dashboard.isDashboardDisplayed() ||
                    driver.findElements(By.xpath("//p[contains(text(),'Invalid')]")).size() > 0);

            // Wrong password case
            if (driver.findElements(By.xpath("//p[contains(text(),'Invalid')]")).size() > 0) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, username + "_wrongPassword");
                test.fail("Wrong password for: " + username)
                    .addScreenCaptureFromPath(screenshotPath);
                System.out.println("Wrong password attempt for: " + username);

                // Mark as failed in TestNG
                Assert.fail("Invalid login for user: " + username);
            } 
            // Dashboard displayed
            else if (dashboard.isDashboardDisplayed()) {
                String dashboardScreenshot = ScreenshotUtil.captureScreenshot(driver, username + "_dashboard");
                test.pass("Login successful and Dashboard displayed for: " + username)
                    .addScreenCaptureFromPath(dashboardScreenshot);
                System.out.println("Dashboard is displayed for: " + username);

            }

        } catch (Exception e) {
            String errorScreenshot = ScreenshotUtil.captureScreenshot(driver, username + "_error");
            test.fail("Exception occurred: " + e.getMessage())
                .addScreenCaptureFromPath(errorScreenshot);
            System.out.println("Test failed for: " + username);
            Assert.fail("Exception during login for: " + username);
        }
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
    }

    @AfterSuite
    public void closeSuite() {
        if (driver != null) {
            driver.quit();   // ✅ quit only after all tests in suite finish
        }
    }

}
