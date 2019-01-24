package Base;

import Support.Support;
import org.openqa.selenium.WebDriver;
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

    protected WebDriver _webdriver;

    @BeforeClass
    public void classSetup(){
       Support.LOGGER.info(String.format("--->EXECUTING TEST CLASS '%s'<---",
                this.getClass().getCanonicalName()));
        _webdriver = TestConfiguration.getDriver();
    }

    @AfterClass
    public void classTearDown(){
        Support.LOGGER.info(String.format("--->FINISHED EXECUTING TEST CLASS '%s'<---",
                this.getClass().getCanonicalName()));
    }

}
