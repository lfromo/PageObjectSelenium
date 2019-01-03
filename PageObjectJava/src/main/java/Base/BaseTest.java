package Base;

import Support.Support;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

public abstract class BaseTest {

    private final String _browserName;
    protected final WebDriver _driver;
    protected final String _url;

    @Factory(dataProvider = "setEnvironmentDefault", dataProviderClass = TestConfiguration.class)
    public BaseTest(String driver, String url){
        EnvironmentConfiguration _environment = new EnvironmentConfiguration(driver, url);
        this._driver = _environment.getDriver();
        this._url = url;
        this._browserName = ((RemoteWebDriver) _driver).getCapabilities()
                .getBrowserName().toUpperCase();

        TestConfiguration.setDriver(_driver);
        TestConfiguration.setUrl(_url);
    }


    @BeforeClass
    public void classStartup(){
        Support.LOGGER.info(String.format("--->EXECUTING TEST CLASS '%s' ON %s",
                this.getClass().getCanonicalName(), _browserName));
    }

    @AfterMethod
    public void afterMethod(){
        String _testName = "TestName";//tr.getName();
        int _status = 1; //tr.getStatus();

        switch (_status){
            case 1: //Success
                Support.LOGGER.info(String.format("Test '%s' Passed", _testName));
                break;
            case 2: //Failure
                Support.LOGGER.error(String.format("Test '%s' Failed", _testName));
                Support.takeScreenshot(_testName);
                _driver.navigate().to(_url); //Ensures a clean start in case of fatal failures in the tested web app.
                break;
            default:
                Support.LOGGER.warn(String.format("Test '%s' Finished. Reason: '%d'", _testName, 1));

        }
    }


    @AfterClass
    public void classTearDown(){
        Support.LOGGER.info(String.format("--->FINISHED EXECUTING TEST CLASS '%s' ON %s",
                this.getClass().getCanonicalName(), _browserName));
    }



}
