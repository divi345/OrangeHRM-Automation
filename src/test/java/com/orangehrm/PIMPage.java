package com.orangehrm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PIMPage {
    WebDriver driver;

    // Menu
    @FindBy(xpath = "//span[text()='PIM']")
    WebElement pimMenu;

    @FindBy(xpath = "//a[text()='Add Employee']")
    WebElement addEmployeeBtn;

    @FindBy(name = "firstName")
    WebElement firstName;

    @FindBy(name = "middleName")
    WebElement middleName;

    @FindBy(name = "lastName")
    WebElement lastName;

    @FindBy(xpath = "//label[text()='Employee Id']/../following-sibling::div/input")
    WebElement empId;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement saveBtn;

    @FindBy(xpath = "//h6[text()='Personal Details']")
    WebElement personalDetailsHeader;

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openPIM() {
        pimMenu.click();
    }

    public void clickAddEmployee() {
        addEmployeeBtn.click();
    }

    public void enterEmployeeDetails(String fName, String mName, String lName, String id) {
        firstName.sendKeys(fName);
        middleName.sendKeys(mName);
        lastName.sendKeys(lName);
        empId.clear();
        empId.sendKeys(id);
    }

    public void saveEmployee() {
        saveBtn.click();
    }

    public boolean isPersonalDetailsDisplayed() {
        return personalDetailsHeader.isDisplayed();
    }
}
