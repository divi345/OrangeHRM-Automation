package com.orangehrm;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.util.ScreenshotUtil;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.time.Duration;

public class LeavePage {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Reports folder
        new File(System.getProperty("user.dir") + "/reports/screenshots/").mkdirs();
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/Leave_Report.html");
        spark.config().setReportName("OrangeHRM Leave Automation Report");
        spark.config().setDocumentTitle("Leave Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project", "OrangeHRM");
        extent.setSystemInfo("Tester", "Divyasree");

        // Login
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test(priority = 1)
    public void openLeavePage() {
        test = extent.createTest("Open Leave Page");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement leaveMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Leave")));
            leaveMenu.click();
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "LeavePage_Open");
            test.pass("Leave page opened successfully").addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "LeavePage_Fail");
            test.fail("Failed to open Leave page: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            Assert.fail("Leave Page Test Failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void validateApplyLeavePage() {
        test = extent.createTest("Validate Apply Leave Page");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement applyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Apply')]")));
            applyBtn.click();

            WebElement applyLeaveHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h6[text()='Apply Leave']")));
            Assert.assertEquals(applyLeaveHeading.getText(), "Apply Leave");

            WebElement noLeaveMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[text()='No Leave Types with Leave Balance']")));
            Assert.assertEquals(noLeaveMsg.getText(), "No Leave Types with Leave Balance");

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "ApplyLeavePage_Success");
            test.pass("Apply Leave page verified successfully").addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "ApplyLeavePage_Fail");
            test.fail("Apply Leave Page Test failed: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            Assert.fail("Apply Leave Page Test Failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void validateMyLeavePage() {
        test = extent.createTest("Validate My Leave Page Opens Successfully");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement myLeaveMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("My Leave")));
            myLeaveMenu.click();

            WebElement myLeaveHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h5[normalize-space()='My Leave']")));
            Assert.assertTrue(myLeaveHeading.isDisplayed(), "My Leave page is not displayed");

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "MyLeave_Page");
            test.pass("My Leave page opened successfully").addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "MyLeave_Page_Fail");
            test.fail("My Leave page validation failed: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            Assert.fail("My Leave Page Test Failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void addIndividualEntitlement() {
        test = extent.createTest("Add Leave Entitlement - Individual Employee");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            WebElement entitlementMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[.//span[contains(text(),'Entitlements')]]")));
            actions.moveToElement(entitlementMenu).perform();

            WebElement addEntitlement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[.//span[contains(text(),'Entitlements')]]//ul//a[contains(text(),'Add Entitlement')]")));
            js.executeScript("arguments[0].click();", addEntitlement);

            WebElement individualRadio = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[normalize-space()='Individual Employee']")));
            individualRadio.click();

            WebElement empName = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Type for hints...']")));
            empName.clear();
            empName.sendKeys("Divya Sri Surya");

            WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='listbox']//span[contains(text(),'Divya Sri Surya')]")));
            js.executeScript("arguments[0].click();", suggestion);

            WebElement leaveType = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='-- Select --']")));
            leaveType.click();
            WebElement leaveTypeOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='option' and normalize-space()='US - Personal']")));
            leaveTypeOption.click();

            WebElement entitlement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Entitlement']/ancestor::div[contains(@class,'oxd-input-group')]//input")));
            entitlement.clear();
            entitlement.sendKeys("12");

            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            js.executeScript("arguments[0].click();", saveBtn);

            WebElement popupMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'oxd-dialog-container')]//p")));
            Assert.assertTrue(popupMsg.getText().contains("will be updated"));

            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Confirm']")));
            js.executeScript("arguments[0].click();", confirmBtn);

            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'oxd-table-card')]")));
            Assert.assertTrue(row.isDisplayed(), "Entitlement row not found!");

            test.pass("Individual Employee Entitlement added successfully");
        } catch (Exception e) {
            test.fail("Failed Individual Employee Entitlement: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void addMultipleEntitlement() {
        test = extent.createTest("Add Leave Entitlement - Multiple Employees");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            WebElement entitlementMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[.//span[contains(text(),'Entitlements')]]")));
            actions.moveToElement(entitlementMenu).perform();

            WebElement addEntitlement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[.//span[contains(text(),'Entitlements')]]//ul//a[contains(text(),'Add Entitlement')]")));
            js.executeScript("arguments[0].click();", addEntitlement);

            WebElement multipleRadio = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Multiple Employees']/preceding-sibling::input")));
            multipleRadio.click();

            WebElement location = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='location']")));
            location.sendKeys("Texas R&D");

            WebElement subUnit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='subunit']")));
            subUnit.sendKeys("Engineering");

            WebElement leaveType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='leaveType']")));
            leaveType.sendKeys("US - FMLA");

            WebElement entitlement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='entitlement']")));
            entitlement.clear();
            entitlement.sendKeys("14");

            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Save']")));
            js.executeScript("arguments[0].click();", saveBtn);

            WebElement popupMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Matching Employees')]")));
            Assert.assertTrue(popupMsg.getText().contains("Updating Entitlement - Matching Employees"));

            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Confirm']")));
            js.executeScript("arguments[0].click();", confirmBtn);

            WebElement employee1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Russel Hamilton']")));
            WebElement employee2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Rebecca Harmony']")));
            Assert.assertTrue(employee1.isDisplayed() && employee2.isDisplayed());

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "MultipleEntitlement_Success");
            test.pass("Multiple Employee Entitlements added successfully").addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "MultipleEntitlement_Fail");
            test.fail("Failed Multiple Employee Entitlement: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            Assert.fail("Multiple Entitlement Test Failed: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    public void searchLeaveEntitlements() {
        test = extent.createTest("Search Leave Entitlements");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            WebElement entitlementMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[.//span[contains(text(),'Entitlements')]]")));
            actions.moveToElement(entitlementMenu).perform();

            WebElement leaveEntitlementLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[.//span[contains(text(),'Entitlements')]]//a[contains(text(),'Leave Entitlements')]")));
            js.executeScript("arguments[0].click();", leaveEntitlementLink);

            WebElement empName = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Type for hints...']")));
            empName.sendKeys("Divya Sri Surya");

            WebElement leaveType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='leaveType']")));
            leaveType.sendKeys("US - Personal");

            WebElement leavePeriod = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='period']")));
            leavePeriod.sendKeys("2024-01-01 - 2024-31-12");

            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Search']")));
            js.executeScript("arguments[0].click();", searchBtn);

            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'oxd-table-card')]")));
            Assert.assertTrue(row.isDisplayed(), "No record found for entitlement search");

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "LeaveEntitlement_Search");
            test.pass("Leave Entitlement record found successfully").addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "LeaveEntitlement_Search_Fail");
            test.fail("Failed Leave Entitlement Search: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            Assert.fail("Leave Entitlement Search Test Failed: " + e.getMessage());
        }
    }

    @Test(priority = 7)
    public void logoutTest() {
        test = extent.createTest("Logout Test");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//p[@class='oxd-userdropdown-name']")));
            profileMenu.click();

            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[text()='Logout']")));
            js.executeScript("arguments[0].click();", logoutBtn);

            WebElement loginPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='orangehrm-login-container']")));
            Assert.assertTrue(loginPanel.isDisplayed(), "Logout failed - Login page not displayed");

            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Logout_Success");
            test.pass("User logged out successfully").addScreenCaptureFromPath(screenshotPath);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Logout_Fail");
            test.fail("Logout failed: " + e.getMessage()).addScreenCaptureFromPath(screenshotPath);
            Assert.fail("Logout Test Failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
        if (extent != null) extent.flush();
    }
}
