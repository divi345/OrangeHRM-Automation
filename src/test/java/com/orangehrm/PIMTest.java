package com.orangehrm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.util.ScreenshotUtil;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

import java.time.Duration;



public class PIMTest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;
    PIMPage pim;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Reports
        new File(System.getProperty("user.dir") + "/reports/screenshots/").mkdirs();
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/PIM_Report.html");
        spark.config().setReportName("OrangeHRM PIM Automation Report");
        spark.config().setDocumentTitle("PIM Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project", "OrangeHRM");
        extent.setSystemInfo("Tester", "Divyasree");

        // Login
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        pim = new PIMPage(driver);
    }

    @Test(priority = 1)
    public void addEmployeeTest() {
        test = extent.createTest("Add Employee Test");
        try {
            pim.openPIM();
            Thread.sleep(2000); 

            pim.clickAddEmployee();
            Thread.sleep(2000); 

            pim.enterEmployeeDetails("Divya", "Sri", "Surya", "03991");
            Thread.sleep(2000); 

            pim.saveEmployee();
            Thread.sleep(2000); 

            Assert.assertTrue(pim.isPersonalDetailsDisplayed(), "Personal Details page not displayed!");
            test.pass("Employee added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "AddEmployee"));
        } catch (Exception e) {
            test.fail("Failed to add employee: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "AddEmployee_Error"));
            Assert.fail("Add Employee test failed");
        }
    }


    @Test(priority = 2)
    public void fillPersonalDetailsTest() {
        test = extent.createTest("Fill Personal Details");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Personal Details tab
       
            // Other Id
            WebElement otherId = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Other Id']/ancestor::div[contains(@class,'oxd-input-group')]//input")));
            otherId.clear();
            otherId.sendKeys("OIY123");
            Thread.sleep(1000);

            // Driver’s License Number
            WebElement licenseNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[normalize-space(text())='Driver License Number']/following::input[1]")));
            licenseNumber.clear();
            licenseNumber.sendKeys("DL9876543210");
            Thread.sleep(1000);

            // Nationality
            driver.findElement(By.xpath("//label[text()='Nationality']/../following-sibling::div//i")).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Indian']"))).click();
            Thread.sleep(1000);

            // Marital Status
            driver.findElement(By.xpath("//label[text()='Marital Status']/../following-sibling::div//i")).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Single']"))).click();
            Thread.sleep(1000);

            // Gender
            WebElement gender = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Female']")));
            gender.click();
            Thread.sleep(1000);

            // First Save
            WebElement firstSaveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@type='submit'])[1]")));
            firstSaveBtn.click();
            Thread.sleep(2000);

            // Blood Type
            driver.findElement(By.xpath("//label[text()='Blood Type']/../following-sibling::div//i")).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='O+']"))).click();
            Thread.sleep(1000);

            // Custom Field
            WebElement customField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Test_Field']/../following-sibling::div/input")));
            customField.clear();
            customField.sendKeys("Sample Field Value");
            Thread.sleep(1000);

            // Second Save
            WebElement secondSaveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@type='submit'])[2]")));
            secondSaveBtn.click();
            Thread.sleep(2000);

            // Confirm Save (Blood type visible after save)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='O+']")));

            test.pass("Personal details updated successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "PersonalDetails"));

        } catch (Exception e) {
            test.fail("Failed to update personal details: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "PersonalDetails_Error"));
            Assert.fail("Personal Details test failed");
        }
    }


    @Test(priority = 3)
    public void contactDetailsTest() throws InterruptedException {
        test = extent.createTest("Contact Details");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Go to Contact Details tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Contact Details"))).click();
            Thread.sleep(2000);

            // Street 1
            WebElement street1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Street 1']/../following-sibling::div/input")));
            street1.clear();
            street1.sendKeys("1st Street");
            Thread.sleep(2000);

            // Street 2
            WebElement street2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Street 2']/../following-sibling::div/input")));
            street2.clear();
            street2.sendKeys("2nd Cross");
            Thread.sleep(2000);

            // City
            WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='City']/../following-sibling::div/input")));
            city.clear();
            city.sendKeys("Vellore");
            Thread.sleep(2000);

            // State/Province
            WebElement state = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='State/Province']/../following-sibling::div/input")));
            state.clear();
            state.sendKeys("Tamil Nadu");
            Thread.sleep(2000);

            // Zip/Postal Code
            WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Zip/Postal Code']/../following-sibling::div/input")));
            zip.clear();
            zip.sendKeys("632001");
            Thread.sleep(2000);

            // Country
            driver.findElement(By.xpath("//label[text()='Country']/../following-sibling::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='India']"))).click();
            Thread.sleep(2000);

            // Home Phone
            WebElement homePhone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Home']/../following-sibling::div/input")));
            homePhone.clear();
            homePhone.sendKeys("0416-123456");
            Thread.sleep(2000);

            // Mobile Phone
            WebElement mobile = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Mobile']/../following-sibling::div/input")));
            mobile.clear();
            mobile.sendKeys("9876543210");
            Thread.sleep(2000);

            // Work Phone
            WebElement workPhone = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Work']/../following-sibling::div/input")));
            workPhone.clear();
            workPhone.sendKeys("0416-654321");
            Thread.sleep(2000);

            // Work Email
            WebElement workEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Work Email']/../following-sibling::div/input")));
            workEmail.clear();
            workEmail.sendKeys("addivi@company.com");
            Thread.sleep(2000);

            // Other Email
            WebElement otherEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Other Email']/../following-sibling::div/input")));
            otherEmail.clear();
            otherEmail.sendKeys("addivi@example.com");
            Thread.sleep(2000);

            // Save Contact Details
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit']")));
            saveBtn.click();

            test.pass("Contact details saved successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "ContactDetails"));

        } catch (Exception e) {
            test.fail("Failed in Contact details: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "ContactDetails_Error"));
        }
    }


    @Test(priority = 4)
    public void emergencyContactsTest() throws InterruptedException {
        test = extent.createTest("Emergency Contacts");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Navigate to Emergency Contacts tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Emergency Contacts"))).click();

            // 2️⃣ Click Add button
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Add']")));
            addBtn.click();
            Thread.sleep(2000);

            // 3️⃣ Fill Name
            WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Name']/../following-sibling::div/input")));
            name.sendKeys("Joe");
            Thread.sleep(2000);

            // 4️⃣ Fill Relationship
            WebElement relationship = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Relationship']/../following-sibling::div/input")));
            relationship.sendKeys("Brother");
            Thread.sleep(2000);

            // 5️⃣ Home Telephone
            WebElement homeTel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Home Telephone']/../following-sibling::div/input")));
            homeTel.sendKeys("0416-123456");
            Thread.sleep(2000);

            // 6️⃣ Mobile
            WebElement mobile = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Mobile']/../following-sibling::div/input")));
            mobile.sendKeys("9876543210");
            Thread.sleep(2000);

            // 7️⃣ Work Telephone
            WebElement workTel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Work Telephone']/../following-sibling::div/input")));
            workTel.sendKeys("0416-654321");
            Thread.sleep(2000);

            // 8️⃣ Click Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();

            test.pass("Emergency contact added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "EmergencyContacts"));

        } catch (Exception e) {
            test.fail("Failed to add emergency contact: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "EmergencyContacts_Error"));
        }
    }


    @Test(priority = 5)
    public void dependentsTest() throws InterruptedException {
        test = extent.createTest("Dependents");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Navigate to Dependents tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Dependents"))).click();

            // 2️⃣ Click Add button
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Add'])[1]")));
            addBtn.click();
            Thread.sleep(2000);

            // 3️⃣ Fill Name
            WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Name']/../following-sibling::div/input")));
            name.sendKeys("Pradeep");
            Thread.sleep(2000);

            // 4️⃣ Select Relationship
            driver.findElement(By.xpath("//label[text()='Relationship']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Child']"))).click();
            Thread.sleep(2000);

            // 6️⃣ Click Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();

            test.pass("Dependent added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Dependents"));

        } catch (Exception e) {
            test.fail("Failed to add dependent: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Dependents_Error"));
        }
    }

   


    @Test(priority = 6)
    public void immigrationRecordsTest() {
        test = extent.createTest("Immigration Records");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Immigration tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Immigration"))).click();
            Thread.sleep(1000);

            // 2️⃣ Add button
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Add'])[1]")));
            addBtn.click();
            Thread.sleep(1000);

            // 3️⃣ Document Type - Select Visa (radio button)
            WebElement visaOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(.,'Visa')]")));
            visaOption.click();
            Thread.sleep(1000);

            // 4️⃣ Number
            WebElement number = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Number']/../following-sibling::div/input")));
            number.clear();
            number.sendKeys("1234567");
            Thread.sleep(1000);

            // 5️⃣ Eligible Status
            WebElement eligibleStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Eligible Status']/../following-sibling::div/input")));
            eligibleStatus.clear();
            eligibleStatus.sendKeys("Valid");
            Thread.sleep(1000);

            // 6️⃣ Issued By (dropdown)
            WebElement issuedByDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Issued By']/../following-sibling::div//div[contains(@class,'oxd-select-text')]")));
            issuedByDropdown.click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='listbox']//span[text()='India']"))).click();
            Thread.sleep(1000);

            // 7️⃣ Comments
            WebElement comments = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Comments']/../following-sibling::div/textarea")));
            comments.clear();
            comments.sendKeys("Valid passport issued by Govt.");
            Thread.sleep(1000);

            // 8️⃣ Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();
            Thread.sleep(1000);

            test.pass("Immigration record added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "ImmigrationRecords"));

        } catch (Exception e) {
            test.fail("Failed to add immigration record: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "ImmigrationRecords_Error"));
        }
    }


    @Test(priority = 7)
    public void jobDetailsTest() throws InterruptedException {
        test = extent.createTest("Job Details");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Open Job tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Job"))).click();
            Thread.sleep(2000);

            // 2️⃣ Job Title
            WebElement jobTitleDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Job Title']/../following-sibling::div//i")));
            jobTitleDropdown.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='QA Engineer']"))).click(); // change as needed
            Thread.sleep(1000);

            // 3️⃣ Job Category
            WebElement jobCategoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Job Category']/../following-sibling::div//i")));
            jobCategoryDropdown.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Professionals']"))).click();
            Thread.sleep(1000);

            // 4️⃣ Sub Unit
            WebElement subUnitDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Sub Unit']/../following-sibling::div//i")));
            subUnitDropdown.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Engineering']"))).click();
            Thread.sleep(1000);

            // 5️⃣ Location
            WebElement locationDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Location']/../following-sibling::div//i")));
            locationDropdown.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Texas')]"))).click();
            Thread.sleep(1000);

            // 6️⃣ Employment Status
            WebElement empStatusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Employment Status']/../following-sibling::div//i")));
            empStatusDropdown.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Full-Time Permanent']"))).click();
            Thread.sleep(1000);

            // 7️⃣ Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();
            Thread.sleep(2000);

            test.pass("Job details updated successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "JobDetails"));

        } catch (Exception e) {
            test.fail("Failed to update job details: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "JobDetails_Error"));
            Assert.fail("Job Details test failed");
        }
    }


    @Test(priority = 8)
    public void salaryComponentsTest() throws InterruptedException {
        test = extent.createTest("Salary Components");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Navigate to Salary tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Salary"))).click();
            Thread.sleep(2000);

            // 2️⃣ Click Add button
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Add']")));
            addBtn.click();
            Thread.sleep(2000);

            WebElement salaryComponent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Salary Component']/../following-sibling::div/input")));
            salaryComponent.clear();
            salaryComponent.sendKeys("Test Field");   // or "Basic"
            Thread.sleep(2000);


            // 4️⃣ Select Pay Grade
            driver.findElement(By.xpath("//label[text()='Pay Grade']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Grade 1']"))).click();
            Thread.sleep(2000);

            // 5️⃣ Select Pay Frequency
            driver.findElement(By.xpath("//label[text()='Pay Frequency']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Monthly']"))).click();
            Thread.sleep(2000);

            // 6️⃣ Select Currency
            driver.findElement(By.xpath("//label[text()='Currency']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='United States Dollar']"))).click();
            Thread.sleep(2000);

            // 7️⃣ Enter Amount
            WebElement amount = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Amount']/../following-sibling::div/input")));
            amount.clear();
            amount.sendKeys("50001");
            Thread.sleep(2000);

            // 8️⃣ Comments
            WebElement comments = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Comments']/../following-sibling::div/textarea")));
            comments.sendKeys("Monthly salary component");
            Thread.sleep(2000);

            // 🔟 Click Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();

            test.pass("Salary component added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "SalaryComponents"));

        } catch (Exception e) {
            test.fail("Failed to add salary component: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "SalaryComponents_Error"));
        }
    }


    @Test(priority = 9)
    public void supervisorsTest() throws InterruptedException {
        test = extent.createTest("Assigned Supervisors");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Navigate to "Report-to" tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Report-to"))).click();
            Thread.sleep(2000);

            // 2️⃣ Click the FIRST Add button (specific to Supervisor section)
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Add'])[1]")));
            addBtn.click();
            Thread.sleep(2000);

            // 3️⃣ Enter Name (do not select suggestions)
            WebElement name = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Name']/ancestor::div[contains(@class,'oxd-input-group')]//input")));
            name.sendKeys("John Smith");
            Thread.sleep(2000);

            // 4️⃣ Select Reporting Method
            WebElement reportingDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Reporting Method']/../following-sibling::div//i")));
            reportingDropdown.click();
            Thread.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Direct']"))).click();
            Thread.sleep(2000);

            // 5️⃣ Click Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();

            test.pass("Supervisor added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Supervisors"));

        } catch (Exception e) {
            test.fail("Failed to add supervisor: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Supervisors_Error"));
        }
    }


    @Test(priority = 10)
    public void qualificationsTest() throws InterruptedException {
        test = extent.createTest("Qualifications Section");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // ================== Navigate to Qualifications tab ==================
            WebElement qualificationsTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Qualifications')]")));
            qualificationsTab.click();
            Thread.sleep(2000);

            // ================== 1. Add Work Experience ==================
            WebElement addWorkExp = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h6[text()='Work Experience']/following::button[normalize-space()='Add'][1]")));
            addWorkExp.click();
            Thread.sleep(2000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[contains(text(),'Company')]/following::input[1]"))).sendKeys("Google");
            driver.findElement(By.xpath("//label[contains(text(),'Job Title')]/following::input[1]"))
                  .sendKeys("QA Engineer");
            driver.findElement(By.xpath("//label[contains(text(),'Comment')]/following::textarea[1]"))
                  .sendKeys("Worked on automation projects");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']"))).click();
            Thread.sleep(2000);

            // ================== 2. Add Education ==================
         // Add Education
            WebElement addEdu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h6[text()='Education']/following::button[normalize-space()='Add'][1]")));
            addEdu.click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(text(),'Level')]/following::div//i"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()=\"Bachelor's Degree\"]"))).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[contains(text(),'Institute')]/following::input[1]"))).sendKeys("MIT");

            driver.findElement(By.xpath("//label[contains(text(),'Major')]/following::input[1]")).sendKeys("Computer Science");
            driver.findElement(By.xpath("//label[contains(text(),'Year')]/following::input[1]")).sendKeys("2024");
            driver.findElement(By.xpath("//label[contains(text(),'GPA')]/following::input[1]")).sendKeys("8.5");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']"))).click();


            // ================== 3. Add Skill ==================
            WebElement addSkill = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h6[text()='Skills']/following::button[normalize-space()='Add'][1]")));
            addSkill.click();
            Thread.sleep(2000);

            driver.findElement(By.xpath("//label[contains(text(),'Skill')]/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Java']"))).click();

            driver.findElement(By.xpath("//label[contains(text(),'Years of Experience')]/following::input[1]"))
                  .sendKeys("3");
            driver.findElement(By.xpath("//label[contains(text(),'Comments')]/following::textarea[1]"))
                  .sendKeys("Strong in OOP and Selenium automation");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']"))).click();
            Thread.sleep(2000);

            // ================== 4. Add Language ==================
            WebElement addLang = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h6[text()='Languages']/following::button[normalize-space()='Add'][1]")));
            addLang.click();
            Thread.sleep(2000);

            driver.findElement(By.xpath("//label[contains(text(),'Language')]/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='English']"))).click();
            Thread.sleep(2000);

            driver.findElement(By.xpath("//label[contains(text(),'Competency')]/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Good']"))).click();
            Thread.sleep(2000);

            driver.findElement(By.xpath("//label[contains(text(),'Comments')]/following::textarea[1]"))
                  .sendKeys("Fluent in business communication");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']"))).click();
            Thread.sleep(2000);

            // ================== 5. Add License ==================
            WebElement addLicense = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//h6[text()='License']/following::button[normalize-space()='Add'][1]")));
            addLicense.click();
            Thread.sleep(2000);

            driver.findElement(By.xpath("//label[contains(text(),'License Type')]/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Certified Digital Marketing Professional (CDMP)']"))).click();
            Thread.sleep(2000);

            driver.findElement(By.xpath("//label[contains(text(),'License Number')]/following::input[1]"))
                  .sendKeys("DL123456");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']"))).click();
            Thread.sleep(2000);

            // ================== Test pass log ==================
            test.pass("All Qualification details added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Qualifications"));

        } catch (Exception e) {
            test.fail("Failed in Qualifications: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Qualifications_Error"));
            Assert.fail("Qualifications test failed");
        }
    }

    @Test(priority = 11)
    public void membershipsTest() throws InterruptedException {
        test = extent.createTest("Assigned Memberships");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Navigate to Memberships tab
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Memberships"))).click();
            Thread.sleep(2000);

            // 2️⃣ Click Add button
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Add'])[1]")));
            addBtn.click();
            Thread.sleep(2000);

            // 3️⃣ Select Membership
            driver.findElement(By.xpath("//label[text()='Membership']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='ACCA']"))).click();
            Thread.sleep(2000);

            // 4️⃣ Select Subscription Paid By
            driver.findElement(By.xpath("//label[text()='Subscription Paid By']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Company']"))).click();
            Thread.sleep(2000);

            // 5️⃣ Enter Subscription Amount
            WebElement amount = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[text()='Subscription Amount']/../following-sibling::div/input")));
            amount.clear();
            amount.sendKeys("1000");
            Thread.sleep(2000);

            // 6️⃣ Select Currency
            driver.findElement(By.xpath("//label[text()='Currency']/../following-sibling::div//i")).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Indian Rupee']"))).click();
            Thread.sleep(2000);

            // 7️⃣ Click Save
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Save']")));
            saveBtn.click();

            test.pass("Membership added successfully")
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Memberships"));

        } catch (Exception e) {
            test.fail("Failed to add membership: " + e.getMessage())
                .addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(driver, "Memberships_Error"));
        }
       }
    
    
    @Test(priority = 12)
    public void employeeListSearchTest() {
        String employeeName = "Divya Sri Surya"; 
        test = extent.createTest("Employee List Search");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Navigate to PIM > Employee List
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("PIM"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Employee List"))).click();

            // Wait for filters to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Employee Name']")));

            // Enter Employee Name
            WebElement empName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Employee Name']/../following-sibling::div//input")));
            empName.clear();
            empName.sendKeys(employeeName);

            // Employment Status
            driver.findElement(By.xpath("//label[text()='Employment Status']/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Full-Time Permanent']"))).click();

            // Include
            driver.findElement(By.xpath("//label[text()='Include']/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Current and Past Employees']"))).click();

            // Job Title
            driver.findElement(By.xpath("//label[text()='Job Title']/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='QA Engineer']"))).click();

            // Sub Unit
            driver.findElement(By.xpath("//label[text()='Sub Unit']/following::div//i")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Engineering']"))).click();

            // Click Search
            driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

            // Wait until table results or "No Records Found"
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='oxd-table-body']")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'No Records Found')]"))
            ));

            // Get the table body text
            WebElement tableBody = driver.findElement(By.xpath("//div[@class='oxd-table-body']"));
            String tableText = tableBody.getText();

            // Always capture screenshot after search
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EmployeeListSearch");

            if (tableText.contains(employeeName)) {
                test.pass("✅ Employee '" + employeeName + "' found in Employee List")
                    .addScreenCaptureFromPath(screenshotPath);
            } else {
                String extra = "";
                try {
                    WebElement noRec = driver.findElement(By.xpath("//*[contains(text(),'No Records Found')]"));
                    if (noRec.isDisplayed()) {
                        extra = " - UI shows 'No Records Found'";
                    }
                } catch (Exception ignore) {}

                test.fail("❌ Employee '" + employeeName + "' not found in Employee List" + extra)
                    .addScreenCaptureFromPath(screenshotPath);

                Assert.fail("Employee '" + employeeName + "' not found in Employee List" + extra);
            }

        } catch (Exception e) {
            // On exception → screenshot + fail log
            String exScreenshot = ScreenshotUtil.captureScreenshot(driver, "EmployeeListSearch_Exception");
            test.fail("⚠️ Exception during Employee List Search: " + e.getMessage())
                .addScreenCaptureFromPath(exScreenshot);
            e.printStackTrace();
            Assert.fail("Employee List Search Test Failed: " + e.getMessage());
        }
         
    }

 
    

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        extent.flush();
    }
}
