package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

protected final WebDriver _driver = TestConfiguration.getDriver();

    public BasePage() {
        PageFactory.initElements(_driver, this);
    }

}
