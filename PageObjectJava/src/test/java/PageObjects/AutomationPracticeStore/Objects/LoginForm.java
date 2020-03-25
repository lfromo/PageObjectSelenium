package PageObjects.AutomationPracticeStore.Objects;

import Base.TestConfiguration;
import PageObjects.AutomationPracticeStore.AccountPage;
import PageObjects.AutomationPracticeStore.ErrorMessageException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.ArrayList;
import java.util.List;

public class LoginForm  {

    @FindBy(how = How.ID_OR_NAME, using = "email")
    private WebElement inputEmailAddress;

    @FindBy(how = How.ID_OR_NAME, using = "passwd")
    private WebElement inputPassword;

    @FindBy(how = How.CSS, using = "p.lost_password a")
    private WebElement linkForgotYourPassword;

    @FindBy(how = How.ID_OR_NAME, using = "SubmitLogin")
    private WebElement buttonSignIn;

    public LoginForm setExistentEmailAddress(String emailAddress){
        this.inputEmailAddress.clear();
        this.inputEmailAddress.sendKeys(emailAddress);
        return this;
    }

    public String getExistentEmailAddress(){
        return this.inputEmailAddress.getAttribute("value");
    }

    public LoginForm setPassword(String password){
        this.inputPassword.clear();
        this.inputPassword.sendKeys(password);
        return this;
    }

    public String getPassword(){
        return this.inputPassword.getAttribute("value");
    }

    public AccountPage pressSignInButton(){
        this.buttonSignIn.click();
        List<WebElement> errorMessageBox = TestConfiguration.getDriver().findElements(By.cssSelector("div.alert"));
        if(!errorMessageBox.isEmpty()){
            List<String> errorList = new ArrayList<>();

            List<WebElement> errorItems = errorMessageBox.get(0).findElements(By.tagName("li"));
            errorItems.add(errorMessageBox.get(0).findElement(By.tagName("p")));
            errorItems.forEach(errorWe ->{
                errorList.add(errorWe.getText());
            });
            throw new ErrorMessageException(errorList);
        }

        return new AccountPage();
    }
}
