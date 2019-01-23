package Base;

import Support.Support;
import org.testng.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class DefaultListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Support.LOGGER.info(String.format("--->EXECUTING TEST '%s'",  result.getName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Support.LOGGER.info(String.format("--->TEST '%s' PASSED", result.getName()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Support.LOGGER.error(String.format("--->TEST '%s' FAILED", result.getName()));
        Support.takeScreenshot();
        addScreenshotsToReport(result);
        Support.clearScreenshotsList();

        //Ensures a clean start in case of fatal failures happen in the tested web app.
        TestConfiguration.getDriver().navigate().to(TestConfiguration.getURL());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Support.LOGGER.warn(String.format("--->TEST '%s' SKIPPED", result.getName()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Support.LOGGER.warn(String.format("--->TEST '%s' FAILED BUT WITHIN SUCCESS PERCENTAGE", result.getName()));
    }

    @Override
    public void onStart(ITestContext context) {
        String browser = context.getCurrentXmlTest().getParameter("Browser");
        String url = context.getCurrentXmlTest().getParameter("Url");
        BrowserConfiguration _configuration = new BrowserConfiguration(browser, url);
        TestConfiguration.setDriver(_configuration.getDriver());
        TestConfiguration.setUrl(url);
        Support.LOGGER.info("*******STARTING TEST SUITE '" + context.getName().toUpperCase() +
                "' ON " + TestConfiguration.getBrowserName().toUpperCase() + " DRIVER*******");
    }

    @Override
    public void onFinish(ITestContext context) {
        TestConfiguration.getDriver().quit();
        Support.LOGGER.info("*******FINISHED TEST SUITE '" + context.getName().toUpperCase() +
                "' ON " + TestConfiguration.getBrowserName().toUpperCase() + " DRIVER*******");
    }


    private void addScreenshotsToReport(ITestResult result){
        String screenshotFileName = TestConfiguration.getBrowserName().toUpperCase() + "_" + result.getName();
        String path = result.getTestContext().getOutputDirectory();
        Path baseDir = Paths.get(path);

        Support.getStoredScreenshots().forEach(shot ->{
            File imgFile = new File(path, screenshotFileName + "_" + new Date().getTime() + ".png");

            try {
                if(!Files.exists(baseDir))
                    Files.createDirectory(baseDir);
                ImageIO.write(shot.getImage(),"png", imgFile);
                Reporter.log(String.format("<a href=\"%s\" title=\"%s\"><img src=\"%1$s\" height='100' width='150'/></a>", imgFile.getPath(), result.getName()));
            } catch (IOException e) {
                Support.LOGGER.fatal(String.format("There was an error saving the screenshot for Test %s:\n %s",
                        result.getName(), e.getMessage()));
            }
        });
    }
}