package Base;

import Support.Support;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.util.Strings;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BrowserConfiguration<T extends MutableCapabilities> {

    private T configOpts = null;
    private String browser;
    private URL url;
    private URL hubAddress;

    private final String EDGE = "MICROSOFTEDGE";
    private final String CHROME = "CHROME";
    private final String IE11 = "INTERNET EXPLORER";
    private final String FIREFOX = "FIREFOX";
    private final String REMOTE = "REMOTE";


    public BrowserConfiguration(String browserName, String baseUrl) throws MalformedURLException{
        this.browser = browserName.toUpperCase();
        this.url = new URL(baseUrl);
    }

    public BrowserConfiguration(String hubUrl, String baseUrl, Map<String, ?> capabilities) throws MalformedURLException{
        this.hubAddress = new URL(hubUrl);
        this.url = new URL(baseUrl);
        configOpts = (T) new MutableCapabilities(capabilities);
        this.browser = getInstanceOf(configOpts);
    }

    public BrowserConfiguration(T browserConfigurationOptions, String hubUrl, String baseUrl) throws MalformedURLException {
        this.browser = Strings.isNullOrEmpty(browserConfigurationOptions.getBrowserName()) ? getInstanceOf(browserConfigurationOptions)
                : browserConfigurationOptions.getBrowserName().toUpperCase();
        this.configOpts = browserConfigurationOptions;
        this.url = new URL(baseUrl);
        this.hubAddress = new URL(hubUrl);
    }




    /**
     * This method will return the corresponding {@link WebDriver} for the given browser reference during construction
     * using the default configuration.
     *
     * IMPORTANT: If this method is used, make sure the necessary system configurations are done beforehand.
     * @return a configured {@link WebDriver} instance, or {@code null} if there was a problem creating the instance.
     */
    public WebDriver getDriver(){
      return configureDriver(configOpts);
    }


    private <T extends MutableCapabilities> WebDriver configureDriver(T configOptions){
        WebDriver drv;
        boolean areThereConfigOptions = Objects.nonNull(configOptions);

        if(Objects.nonNull(hubAddress) && !areThereConfigOptions)
            throw new WebDriverException("Configuration options are required for a remote web driver");

        try{
            switch (this.browser){
                case REMOTE:
                    drv = new RemoteWebDriver(hubAddress, configOptions);
                    break;
                case EDGE:
                    drv = areThereConfigOptions ? new EdgeDriver((EdgeOptions) configOptions) : new EdgeDriver();
                    break;
                case IE11:
                    drv = areThereConfigOptions ? new InternetExplorerDriver((InternetExplorerOptions) configOptions)
                            : new InternetExplorerDriver();
                    break;
                case CHROME:
                    drv = areThereConfigOptions ? new ChromeDriver((ChromeOptions) configOptions): new ChromeDriver();
                    break;
                case FIREFOX:
                    drv = areThereConfigOptions ? new FirefoxDriver((FirefoxOptions) configOptions) : new FirefoxDriver();
                    break;
                default:
                    String msg = "\nSupported drivers are EDGE, IE11, CHROME and FIREFOX";
                    throw new WebDriverException(String.format("WebDriver for %s is not supported.%s", this.browser, msg));
            }

            // TODO Consider removing the next two code lines and just return the driver.
            // However, a proper environment is not defined just by the browser but also by the initial URL
            // but this may cause problems with remote web drivers testing mobile applications (i.e. Appium).

            //drv.manage().window().maximize();
            drv.navigate().to(this.url);
            return drv;

        }catch (Exception ex){
            Support.LOGGER.fatal(String.format("An error occurred configuring '%s' web driver", this.browser), ex.getMessage());
            throw new WebDriverException(ex.getMessage());
        }

    }

    private <T extends MutableCapabilities> String getInstanceOf(T configOptions) {
        if(Objects.nonNull(hubAddress))
            return REMOTE;
        if(configOptions instanceof ChromeOptions || configOptions.getBrowserName().equalsIgnoreCase(CHROME))
            return CHROME;
        if(configOptions instanceof InternetExplorerOptions || configOptions.getBrowserName().equalsIgnoreCase(IE11))
            return IE11;
        if(configOptions instanceof EdgeOptions || configOptions.getBrowserName().equalsIgnoreCase(EDGE))
            return EDGE;
        if(configOptions instanceof FirefoxOptions || configOptions.getBrowserName().equalsIgnoreCase(FIREFOX))
            return FIREFOX;
        throw new WebDriverException("The provided config options are not supported");
    }

}
