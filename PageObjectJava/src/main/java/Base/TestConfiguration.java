package Base;

import Support.PropertiesHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.util.*;

public class TestConfiguration {

    private static WebDriver DRIVER;
    private static String URL;

    @DataProvider(name = "setEnvironmentWithProperties")
    public static Iterator<Object[]> setEnvironmentWithProperties(){
        List<Object[]> params = new ArrayList<>();
        PropertiesHandler handler = new PropertiesHandler();
        Properties props = handler.getProperties("resources\\general.properties");
        String[] browsers = props.getProperty("Browsers").split(",");
        String url = props.getProperty("AppURL");
        Arrays.stream(browsers).forEach(browser -> params.add(new Object[] {browser, url}));
        return params.iterator();
    }

    @DataProvider(name = "setEnvironmentWithEnum")
    public static Iterator<Object[]> setEnvironmentWithEnum(){
        List<Object[]> params = new ArrayList<>();
        //TODO Set an enum min the project to use but also we can demonstrate the following...
        params.add(new Object[] {new BrowserConfiguration("CHROME", "https://www.phptravels.net").getDriver()});
        params.add(new Object[] {new BrowserConfiguration("IE11", "https://www.phptravels.net").getDriver()});
        params.add(new Object[] {new BrowserConfiguration("EDGE", "https://www.phptravels.net").getDriver()});
        return params.iterator();
    }

    @DataProvider(name = "setEnvironmentWithXMLTestContext")
    public static Iterator<Object[]> setEnvironmentWithTestNG(ITestContext context){
        List<Object[]> params = new ArrayList<>();
        Map<String, String> suiteLevelParams = context.getSuite().getXmlSuite().getParameters();
        suiteLevelParams.entrySet().forEach(entry ->
                params.add(new String[] { entry.getKey(), entry.getValue() }));
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
        return ((RemoteWebDriver) DRIVER).getCapabilities().getBrowserName();
    }

}
