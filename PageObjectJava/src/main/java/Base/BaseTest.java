package Base;

import Support.Support;
import org.testng.*;
import org.testng.annotations.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Listeners({DefaultListener.class})
public abstract class BaseTest{

    @BeforeClass
    public void classStartup(){
       //BrowserConfiguration _conf = new BrowserConfiguration("EDGE", "https://www.phptravels.net/");
       //TestConfiguration.setDriver(_conf.getDriver());
       //TestConfiguration.setUrl("https://www.phptravels.net/");

       System.out.println(String.format("--->EXECUTING TEST CLASS '%s' ON %s<---",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName().toUpperCase()));
    }

    @AfterClass
    public void classTearDown(){
        TestConfiguration.getDriver().quit();
        System.out.println(String.format("--->FINISHED EXECUTING TEST CLASS '%s' ON %s<---",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName().toUpperCase()));
    }

    @DataProvider(name = "setEnvironmentDefault")
    public Object[][] setEnvironment(){
        Object[][] params = new Object[][]{
            {"CHROME", "https://www.phptravels.net"},
            {"IE11"  , "https://www.phptravels.net"},
            {"EDGE"  , "https://www.phptravels.net"}
        };
        return params;
    }

}
