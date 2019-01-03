package Base;

import Support.Support;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.*;

import java.util.Arrays;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeClass()
    public void classStartup(){
        EnvironmentConfiguration _environment = new EnvironmentConfiguration(driver, url);
        TestConfiguration.setDriver(_environment.getDriver());
        TestConfiguration.setUrl(url);

        Support.LOGGER.info(String.format("--->EXECUTING TEST CLASS '%s' ON %s",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName()));
    }


    @BeforeTest
    public void beforeTest(ITestContext testContext){
        Support.LOGGER.info(String.format("--->EXECUTING TEST CLASS '%s' ON %s",
                testContext.getName(), TestConfiguration.getBrowserName()));
    }


    @AfterMethod
    public void afterMethod(ITestResult tr){
        String _testName = tr.getName();
        int _status = tr.getStatus();

        switch (_status){
            case 1: //Success
                Support.LOGGER.info(String.format("Test '%s' Passed", _testName));
                break;
            case 2: //Failure
                Support.LOGGER.error(String.format("Test '%s' Failed", _testName));
                Support.takeScreenshot(_testName);
                TestConfiguration.getDriver().navigate().to(TestConfiguration.getURL()); //Ensures a clean start in case of fatal failures in the tested web app.
                break;
            default:
                Support.LOGGER.warn(String.format("Test '%s' Finished. Reason: '%d'", _testName, _status));

        }
    }


    @AfterClass
    public void classTearDown(){
        Support.LOGGER.info(String.format("--->FINISHED EXECUTING TEST CLASS '%s' ON %s",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName()));
    }



}
