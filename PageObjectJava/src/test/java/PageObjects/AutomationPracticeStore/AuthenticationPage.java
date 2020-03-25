package PageObjects.AutomationPracticeStore;

import Base.TestConfiguration;
import PageObjects.AutomationPracticeStore.Objects.LoginForm;
import PageObjects.AutomationPracticeStore.Objects.NewAccountEmail;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

public class AuthenticationPage extends StorePage {

    private final By formErrors = By.cssSelector("div.form-error");

    public LoginForm alreadyRegistered() {
        return PageFactory.initElements(this._driver, LoginForm.class);
    }

    public NewAccountEmail createAnAccount() {
        return PageFactory.initElements(this._driver, NewAccountEmail.class);
    }

    public boolean areThereFormErrors(){
        return TestConfiguration.getDriver().findElements(formErrors).size() > 0;
    }

}
