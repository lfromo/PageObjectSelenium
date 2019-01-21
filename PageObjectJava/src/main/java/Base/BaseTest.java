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

    protected final WebDriver _webdriver = TestConfiguration.getDriver();

    @BeforeClass
    public void classSetup(){
       Support.LOGGER.info(String.format("--->EXECUTING TEST CLASS '%s'<---",
                this.getClass().getCanonicalName()));
    }

    @AfterClass
    public void classTearDown(){
        Support.LOGGER.info(String.format("--->FINISHED EXECUTING TEST CLASS '%s'<---",
                this.getClass().getCanonicalName()));
    }

}
