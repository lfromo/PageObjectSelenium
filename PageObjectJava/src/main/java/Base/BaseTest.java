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

    @BeforeClass
    public void classSetup(){
       System.out.println(String.format("--->EXECUTING TEST CLASS '%s' ON %s<---",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName().toUpperCase()));
    }

    @AfterClass
    public void classTearDown(){
        System.out.println(String.format("--->FINISHED EXECUTING TEST CLASS '%s' ON %s<---",
                this.getClass().getCanonicalName(), TestConfiguration.getBrowserName().toUpperCase()));
    }

}
