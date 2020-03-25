package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;

import java.util.Objects;

public abstract class BasePageObject {

    protected final WebDriver _driver = TestConfiguration.getDriver();

    public BasePageObject() { PageFactory.initElements(_driver, this); }

    public BasePageObject(WebDriver driver){
        if(Objects.isNull(_driver))
            TestConfiguration.setDriver(driver);
        PageFactory.initElements(driver, this);
    }

}
