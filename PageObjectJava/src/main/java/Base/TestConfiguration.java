package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestConfiguration {

    private static WebDriver DRIVER;
    private static String URL;


    @DataProvider(name = "setEnvironmentDefault")
    public static Iterator<Object[]> setEnvironment(){
        List<Object[]> params = new ArrayList<>();
        params.add(new Object[] { "CHROME", "https://www.phptravels.net"});
        //params.add(new Object[] { "IE11", "https://www.phptravels.net"});
        //params.add(new Object[] { "EDGE", "https://www.phptravels.net"});
        return params.iterator();
    }


    @DataProvider(name = "setEnvironmentWithProperties")
    public static Iterator<Object[]> setEnvironmentWithProperties(){
        List<Object[]> params = new ArrayList<>();

        return params.iterator();
    }

    @DataProvider(name = "setEnvironmentWithEnum")
    public static Iterator<Object[]> setEnvironmentWithEnum(){
        List<Object[]> params = new ArrayList<>();

        return params.iterator();
    }


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
        return ((RemoteWebDriver) DRIVER).getCapabilities().getBrowserName().toUpperCase();
    }

}
