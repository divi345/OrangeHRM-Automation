# OrangeHRM Automation Project

## Project Overview
This project automates key workflows in the OrangeHRM web application using Selenium WebDriver, Java, and TestNG.  
It covers essential HR functionalities such as login, leave management, employee management, and generates detailed reports with screenshots for test validation.  

The project follows the **Page Object Model (POM)** design pattern for better maintainability and reusability of code.

---

## Features
- Login and logout automation  
- Add, edit, and delete leave entitlements  
- Employee list search and validation  
- Screenshot capture for each test step  
- HTML/ExtentReports for detailed test results  
- TestNG framework for structured test execution  

---

## Technology Stack
- **Programming Language:** Java  
- **Automation Tool:** Selenium WebDriver  
- **Test Framework:** TestNG  
- **Reporting:** ExtentReports  
- **IDE:** Eclipse  
- **Version Control:** Git & GitHub  
- **Build Tool (Optional):** Maven/Gradle  

---

## Folder Structure


### **Explanation**
- **`/src`**: Contains all automation scripts organized using **Page Object Model**. Separating pages, tests, and utilities demonstrates clean coding practices.  
- **`/screenshots`**: Provides evidence for test steps and failures, improving report clarity.  
- **`/reports`**: HTML/ExtentReports for **professional test results** with embedded screenshots.  
- **`/lib`**: External libraries for easy project setup.  
- **`.gitignore`**: Keeps the repository clean by ignoring unnecessary files like binaries and IDE configs.  
- **`pom.xml / build.gradle`**: Build automation configuration (if used).  
- **`testng.xml`**: Defines the test suite and execution sequence.  

---

## How to Run
1. Clone the repository:  
```bash
git clone https://github.com/username/OrangeHRMAutomation.git
