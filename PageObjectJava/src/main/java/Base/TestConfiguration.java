package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IInvokedMethod;

public class TestConfiguration {

    private static WebDriver DRIVER;
    private static String URL;

    protected static void setDriver(WebDriver driver){
        DRIVER = driver;
    }

    public static WebDriver getDriver(){
        return DRIVER;
    }

    protected static void setUrl(String url){
        URL = url;
    }

    public static String getURL(){
        return URL;
    }

    public static String getBrowserName(){
        return ((RemoteWebDriver) DRIVER).getCapabilities().getBrowserName();
    }

}
