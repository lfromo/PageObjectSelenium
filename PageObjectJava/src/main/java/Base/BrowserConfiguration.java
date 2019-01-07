package Base;

import Support.Support;
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

import java.util.Objects;

public class BrowserConfiguration<T extends MutableCapabilities> {

    private T configOpts = null;
    private boolean isRemote = false;
    private String browser;
    private String url;


    private final String EDGE = "EDGE";
    private final String CHROME = "CHROME";
    private final String IE11 = "IE11";
    private final String FIREFOX = "FIREFOX";
    private final String REMOTE = "REMOTE";


    public BrowserConfiguration(String browserName, String baseUrl){
        this.browser = browserName;
        this.url = baseUrl;
    }

    public BrowserConfiguration(Browser browser, String baseUrl){
        this.browser = browser.name();
        this.url = baseUrl;
    }

    public BrowserConfiguration(T browserConfigurationOptions, String baseUrl){
        this(browserConfigurationOptions, baseUrl, false);
    }

    public BrowserConfiguration(T browserConfigurationOptions, String baseUrl, boolean isRemote){
        this.browser = getInstanceOf(browserConfigurationOptions);
        this.configOpts = browserConfigurationOptions;
        this.url = baseUrl;
        this.isRemote = isRemote;
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

        if(isRemote && !areThereConfigOptions)
            throw new WebDriverException("configuration options are required for a remote web driver");

        try{
            switch (this.browser){
                case REMOTE:
                    drv = new RemoteWebDriver(configOptions);
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
                    throw new WebDriverException(String.format("The driver for %s is not supported", this.browser));
            }

            //TODO Consider removing the next two code lines and just return the driver.
            // However, a proper environment is not defined just by the browser but also by the initial URL
            // but this may cause problems with remote web drivers for mobile applications (Appium).

            drv.manage().window().maximize();
            drv.navigate().to(this.url);
            return drv;

        }catch (Exception ex){
            Support.LOGGER.fatal(String.format("An error occurred configuring '%s' web driver", this.browser), ex.getMessage());
            throw new WebDriverException(ex.getMessage());
        }

    }

    private <T extends MutableCapabilities> String getInstanceOf(T configOptions) {
        if(isRemote)
            return REMOTE;
        if(configOptions instanceof ChromeOptions)
            return CHROME;
        if(configOptions instanceof InternetExplorerOptions)
            return IE11;
        if(configOptions instanceof EdgeOptions)
            return EDGE;
        if(configOptions instanceof FirefoxOptions)
            return FIREFOX;
        throw new WebDriverException("The provided config options are from a non-supported browser");
    }

}
