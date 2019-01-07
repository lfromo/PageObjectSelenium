package Base;

import Support.Support;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

public abstract class BaseTest {


    @BeforeClass()
    public void classStartup(){

       BrowserConfiguration _conf = new BrowserConfiguration("CHROME", "https://www.phptravels.net/");
       TestConfiguration.setDriver(_conf.getDriver());
       TestConfiguration.setUrl("https://www.phptravels.net/");

       Support.LOGGER.info(String.format("--->EXECUTING TEST CLASS '%s' ON %s",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName().toUpperCase()));
    }


    @BeforeTest
    public void beforeTest(ITestContext testContext){
        Support.LOGGER.info(String.format("--->EXECUTING TEST '%s'",  testContext.getName()));
    }


    @AfterMethod
    public void afterMethod(ITestResult tr){
        String _testName = tr.getName();
        int _status = tr.getStatus();

        switch (_status){
            case 1: //Success
                Support.LOGGER.info(String.format("TEST '%s' PASSED", _testName));
                break;
            case 2: //Failure
                Support.LOGGER.error(String.format("TEST '%s' FAILED", _testName));
                //Support.takeScreenshot(_testName);
                TestConfiguration.getDriver().navigate().to(TestConfiguration.getURL()); //Ensures a clean start in case of fatal failures in the tested web app.
                break;
            default:
                Support.LOGGER.warn(String.format("TEST '%s' FINISHED. REASON: '%d'", _testName, _status));
        }
    }


    @AfterClass
    public void classTearDown(){
        TestConfiguration.getDriver().quit();
        Support.LOGGER.info(String.format("--->FINISHED EXECUTING TEST CLASS '%s' ON %s",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName().toUpperCase()));
    }

}
