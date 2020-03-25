package Base;

import Support.Support;
import org.apache.commons.collections.map.HashedMap;
import org.openqa.selenium.MutableCapabilities;
import org.testng.*;
import org.testng.reporters.jq.ReporterPanel;
import org.testng.util.Strings;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DefaultListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Support.LOGGER.info(String.format("--->EXECUTING TEST '%s'",  result.getName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Support.LOGGER.info(String.format("--->TEST '%s' PASSED", result.getName()));
        List<String> tstRst = Reporter.getOutput(result);
        Reporter.setCurrentTestResult(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Support.LOGGER.error(String.format("--->TEST '%s' FAILED", result.getName()));
        Support.takeScreenshot();
        addScreenshotsToReport(result);
        Support.clearScreenshotsList();
        Reporter.setCurrentTestResult(result);

        //Ensures a clean start in case of fatal failures happen in the tested web app.
        TestConfiguration.getDriver().navigate().to(TestConfiguration.getURL());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Support.LOGGER.warn(String.format("--->TEST '%s' SKIPPED", result.getName()));
        Reporter.setCurrentTestResult(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Support.LOGGER.warn(String.format("--->TEST '%s' FAILED BUT WITHIN SUCCESS PERCENTAGE", result.getName()));
        Reporter.setCurrentTestResult(result);
    }

    @Override
    public void onStart(ITestContext context) {
        String browser = context.getCurrentXmlTest().getParameter("Browser");
        String url = context.getCurrentXmlTest().getParameter("Url");
        String hubUrl = context.getCurrentXmlTest().getParameter("HubUrl");
        String capabilities = context.getCurrentXmlTest().getParameter("Capabilities");

        BrowserConfiguration _configuration = null;

        try {
            if(Strings.isNullOrEmpty(hubUrl))
                _configuration = Strings.isNullOrEmpty(capabilities) ?
                        new BrowserConfiguration(browser, url) :
                        new BrowserConfiguration(browser, url, setCapabilities(capabilities));
            else
                _configuration = new BrowserConfiguration(hubUrl, url, setCapabilities(capabilities));
        } catch (MalformedURLException e) {
            Support.LOGGER.fatal("An invalid URL has been provided", e);
        }
        TestConfiguration.setDriver(_configuration.getDriver());
        TestConfiguration.setUrl(url);
        Support.LOGGER.info("*******STARTING TEST SUITE '" + context.getName().toUpperCase() +
                "' ON " + TestConfiguration.getBrowserName().toUpperCase() + " DRIVER*******");
        if(Objects.nonNull(capabilities))
            setCapabilities(capabilities).forEach((parameter, value) ->
                    Support.LOGGER.info(String.format("%s : %s",parameter, value)));
    }

    @Override
    public void onFinish(ITestContext context) {
        TestConfiguration.getDriver().quit();
        Support.LOGGER.info("*******FINISHED TEST SUITE '" + context.getName().toUpperCase() +
                "' ON " + TestConfiguration.getBrowserName().toUpperCase() + " DRIVER*******");
    }


    private Map<String,?> setCapabilities(String capabilities){
        Map caps = new HashMap<String,String>();
        String[] allCapturedCaps = capabilities.split(";");
        try{
            Arrays.stream(allCapturedCaps).forEach(cap -> {
                String[] keyValuePair = cap.split(":");
                caps.put(keyValuePair[0], keyValuePair[1]);
            });
            return caps;
        }catch(Exception ex){
            throw new RuntimeException("There was an error parsing the provided capabilities", ex.getCause());
        }
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
