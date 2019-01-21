package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePageObject {

    protected final WebDriver _driver = TestConfiguration.getDriver();

    public BasePageObject() { PageFactory.initElements(_driver, this); }

}
