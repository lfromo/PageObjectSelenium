package Base;

import Support.Support;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;


@Test(dataProvider = "setEnvironmentDefault", dataProviderClass = TestConfiguration.class)
public abstract class BaseTest {

    private final String browserName;
    protected final WebDriver _driver;
    protected final String _url;


    public BaseTest(String driver, String url){
        EnvironmentConfiguration _environment = new EnvironmentConfiguration(driver, url);
        this._driver = _environment.getDriver();
        this._url = url;
        browserName = ((RemoteWebDriver) _driver).getCapabilities()
                .getBrowserName().toUpperCase();

        TestConfiguration.setDriver(_driver);
        TestConfiguration.setUrl(_url);
    }


    @BeforeClass
    public void classSetup(){
        Support.LOGGER.info("--->EXECUTING TEST CLASS ON " + browserName);
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
                Support.LOGGER.error(String.format("Test '%s' Failed", _testName), tr.getThrowable());
                Support.takeScreenshot(_testName);

                //Always guarantee a clean start in case of fatal failures in the web app being tested.
                _driver.navigate().to(_url);

                break;
            default:
                Support.LOGGER.warn(String.format("Test '%s' Finished. Reason: '%d'", _testName, tr.getStatus()));

        }
    }


    @AfterClass
    public void classTearDown(){

        Support.LOGGER.info("--->FINISHED EXECUTING TEST CLASS ON " + browserName);
    }



}
