package com.util;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class ScreenshotUtil {

    // Capture screenshot and return the path
    public static String captureScreenshot(WebDriver driver, String fileName) {
        // Add timestamp to make filenames unique
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotPath = System.getProperty("user.dir")
                + "/reports/screenshots/" + fileName + "_" + timestamp + ".png";

        try {
            // Small delay to allow UI to render
            Thread.sleep(1000);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return screenshotPath;
    }
}
