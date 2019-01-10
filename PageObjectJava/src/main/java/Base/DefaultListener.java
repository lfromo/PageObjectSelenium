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
        System.out.println(String.format("--->EXECUTING TEST '%s'",  result.getName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(String.format("--->TEST '%s' PASSED", result.getName()));
        addScreenshotsToReport(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(String.format("--->TEST '%s' FAILED", result.getName()));
        addScreenshotsToReport(result);

        //Ensures a clean start in case of fatal failures happen in the tested web app.
        TestConfiguration.getDriver().navigate().to(TestConfiguration.getURL());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(String.format("--->TEST '%s' SKIPPED", result.getName()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("PRINT FROM LISTENER: onTestFailedButWithinSuccessPercentage");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("PRINT FROM LISTENER: onStart");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("PRINT FROM LISTENER: onFinish");
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
                Reporter.log(String.format("<a href=\"%s\" title=\"%s\"><img src=\"%1$s\" height='100' width='100'/></a>", imgFile.getPath(), result.getName()));
            } catch (IOException e) {
                Support.LOGGER.fatal("There was an error saving the screenshot for Test %s:\n %s",
                        result.getName(), e.getMessage());
            }
        });
    }
}
