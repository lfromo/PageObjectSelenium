package Support;

import Base.TestConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

public class Support {

    public static final Logger LOGGER = LogManager.getLogger(Support.class);
    public static final String SCREENSHOTS_PATH = "C:\\source\\ScreenShots\\General";

    public static Path takeScreenshot(String fileName){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("resources\\general.properties"));
        } catch (IOException e) {
            LOGGER.fatal("Could not load the properties file", e.getCause());
        }

        Date currentDate = new Date();
        String baseUrl = properties.getProperty("screenshotPath", SCREENSHOTS_PATH);
        Path baseDir = Paths.get(baseUrl);

        TakesScreenshot photographer = (TakesScreenshot) TestConfiguration.getDriver();
        File screenshot = photographer.getScreenshotAs(OutputType.FILE);
        boolean renamed = screenshot.renameTo(new File(fileName + "_" +  currentDate.getTime() +".png"));

        if(!renamed)
            LOGGER.warn(String.format("The screenshot could not be renamed. The file name is:'%s'", screenshot.getName()));

        try {
            if(!Files.exists(baseDir))
                Files.createDirectory(baseDir);
            return Files.move(screenshot.toPath(), baseDir);
        } catch (IOException e) {
           LOGGER.fatal("There was an error saving the screenshot", e.getCause());
           return null;
        }
    }

    public static void javaScriptClick(WebElement element){
        getJavaScriptExecutor().executeScript("arguments[0].click();", element);
    }

    public static String extractTextWithJavaScript(WebElement element){
        return getJavaScriptExecutor()
                .executeScript("return arguments[0].textContent;", element)
                .toString();
    }

    public static JavascriptExecutor getJavaScriptExecutor(){
        return (JavascriptExecutor) TestConfiguration.getDriver();
    }



}
